package com.ucab.base.security.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucab.base.commons.helpers.HttpCommunicationHandler;
import com.ucab.base.data.models.security.User;
import com.ucab.base.security.UserAuthentication;
import com.ucab.base.services.security.SecurityUserService;
import com.ucab.base.services.security.sessionless.TokenAuthenticationManager;

public class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {

	private final TokenAuthenticationManager authenticationManager;
	private final SecurityUserService userService;

	public StatelessLoginFilter(String urlMapping, TokenAuthenticationManager authenticationManager,
			SecurityUserService userService, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(urlMapping));
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		final User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
		final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(
				user.getUsername(), user.getPassword());
		return getAuthenticationManager().authenticate(loginToken);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		response = HttpCommunicationHandler.AddCORSHeadertoResponse(request, response);
		getFailureHandler().onAuthenticationFailure(request, response, failed);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {

		// Lookup the complete User object from the database and create an
		// Authentication for it
		final User authenticatedUser = userService.loadUserByUsername(authentication.getName());
		final Authentication userAuthentication = new UserAuthentication(authenticatedUser);

		// Add the custom token as HTTP header to the response
		authenticationManager.addAuthentication(response, userAuthentication);

		// Add the authentication to the Security context
		SecurityContextHolder.getContext().setAuthentication(userAuthentication);
		request.getRequestDispatcher(request.getServletPath()).forward(request, response);
	}

}