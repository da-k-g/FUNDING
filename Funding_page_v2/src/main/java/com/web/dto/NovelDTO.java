package com.web.dto;

import org.springframework.web.multipart.MultipartFile;

import com.web.domain.Novel;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * 소설(Novel) 정보를 전송하기 위한 DTO 클래스
 * - 데이터 전송 계층에서 사용
 * - 유효성 검증 및 엔티티 변환 기능 포함
 */
@Data
@Builder
public class NovelDTO {

    private Long id; // 소설 id

    @NotNull // 유효성 검증: NULL을 허용하지 않음
    private String title; // 제목

    @NotNull
    private String category; // 소설 카테고리

    @NotNull
    private String author; // 작가

    private String description; // 소설 설명(줄거리)

    private MultipartFile coverImage; // 소설 표지 이미지 파일 (업로드용)
    
    private String coverImageUrl; // 소설 이미지 URL (저장된 경로)
    
    private Integer likeCount; // 좋아요 개수

    private Integer viewCount; // 조회수

    private boolean paid; // 무료/유료 여부 (true = 유료)

    
    // NovelDTO -> Novel 엔티티 변환
    public static Novel toEntity(NovelDTO novelDTO) {
        return Novel.builder()
                .id(novelDTO.getId())
                .title(novelDTO.getTitle())
                .author(novelDTO.getAuthor())
                .category(novelDTO.getCategory())
                .description(novelDTO.getDescription())
                .coverImageUrl(novelDTO.getCoverImageUrl())
                .viewCount(novelDTO.getViewCount())
                .paid(novelDTO.isPaid())
                .build();
    }

    
    
    // Novel 엔티티 -> NovelDTO 변환
    public static NovelDTO toDto(Novel novel) {
        return NovelDTO.builder()
                .id(novel.getId())
                .title(novel.getTitle())
                .author(novel.getAuthor())
                .category(novel.getCategory())
                .description(novel.getDescription())
                .coverImageUrl(novel.getCoverImageUrl())
                .viewCount(novel.getViewCount())
                .paid(novel.isPaid())
                .build();
    }
}
