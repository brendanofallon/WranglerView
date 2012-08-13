package test;

import java.io.File;
import java.io.IOException;

import jobWrangler.job.ShellJob;


public class JobTester {

	public static void main(String[] args) {
		ShellJob sj;
		try {
			sj = new ShellJob("echo \"Hello everyone!!\"", new File("/Users/brendanofallon/"));

			sj.runJob();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
