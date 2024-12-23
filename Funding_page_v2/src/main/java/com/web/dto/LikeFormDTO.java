package com.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * 추천/비추천 요청 데이터를 전송하기 위한 DTO 클래스
 * - 사용자와 소설 간의 추천/비추천 정보를 관리
 */

@Data // Lombok: Getter, Setter, toString, equals, hashCode 자동 생성
@AllArgsConstructor // 모든 필드를 포함하는 생성자 생성
@NoArgsConstructor // 기본 생성자 생성
@Builder // builder 패턴 생성
@ToString 
public class LikeFormDTO {

	private Long id; // 추천/비추천 id
	
	private Long uid; // 추천/비추천을 남긴 사용자 id
	
	private Long novelId; // 추천/비추천 대상 소설 id
	
	private String voteType; // 추천/비추천 유형

}
