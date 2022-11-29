package com.shaic.claim.outpatient.processOPpages.assessmentsheet;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

public class OPBillAssessmentPageUI extends ViewComponent{

	private static final long serialVersionUID = -6039649831441636195L;
	
	@Inject
	private OutPatientDTO bean;
	
	//private GWizard wizard;
	
	
	//private VerticalLayout approveLayout;
	
	Map<String, Object> referenceData;
	
	private WeakHashMap fileMap;
	
	public void init(OutPatientDTO bean, GWizard wizard) {
		this.bean = bean;
		this.fileMap = new WeakHashMap();
		//this.wizard = wizard;
	}

	public Component getContent() {
		DocumentGenerator docGen = new DocumentGenerator();
		ReportDto reportDto = new ReportDto();
		
		List<OutPatientDTO> dtoList = new ArrayList<OutPatientDTO>();
		dtoList.add(this.bean);
		
		reportDto.setClaimId(this.bean.getClaimDTO().getClaimId());
		reportDto.setBeanList(dtoList);		
		String filePath = "";
		
		filePath = docGen.generatePdfDocument("OPClaimsAssessmentSheet", reportDto);	
		
		final String finalFilePath = filePath;
		Path p = Paths.get(finalFilePath);
		String fileName = p.getFileName().toString();
		fileMap.put("BillSummaryDocFilePath", finalFilePath);
		fileMap.put("BillSummaryDocType", SHAConstants.OP_BILLASSESSMENTSHEET);
		this.bean.setFilePathAndTypeMap(fileMap);

		StreamResource.StreamSource s = new StreamResource.StreamSource() {

			private static final long serialVersionUID = -8311516294452493897L;

			public FileInputStream getStream() {
				try {

					File f = new File(finalFilePath);
					FileInputStream fis = new FileInputStream(f);
					return fis;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		};

		StreamResource r = new StreamResource(s, fileName);
		Embedded e = new Embedded();
		e.setSizeFull();
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		HorizontalLayout horizontalLayout = new HorizontalLayout(e);
		horizontalLayout.setWidth("100%");
		horizontalLayout.setHeight("500px");
		Panel panel = new Panel(horizontalLayout);
		 panel.setHeight("450px");

		 return panel;
		}
	
	
	
	@SuppressWarnings("unchecked")
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		
	}
	
	@SuppressWarnings("static-access")
	public boolean validatePage() {
		/*Boolean hasError = false;
		String eMsg = "";*/	
		return true;
	}

	
}
