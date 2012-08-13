package test;

import jobWrangler.executor.ExecutorEvent;
import jobWrangler.executor.ExecutorListener;


public class DebugListener implements ExecutorListener {

	@Override
	public void executorUpdated(ExecutorEvent evt) {
		System.out.println("Got event : " + evt.type + " from job : " + evt.job.getID() + ", whose state is " + evt.job.getJobState());
	}

}
