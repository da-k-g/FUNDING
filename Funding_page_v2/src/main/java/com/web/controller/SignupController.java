package com.web.controller;

import com.web.dto.UserDTO;
import com.web.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("userForm", new UserDTO());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(@Valid @ModelAttribute("userForm") UserDTO userForm,
                               BindingResult bindingResult) {
             
        if(userService.findByUsername(userForm.getUsername())!=null) {
        	bindingResult.rejectValue("username", "error.userForm", "이미 등록된 아이디 입니다.");
        	return "signup";
        }
        
        if(userService.findByEmail(userForm.getEmail())!=null) {
        	bindingResult.rejectValue("email", "error.userForm", "이미 등록된 이메일 입니다.");
        	return "signup";
        }
        
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        
        userService.saveUser(userForm);
        return "redirect:/login";
    }
    
    
    
    
    
}
