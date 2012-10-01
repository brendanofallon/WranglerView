package wranglerView.client;

import java.util.List;

import wranglerView.client.jobSubmission.FastQService;
import wranglerView.client.jobSubmission.FastQServiceAsync;
import wranglerView.client.jobSubmission.FastQsPanel;
import wranglerView.client.jobSubmission.JobSettingsPanel;
import wranglerView.client.jobSubmission.TemplateService;
import wranglerView.client.jobSubmission.TemplateServiceAsync;
import wranglerView.client.jobSubmission.TemplatesPanel;
import wranglerView.shared.AuthToken;
import wranglerView.shared.FastQDirInfo;
import wranglerView.shared.TemplateInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Wraps a fastq, template, and job settings panel. Together the allow for job submission
 * @author brendanofallon
 *
 */
public class JobSubmissionPanel {

	final AuthToken token;
	
	public JobSubmissionPanel(AuthToken tok) {
		this.token = tok;
		initializeFastQsPanel();
		initializeTemplatesPanel();
		initializeSubmissionPanel();
	}
	
	public TemplateInfo getSelectedTemplate() {
		return templatesPanel.getSelectedTemplate();
	}
	
	public FastQDirInfo getSelectedFastQInfo() {
		return fqsPanel.getSelectedFQInfo();
	}

	public AuthToken getAuthToken() {
		return token;
	}

	private void initializeSubmissionPanel() {
		settingsPanel = new JobSettingsPanel(this);
		settingsPanel.getWidget().setStylePrimaryName("rightpanel");
		
		hPanel.add(settingsPanel.getWidget());
	}
	
	private void initializeTemplatesPanel() {
		templatesPanel = new TemplatesPanel();
		templatesPanel.getWidget().setStylePrimaryName("centerpanel");
		hPanel.add(templatesPanel.getWidget());
		
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
	

	public Widget getWidget() {
		return hPanel;
	}

	/**
	 * Initialize the FastQ panel by making an RPC call to the server to obtain
	 * the list of FastQDirInfo objects that we will display
	 */
	private void initializeFastQsPanel() {
		fqsPanel = new FastQsPanel(this);
		fqsPanel.getWidget().setStylePrimaryName("leftPanel");
		hPanel.add(fqsPanel.getWidget());
		
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

	public void setSelectedFastQ(FastQDirInfo selectedFQInfo) {
		if (settingsPanel != null)
			settingsPanel.suggestSampleID(selectedFQInfo.sampleName);
	}
	
	/**
	 * Add the given list of fqDirInfo objects to the fastqs panel.
	 * Does not clear the current list, just adds new objects
	 * @param fqDirList
	 */
	protected void addFastQDirs(List<FastQDirInfo> fqDirList) {
		
		if (fqDirList != null && fqDirList.size()>0) {
			fqsPanel.setFQInfoRoot(fqDirList);
		}
	}

	protected void addTemplates(List<TemplateInfo> templateList) {
		for(TemplateInfo info : templateList) {
			templatesPanel.addTemplate(info);
		}
	}

	private HorizontalPanel hPanel = new HorizontalPanel();
	private TemplateServiceAsync templateFetcher = GWT.create(TemplateService.class);
	private FastQServiceAsync fqFetcher = GWT.create(FastQService.class);
	private TemplatesPanel templatesPanel;
	private FastQsPanel fqsPanel;
	private JobSettingsPanel settingsPanel;
	
}
