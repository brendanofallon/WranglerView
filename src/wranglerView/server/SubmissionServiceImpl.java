package wranglerView.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import jobWrangler.dispatch.Dispatcher;
import jobWrangler.dispatch.DispatcherManager;
import jobWrangler.job.Job;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import wranglerView.client.jobSubmission.SubmissionService;
import wranglerView.logging.WLogger;
import wranglerView.server.template.TemplateRegistry;
import wranglerView.server.template.TemplateTransformer;
import wranglerView.shared.AnalysisJobDescription;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class SubmissionServiceImpl extends RemoteServiceServlet implements SubmissionService{

	public static final String defaultProjectRoot = WranglerProperties.getExecutionDirPath(); //
	
	@Override
	public String submitJob(AnalysisJobDescription jobDesc) throws IllegalArgumentException {
		//System.out.println("Server got job with parent dir: " + jobDesc.pathToFastQDir + "\n AnalysisID: " + jobDesc.templateID + "\n Sample : " + jobDesc.sampleName);
		WLogger.info("Submitting a new job with sample: " + jobDesc.sampleName +" fastqdir: " + jobDesc.pathToFastQDir + " analysis id:" + jobDesc.templateID);
		
		Job jobToSubmit = buildJob(jobDesc);
		
		Dispatcher dispatcher = DispatcherManager.getDispatcher();
		
		if (jobToSubmit != null)
			dispatcher.submitJob(jobToSubmit);		
		
		return jobToSubmit.getID();
	}

	private Job buildJob(AnalysisJobDescription jobDesc) {
		
		String templateID = jobDesc.templateID;
		TemplateRegistry tReg;
		
		WranglerJob job = null;
		try {
			tReg = TemplateRegistry.getRegistry();
			File templateFile = tReg.getFileForID(templateID);
			
			//Search for fastq files within 
			
			//At some point we should have different job builders for different
			//types of jobs
			Map<String, String> subs = new HashMap<String, String>();
			subs.put("SAMPLE", jobDesc.sampleName);
			
			subs.put("INPUTFILE", jobDesc.pathToFastQDir + "/" + jobDesc.reads1Name);
			subs.put("INPUTFILE2", jobDesc.pathToFastQDir + "/" + jobDesc.reads2Name);
			
			Document inputDoc = TemplateTransformer.transformTemplate(new BufferedReader(new FileReader(templateFile)), subs);
			
			
			String projHomeName = jobDesc.sampleName + "-" + ("" + System.currentTimeMillis()).substring(5);
			File jobHome = new File(defaultProjectRoot + "/" + projHomeName);
			if (jobHome.exists()) {
				throw new IllegalArgumentException("Yikes, project home " + jobHome.getAbsolutePath() + " already exists!");
			}
			else {
				jobHome.mkdir();
			}
			
			String inputFilename = jobDesc.sampleName + "_input.xml";
			writeInputFile(inputDoc, jobHome, inputFilename);
			
			
			job = new PipelineJob(jobHome, new File(inputFilename));
			job.setAnalysisType(jobDesc.templateName);
			job.setSampleName(jobDesc.sampleName);
			job.setSubmitter(jobDesc.submitter);
			
			WLogger.info("Created new Pipeline job for sample : " + jobDesc.sampleName + " with home: " + jobHome.getAbsolutePath() + " and input filename: " + inputFilename + " job id is: " + job.getID());
			
		} catch (IOException e) {
			e.printStackTrace();
			WLogger.severe("Error creating job for sample: " + jobDesc.sampleName + " : " + e.getMessage() );
			return null;
		} catch (ParserConfigurationException e) {
			WLogger.severe("Error creating job for sample: " + jobDesc.sampleName + " : " + e.getMessage() );
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			WLogger.severe("Error creating job for sample: " + jobDesc.sampleName + " : " + e.getMessage() );
			e.printStackTrace();
			return null;
		}
		
//		int length = (int)(30.0*Math.random());
//		ShellJob job = new SleeperJob(length);
//		System.out.println("Creating sleeper job with wait time : " + length);
		
		return job;
	}

	private void writeInputFile(Document doc, File jobHome, String filename) throws IOException {
		
		WLogger.info("Creating new input file in home: " + jobHome + " with file name: " + filename);
		
		OutputFormat format = new OutputFormat(doc);
        format.setLineWidth(80);
        format.setIndenting(true);
        format.setIndent(4);
        Writer writer = new BufferedWriter(new FileWriter(jobHome + "/" + filename));
        XMLSerializer serializer = new XMLSerializer(writer, format);
        serializer.serialize(doc);
        writer.close();
        
	}

}
