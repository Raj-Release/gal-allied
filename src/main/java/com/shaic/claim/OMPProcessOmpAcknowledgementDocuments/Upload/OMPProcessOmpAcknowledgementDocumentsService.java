package com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.claim.OMPProcessOmpClaimProcessor.search.OMPProcessOmpClaimProcessorMapper;
import com.shaic.claim.OMPProcessOmpClaimProcessor.search.OMPProcessOmpClaimProcessorTableDTO;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.domain.preauth.Preauth;


@Stateless
//public class OMPProcessOmpClaimProcessorService extends AbstractDAO<Preauth> {
public class OMPProcessOmpAcknowledgementDocumentsService extends AbstractDAO<OMPIntimation> {

	@EJB
	private OMPIntimationService ompIntimationService;
	
	public OMPProcessOmpAcknowledgementDocumentsService() {
		super();
	}

	@SuppressWarnings("unchecked")
	public Page<OMPProcessOmpAcknowledgementDocumentsTableDTO> search(
			OMPProcessOmpAcknowledgementDocumentsFormDto formDTO, String userName, String passWord) {
		
		List<OMPClaim> listIntimations = new ArrayList<OMPClaim>(); 
		try{
		String intimationNo =  null != formDTO.getIntimationNo() && !formDTO.getIntimationNo().isEmpty() ? formDTO.getIntimationNo() :null;
		String policyNo =  null != formDTO.getPolicyno() && !formDTO.getPolicyno().isEmpty() ? formDTO.getPolicyno() : null;
		String classification = formDTO.getClassification() != null ? formDTO.getClassification().getValue() != null ? formDTO.getClassification().getValue(): null : null;
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
		/*if(classification!=null){
			List<Long> claimbyClassification = ompIntimationService.getClaimbyClassification(classification);
			Expression<Long> exp = root.get("key");
			Predicate predicate = exp.in(claimbyClassification);
			conditionList.add(predicate);
		}*/
		Predicate condition3 = criteriaBuilder.notEqual(root.<Status>get("status").<Long>get("key"), ReferenceTable.OMP_REGISTRATION_REJECTED);
//		conditionList.add(condition3);
		
//		Predicate condition4 = criteriaBuilder.notEqual(root.<Status>get("status").<Long>get("key"), ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS);
//		conditionList.add(condition4);
		
		Predicate condition5 = criteriaBuilder.notEqual(root.<Status>get("status").<Long>get("key"), ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY);
//		conditionList.add(condition5);
		Predicate condition2 = criteriaBuilder.or(condition3,condition5);
		Predicate condition6 = criteriaBuilder.equal(root.<Status>get("status").<Long>get("key"), ReferenceTable.CLAIM_REGISTERED_STATUS);
//		conditionList.add(condition6);
		Predicate condition7 = criteriaBuilder.equal(root.<Status>get("status").<Long>get("key"), ReferenceTable.OMP_PROCESSOR_REJECT_STATUS);
//		conditionList.add(condition7);
		Predicate condition8 = criteriaBuilder.equal(root.<Status>get("status").<Long>get("key"), ReferenceTable.OMP_APPROVER_REJECT_STATUS);
//		conditionList.add(condition8);
		
		Predicate condition9 = criteriaBuilder.equal(root.<Status>get("status").<Long>get("key"), ReferenceTable.FINANCIAL_SETTLED);
//		conditionList.add(condition9);
		Predicate condition10 = criteriaBuilder.or(condition6,condition7,condition8,condition9);
		Predicate condition4 = criteriaBuilder.and(condition10,condition2);
		conditionList.add(condition4);
		
		
		//IMSSUPPOR-28836
		/*Predicate condition6 = criteriaBuilder.notEqual(root.<Status>get("status").<Long>get("key"), ReferenceTable.CLAIM_CLOSED_STATUS);
		conditionList.add(condition6);*/
				
		if(intimationNo == null && policyNo == null && classification==null){
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

		if(intimationNo == null && policyNo == null && classification==null){
		listIntimations = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
		}else{
			listIntimations = typedQuery.getResultList();
		}
//		for(Intimation inti:listIntimations){
//			System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
//		}
		List<OMPClaim> doList = listIntimations;
		
//		List<OMPReceiptofDocumentsAndBillEntryTableDTO> tableDTO = OMPReceiptofDocumentsAndBillEntryMapper.getInstance().getIntimationDTO(doList);
		List<OMPProcessOmpAcknowledgementDocumentsTableDTO> tableDTO = OMPProcessOmpAcknowledgementDocumentsMapper.getAckInstance().getOMPProcessOmpAcknowledgementDocumentsTableDTOForIntimation(doList);
	
		for(OMPProcessOmpAcknowledgementDocumentsTableDTO billEntryDto : tableDTO){
			if(billEntryDto != null){
				billEntryDto.setEventcode(billEntryDto.getEventcode()+'-'+ billEntryDto.getEventDesc());
				billEntryDto.setUserName(userName);
				
				if(billEntryDto.getNonHospitalizationFlag() != null && billEntryDto.getNonHospitalizationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG) && billEntryDto.getLossDetail() != null){
					billEntryDto.setAilment(billEntryDto.getLossDetail());
				}
			}
			
		}
		
//		List<OMPReceiptofDocumentsAndBillEntryTableDTO> result = new ArrayList<OMPReceiptofDocumentsAndBillEntryTableDTO>();
		List<OMPProcessOmpAcknowledgementDocumentsTableDTO> result = new ArrayList<OMPProcessOmpAcknowledgementDocumentsTableDTO>();
		result.addAll(tableDTO);
//		Page<OMPReceiptofDocumentsAndBillEntryTableDTO> page = new Page<OMPReceiptofDocumentsAndBillEntryTableDTO>();
		Page<OMPProcessOmpAcknowledgementDocumentsTableDTO> page = new Page<OMPProcessOmpAcknowledgementDocumentsTableDTO>();
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
	}

	@Override
	public Class<OMPIntimation> getDTOClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
