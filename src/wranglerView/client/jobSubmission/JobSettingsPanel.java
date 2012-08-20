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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.HTML;
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
		
		submitSuccessMessage = new HTML("Job submitted successfully");
		mainPanel.add(submitSuccessMessage);
		submitSuccessMessage.setVisible(false);
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
		desc.reads1Name = fqInfo.reads1;
		desc.reads2Name = fqInfo.reads2;
		desc.templateID = selectedTemplate.templateID;
		desc.sampleName = sampleName; 
		desc.submitter = submitterIdBox.getText().replace(" ", "_");
		desc.templateName = selectedTemplate.templateName;
		
		submissionService.submitJob(desc, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error submitting job : " + caught.getMessage());
			}

			@Override
			public void onSuccess(String id) {
				showSubmitSuccessBox(id);
			}
			
		});
	}

	protected void showSubmitSuccessBox(String jobID) {
		if (submitSuccessMessage != null) {
			submitSuccessMessage.setHTML("<b>Job submitted successfully, job id is: " + jobID + "<b>");
			submitSuccessMessage.setVisible(true);
		}
		
		Timer timer = new Timer() {

			@Override
			public void run() {
				submitSuccessMessage.setVisible(false);
			}
			
		};
		timer.schedule(10 * 1000);
	}

	private boolean hasUserSampleID = false;
	private TextBox submitterIdBox;
	private TextBox sampleIdBox;
	private HTML submitSuccessMessage;
}
