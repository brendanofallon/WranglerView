package wranglerView.client.jobSubmission;


import wranglerView.client.JobSubmissionPanel;
import wranglerView.shared.AnalysisJobDescription;
import wranglerView.shared.FastQDirInfo;
import wranglerView.shared.TemplateInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Creates and manages objects for applying settings, like sample id, submitter name, and
 * notifications (someday) for a job that will be submitted soon 
 * @author brendanofallon
 *
 */
public class JobSettingsPanel {

	VerticalPanel mainPanel;
	JobSubmissionPanel submissionPanel;
	
	private final SubmissionServiceAsync submissionService = GWT.create(SubmissionService.class);
	
	public JobSettingsPanel(JobSubmissionPanel submissionPanel) {
		this.submissionPanel = submissionPanel;
		initComponents();
	}
	
	public CellPanel getWidget() {
		return mainPanel;
	}

	/**
	 * If the user has not already put in a sample id, use this one
	 * @param suggestedID
	 */
	public void suggestSampleID(String suggestedID) {
		if (! hasUserSampleID) {
			sampleIdBox.setText(suggestedID);
			hasUserSampleID = false;
		}
	}
	
	private void initComponents() {
		mainPanel = new VerticalPanel();
		
		Label sampleIDLabel = new Label("Enter Sample ID:");
		sampleIdBox = new TextBox();
		sampleIdBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				setHasUserSampleName();
			}
		});
		sampleIdBox.setText("enter sample id");
		
		mainPanel.add(sampleIDLabel);
		mainPanel.add(sampleIdBox);
		
		
		Label submitterLabel = new Label("Enter Submitter (e.g. your name):");
		submitterIdBox = new TextBox();
		mainPanel.add(submitterLabel);
		mainPanel.add(submitterIdBox);
		
		Button submitJobButton = new Button("Submit job");
		mainPanel.add(submitJobButton);
		submitJobButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				handleSubmitButtonClick();
			}
		});
	}

	protected void setHasUserSampleName() {
		hasUserSampleID = true;
	}

	/**
	 * Called when submit job button is pressed...
	 */
	protected void handleSubmitButtonClick() {
		
		AnalysisJobDescription desc = new AnalysisJobDescription();
		FastQDirInfo fqInfo = submissionPanel.getSelectedFastQInfo();
		TemplateInfo selectedTemplate = submissionPanel.getSelectedTemplate();
		
		if (fqInfo == null) {
			Window.alert("Please select a FastQ containing directory");
			return;
		}
		
		if (selectedTemplate == null) {
			Window.alert("Please select an analysis type");
			return;
		}
		
		String sampleName = sampleIdBox.getText().trim();
		if (sampleName.length() == 0) {
			Window.alert("Please enter a name for the sample");
			return;
		}
		sampleName = sampleName.replace(" ", "_");
		
		
		desc.pathToFastQDir = fqInfo.parentDir;
		desc.templateID = selectedTemplate.templateID;
		desc.sampleName = sampleName; 
		
		submissionService.submitJob(desc, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				System.out.println("Failure, exception is : " + caught.getMessage() );
			}

			@Override
			public void onSuccess(Void v) {
				// TODO Auto-generated method stub
				System.out.println("Success, btw");
			}
			
		});
	}

	private boolean hasUserSampleID = false;
	private TextBox submitterIdBox;
	private TextBox sampleIdBox;
}
