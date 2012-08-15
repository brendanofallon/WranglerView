package wranglerView.client.jobSubmission;

import java.util.List;

import wranglerView.shared.TemplateInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TemplateServiceAsync {
	
	public void getAvailableTemplates(AsyncCallback< List<TemplateInfo> > callback);
}
