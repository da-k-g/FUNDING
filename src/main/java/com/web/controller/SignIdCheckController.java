package com.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.repository.UserRepository;

@RestController
public class SignIdCheckController {

	 private final UserRepository userRepository;

	    public SignIdCheckController(UserRepository userRepository) {
	        this.userRepository = userRepository;
	    }
	    
	    @GetMapping("/check-username")
	    public Map<String, Boolean> checkUsername(@RequestParam String username){
	    	boolean exists=userRepository.existsByUsername(username);
	    	Map<String, Boolean> response=new HashMap<>();
	    	response.put("exists", exists);
	    	return response;
	    }
}
