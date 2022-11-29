/**
 * 
 */
package com.shaic.reimbursement.medicalapproval.processclaimrequestzonal.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
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
import com.shaic.domain.IncurredClaimRatio;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementBenefits;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;



/**
 * @author ntv.narenj
 *
 */
@Stateless
public class SearchProcessClaimRequestZonalService extends
		AbstractDAO<Intimation> {

	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private ReimbursementService reimbursementService;


	@PersistenceContext
	protected EntityManager entityManager;
	
	
	String userName;
	
	//Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	Map<Long, Object> workFlowMap = null;

	public SearchProcessClaimRequestZonalService() {
		super();

	}

	public Page<SearchProcessClaimRequestZonalTableDTO> search(
			SearchProcessClaimRequestZonalFormDTO searchFormDTO,
			String userName, String passWord) {

		List<Map<String, Object>> taskProcedure = null ;
		try {
			String intimationNo = null != searchFormDTO && null!= searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() : null;
			this.userName = userName;
			String policyNo = null != searchFormDTO && null!= searchFormDTO.getPolicyNo() ? searchFormDTO.getPolicyNo() : null;
			String intimationSource =  null != searchFormDTO &&  null != searchFormDTO.getIntimationSource() ? searchFormDTO.getIntimationSource().getValue() : null;
			String hospitaltype =  null != searchFormDTO && null != searchFormDTO.getHospitalType() ? searchFormDTO.getHospitalType().getValue() : null;
			String networkHospitalId =  null != searchFormDTO && null != searchFormDTO.getNetworkHospType() ? searchFormDTO.getNetworkHospType().getValue() : null;
					
			String cpuCode =  null != searchFormDTO && searchFormDTO.getCpuCode() != null ? searchFormDTO.getCpuCode().getId() != null ? searchFormDTO.getCpuCode().getId().toString() : null : null;
			String productName =  null != searchFormDTO && null != searchFormDTO.getProductName() ? searchFormDTO.getProductName().getValue() : null;
					
			String priority =  null != searchFormDTO && searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
			String source =  null != searchFormDTO && searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
			String type =  null != searchFormDTO && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
			String priorityNew = searchFormDTO.getPriorityNew() != null ? searchFormDTO.getPriorityNew().getValue() != null ? searchFormDTO.getPriorityNew().getValue() : null : null;
			
			Boolean priorityAll = searchFormDTO.getPriorityAll() != null ? searchFormDTO.getPriorityAll() : null;
			Boolean crm = searchFormDTO.getCrm() != null ? searchFormDTO.getCrm() : null;
			Boolean vip = searchFormDTO.getVip() != null ? searchFormDTO.getVip() : null;
			List<String> selectedPriority = searchFormDTO.getSelectedPriority() != null ? searchFormDTO.getSelectedPriority() : null;
			
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			Integer totalRecords = 0; 
			
			//List<Long> keys = new ArrayList<Long>(); 
			
			
			mapValues.put(SHAConstants.USER_ID, userName);
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.ZMR_CURRENT_QUEUE);
			
			mapValues.put(SHAConstants.LOB, SHAConstants.HEALTH_LOB);
			mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.HEALTH_LOB_TYPE);

		
			Long rodKey;
			List<String> intimationNoList = new ArrayList<String>();

			
			List<Long> rodKeyList = new ArrayList<Long>();
			
			//List<Integer> taskNumber = new ArrayList<Integer>();
			
			
			if (null != intimationNo && !intimationNo.isEmpty()) {
				
				mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);
				/*intimationType.setIntimationNumber(intimationNo);
				payloadBOType.setIntimation(intimationType);*/

			}
			
			if (null != policyNo && !policyNo.isEmpty()) {
				
				mapValues.put(SHAConstants.POLICY_NUMBER, policyNo);
			/*	policyType.setPolicyId(policyNo);
				payloadBOType.setPolicy(policyType);*/
			}
			if (null != intimationSource) {
				
				
				mapValues.put(SHAConstants.INT_SOURCE, intimationSource);
				/*intimationType.setIntimationSource(intimationSource);
				payloadBOType.setIntimation(intimationType);*/

			}
		
			if (null != hospitaltype) {
				
				mapValues.put(SHAConstants.HOSPITAL_TYPE, hospitaltype);
				/*hospitalInfoType.setHospitalType(hospitaltype);
				payloadBOType.setHospitalInfo(hospitalInfoType);*/

			}

			if (null != networkHospitalId) {
				
				
				mapValues.put(SHAConstants.NETWORK_TYPE, networkHospitalId);
				/*hospitalInfoType.setNetworkHospitalType(networkHospitalId);
				payloadBOType.setHospitalInfo(hospitalInfoType);*/
			}
			
			
			if(null != cpuCode){
				
				mapValues.put(SHAConstants.CPU_CODE, cpuCode);
				/*claimRequestType.setCpuCode(cpuCode);
				payloadBOType.setClaimRequest(claimRequestType);*/

			}
			
			
			if(productName != null){
				
//				String[] split = productName.split("\\(");
//				String prodctName = split[0];
				
				if(productName != null) {
				//	productName = productName.replaceAll("\\s","");
				
				mapValues.put(SHAConstants.PRODUCT_NAME, productName);	
				}
				/*productType.setProductName(productName);
				payloadBOType.setProductInfo(productType);*/
			}
			
			
			
			if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
					|| type != null && ! type.isEmpty()){
				
				
				if(priority != null && ! priority.isEmpty())
					if(!priority.equalsIgnoreCase(SHAConstants.ALL)){
						//priority = null;
					
				//classification.setPriority(priority);
				mapValues.put(SHAConstants.PRIORITY,priority);
			}
				if(source != null && ! source.isEmpty()){
					//classification.setSource(source);
					mapValues.put(SHAConstants.STAGE_SOURCE,source);
				}
				
				if(type != null && ! type.isEmpty()){
					if(!type.equalsIgnoreCase(SHAConstants.ALL)){
						//type = null;
					
				//	classification.setType(type);
					mapValues.put(SHAConstants.RECORD_TYPE, type);
					}
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
		    
		    if(priorityAll !=null && priorityAll){
		    	mapValues.put(SHAConstants.PRIORITY, ReferenceTable.SELECTED_ALL_PRIORITY);
		    }else if(selectedPriority !=null && !selectedPriority.isEmpty()){
		    	 StringBuilder priorit = new StringBuilder();
	    		 selectedPriority.forEach(prio -> priorit.append(prio+" "));
		    	 System.out.println(priorit.toString().trim().replace(" ","|"));
		    	 mapValues.put(SHAConstants.PRIORITY, priorit.toString().trim().replace(" ","|"));
		    	
		    }
			
			Pageable pageable = searchFormDTO.getPageable();
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
			

			/*ProcessClaimRequestZMRTask processClaimRequestZMRTask = BPMClientContext
					.getprocessClaimRequestZonalTask(userName, passWord);

			PagedTaskList taskList;
			
			taskList = processClaimRequestZMRTask.getTasks(userName,
					pageable, payloadBOType);*/
			workFlowMap= new WeakHashMap<Long, Object>();
			
			Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
			
			DBCalculationService dbCalculationService = new DBCalculationService();
			taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);	
	
		
			if (null != taskProcedure) {
				for (Map<String, Object> outPutArray : taskProcedure) {							
						Long keyValue = (Long) outPutArray.get(SHAConstants.PAYLOAD_ROD_KEY);
						String intimationNumber = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
						workFlowMap.put(keyValue,outPutArray);
						intimationNoList.add(intimationNumber);
						rodKeyList.add(keyValue);
					
					totalRecords = (Integer) outPutArray
							.get(SHAConstants.TOTAL_RECORDS);
				}
			}
			

			/*if (null != processClaimRequestZMRTask) {
				List<HumanTask> humanTaskList = taskList.getHumanTasks();
				if (null == humanTaskList | humanTaskList.isEmpty()) {
					return null;
				}

				for (HumanTask humanTask : humanTaskList) {
					PayloadBOType payloadBO = humanTask.getPayload();
					if (payloadBO.getRod() != null) {
						intimationNoList.add(payloadBO.getIntimation()
								.getIntimationNumber());
						humanTaskListDTO.add(humanTask);

						rodKeyList.add(payloadBO.getRod().getKey());
						taskNumber.add(humanTask.getNumber());
					}
				}
			}*/
			List<SearchProcessClaimRequestZonalTableDTO> tableDTO = new ArrayList<SearchProcessClaimRequestZonalTableDTO>();

			for (int index = 0; index < intimationNoList.size(); index++) {
				if (index < rodKeyList.size()) {
					intimationNo = intimationNoList.get(index);

				//	humanTaskDTO = humanTaskListDTO.get(index);

					rodKey = rodKeyList.get(index);
					//Integer taskNo = taskNumber.get(index);
					 List<SearchProcessClaimRequestZonalTableDTO> intimationData = getIntimationData(intimationNo, rodKey);
					if (null != intimationData)
						tableDTO.addAll(intimationData);
				}
			}

			/**
			 * To Sort manually we need to call the following method.
			 * 
			 */
			Collections.sort(tableDTO, getComparator());
			
			Page<SearchProcessClaimRequestZonalTableDTO> page = new Page<SearchProcessClaimRequestZonalTableDTO>();
			//page.setPageNumber(taskList.getCurrentPage());
			page.setPageItems(tableDTO);
			page.setTotalRecords(totalRecords);
			
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh" + e.getMessage()
					+ "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		}
		return null;

	}

	private List<SearchProcessClaimRequestZonalTableDTO> getIntimationData(
			String intimationNo, Long rodKey/*, HumanTask humanTask,Integer taskNumber*/) {
		final CriteriaBuilder criteriaBuilder = entityManager
				.getCriteriaBuilder();
		final CriteriaQuery<Intimation> criteriaQuery = criteriaBuilder
				.createQuery(Intimation.class);

		List<Intimation> intimationsList = new ArrayList<Intimation>();

		Root<Intimation> root = criteriaQuery.from(Intimation.class);

		List<Predicate> conditionList = new ArrayList<Predicate>();
		if (intimationNo != null) {
			Predicate condition1 = criteriaBuilder.equal(
					root.<String> get("intimationId"), intimationNo);
			conditionList.add(condition1);
		}
		try {
			if (intimationNo != null) {
				criteriaQuery.select(root).where(
						criteriaBuilder.and(conditionList
								.toArray(new Predicate[] {})));
				final TypedQuery<Intimation> typedQuery = entityManager
						.createQuery(criteriaQuery);
				intimationsList = typedQuery.getResultList();
			}

			for (Intimation inti : intimationsList) {
				System.out.println(inti.getIntimationId()
						+ "oooooooooooooooooooooooooo"
						+ inti.getPolicy().getPolicyNumber() + "tttttttttttttt"
						+ inti.getCreatedBy());
			}
			List<Intimation> doList = intimationsList;
			List<SearchProcessClaimRequestZonalTableDTO> tableDTO = SearchProcessClaimRequestZonalMapper.getInstance()
					.getIntimationDTO(doList);
			for (SearchProcessClaimRequestZonalTableDTO searchProcessClaimRequestZonalTableDTO : tableDTO) {
				
				Claim claimByKey = getClaimByIntimation(searchProcessClaimRequestZonalTableDTO.getIntimationKey());
				if(searchProcessClaimRequestZonalTableDTO.getIntimationNo() != null){
					Intimation intimationByNo = getIntimationByNo(searchProcessClaimRequestZonalTableDTO.getIntimationNo());
					if(ReferenceTable.getGMCProductList().containsKey(intimationByNo.getPolicy().getProduct().getKey())){
						String colorCodeForGMC = getColorCodeForGMC(intimationByNo.getPolicy().getPolicyNumber(), intimationByNo.getInsured().getInsuredId().toString());
						searchProcessClaimRequestZonalTableDTO.setColorCode(colorCodeForGMC);
					}
				}
				searchProcessClaimRequestZonalTableDTO.setCrmFlagged(claimByKey.getCrcFlag());
				/*if(searchProcessClaimRequestZonalTableDTO.getCrmFlagged() != null){
					if(searchProcessClaimRequestZonalTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
						searchProcessClaimRequestZonalTableDTO.setColorCodeCell("OLIVE");
						searchProcessClaimRequestZonalTableDTO.setCrmFlagged(null);
						//objSearchProcessPreAuthTableDTO.setCrmFlagged(FontAwesome.ADJUST.getHtml());
					}
				}*/
				
				searchProcessClaimRequestZonalTableDTO.setUsername(userName);
				
				
				
				if(claimByKey != null) {
					searchProcessClaimRequestZonalTableDTO.setCoverCode(claimByKey.getClaimCoverCode());
					searchProcessClaimRequestZonalTableDTO.setSubCoverCode(claimByKey.getClaimSubCoverCode());
					searchProcessClaimRequestZonalTableDTO.setSectionCode(claimByKey.getClaimSectionCode());
					
					if(searchProcessClaimRequestZonalTableDTO.getCrmFlagged() != null){
						if(searchProcessClaimRequestZonalTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
							if (claimByKey.getCrcFlag() != null && claimByKey.getCrcFlag().equalsIgnoreCase("Y")) {
								searchProcessClaimRequestZonalTableDTO.setColorCodeCell("OLIVE");
							}
							searchProcessClaimRequestZonalTableDTO.setCrmFlagged(null);
						}
					}
					if (claimByKey.getIsVipCustomer() != null && claimByKey.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
						searchProcessClaimRequestZonalTableDTO.setColorCodeCell("VIP");
					}
					if (claimByKey.getCrcFlag() != null && claimByKey.getCrcFlag().equalsIgnoreCase("Y") 
							&& claimByKey.getIsVipCustomer() != null && claimByKey.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
						searchProcessClaimRequestZonalTableDTO.setColorCodeCell("CRMVIP");
					}
					
				}
			}
			// tableDTO =
			// SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));

			// if (humanTask.getPayload().getClaimRequest() != null) {
			// ClaimRequestType claim = humanTask.getPayload()
			// .getClaimRequest();
			//
			// for (SearchProcessClaimRequestZonalTableDTO
			// searchProcessClaimRequestZonalTableDTO : tableDTO) {
			// searchProcessClaimRequestZonalTableDTO.setClaimKey(claim
			// .getKey());
			// }
			// }
			tableDTO = getclaimNumber(tableDTO);

			tableDTO = getHospitalDetails(tableDTO, rodKey/*, humanTask,taskNumber*/);

			return tableDTO;
		} catch (Exception e) {
			return null;
		}
	}

	private Comparator<SearchProcessClaimRequestZonalTableDTO> getComparator(){
		
		class SortableZMRSearchTableDto extends SearchProcessClaimRequestZonalTableDTO implements Comparator<SearchProcessClaimRequestZonalTableDTO> {

			@Override
			public int compare(SearchProcessClaimRequestZonalTableDTO o1,
					SearchProcessClaimRequestZonalTableDTO o2) {
				
				String[] o1ageSplit = o1.getRodAgeing().split(" ");
				
				int age1 = Integer.valueOf(o1ageSplit[0]);
				
				String[] o2ageSplit = o2.getRodAgeing().split(" ");
				
				int age2 = Integer.valueOf(o2ageSplit[0]);
				
				return age1 - age2 >= 0 ? -1 : 1;
				
			}
			
		}
		return new SortableZMRSearchTableDto();
		
	}
	
	
	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}

	protected Map<String, Reimbursement> getPreviousLatestROD(Long claimKey,
			Reimbursement currentReimbursement) {
		Reimbursement previousROD = null;
		Reimbursement hospitalizationROD = null;
		Map<String, Reimbursement> reimbursements = new HashMap<String, Reimbursement>();
		String[] split = currentReimbursement.getRodNumber().split("/");
		String defaultNumber = split[split.length - 1];
		Integer nextRODNo = Integer.valueOf(defaultNumber);
		Integer previousNumber = nextRODNo - 1;
		List<Reimbursement> previousRODByClaimKey = reimbursementService
				.getPreviousRODByClaimKey(claimKey);
		if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()) {
			for (Reimbursement reimbursement : previousRODByClaimKey) {
				String[] eachSplit = reimbursement.getRodNumber().split("/");
				String eachSplitNo = eachSplit[eachSplit.length - 1];
				Integer eachRODNo = Integer.valueOf(eachSplitNo);
				if(eachRODNo.equals(1)) {
					hospitalizationROD = reimbursement;
					reimbursements.put("hospitalization", hospitalizationROD);
				}
				if (previousNumber.equals(eachRODNo)) {
					previousROD = reimbursement;
					reimbursements.put("previous", previousROD);
				}
			}
		}

		return reimbursements;
	}
	
	private List<SearchProcessClaimRequestZonalTableDTO> getHospitalDetails(
			List<SearchProcessClaimRequestZonalTableDTO> tableDTO, Long rodKey/*,
			HumanTask humanTask,Integer taskNumber*/) {
		Hospitals hospitalDetail;
		for (int index = 0; index < tableDTO.size(); index++) {

			//tableDTO.get(index).setHumanTaskListDTO(humanTask);
			tableDTO.get(index).setRodKey(rodKey);
			
			Object workflowKey = workFlowMap.get(tableDTO.get(index).getRodKey());
//			tableDto.setDbOutArray(workflowKey);
			tableDTO.get(index).setDbOutArray(workflowKey);
			
		//	tableDTO.get(index).setTaskNumber(taskNumber);
			
			try{
				if(rodKey != null){
					Reimbursement reimbursementByKey = getReimbursementByKey(rodKey);
					if(reimbursementByKey != null){
						 if(reimbursementByKey.getDocAcknowLedgement() != null && reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId() != null
								 && reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue() != null){
							 tableDTO.get(index).setDocumentReceivedFrom(reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
						 }
						 String rodAgeing = SHAUtils.getRodAgeing(reimbursementByKey.getCreatedDate());
						 tableDTO.get(index).setRodAgeing(rodAgeing);
					}
				   
				    
				}
			}catch(Exception e){
				
			}
			
			tableDTO.get(index).setBalanceSI(
					getBalanceSI(tableDTO.get(index).getPolicyKey(), tableDTO
							.get(index).getInsuredKey(), tableDTO.get(index)
							.getClaimKey(), rodKey));
			String type = null != getType(tableDTO.get(index).getRodKey()) ? getType(
					tableDTO.get(index).getRodKey()).getStatus()
					.getProcessValue() : "";
			/*double claimedAmt = null != getClaimedAmount(tableDTO.get(index).getRodKey()) ? null != getClaimedAmount(
					tableDTO.get(index).getRodKey()).getCurrentProvisionAmt() ? getType(
					tableDTO.get(index).getRodKey()).getCurrentProvisionAmt()
					: 0.0
					: 0.0;*/
			tableDTO.get(index).setClaimedAmt(getClaimedAmount(tableDTO.get(index).getRodKey()));
			tableDTO.get(index).setType(type);

			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key",
					tableDTO.get(index).getHospitalNameID());
			try {
				hospitalDetail = (Hospitals) findByHospitalKey
						.getSingleResult();
				if (hospitalDetail != null) {

					tableDTO.get(index).setHospitalName(
							hospitalDetail.getName());
//					tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
//					String cpuName = null != getTmpCPUName(tableDTO.get(index)
//							.getCpuId()) ? getTmpCPUName(
//							tableDTO.get(index).getCpuId()).getDescription()
//							: "";
//					tableDTO.get(index).setCpuName(cpuName);

				}
			} catch (Exception e) {
				continue;
			}
			
			
		}

		return tableDTO;
	}

	/*private TmpCPUCode getTmpCPUName(Long cpuId) {
		try {
			Query findCpuCode = entityManager.createNamedQuery(
					"TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
			TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
			return tmpCPUCode;
		} catch (Exception e) {

		}
		return null;
	}*/

	/*private Double getSumInsured(Long insuredKey, Long policyKey) {
		try {
			Query findInsuredKey = entityManager.createNamedQuery(
					"Insured.findByInsured").setParameter("key", insuredKey);
			Insured insured = (Insured) findInsuredKey.getSingleResult();
			return dbCalculationService.getInsuredSumInsured(
					String.valueOf(insured.getInsuredId()), policyKey);
		} catch (Exception e) {
			return null;
		}

	}*/

	private Double getBalanceSI(Long policyKey, Long insuredKey, Long claimKey,Long rodKey){
		Double balanceSI = 0.0;
		if(policyKey !=null && insuredKey != null){
			Map<String, Double> balanceSIForReimbursement = dbCalculationService.getBalanceSIForReimbursement(policyKey, insuredKey, claimKey, rodKey);
			if(null != balanceSIForReimbursement && !balanceSIForReimbursement.isEmpty()){
				balanceSI = balanceSIForReimbursement.get(SHAConstants.TOTAL_BALANCE_SI);
			}
			//balance SI procedure is been called twice in below code.
			/*balanceSI = dbCalculationService.getBalanceSIForReimbursement(
					policyKey, insuredKey, claimKey, rodKey).get(
					SHAConstants.TOTAL_BALANCE_SI) != null ? dbCalculationService
					.getBalanceSIForReimbursement(policyKey, insuredKey,
							claimKey, rodKey)
					.get(SHAConstants.TOTAL_BALANCE_SI) : 0.0;*/
		}
				return balanceSI;
		
	}

	private List<SearchProcessClaimRequestZonalTableDTO> getclaimNumber(
			List<SearchProcessClaimRequestZonalTableDTO> intimationList) {
		Claim a_claim = null;
		for (int index = 0; index < intimationList.size(); index++) {
			System.out.println("Intimationkey+++++++++++++++++++++"
					+ intimationList.get(index).getIntimationKey());

			if (intimationList.get(index).getIntimationKey() != null) {

				Query findByIntimationKey = entityManager
						.createNamedQuery("Claim.findByIntimationKey");
				findByIntimationKey = findByIntimationKey.setParameter(
						"intimationKey", intimationList.get(index)
								.getIntimationKey());
				try {
					a_claim = (Claim) findByIntimationKey.getSingleResult();
					if (a_claim != null) {
						intimationList.get(index).setClaimKey(a_claim.getKey());
						intimationList.get(index).setClaimType(
								a_claim.getClaimType().getValue());
						/*intimationList.get(index).setClaimedAmt(
								a_claim.getClaimedAmount() != null ? a_claim
										.getClaimedAmount() : 0);*/
					}
				} catch (Exception e) {
					continue;
				}
			}
		}
		return intimationList;
	}

	private Reimbursement getType(Long key) {
		try {
			Query findType = entityManager.createNamedQuery(
					"Reimbursement.findByKey").setParameter("primaryKey", key);
			Reimbursement reimbursement = (Reimbursement) findType
					.getSingleResult();
			return reimbursement;
		} catch (Exception e) {

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
	public Reimbursement getReimbursementByKey(Long rodKey) {
		Query query = entityManager.createNamedQuery("Reimbursement.findByKey")
				.setParameter("primaryKey", rodKey);
		List<Reimbursement> rodList = query.getResultList();

		if (rodList != null && !rodList.isEmpty()) {
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
		} 
		else {
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