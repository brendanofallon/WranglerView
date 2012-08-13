package jobWrangler.job;

/**
 * These get thrown when something fails during execution (as opposed to initialization)
 * @author brendanofallon
 *
 */
public class ExecutionFailedException extends Exception {

	private Job sourceJob = null;
	
	public ExecutionFailedException(String message, Job job) {
		super(message);
		this.sourceJob = job;
	}
	
	public Job getSourceJob() {
		return sourceJob;
	}
}
