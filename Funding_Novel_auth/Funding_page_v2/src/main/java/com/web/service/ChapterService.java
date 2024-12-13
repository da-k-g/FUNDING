package com.web.service;

import com.web.domain.Chapter;

import java.util.List;
import java.util.Optional;

public interface ChapterService {
    List<Chapter> findByNovelId(Long novelId);

    Optional<Chapter> findById(Long id);

    void save(Chapter chapter);

    void update(Long id, Chapter updatedChapter);

    void delete(Long id);
}
