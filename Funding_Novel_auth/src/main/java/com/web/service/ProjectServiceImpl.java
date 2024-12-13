package com.web.service;

import com.web.domain.Project;
import com.web.domain.User;
import com.web.dto.ProjectDTO;
import com.web.repository.ProjectRepository;
import com.web.repository.UserRepository;

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
    private final UserRepository userRepository;

    @Autowired     // 프로젝트 데이터베이스에 접근
    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    //모든 프로젝트 데이터를 조회하고 DTO로 변환 후 반환
    @Override
    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()   //findAll() 메서드를 사용하여 DB에서 프로젝트 데이터를 가져옴
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    //특정 ID의 프로젝트를 조회
    //프로젝트가 존재하지 않으면 IllegalArgumentException 예외 발생
    @Override
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + id));
        return convertToDTO(project);  //조회된 엔티티를 DTO로 변환 후 반환
    }

    @Override
    public void createProject(ProjectDTO projectDTO) {
        Project project = convertToEntity(projectDTO);  //DTO를 엔티티로 변환한 후 저장
        projectRepository.save(project);                //새로운 프로젝트를 데이터베이스에 추가
    }

    // 특정 프로젝트에 펀딩 금액 추가
    @Override
    public void fundProject(Long projectId, int amount) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + projectId));  //프로젝트 존재 X시 예외 발생
        project.setCurrentFunding(project.getCurrentFunding() + amount);   // 현재 펀딩액 업데이트후 저장
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
    
    //최근 프로젝트 조회
    @Override
    public List<ProjectDTO> getRecentProjects(int limit) {
        return projectRepository.findAllByOrderByStartDateAsc().stream()
                .limit(limit) // limit 적용   상위 limit 개의 결과만 가져옴
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

// 특정 키워드 포함시키는 검색 검색기능
    @Override
    public List<ProjectDTO> searchProjectsByTitle(String title) {
        return projectRepository.findByTitleContaining(title).stream()   // 특정 title 즉 단어가 포함되는것을 검색
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

    // 프로젝트 엔티티 dto로 변환
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

    // dto db에 저장되게끔 엔티티로 변환
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
    @Transactional
    public void fundWithPoints(Long projectId, int amount, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        
        if (user.getPoints() < amount) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트를 찾을 수 없습니다."));

        // 포인트 차감 및 프로젝트 펀딩 금액 업데이트
        user.setPoints(user.getPoints() - amount);
        userRepository.save(user);

        project.setCurrentFunding(project.getCurrentFunding() + amount);
        projectRepository.save(project);
    }
}

