package wranglerView.server;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wranglerView.client.jobSubmission.FastQService;
import wranglerView.shared.FastQDirInfo;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class FastQServiceImpl extends RemoteServiceServlet implements FastQService {

	public static final String defaultFastQRoot = WranglerProperties.getFastQBaseDir().getAbsolutePath(); //
	
	static DecimalFormat formatter = new DecimalFormat("#,##0.0");
	
	@Override
	public List<FastQDirInfo> getFastQFolders() {
		File root = getRootFolder();
		
		if (!root.exists() ) {
			throw new IllegalArgumentException("Root directory " + root.getAbsolutePath() + " does not exist");
		}
		if (! root.isDirectory()) {
			throw new IllegalArgumentException("Root directory " + root.getAbsolutePath() + " is not a directory");
		}
		
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
		for(int i=0; i<files.length; i++) {
			if (looksLikeFastQ(files[i])) {
				if (info.reads1 == null) {
					info.reads1 = file.getName();
					info.reads1ModifiedTime = new Date(file.lastModified());
					long bytes = file.length();
					double sizeMb = (double)bytes / (1024.0 * 1024.0);
					info.reads1Size = formatter.format(sizeMb) + "Mb";
				}else {
					if (info.reads2 == null) {
						info.reads2 = file.getName();
						info.reads2ModifiedTime = new Date(file.lastModified());
						long bytes = file.length();
						double sizeMb = (double)bytes / (1024.0 * 1024.0);
						info.reads2Size = formatter.format(sizeMb) + "Mb";
					}
					else {
						//Yikes! Too many reads files in this directory...
					}
				}
			}
		}
		
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
