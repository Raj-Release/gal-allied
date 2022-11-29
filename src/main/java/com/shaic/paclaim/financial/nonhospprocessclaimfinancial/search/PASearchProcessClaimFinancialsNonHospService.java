
/**
 * 
 */
package com.shaic.paclaim.financial.nonhospprocessclaimfinancial.search;

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
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpCPUCode;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsFormDTO;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsTableDTO;
/**
 * 
 *
 */
@Stateless
public class PASearchProcessClaimFinancialsNonHospService extends AbstractDAO<Intimation>{

	
	@PersistenceContext
	protected EntityManager entityManager;
	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	
	public String userName;
	
	
	public PASearchProcessClaimFinancialsNonHospService() {
		super();
		
	}
	
	public  Page<SearchProcessClaimFinancialsTableDTO> search(
			SearchProcessClaimFinancialsFormDTO searchFormDTO,
			String userName, String passWord) {
		
			try{
				String intimationNo = null != searchFormDTO && null != searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() : null;
				String policyNo = null != searchFormDTO && null != searchFormDTO.getPolicyNo() ? searchFormDTO.getPolicyNo() : null ;
				String claimTypeValue = null != searchFormDTO && null != searchFormDTO.getClaimType() ? searchFormDTO.getClaimType().getValue() : null;
				String cpuCode = null != searchFormDTO && null != searchFormDTO.getCpuCode() ? searchFormDTO.getCpuCode().getId() != null ? searchFormDTO.getCpuCode().getId().toString() : null : null;
				String productName = null != searchFormDTO && null != searchFormDTO.getProductName() ? searchFormDTO.getProductName().getValue() : null;
				String productId = null != searchFormDTO && null != searchFormDTO.getProductName() ? searchFormDTO.getProductName().getId() != null ? searchFormDTO.getProductName().getId().toString() : null : null; 
				
				
				String priority = null != searchFormDTO && searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
				String source = null != searchFormDTO && searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
				String type = null != searchFormDTO && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
				
				Double claimedAmountFrom = searchFormDTO.getClaimedAmountFrom();
				Double claimedAmountTo = searchFormDTO.getClaimedAmountTo();
				
				Long rodKey = null;
				
				List<String> intimationNoList = new ArrayList<String>();
		
				List<Long> rodList = new ArrayList<Long>();
				
				List<Integer> taskNumber = new ArrayList<Integer>();
				
				Map<String, Object> mapValues = new WeakHashMap<String, Object>();			
				workFlowMap= new WeakHashMap<Long, Object>();
				Integer totalRecords = 0;
				List<Map<String, Object>> taskProcedure = null ;		
				
				mapValues.put(SHAConstants.USER_ID, userName);
				mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.FA_CURRENT_QUEUE);
				mapValues.put(SHAConstants.LOB, SHAConstants.PA_LOB);
				mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.PA_LOB_TYPE);
					
				
					if(null != intimationNo && !intimationNo.isEmpty()){
				  
						mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);
					}
					
					if(null != policyNo && !policyNo.isEmpty()){
						
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
						String[] split = productName.split("\\(");
						String prodName = split[0];
						
						if(productName != null) {
						//	productName = productName.replaceAll("\\s","");
						
						mapValues.put(SHAConstants.PRODUCT_NAME, prodName);	
						}

						
						if(productId != null){
							mapValues.put(SHAConstants.PRODUCT_KEY, productId);	
						}			
					}
					
					
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
					}
				
					
					Pageable pageable = searchFormDTO.getPageable();
					pageable.setPageNumber(1);
					pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);

					
					Map<Long,Double> claimedAmountMap = new WeakHashMap<Long, Double>();
					
					Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
					
					DBCalculationService dbCalculationService = new DBCalculationService();
					taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);	
					
						if (null != taskProcedure) {
							for (Map<String, Object> outPutArray : taskProcedure) {							
									Long keyValue = (Long) outPutArray.get(SHAConstants.PAYLOAD_ROD_KEY);
									String intimationNumber = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
									workFlowMap.put(keyValue,outPutArray);							
									intimationNoList.add(intimationNumber);
//									Long reimbursementKey = (Long) outPutArray.get(SHAConstants.PAYLOAD_ROD_KEY);							
									rodList.add(keyValue);
								
								totalRecords = (Integer) outPutArray
										.get(SHAConstants.TOTAL_RECORDS);
							}
						}
					
					

					List<SearchProcessClaimFinancialsTableDTO> tableDTO = new ArrayList<SearchProcessClaimFinancialsTableDTO>();
					for(int index = 0 ; index < intimationNoList.size() ; index++){
						if(index < rodList.size()){
						intimationNo = intimationNoList.get(index);
					//	humanTaskDTO = humanTaskListDTO.get(index);						
						rodKey = rodList.get(index);
					//	Integer taskNo = taskNumber.get(index);
						tableDTO.addAll(getIntimationData(intimationNo, rodKey/*, humanTaskDTO,taskNo*/));
						}
					}

					
					List<SearchProcessClaimFinancialsTableDTO> filterDTO = new ArrayList<SearchProcessClaimFinancialsTableDTO>();
					
					if(claimedAmountFrom != null || claimedAmountTo != null){
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
					}
					
					
					Page<SearchProcessClaimFinancialsTableDTO> page = new Page<SearchProcessClaimFinancialsTableDTO>();
				//	page.setPageNumber(taskList.getCurrentPage());
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
			List<SearchProcessClaimFinancialsTableDTO> tableDTO =  PASearchProcessClaimFinancialsNonHospMapper.getInstance().getIntimationDTO(doList);
			
	        for (SearchProcessClaimFinancialsTableDTO searchProcessClaimFinancialsTableDTO : tableDTO) {
	        	
	        	if(searchProcessClaimFinancialsTableDTO.getProductKey() != null && ReferenceTable.getGPAProducts().containsKey(searchProcessClaimFinancialsTableDTO.getProductKey())){
					if(searchProcessClaimFinancialsTableDTO.getPaPatientName() != null){
						searchProcessClaimFinancialsTableDTO.setInsuredPatientName(searchProcessClaimFinancialsTableDTO.getPaPatientName());
					}
				}
	        	if(searchProcessClaimFinancialsTableDTO.getCpuCode() != null){
	        		
					Long cpuCode = searchProcessClaimFinancialsTableDTO.getCpuCode();
					searchProcessClaimFinancialsTableDTO.setStrCpuCode(String.valueOf(cpuCode));
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
							Object workflowKey = workFlowMap.get(intimationList.get(index).getRodKey());
							Map<String, Object> outPutArray = (Map<String, Object>)workflowKey;
							
							Reimbursement reimbursementByKey = getReimbursementByKey(rodKey);
							
							if(null != reimbursementByKey.getBenApprovedAmt()){
								intimationList.get(index).setClaimedAmountAsPerBill(Double.valueOf(reimbursementByKey.getBenApprovedAmt()));
							}
							
							 if(reimbursementByKey.getDocAcknowLedgement() != null && reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId() != null
									 && reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue() != null){
								 intimationList.get(index).setDocumentReceivedFrom(reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
							 }
							 intimationList.get(index).setDbOutArray(workflowKey);
							intimationList.get(index).setOriginatorID(reimbursementByKey.getModifiedBy());
							intimationList.get(index).setOriginatorName(reimbursementByKey.getModifiedBy());
							intimationList.get(index).setClaimKey(a_claim.getKey());
						//	intimationList.get(index).setTaskNumber(taskNumber);
							if(a_claim.getClaimType() != null){
								intimationList.get(index).setClaimType(a_claim.getClaimType().getValue());
							}
							String type = null != getType(intimationList.get(index).getRodKey()) ? getType(intimationList.get(index).getRodKey()).getStatus().getProcessValue() : "";
							intimationList.get(index).setType(type);
							if(null != a_claim.getIncidenceFlag())
							{
							if((SHAConstants.ACCIDENT_FLAG).equalsIgnoreCase(a_claim.getIncidenceFlag()))
							{
								intimationList.get(index).setAccidentOrDeath(SHAConstants.ACCIDENT);
							}
							else
							{
								intimationList.get(index).setAccidentOrDeath(SHAConstants.DEATH);
							}
							}
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
}