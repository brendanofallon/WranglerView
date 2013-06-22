package wranglerView.server.jobInfo;

import jobWrangler.dispatch.Dispatcher;
import jobWrangler.job.Job;
import jobWrangler.job.Job.JobState;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import wranglerView.client.queueView.JobModifyService;
import wranglerView.shared.JobModifyRequest;
import wranglerView.shared.JobModifyResult;
import wranglerView.shared.JobModifyResult.ResultType;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * This serivce handles requests to modify jobs, either by removing jobs from the queue or
 * terminating executing jobs (in the future, we may also handle re-ordering jobs in the queue)
 * @author brendan
 *
 */
public class JobModifyServiceImpl extends RemoteServiceServlet implements JobModifyService {

	Dispatcher dispatcher = null;
	
	
	
	public Dispatcher getDispatcher() {
		return dispatcher;
	}



	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}



	@Override
	public JobModifyResult modifyJob(JobModifyRequest req) {
		JobModifyResult result = new JobModifyResult();
		
		if (dispatcher == null) {	
			String path = "spring.xml";
			ApplicationContext context = new ClassPathXmlApplicationContext(path);
			dispatcher = (Dispatcher) context.getBean("dispatcher");
		}
		
		Job job = dispatcher.getJobForID(req.getID());
		
		if (job == null) {
			result.setType(ResultType.ERROR);
			result.setErrorMessage("No job with id " + req.getID() + " found");
			return result;
		}
		
		if (req.getRequestType()==JobModifyRequest.Type.DELETE) {
			if (job.getJobState() == JobState.UNINITIALIZED) {
				boolean ok = dispatcher.removeWaitingJob(job);
				if (ok) {
					return result;
				}
				else {
					result.setType(ResultType.ERROR);
					result.setErrorMessage("Job with id " + job.getID() + " not in waiting queue");
					return result;
				}
			} else {
				dispatcher.requestKillJob(job);
			}
		}
		else {
			result.setType(ResultType.ERROR);
			result.setErrorMessage("Cannot honor request type " + req.getRequestType());
		}
		
		return result;
	}
	

}
