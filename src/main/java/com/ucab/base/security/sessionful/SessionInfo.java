package com.ucab.base.security.sessionful;

import java.util.Date;

import org.springframework.security.core.Authentication;

public final class SessionInfo {
	
	private long created = System.currentTimeMillis();
	private final String token;
	private final Authentication authUser;
	private boolean isValid;

	public SessionInfo(Authentication authUser ,String token) {
		this.authUser = authUser;
		this.token = token;
		isValid = true;
	}

	public String getToken() {
		return token;
	}

	public long getCreated() {
		return created;
	}
	
	public void setCreated(long created) {
		this.created = created;
	}

	public Authentication getAuthUser() {
		return authUser;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	@Override
	public String toString() {
		return "SessionInfo{" +
			"token='" + token + '\'' +
			", User" + authUser.getName() +
			", created=" + new Date(created) +
			'}';
	}

}
