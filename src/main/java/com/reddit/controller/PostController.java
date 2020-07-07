package com.reddit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.status;

import com.reddit.dto.PostRequest;
import com.reddit.dto.PostResponse;
import com.reddit.service.PostService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/posts/")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) {
        postService.save(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return status(HttpStatus.OK).body(postService.getAllPosts());
    }

	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return status(HttpStatus.OK).body(postService.getPost(id));
    }

	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("by-subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(Long id) {
        return status(HttpStatus.OK).body(postService.getPostsBySubreddit(id));
    }

	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("by-user/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(String username) {
        return status(HttpStatus.OK).body(postService.getPostsByUsername(username));
    }
}
