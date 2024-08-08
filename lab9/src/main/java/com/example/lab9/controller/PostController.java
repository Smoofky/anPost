/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.lab9.controller;

import com.example.lab9.dto.PostDto;
import com.example.lab9.model.Post;
import com.example.lab9.model.User;
import com.example.lab9.service.PostService;
import com.example.lab9.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Date;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @PostMapping("/add")
    public String addPost(@ModelAttribute("postDto") PostDto postDto, Principal principal, RedirectAttributes redirectAttributes) {

        if (postDto.getTitle() == null || postDto.getTitle().isEmpty() || postDto.getContent() == null || postDto.getContent().isEmpty()) {
            // Set an error message and redirect back to the form
            redirectAttributes.addFlashAttribute("errorMessage", "Post title and content cannot be empty.");
            return "redirect:/hello";
        }

        String username = principal.getName();
        User user = userService.findByUsername(username);

        // Create a Post entity from the DTO and set the user
        Post post = new Post();
        post.setContent(postDto.getContent());
        post.setAuthor(user.getUsername());
        post.setLikes(0);
        post.setAddedDate(new Date());
        post.setUser(user);
        post.setTitle(postDto.getTitle());

        // Save the post
        postService.savePost(post);

        // Redirect to the hello page or another appropriate page
        return "redirect:/hello";
    }

    @PostMapping("/deletePost/{postId}")
    public String deletePost(@PathVariable Long postId, Principal principal,
            RedirectAttributes redirectAttributes) {
        Post post = postService.getPostById(postId);

        if (post != null) {
            User loggedInUser = userService.findByUsername(principal.getName());

            // Check if the post belongs to the logged-in user
            if (post.getAuthor().equals(loggedInUser.getUsername())) {
                postService.deletePost(postId);
            } else {
                // Set an error message in the session
                redirectAttributes.addFlashAttribute("errorMessage", "You don't have permission to delete this post.");
            }
        } else {
            // Set an error message in the session
            redirectAttributes.addFlashAttribute("errorMessage", "Post not found with id: " + postId);
        }

        return "redirect:/hello";
    }

    @GetMapping("/editPost/{postId}")
    public String showEditPostPage(@PathVariable Long postId, Model model) {
        Post post = postService.getPostById(postId);
        if (post == null) {
            model.addAttribute("errorMessage", "Post not found");
            return "hello"; // Or whatever your error page is
        }
        model.addAttribute("post", post);
        return "editPost";
    }

    @PostMapping("/editPost/{postId}")
    public String editPost(@PathVariable Long postId, @RequestParam String content, @RequestParam String title, Model model, RedirectAttributes redirectAttributes) {
        Post post = postService.getPostById(postId);
        if (post == null) {
            model.addAttribute("errorMessage", "Post not found");
            return "hello"; // Or whatever your error page is
        }
        if (content == null || content.isEmpty() || title == null || title.isEmpty()) {
            // Set an error message and redirect back to the form
            redirectAttributes.addFlashAttribute("errorMessage", "Post title and content cannot be empty.");
            return "redirect:/hello";

        }
        postService.editPost(postId, content, title);
        return "redirect:/hello";
    }

    @PostMapping("/like/{postId}")
    public String likePost(@PathVariable Long postId, Principal principal) {
        System.out.println("LIKE-POSTID: " + postId);
        String userId = principal.getName();
        postService.likePost(postId, userId);
        return "redirect:/hello";
    }

    @PostMapping("/dislike/{postId}")
    public String dislikePost(@PathVariable Long postId, Principal principal) {
        System.out.println("DISLIKE-POSTID: " + postId);
        String userId = principal.getName();
        postService.dislikePost(postId, userId);
        return "redirect:/hello";
    }

}
