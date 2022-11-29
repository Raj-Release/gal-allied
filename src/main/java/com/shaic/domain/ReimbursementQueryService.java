package com.shaic.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.DocAcknowledgementDto;
import com.shaic.claim.DocAcknowledgementMapper;
import com.shaic.claim.ReimbursementDto;
import com.shaic.claim.ReimbursementMapper;
import com.shaic.claim.ReimbursementQueryMapper;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.InvesAndQueryAndFvrParallelFlowTableDTO;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.reimbursement.queryrejection.draftquery.search.DraftQueryLetterDetailTableDto;

/**
 * 
 * @author Lakshminarayana
 *
 */
@Stateless
public class ReimbursementQueryService {

	@PersistenceContext
	protected EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public ReimbursementQueryDto getReimbursementQuery(Long primaryKey){
		
		ReimbursementQueryDto reimbursementQueryDto = null;
		ReimbursementQuery reimbursementQuery = null;
		
		reimbursementQuery = getReimbQueryObjByKey(primaryKey);
			
			ReimbursementQueryMapper rqMapper = new ReimbursementQueryMapper();
	
			try{
			if(reimbursementQuery != null){
			reimbursementQueryDto =  rqMapper.getReimbursementQueryDto(reimbursementQuery);
				
			List<QueryLetterDetails> draftQueryLetterDetailsList =  getDraftQueryLetterDetails(reimbursementQuery.getKey());
			
			List<DraftQueryLetterDetailTableDto> draftQueryLetterDetailsDtoList = getDraftQueryLetterDetailsDtoList(draftQueryLetterDetailsList);
			
			reimbursementQueryDto.setQueryDarftList(draftQueryLetterDetailsDtoList);
						
			List<QueryLetterDetails> redraftQueryLetterDetailsList =  getReDraftQueryLetterDetails(reimbursementQuery.getKey());
			List<DraftQueryLetterDetailTableDto> redraftQueryLetterDetailsDtoList = getDraftQueryLetterDetailsDtoList(redraftQueryLetterDetailsList);
			reimbursementQueryDto.setRedraftList(redraftQueryLetterDetailsDtoList);
			
			Reimbursement reimbrsmnt = entityManager.find(Reimbursement.class, reimbursementQueryDto.getReimbursementDto().getKey());
			
			ReimbursementMapper rmbsmntMapper = new ReimbursementMapper();
			ReimbursementDto reimbursementDto = rmbsmntMapper.getReimbursementDto(reimbrsmnt);
			
			Query q = entityManager.createNamedQuery("PedValidation.findByTransactionKey");
			q.setParameter("transactionKey", reimbursementDto.getKey());
			
			List<PedValidation> diagnosisList =  q.getResultList();
			String diagnosis = null;
			if(diagnosisList != null && ! diagnosisList.isEmpty()){
				diagnosis ="";
				for(PedValidation pedDiagnosis : diagnosisList){
				
					if(pedDiagnosis != null){
						if(pedDiagnosis.getDiagnosisId() != null){
	//						Diagnosis diag = entityManager.find(Diagnosis.class, pedDiagnosis.getDiagnosisId());
							String diag = getDiagnosisName(pedDiagnosis.getDiagnosisId());
							
							if(diag != null){
							diagnosis = !("").equalsIgnoreCase(diagnosis) ? diagnosis + " / " + diag : diag;
							}
						}						
					}
					
				}
			}
			if(diagnosis != null){
				reimbursementDto.setDiagnosis(diagnosis);
			}			
			
			Claim claim = reimbrsmnt.getClaim();
			
			ClaimMapper clmMapper =  ClaimMapper.getInstance();
			ClaimDto claimDto = clmMapper.getClaimDto(claim);
			
			if (claim.getIntimation().getPolicy().getHomeOfficeCode() != null) {
				 List<MasOmbudsman> ombudsmanOfficeList = getOmbudsmanOffiAddrByPIOCode(claim.getIntimation().getPolicy().getHomeOfficeCode());
				 if(ombudsmanOfficeList !=null && !ombudsmanOfficeList.isEmpty())
					 claimDto.setOmbudsManAddressList(ombudsmanOfficeList);
			 }
			
			Intimation intiamtion = claim.getIntimation();
			
			NewIntimationDto newIntimationDto = getIntimationDto(intiamtion);

			List<NomineeDetailsDto> nomineeList = (new IntimationService()).getNomineeDetailsListByTransactionKey(reimbrsmnt.getKey(),entityManager);
			reimbursementDto.setNomineeDeceasedFlag(reimbrsmnt.getNomineeFlag());
			if(nomineeList != null && !nomineeList.isEmpty())
			{	
				newIntimationDto.setNomineeList(nomineeList);
				//IMSSUPPOR-29423
				if((reimbrsmnt.getPatientStatus() != null 
						 && (ReferenceTable.PATIENT_STATUS_DECEASED.equals(reimbrsmnt.getPatientStatus().getKey())
								 || ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(reimbrsmnt.getPatientStatus().getKey()))
						 && reimbrsmnt.getClaim().getIntimation().getInsured().getRelationshipwithInsuredId() != null
						 && ReferenceTable.RELATION_SHIP_SELF_KEY.equals(reimbrsmnt.getClaim().getIntimation().getInsured().getRelationshipwithInsuredId().getKey()))
						 || (reimbrsmnt.getClaim().getIncidenceFlag() != null 
						 			&& SHAConstants.DEATH_FLAG.equalsIgnoreCase(reimbrsmnt.getClaim().getIncidenceFlag()))) {
				 
						 StringBuffer selectedNomineeName = new StringBuffer("");
						 for (NomineeDetailsDto proposerNomineeSelect : nomineeList) {
							
							 if(proposerNomineeSelect.getSelectedNomineeFlag() != null && SHAConstants.YES_FLAG.equalsIgnoreCase(proposerNomineeSelect.getSelectedNomineeFlag())){
								 selectedNomineeName = selectedNomineeName.toString().isEmpty() ? selectedNomineeName.append(proposerNomineeSelect.getNomineeName()) : selectedNomineeName.append(", ").append(proposerNomineeSelect.getNomineeName());
							 }
						 }
						 newIntimationDto.setNomineeName(selectedNomineeName.toString());
				 }
			}	
			
//			NomineeDetails nomineeObj = entityManager.find(
//					NomineeDetails.class, intiamtion.getInsured().getKey());
			//IMSSUPPOR-29423
			/*NomineeDetails nomineeObj = (new IntimationService()).getNomineeDetailsByInsuredId(intiamtion.getInsured().getKey(), entityManager);
			
			if(nomineeObj != null){
				newIntimationDto.setNomineeName(nomineeObj.getNomineeName());
			}
			else*/
			if ((nomineeList == null || nomineeList.isEmpty() ) && reimbrsmnt.getNomineeName() != null && reimbrsmnt.getNomineeAddr() != null) {
				newIntimationDto.setNomineeName(reimbrsmnt.getNomineeName());
				newIntimationDto.setNomineeAddr(reimbrsmnt.getNomineeAddr());
				
				reimbursementDto.setNomineeName(reimbrsmnt.getNomineeName());
				reimbursementDto.setNomineeAddr(reimbrsmnt.getNomineeAddr());
			}
			
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
					/*if(nomineeObj != null) {
						newIntimationDto.setNomineeName(nomineeObj.getNomineeName());
					}
					else {
						newIntimationDto.setNomineeName(null);
					}*/
				}		
			}			
			
			claimDto.setNewIntimationDto(newIntimationDto);
						
			reimbursementDto.setClaimDto(claimDto);
			
			DocAcknowledgement docAckdgmnt = reimbrsmnt.getDocAcknowLedgement();
			
			DocAcknowledgementMapper ackdgmntMapper = new DocAcknowledgementMapper();
			
			DocAcknowledgementDto docAckdgmntDto = ackdgmntMapper.getDocAcknowledgementDto(docAckdgmnt);
			
			docAckdgmntDto.setClaimDto(claimDto);
			
			reimbursementDto.setDocAcknowledgementDto(docAckdgmntDto);
			
//			Implemented for hospitalcash product diagnosis name in letter
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
			
			reimbursementQueryDto.setReimbursementDto(reimbursementDto);	
		  }
		
		}
		catch(Exception e){
			e.printStackTrace();
		}		
		
		return reimbursementQueryDto;
		
	}

	private ReimbursementQuery getReimbQueryObjByKey(Long primaryKey) {
		ReimbursementQuery reimbursementQuery = null;
		try {
			if (primaryKey != null) {

				Query query = entityManager
						.createNamedQuery("ReimbursementQuery.findByKey");
				query = query.setParameter("primaryKey", primaryKey);

				List<ReimbursementQuery> reimbursementQueryList = (List<ReimbursementQuery>) query
						.getResultList();

				if (reimbursementQueryList != null
						&& !reimbursementQueryList.isEmpty()) {
					reimbursementQuery = reimbursementQueryList.get(0);
					entityManager.refresh(reimbursementQuery);
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return reimbursementQuery;
	}
	
	public ReimbursementQuery getReimbQueryObjByQueryKey(Long primaryKey,EntityManager em) {
		entityManager = em;
		return getReimbQueryObjByKey(primaryKey);
		
	}

	private List<DraftQueryLetterDetailTableDto> getDraftQueryLetterDetailsDtoList(List<QueryLetterDetails> draftQueryLetterDetailsList) {
		List<DraftQueryLetterDetailTableDto> draftRemarksDtoList = new ArrayList<DraftQueryLetterDetailTableDto>();
		if(draftQueryLetterDetailsList != null && !draftQueryLetterDetailsList.isEmpty()){			
			for (QueryLetterDetails queryLetterDetails : draftQueryLetterDetailsList) {
				DraftQueryLetterDetailTableDto draftRemarksDto = new DraftQueryLetterDetailTableDto();
				draftRemarksDto.setSno(queryLetterDetails.getSno() != null ?queryLetterDetails.getSno().intValue() : 0 );
				draftRemarksDto.setQueryDetailKey(queryLetterDetails.getKey());
				draftRemarksDto.setDraftOrRedraftRemarks(queryLetterDetails.getDraftOrReDraftRemarks());
				draftRemarksDto.setDeltedFlag(queryLetterDetails.getDeletedFlag());
				draftRemarksDto.setProcessType(queryLetterDetails.getProcessType());
				draftRemarksDto.setQueryKey(queryLetterDetails.getReimbQuery().getKey());
				draftRemarksDto.setReimbKey(queryLetterDetails.getReimbursementKey());
				draftRemarksDto.setCreatedDate(queryLetterDetails.getCreatedDate());
				draftRemarksDto.setModifiedDate(queryLetterDetails.getModifiedDate());
				draftRemarksDtoList.add(draftRemarksDto);
			}
		}
		return draftRemarksDtoList;
	}
	
	private List<QueryLetterDetails> getDraftQueryLetterDetails(Long reimbQueryKey){
		
		List<QueryLetterDetails> queryLetterDetailsList = null;
		
		try{
		Query draftReimQueryLetterQuery = entityManager.createNamedQuery("QueryLetterDetails.findDraftDetailsByQueryKey");
		draftReimQueryLetterQuery.setParameter("queryKey", reimbQueryKey);
		queryLetterDetailsList = (List<QueryLetterDetails>)draftReimQueryLetterQuery.getResultList();
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return queryLetterDetailsList;
	}
	
	private List<QueryLetterDetails> getReDraftQueryLetterDetails(Long reimbQueryKey){
		
		List<QueryLetterDetails> queryLetterDetailsList = null;
		
		try{
		Query draftReimQueryLetterQuery = entityManager.createNamedQuery("QueryLetterDetails.findReDraftDetailsByQueryKey");
		draftReimQueryLetterQuery.setParameter("queryKey", reimbQueryKey);
		queryLetterDetailsList = (List<QueryLetterDetails>)draftReimQueryLetterQuery.getResultList();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return queryLetterDetailsList;
	}
	
	public ReimbursementQueryDto getReimbursementQuery(Long primaryKey,EntityManager em){
		this.entityManager = em;
		return getReimbursementQuery(primaryKey);
	}
	
	@SuppressWarnings("unchecked")
	public ReimbursementQuery getReimbursementyQueryByRodKey(Long reimbursementKey){
		
		
		Query query = entityManager
				.createNamedQuery("ReimbursementQuery.findByReimbursement");
		query = query.setParameter("reimbursementKey", reimbursementKey);
		
		List<ReimbursementQuery> reimbursementQuery = (List<ReimbursementQuery>) query.getResultList();
		
		List<Long> keyList = new ArrayList<Long>();
		
        for (ReimbursementQuery reimbursementQuery2 : reimbursementQuery) {	
        	keyList.add(reimbursementQuery2.getKey());	
		}
        
        if(! keyList.isEmpty()){
        	Long maxKey = Collections.max(keyList);
        	
        	Query query2 = entityManager
    				.createNamedQuery("ReimbursementQuery.findByKey");
    		query2 = query2.setParameter("primaryKey", maxKey);
    		
    		ReimbursementQuery latestQuery = (ReimbursementQuery)query2.getSingleResult();
    		
            return latestQuery;
    		
        }
        
        return null;
	}
	
public ReimbursementQuery getReimbQueryByReimbKey(Long reimbursementKey,EntityManager em){
		
	ReimbursementQuery latestQuery = null;
		this.entityManager = em;
		Query query = entityManager
				.createNamedQuery("ReimbursementQuery.findByReimbursement");
		query = query.setParameter("reimbursementKey", reimbursementKey);
		
		List<ReimbursementQuery> reimbursementQueryList = (List<ReimbursementQuery>) query.getResultList();
		
		if(reimbursementQueryList != null){
		
			latestQuery =  reimbursementQueryList.get(0);	
			entityManager.refresh(latestQuery);
		}      	
    		
        return latestQuery;
    		
        
	}
	
	private NewIntimationDto getIntimationDto(Intimation intimation){
		IntimationService intimationService = new IntimationService();
		
		NewIntimationDto newIntimationDto = intimationService.getIntimationDto(intimation, entityManager);
		
		return newIntimationDto;
	}
	
	public ReimbursementQuery submitProcessedReimbursementQuery(ReimbursementQueryDto reimbursementQueryDto){
		
		try
		{
		if(reimbursementQueryDto.getKey() != null){
			ReimbursementQuery reimbursementQuery = getReimbQueryObjByKey(reimbursementQueryDto.getKey());
			
//			entityManager.find(ReimbursementQuery.class, reimbursementQueryDto.getKey());
			
			if (reimbursementQuery != null) {
			
			Reimbursement reimbursement = reimbursementQuery.getReimbursement();
			
//			reimbursement = entityManager.find(Reimbursement.class, reimbursementQuery.getReimbursement().getKey());
			
			reimbursement = getReimbByReimbKey(reimbursementQuery.getReimbursement().getKey());
			if(reimbursement != null){
				entityManager.refresh(reimbursement);	
			}			
			
			Status status = null;
			Stage stage = reimbursement.getStage();
			
			
			if(reimbursementQueryDto.getStatusKey() != null && ! reimbursementQueryDto.getStatusKey().equals(0l) && 
					(reimbursementQueryDto.getStatusKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_APPROVE_STATUS) || reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS) || reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS))){
			
				if(reimbursementQueryDto.getQueryDarftList() != null && !reimbursementQueryDto.getQueryDarftList().isEmpty()){
					saveReimbDraftQueryLetterDetails(reimbursementQueryDto,reimbursementQuery);
					reimbursementQuery.setQueryLetterRemarks(reimbursementQueryDto.getQueryLetterRemarks());
					reimbursementQuery.setQueryRemarks(reimbursementQueryDto.getQueryRemarks());
					reimbursementQuery.setApprovedRejectionDate(new Date());
				}
				
				if(reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS)){
					status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS);
				}
			
				if(reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS)){
					status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS);
				}
				if(reimbursementQueryDto.getStatusKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_APPROVE_STATUS)){
					status = entityManager.find(Status.class, ReferenceTable.CLAIM_APPROVAL_QUERY_APPROVE_STATUS);
				}
				
				if(reimbursement.getDocAcknowLedgement().getHospitalizationRepeatFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalizationRepeatFlag().equalsIgnoreCase("Y") ){
					DBCalculationService dbCalculationService = new DBCalculationService();
					if(reimbursement != null) {
						dbCalculationService.reimbursementRollBackProc(reimbursement.getKey(), "R");			
					}
				}else{
					Reimbursement hospitalizationRod = getHospitalizationRod(reimbursement.getClaim().getKey(), reimbursement.getKey());
					DBCalculationService dbCalculationService = new DBCalculationService();
					if(hospitalizationRod != null){
						dbCalculationService.reimbursementRollBackProc(hospitalizationRod.getKey(), "R");	
					}else {
						if(reimbursement != null) {
							dbCalculationService.reimbursementRollBackProc(reimbursement.getKey(), "R");			
						}
					}
				}
				
				
			}
			
			if(reimbursementQueryDto.getStatusKey() != null && ! reimbursementQueryDto.getStatusKey().equals(0l) &&  
					(reimbursementQueryDto.getStatusKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_REDRAFT_STATUS) || reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REDRAFT_STATUS) || reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REDRAFT_STATUS))){
				
				if(reimbursementQueryDto.getRedraftList() != null && !reimbursementQueryDto.getRedraftList().isEmpty()){
					reimbursementQueryDto = saveReimbDraftQueryLetterDetails(reimbursementQueryDto,reimbursementQuery);
					reimbursementQuery.setRedraftRemarks(reimbursementQueryDto.getRedraftRemarks());
//					reimbursementQuery.setRedraftRemarks(reimbursementQueryDto.getRedraftRemarks());
					reimbursementQuery.setReDraftDate(new Date());
				}
				
				if(reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REDRAFT_STATUS))
				{
					status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REDRAFT_STATUS);
				}
				else if(reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REDRAFT_STATUS))
				{
					status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REDRAFT_STATUS);
				}
				else if(reimbursementQueryDto.getStatusKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_REDRAFT_STATUS))
				{
					status = entityManager.find(Status.class, ReferenceTable.CLAIM_APPROVAL_QUERY_REDRAFT_STATUS);
				}
				
			}
			
			
			if(reimbursementQueryDto.getStatusKey() != null && ! reimbursementQueryDto.getStatusKey().equals(0l) && 
					(reimbursementQueryDto.getStatusKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_REJECT_STATUS) ||reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS) ||  reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REJECT_STATUS))){
				
				if(reimbursementQueryDto.getRejectionRemarks() != null){
					reimbursementQuery.setRejectionRemarks(reimbursementQueryDto.getRejectionRemarks());
					reimbursementQuery.setApprovedRejectionDate(new Date());
				}	
				if(reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS))
				{
					status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS);
				}
				else if(reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REJECT_STATUS))
				{
					status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REJECT_STATUS);
				}
				else if(reimbursementQueryDto.getStatusKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_REJECT_STATUS))
				{
					status = entityManager.find(Status.class, ReferenceTable.CLAIM_APPROVAL_QUERY_REJECT_STATUS);
				}
			}
			
			if(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS.equals(status.getKey()) 
					|| ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS.equals(status.getKey())
					|| ReferenceTable.CLAIM_APPROVAL_QUERY_APPROVE_STATUS.equals(status.getKey())) {
				
				if(ReferenceTable.RECEIVED_FROM_INSURED.equals(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getKey()) &&
						((SHAConstants.DEATH_FLAG).equalsIgnoreCase(reimbursementQueryDto.getReimbursementDto().getClaimDto().getIncidenceFlagValue()) 
								||  ("Y").equalsIgnoreCase(reimbursementQueryDto.getQueryType())								
								|| (reimbursement.getPatientStatus() != null 
										&& ((ReferenceTable.PATIENT_STATUS_DECEASED).equals(reimbursement.getPatientStatus().getKey()) 
												|| (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB).equals(reimbursement.getPatientStatus().getKey()))
										&& reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
										&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId())))) {
					
					if(reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList() != null &&
							!reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList().isEmpty()){
						saveNomineeDetails(reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList(), reimbursement);
						if(reimbursementQueryDto.getReimbursementDto().getIsNomineeDeceased() != null
								&& !reimbursementQueryDto.getReimbursementDto().getIsNomineeDeceased()){
							updateLegalHeir(reimbursementQueryDto);
						}
					}					
					else{
						/*reimbursement.setNomineeName(reimbursementQueryDto.getReimbursementDto().getNomineeName());
						reimbursement.setNomineeAddr(reimbursementQueryDto.getReimbursementDto().getNomineeAddr());*/
						
						if(reimbursementQueryDto.getReimbursementDto().getLegalHeirDTOList() != null && !reimbursementQueryDto.getReimbursementDto().getLegalHeirDTOList().isEmpty()) {
							saveLegalHeirAndDocumentDetails(reimbursementQueryDto);
						}	
					}	
					reimbursement.setNomineeFlag(reimbursementQueryDto.getReimbursementDto().getNomineeDeceasedFlag());
				}
			}
			
