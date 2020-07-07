package com.reddit.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RefreshTokenRequest {

	@NotBlank
	private String refreshToken;
	private String userName;
	
}
