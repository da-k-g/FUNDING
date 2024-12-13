package com.web.service;

import com.web.domain.Project;
import com.web.dto.ProjectDTO;
import com.web.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        return projectRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + id));
        return convertToDTO(project);
    }

    @Override
    public void createProject(ProjectDTO projectDTO) {
        Project project = convertToEntity(projectDTO);
        projectRepository.save(project);
    }

    @Override
    public void fundProject(Long projectId, int amount) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + projectId));
        project.setCurrentFunding(project.getCurrentFunding() + amount);
        projectRepository.save(project);
    }

    // 최근 프로젝트 조회
//    @Override
//    public List<ProjectDTO> getRecentProjects(int limit) {
//        return projectRepository.findAllByOrderByStartDateDesc().stream()
//                .limit(limit) // limit 적용
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
    @Override
    public List<ProjectDTO> getRecentProjects(int limit) {
        return projectRepository.findAllByOrderByStartDateAsc().stream()
                .limit(limit) // limit 적용
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<ProjectDTO> searchProjectsByTitle(String title) {
        return projectRepository.findByTitleContaining(title).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    // 제목으로 프로젝트 검색
    @Override
    public List<ProjectDTO> getProjectsByTitle(String title) {
        return projectRepository.findAll().stream()
                .filter(project -> project.getTitle().contains(title)) // 제목에 키워드 포함 여부 확인
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<ProjectDTO> getProjectsWithPaging(int page, int size) {
        // 페이지 요청 객체 생성
        PageRequest pageRequest = PageRequest.of(page, size);
        // 페이징 처리된 프로젝트 데이터 가져오기
        Page<Project> projectPage = projectRepository.findAll(pageRequest);

        // 프로젝트 데이터를 DTO로 변환하여 반환
        return projectPage.map(this::convertToDTO);
    }

    private ProjectDTO convertToDTO(Project project) {
        return new ProjectDTO(
                project.getProjectId(),
                project.getTitle(),
                project.getDescription(),
                project.getGoal(),
                project.getCurrentFunding(),
                project.getStartDate(),
                project.getEndDate(),
                project.getImagePath(),
                project.getCreatedBy() 
        );
    }

    private Project convertToEntity(ProjectDTO dto) {
        Project project = new Project();
        project.setProjectId(dto.getId());
        project.setTitle(dto.getTitle());
        project.setDescription(dto.getDescription());
        project.setGoal(dto.getGoal());
        project.setCurrentFunding(dto.getCurrentFunding());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setImagePath(dto.getImagePath());
        project.setCreatedBy(dto.getCreatedBy());
        return project;
    }
}

