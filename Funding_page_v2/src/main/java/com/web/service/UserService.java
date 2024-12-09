package com.web.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.web.dto.UserDTO;

public interface UserService extends UserDetailsService {
    void saveUser(UserDTO userDTO);
}

