package wranglerView.client.jobSubmission;

import wranglerView.shared.AnalysisJobDescription;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SubmissionServiceAsync {

	public void submitJob(AnalysisJobDescription desc, AsyncCallback<String> hmmm);

}
