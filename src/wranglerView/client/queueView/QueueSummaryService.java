package wranglerView.client.queueView;

import wranglerView.shared.QueueSummary;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("queuesummary")
public interface QueueSummaryService extends RemoteService {

	QueueSummary getQueueSummary();
	
}
