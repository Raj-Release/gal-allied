/**
 * 
 */
package com.shaic.claim.reimbursement.commonBillingFA;

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
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.IncurredClaimRatio;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpCPUCode;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsFormDTO;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsMapper;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsTableDTO;
/**
 * @author ntv.narenj
 *
 */
@Stateless
public class SearchProcessClaimCommonBillingAndFinancialsService extends AbstractDAO<Intimation>{

	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public String userName;
	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	
	public SearchProcessClaimCommonBillingAndFinancialsService() {
		super();
		
	}
	
	public  Page<SearchProcessClaimFinancialsTableDTO> search(
			SearchProcessClaimFinancialsFormDTO searchFormDTO,
			String userName, String passWord) {
		
			try{
				String intimationNo = null != searchFormDTO && null != searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() : null;
				/*String policyNo = null != searchFormDTO && null != searchFormDTO.getPolicyNo() ? searchFormDTO.getPolicyNo() : null ;
				String claimTypeValue = null != searchFormDTO && null != searchFormDTO.getClaimType() ? searchFormDTO.getClaimType().getValue() : null;
				String cpuCode = null != searchFormDTO && null != searchFormDTO.getCpuCode() ? searchFormDTO.getCpuCode().getId() != null ? searchFormDTO.getCpuCode().getId().toString() : null : null;
				String productName = null != searchFormDTO && null != searchFormDTO.getProductName() ? searchFormDTO.getProductName().getValue() : null;
				String productId = null != searchFormDTO && null != searchFormDTO.getProductName() ? searchFormDTO.getProductName().getId() != null ? searchFormDTO.getProductName().getId().toString() : null : null; 
				
				
				String priority = null != searchFormDTO && searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
				String source = null != searchFormDTO && searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
				String type = null != searchFormDTO && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
				String priorityNew = searchFormDTO.getPriorityNew() != null ? searchFormDTO.getPriorityNew().getValue() != null ? searchFormDTO.getPriorityNew().getValue() : null : null;*/
				
				Boolean priorityAll = searchFormDTO.getPriorityAll() != null ? searchFormDTO.getPriorityAll() : null;
				Boolean crm = searchFormDTO.getCrm() != null ? searchFormDTO.getCrm() : null;
				Boolean vip = searchFormDTO.getVip() != null ? searchFormDTO.getVip() : null;
				List<String> selectedPriority = searchFormDTO.getSelectedPriority() != null ? searchFormDTO.getSelectedPriority() : null;
				
				/*Double claimedAmountFrom = searchFormDTO.getClaimedAmountFrom();
				Double claimedAmountTo = searchFormDTO.getClaimedAmountTo();*/
				
					Long rodKey = null;
					
					List<String> intimationNoList = new ArrayList<String>();
			
					List<Long> rodList = new ArrayList<Long>();
					
					//List<Integer> taskNumber = new ArrayList<Integer>();
					
					Map<String, Object> mapValues = new WeakHashMap<String, Object>();			
					workFlowMap= new WeakHashMap<Long, Object>();
					Integer totalRecords = 0;
					List<Map<String, Object>> taskProcedure = null ;
					
					mapValues.put(SHAConstants.USER_ID, userName);
					
					mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.BILLING_CURRENT_QUEUE);
					
					mapValues.put(SHAConstants.LOB, SHAConstants.HEALTH_LOB);
					mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.HEALTH_LOB_TYPE);
					
