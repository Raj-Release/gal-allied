package com.shaic.claim.enhancements.preauth.wizard;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.teemu.wizards.WizardStep;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
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
import com.vaadin.server.Sizeable.Unit;
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
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;


public class EnhancementDecisionCommunicationPage extends ViewComponent implements
WizardStep<PreauthDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PreauthDTO bean;
	
	@EJB
	private PreviousPreAuthService previousPreAuthService;

	//private Map<String, Object> referenceData;
	
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
		// A resource reference to some object
		
		DocumentGenerator docGen = new DocumentGenerator();
		ReportDto reportDto = new ReportDto();
		
		if(this.bean.getPreauthMedicalDecisionDetails() != null && this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null){
			Double initialTotalApprovedAmt = this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt();
			if(this.bean.getPreauthMedicalDecisionDetails().getEnhBenefitApprovedAmount() != null){
				initialTotalApprovedAmt += this.bean.getPreauthMedicalDecisionDetails().getEnhBenefitApprovedAmount();
			}
			String amtInwords = SHAUtils.getParsedAmount(initialTotalApprovedAmt);	
			this.bean.getPreauthMedicalDecisionDetails().setAmountInwords(amtInwords);
		}
		
		if(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			
			String amtInwords = SHAUtils.getParsedAmount(this.bean.getPreauthMedicalDecisionDetails().getAmountToHospAftPremium());	
			this.bean.getPreauthMedicalDecisionDetails().setAmountInwords(amtInwords);
			
		}
		if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
			String amtInwords = SHAUtils.getParsedAmount(this.bean.getPreauthMedicalDecisionDetails().getAmountToHospAftPremium());	
			this.bean.getPreauthMedicalDecisionDetails().setAmountInwords(amtInwords);
		}
