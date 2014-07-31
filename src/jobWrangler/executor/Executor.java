package jobWrangler.executor;

import java.util.List;

import jobWrangler.job.Job;

public interface Executor {
	
	/**
	 * The number of jobs currently contained in this executor
	 * @return
	 */
	public int getJobCount();
	
	/**
	 * Should return true if we can immediately begin running the given job
	 * @param job
	 * @return
	 */
	public boolean canSubmitJob(Job job);
	
	/**
	 * Return a list of jobs currently running in this executor
	 * @param job
	 * @return
	 */
	public List<Job> getJobs();
	
	/**
	 * Attempt to immediately begin running the given job. Jobs may be rejected, for
	 * instance, if something else submitted a job right before the call did
	 * @param job
	 */
	public boolean runJob(Job job);
	
	/**
	 * Attempt to immediately terminate the given job
	 * @param job
	 */
	public void killJob(Job job);
	
	public void addListener(ExecutorListener l);
	
	public boolean removeListener(ExecutorListener l);
	
}
