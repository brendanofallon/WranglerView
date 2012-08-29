package wranglerView.client.queueView;


import wranglerView.client.QueueStatusPanel;
import wranglerView.shared.JobModifyRequest;
import wranglerView.shared.JobModifyResult;
import wranglerView.shared.JobModifyResult.ResultType;
import wranglerView.shared.JobQueryResult;
import wranglerView.shared.QueueSummary;
import wranglerView.shared.QueueSummary.JobInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
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
	
	
	protected void showSummary(QueueSummary summary) {
		removeAll();
		
		mainPanel.add(qStatus.getWidget());
		if (summary.jobInfo == null || summary.jobInfo.size() == 0) {
			jobListPanel.add(new HTML("<h2> No jobs found in queue </h2>"));
			return;
		}
		
		for(JobInfo info : summary.jobInfo) {
			SingleJobPanel jobPanel = new SingleJobPanel(info, this);
			jobListPanel.add( jobPanel.getWidget() );
		}
		
	}
	
	public Widget getWidget() {
		return mainPanel;
	}

	private void removeAll() {
		while(jobListPanel.getWidgetCount() > 0) {
			jobListPanel.remove(0);
		}
	}

	protected void getQueryForJob(String jobID) {
		jobQueryFetcher.queryJob(jobID, new AsyncCallback<JobQueryResult>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error getting details for job : " + caught.getMessage());
			}

			@Override
			public void onSuccess(JobQueryResult result) {
				showDetailsPanel(result);
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

	protected void showDetailsPanel(JobQueryResult result) {
		while(detailsPanel.getWidgetCount() > 0) {
			detailsPanel.remove(0);
		}
		
		for(String key : result.statusVals.keySet()) {
			String value = result.statusVals.get(key);
			HTML msg = new HTML(key + " : " + value);
			detailsPanel.add(msg);
			msg.setStylePrimaryName("detailmessage");
		}
	}

	private void initComponents() {
		mainPanel = new HorizontalPanel();
		mainPanel.setWidth("900px");
		jobListPanel = new VerticalPanel();
		scrollPanel = new ScrollPanel();
		scrollPanel.add(jobListPanel);
		scrollPanel.setWidth("400px");
		
		mainPanel.add(scrollPanel);
		
		detailsPanel = new VerticalPanel();
		detailsPanel.setWidth("300px");
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



	private VerticalPanel detailsPanel;
	private HorizontalPanel mainPanel;
	private ScrollPanel scrollPanel;
	private VerticalPanel jobListPanel;
	private QueueStatusPanel qStatus;
	
	private QueueSummaryServiceAsync qSummaryFetcher = GWT.create(QueueSummaryService.class);
	private JobQueryServiceAsync jobQueryFetcher = GWT.create(JobQueryService.class);
	private JobModifyServiceAsync jobModifyFetcher = GWT.create(JobModifyService.class);

	Timer timer;

	
	
}
