/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.lab9.controller;

import com.example.lab9.dto.UserDto;
import com.example.lab9.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author rytel
 */
@Controller
@CrossOrigin
public class RegisterController {

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model, @RequestParam(value = "usernameTaken", required = false) String usernameTaken) {
        model.addAttribute("user", new UserDto());
        model.addAttribute("usernameTaken", usernameTaken);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDto user, Model model) {
        try {
            // Check if the username is already taken
            if (userDetailsService.isUsernameTaken(user.getUsername())) {
                model.addAttribute("error", "Username is already taken. Please choose a different one.");
                return "redirect:/register?usernameTaken=true";
            }

            // Save the user
            userDetailsService.save(user);

            // Redirect to the login page with a success message
            return "redirect:/login?success=registered";
        } catch (Exception e) {
            // Redirect to the login page with an error message
            model.addAttribute("error", "Something went wrong. Please try again.");
            return "register";
        }
    }
}
