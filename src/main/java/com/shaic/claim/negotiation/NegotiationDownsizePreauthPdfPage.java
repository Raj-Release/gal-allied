package com.shaic.claim.negotiation;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.teemu.wizards.WizardStep;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ReportDto;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.server.StreamResource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;

public class NegotiationDownsizePreauthPdfPage extends ViewComponent implements WizardStep<PreauthDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PreauthDTO bean;
	
	public static String dataDir = System.getProperty("jboss.server.data.dir");

	private CheckBox validateLetterChk;
	
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
		/*Panel panel = new Panel();
		panel.setCaption("Withdrawal of Authorization Letter");
       // panel.setSizeFull();
        panel.setHeight("450px");*/
        
        DocumentGenerator docGen = new DocumentGenerator();
		ReportDto reportDto = new ReportDto();
		
		
		if(this.bean.getPreauthMedicalDecisionDetails() != null && this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null){
			String amtInwords = SHAUtils.getParsedAmount(this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt());	
			this.bean.getPreauthMedicalDecisionDetails().setAmountInwords(amtInwords);
			this.bean.setDiffAmount(this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt());
		}
		
		if(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			
			String amtInwords = SHAUtils.getParsedAmount(this.bean.getPreauthMedicalDecisionDetails().getAmountToHospAftPremium());	
			this.bean.getPreauthMedicalDecisionDetails().setAmountInwords(amtInwords);
			this.bean.setDiffAmount(this.bean.getPreauthMedicalDecisionDetails().getAmountToHospAftPremium());
			
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
		
		String prevApprovDetails = "";
		if(this.bean.getPreviousPreauthTableDTO() != null && !this.bean.getPreviousPreauthTableDTO().isEmpty()){
			// IMSSUPPOR-28105 --- IMSSUPPOR-28458 // commented for jira - IMSSUPPOR-28726
//			this.bean.getClaimDTO().setPreauthApprovedDate(this.bean.getPreviousPreauthTableDTO().get(0).getModifiedDate() != null ? this.bean.getPreviousPreauthTableDTO().get(0).getModifiedDate() : this.bean.getPreviousPreauthTableDTO().get(0).getCreatedDate());
			Double totalPrevApprovedAmt = 0d;
			for(PreviousPreAuthTableDTO prevPreauthDto : this.bean.getPreviousPreauthTableDTO()){
				if(prevPreauthDto.getApprovedAmt() != null && !("0.0").equalsIgnoreCase(prevPreauthDto.getApprovedAmt())) {
					
					totalPrevApprovedAmt = totalPrevApprovedAmt + Double.parseDouble(prevPreauthDto.getApprovedAmt());
				
					if(prevApprovDetails.equalsIgnoreCase("")) {
						if(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
							prevApprovDetails = (prevPreauthDto.getApprovedAmtAftPremiumDeduction() != null && prevPreauthDto.getCreatedDate() != null) ? "Rs."+ (int)Double.parseDouble(prevPreauthDto.getApprovedAmtAftPremiumDeduction()) + "/- on " + (new SimpleDateFormat("dd-MMM-yy").format(prevPreauthDto.getCreatedDate())).toUpperCase() : "";
						}
						else{
							prevApprovDetails = (prevPreauthDto.getApprovedAmt() != null && prevPreauthDto.getCreatedDate() != null) ? "Rs."+ (int)Double.parseDouble(prevPreauthDto.getApprovedAmt()) + "/- on " + (new SimpleDateFormat("dd-MMM-yy").format(prevPreauthDto.getCreatedDate())).toUpperCase() : "";		
						}
					}
					else{
						if(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
							prevApprovDetails = prevApprovDetails + ", and " +( (prevPreauthDto.getApprovedAmtAftPremiumDeduction() != null && prevPreauthDto.getCreatedDate() != null) ? "Rs."+ (int)Double.parseDouble(prevPreauthDto.getApprovedAmtAftPremiumDeduction()) + "/- on " + (new SimpleDateFormat("dd-MM-yy").format(prevPreauthDto.getCreatedDate())) : "");
						}
						else{
							prevApprovDetails = prevApprovDetails + ", and " +( (prevPreauthDto.getApprovedAmt() != null && prevPreauthDto.getCreatedDate() != null) ? "Rs."+ (int)Double.parseDouble(prevPreauthDto.getApprovedAmt()) + "/- on " + (new SimpleDateFormat("dd-MM-yy").format(prevPreauthDto.getCreatedDate())) : "");
						}
					}
				}
			}
			
			if(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				totalPrevApprovedAmt = bean.getPreauthDataExtractionDetails().getApprovedAmountAftDeduction();
			}
			prevApprovDetails = prevApprovDetails + (" in all Rs." + totalPrevApprovedAmt.longValue() + "/-.");
		
		}
		this.bean.setPrevPreAuthApprovDetails(prevApprovDetails);
		
		List<PreauthDTO> preauthDTOList = new ArrayList<PreauthDTO>();
		preauthDTOList.add(this.bean);		
		reportDto.setClaimId(bean.getClaimDTO().getClaimId());
		reportDto.setBeanList(preauthDTOList);
		
		String templateName = "PreauthDownSizeLetter";
		
		if(!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			templateName = "PreauthDownSizeLetter_Non_GMC";
		}
		
		final String filePath = docGen.generatePdfDocument(templateName, reportDto);     
		
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
		
		validateLetterChk = new CheckBox();

		validateLetterChk.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
            	
                if(event.getProperty().getValue() != null)
                   	bean.setLetterContentValidated((boolean)(event.getProperty().getValue()));
			}
		});
		
        Label chkLbl = new Label("<B>I confirm that the Contents of the Letter have been reviewed and are found to be in Order</B>",
                        ContentMode.HTML);
        HorizontalLayout validatechkLayout = new HorizontalLayout(validateLetterChk, chkLbl);
        
//		panel.setContent(e);
//		return panel;
		
		VerticalSplitPanel panel = new VerticalSplitPanel();
		panel.setSplitPosition(90.0f, Unit.PERCENTAGE);
		panel.setLocked(true);
		panel.setFirstComponent(e);
		panel.setSecondComponent(validatechkLayout);
		panel.setHeight("450px");

		return panel;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onAdvance() {
		
		if(bean.isLetterContentValidated())
			return true;
		else {
			
			MessageBox.createInfo()
	    	.withCaptionCust(SHAConstants.INFO_TITLE).withHtmlMessage(SHAConstants.VALIDATE_LETTER_CONTENT_MSG)
	        .withOkButton(ButtonOption.caption("OK")).open();
		}
		return false;

	}

	@Override
	public boolean onBack() {
		this.bean.setIsReverseAllocation(false);
		return true;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}
	

}
