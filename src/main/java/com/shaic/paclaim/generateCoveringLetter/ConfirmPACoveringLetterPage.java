package com.shaic.paclaim.generateCoveringLetter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.registration.GenerateCoveringLetterSearchTableDto;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * 
 * @author Lakshminarayana
 *
 */

public class ConfirmPACoveringLetterPage extends ViewComponent{

	
	GenerateCoveringLetterSearchTableDto bean;


	public void init(GenerateCoveringLetterSearchTableDto bean) {
		this.bean = bean;
	}
	
	public Component getContent() {

		if(bean.getClaimDto().getDocumentCheckListDTO() != null && !bean.getClaimDto().getDocumentCheckListDTO().isEmpty()){
			VerticalLayout vLayout = new VerticalLayout();
			vLayout.setWidth("100%");
			vLayout.setHeight("400px");
					
			DocumentGenerator docGenarator = new DocumentGenerator();
			
			List<ClaimDto> a_beanList = new ArrayList<ClaimDto>();
			a_beanList.add(bean.getClaimDto());
			
			ReportDto reportDto = new ReportDto();
			reportDto.setClaimId(bean.getClaimDto().getClaimId());
			reportDto.setBeanList(a_beanList);
			String templateName = "";
			
			templateName = null != bean.getClaimDto().getIncidenceFlagValue() ? ( (SHAConstants.ACCIDENT_FLAG).equalsIgnoreCase(bean.getClaimDto().getIncidenceFlagValue()) ? "AccidentCoveringLetter" : "DeathCoveringLetter" ) : "";
			final String filePath = docGenarator.generatePdfDocument(templateName, reportDto);
			bean.getClaimDto().setDocFilePath(filePath);
			bean.getClaimDto().setDocType(SHAConstants.PA_CLAIM_COVERING_LETTER);
			bean.getClaimDto().setConversionLetter(1l);
			Path p = Paths.get(filePath);
			String fileName = p.getFileName().toString();
			StreamResource.StreamSource s = SHAUtils.getStreamResource(filePath);
			/*StreamResource.StreamSource s = new StreamResource.StreamSource() {
	
				public FileInputStream getStream() {
					try {
	
						File f = new File(filePath);
						FileInputStream fis = new FileInputStream(f);
						return fis;
	
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}
			};*/
	
			StreamResource r = new StreamResource(s, fileName);
			Embedded e = new Embedded();
			e.setSizeFull();
			e.setType(Embedded.TYPE_BROWSER);
			r.setMIMEType("application/pdf");
			e.setSource(r);
			SHAUtils.closeStreamResource(s);
			Panel vPanel = new Panel();
			vPanel.setHeight("100%");
			vPanel.setContent(e);
			
			vLayout.addComponent(vPanel);
	
			return vLayout;
		}
		else{
			return new VerticalLayout(new Label("Claim Record Saved Successfully."));
		}
	}
}
