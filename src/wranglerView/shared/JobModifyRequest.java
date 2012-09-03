package wranglerView.shared;

import java.io.Serializable;

/**
 * These objects are passed from client to server during a JobModifyService call
 * to request that a certain job be 'modified' which right now means only cancelling
 * @author brendan
 *
 */
public class JobModifyRequest implements Serializable {

	public enum Type {DELETE /*, HOLD, SEND_TO_TOP, SEND_TO_BOTTOM */}
	
	Type type = Type.DELETE;
	
	String jobID ;
	
		
	public void setType(Type type) {
		this.type = type;
	}

	public void setJobID(String jobID) {
		this.jobID = jobID;
	}

	public String getID() {
		return jobID;
	}
	
	public Type getRequestType() {
		return type;
	}
	
}
