package com.web.repository;

import com.web.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Project를 ID로 찾는 메서드는 JpaRepository에서 기본 제공
}
