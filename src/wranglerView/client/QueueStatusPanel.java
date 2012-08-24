package wranglerView.client;

import wranglerView.client.queueView.QueueSummaryService;
import wranglerView.client.queueView.QueueSummaryServiceAsync;
import wranglerView.shared.QueueSummary;
import wranglerView.shared.QueueSummary.JobInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A small panel that shows a quick update of the status of the main queue
 * @author brendan
 *
 */
public class QueueStatusPanel {

	VerticalPanel mainPanel;
	
	public QueueStatusPanel() {
		initComponents();
	}

	public Widget getWidget() {
		return mainPanel;
	}
	
	public void refresh() {
		
		qSummaryFetcher.getQueueSummary(new AsyncCallback< QueueSummary>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error retrieving job queue information: " + caught.getMessage());
			}

			@Override
			public void onSuccess(QueueSummary result) {
				setValues(result);
			}
			
		});
	}
	
	protected void setValues(QueueSummary result) {
		completed = 0;
		running = 0;
		waiting = 0;
		errors = 0;
		
		for(JobInfo info : result.jobInfo) {
			if (info.status.equalsIgnoreCase("waiting") || info.status.equals("initializing")) {
				waiting++;
			}
			if (info.status.equalsIgnoreCase("running")) {
				running++;
			}
			if (info.status.equalsIgnoreCase("Completed without error")) {
				completed++;
			}
			if (info.status.equalsIgnoreCase("error")) {
				errors++;
			}
		}
		
		completedLabel.setHTML("Completed jobs: " + completed);
		runningLabel.setHTML("Running jobs: " + running);
		waitingLabel.setHTML("Waiting jobs: " + waiting);
		errorsLabel.setHTML("Error jobs: " + errors);
	}
	
	private void initComponents() {
		mainPanel = new VerticalPanel();
		mainPanel.setStylePrimaryName("qstatus");
		
		HTML topLabel =new HTML("<b>Queue status:</b>");
		topLabel.setStylePrimaryName("qlabel");
		mainPanel.add(topLabel);
		
		completedLabel = new HTML("Completed jobs: ?");
		completedLabel.setStylePrimaryName("qlabel");
		runningLabel = new HTML("Running jobs: ?");
		runningLabel.setStylePrimaryName("qlabel");
		waitingLabel = new HTML("Waiting jobs: ?");
		waitingLabel.setStylePrimaryName("qlabel");
		errorsLabel = new HTML("Error jobs: ?");
		errorsLabel.setStylePrimaryName("qlabel");
		mainPanel.add(runningLabel);
		mainPanel.add(waitingLabel);
		mainPanel.add(completedLabel);
		mainPanel.add(errorsLabel);
	}
	
	private int completed;
	private int running;
	private int waiting;
	private int errors;
	private HTML errorsLabel;
	private HTML waitingLabel;
	private HTML runningLabel;
	private HTML completedLabel;
	
	private QueueSummaryServiceAsync qSummaryFetcher = GWT.create(QueueSummaryService.class);

}

