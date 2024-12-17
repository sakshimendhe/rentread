package com.example.rentread.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity (not recommended for production)
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/register", "/api/login").permitAll() // Public endpoints
                .requestMatchers("/api/books").hasAnyRole("USER", "ADMIN")  // Private endpoint for USER and ADMIN
                .requestMatchers("/api/books/**").hasRole("ADMIN")          // Admin-only endpoint
                .anyRequest().authenticated()  // All other requests require authentication
            )
            .httpBasic() // Enable Basic Authentication
            .and()
            .formLogin().disable(); // Disable form-based login

        return http.build();
    }
}
