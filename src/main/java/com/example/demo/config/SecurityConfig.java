package com.example.demo.config;

import com.example.demo.unit.JwtAuthenticationFilter;
import com.example.demo.unit.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers((header) -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

                .sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests((registry) ->
                        registry.requestMatchers(
                                        new AntPathRequestMatcher("/login"),
                                        new AntPathRequestMatcher("/register"),
                                        new AntPathRequestMatcher("/v1/email/certification/check"),
                                        new AntPathRequestMatcher("/sendNewPassword"),
                                        new AntPathRequestMatcher("/swagger-ui/**"),
                                        new AntPathRequestMatcher("/swagger-resources/**"),
                                        new AntPathRequestMatcher("/v2/api-docs"),
                                        new AntPathRequestMatcher("/v3/api-docs/**")
                                )
                                .permitAll()
                                .anyRequest().authenticated()

                );



        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}