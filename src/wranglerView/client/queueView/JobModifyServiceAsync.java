package wranglerView.client.queueView;

import wranglerView.shared.JobModifyRequest;
import wranglerView.shared.JobModifyResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface JobModifyServiceAsync {
	
	public void modifyJob(JobModifyRequest req, AsyncCallback<JobModifyResult> res);
	
}
