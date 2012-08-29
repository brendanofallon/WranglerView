package wranglerView.client.queueView;

import wranglerView.shared.QueueSummary.JobInfo;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Displays information for a single job in the QueueView
 * @author brendan
 *
 */
public class SingleJobPanel {

	final JobInfo info;
	final QueueView parent;
	
	public SingleJobPanel(JobInfo info, QueueView parent) {
		this.info = info;
		this.parent = parent;
		initComponents();
	}
	
	public Panel getWidget() {
		return panel;
	}
	
	private Widget initComponents() {
		panel = new HorizontalPanel();
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
			
		VerticalPanel buttonsPanel = new VerticalPanel();
		
		
		PushButton detailsButton = new PushButton("Job Details");
		detailsButton.setStylePrimaryName("jobbutton");
		
		detailsButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				parent.getQueryForJob(info.jobID);
			}
			
		});
		
		
		buttonsPanel.add(detailsButton);
		
		PushButton cancelButton = new PushButton("Cancel job");
		final boolean running = info.status.equalsIgnoreCase("running");
		if (running) {
			cancelButton.setStylePrimaryName("jobbutton-red");
		}
		else {
			cancelButton.setStylePrimaryName("jobbutton");
		}
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				 parent.cancelJob(info.jobID, info.sampleName, running);
			}
			
		});
		
		if (info.status.equalsIgnoreCase("Completed without error") || info.status.equalsIgnoreCase("error")) {
			//	Anything we should do here? 	
		}
		else {
			buttonsPanel.add(cancelButton);
		}
		
		
		panel.add(buttonsPanel);
		
		return panel;
	}
	
	
	HorizontalPanel panel;
}
