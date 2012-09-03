package wranglerView.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jobWrangler.dispatch.Dispatcher;
import jobWrangler.dispatch.DispatcherManager;
import jobWrangler.job.Job;
import jobWrangler.job.ShellJob;
import wranglerView.client.queueView.JobQueryService;
import wranglerView.logging.WLogger;
import wranglerView.shared.JobQueryResult;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class JobQueryServiceImpl  extends RemoteServiceServlet implements JobQueryService {

	@Override
	public JobQueryResult queryJob(String id) {
		
		//WLogger.info("Attempting to query job id: " + id);
		Dispatcher dispatcher = DispatcherManager.getDispatcher();
		JobQueryResult result = null;
		
		for(Job job : dispatcher.getQueuedJobs()) {
			if (job.getID().equals(id)) {
				result = buildResultForJob(job);
			}
		}
		
		for(Job job : dispatcher.getRunningJobs()) {
			if (job.getID().equals(id)) {
				result = buildResultForJob(job);
			}
		}
		
		for(int i=0; i<dispatcher.getCompletedJobCount(); i++) {
			if (dispatcher.getCompletedJob(i).getID().equals(id)) {
				result = buildResultForJob(dispatcher.getCompletedJob(i));
			}
		}

		if (result == null) {
			WLogger.warn("Could not find any job for id: " + id);
		}

		
		return result;
	}

	private JobQueryResult buildResultForJob(Job job) {
		JobQueryResult result = new JobQueryResult();
		result.statusVals = new HashMap<String, String>();
		
		result.status = job.getJobState().name();
		
		if (job instanceof WranglerJob) {
			WranglerJob wJob = (WranglerJob)job;
			
			result.statusVals.put("Submitter", wJob.getSubmitter() );
			result.statusVals.put("Sample", wJob.getSampleName() );
			result.statusVals.put("Analysis type", wJob.getAnalysisType() );	
			result.statusVals.put("Submission time", wJob.getCreationDate().toString());
		}
		

		if (job.getJobInfo().getStartTime() != null)
			result.statusVals.put("Start time", job.getJobInfo().getStartTime().toString() );
		if (job.getJobInfo().getEndTime() != null)
			result.statusVals.put("End time", job.getJobInfo().getEndTime().toString() );

		
		if (job instanceof ShellJob) {
			ShellJob shJob = (ShellJob)job;
			if (shJob.getBaseDir() != null) {
				result.statusVals.put("Project home", shJob.getBaseDir().getName() );
				readStatusFile(result.statusVals, shJob.getBaseDir());
			}
		}
		
		
		
		return result;
	}

	private void readStatusFile(Map<String, String> vals, File baseDir) {
		File statusFile = new File(baseDir.getAbsolutePath() + "/wrangler.status.txt");
		if (statusFile != null) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(statusFile));
				String line= reader.readLine();
				while(line != null) {
					String[] toks = line.split("=");
					if (toks.length==2) {
						vals.put(toks[0], toks[1]);
					}
					line = reader.readLine();
				}
				reader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
