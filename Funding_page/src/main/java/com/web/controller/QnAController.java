package com.web.controller;

import com.web.domain.QnA;
import com.web.domain.User;
import com.web.repository.UserRepository;
import com.web.service.QnAService;

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

    @GetMapping
    public String showQnAList(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<QnA> qnaList = qnaService.findAll();
        model.addAttribute("qnaList", qnaList);
        if (userDetails != null) {
            model.addAttribute("currentUserEmail", userDetails.getUsername());
        }
        return "qna_list";
    }

    @GetMapping("/new")
    public String showQnAForm() {
        return "qna_form";
    }

    @PostMapping
    public String createQnA(@RequestParam String title,
                            @RequestParam String content,
                            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername());
        if (user == null) {
            throw new RuntimeException("User not found");
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

    @GetMapping("/delete")
    public String deleteQnA(@RequestParam Long id, @AuthenticationPrincipal UserDetails userDetails) {
        QnA qna = qnaService.findById(id);
        if (qna != null && qna.getAuthor().getEmail().equals(userDetails.getUsername())) {
            qnaService.deleteById(id);
        }
        return "redirect:/qna";
    }
}
