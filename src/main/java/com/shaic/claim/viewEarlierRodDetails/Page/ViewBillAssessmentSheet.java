package com.shaic.claim.viewEarlierRodDetails.Page;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ReportDto;
import com.shaic.claim.common.RevisedMedicalDecisionTable;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.dto.ResidualAmountDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.premedical.dto.OtherBenefitsTableDto;
import com.shaic.claim.reimbursement.financialapproval.pages.communicationPage.NonPayableReasonDto;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.viewEarlierRodDetails.dto.PreHospitalizationDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Panel;

public class ViewBillAssessmentSheet extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PreauthDTO bean;
	
	
	@EJB
	private CreateRODService createRodService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private MasterService masterService;
	
	@Inject
	private ViewBillAssessmentUI billAssessment;
	
	@Inject
	private Instance<RevisedMedicalDecisionTable> medicalDecisionTable;
	
	private RevisedMedicalDecisionTable medicalDecisionTableObj;
	
	
	public void init(PreauthDTO bean, Long reimbursmentKey){
		
		if(reimbursmentKey != null){
//			dbCalculationService.callBillAssessmentSheet(reimbursmentKey);
		}
		
		this.bean = new PreauthDTO();
		this.bean = bean;
		
		
		this.medicalDecisionTableObj = medicalDecisionTable.get();
		this.medicalDecisionTableObj.init(this.bean);
		
		Reimbursement reimbursement = createRodService.getReimbursementObjectByKey(reimbursmentKey);
		
		List<BillEntryDetailsDTO> billEntryDetailsDTO = new ArrayList<BillEntryDetailsDTO>();
		List<PreHospitalizationDTO> postHospitalisationDTO = new ArrayList<PreHospitalizationDTO>();
		List<PreHospitalizationDTO> preHospitalisationDTO = new ArrayList<PreHospitalizationDTO>();
		this.bean.setBillEntryDetailsDTO(billEntryDetailsDTO);
		this.bean.setPrehospitalizationDTO(preHospitalisationDTO);
		this.bean.setPostHospitalizationDTO(postHospitalisationDTO);
//		
		
		if(reimbursement.getDocAcknowLedgement() != null && (reimbursement.getDocAcknowLedgement().getPreHospitalisationFlag()== null
				||(reimbursement.getDocAcknowLedgement().getPreHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getPreHospitalisationFlag().equalsIgnoreCase("N"))))
		{
			this.bean.setPrehospitalizationDTO(null);
		}

		if(reimbursement.getDocAcknowLedgement() != null && (reimbursement.getDocAcknowLedgement().getPostHospitalisationFlag()== null
				||(reimbursement.getDocAcknowLedgement().getPostHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getPostHospitalisationFlag().equalsIgnoreCase("N"))))
		{
			this.bean.setPostHospitalizationDTO(null);
		}
		
		Boolean isBill = false;
		billAssessment.init(this.bean, reimbursmentKey,true);
		if(null != this.bean.getBillEntryDetailsDTO() && !this.bean.getBillEntryDetailsDTO().isEmpty())
		{
			for (BillEntryDetailsDTO billDetailsDTO :  this.bean.getBillEntryDetailsDTO()) {
				
				if((SHAConstants.PACKAGES_CHARGES).equalsIgnoreCase(billDetailsDTO.getItemName()))
				{
					isBill = true;
				}
				billDetailsDTO.setPresenterString(SHAConstants.BILLING);
			}
			
			/*if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET) || this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_PRODUCT)){*/
			if(null != this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
					(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
				for(BillEntryDetailsDTO roomListDTO : this.bean.getRoomRentNursingChargeList()){
					roomListDTO.setPresenterString(SHAConstants.BILLING);
				}
				
				for(BillEntryDetailsDTO limtDListDTO : this.bean.getPolicyLimitDList()){
					limtDListDTO.setPresenterString(SHAConstants.BILLING);
				}
				
				for(BillEntryDetailsDTO limtEListDTO : this.bean.getPolicyLimitEList()){
					limtEListDTO.setPresenterString(SHAConstants.BILLING);
				}
	
				for(BillEntryDetailsDTO limitDNEListDTO : this.bean.getPolicyLimitDandEList()){
					limitDNEListDTO.setPresenterString(SHAConstants.BILLING);
				}
				for(BillEntryDetailsDTO ambulanceListDTO : this.bean.getAmbulanceChargeList()){
					ambulanceListDTO.setPresenterString(SHAConstants.BILLING);
				}	
			}
		}
//		billAssessment.init(this.bean, reimbursmentKey,true);
		
		if(reimbursement.getDocAcknowLedgement() != null && (reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null
				&& (reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("N")|| reimbursement.getDocAcknowLedgement().getPostHospitalisationFlag()== null) &&
				reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag() != null
				&& (reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag().equalsIgnoreCase("N")|| reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag()== null)
				&& (reimbursement.getDocAcknowLedgement().getHospitalizationRepeatFlag().equalsIgnoreCase("N")|| reimbursement.getDocAcknowLedgement().getHospitalizationRepeatFlag()== null)))
		{
			this.bean.setBillEntryDetailsDTO(null);
		}else{
			
//			if(isBill)
			setMedicalDecisionTableDTO();	
			List<DiagnosisProcedureTableDTO> values = medicalDecisionTableObj.getValues();
			this.bean.setDiagnosisProcedureDtoList(values);
		}
	
		
		if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null) {
			this.bean.getPreauthMedicalDecisionDetails().setAmountInwords(SHAUtils.getParsedAmount(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()));
		}
		
		List<NonPayableReasonDto> nonPayableReasonListDto = new ArrayList<NonPayableReasonDto>();
		NonPayableReasonDto nonPayableReasonDto = null;
//		int entityCode = 9312;  
		int entityCode = 1;
		
		if(this.bean.getNonPayableReasonListDto() == null || this.bean.getNonPayableReasonListDto().isEmpty()){
			/*if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET) || this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_PRODUCT))*/
			if(null != this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
					(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())))
			{			
				List<BillEntryDetailsDTO> roomRentNursingChargeList = this.bean.getRoomRentNursingChargeList();
				List<BillEntryDetailsDTO> policyLimitDList = this.bean.getPolicyLimitDList();
				List<BillEntryDetailsDTO> policyLimitEList = this.bean.getPolicyLimitEList();
				List<BillEntryDetailsDTO> policyLimitDandEList = this.bean.getPolicyLimitDandEList();
				StringBuffer strBuf = new StringBuffer("");
				if(roomRentNursingChargeList != null && !roomRentNursingChargeList.isEmpty()){
					strBuf = new StringBuffer("");
					String deductibleOrNonPayableReason = "";
					for (BillEntryDetailsDTO abcTableDto : roomRentNursingChargeList) {
						if(("billing").equalsIgnoreCase(abcTableDto.getPresenterString()) && abcTableDto.getDeductibleNonPayableReasonBilling()!=null){
							strBuf.append(abcTableDto.getDeductibleNonPayableReasonBilling());
						}
						else if(("financial").equalsIgnoreCase(abcTableDto.getPresenterString())&&abcTableDto.getDeductibleNonPayableReasonFA()!=null){
							strBuf.append(abcTableDto.getDeductibleNonPayableReasonFA());
						}
						deductibleOrNonPayableReason=strBuf.toString();
						if(strBuf.length() != 0 && strBuf.toString().length() > 75){
							nonPayableReasonDto = new NonPayableReasonDto();
	//						nonPayableReasonDto.setBillNo(abcTableDto.getBillNo());
	//						nonPayableReasonDto.setItemName(abcTableDto.getItemName());
							nonPayableReasonDto.setDeductibleOrNonPayableReason(strBuf.toString());
//							String deductibleOrNonPayableReason = strBuf.toString().substring(0, 75) + "  *<sup>"+entityCode+"</sup>";  /*" <style isBold='true' pdfFontName='CombiNumeralsLtd'>&#"+entityCode+";</style>";*/	
							 deductibleOrNonPayableReason = "  Refer Note #"+entityCode;
							if(("billing").equalsIgnoreCase(abcTableDto.getPresenterString()))
								abcTableDto.setDeductibleNonPayableReasonBilling(deductibleOrNonPayableReason);
							else if(("financial").equalsIgnoreCase(abcTableDto.getPresenterString()))
								abcTableDto.setDeductibleNonPayableReasonFA(deductibleOrNonPayableReason);
							else 
							{
								abcTableDto.setDeductibleNonPayableReasonBilling("");
								abcTableDto.setDeductibleNonPayableReasonFA("");
							}
								
							System.out.println("Deductible Reason    ***************** ==========  : " + deductibleOrNonPayableReason);
		//					nonPayableReasonDto.setSno(" &#"+entityCode+";");
							nonPayableReasonDto.setSno("#"+entityCode);
							abcTableDto.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
							entityCode++;
							nonPayableReasonListDto.add(nonPayableReasonDto);
						}else{
							abcTableDto.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
							}

//						strBuf = new StringBuffer("");
					
					}
				}
				if(policyLimitDList != null && !policyLimitDList.isEmpty()){
					strBuf = new StringBuffer("");
					String deductibleOrNonPayableReason = "";
					for (BillEntryDetailsDTO DTableDto : policyLimitDList) {
	
						if(("billing").equalsIgnoreCase(DTableDto.getPresenterString()) && DTableDto.getDeductibleNonPayableReasonBilling()!=null){
							strBuf.append(DTableDto.getDeductibleNonPayableReasonBilling());
						}
						else if(("financial").equalsIgnoreCase(DTableDto.getPresenterString())&&DTableDto.getDeductibleNonPayableReasonFA()!=null){
							strBuf.append(DTableDto.getDeductibleNonPayableReasonFA());
						}
						deductibleOrNonPayableReason=strBuf.toString();
						if(strBuf.length() != 0 && strBuf.toString().length() > 75){
							nonPayableReasonDto = new NonPayableReasonDto();
	//						nonPayableReasonDto.setBillNo(abcTableDto.getBillNo());
	//						nonPayableReasonDto.setItemName(abcTableDto.getItemName());
							nonPayableReasonDto.setDeductibleOrNonPayableReason(strBuf.toString());
//							String deductibleOrNonPayableReason = strBuf.toString().substring(0, 75) + "  *<sup>"+entityCode+"</sup>";  /*" <style isBold='true' pdfFontName='CombiNumeralsLtd'>&#"+entityCode+";</style>";*/
							 deductibleOrNonPayableReason = "  Refer Note #"+entityCode;
							if(("billing").equalsIgnoreCase(DTableDto.getPresenterString()))
								DTableDto.setDeductibleNonPayableReasonBilling(deductibleOrNonPayableReason);
							else if(("financial").equalsIgnoreCase(DTableDto.getPresenterString()))
								DTableDto.setDeductibleNonPayableReasonFA(deductibleOrNonPayableReason);
							else 
							{
								DTableDto.setDeductibleNonPayableReasonBilling("");
								DTableDto.setDeductibleNonPayableReasonFA("");
							}
								
							System.out.println("Deductible Reason    ***************** ==========  : " + deductibleOrNonPayableReason);
		//					nonPayableReasonDto.setSno(" &#"+entityCode+";");
							nonPayableReasonDto.setSno("#"+entityCode);
							DTableDto.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
							entityCode++;
							nonPayableReasonListDto.add(nonPayableReasonDto);
						}else{
							DTableDto.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
							}
//						strBuf = new StringBuffer("");
					}
				}
					
				if(policyLimitEList != null && !policyLimitEList.isEmpty()){
					strBuf = new StringBuffer("");
					String deductibleOrNonPayableReason = "";
					for (BillEntryDetailsDTO ETableDto : policyLimitEList) {
	
						if(("billing").equalsIgnoreCase(ETableDto.getPresenterString()) && ETableDto.getDeductibleNonPayableReasonBilling()!=null){
							strBuf.append(ETableDto.getDeductibleNonPayableReasonBilling());
						}
						else if(("financial").equalsIgnoreCase(ETableDto.getPresenterString())&&ETableDto.getDeductibleNonPayableReasonFA()!=null){
							strBuf.append(ETableDto.getDeductibleNonPayableReasonFA());
						}
						deductibleOrNonPayableReason=strBuf.toString();
						if(strBuf.length() != 0 && strBuf.toString().length() > 75){
							nonPayableReasonDto = new NonPayableReasonDto();
	//						nonPayableReasonDto.setBillNo(abcTableDto.getBillNo());
	//						nonPayableReasonDto.setItemName(abcTableDto.getItemName());
							nonPayableReasonDto.setDeductibleOrNonPayableReason(strBuf.toString());
//							String deductibleOrNonPayableReason = strBuf.toString().substring(0, 75) + "  *<sup>"+entityCode+"</sup>";  /*" <style isBold='true' pdfFontName='CombiNumeralsLtd'>&#"+entityCode+";</style>";*/
							 deductibleOrNonPayableReason = "  Refer Note #" + entityCode;
							if(ETableDto.getPresenterString().equalsIgnoreCase("billing"))
								ETableDto.setDeductibleNonPayableReasonBilling(deductibleOrNonPayableReason);
							else if(("financial").equalsIgnoreCase(ETableDto.getPresenterString()))
								ETableDto.setDeductibleNonPayableReasonFA(deductibleOrNonPayableReason);
							else 
							{
								ETableDto.setDeductibleNonPayableReasonBilling("");
								ETableDto.setDeductibleNonPayableReasonFA("");
							}
								
							System.out.println("Deductible Reason    ***************** ==========  : " + deductibleOrNonPayableReason);
		//					nonPayableReasonDto.setSno(" &#"+entityCode+";");
							nonPayableReasonDto.setSno("#"+entityCode);
							ETableDto.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
							entityCode++;
							nonPayableReasonListDto.add(nonPayableReasonDto);
						}else{
							ETableDto.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
							}
//						strBuf = new StringBuffer("");
					}
				}
					
				if(policyLimitDandEList != null && !policyLimitDandEList.isEmpty() && bean.getPackageFlag()){
					strBuf = new StringBuffer("");
					String deductibleOrNonPayableReason = "";
					for (BillEntryDetailsDTO DnETableDto : policyLimitDandEList) {
	
						if(("billing").equalsIgnoreCase(DnETableDto.getPresenterString()) && DnETableDto.getDeductibleNonPayableReasonBilling()!=null){
							strBuf.append(DnETableDto.getDeductibleNonPayableReasonBilling());
						}
						else if(("financial").equalsIgnoreCase(DnETableDto.getPresenterString())&&DnETableDto.getDeductibleNonPayableReasonFA()!=null){
							strBuf.append(DnETableDto.getDeductibleNonPayableReasonFA());
						}
						deductibleOrNonPayableReason=strBuf.toString();
						if(strBuf.length() != 0 && strBuf.toString().length() > 75){
							nonPayableReasonDto = new NonPayableReasonDto();
	//						nonPayableReasonDto.setBillNo(abcTableDto.getBillNo());
	//						nonPayableReasonDto.setItemName(abcTableDto.getItemName());
							nonPayableReasonDto.setDeductibleOrNonPayableReason(strBuf.toString());
//							String deductibleOrNonPayableReason = strBuf.toString().substring(0, 75) + "  *<sup>"+entityCode+"</sup>";  /*" <style isBold='true' pdfFontName='CombiNumeralsLtd'>&#"+entityCode+";</style>";*/
							 deductibleOrNonPayableReason = "  Refer Note #"+entityCode;
							if(("billing").equalsIgnoreCase(DnETableDto.getPresenterString()))
								DnETableDto.setDeductibleNonPayableReasonBilling(deductibleOrNonPayableReason);
							else if(("financial").equalsIgnoreCase(DnETableDto.getPresenterString()))
								DnETableDto.setDeductibleNonPayableReasonFA(deductibleOrNonPayableReason);
							else 
							{
								DnETableDto.setDeductibleNonPayableReasonBilling("");
								DnETableDto.setDeductibleNonPayableReasonFA("");
							}
								
							System.out.println("Deductible Reason    ***************** ==========  : " + deductibleOrNonPayableReason);
		//					nonPayableReasonDto.setSno(" &#"+entityCode+";");
							nonPayableReasonDto.setSno("#"+entityCode);
							DnETableDto.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
							entityCode++;
							nonPayableReasonListDto.add(nonPayableReasonDto);
						}else{
							DnETableDto.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
							}
//						strBuf = new StringBuffer("");
					}
				}
			}
			else{
	
				List<BillEntryDetailsDTO> hospitalisationListDto = this.bean.getBillEntryDetailsDTO(); 
				if(hospitalisationListDto != null && !hospitalisationListDto.isEmpty()){
					
					for (BillEntryDetailsDTO hospitalisationDto : hospitalisationListDto) {
						if(hospitalisationDto.getDeductibleOrNonPayableReason() != null && !hospitalisationDto.getDeductibleOrNonPayableReason().isEmpty() && hospitalisationDto.getDeductibleOrNonPayableReason().length()>75){
							nonPayableReasonDto = new NonPayableReasonDto();
	//						nonPayableReasonDto.setBillNo(hospitalisationDto.getBillNo());
	//						nonPayableReasonDto.setItemName(hospitalisationDto.getItemName());
							nonPayableReasonDto.setDeductibleOrNonPayableReason(hospitalisationDto.getDeductibleOrNonPayableReason());
//							String deductibleOrNonPayableReason = hospitalisationDto.getDeductibleOrNonPayableReason().substring(0, 75) + /*" <style isBold='true' pdfFontName='CombiNumeralsLtd'>&#"+entityCode+";</style>";*/
//																																		  "  *<sup>"+entityCode+"</sup>";
							String deductibleOrNonPayableReason = "  Refer Note #"+entityCode;
							System.out.println("Deductible Reason    ***************** ==========  : " + deductibleOrNonPayableReason);
		//					nonPayableReasonDto.setSno(" &#"+entityCode+";");
							nonPayableReasonDto.setSno("#"+entityCode);
							hospitalisationDto.setDeductibleOrNonPayableReason(deductibleOrNonPayableReason);
							hospitalisationDto.setNonPayableRmrksForAssessmentSheet(deductibleOrNonPayableReason);
							entityCode++;
							nonPayableReasonListDto.add(nonPayableReasonDto);
						}else{
							hospitalisationDto.setNonPayableRmrksForAssessmentSheet(hospitalisationDto.getDeductibleOrNonPayableReason());
						}
					
					}
				}
				
				List<OtherBenefitsTableDto> otherBenefitsList = this.bean.getPreauthDataExtractionDetails().getOtherBenefitsList();
				if(otherBenefitsList != null && !otherBenefitsList.isEmpty()){
					for (OtherBenefitsTableDto otherBenefitsTableDto : otherBenefitsList) {
						if(otherBenefitsTableDto.getRemarks() != null && !otherBenefitsTableDto.getRemarks().isEmpty() && otherBenefitsTableDto.getRemarks().length()>75){
							nonPayableReasonDto = new NonPayableReasonDto();
	//						nonPayableReasonDto.setBillNo(hospitalisationDto.getBillNo());
	//						nonPayableReasonDto.setItemName(hospitalisationDto.getItemName());
							nonPayableReasonDto.setDeductibleOrNonPayableReason(otherBenefitsTableDto.getRemarks());
//							String deductibleOrNonPayableReason = otherBenefitsTableDto.getRemarks().substring(0, 75) + /*" <style isBold='true' pdfFontName='CombiNumeralsLtd'>&#"+entityCode+";</style>";*/
//																																		  "  *<sup>"+entityCode+"</sup>";
							String deductibleOrNonPayableReason = "  Refer Note #"+entityCode;
							System.out.println("Deductible Reason    ***************** ==========  : " + deductibleOrNonPayableReason);
		//					nonPayableReasonDto.setSno(" &#"+entityCode+";");
							nonPayableReasonDto.setSno("#"+entityCode);
							otherBenefitsTableDto.setRemarks(deductibleOrNonPayableReason);
							otherBenefitsTableDto.setNonPayableRmrksForAssessmentSheet(deductibleOrNonPayableReason);
							entityCode++;
							nonPayableReasonListDto.add(nonPayableReasonDto);
						}else{
							otherBenefitsTableDto.setNonPayableRmrksForAssessmentSheet(otherBenefitsTableDto.getRemarks());
						}
					}
				}
				
				if(this.bean.getHospitalDiscountRemarksForAssessmentSheet() != null && !this.bean.getHospitalDiscountRemarksForAssessmentSheet().isEmpty() && this.bean.getHospitalDiscountRemarksForAssessmentSheet().length()>75){
					nonPayableReasonDto.setDeductibleOrNonPayableReason(this.bean.getHospitalDiscountRemarksForAssessmentSheet());
//					String deductibleOrNonPayableReason = this.bean.getHospitalDiscountRemarks().substring(0, 75) + /*" <style isBold='true' pdfFontName='CombiNumeralsLtd'>&#"+entityCode+";</style>";*/
//																																  "  *<sup>"+entityCode+"</sup>";
					String deductibleOrNonPayableReason = "  Refer Note #"+entityCode;
					System.out.println("Deductible Reason    ***************** ==========  : " + deductibleOrNonPayableReason);
	//					nonPayableReasonDto.setSno(" &#"+entityCode+";");
					nonPayableReasonDto.setSno("#"+entityCode);
					this.bean.setHospitalDiscountRemarks(deductibleOrNonPayableReason);
					entityCode++;
					nonPayableReasonListDto.add(nonPayableReasonDto);
				}
				else{
					this.bean.setHospitalDiscountRemarks(this.bean.getHospitalDiscountRemarksForAssessmentSheet());
				}
				
				if(this.bean.getDeductionRemarksForAssessmentSheet() != null && !this.bean.getDeductionRemarksForAssessmentSheet().isEmpty() && this.bean.getDeductionRemarksForAssessmentSheet().length()>75){
					nonPayableReasonDto.setDeductibleOrNonPayableReason(this.bean.getDeductionRemarksForAssessmentSheet());
//					String deductibleRemarks = this.bean.getDeductionRemarks().substring(0, 75) + /*" <style isBold='true' pdfFontName='CombiNumeralsLtd'>&#"+entityCode+";</style>";*/
//																																  "  *<sup>"+entityCode+"</sup>";
					String deductibleRemarks = "  Refer Note #"+entityCode;
					System.out.println("Deductible Reason    ***************** ==========  : " + deductibleRemarks);
	//					nonPayableReasonDto.setSno(" &#"+entityCode+";");
					nonPayableReasonDto.setSno("#"+entityCode);
					this.bean.setDeductionRemarks(deductibleRemarks);
					entityCode++;
					nonPayableReasonListDto.add(nonPayableReasonDto);
				}
				else{
					this.bean.setDeductionRemarks(this.bean.getDeductionRemarksForAssessmentSheet());
				}
			}
		
			List<PreHospitalizationDTO> prehospitalizationListDTO = this.bean.getPrehospitalizationDTO();
			if(prehospitalizationListDTO != null && !prehospitalizationListDTO.isEmpty()){
				for (PreHospitalizationDTO preHospitalizationDTO : prehospitalizationListDTO) {
					if(preHospitalizationDTO.getReason() != null && !preHospitalizationDTO.getReason().isEmpty() && preHospitalizationDTO.getReason().length()>75){
						nonPayableReasonDto = new NonPayableReasonDto();
		//				nonPayableReasonDto.setBillNo(hospitalisationDto.getBillNo());
		//				nonPayableReasonDto.setItemName(hospitalisationDto.getItemName());
						nonPayableReasonDto.setDeductibleOrNonPayableReason(preHospitalizationDTO.getReason());
//						String deductibleOrNonPayableReason = preHospitalizationDTO.getReason().substring(0, 75) + /*" <style isBold='true' pdfFontName='CombiNumeralsLtd'>&#"+entityCode+";</style>";*/
//																																	  "  *<sup>"+entityCode+"</sup>";
						String deductibleOrNonPayableReason = "  Refer Note #"+entityCode;
						System.out.println("Deductible Reason    ***************** ==========  : " + deductibleOrNonPayableReason);
		//				nonPayableReasonDto.setSno(" &#"+entityCode+";");
						nonPayableReasonDto.setSno("#"+entityCode);
						preHospitalizationDTO.setReason(deductibleOrNonPayableReason);
						preHospitalizationDTO.setNonPayableRmrksForAssessmentSheet(deductibleOrNonPayableReason);
						entityCode++;
						nonPayableReasonListDto.add(nonPayableReasonDto);
					}else{
						preHospitalizationDTO.setNonPayableRmrksForAssessmentSheet(preHospitalizationDTO.getReason());
					}
				}
			}
			List<PreHospitalizationDTO> postHospitalizationListDTO = this.bean.getPostHospitalizationDTO();
			if(postHospitalizationListDTO != null && !postHospitalizationListDTO.isEmpty()){
				for (PreHospitalizationDTO postHospitalizationDTO : postHospitalizationListDTO) {
					if(postHospitalizationDTO.getReason() != null && !postHospitalizationDTO.getReason().isEmpty() && postHospitalizationDTO.getReason().length()>75){
						nonPayableReasonDto = new NonPayableReasonDto();
		//				nonPayableReasonDto.setBillNo(postHospitalizationDTO.getBillNo());
		//				nonPayableReasonDto.setItemName(postHospitalizationDTO.getItemName());
						nonPayableReasonDto.setDeductibleOrNonPayableReason(postHospitalizationDTO.getReason());
//						String deductibleOrNonPayableReason = postHospitalizationDTO.getReason().substring(0, 75) + /*" <style isBold='true' pdfFontName='CombiNumeralsLtd'>&#"+entityCode+";</style>";*/
//																																	  "  *<sup>"+entityCode+"</sup>";
						String deductibleOrNonPayableReason = "  Refer Note #"+entityCode;
						System.out.println("Deductible Reason    ***************** ==========  : " + deductibleOrNonPayableReason);
		//				nonPayableReasonDto.setSno(" &#"+entityCode+";");
						nonPayableReasonDto.setSno("#"+entityCode);
						postHospitalizationDTO.setReason(deductibleOrNonPayableReason);
						postHospitalizationDTO.setNonPayableRmrksForAssessmentSheet(deductibleOrNonPayableReason);
						entityCode++;
						nonPayableReasonListDto.add(nonPayableReasonDto);
					}else{
						postHospitalizationDTO.setNonPayableRmrksForAssessmentSheet(postHospitalizationDTO.getReason());
					}
				}
			}
		
			this.bean.setNonPayableReasonListDto(nonPayableReasonListDto);	
		}
		
		DocumentGenerator docGen = new DocumentGenerator();
		ReportDto reportDto = new ReportDto();
		
		List<PreauthDTO> preauthDTOList = new ArrayList<PreauthDTO>();
		preauthDTOList.add(this.bean);
		/*if(!ReferenceTable.SENIOR_CITIZEN_RED_CARPET.equals(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && !this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_PRODUCT)){*/
		if(null != this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey() && 
				!(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) ){
			calculateTotalForReport();	
		}
		
		reportDto.setClaimId(this.bean.getClaimDTO().getClaimId());
		reportDto.setBeanList(preauthDTOList);		

		String templateName = "BillSummaryOtherProducts";
		
		/*if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET) || this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_PRODUCT))*/
		if(null != this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey() && 
				(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())))
		{
			templateName = "BillAssessmentSheetSCRC";
		}
		
		Panel firstVertical = getContentofTemplate(templateName,reportDto,docGen);
