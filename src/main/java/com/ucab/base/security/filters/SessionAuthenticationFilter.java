package com.ucab.base.security.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.ucab.base.commons.helpers.HttpCommunicationHandler;
import com.ucab.base.services.security.sessionful.IAuthenticationService;

public class SessionAuthenticationFilter extends GenericFilterBean {

	private static final String HEADER_TOKEN = "X-Auth-Token";

	private final IAuthenticationService authenticationService;

	final static Logger log = Logger.getLogger(SessionAuthenticationFilter.class);

	public SessionAuthenticationFilter(IAuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		boolean authenticated = checkSession(httpRequest, httpResponse);

		if (authenticated) {
			chain.doFilter(request, response);
		}
	}

	/** Returns true, if request contains valid authentication token. */
	private boolean checkSession(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		String token = httpRequest.getHeader(HEADER_TOKEN);
		if (HttpMethod.OPTIONS.matches(httpRequest.getMethod())) {
			return true;

		}
		if (token == null) {
			return false;
		}

		if (authenticationService.checkToken(token)) {
			log.info(" *** " + HEADER_TOKEN + " valid for: "
					+ SecurityContextHolder.getContext().getAuthentication().getName());
			return true;
		} else {
			log.info(" *** Invalid " + HEADER_TOKEN + ' ' + token);
			SecurityContextHolder.clearContext();
			httpResponse = HttpCommunicationHandler.AddCORSHeadertoResponse(httpRequest, httpResponse);
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return false;
	}

}
