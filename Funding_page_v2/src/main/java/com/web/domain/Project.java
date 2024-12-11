package com.web.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId; // 필드명 변경

    private String title;

    private String description;

    private int goal; // 목표 금액

    private int currentFunding; // 현재 펀딩 금액

    private LocalDate startDate;

    private LocalDate endDate;
    
    private String imagePath;
    
    private String createdBy;

    public Project() {}
    
    // Getter and Setter for createdBy
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // Getters and Setters
    public Long getProjectId() { // 메서드 이름 수정
        return projectId;
    }

    public void setProjectId(Long projectId) { // 메서드 이름 수정
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getCurrentFunding() {
        return currentFunding;
    }

    public void setCurrentFunding(int currentFunding) {
        this.currentFunding = currentFunding;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
