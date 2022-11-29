package com.shaic.paclaim.cashless.preauth.wizard.pages;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ReportDto;
import com.shaic.claim.enhacement.table.PreviousPreAuthService;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.domain.ReferenceTable;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

public class PAPreauthDecisionCommunicationPage extends ViewComponent implements
		WizardStep<PreauthDTO> {

	
	private static final long serialVersionUID = 2957181595610576628L;

	private GWizard wizard;

	PreauthDTO bean;
	
	@EJB
	private PreviousPreAuthService previousPreAuthService;

	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;
	}
	
	public void init(PreauthDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
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
		}
		
		
		if(this.bean.getPreauthDataExtractionDetails() != null && this.bean.getPreauthDataExtractionDetails().getTotalApprAmt() != null){
			String amtInwords = SHAUtils.getParsedAmount(this.bean.getPreauthDataExtractionDetails().getTotalApprAmt());	
			this.bean.getPreauthDataExtractionDetails().setAmountInWords(amtInwords);
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
		if(!this.bean.getPreauthDataExtractionDetails().getClaimedDetailsList().isEmpty()){
			 List<NoOfDaysCell> claimedDetailsListForBenefitSheet = this.bean.getPreauthDataExtractionDetails().getClaimedDetailsList();
			for(NoOfDaysCell deduction :claimedDetailsListForBenefitSheet){
				otherDeduction = otherDeduction + deduction.getNonPayableAmount() + deduction.getDeductibleAmount();		
			}
			this.bean.setOtherDeductionAmount(otherDeduction);
		}
		List<PreauthDTO> preauthDTOList = new ArrayList<PreauthDTO>();
		preauthDTOList.add(this.bean);		
		reportDto.setClaimId(this.bean.getClaimDTO().getClaimId());
		reportDto.setBeanList(preauthDTOList);		
		String filePath = "";
		
		if(this.bean.getStatusKey() == ReferenceTable.PREAUTH_APPROVE_STATUS){			
			filePath = docGen.generatePdfDocument("PreauthInitialApprovalLetter", reportDto);
			bean.setDocType(SHAConstants.PREAUTH_INTIAL_APPROVAL_LETTER);
		}else if(this.bean.getStatusKey() == ReferenceTable.PREAUTH_QUERY_STATUS){
			filePath = docGen.generatePdfDocument("PreauthQueryLetter", reportDto);
			bean.setDocType(SHAConstants.PREAUTH_QUERY_LETTER);
		}else if(this.bean.getStatusKey() == ReferenceTable.PREAUTH_REJECT_STATUS){
			filePath = docGen.generatePdfDocument("PreauthRejectionLetter", reportDto);
			bean.setDocType(SHAConstants.PREAUTH_REJECTION_LETTER);
		}else if(this.bean.getStatusKey() == ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS){
			filePath = docGen.generatePdfDocument("PreauthDenialLetter", reportDto);
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
		};
*/
		StreamResource r = new StreamResource(s, fileName);
		Embedded e = new Embedded();
		e.setSizeFull();
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		SHAUtils.closeStreamResource(s);
		HorizontalLayout horizontalLayout = new HorizontalLayout(e);
		horizontalLayout.setWidth("100%");
		horizontalLayout.setHeight("500px");
		Panel panel = new Panel(horizontalLayout);
		 panel.setHeight("450px");

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
