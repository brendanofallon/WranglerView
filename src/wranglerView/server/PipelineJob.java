package wranglerView.server;

import java.io.File;
import java.io.IOException;

import jobWrangler.job.InitializationFailedException;
import wranglerView.logging.WLogger;

public class PipelineJob extends WranglerJob {
	
	final File inputFile;
	final File projHome;
	final String javaPath = " java ";
	final String memoryStr = " -Xmx8g ";
	private String pipelinePath = null; //Gets set during initialization
	
	public PipelineJob(File projHome, File inputFile) {
		this.inputFile = inputFile;
		this.projHome = projHome;
		this.setBaseDir(projHome);
		setCommand(javaPath + " " + memoryStr + " -jar pipeline.jar -home " + projHome.getAbsolutePath() + " " + projHome.getAbsolutePath() + "/" + inputFile.getName() );
		WLogger.info("Creating new Pipeline job with command : " + getCommand());
	}

	/**
	 * Copy pipeline to project home
	 */
	protected void initialize() throws InitializationFailedException {
		WLogger.info("Pipelinejob " + getID() + " is attempting to initialize");
		//Create base directory and initialize process builder with command
		super.initialize();
		
		if (! projHome.exists()) {
			projHome.mkdir();
		}
				
		//Copy pipeline executable to project home
		copyPipelineToProjHome();		
		WLogger.info("Pipelinejob " + getID() + " has initialized successfully");
	}


	/**
	 * Attempt to copy the
	 * @throws InitializationFailedException
	 */
	private void copyPipelineToProjHome() throws InitializationFailedException {
		
		pipelinePath = WranglerProperties.getWranglerRoot() + "/pipeline.jar";
		
		String com = "cp " + pipelinePath + " " + projHome.getAbsolutePath() + "/pipeline.jar";
		ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", com);
		WLogger.info("ProcessBuilder has creating copying job with command : "+ com);
		
		try {
			Process proc = processBuilder.start();
			
			int exitVal = proc.waitFor();
		} catch (IOException e) {
			
			e.printStackTrace();
			throw new InitializationFailedException("Could not copy pipeline executable from " + pipelinePath + " to " + projHome.getAbsolutePath(), this);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new InitializationFailedException("Interrupted while copying pipeline executable from " + pipelinePath + " to " + projHome.getAbsolutePath(), this);
		}
		
		
		
		
	}
}
