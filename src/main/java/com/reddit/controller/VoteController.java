package com.reddit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reddit.dto.VoteRequest;
import com.reddit.service.VoteService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/votes")
public class VoteController {

	private final VoteService voteService;
	
	@ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
	@PostMapping
	public ResponseEntity<Void> submitVote(@RequestBody VoteRequest voteRequest) {
		voteService.vote(voteRequest);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
