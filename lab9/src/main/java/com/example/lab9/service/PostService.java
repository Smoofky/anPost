/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.lab9.service;

import com.example.lab9.dto.PostDto;
import com.example.lab9.model.Comment;
import com.example.lab9.model.Post;
import com.example.lab9.model.User;
import java.util.List;

public interface PostService {

    List<PostDto> getAllPosts();

    List<Post> getPostsByUser(User user);

    void savePost(Post post);

    Post getPostById(Long postId);

    void addCommentToPost(Long postId, Comment comment);

    public void deletePost(Long postId);

    public void editPost(Long postId, String content, String title);

    void likePost(Long postId, String userId);

 public void dislikePost(Long postId, String userId);    
    public List<Long> getLikedPostIdsForUser(Long userId);
}   
