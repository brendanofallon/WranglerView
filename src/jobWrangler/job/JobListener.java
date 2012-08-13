package jobWrangler.job;

/**
 * Interface for things that listen for job updatess
 * @author brendanofallon
 *
 */
public interface JobListener {

	public void jobUpdated(Job sourceJob);
	
}
