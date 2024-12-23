package com.web.domain;

import org.hibernate.annotations.ColumnDefault;

import com.web.domain.base.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


// 소설의 회차 정보를 저장 하는 엔티티 클래스
@Entity // 엔티티로 선언
@Getter
@Setter
@Builder // Builder 패턴을 위한 Lombok 어노테이션
@NoArgsConstructor // 기본 생성자 생성
@AllArgsConstructor // 모든 필드를 포함하는 생성자 생성
public class Chapter extends BaseTimeEntity { // BaseTimeEntity를 상속받아 생성/수정 시간 자동 관리

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키와 자동 생성 전략 (AUTO_INCREMENT), 시퀀스 역활을 대신 해주고 있음
    private Long id; // 회차 ID

    @Column(nullable = false) // 제목은 NULL이 불가능
    private String title; // 회차 제목

    @Lob // 대용량 텍스트 저장
    private String content; // 회차 내용

    @Column(name = "thumbnail_image_url") // DB 칼럼 이름 매핑
    private String thumbnailImageUrl; // 회차 썸네일 이미지 경로
    
    @Column(name = "is_paid", nullable = false) // 무료/유료 여부
    private boolean isPaid;

    
    @ColumnDefault("0") // 기본 값 0 설정
    @Column(name = "view_count", nullable = false)
    private Integer viewCount=0; // 회차 조회수, 기본 값 0
    
    
    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계 설정, 지연 로딩 사용
    @JoinColumn(name = "novel_id", nullable = false) // 외래 키 설정 및 Not Null
    private Novel novel; // 해당 회차가 속한 소설
    
 

}


