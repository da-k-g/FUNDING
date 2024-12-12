package com.web.service;

import com.web.domain.Novel;

import java.util.List;
import java.util.Optional;

public interface NovelService {
    List<Novel> findAll(int page, int size);
    List<Novel> findByCategory(String category, int page, int size);
    Optional<Novel> findById(Long id);
    long getTotalCount();
    int getTotalPages(int size);
    void save(Novel novel);
    void update(Long id, Novel updatedNovel);
    void delete(Long id);
    void addLikeCount(Long id);
    void subLikeCount(Long id);
}

