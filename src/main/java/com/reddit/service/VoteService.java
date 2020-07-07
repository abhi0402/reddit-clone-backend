package com.reddit.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reddit.dto.VoteRequest;
import com.reddit.exception.PostNotFoundException;
import com.reddit.exception.SpringRedditException;
import com.reddit.model.Post;
import com.reddit.model.Vote;
import com.reddit.model.VoteType;
import com.reddit.repository.PostRepository;
import com.reddit.repository.VoteRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class VoteService {
	
	private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteRequest voteRequest) {
        Post post = postRepository.findById(voteRequest.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteRequest.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteRequest.getVoteType())) {
            throw new SpringRedditException("You have already "
                    + voteRequest.getVoteType() + "'d for this post");
        }
        if (VoteType.UPVOTE.equals(voteRequest.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(mapToVote(voteRequest, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteRequest voteRequest, Post post) {
        return Vote.builder()
                .voteType(voteRequest.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
	
}
