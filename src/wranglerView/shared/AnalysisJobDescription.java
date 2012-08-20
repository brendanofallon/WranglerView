package wranglerView.shared;

import java.io.Serializable;

/**
 * This class contains all the necessary info to build and run an analysis - 
 * a path to the directory containing the input fastq's, an analysis template type, 
 * a sample name, and possible more data
 * @author brendan
 *
 */
public class AnalysisJobDescription implements Serializable {
	
	public String pathToFastQDir = null; //Absolute path to parent directory
	public String reads1Name = null;	//Name of fastq file containing forward reads
	public String reads2Name = null;	//Name of fastq file containing reverse reads
	public String templateID = null;	//Unique ID of template to use for analysis
	public String sampleName = null;	//Name of sample
	public String submitter = null;		//Name of submitter
	public String templateName = null;	//User-readable name of analysis type
	

}
