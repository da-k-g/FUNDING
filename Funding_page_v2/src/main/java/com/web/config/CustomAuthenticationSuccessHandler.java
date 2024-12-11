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

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
	    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

	    String redirectUrl = "/dashboard"; // 기본 경로
	    for (GrantedAuthority authority : authorities) {
	        if (authority.getAuthority().equals("ROLE_ADMIN")) { // 정확한 ROLE_ADMIN 확인
	            redirectUrl = "/admin/dashboard";
	            break;
	        }
	    }

	    // 로그 추가 (권한 확인 및 리다이렉트 URL 확인)
	    System.out.println("Authenticated user: " + authentication.getName());
	    System.out.println("Authorities: " + authorities);
	    System.out.println("Redirecting to: " + redirectUrl);

	    response.sendRedirect(redirectUrl);
	}


}
