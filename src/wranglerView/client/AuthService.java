package wranglerView.client;

import wranglerView.shared.AuthToken;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("auth")
public interface AuthService extends RemoteService {
	
	AuthToken authenticate(String username, String password);

}
