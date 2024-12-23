package com.web.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.web.domain.User;
import com.web.dto.UserDTO;

public interface UserService extends UserDetailsService {
  
	void saveUser(UserDTO userDTO);
	
	public User findByEmail(String email);

	public User findByUsername(String username);
    
}

