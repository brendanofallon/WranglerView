package jobWrangler.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import jobWrangler.job.Job;
import jobWrangler.job.Job.JobState;
import jobWrangler.job.JobListener;
import jobWrangler.job.JobMonitor;
import wranglerView.logging.WLogger;

/**
 * This type of executor runs only a single job at a time
 * @author brendanofallon
 *
 */
public class SingleJobExecutor extends AbstractExecutor implements JobListener {

	private RunningJob runner = null;
	private Future<?> jobFuture = null;
	ExecutorService pool = Executors.newSingleThreadExecutor();
	
	@Override
	public boolean canSubmitJob(Job job) {
		if (runner != null) {
			jobUpdated(runner.job);
		}
		return runner == null;
	}

	/**
	 * Returns the currently executing job, or null if there is no such job
	 * @return
	 */
	public Job getCurrentJob() {
		if (runner == null) {
			return null;
		}
		else {
			return runner.getJob();
		}
	}
	
	@Override
	public void runJob(Job job) {
		JobMonitor monitor = new JobMonitor(job);
		monitor.addListener(this);
		monitor.startMonitoring();
		runner = new RunningJob(job);
		jobFuture = pool.submit(runner);
		fireEvent(new ExecutorEvent(job, ExecutorEvent.EventType.JOB_STARTED));
	}

	@Override
	public void jobUpdated(Job job) {
		//This gets called from thread in which job is running, so maybe we shouldn't 
		//really do anything here
		
		JobState state = job.getJobState();
		if (state == JobState.FINISHED_ERROR ) {
			Exception ex = job.getException();
			releaseJob();
			fireEvent(new ExecutorEvent(job, ExecutorEvent.EventType.JOB_ERROR, ex));
		} else {
			if (state == JobState.FINISHED_SUCCESS) {
				releaseJob();
				fireEvent(new ExecutorEvent(job, ExecutorEvent.EventType.JOB_FINISHED));
			}	
		}
		
	}

	/**
	 * Called when a job has completed to set the runner back to null
	 */
	private void releaseJob() {
		runner = null;
		if (jobFuture != null) {
			jobFuture.cancel(true);
		}
		jobFuture = null;
	}

	@Override
	public void killJob(Job job) {
		if (runner != null && jobFuture != null) {
			if (runner.getJob() == job) {
				WLogger.info("SingleJobExecutor is killing job with id: " + job.getID() );
				job.killJob();
				releaseJob();
			}
		}
		else {
			WLogger.warn("SingleJobExecutor has no runner, cannot try to kill job " + job.getID());
		}
	}
	
	@Override
	public int getJobCount() {
		if (getCurrentJob()==null)
			return 0;
		else
			return 1;
	}

	@Override
	public List<Job> getJobs() {
		List<Job> list = new ArrayList<Job>();
		
		if (getCurrentJob() != null)
			list.add( getCurrentJob() );
		return list;
	}



}
