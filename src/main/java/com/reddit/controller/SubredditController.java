package com.reddit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reddit.dto.SubredditRequest;
import com.reddit.service.SubredditService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/subreddit")
public class SubredditController {
	
	private final SubredditService subredditService;
	
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	@PostMapping
	public ResponseEntity<SubredditRequest> createSubReddit(@RequestBody SubredditRequest subredditRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(subredditService.save(subredditRequest));
	}

	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	@GetMapping
	public ResponseEntity<List<SubredditRequest>> getAllSubreddits() {
		return ResponseEntity.status(HttpStatus.OK).body(subredditService.getAll());
	}
	
}
