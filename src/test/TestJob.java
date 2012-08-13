package test;

import jobWrangler.job.ExecutionFailedException;
import jobWrangler.job.Job;


public class TestJob extends Job {

	public int waitTime = 5000;
	
	public TestJob(int msToWait) {
		this.waitTime = msToWait;
	}
	
	@Override
	protected void execute() throws ExecutionFailedException {
		
		
		boolean immediateError = Math.random() < 0.10;
		if (immediateError) {
			System.out.println("Job " + getID() + " is throwing an exception...");
			throw new ExecutionFailedException("Dang! Job with id : " + getID() + " has an immediate error", this);
		}
		boolean willError = Math.random() < 0.20;
		if (willError) {
			System.out.println("Test job " + getID() + " is executing..but will throw an exception in a while");
		}
		else {
			System.out.println("Test job " + getID() + " is executing!");
		}
		
		
		try {
			if (willError) {
				Thread.sleep(waitTime / 2);
				System.out.println("Job " + getID() + " is throwing an exception...");
				throw new ExecutionFailedException("Dang! Job with id : " + getID() + " has errored out", this);
			}
			else {
				Thread.sleep(waitTime);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Test job " + getID() + " is done executing");
	}

}
