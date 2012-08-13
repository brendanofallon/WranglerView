package wranglerView.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WranglerView2 implements EntryPoint {
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
	
		RootPanel toolbar = RootPanel.get("toolbar");
		Button button1 = new Button("Submit Job");
		toolbar.add(button1);
		
		
		RootPanel rightpanel = RootPanel.get("rightpanel");
		JobSettingsPanel settingsPanel = new JobSettingsPanel();
		rightpanel.add(settingsPanel.getWidget());
		
		System.out.println("Module name is : " + GWT.getModuleBaseURL());
	}
	
}
