package com.ceos20.instagram.global.config;

import com.ceos20.instagram.domain.user.domain.CustomUserDetailsService;
import com.ceos20.instagram.global.config.jwt.JwtAuthenticationFilter;
import com.ceos20.instagram.global.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManager authenticationManager = authenticationManager(httpSecurity);

        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/users/login").permitAll()
                .requestMatchers("/users/id/**").permitAll()
                .requestMatchers("/users/register").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // JwtAuthenticationFilter 등록
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        return JwtAuthenticationFilter.builder()
                .jwtTokenProvider(jwtTokenProvider)
                .authenticationManager(authenticationManager)
                .customUserDetailsService(customUserDetailsService)
                .build();
    }


    // AuthenticationManager 등록
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService);
        return authenticationManagerBuilder.build();
    }
}
