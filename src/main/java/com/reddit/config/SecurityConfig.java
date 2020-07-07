package com.reddit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.reddit.security.JwtAuthEntryPoint;
import com.reddit.security.JwtAuthenticationFilter;

import lombok.AllArgsConstructor;

@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final UserDetailsService userDetailsService;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthEntryPoint unauthorizedHandler;
	
	private static final String[] AUTH_WHITELIST = {
            // -- swagger UI
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
            // other public endpoints of your API can be appended to this array
    };
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
					.authorizeRequests()
					.antMatchers(AUTH_WHITELIST).permitAll()
					.antMatchers("/api/auth/**")
					.permitAll()
					.anyRequest()
					.authenticated()
					.and()
	                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
	                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);;
		
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder authBuilder) throws Exception {
		authBuilder.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncode());
	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncode() {
		return new BCryptPasswordEncoder();
	}
}
