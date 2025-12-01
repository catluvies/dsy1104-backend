package com.pasteleriamilsabores.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/uploads/**").permitAll()

                        // Categorías y Productos: GET público
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/categorias/**",
                                "/api/v1/productos/**")
                        .permitAll()

                        // Categorías y Productos: Modificaciones solo ADMIN
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/categorias/**",
                                "/api/v1/productos/**")
                        .hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/v1/categorias/**",
                                "/api/v1/productos/**")
                        .hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/api/v1/categorias/**",
                                "/api/v1/productos/**")
                        .hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/v1/categorias/**",
                                "/api/v1/productos/**")
                        .hasRole("ADMIN")

                        // Boletas:
                        // - GET /api/v1/boletas (Todas): Solo Admin y Vendedor
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/boletas")
                        .hasAnyRole("ADMIN", "VENDEDOR")
                        // - GET /api/v1/boletas/** (Por ID o Usuario): Cliente también puede (para ver
                        // las suyas)
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/boletas/**")
                        .hasAnyRole("ADMIN", "VENDEDOR", "CLIENTE")

                        // - POST: Cliente (comprar) y Admin
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/boletas/**")
                        .hasAnyRole("ADMIN", "CLIENTE")
                        // - PATCH: Solo Admin (cambiar estados)
                        .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/api/v1/boletas/**")
                        .hasRole("ADMIN")
                        // - DELETE: Solo Admin
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/v1/boletas/**")
                        .hasRole("ADMIN")

                        // Crear Vendedores: Solo Admin
                        .requestMatchers("/api/v1/auth/register/vendedor").hasRole("ADMIN")

                        // Usuarios: Modificaciones solo ADMIN
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/v1/usuarios/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
