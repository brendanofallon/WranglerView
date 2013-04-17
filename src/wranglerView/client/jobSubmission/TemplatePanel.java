package wranglerView.client.jobSubmission;

import wranglerView.shared.TemplateInfo;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TemplatePanel {

	private final TemplateInfo templateInfo;
	private final TemplatesPanel parentPanel;
	private boolean selected = false;
	
	public TemplatePanel(TemplatesPanel parent, TemplateInfo info) {
		this.templateInfo = info;
		this.parentPanel = parent;
		initComponents();
		
	}
	
	
	public TemplateInfo getTemplateInfo() {
		return templateInfo;
	}
	
	private void initComponents() {
		wrapper = new FocusPanel();
		mainPanel = new HorizontalPanel();
		mainPanel.setStyleName("templatemain");
		Image folderImage = new Image("images/star.png");
		mainPanel.add(folderImage);
		
		VerticalPanel labelsPanel = new VerticalPanel();
		labelsPanel.getElement().setId("templatemain");
		
		HTML topLabel = new HTML("<b>" + templateInfo.templateName + "</b> (v. " + templateInfo.version + ")");
		topLabel.setStylePrimaryName("filelabelheader");
		labelsPanel.add(topLabel);
		labelsPanel.add(new HTML( templateInfo.description ));
		
		for(int i=1; i<labelsPanel.getWidgetCount(); i++) {
			labelsPanel.getWidget(i).setStylePrimaryName("filelabel");
		}
		
		
		mainPanel.add(labelsPanel);
		wrapper.add(mainPanel);
		
		
		wrapper.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				handleClick();
				
			}
			
		});
		
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
				wrapper.setStyleName("noborder");
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
			mainPanel.setStyleName("templatemain-selected");
		}
		else {
			mainPanel.setStyleName("templatemain");
		}
	}

	public Panel getWidget() {
		return wrapper;
	}
	
	FocusPanel wrapper;
	HorizontalPanel mainPanel;



	
}
