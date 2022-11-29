package com.shaic.claim.reports.opclaimreport;

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
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;

@Stateless
public class OPClaimReportService extends AbstractDAO<Intimation>{
	
	@PersistenceContext
	protected EntityManager entityManager;
	protected double count;
	protected double totalAmt;
	
	public OPClaimReportTableDTO tableDTO;
	
	public OPClaimReportService() {
		super();
	}
	
	public  Page<OPClaimReportTableDTO> search(OPClaimReportFormDTO hospitalFormDTO,String userName, String passWord) {
		
		count = 0l;
		totalAmt = 0l;
		tableDTO = new OPClaimReportTableDTO();
		List<Claim> listIntimations = new ArrayList<Claim>(); 
		try
		{
			Date fromDate = null != hospitalFormDTO.getFromDate() ? hospitalFormDTO.getFromDate() : null;
			Date toDate = null != hospitalFormDTO.getToDate() ? hospitalFormDTO.getToDate() : null;
			
			
			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
			
			Root<Claim> root = criteriaQuery.from(Claim.class);
			
			List<Predicate> conditionList = new ArrayList<Predicate>();	
			
			if(null != fromDate && null != toDate)
			{
				Predicate condition1 = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createdDate"), fromDate);
				conditionList.add(condition1);	
				Calendar c = Calendar.getInstance();
				c.setTime(toDate);
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();
				Predicate condition2 = criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createdDate"), toDate);
				conditionList.add(condition2);
			}
			
			List<Long> myStatusList = new ArrayList<Long>();
			
			myStatusList.add(ReferenceTable.OUT_PATIENT);		
			myStatusList.add(ReferenceTable.HEALTH_CHECK_UP);
						
//			Expression<Long> exp = root.<Status> get("status").<Long> get("key");
			Predicate condition3 = root.<MastersValue> get("claimType").<Long> get("key").in(myStatusList);             //exp.in(myStatusList);
			conditionList.add(condition3);
			
			
			/*if (fromDate == null && toDate == null) 
			{
				criteriaQuery.select(root);
				
			} 
			else 
			{*/
				criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			
			
			final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
			
			SHAUtils.popinReportLog(entityManager, userName, "OPClaimReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
			
			listIntimations = typedQuery.getResultList();
			
		    List<Claim> doList = listIntimations;

		   // List<OPClaimReportTableDTO> tableDTO = new ArrayList<OPClaimReportTableDTO>();
		  
			for (Claim doList1 : doList) {
				
				count++;
				totalAmt+=doList1.getClaimedAmount();
				
			}
			
              tableDTO.setNoOfOpClaims(count);
              tableDTO.setClaimsOpAmount(totalAmt);
              count =0;
              totalAmt =0;
						
		
			List<OPClaimReportTableDTO> result = new ArrayList<OPClaimReportTableDTO>();
			result.add(tableDTO);
			Page<OPClaimReportTableDTO> page = new Page<OPClaimReportTableDTO>();
			page.setHasNext(true);
			if (result.isEmpty()) {
				hospitalFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageItems(result);
			page.setIsDbSearch(true);
			
			SHAUtils.popinReportLog(entityManager, userName, "OPClaimReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
			return page;
		  }
			catch (Exception e) 
		   {
				SHAUtils.popinReportLog(entityManager, userName, "OPClaimReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
		     e.printStackTrace();
		   }
		   return null;	
		}
	
	/*private List<OPClaimReportTableDTO> getHospitalDetails(List<OPClaimReportTableDTO> tableDTO)
	{
		int index = 0;
		
		for(; index < tableDTO.size(); index++){
			
			
						
		}
		
		tableDTO.get(0).setClaimsOpAmount(totalAmt);
		
		
	
		
		return tableDTO;
		}
			*/
			

	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	
	
	
}
