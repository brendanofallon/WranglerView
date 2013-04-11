package wranglerView.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import wranglerView.logging.WLogger;

/**
 * Stores a few static properties - actually right now just the location
 * of the 'root' JobWrangler directory
 * @author brendanofallon
 *
 */
public class WranglerProperties {

	public static final String WRANGLER_PROPERTIES_FILENAME = "wranglerprops.txt";
	
	protected static File defaultWranglerRoot = new File( System.getProperty("user.home") + "/jobwrangler");
	
	//protected static File fastQBaseDir = new File("/mnt/storage1/fastq_for_parsing/fastqs_for_scheduler");
	//protected static File fastQBaseDir = new File( System.getProperty("user.home") + "/fastq_files");
	protected static File defaultFastQBaseDir = new File("/mnt/research1/fastq_for_parsing");
	protected static File defaultCompletedDir = new File(defaultWranglerRoot.getAbsolutePath() + "/completed/");
	
	protected static String templatesRelativePath = "templates";
	protected static String executionDirRelativePath = "wrangler-jobs";
	
	private static Map<String, String> props = new HashMap<String, String>();
	
	private static boolean propertiesLoaded = false;
	
	public static File getWranglerRoot() {
		if (! propertiesLoaded) {
			try {
				loadProperties();
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		String wranglerRootPath = props.get("wrangler.root");
		if (wranglerRootPath == null) {
			throw new IllegalStateException("No wrangler root specified, property keys are: " + props.keySet());
		}
		
		return new File(wranglerRootPath);
	}
	
	public static void loadProperties() throws IOException {
		//Add default properties...
		props.put("wrangler.root", defaultWranglerRoot.getAbsolutePath());
		props.put("fastq.dir", defaultFastQBaseDir.getAbsolutePath());
		props.put("completed.dir", defaultCompletedDir.getAbsolutePath());
		
		//Look for a file to add more properties
		File propsFile = new File(WRANGLER_PROPERTIES_FILENAME);
		if (! propsFile.exists()) {
			WLogger.info("Attempting to load WranglerView properties from " + propsFile.getAbsolutePath());
			propsFile = new File("../" + WRANGLER_PROPERTIES_FILENAME);
		}
		if (! propsFile.exists()) {
			WLogger.info("Attempting to load WranglerView properties from " + propsFile.getAbsolutePath());
			propsFile = new File(System.getProperty("user.dir") + "/" + WRANGLER_PROPERTIES_FILENAME);
		}
		if (! propsFile.exists()) {
			WLogger.info("Attempting to load WranglerView properties from " + propsFile.getAbsolutePath());
			propsFile = new File(System.getProperty("user.home") + "/" + WRANGLER_PROPERTIES_FILENAME);
		}
		
		
		if (!propsFile.exists()) {
			WLogger.warn("Could not find WranglerView properties file in any path!");
			return;
		}
		
		BufferedReader reader;
		reader = new BufferedReader(new FileReader(propsFile));
		String line = reader.readLine();
		WLogger.info("Found properties file at " + propsFile.getAbsolutePath());
		while(line != null) {
			String[] toks = line.split("=");
			if (toks.length==2) {
				props.put(toks[0], toks[1]);
				WLogger.info("Adding property " + toks[0] + "=" + toks[1]);
			}
			line = reader.readLine();
		}
		
		reader.close();
		propertiesLoaded = true;
	}
	
	public static File getFastQBaseDir() {
		if (! propertiesLoaded) {
			try {
				loadProperties();
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return new File(props.get("fastq.dir"));
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
		if (! propertiesLoaded) {
			try {
				loadProperties();
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return props.get("wrangler.root") + "/" + templatesRelativePath;
	}
	
	/**
	 * Return the full (absolute) path to the execution directory
	 * @return
	 */
	public static String getExecutionDirPath() {
		if (! propertiesLoaded) {
			try {
				loadProperties();
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return props.get("wrangler.root") + "/" + executionDirRelativePath;
	}
	
}
