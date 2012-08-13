package jobWrangler.executor;

import jobWrangler.job.Job;
import jobWrangler.job.JobListener;
import jobWrangler.job.JobMonitor;
import jobWrangler.job.Job.JobState;

/**
 * This type of executor runs only a single job at a time
 * @author brendanofallon
 *
 */
public class SingleJobExecutor extends Executor implements JobListener {

	private RunningJob runner = null;
	
	@Override
	public boolean canSubmitJob(Job job) {
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
		runner.execute(); //Job will begin executing in background
		
		fireEvent(new ExecutorEvent(job, ExecutorEvent.EventType.JOB_STARTED));		
	}

	@Override
	public void jobUpdated(Job job) {
		//This gets called from thread in which job is running, so maybe we shouldn't 
		//really do anything here
		
		JobState state = job.getJobState();
		System.out.println("Executor is hearing that job status is now : " + state);
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
		if (runner == null) {
			throw new IllegalStateException("Huh? Release job has been called, but runner is null");
		}
		else {
			if (runner.isDone()) {
				System.out.println("Running job " + runner.job.getID() + " has finished and thread is done, releasing");
				runner = null;
			}
			else {
				runner.cancel(true);
				System.out.println("Running job " + runner.job.getID() + " has finished and but thread is not done, canceling thread...");
				runner = null;
			}
		}
	}

	@Override
	public int getJobCount() {
		if (getCurrentJob()==null)
			return 0;
		else
			return 1;
	}

}
