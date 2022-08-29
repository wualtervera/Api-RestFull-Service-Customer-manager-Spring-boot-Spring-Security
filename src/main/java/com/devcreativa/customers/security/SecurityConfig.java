package com.devcreativa.customers.security;

import com.devcreativa.customers.exceptions.ExceptionResponse;
import com.devcreativa.customers.utils.Constants;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.devcreativa.customers.security.Roles.*;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private Gson gson;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authentication = new DaoAuthenticationProvider();
        authentication.setUserDetailsService(userDetailsService);
        authentication.setPasswordEncoder(passwordEncoder());
        return authentication;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        final String USERS = "/users/**";
        final String CUSTOMERS = "/api/v1/customer/**";

        http.csrf().disable();

        //login
        http.formLogin()
            .loginProcessingUrl("/login")
            .usernameParameter(Constants.USER_USERNAME)
            .passwordParameter(Constants.USER_PASSWORD)
            .successHandler(authenticationSuccessHandler())
            .failureHandler(authenticationFailureHandler())
            .permitAll();
        http.exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint());

        //logout
        http.logout()
            .logoutUrl("/logout")
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true)
            .logoutSuccessHandler(logoutSuccessHandler())
            .permitAll();


        //Add authority to resources

        //super users
        http.authorizeRequests()
            .antMatchers(HttpMethod.POST, "/admin").permitAll() //added init user
            .antMatchers(HttpMethod.PUT, "/moderator/**").hasAnyAuthority(ADMIN) // added moderathor
            .antMatchers(HttpMethod.PUT, "/default/**").hasAnyAuthority(MODERATOR)// added default role user
            .and()
            .httpBasic();
        //user
        http.authorizeRequests()
            .antMatchers(HttpMethod.GET, USERS).hasAnyAuthority(ADMIN, MODERATOR, USER)
            .antMatchers(HttpMethod.POST, USERS).hasAnyAuthority(ADMIN, MODERATOR)
            .antMatchers(HttpMethod.PUT, USERS).hasAnyAuthority(ADMIN, MODERATOR)
            .antMatchers(HttpMethod.PATCH, USERS).hasAnyAuthority(ADMIN)
            .antMatchers(HttpMethod.DELETE, USERS).hasAnyAuthority(ADMIN)
            .and()
            .httpBasic();
        //customer
        http.authorizeRequests()
            .antMatchers(HttpMethod.GET, CUSTOMERS).hasAnyAuthority(ADMIN, MODERATOR, USER)
            .antMatchers(HttpMethod.POST, CUSTOMERS).hasAnyAuthority(ADMIN)
            .antMatchers(HttpMethod.PUT, CUSTOMERS).hasAnyAuthority(ADMIN, MODERATOR)
            .antMatchers(HttpMethod.PATCH, CUSTOMERS).hasAnyAuthority(ADMIN, MODERATOR)
            .antMatchers(HttpMethod.DELETE, CUSTOMERS).hasAnyAuthority(ADMIN)
            .and()
            .httpBasic();


        //add other resource


        //access to resources
        http.authorizeRequests()
            //assign access to roles
            .antMatchers("/moderator/**").authenticated()
            .antMatchers("/default/**").authenticated()

            //assign access to resource
            .antMatchers(USERS).authenticated()
            .antMatchers(CUSTOMERS).authenticated()
            //...

            .and()
            .httpBasic();


        //single swagger
        http.authorizeRequests()
            .antMatchers("/swagger-ui/**").permitAll()
            .antMatchers("/v3/api-docs/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .httpBasic();

    }


    //handlers
    private LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

                httpServletResponse(response, 200, "Closed authentication");
            }
        };
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response,
                                 AuthenticationException authException) throws IOException {

                httpServletResponse(response, 401, "Authentication is required to access this resource");
            }
        };
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException {

                httpServletResponse(response, 200, "Successful authentication");
            }
        };
    }

    private AuthenticationFailureHandler authenticationFailureHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                                AuthenticationException exception) throws IOException {

                httpServletResponse(response, 401, "Failed authentication");
            }
        };
    }

    private void httpServletResponse(HttpServletResponse response, int statusCode, String message) throws IOException {

        ExceptionResponse exceptionResponse = new ExceptionResponse(statusCode, message);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(statusCode);
        response.getWriter().printf(gson.toJson(exceptionResponse));
        response.getWriter().flush();
        response.getWriter().close();
    }


}
