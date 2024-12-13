package com.web.service;

import com.web.domain.CustomOAuth2User;
import com.web.domain.User;
import com.web.repository.UserRepository;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomOAuth2UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = null;
        String name = null;

        // 네이버 OAuth2 처리
        if ("naver".equals(registrationId)) {
            attributes = (Map<String, Object>) attributes.get("response");
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
        }
        // 카카오 OAuth2 처리
        else if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            email = (String) kakaoAccount.get("email");
            name = (String) profile.get("nickname");
        }

        // 사용자 데이터베이스 업데이트 또는 저장
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setUsername(name);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode("N/A"));  // OAuth2 사용자는 비밀번호 불필요
            user.setRole("USER");  // 기본 역할을 "USER"로 설정

            // 특정 이메일에 대해 ROLE_ADMIN 부여 (예: 관리자 이메일)
            if ("admin2@example.com".equals(email)) {
                user.setRole("ROLE_ADMIN");  // ROLE_ADMIN 설정
            }

            userRepository.save(user);
        }

        // Spring Security의 Principal로 사용할 객체 생성
        return new CustomOAuth2User(
            Collections.singletonList(new SimpleGrantedAuthority(user.getRole())),  // 권한은 'ADMIN' 또는 'USER'
            attributes,
            user.getUsername(),
            user.getEmail()
        );
    }
}
