package com.shaic.claim.search;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Policy;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpIntimation;

@Stateless
public class SearchClaimService extends  AbstractDAO<Claim> {

	
	public Page<SearchClaimTableDTO> search(SearchClaimFormDTO formDTO)
	{
		try 
		{
			List<ViewTmpClaim> pageItemList = new ArrayList<ViewTmpClaim>();
			int pageNumber = formDTO.getPageable().getPageNumber();
			String strIntimationNo =  null != formDTO && null != formDTO.getIntimationNo() ? formDTO.getIntimationNo() : null;
			String strPolicyNo =  null != formDTO && null != formDTO.getPolicyNo() ? formDTO.getPolicyNo() : null;
			String strClaimNo =  null != formDTO && null != formDTO.getClaimNo() ? formDTO.getClaimNo() : null;			
			List<SearchClaimTableDTO> searchClaimTableDTO = new ArrayList<SearchClaimTableDTO>(); 
			if(null != formDTO)
			{

				final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
				final CriteriaQuery<ViewTmpClaim> criteriaQuery = builder
						.createQuery(ViewTmpClaim.class);
	
				Root<ViewTmpClaim> searchRoot = criteriaQuery.from(ViewTmpClaim.class);
				
				List<Predicate> predicates = new ArrayList<Predicate>();
	
				List<ViewTmpClaim> resultList = new ArrayList<ViewTmpClaim>();
				
				if (strIntimationNo != null) {
					Predicate intimationPredicate = builder.like(
							searchRoot.<ViewTmpIntimation> get("intimation")
									.<String> get("intimationId"), "%"
									+ strIntimationNo + "%");
					predicates.add(intimationPredicate);
				}
				if (strPolicyNo != null) {
					Predicate policyPredicate = builder
							.like(searchRoot.<ViewTmpIntimation> get("intimation")
									.<Policy> get("policy").<String> get("policyNumber"), "%"
									+ strPolicyNo + "%");
					predicates.add(policyPredicate);
				}
				if (strClaimNo != null) {
					Predicate claimPredicate = builder
							.like(searchRoot.<String> get("claimId"), "%"
									+ strClaimNo + "%");
					predicates.add(claimPredicate);
				}
				
				criteriaQuery.select(searchRoot).where(
						builder.and(predicates.toArray(new Predicate[] {})));

				final TypedQuery<ViewTmpClaim> searchClaimQuery = entityManager
						.createQuery(criteriaQuery);

				int firtResult;
				if(pageNumber > 1){
					firtResult = (pageNumber-1) *10;
				}else{
					firtResult = 0;
				}
				
				if(strIntimationNo == "" && strPolicyNo == "" && strClaimNo == "" /*&& pageItemList.size()>10*/){
					pageItemList = searchClaimQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
				}else{
					pageItemList  = searchClaimQuery.getResultList();
				}
	
				searchClaimTableDTO = SearchClaimMapper.getInstance().getSearchClaimDTO(pageItemList) ;
				
				List<Long>  hospTypeList = new ArrayList<Long>();
				
				Long hospitalTypeId = null;
				
				for (SearchClaimTableDTO objSearchClaimTableDTO : searchClaimTableDTO) {
					hospitalTypeId = objSearchClaimTableDTO.getHospitalTypeId();
					
					 if(hospitalTypeId != null) {
						 hospTypeList.add(hospitalTypeId);
					 }
				}
				
//				ListIterator<ViewTmpClaim> iterClaim = pageItemList.listIterator();
//				List<Long>  hospTypeList = new ArrayList<Long>();
//				
//				while (iterClaim.hasNext())
//				{
//					 ViewTmpClaim objClaim = iterClaim.next();
//					 //MastersValue hospTypeInfo = objClaim.getIntimation().getHospitalType();
//					 Long hospitalTypeId = null;
//					 if(objClaim.getIntimation() != null) {
//						 hospitalTypeId = objClaim.getIntimation().getHospital();
//					 }
//					
//					 /*
//					  * To fetch hospital information from VW_HOSPITALS 
//					  * we require hospital type id. Hence forming
//					  * below list
//					  * 
//					  * **/
//					 if(hospitalTypeId != null) {
//						 hospTypeList.add(hospitalTypeId);
//					 }
//					
//				}
				
				List<Hospitals> resultListForHospitalInfo = new ArrayList<Hospitals>();
				List<SearchClaimTableDTO> searchClaimTableDTOForHospitalInfo = new ArrayList<SearchClaimTableDTO>();
				resultListForHospitalInfo = SHAUtils.getHospitalInformation(entityManager, hospTypeList);
				if(null != resultListForHospitalInfo)
				{
					searchClaimTableDTOForHospitalInfo = SearchClaimMapper.getHospitalInfoList(resultListForHospitalInfo);
					for (SearchClaimTableDTO objSearchClaimTableDTO : searchClaimTableDTO )
					{
						objSearchClaimTableDTO.setViewClaimStatus(SHAConstants.VIEW_CLAIM_STATUS);
						for (SearchClaimTableDTO objSearchClaimTableDTOForHospitalInfo : searchClaimTableDTOForHospitalInfo)
						{
							
							/**
							 * objSearchPEDRequestProcessTableDTOForHospitalInfo.getKey() --> will store the hosptial type id.
							 * objSearchPEDRequestProcessTableDTOForHospitalInfo list belongs to VW_HOSPITAL view. This list is of
							 * Hospital type. In Hospital.java , we store the key. 
							 * 
							 * But this key will come from intimation table hospital type id. objSearchPEDRequestProcessTableDTO is of 
							 * SearchPEDRequestProcessTableDTO , in which we store hospital type in a variable known as hospitalTypeId .
							 * That is why we equate hospitalTypeId from SearchPEDRequestProcessTableDTO with key from HospitalDTO.
							 * */
							if(objSearchClaimTableDTO.getHospitalTypeId() != null &&
									objSearchClaimTableDTOForHospitalInfo.getKey() != null && 
									objSearchClaimTableDTO.getHospitalTypeId().equals(objSearchClaimTableDTOForHospitalInfo.getKey()))
							{
								objSearchClaimTableDTO.setHospitalName(objSearchClaimTableDTOForHospitalInfo.getHospitalName());
								objSearchClaimTableDTO.setHospitalCity(objSearchClaimTableDTOForHospitalInfo.getHospitalCity());
								
								break;
							}
						}
						
					}
				}
			 }
//			Page<Claim> pagedList = super.pagedList(formDTO.getPageable());
			Page<SearchClaimTableDTO> page = new Page<SearchClaimTableDTO>();
			//page.setPageNumber(pagedList.getPageNumber());
//			page.setPageItems(searchClaimTableDTO);
			formDTO.getPageable().setPageNumber(pageNumber+1);
			
			if(pageItemList.size()<10){
				page.setHasNext(false);
			}else{
				page.setHasNext(true);
			}
			
			if(searchClaimTableDTO.isEmpty()){
				formDTO.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(searchClaimTableDTO);
			
			return page;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	@SuppressWarnings("unchecked")
	public Claim getClaimByKey (Long claimKey) {
		
		Query findByKey = entityManager.createNamedQuery("Claim.findAll");

		List<Claim> claimkeyByList = (List<Claim>) findByKey
				.getResultList();

		if (!claimkeyByList.isEmpty()) {
			return claimkeyByList.get(0);

		}
		return null;
	}

	
	
	
	@Override
	public Class<Claim> getDTOClass() {
		// TODO Auto-generated method stub
		return Claim.class;
	}

}
