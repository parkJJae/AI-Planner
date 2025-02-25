package com.example.aiplanner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/users/register")).permitAll() // 회원가입 누구나 허용
                        .requestMatchers(new AntPathRequestMatcher("/users/**")).permitAll() // 로그인, 회원가입 관련 경로 인증 없이 가능
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll() // H2 콘솔 허용
                        .requestMatchers(new AntPathRequestMatcher("/api/planner/**")).authenticated() // 플래너 관련 API는 로그인 필요
                        .anyRequest().authenticated() // 나머지는 로그인 필요
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/users/register")) // 회원가입 CSRF 예외
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/users/login")) // 로그인 CSRF 예외
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")) // H2 콘솔 CSRF 예외
                        .disable() // API 방식에서는 CSRF 비활성화

                )
                .headers(headers -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                )
                ;

        return http.build();
    }
    // http 부분 -> 로그인 하지 않더라도 페이지에 접근 할 수 있도록.
// .csrf 부분 -> 스프링 시큐리티가 CSRF 처리시 H2 콘솔은 예외로 처리할 수 있도록
// .headers 부분 -> H2 콘솔은 프레임으로 이루어져있어 프레임 구조의 웹사이트는 이 헤더의 값이 DENY인 경우 화면 깨져 보임

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

