package com.web.service;

import com.web.dto.ProjectDTO;

import java.util.List;

public interface ProjectService {
    List<ProjectDTO> getAllProjects();
    ProjectDTO getProjectById(Long id);
    void createProject(ProjectDTO projectDTO);
    void fundProject(Long projectId, int amount);
}
