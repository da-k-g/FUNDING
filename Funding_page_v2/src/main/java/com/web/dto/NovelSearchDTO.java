package com.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NovelSearchDTO {

    private String title; // 검색할 소설 제목
    private String category; // 검색할 소설 카테고리
    private Boolean paid; // 무료/유료 여부 (true : 유료)
    private String author; // 검색할 작가 이름
    private String searchQuery = ""; // 검색 키워드(기본값: 빈 문자열)
    private String searchType = "all"; //검색유형 (기본값: 전체 검색)
    private String orderBy;   // 정렬 기준
}
