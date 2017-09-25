package com.ucab.base.services.security.sessionful;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;

import com.ucab.base.data.models.security.User;
import com.ucab.base.security.sessionful.SessionInfo;

@Component
public class SessionManagerInMemory implements ISessionManager {
	
	final static Logger log = Logger.getLogger(SessionManagerInMemory.class);

	private Map<String, User> validUsers = new HashMap<>();

	/**
	 * This maps system users to sessions because equals/hashCode is delegated to User entity.
	 * This can store either one token or list of them for each user, depending on what you want to do.
	 * Here we store single token, which means, that any older sessions are invalidated.
	 */
	private Map<User, SessionInfo> sessions = new HashMap<>();
	
	@Override
	public SessionInfo createNewSession(Authentication user) {
		String token;
		do {
			token = generateToken();
		} while (validUsers.containsKey(token));

		SessionInfo sessionInfo = new SessionInfo(user, token);
		removeUser((User) user.getPrincipal());
		User previous = validUsers.put(token, (User) user.getPrincipal());
		if (previous != null) {
			log.error(" *** SERIOUS PROBLEM HERE - generated the same token (randomly?)!");
			return null;
		}
		sessions.put((User) user.getPrincipal(), sessionInfo);

		return sessionInfo;
	}
	
	private String generateToken() {
		byte[] tokenBytes = new byte[32];
		new SecureRandom().nextBytes(tokenBytes);
		return new String(Base64.encode(tokenBytes), StandardCharsets.UTF_8);
	}

	@Override
	public void removeUser(User user) {
		
		SessionInfo token = sessions.remove(user);
		if (token != null) {
			validUsers.remove(token.getToken());
		}

	}

	@Override
	public User removeSession(String token) {
		User user = validUsers.remove(token);
		if (user != null) {
			sessions.remove(user);
		}
		return user;
	}

	@Override
	public User getUser(String token) {
		return validUsers.get(token);
	}

	@Override
	public Collection<SessionInfo> getUserSessions(User user) {
		return Arrays.asList(sessions.get(user));
	}

	@Override
	public Map<String, User> getValidUsers() {
		return Collections.unmodifiableMap(validUsers);
	}

	@Override
	public SessionInfo isSessionValid(String Token) {
		User user = validUsers.get(Token);
		if(user == null)
		{
			return null;
		}
		SessionInfo session = sessions.get(user);
		if(session.getCreated() + user.getExpires() > System.currentTimeMillis()){
			session.setValid(true);
			session.setCreated(System.currentTimeMillis());
			return session;
		}
		else{
			session.setValid(false);
			return session;
		}
	}

}