//		firstVertical.setWidth("440px");
//		firstVertical.setHeight("440px");
		
//		panel.setHeight("420px");
		
		firstVertical.setHeight("900px");

		setCompositionRoot(firstVertical);		
		
	}
	
	public void setMedicalDecisionTableDTO(){
		
		String previousApprovedAmt = SHAUtils.getPreviousApprovedAmt(this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList(), this.bean
				.getPreauthMedicalProcessingDetails()
				.getProcedureExclusionCheckTableList(), this.bean.getResidualAmountDTO());
		
		Boolean isSetZero = false;
		if(SHAUtils.getDoubleFromString(this.bean.getAmountConsidered()) < SHAUtils.getDoubleFromString(previousApprovedAmt)) {
			isSetZero = true;
		}
		
		List<DiagnosisProcedureTableDTO> filledDTO = this.bean
				.getPreauthMedicalDecisionDetails()
				.getMedicalDecisionTableDTO();

		List<DiagnosisProcedureTableDTO> medicalDecisionDTOList = new ArrayList<DiagnosisProcedureTableDTO>();
		if (filledDTO.isEmpty()) {
			List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO pedValidationTableDTO : pedValidationTableList) {
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				
				if(this.bean.getNewIntimationDTO().getPolicy().getProductType().getKey().intValue() != 2904 ||
						this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CRITICARE_OTHER_BANKS)){
					
					dto.setPedImpactOnDiagnosis(pedValidationTableDTO.getPedImpactOnDiagnosis() != null && pedValidationTableDTO.getPedImpactOnDiagnosis().getValue() != null ? pedValidationTableDTO.getPedImpactOnDiagnosis().getValue() : "");
					dto.setNotPayingReason(pedValidationTableDTO.getReasonForNotPaying() != null && pedValidationTableDTO.getReasonForNotPaying().getValue() != null ? pedValidationTableDTO.getReasonForNotPaying().getValue() : "");
					dto.setReasonForNotPaying(pedValidationTableDTO.getReasonForNotPaying() != null ? pedValidationTableDTO.getReasonForNotPaying() : null);
				}
				
				if (pedValidationTableDTO.getConsiderForPaymentFlag() != null) {
					Boolean isPaymentAvailable = pedValidationTableDTO
							.getConsiderForPaymentFlag().toLowerCase()
							.equalsIgnoreCase("y") ? true : false;
					if (isPaymentAvailable) {
						List<PedDetailsTableDTO> pedList = pedValidationTableDTO
								.getPedList();
						if (!pedList.isEmpty()) {
							for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {

								List<ExclusionDetails> exclusionAllDetails = pedDetailsTableDTO
										.getExclusionAllDetails();
								String paymentFlag = "y";
								if(exclusionAllDetails != null) {
									for (ExclusionDetails exclusionDetails : exclusionAllDetails) {
										if (null != pedDetailsTableDTO
												.getExclusionDetails()
												&& exclusionDetails
														.getKey()
														.equals(pedDetailsTableDTO
																.getExclusionDetails()
																.getId())) {
											paymentFlag = exclusionDetails
													.getPaymentFlag();
										}
									}
								}
								

								if (paymentFlag.toLowerCase().equalsIgnoreCase(
										"n")) {
									isPaymentAvailable = false;
									break;
								}
							}
						}
					}

					if (!isPaymentAvailable) {
						dto.setMinimumAmount(0);
					}
					dto.setIsPaymentAvailable(isPaymentAvailable);
				} else {
					dto.setIsPaymentAvailable(false);
				}
				if (pedValidationTableDTO.getSumInsuredRestriction() == null) {
					dto.setRestrictionSI("NA");
				} else {
					dto.setRestrictionSI(SHAUtils.getIntegerFromString(
							pedValidationTableDTO.getSumInsuredRestriction()
									.getValue()).toString());
				}
				List<PedDetailsTableDTO> pedList = pedValidationTableDTO.getPedList();
				for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
					if(pedDetailsTableDTO.getCopay() != null) {
						dto.setCoPayPercentage(pedDetailsTableDTO.getCopay());
					}
				}

				if (pedValidationTableDTO.getSublimitName() == null) {
					dto.setSubLimitAmount("NA");
				} else {
					dto.setSubLimitAmount(String.valueOf( SHAUtils.getDoubleFromString(
							pedValidationTableDTO.getSublimitAmt()).intValue())  );
				}
				dto.setPackageAmt("NA");
				dto.setDiagnosisDetailsDTO(pedValidationTableDTO);
				dto.setAmountConsidered(pedValidationTableDTO.getAmountConsideredAmount() != null ? pedValidationTableDTO.getAmountConsideredAmount().intValue() : 0);
				
				
				dto.setCoPayAmount(pedValidationTableDTO.getCopayAmount() != null ? pedValidationTableDTO.getCopayAmount().intValue() : 0);
				dto.setMinimumAmountOfAmtconsideredAndPackAmt(pedValidationTableDTO.getMinimumAmount() != null ? pedValidationTableDTO.getMinimumAmount().intValue() : 0);
				dto.setNetAmount(pedValidationTableDTO.getNetAmount() != null ? pedValidationTableDTO.getNetAmount().intValue() : 0);
				dto.setMinimumAmount(pedValidationTableDTO.getApprovedAmount() != null ? pedValidationTableDTO.getApprovedAmount().intValue() : 0);
				dto.setRemarks(pedValidationTableDTO.getApproveRemarks() != null ? pedValidationTableDTO.getApproveRemarks() : "");
				dto.setReverseAllocatedAmt(pedValidationTableDTO.getNetApprovedAmount() != null ? pedValidationTableDTO.getNetApprovedAmount().intValue() : 0);
				if(pedValidationTableDTO.getCopayPercentage() != null) {
					SelectValue value = new SelectValue();
					value.setId(pedValidationTableDTO.getCopayPercentage().longValue());
					value.setValue( String.valueOf(pedValidationTableDTO.getCopayPercentage().longValue()));
					dto.setCoPayPercentage(value);
				}
				Integer amount = SHAUtils.getIntegerFromString(this.bean.getInitialAmountConsidered()) - (SHAUtils.getIntegerFromString(this.bean.getPreHospitalisationValue()) + SHAUtils.getIntegerFromString(this.bean.getPostHospitalisationValue()));
				if(isSetZero) {
					dto.setAmountConsidered(0);
					dto.setCoPayAmount(0);
					dto.setNetAmount(0);
					dto.setMinimumAmount(0);
					dto.setNetApprovedAmt(0);
					dto.setReverseAllocatedAmt(0);
				}
				
				if(dto.getAmountConsidered() == 0 ) {
					dto.setMinimumAmount(0);
				}
				medicalDecisionDTOList.add(dto);
			}
			List<ProcedureDTO> procedureExclusionCheckTableList = this.bean
					.getPreauthMedicalProcessingDetails()
					.getProcedureExclusionCheckTableList();
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				dto.setProcedureDTO(procedureDTO);
				Boolean isPaymentAvailable = true;
				if (procedureDTO.getConsiderForPaymentFlag() != null) {
					isPaymentAvailable = procedureDTO
							.getConsiderForPaymentFlag().toLowerCase()
							.equalsIgnoreCase("y") ? true : false;
				} else {
					isPaymentAvailable = false;
					if(procedureDTO.getNewProcedureFlag() != null && procedureDTO.getNewProcedureFlag().equals(1)) {
						isPaymentAvailable = true;
					}
					dto.setIsPaymentAvailable(isPaymentAvailable);
					
				}
					if(isPaymentAvailable) {
						if(procedureDTO.getExclusionDetails() != null && procedureDTO.getExclusionDetails().getValue() != null && !procedureDTO.getExclusionDetails().getValue().toLowerCase().equalsIgnoreCase("not applicable")) {
							isPaymentAvailable = false;
						}
					}
					if (!isPaymentAvailable) {
						dto.setMinimumAmount(0);
					}
					dto.setIsPaymentAvailable(isPaymentAvailable);
				dto.setRestrictionSI("NA");
				
				dto.setPackageAmt("NA");
				if(procedureDTO.getPackageRate() != null && procedureDTO.getPackageRate() >= 0) {
					dto.setPackageAmt(procedureDTO.getPackageRate().toString());
				}
				
				if(procedureDTO.getCopay() != null) {
					dto.setCoPayPercentage(procedureDTO.getCopay());
				}

				if (procedureDTO.getSublimitName() == null) {
					dto.setSubLimitAmount("NA");
				} else {
					dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
							procedureDTO.getSublimitAmount()).toString());
				}
				
				dto.setAmountConsidered(procedureDTO.getAmountConsideredAmount() != null ? procedureDTO.getAmountConsideredAmount().intValue() : 0);
				
				dto.setCoPayAmount(procedureDTO.getCopayAmount() != null ? procedureDTO.getCopayAmount().intValue() : 0);
				dto.setMinimumAmountOfAmtconsideredAndPackAmt(procedureDTO.getMinimumAmount() != null ? procedureDTO.getMinimumAmount().intValue() : 0);
				dto.setNetAmount(procedureDTO.getNetAmount() != null ? procedureDTO.getNetAmount().intValue() : 0);
				dto.setMinimumAmount(procedureDTO.getApprovedAmount() != null ? procedureDTO.getApprovedAmount().intValue() : 0);
				dto.setReverseAllocatedAmt(procedureDTO.getNetApprovedAmount() != null ? procedureDTO.getNetApprovedAmount().intValue() : 0);
				dto.setRemarks(procedureDTO.getApprovedRemarks() != null ? procedureDTO.getApprovedRemarks() : "");
				if(procedureDTO.getCopayPercentage() != null) {
					SelectValue value = new SelectValue();
					value.setId(procedureDTO.getCopayPercentage().longValue());
					value.setValue( String.valueOf(procedureDTO.getCopayPercentage().doubleValue()));
					dto.setCoPayPercentage(value);
				}
				Integer amount = SHAUtils.getIntegerFromString(this.bean.getInitialAmountConsidered()) - (SHAUtils.getIntegerFromString(this.bean.getPreHospitalisationValue()) + SHAUtils.getIntegerFromString(this.bean.getPostHospitalisationValue()));
				if(isSetZero) {
					dto.setAmountConsidered(0);
					dto.setCoPayAmount(0);
					dto.setNetAmount(0);
					dto.setMinimumAmount(0);
					dto.setNetApprovedAmt(0);
					dto.setReverseAllocatedAmt(0);
				}
				
				if(dto.getAmountConsidered() == 0 ) {
					dto.setMinimumAmount(0);
				}
				medicalDecisionDTOList.add(dto);
			}
			
			Map<String, Object> caluculationInputValues = new HashMap<String, Object>();
			caluculationInputValues.put("policyNumber", this.bean
					.getPolicyDto().getPolicyNumber());
			// caluculationInputValues.put("insuredId",
			// this.bean.getPolicyDto().getInsuredId());
			caluculationInputValues.put("insuredId", this.bean
					.getNewIntimationDTO().getInsuredPatient().getInsuredId());

			DBCalculationService dbCalculationService = new DBCalculationService();
			Double insuredSumInsured = dbCalculationService
					.getInsuredSumInsured(this.bean.getNewIntimationDTO()
							.getInsuredPatient().getInsuredId().toString(),
							this.bean.getPolicyDto().getKey(),this.bean.getNewIntimationDTO()
							.getInsuredPatient().getLopFlag());
			caluculationInputValues.put(
					"policySI",
					insuredSumInsured != 0 ? String.valueOf(insuredSumInsured)
							: String.valueOf(this.bean.getPolicyDto()
									.getTotalSumInsured()));

			if (null != medicalDecisionDTOList
					&& medicalDecisionDTOList.size() > 0) {
				int diag = 1;
				int proc = 1;
				for (int i = 0; i < medicalDecisionDTOList.size(); i++) {
					DiagnosisProcedureTableDTO medicalDecisionDto = medicalDecisionDTOList
							.get(i);
					if (medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Diag "
								+ (diag++));
						caluculationInputValues
								.put("restrictedSI",
										medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction() != null ? medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction().getId()
										: null);
						caluculationInputValues
						.put("restrictedSIAmount",
								medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction() != null ? medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction().getValue()
										: null);
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues.put("diagOrProcId",
								medicalDecisionDto.getDiagnosisDetailsDTO().getDiagnosisName() == null ? 0l : medicalDecisionDto.getDiagnosisDetailsDTO().getDiagnosisName().getId());
						caluculationInputValues.put("diagnosisId",
								medicalDecisionDto.getDiagnosisDetailsDTO()
										.getDiagnosisName().getId().toString());
						caluculationInputValues.put("referenceFlag", "D");
					} else if (medicalDecisionDto.getProcedureDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Proc "
								+ (proc++));
						caluculationInputValues.put("restrictedSI", null);
						caluculationInputValues.put("restrictedSIAmount", null);
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto.getProcedureDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getProcedureDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues
								.put("diagOrProcId", medicalDecisionDto.getProcedureDTO().getProcedureName() == null ? 0l : (medicalDecisionDto.getProcedureDTO().getProcedureName().getId() == null ? 0l : medicalDecisionDto.getProcedureDTO().getProcedureName().getId()));
						caluculationInputValues.put("referenceFlag", "P");
					}
					caluculationInputValues.put("preauthKey", this.bean.getKey());
					
					caluculationInputValues.put(SHAConstants.IS_NON_ALLOPATHIC, bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null ? (bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue().toLowerCase().contains("non") ? true : false) : false);
					caluculationInputValues.put(SHAConstants.INSURED_KEY, bean.getNewIntimationDTO().getInsuredPatient().getKey());
					caluculationInputValues.put(SHAConstants.POLICY_KEY, bean.getNewIntimationDTO().getPolicy().getKey());
					caluculationInputValues.put(SHAConstants.CLAIM_KEY, bean.getClaimKey());
					
					System.out.println("***************************************************"+ bean.getClaimKey());
					
					
					
					bean.setIsNonAllopathic((Boolean)caluculationInputValues.get(SHAConstants.IS_NON_ALLOPATHIC));
					
					medicalDecisionDto.setRestrictionSI(caluculationInputValues
							.get("restrictedSIAmount") != null ? SHAUtils
							.getIntegerFromString(
									(String) caluculationInputValues
											.get("restrictedSIAmount")).toString()
							: "NA");
					
					sumInsuredCalculation(caluculationInputValues, medicalDecisionDto);
				}
				
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				dto.setDiagOrProcedure("Residual Treatment / Procedure Amount");
				dto.setPackageAmt("NA");
				dto.setSubLimitAmount("NA");
				dto.setRestrictionSI("NA");
				
				if(this.bean.getResidualAmountDTO() != null && this.bean.getResidualAmountDTO().getKey() != null) {
					ResidualAmountDTO residualAmountDTO = this.bean.getResidualAmountDTO();
					dto.setAmountConsidered(residualAmountDTO.getAmountConsideredAmount() != null ? residualAmountDTO.getAmountConsideredAmount().intValue() : 0);
					
					dto.setMinimumAmount(residualAmountDTO.getMinimumAmount() != null ? residualAmountDTO.getMinimumAmount().intValue(): 0);
					dto.setCoPayAmount(residualAmountDTO.getCopayAmount() != null ? residualAmountDTO.getCopayAmount().intValue() : 0);
					dto.setRemarks(residualAmountDTO.getRemarks() != null ? residualAmountDTO.getRemarks() : "");
					
					SelectValue value = new SelectValue();
					value.setId(residualAmountDTO.getCopayPercentage() != null ? residualAmountDTO.getCopayPercentage().longValue() : 0l);
					value.setValue(residualAmountDTO.getCopayPercentage() != null ? String.valueOf(residualAmountDTO.getCopayPercentage().intValue())  : "0");
					
					dto.setCoPayPercentage(value);
					dto.setNetAmount(residualAmountDTO.getNetAmount() != null ? residualAmountDTO.getNetAmount().intValue() : 0);
					dto.setNetApprovedAmt(residualAmountDTO.getApprovedAmount() != null ? residualAmountDTO.getApprovedAmount().intValue() : 0);
					dto.setReverseAllocatedAmt(residualAmountDTO.getNetApprovedAmount() != null ? residualAmountDTO.getNetApprovedAmount().intValue() : 0);
					if(isSetZero) {
						dto.setAmountConsidered(0);
						dto.setCoPayAmount(0);
						dto.setNetAmount(0);
						dto.setMinimumAmount(0);
						dto.setNetApprovedAmt(0);
						dto.setReverseAllocatedAmt(0);
					}
					if(dto.getAmountConsidered() == 0 ) {
						dto.setMinimumAmount(0);
					}
				}
				
				this.medicalDecisionTableObj.addBeanToList(dto);
			}
			
			if(bean.getIsNonAllopathic()) {
//				createNonAllopathicFields(bean.getNonAllopathicOriginalAmt(), bean.getNonAllopathicUtilizedAmt());
			}
		} else {
			for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : filledDTO) {
				if(diagnosisProcedureTableDTO.getProcedureDTO() != null) {
					Boolean procedureStatus = SHAUtils.getProcedureStatus(diagnosisProcedureTableDTO.getProcedureDTO());
					if(!procedureStatus) {
						diagnosisProcedureTableDTO.setMinimumAmount(0);
					}
					diagnosisProcedureTableDTO.setIsPaymentAvailable(procedureStatus);
				} else if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null) {
					Boolean diagnosisStatus = SHAUtils.getDiagnosisStatus(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO());
					if(!diagnosisStatus) {
						diagnosisProcedureTableDTO.setMinimumAmount(0);
					}
					diagnosisProcedureTableDTO.setIsPaymentAvailable(diagnosisStatus);
				}
			}
			this.medicalDecisionTableObj.addList(filledDTO);
			if(bean.getIsNonAllopathic()) {
//				createNonAllopathicFields(bean.getNonAllopathicOriginalAmt(), bean.getNonAllopathicUtilizedAmt());
			}
			
		}
		
		if(this.bean.getIsReverseAllocation()) {
//			createReverseRelatedFields();
		}
	}
	
	
	public void sumInsuredCalculation(Map<String, Object> values,DiagnosisProcedureTableDTO dto){
		

		String diagnosis = null;
		if(values.containsKey("diagnosisId")) {
			diagnosis = masterService.getDiagnosis(Long.valueOf((String) values.get("diagnosisId")));
		}
		
		if (dto.getDiagnosisDetailsDTO() != null) {
			dto.getDiagnosisDetailsDTO()
					.setDiagnosis(diagnosis);
		}
		
//		Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRod(preauthDto.getClaimDTO().getKey(), preauthDto.getKey());
//		if(hospitalizationRod != null){
//			values.put("preauthKey", preauthDto.getKey());
//		}else{
			values.put("preauthKey",this.bean.getKey());

//		}
		
		Map<String, Object> medicalDecisionTableValues = dbCalculationService.getMedicalDecisionTableValue(values,bean.getNewIntimationDTO());
		
//		if(values.containsKey(SHAConstants.IS_NON_ALLOPATHIC) && (Boolean)values.get(SHAConstants.IS_NON_ALLOPATHIC)) {
//			Map<String, Double> nonAllopathicAmount = dbCalculationService.getNonAllopathicAmount((Long)values.get(SHAConstants.POLICY_KEY), (Long)values.get(SHAConstants.INSURED_KEY)
//					,(Long)values.get("preauthKey"),"R", (Long)values.get(SHAConstants.CLAIM_KEY));
//			medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT));
//			medicalDecisionTableValues.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, nonAllopathicAmount.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
//		}
		
	   setApprovedAmountForDiagnosisProcedure(dto, medicalDecisionTableValues);
		
		
	}
	
	
	public void setApprovedAmountForDiagnosisProcedure(DiagnosisProcedureTableDTO medicalDecisionDto, Map<String, Object> values){
		
		if(bean.getIsNonAllopathic()) {
			bean.setNonAllopathicOriginalAmt((Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT));
			bean.setNonAllopathicUtilizedAmt((Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
//			createNonAllopathicFields((Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT), (Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
		}
		medicalDecisionDto.setAvailableAmout(((Double) values
				.get("restrictedAvailAmt")).intValue());
		medicalDecisionDto.setUtilizedAmt(((Double) values
				.get("restrictedUtilAmt")).intValue());
		medicalDecisionDto.setSubLimitAmount(((Double) values
				.get("currentSL")).intValue() > 0 ? (String
				.valueOf(((Double) values.get("currentSL"))
						.intValue())) : "NA");
		medicalDecisionDto.setSubLimitUtilAmount(((Double) values
				.get("SLUtilAmt")).intValue());
		medicalDecisionDto.setSubLimitAvaliableAmt(((Double) values
				.get("SLAvailAmt")).intValue());
		medicalDecisionDto
				.setCoPayPercentageValues((List<String>) values
						.get("copay"));

		// need to implement in new medical listener table
		
		if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CARE_DELITE)) {
			Integer subLimitAvaliableAmt = 0;
			Boolean isResidual = false;
			if(medicalDecisionDto.getDiagnosisDetailsDTO() != null && medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName() != null && (medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
				subLimitAvaliableAmt = medicalDecisionDto.getSubLimitAvaliableAmt();
			} else if (medicalDecisionDto.getProcedureDTO() != null && medicalDecisionDto.getProcedureDTO().getSublimitName() != null && (medicalDecisionDto.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || medicalDecisionDto.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
				subLimitAvaliableAmt = medicalDecisionDto.getSubLimitAvaliableAmt();
			} else {
				isResidual = true;
			}
			
			if(!isResidual) {
				Integer entitlementNoOfDays = SHAUtils.getEntitlementNoOfDays(bean.getUploadDocumentDTO());
				Integer availAmt = entitlementNoOfDays * subLimitAvaliableAmt;
				int min = Math.min(SHAUtils.getIntegerFromString(medicalDecisionDto.getSubLimitAmount()) , availAmt);
				medicalDecisionDto.setSubLimitAvaliableAmt(min);
				medicalDecisionDto.setSubLimitUtilAmount(0);
			}
		}
		
		this.medicalDecisionTableObj
				.addBeanToList(medicalDecisionDto);
		
	}
	
	
	
	
private Panel getContentofTemplate(String templateName,ReportDto reportDto,DocumentGenerator docGen) {
		
		
		String filePath = docGen.generatePdfDocument(templateName, reportDto);	
		final String finalFilePath = filePath;
		
		Path p = Paths.get(finalFilePath);
		String fileName = p.getFileName().toString();

		StreamResource.StreamSource s = new StreamResource.StreamSource() {

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
		Panel panel = new Panel();	
		panel.setHeight("100%");
		panel.setContent(e);
		return panel;
	}

 public void calculateTotalForReport(){
	
//	List<BillEntryDetailsDTO> itemIconPropertyId = this.bean.getBillEntryDetailsDTO();
//	//Long netAmount =0l;
//	Double claimedAmount = 0d;
//	Double allowableAmount = 0d;
//	Double nonPayablePdtBased = 0d;
//	Double nonPayableAmount = 0d;
//	Double proportionateDeduction = 0d;
//	Double totalDisallowances = 0d;
//	Double reasonableDeduction = 0d;
//	Double netAmount = 0d;
//	/*Long amount =0l;
//	Long nonPayableAmount =0l;
//	Long payableAmount =0l;*/
//	
//	if(itemIconPropertyId != null && !itemIconPropertyId.isEmpty()){
//		for (BillEntryDetailsDTO billEntryDetailsDTO : itemIconPropertyId) {
//		
//		if(null != billEntryDetailsDTO.getItemValue())
//		{
//			if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent") || billEntryDetailsDTO.getItemName().contains("Total ICU Charges"))))
//					{
//						claimedAmount += billEntryDetailsDTO.getItemValue();
//						if(("Hospital Discount").equalsIgnoreCase(billEntryDetailsDTO.getItemName()))
//						{
//							claimedAmount -= billEntryDetailsDTO.getItemValue();
//						}
//					}
//		}
//		
//		if(null != billEntryDetailsDTO.getAmountAllowableAmount())
//		{
//			//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
//			if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent") || billEntryDetailsDTO.getItemName().contains("Total ICU Charges"))))
//					{
//						allowableAmount += billEntryDetailsDTO.getAmountAllowableAmount();
//					}
//		}
//		
//		if(null != billEntryDetailsDTO.getNonPayableProductBased())
//		{
//			//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
//			if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent") 
//					|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
//					)))
//					{
//						nonPayablePdtBased += billEntryDetailsDTO.getNonPayableProductBased();
//					}
//		}
//		
//		if(null != billEntryDetailsDTO.getNonPayable())
//		{
//			//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
//			if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
//					|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
//					)))
//					{
//						nonPayableAmount += billEntryDetailsDTO.getNonPayable();
//					}
//		}
//		
//		if(null != billEntryDetailsDTO.getProportionateDeduction())
//		{
//			//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
//			if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
//					|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
//					)))
//					{
//						proportionateDeduction += billEntryDetailsDTO.getProportionateDeduction();
//					}
//		}
//		
//		if(null != billEntryDetailsDTO.getReasonableDeduction())
//		{
//			//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
//			if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
//					|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
//					)))
//					{
//						reasonableDeduction += billEntryDetailsDTO.getReasonableDeduction();
//					}
//		}
//		
//		if(null != billEntryDetailsDTO.getTotalDisallowances())
//		{
//			//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
//			/*if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
//					|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
//					)))*/
//			if(!( billEntryDetailsDTO.getItemName().contains("Total Room Rent")
//					|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
//					))
//					{
//						totalDisallowances += billEntryDetailsDTO.getTotalDisallowances();
//					}
//		}
//		if(null != billEntryDetailsDTO.getNetPayableAmount())
//		{
//			//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
//			if(!((billEntryDetailsDTO.getItemName().contains("Sub Total"))))// || billEntryDetailsDTO.getItemName().contains("Total Room Rent"))))
//					{
//						netAmount += billEntryDetailsDTO.getNetPayableAmount();
//					}
//		}
//		
//	  }
//	}
//	this.bean.setTotalClaimedAmt(claimedAmount);
//	this.bean.setToatlNonPayableAmt(reasonableDeduction + nonPayableAmount + proportionateDeduction);
//	this.bean.setTotalApprovedAmt(netAmount);
	 
	 
	 
		List<BillEntryDetailsDTO> itemIconPropertyId = this.bean.getBillEntryDetailsDTO();
		//Long netAmount =0l;
		Double claimedAmount = 0d;
		Double allowableAmount = 0d;
		Double nonPayablePdtBased = 0d;
		Double nonPayableAmount = 0d;
		Double proportionateDeduction = 0d;
		Double totalDisallowances = 0d;
		Double reasonableDeduction = 0d;
		Double netAmount = 0d;
		/*Long amount =0l;
		Long nonPayableAmount =0l;
		Long payableAmount =0l;*/
		
		if(itemIconPropertyId != null && !itemIconPropertyId.isEmpty()){
			for (BillEntryDetailsDTO billEntryDetailsDTO : itemIconPropertyId) {
			
			if(null != billEntryDetailsDTO.getItemValue())
			{
				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent") || billEntryDetailsDTO.getItemName().contains("Total ICU Charges"))))
						{
							claimedAmount += billEntryDetailsDTO.getItemValue();
							if(("Hospital Discount").equalsIgnoreCase(billEntryDetailsDTO.getItemName()))
							{
								claimedAmount -= billEntryDetailsDTO.getItemValue();
							}
						}
			}
			
			if(null != billEntryDetailsDTO.getAmountAllowableAmount())
			{
				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent") || billEntryDetailsDTO.getItemName().contains("Total ICU Charges"))))
						{
							allowableAmount += billEntryDetailsDTO.getAmountAllowableAmount();
						}
			}
			
			if(null != billEntryDetailsDTO.getNonPayableProductBased())
			{
				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent") 
						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
						)))
						{
							nonPayablePdtBased += billEntryDetailsDTO.getNonPayableProductBased();
						}
			}
			
			if(null != billEntryDetailsDTO.getNonPayable())
			{
				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
						)))
						{
							nonPayableAmount += billEntryDetailsDTO.getNonPayable();
						}
			}
			
			if(null != billEntryDetailsDTO.getProportionateDeduction())
			{
				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
						)))
						{
							proportionateDeduction += billEntryDetailsDTO.getProportionateDeduction();
						}
			}
			
			if(null != billEntryDetailsDTO.getReasonableDeduction())
			{
				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
						)))
						{
							reasonableDeduction += billEntryDetailsDTO.getReasonableDeduction();
						}
			}
			
			if(null != billEntryDetailsDTO.getTotalDisallowances())
			{
				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
				/*if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
						)))*/
				if(!( billEntryDetailsDTO.getItemName().contains("Total Room Rent")
						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
						))
						{
							totalDisallowances += billEntryDetailsDTO.getTotalDisallowances();
						}
			}
			if(null != billEntryDetailsDTO.getNetPayableAmount())
			{
				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total"))))// || billEntryDetailsDTO.getItemName().contains("Total Room Rent"))))
						{
							netAmount += billEntryDetailsDTO.getNetPayableAmount();
						}
			}
			
		  }
		}
		this.bean.setTotalClaimedAmt(claimedAmount);
		this.bean.setToatlNonPayableAmt(reasonableDeduction + nonPayableAmount + proportionateDeduction);
		/*
		 * commenting the below line, since the total is already set in view bill assessment ui file
		 * **/
		//this.bean.setTotalApprovedAmt(netAmount);
		this.bean.setAmountTotal(netAmount);
		this.bean.setNonpayableProdTotal(nonPayablePdtBased);
		this.bean.setNonpayableTotal(nonPayableAmount);
		this.bean.setPropDecutTotal(proportionateDeduction);
		this.bean.setReasonableDeducTotal(reasonableDeduction);
		this.bean.setDisallowanceTotal(totalDisallowances);
		this.bean.setNetPayableAmtTotal(netAmount);
	 
	 
	/*table.setColumnFooter("itemValue", String.valueOf(claimedAmount));
	table.setColumnFooter("amountAllowableAmount" , String.valueOf(allowableAmount));
	table.setColumnFooter("nonPayableProductBased" , String.valueOf(nonPayablePdtBased));
	table.setColumnFooter("nonPayable" , String.valueOf(nonPayableAmount));
	table.setColumnFooter("proportionateDeduction"  , String.valueOf(proportionateDeduction));
	table.setColumnFooter("reasonableDeduction" , String.valueOf(reasonableDeduction));
	table.setColumnFooter("totalDisallowances"  , String.valueOf(totalDisallowances));
	table.setColumnFooter("netPayableAmount"  , String.valueOf(netAmount));*/

	/*table.setColumnFooter("amount", String.valueOf(amount));
	table.setColumnFooter("deductingNonPayable", String.valueOf(nonPayableAmount));
	table.setColumnFooter("payableAmount", String.valueOf(payableAmount));*/
	//table.setColumnFooter("itemName", "Total");
	
}
	
