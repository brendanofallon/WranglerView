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
import jobWrangler.job.ShellJob;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import test.ExplodingJob;
import test.SleeperJob;
import wranglerView.client.jobSubmission.SubmissionService;
import wranglerView.logging.WLogger;
import wranglerView.server.template.DirTemplateSource;
import wranglerView.server.template.TemplateTransformer;
import wranglerView.shared.AnalysisJobDescription;
import wranglerView.shared.TemplateInfo;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class SubmissionServiceImpl extends RemoteServiceServlet implements SubmissionService{

	public static final String defaultProjectRoot = WranglerProperties.getExecutionDirPath(); //
	
	@Override
	public String submitJob(AnalysisJobDescription jobDesc) throws IllegalArgumentException {
		WLogger.info("Submitting a new job with sample: " + jobDesc.sampleName +" fastqdir: " + jobDesc.pathToFastQDir + " analysis id:" + jobDesc.templateID);
		
		Job jobToSubmit = null;
		if (jobDesc.analysisStyle == AnalysisJobDescription.AnalysisStyle.BRENDAN)
			jobToSubmit = buildBrendanJob(jobDesc);
			
		if (jobDesc.analysisStyle == AnalysisJobDescription.AnalysisStyle.MARC)
			jobToSubmit = buildMarcJob(jobDesc);
		
		//A few debugging jobs...
		if (jobDesc.analysisStyle == AnalysisJobDescription.AnalysisStyle.EXPLODE_JOB) {
			String projHomeName = jobDesc.sampleName + "-" + ("" + System.currentTimeMillis()).substring(5);
			ExplodingJob exJob = new ExplodingJob();
			exJob.setBaseDir( new File(defaultProjectRoot + "/" + projHomeName) );
			jobToSubmit = exJob;
			
		}
		if (jobDesc.analysisStyle == AnalysisJobDescription.AnalysisStyle.WAIT_JOB) {
			jobToSubmit = new SleeperJob();
		}
			
		Dispatcher dispatcher = DispatcherManager.getDispatcher();
		
		if (jobToSubmit != null)
			dispatcher.submitJob(jobToSubmit);		
		
		
		String homeDir = "unknown";
		if(jobToSubmit instanceof ShellJob) {
			homeDir = ((ShellJob)jobToSubmit).getBaseDir().getName();
		}
		
		return homeDir;
	}

	private Job buildMarcJob(AnalysisJobDescription jobDesc) {
		String templateID = jobDesc.templateID;
		DirTemplateSource tReg;
		WranglerJob job = null;
		try {
			tReg = DirTemplateSource.getRegistry();
			File templateFile = tReg.getFileForID(templateID);
			
			String projHomeName = jobDesc.sampleName + "-" + ("" + System.currentTimeMillis()).substring(5);
			File jobHome = new File(defaultProjectRoot + "/" + projHomeName);
			if (jobHome.exists()) {
				throw new IllegalArgumentException("Yikes, project home " + jobHome.getAbsolutePath() + " already exists!");
			}
			else {
				jobHome.mkdir();
			}

			String fastq1 = jobDesc.pathToFastQDir + "/" + jobDesc.reads1Name;
			String fastq2 = jobDesc.pathToFastQDir + "/" + jobDesc.reads2Name;
			
			job = new MarcJob(jobHome, 
					templateFile.getAbsolutePath(), 
					jobDesc.sampleName,
					jobDesc.submitter,
					fastq1,
					fastq2);
			
			job.setAnalysisType(jobDesc.templateName);
			job.setSampleName(jobDesc.sampleName);
			job.setSubmitter(jobDesc.submitter);
			
			WLogger.info("Created new Marc job for sample : " + jobDesc.sampleName + " with home: " + jobHome.getAbsolutePath() + " and config file: " + templateFile.getName() + " job id is: " + job.getID());
			
		} catch (IOException e) {
			e.printStackTrace();
			WLogger.severe("Error Marc creating job for sample: " + jobDesc.sampleName + " : " + e.getMessage() );
			return null;
		}
		
		return job;			
	}
		
	/**
	 * Construct a Brendan-style Pipeline job from the given analysis description object 	
	 * @param jobDesc
	 * @return
	 */
	private Job buildBrendanJob(AnalysisJobDescription jobDesc) {
		
		String templateID = jobDesc.templateID;
		DirTemplateSource tReg;
		
		WranglerJob job = null;
		try {
			tReg = DirTemplateSource.getRegistry();
			File templateFile = tReg.getFileForID(templateID);
			TemplateInfo info = tReg.getInfoForID(jobDesc.templateID);
			//Search for fastq files within 
			
			//At some point we should have different job builders for different
			//types of jobs
			Map<String, String> subs = new HashMap<String, String>();
			subs.put("SAMPLE", jobDesc.sampleName);
			
			subs.put("INPUTFILE", jobDesc.pathToFastQDir + "/" + jobDesc.reads1Name);
			subs.put("INPUTFILE2", jobDesc.pathToFastQDir + "/" + jobDesc.reads2Name);
			subs.put("DESTDIR", jobDesc.destDirName);
			subs.put("SUBMITTER", jobDesc.submitter);
			subs.put("ANALYSIS_TYPE", jobDesc.templateName + " (v. " + info.version + ")");
			
			
			Document inputDoc = TemplateTransformer.transformTemplate(new BufferedReader(new FileReader(templateFile)), subs);
			
			
			String projHomeName = jobDesc.destDirName + "-" + ("" + System.currentTimeMillis()).substring(5);
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
