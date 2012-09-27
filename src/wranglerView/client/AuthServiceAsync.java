package wranglerView.client;

import wranglerView.shared.AuthToken;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuthServiceAsync {

	void authenticate(String username, String password, AsyncCallback<AuthToken> token);
}