//			if(reimbursementQueryDto.getQueryLetterRemarks() != null){ 
//				reimbursementQuery.setQueryLetterRemarks(reimbursementQueryDto.getQueryLetterRemarks());
//				reimbursementQuery.setApprovedRejectionDate(new Date());
//		
//				if(reimbursementQueryDto.getQueryRemarks() != null){
//					reimbursementQuery.setQueryRemarks(reimbursementQueryDto.getQueryRemarks());					
//				}
//			
//				if(reimbursement.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY)){
//					
//					status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS);	
//				}
//				else if(reimbursement.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY)){
//					status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS);
//				}				
//				saveReimbDraftQueryLetterDetails(reimbursementQueryDto,reimbursementQuery);
//			}
//			
//			if(reimbursementQueryDto.getRedraftRemarks() != null){
//				reimbursementQuery.setRedraftRemarks(reimbursementQueryDto.getRedraftRemarks());
//				reimbursementQuery.setReDraftDate(new Date());
//			
//				if(reimbursement.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY))
//				{
//					status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REDRAFT_STATUS);
//				}
//				else if(reimbursement.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY))
//				{
//					status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REDRAFT_STATUS);
//				}
//				saveReimbDraftQueryLetterDetails(reimbursementQueryDto,reimbursementQuery);
//			}
//						
//			if(reimbursementQueryDto.getRejectionRemarks() != null){
//				reimbursementQuery.setRejectionRemarks(reimbursementQueryDto.getRejectionRemarks());
//				reimbursementQuery.setApprovedRejectionDate(new Date());
//			
//				if(reimbursement.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY))
//				{
//					status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS);
//				}
//				else if(reimbursement.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY))
//				{
//					status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REJECT_STATUS);
//				}
//			
//			
//			}
			
			//CR-R1044
			Claim claim = entityManager.find(Claim.class, reimbursementQueryDto.getReimbursementDto().getClaimDto().getKey());
			claim.setDataOfAdmission(reimbursementQueryDto.getReimbursementDto().getClaimDto().getAdmissionDate());
			claim.setDataOfDischarge(reimbursementQueryDto.getReimbursementDto().getClaimDto().getDischargeDate());
			entityManager.merge(claim);
			
			reimbursement.setDateOfAdmission(reimbursementQueryDto.getReimbursementDto().getClaimDto().getAdmissionDate());
			reimbursement.setDateOfDischarge(reimbursementQueryDto.getReimbursementDto().getClaimDto().getDischargeDate());
			
			String strUserName = reimbursementQueryDto.getUsername();
			String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
									
			if(status != null){
				/**The below code is commented because this is no longer needed after refactoring of  BSI procedure**/
				/*if(reimbursementQueryDto.getStatusKey() != null && ! reimbursementQueryDto.getStatusKey().equals(0l) && 
						(reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS) ||reimbursementQueryDto.getStatusKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_APPROVE_STATUS) ||  reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS))){
					reimbursement.setAddOnCoversApprovedAmount(0d);
					reimbursement.setOptionalApprovedAmount(0d);
					resetReimbursementBenefitAmount(reimbursement);
				}*/
				reimbursement.setStatus(status);
				reimbursement.setModifiedBy(userNameForDB);
				reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(reimbursement);
				entityManager.flush();
				entityManager.clear();
			}
			
			//IMSSUPPOR-30766
			if(reimbursementQueryDto.getStatusKey() != null && ! reimbursementQueryDto.getStatusKey().equals(0l) && 
                    (reimbursementQueryDto.getStatusKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_APPROVE_STATUS) || reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS) || reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS))){
				if(! (reimbursement.getDocAcknowLedgement().getHospitalizationRepeatFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalizationRepeatFlag().equalsIgnoreCase("Y"))){
					
					Reimbursement hospitalizationRod = getHospitalizationRod(reimbursement.getClaim().getKey(), reimbursement.getKey());
					
					if(hospitalizationRod != null){
						DBCalculationService dbCalculationService = new DBCalculationService();
						dbCalculationService.invokeReimbursementAccumulatorProcedure(hospitalizationRod.getKey());
					}
				}
             }
			
						
