package com.ev.ready.config;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, ex) -> writeErrorResponse(
                                response,
                                HttpStatus.UNAUTHORIZED,
                                "Authentication required.",
                                request.getRequestURI()
                        ))
                        .accessDeniedHandler((request, response, ex) -> writeErrorResponse(
                                response,
                                HttpStatus.FORBIDDEN,
                                "Access denied.",
                                request.getRequestURI()
                        ))
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/brands",
                                "/api/v1/charger-types",
                                "/api/v1/vehicles",
                                "/api/v1/vehicles/*",
                                "/api/v1/chargers",
                                "/api/v1/chargers/*",
                                "/api/v1/chargers/cities"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/leads",
                                "/api/v1/contact-submissions",
                                "/api/v1/admin/auth/login"
                        ).permitAll()
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService(
            @Value("${evready.admin.username:}") String adminUsername,
            @Value("${evready.admin.password:}") String adminPassword,
            PasswordEncoder passwordEncoder
    ) {
        if (!hasText(adminUsername) || !hasText(adminPassword)) {
            String disabledPassword = UUID.randomUUID().toString();
            return new InMemoryUserDetailsManager(User.withUsername("__admin_disabled__")
                    .password(passwordEncoder.encode(disabledPassword))
                    .roles("ADMIN")
                    .disabled(true)
                    .build());
        }

        return new InMemoryUserDetailsManager(User.withUsername(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .roles("ADMIN")
                .build());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private void writeErrorResponse(
            HttpServletResponse response,
            HttpStatus status,
            String message,
            String path
    ) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write("""
                {"timestamp":"%s","status":%d,"error":"%s","message":"%s","path":"%s"}\
                """.formatted(
                escapeJson(OffsetDateTime.now().toString()),
                status.value(),
                escapeJson(status.getReasonPhrase()),
                escapeJson(message),
                escapeJson(path)
        ));
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
