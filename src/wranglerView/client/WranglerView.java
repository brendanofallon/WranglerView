package wranglerView.client;

import wranglerView.shared.FastQDirInfo;
import wranglerView.shared.TemplateInfo;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WranglerView implements EntryPoint {
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
	
		RootPanel toolbar = RootPanel.get("toolbar");
		Button button1 = new Button("Submit Job");
		toolbar.add(button1);
		
		
		RootPanel rightpanel = RootPanel.get("rightpanel");
		JobSettingsPanel settingsPanel = new JobSettingsPanel();
		rightpanel.add(settingsPanel.getWidget());
		
		FastQDirInfo fq1 = new FastQDirInfo();
		fq1.parentDir = "somesample#1";
		fq1.reads1 = "/home/brendan/reads1.fq.gz";
		fq1.reads2 = "/home/brendan/reads2.fq.gz";
		fq1.sampleName = "test sample #1";
		
		RootPanel leftPanel = RootPanel.get("leftpanel");
		FastQsPanel fqsPanel = new FastQsPanel();
		fqsPanel.addFQInfo(fq1);
		
		FastQDirInfo fq2 = new FastQDirInfo();
		fq2.parentDir = "Sample#2";
		fq2.reads1 = "/home/brendan/reads_A1.fq.gz";
		fq2.reads2 = "/home/brendan/reads_B2.fq.gz";
		fq2.sampleName = "test sample #2";
		
		fqsPanel.addFQInfo(fq2);
		
		

		FastQDirInfo fq3 = new FastQDirInfo();
		fq3.parentDir = "Crazy sample";
		fq3.reads1 = "/home/brendan/reads_crazy1.fq.gz";
		fq3.reads2 = "/home/brendan/reads_crazy2.fq.gz";
		fq3.sampleName = "test sample #3";
		
		fqsPanel.addFQInfo(fq3);
		
		leftPanel.add(fqsPanel.getWidget());
		
		
			
		RootPanel centerPanel = RootPanel.get("centerpanel");
		TemplatesPanel templatesPanel = new TemplatesPanel();
		centerPanel.add(templatesPanel.getWidget());
		
		TemplateInfo tInfo1 = new TemplateInfo();
		tInfo1.templateName = "Full exome analysis";
		tInfo1.description ="Plain old full-exome business";
		templatesPanel.addTemplate(tInfo1);
		
		TemplateInfo tInfo2 = new TemplateInfo();
		tInfo2.templateName = "Mito seq. analysis";
		tInfo2.description = "Something to do with mitochondria, I guess";
		templatesPanel.addTemplate(tInfo2);
		
		TemplateInfo tInfo3 = new TemplateInfo();
		tInfo3.templateName = "Aortapathies analysis";
		tInfo3.description = "See if the patient has something wrong with aortapathies";
		templatesPanel.addTemplate(tInfo3);
		
	}
	
}
