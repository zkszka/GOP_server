package com.mysite.community.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mysite.community.entity.Comment;
import com.mysite.community.entity.Post;
import com.mysite.community.service.CommentService;
import com.mysite.community.service.PostService;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    // 게시물의 모든 댓글 가져오기
    @GetMapping
    public List<Comment> getComments(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    // 댓글 추가
    @PostMapping
    public Comment addComment(@PathVariable Long postId, @RequestBody Comment comment) {
        return commentService.addComment(postId, comment);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = auth.getName();
        String userRole = auth.getAuthorities().toString(); // 권한 가져오기

        try {
            Comment comment = commentService.findById(commentId);
            Post post = postService.findById(postId);

            boolean isCommentAuthor = comment.getAuthor().equals(currentUserEmail);
            boolean isPostAuthor = post.getAuthor().equals(currentUserEmail);
            boolean isAdmin = userRole.contains("ADMIN");

            if (isCommentAuthor || isPostAuthor || isAdmin) {
                commentService.deleteComment(commentId);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 댓글이나 게시물이 없을 때
        }
    }
}