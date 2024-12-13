package com.web.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.web.domain.Chapter;
import com.web.domain.Novel;
import com.web.repository.ChapterRepository;
import com.web.repository.NovelRepository;

@Controller
@RequestMapping("/chapters")
public class ChapterController {

    @Autowired
    private NovelRepository novelRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    // 회차 등록 폼
    @GetMapping("/{novelId}/new")
    public String showCreateChapterForm(@PathVariable Long novelId, Model model) {
        Novel novel = novelRepository.findById(novelId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid novel Id: " + novelId));
        Chapter chapter = new Chapter();
        chapter.setNovel(novel);

        model.addAttribute("chapter", chapter);
        return "chapters/create"; // 회차 등록 폼 템플릿
    }

    // 회차 등록 처리
 // 회차 등록 처리
    @PostMapping("/{novelId}/new")
    public String createChapter(@PathVariable Long novelId, @ModelAttribute Chapter chapter) {
        Novel novel = novelRepository.findById(novelId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid novel Id: " + novelId));
        chapter.setNovel(novel);
        chapterRepository.save(chapter); // 회차 저장
        return "redirect:/chapters/" + novelId; // 회차 목록 페이지로 리다이렉트
    }


    // 회차 목록 보기
    @GetMapping("/{novelId}")
    public String listChapters(@PathVariable Long novelId, Model model) {
        Novel novel = novelRepository.findById(novelId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid novel Id: " + novelId));
        List<Chapter> chapters = chapterRepository.findByNovel(novel);
        model.addAttribute("novel", novel);
        model.addAttribute("chapters", chapters != null ? chapters : new ArrayList<>());
        return "chapters/list";
    }


    // 회차 수정 폼
    @GetMapping("/modify/{chapterId}")
    public String showmodifyChapterForm(@PathVariable Long chapterId, Model model) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chapter Id: " + chapterId));
        model.addAttribute("chapter", chapter);
        return "chapters/modify"; // 회차 수정 폼 템플릿
    }

    // 회차 수정 처리
    @PostMapping("/modify/{chapterId}")
    public String modifyChapter(@PathVariable Long chapterId, @ModelAttribute Chapter updatedChapter) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chapter Id: " + chapterId));
        chapter.setTitle(updatedChapter.getTitle());
        chapter.setContent(updatedChapter.getContent());
        chapter.setPaid(updatedChapter.isPaid());
        chapterRepository.save(chapter); // 회차 수정 저장
        return "redirect:/chapters/" + chapter.getNovel().getId();
    }

    // 회차 삭제 처리
    @PostMapping("/delete/{chapterId}")
    public String deleteChapter(@PathVariable Long chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chapter Id: " + chapterId));
        Long novelId = chapter.getNovel().getId();
        chapterRepository.delete(chapter); // 회차 삭제
        return "redirect:/chapters/" + novelId;
    }
}
