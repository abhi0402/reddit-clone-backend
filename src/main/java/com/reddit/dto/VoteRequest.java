package com.reddit.dto;

import com.reddit.model.VoteType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteRequest {
    private VoteType voteType;
    private Long postId;
}