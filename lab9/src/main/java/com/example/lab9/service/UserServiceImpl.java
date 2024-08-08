/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.lab9.service;

import com.example.lab9.dto.UserDto;
import com.example.lab9.model.User;
import com.example.lab9.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void saveUser(User userDto) {
        if (isUsernameTaken(userDto.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(bcryptEncoder.encode(userDto.getPassword()));
        
        userRepository.save(user);
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username) != null;
    }

    @Override
    public UserDto getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return mapUserToUserDto(user);
        } else {
            return null;
        }
    }
    @Override
    public UserDto getUserDtoByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user != null) {
            return mapUserToUserDto(user);
        } else {
            return null;
        }
    }
    private UserDto mapUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setImageUrl(user.getImageUrl());
        userDto.setUserId(user.getId());
        return userDto;
    }

    @Override
    public long countPostsByUsername(String username) {
        return userRepository.countPostsByUsername(username);
    }

    @Override
    public long countCommentsByUsername(String username) {
        return userRepository.countCommentsByUsername(username);
    }
    @Override
    public long countLikesByUsername(String username) {
        return userRepository.countLikesByUsername(username);
    }
    
}
