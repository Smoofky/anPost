/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.lab9.service;
import com.example.lab9.dto.PostDto;
import com.example.lab9.model.Comment;
import com.example.lab9.model.Like;
import com.example.lab9.model.Post;
import com.example.lab9.model.User;
import com.example.lab9.repository.LikeRepository;
import com.example.lab9.repository.PostRepository;
import com.example.lab9.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Override
    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }
    
    @Override
    public List<Post> getPostsByUser(User user) {
        return postRepository.findByUser(user);
    }

    @Override
    public void savePost(Post post) {
        postRepository.save(post);
    }

    @Override
    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }
    
    @Override
    public void addCommentToPost(Long postId, Comment comment) {
        Post post = getPostById(postId);
        if (post != null) {
            comment.setPost(post);
            post.getComments().add(comment);
            postRepository.save(post);
        }
    }
    
    @Override
    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    private PostDto convertToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setContent(post.getContent());
        postDto.setAuthor(post.getAuthor());
        postDto.setLikes(post.getLikes());
        postDto.setAddedDate(post.getAddedDate());
        postDto.setTitle(post.getTitle());
        postDto.setImageUrl(post.getUser().getImageUrl());
        return postDto;
    }
        @Override
    public void editPost(Long postId, String newContent, String newTitle) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            post.setContent(newContent);
            post.setTitle(newTitle);
            postRepository.save(post);
        }
    }

     @Override
     @Transactional
    public void likePost(Long postId, String userId) {
        Post post = postRepository.findById(postId).orElse(null);
        User user = userRepository.findByUsername(userId);

        if (post != null && user != null) {
            // Update the Post entity
            post.setLikes(post.getLikes() + 1);
            postRepository.save(post);

            // Create and save the Like entity
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            likeRepository.save(like);
        }
    }

    @Override
    @Transactional
    public void dislikePost(Long postId, String userId) {
        Post post = postRepository.findById(postId).orElse(null);
        User user = userRepository.findByUsername(userId);

        if (post != null && user != null) {
            // Update the Post entity
            post.setLikes(Math.max(0, post.getLikes() - 1));
            postRepository.save(post);

            // Create and save the Like entity
            likeRepository.deleteByUserAndPost(user, post);
        }
    }
    
    @Override
public List<Long> getLikedPostIdsForUser(Long userId) {
    List<Like> userLikes = likeRepository.findByUserId(userId);

    // Log the likes to check if they are retrieved correctly
    System.out.println("User Likes: " + userLikes);

    // Extract post IDs from likes
    List<Long> likedPostIds = userLikes.stream()
            .map(like -> like.getPost().getId())
            .collect(Collectors.toList());

    // Log the extracted post IDs
    System.out.println("Liked Post IDs: " + likedPostIds);

    return likedPostIds;
}

}
