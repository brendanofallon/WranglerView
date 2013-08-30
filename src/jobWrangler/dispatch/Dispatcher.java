package jobWrangler.dispatch;

import java.util.List;

import jobWrangler.executor.AbstractExecutor;
import jobWrangler.job.Job;

/**
 * These things monitor one or more executors as well as a list of Jobs, and push 
 * jobs to executors when one becomes available. 
 * @author brendan
 *
 */
public interface Dispatcher {

	/**
	 * Return the job associated with the given id
	 * @param id
	 * @return
	 */
	public Job getJobForID(String id);
	
	
	/**
	 * Add a new job to the queue. Initially this job will be waiting, but will at some point
	 * begin executing when an executor becomes available
	 * @param job
	 */
	public void submitJob(Job job);
	
	/**
	 * Removed a queued but not executing job from the queue. This has no effect is the
	 * job is running or has completed. 
	 * @param job
	 * @return
	 */
	public boolean removeWaitingJob(Job job);
	
	/**
	 * Attempt to kill a running job. If the job is not running no action is taken 
	 * @param job
	 * @return
	 */
	public void requestKillJob(Job job);
	
	/**
	 * Add a new executor to this dispatcher - the executor will immediately be available
	 * to run jobs
	 * @param exec
	 */
	public void addExecutor(AbstractExecutor exec);
	
	/**
	 * Return the number of jobs waiting to be executed
	 * @return
	 */
	public int getQueueSize();
	
	/**
	 * Return the number of jobs currently being executed
	 * @return
	 */
	public int getRunningJobCount();
	
	
	public List<Job> getRunningJobs();
	
	/**
	 * Return a newly allocated list of all jobs in the completed area
	 * @return
	 */
	public List<Job> getCompletedJobs();
	
	/**
	 * Get the number of jobs that have completed - either successfully or with an error
	 * @return
	 */
	public int getCompletedJobCount();
	
	public Job getCompletedJob(int which);
	
	/**
	 * Get the number of jobs that have encoutered an error
	 * @return
	 */
	public int getErrorJobCount();

	public Job getErrorJob(int which);


	/**
	 * Get list of jobs waiting to execute
	 * @return
	 */
	public List<Job> getQueuedJobs();
}
