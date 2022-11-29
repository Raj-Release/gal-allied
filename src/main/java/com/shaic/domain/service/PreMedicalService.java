package com.shaic.domain.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.mapper.PreauthMapper;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.claim.premedical.dto.OtherBenefitsTableDto;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.domain.AutoAllocationCancelRemarks;
import com.shaic.domain.Claim;
import com.shaic.domain.DocUploadToPremia;
import com.shaic.domain.GpaPolicy;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasCpuLimit;
import com.shaic.domain.MasProductCpuRouting;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.PaayasPolicy;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyNominee;
import com.shaic.domain.Product;
import com.shaic.domain.ProposerNominee;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.StarJioPolicy;
import com.shaic.domain.StarKotakPolicy;
import com.shaic.domain.Status;
import com.shaic.domain.TataPolicy;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.ZUAQueryHistoryTable;
import com.shaic.domain.preauth.BenefitAmountDetails;
import com.shaic.domain.preauth.ClaimAmountDetails;
import com.shaic.domain.preauth.ClaimLimit;
import com.shaic.domain.preauth.Coordinator;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.DiagnosisPED;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.IcdCode;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.PreauthQuery;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.ResidualAmount;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.preauth.StageInformation;
import com.shaic.domain.preauth.StarFaxReverseFeed;
import com.shaic.domain.preauth.UpdateOtherClaimDetails;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.vaadin.v7.data.util.BeanItemContainer;

@Stateless
public class PreMedicalService  {

	@PersistenceContext
	protected EntityManager entityManager;
	
	 private final Logger log = LoggerFactory.getLogger(PreMedicalService.class);

	 String current_q = null;
	
	public PreMedicalService() {
		super();
	}
	
	public Claim searchByClaimKey(Long a_key) {
		Claim find = entityManager.find(Claim.class, a_key);
		entityManager.refresh(find);
		return find;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Preauth submitPreMedical(PreauthDTO preauthDTO, Boolean isPremedicalPreauth) {
		try{
			log.info("**** SUBMIT PREMEDICAL ------> " + (preauthDTO.getNewIntimationDTO() != null ? preauthDTO.getNewIntimationDTO().getIntimationId() : "NO INTIMATIION"));
			
			Preauth preauth = new Preauth();
			/*if(null != preauthDTO.getNewIntimationDTO().getIsTataPolicy() && preauthDTO.getNewIntimationDTO().getIsTataPolicy()){
				preauth = savePreauthValues(preauthDTO, isPremedicalPreauth);
			}
			else
			{
				preauth = saveTataTrustPreauthValues(preauthDTO, isPremedicalPreauth);
			}*/
			preauth = savePreauthValues(preauthDTO, isPremedicalPreauth);
			
			
			if(!isPremedicalPreauth) {
				ResidualAmount residualAmt = new ResidualAmount();
			  	residualAmt.setTransactionKey(preauth.getKey());
			  	residualAmt.setStage(preauth.getStage());
			  	residualAmt.setStatus(preauth.getStatus());
			  	residualAmt.setApprovedAmount(preauthDTO.getResidualAmountDTO().getApprovedAmount());
			  	residualAmt.setRemarks(preauthDTO.getResidualAmountDTO().getRemarks());
			  	if(preauthDTO.getResidualAmountDTO().getCoPayTypeId() != null){
			  		MastersValue copayType = new MastersValue();
			  		copayType.setKey(preauthDTO.getResidualAmountDTO().getCoPayTypeId().getId());
			  		copayType.setValue(preauthDTO.getResidualAmountDTO().getCoPayTypeId().getValue());
			  		residualAmt.setCoPayTypeId(copayType);
			  	}
			  	 entityManager.persist(residualAmt);				
				 entityManager.flush();
				 entityManager.clear();
				 log.info("------ResidualAmount------>"+residualAmt+"<------------");

			}
			
//			setBPMOutcome(preauthDTO, preauth, isPremedicalPreauth);
	//		setDBWorkFlowObject(preauth,preauthDTO,isPremedicalPreauth);
			return preauth;
		} catch(Exception e) {
			e.printStackTrace();
			log.error(e.toString());
//			Notification.show("Already Submitted. Please Try Another Record.");
		}
		
		return null;
		
	}


	public Preauth savePreauthValues(PreauthDTO preauthDTO, Boolean isRePremedicalPreauth) {
		
		
		
		if(null != preauthDTO.getPreauthDataExtractionDetails() && null != preauthDTO.getPreauthDataExtractionDetails().getEnhancementTreatmentRemarks() 
				&& !("").equals(preauthDTO.getPreauthDataExtractionDetails().getEnhancementTreatmentRemarks()))
		{
			preauthDTO.getPreauthDataExtractionDetails().setTreatmentRemarks(preauthDTO.getPreauthDataExtractionDetails().getEnhancementTreatmentRemarks());
		}
		
		if(preauthDTO.getStageKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE)){
			preauthDTO.getPreauthDataExtractionDetails().setTreatmentRemarks(preauthDTO.getPreauthDataExtractionDetails().getEnhancementTreatmentRemarks());
		}
		
		PreMedicalMapper preMedicalMapper=PreMedicalMapper.getInstance();
//		PreMedicalMapper.getAllMapValues();
		Preauth preauth = preMedicalMapper.getPreauth(preauthDTO);
		if (preauthDTO != null && preauthDTO.getPreauthDataExtractionDetails() != null 
				&& preauthDTO.getPreauthDataExtractionDetails().getCategory() !=null 
				&& preauthDTO.getPreauthDataExtractionDetails().getCategory().getValue() != null) {
		preauth.setCategory(preauthDTO.getPreauthDataExtractionDetails().getCategory().getValue());
		}
		preauth.setDateOfDischarge(preauthDTO.getPreauthDataExtractionDetails().getDischargeDate());
		preauth.setActiveStatus(1l);
		if(null != preauthDTO.getPreauthDataExtractionDetails().getCatastrophicLoss()){
			preauth.setCatastrophicLoss(preauthDTO.getPreauthDataExtractionDetails().getCatastrophicLoss().getId());
		}
		if(null != preauthDTO.getPreauthDataExtractionDetails().getNatureOfLoss()){
			preauth.setNatureOfLoss(preauthDTO.getPreauthDataExtractionDetails().getNatureOfLoss().getId());
		}
		if(null != preauthDTO.getPreauthDataExtractionDetails().getCauseOfLoss()) {
			preauth.setCauseOfLoss(preauthDTO.getPreauthDataExtractionDetails().getCauseOfLoss().getId());
		}
		if(null != preauthDTO.getSfxMatchedQDate())
			preauth.setSfxMatchedQDate(preauthDTO.getSfxMatchedQDate());
		if(null != preauthDTO.getSfxRegisteredQDate())
			preauth.setSfxRegisteredQDate(preauthDTO.getSfxRegisteredQDate());
		
		if(preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS) || preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS)) {
			preauth.setRemarks(preauthDTO.getPreauthMedicalProcessingDetails().getRejectionRemarks());
		
			/*if((preauth.getPatientStatus() != null
					&& ((ReferenceTable.PATIENT_STATUS_DECEASED).equals(preauth.getPatientStatus().getKey()) 
											|| (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB).equals(preauth.getPatientStatus().getKey())))) { 
				
				if(preauthDTO.getNewIntimationDTO().getNomineeList() != null &&
						!preauthDTO.getNewIntimationDTO().getNomineeList().isEmpty()){
					saveNomineeDetails(preauthDTO.getNewIntimationDTO().getNomineeList(), preauth);
				}					
			}*/
		
		} else if (preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS) || preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS) ) {
			preauth.setRemarks(preauthDTO.getPreauthMedicalProcessingDetails().getMedicalRemarks());
			
			/*if((preauth.getPatientStatus() != null
					&& ((ReferenceTable.PATIENT_STATUS_DECEASED).equals(preauth.getPatientStatus().getKey()) 
											|| (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB).equals(preauth.getPatientStatus().getKey())))) { 
				
				if(preauthDTO.getNewIntimationDTO().getNomineeList() != null &&
						!preauthDTO.getNewIntimationDTO().getNomineeList().isEmpty()){
					saveNomineeDetails(preauthDTO.getNewIntimationDTO().getNomineeList(), preauth);
				}					
			}*/
			
		} else if (preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS) || preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS) ) {
			preauth.setRemarks(preauthDTO.getPreauthMedicalProcessingDetails().getQueryRemarks());
			
			/*if((preauth.getPatientStatus() != null
					&& ((ReferenceTable.PATIENT_STATUS_DECEASED).equals(preauth.getPatientStatus().getKey()) 
											|| (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB).equals(preauth.getPatientStatus().getKey())))) { 
				
				if(preauthDTO.getNewIntimationDTO().getNomineeList() != null &&
						!preauthDTO.getNewIntimationDTO().getNomineeList().isEmpty()){
					saveNomineeDetails(preauthDTO.getNewIntimationDTO().getNomineeList(), preauth);
				}					
			}*/
		}
		
		
		Claim currentClaim = null;
		
		if(preauth.getClaim() != null) {
			currentClaim = searchByClaimKey(preauth.getClaim().getKey());
			// As per Prakash and Satish request commented stage and status updates on Claim...
//			currentClaim.setStatus(preauth.getStatus());
//			currentClaim.setStage(preauth.getStage());
			if(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration() != null && preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().equalsIgnoreCase(SHAConstants.AUTO_RESTORATION_DONE)) {
				currentClaim.setAutoRestroationFlag(SHAConstants.YES_FLAG);
			}
			currentClaim.setDataOfAdmission(preauth.getDataOfAdmission());
			currentClaim.setDataOfDischarge(preauth.getDateOfDischarge());
			currentClaim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			
			// SECTION IMPLEMENTED FOR COMPREHENSIVE AND UPDATED THE SECTION VALUES IN CLAIM LEVEL...
			if(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null) {
				currentClaim.setClaimSectionCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue());
				currentClaim.setClaimCoverCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getCover().getCommonValue());
				currentClaim.setClaimSubCoverCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue());
			}
			currentClaim.setIncidenceFlag(preauthDTO.getPreauthDataExtractionDetails().getAccidentOrDeathFlag());
			currentClaim.setIncidenceDate(preauthDTO.getPreauthDataExtractionDetails().getDateOfDeathAcc());
			
			if(null != preauthDTO.getPreauthDataExtractionDetails().getDateOfAccident())
			{
				currentClaim.setAccidentDate(preauthDTO.getPreauthDataExtractionDetails().getDateOfAccident());
			}
			
			if(null != preauthDTO.getPreauthDataExtractionDetails().getDateOfDeath())
			{
				currentClaim.setDeathDate(preauthDTO.getPreauthDataExtractionDetails().getDateOfDeath());
			}
			
			if(null != preauthDTO.getPreauthDataExtractionDetails().getDateOfDisablement())
			{
				currentClaim.setDisablementDate(preauthDTO.getPreauthDataExtractionDetails().getDateOfDisablement());
			}
			
			if(preauthDTO.getStatusKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS) || 
					preauthDTO.getStatusKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS)){
				currentClaim.setPaHospExpenseAmt(0d);
				
				/*if((preauth.getPatientStatus() != null
						&& ((ReferenceTable.PATIENT_STATUS_DECEASED).equals(preauth.getPatientStatus().getKey()) 
												|| (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB).equals(preauth.getPatientStatus().getKey())))) { 
					
					if(preauthDTO.getNewIntimationDTO().getNomineeList() != null &&
							!preauthDTO.getNewIntimationDTO().getNomineeList().isEmpty()){
						saveNomineeDetails(preauthDTO.getNewIntimationDTO().getNomineeList(), preauth);
					}					
				}*/
			}
			
			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				if(preauthDTO.getPreauthDataExtractionDetails().getCorpBuffer() != null){
					String bufferFlag = preauthDTO.getPreauthDataExtractionDetails().getCorpBuffer() ? "Y":"N";
					currentClaim.setGmcCorpBufferFlag(bufferFlag);
					if(preauthDTO.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim() != null){
						currentClaim.setGmcCorpBufferLmt(preauthDTO.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim().doubleValue());
					}
				}
			}
			
			if(null != currentClaim.getIntimation().getPolicy().getProduct().getKey() &&
					(ReferenceTable.getGPAProducts().equals(currentClaim.getIntimation().getPolicy().getProduct().getKey())))
			{
				
				currentClaim.setGpaParentName(preauthDTO.getPreauthDataExtractionDetails().getParentName());
				currentClaim.setGpaParentDOB(preauthDTO.getPreauthDataExtractionDetails().getDateOfBirth());				
				currentClaim.setGpaParentAge(preauthDTO.getPreauthDataExtractionDetails().getAge());
				currentClaim.setGpaRiskName(preauthDTO.getPreauthDataExtractionDetails().getRiskName());
				currentClaim.setGpaRiskDOB(preauthDTO.getPreauthDataExtractionDetails().getGpaRiskDOB());
				currentClaim.setGpaRiskAge(preauthDTO.getPreauthDataExtractionDetails().getGpaRiskAge());
				
				SelectValue gpaCategory = new SelectValue();
				if(null != preauthDTO.getPreauthDataExtractionDetails().getGpaCategory()){
					gpaCategory = preauthDTO.getPreauthDataExtractionDetails().getGpaCategory();
					
					if(null != gpaCategory.getValue()){
						
						String[] splitCategory = gpaCategory.getValue().split("-");
						String category = splitCategory[0];
						if(null != category && !("null").equalsIgnoreCase(category)){
						currentClaim.setGpaCategory(category);
						}
					}
					
					
				}
												
				currentClaim.setGpaSection(preauthDTO.getPreauthDataExtractionDetails().getGpaSection());
			}
			
			if(currentClaim.getClaimRegisteredDate() == null){
				currentClaim.setClaimRegisteredDate((new Timestamp(System
						.currentTimeMillis())));
			}
			if(currentClaim.getDocumentReceivedDate() == null){
				currentClaim.setDocumentReceivedDate((new Timestamp(System
						.currentTimeMillis())));
			}

			entityManager.merge(currentClaim);
			entityManager.flush();
			entityManager.clear();
			log.info("------Claim------>"+currentClaim+"<------------");
			
			preauth.setClaim(currentClaim);
		}
		
		
		if(preauthDTO.getPreauthPreviousClaimsDetails().getAttachToPreviousClaim() != null) {
			Claim searchByClaimKey = searchByClaimKey(preauthDTO.getPreauthPreviousClaimsDetails().getAttachToPreviousClaim().getId());
			searchByClaimKey.setClaimLink(preauthDTO.getClaimKey());
			entityManager.merge(searchByClaimKey);
			entityManager.flush();
			entityManager.clear();
		}
		
		if(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration() != null) {
			if(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().toLowerCase().contains("n/a")) {
				preauth.setAutoRestoration("o");
			} else {
				preauth.setAutoRestoration(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().toLowerCase().contains("not") ? "N" : "Y");
			}
		}
		
		if(null != preauthDTO && null != preauthDTO.getPreauthDataExtractionDetails() && 
				null != preauthDTO.getPreauthDataExtractionDetails().getWorkOrNonWorkPlaceFlag()){
			preauth.setWorkPlace(preauthDTO.getPreauthDataExtractionDetails().getWorkOrNonWorkPlaceFlag());
		}
		
		preauth.setKey(null);
		if(preauthDTO.getPreauthDataExtractionDetails() != null && preauthDTO.getPreauthDataExtractionDetails().getPreAuthType() != null
				&& preauthDTO.getPreauthDataExtractionDetails().getPreAuthType().getValue() != null) {
			if( preauthDTO.getPreauthDataExtractionDetails().getPreAuthType().getValue().equalsIgnoreCase(ReferenceTable.ONLY_PRE_AUTH)) {
			
					preauth.setStpProcessLevel(ReferenceTable.ONLY_PRE_AUTH_KEY);
//					entityManager.merge(preauth);
				
			}else if(preauthDTO.getPreauthDataExtractionDetails().getPreAuthType().getValue().equalsIgnoreCase(ReferenceTable.PRE_AUTH_WITH_FINAL_BILL)) {
					preauth.setStpProcessLevel(ReferenceTable.PRE_AUTH_WITH_FINAL_BILL_KEY);
//					entityManager.merge(preauth);
			}
		}
		/**
		 * Insured key is a new column added in IMS_CLS_CASHLESS TABLE
		 * during policy refractoring activity. Below code is added for inserting
		 * value in the insured key column.
		 * */
	
		//added for FLP auto allocation user ID capture issue on 28-02-2020
		
		if (preauthDTO.getIsPreauthAutoAllocationQ()) {
			String strUserName = preauthDTO.getStrUserName();
			strUserName = SHAUtils.getUserNameForDB(strUserName);
			preauth.setFlpUserID(strUserName);
			preauth.setFLPSubmitDate(new Timestamp(System.currentTimeMillis()));
		}
		
