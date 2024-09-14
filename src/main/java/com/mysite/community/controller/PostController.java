package com.mysite.community.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mysite.community.entity.Post;
import com.mysite.community.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(
        @RequestParam(required = false, defaultValue = "") String searchTerm,
        @RequestParam(required = false, defaultValue = "popular") String sortOrder
    ) {
        List<Post> posts = postService.searchPosts(searchTerm, sortOrder);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post != null) {
            postService.incrementPostViews(id);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Post> createPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {
        
        // Debugging: Check if photo is null and its size
        if (photo != null) {
            System.out.println("Received photo with size: " + photo.getSize());
        } else {
            System.out.println("No photo received");
        }

        // Existing code
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String loggedInUserEmail = authentication.getName();

            Post post = new Post();
            post.setTitle(title);
            post.setContent(content);
            post.setAuthor(loggedInUserEmail);

            if (photo != null) {
                try {
                    post.setPhoto(photo.getBytes());
                } catch (IOException e) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            Post createdPost = postService.createPost(post);
            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePost(
            @PathVariable Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {

        try {
            Post post = postService.getPostById(id);
            if (post == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시물이 존재하지 않습니다.");
            }

            if (title != null) post.setTitle(title);
            if (content != null) post.setContent(content);
            if (photo != null) {
                try {
                    post.setPhoto(photo.getBytes());
                } catch (IOException e) {
                    return new ResponseEntity<>("사진 업로드 실패", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            postService.updatePost(id, post);
            return ResponseEntity.ok("게시물이 수정되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication instanceof UsernamePasswordAuthenticationToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 인증 실패");
        }

        String loggedInUserEmail = authentication.getName();
        Post post = postService.getPostById(id);
        if (post != null) {
            if (post.getAuthor().equals(loggedInUserEmail)) {
                postService.deletePost(id);
                return ResponseEntity.ok("게시물이 삭제되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시물이 존재하지 않습니다.");
        }
    }
}
