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

@Service //Spring의 서비스 계층
public class PaymentServiceImpl implements PaymentService {

						//HTTP 요청 전송 및 응답 처리
    private final RestTemplate restTemplate;
    //getAccessToken 메서드를 통해 Iamport API 인증 토큰을 발급받는 데 사용
    private final PortOneService portOneService;

    //application.properties에 설정된 값 주입
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
        String url = "https://api.iamport.kr/payments/prepare"; //Iamport 결제 준비 API의 URL인 https://api.iamport.kr/payments/prepare로 요청을 보냄.

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);   //요청 헤더에 Content-Type을 application/json으로 설정
        headers.setBearerAuth(accessToken);						//Bearer 토큰 방식을 사용하여 인증 헤더 추가

        Map<String, Object> body = new HashMap<>();
        body.put("merchant_uid", "project_" + projectId); // 고유 주문번호
        body.put("amount", amount); // 결제 금액

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
            //RestTemplate의 exchange 메서드를 사용하여 POST 요청

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return "결제 준비 완료: " + response.getBody().toString();
            }   //HTTP 응답이 200 OK이고 응답 본문이 비어 있지 않으면 결제 준비 성공 메시지를 반환
            throw new RuntimeException("결제 요청 실패: 응답 데이터가 비어 있습니다.");
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("결제 요청 중 오류 발생: " + e.getMessage(), e);
        } //응답 데이터가 비어 있거나 오류가 발생하면 예외를 던짐
    }

}
