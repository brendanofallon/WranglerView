package wranglerView.server;

import java.util.List;

import org.springframework.context.ApplicationContext;

import wranglerView.client.jobSubmission.FastQService;
import wranglerView.logging.WLogger;
import wranglerView.server.fastq.FastQHandler;
import wranglerView.shared.FastQDirInfo;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class FastQServiceImpl extends RemoteServiceServlet implements FastQService {

	//public static final String defaultFastQRoot = WranglerProperties.getFastQBaseDir().getAbsolutePath();
	
	
	private FastQHandler handler = null;
	


	@Override
	public List<FastQDirInfo> getFastQFolders() {
		
		if (handler == null) {
			ApplicationContext ctxt = SpringContext.getContext();
			handler = (FastQHandler) ctxt.getBean("fastQHandler");
		}
		
		List<FastQDirInfo> fqDirs = handler.getFastqList();
		
		if (fqDirs == null) {
			WLogger.severe("Error reading fastq information: list is null!");
		}
		
		return fqDirs;
		
		
		
	}
	
	
	
}
