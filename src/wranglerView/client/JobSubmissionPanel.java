package wranglerView.client;

import java.util.List;

import wranglerView.client.jobSubmission.FastQService;
import wranglerView.client.jobSubmission.FastQServiceAsync;
import wranglerView.client.jobSubmission.FastQsPanel;
import wranglerView.client.jobSubmission.JobSettingsPanel;
import wranglerView.client.jobSubmission.TemplateService;
import wranglerView.client.jobSubmission.TemplateServiceAsync;
import wranglerView.client.jobSubmission.TemplatesPanel;
import wranglerView.shared.FastQDirInfo;
import wranglerView.shared.TemplateInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class JobSubmissionPanel {

	public JobSubmissionPanel() {
		initializeSubmissionPanel();
		initializeTemplatesPanel();
		initializeFastQsPanel();
	}
	
	public TemplateInfo getSelectedTemplate() {
		return templatesPanel.getSelectedTemplate();
	}
	
	public FastQDirInfo getSelectedFastQInfo() {
		return fqsPanel.getSelectedFQInfo();
	}


	private void initializeSubmissionPanel() {
		RootPanel rightpanel = RootPanel.get("rightpanel");
		JobSettingsPanel settingsPanel = new JobSettingsPanel(this);
		rightpanel.add(settingsPanel.getWidget());
	}
	
	private void initializeTemplatesPanel() {
		RootPanel centerPanel = RootPanel.get("centerpanel");
		templatesPanel = new TemplatesPanel();
		centerPanel.add(templatesPanel.getWidget());
		
		templateFetcher.getAvailableTemplates(new AsyncCallback< List<TemplateInfo>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error retrieving analysis templates : " + caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(List<TemplateInfo> templateList) {
				addTemplates(templateList);
			}
			
		});
		
	}
	
	

	/**
	 * Initialize the FastQ panel by making an RPC call to the server to obtain
	 * the list of FastQDirInfo objects that we will display
	 */
	private void initializeFastQsPanel() {
		RootPanel leftPanel = RootPanel.get("leftpanel");
		fqsPanel = new FastQsPanel();
		leftPanel.add(fqsPanel.getWidget());
		
		fqFetcher.getFastQFolders(new AsyncCallback<List<FastQDirInfo>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error obtaining fastq directory info : " + caught.getMessage());
			}

			@Override
			public void onSuccess(List<FastQDirInfo> fqDirList) {
				addFastQDirs( fqDirList );
			}
			
		});
		
	}
	
	/**
	 * Add the given list of fqDirInfo objects to the fastqs panel.
	 * Does not clear the current list, just adds new objects
	 * @param fqDirList
	 */
	protected void addFastQDirs(List<FastQDirInfo> fqDirList) {
		for(FastQDirInfo info : fqDirList) {
			fqsPanel.addFQInfo(info);
		}
	}

	protected void addTemplates(List<TemplateInfo> templateList) {
		for(TemplateInfo info : templateList) {
			templatesPanel.addTemplate(info);
		}
	}
	
	private TemplateServiceAsync templateFetcher = GWT.create(TemplateService.class);
	private FastQServiceAsync fqFetcher = GWT.create(FastQService.class);
	private TemplatesPanel templatesPanel;
	private FastQsPanel fqsPanel;
}
