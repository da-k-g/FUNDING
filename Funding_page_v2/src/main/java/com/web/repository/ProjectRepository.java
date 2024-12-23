package com.web.repository;

import com.web.domain.Project;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Project를 ID로 찾는 메서드는 JpaRepository에서 기본 제공
//	 @Query("SELECT p FROM Project p ORDER BY p.startDate DESC")
//	    List<Project> findAllByOrderByStartDateDesc();
	 @Query("SELECT p FROM Project p ORDER BY p.startDate ASC")
	    List<Project> findAllByOrderByStartDateAsc();
	 @Query("SELECT p FROM Project p WHERE p.title LIKE %:title% ORDER BY p.startDate ASC")
	    List<Project> findByTitleContaining(@Param("title") String title);
	 @Query("SELECT p FROM Project p WHERE LOWER(TRIM(p.title)) = LOWER(TRIM(:title))")
	 Optional<Project> findByTitleIgnoreCase(@Param("title") String title);


	 

}