//		if(this.bean.getCopay() != null){
//			this.bean.setCoPayValue(this.bean.getCopay().intValue());
//		}
		
		
		if(this.bean.getPreauthDataExtractionDetails() != null && this.bean.getPreauthDataExtractionDetails().getTotalApprAmt() != null){
			Double totalApprAmt = this.bean.getPreauthDataExtractionDetails().getTotalApprAmt();
			if(this.bean.getPreauthMedicalDecisionDetails().getEnhBenefitApprovedAmount() != null){
				totalApprAmt += this.bean.getPreauthMedicalDecisionDetails().getEnhBenefitApprovedAmount();
			}
			String amtInwords = SHAUtils.getParsedAmount(totalApprAmt);	
			this.bean.getPreauthDataExtractionDetails().setAmountInWords(amtInwords);
		}
		
		if(this.bean.getPreauthMedicalDecisionDetails().getInitialApprovedAmt() != null && this.bean.getPreauthDataExtractionDetails().getTotalApprAmt() != null){
			Double diffAmount =  this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() - this.bean.getPreauthDataExtractionDetails().getTotalApprAmt();
			this.bean.setDiffAmount(diffAmount);
		}
		
		this.bean.setFinalTotalApprovedAmount(this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt());
		
		if(this.bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS)){
			this.bean.setFinalTotalApprovedAmount(this.bean.getPreauthMedicalDecisionDetails().getDownsizedAmt());
			
		}
		
		if(this.bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS)){
			
			this.bean.setFinalTotalApprovedAmount(this.bean.getPreauthDataExtractionDetails().getTotalApprAmt());
			this.bean.setDiffAmount(0d);
			
			if(this.bean.getIsPreviousPreauthWithdraw()){
				//this.bean.setFinalTotalApprovedAmount(0d);
				this.bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(0d);
				this.bean.getPreauthDataExtractionDetails().setTotalApprAmt(0d);
			}
			
		}
		
		if(this.bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)){
			
			if(this.bean.getIsPreviousPreauthWithdraw() != null && this.bean.getIsPreviousPreauthWithdraw()){
				//this.bean.setFinalTotalApprovedAmount(0d);
				this.bean.getPreauthDataExtractionDetails().setTotalApprAmt(0d);
				this.bean.getPreauthDataExtractionDetails().setApprovedAmountAftDeduction(0d);
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
		Double totalPrevApprovedAmt = 0d;
		if(this.bean.getPreviousPreauthTableDTO() != null && !this.bean.getPreviousPreauthTableDTO().isEmpty()){
			// IMSSUPPOR-28105 --- IMSSUPPOR-28458
//			this.bean.getClaimDTO().setPreauthApprovedDate(this.bean.getPreviousPreauthTableDTO().get(0).getModifiedDate() != null ? this.bean.getPreviousPreauthTableDTO().get(0).getModifiedDate() : this.bean.getPreviousPreauthTableDTO().get(0).getCreatedDate());
			for(PreviousPreAuthTableDTO prevPreauthDto : this.bean.getPreviousPreauthTableDTO()){
				if(prevPreauthDto.getStatusKey() != null && (prevPreauthDto.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)
						||prevPreauthDto.getStatusKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)
								||prevPreauthDto.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)
								||prevPreauthDto.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS)
								||prevPreauthDto.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS))){
					prevPreauthDto.setApprovedAmt("0");
					
					//IMSSUPPOR-23349
					if(prevPreauthDto.getStatusKey() != null && prevPreauthDto.getStatusKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)){
						prevPreauthDto.setApprovedAmt(prevPreauthDto.getDiffAmount() != null ? prevPreauthDto.getDiffAmount() : "0");
					}
				}
				
				if(prevPreauthDto.getApprovedAmt() != null && !("0.0").equalsIgnoreCase(prevPreauthDto.getApprovedAmt())) {
					totalPrevApprovedAmt = totalPrevApprovedAmt + Double.parseDouble(prevPreauthDto.getApprovedAmt());
				
					if(prevApprovDetails.equalsIgnoreCase("")) {
						//added for CR2019184 policy instalment handling changes
						if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
							prevApprovDetails = (prevPreauthDto.getApprovedAmtAftPremiumDeduction() != null && prevPreauthDto.getCreatedDate() != null) ? "Rs."+ (int)Double.parseDouble(prevPreauthDto.getApprovedAmtAftPremiumDeduction()) + "/- on " + (new SimpleDateFormat("dd-MMM-yy").format(prevPreauthDto.getCreatedDate())).toUpperCase() : "";	
						}
						else if(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
							prevApprovDetails = (prevPreauthDto.getApprovedAmtAftPremiumDeduction() != null && prevPreauthDto.getCreatedDate() != null) ? "Rs."+ (int)Double.parseDouble(prevPreauthDto.getApprovedAmtAftPremiumDeduction()) + "/- on " + (new SimpleDateFormat("dd-MMM-yy").format(prevPreauthDto.getCreatedDate())).toUpperCase() : "";
						}
						else{
							prevApprovDetails = (prevPreauthDto.getApprovedAmt() != null && prevPreauthDto.getCreatedDate() != null) ? "Rs."+ (int)Double.parseDouble(prevPreauthDto.getApprovedAmt()) + "/- on " + (new SimpleDateFormat("dd-MMM-yy").format(prevPreauthDto.getCreatedDate())).toUpperCase() : "";		
						}
					}
					else{
						//added for CR2019184 policy instalment handling changes
						if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
							prevApprovDetails = (prevPreauthDto.getApprovedAmtAftPremiumDeduction() != null && prevPreauthDto.getCreatedDate() != null) ? "Rs."+ (int)Double.parseDouble(prevPreauthDto.getApprovedAmtAftPremiumDeduction()) + "/- on " + (new SimpleDateFormat("dd-MMM-yy").format(prevPreauthDto.getCreatedDate())).toUpperCase() : "";	
						}
						else if(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
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
			if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
				totalPrevApprovedAmt = bean.getPreauthDataExtractionDetails().getApprovedAmountAftDeduction();
			}
			
			if(bean.getPreauthDataExtractionDetails().getTotalOtherBenefitsApprovedAmt() != null){
				totalPrevApprovedAmt+=bean.getPreauthDataExtractionDetails().getTotalOtherBenefitsApprovedAmt();
			}	
			
			if(this.bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS)){
				if(this.bean.getIsPreviousPreauthWithdraw() != null && this.bean.getIsPreviousPreauthWithdraw()){
					totalPrevApprovedAmt = 0d;
					this.bean.setPreviousPreauthPayableAmount(0);
					this.bean.getPreauthDataExtractionDetails().setApprovedAmountAftDeduction(0d);
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
					/*if(deduction.getStatusKey() == 30 || deduction.getStatusKey() == 63){
						totalApprovedAmount = totalApprovedAmount + Double.valueOf(deduction.getDiffAmount()).intValue();		
					}else {
						totalApprovedAmount = totalApprovedAmount + Double.valueOf(deduction.getApprovedAmt()).intValue();		
					}*/
				}
				this.bean.setTotalApprovedAmount(totalApprovedAmount);
				String amtInwords = SHAUtils.getParsedAmount(totalApprovedAmount);	
				this.bean.setTotalApprovedAmountInWords(amtInwords);
		}
		
		
		int otherDeduction = 0;
		if(!this.bean.getPreauthDataExtractionDetails().getClaimedDetailsListForBenefitSheet().isEmpty()){
			 List<NoOfDaysCell> claimedDetailsListForBenefitSheet = this.bean.getPreauthDataExtractionDetails().getClaimedDetailsListForBenefitSheet();
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
			
			String templateName = "CashlessEnhancementApprovalLetter";
			
			/**
			 * As part of CR  R1145
			 */
			/*if(this.bean.getIsPreviousPreauthWithdraw() != null && this.bean.getIsPreviousPreauthWithdraw()){
//				TODO  Change included for this CR  R1145 - Mr. Srikanth sir has Confirmed that this change is applicable for Paayas policy also 
				templateName = bean.getNewIntimationDTO().getIsPaayasPolicy() ?  "PreauthInitialApprovalLetterPaayas" : (ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ? "PreauthInitialApprovalLetter" : "PreauthInitialApprovalLetter_Non_GMC");
			}			
			else if(!(ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
					&& (bean.getNewIntimationDTO().getIsPaayasPolicy() != null && !bean.getNewIntimationDTO().getIsPaayasPolicy())){
				 
				 templateName = "CashlessEnhancementApprovalLetter_Non_GMC";
			}*/			
			
			// End of Change for CR  R1145
			
			filePath = docGen.generatePdfDocument(templateName, reportDto);	
			bean.setDocType(SHAConstants.CASHLESS_ENHANCEMENT_APPROVAL_LETTER);
			bean.setDocSource(SHAConstants.ENHANCEMENT);
		}else if(this.bean.getStatusKey() == ReferenceTable.ENHANCEMENT_QUERY_STATUS){
			
//			filePath = docGen.generatePdfDocument("CashlessEnhancementQueryLetter", reportDto);
			
			String templateName = "ClsEnhancementQueryLetter_NON_GMC";
			
			if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				templateName = "CashlessEnhancementQueryLetter";
			}
			
			filePath = docGen.generatePdfDocument(templateName, reportDto);
			
			bean.setDocType(SHAConstants.CASHLESS_ENHANCEMENT_QUERY_LETTER);
			bean.setDocSource(SHAConstants.ENHANCEMENT);
		}else if(this.bean.getStatusKey() == ReferenceTable.ENHANCEMENT_REJECT_STATUS){
			
			 String templateName = "EnhancementRejectionLetter"; 
			 
			 if(!(ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
				 
				 templateName = "EnhancementRejectionLetter_Non_GMC";  // R1313  - New Template  03-01-2019
			 }
				
			if(this.bean.getIsPreviousPreauthWithdraw() != null && this.bean.getIsPreviousPreauthWithdraw() 
					&& this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null 
					&& this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().intValue() == 0){
					
				templateName = "PreauthRejectionLetter_NonGMC";  // R1177  - New Template
					
				if((ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){ // R1177 New Template 17-05-2018 
						templateName = "EnhancementRejectionLetter";   // R1177 New Template 17-05-2018 
				}//  R20181278    -  Preauth Rejection Letter For Star Cancer Care Gold Product
				else if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
						&& bean.getPreauthMedicalDecisionDetails().getRejectionCategory() != null
						&& ReferenceTable.STAR_CANCER_GOLD_REJ_CATG_KEY.equals(bean.getPreauthMedicalDecisionDetails().getRejectionCategory().getId())){
					templateName = "PreauthRejectionLetter_CancerCareGold";
				}												   // R1177 New Template 17-05-2018			
				
			}
			filePath = docGen.generatePdfDocument(templateName, reportDto);
						
			bean.setDocType(SHAConstants.ENHANCEMENT_REJECTION_LETTER);
			bean.setDocSource(SHAConstants.ENHANCEMENT);
		}else if((ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS).equals(this.bean.getStatusKey())){
			
			String templateName = "PreauthWithDrawLetter_NonGMC";  // R1177 New Template 17-05-2018 
			
			if(ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){  // R1177 17-05-2018
				templateName = "EnhancementWithDrawLetter";   // R1177 17-05-2018
			}                                                 // R1177 17-05-2018
			else{
				templateName = "PreauthWithDrawLetter_NonGMC";   // R1313 03-01-2019
			}
			
			filePath = docGen.generatePdfDocument(templateName, reportDto);
			
			bean.setDocType(SHAConstants.ENHANCEMENT_WITHDRAW_LETTER);
			bean.setDocSource(SHAConstants.ENHANCEMENT);
		} else if((ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT).equals(this.bean.getStatusKey())){			
			
//			String templateName = "PreauthWithDrawAndRejectionLetter_NonGMC";  // R1177  New Template  17-05-2018
			
			String templateName = "CashlessRejectAndWithDrawLetter_NonGMC";   // CR R20181313			
						
			if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){ // R1177 17-05-2018
				templateName = "EnhancementWithDrawAndRejectionLetter";                                                // R1177 17-05-2018
			}                                                                                                          // R1177 17-05-2018
			else{
				// templateName = "PreauthWithDrawAndRejectionLetter_NonGMC";  										   // R1177 03-01-2019
				
				templateName = "CashlessRejectAndWithDrawLetter_NonGMC";       // CR R20181313
			}
			 
			filePath = docGen.generatePdfDocument(templateName, reportDto);
			bean.setDocType(SHAConstants.ENHANCEMENT_WITHDRAW_AND_REJECTION_LETTER);
			bean.setDocSource(SHAConstants.ENHANCEMENT);
		} else if(this.bean.getStatusKey() == ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS){
			
			Double totalApprAmt = this.bean.getPreauthMedicalDecisionDetails().getDownsizedAmt();
			if(this.bean.getPreauthMedicalDecisionDetails().getEnhBenefitApprovedAmount() != null){
				totalApprAmt += this.bean.getPreauthMedicalDecisionDetails().getEnhBenefitApprovedAmount();
			}
			
			this.bean.getPreauthMedicalDecisionDetails().setAmountInwords(SHAUtils.getParsedAmount(totalApprAmt));
			if(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
				
				String amtInwords = SHAUtils.getParsedAmount(this.bean.getPreauthMedicalDecisionDetails().getAmountToHospAftPremium());	
				this.bean.getPreauthMedicalDecisionDetails().setAmountInwords(amtInwords);
				
			}
			if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
				String amtInwords = SHAUtils.getParsedAmount(this.bean.getPreauthMedicalDecisionDetails().getAmountToHospAftPremium());	
				this.bean.getPreauthMedicalDecisionDetails().setAmountInwords(amtInwords);
			}
			
			this.bean.setPrevPreAuthApprovDetails(prevApprovDetails);
			preauthDTOList.clear();
			preauthDTOList.add(this.bean);
			reportDto.setBeanList(preauthDTOList);
			
			String templateName = "PreauthDownSizeLetter";  // R1313  OLD Template
			
			if(!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){ // R1313 03-01-2019
				
				templateName = "PreauthDownSizeLetter_Non_GMC";			// R1313 03-01-2019
			}
			
			filePath = docGen.generatePdfDocument(templateName, reportDto);
			bean.setDocType(SHAConstants.ENHANCEMENT_DOWNSIZE_LETTER);
			bean.setDocSource(SHAConstants.ENHANCEMENT);
		}
		
//		VerticalLayout firstVertical = getStreamResourceLayout(filePath);
//		HorizontalLayout horizontalLayout = new HorizontalLayout(firstVertical);
		
//		horizontalLayout.setWidth("100%");
//		horizontalLayout.setHeight("100%");
		
//		Panel panel = new Panel(horizontalLayout);
		
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
        
		VerticalSplitPanel panel = new VerticalSplitPanel();
		
		panel.setSplitPosition(90.0f, Unit.PERCENTAGE);
		panel.setLocked(true);
		panel.setFirstComponent(getStreamResourceLayout(filePath));
		panel.setSecondComponent(validatechkLayout);
		panel.setHeight("450px");
		
		return panel;
	}

	private Embedded getStreamResourceLayout(String filePath) {
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
		e.setHeight("100%");
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		SHAUtils.closeStreamResource(s);
		
		/*VerticalLayout firstVertical = new VerticalLayout(e);
		firstVertical.setHeight("600px");
		return firstVertical;*/
		
		return e;
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
		firstVertical.setHeight("100%");
		return firstVertical;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		//this.referenceData = referenceData;

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
		return true;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}

}
