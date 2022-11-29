/**
 * 
 */
package com.shaic.reimbursement.investigation.draftinvestigation.search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.Investigation;
import com.shaic.domain.MastersValue;
import com.shaic.ims.bpm.claim.DBCalculationService;

/**
 * @author ntv.narenj
 *
 */
@Stateless
public class SearchDraftInvestigationService extends AbstractDAO<Intimation>{

		
		@PersistenceContext
		protected EntityManager entityManager;
		
		Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
		
		public SearchDraftInvestigationService() {
			super();
			
		}
		
		public  Page<SearchDraftInvestigationTableDTO> search(
				SearchDraftInvestigationFormDTO searchFormDTO,
				String userName, String passWord) {
			
			List<Map<String, Object>> taskProcedure = null ;
			//List<Intimation> listIntimations = new ArrayList<Intimation>(); 
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
			
				Long investigationKey = null;
				Long rodKey = null;
				String reimbReq = null;
				String pedInitiatedDate = null;
				//HumanTask humanTaskDTO = null;
				List<String> intimationNoList = new ArrayList<String>();
				/*List<Integer> cashlessTaskNumber = new ArrayList<Integer>();
				List<String> policyNoList = new ArrayList<String>();
				List<String> roleList = new ArrayList<String>();*/
				
			
				List<Long> investigationKeyList = new ArrayList<Long>();
				List<Long>  rodKeyList = new ArrayList<Long>();
				List<String>  reimbReqByList = new ArrayList<String>();
				List<String>  pedApproveDateList = new ArrayList<String>();
				
				//List<Integer> taskNumber = new ArrayList<Integer>();
				
				Map<String, Object> mapValues = new WeakHashMap<String, Object>();
				
				Integer totalRecords = 0; 
				
				mapValues.put(SHAConstants.USER_ID, userName);
				mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.DRAFT_INVESTIGATION_CURRENT_QUEUE);
				
				/*List<HumanTask> humanTaskListDTO = new ArrayList<HumanTask>();
				PayloadBOType payloadBOType = null;
				com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType cashlessPayloadBOType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
				
				IntimationType intimationType = new IntimationType();
				com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType cashlessIntimationType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType();
*/				
				if(null != intimationNo && !intimationNo.isEmpty() ){
					
					/*if(payloadBOType == null){
						payloadBOType = new PayloadBOType();
					}*/
					
					mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);
					
					/*intimationType.setIntimationNumber(intimationNo);
					cashlessIntimationType.setIntimationNumber(intimationNo);
					payloadBOType.setIntimation(intimationType);
					cashlessPayloadBOType.setIntimation(cashlessIntimationType);*/
					}
					/*PolicyType policyType = new PolicyType();
					com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.policy.PolicyType cashlessPolicyType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.policy.PolicyType();
*/					
				
				if(null != policyNo && !policyNo.isEmpty()){
						

						/*if(payloadBOType == null){
							payloadBOType = new PayloadBOType();
						}*/
						
						mapValues.put(SHAConstants.POLICY_NUMBER, policyNo);
						
						/*policyType.setPolicyId(policyNo);
						cashlessPolicyType.setPolicyId(policyNo);
						payloadBOType.setPolicy(policyType);
						cashlessPayloadBOType.setPolicy(cashlessPolicyType);*/
					}
					/*ClaimRequestType claimRequestType = new ClaimRequestType(); 
					ClaimType cashlessClaimType = new ClaimType();*/
					if(null != cpuCode){

					/*if(payloadBOType == null){
						payloadBOType = new PayloadBOType();
					}*/
					
					mapValues.put(SHAConstants.CPU_CODE, cpuCode);
						
					/*claimRequestType.setCpuCode(cpuCode);
					cashlessClaimType.setClaimType(cpuCode);
					payloadBOType.setClaimRequest(claimRequestType);
					cashlessPayloadBOType.setClaim(cashlessClaimType);*/
					}
					
					//com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType claimType1 = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType();
					if(claimType != null && ! claimType.isEmpty()){
						
						/*if(payloadBOType == null){
							payloadBOType = new PayloadBOType();
						}*/
						mapValues.put(SHAConstants.CLAIM_TYPE, claimType);
						
						/*claimType1.setClaimType(claimType);
						payloadBOType.setClaim(claimType1);*/
					}
					
					//ClassificationType classification = null;
					
				    if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
								|| type != null && ! type.isEmpty()){
							//classification = new ClassificationType();
							
							if(priority != null && ! priority.isEmpty())
								if(priority.equalsIgnoreCase(SHAConstants.ALL)){
									priority = null;
								}
							
							mapValues.put(SHAConstants.PRIORITY,priority);
							
							//classification.setPriority(priority);
							if(source != null && ! source.isEmpty()){
								mapValues.put(SHAConstants.STAGE_SOURCE,source);
								//classification.setSource(source);
							}
							
							if(type != null && ! type.isEmpty()){
								if(type.equalsIgnoreCase(SHAConstants.ALL)){
									type = null;
								}
								mapValues.put(SHAConstants.RECORD_TYPE, type);
								//classification.setType(type);
							}
							
							 /*if(payloadBOType == null){
							    	payloadBOType = new PayloadBOType();
							 }
							payloadBOType.setClassification(classification);*/
				}
				    
				    /*if (priorityNew != null && ! priorityNew.isEmpty() && !priorityNew.equalsIgnoreCase(SHAConstants.ALL)) {
				    	if (priorityNew.equalsIgnoreCase(SHAConstants.NORMAL)) {
				    		mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "N");
				    	} else {
				    		mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "Y");
				    	}
				    }*/
				    
				    if (crm != null && crm.equals(Boolean.TRUE)) {
				    	mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "Y");
				    }
				    
				    if (vip != null && vip.equals(Boolean.TRUE)) {
				    	mapValues.put(SHAConstants.PRIORITY, "VIP");
				    }
	
			//CR2019058 change in get task
