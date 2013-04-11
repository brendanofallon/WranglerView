package wranglerView.client.jobSubmission;

import java.util.ArrayList;
import java.util.List;

import wranglerView.client.JobSubmissionPanel;
import wranglerView.shared.FastQDirInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Presents a list of 'FastQDirInfo' objects to the user, typically as the left
 * panel of the main UI frame
 * @author brendan
 *
 */
public class FastQsPanel {

	private List<FastQDirPanel> fqPanels = null;
	private List<FastQDirInfo> topLevelFQInfos = null;
	private FastQDirInfo selectedFQInfo = null;
	private JobSubmissionPanel parentPanel = null;
	
	public FastQsPanel(JobSubmissionPanel parentPanel) {
		this.parentPanel = parentPanel;
		initComponents();
	}
	
	public FastQDirInfo getSelectedFQInfo() {
		return selectedFQInfo;
	}

	
	public void setFQInfoRoot(List<FastQDirInfo> infoList) {
		this.topLevelFQInfos = infoList;
		layoutPanels();
	}
	
	public void setSelectedPanel(FastQDirPanel selectedPanel) {
		
		for(FastQDirPanel fqPanel : fqPanels) {
			if (fqPanel == selectedPanel) {
				fqPanel.setSelected(true);
				this.selectedFQInfo = fqPanel.getFqInfo();
				parentPanel.setSelectedFastQ(selectedFQInfo);
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
		tree.removeItems();
		fqPanels = new ArrayList<FastQDirPanel>();
		for(FastQDirInfo info : topLevelFQInfos) {
			
			if (info.children.size()==0) {
				FastQDirPanel panel = new FastQDirPanel(this, info);
				fqPanels.add(panel);
				TreeItem fqItem = new TreeItem(panel.getWidget());
				tree.addItem(fqItem);
			}
			else {
				HTML html = new HTML("<h3>" + info.sampleName + "</h3>");
				TreeItem item = new TreeItem(html);
				addSubtree(item, info);
				tree.addItem(item);	
			}
			
		}
	}
	
	private void addSubtree(TreeItem parentItem, FastQDirInfo parentInfo) {
		if (parentInfo.children.size()>0) {
			for(FastQDirInfo kidInfo : parentInfo.children) {
				HTML html = new HTML("<h3>" + kidInfo.sampleName + "</h3>");
				html.setStylePrimaryName("fastqtreelabel");
				TreeItem kidItem = new TreeItem(html);
				parentItem.addItem(kidItem);
				addSubtree(kidItem, kidInfo);
			}
		}
		else {
			FastQDirPanel panel = new FastQDirPanel(this, parentInfo);
			fqPanels.add(panel);
			parentItem.setWidget(panel.getWidget());
			
		}
	}
	
	
	private void initComponents() {
		tree = new Tree(images);
		treePanel.add(tree);
		treePanel.setWidth("420px");
	}


	public Panel getWidget() {
		return treePanel;
	}
	
	
	Tree tree;
	ScrollPanel treePanel = new ScrollPanel();
	
	interface MyTreeImages extends Tree.Resources {
		
		  public ImageResource treeClosed();
		  
		  public ImageResource treeOpen();
		  
		  public ImageResource treeLeaf();
		  
	}
	  

	  Tree.Resources images = (Tree.Resources)GWT.create(MyTreeImages.class);

	
}
