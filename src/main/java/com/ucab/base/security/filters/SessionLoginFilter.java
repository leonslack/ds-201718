package com.ucab.base.security.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucab.base.commons.helpers.HttpCommunicationHandler;
import com.ucab.base.data.models.security.User;
import com.ucab.base.security.sessionful.SessionInfo;
import com.ucab.base.services.security.sessionful.IAuthenticationService;


public class SessionLoginFilter extends AbstractAuthenticationProcessingFilter {
	
	private static final String HEADER_TOKEN = "X-Auth-Token";
	
	final static Logger log = Logger.getLogger(SessionLoginFilter.class);

	private final IAuthenticationService sessionAuthenticationManager;
	
	private SessionInfo session;
	
	
	public SessionLoginFilter(String urlMapping, IAuthenticationService authenticationManager, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(urlMapping));
		this.sessionAuthenticationManager = authenticationManager;
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		User user = null;
		try{
			user = new ObjectMapper().readValue(request.getInputStream(), User.class);
		}
		catch(Exception ex){
			response = HttpCommunicationHandler.AddCORSHeadertoResponse(request, response);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		session = sessionAuthenticationManager.authenticate(user.getUsername(), user.getPassword(),HttpCommunicationHandler.requestIP(request));
		
		
		return session.getAuthUser();
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		response = HttpCommunicationHandler.AddCORSHeadertoResponse(request, response);
		getFailureHandler().onAuthenticationFailure(request, response, failed);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain, Authentication authentication) throws IOException, ServletException {
		if(session != null)
		{
			response.setHeader(HEADER_TOKEN, session.getToken());			
			request.getRequestDispatcher(request.getServletPath()).forward(request, response);
		}
		else
		{
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
}
