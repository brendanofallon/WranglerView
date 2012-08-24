package wranglerView.shared;

import java.io.Serializable;
import java.util.Map;

/**
 * These objects are passed from server to client as a result from the JobQueryService
 * @author brendan
 *
 */
public class JobQueryResult implements Serializable {

	public String status = null;
	public Map<String, String> statusVals = null;
	
}
