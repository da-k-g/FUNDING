package com.web.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.domain.Novel;
import com.web.domain.User;
import com.web.dto.NovelDTO;
import com.web.dto.NovelSearchDTO;
import com.web.dto.specifications.NovelSpecifications;
import com.web.repository.NovelRepository;

import jakarta.validation.Valid;
/**
 * 소설 데이터를 관리하는 서비스 구현체
 * - 소설 CRUD, 검색, 정렬, 추천/비추천, 페이징 등의 기능 포함
 */
@Service
@Transactional // // 클래스 또는 메서드 수준에서 트랜잭션 처리를 활성화
public class NovelServiceImpl implements NovelService {

	private final NovelRepository novelRepository; // 소설 데이터 관리

	
	// 생성자
	public NovelServiceImpl(NovelRepository novelRepository) {
		this.novelRepository = novelRepository;
	}
	
	// 좋아요 수 증가
    @Override
    public void addLikeCount(Long id) {
        Novel novel = novelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid novel Id: " + id));
      //  novel.setLikeCount(novel.getLikeCount() + 1);
        novelRepository.save(novel);
    }

    // 좋아요 수 감소
    @Override
    public void subLikeCount(Long id) {
        Novel novel = novelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid novel Id: " + id));
    }
    
    // 모든 소설 목록 조회
    @Transactional(readOnly = true) // 데이터의 읽기 작업에 최적화되어 있으며, 데이터 변경 작업(INSERT, UPDATE, DELETE)이 수행되지 않도록 보장
	@Override
	public List<Novel> findAll(int page, int size) {
		List<Novel> novels = StreamSupport.stream(novelRepository.findAll().spliterator(), false)
				.collect(Collectors.toList());
		return paginate(novels, page, size);
	}

    // 특정 카테고리의 소설 목록 조회 (페이징 처리)
    @Transactional(readOnly = true) // 데이터의 읽기 작업에 최적화되어 있으며, 데이터 변경 작업(INSERT, UPDATE, DELETE)이 수행되지 않도록 보장
	@Override
	public List<Novel> findByCategory(String category, int page, int size) {
		List<Novel> novels = StreamSupport
				.stream(novelRepository.findByCategoryIgnoreCase(category).spliterator(), false)
				.collect(Collectors.toList());
		return paginate(novels, page, size);
	}

    
    // 소설 총 개수 반환
    @Transactional(readOnly = true) // 데이터의 읽기 작업에 최적화되어 있으며, 데이터 변경 작업(INSERT, UPDATE, DELETE)이 수행되지 않도록 보장
	@Override
	public long getTotalCount() {
		return novelRepository.count();
	}

    // 총 페이지 수 계산
    @Transactional(readOnly = true) // 데이터의 읽기 작업에 최적화되어 있으며, 데이터 변경 작업(INSERT, UPDATE, DELETE)이 수행되지 않도록 보장
	@Override
	public int getTotalPages(int size) {
		return (int) Math.ceil((double) novelRepository.count() / size);
	}
    // 새로운 소설 저장
	@Override
	public void save(NovelDTO novelDTO, User user) {	
		Novel novel =NovelDTO.toEntity(novelDTO);
		novel.setUser(user);
		novelRepository.save(novel);
	}
	
	// 기존 소설 수정
	 @Override
    public void update(Long id, Novel updatedNovel) {
        Novel novel = novelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid novel Id: " + id));
        // 수정 가능한 필드 업데이트
        novel.setTitle(updatedNovel.getTitle());
        novel.setCategory(updatedNovel.getCategory());
        novel.setDescription(updatedNovel.getDescription());
        novelRepository.save(novel);
    }
	// 특정 소설 삭제
    @Override
    public void delete(Long id) {
        novelRepository.deleteById(id);
    }

	    
	// 소설 목록 페이징 처리
	private List<Novel> paginate(List<Novel> novels, int page, int size) {
		int fromIndex = Math.min(page * size, novels.size());
		int toIndex = Math.min((page + 1) * size, novels.size());
		return novels.subList(fromIndex, toIndex);
	}

	// 소설 검색 및 정렬
	@Transactional(readOnly = true) // 데이터의 읽기 작업에 최적화되어 있으며, 데이터 변경 작업(INSERT, UPDATE, DELETE)이 수행되지 않도록 보장
	@Override
    public Novel findById(Long id) {
		return novelRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid novel Id: " + id));
    }
	


