package wranglerView.client;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class ToolBar {

	public ToolBar() {
		initComponents();
	}
	
	public Widget getWidget() {
		return mainPanel;
	}
	
	private void initComponents() {
		mainPanel = new HorizontalPanel();
		
		Image addImage = new Image("images/newfile.png");
		PushButton button1 = new PushButton( addImage );
		button1.setStylePrimaryName("toolbarbutton");
		
		mainPanel.add(button1);
		
		Image monitorImage = new Image("images/monitor.png");
		PushButton button2 = new PushButton( monitorImage );
		button2.setStylePrimaryName("toolbarbutton");
		mainPanel.add(button2);
	}
	
	private HorizontalPanel mainPanel;
}
