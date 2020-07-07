package com.reddit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.reddit.dto.CommentRequest;
import com.reddit.model.Comment;
import com.reddit.model.Post;
import com.reddit.model.User;

@Mapper(componentModel = "spring")
public interface CommentMapper {

	@Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentRequest.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    Comment map(CommentRequest commentRequest, Post post, User user);

    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    CommentRequest mapToDto(Comment comment);
	
}
