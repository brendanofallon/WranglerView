package wranglerView.client.queueView;


import java.util.Collections;
import java.util.Comparator;

import wranglerView.client.QueueStatusPanel;
import wranglerView.shared.JobModifyRequest;
import wranglerView.shared.JobModifyResult;
import wranglerView.shared.JobModifyResult.ResultType;
import wranglerView.shared.JobQueryResult;
import wranglerView.shared.QueueSummary;
import wranglerView.shared.QueueSummary.JobInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Main display panel for showing the state of the job queue. We show a list of all jobs 
 * on the left and details about particular jobs selected by the user on the right
 * @author brendan
 *
 */
public class QueueView {

	public QueueView() {
		initComponents();
	}
	
	public void refreshList() {
		
		qSummaryFetcher.getQueueSummary(new AsyncCallback< QueueSummary>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error retrieving job queue information: " + caught.getMessage());
				
			}

			@Override
			public void onSuccess(QueueSummary result) {
				showSummary(result);
				qStatus.refresh();
			}
			
		});
	}
	
	class SubmissionTimeSorter implements Comparator<JobInfo> {

		@Override
		public int compare(JobInfo a0, JobInfo a1) {
			if (a0.creationTime == a1.creationTime) {
				return 0;
			}
			
			return a0.creationTime < a1.creationTime ? 1 : -1;
		}
		
	}
	
	protected void showSummary(QueueSummary summary) {
		removeAll();
		
		mainPanel.add(qStatus.getWidget());
		
		Collections.sort(summary.jobInfo, new SubmissionTimeSorter());
		
		for(JobInfo info : summary.jobInfo) {
			Widget w = (new SingleJobPanel(info, this)).getWidget();
			if (info.status.equalsIgnoreCase("running")) {
				runningJobsPanel.add( w );
			}
			else {
				if (info.status.contains("Completed") || info.status.contains("error") || info.status.contains("Error"))
					completedJobsPanel.add( w );
				else
					waitingJobsPanel.add(w);
			}
		}
		
		if (runningJobsPanel.getWidgetCount()==0) {
			runningJobsPanel.add( new HTML("<h2>No jobs found</h2>"));
		}
		if (completedJobsPanel.getWidgetCount()==0) {
			completedJobsPanel.add( new HTML("<h2>No jobs found</h2>"));
		}
		if (waitingJobsPanel.getWidgetCount()==0) {
			waitingJobsPanel.add( new HTML("<h2>No jobs found</h2>"));
		}
		
		if (recentDetailsID != null)
			getQueryForJob(recentDetailsID);
	}
	
	/**
	 * Return the main widget containing all display info for this object
	 * @return
	 */
	public Widget getWidget() {
		return mainPanel;
	}

	/**
	 * Remove all job listing widgets from the running, completed, and waiting panels
	 */
	private void removeAll() {
		while(completedJobsPanel.getWidgetCount() > 0) {
			completedJobsPanel.remove(0);
		}
		while(runningJobsPanel.getWidgetCount() > 0) {
			runningJobsPanel.remove(0);
		}
		while(waitingJobsPanel.getWidgetCount() > 0) {
			waitingJobsPanel.remove(0);
		}
	}

	/**
	 * Initiate the jobQueryService to fetch details for a particular jobID from 
	 * the server
	 * @param jobID
	 */
	protected void getQueryForJob(final String jobID) {
		jobQueryFetcher.queryJob(jobID, new AsyncCallback<JobQueryResult>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error getting details for job : " + caught.getMessage());
			}

			@Override
			public void onSuccess(JobQueryResult result) {
				showDetailsPanel(result, jobID);
			}
			
		});
	}
	
	/**
	 * Initiates a new call to the server to cancel the job with the requested id
	 * @param jobID
	 */
	public void cancelJob(final String jobID, final String sampleName, boolean confirm) {
		JobModifyRequest req = new JobModifyRequest();
		req.setJobID(jobID);
		req.setType(JobModifyRequest.Type.DELETE);
		
		if (confirm) {
			boolean ok = Window.confirm("Terminate job for sample " + sampleName + "?");
			if (!ok) {
				return;
			}
		}
		
		jobModifyFetcher.modifyJob(req, new AsyncCallback<JobModifyResult>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Could not delete job " + jobID + "\n" + caught.getMessage());
			}

			@Override
			public void onSuccess(JobModifyResult result) {
				if (result.getType() == ResultType.OK) {
					refreshList();
				}
				else {
					Window.alert("Could not delete job " + jobID + "\n" + result.getErrorMessage());
				}
			}
			
		});
	}

	/**
	 * Clear all elements from details panel, then parse values in the 
	 * given result statusVals and add them to the details panel 
	 * @param result
	 */
	protected void showDetailsPanel(JobQueryResult result, String jobID) {
		recentDetailsID = jobID;
		while(detailsPanel.getWidgetCount() > 0) {
			detailsPanel.remove(0);
		}
		
		//Parse out some info for a reasonable header....
		if (result.statusVals.containsKey("Sample") && result.statusVals.containsKey("Analysis type")) {
			HTML htmlHeader = new HTML("<h3>" + result.statusVals.get("Sample") + " : " +  result.statusVals.get("Analysis type") + "</h3>");
			detailsPanel.add(htmlHeader);
			result.statusVals.remove("Sample");
			result.statusVals.remove("Analysis type");
		}
		
		//Check to see if there are errors
		if (result.statusVals.get("error") != null) {
			String message = result.statusVals.get("error");
			Label errorLabel = new Label("ERROR : " + message);
			errorLabel.setStyleName("error", true);
			detailsPanel.add(errorLabel);
		}
		
		//PARSE SPECIAL-CASE KEYS FIRST AND REMOVE THEM, 
		//THEN ADD 'EM BACK AT THE END
		Widget vcfWidget = parseStatusKey("Final VCF", result.statusVals.get("Final VCF"));
		if (vcfWidget != null) {
			result.statusVals.remove("Final VCF");
		}
		
		Widget bamWidget = parseStatusKey("Final BAM", result.statusVals.get("Final BAM"));
		if (bamWidget != null) {
			result.statusVals.remove("Final BAM");
		}
		
		Widget qcWidget = parseStatusKey("QC report", result.statusVals.get("QC report"));
		if (qcWidget != null) {
			result.statusVals.remove("QC report");
		}
		
		Widget annoVarsWidget = parseStatusKey("Annotated variants", result.statusVals.get("Annotated variants"));
		if (annoVarsWidget != null) {
			result.statusVals.remove("Annotated variants");
		}
		
		
		for(String key : result.statusVals.keySet()) {
			String value = result.statusVals.get(key);
			Widget wig = parseStatusKey(key, value);
			detailsPanel.add(wig);
		}
		
		
		if (annoVarsWidget != null)
			detailsPanel.add(annoVarsWidget);
		
		if (bamWidget != null)
			detailsPanel.add(bamWidget);
		
		if (vcfWidget != null)
			detailsPanel.add(vcfWidget);
		
		if (qcWidget != null)
			detailsPanel.add(qcWidget);
		
		
		
	}

	/**
	 * Create a single widget that reflects the given key and value
	 * @param key
	 * @param value
	 * @return
	 */
	private Widget parseStatusKey(String key, String value) {
		if (value == null)
			return null;
		
		Widget wig = null;
		
		//A few special-case keys
		if (key.equals("Final VCF")) {
			wig = new HTML("Final variants file : <a href=\"" + value + "\"> Download </a>");
		}
		if (key.equals("QC report")) {
			wig = new HTML("QC Metrics : <a href=\"" + value + "\" target=\"_blank\"> View metrics </a>");
		}
		if (key.equals("Final BAM")) {
			wig = new HTML("Final BAM : <a href=\"" + value + "\"> Download </a>");
		}
		if (key.equals("Annotated variants")) {
			wig = new HTML("Annotated variants : <a href=\"" + value + "\"> Download </a>");
		}
		
		if (wig == null)
			wig = new HTML(key + " : " + value);
		return wig;
	}

	private void initComponents() {
		mainPanel = new HorizontalPanel();
		mainPanel.setWidth("860px");
		jobListPanel = new TabLayoutPanel(2.0, Unit.EM);
		jobListPanel.setStylePrimaryName("joblistpanel");
		jobListPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				handleTabSelectionChange(event);
			}
		});

		
		completedJobsPanel = new FlowPanel();
		ScrollPanel completedSP = new ScrollPanel( completedJobsPanel );
		completedSP.setWidth("420px");
		completedSP.setHeight("800px");
		completedJobsPanel.setWidth("420px");
		
		runningJobsPanel = new FlowPanel();
		runningJobsPanel.setWidth("420px");
		runningJobsPanel.setHeight("800px");
		
		
		waitingJobsPanel = new FlowPanel();
		ScrollPanel waitingSP = new ScrollPanel(  waitingJobsPanel );
		waitingSP.setWidth("420px");
		waitingSP.setHeight("800px");
		waitingJobsPanel.setWidth("420px");
		
		HTML header = new HTML("Running");
		header.setStylePrimaryName("joblistheader");
		
		jobListPanel.add(runningJobsPanel, header);
		
		header = new HTML("Waiting");
		header.setStylePrimaryName("joblistheader");
		jobListPanel.add(waitingSP, header);
		
		header = new HTML("Completed");
		header.setStylePrimaryName("joblistheader");
		jobListPanel.add(completedSP, header);

		
		jobListPanel.selectTab(0);
		
		mainPanel.add(jobListPanel);
		
		
		
		detailsPanel = new VerticalPanel();
		detailsPanel.setWidth("350px");
		detailsPanel.setStylePrimaryName("detailspanel");
		mainPanel.add(detailsPanel);
		
		qStatus = new QueueStatusPanel();
		mainPanel.add(qStatus.getWidget());
		qStatus.refresh();
		
		timer = new Timer() {
			public void run() {
				refreshList();
			}
		};
		
		timer.scheduleRepeating(10 * 1000);
		
		
	}

	/**
	 * Handler callback to update style for tab headers in job list panel
	 * @param event
	 */
	protected void handleTabSelectionChange(SelectionEvent<Integer> event) {
		Integer selectedIndex = event.getSelectedItem();
		for(int i=0; i<jobListPanel.getWidgetCount(); i++) {
			Widget w = jobListPanel.getTabWidget(i);
			
			if (selectedIndex != null && selectedIndex == i) {
				w.removeStyleName("tab");
				w.addStyleName("tab-selected");
			}
			else {
				w.removeStyleName("tab-selected");
				w.addStyleName("tab");
			}
		}
	}


	class StatusLink {
		int number;
		String text;
		String target;
	}

	//private JobTreeView treeModel = new JobTreeView();
	//private CellTree jobTree = null;
	
	private FlowPanel completedJobsPanel;
	private FlowPanel runningJobsPanel;
	private FlowPanel waitingJobsPanel;
	
	private String recentDetailsID = null;
	private VerticalPanel detailsPanel;
	private HorizontalPanel mainPanel;
	//private ScrollPanel scrollPanel;
	private TabLayoutPanel jobListPanel;
	private QueueStatusPanel qStatus;
	
	private QueueSummaryServiceAsync qSummaryFetcher = GWT.create(QueueSummaryService.class);
	private JobQueryServiceAsync jobQueryFetcher = GWT.create(JobQueryService.class);
	private JobModifyServiceAsync jobModifyFetcher = GWT.create(JobModifyService.class);

	Timer timer;
	
}
