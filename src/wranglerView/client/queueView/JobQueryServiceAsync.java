package wranglerView.client.queueView;

import wranglerView.shared.JobQueryResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface JobQueryServiceAsync {

	public void queryJob(String id, AsyncCallback<JobQueryResult> callback);
	
}