//			Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
			Object[] setMapValues = SHAUtils.setRevisedObjArrayForAssignInvestigationGetTask(mapValues);

			DBCalculationService dbCalculationService = new DBCalculationService();
//			taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);
			taskProcedure = dbCalculationService.revisedAssignInvestigationGetTaskProcedure(setMapValues);
				    
			
			if (null != taskProcedure) {
				for (Map<String, Object> outPutArray : taskProcedure) {							
						Long keyValue = (Long) outPutArray.get(SHAConstants.PAYLOAD_ROD_KEY);
						String intimationNumber = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
						Long investigationValue = (Long) outPutArray.get(SHAConstants.PAYLOAD_INVESTIGATION_KEY);
						String reimbReqBy = (String)outPutArray.get(SHAConstants.PAYLOAD_REIMB_REQ_BY);
						String pedApproveDate = (String)outPutArray.get(SHAConstants.PAYLOAD_PED_INITIATED_DATE);
						
						workFlowMap.put(investigationValue,outPutArray);
						intimationNoList.add(intimationNumber);
						rodKeyList.add(keyValue);
						investigationKeyList.add(investigationValue);
						reimbReqByList.add(reimbReqBy);
						pedApproveDateList.add(pedApproveDate);
					
					totalRecords = (Integer) outPutArray
							.get(SHAConstants.TOTAL_RECORDS);
				}
			}
				    
			List<SearchDraftInvestigationTableDTO> tableDTO = new ArrayList<SearchDraftInvestigationTableDTO>();
				for(int index = 0 ; index < intimationNoList.size() ; index++){
					if(index < investigationKeyList.size()){
					intimationNo = intimationNoList.get(index);
					 //humanTaskDTO = humanTaskListDTO.get(index);
					 /*String role = null;
					 if(roleList != null && ! roleList.isEmpty()){
						 role = roleList.get(index);
					 }*/
					 investigationKey = investigationKeyList.get(index);
					 rodKey = rodKeyList.get(index);
					 reimbReq = reimbReqByList.get(index);
					 
					 pedInitiatedDate = SHAUtils.getDateFormat(pedApproveDateList.get(index));
					 //Integer taskNo = taskNumber.get(index);
					 tableDTO.addAll(getIntimationData(intimationNo,  investigationKey,rodKey, /*humanTaskDTO,role,*/reimbReq/*,taskNo*/,pedInitiatedDate));
					}
				}
				
		
				Page<SearchDraftInvestigationTableDTO> page = new Page<SearchDraftInvestigationTableDTO>();
				//page.setPageNumber(taskList.getCurrentPage());
				if(tableDTO == null || tableDTO.isEmpty()){
					return null;
				}
				page.setTotalRecords(totalRecords);
				page.setPageItems(tableDTO);
				//page.setTotalRecords(taskList.getTotalRecords() + cashlessTaskList.getTotalRecords());
				return page;
				}
			catch(Exception e){
				e.printStackTrace();
				System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
			}
				return null;	
			
				
		}
		private List<SearchDraftInvestigationTableDTO> getIntimationData(String intimationNo, Long investigationKey,Long rodKey,/* HumanTask humanTask,
				String role,*/String reimbReq/*,Integer taskNumber*/,String pedInitiatedDate){
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
				List<SearchDraftInvestigationTableDTO> tableDTO = SearchDraftInvestigationMapper.getInstance().getIntimationDTO(doList);
				
				for (SearchDraftInvestigationTableDTO searchAssignInvestigationTableDTO : tableDTO) {
					
					Claim claim = getClaimByIntimation(searchAssignInvestigationTableDTO.getKey());
					/*if(claim != null && claim.getCrcFlag()!= null && claim.getCrcFlag().equals("Y")) {
						searchAssignInvestigationTableDTO.setColorCodeCell("OLIVE");
					}*/
					if (claim != null) {
						
						if(searchAssignInvestigationTableDTO.getCrmFlagged() != null){
							if(searchAssignInvestigationTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
								if (claim.getCrcFlag() != null && claim.getCrcFlag().equalsIgnoreCase("Y")) {
									searchAssignInvestigationTableDTO.setColorCodeCell("OLIVE");
								}
								searchAssignInvestigationTableDTO.setCrmFlagged(null);
							}
						}
						if (claim.getIsVipCustomer() != null && claim.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
							searchAssignInvestigationTableDTO.setColorCodeCell("VIP");
						}
						if (claim.getCrcFlag() != null && claim.getCrcFlag().equalsIgnoreCase("Y") 
								&& claim.getIsVipCustomer() != null && claim.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
							searchAssignInvestigationTableDTO.setColorCodeCell("CRMVIP");
						}
							
					}

					if(searchAssignInvestigationTableDTO.getCpuCode() != null){
		        		
						Long cpuCode = searchAssignInvestigationTableDTO.getCpuCode();
						searchAssignInvestigationTableDTO.setStrCpuCode(String.valueOf(cpuCode));
						
						searchAssignInvestigationTableDTO.setReimbReqBy(reimbReq);
						
						searchAssignInvestigationTableDTO.setInvestigationApprovedDate(pedInitiatedDate);
					}
				}
				//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
				
				tableDTO = getHospitalDetails(tableDTO, investigationKey,rodKey, /*humanTask,role,*/reimbReq/*,taskNumber*/);
			
			
			return tableDTO;
			}catch(Exception e){
				return null;
			}
		}

		@Override
		public Class<Intimation> getDTOClass() {
			// TODO Auto-generated method stub
			return Intimation.class;
		}
		

		private List<SearchDraftInvestigationTableDTO> getHospitalDetails(
				List<SearchDraftInvestigationTableDTO> tableDTO, Long investigationKey,Long rodKey,/* HumanTask humanTask,
				String role,*/String reimbReq/*,Integer taskNumber*/) {
			Hospitals hospitalDetail;
			Investigation investigation;
			for(int index = 0; index < tableDTO.size(); index++){
				//tableDTO.get(index).setHumanTaskDTO(humanTask);
				 tableDTO.get(index).setInvestigationKey(investigationKey);
				 tableDTO.get(index).setIsCashlessTask(reimbReq != null ? false : true);
				 tableDTO.get(index).setRodKey(rodKey);
				 
				 tableDTO.get(index).setDbOutArray(workFlowMap.get(investigationKey));
				 
				Query findByHospitalKey = entityManager.createNamedQuery(
						"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
				try{
				 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
				 if(hospitalDetail != null){
					 
					 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
					 tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId()+" ,"+hospitalDetail.getCity());
					 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
					 //tableDTO.get(index).setTaskNumber(taskNumber);
//					 Long cpuCode = null != getTmpCPUCode(tableDTO.get(index).getCpuId()) ? getTmpCPUCode(tableDTO.get(index).getCpuId()).getCpuCode() : 0l;
//					 tableDTO.get(index).setCpuCode(cpuCode);
					 tableDTO.get(index).setlOB(getLOBValue(tableDTO.get(index).getlOBId()).getValue());
					/* if(invstDtls != null){
						 tableDTO.get(index).setInvestigationApprovedDate(invstDtls.getApprovedDate());
					 }*/
					 //tableDTO.get(index).setInvestigationRole(role);
				 }
				}catch(Exception e){
					continue;
				}
			
			}
			
			
			return tableDTO;
		}
		
		

		/*private TmpCPUCode getTmpCPUCode(Long cpuId){
			try{
			Query findCpuCode = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
			 TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
			return tmpCPUCode;
			}catch(Exception e){
				
			}
			return null;
		}*/
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
		
		public Claim getClaimByIntimation(Long intimationKey) {
			if (intimationKey != null) {
				Query findByIntimationKey = entityManager.createNamedQuery("Claim.findByIntimationKey");
				findByIntimationKey = findByIntimationKey.setParameter("intimationKey", intimationKey);
				List<Claim> a_claimList = null;
				try {
					a_claimList = (List<Claim>) findByIntimationKey.getResultList();
					for (Claim claim : a_claimList) {
						entityManager.refresh(claim);
					}
					if(a_claimList.size() > 0) {
						return 	a_claimList.get(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
			return null;
		}
		
		private Investigation getApprovedDate(Long intimationKey){
			try{
				Query query = entityManager.createNamedQuery("Investigation.findByInvestigationKey");
				query = query.setParameter("investigationKey", intimationKey);
				Investigation value = (Investigation) query.getSingleResult();
				  return value;
			}catch(Exception e){
				
			}
			return null;
		
			
		}
		
	}