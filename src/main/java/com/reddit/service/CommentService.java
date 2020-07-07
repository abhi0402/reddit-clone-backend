package com.reddit.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.reddit.dto.CommentRequest;
import com.reddit.exception.PostNotFoundException;
import com.reddit.mapper.CommentMapper;
import com.reddit.model.Comment;
import com.reddit.model.NotificationEmail;
import com.reddit.model.Post;
import com.reddit.model.User;
import com.reddit.repository.CommentRepository;
import com.reddit.repository.PostRepository;
import com.reddit.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {
	
    private static final String POST_URL = "";

	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final AuthService authService;
	private final UserRepository userRepository;
	private final CommentMapper commentMapper;
	private final MailContentBuilder mailContentBuilder;
	private final MailService mailService;
	
	public void save(CommentRequest commentRequest) {
		Post post = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentRequest.getPostId().toString()));
		User user = authService.getCurrentUser();
		Comment comment = commentMapper.map(commentRequest, post, user);
        commentRepository.save(comment);
        
        String message = mailContentBuilder.build(user.getUsername() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, user, post);
	}
	
	private void sendCommentNotification(String message, User user, Post post) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", post.getUser().getEmail(), message));
    }

	public List<CommentRequest> getAllCommentsForPost(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto).collect(Collectors.toList());
	}

	public List<CommentRequest> getAllCommentsForUser(String userName) {
		User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
	}
}