//		Portal Flag updated in cashless table
		if(preauthDTO.getNhpUpdKey() != null){
			preauth.setNhpUpdDocKey(preauthDTO.getNhpUpdKey());
		}
		
		preauth.setInsuredKey(preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
		if(preauth.getKey()!=null) {
			preauth.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			String strUserName = preauthDTO.getStrUserName();
			strUserName = SHAUtils.getUserNameForDB(strUserName);
			preauth.setModifiedBy(strUserName);
			entityManager.merge(preauth);
		}
		else {
			preauth.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			String strUserName = preauthDTO.getStrUserName();
			strUserName = SHAUtils.getUserNameForDB(strUserName);
			preauth.setCreatedBy(strUserName);
			entityManager.persist(preauth);
			
		}
		
		
		entityManager.flush();
		entityManager.clear();
		log.info("------Preauth------>"+preauth+"<------------");
		
		preauthDTO.setKey(preauth.getKey());
		
		preauthDTO.getCoordinatorDetails().setPreauthKey(preauth.getKey());
		preauthDTO.getCoordinatorDetails().setIntimationKey(preauth.getIntimation().getKey());
		preauthDTO.getCoordinatorDetails().setPolicyKey(preauth.getPolicy().getKey());
		
		if(preauthDTO.getCoordinatorDetails().getRefertoCoordinatorFlag().toLowerCase().equalsIgnoreCase("y")) {
			Coordinator coordinator = preMedicalMapper.getCoordinator(preauthDTO.getCoordinatorDetails()); 
			
			coordinator.setActiveStatus(1l);
			coordinator.setStage(preauth.getStage());
			coordinator.setStatus(preauth.getStatus());
			coordinator.setClaim(preauth.getClaim());
			coordinator.setTransactionFlag("C");
			coordinator.setTransactionKey(preauth.getKey());
			coordinator.setKey(null);
			if(coordinator.getKey()!=null) {
				entityManager.merge(coordinator);
			} else {
				entityManager.persist(coordinator);
			}
			entityManager.flush();
			entityManager.clear();
			log.info("------Coordinator------>"+coordinator+"<------------");
			preauthDTO.getCoordinatorDetails().setKey(coordinator.getKey());
		}
		
		List<SpecialityDTO> specialityDTOList = preauthDTO.getPreauthDataExtractionDetails().getSpecialityList();
		if(!specialityDTOList.isEmpty()) {
			StringBuffer specialityName = new StringBuffer();
			for (SpecialityDTO specialityDTO : specialityDTOList) {
				if(specialityDTO.getSpecialityType() != null && specialityDTO.getSpecialityType().getValue() != null){
					specialityName.append(specialityDTO.getSpecialityType().getValue()).append(",");
				}
				Speciality speciality = preMedicalMapper.getSpeciality(specialityDTO);
				//speciality.setPreauth(preauth);
				speciality.setClaim(preauth.getClaim());
				speciality.setStage(preauth.getStage());
				speciality.setStatus(preauth.getStatus());
//				speciality.setKey(null);

				if (speciality.getKey() != null) {
					entityManager.merge(speciality);
				} else {
					entityManager.persist(speciality);
					specialityDTO.setKey(speciality.getKey());
				}
				log.info("------Speciality------>"+speciality+"<------------");
			}
			entityManager.flush();
			entityManager.clear();
			preauthDTO.setSpecialityName(specialityName.toString());
		}
		
//		entityManager.clear();
		List<ProcedureDTO> procedureList = preauthDTO.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
		if(!procedureList.isEmpty()) {
			for (ProcedureDTO procedureDTO : procedureList) {
				Procedure procedure = preMedicalMapper.getProcedure(procedureDTO);
				procedure.setDeleteFlag(1l);
				procedure.setTransactionKey(preauth.getKey());
				procedure.setStage(preauth.getStage());
				procedure.setStatus(preauth.getStatus());
				procedure.setKey(null);
				if(!preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS) && !preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)) {
					if(procedureDTO.getCopay() != null) {
						procedure.setCopayPercentage(Double.valueOf(procedureDTO.getCopay().getValue()));
					}
					
				}
				if (procedure.getKey() != null) {
					entityManager.merge(procedure);
				} else {
					entityManager.persist(procedure);
					procedureDTO.setKey(procedure.getKey());
				}
				log.info("------Procedure------>"+procedure+"<------------");
			}
			entityManager.flush();
			entityManager.clear();
		}
		
		if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) && procedureList != null && procedureList.isEmpty()){
			List<ProcedureDTO> procedureList2 = preauthDTO.getPreauthDataExtractionDetails().getProcedureList();
			
			if(procedureList2 != null && ! procedureList2.isEmpty()){
				for (ProcedureDTO procedureDTO : procedureList2) {
					Procedure procedure = preMedicalMapper.getProcedure(procedureDTO);
					procedure.setDeleteFlag(1l);
					procedure.setTransactionKey(preauth.getKey());
					procedure.setStage(preauth.getStage());
					procedure.setStatus(preauth.getStatus());
					procedure.setKey(null);
					if(!preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS) && !preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)) {
						if(procedureDTO.getCopay() != null) {
							procedure.setCopayPercentage(Double.valueOf(procedureDTO.getCopay().getValue()));
						}
						
					}
					if (procedure.getKey() != null) {
						entityManager.merge(procedure);
					} else {
						entityManager.persist(procedure);
						procedureDTO.setKey(procedure.getKey());
					}
					log.info("------Procedure------>"+procedure+"<------------");
				}
			}
			
			
		}
		
		
		List<DiagnosisDetailsTableDTO> pedValidationTableList = preauthDTO.getPreauthDataExtractionDetails().getDiagnosisTableList();
		//Iterate pedValidationTable List.
			if(!pedValidationTableList.isEmpty()) {
			for (DiagnosisDetailsTableDTO pedValidationDTO : pedValidationTableList) {
				
				//Method to insert into MAS Diagnosis.
				//saveToMasterDiagnosis(pedValidationDTO);
			
				DiagnosisDetailsTableDTO pedValidationDTOWithCodes = setIcdChapterBlock(pedValidationDTO);
				
				PedValidation pedValidation = preMedicalMapper.getPedValidation(pedValidationDTOWithCodes);		
				pedValidation.setDeleteFlag(1l);
				pedValidation.setTransactionKey(preauth.getKey());
				pedValidation.setIntimation(preauth.getIntimation());
				pedValidation.setPolicy(preauth.getPolicy());
				pedValidation.setStage(preauth.getStage());
				pedValidation.setStatus(preauth.getStatus());
				pedValidation.setKey(null);
				
				if(!preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS) && !preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)) {
					List<PedDetailsTableDTO> pedList = pedValidationDTO.getPedList();
					for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
						if(pedDetailsTableDTO.getCopay() != null) {
							pedValidation.setCopayPercentage(Double.valueOf(pedDetailsTableDTO.getCopay().getValue()));
							break;
						}
					}
				}
				
				if(pedValidationDTO.getPrimaryDiagnosis()!=null && pedValidationDTO.getPrimaryDiagnosis())
				{
					pedValidation.setPrimaryDiagnosis("Y");
				}
				else
				{
					pedValidation.setPrimaryDiagnosis("N");
				}
				
				
				if (pedValidation.getKey() != null) {
					entityManager.merge(pedValidation);
				} else {
					entityManager.persist(pedValidation);
					pedValidationDTO.setKey(pedValidation.getKey());
				}
				entityManager.flush();
				entityManager.clear();
				log.info("------PedValidation------>"+pedValidation+"<------------");
				List<PedDetailsTableDTO> pedList = pedValidationDTO.getPedList();
				if(!pedList.isEmpty()) {
					for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
							DiagnosisPED diagnosisPED = preMedicalMapper.getDiagnosisPED(pedDetailsTableDTO);
							diagnosisPED.setPedValidation(pedValidation);
							if(pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null) {
								MastersValue value = new MastersValue();
								value.setKey(pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis().getId() : null);value.setKey(pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis().getId() : null);
								value.setValue(pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis().getValue() : null);
								diagnosisPED.setDiagonsisImpact(value);
							}
							if(pedDetailsTableDTO.getExclusionDetails() != null) {
								ExclusionDetails exclusionValue = new ExclusionDetails();
								exclusionValue.setKey(pedDetailsTableDTO.getExclusionDetails() != null ? pedDetailsTableDTO.getExclusionDetails().getId() : null);exclusionValue.setKey(pedDetailsTableDTO.getExclusionDetails() != null ? pedDetailsTableDTO.getExclusionDetails().getId() : null);
								exclusionValue.setExclusion(pedDetailsTableDTO.getExclusionDetails() != null ? pedDetailsTableDTO.getExclusionDetails().getValue() : null);
								diagnosisPED.setExclusionDetails(exclusionValue);
							}
							diagnosisPED.setDiagnosisRemarks(pedDetailsTableDTO.getRemarks());
							diagnosisPED.setKey(null);
							if (diagnosisPED.getKey() != null) {
								entityManager.merge(diagnosisPED);
							} else {
								entityManager.persist(diagnosisPED);
								pedDetailsTableDTO.setKey(diagnosisPED.getKey());
							}
							entityManager.flush();
							entityManager.clear();
							log.info("------DiagnosisPED------>"+diagnosisPED+"<------------");
					}
				}
			}
			
		}
			
			
		
		
		
		List<NoOfDaysCell> claimedDetailsList = preauthDTO.getPreauthDataExtractionDetails().getClaimedDetailsList();
		if(!claimedDetailsList.isEmpty()) {
			for (NoOfDaysCell noOfDaysCell : claimedDetailsList) {
				ClaimAmountDetails claimedAmountDetails = preMedicalMapper.getClaimedAmountDetails(noOfDaysCell);
				claimedAmountDetails.setPreauth(preauth);
				claimedAmountDetails.setActiveStatus(1l);
				claimedAmountDetails.setStage(preauth.getStage());
				claimedAmountDetails.setStatus(preauth.getStatus());
				claimedAmountDetails.setInsuredKey(preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
				claimedAmountDetails.setKey(null);
				if (claimedAmountDetails.getKey() != null) {
					entityManager.merge(claimedAmountDetails);
				} else {
					entityManager.persist(claimedAmountDetails);
					noOfDaysCell.setKey(claimedAmountDetails.getKey());
				}
				log.info("------ClaimAmountDetails------>"+claimedAmountDetails+"<------------");
			}
			entityManager.flush();
			entityManager.clear();
		}
		
		if (preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS) || preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS)) {
			if(preauth.getKey()!=null){
				PreauthQuery preAuthQuery = new PreauthQuery();
				preAuthQuery.setPreauth(preauth);
				preAuthQuery.setStage(preauth.getStage());
				preAuthQuery.setStatus(preauth.getStatus());
				preAuthQuery.setQueryRemarks(preauthDTO.getPreauthMedicalProcessingDetails().getQueryRemarks());
				String strUserName = preauthDTO.getStrUserName();
				strUserName = SHAUtils.getUserNameForDB(strUserName);
				preAuthQuery.setCreatedBy(strUserName);
				entityManager.persist(preAuthQuery);
				log.info("------PreauthQuery------>"+preAuthQuery+"<------------");
			}
			entityManager.flush();
			entityManager.clear();
		}
		
		List<DiagnosisDetailsTableDTO> deletedDiagnosis = preauthDTO.getDeletedDiagnosis();
		
		if(deletedDiagnosis != null && ! deletedDiagnosis.isEmpty()){
			for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : deletedDiagnosis) {
				PedValidation pedValidation = preMedicalMapper.getPedValidation(diagnosisDetailsTableDTO);		
				pedValidation.setDeleteFlag(0l);
				pedValidation.setTransactionKey(preauth.getKey());
				if(pedValidation.getKey() != null){
					entityManager.merge(pedValidation);
				}
				entityManager.flush();
				entityManager.clear();
				log.info("------PedValidation------>"+pedValidation+"<------------");
			}
		}
		
		List<ProcedureDTO> deletedProcedure = preauthDTO.getDeletedProcedure();
		if(deletedProcedure != null && ! deletedProcedure.isEmpty()){
			for (ProcedureDTO procedureDTO : deletedProcedure) {
				Procedure procedure = preMedicalMapper.getProcedure(procedureDTO);
				procedure.setDeleteFlag(0l);
				procedure.setTransactionKey(preauth.getKey());
				if(procedure.getKey() != null){
					entityManager.merge(procedure);
				}
				entityManager.flush();
				entityManager.clear();
				log.info("------Procedure------>"+procedure+"<------------");
			}
		}
		
		Product product = preauthDTO.getNewIntimationDTO().getPolicy().getProduct();
		if(product.getCode() != null &&  (product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
				preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G"))) {
			
			List<UpdateOtherClaimDetailDTO> updateOtherClaimDetailDTO = preauthDTO.getUpdateOtherClaimDetailDTO();
			if(!updateOtherClaimDetailDTO.isEmpty()){
				List<UpdateOtherClaimDetails> updateOtherClaimDetails = preMedicalMapper.getUpdateOtherClaimDetails(updateOtherClaimDetailDTO);
				for (UpdateOtherClaimDetails updateOtherClaimDetails2 : updateOtherClaimDetails) {
					updateOtherClaimDetails2.setCashlessKey(preauth.getKey());
					updateOtherClaimDetails2.setClaimKey(currentClaim.getKey());
					updateOtherClaimDetails2.setStage(preauth.getStage());
//					updateOtherClaimDetails2.setIntimationId(currentClaim.getIntimation().getIntimationId());
					updateOtherClaimDetails2.setIntimationKey(currentClaim.getIntimation().getKey());
					updateOtherClaimDetails2.setStatus(preauth.getStatus());
					updateOtherClaimDetails2.setClaimType(currentClaim.getClaimType().getValue());
					updateOtherClaimDetails2.setKey(null);
					if(updateOtherClaimDetails2.getKey() != null){
						entityManager.merge(updateOtherClaimDetails2);
						entityManager.flush();
					}else{
						entityManager.persist(updateOtherClaimDetails2);
						entityManager.flush();
					}
				}
			}
		}
			
		if(ReferenceTable.getFHORevisedKeys().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())
				|| ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			preauthDTO.setKey(preauth.getKey());
			saveBenefitAmountDetails(preauthDTO);
		}
		
		/*
		if(null != preauthDTO && null != preauthDTO.getNewIntimationDTO().getKey()){
			
			Intimation intimationObj = getIntimationByKey(preauthDTO.getNewIntimationDTO().getKey());
			if(null != intimationObj){
				
				intimationObj.setPaParentName(preauthDTO.getPreauthDataExtractionDetails().getParentName());
				intimationObj.setPaParentDOB(preauthDTO.getPreauthDataExtractionDetails().getDateOfBirth());
				intimationObj.setPaPatientName(preauthDTO.getPreauthDataExtractionDetails().getRiskName());
				intimationObj.setPaParentAge(preauthDTO.getPreauthDataExtractionDetails().getAge());
				
				if(null != intimationObj.getKey()){
				
					entityManager.merge(intimationObj);
					entityManager.flush();
				}else{
					entityManager.persist(intimationObj);
					entityManager.flush();
				}					
				
			}
			
			
		}*/
		
		if(null != preauthDTO.getDocFilePath() && !("").equalsIgnoreCase(preauthDTO.getDocFilePath()))
		{
			WeakHashMap dataMap = new WeakHashMap();
			dataMap.put("intimationNumber",currentClaim.getIntimation().getIntimationId());
			dataMap.put("claimNumber",currentClaim.getClaimId());
			if(null != currentClaim.getClaimType())
			{
				if((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(currentClaim.getClaimType().getKey()))
					{
						//Preauth preauthObj = SHAUtils.getPreauthClaimKey(entityManager , currentClaim.getKey());
						Preauth preauthObj = getLatestPreauthByClaimKey( currentClaim.getKey());
						if(null != preauthObj)
							dataMap.put("cashlessNumber", preauthObj.getPreauthId());
					}
			}
			dataMap.put("filePath", preauthDTO.getDocFilePath());
			dataMap.put("docType", preauthDTO.getDocType());
			dataMap.put("docSources", SHAConstants.PRE_MEDICAL_PRE_AUTH);
			dataMap.put("createdBy", preauthDTO.getStrUserName());
			String docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
			
			/**
			 * The below code was added to update the document token column
			 * in query table, if an query has been raised at preauth level.
			 * */
			
			if(null != preauth && null != preauth.getStatus() && ((ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS).equals(preauth.getStatus().getKey()) || 
					(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS).equals(preauth.getStatus().getKey())))
			{
				PreauthQuery preauthQuery = getPreauthQueryList(preauth.getKey());
				if(null != preauthQuery && null != docToken)
				{
					preauthQuery.setDocumentToken(Long.valueOf(docToken));
					entityManager.merge(preauthQuery);
					entityManager.flush();
					entityManager.clear();
					log.info("------PreauthQuery------>"+preauthQuery+"<------------");
				}
			}
		}
		if(preauth.getPolicy().getKey() != null) {
			Policy policyDetails = getPolicyDetails(preauth.getPolicy().getKey());
			if(policyDetails != null && preauthDTO.getVbCheckStatusFlag() != null) {
				policyDetails.setChequeStatus(preauthDTO.getVbCheckStatusFlag());
				entityManager.merge(policyDetails);
				entityManager.flush();
				entityManager.clear();
			}
		}
		
		/*//IMSSUPPOR-29030
		if((preauth.getPatientStatus() != null
				&& ((ReferenceTable.PATIENT_STATUS_DECEASED).equals(preauth.getPatientStatus().getKey()) 
										|| (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB).equals(preauth.getPatientStatus().getKey())))
				&& preauthDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(preauthDTO.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) { 
			
			if(preauthDTO.getNewIntimationDTO().getNomineeList() != null &&
					!preauthDTO.getNewIntimationDTO().getNomineeList().isEmpty()){
				saveNomineeDetails(preauthDTO.getNewIntimationDTO().getNomineeList(), preauth);
			}					
		}*/
		
		return preauth;
	}
	
	public PreauthQuery getPreauthQueryList(Long preauthKey) {
		Query findByKey = entityManager.createNamedQuery(
				"PreauthQuery.findBypreauth").setParameter("preAuthPrimaryKey",
				preauthKey);

		List<PreauthQuery> preauthList = (List<PreauthQuery>) findByKey
				.getResultList();

		if (!preauthList.isEmpty()) {
			return preauthList.get(0);

		}
		return null;
	}

	public BenefitAmountDetails getBenefitAmountDetailsByKey(Long benefitKey) {
		try{
			Query findByKey = entityManager.createNamedQuery(
					"BenefitAmountDetails.findByKey").setParameter("benefitKey",
							benefitKey);

			List<BenefitAmountDetails> benefitList = (List<BenefitAmountDetails>) findByKey
					.getResultList();

			if (benefitList != null && !benefitList.isEmpty()) {
				return benefitList.get(0);

			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<OtherBenefitsTableDto> getBenefitAmountDetailsByCashlessKey(Long preauthKey){

		List<OtherBenefitsTableDto> resultList = new ArrayList<OtherBenefitsTableDto>();
		try{
			Query findByPreauthKey = entityManager.createNamedQuery(
					"BenefitAmountDetails.findByPreauthKey").setParameter("preauthKey",
							preauthKey);

			List<BenefitAmountDetails> benefitList = (List<BenefitAmountDetails>) findByPreauthKey
					.getResultList();

			if (benefitList != null && !benefitList.isEmpty()) {
				OtherBenefitsTableDto benefitDto = null;
				for (BenefitAmountDetails benefitAmountDetails : benefitList) {
					benefitDto = new OtherBenefitsTableDto(benefitAmountDetails);
					resultList.add(benefitDto);
				}				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return resultList;
	}
	
	/*private void  saveToMasterDiagnosis(DiagnosisDetailsTableDTO pedValidation)
	{
		if(null == pedValidation.getDiagnosisId())
		{
			Diagnosis diagnosis = new Diagnosis();
			diagnosis.setValue(pedValidation.getPedName());
			diagnosis.setActiveStatus(1l);
			
			entityManager.persist(diagnosis);
			entityManager.flush();
			entityManager.clear();
			pedValidation.setDiagnosisId(diagnosis.getKey());
			pedValidation.setDiagnosis(diagnosis.getValue());
			pedValidation.getDiagnosisName().setId(diagnosis.getKey());
			pedValidation.getDiagnosisName().setValue(diagnosis.getValue());
		}
		
	}*/
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public SelectValue  saveMasterDiagnosis(String diagnosisName)
	{
		/*StringBuffer diagnosisNames = new StringBuffer();
		Query findDiagnosisName = entityManager.createNamedQuery(
				"Diagnosis.findByName").setParameter("diagnosisString",
						diagnosisName);
		List<Diagnosis> diagnosisList = findDiagnosisName.getResultList();
		if(null == diagnosisList && diagnosisList.isEmpty())
		{*/
			Diagnosis diagnosis = new Diagnosis();
			diagnosis.setValue(diagnosisName);
			diagnosis.setActiveStatus(1l);
//			diagnosis.setVersion(1l);
			diagnosis.setCreatedBy("DBA");
			entityManager.persist(diagnosis);
			entityManager.flush();
			entityManager.clear();
			log.info("------Diagnosis------>"+diagnosis+"<------------");
			SelectValue selectValue = new SelectValue();
			selectValue.setId(diagnosis.getKey());
			selectValue.setValue(diagnosis.getValue());
			return selectValue;
			//entityManager.refresh(diagnosis);
			//return diagnosis.getKey();
		//}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public BeanItemContainer<SelectValue>  getMasterDiagnosisValue(String diagnosisName)
	{
		/*StringBuffer diagnosisNames = new StringBuffer();
		Query findDiagnosisName = entityManager.createNamedQuery(
				"Diagnosis.findByName").setParameter("diagnosisString",
						diagnosisName);
		List<Diagnosis> diagnosisList = findDiagnosisName.getResultList();
		if(null == diagnosisList && diagnosisList.isEmpty())
		{*/
			Diagnosis diagnosis = new Diagnosis();
			diagnosis.setValue(diagnosisName);
			diagnosis.setActiveStatus(1l);
//			diagnosis.setVersion(1l);
			diagnosis.setCreatedBy("DBA");
			entityManager.persist(diagnosis);
			entityManager.flush();
			log.info("------Diagnosis------>"+diagnosis+"<------------");
			
			Query findDiagnosisName = entityManager.createNamedQuery(
					"Diagnosis.findByName").setParameter("diagnosisString",
							diagnosisName);
			List<Diagnosis> diagnosisList = findDiagnosisName.getResultList();
			List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
			BeanItemContainer<SelectValue> diagContainer = new BeanItemContainer<SelectValue>(
					SelectValue.class);
			for (Diagnosis objDiagnosis : diagnosisList) {
				SelectValue select = new SelectValue();
				select.setId((objDiagnosis.getKey()));
				select.setValue(objDiagnosis.getValue());
				selectValuesList.add(select);
			}
			diagContainer.addAll(selectValuesList);
			return diagContainer; 
			/*SelectValue selectValue = new SelectValue();
			selectValue.setId(diagnosis.getKey());
			selectValue.setValue(diagnosis.getValue());
			return selectValue;*/
			//entityManager.refresh(diagnosis);
			//return diagnosis.getKey();
		//}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Preauth saveDuplicatePreauthValues(PreauthDTO preauthDTO,String processType) {
		PreauthMapper preMedicalMapper = PreauthMapper.getInstance();
//		PreauthMapper.getAllMapValues();
		Preauth preauth = preMedicalMapper.getPreauth(preauthDTO);
		preauth.setActiveStatus(1l);
	    preauth.setProcessType(processType);
		
		if(preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS) || preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS)) {
			preauth.setRemarks(preauthDTO.getPreauthMedicalProcessingDetails().getRejectionRemarks());
		} else if (preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS) || preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS) ) {
			
			preauth.setRemarks(preauthDTO.getPreauthMedicalProcessingDetails().getMedicalRemarks());
			
		}
		
		if(preauthDTO.getPreauthPreviousClaimsDetails().getAttachToPreviousClaim() != null) {
			Claim searchByClaimKey = searchByClaimKey(preauthDTO.getPreauthPreviousClaimsDetails().getAttachToPreviousClaim().getId());
			searchByClaimKey.setClaimLink(preauthDTO.getClaimKey());
			entityManager.merge(searchByClaimKey);
			entityManager.flush();
			entityManager.clear();
			log.info("------Claim------>"+searchByClaimKey+"<------------");
		}
		
		preauth.setAutoRestoration(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().toLowerCase().contains("not") ? "N" : "Y");
		preauth.setKey(null);
		if(preauth.getKey()!=null) {
			entityManager.merge(preauth);
		}
		else {
			
			entityManager.persist(preauth);
			
		}
		entityManager.flush();
		entityManager.clear();
		log.info("------Preauth------>"+preauth+"<------------");
		preauthDTO.setKey(preauth.getKey());
		
		preauthDTO.getCoordinatorDetails().setPreauthKey(preauth.getKey());
		preauthDTO.getCoordinatorDetails().setIntimationKey(preauth.getIntimation().getKey());
		preauthDTO.getCoordinatorDetails().setPolicyKey(preauth.getPolicy().getKey());
		
		if(preauthDTO.getCoordinatorDetails().getRefertoCoordinatorFlag().toLowerCase().equalsIgnoreCase("y")) {
			Coordinator coordinator = preMedicalMapper.getCoordinator(preauthDTO.getCoordinatorDetails()); 
			
			coordinator.setActiveStatus(1l);
			coordinator.setStage(preauth.getStage());
			coordinator.setStatus(preauth.getStatus());

		//	coordinator.setPreauth(preauth);
			coordinator.setClaim(preauth.getClaim());

			coordinator.setKey(null);
			if(coordinator.getKey()!=null) {
				entityManager.merge(coordinator);
			} else {
				entityManager.persist(coordinator);
			}
			entityManager.flush();
			entityManager.clear();
			log.info("------Coordinator------>"+coordinator+"<------------");
			preauthDTO.getCoordinatorDetails().setKey(coordinator.getKey());
		}
		
		List<SpecialityDTO> specialityDTOList = preauthDTO.getPreauthDataExtractionDetails().getSpecialityList();
		if(!specialityDTOList.isEmpty()) {
			for (SpecialityDTO specialityDTO : specialityDTOList) {
				Speciality speciality = preMedicalMapper.getSpeciality(specialityDTO);
				//speciality.setPreauth(preauth);
				speciality.setClaim(preauth.getClaim());
				speciality.setStage(preauth.getStage());
				speciality.setStatus(preauth.getStatus());
				speciality.setKey(null);
				if (speciality.getKey() != null) {
					entityManager.merge(speciality);
				} else {
					entityManager.persist(speciality);
					specialityDTO.setKey(speciality.getKey());
				}
				log.info("------Speciality------>"+speciality+"<------------");
			}
			entityManager.flush();
		}
		
		entityManager.clear();
		List<ProcedureDTO> procedureList = preauthDTO.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
		if(!procedureList.isEmpty()) {
			for (ProcedureDTO procedureDTO : procedureList) {
				Procedure procedure = preMedicalMapper.getProcedure(procedureDTO);
				procedure.setTransactionKey(preauth.getKey());
				procedure.setStage(preauth.getStage());
				procedure.setStatus(preauth.getStatus());
				procedure.setKey(null);
				if (procedure.getKey() != null) {
					entityManager.merge(procedure);
				} else {
					entityManager.persist(procedure);
					procedureDTO.setKey(procedure.getKey());
				}
				log.info("------Procedure------>"+procedure+"<------------");
			}
			entityManager.flush();
			entityManager.clear();
		}
		
		List<DiagnosisDetailsTableDTO> pedValidationTableList = preauthDTO.getPreauthDataExtractionDetails().getDiagnosisTableList();
			if(!pedValidationTableList.isEmpty()) {
			for (DiagnosisDetailsTableDTO pedValidationDTO : pedValidationTableList) {
				PedValidation pedValidation = preMedicalMapper.getPedValidation(pedValidationDTO);
				pedValidation.setTransactionKey(preauth.getKey());
				pedValidation.setIntimation(preauth.getIntimation());
				pedValidation.setPolicy(preauth.getPolicy());
				pedValidation.setStage(preauth.getStage());
				pedValidation.setStatus(preauth.getStatus());
				pedValidation.setKey(null);
				if (pedValidation.getKey() != null) {
					entityManager.merge(pedValidation);
				} else {
					entityManager.persist(pedValidation);
					pedValidationDTO.setKey(pedValidation.getKey());
				}
				entityManager.flush();
				entityManager.clear();
				log.info("------PedValidation------>"+pedValidation+"<------------");
				List<PedDetailsTableDTO> pedList = pedValidationDTO.getPedList();
				if(!pedList.isEmpty()) {
					for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
							DiagnosisPED diagnosisPED = preMedicalMapper.getDiagnosisPED(pedDetailsTableDTO);
							diagnosisPED.setPedValidation(pedValidation);
							if(pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null) {
								MastersValue value = new MastersValue();
								value.setKey(pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis().getId() : null);value.setKey(pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis().getId() : null);
								value.setValue(pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis().getValue() : null);
								diagnosisPED.setDiagonsisImpact(value);
								
							}
							if(pedDetailsTableDTO.getExclusionDetails() != null) {
								ExclusionDetails exclusionValue = new ExclusionDetails();
								exclusionValue.setKey(pedDetailsTableDTO.getExclusionDetails() != null ? pedDetailsTableDTO.getExclusionDetails().getId() : null);exclusionValue.setKey(pedDetailsTableDTO.getExclusionDetails() != null ? pedDetailsTableDTO.getExclusionDetails().getId() : null);
								exclusionValue.setExclusion(pedDetailsTableDTO.getExclusionDetails() != null ? pedDetailsTableDTO.getExclusionDetails().getValue() : null);
								diagnosisPED.setExclusionDetails(exclusionValue);
							}
							diagnosisPED.setDiagnosisRemarks(pedDetailsTableDTO.getRemarks());
							diagnosisPED.setKey(null);
							if (diagnosisPED.getKey() != null) {
								entityManager.merge(diagnosisPED);
							} else {
								entityManager.persist(diagnosisPED);
								pedDetailsTableDTO.setKey(diagnosisPED.getKey());
							}
							entityManager.flush();
							entityManager.clear();
							log.info("------DiagnosisPED------>"+diagnosisPED+"<------------");
					}
				}
			}
			
		}
			
		List<NoOfDaysCell> claimedDetailsList = preauthDTO.getPreauthDataExtractionDetails().getClaimedDetailsList();
		if(!claimedDetailsList.isEmpty()) {
			for (NoOfDaysCell noOfDaysCell : claimedDetailsList) {
				ClaimAmountDetails claimedAmountDetails = preMedicalMapper.getClaimedAmountDetails(noOfDaysCell);
				claimedAmountDetails.setPreauth(preauth);
				claimedAmountDetails.setKey(null);
				if (claimedAmountDetails.getKey() != null) {
					entityManager.merge(claimedAmountDetails);
				} else {
					entityManager.persist(claimedAmountDetails);
					noOfDaysCell.setKey(claimedAmountDetails.getKey());
				}
				log.info("------ClaimAmountDetails------>"+claimedAmountDetails+"<------------");
			}
			entityManager.flush();
			entityManager.clear();
		}
		
		if (preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS) || preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS)) {
			if(preauth.getKey()!=null){
				PreauthQuery preAuthQuery = new PreauthQuery();
				preAuthQuery.setPreauth(preauth);
				preAuthQuery.setStage(preauth.getStage());
				preAuthQuery.setStatus(preauth.getStatus());
				preAuthQuery.setQueryRemarks(preauthDTO.getPreauthMedicalProcessingDetails().getQueryRemarks());
				String strUserName = preauthDTO.getStrUserName();
				strUserName = SHAUtils.getUserNameForDB(strUserName);
				preAuthQuery.setCreatedBy(strUserName);
				entityManager.persist(preAuthQuery);
				log.info("------PreauthQuery------>"+preAuthQuery+"<------------");
			}
			entityManager.flush();
			entityManager.clear();
		}
		
		if(preauth.getKey()!=null){
//			IntimationType intMsg = new IntimationType();
//			if(preauth.getStatus()!=null){
//			intMsg.setStatus(preauth.getStatus().getProcessValue());
//			}
//			if(preauth.getClaim()!=null){
//				if(preauth.getClaim().getClaimType()!=null){
//					intMsg.setClaimType(preauth.getClaim().getClaimType().getValue());
//				}
//			}	
//			if(preauth.getIntimation()!=null){
//			intMsg.setIntimationNumber(preauth.getIntimation().getIntimationId());
//			if(preauth.getIntimation().getHospitalType()!=null){
//			intMsg.setHospitalType(preauth.getIntimation().getHospitalType().getValue());
//			}
//			intMsg.setKey(preauth.getIntimation().getKey());
//			}
			
		//	PreAuthReqType preAuthReq = new PreAuthReqType();
//			PreAuthReqDetails preAuthReqDetails = new PreAuthReqDetails();
//			preAuthReqDetails.setPolicyId(preauth.getPolicy().getPolicyNumber());
//			preAuthReq.setPreAuthReqDetails(preAuthReqDetails);
			//preAuthReq.setKey(preauth.getKey());
//			BPMClientContext.executeforWithdraw(null, null,intMsg, preAuthReq,null);
		}
		return preauth;
	}
	
	@SuppressWarnings("unchecked")
	public ClaimAmountDetails getClaimAmountDetails(Long claimAmountDetailsKey) {
		Query query = entityManager.createNamedQuery("ClaimAmountDetails.findByKey");
		query.setParameter("claimAmountDetailsKey", claimAmountDetailsKey);
		ClaimAmountDetails singleResult = (ClaimAmountDetails) query.getSingleResult();
		return singleResult;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Preauth updatePreMedical(PreauthDTO preauthDTO, Boolean isPremedicalPreauth) {
		try{
			log.info("**** UPDATE PREMEDICAL ------> " + (preauthDTO.getNewIntimationDTO() != null ? preauthDTO.getNewIntimationDTO().getIntimationId() : "NO INTIMATIION"));
			Preauth preauth = updatePreauthValues(preauthDTO);
			
			if(!isPremedicalPreauth) {
				ResidualAmount residualAmt = new ResidualAmount();
			  	residualAmt.setTransactionKey(preauth.getKey());
			  	residualAmt.setStage(preauth.getStage());
			  	residualAmt.setStatus(preauth.getStatus());
			  	residualAmt.setApprovedAmount(preauthDTO.getResidualAmountDTO().getApprovedAmount());
			  	residualAmt.setRemarks(preauthDTO.getResidualAmountDTO().getRemarks());
			  	 entityManager.persist(residualAmt);
			  	log.info("------ResidualAmount------>"+residualAmt+"<------------");
				 entityManager.flush();
				 entityManager.clear();
			}
			
//			setBPMOutcome(preauthDTO, preauth, isPremedicalPreauth);
		//	setDBWorkFlowObject(preauth,preauthDTO,isPremedicalPreauth);
			return preauth;
		} catch(Exception e) {
			e.printStackTrace();
			log.error(e.toString());
//			Notification.show("Already Submitted. Please Try Another Record.");
		}
		return null;
		
	}

	public void saveBenefitAmountDetails(PreauthDTO preauthDTO) {
		
		List<OtherBenefitsTableDto> benefitsList = preauthDTO.getPreauthDataExtractionDetails().getOtherBenefitsList();
		
		if(benefitsList != null && !benefitsList.isEmpty()){
			BenefitAmountDetails benefitObj = null;
			for (OtherBenefitsTableDto otherBenefitsTableDto : benefitsList) {
//				if(SHAConstants.YES.equalsIgnoreCase(otherBenefitsTableDto.getApplicable().getValue())){
					
					if(otherBenefitsTableDto.getBenefitKey() == null){
						benefitObj = new BenefitAmountDetails();
						benefitObj.setCreatedBy(preauthDTO.getCreatedBy());
						benefitObj.setCreatedDate(preauthDTO.getCreateDate());
						benefitObj.setBenefit(new  ClaimLimit());
						benefitObj.getBenefit().setKey(otherBenefitsTableDto.getBenefitObjId());
					}
					else{
						benefitObj = getBenefitAmountDetailsByKey(otherBenefitsTableDto.getBenefitKey());
						benefitObj.getBenefit().setKey(otherBenefitsTableDto.getBenefitObjId());
						benefitObj.setModifiedBy(preauthDTO.getModifiedBy());
						benefitObj.setModifiedDate(preauthDTO.getCreateDate());
					}
					benefitObj.setTransactionKey(preauthDTO.getKey());
					benefitObj.setApplicable(SHAConstants.No.equalsIgnoreCase(otherBenefitsTableDto.getApplicable().getValue()) ? "N" : "Y");
					
					benefitObj.setDeletedFlag(SHAConstants.YES_FLAG.equalsIgnoreCase(benefitObj.getApplicable()) ? "N" : "Y");
					if(preauthDTO.getPreauthDataExtractionDetails().getOtherBenfitOpt() != null && ! preauthDTO.getPreauthDataExtractionDetails().getOtherBenfitOpt()){
						benefitObj.setDeletedFlag("Y");
					}
					benefitObj.setClaimedAmt(otherBenefitsTableDto.getAmtClaimed() );
					benefitObj.setNonPayableAmt(otherBenefitsTableDto.getNonPayable());
					benefitObj.setNetAmt(otherBenefitsTableDto.getNetPayable());
					benefitObj.setEligibleAmt(otherBenefitsTableDto.getEligibleAmt());
					benefitObj.setPayableAmt(otherBenefitsTableDto.getApprovedAmt());
					benefitObj.setRemarks(otherBenefitsTableDto.getRemarks());
					benefitObj.setSeqNo(String.valueOf(otherBenefitsTableDto.getSno()));
					
					if(otherBenefitsTableDto.getBenefitKey() == null){
						benefitObj.setClaim(searchByClaimKey(preauthDTO.getClaimDTO().getKey()));
						benefitObj.setInsured(preauthDTO.getNewIntimationDTO().getInsuredPatient());
						benefitObj.setStage(new Stage());
						benefitObj.getStage().setKey(preauthDTO.getStageKey());
						benefitObj.setStatus(new Status());
						benefitObj.getStatus().setKey(preauthDTO.getStatusKey());
						benefitObj.setOfficeCode(preauthDTO.getNewIntimationDTO().getOrganizationUnit().getOrganizationUnitId());
						benefitObj.setActiveStatus(1l);
						entityManager.persist(benefitObj);
					}
					else{						
						benefitObj.setStage(new Stage());
						benefitObj.getStage().setKey(preauthDTO.getStageKey());
						benefitObj.setStatus(new Status());
						benefitObj.getStatus().setKey(preauthDTO.getStatusKey());
						entityManager.merge(benefitObj);
					}
//				}		
			}
		}
	}
	
	public Preauth updatePreauthValues(PreauthDTO preauthDTO) {
		PreMedicalMapper preMedicalMapper=PreMedicalMapper.getInstance();
//		PreMedicalMapper.getAllMapValues();
		Preauth preauth = preMedicalMapper.getPreauth(preauthDTO);
		preauth.setActiveStatus(1l);

		if(null != preauthDTO.getPreauthDataExtractionDetails().getCatastrophicLoss()){
			preauth.setCatastrophicLoss(preauthDTO.getPreauthDataExtractionDetails().getCatastrophicLoss().getId());
		}
		if(null != preauthDTO.getPreauthDataExtractionDetails().getNatureOfLoss()){
			preauth.setNatureOfLoss(preauthDTO.getPreauthDataExtractionDetails().getNatureOfLoss().getId());
		}
		if(null != preauthDTO.getPreauthDataExtractionDetails().getCauseOfLoss()) {
			preauth.setCauseOfLoss(preauthDTO.getPreauthDataExtractionDetails().getCauseOfLoss().getId());
		}
		if (preauthDTO != null && preauthDTO.getPreauthDataExtractionDetails() != null 
				&& preauthDTO.getPreauthDataExtractionDetails().getCategory() !=null 
				&& preauthDTO.getPreauthDataExtractionDetails().getCategory().getValue() != null) {
		preauth.setCategory(preauthDTO.getPreauthDataExtractionDetails().getCategory().getValue());
		}
		if(null != preauthDTO.getSfxMatchedQDate())
			preauth.setSfxMatchedQDate(preauthDTO.getSfxMatchedQDate());
		if(null != preauthDTO.getSfxRegisteredQDate())
			preauth.setSfxRegisteredQDate(preauthDTO.getSfxRegisteredQDate());
		
		if(preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS) || preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS)) {
			preauth.setRemarks(preauthDTO.getPreauthMedicalProcessingDetails().getRejectionRemarks());
		} else if (preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS) || preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS) ) {
			preauth.setRemarks(preauthDTO.getPreauthMedicalProcessingDetails().getMedicalRemarks());
		}
		
		Claim currentClaim = null;
		
		if(preauth.getClaim() != null) {
			currentClaim = searchByClaimKey(preauth.getClaim().getKey());
//			currentClaim.setStatus(preauth.getStatus());
//			currentClaim.setStage(preauth.getStage());
			if(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration() != null && preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().equalsIgnoreCase(SHAConstants.AUTO_RESTORATION_DONE)) {
				currentClaim.setAutoRestroationFlag(SHAConstants.YES_FLAG);
			}
			currentClaim.setDataOfAdmission(preauth.getDataOfAdmission());
			currentClaim.setDataOfDischarge(preauth.getDateOfDischarge());
			
			// SECTION IMPLEMENTED FOR COMPREHENSIVE AND UPDATED THE SECTION VALUES IN CLAIM LEVEL...
			if(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null) {
				currentClaim.setClaimSectionCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue());
				currentClaim.setClaimCoverCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getCover().getCommonValue());
				currentClaim.setClaimSubCoverCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue());
			}
			
			if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				if(preauthDTO.getPreauthDataExtractionDetails().getCorpBuffer() != null){
					String bufferFlag = preauthDTO.getPreauthDataExtractionDetails().getCorpBuffer() ? "Y":"N";
					currentClaim.setGmcCorpBufferFlag(bufferFlag);
					if(preauthDTO.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim() != null){
						currentClaim.setGmcCorpBufferLmt(preauthDTO.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim().doubleValue());
					}
					
					
				}
			}
			
			currentClaim.setIncidenceFlag(preauthDTO.getPreauthDataExtractionDetails().getAccidentOrDeathFlag());
			currentClaim.setIncidenceDate(preauthDTO.getPreauthDataExtractionDetails().getDateOfDeathAcc());
			
			if(null != preauthDTO.getPreauthDataExtractionDetails().getDateOfAccident())
			{
				currentClaim.setAccidentDate(preauthDTO.getPreauthDataExtractionDetails().getDateOfAccident());
			}
			
			if(null != preauthDTO.getPreauthDataExtractionDetails().getDateOfDeath())
			{
				currentClaim.setDeathDate(preauthDTO.getPreauthDataExtractionDetails().getDateOfDeath());
			}
			
			if(null != preauthDTO.getPreauthDataExtractionDetails().getDateOfDisablement())
			{
				currentClaim.setDisablementDate(preauthDTO.getPreauthDataExtractionDetails().getDateOfDisablement());
			}
			entityManager.merge(currentClaim);
			entityManager.flush();
			entityManager.clear();
			log.info("------Claim------>"+currentClaim+"<------------");
			preauth.setClaim(currentClaim);
		}
		
		
		if(preauthDTO.getPreauthPreviousClaimsDetails().getAttachToPreviousClaim() != null) {
			Claim searchByClaimKey = searchByClaimKey(preauthDTO.getPreauthPreviousClaimsDetails().getAttachToPreviousClaim().getId());
			searchByClaimKey.setClaimLink(preauthDTO.getClaimKey());
			entityManager.merge(searchByClaimKey);
			entityManager.flush();
			entityManager.clear();
			log.info("------Claim------>"+searchByClaimKey+"<------------");
		}
		
		if(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration() != null) {
			if(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().toLowerCase().contains("n/a")) {
				preauth.setAutoRestoration("o");
			} else {
				preauth.setAutoRestoration(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().toLowerCase().contains("not") ? "N" : "Y");
			}
		}
		if(preauthDTO.getPreauthDataExtractionDetails() != null && preauthDTO.getPreauthDataExtractionDetails().getPreAuthType() != null
				&& preauthDTO.getPreauthDataExtractionDetails().getPreAuthType().getValue() != null) {
			if( preauthDTO.getPreauthDataExtractionDetails().getPreAuthType().getValue().equalsIgnoreCase(ReferenceTable.ONLY_PRE_AUTH)) {
			
					preauth.setStpProcessLevel(ReferenceTable.ONLY_PRE_AUTH_KEY);
				
			}else if(preauthDTO.getPreauthDataExtractionDetails().getPreAuthType().getValue().equalsIgnoreCase(ReferenceTable.PRE_AUTH_WITH_FINAL_BILL)) {
					preauth.setStpProcessLevel(ReferenceTable.PRE_AUTH_WITH_FINAL_BILL_KEY);
			}
		}
		
		if(preauthDTO.getPreauthDataExtractionDetails() != null && preauthDTO.getPreauthDataExtractionDetails().getPreAuthType() != null
				&& preauthDTO.getPreauthDataExtractionDetails().getPreAuthType().getValue() != null) {
			if( preauthDTO.getPreauthDataExtractionDetails().getPreAuthType().getValue().equalsIgnoreCase(ReferenceTable.ONLY_PRE_AUTH)) {
			
					preauth.setStpProcessLevel(ReferenceTable.ONLY_PRE_AUTH_KEY);
//					entityManager.merge(preauth);
				
			}else if(preauthDTO.getPreauthDataExtractionDetails().getPreAuthType().getValue().equalsIgnoreCase(ReferenceTable.PRE_AUTH_WITH_FINAL_BILL)) {
					preauth.setStpProcessLevel(ReferenceTable.PRE_AUTH_WITH_FINAL_BILL_KEY);
//					entityManager.merge(preauth);
			}
		}
		
		//added for FLP auto allocation user ID capture issue on 28-02-2020

		if (preauthDTO.getIsPreauthAutoAllocationQ()) {
			String strUserName = preauthDTO.getStrUserName();
			strUserName = SHAUtils.getUserNameForDB(strUserName);
			preauth.setFlpUserID(strUserName);
			preauth.setFLPSubmitDate(new Timestamp(System.currentTimeMillis()));
		}
		
