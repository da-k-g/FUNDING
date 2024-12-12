package com.web.controller;

import com.web.dto.ProjectDTO;
import com.web.service.PaymentService;
import com.web.service.ProjectService;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final PaymentService paymentService; // PortOne API 호출 서비스 추가

    public ProjectController(ProjectService projectService, PaymentService paymentService) {
        this.projectService = projectService;
        this.paymentService = paymentService;
    }

    @PostMapping("/{id}/fund")
    public String fundProject(@PathVariable Long id, @RequestParam int amount) {
        paymentService.processPayment(id, amount);
        projectService.fundProject(id, amount);
        return "redirect:/projects/" + id;
    }

    @GetMapping("/{id}/fund")
    public String fundProjectForm(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails, // 로그인된 사용자 정보 가져오기
            Model model) {
        model.addAttribute("projectId", id);
        model.addAttribute("userId", userDetails.getUsername()); // 로그인된 사용자 ID 전달
        return "fundingPage"; // 결제 페이지
    }

//    @GetMapping
//    public String getAllProjects(Model model) {
//        model.addAttribute("projects", projectService.getAllProjects());
//        return "projectList"; // 프로젝트 목록 페이지
//    }

    @GetMapping("/new")
    public String createProjectForm(Model model) {
        model.addAttribute("project", new ProjectDTO());
        return "project-create"; // 프로젝트 생성 페이지
    }

    @PostMapping("/new")
    public String createProject(
            @ModelAttribute ProjectDTO projectDTO,
            @RequestParam("image") MultipartFile imageFile,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {
    	  // 이미지 저장 디렉터리 설정
        String uploadDir = "src/main/resources/static/uploads/";
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        Path imagePath = Paths.get(uploadDir, fileName);
        Files.createDirectories(imagePath.getParent());
        Files.write(imagePath, imageFile.getBytes());
        projectDTO.setImagePath("/uploads/" + fileName);
        
        projectDTO.setCreatedBy(userDetails.getUsername());
        

        // 프로젝트 저장
        projectService.createProject(projectDTO);

        return "redirect:/projects"; // 목록 페이지로 리다이렉트
    }
    
    @GetMapping("/search")
    public String searchProjects(@RequestParam("title") String title, Model model) {
        List<ProjectDTO> projects = projectService.searchProjectsByTitle(title);
        model.addAttribute("projects", projects);
        model.addAttribute("searchQuery", title); // 검색어를 뷰에 전달
        return "projectList"; // 검색 결과 템플릿
    }
    
    @GetMapping
    public String getAllProjects(
            @RequestParam(defaultValue = "0") int page, // 페이지 번호
            @RequestParam(defaultValue = "10") int size, // 페이지 크기
            Model model
    ) {
        // 서비스 메서드 호출
        Page<ProjectDTO> projectPage = projectService.getProjectsWithPaging(page, size);

        // 모델에 데이터 추가
        model.addAttribute("projects", projectPage.getContent()); // 현재 페이지 데이터
        model.addAttribute("currentPage", projectPage.getNumber()); // 현재 페이지 번호
        model.addAttribute("totalPages", projectPage.getTotalPages()); // 총 페이지 수
        model.addAttribute("size", size); // 페이지 크기

        return "projectList"; // 프로젝트 목록 페이지 반환
    }



    @GetMapping("/{id}")
    public String getProjectDetails(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectService.getProjectById(id));
        return "projectDetails"; // 프로젝트 세부 정보 페이지
    }
    
    

}
