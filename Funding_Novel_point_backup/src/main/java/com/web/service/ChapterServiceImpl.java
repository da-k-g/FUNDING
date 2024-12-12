package com.web.service;

import com.web.domain.Chapter;
import com.web.repository.ChapterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;

    public ChapterServiceImpl(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    @Override
    public List<Chapter> findByNovelId(Long novelId) {
        return chapterRepository.findByNovelId(novelId);
    }

    @Override
    public Optional<Chapter> findById(Long id) {
        return chapterRepository.findById(id);
    }

    @Override
    public void save(Chapter chapter) {
        chapterRepository.save(chapter);
    }

    @Override
    public void update(Long id, Chapter updatedChapter) {
        Chapter existingChapter = chapterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chapter Id: " + id));

        existingChapter.setTitle(updatedChapter.getTitle());
        existingChapter.setContent(updatedChapter.getContent());
        existingChapter.setPaid(updatedChapter.isPaid());

        chapterRepository.save(existingChapter);
    }

    @Override
    public void delete(Long id) {
        chapterRepository.deleteById(id);
    }
}
