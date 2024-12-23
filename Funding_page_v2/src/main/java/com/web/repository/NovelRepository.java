package com.web.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.domain.Novel;

import java.util.List;
/**
 * Novel 엔티티를 위한 Repository 인터페이스
 * - 소설 데이터를 CRUD 및 검색 기준에 따라 관리
 */
public interface NovelRepository extends CrudRepository<Novel, Long>  ,JpaSpecificationExecutor<Novel> {
	 /**
     * 카테고리 기준으로 소설 목록 조회 (대소문자 구분 없이)
     * @param category 검색할 카테고리
     * @return 해당 카테고리에 해당하는 소설 목록
     */
	List<Novel> findByCategoryIgnoreCase(String category);
}
