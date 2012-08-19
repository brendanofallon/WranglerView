package wranglerView.server;

import java.io.File;

/**
 * Stores a few static properties - actually right now just the location
 * of the 'root' JobWrangler directory
 * @author brendanofallon
 *
 */
public class WranglerProperties {

	protected static File wranglerRoot = new File( System.getProperty("user.home") );
	protected static File fastQBaseDir = new File( System.getProperty("user.home") + "/fastq_files");
	
	protected static String templatesRelativePath = "templates";
	protected static String executionDirRelativePath = "wrangler-jobs";
	
	public static File getWranglerRoot() {
		return wranglerRoot;
	}
	public static File getFastQBaseDir() {
		return fastQBaseDir;
	}
	
	public static String getTemplatesRelativePath() {
		return templatesRelativePath;
	}
	
	public static String getExecutionDirRelativePath() {
		return executionDirRelativePath;
	}
	
	/**
	 * Return the full (absolute) path to the templates directory
	 * @return
	 */
	public static String getTemplatesPath() {
		return wranglerRoot.getAbsolutePath() + "/" + templatesRelativePath;
	}
	
	/**
	 * Return the full (absolute) path to the execution directory
	 * @return
	 */
	public static String getExecutionDirPath() {
		return wranglerRoot.getAbsolutePath() + "/" + executionDirRelativePath;
	}
	
	
}
