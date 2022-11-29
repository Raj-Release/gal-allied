/**
 * 
 */
package com.shaic.paclaim.billing.hospprocessclaimbilling.search;

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
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementBenefits;
import com.shaic.domain.TmpCPUCode;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.billing.processclaimbilling.search.SearchProcessClaimBillingFormDTO;
import com.shaic.reimbursement.billing.processclaimbilling.search.SearchProcessClaimBillingMapper;
import com.shaic.reimbursement.billing.processclaimbilling.search.SearchProcessClaimBillingTableDTO;

/**
 * @author ntv.narenj
 *
 */
@Stateless
public class PASearchHospCoverProcessClaimBillingService extends AbstractDAO<Intimation>{

	@PersistenceContext
	protected EntityManager entityManager;
	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	public String userName;
	
	public PASearchHospCoverProcessClaimBillingService() {
		super();
	}

	public  Page<SearchProcessClaimBillingTableDTO> search(
			SearchProcessClaimBillingFormDTO searchFormDTO,
			String userName, String passWord) {
		try{
			String intimationNo =  null != searchFormDTO && null!= searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() : null;
			this.userName = userName;
			String policyNo = null != searchFormDTO && null!= searchFormDTO.getPolicyNo() ? searchFormDTO.getPolicyNo() : null;
			String cpuCode = null != searchFormDTO && searchFormDTO.getCpuCode() != null ? searchFormDTO.getCpuCode().getId() != null ? searchFormDTO.getCpuCode().getId().toString() : null : null;
			String producetName = null != searchFormDTO && null != searchFormDTO.getProductNameCode() ? searchFormDTO.getProductNameCode().getValue() : null;
			String productId = null != searchFormDTO && null != searchFormDTO.getProductNameCode() ? searchFormDTO.getProductNameCode().getId() != null ? searchFormDTO.getProductNameCode().getId().toString() : null : null; 
			
			String priority = null != searchFormDTO && searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
			String source = null != searchFormDTO && searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
			String type = null != searchFormDTO && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
		
Long rodKey = null;
			
			List<String> intimationNoList = new ArrayList<String>();
			List<String> policyNoList = new ArrayList<String>();
			
		
			List<Long> rodKeyList = new ArrayList<Long>();
			
			List<Integer> taskNumber = new ArrayList<Integer>();
			String strClaimType = "";
			
			
			
			List<Map<String, Object>> taskProcedure = null ;
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();			
			workFlowMap= new WeakHashMap<Long, Object>();
			Integer totalRecords = 0;
			
			mapValues.put(SHAConstants.USER_ID, userName);
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.BILLING_CURRENT_KEY);
			
			mapValues.put(SHAConstants.LOB, SHAConstants.HEALTH_LOB);
			mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.HEALTH_LOB_TYPE);
			
		
			if(null != intimationNo && !intimationNo.isEmpty()){
				
				mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);	
				}

				
				if(null != policyNo && !policyNo.isEmpty()){
					
					mapValues.put(SHAConstants.POLICY_NUMBER, policyNo);

				}
				
				if(null != cpuCode){
					mapValues.put(SHAConstants.CPU_CODE, cpuCode);
				}
				
				if(null != searchFormDTO.getClaimType() && ! searchFormDTO.getClaimType().equals(""))
				{
					strClaimType= searchFormDTO.getClaimType().getValue();
					mapValues.put(SHAConstants.CLAIM_TYPE, strClaimType.toUpperCase());
				}			

				
				
				if(null != producetName){
					
					String[] split = producetName.split("\\(");
					String productName = split[0];
					
					if(productName != null) {
					//	productName = productName.replaceAll("\\s","");
					
					mapValues.put(SHAConstants.PRODUCT_NAME, productName);	

					}
				
					if(productId != null){
						
						mapValues.put(SHAConstants.PRODUCT_KEY, productId);		
					}					
				}
				
