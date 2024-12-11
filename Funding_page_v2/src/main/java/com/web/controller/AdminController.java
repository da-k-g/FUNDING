package com.web.controller;

import com.web.domain.User;
import com.web.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 관리자 대시보드
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<User> users = userRepository.findAll();  // 전체 사용자 목록
        model.addAttribute("users", users);
        return "admin-dashboard";
    }

    // 사용자 삭제
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/dashboard";
    }
}
