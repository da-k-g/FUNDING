package com.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.web.domain.Project;
import com.web.service.ProjectService;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * 프로젝트 목록 페이지 조회
     */
    @GetMapping
    public String getAllProjects(Model model) {
        // 모든 프로젝트 가져오기
        List<Project> projects = projectService.getAllProjects();

        // Thymeleaf에 전달할 데이터 설정
        model.addAttribute("projects", projects);

        return "project-list"; // 프로젝트 목록 템플릿
    }

    /**
     * 특정 프로젝트 상세 페이지 조회
     */
    @GetMapping("/{projectId}")
    public String getProjectDetails(@PathVariable String projectId, Model model) {
        // 프로젝트 정보 가져오기
        Project project = projectService.getProjectById(projectId)
                .orElseThrow(() -> new RuntimeException("프로젝트를 찾을 수 없습니다."));

        // Thymeleaf에 전달할 데이터 설정
        model.addAttribute("project", project);

        return "project-details"; // 프로젝트 상세 템플릿
    }

    /**
     * 새 프로젝트 생성 페이지
     */
    @GetMapping("/create")
    public String createProjectPage() {
        return "project-create"; // 새 프로젝트 등록 템플릿
    }

    /**
     * 새 프로젝트 생성 처리
     */
    @PostMapping("/create")
    public String createProject(
            @RequestParam String title,
            @RequestParam int goal,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            // 새 프로젝트 생성
            Project project = new Project();
            project.setProjectId("P-" + System.currentTimeMillis()); // 고유 ID 생성
            project.setTitle(title);
            project.setGoal(goal);
            project.setCurrent(0); // 초기 금액 설정
            project.setStartDate(LocalDate.parse(startDate));
            project.setEndDate(LocalDate.parse(endDate));
            project.setStatus("진행 중"); // 초기 상태

            projectService.createProject(project);

            // 성공 시 프로젝트 목록 페이지로 리다이렉트
            return "redirect:/projects";

        } catch (Exception e) {
            // 에러 처리 시 에러 페이지로 이동
            return "redirect:/projects/error";
        }
    }

    /**
     * 프로젝트 에러 페이지
     */
    @GetMapping("/error")
    public String errorPage(Model model) {
        model.addAttribute("message", "프로젝트 생성 중 오류가 발생했습니다. 다시 시도해주세요.");
        return "error-page"; // 에러 템플릿
    }
}


