package com.mysite.community.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysite.community.entity.Post;
import com.mysite.community.repository.PostRepository;

@Service
public class PostService {
    private final PostRepository postRepository;
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Post getPostById(Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @Transactional
    public void incrementPostViews(Long id) {
        logger.debug("Incrementing views for post id: " + id);
        postRepository.incrementViews(id);
    }

    @Transactional
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(Long id, Post updatedPost) {
        Post existingPost = postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());

        return postRepository.save(existingPost);
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Post> searchPosts(String searchTerm, String sortOrder) {
        List<Post> posts = postRepository.searchPosts(searchTerm);

        if (sortOrder.equals("latest")) {
            posts.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        } else if (sortOrder.equals("popular")) {
            posts.sort((a, b) -> b.getViews() - a.getViews());
        }

        return posts;
    }

    // 추가된 메서드
    @Transactional(readOnly = true)
    public Post findById(Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Post not found"));
    }
}