//	private void setBillingPreHospitalisationValue(Long rodKey){
//		List<PreHospitalizationDTO> hospitalizationList = new ArrayList<PreHospitalizationDTO>();
//		List<MasBillDetailsType> billDetailsType = createRodService.getBillDetails(ReferenceTable.PRE_HOSPITALIZATION);
//		List<BillingPreHospitalisation> billingPreHospitalisationList = createRodService.getBillingPreHospitalisationList(rodKey);
//		
//		int sno =1;
//		for(int i=0;i<billDetailsType.size();i++){                                          //load all master values initially
//			
//			PreHospitalizationDTO prehoDto = new PreHospitalizationDTO();
//			prehoDto.setSno(sno);
//			
//			if(i==2){
//				PreHospitalizationDTO dummyDto = new PreHospitalizationDTO();
//				dummyDto.setDetails("Medicines & Consumables");
//				dummyDto.setSno(sno);
//				hospitalizationList.add(dummyDto);
//				prehoDto.setSno(null);
//			    prehoDto.setDetails("           a)"+billDetailsType.get(i).getValue());
//			}
//			else if(i==3){
//				prehoDto.setSno(null);
//				prehoDto.setDetails("           b)"+billDetailsType.get(i).getValue());
//			}
//			else{
//				prehoDto.setDetails(billDetailsType.get(i).getValue());
//			}
//			hospitalizationList.add(prehoDto);
//			sno++;
//		}
//		
//       for (BillingPreHospitalisation preHospitalisation2 : billingPreHospitalisationList) {                 //Load all value to corresponding Master Values
//    	   int sequenceNumber =0;
//			for (MasBillDetailsType masBillDetailsType : billDetailsType) {
//				if (preHospitalisation2.getBillTypeNumber() != null) {
//					if (preHospitalisation2.getBillTypeNumber().equals(masBillDetailsType.getKey())) {
//						sequenceNumber = masBillDetailsType.getSequenceNumber().intValue();
//						if (!(sequenceNumber > 2)) {
//							sequenceNumber--;
//						}
//						break;
//					}
//				}
//			}
//
//    	   PreHospitalizationDTO preHospitalDto = hospitalizationList.get(sequenceNumber);
//    	   preHospitalDto.setClaimedAmt(preHospitalisation2.getClaimedAmountBills());
//    	   //Need to check why this non payable is set to 0l by default;
//    	   preHospitalDto.setBillingNonPayable(preHospitalisation2.getNonPayableAmount());
//    	//   preHospitalDto.setBillingNonPayable(0l);
//    	//   preHospitalDto.setNetAmount(preHospitalisation2.getClaimedAmountBills());
//    	   preHospitalDto.setNetAmount(preHospitalisation2.getNetAmount());;
//    	   /**
//    	    * Changes done for new pre hospitalization veiw.
//    	    */
//    	   preHospitalDto.setReasonableDeduction(preHospitalisation2.getDeductibleAmount());
//    	 //  preHospitalDto.setAmount(preHospitalisation2.getClaimedAmountBills());
//    	   //preHospitalDto.setDeductingNonPayable(0l);
//    	  // preHospitalDto.setPayableAmount(preHospitalisation2.getClaimedAmountBills());
//    	   preHospitalDto.setReason(preHospitalisation2.getReason());
//    	   preHospitalDto.setClassificationFlag(SHAConstants.PREHOSPITALIZATION);
//		}
//		
//
//		this.bean.setPrehospitalizationDTO(hospitalizationList);
//		
//		
//
//		
//	}
//	
//private void getBillingPostHospitalizationValue(Long rodKey){
//		
//        List<PreHospitalizationDTO> hospitalizationList = new ArrayList<PreHospitalizationDTO>();
//		List<MasBillDetailsType> billDetailsType = createRodService.getBillDetails(ReferenceTable.POST_HOSPITALIZATION);
//		List<BillingPostHospitalisation> preHospitalisation = createRodService.getBillingPostHospitalisationList(rodKey);
//		int sno =1;
//		for(int i=0;i<billDetailsType.size();i++){                                          //load all master values initially
//			
//			PreHospitalizationDTO prehoDto = new PreHospitalizationDTO();
//			prehoDto.setSno(sno);
//			if(i==2){
//				PreHospitalizationDTO dummyDto = new PreHospitalizationDTO();
//				dummyDto.setDetails("Medicines & Consumables");
//				dummyDto.setSno(sno);
//				prehoDto.setSno(null);
//				hospitalizationList.add(dummyDto);
//			    prehoDto.setDetails("           a)"+billDetailsType.get(i).getValue());
//			}
//			else if(i==3){
//				prehoDto.setSno(null);
//				prehoDto.setDetails("           b)"+billDetailsType.get(i).getValue());
//			}
//			else{
//				prehoDto.setDetails(billDetailsType.get(i).getValue());
//			}
//			hospitalizationList.add(prehoDto);
//			sno++;
//		}
//		
//       for (BillingPostHospitalisation preHospitalisation2 : preHospitalisation) {                 //Load all value to corresponding Master Values
//    	   int sequenceNumber =0;
//    	   for (MasBillDetailsType masBillDetailsType : billDetailsType) {
//    		   if(preHospitalisation2.getBillTypeNumber() != null){
//   			             if(preHospitalisation2.getBillTypeNumber().equals(masBillDetailsType.getKey())){
//   			            	 sequenceNumber = masBillDetailsType.getSequenceNumber().intValue();
//   			            	 if(!(sequenceNumber>2)){
//   			            	 sequenceNumber--;
//   			            	 }
//   			            	 break;
//   			             }
//    		   }
//   			 }
//    	   
//    	   //Need to check why this pre hos
//    	   
//    	   PreHospitalizationDTO preHospitalDto = hospitalizationList.get(sequenceNumber);
//    	   preHospitalDto.setClaimedAmt(preHospitalisation2.getClaimedAmountBills());
//    	   //preHospitalDto.setBillingNonPayable(0l);
//    	   preHospitalDto.setBillingNonPayable(preHospitalisation2.getNonPayableAmount());
//    	   preHospitalDto.setReasonableDeduction(preHospitalisation2.getDeductibleAmount());
//    	 //  preHospitalDto.setNetAmount(preHospitalisation2.getClaimedAmountBills());
//    	   preHospitalDto.setNetAmount(preHospitalisation2.getNetAmount());
//    	  // preHospitalDto.setAmount(preHospitalisation2.getClaimedAmountBills());         //Need to implements
//    	   //preHospitalDto.setDeductingNonPayable(preHospitalisation2.getDeductibleAmount());  //Need to implements 
//    	   //preHospitalDto.setDeductingNonPayable(0l);
//    	   //preHospitalDto.setPayableAmount(preHospitalisation2.getClaimedAmountBills());
//    	   preHospitalDto.setReason(preHospitalisation2.getReason());
//    	   preHospitalDto.setClassificationFlag(SHAConstants.POSTHOSPITALIZATION);
// 
//    	   }
//		
//			this.bean.setPostHospitalizationDTO(hospitalizationList);
//	}

}

