package com.ucab.base.services.security.sessionful;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.Authentication;

import com.ucab.base.data.models.security.User;
import com.ucab.base.security.sessionful.SessionInfo;

/**
 * Manages tokens - separated from {@link IAuthenticationService},
 * so we can implement and plug various policies.
 */

public interface ISessionManager {
	
	/**
	 * Creates a new token for the user and returns its {@link SessionInfo}.
	 * It may add it to the token list or replace the previous one for the user. Never returns {@code null}.
	 */
	SessionInfo createNewSession(Authentication user);

	/** Removes all tokens for user. */
	void removeUser(User user);

	/** Removes a single token. */
	User removeSession(String token);

	/** Returns user details for a token. */
	User getUser(String token);

	/** Returns a collection with token information for a particular user. */
	Collection<SessionInfo> getUserSessions(User user);

	/** Returns a map from valid tokens to users. */
	Map<String, User> getValidUsers();
	
	/** Returns true if the session is still valid */
	SessionInfo isSessionValid(String Token);

}
