package wranglerView.server;

import java.util.ArrayList;

import jobWrangler.dispatch.Dispatcher;
import jobWrangler.dispatch.DispatcherManager;
import jobWrangler.job.Job;
import jobWrangler.job.Job.JobState;
import wranglerView.client.queueView.QueueSummaryService;
import wranglerView.logging.WLogger;
import wranglerView.shared.QueueSummary;
import wranglerView.shared.QueueSummary.JobInfo;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Queries the Dispatcher to gather a bunch of information about various jobs, running, completed and waiting,
 * and returns it in a QueueSummary object
 * @author brendan
 *
 */
public class QueueSummaryServiceImpl  extends RemoteServiceServlet implements QueueSummaryService {

	@Override
	public QueueSummary getQueueSummary() {
		
		QueueSummary summary = new QueueSummary();
		summary.jobInfo = new ArrayList<JobInfo>();
		
		Dispatcher dispatcher = DispatcherManager.getDispatcher();
		
		for(Job job : dispatcher.getQueuedJobs()) {
			summary.jobInfo.add( makeJobInfo(job) );
		}
		
		for(Job job : dispatcher.getRunningJobs()) {
			summary.jobInfo.add( makeJobInfo( job) );
		}
		
		for(int i=0; i<dispatcher.getCompletedJobCount(); i++) {
			summary.jobInfo.add( makeJobInfo( dispatcher.getCompletedJob(i)) );
		}
		
		return summary;
	}
	
	/**
	 * Construct a JobInfo object that reflects some basic information 
	 * about this job. 
	 * @param job
	 * @return
	 */
	private JobInfo makeJobInfo(Job job) {
		JobInfo info = new JobInfo();
		
		if (job instanceof WranglerJob) {
			WranglerJob wj = (WranglerJob)job;
			info.sampleName = wj.getSampleName();
			info.analysisType = wj.getAnalysisType();
			info.submitter = wj.getSubmitter();
			info.creationTime = "" + wj.getCreationDate();
		}
		

		info.status = stateToString( job.getJobState() );
		
		if (job.getJobInfo().getStartTime() != null)
			info.startTime = "" + job.getJobInfo().getStartTime();
		else 
			info.startTime = " ? ";
		
		if (job.getJobInfo().getEndTime() != null)
			info.endTime = "" + job.getJobInfo().getEndTime();
		else 
			info.endTime = " ? ";
		
		
		info.jobID = job.getID();
		if (job.getException() != null) {
			//WLogger.warn("Queue summary found a job with an exception set..\n job id: " + job.getID() + "\n exception msg: " + job.getException().getMessage());
			info.errorMsg = job.getException().getLocalizedMessage();
		}
				
		return info;
	}
	
	private static String stateToString(JobState state) {
		if (state == JobState.UNINITIALIZED) {
			return "Waiting";
		}
		if (state == JobState.INITIALIZING) {
			return "Initializing";
		}
		if (state == JobState.EXECUTING) {
			return "Running";
		}
		if (state == JobState.FINISHED_SUCCESS) {
			return "Completed without error";
		}
		if (state == JobState.FINISHED_ERROR) {
			return "Error";
		}
		return state.name();
	}

}
