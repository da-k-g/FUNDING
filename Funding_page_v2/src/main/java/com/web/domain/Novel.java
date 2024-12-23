package com.web.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.web.domain.base.BaseTimeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// 소설 정보를 저장하는 엔티티
@Entity
@Data // Lombok: Getter, Setter, toString, equals, hashCode 생성
@NoArgsConstructor // 기본 생성자 생성
@DynamicInsert // INSERT 쿼리 생성 시 기본값이 설정된 칼럼 제외
@DynamicUpdate // UPDATE 쿼리 생성 시 변경된 칼럼만 포함
@AllArgsConstructor // 모든 필드를 포함하는 생성자 생성
@Builder // Builder 패턴 생성
@Table(name = "novel") // 테이블 이름 설정
public class Novel  extends BaseTimeEntity{ // BaseTimeEntity 상속: 생성/수정 시간 관리
	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동으로 생성, 시퀀스 역활 대신
    @Column(name = "novel_id")
    private Long id; // 소설 id

    @Column(nullable = false)
    private String title; // 제목

    @Column(nullable = false)
    private String author; // 작가 이름

    @Column(nullable = false)
    private String category; // 설정 카테고리

    @Column(name = "description", nullable = false)
    @Lob // 대용량 텍스트 데이터 저장
    private String description; // 소설 설명(줄거리)

    
    @Column(name = "cover_image_url")
    private String coverImageUrl; // 소설 표지 이미지 경로
    

    @ColumnDefault("0") // 기본값 0설정
    @Column(name = "view_count", nullable = false) // 조회수
    private Integer viewCount; 

    @Column(name = "is_paid", nullable = false) // 무료/유료 여부
    private boolean paid;
       
    @ColumnDefault("0") // 기본 값 0설정
    @Column(name = "like_Score", nullable = false)
    private Integer likeScore; //좋아요 싫어요 점수 -추천 비추천 점수
    
    
    @ManyToOne(fetch =  FetchType.LAZY) // user와 다대일 관계
    @JoinColumn(name = "users_id") // 외래키
    private User user; // 소설 작성자
    
    
    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Chapter> chapters = new ArrayList<>(); // 소설의 회차 목록
    
    
    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes =new ArrayList<Like>(); // 소설의 추천/비추천 목록
    

    /**
     * 조회수 증가 메서드
     * 소설이 조회될 때 호출하여 조회수를 1 증가
     */
    public void incrementViewCount() {
        this.viewCount++; // 조회수를 1씩증가
    }





}


