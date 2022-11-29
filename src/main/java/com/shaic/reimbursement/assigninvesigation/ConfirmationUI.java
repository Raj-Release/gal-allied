package com.shaic.reimbursement.assigninvesigation;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.domain.ReferenceTable;
import com.shaic.reimbursement.draftinvesigation.DraftInvestigatorDto;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.HorizontalLayout;

public class ConfirmationUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private AssignInvestigatorDto assignInvestigatorDto;
	
	public void init(AssignInvestigatorDto assignInvestigatorDto){
		this.assignInvestigatorDto = assignInvestigatorDto;
	}
	
	public Component getContent() {	
		
		DocumentGenerator docGen = new DocumentGenerator();
		
		ReportDto reportDto = new ReportDto();
		reportDto.setClaimId(assignInvestigatorDto.getClaimDto().getClaimId());
		
		List<DraftInvestigatorDto> dtoList = new ArrayList<DraftInvestigatorDto>();
		DraftInvestigatorDto draftInvestigation = new DraftInvestigatorDto(assignInvestigatorDto);
		
		//dtoList.add(assignInvestigatorDto);
		dtoList.add(draftInvestigation);
		reportDto.setBeanList(dtoList);
		
		String templateName = "InvestigationLetter";
		if(ReferenceTable.PA_LOB_KEY.equals(assignInvestigatorDto.getClaimDto().getNewIntimationDto().getPolicy().getLobId())){
			templateName = "PAInvestigationLetter";
		}
		
		final String filePath = docGen.generatePdfDocument(templateName, reportDto);
								
		Path p = Paths.get(filePath);
		String fileName = p.getFileName().toString();
		StreamResource.StreamSource s = SHAUtils.getStreamResource(filePath);

		/*StreamResource.StreamSource s = new StreamResource.StreamSource() {

			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

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
		HorizontalLayout horizontalLayout = new HorizontalLayout(e);
		horizontalLayout.setWidth("100%");
		horizontalLayout.setHeight("400px");

		return horizontalLayout;
	}

}
