/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.lab9.repository;

import com.example.lab9.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Integer> 
{
    
    User findByUsername(String username);
    public Optional<User> findById(Long userId);
    @Query("SELECT COUNT(p) FROM Post p WHERE p.user.username = :username")
    long countPostsByUsername(@Param("username") String username);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.user.username = :username")
    long countCommentsByUsername(@Param("username") String username);
    
    @Query("SELECT COUNT(l) FROM User u JOIN u.likes l WHERE u.username = :username")
long countLikesByUsername(@Param("username") String username);

    
}