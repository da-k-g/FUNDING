package com.web.dto.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.web.domain.Novel;

/**
 * Novel 엔티티에 대한 동적 검색 조건을 생성하기 위한 클래스. JPA Specification을 사용하여 동적 쿼리를 생성.
 */
public class NovelSpecifications {
	/**
	 * 제목(title) 컬럼에서 특정 문자열을 포함하는 데이터를 검색.
	 * 
	 * @param title 검색할 제목 문자열.
	 * @return 제목이 지정된 문자열을 포함하는 조건의 Specification.
	 */
	public static Specification<Novel> titleContains(String title) {
		return (root, query, builder) -> builder.like(root.get("title"), "%" + title + "%");
	}

	/**
	 * 카테고리(category) 컬럼이 특정 값과 일치하는 데이터를 검색.
	 * 
	 * @param category 검색할 카테고리.
	 * @return 카테고리가 일치하는 조건의 Specification.
	 */
	public static Specification<Novel> categoryEquals(String category) {
		return (root, query, builder) -> builder.equal(root.get("category"), category);
	}

	/**
	 * 유료 여부(paid) 컬럼이 특정 값과 일치하는 데이터를 검색.
	 * 
	 * @param paid 유료 여부(Boolean).
	 * @return 유료 여부가 일치하는 조건의 Specification.
	 */
	public static Specification<Novel> isPaid(Boolean paid) {
		return (root, query, builder) -> builder.equal(root.get("paid"), paid);
	}

	/**
	 * 작가(author) 컬럼에서 특정 문자열을 포함하는 데이터를 검색.
	 * 
	 * @param author 검색할 작가 이름 문자열.
	 * @return 작가 이름이 지정된 문자열을 포함하는 조건의 Specification.
	 */
	public static Specification<Novel> authorContains(String author) {
		return (root, query, builder) -> builder.like(root.get("author"), "%" + author + "%");
	}

	/**
	 * 제목(title), 작가(author), 또는 카테고리(category) 컬럼에서 특정 문자열을 포함하는 데이터를 검색.
	 * 
	 * @param searchQuery 검색할 문자열.
	 * @return 제목, 작가, 카테고리 중 하나라도 검색 문자열을 포함하는 조건의 Specification.
	 */
	public static Specification<Novel> searchQueryContains(String searchQuery) {
		return (root, query, builder) -> builder.or(builder.like(root.get("title"), "%" + searchQuery + "%"),
				builder.like(root.get("author"), "%" + searchQuery + "%"),
				builder.like(root.get("category"), "%" + searchQuery + "%"));
	}

}
