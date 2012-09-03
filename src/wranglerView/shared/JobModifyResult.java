package wranglerView.shared;

import java.io.Serializable;


/**
 * These objects are returned when the user attempts to modify a job (right now
 * the only modification possible is 'cancelling' jobs)
 * @author brendan
 *
 */
public class JobModifyResult implements Serializable {

	public enum ResultType {OK, ERROR};
	
	ResultType type = ResultType.OK;
	String errorMessage = null;
	
	public ResultType getType() {
		return type;
	}
	
	public void setType(ResultType type) {
		this.type = type;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
}
