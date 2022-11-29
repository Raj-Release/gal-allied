package com.shaic.claim.icdSublimitMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.shaic.domain.ClaimsReportService;
import com.shaic.domain.IcdSublimitMap;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Sublimit;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.menu.RegistrationBean;
import com.shaic.domain.preauth.IcdCode;
import com.shaic.ims.bpm.claim.DBCalculationService;

@Stateless
public class IcdSubLimitMappingService {

	private final Logger log = LoggerFactory.getLogger(IcdSubLimitMappingService.class);

	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private DBCalculationService dbService;
	
	@EJB
	private PreauthService preauthService;
	
	/*public SearchLumenStatusWiseDto searchbyLumenStatus(SearchLumenStatusWiseDto searchDto){
	
		List<SearchLumenStatusWiseDto> searchResultList = new ArrayList<SearchLumenStatusWiseDto>();
		try{
			if(null != searchDto.getFrmDate() && null != searchDto.getToDate())
			{
				java.util.Date utilFromDate = searchDto.getFrmDate();
				java.util.Date utilToDate = searchDto.getToDate();
						
			    java.sql.Date sqlFromDate = new java.sql.Date(utilFromDate.getTime()); 
			    java.sql.Date sqlToDate = new java.sql.Date(utilToDate.getTime());
				
			   
			    ClaimsReportService.popinReportLog(entityManager, searchDto.getUsername(), "LumenStatusReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
			    searchResultList = dbService.getLumenStatusWiseReport(sqlFromDate, sqlToDate, searchDto.getStatusList(), searchDto.getCpuCodeList(), searchDto.getUsername(), searchDto.getClaimType() != null && searchDto.getClaimType().getId() != null ? searchDto.getClaimType().getId(): 0l);
			    
			    if(searchResultList != null && !searchResultList.isEmpty()){
			    	for (SearchLumenStatusWiseDto searchLumenStatusWiseDto : searchResultList) {
			    		searchLumenStatusWiseDto.setSno(searchResultList.indexOf(searchLumenStatusWiseDto)+1);
					}
			    }
			    ClaimsReportService.popinReportLog(entityManager, searchDto.getUsername(), "LumenStatusReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS); 
			}    
		}
		catch(Exception e){
			e.printStackTrace();
			ClaimsReportService.popinReportLog(entityManager, searchDto.getUsername(), "LumenStatusReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
		}
		
		searchDto.setSearchResultList(searchResultList);
		return searchDto;
	}*/	
	
	public List<IcdSublimitMap> getSublimitMapByName(String sublimitName){
		
		List<IcdSublimitMap> resultList = new ArrayList<IcdSublimitMap>();
		SublimitFunObject sublimitObj = getSublimitDetailsBasedOnName(sublimitName);
		
		if(sublimitObj != null && sublimitObj.getKey() != null){
			resultList = getSublimitMapBySublimitKey(sublimitObj.getKey());
		}
		
		return resultList;
		
	}
	
