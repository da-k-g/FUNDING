package com.web.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    
    Optional<User> findByUsername(String username);
    
    boolean existsByUsername(String username);
}

