/**
 * 
 */
package com.shaic.reimbursement.investigation.reassigninvestigation.search;

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
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.TataPolicy;
import com.shaic.domain.TmpCPUCode;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.investigation.assigninvestigation.search.SearchAssignInvestigationFormDTO;
import com.shaic.reimbursement.investigation.assigninvestigation.search.SearchAssignInvestigationMapper;
import com.shaic.reimbursement.investigation.assigninvestigation.search.SearchAssignInvestigationTableDTO;
import com.shaic.reimbursement.rod.uploadinvestication.search.SearchUploadInvesticationFormDTO;
import com.shaic.reimbursement.rod.uploadinvestication.search.SearchUploadInvesticationMapper;
import com.shaic.reimbursement.rod.uploadinvestication.search.SearchUploadInvesticationTableDTO;

/**
 * 
 *
 */
@Stateless
public class SearchReAssignInvestigationService extends AbstractDAO<Intimation>{

		
		@PersistenceContext
		protected EntityManager entityManager;
		
		Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
		
		
		public SearchReAssignInvestigationService() {
			super();
			
		}
		/***
		 * This menu search needs to be migrated to DB.
		 * As of now, to avoid bpmn references, commenting 
		 * below search. Who ever takes up this migration for
		 * this menu, please uncomment the below method and
		 * migrate the same. 
		 * 
		 */
		public  Page<SearchAssignInvestigationTableDTO> search(
				SearchAssignInvestigationFormDTO searchFormDTO,
				String userName, String passWord) {
			
			List<Intimation> listIntimations = new ArrayList<Intimation>(); 
			try{
				String intimationNo = null != searchFormDTO && null != searchFormDTO.getIntimationNo() ?  searchFormDTO.getIntimationNo() : null ;
				String 	policyNo = null != searchFormDTO && null != searchFormDTO.getPolicyNo() ? searchFormDTO.getPolicyNo() : null;
				String cpuCode = null != searchFormDTO && null != searchFormDTO.getCpuCode() ? searchFormDTO.getCpuCode().getId().toString() : null;
				
				String priority = null != searchFormDTO && searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
				String source = null != searchFormDTO && searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
				String type = null != searchFormDTO && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
				String claimType = null != searchFormDTO && searchFormDTO.getClaimType() != null ? searchFormDTO.getClaimType().getValue() != null ? searchFormDTO.getClaimType().getValue(): null : null;
				String priorityNew = searchFormDTO.getPriorityNew() != null ? searchFormDTO.getPriorityNew().getValue() != null ? searchFormDTO.getPriorityNew().getValue() : null : null;
			
				Boolean priorityAll = searchFormDTO.getPriorityAll() != null ? searchFormDTO.getPriorityAll() : null;
				Boolean crm = searchFormDTO.getCrm() != null ? searchFormDTO.getCrm() : null;
				Boolean vip = searchFormDTO.getVip() != null ? searchFormDTO.getVip() : null;
				
				Map<String, Object> mapValues = new WeakHashMap<String, Object>();
				workFlowMap= new WeakHashMap<Long, Object>();
				List<Map<String, Object>> taskProcedure = null ;
				
				Integer totalRecords = 0; 
				
				mapValues.put(SHAConstants.USER_ID, userName);
				mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.UPLOAD_INVESTIGATION_CURRENT_QUEUE);
				if(null != intimationNo && !intimationNo.isEmpty())
					mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);
				if(null != policyNo && !policyNo.isEmpty())
					mapValues.put(SHAConstants.POLICY_NUMBER, policyNo);
				if(null != cpuCode && !cpuCode.isEmpty())
					mapValues.put(SHAConstants.CPU_CODE, cpuCode);
				if(null != source && !source.isEmpty())
					mapValues.put(SHAConstants.STAGE_SOURCE, source);
				if(null != type && !type.isEmpty())
					mapValues.put(SHAConstants.RECORD_TYPE, type);
				if(null != claimType && !claimType.isEmpty())
					mapValues.put(SHAConstants.CLAIM_TYPE, claimType);
				
				
				Long investigationKey = null;
				Long rodKey = null;
				Long investigationAssignedKey = null;
				List<String> intimationNoList = new ArrayList<String>();
				List<Integer> cashlessTaskNumber = new ArrayList<Integer>();
				List<String> policyNoList = new ArrayList<String>();
				
			
				List<Long> investigationKeyList = new ArrayList<Long>();
				List<Long> investgationAssignedKeyList = new ArrayList<Long>();
				List<Long> rodKeyList = new ArrayList<Long>();
				List<Integer> taskNumber = new ArrayList<Integer>();
				
	
				    if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
								|| type != null && ! type.isEmpty()){
							
							
							if(priority != null && ! priority.isEmpty())
								if(priority.equalsIgnoreCase(SHAConstants.ALL)){
									priority = null;
									
								}
							mapValues.put(SHAConstants.PRIORITY, priority);
							if(type != null && ! type.isEmpty()){
								if(type.equalsIgnoreCase(SHAConstants.ALL)){
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
	
				    Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
					
					DBCalculationService dbCalculationService = new DBCalculationService();
					taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);	
					
				Pageable pageable = searchFormDTO.getPageable();
				pageable.setPageNumber(1);
				pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
			
				
				if (null != taskProcedure) {
					for (Map<String, Object> outPutArray : taskProcedure) {
						
						
						String intimationNumber = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
						intimationNoList.add(intimationNumber);
						String strPolicyNo = (String) outPutArray.get(SHAConstants.POLICY_NUMBER);
						Boolean isTataPolicy = getTataPolicy(strPolicyNo);
						if(!isTataPolicy){
							policyNoList.add(strPolicyNo);
							Long reimbKey = (Long) outPutArray.get(SHAConstants.PAYLOAD_ROD_KEY);
							rodKeyList.add(reimbKey);
							Long InvestKeyValue = (Long)outPutArray.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY);
							Long assignedKeyValue = outPutArray.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY) != null ? Long.valueOf(String.valueOf(outPutArray.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY))) : null;
							
							investigationKeyList.add(InvestKeyValue);
							
							investgationAssignedKeyList.add(assignedKeyValue);
							
							workFlowMap.put(assignedKeyValue,outPutArray);
							totalRecords = (Integer) outPutArray
									.get(SHAConstants.TOTAL_RECORDS);
						}

					}
				}

				
				List<SearchAssignInvestigationTableDTO> tableDTO = new ArrayList<SearchAssignInvestigationTableDTO>();
				for(int index = 0 ; index < intimationNoList.size() ; index++){
					if(index < investgationAssignedKeyList.size()){
						intimationNo = intimationNoList.get(index);
	
						investigationKey = investigationKeyList.get(index);
						 if(! rodKeyList.isEmpty() ){
						 rodKey = rodKeyList.get(index);
						 //Integer taskNo = taskNumber.get(index);
						 investigationAssignedKey = investgationAssignedKeyList.get(index);
						 tableDTO.addAll(getIntimationData(intimationNo, investigationKey,rodKey,investigationAssignedKey));
						}
					}
				}
				
			
				Page<SearchAssignInvestigationTableDTO> page = new Page<SearchAssignInvestigationTableDTO>();
				page.setPageItems(tableDTO);
				page.setTotalRecords(totalRecords);
				return page;
				}
			catch(Exception e){
				e.printStackTrace();
				System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
			}
				return null;	
			
				
		}
		private List<SearchAssignInvestigationTableDTO> getIntimationData(String intimationNo, Long investigationKey, Long rodKey, Long investigationAssignKey){
			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<Intimation> criteriaQuery = criteriaBuilder.createQuery(Intimation.class);
			
			List<Intimation> intimationsList = new ArrayList<Intimation>(); 
			
			Root<Intimation> root = criteriaQuery.from(Intimation.class);
			
			List<Predicate> conditionList = new ArrayList<Predicate>();
			try{
			if(intimationNo != null || !intimationNo.isEmpty()){
				Predicate condition1 = criteriaBuilder.equal(root.<String>get("intimationId"), intimationNo);
				conditionList.add(condition1);
				criteriaQuery.select(root).where(
						criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}
			final TypedQuery<Intimation> typedQuery = entityManager.createQuery(criteriaQuery);
			intimationsList = typedQuery.getResultList();
			
				for(Intimation inti:intimationsList){
					System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
				}
				List<Intimation> doList = intimationsList;
				List<SearchAssignInvestigationTableDTO> tableDTO = SearchAssignInvestigationMapper.getInstance().getIntimationDTO(doList);
				
				for (SearchAssignInvestigationTableDTO searchAssignInvestigationTableDTO : tableDTO) {

					if(searchAssignInvestigationTableDTO.getCpuCode() != null){
		        		
						Long cpuCode = searchAssignInvestigationTableDTO.getCpuCode();
						searchAssignInvestigationTableDTO.setStrCpuCode(String.valueOf(cpuCode));
					}
					
					Claim claimByKey = getClaimByIntimation(searchAssignInvestigationTableDTO.getKey());
					searchAssignInvestigationTableDTO.setCrmFlagged(claimByKey.getCrcFlag());
					/*if(searchAssignInvestigationTableDTO.getCrmFlagged() != null){
						if(searchAssignInvestigationTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
							searchAssignInvestigationTableDTO.setColorCodeCell("OLIVE");
							searchAssignInvestigationTableDTO.setCrmFlagged(null);
							//searchEnterBillDetailTableDTO.setCrmFlagged(FontAwesome.ADJUST.getHtml());
						}
					}*/
					if (claimByKey != null) {
						
						if(searchAssignInvestigationTableDTO.getCrmFlagged() != null){
							if(searchAssignInvestigationTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
								if (claimByKey.getCrcFlag() != null && claimByKey.getCrcFlag().equalsIgnoreCase("Y")) {
									searchAssignInvestigationTableDTO.setColorCodeCell("OLIVE");
								}
								searchAssignInvestigationTableDTO.setCrmFlagged(null);
							}
						}
						if (claimByKey.getIsVipCustomer() != null && claimByKey.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
							searchAssignInvestigationTableDTO.setColorCodeCell("VIP");
						}
						if (claimByKey.getCrcFlag() != null && claimByKey.getCrcFlag().equalsIgnoreCase("Y") 
								&& claimByKey.getIsVipCustomer() != null && claimByKey.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
							searchAssignInvestigationTableDTO.setColorCodeCell("CRMVIP");
						}
							
					}
				}
				//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
				
				tableDTO = getHospitalDetails(tableDTO, investigationKey,rodKey,investigationAssignKey);
			
			
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
		


		private List<SearchAssignInvestigationTableDTO> getHospitalDetails(
				List<SearchAssignInvestigationTableDTO> tableDTO, Long investigationKey,Long rodKey,
				Long investigationAssignKey) {


			Hospitals hospitalDetail;
			for(int index = 0; index < tableDTO.size(); index++){
				//tableDTO.get(index).setHumanTaskDTO(humanTask);
				 tableDTO.get(index).setInvestigationKey(investigationKey);
				 tableDTO.get(index).setInvestigationAssignedKey(investigationAssignKey);
				 tableDTO.get(index).setRodKey(rodKey);
				Object workflowKey = workFlowMap.get(investigationAssignKey);
//					tableDto.setDbOutArray(workflowKey);
				tableDTO.get(index).setDbOutArray(workflowKey);

				Query findByHospitalKey = entityManager.createNamedQuery(
						"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
				try{
				 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
				 if(hospitalDetail != null){
					 
					 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
					 tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId()+" ,"+hospitalDetail.getCity());
					 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
					// tableDTO.get(index).setTaskNumber(taskNumber);
//					 Long cpuCode = null != getTmpCPUCode(tableDTO.get(index).getCpuId()) ? getTmpCPUCode(tableDTO.get(index).getCpuId()).getCpuCode() : 0l;
//					 tableDTO.get(index).setCpuCode(cpuCode);
					 tableDTO.get(index).setlOB(getLOBValue(tableDTO.get(index).getlOBId()).getValue());
					 
					 AssignedInvestigatiorDetails assignedObj = entityManager.find(AssignedInvestigatiorDetails.class, investigationAssignKey);
					 
					 if(assignedObj != null){
						 
						 tableDTO.get(index).setInvestigatorName((assignedObj.getInvestigatorCode() != null && !assignedObj.getInvestigatorCode().isEmpty() ? (assignedObj.getInvestigatorCode() +" - ") : "") + (assignedObj.getInvestigatorName() != null ? assignedObj.getInvestigatorName() : ""));
					 }
					 
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
		private MastersValue getLOBValue(Long getlOBId) {
			try{
			Query query = entityManager
					.createNamedQuery("MastersValue.findByKey");
			query = query.setParameter("parentKey", getlOBId);
			MastersValue value = (MastersValue) query.getSingleResult();
		    return value;
			}catch(Exception e){
				
			}
			return null;
		}
		
		/*private Long getInvestigationKey(Long rodKey){
			try{
				Query findClaimKey = entityManager.createNamedQuery("Reimbursement.findByKey").setParameter("primaryKey", rodKey);
				List<Reimbursement> reimbursementList = findClaimKey.getResultList();
				return getInvestigationByClaimKey(reimbursementList.get(0).getClaim().getKey());
				}catch(Exception e){
					return null;
				}
		}
		
		private Long getInvestigationByClaimKey(Long claimKey){
			
			try{
				Query findinvestigationKey = entityManager.createNamedQuery("Investigation.findByClaimKey").setParameter("claimKey", claimKey);
				List<Investigation> InvestigationList =  findinvestigationKey.getResultList();
				return InvestigationList.get(0).getKey();
				}catch(Exception e){
					return null;
				}
			
		}*/	
		
		public Boolean getTataPolicy(String policyNumber){
			 Query query = entityManager.createNamedQuery("TataPolicy.findByPolicyNumber");
			 query.setParameter("policyNumber", policyNumber);
			    List<TataPolicy> tataPolicy = (List<TataPolicy>)query.getResultList();
			    if(tataPolicy != null && ! tataPolicy.isEmpty()){
			    	return true;
			    }
			    return false;
		}
		
	}