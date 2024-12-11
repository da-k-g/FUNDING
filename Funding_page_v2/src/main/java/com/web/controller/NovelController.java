package com.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.web.domain.Chapter;
import com.web.domain.Novel;
import com.web.domain.User;
import com.web.repository.ChapterRepository;
import com.web.repository.NovelRepository;
import com.web.repository.UserRepository;
import com.web.service.LikeService;

import jakarta.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/novels")
public class NovelController {

    @Autowired
    private NovelRepository novelRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Value("${file.upload-dir}") // 업로드 디렉토리 설정
    private String uploadDir;
    
    @Autowired
    private LikeService likeService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate; // WebSocket 메시지 전송을 위한 객체

    // 업로드 디렉토리 경로 보정
    private String getUploadDirectory() {
        return uploadDir.endsWith("\\") ? uploadDir : uploadDir + "\\";
    }

    // 소설 목록
    @GetMapping
    public String listNovels(@RequestParam(value = "category", required = false) String category, Model model) {
        if (category != null) {
            model.addAttribute("novels", novelRepository.findByCategoryIgnoreCase(category));
        } else {
            model.addAttribute("novels", novelRepository.findAll());
        }
        return "novels/list";
    }

    // 소설 등록 폼
    @GetMapping("/new")
    public String showWriteForm(Model model) {
        model.addAttribute("novel", new Novel());
        return "novels/write";
    }

//    // 소설 등록 처리
//    @PostMapping
//    public String createNovel(@ModelAttribute @Valid Novel novel,
//                              BindingResult bindingResult,
//                              @RequestParam("coverImage") MultipartFile coverImage,
//                              Model model) {
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("errors", bindingResult.getAllErrors());
//            return "novels/write";
//        }
//
//        try {
//            if (!coverImage.isEmpty()) {
//                // 파일 확장자 검증
//                String contentType = coverImage.getContentType();
//                if (contentType == null || !contentType.startsWith("image/")) {
//                    bindingResult.reject("fileTypeError", "이미지 파일만 업로드할 수 있습니다.");
//                    return "novels/write";
//                }
//
//                // 파일 저장
//                String fileName = UUID.randomUUID().toString() + "_" + coverImage.getOriginalFilename();
//                Path path = Paths.get(getUploadDirectory() + fileName);
//                Files.createDirectories(path.getParent()); // 경로 없으면 생성
//                Files.write(path, coverImage.getBytes()); // 파일 저장
//
//                // 파일 경로를 엔티티에 설정
//                novel.setCoverImage("C:\\LIMYUNJI\\image\\" + fileName); // 절대 경로로 설정
//            }
//
//            // 소설 데이터 저장
//            novelRepository.save(novel);
//        } catch (IOException e) {
//            bindingResult.reject("fileUploadError", "파일 업로드 중 문제가 발생했습니다.");
//            return "novels/write";
//        }
//
//        return "redirect:/novels";
//    }
    // 소설 등록 처리
    @PostMapping
    public String createNovel(@ModelAttribute @Valid Novel novel, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "novels/write";
        }

        novelRepository.save(novel);
        return "redirect:/novels";
    }

    // 소설 상세 보기
    @GetMapping("/{id}")
    public String viewNovel(@PathVariable Long id, Model model) {
        Novel novel = novelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid novel Id: " + id));
        model.addAttribute("novel", novel);
        model.addAttribute("freeChapters", chapterRepository.findByNovelAndIsPaid(novel, false));
        model.addAttribute("paidChapters", chapterRepository.findByNovelAndIsPaid(novel, true));
        return "novels/view";
    }
    
 // 소설 수정 폼
    @GetMapping("/modify/{id}")
    public String showmodifyForm(@PathVariable Long id, Model model) {
        Novel novel = novelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid novel Id: " + id));
        model.addAttribute("novel", novel);
        return "novels/modify";
    }

    // 소설 수정 처리
    @PostMapping("/modify/{id}")
    public String updateNovel(@PathVariable Long id, @ModelAttribute @Valid Novel updatedNovel, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "novels/modify";
        }

        Novel novel = novelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid novel Id: " + id));

        novel.setTitle(updatedNovel.getTitle());
        novel.setCategory(updatedNovel.getCategory());
        novel.setDescription(updatedNovel.getDescription());
        novel.setPaid(updatedNovel.isPaid());

        novelRepository.save(novel);
        return "redirect:/novels";
    }
    
    @PostMapping("/{id}/like/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addLike(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();

        if (userDetails == null) {
            response.put("error", "User not authenticated");
            return ResponseEntity.status(401).body(response);
        }

        User user = userRepository.findByEmail(userDetails.getUsername());
        if (user == null) {
            response.put("error", "User not found");
            return ResponseEntity.status(404).body(response);
        }

        try {
            likeService.addLike(user, id);
            long likeCount = likeService.getLikeCount(id);
            response.put("likeCount", likeCount);

            // WebSocket 메시지 전송
            messagingTemplate.convertAndSend("/topic/novels/" + id, likeCount);

            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(409).body(response);
        }
    }

    @PostMapping("/{id}/like/remove")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeLike(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();

        if (userDetails == null) {
            response.put("error", "User not authenticated");
            return ResponseEntity.status(401).body(response);
        }

        User user = userRepository.findByEmail(userDetails.getUsername());
        if (user == null) {
            response.put("error", "User not found");
            return ResponseEntity.status(404).body(response);
        }

        try {
            likeService.removeLike(user, id);
            long likeCount = likeService.getLikeCount(id);
            response.put("likeCount", likeCount);

            // WebSocket 메시지 전송
            messagingTemplate.convertAndSend("/topic/novels/" + id, likeCount);

            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(409).body(response);
        }
    }


    @GetMapping("/{id}/likeCount")
    @ResponseBody
    public Map<String, Object> getLikeCount(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        response.put("likeCount", likeService.getLikeCount(id));
        return response;
    }



 

    // 소설 삭제 처리
    @PostMapping("/delete/{id}")
    public String deleteNovel(@PathVariable Long id) {
        Novel novel = novelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid novel Id: " + id));
        novelRepository.delete(novel);
        return "redirect:/novels";
    }
    
}
