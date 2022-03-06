package mobile.controller;

import mobile.Service.EmailService;
import mobile.Service.UserService;
import mobile.mapping.UserMapping;
import mobile.model.Entity.User;
import mobile.model.payload.request.LoginRequest;
import mobile.model.payload.request.ReActiveRequest;
import mobile.model.payload.request.RefreshTokenRequest;
import mobile.model.payload.request.RegisterRequest;
import mobile.model.payload.response.BaseCustomResponse.RecordNotFoundException;
import mobile.model.payload.response.ErrorResponseMap;
import mobile.model.payload.response.BaseCustomResponse.HttpMessageNotReadableException;
import mobile.model.payload.response.BaseCustomResponse.MethodArgumentNotValidException;
import mobile.model.payload.response.SuccessResponse;
import mobile.security.DTO.AppUserDetail;
import mobile.security.JWT.JwtUtils;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthentiactionController {
    private static final Logger LOGGER = LogManager.getLogger(AuthentiactionController.class);

    private final UserService userService;
    private final EmailService emailService;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<SuccessResponse>  addUser(@RequestBody @Valid RegisterRequest user, BindingResult errors) throws Exception {

        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }
        if (user == null) {
            LOGGER.info("Inside addIssuer, adding: " + user.toString());
            throw new HttpMessageNotReadableException("Missing field");
        } else {
            LOGGER.info("Inside addIssuer...");
        }

        if(userService.existsByEmail(user.getEmail())){
            return SendErrorValid("email",user.getEmail());
        }

        if(userService.existsByUsername(user.getUsername())){
            return SendErrorValid("username",user.getUsername());
        }

        try{

            User newUser = UserMapping.registerToEntity(user);
            String roleName = "USER";
            userService.saveUser(newUser,roleName);
            String token = jwtUtils.generateEmailJwtToken(user.getUsername());

            emailService.sendActiveMessage(newUser);

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Register successful");
            response.setSuccess(true);

            response.getData().put("email",user.getEmail());
            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);

        }catch(Exception ex){
            throw new Exception("Can't create your account");
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<SuccessResponse>  Sigin(@RequestBody @Valid LoginRequest user, BindingResult errors) throws Exception {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AppUserDetail userDetails = (AppUserDetail) authentication.getPrincipal();


        String accessToken = jwtUtils.generateJwtToken(userDetails);
        String refreshToken = jwtUtils.generateRefreshJwtToken(userDetails);
        User loginUser = userService.findByUsername(user.getUsername());

        SuccessResponse response = new SuccessResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Login successful");
        response.setSuccess(true);

        response.getData().put("accessToken",accessToken);
        response.getData().put("refreshToken",refreshToken);
        response.getData().put("name",loginUser.getTenhienthi());
        response.getData().put("image",loginUser.getImage());

        return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
    }
    @PostMapping("/refreshtoken")
    public ResponseEntity<SuccessResponse> refreshToken(@RequestBody RefreshTokenRequest refreshToken, HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if(jwtUtils.validateExpiredToken(accessToken) == false){
                throw new BadCredentialsException("access token is not expired");
            }

            if(jwtUtils.validateExpiredToken(refreshToken.getRefreshToken()) == true){
                throw new BadCredentialsException("refresh token is expired");
            }

            if(refreshToken == null){
                throw new BadCredentialsException("refresh token is missing");
            }

            if(!jwtUtils.getUserNameFromJwtToken(refreshToken.getRefreshToken()).equals(jwtUtils.getUserNameFromJwtToken(refreshToken.getRefreshToken()))){
                throw new BadCredentialsException("two token are not a pair");
            }


            AppUserDetail userDetails =  AppUserDetail.build(userService.findByUsername(jwtUtils.getUserNameFromJwtToken(refreshToken.getRefreshToken())));

            accessToken = jwtUtils.generateJwtToken(userDetails);

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Login successful");
            response.setSuccess(true);

            response.getData().put("accessToken",accessToken);
            response.getData().put("refreshToken",refreshToken);

            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        }
        else
        {
            throw new BadCredentialsException("access token is missing");
        }
    }

    private ResponseEntity SendErrorValid(String field, String message){
        ErrorResponseMap errorResponseMap = new ErrorResponseMap();
        Map<String,String> temp =new HashMap<>();
        errorResponseMap.setMessage("Field already taken");
        temp.put(field,message+" has already used");
        errorResponseMap.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponseMap.setDetails(temp);
        return ResponseEntity
                .badRequest()
                .body(errorResponseMap);
    }

    @GetMapping("/active")
    public ResponseEntity<SuccessResponse> activeToken( @RequestParam(defaultValue = "") String key
    ) {
        if(key != null && key !=""){
            if(!jwtUtils.validateJwtToken(key)){
                if(!jwtUtils.validateExpiredToken(key)){
                    throw new BadCredentialsException("key active is expired");
                }
                throw new BadCredentialsException("key active is not valid");
            }

            String username = jwtUtils.getUserNameFromJwtToken(key);
            User user = userService.findByUsername(username);

            if(user == null){
                throw new RecordNotFoundException("Not found, please register again");
            }

            if(user.getActive() == true){
                throw new RecordNotFoundException("user already has been actived!");
            }

            userService.updateActive(user);



            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Active successful");
            response.setSuccess(true);

            response.getData().put("email",user.getEmail());

            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        }
        else
        {
            throw new BadCredentialsException("key active mising");
        }
    }

    @PostMapping("/reactive")
    public ResponseEntity<SuccessResponse> reActiveToken(@RequestBody @Valid ReActiveRequest reActiveRequest  , BindingResult errors) throws Exception {

        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }
        if (reActiveRequest == null) {
            throw new HttpMessageNotReadableException("Missing field");
        }

        if(!userService.existsByEmail(reActiveRequest.getEmail())){
            throw new HttpMessageNotReadableException("Email is not Registed");
        }

        User user = userService.getUser(reActiveRequest.getEmail());

        if(user.getActive() == true){
            throw new HttpMessageNotReadableException("user already has been actived!");
        }


        try{

            emailService.sendActiveMessage(user);


            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Resend email successful");
            response.setSuccess(true);

            response.getData().put("email",user.getEmail());

            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        }catch (Exception ex){
            throw  new Exception("Some error when send active email");
        }
    }

    @PostMapping("/forgetpassword")
    public ResponseEntity<SuccessResponse> forgetPassword(@RequestBody @Valid ReActiveRequest reActiveRequest  , BindingResult errors) throws Exception {

        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }
        if (reActiveRequest == null) {
            throw new HttpMessageNotReadableException("Missing field");
        }

        if(!userService.existsByEmail(reActiveRequest.getEmail())){
            throw new HttpMessageNotReadableException("Email is not Registed");
        }

        User user = userService.getUser(reActiveRequest.getEmail());

        try{

            RandomString gen = new RandomString(8, ThreadLocalRandom.current());
            String newpass = gen.nextString();

            user = userService.updateUserPassword(user,newpass);
            emailService.sendForgetPasswordMessage(user,newpass);


            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Send email with new password successful");
            response.setSuccess(true);
            response.getData().put("email",user.getEmail());

            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        }catch (Exception ex){
            throw  new Exception("Some error when send active email");
        }
    }
}
