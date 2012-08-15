package wranglerView.server;

import java.util.ArrayList;
import java.util.List;

import wranglerView.client.jobSubmission.TemplateService;
import wranglerView.shared.TemplateInfo;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TemplateServiceImpl extends RemoteServiceServlet implements TemplateService {

	@Override
	public List<TemplateInfo> getAvailableTemplates() {
		 List<TemplateInfo> templateList = new ArrayList<TemplateInfo>();
		 
		 TemplateInfo infoA = new TemplateInfo();
		 infoA.description = "Standard, no-frills exome analysis";
		 infoA.templateName = "Generic Exome";
		 templateList.add(infoA);
		 
		 TemplateInfo infoB = new TemplateInfo();
		 infoB.description = "Clinical Retinitis Pigmentosa analysis";
		 infoB.templateName = "Ret. Pigmentosa";
		 templateList.add(infoB);
		 
		 
		 TemplateInfo infoC = new TemplateInfo();
		 infoC.description = "Clinical mitochondrial sequence analysis";
		 infoC.templateName = "MT. Seq. Analysis";
		 templateList.add(infoC);
		 
		 TemplateInfo infoD = new TemplateInfo();
		 infoD.description = "Clinical aortapathies analysis";
		 infoD.templateName = "Aortapathies analysis";
		 templateList.add(infoD);
		 
		return templateList;
	}

}
