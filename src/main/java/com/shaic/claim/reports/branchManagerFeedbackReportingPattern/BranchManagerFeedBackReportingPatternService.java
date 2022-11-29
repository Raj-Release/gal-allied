package com.shaic.claim.reports.branchManagerFeedbackReportingPattern;

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
import com.shaic.claim.reports.branchManagerFeedBack.BranchManagerFeedBackReportDto;
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
public class BranchManagerFeedBackReportingPatternService {

	private final Logger log = LoggerFactory.getLogger(BranchManagerFeedBackReportingPatternService.class);

	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private DBCalculationService dbService;
	
	public BranchManagerFeedBackReportingPatternDto searchFAapprovedSettlemntPending(BranchManagerFeedBackReportingPatternDto searchDto){
	
		List<BranchManagerFeedBackReportingPatternDto> searchResultList = new ArrayList<BranchManagerFeedBackReportingPatternDto>();
		try{
				Long feedbackTypeId = searchDto.getFeedbackSelect()!= null && searchDto.getFeedbackSelect().getId() != null ? searchDto.getFeedbackSelect().getId() : 0l;
				String period = searchDto.getPeriodSelect() != null ? searchDto.getPeriodSelect().getValue() : "";
				Long zoneCode = searchDto.getZonalOfficeSelect() != null && searchDto.getZonalOfficeSelect().getId() != null ? searchDto.getZonalOfficeSelect().getId() : 0l;
				Long branchCode = searchDto.getBranchOfficeSelect() != null && searchDto.getBranchOfficeSelect().getId() != null ? searchDto.getBranchOfficeSelect().getId() : 0l;
				
				
			    ClaimsReportService.popinReportLog(entityManager, searchDto.getUsername(), "BranchManagerFeedbackReportingPattern", new Date(),new Date(),SHAConstants.RPT_BEGIN);

			    searchResultList = dbService.getBranchManagerFeedbackReportingPattern(feedbackTypeId, period, zoneCode , branchCode);
			    
			    ClaimsReportService.popinReportLog(entityManager, searchDto.getUsername(), "BranchManagerFeedbackReportingPattern", new Date(),new Date(),SHAConstants.RPT_SUCCESS); 
			    
		}
		catch(Exception e){
			e.printStackTrace();
			ClaimsReportService.popinReportLog(entityManager, searchDto.getUsername(), "BranchManagerFeedbackReportingPattern", new Date(),new Date(),SHAConstants.RPT_ERROR);
		}
		
		searchDto.setSearchResultList(searchResultList);
		return searchDto;
	}	
}
