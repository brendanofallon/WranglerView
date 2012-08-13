package wranglerView.shared;

import java.util.Date;

/**
 * These objects are passed from server to client to present in the list
 * of available fastQ-containing Directories to the user
 * @author brendan
 *
 */
public class FastQDirInfo {

	public String sampleName = null;
	public String parentDir = null;
	public String reads1 = null;
	public String reads2 = null;
	public Date reads1ModifiedTime = null;
	public Date reads2ModifiedTime = null;
	
}
