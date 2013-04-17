package wranglerView.server.auth;

import wranglerView.shared.AuthToken;

/**
 * Interface for objects that can produce an authentication token. 
 * @author brendan
 *
 */
public interface AuthenticatorHandler {

	/**
	 * Try to log in the given user. If successful, return a valid token, if not, return null.
	 * @param username
	 * @param password
	 * @return
	 */
	public AuthToken attemptLogin(String username, String password);
	
}
