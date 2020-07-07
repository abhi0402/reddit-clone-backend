package com.reddit.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reddit.exception.SpringRedditException;
import com.reddit.model.RefreshToken;
import com.reddit.repository.RefreshTokenRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {
	
	private final RefreshTokenRepository refreshTokenRepository;

	public RefreshToken generateRefreshToken() {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setCreatedDate(Instant.now());
		refreshToken.setToken(UUID.randomUUID().toString());
		
		return refreshTokenRepository.save(refreshToken);
	}
	
	public void validateRefreshToken(String token) {
		refreshTokenRepository.findByToken(token)
							.orElseThrow(() -> new SpringRedditException("Invalid refresh token"));
	}
	
	public void deleteRefreshToken(String token) {
		refreshTokenRepository.deleteByToken(token);
	}
	
}
