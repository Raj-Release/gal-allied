package com.shaic.claim.reports.autoallocationaancelreport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.AutoAllocationCancelRemarks;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasUser;
import com.shaic.domain.preauth.UserLoginDetails;
@Stateless
public class CancelService extends AbstractDAO<AutoAllocationCancelRemarks>{

	@PersistenceContext
	protected EntityManager entityManager;


	public Page<AutoAllocationCancelDetailReportDTO> getCancelStatusDetails(CancelSearchDTO searchDto,String userName, String passWord) {
		
		List<AutoAllocationCancelRemarks> resultList = new ArrayList<AutoAllocationCancelRemarks>();
		
		
		Date fromDate = null != searchDto.getFromDate() ? searchDto.getFromDate() : null;
		Date toDate = null != searchDto.getToDate() ? searchDto.getToDate() : null;
		
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<AutoAllocationCancelRemarks> criteriaQuery = criteriaBuilder.createQuery(AutoAllocationCancelRemarks.class);
		
		Root<AutoAllocationCancelRemarks> root = criteriaQuery.from(AutoAllocationCancelRemarks.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();	
		
		if(null != fromDate)
		{
			Predicate condition1 = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("cancelledDate"), fromDate);
			conditionList.add(condition1);
		}
		
			
						
		if(null!= toDate)
		{
			Calendar c = Calendar.getInstance();
			c.setTime(toDate);
			c.add(Calendar.DATE, 1);
			toDate = c.getTime();					
			
			Predicate condition2 = criteriaBuilder.lessThanOrEqualTo(root.<Date>get("cancelledDate"), toDate);
			conditionList.add(condition2);
		}
		
		if (fromDate == null && toDate == null) 
		{
			criteriaQuery.select(root);
			
		} 
		else 
		{
			criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
		}
		final TypedQuery<AutoAllocationCancelRemarks> typedQuery = entityManager.createQuery(criteriaQuery);
		resultList = typedQuery.getResultList();
		int pageNumber = searchDto.getPageable().getPageNumber();
		int firtResult;
		if (pageNumber > 1) 
		{
			firtResult = (pageNumber - 1) * 10;
		} 
		else 
		{
			firtResult = 1;
		} 
		
	/*	if( resultList.size()>10)
		{
			resultList = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
		}
		*/
		
	    List<AutoAllocationCancelRemarks> doList = resultList;
	    
	    AutoAllocationCancelReportMapper instance = AutoAllocationCancelReportMapper.getInstance();
	    
		List<AutoAllocationCancelDetailReportDTO> tableDTO = instance.getcancelRemarksTableObjects(doList);
		for (AutoAllocationCancelDetailReportDTO autoAllocationCancelDetailReportDTO : tableDTO) {
			Intimation intimation = searchbyIntimationKey(autoAllocationCancelDetailReportDTO.getIntimationKey());
			autoAllocationCancelDetailReportDTO.setIntimationNumber(intimation.getIntimationId());
			MasUser masUser = getUserName(autoAllocationCancelDetailReportDTO.getCancelledBy());
			autoAllocationCancelDetailReportDTO.setCancelledBy(autoAllocationCancelDetailReportDTO.getCancelledBy()+"-"+masUser.getUserName());
			
		}
		
		List<AutoAllocationCancelDetailReportDTO> result = new ArrayList<AutoAllocationCancelDetailReportDTO>();
		result.addAll(tableDTO);
		Page<AutoAllocationCancelDetailReportDTO> page = new Page<AutoAllocationCancelDetailReportDTO>();	
		page.setPageNumber(pageNumber);
		page.setPageItems(result);
		page.setIsDbSearch(true);
		
		return page;
	}

	public Intimation searchbyIntimationKey(Long intimationKey) {
		Intimation intimation = null;

		Query findByIntimNo = entityManager.createNamedQuery(
				"Intimation.findByKey").setParameter("intiationKey",
				intimationKey);
		try {
			intimation = (Intimation) findByIntimNo.getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return intimation;
	}
	
	 public MasUser getUserName(String initiatorId)
		{
		  MasUser tmpEmployeeDetails;
			Query findByTransactionKey = entityManager.createNamedQuery(
					"MasUser.getById").setParameter("userId", initiatorId.toLowerCase());
			try{
				tmpEmployeeDetails =(MasUser) findByTransactionKey.getSingleResult();
				return tmpEmployeeDetails;
			}
			catch(Exception e)
			{
				return null;
			}								
		}
	
	@Override
	public Class<AutoAllocationCancelRemarks> getDTOClass() {
		// TODO Auto-generated method stub
		return AutoAllocationCancelRemarks.class;
	}
	
}
