package com.reddit.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reddit.dto.AuthenticationResponse;
import com.reddit.dto.LoginRequest;
import com.reddit.dto.RefreshTokenRequest;
import com.reddit.dto.RegisterRequest;
import com.reddit.exception.SpringRedditException;
import com.reddit.model.NotificationEmail;
import com.reddit.model.User;
import com.reddit.model.VerificationToken;
import com.reddit.repository.UserRepository;
import com.reddit.repository.VerificationTokenRepository;
import com.reddit.security.JwtProvider;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationTokenRepository;
	private final MailService mailService;
	private final RefreshTokenService refreshTokenService;
	
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	
	@Transactional
	public void signup(RegisterRequest registerRequest) {
		User user = new User();
		Instant currentTime = Instant.now();
		user.setEmail(registerRequest.getEmail());
		user.setUsername(registerRequest.getUsername());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setEnabled(false);
		user.setCreated(currentTime);
		userRepository.save(user);
		
		String token = generateVerificationToken(user, currentTime);
		
		mailService.sendMail(new NotificationEmail("Activate your Account - Reddit-clone",
                user.getEmail(), "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account : " +
                "http://localhost:4444/api/auth/accountVerification/" + token));
	}

	private String generateVerificationToken(User user, Instant currentTime) {
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationToken.setExpiryDate(currentTime.plus(24, ChronoUnit.HOURS));
		verificationTokenRepository.save(verificationToken);
		
		return token;
	}

	public void verifyAccount(String token) {
		Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
		verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
		fetchUserandEnable(verificationToken.get());
	}

	@Transactional
	private void fetchUserandEnable(VerificationToken verificationToken) {
		Long userId = verificationToken.getUser().getUserId();
		Optional<User> user = userRepository.findById(userId);
		user.orElseThrow(() -> new SpringRedditException("User now found with name - " + verificationToken.getUser().getUsername()));
		User saveUser = user.get();
		saveUser.setEnabled(true);
		userRepository.save(saveUser);
	}

	public AuthenticationResponse login(LoginRequest loginRequest) {
		Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(auth);
		String authenticateToken = jwtProvider.generateToken(auth);
		return AuthenticationResponse.builder()
									.authenticateToken(authenticateToken)
									.username(loginRequest.getUsername())
									.refreshToken(refreshTokenService.generateRefreshToken().getToken())
									.eat(Instant.now().plusMillis(jwtProvider.getJwtExpiration()))
									.build();
	}
	
	@Transactional(readOnly = true)
    public User getCurrentUser() {
		UserPrinciple principal = (UserPrinciple) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }
	
	public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

	public AuthenticationResponse refreshToken(@Valid RefreshTokenRequest refreshTokenRequest) {
		refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
		String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUserName());
        return AuthenticationResponse.builder()
        		.authenticateToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .eat(Instant.now().plusMillis(jwtProvider.getJwtExpiration()))
                .username(refreshTokenRequest.getUserName())
                .build();
	}
	
}
