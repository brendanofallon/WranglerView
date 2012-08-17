package test;

import jobWrangler.job.ShellJob;

/**
 * a quick test job for ShellJobs - just sleeps for a while, then writes something to standard out
 * @author brendan
 *
 */
public class SleeperJob extends ShellJob {

	private static int defaultSleepLength = 10;
	
	public SleeperJob(int length) {
		super(""); //cant refer to ID in constructor, i guess
		setCommand("sleep " + length + " && echo 'sleeper job " + getID() + " has completed'");
	}
	
	public SleeperJob() {
		super("");
		setCommand("sleep " + defaultSleepLength + " && echo 'sleeper job " + getID() + " has completed'");
	}
	
	
}
