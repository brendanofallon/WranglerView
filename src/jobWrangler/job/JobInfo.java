package jobWrangler.job;

import java.util.Date;

/**
 * Storage for a few pieces of information about a job
 * @author brendanofallon
 *
 */
public class JobInfo {

	final Job job;
	String submitterID = null;
	Date startTime = null;
	Date endTime = null;
	
	public JobInfo(Job job) {
		this.job = job;
	}
	
	
	public String getSubmitterID() {
		return submitterID;
	}

	public void setSubmitterID(String submitterID) {
		this.submitterID = submitterID;
	}

	public Date getEndTime() {
		return endTime;
	}

	void setEndTime(Date endime) {
		this.endTime = endime;
	}

	public Date getStartTime() {
		return startTime;
	}

	void setStartTime(Date time) {
		this.startTime = time;
	}
}
