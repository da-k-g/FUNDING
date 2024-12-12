package com.web.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "funding")
public class Funding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "funding_id")
    private Long fundingId;

    @Column(nullable = false)
    private int amount;

    @Column(name = "f_date", nullable = false)
    private LocalDate fundingDate;


    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "merchant_uid", nullable = false, unique = true)
    private String merchantUid;

    public Funding() {}

    // Getters and Setters
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



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMerchantUid() {
        return merchantUid;
    }

    public void setMerchantUid(String merchantUid) {
        this.merchantUid = merchantUid;
    }
}
