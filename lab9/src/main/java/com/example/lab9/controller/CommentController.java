/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.lab9.controller;

import com.example.lab9.dto.CommentDto;
import com.example.lab9.model.Comment;
import com.example.lab9.model.User;
import com.example.lab9.service.CommentService;
import com.example.lab9.service.PostService;
import com.example.lab9.service.UserService;
import java.security.Principal;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @PostMapping("/addComment/{postId}")
    public String addComment(@PathVariable Long postId, CommentDto commentDto, Principal principal, RedirectAttributes redirectAttributes) {

        if (commentDto.getContent() == null || commentDto.getContent().isEmpty()) {
            // Set an error message and redirect back to the form
            redirectAttributes.addFlashAttribute("errorMessage", "Comment content cannot be empty.");
            return "redirect:/hello";

        }

        String username = principal.getName();
        User user = userService.findByUsername(username);

        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setAuthor(user.getUsername());
        comment.setLikes(0);
        comment.setAddedDate(new Date());
        comment.setUser(user);
        comment.setPost(postService.getPostById(postId));

        System.out.println(comment);

        postService.addCommentToPost(postId, comment);

        return "redirect:/hello";
    }

    @PostMapping("/deleteComment/{commentId}")
    public String deleteComment(@PathVariable Long commentId, Principal principal, RedirectAttributes redirectAttributes) {
        Comment comment = commentService.getCommentById(commentId);

        if (comment != null) {
            User loggedInUser = userService.findByUsername(principal.getName());

            // Check if the comment belongs to the logged-in user
            if (comment.getAuthor().equals(loggedInUser.getUsername())) {
                commentService.deleteComment(commentId);
            } else {
                // Set an error message in the session
                redirectAttributes.addFlashAttribute("errorMessage", "You don't have permission to delete this comment.");
            }
        } else {
            // Set an error message in the session
            redirectAttributes.addFlashAttribute("errorMessage", "Comment not found with id: " + commentId);
        }

        return "redirect:/hello";
    }

    @GetMapping("/editComment/{commentId}")
    public String showEditCommentPage(@PathVariable Long commentId, Model model) {
        Comment comment = commentService.getCommentById(commentId);
        if (comment == null) {
            model.addAttribute("errorMessage", "Comment not found");
            return "hello"; // Or whatever your error page is
        }
        // Set an error message and redirect back to the form

        model.addAttribute("comment", comment);
        return "editComment";
    }

    @PostMapping("/editComment/{commentId}")
    public String editComment(@PathVariable Long commentId, @RequestParam String content, Model model, RedirectAttributes redirectAttributes) {
        Comment comment = commentService.getCommentById(commentId);
        if (comment == null) {
            model.addAttribute("errorMessage", "Comment not found");
            return "hello"; // Or whatever your error page is
        }
        if (content == null || content.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Comment content cannot be empty.");
            return "redirect:/hello";

        }

        commentService.editComment(commentId, content);
        return "redirect:/hello";
    }
}
