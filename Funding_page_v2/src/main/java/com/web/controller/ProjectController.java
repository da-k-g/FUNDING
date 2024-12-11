package com.web.controller;


import com.web.dto.ProjectDTO;
import com.web.service.PaymentService;
import com.web.service.ProjectService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public String getAllProjects(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        return "projectList"; // 프로젝트 목록 페이지
    }

    @GetMapping("/new")
    public String createProjectForm(Model model) {
        model.addAttribute("project", new ProjectDTO());
        return "project-create"; // 프로젝트 생성 페이지 (파일명 수정)
    }

    @PostMapping("/new")
    public String createProject(@ModelAttribute ProjectDTO projectDTO) {
        projectService.createProject(projectDTO);
        return "redirect:/projects"; // 목록 페이지로 리다이렉트
    }

    @GetMapping("/{id}")
    public String getProjectDetails(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectService.getProjectById(id));
        return "projectDetails"; // 프로젝트 세부 정보 페이지
    }
    
    // 결제 폼 렌더링 (GET 요청)
//    @GetMapping("/{id}/fund")
//    public String fundProjectForm(@PathVariable Long id, Model model) {
//        model.addAttribute("projectId", id);
//        
//        return "fundingPage"; // 결제 페이지
//    }



//    @GetMapping("/{id}/fund")
//    public String fundProjectForm(@PathVariable Long id, Model model) {
//        model.addAttribute("projectId", id);
//        return "fundingPage"; // 결제 페이지
//    }

    
}