//			claim.setStage(stage);
//			claim.setStatus(status);
//			claim.setModifiedDate(new Timestamp(System.currentTimeMillis()));
//			entityManager.merge(claim);
//			entityManager.flush();
//			entityManager.refresh(claim);

			reimbursementQuery.setReimbursement(reimbursement);
			reimbursementQuery.setModifiedDate(new Date());	
//			reimbursementQuery.setApprovedRejectionDate(new Date());
			reimbursementQuery.setStage(stage);
			reimbursementQuery.setStatus(status);
			reimbursementQuery.setModifiedBy(userNameForDB);
				
			String docToken = null;
//			if(null != status && !(ReferenceTable.CLAIM_APPROVAL_QUERY_REJECT_STATUS.equals(status.getKey()) || ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS.equals(status.getKey()) || ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REJECT_STATUS.equals(status.getKey())))
			if(null != status &&
					(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS.equals(status.getKey()) 
							|| ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS.equals(status.getKey())
							|| ReferenceTable.CLAIM_APPROVAL_QUERY_APPROVE_STATUS.equals(status.getKey())))
			{
				if(null != reimbursementQueryDto.getDocFilePath() && !("").equalsIgnoreCase(reimbursementQueryDto.getDocFilePath()))
				{
					WeakHashMap dataMap = new WeakHashMap();
					if(null != reimbursementQueryDto.getReimbursementDto().getClaimDto())
					{
						dataMap.put("intimationNumber",reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getIntimationId());
						dataMap.put("claimNumber",reimbursementQueryDto.getReimbursementDto().getClaimDto().getClaimId());
						if(null != reimbursementQueryDto.getReimbursementDto().getClaimDto().getClaimType())
						{
							//if((ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY).equals(reimbursementQueryDto.getReimbursementDto().getClaimDto().getClaimType()))
								{
									dataMap.put("reimbursementNumber", reimbursementQueryDto.getReimbursementDto().getRodNumber());
								}
						}
						dataMap.put("filePath", reimbursementQueryDto.getDocFilePath());
						dataMap.put("docType", reimbursementQueryDto.getDocType());
						dataMap.put("docSources", SHAConstants.PROCESS_DRAFT_QUERY);
						dataMap.put("createdBy", userNameForDB);
						docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
					}
					
					if(("Y").equalsIgnoreCase(reimbursementQueryDto.getQueryType()) && ReferenceTable.CLAIM_APPROVAL_QUERY_APPROVE_STATUS.equals(status.getKey())){
						dataMap.put("filePath", reimbursementQueryDto.getDischargeVoucherFilePath());
						dataMap.put("docType", reimbursementQueryDto.getDischargeVoucherDocType());
						String dischargeDocToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
					}	
					SHAUtils.setClearReferenceData(dataMap);
				}
				
				reimbursementQuery.setDocumentToken(null != docToken ? Long.valueOf(docToken):null);
			}
			entityManager.merge(reimbursementQuery);
			entityManager.flush();
			entityManager.clear();

			//submitProcessDraftQueryLetterBpmTask(reimbursementQuery, reimbursementQueryDto);			
			//submitProcessDraftQueryLetterDBTask(reimbursementQuery, reimbursementQueryDto);			
			
			}	
			return reimbursementQuery;
		}
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public void callReminderTaskForDB(Object[] outObjForSubmit){
		
		Object[] outObj = (Object[])outObjForSubmit[0];
		outObj[SHAConstants.INDEX_WORK_FLOW_KEY] = 0;
		outObj[SHAConstants.INDEX_OUT_COME] =  SHAConstants.OUTCOME_INITIATE_QUERY_REIMINDER_PROCESS;
//		outObj[SHAConstants.INDEX_REMINDER_CATEGORY] = SHAConstants.QUERY;     //moved to one level up (this val was already set b4 coming to this menthod )
		DBCalculationService dbCalService = new DBCalculationService();
		dbCalService.revisedInitiateTaskProcedure(outObjForSubmit);
		
	}
	
	public Hospitals getHospitalByKey(Long key){
		Query query = entityManager.createNamedQuery("Hospitals.findByKey");
		query.setParameter("key", key);
		List<Hospitals> resultList = (List<Hospitals>) query.getResultList();
		if(resultList != null && ! resultList.isEmpty()){
			return resultList.get(0);
		}
		return null;
		
	}
	
	@SuppressWarnings("unchecked")
	public Reimbursement getHospitalizationRod(Long claimKey,Long reimbursementKey) {

		Query query = entityManager
				.createNamedQuery("Reimbursement.getRodAscendingOrder");
		query.setParameter("claimkey", claimKey);

		List<Reimbursement> reimbursementList = (List<Reimbursement>) query
				.getResultList();

		for (Reimbursement reimbursement2 : reimbursementList) {
			
			if(! reimbursement2.getKey().equals(reimbursementKey)){
				
				if(reimbursement2.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement2.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y")
						|| reimbursement2.getDocAcknowLedgement().getPartialHospitalisationFlag() != null
						&& reimbursement2.getDocAcknowLedgement().getPartialHospitalisationFlag().equalsIgnoreCase("Y")){
					return reimbursement2;
				}
			}
			
		}
		
		return null;
	}
	
