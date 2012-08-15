package wranglerView.client;

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
	
	private void showJobSubmissionPanel() {
		//Weird, since this uses Root.getElement.... in it's constructor, we don't need
		//to add its widget or anything
		jobSubmissionPanel = new JobSubmissionPanel();
	}

	private void intializeToolbar() {
		RootPanel toolbarPanel = RootPanel.get("toolbar");
		ToolBar toolbar = new ToolBar();
		toolbarPanel.add(toolbar.getWidget());
	}
	

	private JobSubmissionPanel jobSubmissionPanel = null;
	
}
