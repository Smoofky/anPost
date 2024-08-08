/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.lab9.service;

import com.example.lab9.dto.CommentDto;
import com.example.lab9.dto.UserDto;
import com.example.lab9.model.Comment;
import com.example.lab9.model.Post;
import com.example.lab9.model.User;
import com.example.lab9.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(this::convertToCommentDtoWithUserDetails)
                .collect(Collectors.toList());
    }
    
    
    private CommentDto convertToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setContent(comment.getContent());
        commentDto.setAuthor(comment.getAuthor());
        commentDto.setPostId(comment.getPost().getId());
        commentDto.setLikes(comment.getLikes());
        commentDto.setAddedDate(comment.getAddedDate());
        // Convert User to UserDto
        User user = comment.getUser();
                commentDto.setImageUrl(user.getImageUrl());


        return commentDto;
    }
    
    private CommentDto convertToCommentDtoWithUserDetails(Comment comment) {
        CommentDto commentDto = convertToCommentDto(comment);
        // Fetch user details including imageUrl
        UserDto userDto = userService.getUserById(comment.getUser().getId());
        commentDto.setUser(userDto);
        return commentDto;
    }

    @Override
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElse(null);
    }

    @Override
    public void editComment(Long commentId, String newContent) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            comment.setContent(newContent);
            commentRepository.save(comment);
        }
    }
}
