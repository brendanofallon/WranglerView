package wranglerView.server.template;

import java.io.IOException;
import java.io.InputStream;
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
	
	/**
	 * Obtain TemplateInfo objects fr all available templates
	 * @return
	 */
	public List<TemplateInfo> getAvailableTemplates();
	
	/**
	 * A reader that allows access to the template itself
	 * @param templateID
	 * @return
	 * @throws IOException
	 */
	public InputStream getTemplateForID(String templateID) throws IOException;
	
	/**
	 * Obtain templateInfo for a specific template ID
	 * @param templateID
	 * @return
	 */
	public TemplateInfo getInfoForID(String templateID);
}
