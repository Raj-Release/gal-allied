package com.shaic.paclaim.reimbursement.searchuploaddocumentsoutsideprocess;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived.UploadDocumentsForAckNotReceivedFormDTO;
import com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived.UploadDocumentsForAckNotReceivedTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.Preauth;

@Stateless
public class PAUploadDocumentsOutsideProcessService extends AbstractDAO<Preauth>{

	
	@PersistenceContext
	protected EntityManager entityManager;

	public PAUploadDocumentsOutsideProcessService() {
		super();
	}

	public Page<UploadDocumentsForAckNotReceivedTableDTO> search(UploadDocumentsForAckNotReceivedFormDTO searchFormDTO, String userName,String passWord) {
		List<Preauth> cashlessList = new ArrayList<Preauth>();
		List<Reimbursement> reimbursementList = new ArrayList<Reimbursement>();
		try {
			String intimationNo = null != searchFormDTO && !searchFormDTO.getIntimationNo().isEmpty() ? searchFormDTO.getIntimationNo() : null;
			String policyNo = null != searchFormDTO && !searchFormDTO.getPolicyNo().isEmpty() ? searchFormDTO.getPolicyNo() : null;
			
			
			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaBuilder criteriaBuilder1 = entityManager.getCriteriaBuilder();
			
			final CriteriaQuery<Preauth> criteriaQuery = criteriaBuilder.createQuery(Preauth.class);
			final CriteriaQuery<Reimbursement> criteriaQuery1 = criteriaBuilder1.createQuery(Reimbursement.class);
			

			Root<Preauth> root = criteriaQuery.from(Preauth.class);
			Root<Reimbursement> root1 = criteriaQuery1.from(Reimbursement.class);
			
			List<Predicate> conditionList = new ArrayList<Predicate>();
			List<Predicate> conditionList1 = new ArrayList<Predicate>();
			
			
			SelectValue selType = null;
			String strType = null;
			
			selType = searchFormDTO.getType();
			if (null != selType && null != selType.getValue()) {
				strType = selType.getValue();
			}
			
			
			if (intimationNo != null) {
				Predicate condition1 = criteriaBuilder.like(root.<Intimation> get("intimation").<String> get("intimationId"), "%"+ intimationNo + "%");
				conditionList.add(condition1);
			}
			if (policyNo != null) {
				Predicate condition2 = criteriaBuilder.like(root.<Intimation> get("intimation").<Policy> get("policy").<String> get("policyNumber"), "%" + policyNo+ "%");
				conditionList.add(condition2);
			}
				
			if (intimationNo != null) {
				Predicate condition1 = criteriaBuilder1.like(root1.<Claim> get("claim").<Intimation> get("intimation").<String> get("intimationId"), "%"+ intimationNo + "%");
				conditionList1.add(condition1);
			}
			if (policyNo != null) {
				Predicate condition2 = criteriaBuilder1.like(root1.<Claim> get("claim").<Intimation> get("intimation").<Policy> get("policy").<String> get("policyNumber"), "%" + policyNo+ "%");
				conditionList1.add(condition2);
			}
		
			List<Long> myStatusList = new ArrayList<Long>();				
			
			myStatusList.add(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS);
			myStatusList.add(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS);
			myStatusList.add(ReferenceTable.PREAUTH_REJECT_STATUS);
			myStatusList.add(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS);
			myStatusList.add(ReferenceTable.ENHANCEMENT_REJECT_STATUS);
			myStatusList.add(ReferenceTable.ENHANCEMENT_DENIAL_OF_CASHLESS_STATUS);
			myStatusList.add(ReferenceTable.STANDALONE_WITHDRAW_STATUS);
			myStatusList.add(ReferenceTable.PREAUTH_REJECT_STATUS);	
			myStatusList.add(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS);
			myStatusList.add(ReferenceTable.FINANCIAL_APPROVE_STATUS);
			myStatusList.add(ReferenceTable.PAYMENT_SETTLED);
			myStatusList.add(ReferenceTable.FINANCIAL_SETTLED);
			myStatusList.add(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS);
			Expression<Long> exp = root.<Status>get("status").<Long>get("key");
			Predicate condition3 = exp.in(myStatusList);				
			conditionList.add(condition3);	
			
			
			
			List<Long> myStatusList1 = new ArrayList<Long>();
			myStatusList1.add(ReferenceTable.CREATE_ROD_CLOSED);		
			myStatusList1.add(ReferenceTable.BILL_ENTRY_CLOSED);	
			myStatusList1.add(ReferenceTable.ZONAL_REVIEW_CLOSED);
			myStatusList1.add(ReferenceTable.CLAIM_REQUEST_CLOSED);		
			myStatusList1.add(ReferenceTable.BILLING_CLOSED);
			myStatusList1.add(ReferenceTable.FINANCIAL_CLOSED);
			myStatusList1.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS);
			myStatusList1.add(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS);
			myStatusList1.add(ReferenceTable.PAYMENT_REJECTED);
			myStatusList1.add(ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS);
			myStatusList1.add(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
			myStatusList1.add(ReferenceTable.FINANCIAL_APPROVE_STATUS);
			myStatusList1.add(ReferenceTable.PAYMENT_SETTLED);
			myStatusList1.add(ReferenceTable.FINANCIAL_SETTLED);
			Expression<Long> exp1 = root1.<Status>get("status").<Long>get("key");
			Predicate condition4 = exp1.in(myStatusList1);				
			conditionList1.add(condition4);
			
		
			
			criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			criteriaQuery1.select(root1).where(criteriaBuilder1.and(conditionList1.toArray(new Predicate[] {})));

			final TypedQuery<Preauth> typedQuery = entityManager.createQuery(criteriaQuery);
			final TypedQuery<Reimbursement> typedQuery1 = entityManager.createQuery(criteriaQuery1);
			
			
			int pageNumber = searchFormDTO.getPageable().getPageNumber();
			@SuppressWarnings("unused")
			int firtResult;  
			
			if (pageNumber > 1) {
				firtResult = (pageNumber - 1) * 10;
			} 
			else {
				firtResult = 1;
			}
			
			cashlessList = typedQuery.getResultList();
			reimbursementList = typedQuery1.getResultList();
			
			List<Preauth> doList = cashlessList;
			List<Reimbursement> doList1 = reimbursementList;

			List<UploadDocumentsForAckNotReceivedTableDTO> finalList = new ArrayList<UploadDocumentsForAckNotReceivedTableDTO>();
			List<UploadDocumentsForAckNotReceivedTableDTO> finalListWithoutDuplicate = new ArrayList<UploadDocumentsForAckNotReceivedTableDTO>();
			
			List<UploadDocumentsForAckNotReceivedTableDTO> tableDTO = PAUploadDocumentsOutsideProcessMapper.getInstance().getUploadDocumentForAckNotReceivedTableObj(doList);

			for (UploadDocumentsForAckNotReceivedTableDTO searchUploadDocumentsTableDTO : tableDTO) {
				
				searchUploadDocumentsTableDTO.setUsername(userName);
				finalList.add(searchUploadDocumentsTableDTO);
			}
			
		//	finalList = getHospitalDetails(finalList);
			
			List<UploadDocumentsForAckNotReceivedTableDTO> tableDTO1 = PAUploadDocumentsOutsideProcessMapper.getUploadDocumentForAckNotReceivedTableObj1(doList1);

			for (UploadDocumentsForAckNotReceivedTableDTO searchUploadDocumentsTableDTO : tableDTO1) {
								
				searchUploadDocumentsTableDTO.setUsername(userName);
				finalList.add(searchUploadDocumentsTableDTO);
			}
			
			int duplicateCount = 0;	
			if(null != finalList && !finalList.isEmpty()){					
			Long claimKey = finalList.get(0).getClaimKey();	
			
			for (UploadDocumentsForAckNotReceivedTableDTO finalList1 : finalList) {				
				if(claimKey.equals(finalList1.getClaimKey())){
						duplicateCount++;
					}
				}			
			}
						
			if(duplicateCount >= 1){
				finalListWithoutDuplicate.add(finalList.get(0));
			}
			finalListWithoutDuplicate = getHospitalDetails(finalListWithoutDuplicate);
			
			List<UploadDocumentsForAckNotReceivedTableDTO> result = new ArrayList<UploadDocumentsForAckNotReceivedTableDTO>();
			result.addAll(finalListWithoutDuplicate);
			Page<UploadDocumentsForAckNotReceivedTableDTO> page = new Page<UploadDocumentsForAckNotReceivedTableDTO>();
			searchFormDTO.getPageable().setPageNumber(pageNumber + 1);
			page.setHasNext(true);
			if (result.isEmpty()) {
				searchFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(result);
			page.setIsDbSearch(true);
			return page;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh" + e.getMessage()
					+ "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		}
		return null;
	}

	@Override
	public Class<Preauth> getDTOClass() {
		// TODO Auto-generated method stub
		return Preauth.class;
	}

	

	private List<UploadDocumentsForAckNotReceivedTableDTO> getHospitalDetails(
			List<UploadDocumentsForAckNotReceivedTableDTO> tableDTO) {
		Hospitals hospitalDetail;
		for (int index = 0; index < tableDTO.size(); index++) {

			Query findByHospitalKey = entityManager.createNamedQuery("Hospitals.findByKey").setParameter("key",
					tableDTO.get(index).getHospitalNameID());
			try {
				hospitalDetail = (Hospitals) findByHospitalKey
						.getSingleResult();
				if (hospitalDetail != null) {
					tableDTO.get(index).setHospitalName(
							hospitalDetail.getName());

					tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId() + " ,"+ hospitalDetail.getCity());
					tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
					
					TmpCPUCode cpuCode = getTmpCPUCode(tableDTO.get(0).getCpuId());
					
					if(null != cpuCode){
						
						tableDTO.get(index).setCpuCode(cpuCode.getCpuCode().toString());
					}
					
					
				}
			} catch (Exception e) {
				continue;
			}

		}

		return tableDTO;
	}

	private TmpCPUCode getTmpCPUCode(Long cpuId) {
		try {
			Query findCpuCode = entityManager.createNamedQuery(
					"TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
			TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
			return tmpCPUCode;
		} catch (Exception e) {

		}
		return null;
	}

	
	private Reimbursement getType(Long key) {
		try {
			Query findType = entityManager.createNamedQuery(
					"Reimbursement.findByKey").setParameter("primaryKey", key);
			Reimbursement reimbursement = (Reimbursement) findType
					.getSingleResult();
			return reimbursement;
		} catch (Exception e) {

		}
		return null;
	}
	
	
	
	public List<UploadDocumentDTO> getUploadedDocumentDetails(String intimationNo,String docSource)
    {
    	Query query = entityManager.createNamedQuery("DocumentDetails.findByIntimationNoAndDocSource");
    	query = query.setParameter("intimationNumber",intimationNo);
    	query = query.setParameter("documentSource",docSource);
    	
    	List<DocumentDetails> docDetailsList = query.getResultList();
    	List<UploadDocumentDTO> uploadDTOList = null;
    	if(null != docDetailsList && !docDetailsList.isEmpty())
    	{
    		uploadDTOList = new ArrayList<UploadDocumentDTO>();
    		for (DocumentDetails docDetails : docDetailsList) {
    			UploadDocumentDTO uploadDocDTO = new UploadDocumentDTO();
    			if(null != docDetails.getDocumentType())
    			{
    				SelectValue selValue = new SelectValue();
    				//selValue.setId(docDetails.getDocumentType());
    				selValue.setValue(docDetails.getDocumentType());
    				uploadDocDTO.setFileType(selValue);
    			}
    			uploadDocDTO.setDocDetailsKey(docDetails.getKey());
    			uploadDocDTO.setFileName(docDetails.getFileName());    			
    			if(null != docDetails.getCashlessNumber()){
    			uploadDocDTO.setReferenceNoValue(docDetails.getCashlessNumber());
    			}
    			if(null != docDetails.getReimbursementNumber()){
    				uploadDocDTO.setReferenceNoValue(docDetails.getReimbursementNumber());
    			}
    			uploadDTOList.add(uploadDocDTO);
			}
    		return uploadDTOList;
    	}
    	return null;
    }

	


}
