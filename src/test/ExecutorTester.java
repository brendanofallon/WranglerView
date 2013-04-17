package test;

import jobWrangler.dispatch.Dispatcher;
import jobWrangler.dispatch.DispatcherManager;
import jobWrangler.executor.AbstractExecutor;
import jobWrangler.executor.SingleJobExecutor;
import jobWrangler.job.Job;


public class ExecutorTester {

	public static void main(String[] args) {
		
		Dispatcher dispatch = DispatcherManager.getDispatcher();
		AbstractExecutor exec = new SingleJobExecutor();
		dispatch.addExecutor(exec);
		
		DebugListener listener = new DebugListener();
		exec.addListener(listener);
		
		
		for(int i=0; i<20; i++) {
			int length = (int)(10.0*Math.random());
			System.out.println("Submitting job #" + i + " with length : " + length);
			Job job = new SleeperJob(length);
			dispatch.submitJob(job);
			
			int waitLength = (int)(5000*Math.random());
			try {
				System.out.println("Waiting " + waitLength + " until submitting more jobs...");
				System.out.println( "Current state is : " + dispatch.emitState() );
				Thread.sleep(waitLength);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		while(dispatch.getQueueSize() > 0 || dispatch.getRunningJobCount() > 0) {
			System.out.println( dispatch.emitState() );
			try {
				Thread.sleep(673);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		System.out.println("All jobs have been processed, dispatcher has completed : " + dispatch.getCompletedJobCount() + " jobs");
	}
	
	
}
