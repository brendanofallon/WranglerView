package wranglerView.server.jobSubmission;

import jobWrangler.dispatch.Dispatcher;
import jobWrangler.job.Job;
import jobWrangler.job.ShellJob;

import org.springframework.context.ApplicationContext;

import wranglerView.client.jobSubmission.SubmissionService;
import wranglerView.logging.WLogger;
import wranglerView.server.SpringContext;
import wranglerView.server.WranglerProperties;
import wranglerView.shared.AnalysisJobDescription;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Turns an AnalysisJobDescription into a Job and then submits it to a Dispatcher. 
 * @author brendan
 *
 */
public class SubmissionServiceImpl extends RemoteServiceServlet implements SubmissionService{

	public static final String defaultProjectRoot = WranglerProperties.getExecutionDirPath(); //
	
	private JobBuilder builder = null;
	private Dispatcher dispatcher = null;
	
	@Override
	public String submitJob(AnalysisJobDescription jobDesc) throws IllegalArgumentException {
		WLogger.info("Creating a new job with sample: " + jobDesc.sampleName +" fastqdir: " + jobDesc.pathToFastQDir + " analysis id:" + jobDesc.templateID);
	
		if (builder == null) {
			ApplicationContext context = SpringContext.getContext();
			builder = (JobBuilder) context.getBean("jobBuilder");
		}	

		if (dispatcher == null) {	
			ApplicationContext context = SpringContext.getContext();
			dispatcher = (Dispatcher) context.getBean("dispatcher");
		}
		
		Job jobToSubmit = null;
		if (builder != null) {
			WLogger.info("JobBuilder " + builder.getClass().getCanonicalName() + " is building job for sample: " + jobDesc.sampleName +" fastqdir: " + jobDesc.pathToFastQDir + " analysis id:" + jobDesc.templateID);
			jobToSubmit = builder.createJob(jobDesc);
		}
		
		if (jobToSubmit == null) {
			WLogger.severe("Failed to create job for sample " + jobDesc.sampleName + "! Cannot submit job");
			throw new IllegalArgumentException("Error creating job, jobBuilder did not create a valid job");
		}
		
		if (jobToSubmit != null && dispatcher != null) {
			WLogger.info("Submitting job " + jobToSubmit.getID() + " for sample " + jobDesc.sampleName + " to dispatcher");
			dispatcher.submitJob(jobToSubmit);		
		}
		
		
		String homeDir = "unknown";
		if(jobToSubmit instanceof ShellJob) {
			homeDir = ((ShellJob)jobToSubmit).getBaseDir().getName();
		}
		
		return homeDir;
	}


}
