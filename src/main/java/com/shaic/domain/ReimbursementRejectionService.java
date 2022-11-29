package com.shaic.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.DocAcknowledgementDto;
import com.shaic.claim.DocAcknowledgementMapper;
import com.shaic.claim.ReimbursementDto;
import com.shaic.claim.ReimbursementMapper;
import com.shaic.claim.ReimbursementRejectionDetailsDto;
import com.shaic.claim.ReimbursementRejectionDto;
import com.shaic.claim.ReimbursementRejectionMapper;
import com.shaic.claim.reimbursement.processDraftRejectionLetterDetail.RejectionDetailsMapper;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.reimbursement.rod.allowReconsideration.search.SearchAllowReconsiderationTableDTO;

/**
 * 
 * @author Lakshminarayana
 *
 */
@Stateless
public class ReimbursementRejectionService {

	@PersistenceContext
	protected EntityManager entityManager;
	
	private final Logger log = LoggerFactory.getLogger(ReimbursementRejectionService.class);
	
	@SuppressWarnings("unchecked")
	public ReimbursementRejectionDto getReimbursementRejectionByKey(Long reimbursementKey){
	
		ReimbursementRejectionDto reimbursementRejectionDto = null;
		
		try {
			
//		Query query = entityManager
//				.createNamedQuery("ReimbursementRejection.findByReimbursementKey");
//		query = query.setParameter("reimbursementKey", reimbursementKey);
//		
//		List<ReimbursementRejection> reimbRejectionList = (List<ReimbursementRejection>)query.getResultList();
		
//		if (reimbRejectionList != null && !reimbRejectionList.isEmpty()) {
//				ReimbursementRejection reimbursementRejection = reimbRejectionList.get(0);

//		ReimbursementRejection reimbursementRejection = (ReimbursementRejection)query.getResultList().get(0);

			ReimbursementRejection reimbursementRejection = getRejectionKeyByReimbursement(reimbursementKey);
			
			if (reimbursementRejection != null) {
				ReimbursementRejectionMapper rqMapper = new ReimbursementRejectionMapper();
				reimbursementRejectionDto = rqMapper
						.getReimbursementRejectionDto(reimbursementRejection);
			
				Reimbursement reimbrsmnt = getReimbursementByKey(reimbursementRejectionDto
						.getReimbursementDto().getKey());

				ReimbursementMapper rmbsmntMapper = new ReimbursementMapper();
				ReimbursementDto reimbursementDto = rmbsmntMapper
						.getReimbursementDto(reimbrsmnt);

				Query q = entityManager
						.createNamedQuery("PedValidation.findByTransactionKey");
				q.setParameter("transactionKey", reimbursementDto.getKey());

				List<PedValidation> diagnosisList = (List<PedValidation>) q
						.getResultList();
				String diagnosis = null;
				if (diagnosisList != null && !diagnosisList.isEmpty()) {
					diagnosis = "";
					for (PedValidation pedDiagnosis : diagnosisList) {

						if (pedDiagnosis != null) {
							if (pedDiagnosis.getDiagnosisId() != null) {
								Diagnosis diag = entityManager.find(
										Diagnosis.class,
										pedDiagnosis.getDiagnosisId());
								if (diag != null) {
									diagnosis = !("")
											.equalsIgnoreCase(diagnosis) ? diagnosis
											+ " / " + diag.getValue()
											: diag.getValue();
								}
							}

						}

					}
				}
				if (diagnosis != null) {
					reimbursementDto.setDiagnosis(diagnosis);
				}

				Claim claim = reimbrsmnt.getClaim();

				ClaimMapper clmMapper = ClaimMapper.getInstance();
				ClaimDto claimDto = clmMapper.getClaimDto(claim);

				Intimation intiamtion = claim.getIntimation();

				NewIntimationDto newIntimationDto = getIntimationDto(intiamtion);

				newIntimationDto.setInsuredAge(newIntimationDto
						.getInsuredFullAge());

				List<NomineeDetailsDto> nomineeList = (new IntimationService()).getNomineeDetailsListByTransactionKey(reimbrsmnt.getKey(),entityManager);
				
				if(nomineeList != null && !nomineeList.isEmpty())
				{	
					newIntimationDto.setNomineeList(nomineeList);
				}
				reimbursementDto.setNomineeDeceasedFlag(reimbrsmnt.getNomineeFlag());
				
				if(intiamtion != null
						&& intiamtion.getPolicy().getProduct() != null
						&& ((ReferenceTable.getRevisedCriticareProducts().containsKey(intiamtion.getPolicy().getProduct().getKey()))
								|| ((ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM.equals(intiamtion.getPolicy().getProduct().getKey()) 
										|| ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM.equals(intiamtion.getPolicy().getProduct().getKey()))
										&&intiamtion.getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM))
								|| intiamtion.getPolicy().getProduct().getKey().equals(ReferenceTable.RAKSHAK_CORONA_PRODUCT_KEY))		
						&& reimbrsmnt != null ) {
					if(reimbrsmnt.getPatientStatus() != null 
							&& reimbrsmnt.getPatientStatus().getKey() != null 
							&& ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(reimbrsmnt.getPatientStatus().getKey())) {
						
						reimbursementDto.setPatientStatusId(reimbrsmnt.getPatientStatus().getKey());
						NomineeDetails nomineeObj = ((new IntimationService()).getNomineeDetailsByInsuredId(intiamtion.getInsured().getKey())); 
								
						if(nomineeObj != null) {
							newIntimationDto.setNomineeName(nomineeObj.getNomineeName());
						}
						else {
							newIntimationDto.setNomineeName(null);
						}
					}		
				}
				
				
				claimDto.setNewIntimationDto(newIntimationDto);

				reimbursementDto.setClaimDto(claimDto);

				DocAcknowledgement docAckdgmnt = reimbrsmnt
						.getDocAcknowLedgement();

				DocAcknowledgementMapper ackdgmntMapper = new DocAcknowledgementMapper();

				DocAcknowledgementDto docAckdgmntDto = ackdgmntMapper
						.getDocAcknowledgementDto(docAckdgmnt);
				docAckdgmntDto
						.setReconsiderationReason(docAckdgmnt
								.getReconsiderationReasonId() != null ? (new SelectValue(
								docAckdgmnt.getReconsiderationReasonId()
										.getKey(), docAckdgmnt
										.getReconsiderationReasonId()
										.getValue())) : null);

				docAckdgmntDto.setClaimDto(claimDto);

				reimbursementDto.setDocAcknowledgementDto(docAckdgmntDto);
				
//				Implemented for hospitalcash product diagnosis name in letter
				if(reimbursementDto.getClaimDto().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						reimbursementDto.getClaimDto().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076) ||
						reimbursementDto.getClaimDto().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
								reimbursementDto.getClaimDto().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)){
					if(reimbrsmnt.getProdDiagnosisID() != null){	
						DiagnosisHospitalDetails diagnosisValue = getDiagnosisByID(reimbrsmnt.getProdDiagnosisID()) ;
						diagnosis = diagnosisValue.getDiagnosisName();
						if (diagnosis != null) {
							reimbursementDto.setDiagnosis(diagnosis);
						}
					}
		
				}

