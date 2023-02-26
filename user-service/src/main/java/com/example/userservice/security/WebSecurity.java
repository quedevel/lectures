package com.example.userservice.security;

import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    private final BCryptPasswordEncoder passwordEncoder;

    private final Environment env;

    private static final String IP_ADDRESS = "192.168.125.82";
//    private static final String IP_ADDRESS = "10.98.196.225";
//    private static final String IP_ADDRESS = "172.30.1.42";
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

//        http.authorizeHttpRequests().antMatchers("/h2-console.**","/users/**").permitAll();
        http.authorizeRequests()
                .antMatchers("/error/**","/actuator/**").permitAll()
                .antMatchers("/**")
                .access("hasIpAddress('"+IP_ADDRESS+"')")
                .and()
                .addFilter(getAuthenticationFilter());

        http.headers().frameOptions().disable();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception{
        AuthenticationFilter filter = new AuthenticationFilter(authenticationManager(), userService, env);
        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }
}
