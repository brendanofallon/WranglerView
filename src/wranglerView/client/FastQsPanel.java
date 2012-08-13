package wranglerView.client;

import java.util.ArrayList;
import java.util.List;

import wranglerView.shared.FastQDirInfo;

import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Presents a list of 'FastQDirInfo' objects to the user, typically as the left
 * panel of the main UI frame
 * @author brendan
 *
 */
public class FastQsPanel {

	private List<FastQDirPanel> fqPanels = null;
	private List<FastQDirInfo> fqInfo = null;
	private FastQDirInfo selectedFQInfo = null;
	
	public FastQsPanel() {
		initComponents();
	}
	
	public void setFQInfoList(List<FastQDirInfo> fqs) {
		fqInfo = fqs;
		layoutPanels();
	}
	
	public void addFQInfo(FastQDirInfo fqItem) {
		if (fqInfo == null)
			fqInfo = new ArrayList<FastQDirInfo>(16);
		fqInfo.add(fqItem);
		layoutPanels();
	}
	
	public void setSelectedPanel(FastQDirPanel selectedPanel) {
		for(FastQDirPanel fqPanel : fqPanels) {
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
		fqPanels = new ArrayList<FastQDirPanel>();
		
		if (fqInfo == null)
			return;
		
		for(FastQDirInfo info : fqInfo) {
			FastQDirPanel panel = new FastQDirPanel(this, info);
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
