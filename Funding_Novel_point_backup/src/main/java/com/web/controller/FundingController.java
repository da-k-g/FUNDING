package com.web.controller;

import com.web.dto.FundingDTO;
import com.web.service.FundingService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/funding")
public class FundingController {

    @Autowired
    private FundingService fundingService;

    @PostMapping
    public String fundProject(@RequestBody FundingDTO fundingDTO) {
        fundingService.fundProject(fundingDTO.getProjectId(), fundingDTO.getAmount());
        return "펀딩 성공!";
    }

    @PostMapping("/fund-with-points")
    public ResponseEntity<Map<String, Object>> fundWithPoints(@RequestBody Map<String, Object> request) {
        try {
            if (!request.containsKey("projectId") || !request.containsKey("amount") || !request.containsKey("email")) {
                return ResponseEntity.badRequest().body(Map.of("status", "fail", "message", "필수 값이 누락되었습니다."));
            }

            Long projectId = Long.parseLong(request.get("projectId").toString());
            int amount = Integer.parseInt(request.get("amount").toString());
            String email = request.get("email").toString();

            fundingService.fundWithPoints(projectId, amount, email);
            return ResponseEntity.ok(Map.of("status", "success", "message", "펀딩 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "fail", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }


    // (선택) GET 요청에 대한 설명 또는 안내 페이지 제공
    @GetMapping("/fund-with-points")
    public ResponseEntity<String> fundWithPointsHelp() {
        return ResponseEntity.ok("포인트 펀딩은 POST 요청만 지원합니다.");
    }
}


