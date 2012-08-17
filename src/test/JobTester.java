package test;

import jobWrangler.job.ShellJob;


public class JobTester {

	public static void main(String[] args) {
		ShellJob sj;
		sj = new SleeperJob(8);
		sj.runJob();
		
	}
}
