package com.shaic.claim.reports.branchManagerFeedBack;

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
 * AS Part of CR  R1238
 * @author Lakshminarayana
 *
 */
@Stateless
public class BranchManagerFeedBackReportService {

	private final Logger log = LoggerFactory.getLogger(BranchManagerFeedBackReportService.class);

	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private DBCalculationService dbService;
	
	public BranchManagerFeedBackReportDto searchBranchManagerFeedBackReport(BranchManagerFeedBackReportDto searchDto){
	
		List<BranchManagerFeedBackReportDto> searchResultList = new ArrayList<BranchManagerFeedBackReportDto>();
		try{
				Long feedbackTypeId = searchDto.getFeedbackSelect() != null ? searchDto.getFeedbackSelect().getId() : 0l;
				Long period = searchDto.getPeriodSelect() != null ? searchDto.getPeriodSelect().getId() : 0l;
				Long zoneCode = searchDto.getZonalOfficeSelect() != null ? searchDto.getZonalOfficeSelect().getId() : 0l;
				Long branchCode = searchDto.getBranchOfficeSelect() != null ? searchDto.getBranchOfficeSelect().getId() : 0l;
				
			   
			    ClaimsReportService.popinReportLog(entityManager, searchDto.getUsername(), "BranchManagerFeedbackReport", new Date(),new Date(),SHAConstants.RPT_BEGIN);

			    searchResultList = dbService.getBranchManagerFeedbackReport(feedbackTypeId, period, zoneCode , branchCode , searchDto.getSearchType());
			    
			    ClaimsReportService.popinReportLog(entityManager, searchDto.getUsername(), "BranchManagerFeedbackReport", new Date(),new Date(),SHAConstants.RPT_SUCCESS); 
			    
		}
		catch(Exception e){
			e.printStackTrace();
			ClaimsReportService.popinReportLog(entityManager, searchDto.getUsername(), "BranchManagerFeedbackReport", new Date(),new Date(),SHAConstants.RPT_ERROR);
		}
		
		searchDto.setSearchResultList(searchResultList);
		return searchDto;
	}	
}
