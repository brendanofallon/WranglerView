package wranglerView.client;

import wranglerView.client.queueView.QueueView;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WranglerView implements EntryPoint {
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
	
		RootPanel mainPanel = RootPanel.get("displayarea");
		mainPanel.add( mainArea );
		intializeToolbar();
		showJobSubmissionPanel();

	}
	
	void showJobSubmissionPanel() {
		//Weird, since this uses Root.getElement.... in it's constructor, we don't need
		//to add its widget or anything
		clearPanel();
		jobSubmissionPanel = new JobSubmissionPanel();
		mainArea.setWidget( jobSubmissionPanel.getWidget() );
		
	}
	
	
	void showQueueViewPanel() {
		clearPanel();
		
		
		QueueView qvPanel = new QueueView();
		mainArea.add( qvPanel.getWidget() );
		qvPanel.refreshList();
	}

	private void intializeToolbar() {
		RootPanel toolbarPanel = RootPanel.get("toolbar");
		ToolBar toolbar = new ToolBar(this);
		toolbarPanel.add(toolbar.getWidget());
	}
	
	private void clearPanel() {
		Widget w = mainArea.getWidget();
		if (w != null)
			mainArea.remove( w );
		
	}
	

	private JobSubmissionPanel jobSubmissionPanel = null;
	SimplePanel mainArea = new SimplePanel();
}
