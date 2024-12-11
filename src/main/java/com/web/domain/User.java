package com.web.domain;



import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @Column(name = "USER_ID", nullable = false, unique = true, length = 20)
    private String userId; // 사용자 고유 ID

    @Column(name = "USERNAME", nullable = false, length = 50)
    private String username; // 사용자 이름

    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email; // 사용자 이메일

    @Column(name = "ROLE", nullable = false, length = 20)
    private String role; // 사용자 역할 (예: "USER", "ADMIN")

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDate createdAt; // 계정 생성 날짜

    // Funding 엔티티와의 1:N 관계
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Funding> fundings;

    // 기본 생성자 (JPA에서 필요)
    public User() {}

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public List<Funding> getFundings() {
        return fundings;
    }

    public void setFundings(List<Funding> fundings) {
        this.fundings = fundings;
    }
}