	/**
	 * 검색처리 및 정렬
	 */
	@Override
	public Page<Novel> listNovels(NovelSearchDTO searchDTO, PageRequest pageRequest) {
	    Specification<Novel> spec = Specification.where(null);

	    // 검색 조건 추가
	    if (searchDTO.getSearchType() != null) {
	        switch (searchDTO.getSearchType()) {
	            case "title":
	                spec = spec.and(NovelSpecifications.titleContains(searchDTO.getSearchQuery()));
	                break;
	            case "author":
	                spec = spec.and(NovelSpecifications.authorContains(searchDTO.getSearchQuery()));
	                break;
	            case "category":
	                spec = spec.and(NovelSpecifications.categoryEquals(searchDTO.getSearchQuery()));
	                break;
	            case "all":
	            default:
	                spec = spec.and(NovelSpecifications.searchQueryContains(searchDTO.getSearchQuery()));
	                break;
	        }
	    }

	 // 정렬 조건 추가
	    Sort sort = Sort.by(Sort.Direction.DESC, "createDate"); // 기본값: 등록일순
	    if ("likeScoreDesc".equals(searchDTO.getOrderBy())) {
	        sort = Sort.by(Sort.Direction.DESC, "likeScore");
	    } else if ("viewCountDesc".equals(searchDTO.getOrderBy())) {
	        sort = Sort.by(Sort.Direction.DESC, "viewCount");
	    }

	    Pageable sortedPageRequest = PageRequest.of(
	        pageRequest.getPageNumber(),
	        pageRequest.getPageSize(),
	        sort
	    );
	    
	    return novelRepository.findAll(spec, sortedPageRequest);
	}


	
	@Override
	public NovelDTO viewNovel(Long id) {
		// 소설 엔티티를 id로 조회
		Novel  novel= novelRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid novel Id: " + id));
		//조회수 증가처리
		novel.setViewCount(novel.getViewCount()+1);				
		return NovelDTO.toDto(novel); // 엔티티를 DTO로 변환하여 반환
	}
	

	// 소설 삭제
	@Override
	public void deleteNovel(Long id) {
		// 소설 엔티티를 ID로 조회
		Novel novel=novelRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid novel Id: " + id));
		novelRepository.delete(novel); // 엔티티 삭제		
	}

	
	// 소설 수정	
	@Transactional
	@Override
	public void updateNovel(@Valid NovelDTO novelDTO, User user) {
	    // 엔티티 id로 조회
	    Novel novel = novelRepository.findById(novelDTO.getId())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid novel Id: " + novelDTO.getId()));

	    // 작성자 정보 설정
	    novel.setUser(user);
	    // 업데이트할 필드 설정
	    if (novelDTO.getTitle() != null) {
	        novel.setTitle(novelDTO.getTitle());
	    }

	    if (novelDTO.getAuthor() != null) {
	        novel.setAuthor(novelDTO.getAuthor());
	    }
	    
	    if (novelDTO.getDescription() != null) {
	        novel.setDescription(novelDTO.getDescription());
	    }

	    if (novelDTO.getCategory() != null) {
	        novel.setCategory(novelDTO.getCategory());
	    }

	    if (novelDTO.getCoverImageUrl() != null) {
	        novel.setCoverImageUrl(novelDTO.getCoverImageUrl());
	    }	    	   
	    novel.setPaid(novelDTO.isPaid());
	}

	// 소설 ID로 소설 조회 및 DTO 반환
	@Override
	public NovelDTO findByIdNovelDTO(Long novelId) {
		Novel  novel= novelRepository.findById(novelId).orElseThrow(() -> new IllegalArgumentException("Invalid novel Id: " + novelId));		
		return NovelDTO.toDto(novel);
	}
	
	
	/**
	 * 인기작품순
	 */
	@Override
	public Page<Novel> popularNovels(NovelSearchDTO searchDTO, PageRequest pageRequest) {
		Specification<Novel> spec = Specification.where(null);
		  
		 // 정렬 조건 추가
	    Sort sort = Sort.by(Sort.Direction.DESC, "createDate"); // 기본값: 등록일순
	    
	    if ("likeScoreDesc".equals(searchDTO.getOrderBy())) {	        
	    	//좋아요순
	    	sort = Sort.by(Sort.Direction.DESC, "likeScore");
	        
	     	
	    } else if ("viewCountDesc".equals(searchDTO.getOrderBy())) {
	    	//조회순
	        sort = Sort.by(Sort.Direction.DESC, "viewCount");
	        
	    }else if("createDateDesc".equals(searchDTO.getOrderBy())) {
	    	//신작순
	    	
	    	sort = Sort.by(Sort.Direction.DESC, "createDate");
	    }
	    // 페이징 정보와 정렬 조건 결합
	    Pageable sortedPageRequest = PageRequest.of(
	        pageRequest.getPageNumber(),
	        pageRequest.getPageSize(),
	        sort
	    );
	    // 검색 결과 반환
	    return novelRepository.findAll(spec, sortedPageRequest);
	}
	
	/**
	 * 소설 ID로 소설 조회 및 DTO 반환 (읽기 전용)
	 * @param id 소설 ID
	 * @return 소설 DTO
	 */
	@Override
	@Transactional(readOnly = true)
	public NovelDTO getNovelById(Long id) {
	    Novel novel = novelRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Invalid novel Id: " + id));
	    return NovelDTO.toDto(novel); // 엔티티를 DTO로 변환하여 반환
	}

	
	
	
}
