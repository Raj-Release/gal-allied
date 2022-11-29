/**
 * 
 */
package com.shaic.reimbursement.rod.uploadinvestication.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.TmpCPUCode;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.investigation.assigninvestigation.search.SearchAssignInvestigationTableDTO;



/**
 * @author ntv.narenj
 *
 */
@Stateless
public class SearchUploadInvesticationService extends AbstractDAO<Intimation>{
	
	
	
	@PersistenceContext
	protected EntityManager entityManager;
	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	
	public SearchUploadInvesticationService() {
		super();
	}
	/**
	 * 
	 * This search needs to be refractored. Commenting for time being.
	 * 
	 * **/

	public  Page<SearchUploadInvesticationTableDTO> search(
			SearchUploadInvesticationFormDTO searchFormDTO,
			String userName, String passWord) {
		try{
			String intimationNo =  null != searchFormDTO  && null != searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() : null;
			String policyNo =  null != searchFormDTO  && null != searchFormDTO.getPolicyNo() ? searchFormDTO.getPolicyNo() : null;
			
			String priority = null != searchFormDTO  && searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
			String source = null != searchFormDTO  && searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
			String type = null != searchFormDTO  && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
			String claimType = null != searchFormDTO && searchFormDTO.getClaimType() != null ? searchFormDTO.getClaimType().getValue() : null; 
			String priorityNew = searchFormDTO.getPriorityNew() != null ? searchFormDTO.getPriorityNew().getValue() != null ? searchFormDTO.getPriorityNew().getValue() : null : null;
			
			Boolean priorityAll = searchFormDTO.getPriorityAll() != null ? searchFormDTO.getPriorityAll() : null;
			Boolean crm = searchFormDTO.getCrm() != null ? searchFormDTO.getCrm() : null;
			Boolean vip = searchFormDTO.getVip() != null ? searchFormDTO.getVip() : null;
			
			Long investigationAssignedKey = null;
			Long investgationKey = null;
			List<String> intimationNoList = new ArrayList<String>();
			List<String> policyNoList = new ArrayList<String>();
			List<Long> rodKeyList = new ArrayList<Long>();
			//List<Integer> taskNumber = new ArrayList<Integer>();
			List<Map<String, Object>> taskProcedure = null ;
			
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			workFlowMap= new WeakHashMap<Long, Object>();
			
			Integer totalRecords = 0; 
			
			mapValues.put(SHAConstants.USER_ID, userName);
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.UPLOAD_INVESTIGATION_CURRENT_QUEUE);

			
			Long rodKey = 0l;
		
			List<Long> investgationKeyList = new ArrayList<Long>();
			
			List<Long> investgationAssignedKeyList = new ArrayList<Long>();

			
		
			
			if(null != intimationNo && !intimationNo.isEmpty()){
				mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);
				}
			
			//	PolicyType policyType = new PolicyType();
				if(null != policyNo && !policyNo.isEmpty()){
					
					mapValues.put(SHAConstants.POLICY_NUMBER ,policyNo);
				}
				if(null != claimType && !claimType.isEmpty())
				{
					mapValues.put(SHAConstants.CLAIM_TYPE, claimType);

				}
			
