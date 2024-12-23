package com.web.service;



import com.web.domain.Project;
import com.web.domain.User;
import com.web.repository.ProjectRepository;
import com.web.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;


@Service
@Transactional
public class FundingServiceImpl implements FundingService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public FundingServiceImpl(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
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
    
    @Override
    @Transactional
    public void fundWithPoints(Long projectId, int amount, String email) {
        User user = userRepository.findByEmail(email);
        if (user == null || user.getPoints() < amount) {
            throw new IllegalArgumentException("포인트가 부족하거나 사용자를 찾을 수 없습니다.");
        }

        user.setPoints(user.getPoints() - amount);
        userRepository.save(user);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        project.setCurrentFunding(project.getCurrentFunding() + amount);
        projectRepository.save(project);
    }
}
    
    


