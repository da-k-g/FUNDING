package com.web.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 좋아요(Heart) 요청 데이터를 전송하기 위한 DTO 클래스
 * - 사용자와 소설의 좋아요 관계를 관리
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자를 PROTECTED로 제한
public class HeartRequestDTO {

    private Long userId; // 좋아요를 누른 사용자 id
    private Long novelId; // 좋아요 대상 소설 id
    // 생성자: 사용자 ID와 소설 ID를 입력받아 HeartRequestDTO 객체를 생성
    public HeartRequestDTO(Long userId, Long novelId) {
        this.userId = userId;
        this.novelId = novelId;
    }
    
}
