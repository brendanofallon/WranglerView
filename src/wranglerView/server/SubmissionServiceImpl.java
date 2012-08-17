package wranglerView.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import jobWrangler.dispatch.Dispatcher;
import jobWrangler.dispatch.DispatcherManager;
import jobWrangler.job.Job;
import jobWrangler.job.ShellJob;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import test.SleeperJob;
import wranglerView.client.jobSubmission.SubmissionService;
import wranglerView.server.template.TemplateRegistry;
import wranglerView.server.template.TemplateTransformer;
import wranglerView.shared.AnalysisJobDescription;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SubmissionServiceImpl extends RemoteServiceServlet implements SubmissionService{

	public static final String defaultProjectRoot = System.getProperty("user.home") + "/wrangler-jobs";
	
	@Override
	public void submitJob(AnalysisJobDescription jobDesc) throws IllegalArgumentException {
		System.out.println("Server got job with parent dir: " + jobDesc.pathToFastQDir + "\n AnalysisID: " + jobDesc.templateID + "\n Sample : " + jobDesc.sampleName);
		
		System.out.println("Creating job");
		Job jobToSubmit = buildJob(jobDesc);
		
		Dispatcher dispatcher = DispatcherManager.getDispatcher();
		
			
		//ShellJob job = new ShellJob("touch newjobfile.txt", new File( System.getProperty("user.dir")));
		System.out.println("Submitting job...");
		dispatcher.submitJob(jobToSubmit);
		System.out.println("Job submitted");
		
		
	}

	private Job buildJob(AnalysisJobDescription jobDesc) {
		
		// WranglerJob job = ...job ?
		String templateID = jobDesc.templateID;
		TemplateRegistry tReg;
		try {
			tReg = TemplateRegistry.getRegistry();
			File templateFile = tReg.getFileForID(templateID);
			
			Map<String, String> subs = new HashMap<String, String>();
			subs.put("SAMPLE", jobDesc.sampleName);
			subs.put("INPUTFILE", jobDesc.pathToFastQDir);
			subs.put("INPUTFILE2", jobDesc.pathToFastQDir);
			
			Document inputDoc = TemplateTransformer.transformTemplate(new BufferedReader(new FileReader(templateFile)), subs);
			
			
			// ....???
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		}
		
		int length = (int)(30.0*Math.random());
		ShellJob job = new SleeperJob(length);
		System.out.println("Creating sleeper job with wait time : " + length);
		
		return job;
	}

}
