package com.example.DesafioSprint1.config;

import com.example.DesafioSprint1.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf-> csrf.disable()).authorizeHttpRequests(authRequest ->
                        authRequest.requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/v1/helloWorld").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/helloUser").hasAuthority("USER" )
                        .requestMatchers(HttpMethod.GET, "/api/v1/helloUser").hasAuthority("ADMIN" )
                                .requestMatchers(HttpMethod.GET, "/api/v1/helloAdmin").hasAuthority("ADMIN")
                                .anyRequest().authenticated())
                .sessionManagement( sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    
}
