package jobWrangler.executor;

import jobWrangler.job.Job;

/**
 * Stores information about various events that may occur to an executor, 
 * usually having to do with job starting, finishing, or encountering errors
 * @author brendanofallon
 *
 */
public class ExecutorEvent {

	public enum EventType {
		JOB_STARTED,
		JOB_FINISHED,
		JOB_ERROR
	}
	
	public ExecutorEvent(Job job, EventType type) {
		this.job = job;
		this.type = type;
	}
	
	public ExecutorEvent(Job job, EventType type, Exception ex) {
		this(job, type);
		this.ex = ex;
	}
	
	public EventType type = null; //Basic type of event that has occurred
	public Exception ex = null; //Exceptions associated with event, may be null
	public Job job = null; //Job associated with event
}
