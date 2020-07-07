package com.reddit.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reddit.model.User;
import com.reddit.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = userRepository.findByUsername(username);
		User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User with the given name Not Found - " + username));
		return new UserPrinciple(user.getUserId(), user.getUsername(), user.getEmail(), user.getPassword(), "USER", user.isEnabled());
	}

}
