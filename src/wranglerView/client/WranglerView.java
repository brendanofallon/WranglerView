package wranglerView.client;

import wranglerView.client.queueView.QueueView;
import wranglerView.shared.AuthToken;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WranglerView implements EntryPoint {
	
	
	public static final boolean DEBUG = false;
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
	
		RootPanel mainPanel = RootPanel.get("displayarea");
		mainPanel.add( mainArea );
		intializeToolbar();
		
		if (DEBUG) {
			RootPanel debugArea = RootPanel.get("debugarea");	
			debugArea.add(debugPanel);
			debugPanel.add(new HTML("<h3><center>Debug mode is : ON </center></h3>"));
		}
		
		showLoginPanel();
		
		//showJobSubmissionPanel();

	}
	
	private void showLoginPanel() {
		clearPanel();
		LoginPanel panel = new LoginPanel(this);
		mainArea.setWidget( panel.getWidget() );
		
	}

	void showJobSubmissionPanel(AuthToken token) {
		clearPanel();
		if (toolbar != null)
			toolbar.setAuthToken(token);
		jobSubmissionPanel = new JobSubmissionPanel(token);
		mainArea.setWidget( jobSubmissionPanel.getWidget() );		
	}
	
	
	void showQueueViewPanel(AuthToken token) {
		clearPanel();
		if (toolbar != null)
			toolbar.setAuthToken(token);
		QueueView qvPanel = new QueueView();
		mainArea.add( qvPanel.getWidget() );
		qvPanel.refreshList();
	}

	private void intializeToolbar() {
		RootPanel toolbarPanel = RootPanel.get("toolbar");
		if (toolbar == null)
			toolbar = new ToolBar(this);
		toolbarPanel.add(toolbar.getWidget());
	}
	
	private void clearPanel() {
		Widget w = mainArea.getWidget();
		if (w != null)
			mainArea.remove( w );
		
	}
	
	ToolBar toolbar;
	private JobSubmissionPanel jobSubmissionPanel = null;
	SimplePanel mainArea = new SimplePanel();
	VerticalPanel debugPanel = new VerticalPanel();
}
