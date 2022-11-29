package com.shaic.claim.userproduct.document.search;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.table.Page;
import com.shaic.domain.MasUser;


@Stateless
public class SearchDoctorDetailsService  {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public SearchDoctorDetailsService() {
		super();
	}
	
	public  Page<SearchDoctorDetailsTableDTO> search(
			SearchDoctorNameDTO searchFormDTO) {
		
		List<MasUser> listOfClaim = new ArrayList<MasUser>(); 
		try{
			String doctorName = null != searchFormDTO.getDoctorName() && !searchFormDTO.getDoctorName().isEmpty() ? searchFormDTO.getDoctorName() :null;
			
		
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<MasUser> criteriaQuery = criteriaBuilder.createQuery(MasUser.class);
		
		Root<MasUser> root = criteriaQuery.from(MasUser.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		if(doctorName != null){
		Predicate intimationPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.<String>get("userName")), "%"+doctorName.toLowerCase()+"%");
		conditionList.add(intimationPredicate);
		}
//		
		if(doctorName != null){
			criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
		}
		final TypedQuery<MasUser> typedQuery = entityManager.createQuery(criteriaQuery);
		int pageNumber = searchFormDTO.getPageable().getPageNumber();
		int firtResult;
		if (pageNumber > 1) 
		{
			firtResult = (pageNumber - 1) * 10;
		} 
		else 
		{
			firtResult = 0;
		}
//		
//	
		/*if(doctorName == null){
			
			listOfClaim = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
			
		}else{
			listOfClaim = typedQuery.getResultList();
		}*/
			listOfClaim = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
//		
//		
//		List<TmpEmployee> doList = new ArrayList<TmpEmployee>();
//		
//		for(TmpEmployee claim:listOfClaim){
//					doList.add(claim);
//		}
//		doList = listOfClaim;
			
//			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//			final CriteriaQuery<TmpEmployee> criteriaQuery = criteriaBuilder.createQuery(TmpEmployee.class);
//			
//			Root<TmpEmployee> root = criteriaQuery.from(TmpEmployee.class);
//			
//			List<Predicate> conditionList = new ArrayList<Predicate>();
//			
//			
//			if (insuredName != null) {
//				Predicate insuredNamePredicate = builder.like(
//						builder.upper(intimationRoot.<Insured> get(
//								"insured").<String> get(
//								"insuredName")), insuredName);
//				predicates.add(insuredNamePredicate);
//			}
			
			
			
			
			//MasUser fvrInitiatorDetail = getDoctorDetails(doctorName);
			//listOfClaim.add(fvrInitiatorDetail);
			
		List<SearchDoctorDetailsTableDTO> tableDTO = SearchDoctorDetailsMapper.getClaimDTO(listOfClaim);
		//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
	
		
		List<SearchDoctorDetailsTableDTO> result = new ArrayList<SearchDoctorDetailsTableDTO>();
		result.addAll(tableDTO);
		
	
		Page<SearchDoctorDetailsTableDTO> page = new Page<SearchDoctorDetailsTableDTO>();
		//searchFormDTO.getPageable().setPageNumber(pageNumber + 1);
		if(result.size()<=10)
		{
			page.setHasNext(false);
		}
		else
		{
		page.setHasNext(true);
		}
		if (result.isEmpty()) {
			searchFormDTO.getPageable().setPageNumber(1);
		}
		//page.setPageNumber(pageNumber);
		page.setPageItems(result);
		page.setIsDbSearch(true);
		return page;
		}
	catch(Exception e){
		e.printStackTrace();
		System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
	}
		return null;
			
	}
	 private MasUser getDoctorDetails(String initiatorId)
		{
		  MasUser fvrInitiatorDetail;
			Query findByTransactionKey = entityManager.createNamedQuery(
					"MasUser.getEmpByName").setParameter("userName", initiatorId);
			try{
				fvrInitiatorDetail =(MasUser) findByTransactionKey.getSingleResult();
				return fvrInitiatorDetail;
			}
			catch(Exception e)
			{
				return null;
			}
								
		}
	 public  Page<SearchDoctorDetailsTableDTO> searchDoctor(
			 SearchDoctorDetailsTableDTO searchFormDTO) {
			
			List<SearchDoctorDetailsTableDTO> result = new ArrayList<SearchDoctorDetailsTableDTO>();
			result.add(searchFormDTO);
			
		
			Page<SearchDoctorDetailsTableDTO> page = new Page<SearchDoctorDetailsTableDTO>();
			//searchFormDTO.getPageable().setPageNumber(pageNumber + 1);
			if(result.size()<=10)
			{
				page.setHasNext(false);
			}
			else
			{
			page.setHasNext(true);
			}
			
			//page.setPageNumber(pageNumber);
			page.setPageItems(result);
			page.setIsDbSearch(true);
			return page;
			
				
		}
}
