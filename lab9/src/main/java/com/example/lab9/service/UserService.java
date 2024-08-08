/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.lab9.service;

import com.example.lab9.dto.UserDto;
import com.example.lab9.model.User;

/**
 *
 * @author rytel
 */
public interface UserService {

    User findByUsername(String username);

    void saveUser(User user);

    boolean isUsernameTaken(String username);
    public UserDto getUserById(Long id);
    UserDto getUserDtoByUsername(String username);
    
    public long countPostsByUsername(String username);
    public long countCommentsByUsername(String username);
        public long countLikesByUsername(String username);

}
