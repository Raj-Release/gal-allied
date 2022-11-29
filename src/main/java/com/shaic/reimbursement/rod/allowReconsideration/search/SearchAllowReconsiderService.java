package com.shaic.reimbursement.rod.allowReconsideration.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.crypto.SealedObject;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


import org.apache.jasper.tagplugins.jstl.core.ForEach;


import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementRejection;
import com.shaic.domain.ReimbursementRejectionService;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Preauth;

@Stateless
public class SearchAllowReconsiderService extends AbstractDAO<Reimbursement>{
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	public  ReimbursementRejectionService reimbursementRejectionService; 
	
	public Page<SearchAllowReconsiderationTableDTO> doSearch(SearchAllowReconsiderationDTO searchFormDto,String userName,String passWord) {
		
		try{
			String intimationNo = searchFormDto.getIntimationNo();
			String rodNo = searchFormDto.getRodNo();
			List<SearchAllowReconsiderationTableDTO> tableDto = new ArrayList<SearchAllowReconsiderationTableDTO>();
			if(searchFormDto != null){
				final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
				final CriteriaQuery<Reimbursement> criteriaQuery = builder
						.createQuery(Reimbursement.class);
	
				Root<Reimbursement> searchRoot = criteriaQuery.from(Reimbursement.class);
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (intimationNo != null && !intimationNo.isEmpty()) {
					Predicate intimationPredicate = builder.like(
							searchRoot.<Claim> get("claim")
									.<Intimation>get("intimation").<String> get("intimationId"), "%"
									+ intimationNo + "%");
					predicates.add(intimationPredicate);
				}
				if (rodNo != null && !rodNo.isEmpty()) {
					Predicate policyPredicate = builder
							.like(searchRoot.<String> get("rodNumber"), "%"
									+ rodNo + "%");
					predicates.add(policyPredicate);
				}
				
//				Predicate condition1= builder.equal(searchRoot.<Status>get("status").<Long>get("key"),ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS);				
//				predicates.add(condition1);
				
				criteriaQuery.select(searchRoot).where(
						builder.and(predicates.toArray(new Predicate[] {})));

				final TypedQuery<Reimbursement> searchClaimQuery = entityManager
						.createQuery(criteriaQuery);

				List<Reimbursement> pageItemList  = searchClaimQuery.getResultList();
				
				List<SearchAllowReconsiderationTableDTO> resultList = AllowRecosniderMapper.getInstance().getRejectionDTO(pageItemList);
				List<SearchAllowReconsiderationTableDTO> finalList = new ArrayList<SearchAllowReconsiderationTableDTO>();
				/*Set<SearchAllowReconsiderationTableDTO> duplicates = new HashSet<SearchAllowReconsiderationTableDTO>();*/
				List<String> rodList = new ArrayList<String>();
				
				for (SearchAllowReconsiderationTableDTO searchAllowReconsiderationTableDTO : resultList) {
					//if(!rodList.contains(searchAllowReconsiderationTableDTO.getRodNo())){
					if(searchAllowReconsiderationTableDTO.getRodNo() != null) {
						//Reimbursement reimbursementObject = reimbursementRejectionService.getReimbursementObject(searchAllowReconsiderationTableDTO.getRodNo());
						ReimbursementRejection rejectionDetails = reimbursementRejectionService.getRejectionKeyByReimbursement(searchAllowReconsiderationTableDTO.getRodKey());
						if(rejectionDetails != null && (rejectionDetails.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS) ||
								rejectionDetails.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS) ||
								rejectionDetails.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_APPROVE_REJECT_STATUS)
								|| rejectionDetails.getStatus().getKey().equals(ReferenceTable.PAYMENT_REJECTED)) && 
								(rejectionDetails.getAllowReconsideration() == null || !(rejectionDetails.getAllowReconsideration() != null && rejectionDetails.getAllowReconsideration().equalsIgnoreCase("Y")))){
							searchAllowReconsiderationTableDTO.setIsRejected(Boolean.TRUE);
						}
						else if(null != searchAllowReconsiderationTableDTO.getStatusKey() && ((ReferenceTable.PAYMENT_REJECTED).equals(searchAllowReconsiderationTableDTO.getStatusKey()))){
							searchAllowReconsiderationTableDTO.setIsRejected(Boolean.TRUE);
						}
						if(rejectionDetails != null && rejectionDetails.getRejectionDraftDate() != null){
						searchAllowReconsiderationTableDTO.setRejectionDate(rejectionDetails.getRejectionDraftDate());
						}
						searchAllowReconsiderationTableDTO.setRejectionReason(rejectionDetails != null && rejectionDetails.getRejectionCategory() != null && rejectionDetails.getRejectionCategory().getValue()!=null ? rejectionDetails.getRejectionCategory().getValue() : "");
						searchAllowReconsiderationTableDTO.setRejectionRemarks(rejectionDetails != null && rejectionDetails.getRejectionRemarks() != null ? rejectionDetails.getRejectionRemarks() : "");
						searchAllowReconsiderationTableDTO.setCpuCodeandName(searchAllowReconsiderationTableDTO.getCpuId()+"-"+searchAllowReconsiderationTableDTO.getDescription());
						finalList.add(searchAllowReconsiderationTableDTO);
						rodList.add(searchAllowReconsiderationTableDTO.getRodNo());
					}
					//}
					
				}
//				duplicates.addAll(finalList);
//				finalList.addAll(duplicates);
				Page<SearchAllowReconsiderationTableDTO> pageList = new Page<SearchAllowReconsiderationTableDTO>();
				pageList.setPageItems(finalList);
				return pageList;
				
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Class<Reimbursement> getDTOClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
