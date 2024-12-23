package com.web.dto;

import org.springframework.web.multipart.MultipartFile;

import com.web.domain.Chapter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

/**
 * 회차(Chapter) 정보를 전송하기 위한 DTO 클래스
 * - 데이터 전송 계층에서 사용 
 * - 유효성 검증과 엔티티 변환 기능 포함
 */
@Data // Lombok: Getter, Setter, toString, equals, hashCode 생성
@Builder // Builder 패턴 생성
public class ChapterDTO {
	
	private Long id; // 회차 id
	
	private Long novelId; // 소설 id
	
	@NotBlank(message = "제목은 필수 입력 값입니다.") // 유효성 검사: 빈 값 허용 안 함
	@Size(max = 255, message = "제목은 최대 255자까지 입력할 수 있습니다.") // 유효성 검사: 최대 길이 255자
	private String title; // 회차 제목
	
    @NotBlank(message = "내용은 필수 입력 값입니다.") // 유효성 검사: 빈 값 허용 안 함
	private String content; // 회차 내용
	
	private String thumbnailImageUrl; // 회차 썸네일 이미지 URL
	 
    private MultipartFile coverImage;  // 회차 표지 이미지 파일
	
    private Integer viewCount; // 조회수
    
    private boolean paid; // 무료/유료 여부 true = 유료
    
    
    // ChapterDTO -> Chapter 엔티티로 변환
    public static Chapter toEntity(ChapterDTO chapterDTO) {
        return Chapter.builder()
                .id(chapterDTO.getId())
                .title(chapterDTO.getTitle())
                .content(chapterDTO.getContent())
                .thumbnailImageUrl(chapterDTO.getThumbnailImageUrl())
                .viewCount(chapterDTO.getViewCount())
                .isPaid(chapterDTO.isPaid())
                .novel(null) // Novel 객체 설정은 별도 처리
                .build();
    }

    // Chapter 엔티티 -> ChapterDTO 변환
    public static ChapterDTO toDto(Chapter chapter) {
        return ChapterDTO.builder()
                .id(chapter.getId())
                .novelId(chapter.getNovel() != null ? chapter.getNovel().getId() : null)
                .title(chapter.getTitle())
                .content(chapter.getContent())
                .thumbnailImageUrl(chapter.getThumbnailImageUrl())                
                .viewCount(chapter.getViewCount())
                .paid(chapter.isPaid())
                .build();
    }
}


