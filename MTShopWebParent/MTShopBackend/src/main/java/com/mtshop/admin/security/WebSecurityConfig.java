package com.mtshop.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new MTShopUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/users/**").hasAuthority("Admin")
//                .antMatchers("/categories/**").hasAnyAuthority("Admin", "Editor")
//                .antMatchers("/brands/**").hasAnyAuthority("Admin", "Editor")
//                .antMatchers("/articles/**").hasAnyAuthority("Admin", "Editor")
//                .antMatchers("/menus/**").hasAnyAuthority("Admin", "Editor")
//                .antMatchers("/products/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
//                .antMatchers("/questions/**").hasAnyAuthority("Admin", "Assistant")
//                .antMatchers("/reviews/**").hasAnyAuthority("Admin", "Assistant")
//                .antMatchers("/customers/**").hasAnyAuthority("Admin", "Salesperson")
//                .antMatchers("/shippings/**").hasAnyAuthority("Admin", "Salesperson")
//                .antMatchers("/reports/**").hasAnyAuthority("Admin", "Salesperson")
//                .antMatchers("/orders/**").hasAnyAuthority("Admin", "Salesperson", "Shipper")
                .antMatchers("/setting/**").hasAuthority("Admin")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").usernameParameter("email").permitAll()
                .and()
                .logout().permitAll()
                .and().rememberMe().key("AbcdefGhijklMnopqrs_1234567890").tokenValiditySeconds(7 * 24 * 60 * 60);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }
}
