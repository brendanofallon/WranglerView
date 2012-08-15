package wranglerView.client.jobSubmission;

import wranglerView.shared.FastQDirInfo;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Draws a simple panel with some info about a fastQ-containing directory
 * @author brendan
 *
 */
public class FastQDirPanel {

	private final FastQDirInfo dirInfo;
	private final FastQsPanel parentPanel;
	private boolean selected = false;
	
	public FastQDirPanel(FastQsPanel parent, FastQDirInfo info) {
		this.dirInfo = info;
		this.parentPanel = parent;
		initComponents();
		
	}
	
	public FastQDirInfo getFqInfo() {
		return dirInfo;
	}
	
	private void initComponents() {
		wrapper = new FocusPanel();
		mainPanel = new HorizontalPanel();
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		mainPanel.setStyleName("fileselector");
		
		Image folderImage = new Image("images/folder.png");
		folderImage.setSize("36px", "36px");
		folderImage.getElement().setId("folderimage");
		
		mainPanel.add(folderImage);
		
		VerticalPanel labelsPanel = new VerticalPanel();
		labelsPanel.getElement().setId("filelabels");
		
		HTML topLabel = new HTML("<b>" + dirInfo.parentDir + "</b>");
		topLabel.setStylePrimaryName("filelabelheader");
		labelsPanel.add(topLabel);
		topLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		labelsPanel.add(new HTML( dirInfo.reads1 ));
		labelsPanel.add(new HTML( dirInfo.reads2 ));
		labelsPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		for(int i=1; i<labelsPanel.getWidgetCount(); i++) {
			labelsPanel.getWidget(i).setStylePrimaryName("filelabel");
		}
		labelsPanel.setWidth("200px");
		mainPanel.add(labelsPanel);
		wrapper.add(mainPanel);
		
		
		
		wrapper.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				handleClick();
				
			}
			
		});
		
	}

	protected void handleClick() {
		parentPanel.setSelectedPanel(this);
	}
	
	public void setSelected(boolean b) {
		this.selected = b;
		System.out.println("A new fq panel has been selected");
		if (selected) {
			mainPanel.setStyleName("fileselector-selected");
		}
		else {
			mainPanel.setStyleName("fileselector");
		}
	}

	public Panel getWidget() {
		return wrapper;
	}
	
	FocusPanel wrapper;
	HorizontalPanel mainPanel;



	
	
}
