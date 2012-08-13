package wranglerView.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
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
	
	private final SubmissionServiceAsync submissionService = GWT.create(SubmissionService.class);
	
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
		
		Button submitJobButton = new Button("Submit job");
		mainPanel.add(submitJobButton);
		submitJobButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				handleSubmitButtonClick();
			}
		});
	}

	/**
	 * Called when submit job button is pressed...
	 */
	protected void handleSubmitButtonClick() {
		submissionService.submitJob("Some input str", new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				System.out.println("Failure!");
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				System.out.println("Success, btw");
			}
			
		});
	}
}
