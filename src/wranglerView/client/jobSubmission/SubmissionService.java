package wranglerView.client.jobSubmission;

import wranglerView.shared.AnalysisJobDescription;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("submission")
public interface SubmissionService extends RemoteService {
	
	public String submitJob(AnalysisJobDescription jobDesc);

}
