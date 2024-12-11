package com.web.domain;

import jakarta.persistence.*;
import java.time.LocalDate;



@Entity
@Table(name = "FUNDING")
public class Funding {

    @Id // 기본 키 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성
    @Column(name = "FUNDING_ID")
    private Long fundingId;

    @Column(name = "AMOUNT", nullable = false) // 펀딩 금액
    private int amount;

    @Column(name = "F_DATE", nullable = false) // 펀딩 날짜
    private LocalDate fundingDate;

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계
    @JoinColumn(name = "PROJECT_ID", nullable = false) // 프로젝트와 연결
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계
    @JoinColumn(name = "USER_ID", nullable = false) // 사용자와 연결
    private User user;

  

    @Column(name = "MERCHANT_UID", nullable = false, unique = true)
    private String merchantUid; // Port One에서 생성된 고유 주문 번
    
    // 기본 생성자
    public Funding() {}

    // Getters & Setters
    public Long getFundingId() {
        return fundingId;
    }

    public void setFundingId(Long fundingId) {
        this.fundingId = fundingId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDate getFundingDate() {
        return fundingDate;
    }

    public void setFundingDate(LocalDate fundingDate) {
        this.fundingDate = fundingDate;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public String getMerchantUid() {
        return merchantUid;
    }

    public void setMerchantUid(String merchantUid) {
        this.merchantUid = merchantUid;
    }

 
}

