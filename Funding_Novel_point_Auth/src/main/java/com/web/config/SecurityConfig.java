package com.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.web.service.CustomOAuth2UserService;

@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/signup", "/css/**", "/js/**").permitAll() //누구나 접근 가능
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")    // ROlE_ADMIN 이라는 권한있어야 접근 가능
                .requestMatchers("/novels/*/like/*").authenticated()  
                .requestMatchers("/payments/**").authenticated() // 결제는 인증된 사용자만// 인증된 사용자만 접근 가능
//                .requestMatchers("/points/**").permitAll() // 포인트 관련 요청 누구나 접근 가능
//                .requestMatchers("/funding/**").permitAll() // /funding/** 경로 접근 허용 추가
                .requestMatchers("/points/**").authenticated()
                .anyRequest().authenticated()								// 그외도 인증된 사용자만 접근 가능
            )
            .formLogin(form -> form
                .loginPage("/login")											//login url 사용
                .loginProcessingUrl("/perform_login")							// perform_login으로 post 요청
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/dashboard", true)								// 성공시 dashboard로 이동
                .failureUrl("/login?error=true")									// 실패시 
                .permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
            )
            .logout(logout -> logout
                .logoutUrl("/perform_logout")
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    "/novels/*/like/*", "/perform_login", "/oauth2/**","/payments/**","/points/**", "/funding/**"
                )
            );																		// CSRF 보호를 비활성화

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);							//비밀번호를 암호화하기 위해 BCryptPasswordEncoder를 사용하며, 강도는 10으로 설정
    }
}