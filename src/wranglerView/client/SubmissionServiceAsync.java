package wranglerView.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SubmissionServiceAsync {
	
	void submitJob(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;

}
