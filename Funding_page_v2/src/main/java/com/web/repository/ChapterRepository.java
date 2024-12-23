package com.web.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.domain.Chapter;
import com.web.domain.Novel;

import java.util.List;
/**
 * Chapter 엔티티를 위한 Repository 인터페이스
 * - CRUD 작업 및 특정 조건의 Chapter 데이터를 조회
 */
public interface ChapterRepository extends CrudRepository<Chapter, Long> ,JpaSpecificationExecutor<Chapter> {
	
	 /**
     * 특정 소설의 유료/무료 회차를 조회
     * @param novel 대상 소설 엔티티
     * @param isPaid 유료/무료 여부 (true: 유료, false: 무료)
     * @return 해당 조건의 Chapter 목록
     */
    List<Chapter> findByNovelAndIsPaid(Novel novel, boolean isPaid); // 유료/무료
    /**
     * 특정 소설 ID에 해당하는 모든 회차를 조회
     * @param novelId 소설 ID
     * @return 해당 소설의 Chapter 목록
     */
    List<Chapter> findByNovelId(Long novelId);
    /**
     * 특정 소설에 해당하는 모든 회차를 조회
     * @param novel 대상 소설 엔티티
     * @return 해당 소설의 Chapter 목록
     */
	List<Chapter> findByNovel(Novel novel);
}