				reimbursementRejectionDto.setReimbursementDto(reimbursementDto);
				//
				if(reimbursementRejection.getRejectionRemarks() != null && reimbursementRejection.getRejectionRemarks2() != null){
					reimbursementRejectionDto.setRejectionRemarks(reimbursementRejection.getRejectionRemarks()+reimbursementRejection.getRejectionRemarks2());
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.toString());
		}
		return reimbursementRejectionDto;
		
	}
	
	public Reimbursement getReimbursementByKey(Long rodKey) {
		Query query = entityManager.createNamedQuery("Reimbursement.findByKey")
				.setParameter("primaryKey", rodKey);
		List<Reimbursement> rodList = query.getResultList();

		if (rodList != null && !rodList.isEmpty()) {
			for (Reimbursement reimbursement : rodList) {
				entityManager.refresh(reimbursement);
			}
			return rodList.get(0);
		}
		return null;
	}
	public ReimbursementRejection getRejectionByReimbursementKey(Long reimbursementKey,EntityManager em){
		this.entityManager = em;
		ReimbursementRejection rejectionObj = getRejectionKeyByReimbursement(reimbursementKey);
		return rejectionObj;
		
	}
	
	@SuppressWarnings("unchecked")
	public ReimbursementRejection getRejectionKeyByReimbursement(Long reimbursementKey){
		ReimbursementRejection reimbursementRejection = null;
		List<ReimbursementRejection> reimbursementRejectionList = new ArrayList<ReimbursementRejection>();
		try{
			
			Query query = entityManager
					.createNamedQuery("ReimbursementRejection.findByReimbursementKey");
			query = query.setParameter("reimbursementKey", reimbursementKey);
			
			reimbursementRejectionList = (List<ReimbursementRejection>)query.getResultList();
			
			if(reimbursementRejectionList != null && ! reimbursementRejectionList.isEmpty()){
				reimbursementRejection = reimbursementRejectionList.get(0);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.toString());
		}
		return reimbursementRejection;
		
	}
	
	private NewIntimationDto getIntimationDto(Intimation intimation){
		IntimationService intimationService = new IntimationService();
		
		NewIntimationDto newIntimationDto = intimationService.getIntimationDto(intimation, entityManager);
		
		return newIntimationDto;
	}
	
	public ReimbursementRejection findRejectionByKey(Long rejectionKey){
		
		ReimbursementRejection reimbursementRejection = null;
		Query rejectQuery = entityManager.createNamedQuery("ReimbursementRejection.findByKey");
		rejectQuery.setParameter("primaryKey",rejectionKey);
		
		List<ReimbursementRejection> reimbursementRejectionList = (List<ReimbursementRejection>)rejectQuery.getResultList();
		
		if(reimbursementRejectionList != null && !reimbursementRejectionList.isEmpty()){
		
			reimbursementRejection = reimbursementRejectionList.get(0);
		}
		
		return reimbursementRejection;
	}
	
	public ReimbursementRejection submitProcessedReimbursementRejection(ReimbursementRejectionDto reimbursementRejectionDto){
		
	try {
		if(reimbursementRejectionDto != null && reimbursementRejectionDto.getKey() != null){
		
//			Query rejectQuery = entityManager.createNamedQuery("ReimbursementRejection.findByKey");
//			rejectQuery.setParameter("primaryKey",reimbursementRejectionDto.getKey());
//			
//			List<ReimbursementRejection> reimbursementRejectionList = (List<ReimbursementRejection>)rejectQuery.getResultList();
//			if(reimbursementRejectionList != null && !reimbursementRejectionList.isEmpty()){			
//			ReimbursementRejection reimbursementRejection = entityManager.find(ReimbursementRejection.class, reimbursementRejectionDto.getKey());
//			ReimbursementRejection reimbursementRejection = reimbursementRejectionList.get(0);
			
			ReimbursementRejection reimbursementRejection = findRejectionByKey(reimbursementRejectionDto.getKey());
			if(reimbursementRejection != null){
				Reimbursement reimbursement = reimbursementRejection.getReimbursement(); 
				
				if(reimbursement != null && reimbursement.getKey() != null){
					Claim claim = reimbursement.getClaim();				
					
//					Query reimbQuery = entityManager.createNamedQuery("Reimbursement.findByKey");
//					reimbQuery.setParameter("primaryKey", reimbursement.getKey());
//					List<Reimbursement> reimbList = reimbQuery.getResultList();
//					
//					Reimbursement reimbObj = reimbList.get(0);
//					reimbursement = entityManager.find(Reimbursement.class, reimbursement.getKey());

				Reimbursement reimbObj = getReimbursementByKey(reimbursement.getKey());				
				entityManager.refresh(reimbObj);
				
				Status status = null;
				Stage stage = reimbObj.getStage();
				
				if(reimbursementRejectionDto.getRejCategSelectValue() != null){
					MastersValue rejectionCategory = new MastersValue();
					rejectionCategory.setKey(reimbursementRejectionDto.getRejCategSelectValue().getId());
					rejectionCategory.setValue(reimbursementRejectionDto.getRejCategSelectValue().getValue());
					reimbursementRejection.setRejectionCategory(rejectionCategory);
				}
				
				if(reimbursementRejectionDto.getRejSubCategSelectValue() != null){
					MastersValue rejectionSubCategory = new MastersValue();
					rejectionSubCategory.setKey(reimbursementRejectionDto.getRejSubCategSelectValue().getId());
					rejectionSubCategory.setValue(reimbursementRejectionDto.getRejSubCategSelectValue().getValue());
//					reimbursementRejection.setRejectionSubCategory(rejectionSubCategory);
					reimbursement.setRejSubCategoryId(reimbursementRejectionDto.getRejSubCategSelectValue().getId());
				}
				
//			if(reimbursementRejectionDto.getRejectionLetterRemarks() != null){
			if(reimbursementRejectionDto.getStatusValue() != null && !reimbursementRejectionDto.getStatusValue().isEmpty() && SHAConstants.REJECTION_APPROVE_OUT_COME.equalsIgnoreCase(reimbursementRejectionDto.getStatusValue())){
				
			/*if(reimbursementRejectionDto.getRejectionLetterRemarks() != null){
				reimbursementRejection.setRejectionLetterRemarks(reimbursementRejectionDto.getRejectionLetterRemarks());*/
				reimbursementRejection.setApprovedRejectionDate(new Date());
			/*	if(reimbursementRejectionDto.getRejectionRemarks() != null){
					reimbursementRejection.setRejectionRemarks(reimbursementRejectionDto.getRejectionRemarks());
				}*/
				/*if(reimbursementRejectionDto.getRejectionRemarks2() != null){
					reimbursementRejection.setRejectionRemarks2(reimbursementRejectionDto.getRejectionRemarks2());
				}
				
				if(reimbursementRejectionDto.getRejectionLetterRemarks2() != null){
					reimbursementRejection.setRejectionLetterRemarks2(reimbursementRejectionDto.getRejectionLetterRemarks2());
				}*/
				String rejectRemarks1 =reimbursementRejectionDto.getRejectionLetterRemarks();
				String subString1 = "";
				String subString2 = "";
				String rejectionRemarks="";
				String rejectionRemarks2 ="";
				System.out.println(String.format("Reject Remarks Length [%s]", rejectRemarks1.length()));
				if(rejectRemarks1.length() > 4000){
					 subString1 = rejectRemarks1.substring(0,4000);
					 subString2 = rejectRemarks1.substring(4000,8000);
					 
					 reimbursementRejectionDto.setRejectionLetterRemarks(subString1);
					 reimbursementRejectionDto.setRejectionLetterRemarks2(subString2);
					 rejectionRemarks = reimbursementRejectionDto.getRejectionLetterRemarks();
					  rejectionRemarks2 =reimbursementRejectionDto.getRejectionLetterRemarks2();
					 System.out.println(String.format("Reject Remarks 1 [%s]", rejectionRemarks.length()));
					 System.out.println(String.format("Reject Remarks 2 [%s]", rejectionRemarks2.length()));
				
				if(rejectionRemarks!=null){
					
					reimbursementRejection.setRejectionLetterRemarks(rejectionRemarks);
				}
				
				if(rejectionRemarks2!=null){
					
					reimbursementRejection.setRejectionLetterRemarks2(rejectionRemarks2);
				}
				}else {
					reimbursementRejection.setRejectionLetterRemarks(rejectRemarks1);
				}
				
				if(reimbObj.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY)){
					
					status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS);	
				}
				else if(reimbObj.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY)){
					status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS);
				
				}
				else if(reimbObj.getStage().getKey().equals(ReferenceTable.CLAIM_APPROVAL_STAGE)){
					
					status = entityManager.find(Status.class, ReferenceTable.CLAIM_APPROVAL_APPROVE_REJECT_STATUS);	
				}
				
				
				/*if((reimbObj.getDocAcknowLedgement().getReconsiderationRequest() != null && 
						SHAConstants.N_FLAG.equalsIgnoreCase(reimbObj.getDocAcknowLedgement().getReconsiderationRequest())) ||
						(reimbObj.getDocAcknowLedgement().getReconsiderationRequest() != null && 
								SHAConstants.YES_FLAG.equalsIgnoreCase(reimbObj.getDocAcknowLedgement().getReconsiderationRequest()) &&
						reimbObj.getDocAcknowLedgement().getReconsiderationReasonId() != null && 
						!ReferenceTable.SETTLED_RECONSIDERATION_ID.equals(reimbObj.getDocAcknowLedgement().getReconsiderationReasonId().getKey()))){
				
					if(reimbObj.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY)){
						
						status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS);	
					}
					else if(reimbObj.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY)){
						status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS);
					
					}
					else if(reimbObj.getStage().getKey().equals(ReferenceTable.CLAIM_APPROVAL_STAGE)){
						
						status = entityManager.find(Status.class, ReferenceTable.CLAIM_APPROVAL_APPROVE_REJECT_STATUS);	
					}
				}
				else{
					stage = entityManager.find(Stage.class, ReferenceTable.FINANCIAL_STAGE);
					status = entityManager.find(Status.class, ReferenceTable.REIMBURSEMENT_SETTLED_STATUS);
					reimbursement.setStage(stage);
				}*/
			
				if(ReferenceTable.RECEIVED_FROM_INSURED.equals(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getKey()) &&
						((SHAConstants.DEATH_FLAG).equalsIgnoreCase(reimbursementRejectionDto.getReimbursementDto().getClaimDto().getIncidenceFlagValue())
						||(reimbursement.getPatientStatus() != null && ((ReferenceTable.PATIENT_STATUS_DECEASED).equals(reimbursement.getPatientStatus().getKey()) ||
						(ReferenceTable.PATIENT_STATUS_DECEASED_REIMB).equals(reimbursement.getPatientStatus().getKey()))
						&& reimbursementRejectionDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
						&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(reimbursementRejectionDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId())))) { 
					
					if(reimbursementRejectionDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList() != null &&
							!reimbursementRejectionDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList().isEmpty()){
						saveNomineeDetails(reimbursementRejectionDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList(), reimbursement);
						if(reimbursementRejectionDto.getReimbursementDto().getIsNomineeDeceased() != null
								&& !reimbursementRejectionDto.getReimbursementDto().getIsNomineeDeceased()){
							updateLegalHeir(reimbursementRejectionDto);
						}
					}					
					else{
						/*reimbursement.setNomineeName(reimbursementRejectionDto.getReimbursementDto().getNomineeName());
						reimbursement.setNomineeAddr(reimbursementRejectionDto.getReimbursementDto().getNomineeAddr());*/
						
						if(reimbursementRejectionDto.getReimbursementDto().getLegalHeirDTOList() != null && !reimbursementRejectionDto.getReimbursementDto().getLegalHeirDTOList().isEmpty()) {
							saveLegalHeirAndDocumentDetails(reimbursementRejectionDto);
						}
					}	
				}
				
				
			//}	
				String reconsideration ="N";
				
				DocAcknowledgement ackDoc = reimbObj.getDocAcknowLedgement();
				
				if(ackDoc != null && ackDoc.getKey() != null){
					
					ackDoc = entityManager.find(DocAcknowledgement.class, ackDoc.getKey());
					
					if(ackDoc != null){
						
						if(("Y").equalsIgnoreCase(reimbObj.getReconsiderationRequest())){
							
							ackDoc.setReconsiderationRequest(reconsideration);
						}	
					
					entityManager.merge(ackDoc);
					entityManager.flush();
					log.info("------DocAcknowledgement------>"+ackDoc+"<------------");
					entityManager.refresh(ackDoc);
					}					
				}				
			
			}	
			
//			else if(reimbursementRejectionDto.getDisapprovedRemarks() != null){
			else if(reimbursementRejectionDto.getStatusValue() != null && !reimbursementRejectionDto.getStatusValue().isEmpty() && SHAConstants.REJECTION_DISAPPROVE_OUT_COME.equalsIgnoreCase(reimbursementRejectionDto.getStatusValue())){
				reimbursementRejection.setDisapprovedRemarks(reimbursementRejectionDto.getDisapprovedRemarks());
				reimbursementRejection.setDisapprovedDate(new Date());
			
				if(reimbursement.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY)){
			    	status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_REQUEST_DISAPPROVE_REJECT_STATUS);
			    }
			    else if(reimbursement.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY)){
			    	status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_FINANCIAL_DISAPPROVE_REJECT_STATUS);
			    }
			    else if(reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_APPROVAL_STAGE)){
			    	status = entityManager.find(Status.class, ReferenceTable.CLAIM_APPROVAL_DISAPPROVE_REJECT_STATUS);
			    }
			
			}
