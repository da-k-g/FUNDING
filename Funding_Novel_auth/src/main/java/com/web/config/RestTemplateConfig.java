package com.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
                     
@Configuration
public class RestTemplateConfig {                // restTemplate : 서버 간의 HTTP 요청을 쉽게 보낼 수 있도록 설계
												 //                restapi 호출 간단히 처리 가능
    @Bean											// 클라이언트 -> 서버
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