//				ClassificationType classification = null;
				
				if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
						|| type != null && ! type.isEmpty()){
				//	classification = new ClassificationType();
					
					if(priority != null && ! priority.isEmpty())
						if(!priority.equalsIgnoreCase(SHAConstants.ALL)){
							//priority = null;
							mapValues.put(SHAConstants.PRIORITY, priority);
						}
					//classification.setPriority(priority);
				
					if(source != null && ! source.isEmpty()){
						//classification.setSource(source);
						mapValues.put(SHAConstants.STAGE_SOURCE, source);	
					}
					
					if(type != null && ! type.isEmpty()){
						if(!type.equalsIgnoreCase(SHAConstants.ALL)){
						//	type = null;
							mapValues.put(SHAConstants.RECORD_TYPE, type);
						}
						//classification.setType(type);
						
					}					
				}
		
			Pageable pageable = searchFormDTO.getPageable();
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.SEPARATE_ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.SEPARATE_ITEMS_PER_PAGE) : Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE));
			
			//Adding for test purpose
//			PagedTaskList taskList;
//			if(!intimationNo.isEmpty() || !policyNo.isEmpty() || null != cpuCode || null != type || null != producetName){
//				 taskList = processClaimBillingTask.getTasks(userName,null,payloadBOType);
//			}else{
			/*PagedTaskList taskList = processClaimBillingTask.getTasks(userName,pageable,payloadBOType);
//			}
			
			Integer limitCount = BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 20;
			if(null != processClaimBillingTask) {
				List<HumanTask> humanTaskList = taskList.getHumanTasks();
				if(null == humanTaskList | humanTaskList.isEmpty()){
					return null;
				}
				
				for(HumanTask humanTask : humanTaskList){
					PayloadBOType payloadBO = humanTask.getPayload();
					if(payloadBO.getRod() != null  && intimationNoList.size() < limitCount){
						intimationNoList.add(payloadBO.getIntimation().getIntimationNumber());
						
						rodKeyList.add(payloadBO.getRod().getKey());
						taskNumber.add(humanTask.getNumber());
						humanTaskListDTO.add(humanTask);
					}
				}
			}*/
			

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
//							Long reimbursementKey = (Long) outPutArray.get(SHAConstants.PAYLOAD_ROD_KEY);							
							rodKeyList.add(keyValue);
						
						totalRecords = (Integer) outPutArray
								.get(SHAConstants.TOTAL_RECORDS);
					}
				}
			List<SearchProcessClaimBillingTableDTO> tableDTO = new ArrayList<SearchProcessClaimBillingTableDTO>();
			for(int index = 0 ; index < intimationNoList.size() ; index++){
				 if(index < rodKeyList.size()){
				intimationNo = intimationNoList.get(index);
		
				// humanTaskDTO = humanTaskListDTO.get(index);
				
				 rodKey = rodKeyList.get(index);
				 
				 Integer taskNo = taskNumber.get(index);
				 
				 tableDTO.addAll(getIntimationData(intimationNo,  rodKey/*, humanTaskDTO,taskNo*/));
				}
			}

		
			Page<SearchProcessClaimBillingTableDTO> page = new Page<SearchProcessClaimBillingTableDTO>();
			//page.setPageNumber(taskList.getCurrentPage());
			//page.setTotalRecords(taskList.getTotalRecords());
			page.setPageItems(tableDTO);
			return page;
			}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		}
			return null;	
	}
	private List<SearchProcessClaimBillingTableDTO> getIntimationData(String intimationNo, Long rodKey/*, HumanTask humanTask,Integer taskNumber*/){
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
			List<SearchProcessClaimBillingTableDTO> tableDTO = SearchProcessClaimBillingMapper.getInstance().getIntimationDTO(doList);
			
			for (SearchProcessClaimBillingTableDTO searchProcessClaimBillingTableDTO : tableDTO) {
				
				if(searchProcessClaimBillingTableDTO.getCpuCode() != null){
					Long cpuCode = searchProcessClaimBillingTableDTO.getCpuCode();
					searchProcessClaimBillingTableDTO.setStrCpuCode(String.valueOf(cpuCode));
				}
			}
			//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
			
			for (SearchProcessClaimBillingTableDTO searchProcessClaimBillingTableDTO : tableDTO) {
				searchProcessClaimBillingTableDTO.setUsername(userName);
			}
			
			tableDTO = getHospitalDetails(tableDTO);
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
	
	private List<SearchProcessClaimBillingTableDTO> getclaimNumber(List<SearchProcessClaimBillingTableDTO> intimationList, Long rodKey/*, HumanTask humanTask,Integer taskNumber*/){
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
						//	intimationList.get(index).setTaskNumber(taskNumber);
							Reimbursement reimbursementByKey = getReimbursementByKey(rodKey);
							
							 if(reimbursementByKey.getDocAcknowLedgement() != null && reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId() != null
									 && reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue() != null){
								 intimationList.get(index).setDocumentReceivedFrom(reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
							 }
							
							intimationList.get(index).setOriginatorID(reimbursementByKey.getModifiedBy());
							intimationList.get(index).setOriginatorName(reimbursementByKey.getModifiedBy());
							intimationList.get(index).setClaimKey(a_claim.getKey());
							intimationList.get(index).setClaimType(a_claim.getClaimType().getValue());
							String type = null != getType(intimationList.get(index).getRodKey()) ? getType(intimationList.get(index).getRodKey()).getStatus().getProcessValue() : "";
							//double claimedAmt = null != getType(intimationList.get(index).getRodKey()) ? getType(intimationList.get(index).getRodKey()).getCurrentProvisionAmt() : 0.0;
							intimationList.get(index).setClaimAmt(getClaimedAmount(intimationList.get(index).getRodKey()));
							intimationList.get(index).setType(type);
						}else{
							intimationList.get(index).setClaimAmt(0.0);
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

	private List<SearchProcessClaimBillingTableDTO> getHospitalDetails(
			List<SearchProcessClaimBillingTableDTO> tableDTO) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
	
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				 ;
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospitalAddress(hospitalDetail.getAddress());
				 tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId()+" ,"+hospitalDetail.getCity());
				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
//				 Long cpuCode = null != getTmpCPUCodeAndName(tableDTO.get(index).getCpuId()) ? getTmpCPUCodeAndName(tableDTO.get(index).getCpuId()).getCpuCode() : 0l;
//				 tableDTO.get(index).setCpuCode(cpuCode);
//				 String cpuName = null != getTmpCPUCodeAndName(tableDTO.get(index).getCpuId()) ? getTmpCPUCodeAndName(tableDTO.get(index).getCpuId()).getDescription() : "";		
//				 tableDTO.get(index).setCpuName(cpuName);
			 }
			}catch(Exception e){
				continue;
			}
		
		}
		
		
		return tableDTO;
	}
	
	private TmpCPUCode getTmpCPUCodeAndName(Long cpuId){
		try{
		Query findCpuCode = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
		 TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
		return tmpCPUCode;
		}catch(Exception e){
			
		}
		return null;
	}
	private Double getClaimedAmount(Long key) {
		try {
			Double claimedAmount = 0.0;
			Query findType = entityManager.createNamedQuery(
					"DocAcknowledgement.findByLatestAcknowledge").setParameter("rodKey", key);
			List<DocAcknowledgement> reimbursement = (List<DocAcknowledgement>) findType
					.getResultList();
			
			DocAcknowledgement docAcknowledgement = new DocAcknowledgement();
		    if(reimbursement != null && ! reimbursement.isEmpty()){
		    	docAcknowledgement = reimbursement.get(0);
		    }
			
			Query findType1 = entityManager.createNamedQuery(
					"ReimbursementBenefits.findByRodKey").setParameter("rodKey", key);
			List<ReimbursementBenefits> reimbursementBenefitsList = (List<ReimbursementBenefits>) findType1.getResultList();
			Double currentProvisionalAmount = 0.0;
			for(ReimbursementBenefits reimbursementBenefits : reimbursementBenefitsList){
				currentProvisionalAmount += reimbursementBenefits.getTotalClaimAmountBills();
				
			}
			Double hospitalizationClaimedAmount = null != docAcknowledgement.getHospitalizationClaimedAmount() ? docAcknowledgement.getHospitalizationClaimedAmount() : 0.0;
			Double postHospitalizationClaimedAmount = null != docAcknowledgement.getPostHospitalizationClaimedAmount() ? docAcknowledgement.getPostHospitalizationClaimedAmount() : 0.0;
			Double preHospitalizationClaimedAmount = null != docAcknowledgement.getPreHospitalizationClaimedAmount() ? docAcknowledgement.getPreHospitalizationClaimedAmount() : 0.0;
			claimedAmount = hospitalizationClaimedAmount + postHospitalizationClaimedAmount + preHospitalizationClaimedAmount+currentProvisionalAmount;
			
			return claimedAmount;
		} catch (Exception e) {

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