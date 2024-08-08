package com.example.lab9.service;

import com.example.lab9.dto.CommentDto;
import com.example.lab9.model.Comment;
import java.util.List;

public interface CommentService {
    List<CommentDto> getCommentsByPostId(Long postId);
    void saveComment(Comment comment);

    public void deleteComment(Long commentId);

    public Comment getCommentById(Long commentId);

    public void editComment(Long commentId, String editedContent);
}
