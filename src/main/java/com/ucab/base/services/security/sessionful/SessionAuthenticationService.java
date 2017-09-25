package com.ucab.base.services.security.sessionful;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ucab.base.data.models.security.User;
import com.ucab.base.security.UserAuthentication;
import com.ucab.base.security.sessionful.SessionInfo;;;

@Service("IAuthenticationService")
public class SessionAuthenticationService implements IAuthenticationService {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private ISessionManager sessionManager;
	
	
	final static Logger log = Logger.getLogger(SessionAuthenticationService.class);
	
	@Override
	public SessionInfo authenticate(String login, String password,String IP) throws AuthenticationException{

		
		Authentication authentication = new UsernamePasswordAuthenticationToken(login, password);
		try {
			authentication = authenticationManager.authenticate(authentication);

			if(authentication.isAuthenticated())
			{
				User authenticatedUser = (User) authentication.getPrincipal();
				
				authentication = new UserAuthentication(authenticatedUser);
				if(!authenticatedUser.isActive()){
					authentication.setAuthenticated(false);
					SecurityContextHolder.clearContext();
					return null;
				}
				SecurityContextHolder.getContext().setAuthentication(authentication);

				if (authentication.getPrincipal() != null) {
					SessionInfo newToken = sessionManager.createNewSession(authentication);
					if (newToken == null) {
						return null;
					}
					return newToken;
				}
			}
		} catch (AuthenticationException e) {
			
			log.error("Error authenticating user: " + e.getMessage().toString());
			
			throw e;
		}
		return null;
	}
	
	@Override
	public boolean checkToken(String token) {
		log.info(" *** AuthenticationServiceImpl.checkToken");
		SessionInfo session = sessionManager.isSessionValid(token);
		if(session == null){
			return false;
		}
		if(!session.isValid()){
			logout(token);
			return false;
		}

		User user = sessionManager.getUser(token);
		if (user == null) {
			return false;
		}

		Authentication authentication = new UserAuthentication(user);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return true;
	}

	@Override
	public void logout(String token) {
		User logoutUser = sessionManager.removeSession(token);
		log.info(" *** AuthenticationServiceImpl.logout: " + logoutUser.getUsername());
		SecurityContextHolder.clearContext();
	}

	@Override
	public User currentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}
		return (User) authentication.getDetails();
	}

}
