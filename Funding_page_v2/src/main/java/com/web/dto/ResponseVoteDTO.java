package com.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 소설 추천/비추천 상태 정보를 전송하기 위한 DTO 클래스
 * - 추천/비추천 수와 사용자의 현재 투표 상태를 포함
 */
@Data
@Builder // 빌드 패턴 지원
@AllArgsConstructor // 모든 필드를 포함하는 생성자 생성
@NoArgsConstructor // 기본 생성자 생성
public class ResponseVoteDTO {

    private Long novelId; // 소설 id
    
    private String message; // 응답 메시지 (예: 성공 또는 실패 메시지)
    
    private Long upvoteCount; // 추천(UP) 수
    
    private Long downvoteCount; // 비추천(DOWN) 수
    
    @Builder.Default
    private String myVoted = "NONE"; // 사용자의 현재 좋아요 상태 (UP, DOWN, NONE)

    public ResponseVoteDTO(Long novelId, Long upvoteCount, Long downvoteCount) {
        this.novelId = novelId;
        this.upvoteCount = upvoteCount;
        this.downvoteCount = downvoteCount;
        this.message = ""; // 기본 메세지 설정
        this.myVoted = "NONE"; // 기본 투표(좋아요) 상태 명시적으로 설정
    }
    
    
}
