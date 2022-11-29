package com.shaic.paclaim.healthsettlementpullback;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.reimbursement.ReimbursementCalCulationDetails;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.healthsettlementpullback.dto.PAHospSearchSettlementPullBackDTO;

@Stateless
public class PAHospSettlementPullBackService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public Claim getClaimByKey(Long key) {
		Query query = entityManager.createNamedQuery("Claim.findByClaimKey");
		query.setParameter("claimKey", key);
		List<Claim> claim = (List<Claim>)query.getResultList();
		
		if(claim != null && ! claim.isEmpty()){
			for (Claim claim2 : claim) {
				entityManager.refresh(claim2);
			}
			return claim.get(0);
		}
		else{
			return null;
		}
	}
	
	public Hospitals getHospitalObject(Long hospitalKey) {
		TypedQuery<Hospitals> query = entityManager.createNamedQuery("Hospitals.findByKey", Hospitals.class);
		query.setParameter("key", hospitalKey);
		List<Hospitals>	resultList = query.getResultList();
		if (null != resultList && 0 != resultList.size())
		{
			return resultList.get(0);
			
		}
		else
		{
			return null;
		}
	}
	
	public Preauth getPreauthById(Long preauthKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByKey");
		query.setParameter("preauthKey", preauthKey);
		@SuppressWarnings("unchecked")
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()) {
			entityManager.refresh(singleResult.get(0));
			return singleResult.get(0);
		}
		
		return null;
		
		
	}
	
	
	@SuppressWarnings("unchecked")
	public Intimation getIntimationByNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery(
				"Intimation.findByIntimationNumber").setParameter(
				"intimationNo", intimationNo);

		List<Intimation> intimationList = (List<Intimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}
	
	public List<DocumentDetails> getQueryDocumentDetailsDataByRod(String rodNumber,String docType,String docTypeForScrc,String docSource)
	{
		Query query = entityManager.createNamedQuery("DocumentDetails.findQueryByRodNo");
		query = query.setParameter("reimbursementNumber", rodNumber);
		docType = docType.toLowerCase();
		docSource = docSource.toLowerCase();
		if(docTypeForScrc != null){
			docTypeForScrc = docTypeForScrc.toLowerCase();
		}
		query = query.setParameter("billsummary", docType);
		query = query.setParameter("billassessment", docTypeForScrc);
		query = query.setParameter("financialapproval", "%"+docSource+"%");
		List<DMSDocumentDetailsDTO> documentDetailsDTOList =  null;
		List<DocumentDetails> documentDetailsList  = query.getResultList();
		for (DocumentDetails documentDetails : documentDetailsList) {
			entityManager.refresh(documentDetails);
		}
		
		return documentDetailsList;
	}
	
	
	public ReimbursementCalCulationDetails getReimbursementCalcByRodAndClassificationKey(Long rodKey,
			Long classificatonKey) {
		Query query = entityManager
				.createNamedQuery("ReimbursementCalCulationDetails.findByRodAndBillClassificationKey");
		query.setParameter("reimbursementKey", rodKey);
		query.setParameter("billClassificationKey", classificatonKey);

		@SuppressWarnings("unchecked")
		List<ReimbursementCalCulationDetails> reimbBenefits = query
				.getResultList();
		if (reimbBenefits != null && !reimbBenefits.isEmpty()) {
			return reimbBenefits.get(0);
		}
		return null;
	}

	
	public void submitSettlementPullBack(PAHospSearchSettlementPullBackDTO dto, Reimbursement reimbursement) {
		try {
			
			/**
			 *  Needs to be enabled to resolve the reconsideration cases...
			 */
//			DocAcknowledgement docAcknowLedgement = reimbursement.getDocAcknowLedgement();
//			if(docAcknowLedgement != null && docAcknowLedgement.getDocumentReceivedFromId() != null && docAcknowLedgement.getReconsiderationRequest() != null && docAcknowLedgement.getReconsiderationRequest().equalsIgnoreCase(SHAConstants.YES_FLAG) && (docAcknowLedgement.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED) || docAcknowLedgement.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL))) {
//				ReimbursementCalCulationDetails postHospCalc = getReimbursementCalcByRodAndClassificationKey(reimbursement.getKey(), ReferenceTable.POST_HOSPITALIZATION);
//				ReimbursementCalCulationDetails preHospCalc = getReimbursementCalcByRodAndClassificationKey(reimbursement.getKey(), ReferenceTable.PRE_HOSPITALIZATION);
//				ReimbursementCalCulationDetails hospCalc = getReimbursementCalcByRodAndClassificationKey(reimbursement.getKey(), ReferenceTable.HOSPITALIZATION);
//				
//				if(hospCalc != null && hospCalc.getAmountAlreadyPaidAmt() != null && hospCalc.getAmountAlreadyPaidAmt() > 0) {
//					Integer amt = hospCalc.getAmountAlreadyPaidAmt() - (hospCalc.getBalanceToBePaidAmt() != null ? hospCalc.getBalanceToBePaidAmt() : 0);
//					hospCalc.setAmountAlreadyPaidAmt(amt);
//					
//					entityManager.merge(hospCalc);
//					entityManager.flush();
//				}
//				
//				if(preHospCalc != null && preHospCalc.getAmountAlreadyPaidAmt() != null && preHospCalc.getAmountAlreadyPaidAmt() > 0) {
//					Integer amt = preHospCalc.getAmountAlreadyPaidAmt() - (preHospCalc.getBalanceToBePaidAmt() != null ? preHospCalc.getBalanceToBePaidAmt() : 0);
//					preHospCalc.setAmountAlreadyPaidAmt(amt);
//					
//					entityManager.merge(preHospCalc);
//					entityManager.flush();
//				}
//				
//				if(postHospCalc != null && postHospCalc.getAmountAlreadyPaidAmt() != null && postHospCalc.getAmountAlreadyPaidAmt() > 0) {
//					Integer amt = postHospCalc.getAmountAlreadyPaidAmt() - (postHospCalc.getBalanceToBePaidAmt() != null ? postHospCalc.getBalanceToBePaidAmt() : 0);
//					postHospCalc.setAmountAlreadyPaidAmt(amt);
//					
//					entityManager.merge(postHospCalc);
//					entityManager.flush();
//				}
//			}
			
			Stage stage = new Stage();
			stage.setKey(ReferenceTable.BILLING_STAGE);
			reimbursement.setStage(stage);
			
			Status status = new Status();
			status.setKey(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER);
			reimbursement.setStatus(status);
			
			reimbursement.setFinancialCompletedDate(null);
			reimbursement.setFinancialApprovedAmount(0d);
			
			entityManager.merge(reimbursement);
			entityManager.flush();
			
			
			if(dto.getClaimPayment() != null) {
				ClaimPayment claimPayment= dto.getClaimPayment();
				claimPayment.setDeletedFlag("Y");
				
				entityManager.merge(claimPayment);
				entityManager.flush();
			}
			
			
			List<DocumentDetails> queryDocumentDetailsDataByRod = getQueryDocumentDetailsDataByRod(reimbursement.getRodNumber(), SHAConstants.BILL_SUMMARY_OTHER_PRODUCTS,SHAConstants.BILLASSESSMENTSHEETSCRC,SHAConstants.FINANCIAL_APPROVER);
			if(queryDocumentDetailsDataByRod != null && !queryDocumentDetailsDataByRod.isEmpty()) {
				for (DocumentDetails documentDetails : queryDocumentDetailsDataByRod) {
					documentDetails.setDeletedFlag("Y");
					entityManager.merge(documentDetails);
					entityManager.flush();
				}
				
			}
			//submitBPMTaskForPullBack(dto, reimbursement);
			submitPullBackTaskToDB(dto);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/*private void submitBPMTaskForPullBack(PAHospSearchSettlementPullBackDTO dto, Reimbursement reimbursement) {
		HumanTask humanTask = dto.getHumanTask();
		try {
			if(humanTask != null) {
				PayloadBOType payload = humanTask.getPayload();
				
				// Moving to MEDICAL...
				FVR submitFvrTask = BPMClientContext.getInitiateFVR(
						dto.getUsername(), dto.getPassword());
				payload.getClaimRequest().setClientType("MEDICAL");
				PaymentInfoType paymentInfo = null;
				if(payload.getPaymentInfo() == null) {
					paymentInfo = new PaymentInfoType();
				} else {
					paymentInfo = payload.getPaymentInfo();
				}
				
				paymentInfo.setClaimedAmount(0d);
				payload.setPaymentInfo(paymentInfo);
				
				ClaimRequestType claimType = null;

				if (payload.getClaimRequest() != null) {
					claimType = payload.getClaimRequest();
				} else {
					claimType = new ClaimRequestType();
				}
				
				if(payload.getProcessActorInfo() != null) {
					ProcessActorInfoType processActorInfo = payload.getProcessActorInfo();
//					processActorInfo.setEscalatedByUser(dto.getStrUserName());
					if(processActorInfo.getEscalatedByUser() == null){
						processActorInfo.setEscalatedByUser("");
						payload.setProcessActorInfo(processActorInfo);
					}
//					payloadBO.setProcessActorInfo(processActorInfo);
				} else {
					ProcessActorInfoType processActor = payload.getProcessActorInfo();
					if(processActor == null){
						processActor = new ProcessActorInfoType();
					}
					processActor.setEscalatedByUser(dto.getUsername());
					payload.setProcessActorInfo(processActor);
				}
				
				if(reimbursement.getStage() != null){
					claimType.setOption(reimbursement.getStage().getStageName());
				}

				payload.setClaimRequest(claimType);

				RODType rodType = new RODType();
				rodType.setAckNumber(reimbursement.getRodNumber());
				rodType.setKey(dto.getRodKey());
				payload.setRod(rodType);

				CustomerType custType = new CustomerType();
				if (false) {
//					custType.setTreatmentType(dto.getPreauthDataExtractionDetails()
//							.getTreatmentType().getValue());
				} else {
					custType.setTreatmentType("");
				}

				custType.setSpeciality("");
				payload.setCustomer(custType);
				
				if(reimbursement.getStage() != null && reimbursement.getStage().getKey() != null){
					
					Stage stage = entityManager.find(Stage.class, reimbursement.getStage().getKey());
					
					ClassificationType classification = payload.getClassification();
					if(classification.getSource() == null || (classification.getSource() != null && !classification.getSource().equalsIgnoreCase(SHAConstants.QUERY_REPLY) && !classification.getSource().equalsIgnoreCase(SHAConstants.RECONSIDERATION))) {
						classification.setSource(stage.getStageName());
					}
					
					payload.setClassification(classification);
				}
				
				
				submitFvrTask.initiate(dto.getUsername(), payload);
				
				
				ProcessClaimRequestTask processClaimRequest = BPMClientContext.getprocessClaimRequestTask(dto.getUsername(), dto.getPassword());
				PayloadBOType payloadBOType = new PayloadBOType();
				payloadBOType = SHAUtils.getReimPayloadForPA(payloadBOType);
				IntimationType intimationType = new IntimationType();
				intimationType.setIntimationNumber(dto.getNewIntimationDTO().getIntimationId());
				
				payloadBOType.setIntimation(intimationType);
				Pageable pageable = new Pageable();
				pageable.setPageNumber(1);
				pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
				Thread.sleep(1000);
				PagedTaskList taskList = processClaimRequest.getTasks(dto.getUsername(),pageable,payloadBOType);
				
				if(taskList != null && taskList.getHumanTasks() != null && !taskList.getHumanTasks().isEmpty()) {
					List<HumanTask> humanTasks = taskList.getHumanTasks();
					for (HumanTask processItem : humanTasks) {
						PayloadBOType processPayloadBO = processItem.getPayload();
						if(processPayloadBO != null && processPayloadBO.getRod() != null &&  processPayloadBO.getRod().getKey() != null && processPayloadBO.getRod().getKey().equals(reimbursement.getKey())) {
							processPayloadBO.getClaimRequest().setReimbReqBy("FA");
							processPayloadBO.getClaimRequest().setResult("APPROVE");
							HumanTask processHT = new HumanTask(processItem.getNumber(),processItem.getId(),processItem.getTitle(),
	                                  "APPROVE",3,"ASSIGNED",null, processPayloadBO, processItem.getKey(), processItem.getStatus(),processItem.getUserName());
							try {
								SubmitProcessClaimRequestTask claimRequestTask = BPMClientContext
										.getClaimRequest(dto.getUsername(), dto.getPassword());
								claimRequestTask.execute(dto.getUsername(), processHT);
							} catch(Exception exp) {
								exp.printStackTrace();
							}
						}
						
					}
				}
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}*/
	
	private void submitPullBackTaskToDB(PAHospSearchSettlementPullBackDTO dto)
	{
		Map<String, Object> wrkFlowMap = (Map<String, Object>) dto.getDbOutArray();
		DBCalculationService dbCalService = new DBCalculationService();
		if(null != wrkFlowMap)
		{
			Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
			dbCalService.pullBackSubmitProcedure(wrkFlowKey, SHAConstants.PULL_BACK_OUTCOME, dto.getUsername());
		}
	}
	
}
