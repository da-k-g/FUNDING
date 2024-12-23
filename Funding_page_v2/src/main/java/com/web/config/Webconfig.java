package com.web.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//이 클래스는 Spring MVC의 설정을 정의하는 클래스입니다.
//@Configuration 어노테이션을 사용하여 Spring에서 설정 클래스로 인식합니다.
@Configuration
public class Webconfig implements WebMvcConfigurer {
	
	// application.properties 파일에서 설정된 파일 업로드 디렉터리 경로를 주입받습니다.
	@Value("${file.funding-upload-dir}")
	    private String fundingUploadDir;
	
	// 정적 자원의 핸들러를 추가하여 특정 URL 요청이 로컬 파일 시스템의 파일을 제공하도록 설정합니다.
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 소설 이미지 경로 매핑
		registry.addResourceHandler("/uploads/**") 
                .addResourceLocations("file:c:/uploads/");  // "/uploads/**" 경로로 들어오는 요청을 "c:/uploads/" 경로의 파일로 매핑합니다.
        // 펀딩 이미지 정적 경로 매핑
        registry.addResourceHandler("/upload/**")  // "/upload/**" 경로로 들어오는 요청을 fundingUploadDir 변수 값에 해당하는 디렉터리 경로로 매핑합니다.
                .addResourceLocations("file:" + fundingUploadDir);
	}
	
	   // CORS(Cross-Origin Resource Sharing) 설정을 추가합니다.
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 URL 패턴에 대해 CORS 허용
                .allowedOrigins(
                    "http://localhost:8080", // 로컬 호스트 요청 허용
                    "https://api.iamport.kr" // 특정 외부 API 요청 허용
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드 지정
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true); // 쿠키 및 인증 정보를 포함한 요청 허용
    }
    
    
}
