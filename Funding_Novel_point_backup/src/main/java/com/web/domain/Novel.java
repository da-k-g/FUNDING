package com.web.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Data
public class Novel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성 ID
    private Long id;

    private String title;         // 소설 제목
    private String author;		  // 작가
    private String category;      // 카테고리
    private String description;   // 소설설 명
    //private String coverImage;    // 표지 이미지 경로
    private Long cnt;			  // 조회순
    private boolean isPaid; // 무료/유료 여부
    private int likeCount; // 좋아요 개수 필드 추가
    
    public Novel() {
        this.likeCount = 0; // 기본값 설정
    }

    
}
