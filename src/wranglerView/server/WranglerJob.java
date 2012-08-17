package wranglerView.server;

import jobWrangler.job.InitializationFailedException;
import jobWrangler.job.ShellJob;

/**
 * Type of Job created by the JobWrangler web interface that provides some additional
 * info about sample name, submitter, etc. 
 * @author brendan
 *
 */
public abstract class WranglerJob extends ShellJob {

	String sampleName = null;
	String analysisType = null;
	String submitter = null;
	
	public WranglerJob() {
		super("");	
	}



	public String getSampleName() {
		return sampleName;
	}
	
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}
	
	public String getAnalysisType() {
		return analysisType;
	}
	
	public void setAnalysisType(String analysisType) {
		this.analysisType = analysisType;
	}
	
	public String getSubmitter() {
		return submitter;
	}
	
	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}
	
	
	
	/**
	 * Create project "base" directory, aka projectHome for pipeline jobs
	 */
	protected void initialize() throws InitializationFailedException {
		if (baseDir != null) {
			boolean ok = baseDir.mkdir();
			if (! ok ) {
				throw new InitializationFailedException("Could not create project base directory " + baseDir.getAbsolutePath(), this);
			}
		}
		else {
			throw new InitializationFailedException("No project directory specified (base dir is null)", this);
		}
		super.initialize();
	}

}
