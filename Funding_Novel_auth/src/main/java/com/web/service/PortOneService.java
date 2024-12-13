package com.web.service;



import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.Map;

@Service
public class PortOneService {

    private final RestTemplate restTemplate;

    // 하드코딩된 Iamport API Key와 Secret
    private final String API_KEY = "7066613284703264";
    private final String API_SECRET = "F2SSGDxO48QTevkoegXWXNOCBYeBOfvWQQlm66KP1xUwGvR2Xf4zMShVxlRBvT0B3vMuasGsRNyHDvKX";

    public PortOneService() {
        this.restTemplate = new RestTemplate();
    }

  //Iamport API를 호출하여 Access Token을 발급
    public String getAccessToken() {
        String url = "https://api.iamport.kr/users/getToken";

        try {
            // 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);   //Content-Type을 application/json으로 설정

            // 요청 본문 데이터 생성
            Map<String, String> body = new HashMap<>();
            body.put("imp_key", API_KEY); // IAMPORT 제공 키
            body.put("imp_secret", API_SECRET); // IAMPORT 제공 비밀키

            // 요청 본문을 JSON 문자열로 직렬화
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(body);

            // 디버깅: JSON 요청 데이터 확인
            System.out.println("Access Token 요청 JSON: " + jsonBody);

            // HTTP 요청 엔티티 생성
            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

            // RestTemplate을 사용해 POST 요청 전송
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
            System.out.println("Access Token 응답: " + response.getBody());


            // 응답 처리
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseData = (Map<String, Object>) response.getBody().get("response");
                if (responseData != null && responseData.containsKey("access_token")) {
                    System.out.println("Access Token 발급 성공: " + responseData.get("access_token"));
                    return (String) responseData.get("access_token");
                }
            }
            throw new RuntimeException("Access Token 발급 실패: 응답 데이터가 비어 있습니다.");
        } catch (HttpClientErrorException e) {
            // HttpClientErrorException 디버깅
            System.err.println("HttpClientErrorException 발생: 상태 코드: " + e.getStatusCode());
            System.err.println("응답 본문: " + e.getResponseBodyAsString());
            throw new RuntimeException("Access Token 요청 중 오류 발생: " + e.getMessage(), e);
        } catch (Exception e) {
            // 일반 예외 처리
            System.err.println("Access Token 요청 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("Access Token 요청 중 오류 발생: " + e.getMessage(), e);
        }
        
        
    }




    //클래스가 생성되고 의존성 주입이 완료된 뒤 초기화를 수행하는 메서드에 사용
    //API 키와 비밀 키의 유효성을 검증
    @PostConstruct
    public void checkApiKeys() {
        System.out.println("Iamport Key: " + API_KEY);
        System.out.println("Iamport Secret: " + API_SECRET);

        if (API_KEY == null || API_KEY.isEmpty() || API_SECRET == null || API_SECRET.isEmpty()) {
            throw new RuntimeException("Iamport API Key 또는 Secret이 설정되지 않았습니다.");
        }
    }
    
    



    @SuppressWarnings("unchecked")
    public Map<String, Object> verifyPayment(String impUid) {
        String url = "https://api.iamport.kr/payments/" + impUid;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        Map<String, Object> body = response.getBody();
        if (body == null || body.get("response") == null) {
            throw new RuntimeException("결제 검증 실패: Iamport API 응답 데이터가 없습니다.");
        }

        return (Map<String, Object>) body.get("response"); // response 부분만 반환
    }


}
