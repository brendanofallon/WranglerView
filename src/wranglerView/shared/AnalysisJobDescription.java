package wranglerView.shared;

/**
 * This class contains all the necessary info to build and run an analysis - 
 * a path to the directory containing the input fastq's, an analysis template type, 
 * a sample name, and possible more data
 * @author brendan
 *
 */
public class AnalysisJobDescription {
	
	public String pathToFastQDir = null;
	public String templateID = null;
	public String sampleName = null;

}
