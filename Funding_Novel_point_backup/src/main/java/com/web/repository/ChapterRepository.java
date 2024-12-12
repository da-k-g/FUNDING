package com.web.repository;

import org.springframework.data.repository.CrudRepository;

import com.web.domain.Chapter;
import com.web.domain.Novel;

import java.util.List;

public interface ChapterRepository extends CrudRepository<Chapter, Long> {
    List<Chapter> findByNovelAndIsPaid(Novel novel, boolean isPaid); // 유료/무료

    List<Chapter> findByNovelId(Long novelId);


	List<Chapter> findByNovel(Novel novel);
}
