package com.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    // 로그인 전 페이지
    @GetMapping("/")
    public String index(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/dashboard"; // 로그인 상태면 대시보드로 리다이렉트
        }
        return "index"; // 로그인 전 페이지
    }

    // 로그인 후 페이지 (대시보드)
//    @GetMapping("/dashboard")
//    public String dashboard(Model model, Authentication authentication) {
//        String username = authentication.getName(); // 로그인한 사용자 이름 가져오기
//        model.addAttribute("username", username);
//        return "dashboard"; // 로그인 후 전용 홈 페이지
//    }
}
