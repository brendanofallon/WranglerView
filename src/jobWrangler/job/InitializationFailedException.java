package jobWrangler.job;

/**
 * These get thrown when a job fails to initialize
 * @author brendanofallon
 *
 */
public class InitializationFailedException extends Exception {

	private Job sourceJob = null;
	
	public InitializationFailedException(String message, Job source) {
		this.sourceJob = source;
	}
	
	public Job getSourceJob() {
		return sourceJob;
	}
}
