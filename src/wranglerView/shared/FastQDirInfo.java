package wranglerView.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * These objects are passed from server to client to present in the list
 * of available fastQ-containing Directories to the user
 * @author brendan
 *
 */
public class FastQDirInfo implements Serializable {

	
	public String sampleName = null;
	public String parentDir = null;
	public String reads1 = null;
	public String reads2 = null;
	public String modifiedTime = null;
	public String reads1Size = null;
	public String reads2Size = null;
	
	public List<FastQDirInfo> children = new ArrayList<FastQDirInfo>();
}
