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

import com.reddit.dto.CommentRequest;
import com.reddit.service.CommentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
	
	private final CommentService commentService;

	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	@PostMapping
	public ResponseEntity<Void> postComment(@RequestBody CommentRequest commentRequest) {
		commentService.save(commentRequest);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	@GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentRequest>> getAllCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getAllCommentsForPost(postId));
    }

	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    @GetMapping("/by-user/{userName}")
    public ResponseEntity<List<CommentRequest>> getAllCommentsForUser(@PathVariable String userName){
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getAllCommentsForUser(userName));
    }
	
}
