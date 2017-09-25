package com.ucab.base.webservices.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ucab.base.data.models.security.User;
import com.ucab.base.services.security.sessionful.IAuthenticationService;

@RestController
@RequestMapping("/api")
public class AuthController {

	final static Logger log = Logger.getLogger(AuthController.class);

	private static final String HEADER_TOKEN = "X-Auth-Token";

	@Autowired
	private IAuthenticationService authenticationService;

	
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public ResponseEntity<User> AuthenticationRequest() {
		
		User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
		
		return ResponseEntity.ok(authenticatedUser);
		
		
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ResponseEntity<?> LogoutRequest(HttpServletRequest request) {

		String token = request.getHeader(HEADER_TOKEN);

		authenticationService.logout(token);

		return ResponseEntity.ok(null);
	}
}
