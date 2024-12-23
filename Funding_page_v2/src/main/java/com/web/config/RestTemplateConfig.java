package com.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

//이 클래스는 Spring의 설정 파일로 사용됩니다.
//@Configuration 어노테이션을 사용하여 Spring 컨테이너에서 설정 클래스로 인식되도록 합니다.
@Configuration
public class RestTemplateConfig {
	// RestTemplate 객체를 빈(Bean)으로 등록합니다.
    // @Bean 어노테이션을 사용하여 이 메서드에서 생성된 객체가 Spring 컨테이너에 의해 관리되도록 합니다.
    // RestTemplate은 RESTful API 호출 시 편리하게 사용할 수 있는 클래스입니다.
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(); // 새로운 객체 생성 반환
    
    }
}
