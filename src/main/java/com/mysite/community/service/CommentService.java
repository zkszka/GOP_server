package com.mysite.community.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysite.community.entity.Comment;
import com.mysite.community.entity.Post;
import com.mysite.community.repository.CommentRepository;
import com.mysite.community.repository.PostRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    // 특정 게시물의 모든 댓글 가져오기
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    // 댓글 추가
    public Comment addComment(Long postId, Comment comment) {
        return postRepository.findById(postId)
            .map(post -> {
                comment.setPost(post);
                Comment savedComment = commentRepository.save(comment);
                post.setComments(post.getComments() + 1); // 댓글 수 증가
                postRepository.save(post);
                return savedComment;
            })
            .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    // 댓글 삭제
    public void deleteComment(Long commentId) {
        commentRepository.findById(commentId).ifPresent(comment -> {
            Post post = comment.getPost();
            commentRepository.deleteById(commentId);
            post.setComments(post.getComments() - 1); // 댓글 수 감소
            postRepository.save(post);
        });
    }

    // 댓글 조회
    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new RuntimeException("Comment not found"));
    }
}
