package wranglerView.client;

import wranglerView.client.queueView.QueueView;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WranglerView implements EntryPoint {
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
	
		intializeToolbar();
		showJobSubmissionPanel();

	}
	
	void showJobSubmissionPanel() {
		//Weird, since this uses Root.getElement.... in it's constructor, we don't need
		//to add its widget or anything
		clearPanels();
		jobSubmissionPanel = new JobSubmissionPanel();
	}
	
	
	void showQueueViewPanel() {
		clearPanels();
		
		RootPanel leftPanel = RootPanel.get("leftpanel");
		QueueView qvPanel = new QueueView();
		leftPanel.add( qvPanel.getWidget() );
		
	}

	private void intializeToolbar() {
		RootPanel toolbarPanel = RootPanel.get("toolbar");
		ToolBar toolbar = new ToolBar(this);
		toolbarPanel.add(toolbar.getWidget());
	}
	
	private void clearPanels() {
		RootPanel leftPanel = RootPanel.get("leftpanel");
		while(leftPanel.getWidgetCount() > 0) {
			leftPanel.remove(0);
		}
		
		
		RootPanel centerPanel = RootPanel.get("centerpanel");
		while(centerPanel.getWidgetCount() > 0) {
			centerPanel.remove(0);
		}
		
		RootPanel rightPanel = RootPanel.get("rightpanel");
		while(rightPanel.getWidgetCount() > 0) {
			rightPanel.remove(0);
		}
		
	}
	

	private JobSubmissionPanel jobSubmissionPanel = null;
	
}