					if(null != intimationNo && !intimationNo.isEmpty()){
						mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);	
					}
					
					
					/*if(null != policyNo && !policyNo.isEmpty()){
						
						mapValues.put(SHAConstants.POLICY_NUMBER, policyNo);

					}
					
					if(cpuCode != null){
						
						mapValues.put(SHAConstants.CPU_CODE, cpuCode);
					}
					
					
					if(claimTypeValue != null){
						claimTypeValue = claimTypeValue.toUpperCase();
						mapValues.put(SHAConstants.CLAIM_TYPE, claimTypeValue);		
					

					}
					
				
					if(productName != null){
						
//						String[] split = productName.split("\\(");
//						String prodName = split[0];
						
						if(productName != null) {
						//	productName = productName.replaceAll("\\s","");
						
						mapValues.put(SHAConstants.PRODUCT_NAME, productName);	
						}

						
						if(productId != null){
							mapValues.put(SHAConstants.PRODUCT_KEY, productId);	
						}			
					
					}
					
//					ClassificationType classification = null;
					
					if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
							|| type != null && ! type.isEmpty()){
					//	classification = new ClassificationType();
						
						if(priority != null && ! priority.isEmpty())
							if(!priority.equalsIgnoreCase(SHAConstants.ALL)){
								mapValues.put(SHAConstants.PRIORITY, priority);
							}
						
						if(source != null && ! source.isEmpty()){
							//classification.setSource(source);
							mapValues.put(SHAConstants.STAGE_SOURCE, source);	
						}
						
						if(type != null && ! type.isEmpty()){
							if(!type.equalsIgnoreCase(SHAConstants.ALL)){
								mapValues.put(SHAConstants.RECORD_TYPE, type);
							}
							
						}						
					}*/
					
					/*if(priorityNew != null && ! priorityNew.isEmpty() && !priorityNew.equalsIgnoreCase(SHAConstants.ALL))
						if(priorityNew.equalsIgnoreCase(SHAConstants.NORMAL)){
							mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "N");
						}else{
							mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "Y");
						}*/
					
					/*if (crm != null && crm.equals(Boolean.TRUE)) {
				    	mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "Y");
				    }
				    
				    if (vip != null && vip.equals(Boolean.TRUE)) {
				    	mapValues.put(SHAConstants.PRIORITY, "VIP");
				    }*/
					
					
//					payloadBOType = SHAUtils.getReimPayloadForHealth(payloadBOType);
					
					Pageable pageable = searchFormDTO.getPageable();
					pageable.setPageNumber(1);
					pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);

