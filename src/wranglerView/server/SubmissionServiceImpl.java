package wranglerView.server;

import jobWrangler.dispatch.Dispatcher;
import jobWrangler.dispatch.DispatcherManager;
import jobWrangler.job.Job;
import jobWrangler.job.ShellJob;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import wranglerView.client.jobSubmission.SubmissionService;
import wranglerView.logging.WLogger;
import wranglerView.server.jobSubmission.JobBuilder;
import wranglerView.shared.AnalysisJobDescription;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SubmissionServiceImpl extends RemoteServiceServlet implements SubmissionService{

	public static final String defaultProjectRoot = WranglerProperties.getExecutionDirPath(); //
	
	private JobBuilder builder = null;
	
	
	
	public JobBuilder getBuilder() {
		return builder;
	}



	public void setBuilder(JobBuilder builder) {
		this.builder = builder;
	}



	@Override
	public String submitJob(AnalysisJobDescription jobDesc) throws IllegalArgumentException {
		WLogger.info("Submitting a new job with sample: " + jobDesc.sampleName +" fastqdir: " + jobDesc.pathToFastQDir + " analysis id:" + jobDesc.templateID);
	
		
		if (builder == null) {
			String path = "spring.xml";
			WLogger.info("Loading spring config from " + path);
			ApplicationContext context = new ClassPathXmlApplicationContext(path);
			builder = (JobBuilder) context.getBean("jobBuilder");
		}	
		
		Job jobToSubmit = null;
		if (builder != null) {
			jobToSubmit = builder.createJob(jobDesc);
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

//	private Job buildMarcJob(AnalysisJobDescription jobDesc) {
//		String templateID = jobDesc.templateID;
//		DirTemplateSource tReg;
//		WranglerJob job = null;
//		try {
//			tReg = DirTemplateSource.getRegistry();
//			File templateFile = tReg.getFileForID(templateID);
//			
//			String projHomeName = jobDesc.sampleName + "-" + ("" + System.currentTimeMillis()).substring(5);
//			File jobHome = new File(defaultProjectRoot + "/" + projHomeName);
//			if (jobHome.exists()) {
//				throw new IllegalArgumentException("Yikes, project home " + jobHome.getAbsolutePath() + " already exists!");
//			}
//			else {
//				jobHome.mkdir();
//			}
//
//			String fastq1 = jobDesc.pathToFastQDir + "/" + jobDesc.reads1Name;
//			String fastq2 = jobDesc.pathToFastQDir + "/" + jobDesc.reads2Name;
//			
//			job = new MarcJob(jobHome, 
//					templateFile.getAbsolutePath(), 
//					jobDesc.sampleName,
//					jobDesc.submitter,
//					fastq1,
//					fastq2);
//			
//			job.setAnalysisType(jobDesc.templateName);
//			job.setSampleName(jobDesc.sampleName);
//			job.setSubmitter(jobDesc.submitter);
//			
//			WLogger.info("Created new Marc job for sample : " + jobDesc.sampleName + " with home: " + jobHome.getAbsolutePath() + " and config file: " + templateFile.getName() + " job id is: " + job.getID());
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//			WLogger.severe("Error Marc creating job for sample: " + jobDesc.sampleName + " : " + e.getMessage() );
//			return null;
//		}
//		
//		return job;			
//	}
		


}
