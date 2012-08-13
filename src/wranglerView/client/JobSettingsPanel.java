package wranglerView.client;

import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Creates and manages objects for applying settings, like sample id, submitter name, and
 * notifications (someday) for a job that will be submitted soon 
 * @author brendanofallon
 *
 */
public class JobSettingsPanel {

	VerticalPanel mainPanel;
	
	public JobSettingsPanel() {
		initComponents();
	}
	
	public CellPanel getWidget() {
		return mainPanel;
	}

	private void initComponents() {
		mainPanel = new VerticalPanel();
		
		Label sampleIDLabel = new Label("Enter Sample ID:");
		TextBox sampleIdBox = new TextBox();
		sampleIdBox.setText("enter sample id");
		
		mainPanel.add(sampleIDLabel);
		mainPanel.add(sampleIdBox);
		
		
		Label submitterLabel = new Label("Enter Submitter (e.g. your name):");
		TextBox submitterIdBox = new TextBox();
		mainPanel.add(submitterLabel);
		mainPanel.add(submitterIdBox);
	}
}
