package com.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 공통 응답 객체를 전송하기 위한 DTO 클래스
 * - API 응답의 구조를 통일하여 일관성 있는 데이터 전달
 * @param <T> 응답 데이터의 타입 (제네릭)
 */
@AllArgsConstructor // 모든 필드를 포함하는 생성자 생성
@NoArgsConstructor // 기본 생성자 생성
@Data
@Builder // 빌드 패턴 지원
public class ResponseDTO<T> {

	private int code; //1(성공), -1(실패)
    
	private String message; // 응답 메세지: 성공 또는 실패 메세지
    
	private String errorCode; // 오류 코드 (실패 시 제공)
    
	private T data; // 응답 데이터 (제네릭 타입)
	
}