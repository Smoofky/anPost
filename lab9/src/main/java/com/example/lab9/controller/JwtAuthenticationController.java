/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.lab9.controller;

import com.example.lab9.config.JwtTokenUtil;
import com.example.lab9.dto.UserDto;
import com.example.lab9.service.JwtUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public String createAuthenticationToken(@ModelAttribute("user") UserDto user,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes, Model model) {
        try {
            if (authenticate(user.getUsername(), user.getPassword())) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
                String token = jwtTokenUtil.generateToken(userDetails);

                // Zapisz token w sesji
                HttpSession session = request.getSession();
                session.setAttribute("token", token);
                System.out.println("Token zapisany w sesji: " + token);
                System.out.println(session.getAttribute("token"));
                // Redirect do strony "/hello"
                return "redirect:/hello";
            } else {
                // Redirect do strony logowania z komunikatem błędu
                model.addAttribute("error", "Login or password is incorrect.");
                return "redirect:/login";
            }
        } catch (UsernameNotFoundException e) {
            // Obsługa wyjątków
            return "redirect:/login";
        }
    }

    private boolean authenticate(String username, String password) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Check if the user is disabled
            if (!userDetails.isEnabled()) {
                return false;
            }

            // Check if the provided password matches the stored hashed password
            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                return true;
            } else {
                return false;
            }
        } catch (AuthenticationException e) {
            // Handle other exceptions if needed
            return false;
        }
    }
}
