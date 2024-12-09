package com.web.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final RestTemplate restTemplate;
    private final PortOneService portOneService;

    @Value("${iamport.key}")
    private String apiKey;

    @Value("${iamport.secret}")
    private String apiSecret;

    // 생성자 주입
    public PaymentServiceImpl(RestTemplate restTemplate, PortOneService portOneService) {
        this.restTemplate = restTemplate;
        this.portOneService = portOneService;
    }
    @Override
    public String processPayment(Long projectId, int amount) {
        String accessToken = portOneService.getAccessToken(); // Access Token 발급
        String url = "https://api.iamport.kr/payments/prepare";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> body = new HashMap<>();
        body.put("merchant_uid", "project_" + projectId); // 고유 주문번호
        body.put("amount", amount); // 결제 금액

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return "결제 준비 완료: " + response.getBody().toString();
            }
            throw new RuntimeException("결제 요청 실패: 응답 데이터가 비어 있습니다.");
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("결제 요청 중 오류 발생: " + e.getMessage(), e);
        }
    }

}