//			else if(reimbursementRejectionDto.getRedraftRemarks() != null){
			else if(reimbursementRejectionDto.getStatusValue() != null && !reimbursementRejectionDto.getStatusValue().isEmpty() && SHAConstants.REJECTION_REDRAFT_OUT_COME.equalsIgnoreCase(reimbursementRejectionDto.getStatusValue())){
				reimbursementRejection.setRedraftRemarks(reimbursementRejectionDto.getRedraftRemarks());
				reimbursementRejection.setRedraftDate(new Date());
				
				if(reimbursement.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY)){
			    	status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_REQUEST_REDRAFT_REJECT_STATUS);
			    }
			    else if(reimbursement.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY)){
			    	status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_FINANCIAL_REDRAFT_REJECT_STATUS);
			    }
			    else if(reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_APPROVAL_STAGE)){
			    	status = entityManager.find(Status.class, ReferenceTable.CLAIM_APPROVAL_REDRAFT_REJECT_STATUS);
			    }
				
			}
				
			String strUserName = reimbursementRejectionDto.getUsername();
			String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
			claim = entityManager.find(Claim.class,claim.getKey());
			if(status != null){
				reimbursement.setStatus(status);
				
				//CR-R1044
				Claim claimObj = entityManager.find(Claim.class, reimbursementRejectionDto.getReimbursementDto().getClaimDto().getKey());
				claimObj.setDataOfAdmission(reimbursementRejectionDto.getReimbursementDto().getClaimDto().getAdmissionDate());
				claimObj.setDataOfDischarge(reimbursementRejectionDto.getReimbursementDto().getClaimDto().getDischargeDate());
				entityManager.merge(claimObj);

				reimbursement.setDateOfAdmission(reimbursementRejectionDto.getReimbursementDto().getClaimDto().getAdmissionDate());
				reimbursement.setDateOfDischarge(reimbursementRejectionDto.getReimbursementDto().getClaimDto().getDischargeDate());
				
				reimbursement.setModifiedBy(userNameForDB);
				reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(reimbursement);
				entityManager.flush();
				entityManager.clear();
				log.info("------Reimbursement------>"+reimbursement+"<------------");
				
//				claim.setStatus(status);				
			}			
			
//			claim.setStage(stage);
//			claim.setModifiedDate(new Timestamp(System.currentTimeMillis()));	
//			entityManager.merge(claim);
//			entityManager.flush();
//			entityManager.refresh(claim);
			
			reimbursementRejection.setModifiedBy(userNameForDB);
			reimbursementRejection.setModifiedDate(new Timestamp(System.currentTimeMillis()));	
			reimbursementRejection.setReimbursement(reimbursement);
			reimbursementRejection.setStage(stage);
			reimbursementRejection.setStatus(status);
			
			String docToken = null;
