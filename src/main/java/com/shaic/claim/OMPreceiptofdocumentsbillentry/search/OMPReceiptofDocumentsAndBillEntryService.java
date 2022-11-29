/**
 * 
 */
package com.shaic.claim.OMPreceiptofdocumentsbillentry.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.claim.claimhistory.view.ompView.OMPEarlierRodMapper;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.omp.OMPBenefitCover;


@Stateless
public class OMPReceiptofDocumentsAndBillEntryService extends AbstractDAO<OMPIntimation> {

	public OMPReceiptofDocumentsAndBillEntryService() {
		super();
	}

	@SuppressWarnings("unchecked")
	public Page<OMPReceiptofDocumentsAndBillEntryTableDTO> search(
			OMPReceiptofDocumentsAndBillEntryFormDto formDTO, String userName, String passWord) {
		

		List<OMPClaim> listIntimations = new ArrayList<OMPClaim>(); 
		try{
		String intimationNo =  null != formDTO.getIntimationNo() && !formDTO.getIntimationNo().isEmpty() ? formDTO.getIntimationNo() :null;
		String policyNo =  null != formDTO.getPolicyno() && !formDTO.getPolicyno().isEmpty() ? formDTO.getPolicyno() : null;
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<OMPClaim> criteriaQuery = criteriaBuilder.createQuery(OMPClaim.class);
		
		Root<OMPClaim> root = criteriaQuery.from(OMPClaim.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		if(intimationNo != null){
		Predicate condition1 = criteriaBuilder.like(root.<OMPIntimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
		conditionList.add(condition1);
		}
		if(policyNo != null){
		Predicate condition2 = criteriaBuilder.like(root.<OMPIntimation>get("intimation").<Policy>get("policy").<String>get("policyNumber"), "%"+policyNo+"%");
		conditionList.add(condition2);
		}
		Predicate condition3 = criteriaBuilder.notEqual(root.<Status>get("status").<Long>get("key"), ReferenceTable.PROCESS_REJECTED);
		conditionList.add(condition3);
		
		Predicate condition4 = criteriaBuilder.notEqual(root.<Status>get("status").<Long>get("key"), ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS);
		conditionList.add(condition4);
				
		if(intimationNo == null && policyNo == null){
			criteriaQuery.select(root).where(conditionList.toArray(new Predicate[]{}));
			} else{
		criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}
		final TypedQuery<OMPClaim> typedQuery = entityManager.createQuery(criteriaQuery);
		int pageNumber = formDTO.getPageable().getPageNumber();
		int firtResult;
		if(pageNumber > 1){
			firtResult = (pageNumber-1) *10;
		}else{
			firtResult = 0;
		}

		if(intimationNo == null && policyNo == null){
		listIntimations = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
		}else{
			listIntimations = typedQuery.getResultList();
		}
//		for(Intimation inti:listIntimations){
//			System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
//		}
		List<OMPClaim> doList = listIntimations;
		
		List<OMPReceiptofDocumentsAndBillEntryTableDTO> tableDTO = OMPReceiptofDocumentsAndBillEntryMapper.getInstance().getIntimationDTO(doList);
	
		for(OMPReceiptofDocumentsAndBillEntryTableDTO billEntryDto : tableDTO){
			if(billEntryDto != null){
				billEntryDto.setUserName(userName);
			}
		}
		
		List<OMPReceiptofDocumentsAndBillEntryTableDTO> result = new ArrayList<OMPReceiptofDocumentsAndBillEntryTableDTO>();
		result.addAll(tableDTO);
		Page<OMPReceiptofDocumentsAndBillEntryTableDTO> page = new Page<OMPReceiptofDocumentsAndBillEntryTableDTO>();
		formDTO.getPageable().setPageNumber(pageNumber+1);
		page.setHasNext(true);
		if(result.isEmpty()){
			formDTO.getPageable().setPageNumber(1);
		}
		page.setPageNumber(pageNumber);
		page.setPageItems(result);
		page.setIsDbSearch(true);
		
		return page;
		}
	catch(Exception e){
		e.printStackTrace();
	}
		return null;	
	
		
		
		/*
		try 
		{
			String strIntimationNo = "";
			
			String strPolicyNo = "";
			
			
			List<Map<String, Object>> taskParamObjList = null;
			
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			mapValues.put(SHAConstants.CURRENT_Q,SHAConstants.FLP_SUGGEST_REJECTION_CURRENT_QUEUE);
			
			mapValues.put(SHAConstants.USER_ID,userName);
						
			if(null != formDTO.getIntimationNo() && !formDTO.getIntimationNo().equalsIgnoreCase(""))
			{
				strIntimationNo = formDTO.getIntimationNo(); 
				mapValues.put(SHAConstants.INTIMATION_NO,strIntimationNo);
			}
			if(null != formDTO.getPolicyno() && !formDTO.getPolicyno().equalsIgnoreCase(""))
			{
				
				strPolicyNo = formDTO.getPolicyno();
				
				mapValues.put(SHAConstants.POLICY_NUMBER,strPolicyNo);
			}
			
			List<OMPReceiptofDocumentsAndBillEntryTableDTO> searchProcessRejectionTableDTOForIntimation = new ArrayList<OMPReceiptofDocumentsAndBillEntryTableDTO>();
			Page<OMPReceiptofDocumentsAndBillEntryTableDTO> page = new Page<OMPReceiptofDocumentsAndBillEntryTableDTO>();

			Pageable pageable = formDTO.getPageable();
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
			
				
			Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
			
			DBCalculationService dbCalculationService = new DBCalculationService();
			taskParamObjList = dbCalculationService.getTaskProcedure(setMapValues);	
			
			Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
			List<Long> preauthKeys = new ArrayList<Long>();
			List<Long> intimationKeys = new ArrayList<Long>();
			Integer totalRecords = 0;
			if (null != taskParamObjList) {
				for (Map<String, Object> outPutArray : taskParamObjList) {
					Long preauthKeyValue = (Long) outPutArray.get(SHAConstants.CASHLESS_KEY);
					Long inimationKeyValue = (Long) outPutArray.get(SHAConstants.INTIMATION_KEY);
					
					if(preauthKeyValue != null){
						preauthKeys.add(preauthKeyValue);
						if(null != inimationKeyValue){
							intimationKeys.add(inimationKeyValue);
						}					
					}
					else{
						if(null != inimationKeyValue){
							intimationKeys.add(inimationKeyValue);
						}
					}
						
						
					workFlowMap.put(inimationKeyValue,outPutArray);
					totalRecords = (Integer) outPutArray
							.get(SHAConstants.TOTAL_RECORDS);
				}
			}
			
			
				//intimationKeys.
				 if (null != intimationKeys && 0!= intimationKeys.size()) 
			 	{
					List<Intimation> resultList = new ArrayList<Intimation>();
					resultList = SHAUtils.getIntimationInformation(SHAConstants.INTIMATION_SEARCH, entityManager, intimationKeys);
					List<Intimation> pageItemList = resultList;
					
					OMPReceiptofDocumentsAndBillEntryMapper.getInstance();
					for (Intimation intimation : pageItemList) {
						OMPProcessOmpClaimProcessorTableDTO OMPProcessOmpClaimProcessorTableDTO = new OMPProcessOmpClaimProcessorTableDTO();
						OMPProcessOmpClaimProcessorTableDTO.setIntimationNo(intimation.getIntimationId());
						searchProcessRejectionTableDTOForIntimation.add(OMPProcessOmpClaimProcessorTableDTO);
					}
					searchProcessRejectionTableDTOForIntimation  = OMPReceiptofDocumentsAndBillEntryMapper.getOMPProcessReceiptofDocumentsAndBillTableDTOForIntimation(pageItemList);
					
			 	}
				
					for (OMPProcessOmpClaimProcessorTableDTO objSearchProcessRejectionTableDTO :searchProcessRejectionTableDTOForIntimation)
					{
						
						objSearchProcessRejectionTableDTO.setDbOutArray(workFlowMap.get(objSearchProcessRejectionTableDTO.getKey()));
						
//						objSearchProcessRejectionTableDTO.setHumanTask(humanTaskMap.get(objSearchProcessRejectionTableDTO.getKey()));
//						objSearchProcessRejectionTableDTO.setTaskNumber(taskNumberMap.get(objSearchProcessRejectionTableDTO.getKey()));

						if((null != searchProcessRejectionTableDTOForIntimation && 0 != searchProcessRejectionTableDTOForIntimation.size()))
						{
							for(OMPProcessOmpClaimProcessorTableDTO objProcessRejectionForPreauthTableDTO :searchProcessRejectionTableDTOForIntimation)
							{
								if((objProcessRejectionForPreauthTableDTO.getIntimationNo().equalsIgnoreCase(objSearchProcessRejectionTableDTO.getIntimationNo())))
								{
									objSearchProcessRejectionTableDTO.setPreauthStatus(objProcessRejectionForPreauthTableDTO.getPreauthStatus());
								}
							}
						}
					}
					
		   // Page<Intimation> pagedList = super.pagedList(formDTO.getPageable());
			page.setPageItems(searchProcessRejectionTableDTOForIntimation);
			page.setTotalRecords(totalRecords.intValue());
			
//			if(tasks != null && tasks.getIsNextPageAvailable() || nonMedicalTask != null && nonMedicalTask.getIsNextPageAvailable()){
//				page.setHasNext(true);
//			}
			
			if(formDTO.getPageable() != null){
			page.setPagesAvailable(super.pagedList(formDTO.getPageable()).getPagesAvailable());
			}
			
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	*/}
	
	@Override
	public Class<OMPIntimation> getDTOClass() {
		return OMPIntimation.class;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public List<ViewDocumentDetailsDTO> listOfEarlierAckByClaimKey(Long a_key,
			Long reimbursementKey){

		try{
		Query query = entityManager
				.createNamedQuery("OMPReimbursement.findLatestRODByClaimKey");
		query = query.setParameter("claimKey", a_key);

		// Integer.parseInt(Strin)
		List<Long> keysList = new ArrayList<Long>();
		List<OMPReimbursement> rodList = (List<OMPReimbursement>) query
				.getResultList();

		List<OMPReimbursement> earlierRod = new ArrayList<OMPReimbursement>();
		
		for (int index = rodList.size()-1; index >= 0 ; index--){
			earlierRod.add(rodList.get(index));
			keysList.add(rodList.get(index).getKey());
		}
		Long maximumKey = 0l;
		if (!keysList.isEmpty()) {
			maximumKey = Collections.max(keysList);
		}

		List<ViewDocumentDetailsDTO> listDocumentDetails = OMPEarlierRodMapper
				.getInstance().getDocumentDetailsTableReimbursemntWise(rodList);

		for (ViewDocumentDetailsDTO documentDetailsDTO : listDocumentDetails) {

			String date = SHAUtils.getDateFormat(documentDetailsDTO
					.getReceivedDate());

			if (documentDetailsDTO.getRodKey() != null) {
				Long rodKey = new Long(documentDetailsDTO.getRodKey());
				OMPReimbursement reimbursement = getReimbursement(rodKey);
				if (reimbursement != null) {
					documentDetailsDTO.setRodNumber(reimbursement.getRodNumber());
					documentDetailsDTO.setReimbursementKey(reimbursement.getKey());
					if (documentDetailsDTO.getKey() != null
							&& !documentDetailsDTO.getKey().equals(
									reimbursement.getDocAcknowLedgement().getKey())) {
						documentDetailsDTO.setIsReadOnly(true);
					}
				documentDetailsDTO.setApprovedAmount(reimbursement
							.getApprovedAmount());
				documentDetailsDTO.setStatus(reimbursement.getStatus()
						.getProcessValue());
				documentDetailsDTO.setStatusKey(reimbursement.getStatus()
						.getKey());
				if (reimbursement.getMedicalCompletedDate() != null) {
					documentDetailsDTO
							.setMedicalResponseTime(SHAUtils
									.formatDate(reimbursement
											.getMedicalCompletedDate()));
				}

				documentDetailsDTO.setApprovedAmount(reimbursement
						.getCurrentProvisionAmt());

				if (reimbursement.getStage().getKey()
						.equals(ReferenceTable.BILLING_STAGE)) {
					
					if(reimbursement
							.getBillingApprovedAmount() == null){
						documentDetailsDTO.setApprovedAmount(reimbursement
								.getCurrentProvisionAmt());
						
					}else {
						if(reimbursement
								.getBillingApprovedAmount().equals(0)){
							documentDetailsDTO.setApprovedAmount(reimbursement
									.getCurrentProvisionAmt());
						}else{
							documentDetailsDTO.setApprovedAmount(reimbursement
									.getBillingApprovedAmount());
						}
					}
					

				} else if (reimbursement.getStage().getKey()
						.equals(ReferenceTable.FINANCIAL_STAGE)) {
					
					if(reimbursement
							.getFinancialApprovedAmount() == null){
						documentDetailsDTO.setApprovedAmount(reimbursement
								.getCurrentProvisionAmt());
						
					}else{
					
						if(reimbursement
								.getFinancialApprovedAmount().equals(0)){
							documentDetailsDTO.setApprovedAmount(reimbursement
									.getCurrentProvisionAmt());
						}
						else{
							documentDetailsDTO.setApprovedAmount(reimbursement
									.getFinancialApprovedAmount());	
						}
					
					}
					
				}

				if (ReferenceTable.RE_OPEN_CLAIM_STATUS_KEYS
						.containsKey(reimbursement.getStatus().getKey())) {
					documentDetailsDTO.setApprovedAmount(reimbursement
							.getCurrentProvisionAmt());
				}

				if (reimbursement
						.getStatus()
						.getKey()
						.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
						|| reimbursement
								.getStatus()
								.getKey()
								.equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS)) {

					documentDetailsDTO.setApprovedAmount(reimbursement
							.getCurrentProvisionAmt());

				}

				if (ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement
						.getStatus().getKey())) {
					documentDetailsDTO.setApprovedAmount(reimbursement
							.getCurrentProvisionAmt());
				}

				if (reimbursement.getStatus().getKey()
						.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {

					String statusValue = "MA - "
							+ reimbursement.getStatus().getProcessValue();
					documentDetailsDTO.setStatus(statusValue);

				} else if (reimbursement.getStatus().getKey()
						.equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {

					String statusValue = "FA - "
							+ reimbursement.getStatus().getProcessValue();
					documentDetailsDTO.setStatus(statusValue);
				}
			 }	
			}
		}

//		List<String> hospitalization = new ArrayList<String>();
//
//		for (OMPDocAcknowledgement docAcknowledgement : earlierAck) {
//
//			getDocumentBillClassification(hospitalization, docAcknowledgement);
//
//		}

		for (int i = 0; i < listDocumentDetails.size(); i++) {

//			listDocumentDetails.get(i).setBillClassification(
//					hospitalization.get(i));
			// listDocumentDetails.get(0).setLatestKey(maximumKey);
			OMPReimbursement reimbursement = null;
			if (reimbursementKey != null) {
				reimbursement = getReimbursement(reimbursementKey);
			}
			if (reimbursement != null
					&& reimbursement.getDocAcknowLedgement() != null) {
				listDocumentDetails.get(0).setLatestKey(
						reimbursement.getKey());
			} else {
				listDocumentDetails.get(0).setLatestKey(maximumKey);
			}
		}

		return listDocumentDetails;
		
	}
	catch(Exception e){
		e.printStackTrace();
	}			
		return null;	
	}
	
	@SuppressWarnings("unchecked")
	public OMPReimbursement getReimbursement(Long rodkey) {

	try{
		Query rodQuery = entityManager
				.createNamedQuery("OMPReimbursement.findByKey");
		rodQuery = rodQuery.setParameter("primaryKey", rodkey);
		List<OMPReimbursement> reimbursementList = (List<OMPReimbursement>) rodQuery
				.getResultList();

		if (reimbursementList != null && !reimbursementList.isEmpty()) {

			entityManager.refresh(reimbursementList.get(0));
			return reimbursementList.get(0);
		}
		
	}catch(Exception e){
		e.printStackTrace();
	}	

		return null;

	}

	public Double getClaimedAmountValueForView(Long reimbKey){
		Double totalClaimeAmt = 0d;
		
		if(reimbKey != null)
		{	
			List<OMPBenefitCover> listOfCovers = getOMPBenefitsByRodKey(reimbKey);
			
			for (OMPBenefitCover ompBenefitCover : listOfCovers) {
				totalClaimeAmt += (ompBenefitCover.getBillAmount() != null ? ompBenefitCover.getBillAmount() : 0d);
			}
		}
		return totalClaimeAmt;
	}
	
	public Double getBenefitAddOnOptionalApprovedAmt(Long reimbKey)
	{
		Double benApprovedAmt = 0d;
		
		if(reimbKey != null)
		{	
			List<OMPBenefitCover> listOfCovers = getOMPBenefitsByRodKey(reimbKey);
			
			for (OMPBenefitCover ompBenefitCover : listOfCovers) {
				benApprovedAmt += (ompBenefitCover.getApprovedAmount() != null ? ompBenefitCover.getApprovedAmount() : 0d);
			}
		}
		return benApprovedAmt;
	}

	public List<OMPBenefitCover> getOMPBenefitsByRodKey(Long rodKey){
		List<OMPBenefitCover> listOfOMPbenefits = new ArrayList<OMPBenefitCover>();
	try{
		Query ompbenefitQuery = entityManager.createNamedQuery("OMPBenefitCover.findByRodKey");
		ompbenefitQuery.setParameter("rodKey", rodKey);
		listOfOMPbenefits =  (List<OMPBenefitCover>)ompbenefitQuery.getResultList();
		
		if(listOfOMPbenefits != null && !listOfOMPbenefits.isEmpty()){
			for (OMPBenefitCover ompBenefitCover : listOfOMPbenefits) {
				entityManager.refresh(ompBenefitCover);
			}
		}
	}
	catch(Exception e){
		e.printStackTrace();
	}	
		return listOfOMPbenefits;		
	}
	
//	private String getDocumentBillClassification(List<String> hospitalization,
//			OMPDocAcknowledgement docAcknowledgement) {
//		String classification = "";
//		if (docAcknowledgement.getPreHospitalisationFlag() != null) {
//			if (docAcknowledgement.getPreHospitalisationFlag().equals("Y")) {
//				if (classification.equals("")) {
//					classification = "Pre-Hospitalisation";
//				} else {
//					classification = classification + ","
//							+ "Pre-Hospitalisation";
//				}
//			}
//		}
//		if (docAcknowledgement.getHospitalisationFlag() != null) {
//			if (docAcknowledgement.getHospitalisationFlag().equals("Y")) {
//
//				if (classification.equals("")) {
//					classification = "Hospitalisation";
//				} else {
//					classification = classification + "," + " Hospitalisation";
//				}
//			}
//		}
//		if (docAcknowledgement.getPostHospitalisationFlag() != null) {
//
//			if (docAcknowledgement.getPostHospitalisationFlag().equals("Y")) {
//
//				if (classification.equals("")) {
//					classification = "Post-Hospitalisation";
//				} else {
//					classification = classification + ","
//							+ " Post-Hospitalisation";
//				}
//			}
//		}
//
//		if (docAcknowledgement.getHospitalCashFlag() != null) {
//
//			if (docAcknowledgement.getHospitalCashFlag().equals("Y")) {
//
//				if (classification.equals("")) {
//					classification = "Add on Benefits (Hospital cash)";
//				} else {
//					classification = classification + ","
//							+ "Add on Benefits (Hospital cash)";
//				}
//			}
//		}
//
//		if (docAcknowledgement.getPatientCareFlag() != null) {
//
//			if (docAcknowledgement.getPatientCareFlag().equals("Y")) {
//
//				if (classification.equals("")) {
//					classification = "Add on Benefits (Patient Care)";
//				} else {
//					classification = classification + ","
//							+ "Add on Benefits (Patient Care)";
//				}
//			}
//		}
//
//		if (docAcknowledgement.getLumpsumAmountFlag() != null) {
//
//			if (docAcknowledgement.getLumpsumAmountFlag().equals("Y")) {
//
//				if (classification.equals("")) {
//					classification = "Lumpsum Amount";
//				} else {
//					classification = classification + "," + "Lumpsum Amount";
//				}
//			}
//		}
//
//		if (docAcknowledgement.getHospitalizationRepeatFlag() != null) {
//
//			if (docAcknowledgement.getHospitalizationRepeatFlag().equals("Y")) {
//
//				if (classification.equals("")) {
//					classification = "Hospitalization Repeat";
//				} else {
//					classification = classification + ","
//							+ "Hospitalization Repeat";
//				}
//			}
//		}
//
//		if (docAcknowledgement.getPartialHospitalisationFlag() != null) {
//
//			if (docAcknowledgement.getPartialHospitalisationFlag().equals("Y")) {
//
//				if (classification.equals("")) {
//					classification = "Partial Hospitalisation";
//				} else {
//					classification = classification + ","
//							+ "Partial Hospitalisation";
//				}
//			}
//		}
//
//		hospitalization.add(classification);
//
//		return classification;
//	}
	
}
