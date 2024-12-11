package com.web.controller;



import com.web.domain.User;
import com.web.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 사용자 생성
    @PostMapping("/create")
    public ResponseEntity<User> createUser(
            @RequestParam String userId,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String role) {
        User user = userService.createUser(userId, username, email, role);
        return ResponseEntity.ok(user);
    }

    // 사용자 조회 (ID로 조회)
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        Optional<User> user = userService.getUserById(userId);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 사용자 조회 (이메일로 조회)
    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }
}