//			if(null != status && !(ReferenceTable.CLAIM_APPROVAL_DISAPPROVE_REJECT_STATUS.equals(status.getKey()) || ReferenceTable.PROCESS_CLAIM_REQUEST_DISAPPROVE_REJECT_STATUS.equals(status.getKey()) || ReferenceTable.PROCESS_CLAIM_FINANCIAL_DISAPPROVE_REJECT_STATUS.equals(status.getKey())))
			if(reimbursementRejectionDto.getStatusValue() != null && !reimbursementRejectionDto.getStatusValue().isEmpty() && SHAConstants.REJECTION_APPROVE_OUT_COME.equalsIgnoreCase(reimbursementRejectionDto.getStatusValue()))
			{
				if(null != reimbursementRejectionDto.getDocFilePath() && !("").equalsIgnoreCase(reimbursementRejectionDto.getDocFilePath()))
				{
					WeakHashMap dataMap = new WeakHashMap();
					if(null != reimbursementRejectionDto.getReimbursementDto().getClaimDto())
					{
						dataMap.put("intimationNumber",reimbursementRejectionDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getIntimationId());
						dataMap.put("claimNumber",reimbursementRejectionDto.getReimbursementDto().getClaimDto().getClaimId());
						if(reimbursementRejectionDto.getReimbursementDto() != null && reimbursementRejectionDto.getReimbursementDto().getRodNumber() != null)
						{
							dataMap.put("reimbursementNumber", reimbursementRejectionDto.getReimbursementDto().getRodNumber());
						}
						dataMap.put("filePath", reimbursementRejectionDto.getDocFilePath());
						dataMap.put("docType", reimbursementRejectionDto.getDocType());
						dataMap.put("docSources", SHAConstants.PROCESS_DRAFT_REJECTION);
						dataMap.put("createdBy", userNameForDB);
						docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
						SHAUtils.setClearReferenceData(dataMap);
					}
				}

				reimbursementRejection.setFileToken(null != docToken ? docToken: null);
			}
			
			//submitProcessDarftRejectionLetterBpmTask(reimbursementRejection,reimbursementRejectionDto);
			submitProcessDarftRejectionLetterDBTask(reimbursementRejection,reimbursementRejectionDto);
			
			entityManager.merge(reimbursementRejection);
			entityManager.flush();
			entityManager.clear();
			log.info("------ReimbursementRejection------>"+reimbursementRejection+"<------------");
