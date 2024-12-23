package com.web.service;

import com.web.dto.ProjectDTO;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

public interface ProjectService {
    List<ProjectDTO> getAllProjects();
    ProjectDTO getProjectById(Long id);
    void createProject(ProjectDTO projectDTO);
    void fundProject(Long projectId, int amount);
    void fundWithPoints(Long projectId, int amount, String username); // 포인트 펀딩 메서드 추가
    
    List<ProjectDTO> getRecentProjects(int limit); // 최근 프로젝트 조회
    List<ProjectDTO> getProjectsByTitle(String title); 
    List<ProjectDTO> searchProjectsByTitle(String title);
    Page<ProjectDTO> getProjectsWithPaging(int page, int size);
    Optional<ProjectDTO> getProjectByTitle(String title);

}
