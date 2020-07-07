package com.reddit.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {

	public String authenticateToken;
	public String username;
	public String refreshToken;
	public Instant eat;
	
}
