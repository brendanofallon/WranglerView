package wranglerView.client;

import wranglerView.shared.AuthToken;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class ToolBar {
	
	final WranglerView mainView;
	private AuthToken token;

	public ToolBar(WranglerView view) {
		this.mainView = view;
		initComponents();
	}
	
	public void setAuthToken(AuthToken tok) {
		this.token = tok;
	}
	
	public Widget getWidget() {
		return mainPanel;
	}
	
	private void initComponents() {
		mainPanel = new HorizontalPanel();
		
		Image addImage = new Image("images/submitJobButton.png");
		PushButton button1 = new PushButton( addImage );
		button1.setStylePrimaryName("toolbarbutton");
		button1.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showSubmissionPanel();
				
			}
			
		});
		
		mainPanel.add(button1);
		
		Image monitorImage = new Image("images/viewQueueButton.png");
		PushButton button2 = new PushButton( monitorImage );
		button2.setStylePrimaryName("toolbarbutton");
		button2.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showQueuePanel();
				
			}
			
		});
		mainPanel.add(button2);
	}
	
	protected void showQueuePanel() {
		mainView.showQueueViewPanel(token);
	}

	protected void showSubmissionPanel() {
		mainView.showJobSubmissionPanel(token);
	}

	private HorizontalPanel mainPanel;
}
