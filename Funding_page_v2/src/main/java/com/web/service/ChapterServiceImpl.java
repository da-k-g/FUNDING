package com.web.service;


import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.web.domain.Chapter;
import com.web.domain.Novel;
import com.web.domain.User;
import com.web.dto.ChapterDTO;
import com.web.repository.ChapterRepository;
import com.web.repository.NovelRepository;
import com.web.repository.UserRepository;



import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // final 키워드가 붙은 모든 필드를 포함하는 생성자를 자동으로 생성
@Transactional // 클래스 또는 메서드 수준에서 트랜잭션 처리를 활성화
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository; 
    
	private final NovelRepository novelRepository;

	private final UserRepository userRepository;
	

	// 소설 id로 회차 목록 조회
    @Override
    public List<Chapter> findByNovelId(Long novelId) {
        return chapterRepository.findByNovelId(novelId);
    }
    
    // 화차 id로 회차 조회
    @Override
    public Chapter findById(Long id) {
        return chapterRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid chapter Id: " + id));
    }

    // 새로운 회자 저장
    @Override
    public void save(Chapter chapter) {
        chapterRepository.save(chapter);
    }

    // 기존 회차 수정
    @Override
    public void update(Long id, Chapter updatedChapter) {
        Chapter existingChapter = chapterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chapter Id: " + id));

        existingChapter.setTitle(updatedChapter.getTitle());
        existingChapter.setContent(updatedChapter.getContent());
        existingChapter.setPaid(updatedChapter.isPaid());

        chapterRepository.save(existingChapter);
    }

    // 특정 회차 삭제
    @Override
    public void delete(Long id) {
        chapterRepository.deleteById(id);
    }

    // 소설 ID와 사용자 정보를 기반으로 새로운 회차 저장
	@Override
	public void save(Long novelId, ChapterDTO chapterDTO, User user) {
		
		Novel novel=novelRepository.findById(novelId).orElseThrow(EntityNotFoundException::new);
		novel.setUser(user);
		Chapter  chapter =ChapterDTO.toEntity(chapterDTO);
		chapter.setNovel(novel);	
		chapter.setViewCount(0);
		chapterRepository.save(chapter);
	}

	// 특정 회차 삭제
	@Override
	public void deleteChapter(Long chapterId) {
		Chapter  chapter=findById(chapterId);
		chapterRepository.delete(chapter);		
	}

	// 소설 ID에 해당하는 회차 목록을 페이지로 조회
	@Override
	public Page<Chapter> listChapters(Long novelId,  PageRequest pageRequest) {
		 // Specification 정의: novelId 기준 필터 추가
		 Specification<Chapter> spec = (root, query,criteriaBuilder)->
		 			criteriaBuilder.equal(root.get("novel").get("id"), novelId);
		 
		 
		// 정렬 조건 추가
	    Sort sort = Sort.by(Sort.Direction.ASC, "createDate"); // 기본값: 등록일
		
	    Pageable sortedPageRequest = PageRequest.of(
		        pageRequest.getPageNumber(),
		        pageRequest.getPageSize(),
		        sort
		 );
	    
		return chapterRepository.findAll(spec, sortedPageRequest); // 정렬, 페이징 처리된 회차 목록 반환
	}

	// 기존 회차 수정
	@Override
	public void modifyChapter(Novel novel, ChapterDTO chapterDTO, User user) {
		Chapter chapter=chapterRepository.findById(chapterDTO.getId()).orElseThrow(EntityNotFoundException::new);
		
		novel.setUser(user);
		chapter.setNovel(novel);
		
		
		//더티 체킹 업데이트
		chapter.setTitle(chapterDTO.getTitle());
		chapter.setPaid(chapterDTO.isPaid());
		chapter.setContent(chapterDTO.getContent());
		if(StringUtils.hasText(chapterDTO.getThumbnailImageUrl())) {
			chapter.setThumbnailImageUrl(chapterDTO.getThumbnailImageUrl());	
		}
		
	}

	
	// 특정 회차 상세 정보 조회 및 조회수 증가
	@Override
	public Chapter chapterDetail(Long chapterId) {
		Chapter  chapter =findById(chapterId);	
		//조회수 증가 처리
		chapter.setViewCount(chapter.getViewCount()+1);
		return chapter;
	}
	
	
	
	
	
	
}
