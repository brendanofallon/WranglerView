package wranglerView.server;

import wranglerView.client.AuthService;
import wranglerView.logging.WLogger;
import wranglerView.server.auth.PasswordStore;
import wranglerView.shared.AuthToken;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Implementation of authentication, check username and passwd against db 
 * @author brendan
 *
 */
public class AuthServiceImpl extends RemoteServiceServlet implements AuthService {

	@Override
	public AuthToken authenticate(String username, String password) {
	
		WLogger.info("Authentication attempt for user: " + username );
		boolean authOK = PasswordStore.checkPassword(username, password);
		if (authOK) {
			WLogger.info("User: " + username + " authenticated successfully");
			AuthToken token = new AuthToken();
			token.setUsername(username);
			token.setStartTime(System.currentTimeMillis());
			return token;
		}
		else {
			WLogger.info("User: " + username + " access denied");
		}
		return null;
	}

}
