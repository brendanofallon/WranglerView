package jobWrangler.executor;

import java.util.ArrayList;
import java.util.List;

import jobWrangler.job.Job;

/**
 * Simple base class for Executors with listener handling built in. 
 * @author brendanofallon
 *
 */
public abstract class AbstractExecutor implements Executor {

	protected List<ExecutorListener> listeners = new ArrayList<ExecutorListener>();
	
	/**
	 * The number of jobs currently contained in this executor
	 * @return
	 */
	public abstract int getJobCount();
	
	/**
	 * Should return true if we can immediately begin running the given job
	 * @param job
	 * @return
	 */
	public abstract boolean canSubmitJob(Job job);
	
	/**
	 * Return a list of jobs currently running in this executor
	 * @param job
	 * @return
	 */
	public abstract List<Job> getJobs();
	
	/**
	 * Attempt to immediately begin running the given job. Jobs may be rejected, for
	 * instance, if something else submitted a job right before the call did
	 * @param job
	 */
	public abstract boolean runJob(Job job);
	
	/**
	 * Attempt to immediately terminate the given job
	 * @param job
	 */
	public abstract void killJob(Job job);
	
	public void addListener(ExecutorListener l) {
		listeners.add(l);
	}
	
	public boolean removeListener(ExecutorListener l) {
		return listeners.remove(l);
	}
	
	protected void fireEvent(ExecutorEvent evt) {
		for(ExecutorListener l : listeners) {
			l.executorUpdated(evt);
		}
	}
	
	
	class RunningJob implements Runnable {

		final Job job;
		private boolean done  = false;
		
		public RunningJob(Job job) {
			this.job = job;
		}
		
		public Job getJob() {
			return job;
		}

		@Override
		public void run() {
			job.runJob();
			done = true;
		}
		
		public boolean isDone() {
			return done;
		}
		
		
	}
	
}
