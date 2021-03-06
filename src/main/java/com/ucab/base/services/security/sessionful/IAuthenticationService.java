package com.ucab.base.services.security.sessionful;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ucab.base.data.models.security.User;
import com.ucab.base.security.filters.SessionAuthenticationFilter;
import com.ucab.base.security.sessionful.SessionInfo;


/**
 * Authenticates the user or checks valid token. This should put together Spring's
 * {@link AuthenticationManager} and our {@link SessionManager}. It also provides {@link User}
 * for current user and thus hides interaction with {@link SecurityContextHolder}. However
 * this returns only Spring Security interface, so cast may be need to any application specific subclass.
 * Alternatively separate service for obtaining current user can be developed.
 * <p>
 * This should not interact with HTTP as that is job of {@link SessionAuthenticationFilter}.
 */
public interface IAuthenticationService {

	/**
	 * Authenticates the user and returns valid token. If anything fails, {@code null} is returned instead.
	 * Prepares {@link org.springframework.security.core.context.SecurityContext} if authentication succeeded.
	 */
	SessionInfo authenticate(String login, String password , String IP) throws AuthenticationException;

	/**
	 * Checks the authentication token and if it is valid prepares
	 * {@link org.springframework.security.core.context.SecurityContext} and returns true.
	 */
	boolean checkToken(String token);

	/** Logouts the user - token is invalidated/forgotten. */
	void logout(String token);

	/** Returns current user or {@code null} if there is no authentication or user is anonymous. */
	User currentUser();
}
