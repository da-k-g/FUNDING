package com.web.controller;

import com.web.dto.UserDTO;
import com.web.repository.UserRepository;
import com.web.service.UserService;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        userService.saveUser(userForm);
        return "redirect:/login";
    }
 
   
}
