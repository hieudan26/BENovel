package com.socialmedia.loginandregistration.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Jwts;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;

@Slf4j
@Component
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Value("${apps.security.secret}")
    private String secret;

    @Value("${apps.security.header-string}")
    private String headerString;

    @Value("${apps.security.token-prefix}")
    private String tokenPrefix;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/api/auth/login") || request.getServletPath().equals("/api/auth/refreshtoken") || request.getServletPath().equals("/api/auth/register")){
            filterChain.doFilter(request,response);
        }else {
            String authorizationHeader = request.getHeader(headerString);
            if(authorizationHeader != null && authorizationHeader.startsWith(tokenPrefix)){
                try {
                    String user = null;
                    try {
                        // check the token is valid
                        user = Jwts.parser()
                                .setSigningKey(secret.getBytes())
                                .parseClaimsJws(authorizationHeader.replace(tokenPrefix, ""))
                                .getBody()
                                .getSubject();
                    } catch (io.jsonwebtoken.SignatureException ex) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is not valid.");
                    } catch (io.jsonwebtoken.ExpiredJwtException ex) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is expired.");
                    }

                    String token = authorizationHeader.substring(tokenPrefix.length());
                    Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();

                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("rolePermissions").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

                    stream(roles).forEach(role->{
                        authorities.add(new SimpleGrantedAuthority(role));
                    });

                    UsernamePasswordAuthenticationToken authenticationToken = new
                            UsernamePasswordAuthenticationToken(username,null,authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    filterChain.doFilter(request,response);
                }catch (Exception ex){

                    log.error("Error logging in: {}",ex.getMessage());
                    response.setHeader("error ",ex.getMessage());
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    Map<String,String> error = new HashMap<>();
                    error.put("error_message",ex.getMessage());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(),error);
                }
            }
            else
            {
                filterChain.doFilter(request,response);
            }
        }
    }
}
