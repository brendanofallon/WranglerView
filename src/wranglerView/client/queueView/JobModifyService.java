package wranglerView.client.queueView;

import wranglerView.shared.JobModifyResult;
import wranglerView.shared.JobModifyRequest;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("jobmodify")
public interface JobModifyService extends RemoteService {

	public JobModifyResult modifyJob(JobModifyRequest req);
}
