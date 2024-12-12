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

import com.web.domain.User;
import com.web.repository.UserRepository;
import com.web.service.PortOneService;

@Controller
@RequestMapping("/points")
public class PointsController {

    private final PortOneService portOneService;
    private final UserRepository userRepository;

    public PointsController(PortOneService portOneService, UserRepository userRepository) {
        this.portOneService = portOneService;
        this.userRepository = userRepository;
    }

    @PostMapping("/charge")
    public ResponseEntity<Map<String, Object>> chargePoints(
            @RequestBody Map<String, Object> request,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User loggedInUser) {
        String impUid = (String) request.get("impUid");

        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "fail", "message", "로그인이 필요합니다."));
        }

        String email = loggedInUser.getUsername(); // Spring Security에서 현재 로그인된 사용자의 이메일 가져오기

        try {
            Map<String, Object> paymentInfo = portOneService.verifyPayment(impUid);
            int paidAmount = (int) paymentInfo.get("amount");

            User user = userRepository.findByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("status", "fail", "message", "사용자를 찾을 수 없습니다."));
            }

            user.setPoints(user.getPoints() + paidAmount);
            userRepository.save(user);

            return ResponseEntity.ok(Map.of("status", "success", "message", "포인트 충전 완료", "points", user.getPoints()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }


    // 포인트 충전 페이지 렌더링 (GET 요청)
    @GetMapping("/charge")
    public String chargePointsPage(Model model) {
        model.addAttribute("userId", "exampleUser"); // 예제 사용자 ID 전달
        return "chargePoints"; // chargePoints.html 템플릿 반환
    }
}
