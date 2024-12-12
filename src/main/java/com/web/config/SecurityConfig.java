package com.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 권한 설정
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/signup", "/check-username", "/css/**", "/js/**").permitAll() // 비인증 경로
                .requestMatchers("/qna", "/qna/**").authenticated() // QnA는 인증된 사용자만
                .requestMatchers("/payments/**").authenticated() // 결제는 인증된 사용자만
                .anyRequest().authenticated() // 나머지는 인증 필요
            )
            // 로그인 설정
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
            )
            // 로그아웃 설정
            .logout(logout -> logout
                .logoutUrl("/perform_logout")
                .logoutSuccessUrl("/")
                .permitAll()
            )
            // CSRF 설정
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/payments/**", "/qna/**") // 결제와 QnA 관련 요청은 CSRF 보호 비활성화
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    

}
