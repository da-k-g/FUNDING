package com.web.controller;

import com.web.dto.FundingDTO;
import com.web.service.FundingService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController											// ResponseEntitiy : HTTP 응답의 상태 코드를 명시적으로 설정, 응답 본문 자유롭게 설정
@RequestMapping("/funding")
public class FundingController {

    @Autowired
    private FundingService fundingService;

    @PostMapping	// 특정 프로젝트에 펀딩 금액 추가 
    public String fundProject(@RequestBody FundingDTO fundingDTO) { // json 형태로 전송된 데이터 매핑 dto
        fundingService.fundProject(fundingDTO.getProjectId(), fundingDTO.getAmount()); // fundproject() 호출
        return "펀딩 성공!";
    }

    @PostMapping("/fund-with-points")       // 클라이언트에 json 데이터 전송 projectid,email,amount 등
    public ResponseEntity<Map<String, Object>> fundWithPoints(@RequestBody Map<String, Object> request) {
        try {
            if (!request.containsKey("projectId") || !request.containsKey("amount") || !request.containsKey("email")) {
                return ResponseEntity.badRequest().body(Map.of("status", "fail", "message", "필수 값이 누락되었습니다."));
            }   // 셋중에 하나라도 누락되면 
            								//object 타입일수 있으므로
            Long projectId = Long.parseLong(request.get("projectId").toString()); // json 형태 데이터에서 projectid 가져옴  string 으로 변환
            int amount = Integer.parseInt(request.get("amount").toString());
            String email = request.get("email").toString();

            fundingService.fundWithPoints(projectId, amount, email);
            return ResponseEntity.ok(Map.of("status", "success", "message", "펀딩 성공"));  // 성공시 
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "fail", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }


    //  GET 요청에 대한 설명 또는 안내 페이지 제공
    @GetMapping("/fund-with-points")
    public ResponseEntity<String> fundWithPointsHelp() {
        return ResponseEntity.ok("포인트 펀딩은 POST 요청만 지원합니다.");
    }
}