//		Portal Flag updated in cashless table
		if(preauthDTO.getNhpUpdKey() != null){
			preauth.setNhpUpdDocKey(preauthDTO.getNhpUpdKey());
		}
		
		/**
		 * Insured key is a new column added in IMS_CLS_CASHLESS TABLE
		 * during policy refractoring activity. Below code is added for inserting
		 * value in the insured key column.
		 * */
		preauth.setInsuredKey(preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
		if(preauth.getKey()!=null) {
			preauth.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			String strUserName = preauthDTO.getStrUserName();
			strUserName = SHAUtils.getUserNameForDB(strUserName);
			preauth.setModifiedBy(strUserName);
			//added for cheque status batch to run once again if claim is referred to FLP
			preauth.setBatchChequeFlag(null);
			preauth.setBatchChequeRunDate(null);
			entityManager.merge(preauth);
		}
		else {
			preauth.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			String strUserName = preauthDTO.getStrUserName();
			strUserName = SHAUtils.getUserNameForDB(strUserName);
			preauth.setCreatedBy(strUserName);
			entityManager.persist(preauth);
			
		}
		entityManager.flush();
		entityManager.clear();
		log.info("------Preauth------>"+preauth+"<------------");
		
		preauthDTO.setKey(preauth.getKey());
		
		preauthDTO.getCoordinatorDetails().setPreauthKey(preauth.getKey());
		preauthDTO.getCoordinatorDetails().setIntimationKey(preauth.getIntimation().getKey());
		preauthDTO.getCoordinatorDetails().setPolicyKey(preauth.getPolicy().getKey());
		
		if(preauthDTO.getCoordinatorDetails().getRefertoCoordinatorFlag().toLowerCase().equalsIgnoreCase("y")) {
			Coordinator coordinator = preMedicalMapper.getCoordinator(preauthDTO.getCoordinatorDetails()); 
			String strUserName = preauthDTO.getStrUserName();
			strUserName = SHAUtils.getUserNameForDB(strUserName);
			coordinator.setCreatedBy(strUserName);
			coordinator.setActiveStatus(1l);
			coordinator.setStage(preauth.getStage());
			coordinator.setStatus(preauth.getStatus());
			coordinator.setClaim(preauth.getClaim());
			coordinator.setTransactionFlag("C");
			coordinator.setTransactionKey(preauth.getKey());
			coordinator.setKey(null);
			
			if(coordinator.getKey()!=null) {
				entityManager.merge(coordinator);
			} else {
				entityManager.persist(coordinator);
			}
			entityManager.flush();
			entityManager.clear();
			log.info("------Coordinator------>"+coordinator+"<------------");
			preauthDTO.getCoordinatorDetails().setKey(coordinator.getKey());
		}
		
		List<SpecialityDTO> specialityDTOList = preauthDTO.getPreauthDataExtractionDetails().getSpecialityList();
		if(!specialityDTOList.isEmpty()) {
			for (SpecialityDTO specialityDTO : specialityDTOList) {
				Speciality speciality = preMedicalMapper.getSpeciality(specialityDTO);
				//speciality.setPreauth(preauth);
				speciality.setClaim(preauth.getClaim());
				speciality.setStage(preauth.getStage());
				speciality.setStatus(preauth.getStatus());

				if (speciality.getKey() != null) {
					entityManager.merge(speciality);
				} else {
					entityManager.persist(speciality);
					specialityDTO.setKey(speciality.getKey());
				}
				log.info("------Speciality------>"+speciality+"<------------");
			}
			entityManager.flush();
			entityManager.clear();
		}
		
		entityManager.clear();
		List<ProcedureDTO> procedureList = preauthDTO.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
		if(!procedureList.isEmpty()) {
			for (ProcedureDTO procedureDTO : procedureList) {
				Procedure procedure = preMedicalMapper.getProcedure(procedureDTO);
				procedure.setDeleteFlag(1l);
				procedure.setTransactionKey(preauth.getKey());
				procedure.setStage(preauth.getStage());
				procedure.setStatus(preauth.getStatus());
				if(!preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS) && !preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)) {
					if(procedureDTO.getCopay() != null) {
						procedure.setCopayPercentage(Double.valueOf(procedureDTO.getCopay().getValue()));
					}
					
				}
				if (procedure.getKey() != null) {
					entityManager.merge(procedure);
				} else {
					entityManager.persist(procedure);
					procedureDTO.setKey(procedure.getKey());
				}
				log.info("------Procedure------>"+procedure+"<------------");
			}
			entityManager.flush();
			entityManager.clear();
		}
		
		List<DiagnosisDetailsTableDTO> pedValidationTableList = preauthDTO.getPreauthDataExtractionDetails().getDiagnosisTableList();
		//Iterate pedValidationTable List.
			if(!pedValidationTableList.isEmpty()) {
			for (DiagnosisDetailsTableDTO pedValidationDTO : pedValidationTableList) {
				
				//Method to insert into MAS Diagnosis.
				//saveToMasterDiagnosis(pedValidationDTO);
				
				PedValidation pedValidation = preMedicalMapper.getPedValidation(pedValidationDTO);		
				pedValidation.setDeleteFlag(1l);
				pedValidation.setTransactionKey(preauth.getKey());
				pedValidation.setIntimation(preauth.getIntimation());
				pedValidation.setPolicy(preauth.getPolicy());
				pedValidation.setStage(preauth.getStage());
				pedValidation.setStatus(preauth.getStatus());
				
				if(!preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS) && !preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)) {
					List<PedDetailsTableDTO> pedList = pedValidationDTO.getPedList();
					for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
						if(pedDetailsTableDTO.getCopay() != null) {
							pedValidation.setCopayPercentage(Double.valueOf(pedDetailsTableDTO.getCopay().getValue()));
							break;
						}
					}
				}
				
				if (pedValidation.getKey() != null) {
					entityManager.merge(pedValidation);
				} else {
					entityManager.persist(pedValidation);
					pedValidationDTO.setKey(pedValidation.getKey());
				}
				entityManager.flush();
				entityManager.clear();
				log.info("------PedValidation------>"+pedValidation+"<------------");
				List<PedDetailsTableDTO> pedList = pedValidationDTO.getPedList();
				if(!pedList.isEmpty()) {
					for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
							DiagnosisPED diagnosisPED = preMedicalMapper.getDiagnosisPED(pedDetailsTableDTO);
							diagnosisPED.setPedValidation(pedValidation);
							if(pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null) {
								MastersValue value = new MastersValue();
								value.setKey(pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis().getId() : null);value.setKey(pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis().getId() : null);
								value.setValue(pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis().getValue() : null);
								diagnosisPED.setDiagonsisImpact(value);
							}
							if(pedDetailsTableDTO.getExclusionDetails() != null) {
								ExclusionDetails exclusionValue = new ExclusionDetails();
								exclusionValue.setKey(pedDetailsTableDTO.getExclusionDetails() != null ? pedDetailsTableDTO.getExclusionDetails().getId() : null);exclusionValue.setKey(pedDetailsTableDTO.getExclusionDetails() != null ? pedDetailsTableDTO.getExclusionDetails().getId() : null);
								exclusionValue.setExclusion(pedDetailsTableDTO.getExclusionDetails() != null ? pedDetailsTableDTO.getExclusionDetails().getValue() : null);
								diagnosisPED.setExclusionDetails(exclusionValue);
							}
							diagnosisPED.setDiagnosisRemarks(pedDetailsTableDTO.getRemarks());
							if (diagnosisPED.getKey() != null) {
								entityManager.merge(diagnosisPED);
							} else {
								entityManager.persist(diagnosisPED);
								pedDetailsTableDTO.setKey(diagnosisPED.getKey());
							}
							entityManager.flush();
							entityManager.clear();
							log.info("------DiagnosisPED------>"+diagnosisPED+"<------------");
					}
				}
			}
			
		}
			
		List<NoOfDaysCell> claimedDetailsList = preauthDTO.getPreauthDataExtractionDetails().getClaimedDetailsList();
		if(!claimedDetailsList.isEmpty()) {
			for (NoOfDaysCell noOfDaysCell : claimedDetailsList) {
				ClaimAmountDetails claimedAmountDetails = preMedicalMapper.getClaimedAmountDetails(noOfDaysCell);
				claimedAmountDetails.setPreauth(preauth);
				claimedAmountDetails.setStage(preauth.getStage());
				claimedAmountDetails.setStatus(preauth.getStatus());
				claimedAmountDetails.setActiveStatus(1l);
				claimedAmountDetails.setInsuredKey(preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
				if (claimedAmountDetails.getKey() != null) {
					entityManager.merge(claimedAmountDetails);
				} else {
					entityManager.persist(claimedAmountDetails);
					noOfDaysCell.setKey(claimedAmountDetails.getKey());
				}
				log.info("------ClaimAmountDetails------>"+claimedAmountDetails+"<------------");
			}
			entityManager.flush();
			entityManager.clear();
		}
		
		if(preauthDTO.getDeletedClaimedAmountIds() != null && !preauthDTO.getDeletedClaimedAmountIds().isEmpty()) {
			for (Long claimAmountKey : preauthDTO.getDeletedClaimedAmountIds()) {
				ClaimAmountDetails claimAmountDetails = getClaimAmountDetails(claimAmountKey);
				claimAmountDetails.setActiveStatus(0l);
				if(claimAmountDetails.getKey() != null){
					entityManager.merge(claimAmountDetails);
					log.info("------ClaimAmountDetails------>"+claimAmountDetails+"<------------");
					entityManager.flush();
					entityManager.clear();
				}
			}
		}
		
		if (preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS) || preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS)) {
			if(preauth.getKey()!=null){
				PreauthQuery preAuthQuery = new PreauthQuery();
				preAuthQuery.setPreauth(preauth);
				preAuthQuery.setStage(preauth.getStage());
				preAuthQuery.setStatus(preauth.getStatus());
				preAuthQuery.setQueryRemarks(preauthDTO.getPreauthMedicalProcessingDetails().getQueryRemarks());
				String strUserName = preauthDTO.getStrUserName();
			    strUserName = SHAUtils.getUserNameForDB(strUserName);
			    preAuthQuery.setCreatedBy(strUserName);
				entityManager.persist(preAuthQuery);
				log.info("------PreauthQuery------>"+preAuthQuery+"<------------");
			}
			entityManager.flush();
			entityManager.clear();
		}
		
		List<DiagnosisDetailsTableDTO> deletedDiagnosis = preauthDTO.getDeletedDiagnosis();
		
		if(deletedDiagnosis != null && ! deletedDiagnosis.isEmpty()){
			for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : deletedDiagnosis) {
				PedValidation pedValidation = preMedicalMapper.getPedValidation(diagnosisDetailsTableDTO);		
				pedValidation.setDeleteFlag(0l);
				pedValidation.setTransactionKey(preauth.getKey());
				if(pedValidation.getKey() != null){
					entityManager.merge(pedValidation);
					log.info("------PedValidation------>"+pedValidation+"<------------");
				}
				entityManager.flush();
				entityManager.clear();
			}
		}
		
		List<ProcedureDTO> deletedProcedure = preauthDTO.getDeletedProcedure();
		if(deletedProcedure != null && ! deletedProcedure.isEmpty()){
			for (ProcedureDTO procedureDTO : deletedProcedure) {
				Procedure procedure = preMedicalMapper.getProcedure(procedureDTO);
				procedure.setDeleteFlag(0l);
				procedure.setTransactionKey(preauth.getKey());
				if(procedure.getKey() != null){
					entityManager.merge(procedure);
					log.info("------Procedure------>"+procedure+"<------------");
				}
				entityManager.flush();
				entityManager.clear();
			}
		}		
		
		
		if(null != preauthDTO && null != preauthDTO.getNewIntimationDTO().getKey()){
			
			Intimation intimationObj = getIntimationByKey(preauthDTO.getNewIntimationDTO().getKey());
			if(null != intimationObj){
				
				intimationObj.setPaParentName(preauthDTO.getPreauthDataExtractionDetails().getParentName());
				intimationObj.setPaParentDOB(preauthDTO.getPreauthDataExtractionDetails().getDateOfBirth());
				intimationObj.setPaPatientName(preauthDTO.getPreauthDataExtractionDetails().getRiskName());
				intimationObj.setPaParentAge(preauthDTO.getPreauthDataExtractionDetails().getAge());
				
				if(null != intimationObj.getKey()){
				
					entityManager.merge(intimationObj);
					entityManager.flush();
				}else{
					entityManager.persist(intimationObj);
					entityManager.flush();
				}					
				
			}
			
			
		}
		
		if(ReferenceTable.getFHORevisedKeys().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())
				|| ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			preauthDTO.setKey(preauth.getKey());
			saveBenefitAmountDetails(preauthDTO);
		}
		
		Product product = preauthDTO.getNewIntimationDTO().getPolicy().getProduct();
		if(product.getCode() != null &&  (product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
				preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G"))) {
			
			List<UpdateOtherClaimDetailDTO> updateOtherClaimDetailDTO = preauthDTO.getUpdateOtherClaimDetailDTO();
			if(!updateOtherClaimDetailDTO.isEmpty()){
				List<UpdateOtherClaimDetails> updateOtherClaimDetails = preMedicalMapper.getUpdateOtherClaimDetails(updateOtherClaimDetailDTO);
				for (UpdateOtherClaimDetails updateOtherClaimDetails2 : updateOtherClaimDetails) {
					updateOtherClaimDetails2.setCashlessKey(preauth.getKey());
					updateOtherClaimDetails2.setClaimKey(currentClaim.getKey());
					updateOtherClaimDetails2.setStage(preauth.getStage());
//					updateOtherClaimDetails2.setIntimationId(currentClaim.getIntimation().getIntimationId());
					updateOtherClaimDetails2.setIntimationKey(currentClaim.getIntimation().getKey());
					updateOtherClaimDetails2.setStatus(preauth.getStatus());
					updateOtherClaimDetails2.setClaimType(currentClaim.getClaimType().getValue());
					if(updateOtherClaimDetails2.getKey() != null){
						entityManager.merge(updateOtherClaimDetails2);
						entityManager.flush();
					}else{
						entityManager.persist(updateOtherClaimDetails2);
						entityManager.flush();
					}
				}
			}
			
		}
		
		if(null != preauthDTO.getDocFilePath() && !("").equalsIgnoreCase(preauthDTO.getDocFilePath()))
		{
			WeakHashMap dataMap = new WeakHashMap();
			dataMap.put("intimationNumber",currentClaim.getIntimation().getIntimationId());
			dataMap.put("claimNumber",currentClaim.getClaimId());
			if(null != currentClaim.getClaimType())
			{
				if((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(currentClaim.getClaimType().getKey()))
					{
						Preauth preauthObj = SHAUtils.getPreauthClaimKey(entityManager , currentClaim.getKey());
						if(null != preauthObj)
							dataMap.put("cashlessNumber", preauthObj.getPreauthId());
					}
			}
			dataMap.put("filePath", preauthDTO.getDocFilePath());
			dataMap.put("docType", preauthDTO.getDocType());
			dataMap.put("docSources", SHAConstants.PRE_MEDICAL_PRE_AUTH);
			dataMap.put("createdBy", preauthDTO.getStrUserName());
			String docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
			
			/**
			 * The below code was added to update the document token column
			 * in query table, if an query has been raised at preauth level.
			 * */
			
			if(null != preauth && null != preauth.getStatus() && ((ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS).equals(preauth.getStatus().getKey()) || 
						(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS).equals(preauth.getStatus().getKey())))
			{
				PreauthQuery preauthQuery = getPreauthQueryList(preauth.getKey());
				if(null != preauthQuery && null != docToken)
				{
					preauthQuery.setDocumentToken(Long.valueOf(docToken));
					entityManager.merge(preauthQuery);
					entityManager.flush();
					entityManager.clear();
					log.info("------PreauthQuery------>"+preauthQuery+"<------------");
				}
			}
		}
		
		return preauth;
	}
	
	
	
	
/*	@SuppressWarnings("unused")
	private void setBPMOutcome(PreauthDTO preauthDTO , Preauth preauth, Boolean isPremedicalPreauth) {
			String outCome = "";
			PreAuthReqType preauthRequest  = preauthDTO.getRodHumanTask().getPayloadCashless().getPreAuthReq();
			try {
				if(isPremedicalPreauth) {
					outCome = getOutComeForPremedicalPreauth(preauthDTO, preauth, outCome);
				} else {
					outCome = getOutComeForPremedicalEnhancement(preauthDTO, preauth, outCome);
				}
				
				String preauthReqAmt = getPreauthReqAmt(preauth.getKey(),preauth.getClaim().getKey());
				
				preauthDTO.getRodHumanTask().setOutcome(outCome);
				preauthRequest.setKey(preauth.getKey());
				preauthRequest.setPreAuthAmt(preauth.getTotalApprovalAmount() != null ? preauth.getTotalApprovalAmount(): 0d);
				preauthRequest.setOutcome(outCome);
				preauthRequest.setResult(outCome);
				preauthRequest.setPreAuthAmt(Double.valueOf(preauthReqAmt));
				
				if(preauthDTO.getAmountConsidered() != null){
					Double doubleValueFromString = SHAUtils.getDoubleValueFromString(preauthDTO.getAmountConsidered());
					preauthRequest.setPreAuthAmt(doubleValueFromString);
				}
				
				ClaimRequestType claimRequestTyp = preauthDTO.getRodHumanTask().getPayloadCashless().getClaimRequest();
				if(claimRequestTyp == null) {
					claimRequestTyp = new ClaimRequestType();
				}
				
				Stage stage = entityManager.find(Stage.class, preauth.getClaim().getStage().getKey());
				
				if(preauth.getClaim() != null){
					 
				      claimRequestTyp.setOption(stage != null ? stage.getStageName() : "");
				}
				
				preauthDTO.getRodHumanTask().getPayloadCashless().setClaimRequest(claimRequestTyp);
				SelectValue treatmentType = preauthDTO.getPreauthDataExtractionDetails().getTreatmentType();
				CustomerType customer = preauthDTO.getRodHumanTask().getPayloadCashless().getCustomer();
				if(customer == null){
					 customer = new CustomerType();
				}
				customer.setTreatmentType(treatmentType.getValue() != null ? treatmentType.getValue() : "");
				customer.setSpeciality(preauthDTO.getSpecialityName());
				
				
				preauthDTO.getRodHumanTask().getPayloadCashless().getClassification().setSource(stage.getStageName());
				
				preauthDTO.getRodHumanTask().getPayloadCashless().setCustomer(customer);
				
				preauthDTO.getRodHumanTask().getPayloadCashless().setPreAuthReq(preauthRequest);
				
				System.out.println("--the user name----"+preauthDTO.getStrUserName());
				System.out.println("----the password-----"+preauthDTO.getStrPassword());
				if(isPremedicalPreauth) {
					SubmitPreMedicalPreAuthTask task = BPMClientContext.getPremedicalPreauth(preauthDTO.getStrUserName(), preauthDTO.getStrPassword());
					task.execute(preauthDTO.getStrUserName(), preauthDTO.getRodHumanTask());
				} else {
					SubmitPreMedicalPreAuthEnhTask task = BPMClientContext.getPremedicalEnhancementTask(preauthDTO.getStrUserName(), preauthDTO.getStrPassword());
					task.execute(preauthDTO.getStrUserName(), preauthDTO.getRodHumanTask());
				}
				
				if(preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS)
						|| preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS)){
					initiateAcknowledgementHospitalForQuery(preauthDTO);
				}
				
			} catch (Exception e) {
				log.error(e.toString());
				e.printStackTrace();
			}
	}*/
	
	public static void initiateAcknowledgementHospitalForQuery(PreauthDTO preauthDTO){/*
		
		StartAckHospitalCommunication initiateAckHospitalCommunication = BPMClientContext.getInitiateAckHospitalCommunication(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
		
		try {
			initiateAckHospitalCommunication.initiate(BPMClientContext.BPMN_TASK_USER, preauthDTO.getRodHumanTask().getPayloadCashless());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	*/}
	
	public String getPreauthReqAmt(Long preAuthKey, Long claimKey) {
		/*Query query = entityManager.createNamedQuery("Preauth.findByClaimKey");
		query.setParameter("claimkey", claimKey);
		List<Preauth> resultList = (List<Preauth>) query.getResultList();
		//Preauth currentPreauth = new Preauth();
		for (Preauth preauth : resultList) {
			
			entityManager.refresh(preauth);
			if(preauth.getKey().equals(preAuthKey)) {
				//currentPreauth = preauth;
			}
		}*/
		//String calculatePreRequestedAmt = calculatePreRequestedAmt(preAuthKey, claimKey);
		/*if(currentPreauth != null) {
			//String[] split = currentPreauth.getPreauthId().split("/");
			//String string = split[split.length - 1];
			if(SHAUtils.getIntegerFromString(string) != 1) {
				for (Preauth preauthDO : resultList) {
					//String[] splitedStr = preauthDO.getPreauthId().split("/");
					//String number = splitedStr[splitedStr.length - 1];
					//Integer previousNumber = SHAUtils.getIntegerFromString(string) - 1;
					if((previousNumber).equals(SHAUtils.getIntegerFromString(number))) {
						//String calculatePreRequestedAmt2 = calculatePreRequestedAmt(preauthDO.getKey(), claimKey);
						//Double value =  SHAUtils.getDoubleValueFromString(calculatePreRequestedAmt) - SHAUtils.getDoubleValueFromString(calculatePreRequestedAmt2);
//						calculatePreRequestedAmt = String.valueOf(value.intValue());
						break;
					}
				}
			}
		}*/
		return calculatePreRequestedAmt(preAuthKey, claimKey);
	}
	
	public String calculatePreRequestedAmt(Long preAuthKey, Long claimKey)
	{
		String requestedAmt = "0";
		float lAmt= 0f;
		Query findAll = entityManager
				.createNamedQuery("ClaimAmountDetails.findByPreauthKey");
		findAll.setParameter("preauthKey", preAuthKey);		
		List<ClaimAmountDetails> claimAmtDetails = (List<ClaimAmountDetails>)findAll.getResultList();
		for (ClaimAmountDetails claimAmountDetails : claimAmtDetails) {
			entityManager.refresh(claimAmountDetails);			
		}
		if(null !=claimAmtDetails )
		{
			//for(int i = 0 ; i<claimAmtDetails.size() ; i++)
			for(ClaimAmountDetails objClaim :claimAmtDetails )
			{
				entityManager.refresh(objClaim);
				if(null!= objClaim.getClaimedBillAmount())
				{
					lAmt = lAmt+ objClaim.getClaimedBillAmount();
				}
			}
			//return String.valueOf(lAmt);
			
			requestedAmt = String.valueOf(lAmt);
		}
		/*else
		{
			return "0";
		}*/
		
		return requestedAmt;
	}

	/*private String getOutComeForPremedicalEnhancement(PreauthDTO preauthDTO, Preauth preauth,
			String outCome) {
		if(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS.equals(preauth.getStatus().getKey())) {
			outCome = "PREMEDICALQUERYENH";
		} else if (ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS.equals(preauth.getStatus().getKey())) {
			outCome = "PROCESSINGENH";
			if(ReferenceTable.PRE_MEDICAL_PRE_AUTH_NON_MEDICAL_CATEGORY.equals(preauth.getMedicalCategoryId())) {
				outCome = "NONMEDICALPROCESSINGENH";
			} 
		} else if(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS.equals(preauth.getStatus().getKey())) {
			outCome = "MEDICALREJECTIONENH";
			if(ReferenceTable.PRE_MEDICAL_PRE_AUTH_NON_MEDICAL_CATEGORY.equals(preauth.getMedicalCategoryId())) {
				outCome = "NONMEDICALREJECTIONENH";
			} 
		} else if(preauthDTO.getCoordinatorDetails().getKey() != null) {
			outCome = "TRANSLATEENH";
		}
		return outCome;
		
		
		if(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS.equals(preauth.getStatus().getKey())) {
			outCome = "QUERYENH";
		} else if (ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS.equals(preauth.getStatus().getKey())) {
			outCome = "PROCESSINGENH";
			if(ReferenceTable.PRE_MEDICAL_PRE_AUTH_NON_MEDICAL_CATEGORY.equals(preauth.getMedicalCategoryId())) {
				outCome = "NONMEDICALPROCESSINGENH";
			} 
		} else if(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS.equals(preauth.getStatus().getKey())) {
			outCome = "MEDICALREJECTIONENH";
			if(ReferenceTable.PRE_MEDICAL_PRE_AUTH_NON_MEDICAL_CATEGORY.equals(preauth.getMedicalCategoryId())) {
				outCome = "NONMEDICALREJECTIONENH";
			} 
		} else if(preauthDTO.getCoordinatorDetails().getKey() != null) {
			outCome = "TRANSLATEENH";
		}
		return outCome;
	}*/
	
	/*private String getOutComeForPremedicalPreauth(PreauthDTO preauthDTO, Preauth preauth,
			String outCome) {
		if(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS.equals(preauth.getStatus().getKey())) {
			outCome = "PREMEDICALQUERY";
		} else if (ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS.equals(preauth.getStatus().getKey())) {
			outCome = "PROCESSING";
			if(ReferenceTable.PRE_MEDICAL_PRE_AUTH_NON_MEDICAL_CATEGORY.equals(preauth.getMedicalCategoryId())) {
				outCome = "NONMEDICALPROCESSING";
			} 
		} else if(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS.equals(preauth.getStatus().getKey())) {
			outCome = "MEDICALREJECTION";
			if(ReferenceTable.PRE_MEDICAL_PRE_AUTH_NON_MEDICAL_CATEGORY.equals(preauth.getMedicalCategoryId())) {
				outCome = "NONMEDICALREJECTION";
			} 
		} else if(preauthDTO.getCoordinatorDetails().getKey() != null) {
			outCome = "TRANSLATE";
		}
		return outCome;
	}*/
	
	/*@SuppressWarnings({ "unchecked", "unused" })
	private MastersValue getMaster(Long a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		MastersValue a_mastersValue = new MastersValue();
		if (a_key != null) {
			Query query = entityManager
					.createNamedQuery("MastersValue.findByKey");
			query = query.setParameter("parentKey", a_key);
			List<MastersValue> mastersValueList = query.getResultList();
			for (MastersValue mastersValue : mastersValueList)
				a_mastersValue = mastersValue;
		}

		return a_mastersValue;
	}*/
	
	public Preauth getLatestPreauthByClaimKey(Long claimKey)
	{
		Query query = entityManager.createNamedQuery("Preauth.findByClaimKeyInDescendingOrder");
		query.setParameter("claimkey", claimKey);
		List<Preauth> preauthList = (List<Preauth>) query.getResultList();
		
		if(preauthList != null && ! preauthList.isEmpty()){
			entityManager.refresh(preauthList.get(0));
			return preauthList.get(0);
		}
		return null;
	}

	/*private void setKeyToProceduresDTO(PreauthDTO preauthDTO,
			PreMedicalMapper preMedicalMapper) {
//		List<ProcedureDTO> consolidateProcedureDto=new ArrayList<ProcedureDTO>();
//		List<ProcedureDTO> procedureExclusionCheckTableList = preauthDTO.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
//		List<ProcedureDTO> oldProcedure = preMedicalMapper.getProcedureDto(preauthDTO.getPreauthDataExtractionDetails().getProcedureList());
//		List<ProcedureDTO> newProcedure = preMedicalMapper.getNewProcedureDto(preauthDTO.getPreauthDataExtractionDetails().getNewProcedureList());
//		consolidateProcedureDto.addAll(oldProcedure);
//		consolidateProcedureDto.addAll(newProcedure);
//		
//		if(!procedureExclusionCheckTableList.isEmpty()) {
//			for (ProcedureDTO oldProcedureDTO : procedureExclusionCheckTableList) {
//				for (ProcedureDTO newProcedureDTO : consolidateProcedureDto) {
//					if(newProcedureDTO.getProcedureCode().equalsIgnoreCase(oldProcedureDTO.getProcedureCode())) {
//						newProcedureDTO.setKey(oldProcedureDTO.getKey());
//					}
//				}
//			}
//		}
	}*/
	
	public void setDBWorkFlowObject(Preauth preauth,PreauthDTO preauthDTO, Boolean isPremedicalPreauth) {
		
		
		
		Map<String, Object> wrkFlowMap = (Map<String, Object>) preauthDTO.getDbOutArray();
//		wrkFlowMap.put(SHAConstants.TREATEMENT_TYPE,preauth.getTreatmentType().getValue());
		wrkFlowMap.put(SHAConstants.TREATEMENT_TYPE,preauth.getTreatmentType() != null ? preauth.getTreatmentType().getValue() : preauth.getTreatmentType());
		wrkFlowMap.put(SHAConstants.CASHLESS_NO,preauth.getPreauthId());
		wrkFlowMap.put(SHAConstants.CASHLESS_KEY,preauth.getKey());
//		wrkFlowMap.put(SHAConstants.SPECIALITY_NAME,preauth.getSpecialistType().getValue());
		
		String preauthReqAmt = getPreauthReqAmt(preauth.getKey(),preauth.getClaim().getKey());
		
		Double parseDouble = Double.parseDouble(preauthReqAmt);
		
//		wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT,0l);
		if (preauthReqAmt != null ){
			if(isPremedicalPreauth) {
			    current_q="FLPA";
			}
			else{
				current_q="FLEN";	
			}
//			wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT,parseDouble.intValue() < 0 ? 0 :parseDouble.intValue());
//			setProcessingCpuCodeBasedOnLimit(preauth.getClaim().getIntimation(), Long.valueOf(parseDouble.intValue()),preauthDTO.getStrUserName());
			setProcessingCpuCodeBasedOnLimit(preauth.getClaim().getIntimation(), Long.valueOf(parseDouble.intValue()),preauthDTO.getStrUserName(),current_q,preauth.getClaim());
			wrkFlowMap.put(SHAConstants.CPU_CODE,String.valueOf(preauth.getClaim().getIntimation().getCpuCode().getCpuCode()));
		}
		wrkFlowMap.put(SHAConstants.USER_ID,preauthDTO.getStrUserName());
		wrkFlowMap.put(SHAConstants.ADMISSION_DATE,SHAUtils.parseDate(preauth.getDataOfAdmission()));
		if(isPremedicalPreauth) {
		wrkFlowMap.put(SHAConstants.STAGE_SOURCE,SHAConstants.SOURCE_FLP_PROCESS);
		}else{
			wrkFlowMap.put(SHAConstants.STAGE_SOURCE,SHAConstants.SOURCE_FLE_PROCESS);
		}
		
		String outCome="";
		
		if(isPremedicalPreauth) {
			outCome = getDBOutComeForPremedicalPreauth(preauthDTO, preauth);
		} else {
			outCome = getDBOutComeForPremedicalEnhancement(preauthDTO, preauth);
		}
		
		wrkFlowMap.put(SHAConstants.OUTCOME,outCome);
		
		/*if(null != stpValues){
			
			String stpFlag = (String) stpValues.get("stpFlag");		
			String stpOutCome = (String) stpValues.get("outCome");
			Integer stpApprovedAmt = (Integer) stpValues.get("approveAmnt");
			if(null != stpFlag && SHAConstants.YES_FLAG.equalsIgnoreCase(stpFlag)){
				wrkFlowMap.put(SHAConstants.OUTCOME,stpOutCome);
				wrkFlowMap.put(SHAConstants.CLAIMED_AMOUNT, stpApprovedAmt);
			}
		}*/
				
		DBCalculationService dbCalService = new DBCalculationService();
		
		//cpu allocation
		String cpuLimitForUser = dbCalService.getCPULimitForUser(preauthDTO.getNewIntimationDTO().getCpuCode(), parseDouble.toString());
		
		if(cpuLimitForUser != null && !cpuLimitForUser.isEmpty()){
			wrkFlowMap.put(SHAConstants.PAYLOAD_DOCUMENT_RECEVIED_FROM, cpuLimitForUser);
		}
		
		// added for hold release in manual submit on 28-02-2020
		if (preauthDTO.getIsPreauthAutoAllocationQ() != null
				&& !preauthDTO.getIsPreauthAutoAllocationQ()) {
			wrkFlowMap.put(SHAConstants.PAYLOAD_PED_TYPE, null);
			wrkFlowMap.put(SHAConstants.PAYLOAD_PED_INITIATED_DATE, null);
			wrkFlowMap.put(SHAConstants.PAYLOAD_PED_REFERRED_BY, null);
		}

		//Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(wrkFlowMap);
		Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
		
		
		//dbCalService.initiateTaskProcedure(objArrayForSubmit);
		dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
		
		if (preauthDTO.getIsPreauthAutoAllocationQ() != null && preauthDTO.getIsPreauthAutoAllocationQ()) {
				if(null != wrkFlowMap){
					dbCalService.releaseHoldClaim(Long.parseLong(String.valueOf(wrkFlowMap.get(SHAConstants.WK_KEY))));
				}
		}
	}
	
	private String getDBOutComeForPremedicalEnhancement(PreauthDTO preauthDTO, Preauth preauth) {
		String outCome ="";
		if(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS.equals(preauth.getStatus().getKey())) {
			outCome = SHAConstants.OUTCOME_FLE_QUERY;
		} else if (ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS.equals(preauth.getStatus().getKey())) {
			outCome = SHAConstants.OUTCOME_FLE_MED_SEND_FOR_PROCESSING;
			if(ReferenceTable.PRE_MEDICAL_PRE_AUTH_NON_MEDICAL_CATEGORY.equals(preauth.getMedicalCategoryId())) {
				outCome = SHAConstants.OUTCOME_FLE_NON_MED_SEND_FOR_PROCESSING;
			} 
		} else if(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS.equals(preauth.getStatus().getKey())) {
			outCome =  SHAConstants.OUTCOME_FLE_MED_REJECTION;
			if(ReferenceTable.PRE_MEDICAL_PRE_AUTH_NON_MEDICAL_CATEGORY.equals(preauth.getMedicalCategoryId())) {
				outCome = SHAConstants.OUTCOME_FLE_NON_MED_REJECTION;
			} 
		} else if(preauthDTO.getCoordinatorDetails().getKey() != null) {
			outCome = "TRANSLATE";
		}
		return outCome;
	}
	
	private String getDBOutComeForPremedicalPreauth(PreauthDTO preauthDTO, Preauth preauth) {
		
		String outCome= "";
		
		if(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS.equals(preauth.getStatus().getKey())) {
			outCome = SHAConstants.OUTCOME_FLP_QUERY;
		} else if (ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS.equals(preauth.getStatus().getKey())) {
			outCome = SHAConstants.OUTCOME_FLP_MED_SEND_FOR_PROCESSING;
			if(ReferenceTable.PRE_MEDICAL_PRE_AUTH_NON_MEDICAL_CATEGORY.equals(preauth.getMedicalCategoryId())) {
				outCome = SHAConstants.OUTCOME_FLP_NON_MED_SEND_FOR_PROCESSING;
			} 
		} else if(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS.equals(preauth.getStatus().getKey())) {
			outCome = SHAConstants.OUTCOME_FLP_MED_REJECTION;
			if(ReferenceTable.PRE_MEDICAL_PRE_AUTH_NON_MEDICAL_CATEGORY.equals(preauth.getMedicalCategoryId())) {
				outCome = SHAConstants.OUTCOME_FLP_NON_MED_REJECTION;
			} 
		} else if(preauthDTO.getCoordinatorDetails().getKey() != null) {
			outCome = "TRANSLATEENH";
		}
		return outCome;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public Intimation getIntimationByKey(Long intimationKey) {

		Query findByKey = entityManager
				.createNamedQuery("Intimation.findByKey").setParameter(
						"intiationKey", intimationKey);

		List<Intimation> intimationList = (List<Intimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			return intimationList.get(0);

		}
		return null;
	}
	
	
	private Intimation setProcessingCpuCodeBasedOnLimit(Intimation objIntimation,
			Long estarFaxAmt,String userId,String current_q, Claim claim) {
		TmpCPUCode existingCpuCode = objIntimation.getCpuCode();
		//added foe CR GMC CPU Routing GLX2020075
		MasProductCpuRouting gmcRoutingProduct= getMasProductForGMCRouting(objIntimation.getPolicy().getProduct().getKey());
		if(gmcRoutingProduct != null){
			if(objIntimation.getPolicy().getHomeOfficeCode() != null) {
				OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(objIntimation.getPolicy().getHomeOfficeCode());
				if(branchOffice != null && branchOffice.getCpuCode() != null){
					String officeCpuCode = branchOffice.getCpuCode();
					Long processingCpuCode = getProcessingCpuCode(estarFaxAmt, Long.valueOf(officeCpuCode), SHAConstants.PROCESSING_CPU_CODE_GMC);
					if(processingCpuCode != null){
						TmpCPUCode masCpuCode = getMasCpuCode(processingCpuCode);
						objIntimation.setCpuCode(masCpuCode);
						entityManager.merge(objIntimation);
						entityManager.flush();
						Long strFaxNewCPU = getStarFaxCpu(processingCpuCode, estarFaxAmt,current_q,objIntimation.getPolicy().getProduct().getKey());
						if(strFaxNewCPU != 0){
							//commented for CPU escaltion reverse feed insert to DMS--added by noufel on 18-02-2020
//							if(! existingCpuCode.getCpuCode().equals(masCpuCode.getCpuCode()))
						saveStarFaxReverseFeed(objIntimation,processingCpuCode, strFaxNewCPU,Double.valueOf(estarFaxAmt),userId);
						saveClaimCpuCode(claim,processingCpuCode, strFaxNewCPU);
						}
						//added for cpu code save in claim table if function returns zero
						if(strFaxNewCPU == 0){
							saveClaimCpuCode(claim,processingCpuCode, processingCpuCode);
						}
					}else{
						Boolean paayasPolicyDetails = getPaayasPolicyDetails(objIntimation.getPolicy().getPolicyNumber());
						TmpCPUCode masCpuCode = null;
						//added foe CR GMC CPU Routing GLX2020075
						masCpuCode = objIntimation.getCpuCode();
						if(paayasPolicyDetails){
							 masCpuCode = getCpuDetails(ReferenceTable.PAAYAS_CPU_CODE);
						}else{
							Boolean jioPolicy = getJioPolicyDetails(objIntimation.getPolicy().getPolicyNumber());
							if(jioPolicy){
								 masCpuCode = getCpuDetails(ReferenceTable.JIO_CPU_CODE);
							}
							//GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
//							else{
//								masCpuCode = getCpuDetails(ReferenceTable.GMC_CPU_CODE);
//							}
						}
						Long tataPolicyCpuCode = getTataPolicy(objIntimation.getPolicy().getPolicyNumber());
						if(tataPolicyCpuCode != null){
							 masCpuCode = getMasCpuCode(tataPolicyCpuCode);
						}
						
						//added for CR GMC CPU Routing GLX2020075 only for kotak cpu routing
						Boolean kotakPolicy = getKotakPolicyDetails(objIntimation.getPolicy().getPolicyNumber());
						if(kotakPolicy){
							Long kotakCpuCode = getKotakProcessingCpuCode(estarFaxAmt, ReferenceTable.KOTAK_PROCESSING_CPU_CODE, SHAConstants.PROCESSING_CPU_CODE_GMC);
							masCpuCode = getMasCpuCode(kotakCpuCode);
						}
						
						objIntimation.setCpuCode(masCpuCode);
						entityManager.merge(objIntimation);
						entityManager.flush();
						Long strFaxNewCPU = getStarFaxCpu(existingCpuCode.getCpuCode(), estarFaxAmt,current_q,objIntimation.getPolicy().getProduct().getKey());
						if(strFaxNewCPU != 0){
							//commented for CPU escaltion reverse feed insert to DMS--added by noufel on 18-02-2020
//						if(! existingCpuCode.getCpuCode().equals(masCpuCode.getCpuCode()))
						saveStarFaxReverseFeed(objIntimation,existingCpuCode.getCpuCode(), strFaxNewCPU,Double.valueOf(estarFaxAmt),userId);
						saveClaimCpuCode(claim,existingCpuCode.getCpuCode(), strFaxNewCPU);
						}
						//added for cpu code save in claim table if function returns zero
						if(strFaxNewCPU == 0){
							saveClaimCpuCode(claim,existingCpuCode.getCpuCode(), existingCpuCode.getCpuCode());
						}
					}
					
				}
			}
			
		}else{
			Long processingCpuCode = getProcessingCpuCode(estarFaxAmt, objIntimation.getOriginalCpuCode() != null ? objIntimation.getOriginalCpuCode() : objIntimation.getCpuCode().getCpuCode(), SHAConstants.PROCESSING_CPU_CODE_RETAIL);
			if(processingCpuCode != null){
				TmpCPUCode masCpuCode = getMasCpuCode(processingCpuCode);
				objIntimation.setCpuCode(masCpuCode);
				entityManager.merge(objIntimation);
				entityManager.flush();
				Long strFaxNewCPU = getStarFaxCpu(processingCpuCode, estarFaxAmt,current_q,objIntimation.getPolicy().getProduct().getKey());
				if(strFaxNewCPU != 0){
					//commented for CPU escaltion reverse feed insert to DMS--added by noufel on 18-02-2020
//				if(! existingCpuCode.getCpuCode().equals(masCpuCode.getCpuCode()))
				saveStarFaxReverseFeed(objIntimation,processingCpuCode, strFaxNewCPU,Double.valueOf(estarFaxAmt),userId);
				saveClaimCpuCode(claim,processingCpuCode, strFaxNewCPU);
				}
				//added for cpu code save in claim table if function returns zero
				if(strFaxNewCPU == 0){
					saveClaimCpuCode(claim,processingCpuCode, processingCpuCode);
				}
			}else{
				Hospitals hospitalById = getHospitalById(objIntimation.getHospital());
				if(hospitalById != null){
					TmpCPUCode masCpuCode = getCpuDetails(hospitalById.getCpuId());
					objIntimation.setCpuCode(masCpuCode);
					entityManager.merge(objIntimation);
					entityManager.flush();
					Long strFaxNewCPU = getStarFaxCpu(existingCpuCode.getCpuCode(), estarFaxAmt,current_q,objIntimation.getPolicy().getProduct().getKey());
					if(strFaxNewCPU != 0){
						//commented for CPU escaltion reverse feed insert to DMS--added by noufel on 18-02-2020
//					if(! existingCpuCode.getCpuCode().equals(masCpuCode.getCpuCode()))
					saveStarFaxReverseFeed(objIntimation,existingCpuCode.getCpuCode(), strFaxNewCPU,Double.valueOf(estarFaxAmt),userId);
					saveClaimCpuCode(claim,existingCpuCode.getCpuCode(), strFaxNewCPU);
					}
					//added for cpu code save in claim table if function returns zero
					if(strFaxNewCPU == 0){
						saveClaimCpuCode(claim,existingCpuCode.getCpuCode(), existingCpuCode.getCpuCode());
					}
				}
			}
		}
		//GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
//		//added for CPU routing
//		if(objIntimation.getPolicy() != null && objIntimation.getPolicy().getProduct().getKey() != null){
//			String CpuCode= getMasProductCpu(objIntimation.getPolicy().getProduct().getKey());
//			if(CpuCode != null){
//				TmpCPUCode masCpuCode = getMasCpuCode(Long.valueOf(CpuCode));
//				objIntimation.setCpuCode(masCpuCode);
//				entityManager.merge(objIntimation);
//				entityManager.flush();
//				Long strFaxNewCPU = getStarFaxCpu(Long.valueOf(CpuCode), estarFaxAmt,current_q,objIntimation.getPolicy().getProduct().getKey());
//				if(strFaxNewCPU != 0){
//					//commented for CPU escaltion reverse feed insert to DMS--added by noufel on 18-02-2020
////				if(! existingCpuCode.getCpuCode().equals(masCpuCode.getCpuCode()))
//				saveStarFaxReverseFeed(objIntimation,Long.valueOf(CpuCode), strFaxNewCPU,Double.valueOf(estarFaxAmt),userId);
//				saveClaimCpuCode(claim,Long.valueOf(CpuCode), strFaxNewCPU);
//				}
//				if(strFaxNewCPU == 0){
//					saveClaimCpuCode(claim,Long.valueOf(CpuCode), Long.valueOf(CpuCode));
//				}
//			}
//		}
//		//added for CPU routing		
		
		String gpaPolicyDetails = getGpaPolicyDetails(objIntimation.getPolicy().getPolicyNumber());
		if(gpaPolicyDetails != null){
			TmpCPUCode masCpuCode = getMasCpuCode(Long.valueOf(gpaPolicyDetails));
			objIntimation.setCpuCode(masCpuCode);
			entityManager.merge(objIntimation);
			entityManager.flush();
		}
		
		return objIntimation;
	}
	
	public MasCpuLimit getMasCpuLimit(Long cpuId,String polType){
		try{
		Query findCpuCode = entityManager.createNamedQuery("MasCpuLimit.findByCode").setParameter("cpuCode", cpuId);
		findCpuCode.setParameter("polType", polType);
		//findCpuCode.setParameter("activeStatus", 1l);
		List<MasCpuLimit> cpuLimit = (List<MasCpuLimit>)findCpuCode.getResultList();
		
		if(cpuLimit != null && ! cpuLimit.isEmpty()){
			return cpuLimit.get(0);
		}
		
		}catch(Exception e){
				
		}
		return null;
	}
	
	public Long getProcessingCpuCode(Long estarFaxAmt,Long cpuCode, String polType){
		
		MasCpuLimit masCpuLimit = getMasCpuLimit(cpuCode,polType);
		if(masCpuLimit != null){
				if(polType.equalsIgnoreCase(SHAConstants.PROCESSING_CPU_CODE_GMC)){
					if(estarFaxAmt <= masCpuLimit.getCpuLimit()){
						return masCpuLimit.getProcessCpuCode();
					}else{
						return null;
					}
				} else if(polType.equalsIgnoreCase(SHAConstants.PROCESSING_CPU_CODE_RETAIL)){
						if(estarFaxAmt > masCpuLimit.getCpuLimit()){
							return masCpuLimit.getProcessCpuCode();
						}else{
							return null;
						}
				}
			}
		  return null;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public OrganaizationUnit getInsuredOfficeNameByDivisionCode(
			String issuingOfficeCode) {
		List<OrganaizationUnit> organizationList = new ArrayList<OrganaizationUnit>();
		if(issuingOfficeCode != null){
			Query findAll = entityManager.createNamedQuery("OrganaizationUnit.findByUnitId").setParameter("officeCode",issuingOfficeCode);
			organizationList = (List<OrganaizationUnit>) findAll.getResultList();
			if(organizationList != null && ! organizationList.isEmpty()){
				return organizationList.get(0);
			}
		}
		return null;
	}
	
	public TmpCPUCode getMasCpuCode(Long cpuCode){
		Query  query = entityManager.createNamedQuery("TmpCPUCode.findByCode");
		query = query.setParameter("cpuCode", cpuCode);
		List<TmpCPUCode> listOfTmpCodes = query.getResultList();
		if(null != listOfTmpCodes && !listOfTmpCodes.isEmpty()){
			return listOfTmpCodes.get(0);
		}
		return null;
	}
	
	public String getMaximumReportId(String intimationNumber){
		
	    String query = "SELECT MAX(A.PFDUP_REPORT_ID) FROM IMS_SFX_DOC_METADATA A WHERE PFDUP_INTM_NO="+"'"+intimationNumber+"'"+" AND PFDUP_DOC_TYPE<>'FVR'";
		Query nativeQuery = entityManager.createNativeQuery(query);
		String applicationId = (String)nativeQuery.getSingleResult();
		return applicationId;
	}
	
  public Hospitals getHospitalById(Long key){
		
		Query query = entityManager.createNamedQuery("Hospitals.findByKey");
		query.setParameter("key", key);
		
		List<Hospitals> resultList = (List<Hospitals>) query.getResultList();
		
		if(resultList != null && ! resultList.isEmpty()){
			return resultList.get(0);
		}
		
		return null;
		
	}
	
	public  TmpCPUCode getCpuDetails(Long cpuId) {
		TmpCPUCode ack = null;
		Query findByReimbursementKey = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
		try {
			List resultList = findByReimbursementKey.getResultList();
			if(resultList != null && !resultList.isEmpty()) {
				ack = (TmpCPUCode) resultList.get(0);
			}
			return ack;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void saveStarFaxReverseFeed(Intimation intimation, Long fromCpuCode, Long toCpuCode, Double claimedAmt,String userName){
		
		String maximumReportId = getMaximumReportId(intimation.getIntimationId());
	    //String starFaxCpuCode = getStarFaxCpuCode(maximumReportId);
	    //if(starFaxCpuCode != null && ! starFaxCpuCode.equals(toCpuCode.toString())){
		StarFaxReverseFeed reverseFeed = new StarFaxReverseFeed();
		reverseFeed.setApplicationId(maximumReportId);
		reverseFeed.setClaimNumber(intimation.getIntimationId());
		reverseFeed.setFromCpu(String.valueOf(fromCpuCode));
		reverseFeed.setToCpu(String.valueOf(toCpuCode));
		reverseFeed.setAmount(String.valueOf(claimedAmt));
		//As said by Satish Sir - 24-02-2020
		//reverseFeed.setUserId(userName);
		reverseFeed.setUserId("GALAXY");
		reverseFeed.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		// AS Per Nofel confirmation added below value in insert and update records
    	reverseFeed.setIsGlxProcessed("YES");
	    if(reverseFeed.getKey() != null){
	    	entityManager.merge(reverseFeed);
	    	entityManager.flush();
	    }else{
	    	entityManager.persist(reverseFeed);
	    	entityManager.flush();
	      }
	   // }
		
	}
	
	public String getStarFaxCpuCode(String ReportId)
	{
//		Query query = entityManager.createNamedQuery("DocUploadToPremia.findByIntimationAndDocType");
		Query query = entityManager.createNamedQuery("DocUploadToPremia.findByReportId");
		query = query.setParameter("reportId", ReportId); 
		List<DocUploadToPremia> listOfDocUploadData = (List<DocUploadToPremia>)query.getResultList();
		if(null != listOfDocUploadData && !listOfDocUploadData.isEmpty())
		{
			return listOfDocUploadData.get(0).getPfdUpCpuEsc();
		}
		return null;
	}
	
	public Boolean getPaayasPolicyDetails(String policyNumber){
		
		 Query query = entityManager.createNamedQuery("PaayasPolicy.findByPolicyNumber");
		 query = query.setParameter("policyNumber", policyNumber);
		 List<PaayasPolicy> resultList = (List<PaayasPolicy>)query.getResultList();		 
		 if(resultList != null && !resultList.isEmpty()) {
			 return true;
		 } 
				
		 return false;
	}
	
	public Boolean getJioPolicyDetails(String policyNumber){
		
		 Query query = entityManager.createNamedQuery("StarJioPolicy.findByPolicyNumber");
		 query = query.setParameter("policyNumber", policyNumber);
		 List<StarJioPolicy> resultList = (List<StarJioPolicy>)query.getResultList();		 
		 if(resultList != null && !resultList.isEmpty()) {
			 return true;
		 } 
				
		 return false;
	}
	
	public String getGpaPolicyDetails(String policyNumber){
		
		 Query query = entityManager.createNamedQuery("GpaPolicy.findByPolicyNumber");
		 query = query.setParameter("policyNumber", policyNumber);
		 List<GpaPolicy> resultList = (List<GpaPolicy>)query.getResultList();		 
		 if(resultList != null && !resultList.isEmpty()) {
			 return resultList.get(0).getCpuCode();
		 } 
				
		 return null;
	}
	
	private DiagnosisDetailsTableDTO  setIcdChapterBlock(DiagnosisDetailsTableDTO pedValidationDto){
		
		if(pedValidationDto != null && pedValidationDto.getIcdCode() != null){
			Long icdCodeKey = pedValidationDto.getIcdCode().getId();
			IcdCode icdCodeValues = getIcdCode(icdCodeKey);
			if(icdCodeValues != null){
				pedValidationDto.getIcdBlock().setId(icdCodeValues.getIcdBlock().getKey());
				pedValidationDto.getIcdChapter().setId(icdCodeValues.getIcdChapter().getKey());
			}
		}
		return pedValidationDto;
		
	}
	
	   public IcdCode getIcdCode(Long icdCodeKey){
		   
			Query query = entityManager.createNamedQuery("IcdCode.findByKey")
					.setParameter("primaryKey", icdCodeKey);

			List<IcdCode> icdCodeList =  query.getResultList();
			if(null != icdCodeList && !icdCodeList.isEmpty())
			{
				return icdCodeList.get(0);
			}
			
			return null;
	   }
	
	   public Long getTataPolicy(String policyNumber){
			
			 Query query = entityManager.createNamedQuery("TataPolicy.findByPolicyNumber");
			 query.setParameter("policyNumber", policyNumber);
			    List<TataPolicy> tataPolicy = (List<TataPolicy>)query.getResultList();
			    if(tataPolicy != null && ! tataPolicy.isEmpty()){
			    	return tataPolicy.get(0).getCpuCode();
			    }
			    return null;
		}
	   
	   
		public Preauth saveTataTrustPreauthValues(PreauthDTO preauthDTO, Boolean isRePremedicalPreauth) {
			
			if(null != preauthDTO.getPreauthDataExtractionDetails() && null != preauthDTO.getPreauthDataExtractionDetails().getEnhancementTreatmentRemarks() 
					&& !("").equals(preauthDTO.getPreauthDataExtractionDetails().getEnhancementTreatmentRemarks()))
			{
				preauthDTO.getPreauthDataExtractionDetails().setTreatmentRemarks(preauthDTO.getPreauthDataExtractionDetails().getEnhancementTreatmentRemarks());
			}
			
			if(preauthDTO.getStageKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE)){
				preauthDTO.getPreauthDataExtractionDetails().setTreatmentRemarks(preauthDTO.getPreauthDataExtractionDetails().getEnhancementTreatmentRemarks());
			}
			
			PreMedicalMapper preMedicalMapper=PreMedicalMapper.getInstance();
//			PreMedicalMapper.getAllMapValues();
			Preauth preauth = preMedicalMapper.getPreauth(preauthDTO);
			preauth.setDateOfDischarge(preauthDTO.getPreauthDataExtractionDetails().getDischargeDate());
			preauth.setActiveStatus(1l);
			if(null != preauthDTO.getSfxMatchedQDate())
				preauth.setSfxMatchedQDate(preauthDTO.getSfxMatchedQDate());
			if(null != preauthDTO.getSfxRegisteredQDate())
				preauth.setSfxRegisteredQDate(preauthDTO.getSfxRegisteredQDate());
			
			if(preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS) || preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS)) {
				preauth.setRemarks(preauthDTO.getPreauthMedicalProcessingDetails().getRejectionRemarks());
			} else if (preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS) || preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS) ) {
				preauth.setRemarks(preauthDTO.getPreauthMedicalProcessingDetails().getMedicalRemarks());
			} else if (preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS) || preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS) ) {
				preauth.setRemarks(preauthDTO.getPreauthMedicalProcessingDetails().getQueryRemarks());
			}
			Claim currentClaim = null;
			
			if(preauth.getClaim() != null) {
				currentClaim = searchByClaimKey(preauth.getClaim().getKey());
				// As per Prakash and Satish request commented stage and status updates on Claim...
//				currentClaim.setStatus(preauth.getStatus());
//				currentClaim.setStage(preauth.getStage());
				if(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration() != null && preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().equalsIgnoreCase(SHAConstants.AUTO_RESTORATION_DONE)) {
					currentClaim.setAutoRestroationFlag(SHAConstants.YES_FLAG);
				}
				currentClaim.setDataOfAdmission(preauth.getDataOfAdmission());
				currentClaim.setDataOfDischarge(preauth.getDateOfDischarge());
				currentClaim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				
				// SECTION IMPLEMENTED FOR COMPREHENSIVE AND UPDATED THE SECTION VALUES IN CLAIM LEVEL...
				if(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection() != null) {
					currentClaim.setClaimSectionCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue());
					currentClaim.setClaimCoverCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getCover().getCommonValue());
					currentClaim.setClaimSubCoverCode(preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue());
				}
				currentClaim.setIncidenceFlag(preauthDTO.getPreauthDataExtractionDetails().getAccidentOrDeathFlag());
				currentClaim.setIncidenceDate(preauthDTO.getPreauthDataExtractionDetails().getDateOfDeathAcc());
				
				if(null != preauthDTO.getPreauthDataExtractionDetails().getDateOfAccident())
				{
					currentClaim.setAccidentDate(preauthDTO.getPreauthDataExtractionDetails().getDateOfAccident());
				}
				
				if(null != preauthDTO.getPreauthDataExtractionDetails().getDateOfDeath())
				{
					currentClaim.setDeathDate(preauthDTO.getPreauthDataExtractionDetails().getDateOfDeath());
				}
				
				if(null != preauthDTO.getPreauthDataExtractionDetails().getDateOfDisablement())
				{
					currentClaim.setDisablementDate(preauthDTO.getPreauthDataExtractionDetails().getDateOfDisablement());
				}
				
				if(preauthDTO.getStatusKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS) || 
						preauthDTO.getStatusKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS)){
					currentClaim.setPaHospExpenseAmt(0d);
				}
				
				if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					if(preauthDTO.getPreauthDataExtractionDetails().getCorpBuffer() != null){
						String bufferFlag = preauthDTO.getPreauthDataExtractionDetails().getCorpBuffer() ? "Y":"N";
						currentClaim.setGmcCorpBufferFlag(bufferFlag);
						if(preauthDTO.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim() != null){
							currentClaim.setGmcCorpBufferLmt(preauthDTO.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim().doubleValue());
						}
					}
				}
				
				if(null != currentClaim.getIntimation().getPolicy().getProduct().getKey() &&
						(ReferenceTable.getGPAProducts().equals(currentClaim.getIntimation().getPolicy().getProduct().getKey())))
				{
					
					currentClaim.setGpaParentName(preauthDTO.getPreauthDataExtractionDetails().getParentName());
					currentClaim.setGpaParentDOB(preauthDTO.getPreauthDataExtractionDetails().getDateOfBirth());				
					currentClaim.setGpaParentAge(preauthDTO.getPreauthDataExtractionDetails().getAge());
					currentClaim.setGpaRiskName(preauthDTO.getPreauthDataExtractionDetails().getRiskName());
					currentClaim.setGpaRiskDOB(preauthDTO.getPreauthDataExtractionDetails().getGpaRiskDOB());
					currentClaim.setGpaRiskAge(preauthDTO.getPreauthDataExtractionDetails().getGpaRiskAge());
					
					SelectValue gpaCategory = new SelectValue();
					if(null != preauthDTO.getPreauthDataExtractionDetails().getGpaCategory()){
						gpaCategory = preauthDTO.getPreauthDataExtractionDetails().getGpaCategory();
						
						if(null != gpaCategory.getValue()){
							
							String[] splitCategory = gpaCategory.getValue().split("-");
							String category = splitCategory[0];
							if(null != category && !("null").equalsIgnoreCase(category)){
							currentClaim.setGpaCategory(category);
							}
						}
						
						
					}
													
					currentClaim.setGpaSection(preauthDTO.getPreauthDataExtractionDetails().getGpaSection());
				}
				
				if(currentClaim.getClaimRegisteredDate() == null){
					currentClaim.setClaimRegisteredDate((new Timestamp(System
							.currentTimeMillis())));
				}
				if(currentClaim.getDocumentReceivedDate() == null){
					currentClaim.setDocumentReceivedDate((new Timestamp(System
							.currentTimeMillis())));
				}

				entityManager.merge(currentClaim);
				entityManager.flush();
				entityManager.clear();
				log.info("------Claim------>"+currentClaim+"<------------");
				
				preauth.setClaim(currentClaim);
			}
			
			
			if(preauthDTO.getPreauthPreviousClaimsDetails().getAttachToPreviousClaim() != null) {
				Claim searchByClaimKey = searchByClaimKey(preauthDTO.getPreauthPreviousClaimsDetails().getAttachToPreviousClaim().getId());
				searchByClaimKey.setClaimLink(preauthDTO.getClaimKey());
				entityManager.merge(searchByClaimKey);
				entityManager.flush();
				entityManager.clear();
			}
			
			if(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration() != null) {
				if(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().toLowerCase().contains("n/a")) {
					preauth.setAutoRestoration("o");
				} else {
					preauth.setAutoRestoration(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().toLowerCase().contains("not") ? "N" : "Y");
				}
			}
			
			if(null != preauthDTO && null != preauthDTO.getPreauthDataExtractionDetails() && 
					null != preauthDTO.getPreauthDataExtractionDetails().getWorkOrNonWorkPlaceFlag()){
				preauth.setWorkPlace(preauthDTO.getPreauthDataExtractionDetails().getWorkOrNonWorkPlaceFlag());
			}
			
			preauth.setKey(null);
			/**
			 * Insured key is a new column added in IMS_CLS_CASHLESS TABLE
			 * during policy refractoring activity. Below code is added for inserting
			 * value in the insured key column.
			 * */
			preauth.setInsuredKey(preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
			if(preauth.getKey()!=null) {
				preauth.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				String strUserName = preauthDTO.getStrUserName();
				strUserName = SHAUtils.getUserNameForDB(strUserName);
				preauth.setModifiedBy(strUserName);
				entityManager.merge(preauth);
			}
			else {
				preauth.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				String strUserName = preauthDTO.getStrUserName();
				strUserName = SHAUtils.getUserNameForDB(strUserName);
				preauth.setCreatedBy(strUserName);
				entityManager.persist(preauth);
				
			}
			entityManager.flush();
			entityManager.clear();
			log.info("------Preauth------>"+preauth+"<------------");
			
			preauthDTO.setKey(preauth.getKey());
			
			preauthDTO.getCoordinatorDetails().setPreauthKey(preauth.getKey());
			preauthDTO.getCoordinatorDetails().setIntimationKey(preauth.getIntimation().getKey());
			preauthDTO.getCoordinatorDetails().setPolicyKey(preauth.getPolicy().getKey());
			
			if(preauthDTO.getCoordinatorDetails().getRefertoCoordinatorFlag().toLowerCase().equalsIgnoreCase("y")) {
				Coordinator coordinator = preMedicalMapper.getCoordinator(preauthDTO.getCoordinatorDetails()); 
				
				coordinator.setActiveStatus(1l);
				coordinator.setStage(preauth.getStage());
				coordinator.setStatus(preauth.getStatus());
				coordinator.setClaim(preauth.getClaim());
				coordinator.setTransactionFlag("C");
				coordinator.setTransactionKey(preauth.getKey());
				coordinator.setKey(null);
				if(coordinator.getKey()!=null) {
					entityManager.merge(coordinator);
				} else {
					entityManager.persist(coordinator);
				}
				entityManager.flush();
				entityManager.clear();
				log.info("------Coordinator------>"+coordinator+"<------------");
				preauthDTO.getCoordinatorDetails().setKey(coordinator.getKey());
			}
			
			List<SpecialityDTO> specialityDTOList = preauthDTO.getPreauthDataExtractionDetails().getSpecialityList();
			if(!specialityDTOList.isEmpty()) {
				StringBuffer specialityName = new StringBuffer();
				for (SpecialityDTO specialityDTO : specialityDTOList) {
					if(specialityDTO.getSpecialityType() != null && specialityDTO.getSpecialityType().getValue() != null){
						specialityName.append(specialityDTO.getSpecialityType().getValue()).append(",");
					}
					Speciality speciality = preMedicalMapper.getSpeciality(specialityDTO);
					//speciality.setPreauth(preauth);
					speciality.setClaim(preauth.getClaim());
					speciality.setStage(preauth.getStage());
					speciality.setStatus(preauth.getStatus());
//					speciality.setKey(null);

					if (speciality.getKey() != null) {
						entityManager.merge(speciality);
					} else {
						entityManager.persist(speciality);
						specialityDTO.setKey(speciality.getKey());
					}
					log.info("------Speciality------>"+speciality+"<------------");
				}
				entityManager.flush();
				entityManager.clear();
				preauthDTO.setSpecialityName(specialityName.toString());
			}
			
//			entityManager.clear();
			List<ProcedureDTO> procedureList = preauthDTO.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
			if(!procedureList.isEmpty()) {
				for (ProcedureDTO procedureDTO : procedureList) {
					Procedure procedure = preMedicalMapper.getProcedure(procedureDTO);
					procedure.setDeleteFlag(1l);
					procedure.setTransactionKey(preauth.getKey());
					procedure.setStage(preauth.getStage());
					procedure.setStatus(preauth.getStatus());
					procedure.setKey(null);
					if(!preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS) && !preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)) {
						if(procedureDTO.getCopay() != null) {
							procedure.setCopayPercentage(Double.valueOf(procedureDTO.getCopay().getValue()));
						}
						
					}
					if (procedure.getKey() != null) {
						entityManager.merge(procedure);
					} else {
						entityManager.persist(procedure);
						procedureDTO.setKey(procedure.getKey());
					}
					log.info("------Procedure------>"+procedure+"<------------");
				}
				entityManager.flush();
				entityManager.clear();
			}
			
			List<DiagnosisDetailsTableDTO> pedValidationTableList = preauthDTO.getPreauthDataExtractionDetails().getDiagnosisTableList();
			//Iterate pedValidationTable List.
				if(!pedValidationTableList.isEmpty()) {
				for (DiagnosisDetailsTableDTO pedValidationDTO : pedValidationTableList) {
					
					//Method to insert into MAS Diagnosis.
					//saveToMasterDiagnosis(pedValidationDTO);
				
					DiagnosisDetailsTableDTO pedValidationDTOWithCodes = setIcdChapterBlock(pedValidationDTO);
					
					PedValidation pedValidation = preMedicalMapper.getPedValidation(pedValidationDTOWithCodes);		
					pedValidation.setDeleteFlag(1l);
					pedValidation.setTransactionKey(preauth.getKey());
					pedValidation.setIntimation(preauth.getIntimation());
					pedValidation.setPolicy(preauth.getPolicy());
					pedValidation.setStage(preauth.getStage());
					pedValidation.setStatus(preauth.getStatus());
					pedValidation.setKey(null);
					
					if(!preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS) && !preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)) {
						List<PedDetailsTableDTO> pedList = pedValidationDTO.getPedList();
						for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
							if(pedDetailsTableDTO.getCopay() != null) {
								pedValidation.setCopayPercentage(Double.valueOf(pedDetailsTableDTO.getCopay().getValue()));
								break;
							}
						}
					}
					
					
					if (pedValidation.getKey() != null) {
						entityManager.merge(pedValidation);
					} else {
						entityManager.persist(pedValidation);
						pedValidationDTO.setKey(pedValidation.getKey());
					}
					entityManager.flush();
					entityManager.clear();
					log.info("------PedValidation------>"+pedValidation+"<------------");
					List<PedDetailsTableDTO> pedList = pedValidationDTO.getPedList();
					if(!pedList.isEmpty()) {
						for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
								DiagnosisPED diagnosisPED = preMedicalMapper.getDiagnosisPED(pedDetailsTableDTO);
								diagnosisPED.setPedValidation(pedValidation);
								if(pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null) {
									MastersValue value = new MastersValue();
									value.setKey(pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis().getId() : null);value.setKey(pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis().getId() : null);
									value.setValue(pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis().getValue() : null);
									diagnosisPED.setDiagonsisImpact(value);
								}
								if(pedDetailsTableDTO.getExclusionDetails() != null) {
									ExclusionDetails exclusionValue = new ExclusionDetails();
									exclusionValue.setKey(pedDetailsTableDTO.getExclusionDetails() != null ? pedDetailsTableDTO.getExclusionDetails().getId() : null);exclusionValue.setKey(pedDetailsTableDTO.getExclusionDetails() != null ? pedDetailsTableDTO.getExclusionDetails().getId() : null);
									exclusionValue.setExclusion(pedDetailsTableDTO.getExclusionDetails() != null ? pedDetailsTableDTO.getExclusionDetails().getValue() : null);
									diagnosisPED.setExclusionDetails(exclusionValue);
								}
								diagnosisPED.setDiagnosisRemarks(pedDetailsTableDTO.getRemarks());
								diagnosisPED.setKey(null);
								if (diagnosisPED.getKey() != null) {
									entityManager.merge(diagnosisPED);
								} else {
									entityManager.persist(diagnosisPED);
									pedDetailsTableDTO.setKey(diagnosisPED.getKey());
								}
								entityManager.flush();
								entityManager.clear();
								log.info("------DiagnosisPED------>"+diagnosisPED+"<------------");
						}
					}
				}
				
			}
				
			List<NoOfDaysCell> claimedDetailsList = preauthDTO.getPreauthDataExtractionDetails().getClaimedDetailsList();
			if(!claimedDetailsList.isEmpty()) {
				for (NoOfDaysCell noOfDaysCell : claimedDetailsList) {
					ClaimAmountDetails claimedAmountDetails = preMedicalMapper.getClaimedAmountDetails(noOfDaysCell);
					claimedAmountDetails.setPreauth(preauth);
					claimedAmountDetails.setActiveStatus(1l);
					claimedAmountDetails.setStage(preauth.getStage());
					claimedAmountDetails.setStatus(preauth.getStatus());
					claimedAmountDetails.setInsuredKey(preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
					claimedAmountDetails.setKey(null);
					if (claimedAmountDetails.getKey() != null) {
						entityManager.merge(claimedAmountDetails);
					} else {
						entityManager.persist(claimedAmountDetails);
						noOfDaysCell.setKey(claimedAmountDetails.getKey());
					}
					log.info("------ClaimAmountDetails------>"+claimedAmountDetails+"<------------");
				}
				entityManager.flush();
				entityManager.clear();
			}
			
			if (preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS) || preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS)) {
				if(preauth.getKey()!=null){
					PreauthQuery preAuthQuery = new PreauthQuery();
					preAuthQuery.setPreauth(preauth);
					preAuthQuery.setStage(preauth.getStage());
					preAuthQuery.setStatus(preauth.getStatus());
					preAuthQuery.setQueryRemarks(preauthDTO.getPreauthMedicalProcessingDetails().getQueryRemarks());
					String strUserName = preauthDTO.getStrUserName();
					strUserName = SHAUtils.getUserNameForDB(strUserName);
					preAuthQuery.setCreatedBy(strUserName);
					entityManager.persist(preAuthQuery);
					log.info("------PreauthQuery------>"+preAuthQuery+"<------------");
				}
				entityManager.flush();
				entityManager.clear();
			}
			
			List<DiagnosisDetailsTableDTO> deletedDiagnosis = preauthDTO.getDeletedDiagnosis();
			
			if(deletedDiagnosis != null && ! deletedDiagnosis.isEmpty()){
				for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : deletedDiagnosis) {
					PedValidation pedValidation = preMedicalMapper.getPedValidation(diagnosisDetailsTableDTO);		
					pedValidation.setDeleteFlag(0l);
					pedValidation.setTransactionKey(preauth.getKey());
					if(pedValidation.getKey() != null){
						entityManager.merge(pedValidation);
					}
					entityManager.flush();
					entityManager.clear();
					log.info("------PedValidation------>"+pedValidation+"<------------");
				}
			}
			
			List<ProcedureDTO> deletedProcedure = preauthDTO.getDeletedProcedure();
			if(deletedProcedure != null && ! deletedProcedure.isEmpty()){
				for (ProcedureDTO procedureDTO : deletedProcedure) {
					Procedure procedure = preMedicalMapper.getProcedure(procedureDTO);
					procedure.setDeleteFlag(0l);
					procedure.setTransactionKey(preauth.getKey());
					if(procedure.getKey() != null){
						entityManager.merge(procedure);
					}
					entityManager.flush();
					entityManager.clear();
					log.info("------Procedure------>"+procedure+"<------------");
				}
			}
			
			Product product = preauthDTO.getNewIntimationDTO().getPolicy().getProduct();
			if(product.getCode() != null &&  (product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
					preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G"))) {
				
				List<UpdateOtherClaimDetailDTO> updateOtherClaimDetailDTO = preauthDTO.getUpdateOtherClaimDetailDTO();
				if(!updateOtherClaimDetailDTO.isEmpty()){
					List<UpdateOtherClaimDetails> updateOtherClaimDetails = preMedicalMapper.getUpdateOtherClaimDetails(updateOtherClaimDetailDTO);
					for (UpdateOtherClaimDetails updateOtherClaimDetails2 : updateOtherClaimDetails) {
						updateOtherClaimDetails2.setCashlessKey(preauth.getKey());
						updateOtherClaimDetails2.setClaimKey(currentClaim.getKey());
						updateOtherClaimDetails2.setStage(preauth.getStage());
//						updateOtherClaimDetails2.setIntimationId(currentClaim.getIntimation().getIntimationId());
						updateOtherClaimDetails2.setIntimationKey(currentClaim.getIntimation().getKey());
						updateOtherClaimDetails2.setStatus(preauth.getStatus());
						updateOtherClaimDetails2.setClaimType(currentClaim.getClaimType().getValue());
						updateOtherClaimDetails2.setKey(null);
						if(updateOtherClaimDetails2.getKey() != null){
							entityManager.merge(updateOtherClaimDetails2);
							entityManager.flush();
						}else{
							entityManager.persist(updateOtherClaimDetails2);
							entityManager.flush();
						}
					}
				}
			}
				
			if(ReferenceTable.getFHORevisedKeys().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())
					|| ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				preauthDTO.setKey(preauth.getKey());
				saveBenefitAmountDetails(preauthDTO);
			}
			
			if(null != preauthDTO.getDocFilePath() && !("").equalsIgnoreCase(preauthDTO.getDocFilePath()))
			{
				WeakHashMap dataMap = new WeakHashMap();
				dataMap.put("intimationNumber",currentClaim.getIntimation().getIntimationId());
				dataMap.put("claimNumber",currentClaim.getClaimId());
				if(null != currentClaim.getClaimType())
				{
					if((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(currentClaim.getClaimType().getKey()))
						{
							//Preauth preauthObj = SHAUtils.getPreauthClaimKey(entityManager , currentClaim.getKey());
							Preauth preauthObj = getLatestPreauthByClaimKey( currentClaim.getKey());
							if(null != preauthObj)
								dataMap.put("cashlessNumber", preauthObj.getPreauthId());
						}
				}
				dataMap.put("filePath", preauthDTO.getDocFilePath());
				dataMap.put("docType", preauthDTO.getDocType());
				dataMap.put("docSources", SHAConstants.PRE_MEDICAL_PRE_AUTH);
				dataMap.put("createdBy", preauthDTO.getStrUserName());
				String docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
				
				/**
				 * The below code was added to update the document token column
				 * in query table, if an query has been raised at preauth level.
				 * */
				
				if(null != preauth && null != preauth.getStatus() && ((ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS).equals(preauth.getStatus().getKey()) || 
						(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS).equals(preauth.getStatus().getKey())))
				{
					PreauthQuery preauthQuery = getPreauthQueryList(preauth.getKey());
					if(null != preauthQuery && null != docToken)
					{
						preauthQuery.setDocumentToken(Long.valueOf(docToken));
						entityManager.merge(preauthQuery);
						entityManager.flush();
						entityManager.clear();
						log.info("------PreauthQuery------>"+preauthQuery+"<------------");
					}
				}
			}
			
			return preauth;
		}
		
		public Boolean getKotakPolicyDetails(String policyNumber){
			
			 Query query = entityManager.createNamedQuery("StarKotakPolicy.findByPolicyNumber");
			 query = query.setParameter("policyNumber", policyNumber);
			 List<StarKotakPolicy> resultList = (List<StarKotakPolicy>)query.getResultList();		 
			 if(resultList != null && !resultList.isEmpty()) {
				 return true;
			 } 
					
			 return false;
		}
		
		 public String getMasProductCpu(Long key){
				
			 Query query = entityManager.createNamedQuery("MasProductCpuRouting.findByKey");
			 query = query.setParameter("key", key);
			 List<MasProductCpuRouting> resultList = (List<MasProductCpuRouting>)query.getResultList();		 
			 if(resultList != null && !resultList.isEmpty()) {
				 return resultList.get(0).getCpuCode();
			 } 
					
			 return null;
		 }
			 
		public Policy getPolicyDetails(Long policyKey) {
			Query query = entityManager.createNamedQuery("Policy.findByKey");
			query = query.setParameter("policyKey", policyKey);
			List<Policy> resultList = (List<Policy>)query.getResultList();
			if(resultList != null && !resultList.isEmpty()) {
				return resultList.get(0);
			}
			return null;
		}
		
		public void saveNomineeDetails(List<NomineeDetailsDto> nomineeDetailsList, Preauth prauthObj){
			
			for (NomineeDetailsDto nomineeDetailsDto : nomineeDetailsList) {
				
				ProposerNominee nomineeObj = getNomineeDetailsByKey(nomineeDetailsDto.getProposerNomineeKey());

				if(nomineeObj != null){
					nomineeObj.setModifiedBy(nomineeDetailsDto.getModifiedBy());
					nomineeObj.setModifiedDate(new Date());
				}
				else{
					nomineeObj = new ProposerNominee();
					nomineeObj.setNomineeName(nomineeDetailsDto.getNomineeName());
					nomineeObj.setNomineeAge(nomineeDetailsDto.getNomineeAge() != null && !nomineeDetailsDto.getNomineeAge().isEmpty() ? Integer.valueOf(nomineeDetailsDto.getNomineeAge()) : null);
					nomineeObj.setNomineeDob(nomineeDetailsDto.getNomineeDob() != null && !nomineeDetailsDto.getNomineeDob().isEmpty() ? SHAUtils.formatTimeFromString(nomineeDetailsDto.getNomineeDob()) : null);
					nomineeObj.setRelationshipWithProposer(nomineeDetailsDto.getNomineeRelationship());
					nomineeObj.setSharePercent(nomineeDetailsDto.getNomineePercent() != null && !nomineeDetailsDto.getNomineePercent().isEmpty() ?  Double.valueOf(nomineeDetailsDto.getNomineePercent().split("%")[0]) : null);
					nomineeObj.setPolicy(prauthObj.getClaim().getIntimation().getPolicy());
					nomineeObj.setPolicyNominee(getPolicyNomineebyKey(nomineeDetailsDto.getPolicyNomineeKey()));
					nomineeObj.setAppointeeName(nomineeDetailsDto.getAppointeeName());
					nomineeObj.setAppointeeAge(nomineeDetailsDto.getAppointeeAge() != null && !nomineeDetailsDto.getAppointeeAge().isEmpty() ? Integer.valueOf(nomineeDetailsDto.getAppointeeAge()) : null);
					nomineeObj.setAppointeeRelationship(nomineeDetailsDto.getAppointeeRelationship());
					nomineeObj.setCreatedBy(nomineeDetailsDto.getModifiedBy());
					nomineeObj.setCreatedDate(new Date());
					nomineeObj.setActiveStatus(1);
					nomineeObj.setBankName(nomineeDetailsDto.getBankName());
					nomineeObj.setBankBranchName(nomineeDetailsDto.getBankBranchName());
				}
				
				nomineeObj.setInsured(prauthObj.getClaim().getIntimation().getInsured());
				nomineeObj.setIntimation(prauthObj.getClaim().getIntimation());
				nomineeObj.setClaim(prauthObj.getClaim());
				nomineeObj.setTransactionKey(prauthObj.getKey());
				nomineeObj.setSelectedFlag(nomineeDetailsDto.isSelectedNominee() ? ReferenceTable.YES_FLAG : ReferenceTable.NO_FLAG);
				nomineeObj.setTransactionType(prauthObj.getClaim().getClaimType() != null ? prauthObj.getClaim().getClaimType().getValue().toUpperCase() : ReferenceTable.CASHLESS_CLAIM);

				try{
					if(nomineeObj.getKey() == null) {
						entityManager.persist(nomineeObj);
					}
					else{
						entityManager.merge(nomineeObj);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
			
		}
		
		public ProposerNominee getNomineeDetailsByKey(Long proposerNomineeKey) {
			ProposerNominee nomineeObj = null;
				try{
					Query query = entityManager.createNamedQuery("ProposerNominee.findByKey");
					query = query.setParameter("nomineeKey", proposerNomineeKey);
					
					List<ProposerNominee> resultList = (List<ProposerNominee>) query.getResultList();
					
					if(resultList != null && !resultList.isEmpty()) {
						nomineeObj = resultList.get(0);
					}
					
				}
				catch(Exception e){
					e.printStackTrace();
				}
				return nomineeObj;
			
		}
		
		public PolicyNominee getPolicyNomineebyKey(Long policyNomineeKey) {
			
			PolicyNominee nomineeObj = null;
			try{
				Query query = entityManager.createNamedQuery("PolicyNominee.findByKey");
				query = query.setParameter("nomineeKey", policyNomineeKey);
				
				List<PolicyNominee> resultList = (List<PolicyNominee>) query.getResultList();
				
				if(resultList != null && !resultList.isEmpty()) {
					nomineeObj = resultList.get(0);
				}
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return nomineeObj;
		}

		public void updateStageInformation(Preauth preauth,PreauthDTO bean){

			Claim claim = null;
			if(preauth !=null){
				claim = preauth.getClaim();
			}else{
				Query query = entityManager.createNamedQuery("Claim.findByIntimationNo");
				query = query.setParameter("intimationNumber", bean.getNewIntimationDTO().getIntimationId());
				claim = (Claim) query.getSingleResult();
			}
			StageInformation stgInformation = new StageInformation();
			stgInformation.setIntimation(claim.getIntimation());				
			stgInformation.setClaimType(claim.getClaimType());
			if(preauth !=null){
				stgInformation.setStage(preauth.getStage());
			}else{
				Stage stage = new Stage();
				stage.setKey(ReferenceTable.PROCESS_PRE_MEDICAL);
				stgInformation.setStage(stage);
			}
			
			Status status = new Status();
			status.setKey(ReferenceTable.PREAUTH_HOLD_STATUS_KEY);
			status.setProcessValue(ReferenceTable.PREAUTH_HOLD_STATUS);
			stgInformation.setPreauth(preauth);
			stgInformation.setStatus(status);
			stgInformation.setClaim(claim);
			String strUserName = bean.getStrUserName();
			String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
			stgInformation.setCreatedBy(userNameForDB);
			stgInformation.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			stgInformation.setStatusRemarks(bean.getPreMedicalPreauthMedicalDecisionDetails().getHoldRemarks());

			entityManager.persist(stgInformation);
			entityManager.flush();
		}
		
		public void updateCancelRemarks(Preauth preauth, String remarks,PreauthDTO bean){
			
			if(null != preauth){
				preauth.setAutoCancelRemarks(remarks);
				if(null != preauth.getKey()){
					entityManager.merge(preauth);
					entityManager.flush();
				}
			}
			AutoAllocationCancelRemarks autoAllocCancelRemarks = new AutoAllocationCancelRemarks();
			if(bean.getKey() !=null){
				autoAllocCancelRemarks.setTransactionKey(bean.getKey());
			}else{
				autoAllocCancelRemarks.setTransactionKey(0L);
			}
			if(null !=bean.getStageKey()){
				autoAllocCancelRemarks.setStageId(bean.getStageKey());
			}else{
				autoAllocCancelRemarks.setStageId(ReferenceTable.PROCESS_PRE_MEDICAL);
			}
			autoAllocCancelRemarks.setIntimationKey(null != bean.getNewIntimationDTO() ? bean.getNewIntimationDTO().getKey():null);
			autoAllocCancelRemarks.setClaimKey(bean.getClaimDTO().getKey());
			autoAllocCancelRemarks.setTransactionType(SHAConstants.CASHLESS_CHAR);
			autoAllocCancelRemarks.setCancelRemarks(remarks);
			autoAllocCancelRemarks.setCancelledBy(bean.getStrUserName());
			autoAllocCancelRemarks.setCancelledDate(new Timestamp(System.currentTimeMillis()));
			entityManager.persist(autoAllocCancelRemarks);
			entityManager.flush();
		}

		public void saveClaimCpuCode(Claim claim,Long fromCpuCode, Long toCpuCode){
				
			    claim.setSfxCpuCode(fromCpuCode);
			    claim.setSfxProcessingCpuCode(toCpuCode);
			    entityManager.merge(claim);
				entityManager.flush();
				
			}
		
		 public Long getStarFaxCpu(Long pfdUpCpuEsc, Long pfdUpFFAXAmt,String curenntQ,Long productKey) 
			{
				Query cs = entityManager.createNativeQuery(
					"select FUN_SFX_CPU_ROUTING (?1,?2,?3,?4) from dual");
			cs.setParameter(1, pfdUpCpuEsc);
			cs.setParameter(2, pfdUpFFAXAmt);
			cs.setParameter(3, curenntQ);
			cs.setParameter(4, productKey);
			String result =cs.getSingleResult().toString();
			Long result1 = Long.parseLong(result);
			return result1;
			}
		 public MasProductCpuRouting getMasProductForGMCRouting(Long key){

			 Query query = entityManager.createNamedQuery("MasProductCpuRouting.findByKey");
			 query = query.setParameter("key", key);
			 List<MasProductCpuRouting> resultList = (List<MasProductCpuRouting>)query.getResultList();		 
			 if(resultList != null && !resultList.isEmpty()) {
				 return resultList.get(0);
			 } 

			 return null;
		 }
		 
		//added for CR GMC CPU Routing GLX2020075 only for kotak cpu routing
		 public Long getKotakProcessingCpuCode(Long estarFaxAmt,Long cpuCode, String polType){
			 Long kotakCPUCode =cpuCode;
			 MasCpuLimit masCpuLimit = getMasCpuLimit(cpuCode,polType);
			 if(masCpuLimit != null){
				 if(estarFaxAmt >= masCpuLimit.getCpuLimit()){
					 kotakCPUCode = masCpuLimit.getProcessCpuCode();
				 }else {
					 kotakCPUCode = masCpuLimit.getCpuCode();
				 }
			 }
			 return kotakCPUCode;
		 }
}
