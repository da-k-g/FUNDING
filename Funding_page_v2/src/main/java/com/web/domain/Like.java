package com.web.domain;

import com.web.domain.enums.VoteType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  소설 - 추천 / 비추천
 */
@Entity
@Table(name = "likes", indexes = { // 인덱스 설정
        @Index(name = "index_tbl_likes", columnList = "novel_id, users_id")
}, uniqueConstraints = { // 중복 제한
        @UniqueConstraint(name = "unique_user_novel_like", columnNames = {"users_id", "novel_id"})
})
@Data
@NoArgsConstructor // 기본생성자 생성
@AllArgsConstructor // 모든 필드를 포함하는 생성자 생성
@Builder // Builder 패턴 생성
public class Like  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "like_id")
    private Long id; // 추천/비추천 ID

    @ManyToOne(fetch = FetchType.LAZY) // user와 다대일 관계 설정
    @JoinColumn(name = "users_id", nullable = false) // 왜래키
    private User user; // 추천/비추천을 남긴 사용자

    @ManyToOne(fetch = FetchType.LAZY)// novel 다대일 관계 설정
    @JoinColumn(name = "novel_id", nullable = false)// 외래키
    private Novel novel; // 추천/비추천이 속한 소설

    @Enumerated(EnumType.STRING) // Enum(열거형) 값을 문자열로 지정, 더 세부적인 상태 관리 위해 관리(com.web.domain.enums.VoteType.java)
    @Column(name = "voteType", nullable = false)
    private VoteType voteType; // 추천 또는 비추천

    // 소설 작성자 아이디
    private Long novelWriter; // 소설 작성자의 사용자 id
    
}
