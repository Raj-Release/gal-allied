/**
 * 
 */
package com.shaic.claim.OMPprocessrejection.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.omp.carousel.OmpRevisedIntimationMapper;
import com.shaic.claim.processrejection.search.SearchProcessRejectionFormDTO;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.domain.OMPClaim;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.OMPClaimMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;


@Stateless
public class SearchOMPProcessRejectionService extends AbstractDAO<OMPClaim> {

	public SearchOMPProcessRejectionService() {
		super();
	}

	@SuppressWarnings("unchecked")
	public Page<SearchProcessRejectionTableDTO> search(
			SearchProcessRejectionFormDTO formDTO, String userName, String passWord) {
		try 
		{
			String strIntimationNo = "";
			
			String strPolicyNo = "";
			
			List<Map<String, Object>> taskParamObjList = null;
			
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			mapValues.put(SHAConstants.CURRENT_Q,SHAConstants.OMP_SUGGEST_REJECTION_CURRENT_QUEUE);
			
			mapValues.put(SHAConstants.USER_ID,userName);
						
			if(null != formDTO.getIntimationNo() && !formDTO.getIntimationNo().equalsIgnoreCase(""))
			{
				strIntimationNo = formDTO.getIntimationNo(); 
				mapValues.put(SHAConstants.INTIMATION_NO,strIntimationNo);
			}
			if(null != formDTO.getPolicyNo() && !formDTO.getPolicyNo().equalsIgnoreCase(""))
			{
				strPolicyNo = formDTO.getPolicyNo();
				mapValues.put(SHAConstants.POLICY_NUMBER,strPolicyNo);
			}
			
			List<SearchProcessRejectionTableDTO> searchProcessRejectionTableDTOForClaim = new ArrayList<SearchProcessRejectionTableDTO>();
			Page<SearchProcessRejectionTableDTO> page = new Page<SearchProcessRejectionTableDTO>();

			Pageable pageable = formDTO.getPageable();
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
			
				
			Object[] setMapValues = SHAUtils.setOMPObjArrayForGetTask(mapValues);
			
			DBCalculationService dbCalculationService = new DBCalculationService();
			taskParamObjList = dbCalculationService.getOMPTaskProcedure(setMapValues);	
			
			Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
			List<Long> claimKeys = new ArrayList<Long>();
			Integer totalRecords = 0;
			if (null != taskParamObjList) {
				for (Map<String, Object> outPutArray : taskParamObjList) {
					Long claimKeyValue = (Long) outPutArray.get(SHAConstants.DB_CLAIM_KEY);
						if(null != claimKeyValue){
							claimKeys.add(claimKeyValue);
						}
						
					workFlowMap.put(claimKeyValue,outPutArray);
					totalRecords = (Integer) outPutArray
							.get(SHAConstants.TOTAL_RECORDS);
				}
			}
			
				//intimationKeys.
				 if (null != claimKeys && 0!= claimKeys.size()) 
			 	{
					List<OMPClaim> resultList = new ArrayList<OMPClaim>();
					resultList = getOMPClaimInformation(claimKeys);
					List<OMPClaim> pageItemList = resultList;
					searchProcessRejectionTableDTOForClaim = SearchOMPProcessRejectionMapper.getInstance().getSearchProcessRejectionTableDTOForClaim(pageItemList);
					
					if(resultList != null && !resultList.isEmpty()){
						OMPClaimMapper ompclmMapper = OMPClaimMapper.getInstance();
						WeakHashMap<Long, ClaimDto> clmDtoMap = new WeakHashMap<Long, ClaimDto>();
						ClaimDto clmDto = null;
						OmpRevisedIntimationMapper ompIntimMapper = OmpRevisedIntimationMapper.getInstance();  
						NewIntimationDto intimationDto = null;
						for (OMPClaim ompClaim : resultList) {
							clmDto =  ompclmMapper.getClaimDto(ompClaim);
							clmDto.setNewIntimationDto(ompIntimMapper.getNewIntimationDto(ompClaim.getIntimation()));
							clmDtoMap.put(clmDto.getKey(),clmDto);
						}						
						for (SearchProcessRejectionTableDTO objSearchProcessRejectionTableDTO : searchProcessRejectionTableDTOForClaim) {
							objSearchProcessRejectionTableDTO.setDbOutArray(workFlowMap
											.get(objSearchProcessRejectionTableDTO.getKey()));
							objSearchProcessRejectionTableDTO.setUsername(userName);
							objSearchProcessRejectionTableDTO.setClaimDto(clmDtoMap
											.get(objSearchProcessRejectionTableDTO.getKey()));
						}
					}
			 	}	
			page.setPageItems(searchProcessRejectionTableDTOForClaim);
			page.setTotalRecords(totalRecords.intValue());			
			
			if(formDTO.getPageable() != null){
			page.setPagesAvailable(super.pagedList(formDTO.getPageable()).getPagesAvailable());
			}
			
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Class<OMPClaim> getDTOClass() {
		return OMPClaim.class;
	}
	
	   public List<OMPClaim> getOMPClaimInformation(List<Long> claimKeys){
		   
		   final CriteriaBuilder OMPClaimBuilder = entityManager
					.getCriteriaBuilder();
		   final CriteriaQuery<OMPClaim> criteriaQuery = OMPClaimBuilder
					.createQuery(OMPClaim.class);
		   Root<OMPClaim> ompClaimRoot = criteriaQuery
					.from(OMPClaim.class);

			criteriaQuery.where(ompClaimRoot.<Long> get(
					"key").in(claimKeys));
			
			final TypedQuery<OMPClaim> OMPClaimQuery = entityManager
					.createQuery(criteriaQuery);
			
			List<OMPClaim> finalResultList = OMPClaimQuery.getResultList();
			
			return finalResultList;
	   }
	
}
