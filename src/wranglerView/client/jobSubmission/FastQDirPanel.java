package wranglerView.client.jobSubmission;

import wranglerView.shared.FastQDirInfo;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
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
		
		
		HTML topLabel = new HTML("<b>" + dirInfo.sampleName + "</b>");
		topLabel.setStylePrimaryName("filelabelheader");
		labelsPanel.add(topLabel);
		topLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		
		HTML pathLabel = new HTML( dirInfo.parentDir );
		labelsPanel.add(pathLabel);
		labelsPanel.add(new HTML( dirInfo.reads1 + " - " + dirInfo.reads1Size));
		labelsPanel.add(new HTML( dirInfo.reads2 + " - " + dirInfo.reads2Size));
		labelsPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		for(int i=1; i<labelsPanel.getWidgetCount(); i++) {
			labelsPanel.getWidget(i).setStylePrimaryName("filelabel");
		}
		labelsPanel.setWidth("250px");
		mainPanel.add(labelsPanel);
		wrapper.add(mainPanel);
		
		wrapper.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				handleClick();
				
			}
			
		});
		
		wrapper.addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				mainPanel.setStyleName("fileselector-selected");
				wrapper.setStyleName("noborder");
			}
		});
		
	}

	protected void handleClick() {
		parentPanel.setSelectedPanel(this);
	}
	
	public void setSelected(boolean b) {
		this.selected = b;
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
