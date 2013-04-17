package wranglerView.server.jobSubmission;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import jobWrangler.job.Job;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import wranglerView.logging.WLogger;
import wranglerView.server.PipelineJob;
import wranglerView.server.WranglerJob;
import wranglerView.server.template.TemplateReqHandler;
import wranglerView.server.template.TemplateTransformer;
import wranglerView.shared.AnalysisJobDescription;
import wranglerView.shared.TemplateInfo;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * Create Pipeline jobs (input files, really) from an analysis template. Requires a TemplateHandler
 * to get the actual template to convert, as well a directory to put the working dir in. 
 * @author brendan
 *
 */
public class PipelineJobBuilder implements JobBuilder {

	private TemplateReqHandler templateSource = null;
	private File workingDir = null;
	
	public TemplateReqHandler getTemplateSource() {
		return templateSource;
	}

	public void setTemplateSource(TemplateReqHandler templateSource) {
		this.templateSource = templateSource;
	}
	
	public File getWorkingDir() {
		return workingDir;
	}

	public void setWorkingDir(File workingDir) {
		this.workingDir = workingDir;
	}

	@Override
	public Job createJob(AnalysisJobDescription jobDesc) {
		
		String templateID = jobDesc.templateID;
		
		WranglerJob job = null;
		try {
			InputStream template = templateSource.getTemplateForID(templateID);
			TemplateInfo info = templateSource.getInfoForID(jobDesc.templateID);
		 
			Map<String, String> subs = new HashMap<String, String>();
			subs.put("SAMPLE", jobDesc.sampleName);
			
			subs.put("INPUTFILE", jobDesc.pathToFastQDir + "/" + jobDesc.reads1Name);
			subs.put("INPUTFILE2", jobDesc.pathToFastQDir + "/" + jobDesc.reads2Name);
			subs.put("DESTDIR", jobDesc.destDirName);
			subs.put("SUBMITTER", jobDesc.submitter);
			subs.put("ANALYSIS_TYPE", jobDesc.templateName + " (v. " + info.version + ")");
			
			
			Document inputDoc = TemplateTransformer.transformTemplate(template, subs);
			
			
			String projHomeName = jobDesc.destDirName + "-" + ("" + System.currentTimeMillis()).substring(5);
			File jobHome = new File(workingDir + "/" + projHomeName);
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
