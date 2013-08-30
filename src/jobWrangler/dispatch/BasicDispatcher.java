package jobWrangler.dispatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import jobWrangler.executor.AbstractExecutor;
import jobWrangler.executor.Executor;
import jobWrangler.executor.ExecutorEvent;
import jobWrangler.executor.ExecutorListener;
import jobWrangler.job.Job;
import jobWrangler.job.Job.JobState;
import wranglerView.logging.WLogger;

/**
 * Monitors one or more Executors, maintains a list of jobs that are waiting to be
 * executed, and dispatches jobs to executors when the executor can accept a new job
 * This stores references to all jobs that are waiting or have completed, either successfully
 * or with an error.
 * 
 * This class is currently in singleton form - that is we assume there's just one in existence 
 * (per jvm) at any one time. 
 * 
 * @author brendanofallon
 *
 */
public class BasicDispatcher implements Dispatcher, ExecutorListener {

	//Stores the list of jobs waiting to be executed. None of the jobs in the queue
	//should be running 
	protected Queue<Job> queue = new ConcurrentLinkedQueue<Job>();
	
	//Things that can execute jobs
	protected List<Executor> executors = new ArrayList<Executor>();
	
	//All jobs that have completed, either successfully or not
	protected List<Job> completedJobs = new ArrayList<Job>();
	
	//Stores all jobs that threw an exception during execution
	protected List<Job> errorJobs = new ArrayList<Job>();
	
	
	public BasicDispatcher() {
		//
	}
	
	public void setExecutors(List<Executor> execs) {
		if (getRunningJobCount() > 0) {
			throw new IllegalStateException("Cannot reset executor list while there are running jobs.");
		}
		executors.clear();
		for(Executor exec : execs) {
			exec.addListener(this);
			executors.add(exec);
		}
	}
	
	/**
	 * Return the job associated with the given id
	 * @param id
	 * @return
	 */
	public Job getJobForID(String id) {
		for(Job job : getQueuedJobs()) {
			if (job.getID().equals(id)) {
				return job;
			}
		}
		
		for(Job job : getRunningJobs()) {
			if (job.getID().equals(id)) {
				return job;
			}
		}
		
		for(Job job : getCompletedJobs()) {
			if (job.getID().equals(id)) {
				return job;
			}
		}
	
		return null;
	}
	
	/**
	 * Add a new job to the queue. Initially this job will be waiting, but will at some point
	 * begin executing when an executor becomes available
	 * @param job
	 */
	public void submitJob(Job job) {
		boolean ok = queue.add(job);
		if (!ok) {
			//This should never happen as the queue should have basically infinite capacity
			throw new IllegalStateException("Could not add job to queue!"); 
		}
		
		//System.out.println("Submitting job with id:" + job.getID() + " q size is now : " + getQueueSize());
		WLogger.info("Submitting new job with id: " + job.getID());
		pollExecutors();
	}
	
	/**
	 * Removed a queued but not executing job from the queue. This has no effect is the
	 * job is running or has completed. 
	 * @param job
	 * @return
	 */
	public boolean removeWaitingJob(Job job) {
		return queue.remove(job); 
	}
	
	/**
	 * Attempt to kill a running job. If the job is not running no action is taken 
	 * @param job
	 * @return
	 */
	public void requestKillJob(Job job) {
		if (job.getJobState() != JobState.EXECUTING) {
			WLogger.warn("Request to kill job with id: " + job.getID() + " but job state is : " + job.getJobState());
			return;
		}
		
		boolean found = false;
		for(Executor exec : executors) {
			if (exec.getJobs().contains(job)) {
				found = true;
				WLogger.info("Request to kill job with id: " + job.getID() + " found executor and is attempting to terminate");
				exec.killJob(job);
			}
		}
		
		if (!found) {
			WLogger.warn("Request to kill job with id: " + job.getID() + " but was not found in any executor");
		}
	}
	
	/**
	 * Add a new executor to this dispatcher - the executor will immediately be available
	 * to run jobs
	 * @param exec
	 */
	public void addExecutor(AbstractExecutor exec) {
		if (executors.contains(exec))
			throw new IllegalStateException("Dispatcher already contains the given executor, cannot add it again");
		executors.add(exec);
		exec.addListener(this);
		pollExecutors();
	}
	
	
	/**
	 * Attempt to submit jobs in the queue to the executors. This does nothing if no jobs are
	 * in the queue. Otherwise  we iterate over the executors to see if any can accept
	 * a new job, if so we dispatch a job from the queue to the executor.
	 * Returns true if at least one job was submitted to an executor  
	 */
	public synchronized boolean pollExecutors() {
		
		//System.out.println("Polling, current state is : " + this.toString());
		
		if (getQueueSize()==0)
			return false;
		
		boolean found = false;
		Job nextJob = queue.peek();
		Executor exec = getAvailableExecutor(nextJob);
		if (exec != null) {
			queue.poll();
			exec.runJob(nextJob);
			found = true;
		}
		
		return found;
	}
	
