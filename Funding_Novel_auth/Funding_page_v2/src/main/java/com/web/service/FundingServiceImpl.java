package com.web.service;



import com.web.domain.Project;
import com.web.repository.ProjectRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;


@Service
@Transactional
public class FundingServiceImpl implements FundingService {

    private final ProjectRepository projectRepository;

    public FundingServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    @Transactional
    public void fundProject(Long projectId, int amount) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        int updatedFunding = project.getCurrentFunding() + amount;
        project.setCurrentFunding(updatedFunding); // 업데이트된 값 설정
        projectRepository.save(project); // 데이터베이스에 저장
    }

}
