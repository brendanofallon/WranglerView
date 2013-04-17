package wranglerView.server.template;

import java.io.IOException;
import java.util.List;

import wranglerView.shared.TemplateInfo;

/**
 * These guys can return a list of TemplateInfos. 
 * @author brendan
 *
 */
public interface TemplateReqHandler {

	/**
	 * Update list of templates to reflect recent changes
	 */
	public void scanTemplates() throws IOException;
	
	public List<TemplateInfo> getAvailableTemplates();
	
}
