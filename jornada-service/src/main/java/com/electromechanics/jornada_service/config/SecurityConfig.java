package com.electromechanics.jornada_service.config;

import com.electromechanics.jornada_service.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtService jwtService) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .headers(h -> h.frameOptions(f -> f.sameOrigin()))
            .exceptionHandling(e -> e.authenticationEntryPoint((request, response, ex) ->
                    response.setStatus(401)))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/h2-console/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/jornadas/iniciar").hasRole("TRABAJADOR")
                .requestMatchers(HttpMethod.PUT, "/api/v1/jornadas/finalizar").hasRole("TRABAJADOR")
                .requestMatchers(HttpMethod.GET, "/api/v1/jornadas/mias").hasRole("TRABAJADOR")
                .requestMatchers("/api/v1/jornadas/**").hasRole("SUPERVISOR")
                .anyRequest().authenticated())
            .addFilterBefore(new JwtAuthenticationFilter(jwtService),
                    UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}