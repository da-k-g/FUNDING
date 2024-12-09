package com.web.controller;



import com.web.domain.Funding;
import com.web.domain.Project;
import com.web.dto.FundingDTO;
import com.web.repository.FundingRepository;
import com.web.repository.ProjectRepository;
import com.web.service.PortOneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.time.LocalDate;
import java.util.Map;
@RestController
@RequestMapping("/payments")
public class PaymentVerificationController {

    private final PortOneService portOneService;
    private final FundingRepository fundingRepository;
    private final ProjectRepository projectRepository;

    public PaymentVerificationController(PortOneService portOneService, FundingRepository fundingRepository, ProjectRepository projectRepository) {
        this.portOneService = portOneService;
        this.fundingRepository = fundingRepository;
        this.projectRepository = projectRepository;
    }

    @PostMapping("/verify/{impUid}")
    public ResponseEntity<Map<String, Object>> verifyAndProcessPayment(
            @PathVariable("impUid") String impUid,
            @RequestBody FundingDTO fundingDTO) {
        try {
            // 1. 결제 검증
            Map<String, Object> paymentInfo = portOneService.verifyPayment(impUid);
            int paidAmount = (int) paymentInfo.get("amount");

            // 결제 금액 확인
            if (paidAmount != fundingDTO.getAmount()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("status", "fail", "message", "결제 금액이 일치하지 않습니다."));
            }

            // 2. Project 조회 및 Funding 저장
            Project project = projectRepository.findById(fundingDTO.getProjectId())
                    .orElseThrow(() -> new RuntimeException("해당 프로젝트를 찾을 수 없습니다."));

            // 3. Funding 저장
            Funding funding = new Funding();
            funding.setAmount(fundingDTO.getAmount());
            funding.setFundingDate(LocalDate.now());
            funding.setUserId(fundingDTO.getUserId());
            funding.setMerchantUid(impUid);
            funding.setProject(project);
            fundingRepository.save(funding);

            // 4. Current Funding 업데이트
            int updatedFunding = project.getCurrentFunding() + fundingDTO.getAmount();
            project.setCurrentFunding(updatedFunding); // 업데이트된 값 설정
            projectRepository.save(project); // 저장

            return ResponseEntity.ok(Map.of("status", "success", "message", "결제가 성공적으로 완료되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }



    // 로그인한 사용자 ID 가져오기 메서드
    private String getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // 로그인된 사용자의 username 반환
    }

    }



