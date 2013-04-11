package wranglerView.server;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wranglerView.client.jobSubmission.FastQService;
import wranglerView.logging.WLogger;
import wranglerView.shared.FastQDirInfo;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class FastQServiceImpl extends RemoteServiceServlet implements FastQService {

	public static final String defaultFastQRoot = WranglerProperties.getFastQBaseDir().getAbsolutePath();
	
	static final DecimalFormat formatter = new DecimalFormat("#,##0.0");
	static final DateFormat df = new SimpleDateFormat("hh:mm MMM/dd/yyyy");
	
	@Override
	public List<FastQDirInfo> getFastQFolders() {
		File root = getRootFolder();
		
		WLogger.info("FastQ root dir at path :" + root.getAbsolutePath() + " is initializing");
		
		if (!root.exists() ) {
			WLogger.severe("FastQ root dir at path :" + root.getAbsolutePath() + " does not exist");
			throw new IllegalArgumentException("FastQ Root directory " + root.getAbsolutePath() + " does not exist");
		}
		
		if (! root.canRead()) {
			WLogger.severe("FastQ Root dir at path :" + root.getAbsolutePath() + " exists but cannot be read");
			throw new IllegalArgumentException("FastQ Root directory " + root.getAbsolutePath() + " exists but cannot be read");
		}
		
//		if (! root.isDirectory()) {
//			WLogger.severe("FastQ Root dir at path :" + root.getAbsolutePath() + " is not a directory");
//			throw new IllegalArgumentException("FastQ Root directory " + root.getAbsolutePath() + " is not a directory");
//		}
		
		List<FastQDirInfo> fqInfoList = new ArrayList<FastQDirInfo>();
		File[] files = root.listFiles();
		for(int i=0; i<files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				//System.out.println("Examining directory " + file.getAbsolutePath());
				FastQDirInfo info = buildInfo(file);
				if (info != null) {
					fqInfoList.add(info);
				}
			}
		}
		
		return fqInfoList;
	}
	
	
	/**
	 * Create a FastQDirInfo object based on this directory
	 * @param dir
	 * @return
	 */
	private FastQDirInfo buildInfo(File dir) {
		File[] files = dir.listFiles();
		
		if (files.length == 0) {
			return null;
		}
		
		FastQDirInfo info = new FastQDirInfo();
		info.parentDir = dir.getAbsolutePath();
		info.sampleName = dir.getName();
		if (containsFastQs(dir)) {
			readFastQFiles(info, dir);
			return info;
		}
		else {	
			//See if I'm a dir, if so, make a new FastQDirInfo with children that are my subdirs
			for(int i=0; i<files.length; i++) {
				if (files[i].isDirectory()) {
					FastQDirInfo kidInfo = buildInfo(files[i]);
					if (kidInfo != null)
						info.children.add( kidInfo );
				}
			}
		}
		
		return info;
	}
	
	/**
	 * Read files in the given directory, looking for things that seem like fastq files,
	 * and then fill in information in the info object
	 * @param info
	 * @param file
	 */
	private void readFastQFiles(FastQDirInfo info, File file) {
		if (! file.isDirectory()) {
			throw new IllegalArgumentException("File must be a directory");

		}
		
		File[] files = file.listFiles();
		int totalFQsFound = 0;
		for(int i=0; i<files.length; i++) {
			if (looksLikeFastQ(files[i])) {
				totalFQsFound++;
				if (info.reads1 == null) {
					info.reads1 = files[i].getName();
					info.modifiedTime = df.format(new Date(files[i].lastModified()));
					long bytes = files[i].length();
					double sizeMb = (double)bytes / (1024.0 * 1024.0);
					info.reads1Size = formatter.format(sizeMb) + "Mb";
				}else {
					if (info.reads2 == null) {
						info.reads2 = files[i].getName();
						long bytes = files[i].length();
						double sizeMb = (double)bytes / (1024.0 * 1024.0);
						info.reads2Size = formatter.format(sizeMb) + "Mb";
					}
					else {
						//Yikes! Too many reads files in this directory...
					}
				}
			}
		}
		info.totalFQs = totalFQsFound;
	}
	
	/**
	 * Returns true if the file contains a few things that look like fastq files
	 * @param file
	 * @return
	 */
	private boolean containsFastQs(File file) {
		if (! file.isDirectory()) {
			return false;
		}
		
		File[] files = file.listFiles();
		for(int i=0; i<files.length; i++) {
			if ( looksLikeFastQ(files[i] )) {
				return true;
			}
		}
		
		return false;
	}


	/**
	 * Return true if this file has a name that looks like a valid fastq name
	 * @param file
	 * @return
	 */
	private boolean looksLikeFastQ(File file) {
		String name = file.getName();
		if (name.endsWith(".fq.gz") || name.contains("fastq") || name.endsWith(".fq")) {
			return true;
		}
		return false;
	}


	/**
	 * Obtain the 'root' folder for fastq files
	 * @return
	 */
	private File getRootFolder() {
		return new File(defaultFastQRoot);
	}

}
