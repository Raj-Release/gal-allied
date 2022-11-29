package com.shaic.claim.reports.tatreport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.ClaimsReportService;
import com.shaic.domain.Intimation;
import com.shaic.ims.bpm.claim.DBCalculationService;

@Stateless
public class TATReportService extends AbstractDAO<Intimation>{

	@PersistenceContext
	protected EntityManager entityManager;
	
	public TATReportService() {
		super();
	}
	
//	public  Page<TATReportTableDTO> search(TATReportFormDTO tatFormDTO,String userName, String passWord,DBCalculationService dbCalculationService,UsertoCPUMappingService userCPUMapService) {
	public  Page<TATReportTableDTO> search(TATReportFormDTO tatFormDTO,String userName, String passWord,DBCalculationService dbCalculationService) {
		
		//final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();			
		//final CriteriaQuery<FieldVisitRequest> criteriaQuery = criteriaBuilder.createQuery(FieldVisitRequest.class);			
		//Root<DocAcknowledgement> root = criteriaQuery.from(DocAcknowledgement.class);			
		
		//List<Predicate> conditionList = new ArrayList<Predicate>();	
		List<TATReportTableDTO> tatFinalList = null;
		
		try
		{
			
			
			Date fromDate = null != tatFormDTO.getFromDate() ? tatFormDTO.getFromDate() : null;
			Date toDate = null != tatFormDTO.getToDate() ? tatFormDTO.getToDate() : null;
			Long cpuCode = null != tatFormDTO.getCpuCode() && null != tatFormDTO.getCpuCode().getValue()  ? Long.valueOf(tatFormDTO.getCpuCode().getValue()) : null;
			Long clmType = null != tatFormDTO.getClaimType() ? tatFormDTO.getClaimType().getId() : null;
			List<Long> cpuCodeList = new ArrayList<Long>();
			if(cpuCode != null){
				cpuCodeList.add(cpuCode);
				tatFormDTO.setCpuCodeList(cpuCodeList);
			}
			Long officeCode  = null != tatFormDTO.getOfficeCode() && null != tatFormDTO.getOfficeCode().getValue() ? Long.valueOf(tatFormDTO.getOfficeCode().getValue()): null;
			String dateType = null != tatFormDTO.getDateType() &&  null != tatFormDTO.getDateType().getValue()? tatFormDTO.getDateType().getValue() : null;
			String tatDate = null != tatFormDTO.getTatDate() &&  null != tatFormDTO.getTatDate().getValue()? tatFormDTO.getTatDate().getValue() : null;
			
			boolean pendingClaims = null != tatFormDTO.getPendingClaims() ? tatFormDTO.getPendingClaims() : false;
			String tatDateValue = null;
			
			if(null != tatDate)
			{
				
				if(tatDate.equals(SHAConstants.BETWEEN_0_TO_8_DAYS))
				{
					tatDateValue = SHAConstants.DAYS_0_8;
				}
				else if(tatDate.equals(SHAConstants.BETWEEN_9_TO_15_DAYS))
				{
					tatDateValue = SHAConstants.DAYS_9_15;
				}
				else if(tatDate.equals(SHAConstants.BETWEEN_16_TO_21_DAYS))
				{
					tatDateValue = SHAConstants.DAYS_16_21;
				}
				else if(tatDate.equals(SHAConstants.BETWEEN_21_TO_30_DAYS))
				{
					tatDateValue = SHAConstants.DAYS_21_30;
				}
				else if(tatDate.equals(SHAConstants.ABOVE_7_DAYS))
				{
					tatDateValue = SHAConstants.DAYS_7;
				}
				else if(tatDate.equals(SHAConstants.ABOVE_20_DAYS))
				{
					tatDateValue = SHAConstants.DAYS_20;
				}
				else if(tatDate.equals(SHAConstants.ABOVE_30__DAYS))
				{
					tatDateValue = SHAConstants.DAYS_30;
				}
				
			}
			
			
			if(null != fromDate && null != toDate)
			{
				java.util.Date utilFromDate = fromDate;
				java.util.Date utilToDate = toDate;
						
			    java.sql.Date sqlFromDate = new java.sql.Date(utilFromDate.getTime()); 
			    java.sql.Date sqlToDate = new java.sql.Date(utilToDate.getTime());
				
			   
			    ClaimsReportService.popinReportLog(entityManager, userName, "TATReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
			    
				if(pendingClaims == true)
				{	
					
			  //  tatFinalList = dbCalculationService.getPendingClaimsTat(sqlFromDate,sqlToDate,cpuCode,officeCode);
				//	tatFinalList = dbCalculationService.getPendingClaimsTat(sqlFromDate,sqlToDate);
					tatFinalList = dbCalculationService.getPendingClaimsTat(sqlFromDate,sqlToDate,cpuCode,officeCode,tatDateValue,userName,clmType);
					
				}
				else
				{
					
					String dateTypeValue = null;					
					
					if(null != dateType && dateType.equals(SHAConstants.APPROVED_DATE_TYPE))
					{
						dateTypeValue = SHAConstants.TYPE_APPROVED;
					}
					else
					{
						dateTypeValue = SHAConstants.TYPE_PAID;
					}
					tatFinalList = dbCalculationService.getCompletedClaimsTat(sqlFromDate,sqlToDate,dateTypeValue,cpuCode,officeCode,tatDateValue,userName,clmType);
					
					
//				tatFinalList = dbCalculationService.getCompletedClaimsTat(sqlFromDate,sqlToDate,dateTypeValue,cpuCode,officeCode,tatDateValue);
//				//tatFinalList = dbCalculationService.getCompletedClaimsTat(sqlFromDate,sqlToDate,typeApproved,typePaid);
				}
			   
				
			}
			
			List<TATReportTableDTO> result = new ArrayList<TATReportTableDTO>();
			
			result.addAll(tatFinalList);
			
			Page<TATReportTableDTO> page = new Page<TATReportTableDTO>();
			//tatFormDTO.getPageable().setPageNumber(pageNumber + 1);
			page.setHasNext(true);
			if (result.isEmpty()) 
			{
				tatFormDTO.getPageable().setPageNumber(1);
			}
			//page.setPageNumber(pageNumber);
			
			page.setPageItems(result);
			page.setIsDbSearch(true);
			ClaimsReportService.popinReportLog(entityManager, userName, "TATReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
			
			return page;
		}
		catch(Exception e)
		{
			ClaimsReportService.popinReportLog(entityManager, userName, "TATReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
			e.printStackTrace();
		}
		return null;	
	}

	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
}
