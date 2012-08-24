package wranglerView.client.queueView;

import wranglerView.shared.JobQueryResult;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("jobquery")
public interface JobQueryService extends RemoteService {

	public JobQueryResult queryJob(String id);
	
}
