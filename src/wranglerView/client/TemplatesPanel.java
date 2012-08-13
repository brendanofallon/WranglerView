package wranglerView.client;

import java.util.ArrayList;
import java.util.List;

import wranglerView.shared.FastQDirInfo;
import wranglerView.shared.TemplateInfo;

import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TemplatesPanel {

	private List<TemplatePanel> fqPanels = null;
	private List<TemplateInfo> templateInfo = null;
	private FastQDirInfo selectedFQInfo = null;
	
	public TemplatesPanel() {
		initComponents();
	}
	
	public void setTemplateList(List<TemplateInfo> fqs) {
		templateInfo = fqs;
		layoutPanels();
	}
	
	public void addTemplate(TemplateInfo item) {
		if (templateInfo == null)
			templateInfo = new ArrayList<TemplateInfo>(16);
		templateInfo.add(item);
		layoutPanels();
	}
	
	public void setSelectedPanel(TemplatePanel selectedPanel) {
		for(TemplatePanel fqPanel : fqPanels) {
			if (fqPanel == selectedPanel) {
				fqPanel.setSelected(true);
			}
			else {
				fqPanel.setSelected(false);
			}
		}
	}
	
	/**
	 * Remove all child widgets and add new FastQDirPanel widgets for all items
	 * currently in the FastQDirInfo list
	 */
	private void layoutPanels() {
		removeAll();
		fqPanels = new ArrayList<TemplatePanel>();
		
		if (templateInfo == null)
			return;
		
		for(TemplateInfo info : templateInfo) {
			TemplatePanel panel = new TemplatePanel(this, info);
			fqPanels.add(panel);
			mainPanel.add(panel.getWidget());
		}
	}
	
	/**
	 * Remove all widgets from main panel
	 */
	private void removeAll() {
		int count = 0;
		while(mainPanel.getWidgetCount() > 0) {
			mainPanel.remove(0);
			count++;
			if (count > 1000) {
				break;
			}
		}
	}
	
	private void initComponents() {
		mainPanel = new VerticalPanel();
	}


	public CellPanel getWidget() {
		return mainPanel;
	}
	
	
	VerticalPanel mainPanel;
}
