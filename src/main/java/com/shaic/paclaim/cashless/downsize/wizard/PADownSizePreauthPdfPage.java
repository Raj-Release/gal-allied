package com.shaic.paclaim.cashless.downsize.wizard;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Panel;

public class PADownSizePreauthPdfPage extends ViewComponent implements WizardStep<PreauthDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PreauthDTO bean;
	
	public static String dataDir = System.getProperty("jboss.server.data.dir");

	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;
		
	}
	
	@Override
	public String getCaption() {
		return "Decision Communication";
	}

	@Override
	public Component getContent() {
		Panel panel = new Panel();
		panel.setCaption("Withdrawal of Authorization Letter");
       // panel.setSizeFull();
        panel.setHeight("450px");
        
        DocumentGenerator docGen = new DocumentGenerator();
		ReportDto reportDto = new ReportDto();
		
		
		if(this.bean.getPreauthMedicalDecisionDetails() != null && this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null){
			String amtInwords = SHAUtils.getParsedAmount(this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt());	
			this.bean.getPreauthMedicalDecisionDetails().setAmountInwords(amtInwords);
		}
		
		if(this.bean.getPreauthDataExtractionDetails() != null && this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList() != null){
			List<DiagnosisDetailsTableDTO> diagnosisList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
			String diagnosis = "";
			if(!diagnosisList.isEmpty()){
			for(DiagnosisDetailsTableDTO diagnosisDto : diagnosisList){
				if(diagnosis.equals("")){
					diagnosis = diagnosisDto.getDiagnosisName().getValue();
				}
				else{
				diagnosis += " / " + ( diagnosisDto.getDiagnosisName() != null ? diagnosisDto.getDiagnosisName().getValue() : " / " ) ;
				}
			}
			}
			if(!diagnosis.equals("")){
			this.bean.getPreauthDataExtractionDetails().setDiagnosis(diagnosis);
			}
		}
		
		List<PreauthDTO> preauthDTOList = new ArrayList<PreauthDTO>();
		preauthDTOList.add(this.bean);		
		reportDto.setClaimId(bean.getClaimDTO().getClaimId());
		reportDto.setBeanList(preauthDTOList);
		final String filePath = docGen.generatePdfDocument("PreauthDownSizeLetter", reportDto);     
		
		if(null != bean)
        {
        	bean.setDocFilePath(filePath);
        	bean.setDocType(SHAConstants.ENHANCEMENT_DOWNSIZE_LETTER);
        	bean.setDocSource(SHAConstants.DOWNSIZE_PRE_AUTH);
        }
		
		Path p = Paths.get(filePath);
		String fileName = p.getFileName().toString();
		StreamResource.StreamSource s = SHAUtils.getStreamResource(filePath);

		/*StreamResource.StreamSource s = new StreamResource.StreamSource() {

			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 9138325634649289303L;

			public InputStream getStream() {
				try {

					File f = new File(filePath);
					System.out.println(f.getCanonicalPath());
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
		panel.setContent(e);
		return panel;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onAdvance() {
		return true;
	}

	@Override
	public boolean onBack() {
		return true;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}
	

}
