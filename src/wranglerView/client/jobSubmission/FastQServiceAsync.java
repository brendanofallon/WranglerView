package wranglerView.client.jobSubmission;

import java.util.List;

import wranglerView.shared.FastQDirInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FastQServiceAsync {
	
	void getFastQFolders(AsyncCallback<List<FastQDirInfo>> callback);
	
}