			if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
					|| type != null && ! type.isEmpty()){
				
				
				if(priority != null && ! priority.isEmpty())
					if(priority.equalsIgnoreCase(SHAConstants.ALL)){
						priority = null;
					}
					mapValues.put(SHAConstants.PRIORITY, priority);
				if(source != null && ! source.isEmpty()){
					mapValues.put(SHAConstants.STAGE_SOURCE, source);
				}
				
				if(type != null && ! type.isEmpty()){
					if(!type.equalsIgnoreCase(SHAConstants.ALL)){
						type = null;
					}
					mapValues.put(SHAConstants.RECORD_TYPE, type);
				}
			}
			
			/*if(priorityNew != null && ! priorityNew.isEmpty() && !priorityNew.equalsIgnoreCase(SHAConstants.ALL))
				if(priorityNew.equalsIgnoreCase(SHAConstants.NORMAL)){
					mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "N");
				}else{
					mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "Y");
				}*/
			
			if (crm != null && crm.equals(Boolean.TRUE)) {
		    	mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "Y");
		    }
		    
		    if (vip != null && vip.equals(Boolean.TRUE)) {
		    	mapValues.put(SHAConstants.PRIORITY, "VIP");
		    }
			
			Pageable pageable = searchFormDTO.getPageable();
			if(pageable == null){
				pageable = new Pageable();
			}
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 20);
			
			//PagedTaskList taskList;
			
			Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
			
			DBCalculationService dbCalculationService = new DBCalculationService();
			taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);	
			
				if (null != taskProcedure) {
					for (Map<String, Object> outPutArray : taskProcedure) {	
						String intimationNumber = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
						intimationNoList.add(intimationNumber);
						String strPolicyNo = (String) outPutArray.get(SHAConstants.POLICY_NUMBER);
						policyNoList.add(strPolicyNo);
						Long lRodKey = (Long) outPutArray.get(SHAConstants.PAYLOAD_ROD_KEY);
						rodKeyList.add(lRodKey);
						Long InvestKeyValue = (Long)outPutArray.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY);
						Long assignedKeyValue = outPutArray.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY) != null ? Long.valueOf(String.valueOf(outPutArray.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY))) : null;
						
						investgationKeyList.add(InvestKeyValue);
						
						investgationAssignedKeyList.add(assignedKeyValue);
						workFlowMap.put(assignedKeyValue,outPutArray);	
						totalRecords = (Integer) outPutArray
								.get(SHAConstants.TOTAL_RECORDS);

					}
				}

			List<SearchUploadInvesticationTableDTO> tableDTO = new ArrayList<SearchUploadInvesticationTableDTO>();
			for(int index = 0 ; index < intimationNoList.size() ; index++){
				
				
				
				intimationNo = intimationNoList.get(index);
				
				 policyNo = policyNoList.get(index);
				 
				 if(! rodKeyList.isEmpty()){
					 rodKey = rodKeyList.get(index);
				 }
				
				 if((index <investgationAssignedKeyList.size())){
				 investigationAssignedKey = investgationAssignedKeyList.get(index);
				 investgationKey = investgationKeyList.get(index);
				
				 tableDTO.addAll(getIntimationData(intimationNo, policyNo,rodKey,investgationKey,investigationAssignedKey));
				 }
				 
			}

		
			Page<SearchUploadInvesticationTableDTO> page = new Page<SearchUploadInvesticationTableDTO>();
			//page.setPageNumber(taskList.getCurrentPage());
			page.setPageItems(tableDTO);
			page.setTotalRecords(totalRecords);
			return page;
			}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		}
			//return null;	
		
		
		return null;	
	}
	
	private List<SearchUploadInvesticationTableDTO> getIntimationData(String intimationNo, String policyNo ,Long rodKey,
			Long investigationKey,Long investigationAssignedKey){


		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
		
		//List<Intimation> intimationsList = new ArrayList<Intimation>();
		List<Claim> claimList = new ArrayList<Claim>();
		
		//Root<Intimation> root = criteriaQuery.from(Intimation.class);
		Root<Claim> root = criteriaQuery.from(Claim.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		try{
		if(!intimationNo.isEmpty() || intimationNo != null){
			Predicate condition1 = criteriaBuilder.equal(root.<Intimation>get("intimation").<String>get("intimationId"), intimationNo);
			conditionList.add(condition1);
		criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
		final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
		claimList = typedQuery.getResultList();
		}
		
			/*for(Intimation inti:intimationsList){
				System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
			}*/
			//List<Intimation> doList = intimationsList;
		List<Claim> doList = claimList;
			List<SearchUploadInvesticationTableDTO> tableDTO = SearchUploadInvesticationMapper.getInstance().getIntimationDTO(doList);
			
			for (SearchUploadInvesticationTableDTO searchUploadInvesticationTableDTO : tableDTO) {
				
				Claim claimByKey = getClaimByIntimation(searchUploadInvesticationTableDTO.getKey());
				searchUploadInvesticationTableDTO.setCrmFlagged(claimByKey.getCrcFlag());
				/*if(searchUploadInvesticationTableDTO.getCrmFlagged() != null){
					if(searchUploadInvesticationTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
						searchUploadInvesticationTableDTO.setColorCodeCell("OLIVE");
						searchUploadInvesticationTableDTO.setCrmFlagged(null);
						//searchEnterBillDetailTableDTO.setCrmFlagged(FontAwesome.ADJUST.getHtml());
					}
				}*/
				
				if (claimByKey != null) {
					
					if(searchUploadInvesticationTableDTO.getCrmFlagged() != null){
						if(searchUploadInvesticationTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
							if (claimByKey.getCrcFlag() != null && claimByKey.getCrcFlag().equalsIgnoreCase("Y")) {
								searchUploadInvesticationTableDTO.setColorCodeCell("OLIVE");
							}
							searchUploadInvesticationTableDTO.setCrmFlagged(null);
						}
					}
					if (claimByKey.getIsVipCustomer() != null && claimByKey.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
						searchUploadInvesticationTableDTO.setColorCodeCell("VIP");
					}
					if (claimByKey.getCrcFlag() != null && claimByKey.getCrcFlag().equalsIgnoreCase("Y") 
							&& claimByKey.getIsVipCustomer() != null && claimByKey.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
						searchUploadInvesticationTableDTO.setColorCodeCell("CRMVIP");
					}
						
				}
			}
			
			//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
			tableDTO = getclaimNumber(tableDTO);

			tableDTO = getHospitalDetails(tableDTO, investigationKey,rodKey,investigationAssignedKey);

		
		return tableDTO;
		}catch(Exception e){
			return null;
		}
	}
	
	public Claim getClaimByIntimation(Long intimationKey) {
		List<Claim> a_claimList = new ArrayList<Claim>();
		if (intimationKey != null) {

			Query findByIntimationKey = entityManager.createNamedQuery("Claim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter("intimationKey", intimationKey);
			try {

				a_claimList = (List<Claim>) findByIntimationKey.getResultList();
				
				for (Claim claim : a_claimList) {
					entityManager.refresh(claim);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} 
		} else {
			// intimationKey null
		}
		return a_claimList.get(0);

	}

	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	
	private List<SearchUploadInvesticationTableDTO> getclaimNumber(List<SearchUploadInvesticationTableDTO> intimationList){
		Claim a_claim = null;
		for(int index = 0; index < intimationList.size(); index++){
			System.out.println("Intimationkey+++++++++++++++++++++"+intimationList.get(index).getKey());
			
			if (intimationList.get(index).getKey() != null) {

				Query findByIntimationKey = entityManager
						.createNamedQuery("Claim.findByIntimationKey");
				findByIntimationKey = findByIntimationKey.setParameter(
						"intimationKey", intimationList.get(index).getKey());
				try{
						a_claim = (Claim) findByIntimationKey.getSingleResult();
						entityManager.refresh(a_claim);
						if(a_claim != null){
							intimationList.get(index).setClaimNo(a_claim.getClaimId());
							intimationList.get(index).setClaimStatus(a_claim.getStatus().getProcessValue());
						}else{
							intimationList.get(index).setClaimNo("");
						}	
				}catch(Exception e){
					continue;
				}
			} 
		}
		return intimationList;
	}


	private List<SearchUploadInvesticationTableDTO> getHospitalDetails(

			List<SearchUploadInvesticationTableDTO> tableDTO, Long investigationKey,Long rodKey, Long assignedKey) {
		
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
	
			tableDTO.get(index).setInvestigationKey(investigationKey);
			
			tableDTO.get(index).setInvestigationAssignedKey(assignedKey);

			//tableDTO.get(index).setHumanTaskTableDTO(humanTask);

			tableDTO.get(index).setRodKey(rodKey);

			
			Object workflowKey = workFlowMap.get(assignedKey);
//			tableDto.setDbOutArray(workflowKey);
			tableDTO.get(index).setDbOutArray(workflowKey);
			
			
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospitalAddress(hospitalDetail.getAddress());
				 tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId()+" ,"+hospitalDetail.getCity());
				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
				// tableDTO.get(index).setTaskNumber(taskNumber);
				 
				 Long cpuCode = null != getTmpCPUCode(tableDTO.get(index).getCpuId()) ? getTmpCPUCode(tableDTO.get(index).getCpuId()).getCpuCode() : 0l;
				 tableDTO.get(index).setCpuCode(cpuCode);
			 }
			}catch(Exception e){
				continue;
			}
		
		}
		
		
		return tableDTO;
	}
	
	private TmpCPUCode getTmpCPUCode(Long cpuId){
		try{
		Query findCpuCode = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
		 TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
		return tmpCPUCode;
		}catch(Exception e){
			
		}
		return null;
	}
	
	private CityTownVillage getCityTownVillage(Long cpuId){
		
		try{
			Query findCpuCode = entityManager.createNamedQuery("CityTownVillage.findByKey").setParameter("cpuId", cpuId);
			CityTownVillage cityTownVillage = (CityTownVillage) findCpuCode.getSingleResult();
			return cityTownVillage;
			}catch(Exception e){
				
			}
		return null;
		
	}
	
}
