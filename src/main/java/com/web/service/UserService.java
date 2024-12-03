package com.web.service;



import com.web.domain.User;
import com.web.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 사용자 생성
    public User createUser(String userId, String username, String email, String role) {
        User user = new User();
        user.setUserId(userId);
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(role);
        user.setCreatedAt(LocalDate.now());

        return userRepository.save(user);
    }

    // 사용자 조회 (ID로 조회)
    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    // 사용자 조회 (이메일로 조회)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
