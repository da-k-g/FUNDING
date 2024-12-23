package com.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.web.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }
	

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	 
        http
            // 권한 설정
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/signup", "/css/**", "/js/**","/check-username","/novels/popular-list", "/novels/popular-more-list", "/novels/popular", "/images/**", "/uploads/**", "img/**").permitAll() // 비인증 경로
                .requestMatchers("/qna", "/qna/**").authenticated() // QnA는 인증된 사용자만
                .requestMatchers("/payments/**").authenticated() // 결제는 인증된 사용자만
                .requestMatchers("/points/**").authenticated() // 포인트 충천 인증된 사용자만
                .requestMatchers("/oauth2/**").permitAll() // OAuth2 로그인 경로 허용
                .requestMatchers("/funding/fund-with-points").permitAll() // "포인트로 결제"는 인증 없이 허용
                
                .requestMatchers("/novels/**").authenticated() // 기본적으로 인증된 사용자만 허용(로그인 필요)
                .requestMatchers(HttpMethod.POST, "/novels/**").hasRole("ADMIN") // POST는 ADMIN만
                .requestMatchers(HttpMethod.DELETE, "/novels/**").hasRole("ADMIN") // DELETE는 ADMIN만

                .requestMatchers("/chapters/**").authenticated() // 기본적으로 인증된 사용자만 허용
                .requestMatchers(HttpMethod.POST, "/chapters/**").hasRole("ADMIN") // POST는 ADMIN만
                .requestMatchers(HttpMethod.DELETE, "/chapters/**").hasRole("ADMIN") // DELETE는 ADMIN만
               
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
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                    .permitAll()
            )
            
            .oauth2Login(oauth2 -> oauth2
                    .loginPage("/login")
                    .defaultSuccessUrl("/dashboard", true)
                    .failureUrl("/login?error=true")
                    .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                )
            
            
            // CSRF 설정
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/payments/**", "/qna/**", "/ckeditor/**","/points/**", "/funding/fund-with-points","/oauth2/**") // 결제와 QnA 관련 요청은 CSRF 보호 비활성화
            )
        
	        .headers(headers -> headers
	                .frameOptions(frameOptions -> frameOptions.disable()) // X-Frame-Options 비활성화
	                													  // 외부 컨텐츠를 사용하기 위해 설정,  <iframe>으로 페이지를 포함할 수 있도록
	            );
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
