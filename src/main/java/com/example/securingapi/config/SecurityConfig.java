package com.example.securingapi.config;

import com.example.securingapi.config.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    @Lazy // Breaks circular dependency
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/register", "/api/login").permitAll() // Public endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // Only ADMIN role can access /api/admin/**
                        .requestMatchers("/api/users").hasRole("ADMIN") // Only ADMIN role can access /api/users
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Register JWT filter
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER") // Normal user with USER role
                .build());
        manager.createUser(User.withUsername("admin")
                .password(passwordEncoder().encode("adminpassword"))
                .roles("ADMIN") // Admin user with ADMIN role
                .build());
        return manager;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
