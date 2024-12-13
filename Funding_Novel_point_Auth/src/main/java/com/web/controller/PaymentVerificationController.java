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
@RestController   //REST API를 제공하는 컨트롤러로 JSON 형식의 데이터를 반환
@RequestMapping("/payments")   //컨트롤러의 모든 엔드포인트는 /payments 경로를 기본 경로로 사용.
public class PaymentVerificationController {

    private final PortOneService portOneService;   //결제 검증 서비스
    private final FundingRepository fundingRepository;   //펀딩 데이터를 저장/조회하는 DAO
    private final ProjectRepository projectRepository;   //프로젝트 데이터를 관리하는 DAO

    public PaymentVerificationController(PortOneService portOneService, FundingRepository fundingRepository, ProjectRepository projectRepository) {
        this.portOneService = portOneService;
        this.fundingRepository = fundingRepository;
        this.projectRepository = projectRepository;
    }

    @PostMapping("/verify/{impUid}")
//API 응답 상태 및 JSON 데이터 반환
    public ResponseEntity<Map<String, Object>> verifyAndProcessPayment(
            @PathVariable("impUid") String impUid,
            @RequestBody FundingDTO fundingDTO) {   //FundingDTO를 통해 요청 데이터를 전달
        try {
            // 1. 결제 검증
            Map<String, Object> paymentInfo = portOneService.verifyPayment(impUid);   //결제정보를 검증
            int paidAmount = (int) paymentInfo.get("amount");
            //paidAmount와 클라이언트가 요청한 fundingDTO.getAmount()를 비교

            // 결제 금액 확인
            if (paidAmount != fundingDTO.getAmount()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("status", "fail", "message", "결제 금액이 일치하지 않습니다."));
            }

            // 2. Project 조회 및 Funding 저장             //프로젝트 데이터를 조회
            Project project = projectRepository.findById(fundingDTO.getProjectId())
                    .orElseThrow(() -> new RuntimeException("해당 프로젝트를 찾을 수 없습니다."));

            // 3. Funding 엔티티 생성 후 데이터베이스에 저장
            Funding funding = new Funding();
            funding.setAmount(fundingDTO.getAmount());
            funding.setFundingDate(LocalDate.now());
            funding.setUserId(fundingDTO.getUserId());
            funding.setMerchantUid(impUid);
            funding.setProject(project);
            fundingRepository.save(funding);

            // 4. 기존 프로젝트의 currentFunding 값을 업데이트
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



