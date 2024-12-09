package com.web.repository;

import org.springframework.data.repository.CrudRepository;

import com.web.domain.Novel;

import java.util.List;

public interface NovelRepository extends CrudRepository<Novel, Long> {
    List<Novel> findByCategoryIgnoreCase(String category);
}

