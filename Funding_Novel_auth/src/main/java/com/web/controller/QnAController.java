package com.web.controller;

import com.web.domain.QnA;
import com.web.domain.User;
import com.web.repository.UserRepository;
import com.web.service.QnAService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/qna")
public class QnAController {

    private final QnAService qnaService;
    private final UserRepository userRepository;

    public QnAController(QnAService qnaService, UserRepository userRepository) {
        this.qnaService = qnaService;
        this.userRepository = userRepository;
    }

    // Q&A 목록 페이지 (페이징 추가 및 사용자 정보 처리)
    @GetMapping
    public String showQnAList(
            @RequestParam(defaultValue = "0") int page, // 기본 페이지 번호
            @RequestParam(defaultValue = "10") int size, // 기본 페이지 크기
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        Pageable pageable = PageRequest.of(page, size);
        Page<QnA> qnaPage = qnaService.findPaginated(pageable);

        model.addAttribute("qnaPage", qnaPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", qnaPage.getTotalPages());
        model.addAttribute("pageSize", size);

        if (userDetails != null) {
            model.addAttribute("currentUserEmail", userDetails.getUsername());
        }

        return "qna_list"; // templates/qna_list.html
    }

    @GetMapping("/new")
    public String showQnAForm() {
        return "qna_form";
    }

    @PostMapping
    public String createQnA(@RequestParam String title,
                            @RequestParam String content,
                            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        User user = userRepository.findByEmail(userDetails.getUsername());
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        QnA qna = new QnA();
        qna.setTitle(title);
        qna.setContent(content);
        qna.setAuthor(user);

        qnaService.saveQnA(qna);
        return "redirect:/qna";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        QnA qna = qnaService.findById(id);
        if (qna == null || !qna.getAuthor().getEmail().equals(userDetails.getUsername())) {
            return "redirect:/qna";
        }
        model.addAttribute("qna", qna);
        return "qna_edit_form";
    }

    @PostMapping("/update")
    public String updateQnA(@RequestParam Long id,
                            @RequestParam String title,
                            @RequestParam String content,
                            @AuthenticationPrincipal UserDetails userDetails) {
        QnA qna = qnaService.findById(id);
        if (qna != null && qna.getAuthor().getEmail().equals(userDetails.getUsername())) {
            qna.setTitle(title);
            qna.setContent(content);
            qnaService.saveQnA(qna);
        }
        return "redirect:/qna";
    }
    
    @GetMapping("/view")
    public String viewDetail(@RequestParam Long id, Model model) {
        QnA qna = qnaService.findById(id);
        if (qna == null) {
            return "redirect:/qna";
        }
        model.addAttribute("qna", qna);
        return "qna_view"; // qna_view.html 템플릿을 반환
    }
    
    @PostMapping("/answer")
    public String addAnswer(@RequestParam Long id, @RequestParam String answer) {
        QnA qna = qnaService.findById(id);
        if (qna != null) {
            qna.setAnswer(answer); // 답변 저장
            qnaService.saveQnA(qna); // 업데이트
        }
        return "redirect:/qna/view?id=" + id; // 상세 보기 페이지로 리다이렉트
    }


    @GetMapping("/delete")
    public String deleteQnA(@RequestParam Long id, @AuthenticationPrincipal UserDetails userDetails) {
        QnA qna = qnaService.findById(id);
        if (qna != null && qna.getAuthor().getEmail().equals(userDetails.getUsername())) {
            qnaService.deleteById(id);
        }
        return "redirect:/qna";
    }
}

