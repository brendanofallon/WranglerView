package wranglerView.server;

import java.io.File;

import jobWrangler.job.InitializationFailedException;
import wranglerView.logging.WLogger;

/**
 * Encapsulates a Marc-style job, which uses ngs_master_aligner and a config file
 * to describe an analysis
 * 
 * @author brendan
 *
 */
public class MarcJob extends WranglerJob {

	public final String pathToMasterAligner = "/mnt/storage1/genomics/alignment/ngs_master_aligner";
	
	public MarcJob(File projHome,
			String pathToConfigFile, 
			String sampleName, 
			String submitter, 
			String fastq1, 
			String fastq2) {
		
		this.setBaseDir(projHome);
		String command = "perl " + pathToMasterAligner + " -c " + pathToConfigFile + " -u " + submitter + " -r " + sampleName + " " + fastq1 + " " + fastq2;
		setCommand(command);
		setSampleName(sampleName);
		setSubmitter(submitter);
		
		WLogger.info("Creating new Marc job with command : " + getCommand());
	}
	
	/**
	 * Copy pipeline to project home
	 */
	protected void initialize() throws InitializationFailedException {
		WLogger.info("Marc job " + getID() + " is attempting to initialize");
		//Create base directory and initialize process builder with command
		super.initialize();
				
		WLogger.info("Marc job " + getID() + " has initialized successfully");
	}
}