//			entityManager.refresh(reimbursementRejection);

			return reimbursementRejection;
			}
		
		 }
		}	
	}
	catch(Exception e){
		e.printStackTrace();
		log.error(e.toString());
	}
		return null;
	
	}
	
	public Reimbursement updateReimbursementAmount(Reimbursement reimbursement){
		if(reimbursement != null){
			reimbursement.setCurrentProvisionAmt(0d);
			reimbursement.setApprovedAmount(0d);
			reimbursement.setBillingApprovedAmount(0d);
			reimbursement.setFinancialApprovedAmount(0d);		
			reimbursement.setOtherBenefitApprovedAmt(0d);
			reimbursement.setAddOnCoversApprovedAmount(0d);
			reimbursement.setOptionalApprovedAmount(0d);
			resetReimbursementBenefitAmount(reimbursement);
			if(null != reimbursement && (SHAConstants.PA_TYPE).equalsIgnoreCase(reimbursement.getProcessClaimType()))
			{
				reimbursement.setBenApprovedAmt(0d);
				reimbursement.setAddOnCoversApprovedAmount(0d);
				reimbursement.setOptionalApprovedAmount(0d);
			}
			
			entityManager.merge(reimbursement);
			entityManager.flush();
		}
		return reimbursement;
	}
	
	public Reimbursement setReimbursementOtherApprovedAmt(Reimbursement reimbursement){
		
		if(reimbursement != null){
		
			Claim objClaim  = reimbursement.getClaim();
			
			
			/**
			 * This is a common service for both PA and health.
			 * Add on cover and optional cover is eligible
			 * only for PA. Hence if lob type is PA only, the below
			 * code will be invoked.
			 * 
			 * */
		/*	if(null != objClaim && null != objClaim.getIntimation() 
					&& null != objClaim.getIntimation().getLobId() && ReferenceTable.PA_LOB_KEY.equals(objClaim.getIntimation().getLobId().getKey()))*/
			
			/*if(reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)){
				reimbursement.setApprovedAmount(0d);
				reimbursement.setBillingApprovedAmount(0d);
				reimbursement.setFinancialApprovedAmount(0d);
				reimbursement.setBenApprovedAmt(0d);
			}
			else if(reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_APPROVE_REJECT_STATUS)){
				reimbursement.setApprovedAmount(0d);
				reimbursement.setBillingApprovedAmount(0d);
				reimbursement.setFinancialApprovedAmount(0d);
				reimbursement.setBenApprovedAmt(0d);
			}
			
//			reimbursement.setBillingApprovedAmount(0d);
			else if(reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS)){
				reimbursement.setFinancialApprovedAmount(0d);
				reimbursement.setBillingApprovedAmount(0d);
				reimbursement.setApprovedAmount(0d);
				reimbursement.setBenApprovedAmt(0d);
			}*/
			
			
			
			
			//Claim objClaim  = reimbursement.getClaim();
			
			
			/**
			 * This is a common service for both PA and health.
			 * Add on cover and optional cover is eligible
			 * only for PA. Hence if lob type is PA only, the below
			 * code will be invoked.
			 * 
			 * */
			if(null != objClaim && null != objClaim.getIntimation() 
					&& null != objClaim.getIntimation().getLobId() && (ReferenceTable.PA_LOB_KEY.equals(objClaim.getIntimation().getLobId().getKey()) ||
							ReferenceTable.PACKAGE_MASTER_VALUE.equals(objClaim.getIntimation().getLobId().getKey())))
			{
				List<PAAdditionalCovers> addOnCoversList = getAdditionalCoversByRodKey(reimbursement.getKey());
				if(null != addOnCoversList && !addOnCoversList.isEmpty())
				{
					for (PAAdditionalCovers paAdditionalCovers : addOnCoversList) {
						paAdditionalCovers.setProvisionAmount(0d);
						entityManager.merge(paAdditionalCovers);
						entityManager.flush();
					}
				}
				
				List<PAOptionalCover> optionalCoversList = getOptionalCoversByRodKey(reimbursement.getKey());
				if(null != optionalCoversList && !optionalCoversList.isEmpty())
				{
					for (PAOptionalCover paOptionalCovers : optionalCoversList) {
						paOptionalCovers.setProvisionAmount(0d);						
						if(null != reimbursement && null != reimbursement.getStatus() && 
								((ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS.equals(reimbursement.getStatus().getKey())) ||
								(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS.equals(reimbursement.getStatus().getKey())) ||
								(ReferenceTable.CLAIM_APPROVAL_APPROVE_REJECT_STATUS.equals(reimbursement.getStatus().getKey()))))
						{
						paOptionalCovers.setPayableDays(0l);
						}
						entityManager.merge(paOptionalCovers);
						entityManager.flush();
					}
				}
			}
			
			
			entityManager.merge(reimbursement);
			entityManager.flush();
			entityManager.clear();
			log.info("------Reimbursement------>"+reimbursement+"<------------");
			
			List<Procedure> procedureForReimbursmentRejection = getProcedureForReimbursmentRejection(reimbursement);
			for (Procedure procedure : procedureForReimbursmentRejection) {
				procedure.setProcessFlag("T");
//				Double approvedAmount = procedure.getApprovedAmount();
//				procedure.setDiffAmount(approvedAmount);
//				procedure.setApprovedAmount(0d);
				entityManager.merge(procedure);
				entityManager.flush();
				entityManager.clear();
				log.info("------Procedure------>"+procedure+"<------------");
			}
			
			List<PedValidation> pedValidation = findPedValidationByReimbursmentRejection(reimbursement);
			for (PedValidation pedValidation2 : pedValidation) {
				pedValidation2.setProcessFlag("T");
				entityManager.merge(pedValidation2);
				entityManager.flush();
				entityManager.clear();
				log.info("------PedValidation------>"+pedValidation2+"<------------");
			}
		}
		
		return reimbursement;
	}
	
	/*
	 * Added for rejection scenario.
	 * **/
	
	public List<PAAdditionalCovers> getAdditionalCoversByRodKey(Long reimbKey)
	{
		Query query = entityManager.createNamedQuery("PAAdditionalCovers.findByRodKey");
		query = query.setParameter("rodKey", reimbKey);
		List<PAAdditionalCovers> addCoverList = query.getResultList();
		if(null != addCoverList && !addCoverList.isEmpty())
		{
			return addCoverList;
		}
		return null;
	}
	
	private List<PAOptionalCover> getOptionalCoversByRodKey(Long reimbKey)
	{
		Query query = entityManager.createNamedQuery("PAOptionalCover.findByRodKey");
		query = query.setParameter("rodKey", reimbKey);
		List<PAOptionalCover> optionalCoverList = query.getResultList();
		if(null != optionalCoverList && !optionalCoverList.isEmpty())
		{
			return optionalCoverList;
		}
		return null;
	}
	
	
	public List<Procedure> getProcedureForReimbursmentRejection(Reimbursement reimbursement){
		
			Query query = entityManager
					.createNamedQuery("Procedure.findByPreauthKey");
			query.setParameter("preauthKey", reimbursement.getKey());
			@SuppressWarnings("unchecked")
			List<Procedure> resultList = (List<Procedure>) query.getResultList();
			if(resultList != null && !resultList.isEmpty()) {
				for (Procedure procedure : resultList) {
					entityManager.refresh(procedure);
				}
			}
			return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<PedValidation> findPedValidationByReimbursmentRejection(Reimbursement reimbursement) {
		Query query = entityManager
				.createNamedQuery("PedValidation.findByPreauthKey");
		query.setParameter("preauthKey", reimbursement.getKey());
		List<PedValidation> resultList = (List<PedValidation>) query
				.getResultList();
		
		if(resultList != null && !resultList.isEmpty()) {
			for (PedValidation pedValidation : resultList) {
				entityManager.refresh(pedValidation);
			}
		}
		return resultList;
	}
	
	
	
	public void updateReimbursementRejection(ReimbursementRejectionDto reimbursementRejectionDto){
		try{
		
		if(reimbursementRejectionDto.getKey() != null){
//			ReimbursementRejection reimbursementRejection = entityManager.find(ReimbursementRejection.class, reimbursementRejectionDto.getKey());
			ReimbursementRejection reimbursementRejection = findRejectionByKey(reimbursementRejectionDto.getKey());
			if(reimbursementRejection != null){
			Reimbursement reimbursement = reimbursementRejection.getReimbursement(); 
			if(reimbursement != null && reimbursement.getKey() != null){
//				reimbursement = entityManager.find(Reimbursement.class, reimbursement.getKey());
				reimbursement = getReimbursementByKey(reimbursement.getKey());
				if(reimbursement != null)
				entityManager.refresh(reimbursement);
			}
			if(reimbursement != null){
			Status status = null;
			Stage stage = reimbursement.getStage();
			
			 System.out.println(String.format("Reject Letter Remarks 1 in updateReimbursementRejection [%s]", reimbursementRejectionDto.getRejectionLetterRemarks().length()));
			 
			 String rejectLetterRemarks1 = reimbursementRejectionDto.getRejectionLetterRemarks();
				String subString1 = "";
				String subString2 = "";
				System.out.println(String.format("Reject Remarks Length [%s]", rejectLetterRemarks1.length()));
				if(rejectLetterRemarks1.length() > 4000){
					 subString1 = rejectLetterRemarks1.substring(0,4000);
					 subString2 = rejectLetterRemarks1.substring(4000,8000);
					 
					 reimbursementRejectionDto.setRejectionLetterRemarks(subString1);
					 reimbursementRejectionDto.setRejectionLetterRemarks2(subString2);
					 String rejectionLetterRemarks = reimbursementRejectionDto.getRejectionLetterRemarks();
					 String rejectionLetterRemarks2 =reimbursementRejectionDto.getRejectionLetterRemarks2();
					 System.out.println(String.format("Reject Letter Remarks 1 [%s]", rejectionLetterRemarks.length()));
					 System.out.println(String.format("Reject Letter Remarks 2 [%s]", rejectionLetterRemarks2.length()));
				}
			reimbursementRejection.setRejectionLetterRemarks(reimbursementRejectionDto.getRejectionLetterRemarks());
			reimbursementRejection.setRejectionLetterRemarks2(reimbursementRejectionDto.getRejectionLetterRemarks2());
			
		if(reimbursementRejectionDto.getRejCategSelectValue() != null){
			MastersValue rejectionCategory = new MastersValue();
			rejectionCategory.setKey(reimbursementRejectionDto.getRejCategSelectValue().getId());
			rejectionCategory.setValue(reimbursementRejectionDto.getRejCategSelectValue().getValue());
			reimbursementRejection.setRejectionCategory(rejectionCategory);
		}
		
		if(reimbursementRejectionDto.getRejSubCategSelectValue() != null){
			MastersValue rejectionSubCategory = new MastersValue();
			rejectionSubCategory.setKey(reimbursementRejectionDto.getRejSubCategSelectValue().getId());
			rejectionSubCategory.setValue(reimbursementRejectionDto.getRejSubCategSelectValue().getValue());
//			reimbursementRejection.setRejectionSubCategory(rejectionSubCategory);
			reimbursement.setRejSubCategoryId(reimbursementRejectionDto.getRejSubCategSelectValue().getId());
		}
		
		if(reimbursement.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY)){
	    	status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_REQUEST_DRAFT_REJECT_STATUS);
	    }
	    else if(reimbursement.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY)){
	    	status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_FINANCIAL_DRAFT_REJECT_STATUS);
	    }
	    else if(reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_APPROVAL_STAGE)){
	    	status = entityManager.find(Status.class, ReferenceTable.CLAIM_APPROVAL_DRAFT_REJECT_STATUS);
	    }
		
		String strUserName = reimbursementRejectionDto.getUsername();
		String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
		
		if(status != null){
			//CR-R1044
			Claim claim = entityManager.find(Claim.class, reimbursementRejectionDto.getReimbursementDto().getClaimDto().getKey());
			claim.setDataOfAdmission(reimbursementRejectionDto.getReimbursementDto().getClaimDto().getAdmissionDate());
			claim.setDataOfDischarge(reimbursementRejectionDto.getReimbursementDto().getClaimDto().getDischargeDate());
			entityManager.merge(claim);
			
			
			if(ReferenceTable.RECEIVED_FROM_INSURED.equals(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getKey()) &&
					((SHAConstants.DEATH_FLAG).equalsIgnoreCase(reimbursementRejectionDto.getReimbursementDto().getClaimDto().getIncidenceFlagValue())
					||(reimbursement.getPatientStatus() != null 
								&& ((ReferenceTable.PATIENT_STATUS_DECEASED).equals(reimbursement.getPatientStatus().getKey()) 
										|| (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB).equals(reimbursement.getPatientStatus().getKey())) 
								&& reimbursementRejectionDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
								&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(reimbursementRejectionDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())))) { 
				
				if(reimbursementRejectionDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList() != null &&
						!reimbursementRejectionDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList().isEmpty()){
					saveNomineeDetails(reimbursementRejectionDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList(), reimbursement);
					if(reimbursementRejectionDto.getReimbursementDto().getIsNomineeDeceased() != null
							&& !reimbursementRejectionDto.getReimbursementDto().getIsNomineeDeceased()){
						updateLegalHeir(reimbursementRejectionDto);
					}
				}					
				else{
					/*reimbursement.setNomineeName(reimbursementRejectionDto.getReimbursementDto().getNomineeName());
					reimbursement.setNomineeAddr(reimbursementRejectionDto.getReimbursementDto().getNomineeAddr());*/
					
					if(reimbursementRejectionDto.getReimbursementDto().getLegalHeirDTOList() != null && !reimbursementRejectionDto.getReimbursementDto().getLegalHeirDTOList().isEmpty()) {
						saveLegalHeirAndDocumentDetails(reimbursementRejectionDto);
					}
				}	
			}
						
			
			reimbursement.setStatus(status);
			reimbursement.setDateOfAdmission(reimbursementRejectionDto.getReimbursementDto().getClaimDto().getAdmissionDate());
			reimbursement.setDateOfDischarge(reimbursementRejectionDto.getReimbursementDto().getClaimDto().getDischargeDate());
			
			reimbursement.setModifiedBy(userNameForDB);
			reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(reimbursement);
			entityManager.flush();
			entityManager.clear();
			log.info("------Reimbursement------>"+reimbursement+"<------------");
		}
		
//		Claim claim = reimbursement.getClaim();
//		claim.setStage(stage);
//		claim.setStatus(status);
//		claim.setModifiedDate(new Timestamp(System.currentTimeMillis()));	
//		entityManager.merge(claim);
//		entityManager.flush();
		
		
		reimbursementRejection.setReimbursement(reimbursement);
		reimbursementRejection.setStage(stage);
		reimbursementRejection.setStatus(status);
		reimbursementRejection.setRejectionDraftDate(new Date());
		reimbursementRejection.setModifiedBy(userNameForDB);
		reimbursementRejection.setModifiedDate(new Timestamp(System.currentTimeMillis()));	
		entityManager.merge(reimbursementRejection);
		entityManager.flush();
		entityManager.clear();
		log.info("------ReimbursementRejection------>"+reimbursementRejection+"<------------");
		
	//	submitDarftRejectionLetterBpmTask(reimbursementRejectionDto);
		submitDarftRejectionLetterDBTask(reimbursementRejectionDto);
		
		}
		}
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ReimbursementRejectionDetailsDto> getRejectionTableDto(Long reimbKey){
		
		List<ReimbursementRejectionDetailsDto> rejectionDetailsList = new ArrayList<ReimbursementRejectionDetailsDto>();
		try{		
		if(reimbKey != null){
		
			Query query = entityManager.createNamedQuery("ReimbursementRejection.findByReimbursementKey");
			query.setParameter("reimbursementKey", reimbKey);
				
			List<ReimbursementRejection> rejectionDetails = (List<ReimbursementRejection>)query.getResultList();
			
			if(rejectionDetails != null && ! rejectionDetails.isEmpty()){
			
				for(ReimbursementRejection rejection : rejectionDetails){
					RejectionDetailsMapper  rejectionDetailsMapper = new RejectionDetailsMapper();
					entityManager.refresh(rejection);
					ReimbursementRejectionDetailsDto rejectionDetailsDto =  rejectionDetailsMapper.getReimbursementRejectionDetailsDto(rejection);
					Intimation intimation = rejection.getReimbursement().getClaim().getIntimation();
					Hospitals hospital = entityManager.find(Hospitals.class, intimation.getHospital());
					if(hospital != null){
						rejectionDetailsDto.setHospitalName(hospital.getName());
						rejectionDetailsDto.setHospitalAddress(hospital.getAddress());
						rejectionDetailsDto.setHospitalCity(hospital.getCity());
					}
					
					String diagnosis = getDiagnosis(rejection.getReimbursement().getKey(),entityManager);
					rejectionDetailsDto.setDiagnosis(diagnosis);
					rejectionDetailsList.add(rejectionDetailsDto);			
				}
				if(rejectionDetailsList != null && ! rejectionDetailsList.isEmpty()){
					for(int index = 0; index < rejectionDetailsList.size(); index++){
						rejectionDetailsList.get(index).setRejectionNo(index+1);
					}
					
			}
			}	
		}
		
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.toString());
		}
		return rejectionDetailsList;
	}	
	
	private String getDiagnosis(Long transacKey,EntityManager em){
		PEDValidationService pedService = new PEDValidationService();
		StringBuffer diag =new StringBuffer();
		try{
		List<PedValidation> pedValidationList = pedService.getDiagnosisList(transacKey, em);
		if(pedValidationList != null && !pedValidationList.isEmpty()){
			for (PedValidation pedValidation : pedValidationList) {
				Diagnosis diagnosis = em.find(Diagnosis.class, pedValidation.getDiagnosisId());
		        if(diagnosis != null){
		        	if(("").equals(diag)){
		        		diag.append(diagnosis.getValue());
		        	}
		        	else{
		        		diag.append(" / ").append(diagnosis.getValue());
		        	}
		        		
		        }
			}
		}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.toString());
		}
		return diag.toString();
	}
	
	/*private void submitDarftRejectionLetterBpmTask(
			ReimbursementRejectionDto reimbursementRejectionDto) {
		
		reimbursementRejectionDto.getHumanTask().setOutcome(SHAConstants.REJECTION_APPROVE_OUT_COME);			
		SubmitDraftRejectionLetterTask submitQueryTask = BPMClientContext.getSubmitDraftRejectionService(reimbursementRejectionDto.getUsername(),reimbursementRejectionDto.getPassword());
		
		try{
		submitQueryTask.execute(reimbursementRejectionDto.getUsername(),reimbursementRejectionDto.getHumanTask());
		}catch(Exception e){
			log.error(e.toString());
			e.printStackTrace();
		}
		
	}*/
	
	private void submitDarftRejectionLetterDBTask(
			ReimbursementRejectionDto reimbursementRejectionDto) {
		
	Map<String,Object> wrkFlowMap = (Map<String,Object>)reimbursementRejectionDto.getDbOutArray();		
	DBCalculationService dbCalculationService = new DBCalculationService();	
	wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_SUBMIT_DRAFT_REJECTION_LETTER);
	Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);		
	DBCalculationService dbCalService = new DBCalculationService();	
	dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
	}
	
	/*private void submitProcessDarftRejectionLetterBpmTask(
			ReimbursementRejection reimbursementRejection,ReimbursementRejectionDto reimbursementRejectionDto) {
		if(reimbursementRejection.getStatus() != null ){
			
			if(reimbursementRejection.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_DISAPPROVE_REJECT_STATUS) || reimbursementRejection.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_DISAPPROVE_REJECT_STATUS) || reimbursementRejection.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_DISAPPROVE_REJECT_STATUS))
			{
				reimbursementRejectionDto.getHumanTask().setOutcome(SHAConstants.REJECTION_DISAPPROVE_OUT_COME);			
				reimbursementRejectionDto.getHumanTask().getPayload().getClaimRequest().setResult(SHAConstants.REJECTION_DISAPPROVE_OUT_COME);
				reimbursementRejectionDto.getHumanTask().getPayload().getClaimRequest().setIsRejectionDisapproved(true);
				
			}
			else if(reimbursementRejection.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_REDRAFT_REJECT_STATUS ) || reimbursementRejection.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_REDRAFT_REJECT_STATUS) || reimbursementRejection.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_REDRAFT_REJECT_STATUS)){
				reimbursementRejectionDto.getHumanTask().setOutcome(SHAConstants.REJECTION_REDRAFT_OUT_COME);
				reimbursementRejectionDto.getHumanTask().getPayload().getClaimRequest().setResult(SHAConstants.REJECTION_REDRAFT_OUT_COME);
			}
			else if(reimbursementRejection.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_APPROVE_REJECT_STATUS) || reimbursementRejection.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS) || reimbursementRejection.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS) ){
			reimbursementRejectionDto.getHumanTask().setOutcome(SHAConstants.REJECTION_APPROVE_OUT_COME);
			reimbursementRejectionDto.getHumanTask().getPayload().getClaimRequest().setIsRejectionDisapproved(false);
			reimbursementRejectionDto.getHumanTask().getPayload().getClaimRequest().setResult(SHAConstants.REJECTION_APPROVE_OUT_COME);
			
			}
		}
				
		SubmitProcessDraftQueryLetterTask submitProcessDraftQueryTask = BPMClientContext.getSubmitProcessDraftQueryService(reimbursementRejectionDto.getUsername(),reimbursementRejectionDto.getPassword());
		
		try{
		submitProcessDraftQueryTask.execute(reimbursementRejectionDto.getUsername(),reimbursementRejectionDto.getHumanTask());
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.toString());
		}
	}
	*/
	private void submitProcessDarftRejectionLetterDBTask(
			ReimbursementRejection reimbursementRejection,ReimbursementRejectionDto reimbursementRejectionDto) {
		
		Map<String,Object> wrkFlowMap = (Map<String,Object>)reimbursementRejectionDto.getDbOutArray();		
		DBCalculationService dbCalculationService = new DBCalculationService();		
		
		if(reimbursementRejection.getStatus() != null && null != reimbursementRejection.getStage()) {
			
			if(reimbursementRejection.getStage().getKey().equals(ReferenceTable.MA_CORPORATE_QUERY_STAGE) || 
					reimbursementRejection.getStage().getKey().equals(ReferenceTable.FINANCIAL_QUERY_STAGE) || 
					reimbursementRejection.getStage().getKey().equals(ReferenceTable.CLAIM_APPROVAL_STAGE)  ||
					reimbursementRejection.getStage().getKey().equals(ReferenceTable.PAYMENT_PROCESS_STAGE)
					)
			{
				if(reimbursementRejection.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_DISAPPROVE_REJECT_STATUS)){
					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_DISAPPROVE_MA_REJECTION);
					wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER, SHAConstants.PARALLEL_MEDICAL_PENDING);
				}
				else if(reimbursementRejection.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_DISAPPROVE_REJECT_STATUS))
				{
					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_DISAPPROVE_FA_REJECTION);
				}
				else if(reimbursementRejection.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_DISAPPROVE_REJECT_STATUS))
				{
					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_DISAPPROVE_CLAIM_APPROVAL_REJECTION);
				}
				else if(reimbursementRejection.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_REDRAFT_REJECT_STATUS) || 
						reimbursementRejection.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_REDRAFT_REJECT_STATUS) ||
						reimbursementRejection.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_REDRAFT_REJECT_STATUS)){
					
					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_REDRAFT_REJECTION_LETTER);
				}
				else if(reimbursementRejection.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS) || 
						reimbursementRejection.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS)||
						reimbursementRejection.getStatus().getKey().equals(ReferenceTable.REIMBURSEMENT_SETTLED_STATUS)||
						reimbursementRejection.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_APPROVE_REJECT_STATUS)){
				
					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_APPROVE__PROCESS_DRAFT_REJECTION_LETTER);
				}
			}
			
			
		}
				
		Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);		
		DBCalculationService dbCalService = new DBCalculationService();	
		dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
	}
	
	public void submitAllowRejectionReconsider(SearchAllowReconsiderationTableDTO submitAllowDto) {
		
		if(submitAllowDto.getRodNo() != null) {
			
			//Reimbursement reimbursementObject = getReimbursementObject(submitAllowDto.getRodNo());
			ReimbursementRejection rejectionDetails = getRejectionKeyByReimbursement(submitAllowDto.getRodKey());
			if(rejectionDetails != null){
				rejectionDetails.setReconUncheckRemarks(submitAllowDto.getUnCheckRemarks());
				if(submitAllowDto.getAllowReconsiderFlag() != null){
					rejectionDetails.setAllowReconsideration(submitAllowDto.getAllowReconsiderFlag());
				}
				entityManager.merge(rejectionDetails);
				entityManager.flush();
				entityManager.clear();
				log.info("------ReimbursementRejection------>"+rejectionDetails+"<------------");
			}
			
		}
		
	}
	
