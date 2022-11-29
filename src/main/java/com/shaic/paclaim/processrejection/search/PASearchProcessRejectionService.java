/**
 * 
 */
package com.shaic.paclaim.processrejection.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.processrejection.search.SearchProcessRejectionFormDTO;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.Intimation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;


@Stateless
//public class PASearchProcessRejectionService extends AbstractDAO<Preauth> {
public class PASearchProcessRejectionService extends AbstractDAO<Intimation> {

	public PASearchProcessRejectionService() {
		super();
	}

	@SuppressWarnings("unchecked")
	public Page<SearchProcessRejectionTableDTO> search(
			SearchProcessRejectionFormDTO formDTO, String userName, String passWord) {
		try 
		{
			String strIntimationNo = "";
			
			String strPolicyNo = "";
			
			String priority = formDTO.getPriority() != null ? formDTO.getPriority().getValue() != null ? formDTO.getPriority().getValue() : null : null;
			String source = formDTO.getSource() != null ? formDTO.getSource().getValue() != null ? formDTO.getSource().getValue(): null : null;
			String type = formDTO.getType() != null ? formDTO.getType().getValue() != null ? formDTO.getType().getValue(): null : null;
			
			String accDeath = formDTO.getAccidentDeath() != null?(formDTO.getAccidentDeath().getValue() != null ? ((SHAConstants.ACCIDENT).equalsIgnoreCase(formDTO.getAccidentDeath().getValue()) ?SHAConstants.ACCIDENT_FLAG : SHAConstants.DEATH_FLAG) :null ) : null; 
						
			List<Map<String, Object>> taskParamObjList = null;
			
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			mapValues.put(SHAConstants.CURRENT_Q,SHAConstants.FLP_SUGGEST_REJECTION_CURRENT_QUEUE);
			
			mapValues.put(SHAConstants.USER_ID,userName);
			
			mapValues.put(SHAConstants.LOB, SHAConstants.PA_LOB);
		//	mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.PA_LOB_TYPE);
			
			
			
			if(null != formDTO.getIntimationNo() && !formDTO.getIntimationNo().equalsIgnoreCase(""))
			{
				strIntimationNo = formDTO.getIntimationNo(); 
				mapValues.put(SHAConstants.INTIMATION_NO,strIntimationNo);
			}
			
			/*if(null != accDeath && !accDeath.isEmpty()){
				
				if(payloadBO.getIntimation() == null){
					payloadBO.setIntimation(new IntimationType());	
				}
				payloadBO.getIntimation().setReason(accDeath);
				
			}*/
			
			if(null != formDTO.getPolicyNo() && !formDTO.getPolicyNo().equalsIgnoreCase(""))
			{
				strPolicyNo = formDTO.getPolicyNo();				
				mapValues.put(SHAConstants.POLICY_NUMBER,strPolicyNo);
			}
			
			  if((priority != null && ! priority.isEmpty()) || (source != null && ! source.isEmpty())
						|| (type != null && ! type.isEmpty())){

					
					if(priority != null && ! priority.isEmpty() && !(SHAConstants.ALL).equalsIgnoreCase(priority)){	
						mapValues.put(SHAConstants.PRIORITY,priority);
					}	
					if(source != null && ! source.isEmpty()){				
						mapValues.put(SHAConstants.STAGE_SOURCE,source);
					}					

					if(type != null && ! type.isEmpty() && !(SHAConstants.ALL).equalsIgnoreCase(type)){

						mapValues.put(SHAConstants.RECORD_TYPE, type);

					}


			}			
		
				
			  ImsUser imsUser = formDTO.getImsUser();				
			String[] userRoleList = imsUser.getUserRoleList();
			
			List<SearchProcessRejectionTableDTO> searchProcessRejectionTableDTOForPreAuth = new ArrayList<SearchProcessRejectionTableDTO>();
			List<SearchProcessRejectionTableDTO> searchProcessRejectionTableDTOForIntimation = new ArrayList<SearchProcessRejectionTableDTO>();
			Page<SearchProcessRejectionTableDTO> page = new Page<SearchProcessRejectionTableDTO>();

			Pageable pageable = formDTO.getPageable();
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
			
			
			Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
			
			DBCalculationService dbCalculationService = new DBCalculationService();
			taskParamObjList = dbCalculationService.revisedGetTaskProcedure(setMapValues);
			
			Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
			List<Long> preauthKeys = new ArrayList<Long>();
			List<Long> intimationKeys = new ArrayList<Long>();
			Integer totalRecords = 0;
			if (null != taskParamObjList) {
				for (Map<String, Object> outPutArray : taskParamObjList) {
					Long preauthKeyValue = (Long) outPutArray.get(SHAConstants.CASHLESS_KEY);
					Long inimationKeyValue = (Long) outPutArray.get(SHAConstants.INTIMATION_KEY);
					
					if(preauthKeyValue != null && ! preauthKeyValue.equals(0L)){
						preauthKeys.add(preauthKeyValue);
						if(null != inimationKeyValue){
							intimationKeys.add(inimationKeyValue);
						}					
					}
					else if(null != inimationKeyValue && ! inimationKeyValue.equals(0L)){
							intimationKeys.add(inimationKeyValue);
					}
					
						
						
					workFlowMap.put(inimationKeyValue,outPutArray);
					totalRecords = (Integer) outPutArray
							.get(SHAConstants.TOTAL_RECORDS);
				}
			}
				if(null != preauthKeys && 0!= preauthKeys.size())
				{
					List<Preauth> resultList = new ArrayList<Preauth>();
					resultList = SHAUtils.getIntimationInformation(SHAConstants.PREAUTH_SEARCH_SCREEN, entityManager, preauthKeys);
					List<Preauth> pageItemList = resultList;
					searchProcessRejectionTableDTOForIntimation = PASearchProcessRejectionMapper.getInstance()
						.getSearchProcessRejectionTableDTOForPreauth(pageItemList);
					
					
					
					/*List<Claim> claimedList = new ArrayList<Claim>();
					claimedList = SHAUtils.getIntimationInformation(SHAConstants.SEARCH_CLAIM, entityManager, preauthKeys);
					List<Claim> pageClaimedList = claimedList;
					List<SearchProcessRejectionTableDTO> searchProcessRejectionTableDTOForClaim = PASearchProcessRejectionMapper.getSearchProcessRejectionTableDTOForClaim(pageClaimedList);*/
					for (SearchProcessRejectionTableDTO searchProcessRejectionTableDTO : searchProcessRejectionTableDTOForIntimation) {
						
						searchProcessRejectionTableDTO.setDbOutArray(workFlowMap.get(searchProcessRejectionTableDTO.getKey()));
						if(searchProcessRejectionTableDTO.getIntimationDate() != null){
							String formatDate = SHAUtils.formatDate(searchProcessRejectionTableDTO.getIntimationDate());
							searchProcessRejectionTableDTO.setStrIntimationDate(formatDate);
						}
						String accidentDeath = searchProcessRejectionTableDTO.getAccidentDeath();
						if(accidentDeath!=null){
							if("A".equalsIgnoreCase(accidentDeath)){
								searchProcessRejectionTableDTO.setAccidentDeath("Accident");
						}else{
								searchProcessRejectionTableDTO.setAccidentDeath("Death");
							}
						}
						
						if((searchProcessRejectionTableDTO.getIntimationNo().equalsIgnoreCase(searchProcessRejectionTableDTO.getIntimationNo())))
						{
							searchProcessRejectionTableDTO.setPreauthStatus(searchProcessRejectionTableDTO.getPreauthStatus());
						}
					}
					
				
					//searchProcessRejectionTableDTOForIntimation.addAll(searchProcessRejectionTableDTOForClaim);
					
					
				}
				else if (null != intimationKeys && 0!= intimationKeys.size()) 
			 	{
					List<Intimation> resultList = new ArrayList<Intimation>();
					resultList = SHAUtils.getIntimationInformation(SHAConstants.INTIMATION_SEARCH, entityManager, intimationKeys);
					List<Intimation> pageItemList = resultList;
					searchProcessRejectionTableDTOForIntimation = PASearchProcessRejectionMapper.getInstance()
						.getSearchProcessRejectionTableDTOForIntimation(pageItemList);
								 	
				
					for (SearchProcessRejectionTableDTO objSearchProcessRejectionTableDTO :searchProcessRejectionTableDTOForIntimation)
					{
						
						objSearchProcessRejectionTableDTO.setDbOutArray(workFlowMap.get(objSearchProcessRejectionTableDTO.getKey()));
						if(objSearchProcessRejectionTableDTO.getIntimationDate() != null){
							String formatDate = SHAUtils.formatDate(objSearchProcessRejectionTableDTO.getIntimationDate());
							objSearchProcessRejectionTableDTO.setStrIntimationDate(formatDate);
						}
						
						String accidentDeath = objSearchProcessRejectionTableDTO.getAccidentDeath();
						if(accidentDeath!=null){
							if("A".equalsIgnoreCase(accidentDeath)){
								objSearchProcessRejectionTableDTO.setAccidentDeath(SHAConstants.ACCIDENT);
								objSearchProcessRejectionTableDTO.getProcessRejectionDTO().setAccDeathFlag(true);
							}else{
								objSearchProcessRejectionTableDTO.setAccidentDeath(SHAConstants.DEATH);
								objSearchProcessRejectionTableDTO.getProcessRejectionDTO().setAccDeathFlag(false);
							}
						}
					
						if((objSearchProcessRejectionTableDTO.getIntimationNo().equalsIgnoreCase(objSearchProcessRejectionTableDTO.getIntimationNo())))
						{
							objSearchProcessRejectionTableDTO.setPreauthStatus(objSearchProcessRejectionTableDTO.getPreauthStatus());
						}
					}
				}
		 
			page.setPageItems(searchProcessRejectionTableDTOForIntimation);
			page.setTotalRecords(totalRecords);		
			
			if(formDTO.getPageable() != null){
				page.setPagesAvailable(super.pagedList(formDTO.getPageable()).getPagesAvailable());
				}
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Preauth getProcessRejectionKey(Long processRejectionKey) {
		
		Query findByKey = entityManager.createNamedQuery("Preauth.findAll");

		List<Preauth> processRejectionList = (List<Preauth>) findByKey
				.getResultList();

		if (!processRejectionList.isEmpty()) {
			return processRejectionList.get(0);

		}
		return null;
	}

/*	@Override
	public Class<Preauth> getDTOClass() {
		return Preauth.class;
	}
*/
	
	@Override
	public Class<Intimation> getDTOClass() {
		return Intimation.class;
	}
	
	
	
}
