package com.example.bookswapplatform.security;

import com.example.bookswapplatform.exception.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector).servletPath("/spring-mvc");
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                antMatcher("/api/v1/guest/**"),
                                antMatcher("/api/v1/post/filter"),
                                antMatcher("/api/v1/post/id"),
                                antMatcher("/api/v1/vn-pay/**"),
                                antMatcher("/swagger-ui/**"),
                                antMatcher("/swagger-ui.html"),
                                antMatcher("/v3/api-docs/**"),
                                antMatcher("/configuration/ui"),
                                antMatcher("/configuration/security")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/api/v1/user/**")).hasAnyAuthority("ROLE_USER")
                        .requestMatchers(mvcMatcherBuilder.pattern("/api/v1/book/**")).hasAnyAuthority("ROLE_USER")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler))
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
        return http.build();

    }
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt ->
                Optional.ofNullable(jwt.getClaimAsStringList("role"))
                        .stream()
                        .flatMap(Collection::stream)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );

        return converter;
    }
}
