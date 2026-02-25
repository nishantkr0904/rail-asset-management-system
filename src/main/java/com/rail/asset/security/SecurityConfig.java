package com.rail.asset.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public SecurityConfig(RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/assets/**").hasAnyRole(UserRole.ADMIN.name(), UserRole.MANAGER.name())
                .requestMatchers(HttpMethod.PUT, "/api/assets/**").hasAnyRole(UserRole.ADMIN.name(), UserRole.MANAGER.name())
                .requestMatchers(HttpMethod.DELETE, "/api/assets/**").hasRole(UserRole.ADMIN.name())
                .requestMatchers(HttpMethod.GET, "/api/assets/**").hasAnyRole(UserRole.ADMIN.name(), UserRole.MANAGER.name(), UserRole.VIEWER.name())
                .anyRequest().authenticated()
            )
            .httpBasic(customizer -> { })
            .exceptionHandling(ex -> ex.authenticationEntryPoint(restAuthenticationEntryPoint));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.withUsername("admin")
            .password(passwordEncoder.encode("adminPass!"))
            .roles(UserRole.ADMIN.name())
            .build();

        UserDetails manager = User.withUsername("manager")
            .password(passwordEncoder.encode("managerPass!"))
            .roles(UserRole.MANAGER.name())
            .build();

        UserDetails viewer = User.withUsername("viewer")
            .password(passwordEncoder.encode("viewerPass!"))
            .roles(UserRole.VIEWER.name())
            .build();

        return new InMemoryUserDetailsManager(admin, manager, viewer);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