public ReimbursementQueryDto saveReimbursementDraftQuery(ReimbursementQueryDto reimbursementQueryDto){
		
		try{
			if(reimbursementQueryDto.getKey() != null){
				ReimbursementQuery reimbursementQuery = entityManager.find(ReimbursementQuery.class, reimbursementQueryDto.getKey());
			
			if(reimbursementQuery != null){
				Reimbursement reimbursement = reimbursementQuery.getReimbursement();
				
				reimbursement = entityManager.find(Reimbursement.class, reimbursementQuery.getReimbursement().getKey());
				entityManager.refresh(reimbursement);
				
				Status status = null;
				
				Stage stage = reimbursement.getStage();
				
			reimbursementQuery.setQueryLetterRemarks(reimbursementQueryDto.getQueryLetterRemarks());
			reimbursementQueryDto = saveReimbDraftQueryLetterDetails(reimbursementQueryDto,reimbursementQuery);
						
			reimbursementQuery.setModifiedDate(new Date());
			
			if(reimbursementQueryDto.getRedraftRemarks() != null){
				reimbursementQuery.setReDraftDate(new Date());
			}
			else{
				reimbursementQuery.setDraftedDate(new Date());
			}
			
			if(reimbursement.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY))
			{
				status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_REQUEST_DRAFT_QUERY_STATUS);
			}
			else if(reimbursement.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY))
			{
				status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_FINANCIAL_DRAFT_QUERY_STATUS);
			}
			else if(reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_APPROVAL_STAGE))
			{
				status = entityManager.find(Status.class, ReferenceTable.CLAIM_APPROVAL_QUERY_DRAFT_STATUS);
			}
			
			String strUserName = reimbursementQueryDto.getUsername();
			String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
			

			
			if(status != null){
				
				if(reimbursement.getKey() != null){
					
					Claim claim = entityManager.find(Claim.class, reimbursementQueryDto.getReimbursementDto().getClaimDto().getKey());
					claim.setDataOfAdmission(reimbursementQueryDto.getReimbursementDto().getClaimDto().getAdmissionDate());
					claim.setDataOfDischarge(reimbursementQueryDto.getReimbursementDto().getClaimDto().getDischargeDate());
					entityManager.merge(claim);
					
					if(ReferenceTable.RECEIVED_FROM_INSURED.equals(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getKey()) &&
							((SHAConstants.DEATH_FLAG).equalsIgnoreCase(reimbursementQueryDto.getReimbursementDto().getClaimDto().getIncidenceFlagValue()) 
									||  ("Y").equalsIgnoreCase(reimbursementQueryDto.getQueryType())								
									|| (reimbursement.getPatientStatus() != null 
											&& ((ReferenceTable.PATIENT_STATUS_DECEASED).equals(reimbursement.getPatientStatus().getKey()) 
													|| (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB).equals(reimbursement.getPatientStatus().getKey()))
											&& reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
											&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId())))) {
						
						if(reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList() != null &&
								!reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList().isEmpty()){
							saveNomineeDetails(reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList(), reimbursement);
							if(reimbursementQueryDto.getReimbursementDto().getIsNomineeDeceased() != null
									&& !reimbursementQueryDto.getReimbursementDto().getIsNomineeDeceased()){
								updateLegalHeir(reimbursementQueryDto);
							}
						}					
						else{
							/*reimbursement.setNomineeName(reimbursementQueryDto.getReimbursementDto().getNomineeName());
							reimbursement.setNomineeAddr(reimbursementQueryDto.getReimbursementDto().getNomineeAddr());*/
							
							if(reimbursementQueryDto.getReimbursementDto().getLegalHeirDTOList() != null && !reimbursementQueryDto.getReimbursementDto().getLegalHeirDTOList().isEmpty()) {
								saveLegalHeirAndDocumentDetails(reimbursementQueryDto);
							}
						}	
						
						reimbursement.setNomineeFlag(reimbursementQueryDto.getReimbursementDto().getNomineeDeceasedFlag());
					}					
					
					reimbursement.setStatus(status);
					
					reimbursement.setDateOfAdmission(reimbursementQueryDto.getReimbursementDto().getClaimDto().getAdmissionDate());
					reimbursement.setDateOfDischarge(reimbursementQueryDto.getReimbursementDto().getClaimDto().getDischargeDate());
					
					reimbursement.setModifiedBy(userNameForDB);
					reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(reimbursement);
					entityManager.flush();
					entityManager.clear();
				}
			}
		
			reimbursementQuery.setReimbursement(reimbursement);
			reimbursementQuery.setStage(stage);
			reimbursementQuery.setStatus(status);
			reimbursementQuery.setModifiedBy(userNameForDB);
			reimbursementQuery.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(reimbursementQuery);
			entityManager.flush();
			entityManager.clear();
						
			}
		  }
		}
		catch(Exception e){
			e.printStackTrace();
		}	
		
		return reimbursementQueryDto;
	
	}
	
	public void updateReimbursementQueryRemarks(ReimbursementQueryDto reimbursementQueryDto){
		
		try{
			if(reimbursementQueryDto.getKey() != null){
//				ReimbursementQuery reimbursementQuery = entityManager.find(ReimbursementQuery.class, reimbursementQueryDto.getKey());
			
				ReimbursementQuery reimbursementQuery = getByQueryKey(reimbursementQueryDto.getKey());
			
			if(reimbursementQuery != null){
				Reimbursement reimbursement = reimbursementQuery.getReimbursement();
				
				reimbursement = getReimbByReimbKey(reimbursementQuery.getReimbursement().getKey());
				
//				entityManager.find(Reimbursement.class, reimbursementQuery.getReimbursement().getKey());
				
				/*if(reimbursement != null){
					entityManager.refresh(reimbursement);
					
					reimbursement.setNomineeName(reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeName());
					reimbursement.setNomineeAddr(reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeAddr());	
					entityManager.merge(reimbursement);
					entityManager.flush();
				}*/				
				
				reimbursementQueryDto = saveReimbDraftQueryLetterDetails(reimbursementQueryDto,reimbursementQuery);
				
				Status status = null;
				
				Stage stage = reimbursement.getStage();
				
				if(reimbursement.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY))
				{
					status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_REQUEST_DRAFT_QUERY_STATUS);
				}
				else if(reimbursement.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY))
				{
					status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_FINANCIAL_DRAFT_QUERY_STATUS);
				}
				
				else if(reimbursement.getStage().getKey().equals(ReferenceTable.CLAIM_APPROVAL_STAGE))
				{
					status = entityManager.find(Status.class, ReferenceTable.CLAIM_APPROVAL_QUERY_DRAFT_STATUS);
				}
		        //IMSSUPPOR-29256
                else if(reimbursementQuery.getStage()!=null && reimbursementQuery.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY))
                {
                        status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_REQUEST_DRAFT_QUERY_STATUS);
                        stage = entityManager.find(Stage.class, ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
                }
                else if(reimbursementQuery.getStage()!=null && reimbursementQuery.getStage().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY))
                {
                        status = entityManager.find(Status.class, ReferenceTable.PROCESS_CLAIM_FINANCIAL_DRAFT_QUERY_STATUS);
                        stage = entityManager.find(Stage.class, ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY);
                }
                //IMSSUPPOR-29256

				//IMSSUPPOR-29162
				if(ReferenceTable.RECEIVED_FROM_INSURED.equals(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getKey()) &&
						((SHAConstants.DEATH_FLAG).equalsIgnoreCase(reimbursementQueryDto.getReimbursementDto().getClaimDto().getIncidenceFlagValue()) 
								||  ("Y").equalsIgnoreCase(reimbursementQueryDto.getQueryType())								
								|| (reimbursement.getPatientStatus() != null 
										&& ((ReferenceTable.PATIENT_STATUS_DECEASED).equals(reimbursement.getPatientStatus().getKey()) 
												|| (ReferenceTable.PATIENT_STATUS_DECEASED_REIMB).equals(reimbursement.getPatientStatus().getKey()))
										&& reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
										&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId())))) {
					if(reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList() != null &&
							!reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList().isEmpty()){
						saveNomineeDetails(reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList(), reimbursement);
						if(reimbursementQueryDto.getReimbursementDto().getIsNomineeDeceased() != null
								&& !reimbursementQueryDto.getReimbursementDto().getIsNomineeDeceased()){
							updateLegalHeir(reimbursementQueryDto);
						}
					}					
					else{
						/*reimbursement.setNomineeName(reimbursementQueryDto.getReimbursementDto().getNomineeName());
						reimbursement.setNomineeAddr(reimbursementQueryDto.getReimbursementDto().getNomineeAddr());*/
						
						if(reimbursementQueryDto.getReimbursementDto().getLegalHeirDTOList() != null && !reimbursementQueryDto.getReimbursementDto().getLegalHeirDTOList().isEmpty()) {
							saveLegalHeirAndDocumentDetails(reimbursementQueryDto);
						}
					}	
					reimbursement.setNomineeFlag(reimbursementQueryDto.getReimbursementDto().getNomineeDeceasedFlag());
				}
			
				
			reimbursementQueryDto.setStatusKey(status != null  ? status.getKey() : null );				
				
			reimbursementQuery.setQueryLetterRemarks(reimbursementQueryDto.getQueryLetterRemarks());
			
			reimbursementQuery.setModifiedDate(new Date());
			
			if(reimbursementQueryDto.getRedraftRemarks() != null){
				reimbursementQuery.setReDraftDate(new Date());
			}
			else{
				reimbursementQuery.setDraftedDate(new Date());
			}			
			
			String strUserName = reimbursementQueryDto.getUsername();
			String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
			
			if(status != null){
				
				if(reimbursement.getKey() != null){
					reimbursement.setStatus(status);
					
					reimbursement.setModifiedBy(userNameForDB);
					reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(reimbursement);
					entityManager.flush();
					entityManager.clear();
				}
			}
					
			reimbursementQuery.setReimbursement(reimbursement);
			reimbursementQuery.setStage(stage);
			reimbursementQuery.setStatus(status);
			reimbursementQuery.setModifiedBy(userNameForDB);
			reimbursementQuery.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(reimbursementQuery);
			entityManager.flush();
			entityManager.clear();
						
		//	submitDraftQueryLetterBpmTask(reimbursementQueryDto);
			submitDraftQueryLetterDBTask(reimbursementQueryDto);
			}
		  }
		}
		catch(Exception e){
			e.printStackTrace();
		}		
	
	}
	
	private ReimbursementQueryDto saveReimbDraftQueryLetterDetails(ReimbursementQueryDto reimbursementQueryDto, ReimbursementQuery reimbursementQuery){
		
		List<DraftQueryLetterDetailTableDto>  queryDetailDtoList = reimbursementQueryDto.getQueryDarftList();
		List<DraftQueryLetterDetailTableDto>  queryRedraftdetailDtoList = reimbursementQueryDto.getRedraftList();
		List<DraftQueryLetterDetailTableDto>  deletedDraftList = reimbursementQueryDto.getDeletedList();
//		String queryLetterRemarks = "";
		String redraftRemarks = "";

		
		if(reimbursementQueryDto.getStatusKey() != null && 
				(reimbursementQueryDto.getStatusKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_STATUS) || (reimbursementQueryDto.getStatusKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_DRAFT_STATUS))
				|| reimbursementQueryDto.getStatusKey().equals(ReferenceTable.FINANCIAL_QUERY_STATUS) || (reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_DRAFT_QUERY_STATUS)  
				|| reimbursementQueryDto.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS) || reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_DRAFT_QUERY_STATUS))
				|| reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS ) || reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS))){
		if(queryDetailDtoList != null && ! queryDetailDtoList.isEmpty()){
//			queryDetailDtoList.addAll(deletedDraftList);
			
			if(deletedDraftList != null && !deletedDraftList.isEmpty()){
				for (DraftQueryLetterDetailTableDto deletedRemarksDto : deletedDraftList) {
					
					QueryLetterDetails  deletedObj = getQueryDraftDetails(reimbursementQuery, deletedRemarksDto);
					deletedObj.setProcessType("D");
					deletedObj.setDeletedFlag("Y");
					updateDeletedRemarksDetails(deletedObj);
					
//					reimbursementQueryDto.getQueryDarftList().add(draftQueryLetterDetailTableDto);	
				} 
				
			}
			
			for (DraftQueryLetterDetailTableDto draftQueryLetterDetailTableDto : queryDetailDtoList) {
				if(draftQueryLetterDetailTableDto != null){
			
//					queryLetterRemarks = queryLetterRemarks + draftQueryLetterDetailTableDto.getSno() +" ) "+ draftQueryLetterDetailTableDto.getDraftOrRedraftRemarks();
					QueryLetterDetails draftQueryLetterDetails = getQueryDraftDetails(
							reimbursementQuery, draftQueryLetterDetailTableDto);
					draftQueryLetterDetails.setProcessType("D");
					if(draftQueryLetterDetailTableDto.getQueryDetailKey() != null){
						
						QueryLetterDetails queryremarksObj = entityManager.find(QueryLetterDetails.class, draftQueryLetterDetailTableDto.getQueryDetailKey());
						if(queryremarksObj != null){
						draftQueryLetterDetails.setCreatedDate(queryremarksObj.getCreatedDate());
						draftQueryLetterDetails.setModifiedDate(new Date());
						entityManager.merge(draftQueryLetterDetails);
						entityManager.flush();	
						entityManager.clear();
						}
					}
					else{
						if(!("Y").equalsIgnoreCase(draftQueryLetterDetails.getDeletedFlag())){
							draftQueryLetterDetails.setCreatedDate(new Date());
							entityManager.persist(draftQueryLetterDetails);
							entityManager.flush();
							entityManager.clear();
							draftQueryLetterDetailTableDto.setQueryDetailKey(draftQueryLetterDetails.getKey());
						}
					}
			    
				}
			}
			
//			reimbursementQueryDto.setQueryLetterRemarks(queryLetterRemarks);
//			reimbursementQuery.setQueryLetterRemarks(queryLetterRemarks);
//			reimbursementQueryDto.setRedraftRemarks(redraftRemarks);
//			reimbursementQuery.setRedraftRemarks(redraftRemarks);
		}
	}
		if(reimbursementQueryDto.getStatusKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_REDRAFT_STATUS) || reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REDRAFT_STATUS) || reimbursementQueryDto.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REDRAFT_STATUS)){
		if(queryRedraftdetailDtoList != null && ! queryRedraftdetailDtoList.isEmpty() )	{
			redraftRemarks = "";
			if(deletedDraftList != null && !deletedDraftList.isEmpty()){
				for (DraftQueryLetterDetailTableDto deletedRemarksDto : deletedDraftList) {
					
					QueryLetterDetails  deletedObj = getQueryDraftDetails(reimbursementQuery, deletedRemarksDto);
					deletedObj.setProcessType("R");
					deletedObj.setDeletedFlag("Y");
					updateDeletedRemarksDetails(deletedObj);
					
//					reimbursementQueryDto.getQueryDarftList().add(draftQueryLetterDetailTableDto);	
				} 
				
			}
			for (DraftQueryLetterDetailTableDto redraftQueryLetterDetailTableDto : queryRedraftdetailDtoList) {
				if(redraftQueryLetterDetailTableDto != null){
			
						redraftRemarks = redraftRemarks + redraftQueryLetterDetailTableDto.getSno() +" ) "+ redraftQueryLetterDetailTableDto.getDraftOrRedraftRemarks();
										
					QueryLetterDetails redraftQueryLetterDetails = getQueryDraftDetails(
							reimbursementQuery, redraftQueryLetterDetailTableDto);
					redraftQueryLetterDetails.setProcessType("R");
					if(redraftQueryLetterDetailTableDto.getQueryDetailKey() != null){
						
						QueryLetterDetails queryremarksObj = entityManager.find(QueryLetterDetails.class, redraftQueryLetterDetailTableDto.getQueryDetailKey());
						if(queryremarksObj != null){
						redraftQueryLetterDetails.setCreatedDate(queryremarksObj.getCreatedDate());
						redraftQueryLetterDetails.setModifiedDate(new Date());
						entityManager.merge(redraftQueryLetterDetails);
						entityManager.flush();	
						entityManager.clear();
						}
					}
					else{
						if(!("Y").equalsIgnoreCase(redraftQueryLetterDetails.getDeletedFlag())){
							redraftQueryLetterDetails.setCreatedDate(new Date());
							entityManager.persist(redraftQueryLetterDetails);
							entityManager.flush();
							entityManager.clear();
							redraftQueryLetterDetailTableDto.setQueryDetailKey(redraftQueryLetterDetails.getKey());
						}
					}
			    
				}
			}
			
//			reimbursementQueryDto.setQueryLetterRemarks(queryLetterRemarks);
//			reimbursementQuery.setQueryLetterRemarks(queryLetterRemarks);
			reimbursementQueryDto.setRedraftRemarks(redraftRemarks);
//			reimbursementQuery.setRedraftRemarks(redraftRemarks);		
		}
	  }
		return reimbursementQueryDto;
	}
	private void updateDeletedRemarksDetails(
			QueryLetterDetails deletedRemarks) {
			if(deletedRemarks.getKey() != null){
				
				QueryLetterDetails queryremarksObj = entityManager.find(QueryLetterDetails.class, deletedRemarks.getKey());
				if(queryremarksObj != null){
				deletedRemarks.setCreatedDate(queryremarksObj.getCreatedDate());
				deletedRemarks.setModifiedDate(new Date());
				entityManager.merge(deletedRemarks);
				entityManager.flush();
				entityManager.clear();
				}
			}
		
	}

	private QueryLetterDetails getQueryDraftDetails(ReimbursementQuery reimbursementQuery,
			DraftQueryLetterDetailTableDto draftQueryLetterDetailTableDto) {
		QueryLetterDetails draftQueryLetterDetails = new QueryLetterDetails();
		
		draftQueryLetterDetails.setReimbQuery(reimbursementQuery);
		draftQueryLetterDetails.setReimbursementKey(reimbursementQuery.getReimbursement().getKey());
		draftQueryLetterDetails.setKey(draftQueryLetterDetailTableDto.getQueryDetailKey());
		draftQueryLetterDetails.setDraftOrReDraftRemarks(draftQueryLetterDetailTableDto.getDraftOrRedraftRemarks());
		draftQueryLetterDetails.setSno(new Long(draftQueryLetterDetailTableDto.getSno()));
		draftQueryLetterDetails.setProcessType(draftQueryLetterDetailTableDto.getProcessType());
		draftQueryLetterDetails.setDeletedFlag(draftQueryLetterDetailTableDto.getDeltedFlag());
		
		return draftQueryLetterDetails;
	}

	/*private void submitDraftQueryLetterBpmTask(ReimbursementQueryDto reimbursementQueryDto) {
		 
		PayloadBOType payload = reimbursementQueryDto.getHumanTask().getPayload();
		
		QueryType queryType = payload.getQuery();
		
//		if(reimbursementQueryDto.getRejectionRemarks() != null )
//		{
//			queryType.setIsQueryDisapproved(true);
//		}
				
		payload.setQuery(queryType);
		reimbursementQueryDto.getHumanTask().setPayload(payload);
		reimbursementQueryDto.getHumanTask().setOutcome(SHAConstants.QUERY_DRAFT_OUT_COME);			
		SubmitDraftQueryLetterTask submitQueryTask = BPMClientContext.getSubmitDraftQueryService(reimbursementQueryDto.getUsername(),reimbursementQueryDto.getPassword());
		try{
		submitQueryTask.execute(reimbursementQueryDto.getUsername(),reimbursementQueryDto.getHumanTask());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	*/
	private void submitDraftQueryLetterDBTask(ReimbursementQueryDto reimbursementQueryDto) {	
		
		Map<String,Object> wrkFlowMap = (Map<String,Object>)reimbursementQueryDto.getDbOutArray();		
		DBCalculationService dbCalculationService = new DBCalculationService();	
		wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_SUBMIT_DRAFT_QUERY_LETTER);
		Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);		
		DBCalculationService dbCalService = new DBCalculationService();	
		dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
	
	}
	
