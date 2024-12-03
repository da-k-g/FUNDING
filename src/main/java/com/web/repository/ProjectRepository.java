package com.web.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.domain.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

    // 프로젝트 이름으로 검색
    List<Project> findByTitleContaining(String title);

    // 상태로 검색 (예: 진행 중인 프로젝트)
    List<Project> findByStatus(String status);

    // 특정 기간 내에 시작한 프로젝트 검색
    List<Project> findByStartDateBetween(LocalDate start, LocalDate end);
}

