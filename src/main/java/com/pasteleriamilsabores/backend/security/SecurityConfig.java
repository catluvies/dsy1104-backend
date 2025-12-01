package com.pasteleriamilsabores.backend.security;

import java.util.Arrays;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                // Endpoints de Auth específicos (Orden importa: específicos primero)
                                                .requestMatchers("/api/v1/auth/register/vendedor").hasRole("ADMIN")
                                                .requestMatchers("/api/v1/auth/cambiar-password").authenticated()

                                                // Resto de Auth público
                                                .requestMatchers("/api/v1/auth/**").permitAll()
                                                .requestMatchers("/swagger-ui/**", "/api-docs/**", "/swagger-ui.html")
                                                .permitAll()
                                                .requestMatchers("/uploads/**").permitAll()

                                                // Categorías y Productos: GET público
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/api/v1/categorias/**",
                                                                "/api/v1/productos/**")
                                                .permitAll()

                                                // Categorías y Productos: Modificaciones solo ADMIN
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/v1/categorias/**",
                                                                "/api/v1/productos/**")
                                                .hasRole("ADMIN")
                                                .requestMatchers(org.springframework.http.HttpMethod.PUT,
                                                                "/api/v1/categorias/**",
                                                                "/api/v1/productos/**")
                                                .hasRole("ADMIN")
                                                .requestMatchers(org.springframework.http.HttpMethod.PATCH,
                                                                "/api/v1/categorias/**",
                                                                "/api/v1/productos/**")
                                                .hasRole("ADMIN")
                                                .requestMatchers(org.springframework.http.HttpMethod.DELETE,
                                                                "/api/v1/categorias/**",
                                                                "/api/v1/productos/**")
                                                .hasRole("ADMIN")

                                                // Boletas:
                                                // - GET /api/v1/boletas (Todas): Solo Admin y Vendedor
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/api/v1/boletas")
                                                .hasAnyRole("ADMIN", "VENDEDOR")
                                                // - GET /api/v1/boletas/** (Por ID o Usuario): Cliente también puede
                                                // (para ver
                                                // las suyas)
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/api/v1/boletas/**")
                                                .hasAnyRole("ADMIN", "VENDEDOR", "CLIENTE")

                                                // - POST: Cliente (comprar) y Admin
                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/api/v1/boletas/**")
                                                .hasAnyRole("ADMIN", "CLIENTE")
                                                // - PATCH: Solo Admin y Vendedor (cambiar estados)
                                                .requestMatchers(org.springframework.http.HttpMethod.PATCH,
                                                                "/api/v1/boletas/**")
                                                .hasAnyRole("ADMIN", "VENDEDOR")
                                                // - DELETE: Solo Admin
                                                .requestMatchers(org.springframework.http.HttpMethod.DELETE,
                                                                "/api/v1/boletas/**")
                                                .hasRole("ADMIN")

                                                // Usuarios: perfil propio (cualquier autenticado) - validación en
                                                // @PreAuthorize
                                                .requestMatchers(org.springframework.http.HttpMethod.PUT,
                                                                "/api/v1/usuarios/*/perfil")
                                                .authenticated()

                                                // Usuarios: modificaciones admin (PUT sin /perfil, DELETE)
                                                .requestMatchers(org.springframework.http.HttpMethod.PUT,
                                                                "/api/v1/usuarios/*")
                                                .hasRole("ADMIN")
                                                .requestMatchers(org.springframework.http.HttpMethod.DELETE,
                                                                "/api/v1/usuarios/**")
                                                .hasRole("ADMIN")

                                                .anyRequest().authenticated())
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList(
                                "http://localhost:5173",
                                "https://react-project-dsy1104-production.up.railway.app"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("*"));
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}