/*	private void submitProcessDraftQueryLetterBpmTask(ReimbursementQuery reimbursementQuery, ReimbursementQueryDto reimbursementQueryDto) {
		 
		PayloadBOType payload = reimbursementQueryDto.getHumanTask().getPayload();
		
		QueryType queryType = payload.getQuery();
		if(reimbursementQuery.getStatus() != null) {
		
		if(reimbursementQuery.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_REJECT_STATUS) || reimbursementQuery.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS) || reimbursementQuery.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REJECT_STATUS))
		{
			queryType.setStatus(SHAConstants.QUERY_REJECTION_OUT_COME);
			reimbursementQueryDto.getHumanTask().setOutcome(SHAConstants.QUERY_REJECTION_OUT_COME);
		}
		else if(reimbursementQuery.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_REDRAFT_STATUS) || reimbursementQuery.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REDRAFT_STATUS) || reimbursementQuery.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REDRAFT_STATUS)){
			queryType.setStatus(SHAConstants.QUERY_REDRAFT_OUT_COME);
			reimbursementQueryDto.getHumanTask().setOutcome(SHAConstants.QUERY_REDRAFT_OUT_COME);
		}
		else if(reimbursementQuery.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_APPROVE_STATUS) || reimbursementQuery.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS) || reimbursementQuery.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS)){
			queryType.setIsQueryDisapproved(false);
			queryType.setStatus(SHAConstants.QUERY_APPROVE_OUT_COME);
			reimbursementQueryDto.getHumanTask().setOutcome(SHAConstants.QUERY_APPROVE_OUT_COME);
		}
				
		}
		payload.setQuery(queryType);
		payload.getClaim().setClaimId(reimbursementQuery.getReimbursement().getClaim().getClaimId());
		payload.getClaim().setKey(reimbursementQuery.getReimbursement().getClaim().getKey());
		payload.getIntimation().setKey(reimbursementQuery.getReimbursement().getClaim().getIntimation().getKey());
		payload.getClaimRequest().setOption((SHAConstants.QUERY).toUpperCase());
		reimbursementQueryDto.getHumanTask().setPayload(payload);
		SubmitProcessDraftQueryLetterTask submitProcessDraftQueryTask = BPMClientContext.getSubmitProcessDraftQueryService(reimbursementQueryDto.getUsername(),reimbursementQueryDto.getPassword());
		
		try{
		submitProcessDraftQueryTask.execute(reimbursementQueryDto.getUsername(),reimbursementQueryDto.getHumanTask());
		}catch(Exception e){
			e.printStackTrace();
		}
	}*/
	
	public void submitProcessDraftQueryLetterDBTask(ReimbursementQuery reimbursementQuery, ReimbursementQueryDto reimbursementQueryDto) {
		 
		Map<String,Object> wrkFlowMap = (Map<String,Object>)reimbursementQueryDto.getDbOutArray();		
		DBCalculationService dbCalService = new DBCalculationService();
		
		if(reimbursementQuery.getStatus() != null && null != reimbursementQuery.getStage()) {
		
			if(reimbursementQuery.getStage().getKey().equals(ReferenceTable.MA_CORPORATE_QUERY_STAGE) || reimbursementQuery.getStage().getKey().equals(ReferenceTable.FINANCIAL_QUERY_STAGE)
					||reimbursementQuery.getStage().getKey().equals(ReferenceTable.CLAIM_APPROVAL_STAGE))
			{
				if(reimbursementQuery.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS))
				{
					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.PARALLEL_QUERY_REJECTION_OUTCOME);
				
				}
				else if(reimbursementQuery.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REJECT_STATUS))
				{
					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_DISAPPROVE_FA_QUERY);
				}
				else if(reimbursementQuery.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_REJECT_STATUS))
				{
					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_DISAPPROVE_CLAIM_APPROVAL_QUERY);
				}
				
				
				if(reimbursementQuery.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REDRAFT_STATUS) || 
						reimbursementQuery.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REDRAFT_STATUS) ||
						reimbursementQuery.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_REDRAFT_STATUS)){
					
					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_REDRAFT_QUERY_LETTER);			
				}
				else if(reimbursementQuery.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS) || 
						reimbursementQuery.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS) ||
						reimbursementQuery.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_APPROVE_STATUS)	){
					
					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_APPROVE_PROCESS_DRAFT_QUERY_LETTER);
					
					if(reimbursementQuery.getReimbursement() != null && reimbursementQuery.getReimbursement().getClaim() != null && reimbursementQuery.getReimbursement().getClaim().getClaimType() != null ){
						wrkFlowMap.put(SHAConstants.PROCESS_TYPE,String.valueOf(reimbursementQuery.getReimbursement().getClaim().getClaimType().getValue()));
					}
					
					if(ReferenceTable.PA_LOB_KEY.equals(reimbursementQuery.getReimbursement().getClaim().getLobId())){
					
						if(reimbursementQuery.getQueryType() != null && SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursementQuery.getQueryType())){
							wrkFlowMap.put(SHAConstants.PAYLOAD_REMINDER_CATEGORY,SHAConstants.PA_PAYMENT_QUERY);
						}
						else{
							wrkFlowMap.put(SHAConstants.PAYLOAD_REMINDER_CATEGORY,SHAConstants.PA_QUERY);
						}	
					
					}
					else{
						wrkFlowMap.put(SHAConstants.PAYLOAD_REMINDER_CATEGORY,SHAConstants.QUERY);
					}
					
					wrkFlowMap.put(SHAConstants.PAYLOAD_QUERY_KEY,reimbursementQuery.getKey());
					Object[] outObjForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
					callReminderTaskForDB(outObjForSubmit);	
				}

			}				
		}		
		
		Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);		
		dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
		
		if(reimbursementQuery.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS)){
			
			Long wkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
			String intimationNo = (String) wrkFlowMap.get(SHAConstants.INTIMATION_NO);
			
			dbCalService.workFlowEndCallProcedure(wkFlowKey, intimationNo);
		}

		
	}
	
	@SuppressWarnings("unchecked")
	public Boolean isQueryReceivedStatusRod(Long rodKey){
		
		Query query = entityManager
				.createNamedQuery("ReimbursementQuery.findByReimbursement");
		query = query.setParameter("reimbursementKey", rodKey);
		
		List<ReimbursementQuery> reimbursementQuery = (List<ReimbursementQuery>) query.getResultList();
	
		for (ReimbursementQuery reimbursementQuery2 : reimbursementQuery) {
			entityManager.refresh(reimbursementQuery2);
			
			if(reimbursementQuery2.getQueryReply() != null && ("Y").equalsIgnoreCase(reimbursementQuery2.getQueryReply())){
				return true;
			}
		}
		
		return false;
		
	}
	
	public Boolean isQueryRaisedFromMA(Long rodKey){
		
		Query query = entityManager
				.createNamedQuery("ReimbursementQuery.findByReimbursement");
		query = query.setParameter("reimbursementKey", rodKey);
		
		List<ReimbursementQuery> reimbursementQuery = (List<ReimbursementQuery>) query.getResultList();
	
		for (ReimbursementQuery reimbursementQuery2 : reimbursementQuery) {
			entityManager.refresh(reimbursementQuery2);
			
			if(reimbursementQuery2.getQueryReply() != null && ("Y").equalsIgnoreCase(reimbursementQuery2.getQueryReply())
					&& reimbursementQuery2.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)){
				return true;
			}
		}
		
		return false;
	}
	
	public Reimbursement getReimbByReimbKey(Long reimbKey){
		Reimbursement reimbObj = null;
		
		reimbObj = (new ReimbursementService()).getReimbursementbyRod(reimbKey, entityManager);
		
		return reimbObj;
		
	}
	
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
	@SuppressWarnings("unchecked")	
	public ReimbursementQuery getByQueryKey(Long queryKey){
		ReimbursementQuery qryObj = null;
		try{
			Query findAll = entityManager.createNamedQuery(
					"ReimbursementQuery.findByKey").setParameter("primaryKey",
							queryKey);
			List<ReimbursementQuery> queryList = (List<ReimbursementQuery>) findAll
					.getResultList();
			if(!queryList.isEmpty()){
				qryObj = queryList.get(0);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return qryObj;
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
	
	public void updateQueryStatus(Long queryKey,InvesAndQueryAndFvrParallelFlowTableDTO cancelledFvrInvsOrquery,PreauthDTO bean)
	{
		ReimbursementQuery query = getByQueryKey(queryKey);
		if(null != query){
			
				
				if(null != cancelledFvrInvsOrquery.getFvrInvsQueryCancelStatus() && cancelledFvrInvsOrquery.getFvrInvsQueryCancelStatus()){
					
					query.setQueryCancelRequest(SHAConstants.YES_FLAG);
				}

				if(null != cancelledFvrInvsOrquery.getCancelRemarks()){
			
					query.setQueryCancelRemarks(cancelledFvrInvsOrquery.getCancelRemarks());
				}
				
			query.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			query.setModifiedBy(bean.getStrUserName());

			Status status = new Status();
			status.setKey(ReferenceTable.PARALLEL_QUERY_CANCELLED);
			query.setStatus(status);
			
			
			
			entityManager.merge(query);
			
		}
		
	}
	
	public void submitPAProcessDraftQueryLetterDBTask(ReimbursementQuery reimbursementQuery, ReimbursementQueryDto reimbursementQueryDto) {
		 
		Map<String,Object> wrkFlowMap = (Map<String,Object>)reimbursementQueryDto.getDbOutArray();		
		DBCalculationService dbCalService = new DBCalculationService();	
		
		if(reimbursementQuery.getStatus() != null && null != reimbursementQuery.getStage()) {
		
			if(reimbursementQuery.getStage().getKey().equals(ReferenceTable.MA_CORPORATE_QUERY_STAGE) || reimbursementQuery.getStage().getKey().equals(ReferenceTable.FINANCIAL_QUERY_STAGE)
					||reimbursementQuery.getStage().getKey().equals(ReferenceTable.CLAIM_APPROVAL_STAGE))
			{
				

				if(reimbursementQuery.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS))
				{
					//For PA Parallel flow 
//	              wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_DISAPPROVE_MA_QUERY);
	              wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.PARALLEL_QUERY_REJECTION_OUTCOME);
				}
				else if(reimbursementQuery.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REJECT_STATUS))
				{
					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_DISAPPROVE_FA_QUERY);
				}
				else if(reimbursementQuery.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_REJECT_STATUS))
				{
					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_DISAPPROVE_CLAIM_APPROVAL_QUERY);
				}
				else if(reimbursementQuery.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REDRAFT_STATUS) || 
						reimbursementQuery.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REDRAFT_STATUS) ||
						reimbursementQuery.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_REDRAFT_STATUS)){
					
					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_REDRAFT_QUERY_LETTER);			
				}
				else if(reimbursementQuery.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS) || 
						reimbursementQuery.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS) ||
						reimbursementQuery.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_QUERY_APPROVE_STATUS)	){
					
					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_APPROVE_PROCESS_DRAFT_QUERY_LETTER);
					
					if(reimbursementQuery.getReimbursement() != null && reimbursementQuery.getReimbursement().getClaim() != null && reimbursementQuery.getReimbursement().getClaim().getClaimType() != null ){
						wrkFlowMap.put(SHAConstants.PROCESS_TYPE,String.valueOf(reimbursementQuery.getReimbursement().getClaim().getClaimType().getValue()));
					}
					
					if(ReferenceTable.PA_LOB_KEY.equals(reimbursementQuery.getReimbursement().getClaim().getLobId())){
					
						if(reimbursementQuery.getQueryType() != null && SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursementQuery.getQueryType())){
							wrkFlowMap.put(SHAConstants.PAYLOAD_REMINDER_CATEGORY,SHAConstants.PA_PAYMENT_QUERY);
						}
						else{
							wrkFlowMap.put(SHAConstants.PAYLOAD_REMINDER_CATEGORY,SHAConstants.PA_QUERY);
						}	
					
					}
					else{
						wrkFlowMap.put(SHAConstants.PAYLOAD_REMINDER_CATEGORY,SHAConstants.QUERY);
					}
					
					wrkFlowMap.put(SHAConstants.PAYLOAD_QUERY_KEY,reimbursementQuery.getKey());
					Object[] outObjForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
					callReminderTaskForDB(outObjForSubmit);					
				}

			}				
		}		
		
		Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);		
		dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
		
		if(reimbursementQuery.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS)){
			
			Long wkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
			String intimationNo = (String) wrkFlowMap.get(SHAConstants.INTIMATION_NO);
			
			dbCalService.workFlowEndCallProcedure(wkFlowKey, intimationNo);
		}
		
		
	}

	public boolean rodAdmissionDateCompare(Long argClaimKey, Date argAdmissionDate){
		boolean flag = false;
		//System.out.println(argClaimKey);
		Date firstRodAdmissionDate = null;
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey");
		query = query.setParameter("claimKey", argClaimKey);

		List<Reimbursement> reimbBenefits = query.getResultList();
		if(reimbBenefits != null && reimbBenefits.size() > 1){
			Reimbursement obj = reimbBenefits.get(0);
			firstRodAdmissionDate = obj.getDateOfAdmission();
			//if entered date is before the the first rod Admission Date, then return true.
			if (firstRodAdmissionDate != null && !(argAdmissionDate.after(firstRodAdmissionDate) || argAdmissionDate.compareTo(firstRodAdmissionDate) == 0)) {
				flag = true;
			}else{
				flag = false;
			}
		}
		return flag;
	}
	
	private List<MasOmbudsman> getOmbudsmanOffiAddrByPIOCode(String pioCode) {
		
		List<MasOmbudsman> ombudsmanDetailsByCpuCode = new ArrayList<MasOmbudsman>();
		if(pioCode != null){
			OrganaizationUnit branchOffice = getOrgUnitByCode(pioCode);
			if (branchOffice != null) {
				String ombudsManCode = branchOffice.getOmbudsmanCode();
				if (ombudsManCode != null) {
					ombudsmanDetailsByCpuCode = getOmbudsmanDetailsByCpuCode(ombudsManCode);
				}
			}
		}
		return ombudsmanDetailsByCpuCode;
	}
	
	public OrganaizationUnit getOrgUnitByCode(String polDivnCode) {
		List<OrganaizationUnit> organizationUnit = null;
		if(polDivnCode != null){			
			Query findAll = entityManager.createNamedQuery("OrganaizationUnit.findByUnitId").setParameter("officeCode", polDivnCode);
			organizationUnit = (List<OrganaizationUnit>)findAll.getResultList();
		}
		
		if(organizationUnit != null && ! organizationUnit.isEmpty()){
			return organizationUnit.get(0);
		}
		
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<MasOmbudsman> getOmbudsmanDetailsByCpuCode(String ombudsManCode) {
		try{
			List<MasOmbudsman> ombudsman = new ArrayList<MasOmbudsman>();
			if(ombudsManCode != null){
				Query findAll = entityManager.createNamedQuery("MasOmbudsman.findByOmbudsManCode").setParameter("ombudsmanCode",ombudsManCode);
				ombudsman = (List<MasOmbudsman>) findAll.getResultList();
			}
			return ombudsman;
		}catch(Exception e){
			return null;
		}
	}

	public String getDiagnosisName(Long key) {

		String diagnosisName = "";
		try{
			
			Query diagnosis = entityManager
					.createNamedQuery("Diagnosis.findDiagnosisByKey");
			diagnosis.setParameter("diagnosisKey",	key);
			Diagnosis masters = (Diagnosis) diagnosis.getSingleResult();
			if (masters != null) {
				diagnosisName += masters.getValue() + ",";
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return diagnosisName;
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
				nomineeObj.setNomineeDob(nomineeDetailsDto.getNomineeDob() != null && !nomineeDetailsDto.getNomineeDob().isEmpty() ? SHAUtils.formatTimeFromString(nomineeDetailsDto.getNomineeDob()) : null);
				nomineeObj.setRelationshipWithProposer(nomineeDetailsDto.getNomineeRelationship());
				nomineeObj.setSharePercent(nomineeDetailsDto.getNomineePercent() != null && !nomineeDetailsDto.getNomineePercent().isEmpty() ?  Double.valueOf(nomineeDetailsDto.getNomineePercent().split("%")[0]) : null);
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

	public void saveLegalHeirAndDocumentDetails(ReimbursementQueryDto reimbursementQueryDto) {
		ReimbursementDto rodDTO = reimbursementQueryDto.getReimbursementDto();
		String strUserName = reimbursementQueryDto.getUsername();
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
				legalHeir.setInsuredKey(rodDTO.getClaimDto().getNewIntimationDto().getInsuredPatient().getKey());
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
					//docDetails.setReimbursementNumber(rodDTO.getRodNumberForUploadTbl());
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
	
	private void updateLegalHeir(ReimbursementQueryDto reimbursementQueryDto){
		ReimbursementDto rodDTO = reimbursementQueryDto.getReimbursementDto();
		String strUserName = reimbursementQueryDto.getUsername();
		String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
		List<LegalHeir> list = getlegalHeirListByTransactionKey(rodDTO.getKey());
		if(list != null && !list.isEmpty()){
			for (LegalHeir legalHeir : list) {
				legalHeir.setModifiedBy(userNameForDB);
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
}