	public SublimitFunObject getSublimitDetailsBasedOnName(String sublimitName){
		SublimitFunObject resultObj = null;
		
		if(sublimitName != null && !sublimitName.isEmpty()){
			try {

				Query sublimitQuery = entityManager.createNamedQuery("Sublimit.findBySublimitName");
				sublimitQuery.setParameter("sublimitName", "%"+sublimitName.toLowerCase()+"%");
				
				List<Sublimit> resultList = sublimitQuery.getResultList();
				
				if(resultList != null && !resultList.isEmpty()){
					resultObj = new SublimitFunObject();
					resultObj.setName(resultList.get(0).getSublimitDesc());
					resultObj.setKey(resultList.get(0).getKey());
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return resultObj;
	}
	
	public SublimitFunObject getSublimitDetailsBasedOnIcdCode(Long icdCodeKey){
		SublimitFunObject resultObj = null;
		
		if(icdCodeKey != null){
			try {

				Query mapQuery = entityManager.createNamedQuery("IcdSublimitMap.findByIcdCodeKey");
				mapQuery.setParameter("icdCodeKey", icdCodeKey);
				
				List<IcdSublimitMap> resultList = mapQuery.getResultList();
				
				if(resultList != null && !resultList.isEmpty()){
					resultObj = new SublimitFunObject();
					resultObj.setName(resultList.get(0).getSublimit().getSublimitDesc());
//					resultObj.setAmount(resultList.get(0).getSublimit().getSublimitAmount());
					resultObj.setKey(resultList.get(0).getSublimit().getKey());
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}		
		
		return resultObj;
	}
		
	public List<IcdSublimitMap> getSublimitMapByIcdBlockKey(Long icdBlockKey){
		List<IcdSublimitMap> resultList = new ArrayList<IcdSublimitMap>();
		
		if(icdBlockKey != null){
			try {

				Query mapQuery = entityManager.createNamedQuery("IcdSublimitMap.findByIcdBlockKey");
				mapQuery.setParameter("icdBlockKey", icdBlockKey);
				
				resultList = mapQuery.getResultList();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}		
		
		return resultList;
	}
	
	public List<IcdSublimitMap> getSublimitMapByICDChapterKey(Long icdChapKey){
		List<IcdSublimitMap> resultList = new ArrayList<IcdSublimitMap>();
		
		if(icdChapKey != null){
			try {

				Query mapQuery = entityManager.createNamedQuery("IcdSublimitMap.findByIcdChapKey");
				mapQuery.setParameter("icdChapterKey", icdChapKey);
				
				resultList = mapQuery.getResultList();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}		
		
		return resultList;
	}
	public boolean submit(SearchICDSubLimitMappingDto finalDto){
		boolean result = false;
		
		IcdSublimitMap finalObj = null;
		
		if(finalDto.getIcdCodeSelectList() != null && !finalDto.getIcdCodeSelectList().isEmpty()){
			try{

				List<IcdSublimitMappingDto> mapList = finalDto.getIcdCodeSelectList();
				 
				for (IcdSublimitMappingDto mapObj : mapList) {
					
					finalObj = new IcdSublimitMap();
					
					if(mapObj.getIcdSublimitMapKey() == null){
						finalObj.setCreatedBy(finalDto.getUsername());
						finalObj.setCreatedDate(new Date());
					}
					else{
						
						finalObj = getSublimitMapByKey(mapObj.getIcdSublimitMapKey());
						finalObj.setModifiedBy(finalDto.getUsername());
						finalObj.setModifiedDate(new Date());
					}
					
					if(finalObj != null){
					
						finalObj.setSublimit(getSublimitByKey(finalDto.getSublimitKey()));
						finalObj.setIcdBlock(finalDto.getIcdBlockSelect() != null ? preauthService.getIcdBlock(finalDto.getIcdBlockSelect().getId()) : null);
						finalObj.setIcdChapter(finalDto.getIcdChapterSelect() != null ? preauthService.getIcdChapter(finalDto.getIcdChapterSelect().getId()) : null);
						finalObj.setIcdCode(preauthService.getIcdCode(mapObj.getIcdCodeSelect().getId()));
						
						if(mapObj.getIcdSublimitMapKey() == null){	
							finalObj.setActiveStatus(mapObj.isSelected() ? 1 : 0);
							entityManager.persist(finalObj);
						}
						else{
							 switch(finalObj.getActiveStatus().intValue()){
							 case 1:
									 if(!mapObj.isSelected()){
										 finalObj.setActiveStatus(0);
										 entityManager.merge(finalObj);
									 }
								    break;
							 case 0:
									 if(mapObj.isSelected()){
										 finalObj.setActiveStatus(1);
										 entityManager.merge(finalObj);
									 }
								    break;
							 }
						}
					}	
				}
				return true;
			}	
			catch(Exception errObj){
				errObj.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public Sublimit getSublimitByKey(Long sublimitKey){
		List<Sublimit> sublimitList = null;
		if(sublimitKey != null){
			try{
				Query sublimitQry = entityManager.createNamedQuery("Sublimit.findByKey");
				sublimitQry.setParameter("parentKey", sublimitKey);
				sublimitList = sublimitQry.getResultList();
				if(sublimitList != null && !sublimitList.isEmpty()){
					return sublimitList.get(0);
				}
			}
			catch(Exception errObj){
				errObj.printStackTrace();
			}
		}	
		return null;
		
	}
	
	/*public IcdCode getIcdCodeByKey(Long icdCodeKey){
		List<IcdCode> icdCodeList = null;
		if(icdCodeKey != null){
			try{
				Query icdCodeQry = entityManager.createNamedQuery("IcdCode.findByKey");
				icdCodeQry.setParameter("icdCodeKey", icdCodeKey);
				icdCodeList = icdCodeQry.getResultList();
				if(icdCodeList != null && !icdCodeList.isEmpty()){
					return icdCodeList.get(0);
				}
			}
			catch(Exception errObj){
				errObj.printStackTrace();
			}
		}	
		return null;
		
	}*/
	
	public IcdSublimitMap getSublimitMapByKey(Long sublimitIcdMapKey){
		
		List<IcdSublimitMap> resultList = null;
		if(sublimitIcdMapKey != null){
			try{
				Query sublimitIcdMapQry = entityManager.createNamedQuery("IcdSublimitMap.findBySublimitMapKey");
				sublimitIcdMapQry.setParameter("sublimitMapKey", sublimitIcdMapKey);
				resultList = sublimitIcdMapQry.getResultList();
				if(resultList != null && !resultList.isEmpty()){
					return resultList.get(0);
				}
			}
			catch(Exception errObj){
				errObj.printStackTrace();
			}
		}	
		return null;
	}
	public List<IcdSublimitMap> getSublimitMapBySublimitKey(Long sublimitKey){
		
		List<IcdSublimitMap> resultList = new ArrayList<IcdSublimitMap>();
		if(sublimitKey != null){
			try{
				Query sublimitIcdMapQry = entityManager.createNamedQuery("IcdSublimitMap.findBySublimitKey");
				sublimitIcdMapQry.setParameter("sublimitKey", sublimitKey);
				resultList = sublimitIcdMapQry.getResultList();
			}
			catch(Exception errObj){
				errObj.printStackTrace();
			}
		}	
		return resultList;
	}
}
