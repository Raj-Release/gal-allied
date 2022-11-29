package com.shaic.claim.reports.marketingEscalationReport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.reports.opinionvalidationreport.OpinionValidationReportFormDTO;
import com.shaic.claim.reports.opinionvalidationreport.OpinionValidationReportTableDTO;
import com.shaic.domain.ClaimsReportService;
import com.shaic.domain.Intimation;
import com.shaic.domain.TmpEmployee;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.VaadinSession;

public class MarketingEscalationReportService {

	@PersistenceContext
	protected EntityManager entityManager;
	protected Date fromDate;
	protected Date toDate;
	
	
	@Resource
	private UserTransaction utx;
	
	public MarketingEscalationReportService() {
		super();
	}
	
	public  Page<MarketingEscalationReportTableDTO> search(MarketingEscalationReportFormDTO searchFormDTO,String userName, String passWord, DBCalculationService dbService) {
		

		try
		{

			VaadinSession.getCurrent().getSession().setMaxInactiveInterval(3600);
			utx.setTransactionTimeout(3600);	
			utx.begin();
			
			 fromDate = null != searchFormDTO.getFromDate() ? searchFormDTO.getFromDate() : null;
		     toDate = null != searchFormDTO.getToDate() ? searchFormDTO.getToDate() : null;
			 SelectValue selProduct = null != searchFormDTO.getProductNameCode() ? searchFormDTO.getProductNameCode() : null;
		     String productCodeName = null;

		     if(null != selProduct && null != selProduct.getValue()){
				   String productCode[] = selProduct.getValue().split("/");
				   String strcpuCode = productCode[1];
				   if(null != strcpuCode){
					   productCodeName = strcpuCode.trim();
				 }
			 }
			

						
			int pageNumber = searchFormDTO.getPageable().getPageNumber();
			
			ClaimsReportService.popinReportLog(entityManager, userName, "MarketingEscalationReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
			List<MarketingEscalationReportTableDTO> resultList = dbService.getMarketingEscalationReportList(new java.sql.Date(fromDate.getTime()), new java.sql.Date(toDate.getTime()),productCodeName);
			
			Page<MarketingEscalationReportTableDTO> page = new Page<MarketingEscalationReportTableDTO>();
			searchFormDTO.getPageable().setPageNumber(pageNumber + 1);
			page.setHasNext(true);
			if (resultList.isEmpty()) {
				searchFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(resultList);
			page.setIsDbSearch(false);
			page.setTotalRecords(resultList.size());
			ClaimsReportService.popinReportLog(entityManager, userName, "MarketingEscalationReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
			
			utx.commit();
			return page;
		  
		}
			catch (Exception e) 
		   {
				ClaimsReportService.popinReportLog(entityManager, userName, "OpinionValidationReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
				try {
					utx.rollback();
				} catch (IllegalStateException | SecurityException
						| SystemException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		     e.printStackTrace();
		   }
		   return null;	
		}
	

	



}
