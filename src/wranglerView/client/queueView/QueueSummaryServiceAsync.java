package wranglerView.client.queueView;

import wranglerView.shared.QueueSummary;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface QueueSummaryServiceAsync {

	void getQueueSummary(AsyncCallback< QueueSummary > cb);
}
