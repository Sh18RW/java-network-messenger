package ru.sh18rw.network;

import java.io.Serializable;

public abstract class UserId implements Serializable {
	protected final String username;
	
	public UserId(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
}
