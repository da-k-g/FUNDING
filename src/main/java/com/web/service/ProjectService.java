package com.web.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.domain.Project;
import com.web.repository.ProjectRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // 모든 프로젝트 조회
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // 특정 프로젝트 조회
    public Optional<Project> getProjectById(String projectId) {
        return projectRepository.findById(projectId);
    }

    // 새로운 프로젝트 저장
    @Transactional
    public Project createProject(Project project) {
        project.setCurrent(0); // 현재 모금액 초기화
        project.setStatus("진행 중"); // 상태 초기화
        return projectRepository.save(project);
    }

    // 프로젝트 상태 업데이트 (예: 성공, 실패)
    @Transactional
    public Project updateProjectStatus(String projectId, String status) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("프로젝트를 찾을 수 없습니다."));
        project.setStatus(status);
        return projectRepository.save(project);
    }

    // 특정 상태의 프로젝트 조회
    public List<Project> getProjectsByStatus(String status) {
        return projectRepository.findByStatus(status);
    }

    // 기간 내의 프로젝트 조회
    public List<Project> getProjectsByDateRange(LocalDate start, LocalDate end) {
        return projectRepository.findByStartDateBetween(start, end);
    }
}

