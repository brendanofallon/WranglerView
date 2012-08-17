package wranglerView.server.template;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class TemplateTransformer {

	/**
	 * Takes a template file in proto-xml format, and substitutes in actual terms from the given map
	 * for the key replacement terms, typically denoted in ${REPLACEME} format in the template
	 * @param templateFile
	 * @param substitutions
	 * @return
	 * @throws IOException 
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 */
	public static Document transformTemplate(InputStream template, Map<String, String> substitutions) throws IOException, ParserConfigurationException, SAXException {
		return transformTemplate(new BufferedReader(new InputStreamReader(template)), substitutions);
	}

	public static Document transformTemplate(BufferedReader reader, Map<String, String> substitutions) throws IOException, ParserConfigurationException, SAXException {
		String substitutedTemplate = substituteTerms(reader, substitutions);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
				
		Document xmlDoc = builder.parse( new ByteArrayInputStream( substitutedTemplate.getBytes("UTF-8")) );
		return xmlDoc;
	}
	
	/**
	 * Read in the template file, and replace all matching occurrences of everything in the substitutions map
	 * with the value terms in the substitutions map
	 * @param templateFile
	 * @param substitutions
	 * @return
	 * @throws IOException
	 */
	private static String substituteTerms(InputStream template, Map<String, String> substitutions) throws IOException {
		return substituteTerms(new BufferedReader(new InputStreamReader(template)), substitutions);
	}
	
	private static String substituteTerms(BufferedReader reader, Map<String, String> substitutions) throws IOException {
		StringBuilder str = new StringBuilder();
		String lineSep = System.getProperty("line.separator");
		
		String line = reader.readLine();
		while(line != null) {
			if (line.trim().length()==0) {
				line = reader.readLine();
				continue;
			}
			
			String tLine = line;
			for(String key : substitutions.keySet()) {
				String tag = keyToTag(key);
				String repStr = substitutions.get(key);
				if (repStr == null) {
					throw new IllegalArgumentException("Found null value for key:" + key);
				}
				tLine = tLine.replace(tag, repStr);
				
			}
			str.append(tLine + lineSep);
			
			line = reader.readLine();
		}
		
		return str.toString();
	}

	private static String keyToTag(String key) {
		return "${" + key + "}";
	}
	
}
