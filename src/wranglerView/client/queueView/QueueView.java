package wranglerView.client.queueView;


import wranglerView.client.QueueStatusPanel;
import wranglerView.shared.JobQueryResult;
import wranglerView.shared.QueueSummary;
import wranglerView.shared.QueueSummary.JobInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
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
			jobListPanel.add( createJobInfoWidget(info));
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

	private Widget createJobInfoWidget(final JobInfo info) {
		HorizontalPanel panel = new HorizontalPanel();
		panel.setStylePrimaryName("jobinfo");
		
		Image image = null;
		if (info.status.equalsIgnoreCase("waiting") || info.status.equals("initializing")) {
			image = new Image("images/waiting.png");
		}
		if (info.status.equalsIgnoreCase("running")) {
			image = new Image("images/running.png");
		}
		if (info.status.equalsIgnoreCase("Completed without error")) {
			image = new Image("images/completedOK.png");
		}
		if (info.status.equalsIgnoreCase("error")) {
			image = new Image("images/error1.png");
		}
		if (image == null) {
			image = new Image("images/questionmark.png");
		}
		panel.add(image);
		System.out.println("Status of job " + info.sampleName + " is :" + info.status);
		VerticalPanel vp = new VerticalPanel();
		//vp.setStylePrimaryName("infolabel");
		
		
		vp.add(new HTML("Sample : " + info.sampleName));
		vp.add(new HTML("Status: " + info.status));
		vp.add(new HTML("Analysis type: " + info.analysisType));
		
		for(int i=0; i<vp.getWidgetCount(); i++) {
			vp.getWidget(i).setStylePrimaryName("infolabel");
		}
		
		
		panel.add(vp);
			
		Button detailsButton = new Button("Details");
		panel.add(detailsButton);
		detailsButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getQueryForJob(info.jobID);
			}
			
		});
		
		return panel;
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

	Timer timer;
	
}
