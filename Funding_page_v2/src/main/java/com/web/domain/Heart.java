package com.web.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * '좋아요'(Heart) 정보를 저장하는 엔티티
 * - 특정 사용자와 특정 소설 간의 좋아요 관계를 표현
 */
@Getter
@Setter
@Entity
@Table(name = "heart") // 테이블 이름 설정
public class Heart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // GenerationType.IDENTITY는 시퀀스를 생성하지 않고, 데이터베이스의 자동 증가(AUTO_INCREMENT) 기능에 의존합니다.
    // 때문에 시퀀스를 넣지 않아도 시퀀스의 역활을 대신합니다
    @Column(name = "heart_id")
    private Long id; // 좋아요 ID

    @ManyToOne(fetch = FetchType.LAZY) // User와 다대일 관계 설정
    @JoinColumn(name = "user_id") // 외래 키 user_id
    private User user; // 좋아요를 누른 사용자

    @ManyToOne(fetch = FetchType.LAZY) // Novel과 다대일 관계 설정
    @JoinColumn(name = "novel_id") // 왜래키
    private Novel novel; // 좋아요가 속한 소설
    
    /**
     * 생성자: 사용자와 소설을 입력받아 Heart 객체를 생성
     * @param user 좋아요를 누른 사용자
     * @param novel 좋아요가 속한 소설
     */
    @Builder
    public Heart(User user, Novel novel) {
        this.user = user;
        this.novel = novel;
    }
 // 기본 생성자 : JPA 사용을 위해 필요
    public Heart() {
    }
    
    

}
