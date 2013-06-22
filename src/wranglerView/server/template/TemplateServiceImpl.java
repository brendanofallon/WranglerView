package wranglerView.server.template;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import wranglerView.client.jobSubmission.TemplateService;
import wranglerView.logging.WLogger;
import wranglerView.shared.TemplateInfo;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Uses a TemplateReq to find a list of templates that can be used 
 * for various analyses, and returns the list. 
 * @author brendan
 *
 */
public class TemplateServiceImpl extends RemoteServiceServlet implements TemplateService {

	TemplateReqHandler handler = null;
	
	@Override
	public List<TemplateInfo> getAvailableTemplates() {
		 
		if (handler == null) {
			String path = "spring.xml";
			WLogger.info("Loading spring config from " + path);
			ApplicationContext context = new ClassPathXmlApplicationContext(path);
			handler = (TemplateReqHandler) context.getBean("templateHandler");
			
		}
		
		if (handler != null) {
			return handler.getAvailableTemplates();
		}
		
		WLogger.warn("Template handler not initialized, template service is returning empty template list");
		 
		return new ArrayList<TemplateInfo>();
	}

}
