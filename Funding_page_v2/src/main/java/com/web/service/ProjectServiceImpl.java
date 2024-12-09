package com.web.service;

import com.web.domain.Project;
import com.web.dto.ProjectDTO;
import com.web.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public List<ProjectDTO> getAllProjects() {
        // 모든 프로젝트를 조회하고 DTO로 변환
        return projectRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDTO getProjectById(Long id) {
        // 프로젝트를 ID로 조회하고 DTO로 변환
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + id));
        return convertToDTO(project);
    }

    @Override
    public void createProject(ProjectDTO projectDTO) {
        // DTO를 엔티티로 변환하여 저장
        Project project = convertToEntity(projectDTO);
        projectRepository.save(project);
    }

    @Override
    public void fundProject(Long projectId, int amount) {
        // 프로젝트를 조회하고 펀딩 금액을 추가한 후 저장
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + projectId));

        project.setCurrentFunding(project.getCurrentFunding() + amount);
        projectRepository.save(project);
    }

    // DTO로 변환
    private ProjectDTO convertToDTO(Project project) {
        return new ProjectDTO(
                project.getProjectId(),
                project.getTitle(),
                project.getDescription(),
                project.getGoal(),
                project.getCurrentFunding(),
                project.getStartDate(),
                project.getEndDate()
        );
    }

    // 엔티티로 변환
    private Project convertToEntity(ProjectDTO dto) {
        Project project = new Project();
        project.setProjectId(dto.getId());
        project.setTitle(dto.getTitle());
        project.setDescription(dto.getDescription());
        project.setGoal(dto.getGoal());
        project.setCurrentFunding(dto.getCurrentFunding());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        return project;
    }
}
