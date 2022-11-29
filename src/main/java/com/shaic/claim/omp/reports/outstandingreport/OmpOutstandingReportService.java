package com.shaic.claim.omp.reports.outstandingreport;

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

@Stateless
public class OmpOutstandingReportService {

	private final Logger log = LoggerFactory.getLogger(OmpOutstandingReportService.class);

	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private DBCalculationService dbService;
	
	public OmpStatusReportDto searchOmpOutStanding(OmpStatusReportDto searchDto){
	
		List<OmpStatusReportDto> searchResultList = new ArrayList<OmpStatusReportDto>();
		try{
			if(null != searchDto.getFrmDate() && null != searchDto.getToDate())
			{
				java.util.Date utilFromDate = searchDto.getFrmDate();
				java.util.Date utilToDate = searchDto.getToDate();
						
			    java.sql.Date sqlFromDate = new java.sql.Date(utilFromDate.getTime()); 
			    java.sql.Date sqlToDate = new java.sql.Date(utilToDate.getTime());
				
			   
			    ClaimsReportService.popinReportLog(entityManager, searchDto.getUsername(), "OmpOutStandingReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
			    searchResultList = dbService.getOmpReport(sqlFromDate, sqlToDate, searchDto.getClassificationSelect() != null ? searchDto.getClassificationSelect().getId() : null , searchDto.getSubClassificationSelect() != null ? searchDto.getSubClassificationSelect().getId() : null,null,null,null,"o", searchDto.getUsername());
			    
			    if(searchResultList != null && !searchResultList.isEmpty()){
			    	for (OmpStatusReportDto searchLumenStatusWiseDto : searchResultList) {
			    		searchLumenStatusWiseDto.setSno(String.valueOf(searchResultList.indexOf(searchLumenStatusWiseDto)+1));
					}
			    }
			    ClaimsReportService.popinReportLog(entityManager, searchDto.getUsername(), "OmpOutStandingReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS); 
			}    
		}
		catch(Exception e){
			e.printStackTrace();
			ClaimsReportService.popinReportLog(entityManager, searchDto.getUsername(), "OmpOutStandingReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
		}
		
		searchDto.setSearchResultList(searchResultList);
		return searchDto;
	}	
}
