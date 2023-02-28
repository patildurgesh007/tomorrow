package com.tomorrow.queueSystem.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    RequestMatcher requestMatcherUserManagement = new RequestMatcher() {
        @Override
        public boolean matches(HttpServletRequest request) {
            return request.getRequestURI().contains("/userManagement/**");
        }
    };

    RequestMatcher requestMatcherFirstUser = new RequestMatcher() {
        @Override
        public boolean matches(HttpServletRequest request) {
            return request.getRequestURI().contains("/**");
        }
    };

    @Bean
    public UserDetailsService getUserDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        RequestMatcher requestMatcher = new RequestMatcher() {
            @Override
            public boolean matches(HttpServletRequest request) {
                return request.getRequestURI().contains("/userManagement/**");
            }
        };
      return  http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(requestMatcherFirstUser)
                .permitAll()
                .and()
//                .authorizeHttpRequests()
//                .requestMatchers(requestMatcherUserManagement)
//                .authenticated()
//                .anyRequest()
//                .hasAnyRole("ADMIN")
//                .and()
                .httpBasic()
                .and()
                .build();
//                .formLogin()
//                .permitAll()
//                .and().build();
    }

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider getAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(getUserDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
        return daoAuthenticationProvider;
    }
}
