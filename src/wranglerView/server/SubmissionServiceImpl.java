package wranglerView.server;

import java.io.File;
import java.io.IOException;

import jobWrangler.dispatch.Dispatcher;
import jobWrangler.dispatch.DispatcherManager;
import jobWrangler.job.ShellJob;
import wranglerView.client.jobSubmission.SubmissionService;
import wranglerView.shared.AnalysisJobDescription;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SubmissionServiceImpl extends RemoteServiceServlet implements SubmissionService{

	@Override
	public void submitJob(AnalysisJobDescription jobDesc) throws IllegalArgumentException {
		System.out.println("Job is gettin submitted!");
		
		Dispatcher dispatcher = DispatcherManager.getDispatcher();
		try {
			System.out.println("Creating job");
			ShellJob job = new ShellJob("touch newjobfile.txt", new File( System.getProperty("user.dir")));
			System.out.println("Submitting job...");
			dispatcher.submitJob(job);
			System.out.println("Job submitted");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
