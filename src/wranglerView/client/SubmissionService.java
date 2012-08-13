package wranglerView.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("submission")
public interface SubmissionService extends RemoteService {
	
	String submitJob(String jobInfo) throws IllegalArgumentException;
	
}
