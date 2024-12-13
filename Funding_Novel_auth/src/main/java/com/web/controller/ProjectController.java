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

    // 기존 결제 방식 + 포인트 지원 추가
    @PostMapping("/{id}/fund")
    public String fundProject(
            @PathVariable Long id,
            @RequestParam int amount,
            @RequestParam String method, // 결제 방식 (payment 또는 points)
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if ("points".equalsIgnoreCase(method)) {
            // 포인트로 펀딩 처리
            projectService.fundWithPoints(id, amount, userDetails.getUsername());
        } else if ("payment".equalsIgnoreCase(method)) {
            // 일반 결제 처리
            paymentService.processPayment(id, amount);
            projectService.fundProject(id, amount);
        } else {
            throw new IllegalArgumentException("잘못된 결제 방식입니다.");
        }

        return "redirect:/projects/" + id;
    }

    // 결제 및 포인트 펀딩 방식 선택 페이지
    @GetMapping("/{id}/fund")
    public String fundProjectForm(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails, // 로그인된 사용자 정보 가져오기
            Model model) {
        model.addAttribute("projectId", id);
        model.addAttribute("userId", userDetails.getUsername()); // 로그인된 사용자 ID 전달
        model.addAttribute("project", projectService.getProjectById(id)); // 프로젝트 정보 전달
        return "fundingPage"; // 결제 및 포인트 선택 페이지
    }

    // 프로젝트 생성 관련 메서드 (기존 유지)
    @GetMapping("/new")
    public String createProjectForm(Model model) {
        model.addAttribute("project", new ProjectDTO());
        return "project-create";
    }

    @PostMapping("/new")
    public String createProject(
            @ModelAttribute ProjectDTO projectDTO,
            @RequestParam("image") MultipartFile imageFile,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        String uploadDir = "src/main/resources/static/uploads/";
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        Path imagePath = Paths.get(uploadDir, fileName);
        Files.createDirectories(imagePath.getParent());
        Files.write(imagePath, imageFile.getBytes());
        projectDTO.setImagePath("/uploads/" + fileName);
        projectDTO.setCreatedBy(userDetails.getUsername());

        projectService.createProject(projectDTO);

        return "redirect:/projects";
    }

    // 검색 및 목록 관련 메서드 (기존 유지)
    @GetMapping("/search")
    public String searchProjects(@RequestParam("title") String title, Model model) {
        List<ProjectDTO> projects = projectService.searchProjectsByTitle(title);
        model.addAttribute("projects", projects);
        model.addAttribute("searchQuery", title);
        return "projectList";
    }

    @GetMapping
    public String getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<ProjectDTO> projectPage = projectService.getProjectsWithPaging(page, size);
        model.addAttribute("projects", projectPage.getContent());
        model.addAttribute("currentPage", projectPage.getNumber());
        model.addAttribute("totalPages", projectPage.getTotalPages());
        model.addAttribute("size", size);

        return "projectList";
    }

    @GetMapping("/{id}")
    public String getProjectDetails(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectService.getProjectById(id));
        return "projectDetails";
    }
}

    
    


