package com.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.HttpStatus;

import java.util.Map;

import com.web.domain.CustomOAuth2User;
import com.web.domain.User;
import com.web.repository.UserRepository;
import com.web.service.PortOneService;

@Controller
@RequestMapping("/points")
public class PointsController {

    private final PortOneService portOneService;
    private final UserRepository userRepository;

    public PointsController(PortOneService portOneService, UserRepository userRepository) {
        this.portOneService = portOneService;   //결제 정보
        this.userRepository = userRepository;   // 유저 정보
    }  // 두 개에 의존성 주입해서 controller 사용

    @PostMapping("/charge")
    public ResponseEntity<Map<String, Object>> chargePoints(
            @RequestBody Map<String, Object> request,
            @AuthenticationPrincipal Object loggedInUser) {   // 현재 로그인된 인증된 사용자인지 확인 
        String impUid = (String) request.get("impUid");      // 결제 id 결제금액 전달 받음

        if (loggedInUser == null) {			// 인증된 유저가 아닐경우 
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "fail", "message", "로그인이 필요합니다."));
        }

        String email;

        // 일반 회원인지 확인
        if (loggedInUser instanceof org.springframework.security.core.userdetails.User) {   
            org.springframework.security.core.userdetails.User userDetails =
                    (org.springframework.security.core.userdetails.User) loggedInUser;
            email = userDetails.getUsername(); // 일반 로그인 사용자의 이메일
        }
        // 소셜 로그인 회원인지 확인
        else if (loggedInUser instanceof CustomOAuth2User) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) loggedInUser;
            email = oAuth2User.getUsername(); // 소셜 로그인 사용자의 이메일
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "fail", "message", "알 수 없는 사용자 유형입니다."));
        }

        try {
            Map<String, Object> paymentInfo = portOneService.verifyPayment(impUid);  // 결제 정보 검증 
            int paidAmount = (int) paymentInfo.get("amount");							// 결제 유효시 실제 금액 가져옴

            User user = userRepository.findByEmail(email);							// 사용자 이메일 기준으로 찾기
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("status", "fail", "message", "사용자를 찾을 수 없습니다."));
            }

            user.setPoints(user.getPoints() + paidAmount);   // 충전 포인트 업데이트
            userRepository.save(user);							// 업데이트 된 충전 포인트 저장

            return ResponseEntity.ok(Map.of("status", "success", "message", "포인트 충전 완료", "points", user.getPoints()));  // 성공시 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));   // 실패시 반환
        }
    }



    // 포인트 충전 페이지 렌더링 (GET 요청)
    @GetMapping("/charge")			// 페이지에 필요한 데이터인 userid 전달
    public String chargePointsPage(Model model) {
        model.addAttribute("userId", "exampleUser"); // 예제 사용자 ID 전달
        return "chargePoints"; // chargePoints.html 템플릿 반환
    }
}
