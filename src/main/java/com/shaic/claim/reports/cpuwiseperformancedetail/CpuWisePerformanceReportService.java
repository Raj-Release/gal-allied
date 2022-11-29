package com.shaic.claim.reports.cpuwiseperformancedetail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.UsertoCPUMappingService;

@Stateless
public class CpuWisePerformanceReportService  extends AbstractDAO<Intimation>{
	@PersistenceContext
	protected EntityManager entityManager;
	
	
	protected  Hospitals hospitalType = null;
	
//	@Inject
	CpuWisePerformanceReportTableDTO tableDTO;
	
	public CpuWisePerformanceReportService() {
		super();
	}
	
	public  Page< CpuWisePerformanceReportTableDTO> search( CpuWisePerformanceReportFormDTO hospitalFormDTO,String userName, String passWord,UsertoCPUMappingService usertoCPUMapService) {
		
		

		List<Claim> listIntimations = new ArrayList<Claim>(); 
		try
		{
			Date fromDate = null != hospitalFormDTO.getFromDate() ? hospitalFormDTO.getFromDate() : null;
			Date toDate = null != hospitalFormDTO.getToDate() ? hospitalFormDTO.getToDate() : null;
			
			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
			
			double networkCount = 0l;
			 double nonNetworkCount = 0l;
			 double networkAmount = 0l;
			 double nonnetworkAmount = 0l;
			 double totalCount = 0l;
			 double totalAmount = 0l;
			 tableDTO = new CpuWisePerformanceReportTableDTO();
			
			Root<Claim> root = criteriaQuery.from(Claim.class);
			
			List<Predicate> conditionList = new ArrayList<Predicate>();	
			
			/*if(null != fromDate && null != toDate)
			{
				Predicate condition1 = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createdDate"), fromDate);
				conditionList.add(condition1);	
				Calendar c = Calendar.getInstance();
				c.setTime(toDate);
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();
				Predicate condition2 = criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createdDate"), toDate);
				conditionList.add(condition2);
			}*/
			
			if(null != fromDate)
			{
				Predicate condition6 = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createdDate"), fromDate);
				conditionList.add(condition6);
			}
			if(null != toDate)
			{
				Calendar c = Calendar.getInstance();
				c.setTime(toDate);
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();
				Predicate condition7 = criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createdDate"), toDate);
				conditionList.add(condition7);
			}
			
			
			Predicate condition12 = criteriaBuilder.notEqual(root.<MastersValue>get("claimType"),ReferenceTable.OUT_PATIENT);
			Predicate condition13 = criteriaBuilder.notEqual(root.<MastersValue>get("claimType"),ReferenceTable.HEALTH_CHECK_UP);
			
			Predicate excludeOP = criteriaBuilder.and(condition12,condition13);
			conditionList.add(excludeOP);
			
			List<Long> cpuKeyList = usertoCPUMapService.getCPUCodeList(userName, entityManager);
			Predicate condition14 = root.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key").in(cpuKeyList);
			conditionList.add(condition14);
			
			if (fromDate == null && toDate == null) 
			{
				criteriaQuery.select(root);
				
			} 
			else 
			{
				criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}
			
			final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
			int pageNumber = hospitalFormDTO.getPageable().getPageNumber();
			
			/*int firtResult;
			if (pageNumber > 1) 
			{
				firtResult = (pageNumber - 1) * 10;
			} 
			else 
			{
				firtResult = 1;
			}*/
			
			SHAUtils.popinReportLog(entityManager, userName, "CPUWisePerformanceReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
			listIntimations = typedQuery.getResultList();
			
		    List<Claim> doList = listIntimations;
		   
 		    for (Claim claim : doList) {
 		    				
		    	if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY))
		    	{
		    		networkCount++;
		    		networkAmount+=claim.getProvisionAmount();
		    	}
		    	else
		    	{
		    		nonNetworkCount++;
		    		nonnetworkAmount+=claim.getProvisionAmount();
		    	}
		    	   
			} 		    		    
		    
		    totalCount = networkCount + nonNetworkCount;
		    totalAmount = networkAmount+ nonnetworkAmount;
		    
		    tableDTO.setNonGmcNetWork(networkCount);
		    tableDTO.setNonGmcNonNetWork(nonNetworkCount);
		    tableDTO.setNonGmcProvisionAmount(networkAmount);
		    tableDTO.setNonGmcProvisionAmount1(nonnetworkAmount);
		    tableDTO.setTotal(totalCount);
		    tableDTO.setProvisionAmountTotal(totalAmount);
		   
		    
			List< CpuWisePerformanceReportTableDTO> result = new ArrayList< CpuWisePerformanceReportTableDTO>();
			result.add(tableDTO);
			Page<CpuWisePerformanceReportTableDTO> page = new Page<CpuWisePerformanceReportTableDTO>();
			hospitalFormDTO.getPageable().setPageNumber(pageNumber + 1);
			page.setHasNext(true);
			if (result.isEmpty()) {
				hospitalFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(result);
			page.setIsDbSearch(true);

			SHAUtils.popinReportLog(entityManager, userName, "CPUWisePerformanceReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
			return page;
		  }
			catch (Exception e) 
		   {
				SHAUtils.popinReportLog(entityManager, userName, "CPUWisePerformanceReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
		     e.printStackTrace();
		   }
		   return null;	
		}
	
	
	
		
	
	
	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	

}
