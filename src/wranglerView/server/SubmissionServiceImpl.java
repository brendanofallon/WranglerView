package wranglerView.server;

import wranglerView.client.SubmissionService;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@RemoteServiceRelativePath("submission")
public class SubmissionServiceImpl extends RemoteServiceServlet implements SubmissionService{

	@Override
	public String submitJob(String jobInfo) throws IllegalArgumentException {
		System.out.println("Job is gettin submitted!");
		return null;
	}

}
