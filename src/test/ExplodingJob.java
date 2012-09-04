package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import wranglerView.server.WranglerProperties;

import jobWrangler.job.ExecutionFailedException;
import jobWrangler.job.Job;
import jobWrangler.job.ShellJob;

/**
 * This type of job just waits a while and then throws an exception
 * @author brendanofallon
 *
 */
public class ExplodingJob extends ShellJob {

	public ExplodingJob() {
		super(""); //We dont actually execute any command
	}
	
	@Override
	protected void execute() throws ExecutionFailedException {
		int secs = (int)Math.round( 10.0*Math.random()+1 );
		System.out.println("Waiting for " + secs + " seconds...");
		try {
			Thread.sleep( secs * 1000 );
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File file = new File(this.getBaseDir() + "/wrangler.status.txt");
		
		try {
			System.out.println("Creating file : " + file.getAbsolutePath());
			file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write("error=The job exploded\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		throw new ExecutionFailedException("Ahh! Exception", this);
	}

	

}