	/**
	 * Returns the first executor found that can accept a new job, or null
	 * if there is no such executor
	 * @return
	 */
	private Executor getAvailableExecutor(Job job) {
		for(Executor exec : executors) {
			if (exec.canSubmitJob(job))
				return exec;
		}
		return null;
	}
	
	/**
	 * Return the number of jobs waiting to be executed
	 * @return
	 */
	public int getQueueSize() {
		return queue.size();
	}
	
	/**
	 * Return the number of jobs currently being executed
	 * @return
	 */
	public int getRunningJobCount() {
		int sum = 0;
		for(Executor exec : executors) {
			sum += exec.getJobCount();
		}
		
		return sum;
	}
	
	
	public List<Job> getRunningJobs() {
		List<Job> runningJobs = new ArrayList<Job>();
		
		for(Executor exec : executors) {
			runningJobs.addAll( exec.getJobs() );
		}
		
		return runningJobs;
	}
	
	@Override
	public void executorUpdated(ExecutorEvent evt) {
		//System.out.println("Executor updated, event type is: " + evt.type + " job is:" + evt.job.getID());
		WLogger.info("Executor update, type: " + evt.type + " job:" + evt.job.getID() );
		if (evt.type == ExecutorEvent.EventType.JOB_FINISHED || evt.type == ExecutorEvent.EventType.JOB_ERROR) {
			Job job = evt.job;
			if (! completedJobs.contains(job)) 
				completedJobs.add(job);
		}
		
		if (evt.type == ExecutorEvent.EventType.JOB_ERROR) {
			if (! errorJobs.contains(evt.job)) {
				errorJobs.add(evt.job);
			}
		}
		pollExecutors();
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		int count = 0;
		str.append("Waiting jobs : " + getQueueSize() +", running jobs: " + getRunningJobCount() + ", completed jobs: " + getCompletedJobCount() + " errors: " + getErrorJobCount() + "\n" );
//		for(Job j : queue) {
//			str.append("Job #" + count + " : " + j.getID() + ", " + j.getJobState() +"\n");
//			count++;
//		}
		return str.toString();
	}

	public String emitState() {
		StringBuilder str = new StringBuilder();
		int count = 0;
		str.append("Waiting jobs : " + getQueueSize() +", running jobs: " + getRunningJobCount() + ", completed jobs: " + getCompletedJobCount() + " errors: " + getErrorJobCount() + "\n" );
		str.append("Waiting jobs: \n");
		for(Job j : queue) {
			str.append("Job #" + count + " : " + j.getID() + ", " + j.getJobState() +"\n");
			count++;
		}
		
		str.append("Completed jobs: \n");
		for(Job j : completedJobs) {
			str.append("Job #" + count + " : " + j.getID() + ", " + j.getJobState() +"\n");
			count++;
		}
		
		str.append("Jobs with errors: \n");
		for(Job j : errorJobs) {
			str.append("Job #" + count + " : " + j.getID() + ", " + j.getJobState() +"\n");
			count++;
		}
		
		return str.toString();
	}
	
	/**
	 * Get the number of jobs that have completed - either successfully or with an error
	 * @return
	 */
	public int getCompletedJobCount() {
		return completedJobs.size();
	}
	
	public Job getCompletedJob(int which) {
		return completedJobs.get(which);
	}
	
	/**
	 * Return a newly allocated list of all jobs in the completed area
	 * @return
	 */
	public List<Job> getCompletedJobs() {
		List<Job> jobs = new ArrayList<Job>();
		jobs.addAll( completedJobs );
		//Collections.reverse(jobs); 
		return jobs;
	}
	
	/**
	 * Returns a (newly instantiated) list of jobs which aer all jobs
	 * waiting to be executed
	 * @return
	 */
	public List<Job> getQueuedJobs() {
		List<Job> jobs = new ArrayList<Job>();
		jobs.addAll( queue );
		//Collections.reverse( jobs );  
		return jobs;
	}
	
	/**
	 * Get the number of jobs that have encoutered an error
	 * @return
	 */
	public int getErrorJobCount() {
		return errorJobs.size();
	}

	public Job getErrorJob(int which) {
		return errorJobs.get(which);
	}
}
