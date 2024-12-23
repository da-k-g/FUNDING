package com.web.service;

import com.web.domain.Chapter;
import com.web.domain.Novel;
import com.web.domain.User;
import com.web.dto.ChapterDTO;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
/**
 * Chapter 엔티티를 위한 서비스 인터페이스
 * - 회차 데이터를 관리하고 비즈니스 로직을 수행
 */
public interface ChapterService {
	// 특정 소설 id에 해당하는 모든 회차 조회
	List<Chapter> findByNovelId(Long novelId);
	// 특정 회차 조회
	Chapter findById(Long id);
	// 새로운 회차 저장
	void save(Chapter chapter);
	// 기존 회차 수정
	void update(Long id, Chapter updatedChapter);
	// 특정 회차 삭제
	void delete(Long id);
	// 소설 id와 사용자 정보로 새로운 회차 저장
	void save(Long novelId,  ChapterDTO chapterDTO, User user);
	// 특정 회차 삭제
	void deleteChapter(Long chapterId);
	// 특정 회차 목록을 페이지로 조회, 페이징 처리
	Page<Chapter> listChapters(Long novelId ,PageRequest pageable);
	// 기존 회차 수정
	void modifyChapter(Novel novel, ChapterDTO chapterDTO, User user);
	// 특정 회차 상세 정보 조회(자세히 보기)
	Chapter chapterDetail(Long chapterId);
}
