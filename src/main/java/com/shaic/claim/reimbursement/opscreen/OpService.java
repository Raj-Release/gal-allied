package com.shaic.claim.reimbursement.opscreen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;

import com.shaic.arch.table.Page;
//import com.shaic.domain.OPPayment;

@Stateless

public class OpService {
	
	@PersistenceContext(unitName = "PERSISTENCE_UNIT_NAME", type = PersistenceContextType.EXTENDED)
	protected EntityManager entityManager;
	
		
	public OpService() {
		super();
	}
	
	public  Page<OpTableDTO> search(OpFormDTO opFormDTO,String userName, String passWord) {
		
//		List<OPPayment> listIntimations = new ArrayList<OPPayment>(); 
		try{
		
		
		Date fromDate = null != opFormDTO.getFromDate() ? opFormDTO.getFromDate() : null;
		Date toDate = null != opFormDTO.getToDate() ? opFormDTO.getToDate() : null;
		
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//		final CriteriaQuery<OPPayment> criteriaQuery = criteriaBuilder.createQuery(OPPayment.class);
				

//		 Root<OPPayment> root = criteriaQuery.from(OPPayment.class);
 		 List<Predicate> conditionList = new ArrayList<Predicate>();
			
		 					
	
		
//		criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
		
//		final TypedQuery<OPPayment> typedQuery = entityManager.createQuery(criteriaQuery);
		int pageNumber = opFormDTO.getPageable().getPageNumber();
		@SuppressWarnings("unused")
		int firtResult;
		
		if (pageNumber > 1) {
			firtResult = (pageNumber - 1) * 10;
		} 
		else {
			firtResult = 1;
		}
		
//		listIntimations = typedQuery.getResultList();
				
//	    List<OPPayment> doList = listIntimations;

//		List<OpTableDTO> tableDTO = OpMapper.getOpTableObjects(doList);

			
		List<OpTableDTO> result = new ArrayList<OpTableDTO>();
//		result.addAll(tableDTO);
		Page<OpTableDTO> page = new Page<OpTableDTO>();
		opFormDTO.getPageable().setPageNumber(pageNumber + 1);
		page.setHasNext(true);
		if (result.isEmpty()) {
			opFormDTO.getPageable().setPageNumber(1);
		}
		page.setPageNumber(pageNumber);
		page.setPageItems(result);

		return page;
	  }
		catch (Exception e) 
	   {
	     e.printStackTrace();
	   }
	   return null;	
		
		

}
}