//	public List<ReimbursementRejectionDetailsDto> getRejectionListDto(Long reimbursementKey){
//		
//		try{
//		Query query = entityManager
//				.createNamedQuery("ReimbursementRejection.findByReimbursementKey");
//		query = query.setParameter("reimbursementKey", reimbursementKey);
//		
//		List<ReimbursementRejection> reimbursementRejectionList = (List<ReimbursementRejection> )query.getResultList();
//		
//		if(reimbursementRejectionList != null && !reimbursementRejectionList.isEmpty()){
//		
//		  for(ReimbursementRejection rejection : reimbursementRejectionList){
//		  Reimbursement reimbrsmnt = entityManager.find(Reimbursement.class, rejection.getReimbursement().getKey());
//		  RejectionDetailsMapper rejectionDetailsMapper = new RejectionDetailsMapper(); 
//
//		  rejectionDetailsMapper.getReimbursementRejectinDetailsDto(rejection);
//		  
//		  }
//		}
//		
//		
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		
//		return null;
//
//	
//	}
	
	public void resetReimbursementBenefitAmount(Reimbursement reimbursement){
		
		List<ReimbursementBenefits> reimbursementBenefitsByRodKey = getReimbursementBenefitsByRodKey(reimbursement.getKey());
		if(reimbursementBenefitsByRodKey != null){
		for (ReimbursementBenefits reimbursementBenefits : reimbursementBenefitsByRodKey) {
			reimbursementBenefits.setTotalClaimAmountBills(0d);
			reimbursementBenefits.setTotalAmount(0d);
			reimbursementBenefits.setPayableAmount(0d);
			entityManager.merge(reimbursementBenefits);
			entityManager.flush();
		}
		}
		
	}
	
	public List<ReimbursementBenefits> getReimbursementBenefitsByRodKey(
			Long rodKey) {
		Query query = entityManager
				.createNamedQuery("ReimbursementBenefits.findByRodKey");
		query = query.setParameter("rodKey", rodKey);

		List<ReimbursementBenefits> reimbBenefits = query.getResultList();
		if (reimbBenefits != null && !reimbBenefits.isEmpty()) {
			return reimbBenefits;
		}

		return reimbBenefits;
	}
	
	public Reimbursement getReimbursementObject(String rodNo)
	{
		Query query = entityManager.createNamedQuery("Reimbursement.findRodByNumberWise");
		query = query.setParameter("rodNumber", rodNo);
		List<Reimbursement> reimbursementObjectList = query.getResultList();
		if(null != reimbursementObjectList && !reimbursementObjectList.isEmpty())
		{
			entityManager.refresh(reimbursementObjectList.get(0));
			return reimbursementObjectList.get(0);
		}
		return null;
	}
	
	public void updateReimbursementCurrentProvisionAmt(Reimbursement reimbursement){
		entityManager.merge(reimbursement);
		entityManager.flush();
	}
	
	public void saveNomineeDetails(List<NomineeDetailsDto> nomineeDetailsList, Reimbursement reimbObj){
		
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
				//nomineeObj.setNomineeAge(Integer.valueOf(nomineeDetailsDto.getNomineeAge()));
				nomineeObj.setRelationshipWithProposer(nomineeDetailsDto.getNomineeRelationship());
				nomineeObj.setSharePercent(nomineeDetailsDto.getNomineePercent() != null && !nomineeDetailsDto.getNomineePercent().isEmpty() ?  Double.valueOf(nomineeDetailsDto.getNomineePercent().split("%")[0]) : null);
				//nomineeObj.setSharePercent(Double.valueOf(nomineeDetailsDto.getNomineePercent().split("%")[0]));
				nomineeObj.setPolicy(reimbObj.getClaim().getIntimation().getPolicy());
				nomineeObj.setPolicyNominee(getPolicyNomineebyKey(nomineeDetailsDto.getPolicyNomineeKey()));
				nomineeObj.setAppointeeName(nomineeDetailsDto.getAppointeeName());
				nomineeObj.setAppointeeAge(nomineeDetailsDto.getAppointeeAge() != null && !nomineeDetailsDto.getAppointeeAge().isEmpty() ? Integer.valueOf(nomineeDetailsDto.getAppointeeAge()) : null);
				nomineeObj.setAppointeeRelationship(nomineeDetailsDto.getAppointeeRelationship());
				nomineeObj.setCreatedBy(nomineeDetailsDto.getModifiedBy() != null && !nomineeDetailsDto.getModifiedBy().isEmpty() ? nomineeDetailsDto.getModifiedBy() : "SYSTEM");
				nomineeObj.setCreatedDate(new Date());
				nomineeObj.setActiveStatus(1);
				nomineeObj.setBankName(nomineeDetailsDto.getBankName());
				nomineeObj.setBankBranchName(nomineeDetailsDto.getBankBranchName());
			}
			
			nomineeObj.setInsured(reimbObj.getClaim().getIntimation().getInsured());
			nomineeObj.setIntimation(reimbObj.getClaim().getIntimation());
			nomineeObj.setClaim(reimbObj.getClaim());
			nomineeObj.setTransactionKey(reimbObj.getKey());
			nomineeObj.setSelectedFlag(nomineeDetailsDto.isSelectedNominee() ? ReferenceTable.YES_FLAG : ReferenceTable.NO_FLAG);
			nomineeObj.setTransactionType(reimbObj.getClaim().getClaimType() != null ? reimbObj.getClaim().getClaimType().getValue().toUpperCase() : ReferenceTable.REIMBURSEMENT_CLAIM);

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
	
	public void saveLegalHeirAndDocumentDetails(ReimbursementRejectionDto reimbursementRejectionDto) {
		ReimbursementDto rodDTO = reimbursementRejectionDto.getReimbursementDto();
		String strUserName = reimbursementRejectionDto.getUsername();
		String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
		if(rodDTO.getLegalHeirDTOList() != null && !rodDTO.getLegalHeirDTOList().isEmpty()) {
			LegalHeir legalHeir;
			for (LegalHeirDTO legalHeirDto : rodDTO.getLegalHeirDTOList()) {
				legalHeir = new LegalHeir();
				if(legalHeirDto.getLegalHeirKey() != null) {
					legalHeir.setKey(legalHeirDto.getLegalHeirKey());
				}
				
				legalHeir.setLegalHeirName(legalHeirDto.getHeirName());
				legalHeir.setRelationCode(legalHeirDto.getRelationship() != null ? legalHeirDto.getRelationship().getId() : null);
				legalHeir.setRelationDesc(legalHeirDto.getRelationship() != null ? legalHeirDto.getRelationship().getValue() : "");
				legalHeir.setSharePercentage(legalHeirDto.getSharePercentage());
				legalHeir.setAddress(legalHeirDto.getAddress());
				if(legalHeirDto.getAccountType() != null && legalHeirDto.getAccountType().getValue() != null) {
					legalHeir.setAccountType(legalHeirDto.getAccountType().getValue());
				}
				legalHeir.setBeneficiaryName(legalHeirDto.getBeneficiaryName());
				if(legalHeirDto.getAccountNo() != null && !legalHeirDto.getAccountNo().isEmpty()){
					legalHeir.setAccountNo(Long.valueOf(legalHeirDto.getAccountNo()));
				}
				legalHeir.setAccountPreference(legalHeirDto.getAccountPreference() != null ? legalHeirDto.getAccountPreference().getValue() : "");
				legalHeir.setPaymentModeId(legalHeirDto.getPaymentModeId());
				legalHeir.setPayableAt(legalHeirDto.getPayableAt());
				legalHeir.setBankName(legalHeirDto.getBankName());
				legalHeir.setBankBranchName(legalHeirDto.getBankBranchName());
				legalHeir.setIfscCode(legalHeirDto.getIfscCode());
				if (legalHeirDto.getDocumentToken() != null) {
					legalHeir.setUploadFlag(SHAConstants.YES_FLAG);
				} else {
					legalHeir.setUploadFlag(SHAConstants.N_FLAG);
				}
				if (legalHeirDto.getDocumentToken() != null) {
					legalHeir.setDocKey(legalHeirDto.getDocumentToken());
				}
				if(legalHeirDto.getDeleteLegalHeir() != null && legalHeirDto.getDeleteLegalHeir().equalsIgnoreCase(SHAConstants.YES_FLAG)) {
					legalHeir.setActiveStatus(SHAConstants.N_FLAG);
				}else{
					legalHeir.setActiveStatus(SHAConstants.YES_FLAG);
				}
				legalHeir.setRodKey(rodDTO.getKey());
	//			legalHeir.setIntimationKey(rodDTO.getNewIntimationDTO().getKey());
				legalHeir.setPolicyKey(rodDTO.getClaimDto().getNewIntimationDto().getPolicy().getKey());
				if(rodDTO.getClaimDto().getNewIntimationDto().getInsuredPatient() != null){
					legalHeir.setInsuredKey(rodDTO.getClaimDto().getNewIntimationDto().getInsuredPatient().getKey());
				}
				legalHeir.setPincode(legalHeirDto.getPincode());
				if(legalHeirDto.getLegalHeirKey() == null) {
					legalHeir.setCreatedBy(userNameForDB);
					legalHeir.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.persist(legalHeir);
				}	
				else {
					legalHeir.setModifiedBy(userNameForDB);
					legalHeir.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(legalHeir);
				}
				entityManager.flush();
				//persisting in document details table
				if(legalHeirDto.getDocumentToken() != null){
					DocumentDetails docDetails = new DocumentDetails();
					docDetails.setDocumentToken(legalHeirDto.getDocumentToken());
					docDetails.setIntimationNumber(rodDTO.getClaimDto().getNewIntimationDto().getIntimationId());
					docDetails.setClaimNumber(rodDTO.getClaimDto().getClaimId());
					//			docDetails.setReimbursementNumber(rodDTO.getRodNumberForUploadTbl());
					docDetails.setFileName(legalHeirDto.getFileName());
					docDetails.setDocumentType(SHAConstants.LEGAL_HEIR_CERT);
					docDetails.setRodKey(rodDTO.getKey());
					entityManager.persist(docDetails);
					entityManager.flush();
				}
			}
		}
	}
	
	public DiagnosisHospitalDetails getDiagnosisByID(Long key) {
		Query query = entityManager.createNamedQuery("DiagnosisHospitalDetails.findDiagnosisByKey");
		query=query.setParameter("findDiagnosisByKey", key);
		
		List<DiagnosisHospitalDetails> reimbursementList = query.getResultList();
		if(null != reimbursementList && !reimbursementList.isEmpty())
		{
			entityManager.refresh(reimbursementList.get(0));
			return reimbursementList.get(0);
		}
		return null;
	}
	
	private void updateLegalHeir(ReimbursementRejectionDto reimbursementRejectionDto){
		ReimbursementDto rodDTO = reimbursementRejectionDto.getReimbursementDto();
		String strUserName = reimbursementRejectionDto.getUsername();
		List<LegalHeir> list = getlegalHeirListByTransactionKey(rodDTO.getKey());
		if(list != null && !list.isEmpty()){
			for (LegalHeir legalHeir : list) {
				legalHeir.setModifiedBy(strUserName);
				legalHeir.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				legalHeir.setActiveStatus(SHAConstants.N_FLAG);
				entityManager.merge(legalHeir);
				entityManager.flush();
			}
		}
	}
	
	public List<LegalHeir> getlegalHeirListByTransactionKey(Long rodKey) {
		Query query = entityManager.createNamedQuery("LegalHeir.findByTransactionKey");
		query.setParameter("transacKey", rodKey);
		List<LegalHeir> resultList = query.getResultList();
		if(!resultList.isEmpty() && resultList != null) {
			return resultList;
		}
		return null;
		
	}
	
	public ReimbursementRejection findRejectionByStatus(Long intimationKey,Long statusKey){
		
		ReimbursementRejection reimbursementRejection = null;
		Query rejectQuery = entityManager.createNamedQuery("ReimbursementRejection.findByRejectStatus");
		rejectQuery.setParameter("intimationKey",intimationKey);
		rejectQuery.setParameter("statusKey",statusKey);
		
		List<ReimbursementRejection> reimbursementRejectionList = (List<ReimbursementRejection>)rejectQuery.getResultList();
		
		if(reimbursementRejectionList != null && !reimbursementRejectionList.isEmpty()){
		
			reimbursementRejection = reimbursementRejectionList.get(0);
			
		}
		
		return reimbursementRejection;
	}

}