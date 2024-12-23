package com.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 소설 추천 수를 전송하기 위한 DTO 클래스
 * - 특정 소설의 추천 수를 전달하거나 표시
 */
@Data
@Builder
@AllArgsConstructor // 모든 필드를 포함하는 생성자 생성
@NoArgsConstructor // 기본 생성자 생성
public class NovelUpvoteCountDTO {
	
	  private Long novelId; // 소설 ID
	  private Long  upvoteCount; // 추천 수

}
