package wranglerView.shared;

import java.io.Serializable;
import java.util.List;


/**
 * A quick summary of the current state of the queue - pretty much a listing of jobs and
 * their statuses
 * @author brendan
 *
 */
public class QueueSummary implements Serializable {

	public List<JobInfo> jobInfo = null;
	
	public static class JobInfo implements Serializable {
		public String sampleName = null;
		public String analysisType = null;
		public String jobID = null;
		public String submitter = null;
		public String startTime = null;
		public String creationTime = null;
		public String endTime = null;
		public String status = null;
		public String errorMsg = null;
	}
}
