package jobWrangler.job;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import jobWrangler.job.Job.JobState;

/**
 * JobMonitors periodically poll a Job to see what its state is, notify other objects
 * of changes in a job's state, and to detect errors. Usually each job has exactly one
 * Monitor associated with it. Because Monitors run in a separate thread from the Job
 * they're attached to, the job can die horribly (e.g. throw a runtime exception) and
 * the Monitor will be unimpeded. 
 * @author brendanofallon
 *
 */
public class JobMonitor implements ActionListener {

	private Timer timer;
	final Job job;
	private JobState previousState = null;
	private boolean autoStop = true; //If true, automatically stop monitoring if job ends successfully
	
	protected List<JobListener> listeners = new ArrayList<JobListener>(4);
	
	public JobMonitor(Job job) {
		this.job = job;
		timer = new Timer(1000, this);
		timer.setRepeats(true);
	}
	
	/**
	 * True if this monitor will automatically stop when job ends
	 * @return
	 */
	public boolean isAutoStop() {
		return autoStop;
	}


	/**
	 * If true, monitor will automatically stop when job ends, otherwise it will continue until
	 * stopMonitoring() is called. Default is true. 
	 * @param autoStop
	 */
	public void setAutoStop(boolean autoStop) {
		this.autoStop = autoStop;
	}

	/**
	 * Begin polling the job associated with this monitor
	 */
	public void startMonitoring() {
		timer.start();
	}
	
	/**
	 * Cease polling the job. Polling can be restarted by a call to .startMonitoring()
	 */
	public void stopMonitoring() {
		timer.stop();
	}
	
	/**
	 * Returns true if we're currently monitoring (if the timer is polling the job)
	 * @return
	 */
	public boolean isMonitoring() {
		return timer.isRunning();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		//System.out.println("Monitor is polling, current job " + job.getID() + " state is : " + job.getJobState());
		if (previousState != job.getJobState()) {
			fireJobUpdate();
		}
		
		if (autoStop && (job.getJobState() == JobState.FINISHED_ERROR || job.getJobState() == JobState.FINISHED_SUCCESS)) {
			stopMonitoring();
		}
		previousState = job.getJobState();
	}
	
	/**
	 * Add the given listener to those objects that will be notified of job 
	 * update events
	 * @param l
	 */
	public void addListener(JobListener l) {
		listeners.add(l);
	}
	
	/**
	 * Remove the given object from the listener list
	 * @param l
	 * @return True if object was already a listener
	 */
	public boolean removeListener(JobListener l) {
		return listeners.remove(l);
	}
	
	/**
	 * Notify all listeners that this job's status is updated
	 */
	protected void fireJobUpdate() {
		for(JobListener l : listeners) {
			l.jobUpdated(job);
		}
	}
}
