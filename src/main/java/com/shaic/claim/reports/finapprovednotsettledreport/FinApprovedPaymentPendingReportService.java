package com.shaic.claim.reports.finapprovednotsettledreport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.shaic.domain.ClaimsReportService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.menu.RegistrationBean;
import com.shaic.ims.bpm.claim.DBCalculationService;
/**
 * AS Part of CR  R1201
 * @author Lakshminarayana
 *
 */
@Stateless
public class FinApprovedPaymentPendingReportService {

	private final Logger log = LoggerFactory.getLogger(FinApprovedPaymentPendingReportService.class);

	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private DBCalculationService dbService;
	
	public FinApprovedPaymentPendingReportDto searchFAapprovedSettlemntPending(FinApprovedPaymentPendingReportDto searchDto){
	
		List<FinApprovedPaymentPendingReportDto> searchResultList = new ArrayList<FinApprovedPaymentPendingReportDto>();
		try{
			if(null != searchDto.getFrmDate() && null != searchDto.getToDate())
			{
				java.util.Date utilFromDate = searchDto.getFrmDate();
				java.util.Date utilToDate = searchDto.getToDate();
						
			    java.sql.Date sqlFromDate = new java.sql.Date(utilFromDate.getTime()); 
			    java.sql.Date sqlToDate = new java.sql.Date(utilToDate.getTime());
				
			   
			    ClaimsReportService.popinReportLog(entityManager, searchDto.getUsername(), "FAapprovedSettlementPendingReport", new Date(),new Date(),SHAConstants.RPT_BEGIN);

			    searchResultList = dbService.getFAapprovedSettlementPendingReport(sqlFromDate, sqlToDate, searchDto.getCpuSelect()!= null ? searchDto.getCpuSelect().getId() : 0l , searchDto.getUsername());
			    
			    ClaimsReportService.popinReportLog(entityManager, searchDto.getUsername(), "FAapprovedSettlementPendingReport", new Date(),new Date(),SHAConstants.RPT_SUCCESS); 
			}    
		}
		catch(Exception e){
			e.printStackTrace();
			ClaimsReportService.popinReportLog(entityManager, searchDto.getUsername(), "FAapprovedSettlementPendingReport", new Date(),new Date(),SHAConstants.RPT_ERROR);
		}
		
		searchDto.setSearchResultList(searchResultList);
		return searchDto;
	}	
}
