package wranglerView.client.jobSubmission;

import java.util.List;

import wranglerView.shared.FastQDirInfo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("fastq")
public interface FastQService extends RemoteService {
	
	List<FastQDirInfo> getFastQFolders();
	
}
