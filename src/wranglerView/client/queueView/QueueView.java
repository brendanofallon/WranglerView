package wranglerView.client.queueView;


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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

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
			}
			
		});
	}
	
	
	protected void showSummary(QueueSummary summary) {
		removeAll();
		
		if (summary.jobInfo == null || summary.jobInfo.size() == 0) {
			mainPanel.add(new HTML("<h2> No jobs found in queue </h2>"));
			return;
		}
		
		for(JobInfo info : summary.jobInfo) {
			mainPanel.add( createJobInfoWidget(info));
		}
		
	}
	
	public Widget getWidget() {
		return mainPanel;
	}

	private void removeAll() {
		while(mainPanel.getWidgetCount() > 0) {
			mainPanel.remove(0);
		}
	}

	private Widget createJobInfoWidget(JobInfo info) {
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
		
		
		vp.add(new HTML("Sample : " + info.sampleName + " Status: " + info.status));
		vp.add(new HTML("Analysis type: " + info.analysisType));
		vp.add(new Label("Start time: " + info.startTime));
		vp.add(new Label("Submitter: " + info.submitter));
		for(int i=0; i<vp.getWidgetCount(); i++) {
			vp.getWidget(i).setStylePrimaryName("infolabel");
		}
		
		
		panel.add(vp);
			
		
		return panel;
	}
	
	

	private void initComponents() {
		mainPanel = new VerticalPanel();
//		Button refreshButton  = new Button("Refresh list");
//		refreshButton.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				refreshList();
//			}
//		});
//		refreshList();
		
		timer = new Timer() {
			public void run() {
				refreshList();
			}
		};
		
		timer.scheduleRepeating(10 * 1000);
		
		
	}




	private VerticalPanel mainPanel;
	
	private QueueSummaryServiceAsync qSummaryFetcher = GWT.create(QueueSummaryService.class);

	Timer timer;
	
}
