package com.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.siot.IamportRestClient.IamportClient;
/**
 * 애플리케이션 전역 설정을 관리하는 클래스.
 * 이 클래스는 Spring의 @Configuration 어노테이션을 사용하여 설정 빈을 정의합니다.
 * 펀딩하기 및 point 결제 기능(간편결제: 카카오페이)을 구현하기 위해 사용
 */
@Configuration
public class AppConfig {
	// 아임포트(결제 톻합 플랫폼)API 키
    String apiKey = "7066613284703264";
    // 아임포트 시크릿 키
    String secretKey = "MK17kAot8ys07vpyygvLlqi2lMTuHhwcttRW2SmFp0gBKQuM9kok19P6zXzQig7BVXlaCseysRHXHWFF";

    /**
     * IamportClient 빈을 생성하여 스프링 컨테이너에 등록합니다.
     * 아임포트 API 통신을 처리하기 위해 IamportClient 객체를 생성하며,
     * API 키와 Secret 키를 사용하여 인증을 설정합니다.
     *
     * @return IamportClient 객체
     */
    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, secretKey);
    }
}