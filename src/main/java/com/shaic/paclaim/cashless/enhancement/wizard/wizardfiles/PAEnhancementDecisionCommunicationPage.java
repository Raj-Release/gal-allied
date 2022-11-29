package com.shaic.paclaim.cashless.enhancement.wizard.wizardfiles;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
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
import com.vaadin.v7.ui.VerticalLayout;

public class PAEnhancementDecisionCommunicationPage extends ViewComponent implements
WizardStep<PreauthDTO> {
	
	private static final long serialVersionUID = -2767295799754968051L;

	PreauthDTO bean;

	@EJB
	private PreviousPreAuthService previousPreAuthService;
	
	private Map<String, Object> referenceData;
	
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
		// A resource reference to some object
		
		DocumentGenerator docGen = new DocumentGenerator();
		ReportDto reportDto = new ReportDto();
		
		if(this.bean.getPreauthMedicalDecisionDetails() != null && this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null){
			String amtInwords = SHAUtils.getParsedAmount(this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt());	
			this.bean.getPreauthMedicalDecisionDetails().setAmountInwords(amtInwords);
		}
		
//		if(this.bean.getCopay() != null){
//			this.bean.setCoPayValue(this.bean.getCopay().intValue());
//		}
		
		
		if(this.bean.getPreauthDataExtractionDetails() != null && this.bean.getPreauthDataExtractionDetails().getTotalApprAmt() != null){
			String amtInwords = SHAUtils.getParsedAmount(this.bean.getPreauthDataExtractionDetails().getTotalApprAmt());	
			this.bean.getPreauthDataExtractionDetails().setAmountInWords(amtInwords);
		}
		
		if(this.bean.getPreauthMedicalDecisionDetails().getInitialApprovedAmt() != null && this.bean.getPreauthDataExtractionDetails().getTotalApprAmt() != null){
			Double diffAmount =  this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() - this.bean.getPreauthDataExtractionDetails().getTotalApprAmt();
			this.bean.setDiffAmount(diffAmount);
		}
		
		this.bean.setFinalTotalApprovedAmount(this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt());
		
		if(this.bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS)){
			
			this.bean.setFinalTotalApprovedAmount(this.bean.getPreauthDataExtractionDetails().getTotalApprAmt());
			this.bean.setDiffAmount(0d);
			
			if(this.bean.getIsPreviousPreauthWithdraw()){
				this.bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(0d);
				this.bean.getPreauthDataExtractionDetails().setTotalApprAmt(0d);
			}
		}
		
		if(this.bean.getPreauthDataExtractionDetails() != null && this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList() != null){
			
			List<DiagnosisDetailsTableDTO> diagnosisList = new ArrayList<DiagnosisDetailsTableDTO>();
			diagnosisList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
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
				diagnosis = StringUtils.removeEndIgnoreCase(diagnosis, "/");
			this.bean.getPreauthDataExtractionDetails().setDiagnosis(diagnosis);
			}
		}
		String prevApprovDetails = "";
		if(this.bean.getPreviousPreauthTableDTO() != null && !this.bean.getPreviousPreauthTableDTO().isEmpty()){
			this.bean.setCreateDate(this.bean.getPreviousPreauthTableDTO().get(0).getCreatedDate());
			Integer totalPrevApprovedAmt = 0;
			for(PreviousPreAuthTableDTO prevPreauthDto : this.bean.getPreviousPreauthTableDTO()){
				if(prevPreauthDto.getApprovedAmt() != null && !("0.0").equalsIgnoreCase(prevPreauthDto.getApprovedAmt())) {
					totalPrevApprovedAmt = totalPrevApprovedAmt + (int)Double.parseDouble(prevPreauthDto.getApprovedAmt());
				
					if(prevApprovDetails.equalsIgnoreCase("")) {
						prevApprovDetails = (prevPreauthDto.getApprovedAmt() != null && prevPreauthDto.getCreatedDate() != null) ? "Rs."+ (int)Double.parseDouble(prevPreauthDto.getApprovedAmt()) + "/- on " + (new SimpleDateFormat("dd-MMM-yy").format(prevPreauthDto.getCreatedDate())).toUpperCase() : "";
					}
					else{
						prevApprovDetails = prevApprovDetails + ", and " +( (prevPreauthDto.getApprovedAmt() != null && prevPreauthDto.getCreatedDate() != null) ? "Rs."+ (int)Double.parseDouble(prevPreauthDto.getApprovedAmt()) + "/- on " + (new SimpleDateFormat("dd-MM-yy").format(prevPreauthDto.getCreatedDate())) : "");
					}
				}
			}
			prevApprovDetails = prevApprovDetails + (" in all Rs." + totalPrevApprovedAmt + "/-.");
		
		}
		List<PreviousPreAuthTableDTO> searchPrevPreAuthReport = previousPreAuthService.searchPrevPreAuthReport(this.bean.getIntimationKey());
		this.bean.setPreviousPreauthTableDTOReportList(searchPrevPreAuthReport);
		
		this.bean.setStrUserName(this.bean.getStrUserName() != null ? this.bean.getStrUserName().toUpperCase() : "" );
		Double approvedAmount =0d;
		List<PreviousPreAuthTableDTO> reportLit = new ArrayList<PreviousPreAuthTableDTO>();
		if(!this.bean.getPreviousPreauthTableDTOReportList().isEmpty()){
		/*	PreviousPreAuthTableDTO previousPreAuthTableDTOReport = new PreviousPreAuthTableDTO();			
			previousPreAuthTableDTOReport.setModifiedDate(new Date());
			if(this.bean.getPreauthMedicalDecisionDetails() != null && this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null){
				approvedAmount = this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt();
				if(this.bean.getPreauthMedicalDecisionDetails().getEnhBenefitApprovedAmount() != null){
					approvedAmount += this.bean.getPreauthMedicalDecisionDetails().getEnhBenefitApprovedAmount();
				}
			}
			if(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
				approvedAmount = this.bean.getPreauthMedicalDecisionDetails().getAmountToHospAftPremium();	
			}
			previousPreAuthTableDTOReport.setApprovedAmt(String.valueOf(approvedAmount));
			previousPreAuthTableDTOReport.setApprovedAmt(String.valueOf(this.bean.getDiffAmount()));
			previousPreAuthTableDTOReport.setStatus("Approved (Enhancement)");
			int size = this.bean.getPreviousPreauthTableDTOReportList().size();
			PreviousPreAuthTableDTO previousPreAuthTableDTO2 = this.bean.getPreviousPreauthTableDTOReportList().get(size-1);
			char charAt = previousPreAuthTableDTO2.getReferenceNo().charAt(previousPreAuthTableDTO2.getReferenceNo().length() - 1);
			int refNo = Integer.valueOf(String.valueOf(previousPreAuthTableDTO2.getReferenceNo().charAt(previousPreAuthTableDTO2.getReferenceNo().length() - 1)));
			previousPreAuthTableDTOReport.getReferenceNo().replace(charAt,(char)(refNo+1));
			previousPreAuthTableDTOReport.setReferenceNo(String.valueOf(refNo+1));
			reportLit.add(previousPreAuthTableDTOReport);
			this.bean.setPreviousPreauthTableDTOReportList(reportLit);*/
			
			int totalApprovedAmount = 0;
			 List<PreviousPreAuthTableDTO> previousPreAuthTableDTO = this.bean.getPreviousPreauthTableDTOReportList();
				for(PreviousPreAuthTableDTO deduction :previousPreAuthTableDTO){
					if(deduction.getStatusKey() == 21 || deduction.getStatusKey() == 181 || deduction.getStatusKey() == 33||
							deduction.getStatusKey() == 20 || deduction.getStatusKey() == 66 || deduction.getStatusKey() ==21){
						deduction.setApprovedAmt(this.bean.getDiffAmount() != null ? String.valueOf(this.bean.getDiffAmount()) : "0");
					}
					totalApprovedAmount = totalApprovedAmount + Double.valueOf(deduction.getApprovedAmt()).intValue();		
				}
				this.bean.setTotalApprovedAmount(totalApprovedAmount);
				String amtInwords = SHAUtils.getParsedAmount(totalApprovedAmount);	
				this.bean.setTotalApprovedAmountInWords(amtInwords);
		}
		
		
		int otherDeduction = 0;
		if(!this.bean.getPreauthDataExtractionDetails().getClaimedDetailsList().isEmpty()){
			 List<NoOfDaysCell> claimedDetailsListForBenefitSheet = this.bean.getPreauthDataExtractionDetails().getClaimedDetailsList();
			for(NoOfDaysCell deduction :claimedDetailsListForBenefitSheet){
				otherDeduction = otherDeduction + deduction.getNonPayableAmount() + deduction.getDeductibleAmount();		
			}
			this.bean.setOtherDeductionAmount(otherDeduction);
		}
		
		this.bean.setStrUserName(this.bean.getStrUserName() != null ? this.bean.getStrUserName().toUpperCase() : "" );
		List<PreauthDTO> preauthDTOList = new ArrayList<PreauthDTO>();
		preauthDTOList.add(this.bean);		
		reportDto.setClaimId(this.bean.getClaimDTO().getClaimId());
		reportDto.setBeanList(preauthDTOList);		
		String filePath = "";
		
		if(this.bean.getStatusKey() == ReferenceTable.ENHANCEMENT_APPROVE_STATUS){			
			filePath = docGen.generatePdfDocument("CashlessEnhancementApprovalLetter", reportDto);	
			bean.setDocType(SHAConstants.CASHLESS_ENHANCEMENT_APPROVAL_LETTER);
			bean.setDocSource(SHAConstants.ENHANCEMENT);
		}else if(this.bean.getStatusKey() == ReferenceTable.ENHANCEMENT_QUERY_STATUS){
			filePath = docGen.generatePdfDocument("CashlessEnhancementQueryLetter", reportDto);
			bean.setDocType(SHAConstants.CASHLESS_ENHANCEMENT_QUERY_LETTER);
			bean.setDocSource(SHAConstants.ENHANCEMENT);
		}else if(this.bean.getStatusKey() == ReferenceTable.ENHANCEMENT_REJECT_STATUS){
			
			String templateName = "EnhancementRejectionLetter";
			
			if(this.bean.getIsPreviousPreauthWithdraw() != null && this.bean.getIsPreviousPreauthWithdraw()
					&& this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null 
					&& this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().intValue() == 0){
				templateName = "PreauthRejectionLetter";
			}
			filePath = docGen.generatePdfDocument(templateName, reportDto);
			
//			filePath = docGen.generatePdfDocument("EnhancementRejectionLetter", reportDto);
			bean.setDocType(SHAConstants.ENHANCEMENT_REJECTION_LETTER);
			bean.setDocSource(SHAConstants.ENHANCEMENT);
		}else if(this.bean.getStatusKey() == ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS){
			filePath = docGen.generatePdfDocument("EnhancementWithDrawLetter", reportDto);
			bean.setDocType(SHAConstants.ENHANCEMENT_WITHDRAW_LETTER);
			bean.setDocSource(SHAConstants.ENHANCEMENT);
		} else if(this.bean.getStatusKey() == ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT){
			filePath = docGen.generatePdfDocument("EnhancementWithDrawAndRejectionLetter", reportDto);
			bean.setDocType(SHAConstants.ENHANCEMENT_WITHDRAW_AND_REJECTION_LETTER);
			bean.setDocSource(SHAConstants.ENHANCEMENT);
		} else if(this.bean.getStatusKey() == ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS){
			this.bean.getPreauthMedicalDecisionDetails().setAmountInwords(SHAUtils.getParsedAmount(this.bean.getPreauthMedicalDecisionDetails().getDownsizedAmt()));
			this.bean.setPrevPreAuthApprovDetails(prevApprovDetails);
			preauthDTOList.clear();
			preauthDTOList.add(this.bean);
			reportDto.setBeanList(preauthDTOList);
			filePath = docGen.generatePdfDocument("PreauthDownSizeLetter", reportDto);
			bean.setDocType(SHAConstants.ENHANCEMENT_DOWNSIZE_LETTER);
			bean.setDocSource(SHAConstants.ENHANCEMENT);
		}
		
		VerticalLayout firstVertical = getStreamResourceLayout(filePath);
		HorizontalLayout horizontalLayout = new HorizontalLayout(firstVertical);
		
