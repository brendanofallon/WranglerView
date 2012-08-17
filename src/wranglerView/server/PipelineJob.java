package wranglerView.server;

import java.io.File;
import java.io.IOException;

import jobWrangler.job.InitializationFailedException;
import wranglerView.server.template.TemplateRegistry;

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
		setCommand(javaPath + " " + memoryStr + " -jar pipeline.jar -home " + projHome.getAbsolutePath() + " " + inputFile.getAbsolutePath() );
	}

	/**
	 * Copy pipeline to project home
	 */
	protected void initialize() throws InitializationFailedException {
		//Create base directory and initialize process builder with command
		super.initialize();
		
		if (! projHome.exists()) {
			projHome.mkdir();
		}
		
		//Copy pipeline executable to project home
		copyPipelineToProjHome();		
	}


	/**
	 * Attempt to copy the
	 * @throws InitializationFailedException
	 */
	private void copyPipelineToProjHome() throws InitializationFailedException {
		try {
			pipelinePath = TemplateRegistry.getRegistry().getTemplateDirectory() + "/pipeline.jar";
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new InitializationFailedException("Could not copy pipeline executable from " + pipelinePath + " to " + projHome.getAbsolutePath(), this);
		}
		
		
		ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "cp " + pipelinePath + " " + projHome.getAbsolutePath());
		
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
