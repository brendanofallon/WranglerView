package wranglerView.client.queueView;

import java.util.ArrayList;
import java.util.List;

import wranglerView.shared.QueueSummary;
import wranglerView.shared.QueueSummary.JobInfo;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.TreeViewModel;

public class JobTreeView implements TreeViewModel {

	final JobCategory[] jobCats = new JobCategory[3];

	public JobTreeView() {
		jobCats[0] = new JobCategory();
		jobCats[0].title = "Running Jobs";
		jobCats[0].jobsInCategory = new ArrayList<JobInfo>();
		
		jobCats[1] = new JobCategory();
		jobCats[1].title = "Waiting Jobs";
		jobCats[1].jobsInCategory = new ArrayList<JobInfo>();
		
		jobCats[2] = new JobCategory();
		jobCats[2].title = "Completed Jobs";
		jobCats[2].jobsInCategory = new ArrayList<JobInfo>();
	}
	
	public JobTreeView(QueueSummary qData) {
		this();
		
		refreshData(qData);
	}
	
	public void refreshData(QueueSummary qData) {
		jobCats[0].jobsInCategory.clear();
		jobCats[1].jobsInCategory.clear();
		jobCats[2].jobsInCategory.clear();
		
		System.out.println("Refreshing tree view data...");
		
		for(JobInfo info : qData.jobInfo) {
			if (info.status.equalsIgnoreCase("Running")) {
				jobCats[0].jobsInCategory.add(info);
				continue;
			}
			
			if (info.status.contains("Completed")) {
				jobCats[2].jobsInCategory.add(info);
				continue;
			}

			//Job not running and not completed, must be waiting?
			jobCats[1].jobsInCategory.add(info);
		}
	}
	
	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {		
		if (value == null) {
			System.out.println("Got value root, must be at root of tree...");
			//The root, right?
			ListDataProvider<JobCategory> children = new ListDataProvider<JobCategory>();
			
			children.getList().add(jobCats[0]);
			children.getList().add(jobCats[1]);
			children.getList().add(jobCats[2]);
			
			System.out.println("Catgeory 0 has " + jobCats[0].jobsInCategory.size() + " jobs");
			System.out.println("Catgeory 1 has " + jobCats[1].jobsInCategory.size() + " jobs");
			System.out.println("Catgeory 2 has " + jobCats[2].jobsInCategory.size() + " jobs");
			
			return new DefaultNodeInfo<JobCategory>(children, new JobCategoryCell());
		}
		
		if (value instanceof JobCategory) {
			JobCategory cat = (JobCategory)value;
			System.out.println("Got JobCategory obj..");
			
			ListDataProvider<JobInfo> children = new ListDataProvider<JobInfo>();
			if (cat.jobsInCategory != null)
				children.getList().addAll( cat.jobsInCategory );
			
			System.out.println("found " + children.getList().size() + " kids in category");
			return new DefaultNodeInfo<JobInfo>(children, new JobInfoCell());
		}
		
		System.out.println("Found non-null, non- known category...");
        return null;
	}

	@Override
	public boolean isLeaf(Object value) {
		if (value == null || value instanceof JobCategory)
			return false;
		return true;
	}
	
	static class JobCategory {
		String title = null;
		List<JobInfo> jobsInCategory = null;
	}
	
	class JobInfoCell extends AbstractCell<JobInfo> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				JobInfo info, SafeHtmlBuilder sb) {
			
			
			//NOT THE WAY TO USER SAFEHTML.. CAN CELLS BE USED AT ALL WITH USER-SUPPLIED INFO?
			sb.appendHtmlConstant("<ul id=\"jobinfolist\"> <li> First list item! </li>");
//			sb.appendHtmlConstant("<li> Sample name: ").appendEscaped(info.sampleName).appendHtmlConstant("</li>");
//			sb.appendHtmlConstant("<li> Analysis type: ").appendEscaped( info.analysisType).appendHtmlConstant("</li>");
//			sb.appendHtmlConstant("<li> Date submitted: ").appendEscaped( info.creationTime).appendHtmlConstant("</li>");
			sb.appendHtmlConstant("</ul>");
			
			
		}
		
	}
	
	class JobCategoryCell extends AbstractCell<JobCategory> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				JobCategory cat, SafeHtmlBuilder sb) {
			
			sb.appendHtmlConstant("<b> Some category </b>");
			
		}
		
	}

}
