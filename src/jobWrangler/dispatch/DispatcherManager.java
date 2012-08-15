package jobWrangler.dispatch;

import jobWrangler.executor.SingleJobExecutor;

/**
 * A few static methods that provide some initalization features for the Dispatcher.
 * Right now, this just means creating a SingleJobExecutor and attaching it to the
 * Dispatcher. In the future we may add more functionality, for instance reading
 * executor specs from a file and creating them so we can have a multi-executor dispatcher
 * running.
 *  
 * @author brendan
 *
 */
public class DispatcherManager {

	
	private static Dispatcher dispatcher = null;
	
	/**
	 * Obtain a reference to the primary Dispatcher instance. If one has not been created
	 * it will be instantiated, then returned. 
	 * @return
	 */
	public static Dispatcher getDispatcher() {
		if (dispatcher == null) {
			dispatcher = new Dispatcher();
			intializeDispatcher();
		}
		
		return dispatcher;
	}

	/**
	 * Adds one or more executors to the dispatcher
	 */
	private static void intializeDispatcher() {
		SingleJobExecutor exec = new SingleJobExecutor();
		dispatcher.addExecutor( exec );
	}
	
}
