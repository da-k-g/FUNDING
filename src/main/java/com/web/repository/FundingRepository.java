package com.web.repository;

import com.web.domain.Funding;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FundingRepository extends JpaRepository<Funding, Long> {
    List<Funding> findByProject_ProjectId(Long projectId); // 수정된 쿼리 메서드
}
