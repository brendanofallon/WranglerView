package wranglerView.client.jobSubmission;

import java.util.List;

import wranglerView.shared.TemplateInfo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Interface for retrieving list of templates from the server
 * @author brendan
 *
 */
@RemoteServiceRelativePath("template")
public interface TemplateService extends RemoteService {
	
	public List<TemplateInfo> getAvailableTemplates();

}
