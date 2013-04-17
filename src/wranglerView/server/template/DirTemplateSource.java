package wranglerView.server.template;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wranglerView.logging.WLogger;
import wranglerView.server.WranglerProperties;
import wranglerView.shared.TemplateInfo;

/**
 * Reads templates from a directory. 
 * @author brendan
 *
 */
public class DirTemplateSource implements TemplateReqHandler {

	public static final String defaultTemplateDir = WranglerProperties.getTemplatesPath();
	private File templateDir;
	private Map<String, TemplateInfo> templates;
	private Map<String, File> templateFiles;
	
	public DirTemplateSource() throws IOException {
		this(new File(defaultTemplateDir));
	}
	
	public DirTemplateSource(File templateDir) throws IOException {
		WLogger.info("Creating new TemplateRegistry object with template dir: " + templateDir);
		this.templateDir = templateDir;
		scanTemplates();
	}
	
	public void setTemplateDir(File templateDir) {
		this.templateDir = templateDir;
		try {
			scanTemplates();
		} catch (IOException e) {
			WLogger.severe("Error reading template directory: " + templateDir + " : " + e.getLocalizedMessage());
		}
	}

	/**
	 * Return the directory File used to search for templates
	 * @return
	 */
	public File getTemplateDirectory() {
		return templateDir;
	}
	
	/**
	 * Obtain the absolute path to the file associated with the given id
	 * @param templateID
	 * @return
	 */
	public File getFileForID(String templateID) {
		return templateFiles.get(templateID);	
	}
	
	/**
	 * Obtain the TemplateInfo object associated with the given ID
	 * @param templateID
	 * @return
	 */
	public TemplateInfo getInfoForID(String templateID) {
		return templates.get(templateID);
	}
	
	/**
	 * @throws IOException 
	 * 
	 */
	public void scanTemplates() throws IOException {
		if (! templateDir.exists()) {
			WLogger.severe( "Template directory " + templateDir.getAbsolutePath() + " does not exist!" );
			throw new IOException("Template directory " + templateDir.getAbsolutePath() + " does not exist");
		}
		
		if (! templateDir.isDirectory()) {
			WLogger.severe( "Template directory " + templateDir.getAbsolutePath() + " is not a directory" );
			throw new IOException("Template directory " + templateDir.getAbsolutePath() + " is not a directory");
		}
		
		templates = new HashMap<String, TemplateInfo>();
		templateFiles = new HashMap<String, File>();
		
		File[] files = templateDir.listFiles();
		for(int i=0; i<files.length; i++) {
			
			//Parse Brendan-style templates
			if (files[i].getName().endsWith(".xml")
					|| files[i].getName().endsWith(".cnf")) {
				TemplateInfo info = parseInfoForFile(files[i]);
			
				
				
				if (info != null) {
					if (templates.get(info.templateID) != null) {
						throw new IOException("Conflicting template ids found: " + info.templateID);
					}
					
					templates.put(info.templateID, info);
					templateFiles.put(info.templateID, files[i]);
					//WLogger.info("Creating new analysis template with name: " + info.templateName + " and id: " + info.templateID);
				}
			}
			
			
			
		}
		
	}

	/**
	 * Scan the given file for .xml files which we parse to see if they're 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private TemplateInfo parseInfoForFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		TemplateInfo info = new TemplateInfo();
		
		boolean hasName = false;
		boolean hasDesc = false;
		boolean hasID = false;
		boolean hasVersion = false;
		
		while(line != null) {
			
			if (line.contains("Analysis name")) {
				String name = line.replace("Analysis name", "").replace(":", "");
				name = name.replace("<!--", "");
				name = name.replace("-->", "");
				name = name.replace("#", "");
				name = name.trim();
				info.templateName = name;
				hasName = true;
			}
			
			if (line.contains("Analysis description")) {
				String desc = line.replace("Analysis description", "").replace(":", "");
				desc = desc.replace("<!--", "");
				desc = desc.replace("-->", "");
				desc = desc.replace("#", "");
				desc = desc.trim();
				info.description = desc;
				hasDesc = true;
			}
			
			if (line.contains("Analysis ID")) {
				String id = line.replace("Analysis ID", "").replace(":", "");
				id = id.replace("<!--", "");
				id = id.replace("-->", "");
				id = id.replace("#", "");
				id = id.trim();
				info.templateID = id;
				hasID = true;
			}
			
			if (line.contains("Version")) {
				String id = line.replace("Version", "").replace(":", "");
				id = id.replace("<!--", "");
				id = id.replace("-->", "");
				id = id.replace("#", "");
				id = id.trim();
				info.version = id;
				hasVersion = true;
			}
			
			if (hasName && hasID && hasDesc && hasVersion) {
				break;
			}
			
			line = reader.readLine();
		}
		
		if (file.getName().endsWith("xml")) {
			info.isMarcAnalysis = false;
		}
		if (file.getName().endsWith("cnf")) {
			info.isMarcAnalysis = true;
		}
		
		reader.close();
		return info;
	}

	/**
	 * Obtain a list of all available templates found by thsi registry
	 * @return
	 */
	public List<TemplateInfo> getAvailableTemplates() {
		List<TemplateInfo> list = new ArrayList<TemplateInfo>();
		list.addAll( templates.values() );
		return list;
	}

}
