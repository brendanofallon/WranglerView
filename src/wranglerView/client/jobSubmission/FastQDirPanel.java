package wranglerView.client.jobSubmission;

import wranglerView.shared.FastQDirInfo;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
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
		//wrapper.setStylePrimaryName("fqfocuspanel");
		wrapper.setStylePrimaryName("noborder");
		mainPanel = new HorizontalPanel();
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		mainPanel.setStylePrimaryName("fileselector");
		
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
		
//		HTML pathLabel = new HTML( dirInfo.parentDir );
//		labelsPanel.add(pathLabel);
		HTML timeLabel = new HTML(  dirInfo.modifiedTime );
		labelsPanel.add(timeLabel);
		labelsPanel.add(new HTML( shortenLongStr(dirInfo.reads1, 20) + " - " + dirInfo.reads1Size));
		labelsPanel.add(new HTML( shortenLongStr(dirInfo.reads2, 20) + " - " + dirInfo.reads2Size));
		labelsPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		for(int i=1; i<labelsPanel.getWidgetCount(); i++) {
			labelsPanel.getWidget(i).setStylePrimaryName("filelabel");
		}
		labelsPanel.setWidth("250px");
		mainPanel.add(labelsPanel);
		wrapper.add(mainPanel);
		
		wrapper.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				wrapper.setStylePrimaryName("fqfocuspanel-hovering");
			} 
			
		});

		wrapper.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				wrapper.setStylePrimaryName("fqfocuspanel");
			}
			
		});
		
		wrapper.addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				handleClick();
				wrapper.setStyleName("noborder");
			}
		});
		
	}

	private static String shortenLongStr(String str, int length) {
		if (str == null) {
			return "no info";
		}
		if (str.length() <= length)
			return str;
		else {
			int firstLength = length-4;
			int lastPart = 4;
			return str.substring(0, firstLength) + "..." + str.substring(str.length()-lastPart);
		}
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
		wrapper.setStyleName("noborder");
	}

	public Panel getWidget() {
		return wrapper;
	}
	
	FocusPanel wrapper;
	HorizontalPanel mainPanel;	
}
