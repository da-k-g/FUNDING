package com.web.utils;


import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;
/**
 * HTML 관련 유틸리티 클래스를 정의한 컴포넌트.
 * HTML 태그를 제거하거나 텍스트 변환 등의 작업을 처리.
 * 태그가 제거된 텍스트만 깔끔하게 출력하기 위해서 사용
 */
@Component
public class HtmlUtils {
	/**
     * HTML 태그를 제거하고 순수 텍스트만 반환.
     * 
     * @param input HTML 형식의 문자열.
     * @return HTML 태그가 제거된 텍스트.
     */
    public String stripHtml(String input) {
        return Jsoup.parse(input).text();  // Jsoup를 사용하여 HTML을 파싱하고 텍스트만 추출.
        // Jsoup pom.xml 에 의존성 추가
        // chapters view에서 줄거리만 출력하기 위해서
    }
    
}