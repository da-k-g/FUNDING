package com.web.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity // 이 클래스가 JPA 엔티티임을 명시
@Table(name = "PROJECT") // 데이터베이스의 테이블 이름
public class Project {

    @Id // 기본 키 설정
    @Column(name = "PROJECT_ID", length = 20) // 컬럼 이름과 속성 설정
    private String projectId;

    @Column(name = "TITLE", nullable = false) // 제목
    private String title;

    @Column(name = "GOAL", nullable = false) // 목표 금액
    private int goal;

    @Column(name = "P_CURRENT", nullable = false) // 현재 모금액
    private int current;

    @Column(name = "START_DATE", nullable = false) // 시작 날짜
    private LocalDate startDate;

    @Column(name = "END_DATE", nullable = false) // 종료 날짜
    private LocalDate endDate;

    @Column(name = "P_STATUS", nullable = false) // 진행 상태
    private String status; // 예: 진행 중, 성공, 실패

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Funding> fundings; // 이 프로젝트와 관련된 펀딩 목록

    // 기본 생성자
    public Project() {}

    // Getters & Setters
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Funding> getFundings() {
        return fundings;
    }

    public void setFundings(List<Funding> fundings) {
        this.fundings = fundings;
    }
}

