package com.reddit.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.reddit.dto.SubredditRequest;
import com.reddit.model.Post;
import com.reddit.model.Subreddit;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

	@Mapping(target = "numOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditRequest mapSubredditToDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    Subreddit mapDtoToSubreddit(SubredditRequest subredditRequest);
	
}