//		if(bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS))
//		{
//		   filePath = docGen.generatePdfDocument("BillAssessmentCashlessReport", reportDto);
//		   bean.setBillAssessmentDocType(SHAConstants.CASHLESS_ENHANCEMENT_BILL_ASSESSMENT);
//		   bean.setBillAssessmentDocSource(SHAConstants.ENHANCEMENT);
//		   bean.setBillAssessmentDocFilePath(filePath);
//		   VerticalLayout secondVertical = getStreamResourceLayoutForBillAssessment(filePath);   // need to implement
//		   horizontalLayout.addComponent(secondVertical);
//		}

		horizontalLayout.setWidth("100%");
		horizontalLayout.setHeight("100%");
		Panel panel = new Panel(horizontalLayout);
//		panel.setHeight("px");

		return panel;
	}

	private VerticalLayout getStreamResourceLayout(String filePath) {
		final String finalFilePath = filePath;
		this.bean.setDocFilePath(finalFilePath);
		this.bean.setDocSource(SHAConstants.PRE_AUTH_ENHANCEMENT);
		final Path p = Paths.get(finalFilePath);
		String fileName = p.getFileName().toString();
		StreamResource.StreamSource s = SHAUtils.getStreamResource(finalFilePath);

		/*StreamResource.StreamSource s = new StreamResource.StreamSource() {

			public FileInputStream getStream() {
				try {

					File f = p.toFile();    // new File(finalFilePath);
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
		VerticalLayout firstVertical = new VerticalLayout(e);
		firstVertical.setHeight("600px");
		return firstVertical;
	}
	
	
	private VerticalLayout getStreamResourceLayoutForBillAssessment(String filePath) {
		final String finalFilePath = filePath;
		this.bean.setDocSource(SHAConstants.PRE_AUTH_ENHANCEMENT);
		final Path p = Paths.get(finalFilePath);
		String fileName = p.getFileName().toString();
		StreamResource.StreamSource s = SHAUtils.getStreamResource(finalFilePath);

		/*StreamResource.StreamSource s = new StreamResource.StreamSource() {

			public FileInputStream getStream() {
				try {

					File f = p.toFile();    // new File(finalFilePath);
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
		VerticalLayout firstVertical = new VerticalLayout(e);
		firstVertical.setHeight("600px");
		return firstVertical;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;

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
