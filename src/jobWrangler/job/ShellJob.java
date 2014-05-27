package jobWrangler.job;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import jobWrangler.util.StreamRedirector;
import wranglerView.logging.WLogger;


/**
 * This type of job executes a script supplied as a string using bash, as in
 * bash -c 'thecommand' 
 * @author brendanofallon
 *
 */
public class ShellJob extends Job {

	//This is the command that will be executed by the script
	protected String command;
	protected File baseDir = new File( System.getProperty("user.dir") );
	private ProcessBuilder processBuilder = null;
	private Process proc = null; //Created when we actually begin process, in execute() method
	private int exitValue = -1; 
	private boolean directOutputToFile = true;
	private File outputFile = null; //File that will contain all output when the process is run
	
	public ShellJob(String command) {
		this.command = command;	
	}
	
	public ShellJob(String command, File dir) throws IOException {
		this.command = command;
		if (! dir.exists()) {
			throw new IOException("Specified base directory, " + dir.getAbsolutePath() + ", does not exist");
		}
		if (! dir.isDirectory()) {
			throw new IOException("Specified base directory, " + dir.getAbsolutePath() + ", is not a directory");
		}
		
		baseDir = dir;
	}
	
	public String getCommand() {
		return command;
	}
	
	/**
	 * Set the base directory in which all commands will be executed
	 * @param dir
	 */
	public void setBaseDir(File dir) {
		if (getJobState() == JobState.UNINITIALIZED)
			this.baseDir = dir;
		else {
			throw new IllegalStateException("Cannot set job home directory after initialization");
		}
	}
	
	/**
	 * Set the command to be executed by BASH
	 * @param command
	 */
	public void setCommand(String command) {
		if (getJobState() == JobState.UNINITIALIZED)
			this.command = command;
		else {
			throw new IllegalStateException("Cannot set job commands after initialization");
		}
	}
	
	/**
	 * If true, all output (both stderr and stdout) are directed to a text file.
	 * @return
	 */
	public boolean isDirectOutputToFile() {
		return directOutputToFile;
	}

	public void setDirectOutputToFile(boolean directOutputToFile) {
		this.directOutputToFile = directOutputToFile;
	}

	public File getBaseDir() {
		return baseDir;
	}
	
	/**
	 * Returns the exit value of the process, or -1 if the process has not completed yet
	 * @return
	 */
	public int getExitValue() {
		return exitValue;
	}
	
	protected void initialize() throws InitializationFailedException {
		WLogger.info("Initializing shell job with command: " + command);
		processBuilder = new ProcessBuilder("bash", "-c", command);
		
		baseDir.mkdir();
		
		processBuilder.directory(baseDir);
		processBuilder.redirectErrorStream(true);
		if (directOutputToFile) {
			String newFilename = "job" + this.getID() + "-output.txt";
			outputFile = new File(baseDir + System.getProperty("file.separator") + newFilename);
		}
	}
	
	/**
	 * If redirectOutputToFile is true, this process will write all output (stdout and stderr) to 
	 * a file. This returns the File object associated with the output file. This will be null
	 * if the job is not initialized, or if directOutputToFile is false
	 * @return
	 */
	public File getOutputFile() {
		return outputFile;
	}
	
	public void killJob() {
		if (proc != null) {
			WLogger.info("Destroying process with command : " + command);
			proc.destroy();
		}
	}
	
	@Override
	protected void execute() throws ExecutionFailedException {
		
		WLogger.info("Executing shell job with command: " + command);
		try {
			proc = processBuilder.start();
			
			StreamRedirector streamWriter = null;
			
			//Redirect output stream to file
			if (directOutputToFile) {
				streamWriter = new StreamRedirector(proc.getInputStream(), new PrintStream(new FileOutputStream(outputFile)));
				streamWriter.start();
			}
			exitValue = proc.waitFor();
			
			if (directOutputToFile) {
				streamWriter.join();
			}
			
			if (exitValue != 0) {
				WLogger.warn("Job failure, nonzero exit value for process running job " + getID());
				throw new ExecutionFailedException("Job failure, unknown reason", this);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			WLogger.warn("IOException in job " +  getID() + " : " + ex.getLocalizedMessage() );
			throw new ExecutionFailedException(e.getMessage(), this);
		} catch (InterruptedException e) {
			e.printStackTrace();
			WLogger.warn("Interruption in job " +  getID() + " : " + ex.getLocalizedMessage() );
			throw new ExecutionFailedException(e.getMessage(), this);
		}
		
	}

}
