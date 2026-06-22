package com.example.recycling_campaign_system.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/user").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/login").permitAll()

                        .requestMatchers(HttpMethod.GET, "/campaigns/**", "/reports/**", "/api/zones/**", "/api/waste/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/campaigns/**", "/api/zones/**", "/api/waste/**").hasRole("GESTOR")
                        .requestMatchers(HttpMethod.PUT, "/campaigns/**", "/api/zones/**", "/api/waste/**").hasRole("GESTOR")
                        .requestMatchers(HttpMethod.DELETE, "/campaigns/**", "/api/zones/**", "/api/waste/**").hasRole("GESTOR")

                        .requestMatchers(HttpMethod.POST, "/reports/**").hasAnyRole("GESTOR", "VOLUNTARIO")
                        .requestMatchers(HttpMethod.PUT, "/reports/**").hasAnyRole("GESTOR", "VOLUNTARIO")
                        .requestMatchers(HttpMethod.DELETE, "/reports/**").hasAnyRole("GESTOR", "VOLUNTARIO")

                        .requestMatchers("/enrollments/**").hasAnyRole("GESTOR", "VOLUNTARIO")

                        .requestMatchers(HttpMethod.GET, "/api/user").hasRole("GESTOR")
                        .requestMatchers(HttpMethod.GET, "/api/user/**").hasAnyRole("GESTOR", "VOLUNTARIO")
                        .requestMatchers(HttpMethod.PUT, "/api/user/**").hasAnyRole("GESTOR", "VOLUNTARIO")
                        .requestMatchers(HttpMethod.DELETE, "/api/user/**").hasRole("GESTOR")

                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}
