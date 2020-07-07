package com.reddit.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserPrinciple implements UserDetails {
	
	private static final long serialVersionUID = 1L;

	private Long id;

	private String username;

	private String email;
	
	@JsonIgnore
	private String password;
	
	private String role;
	
	private boolean enabled;
	
	public UserPrinciple(Long id, String username, String email, String password,
			String role, boolean enabled) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
		this.enabled = enabled;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority(role));
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        UserPrinciple user = (UserPrinciple) o;
        return Objects.equals(id, user.id);
	}

}
