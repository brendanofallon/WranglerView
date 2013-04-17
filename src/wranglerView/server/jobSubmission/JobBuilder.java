package wranglerView.server.jobSubmission;

import jobWrangler.job.Job;
import wranglerView.shared.AnalysisJobDescription;

public interface JobBuilder {

	public Job createJob(AnalysisJobDescription desc);
	
}
