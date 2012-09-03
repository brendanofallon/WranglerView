package wranglerView.client;

import java.util.ArrayList;
import java.util.List;

import wranglerView.shared.QueueSummary.JobInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class TreePanel {

	Tree tree;
	
	public TreePanel(List<JobCategory> cats) {
		tree = new Tree();
		
		for(JobCategory cat : cats) {
			SafeHtmlBuilder sb = new SafeHtmlBuilder();
			sb.appendHtmlConstant("<h2>" + cat.title + "</h2>");
			TreeItem catItem = new TreeItem(sb.toSafeHtml());
			tree.addItem( catItem );
			
			for(JobInfo jInfo : cat.jobs) {
				TreeItem folderItem = new TreeItem("Sample : " + jInfo.sampleName);
				catItem.addItem( folderItem );
			}
		}
		
		
		
		tree.setAnimationEnabled(true);
		tree.setWidth("500px");
	}
	
	public Tree getTree() {
		return tree;
	}
	  
	static class JobCategory {
		String title;
		List<JobInfo> jobs = new ArrayList<JobInfo>();
		
		public JobCategory(String title) {
			this.title = title;
		}
		
		public void addJobInfo(JobInfo info) {
			jobs.add(info);
		}
	}
	

}