//					ProcessClaimFinancialsTask processClaimFinancialsTask = BPMClientContext.getProcessClaimfinancialTask(userName, passWord);
//					
//					PagedTaskList taskList;

				//	taskList = processClaimFinancialsTask.getTasks(userName,pageable,payloadBOType);
				
					/*if(null != processClaimFinancialsTask){
						List<HumanTask> humanTaskList = taskList.getHumanTasks();
						if(null == humanTaskList | humanTaskList.isEmpty()){
							return null;
						}
						
						for(HumanTask humanTask : humanTaskList){
							PayloadBOType payloadBO = humanTask.getPayload();
							if(payloadBO.getRod() != null){
							intimationNoList.add(payloadBO.getIntimation().getIntimationNumber());							
							rodList.add(payloadBO.getRod().getKey());   
							taskNumber.add(humanTask.getNumber());
							humanTaskListDTO.add(humanTask);
							}
						}
					}*/
					
					//Map<Long,Double> claimedAmountMap = new WeakHashMap<Long, Double>();
					
					if (crm != null && crm.equals(Boolean.TRUE)) {
				    	mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "Y");
				    }
					
					if(priorityAll !=null && priorityAll){
				    	mapValues.put(SHAConstants.PRIORITY, ReferenceTable.SELECTED_ALL_PRIORITY);
				    }else if(selectedPriority !=null && !selectedPriority.isEmpty()){
				    	 StringBuilder priorit = new StringBuilder();
			    		 selectedPriority.forEach(prio -> priorit.append(prio+" "));
				    	 System.out.println(priorit.toString().trim().replace(" ","|"));
				    	 mapValues.put(SHAConstants.PRIORITY, priorit.toString().trim().replace(" ","|"));
				    	
				    }
					
					Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
					
					DBCalculationService dbCalculationService = new DBCalculationService();
					taskProcedure = dbCalculationService.revisedGetTaskProcedureForCommonBillingAndFA(setMapValues);	
					
						if (null != taskProcedure) {
							for (Map<String, Object> outPutArray : taskProcedure) {							
									Long keyValue = (Long) outPutArray.get(SHAConstants.PAYLOAD_ROD_KEY);
									String intimationNumber = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
									workFlowMap.put(keyValue,outPutArray);							
									intimationNoList.add(intimationNumber);
									rodList.add(keyValue);
								
								totalRecords = (Integer) outPutArray
										.get(SHAConstants.TOTAL_RECORDS);
							}
						}

					List<SearchProcessClaimFinancialsTableDTO> tableDTO = new ArrayList<SearchProcessClaimFinancialsTableDTO>();
					for(int index = 0 ; index < intimationNoList.size() ; index++){
						if(index < rodList.size()){
						intimationNo = intimationNoList.get(index);
						rodKey = rodList.get(index);
						tableDTO.addAll(getIntimationData(intimationNo, rodKey));
						}
					}

					
					List<SearchProcessClaimFinancialsTableDTO> filterDTO = new ArrayList<SearchProcessClaimFinancialsTableDTO>();
					
					filterDTO.addAll(tableDTO);
					
					/*if(claimedAmountFrom != null || claimedAmountTo != null){
						if(claimedAmountFrom != null && claimedAmountTo != null){
							for (SearchProcessClaimFinancialsTableDTO searchProcessClaimRequestTableDTO : tableDTO) {
								if(searchProcessClaimRequestTableDTO.getClaimedAmountAsPerBill() != null && 
										(searchProcessClaimRequestTableDTO.getClaimedAmountAsPerBill() >= claimedAmountFrom && searchProcessClaimRequestTableDTO.getClaimedAmountAsPerBill() <= claimedAmountTo)){
									filterDTO.add(searchProcessClaimRequestTableDTO);
								}
							}
						}else if(claimedAmountFrom != null){
							for (SearchProcessClaimFinancialsTableDTO searchProcessClaimRequestTableDTO : tableDTO) {
								if(searchProcessClaimRequestTableDTO.getClaimedAmountAsPerBill() != null && 
										(searchProcessClaimRequestTableDTO.getClaimedAmountAsPerBill() >= claimedAmountFrom)){
									filterDTO.add(searchProcessClaimRequestTableDTO);
								}
							}
						}else if(claimedAmountTo != null){
							for (SearchProcessClaimFinancialsTableDTO searchProcessClaimRequestTableDTO : tableDTO) {
								if(searchProcessClaimRequestTableDTO.getClaimedAmountAsPerBill() != null && 
										(searchProcessClaimRequestTableDTO.getClaimedAmountAsPerBill() <= claimedAmountTo)){
									filterDTO.add(searchProcessClaimRequestTableDTO);
								}
							}
						}
					}else{
						filterDTO.addAll(tableDTO);
					}*/
					
					Page<SearchProcessClaimFinancialsTableDTO> page = new Page<SearchProcessClaimFinancialsTableDTO>();
					page.setTotalRecords(totalRecords);
					page.setPageItems(filterDTO);
					
					if(null != searchFormDTO.getIntimationNo() || ("").equalsIgnoreCase(searchFormDTO.getIntimationNo())) {
						page.setSearchId(searchFormDTO.getIntimationNo());
					}
					
					return page;
					}
				catch(Exception e){
					e.printStackTrace();
				}
					return null;	
			
	}

	private List<SearchProcessClaimFinancialsTableDTO> getIntimationData(String intimationNo,Long rodKey/*, HumanTask humanTask,Integer taskNumber*/){
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
			List<SearchProcessClaimFinancialsTableDTO> tableDTO =  SearchProcessClaimFinancialsMapper.getInstance().getIntimationDTO(doList);
			
	        for (SearchProcessClaimFinancialsTableDTO searchProcessClaimFinancialsTableDTO : tableDTO) {
	        	
	        	if(searchProcessClaimFinancialsTableDTO.getIntimationNo() != null){
					Intimation intimationByNo = getIntimationByNo(searchProcessClaimFinancialsTableDTO.getIntimationNo());
					if(ReferenceTable.getGMCProductList().containsKey(intimationByNo.getPolicy().getProduct().getKey())){
						String colorCodeForGMC = getColorCodeForGMC(intimationByNo.getPolicy().getPolicyNumber(), intimationByNo.getInsured().getInsuredId().toString());
						searchProcessClaimFinancialsTableDTO.setColorCode(colorCodeForGMC);
					}
				}
	        	Claim claimByKey = getClaimByIntimation(searchProcessClaimFinancialsTableDTO.getKey());
	        	searchProcessClaimFinancialsTableDTO.setCrmFlagged(claimByKey.getCrcFlag());
	        	/*if(searchProcessClaimFinancialsTableDTO.getCrmFlagged() != null){
					if(searchProcessClaimFinancialsTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
						searchProcessClaimFinancialsTableDTO.setColorCodeCell("OLIVE");
						searchProcessClaimFinancialsTableDTO.setCrmFlagged(null);
						//objSearchProcessPreAuthTableDTO.setCrmFlagged(FontAwesome.ADJUST.getHtml());
					}
				}*/
	        	
	        	if(searchProcessClaimFinancialsTableDTO.getCpuCode() != null){
	        		
					Long cpuCode = searchProcessClaimFinancialsTableDTO.getCpuCode();
					searchProcessClaimFinancialsTableDTO.setStrCpuCode(String.valueOf(cpuCode));
				}
	        	
	        	
				if(claimByKey != null) {
					searchProcessClaimFinancialsTableDTO.setCoverCode(claimByKey.getClaimCoverCode());
					searchProcessClaimFinancialsTableDTO.setSubCoverCode(claimByKey.getClaimSubCoverCode());
					searchProcessClaimFinancialsTableDTO.setSectionCode(claimByKey.getClaimSectionCode());
					
					if(searchProcessClaimFinancialsTableDTO.getCrmFlagged() != null){
						if(searchProcessClaimFinancialsTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
							if (claimByKey.getCrcFlag() != null && claimByKey.getCrcFlag().equalsIgnoreCase("Y")) {
								searchProcessClaimFinancialsTableDTO.setColorCodeCell("OLIVE");
							}
							searchProcessClaimFinancialsTableDTO.setCrmFlagged(null);
						}
					}
					if (claimByKey.getIsVipCustomer() != null && claimByKey.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
						searchProcessClaimFinancialsTableDTO.setColorCodeCell("VIP");
					}
					if (claimByKey.getCrcFlag() != null && claimByKey.getCrcFlag().equalsIgnoreCase("Y") 
							&& claimByKey.getIsVipCustomer() != null && claimByKey.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
						searchProcessClaimFinancialsTableDTO.setColorCodeCell("CRMVIP");
					}
					
				}
			}
			
			int i=0;
			
			for (SearchProcessClaimFinancialsTableDTO searchProcessClaimFinancialsTableDTO : tableDTO) {
				String productName = searchProcessClaimFinancialsTableDTO.getProductName();
				productName = productName + "/"+doList.get(i).getPolicy().getProduct().getCode();
				searchProcessClaimFinancialsTableDTO.setProductName(productName);
				i++;
			}
			//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
			
			for (SearchProcessClaimFinancialsTableDTO searchProcessClaimFinancialsTableDTO : tableDTO) {
				searchProcessClaimFinancialsTableDTO.setUsername(userName);
			}
			
			tableDTO = getHospitalDetails(tableDTO/*, humanTask*/);
			tableDTO = getclaimNumber(tableDTO, rodKey/*, humanTask,taskNumber*/);
		
		return tableDTO;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	

	private List<SearchProcessClaimFinancialsTableDTO> getHospitalDetails(
			List<SearchProcessClaimFinancialsTableDTO> tableDTO/*, HumanTask humanTask*/) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
			
			 tableDTO.get(index).setlOB(getLOBValue(tableDTO.get(index).getlOBId()).getValue());
			/* if(humanTask.getPayload() != null && humanTask.getPayload().getPaymentInfo() != null && humanTask.getPayload().getPaymentInfo().getClaimedAmount() != null){
				 tableDTO.get(index).setClaimedAmountAsPerBill(humanTask.getPayload().getPaymentInfo().getClaimedAmount());
			 }*/
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				 
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospitalAddress(hospitalDetail.getAddress());
//				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
//				 Long cpuCode = null != getTmpCPUCode(tableDTO.get(index).getCpuId()) ? getTmpCPUCode(tableDTO.get(index).getCpuId()).getCpuCode() : 0l;
//				 tableDTO.get(index).setCpuCode(cpuCode);
//				 String cpuName = null != getTmpCPUCode(tableDTO.get(index).getCpuId()) ? getTmpCPUCode(tableDTO.get(index).getCpuId()).getDescription() : "";		
//				 tableDTO.get(index).setCpuName(cpuName);
				
			 }
			}catch(Exception e){
				continue;
			}
		
		}
		
		
		return tableDTO;
	}
	
	private List<SearchProcessClaimFinancialsTableDTO> getclaimNumber(List<SearchProcessClaimFinancialsTableDTO> intimationList, Long rodKey/*, HumanTask humanTask,Integer taskNumber*/){
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
						if(a_claim != null){
						//	intimationList.get(index).setHumanTaskDTO(humanTask);
							intimationList.get(index).setRodKey(rodKey);
							

							Reimbursement reimbursementByKey = getReimbursementByKey(rodKey);
							
							Object workflowKey = workFlowMap.get(intimationList.get(index).getRodKey());
							Map<String, Object> outPutArray = (Map<String, Object>)workflowKey;
							String claimedAmount = (String) outPutArray.get(SHAConstants.CLAIMED_AMOUNT);
							if(claimedAmount != null){
								intimationList.get(index).setClaimedAmountAsPerBill(Double.valueOf(claimedAmount));
							}
							
//							if(null != reimbursementByKey.getBenApprovedAmt()){
//								intimationList.get(index).setClaimedAmountAsPerBill(Double.valueOf(reimbursementByKey.getBenApprovedAmt()));
//							}
							
//							tableDto.setDbOutArray(workflowKey);
							intimationList.get(index).setDbOutArray(workflowKey);
							
							
							
							 if(reimbursementByKey.getDocAcknowLedgement() != null && reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId() != null
									 && reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue() != null){
								 intimationList.get(index).setDocumentReceivedFrom(reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
							 }
							
							intimationList.get(index).setOriginatorID(reimbursementByKey.getModifiedBy());
							intimationList.get(index).setOriginatorName(reimbursementByKey.getModifiedBy());
							intimationList.get(index).setClaimKey(a_claim.getKey());
						//	intimationList.get(index).setTaskNumber(taskNumber);
							if(a_claim.getClaimType() != null){
								intimationList.get(index).setClaimType(a_claim.getClaimType().getValue());
							}
							String type = null != getType(intimationList.get(index).getRodKey()) ? getType(intimationList.get(index).getRodKey()).getStatus().getProcessValue() : "";
							intimationList.get(index).setType(type);
						}	
				}catch(Exception e){
					continue;
				}
			} 
		}
		return intimationList;
	}
	
	private Reimbursement getType(Long key){
		try{
		Query findType = entityManager.createNamedQuery("Reimbursement.findByKey").setParameter("primaryKey", key);
		Reimbursement reimbursement = (Reimbursement) findType.getSingleResult();
		return reimbursement;
		}catch(Exception e){
			
		}
		return null;
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
	
	@SuppressWarnings("unchecked")
	public Reimbursement getReimbursementByKey(Long rodKey){
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByKey").setParameter(
						"primaryKey", rodKey);
		List<Reimbursement> rodList = query.getResultList();
		
		if(rodList != null && !rodList.isEmpty()) {
			for (Reimbursement reimbursement : rodList) {
				entityManager.refresh(reimbursement);
			}
			return rodList.get(0);
		}
		return null;
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
	
	public IncurredClaimRatio getIncurredClaimRatio(String policyNumber, String insuredNumber){
		
		Query query = entityManager
				.createNamedQuery("IncurredClaimRatio.findByPolicyNumber");
		query.setParameter("policyNumber", policyNumber);
		//query.setParameter("insuredNumber", insuredNumber);
		List<IncurredClaimRatio> result = (List<IncurredClaimRatio>) query.getResultList();
		if(result != null && ! result.isEmpty()){
			return result.get(0);
		}
		return null;
		
	}


	public String getColorCodeForGMC(String policyNumber, String insuredNumber){
		IncurredClaimRatio incurredClaimRatio = getIncurredClaimRatio(policyNumber, insuredNumber);
		if(incurredClaimRatio != null){
			return incurredClaimRatio.getClaimColour();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Intimation getIntimationByNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery(
				"Intimation.findByIntimationNumber").setParameter(
				"intimationNo", intimationNo);

		List<Intimation> intimationList = (List<Intimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}
}