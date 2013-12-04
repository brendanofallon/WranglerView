package wranglerView.server.auth;

import org.springframework.context.ApplicationContext;

import wranglerView.client.AuthService;
import wranglerView.logging.WLogger;
import wranglerView.server.SpringContext;
import wranglerView.shared.AuthToken;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Implementation of authentication, check username and passwd against db 
 * @author brendan
 *
 */
public class AuthServiceImpl extends RemoteServiceServlet implements AuthService {

	AuthenticatorHandler authenticator = null;
	
	@Override
	public AuthToken authenticate(String username, String password) {

		WLogger.info("Authentication attempt for user: " + username );

		//If the authenticator has not been initialized, try to initialize it. 
		if (authenticator == null) {
			ApplicationContext context = SpringContext.getContext();
			authenticator = (AuthenticatorHandler) context.getBean("authenticator");
		}	
			
		if (authenticator != null) {
			AuthToken token = authenticator.attemptLogin(username, password);
			
			if (token != null) {
				WLogger.info("User: " + username + " authenticated successfully");
			}
			else {
				WLogger.info("User: " + username + " access denied");	
			}
			return token;
		}
		
		return null;
	}

}
