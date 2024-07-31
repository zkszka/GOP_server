package com.mysite.community.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysite.community.entity.Post;
import com.mysite.community.repository.PostRepository;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Transactional
    public Post getPostById(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Post not found"));
        postRepository.incrementViews(id); // 조회수 증가
        return post;
    }

    @Transactional
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(Post updatedPost) {
        // 예시 코드로, 실제 필요에 따라 수정이 필요할 수 있습니다.
        return postRepository.save(updatedPost);
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
