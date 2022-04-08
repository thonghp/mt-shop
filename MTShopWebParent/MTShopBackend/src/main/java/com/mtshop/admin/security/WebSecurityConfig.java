package com.mtshop.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * - @Configuration also a @Component but @Component cannot act like @Configuration
 * -- contains 1 or more @Bean
 * - @EnableWebSecurity used to enable Spring security
 * - WebSecurityConfigurerAdapter is a utility interface of Spring Security that helps us to install information more easily.
 * - @Bean commonly declared in class @Configuration, instantiate the instance then put the bean in the context
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll(); // allow public access to the admin app without authentication
    }
}
