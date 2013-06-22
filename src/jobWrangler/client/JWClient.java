package jobWrangler.client;

public interface JWClient {

	
	
	/**
	 * Request that this client send its status to the server
	 */
	public void requestStatus();
	
	
	public void submitPipelineJob(String pplInputDocument);
	
	
}
