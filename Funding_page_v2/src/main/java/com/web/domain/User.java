package com.web.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // GenerationType.IDENTITY는 시퀀스를 생성하지 않고, 데이터베이스의 자동 증가(AUTO_INCREMENT) 기능에 의존합니다.
    // 때문에 시퀀스를 넣지 않아도 시퀀스의 역활을 대신합니다
    @Column(name = "users_id")
    private Long id;

    @Column(name="username", nullable = false, unique = true)
    private String username;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="password", nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
    
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QnA> qnaList = new ArrayList<>();
    
    @Column(nullable = false)
    private int points = 0; // 초기값은 0

    public enum Role {
        ADMIN,
        USER,
        GUEST
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> like;
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public User() {}


}
