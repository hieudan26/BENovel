package com.socialmedia.loginandregistration.security.config;

import com.socialmedia.loginandregistration.security.Service.AppUserDetailService;
import com.socialmedia.loginandregistration.security.filter.CustomAuthenticationFilter;
import com.socialmedia.loginandregistration.security.filter.CustomAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//import javax.sql.DataSource;

import static com.socialmedia.loginandregistration.common.UserPermission.*;


@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final AppUserDetailService appUserDetailService;
    private final CustomAuthorizationFilter customAuthorizationFilter ;
    private final CustomAuthenticationFilter customAuthenticationFilter ;

    @Autowired
    public AppSecurityConfig(PasswordEncoder passwordEncoder,
                             AppUserDetailService appUserDetailService,
                             CustomAuthorizationFilter customAuthorizationFilter,
                             CustomAuthenticationFilter customAuthenticationFilter
    ) {
        this.passwordEncoder = passwordEncoder;
        this.appUserDetailService = appUserDetailService;
        this.customAuthorizationFilter = customAuthorizationFilter;
        this.customAuthenticationFilter = customAuthenticationFilter;
    }

//    @Autowired
//    private DataSource dataSource;
//

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoauthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
//        http.addFilter(customAuthenticationFilter);
//        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.csrf().disable();
//        http.cors();
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers("/api/auth/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/admin/**").hasAnyAuthority(ADMIN_READ.getPermission());
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/admin/**").hasAnyAuthority(ADMIN_WRITE.name());
        http.authorizeRequests().anyRequest().permitAll();

        http.cors()
                .and().csrf().disable().authorizeRequests()
                .and()
                .addFilterBefore(customAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(customAuthorizationFilter,UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService(){
        return new AppUserDetailService();
    }

    @Bean
    public DaoAuthenticationProvider daoauthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder); //add if DB save plaintext password
        provider.setUserDetailsService(userDetailsService());
        return provider;
    }


//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }


    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        //CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();
        customAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");
        //customAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return customAuthenticationFilter;
    }

}
