package jobWrangler.job;

import java.util.Date;

import jobWrangler.util.IDGenerator;


/**
 * A single executing task, associated with some meta data provided by a JobInfo
 * object. State information is available through a JobState object
 * @author brendanofallon
 *
 */
public abstract class Job {

	public enum JobState {
		UNINITIALIZED, //Job has not been initialized (initialize has not been called yet)
		INITIALIZING,   //Initialize has been called, but has not yet completed
		EXECUTING,	   //Job is currently executing
		FINISHED_SUCCESS, //Job has finished without error
		FINISHED_ERROR  //Job has finished but an error was encountered
	}
	protected JobMonitor monitor = null;
	protected JobInfo info = new JobInfo(this);
	protected JobState state = JobState.UNINITIALIZED;
	protected final String id = IDGenerator.generateID(10); //Unique id for this job
	protected Exception ex = null; //If an exception is thrown, we keep track of it here. 
	
	
	
	/**
	 * Return a unique id associated with this job
	 * @return
	 */
	public String getID() {
		return id;
	}
	
	/**
	 * The monitor object associated with this job. May be null;
	 * @return
	 */
	public JobMonitor getMonitor() {
		return monitor;
	}

	public void removeMonitor() {
		this.monitor = null;
	}
	
	public void setMonitor(JobMonitor monitor) {
		if (this.monitor != null)
			throw new IllegalStateException("A monitor is already associated with this object");
		this.monitor = monitor;
	}

	/**
	 * Initialize, execute, and then dispose of this job
	 * @throws InitializationFailedException 
	 * @throws ExecutionFailedException 
	 */
	public void runJob() {
		try {
			info.setStartTime(new Date());
			updateState(JobState.INITIALIZING);
			initialize();
			updateState(JobState.EXECUTING);
			execute();
			cleanUp();
			if (monitor != null) {
				monitor.stopMonitoring();
			}
			updateState(JobState.FINISHED_SUCCESS);
		}
		catch (Exception ex) {
			System.out.println("Caught exception " + ex + " for job " + this.getID());
			this.ex = ex;
			if (monitor != null) {
				monitor.stopMonitoring();
			}
			updateState(JobState.FINISHED_ERROR);
		}
		info.setEndTime(new Date());
		if (monitor != null) {
			monitor.stopMonitoring();
		}
	}
	
	/**
	 * Open files and perform validity checks for this job
	 */
	protected void initialize() throws InitializationFailedException {
		//Called before execute, default is no-op
	}
	
	
	/**
	 * Perform some long-running task
	 */
	protected abstract void execute() throws ExecutionFailedException;

	/**
	 * Release resources and close streams associated with this task
	 */
	protected void cleanUp() {
		//Called when execution has completed, default is no-op
	}
	
	public void killJob() {
		//Called prior to forcible termination of the job. Do some cleanup here if need be
	}
	
	/**
	 * Get the current state of the this job
	 * @return
	 */
	public JobState getJobState() {
		return state;
	}
	
	public JobInfo getJobInfo() {
		return info;
	}
	
	/**
	 * Obtain an exception thrown during runJob(), if there was one. Hopefully this will be null...
	 * @return
	 */
	public Exception getException() {
		return ex;
	}
	
	/**
	 * Set the job state to the newly given state. Does nothing if state
	 * is equal to current state
	 * @param newState
	 */
	protected void updateState(JobState newState) {
			state = newState;
			if (monitor != null) {
				monitor.fireJobUpdate();
			}
	}
}
