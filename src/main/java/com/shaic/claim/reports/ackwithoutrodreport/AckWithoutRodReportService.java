package com.shaic.claim.reports.ackwithoutrodreport;

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
public class AckWithoutRodReportService {

	private final Logger log = LoggerFactory.getLogger(AckWithoutRodReportService.class);

	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private DBCalculationService dbService;
	
	public List<AckWithoutRodTableDto> searchAckWithoutRod(AckWithoutRodTableDto searchDto){
	
		List<AckWithoutRodTableDto> searchResultList = new ArrayList<AckWithoutRodTableDto>();
		try{
			if(null != searchDto.getFrmDate() && null != searchDto.getToDate())
			{
				java.util.Date utilFromDate = searchDto.getFrmDate();
				java.util.Date utilToDate = searchDto.getToDate();
						
			    java.sql.Date sqlFromDate = new java.sql.Date(utilFromDate.getTime()); 
			    java.sql.Date sqlToDate = new java.sql.Date(utilToDate.getTime());
				
			   
			    ClaimsReportService.popinReportLog(entityManager, searchDto.getUsername(), "AckWithoutRodReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
			    searchResultList = dbService.getAckWithoutRodReport(sqlFromDate, sqlToDate, searchDto.getCpuCodeSelect() != null && searchDto.getCpuCodeSelect().getId() != null ? searchDto.getCpuCodeSelect().getId() : 0l, searchDto.getDocRecvdFrmSelect() != null && searchDto.getDocRecvdFrmSelect().getId() != null ? searchDto.getDocRecvdFrmSelect().getId(): 0l, searchDto.getUsername());
			    
			    if(searchResultList != null && !searchResultList.isEmpty()){
			    	for (AckWithoutRodTableDto searchLumenStatusWiseDto : searchResultList) {
			    		searchLumenStatusWiseDto.setSerialNumber(searchResultList.indexOf(searchLumenStatusWiseDto)+1);
					}
			    }
			    ClaimsReportService.popinReportLog(entityManager, searchDto.getUsername(), "AckWithoutRodReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS); 
			}    
		}
		catch(Exception e){
			e.printStackTrace();
			ClaimsReportService.popinReportLog(entityManager, searchDto.getUsername(), "AckWithoutRodReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
		}
		
		return searchResultList;
	}	
}
