package com.web.controller;

import com.web.dto.ProjectDTO;
import com.web.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final ProjectService projectService;

    public DashboardController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // 프로젝트 데이터 가져오기
        List<ProjectDTO> projects = projectService.getAllProjects();

        // 8개 프로젝트 표시
        List<ProjectDTO> recentProjects = projects.stream().limit(8).toList();

        model.addAttribute("projects", recentProjects); // 대시보드에 전달
        return "dashboard";
    }
}
