package wranglerView.client.jobSubmission;


import wranglerView.client.JobSubmissionPanel;
import wranglerView.client.QueueStatusPanel;
import wranglerView.client.WranglerView;
import wranglerView.shared.AnalysisJobDescription;
import wranglerView.shared.AnalysisJobDescription.AnalysisStyle;
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Creates and manages objects for applying settings, like sample id, submitter name, and
 * notifications (someday) for a job that will be submitted soon 
 * @author brendanofallon
 *
 */
public class JobSettingsPanel {

	ScrollPanel scrollPane;
	VerticalPanel mainPanel;
	JobSubmissionPanel submissionPanel;
	
	private final SubmissionServiceAsync submissionService = GWT.create(SubmissionService.class);
	
	public JobSettingsPanel(JobSubmissionPanel submissionPanel) {
		this.submissionPanel = submissionPanel;
		initComponents();
	}
	
	public Panel getWidget() {
		return scrollPane;
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
		scrollPane = new ScrollPanel();
		scrollPane.add(mainPanel);
		
		
		qPanel = new QueueStatusPanel();
		mainPanel.add(qPanel.getWidget());
		qPanel.refresh();
		
		Label sampleIDLabel = new HTML("<b>Enter Sample ID:</b>");
		sampleIDLabel.setStyleName("filelabel");
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
		
		
		Label submitterLabel = new HTML("<b>Enter Submitter (e.g. your name): </b>");
		submitterLabel.setStyleName("filelabel");
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
		
		if (WranglerView.DEBUG) {
			Button submitExplodingJobButton = new Button("Submit exploding job");
			mainPanel.add(submitExplodingJobButton);
			submitExplodingJobButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					handleSubmitExplodeButtonClick();
				}
			});	
			
			
			Button submitSleeperJobButton = new Button("Submit sleeper job");
			mainPanel.add(submitSleeperJobButton);
			submitSleeperJobButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					handleSubmitSleeperButtonClick();
				}
			});	
		}
		
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
		
		
		if (selectedTemplate.isMarcAnalysis) {
			desc.analysisStyle = AnalysisStyle.MARC;
		}
		else {
			desc.analysisStyle = AnalysisStyle.BRENDAN;
		}
		
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
				qPanel.refresh();
			}
			
		});
	}

	
	/**
	 * Special debug function to submit an 'exploding job'
	 */
	protected void handleSubmitExplodeButtonClick() {
		AnalysisJobDescription desc = new AnalysisJobDescription();
		desc.analysisStyle = AnalysisJobDescription.AnalysisStyle.EXPLODE_JOB;
		desc.submitter = submitterIdBox.getText().replace(" ", "_");
		String sampleName = sampleIdBox.getText().trim();
		desc.sampleName = sampleName;
		
		submissionService.submitJob(desc, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error submitting job : " + caught.getMessage());
			}

			@Override
			public void onSuccess(String id) {
				showSubmitSuccessBox(id);
				qPanel.refresh();
			}
		});
	}
	
	/**
	 * Special debug function to submit a sleeper job
	 */
	protected void handleSubmitSleeperButtonClick() {
		AnalysisJobDescription desc = new AnalysisJobDescription();
		desc.analysisStyle = AnalysisJobDescription.AnalysisStyle.WAIT_JOB;
		desc.submitter = submitterIdBox.getText().replace(" ", "_");
		String sampleName = sampleIdBox.getText().trim();
		desc.sampleName = sampleName;
		
		submissionService.submitJob(desc, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error submitting job : " + caught.getMessage());
			}

			@Override
			public void onSuccess(String id) {
				showSubmitSuccessBox(id);
				qPanel.refresh();
			}
		});
	}
	
	protected void showSubmitSuccessBox(String jobID) {
		if (submitSuccessMessage != null) {
			submitSuccessMessage.setHTML("<b>Job submitted successfully, job home directory is: " + jobID + "<b>");
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

	private QueueStatusPanel qPanel;
	private boolean hasUserSampleID = false;
	private TextBox submitterIdBox;
	private TextBox sampleIdBox;
	private HTML submitSuccessMessage;


}
