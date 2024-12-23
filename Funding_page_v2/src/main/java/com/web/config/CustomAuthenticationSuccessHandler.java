package com.web.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
/**
 * 사용자 인증 성공 후 동작을 정의하는 커스텀 AuthenticationSuccessHandler.
 * 인증에 성공한 사용자의 권한에 따라 적절한 경로로 리다이렉트 처리합니다.
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
	    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities(); // 인증된 사용자의 권한 목록 가져오기	 
	    String redirectUrl = "/dashboard"; // 기본 리다이렉트 URL 설정
	    for (GrantedAuthority authority : authorities) { // 사용자가 관리자 권한을 가진 경우
	        if (authority.getAuthority().equals("ROLE_ADMIN")) { // 정확한 ROLE_ADMIN 확인
	            redirectUrl = "/admin/dashboard"; // 기본 리다이렉트 URL 설정
	            break; // 관리자 권환이 확인 되면 루프 종료
	        }
	    }

	    // 로그 추가 (권한 확인 및 리다이렉트 URL 확인)
	    System.out.println("Authenticated user: " + authentication.getName());
	    System.out.println("Authorities: " + authorities); 
	    System.out.println("Redirecting to: " + redirectUrl);

	    response.sendRedirect(redirectUrl);
	}


}
