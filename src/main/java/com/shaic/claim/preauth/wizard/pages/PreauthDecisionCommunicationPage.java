package com.shaic.claim.preauth.wizard.pages;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import oracle.jdbc.proxy.annotation.GetCreator;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ReportDto;
import com.shaic.claim.enhacement.table.PreviousPreAuthService;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.domain.ReferenceTable;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Panel;

public class PreauthDecisionCommunicationPage extends ViewComponent implements
		WizardStep<PreauthDTO> {

	private static final long serialVersionUID = 7618109414654462771L;
	
//	private GWizard wizard;

	PreauthDTO bean;
	
	private CheckBox validateLetterChk;
	
	@EJB
	private PreviousPreAuthService previousPreAuthService;

	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;
	}
	
	public void init(PreauthDTO bean, GWizard wizard) {
		this.bean = bean;
//		this.wizard = wizard;
	}

	@Override
	public String getCaption() {
		return "Decision Communication";
	}

	@Override
	public Component getContent() {
		// A resource reference to some object
		
		DocumentGenerator docGen = new DocumentGenerator();
		ReportDto reportDto = new ReportDto();
		
		if(this.bean.getPreauthMedicalDecisionDetails() != null && this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null){
			String amtInwords = SHAUtils.getParsedAmount(this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt());	
			this.bean.getPreauthMedicalDecisionDetails().setAmountInwords(amtInwords);
			if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY)){
				amtInwords = SHAUtils.getParsedAmount(this.bean.getPreauthMedicalDecisionDetails().getAmountToHospAftPremium());	
				this.bean.getPreauthMedicalDecisionDetails().setAmountInwords(amtInwords);
			}
			if (bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
				amtInwords = SHAUtils.getParsedAmount(this.bean.getPreauthMedicalDecisionDetails().getAmountToHospAftPremium());	
				this.bean.getPreauthMedicalDecisionDetails().setAmountInwords(amtInwords);
			}
		}
		
		
		if(this.bean.getPreauthDataExtractionDetails() != null && this.bean.getPreauthDataExtractionDetails().getTotalApprAmt() != null){
			String amtInwords = SHAUtils.getParsedAmount(this.bean.getPreauthDataExtractionDetails().getTotalApprAmt());	
			this.bean.getPreauthDataExtractionDetails().setAmountInWords(amtInwords);
		}
		
		if(this.bean.getPreauthDataExtractionDetails().getOtherBenfitOpt() != null && this.bean.getPreauthDataExtractionDetails().getOtherBenfitOpt() && this.bean.getPreauthDataExtractionDetails().getTotalOtherBenefitsApprovedAmt() != null){
			String amtInwords = SHAUtils.getParsedAmount(this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() + this.bean.getPreauthDataExtractionDetails().getTotalOtherBenefitsApprovedAmt());	
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
				diagnosis += " / " + ( diagnosisDto.getDiagnosisName() != null ? ( diagnosisDto.getDiagnosisName().getValue() != null ? diagnosisDto.getDiagnosisName().getValue() : "" ) : " / " ) ;
				}
			}
			}
			if(!diagnosis.equals("")){
				diagnosis = StringUtils.removeEnd(diagnosis, "/");
				diagnosis = StringUtils.replace(diagnosis,"&","-");
				
			this.bean.getPreauthDataExtractionDetails().setDiagnosis(diagnosis);
			}
		}
		if(this.bean.getNewIntimationDTO().getHospitalDto() != null && this.bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals() != null)
		{
			if(this.bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getName() != null){
				String hospitalName = this.bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getName();
				hospitalName = StringUtils.replace(hospitalName,"&","-");
				this.bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().setName(hospitalName);
			}
		}
		
		this.bean.setStrUserName(this.bean.getStrUserName() != null ? this.bean.getStrUserName().toUpperCase() : "" );
		
		List<PreviousPreAuthTableDTO> searchPrevPreAuthReport = previousPreAuthService.searchPrevPreAuthReport(this.bean.getIntimationKey());
		this.bean.setPreviousPreauthTableDTOReportList(searchPrevPreAuthReport);
		
		Double approvedAmount =0d;
		List<PreviousPreAuthTableDTO> reportLit = new ArrayList<PreviousPreAuthTableDTO>();
		if(this.bean.getPreviousPreauthTableDTOReportList().isEmpty()){
			PreviousPreAuthTableDTO previousPreAuthTableDTOReport = new PreviousPreAuthTableDTO();			
			previousPreAuthTableDTOReport.setModifiedDate(new Date());
			if(this.bean.getPreauthMedicalDecisionDetails() != null && this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null){
				approvedAmount = this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt();	
				if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY)){
					approvedAmount = this.bean.getPreauthMedicalDecisionDetails().getAmountToHospAftPremium();	
				}
				if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
					approvedAmount = this.bean.getPreauthMedicalDecisionDetails().getAmountToHospAftPremium();		
				}
			}
			
			if(this.bean.getPreauthDataExtractionDetails().getOtherBenfitOpt() != null && this.bean.getPreauthDataExtractionDetails().getOtherBenfitOpt() && this.bean.getPreauthDataExtractionDetails().getTotalOtherBenefitsApprovedAmt() != null){
				approvedAmount = this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() + this.bean.getPreauthDataExtractionDetails().getTotalOtherBenefitsApprovedAmt();	
			}
			previousPreAuthTableDTOReport.setApprovedAmt(String.valueOf(approvedAmount));
			previousPreAuthTableDTOReport.setStatus("Approved (Pre Auth)");
			previousPreAuthTableDTOReport.setReferenceNo(this.bean.getClaimNumber() + "/001");
			reportLit.add(previousPreAuthTableDTOReport);
			this.bean.setPreviousPreauthTableDTOReportList(reportLit);
			
		}
		
		int totalApprovedAmount = 0;
		 List<PreviousPreAuthTableDTO> previousPreAuthTableDTO = this.bean.getPreviousPreauthTableDTOReportList();
			for(PreviousPreAuthTableDTO deduction :previousPreAuthTableDTO){
				double amount = Double.valueOf(deduction.getApprovedAmt());
				totalApprovedAmount = totalApprovedAmount + Double.valueOf(deduction.getApprovedAmt()).intValue();		
			}
			this.bean.setTotalApprovedAmount(totalApprovedAmount);
			String amtInwords = SHAUtils.getParsedAmount(totalApprovedAmount);	
			this.bean.setTotalApprovedAmountInWords(amtInwords);
		
		int otherDeduction = 0;
		if(!this.bean.getPreauthDataExtractionDetails().getClaimedDetailsListForBenefitSheet().isEmpty()){
			 List<NoOfDaysCell> claimedDetailsListForBenefitSheet = this.bean.getPreauthDataExtractionDetails().getClaimedDetailsListForBenefitSheet();
			for(NoOfDaysCell deduction :claimedDetailsListForBenefitSheet){
				otherDeduction = otherDeduction + deduction.getNonPayableAmount() + deduction.getDeductibleAmount();		
			}
			this.bean.setOtherDeductionAmount(otherDeduction);
		}
		
		/*if(ReferenceTable.PREAUTH_REJECT_STATUS.equals(this.bean.getStatusKey())
			&& ReferenceTable.CARDIAC_RELATED_90_DAYS_WAITING_PERIOD.equals(this.bean.getPreauthMedicalDecisionDetails().getRejectionCategory().getId())) {
			
			if(this.bean.getPreauthMedicalDecisionDetails().getRejectionRemarks() != null 
					&& !this.bean.getPreauthMedicalDecisionDetails().getRejectionRemarks().isEmpty()
					&& (".").equalsIgnoreCase(this.bean.getPreauthMedicalDecisionDetails().getRejectionRemarks())) {
				this.bean.getPreauthMedicalDecisionDetails().setRejectionRemarks("");
			}
		}*/	
		
		List<PreauthDTO> preauthDTOList = new ArrayList<PreauthDTO>();
		preauthDTOList.add(this.bean);		
		reportDto.setClaimId(this.bean.getClaimDTO().getClaimId());
		reportDto.setBeanList(preauthDTOList);		
		String filePath = "";
		
		if(ReferenceTable.PREAUTH_APPROVE_STATUS.equals(this.bean.getStatusKey())){
			
			String templateName = "PreauthInitialApprovalLetter";
						
			/*if(bean.getNewIntimationDTO().getIsPaayasPolicy()){
				templateName = "PreauthInitialApprovalLetterPaayas";
			}
			else if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
				templateName = "PreauthInitialApprovalLetter";
			}*/
			
			filePath = docGen.generatePdfDocument(templateName, reportDto);
			bean.setDocType(SHAConstants.PREAUTH_INTIAL_APPROVAL_LETTER);
		}else if(ReferenceTable.PREAUTH_QUERY_STATUS.equals(this.bean.getStatusKey())){

//			filePath = docGen.generatePdfDocument("PreauthQueryLetter", reportDto);
			
			String templateName = "PreauthQueryLetter_NON_GMC";
			if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				templateName = "PreauthQueryLetter";
			}
			filePath = docGen.generatePdfDocument(templateName, reportDto);
			
			bean.setDocType(SHAConstants.PREAUTH_QUERY_LETTER);
		}else if(ReferenceTable.PREAUTH_REJECT_STATUS.equals(this.bean.getStatusKey())){
			
			String templateName = "PreauthRejectionLetter_NonGMC";   // R1177 New Template 17-05-2018 
			
			if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){ // R1177 17-05-2018
				templateName = "PreauthRejectionLetter";     // R1177 17-05-2018
			}//  R20181278    -  Preauth Rejection Letter For Star Cancer Care Gold Product
			else if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
					&& bean.getPreauthMedicalDecisionDetails().getRejectionCategory() != null
					&& ReferenceTable.STAR_CANCER_GOLD_REJ_CATG_KEY.equals(bean.getPreauthMedicalDecisionDetails().getRejectionCategory().getId())){
				templateName = "PreauthRejectionLetter_CancerCareGold";
			}                                                // R1177 17-05-2018

			filePath = docGen.generatePdfDocument(templateName, reportDto);
			bean.setDocType(SHAConstants.PREAUTH_REJECTION_LETTER);
		}else if((ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS).equals(this.bean.getStatusKey())){
			
			String templateName = "PreauthDenialLetter_NonGMC";   // R1177 New Template 17-05-2018
			
			
			if(bean.getPreauthMedicalDecisionDetails().getUserClickAction().equals("clsNotReq") 
					&& !ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				this.bean.getPreauthMedicalDecisionDetails().setDenialRemarks(this.bean.getPreauthMedicalDecisionDetails().getRemarksForInsured());
				
				templateName = "CashlessNotRequiredLetter_NonGMC";     // R20149065
			}
			else if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){ // R1177 17-05-2018
				templateName = "PreauthDenialLetter";     // R1177 17-05-2018
			}                                             // R1177 17-05-2018
			
			filePath = docGen.generatePdfDocument(templateName, reportDto);
			bean.setDocType(SHAConstants.PREAUTH_DENIAL_LETTER);
		}
		
		final String finalFilePath = filePath;
		this.bean.setDocFilePath(finalFilePath);
		this.bean.setDocSource(SHAConstants.PRE_AUTH);
		Path p = Paths.get(finalFilePath);
		String fileName = p.getFileName().toString();
		StreamResource.StreamSource s = SHAUtils.getStreamResource(finalFilePath);

		/*StreamResource.StreamSource s = new StreamResource.StreamSource() {

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
		};*/

		StreamResource r = new StreamResource(s, fileName);
		Embedded e = new Embedded();
		e.setSizeFull();
		e.setHeight("100%");
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
        
        
		/*VerticalLayout vLayout = new VerticalLayout();
		vLayout.addComponents(e);				
		vLayout.setWidth("100%");
		vLayout.setHeight("100%");
		Panel panel = new Panel(vLayout);*/
        
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
		bean.getPreauthMedicalDecisionDetails().setNegotiationAmount(null);
		bean.getPreauthMedicalDecisionDetails().setNegotiationMade(false);
		fireViewEvent(PreauthWizardPresenter.PREAUTH_DYNAMICFRMLAYOUT_REMOVE_COMP, null);
		return true;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}

}
