package com.web.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.web.domain.Novel;
import com.web.domain.User;
import com.web.dto.ChapterDTO;
import com.web.dto.NovelDTO;
import com.web.dto.NovelSearchDTO;
import com.web.utils.PageMaker;

import jakarta.validation.Valid;
/**
 * 소설 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 * - 소설의 CRUD 작업, 추천/비추천 관리, 검색 및 정렬 처리
 */
public interface NovelService {
	
	 /**
     * 모든 소설 목록 조회 (페이징 처리)
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 소설 목록
     */
	List<Novel> findAll(int page, int size);
	 /**
     * 특정 카테고리에 속한 소설 목록 조회 (페이징 처리)
     * @param category 소설 카테고리
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 해당 카테고리의 소설 목록
     */
	List<Novel> findByCategory(String category, int page, int size);
	// 특정 소설 id값으로 조회
	Novel findById(Long id);
	// 총 소설 개수 반환
	long getTotalCount();
	// 총 페이지 수 계산
	int getTotalPages(int size);
	// 새로운 소설 저장		소설정보DTO, 소설 작성자
	void save(NovelDTO novelDTO, User user);
	// 기존 소설 수정  수정할 소설id, 수정된 소설 객체
	void update(Long id, Novel updatedNovel);
	// 특정 소설 삭제
	void delete(Long id);
	// 소설의 추천 수 증가
	void addLikeCount(Long id);
	// 소설의 추천 수 감소
	void subLikeCount(Long id);
	// 소설 목록 조회
	Page<Novel> listNovels(NovelSearchDTO novelSearchDTO, PageRequest pageRequest);
	// 소설 상세보기
	NovelDTO viewNovel(Long id);
	// 소설 삭제
	void deleteNovel(Long id);
	// 소설 수정
	void updateNovel(@Valid NovelDTO novelDTO, User user);
	// 소설 조회
	NovelDTO findByIdNovelDTO(Long novelId);
	// 인기 소설 목록 조회
	Page<Novel> popularNovels(NovelSearchDTO searchDTO, PageRequest pageable);
	// 특정 소설 조회
	NovelDTO getNovelById(Long id);

}
