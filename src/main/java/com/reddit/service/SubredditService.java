package com.reddit.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reddit.dto.SubredditRequest;
import com.reddit.mapper.SubredditMapper;
import com.reddit.model.Subreddit;
import com.reddit.model.User;
import com.reddit.repository.SubredditRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SubredditService {

	private final SubredditRepository subredditRepository;
	private final SubredditMapper subredditMapper;
	private final AuthService authService;
	
	@Transactional
	public SubredditRequest save(SubredditRequest subredditRequest) {
		Subreddit subreddit = subredditMapper.mapDtoToSubreddit(subredditRequest);
		User user = authService.getCurrentUser();
		subreddit.setUser(user);
		Subreddit savedSubreddit = subredditRepository.save(subreddit);
		subredditRequest.setId(savedSubreddit.getId());
		return subredditRequest;
	}

	

	@Transactional(readOnly = true)
	public List<SubredditRequest> getAll() {
		return subredditRepository.findAll()
							.stream()
							.map(subredditMapper::mapSubredditToDto)
							.collect(Collectors.toList());
	}
	
	/*
	 * //builder pattern private Subreddit mapSubredditRequest(SubredditRequest
	 * subredditRequest) { return
	 * Subreddit.builder().name(subredditRequest.getName())
	 * .description(subredditRequest.getDescription()) .createdDate(Instant.now())
	 * .build(); }
	 */
	
	/*
	 * private SubredditRequest mapToDto(Subreddit subreddit) { return
	 * SubredditRequest.builder().name(subreddit.getName()) .id(subreddit.getId())
	 * .description(subreddit.getDescription())
	 * .numOfPosts(subreddit.getPosts().size()) .build(); }
	 */
	
}
