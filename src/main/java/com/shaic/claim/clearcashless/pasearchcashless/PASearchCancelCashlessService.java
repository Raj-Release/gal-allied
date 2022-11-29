
package com.shaic.claim.clearcashless.pasearchcashless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.clearcashless.dto.SearchClearCashlessDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
@Stateless
public class PASearchCancelCashlessService extends AbstractDAO<Intimation> {
	

	private IntimationService intimationService;
	@EJB
	private ClaimService claimService;
	
	public PASearchCancelCashlessService(){
		
	}
	
	
	public  Page<SearchClearCashlessDTO> searchForCancelCashless(
			PASearchCancelCashlessFormDTO formDTO,
			String userName, String passWord, IntimationService intimationService) {
		this.intimationService = intimationService;
		String intimationNo = formDTO.getIntimationNo() != null ? formDTO.getIntimationNo()!= null ? formDTO.getIntimationNo() : null : null; ;
		String policyNo = formDTO.getPolicyNo() != null ? formDTO.getPolicyNo()!= null ? formDTO.getPolicyNo() : null : null; ;;
		
		Page<SearchClearCashlessDTO> page = new Page<SearchClearCashlessDTO>();
		
		
		
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		
		mapValues.put(SHAConstants.USER_ID, userName);
		
		mapValues.put(SHAConstants.LOB, SHAConstants.PA_LOB);
		if(intimationNo!=null){
			Claim claimObi= claimService.getClaimsByIntimationNumber(intimationNo);
			if(claimObi!=null){
				if(claimObi.getProcessClaimType()!=null && claimObi.getProcessClaimType().equalsIgnoreCase(SHAConstants.PA_LOB_TYPE)){
					mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.PA_LOB_TYPE);
				}else{
					mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.PA_CASHLESS_LOB_TYPE);
				}
			}/*else{
				mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.PA_CASHLESS_LOB_TYPE);
			}*/
		}
		/*mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.PA_LOB_TYPE);
		mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.PA_CASHLESS_LOB_TYPE);*/
		
		

		if(null != intimationNo && !intimationNo.isEmpty()) {
//			IntimationType intimationType = new IntimationType();
//			intimationType.setIntimationNumber(intimationNo);
//			payloadBOType.setIntimation(intimationType);
			
			mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);
		}
			
		if(null != policyNo && !policyNo.isEmpty()) {
//			PolicyType policyType = new PolicyType();
//			policyNo= formDTO.getPolicyNo();
//			policyType.setPolicyId(policyNo);
//			payloadBOType.setPolicy(policyType);
			
			mapValues.put(SHAConstants.POLICY_NUMBER, policyNo);
			
		}
			
		Pageable pageable = formDTO.getPageable();
		pageable.setPageNumber(1);
		
		pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
		
		Map<Long, Map<String, Object>> humanTaskMap = new HashMap<Long, Map<String, Object>>();
//		Map<Long, Integer>	taskNumberMap = new HashMap<Long, Integer>();
		
		
		
		List<SearchClearCashlessDTO> commonList = new ArrayList<SearchClearCashlessDTO>();
//		List<HumanTask> commonPreauthEnhHumanTasksList = new ArrayList<HumanTask>();
//		List<HumanTask> commonFirstLevelPreauthHumanTasksList = new ArrayList<HumanTask>();
//		List<HumanTask> commonFirstLevelEnhancementHumanTasksList = new ArrayList<HumanTask>();
		
		List<Map<String, Object>> commonPreauthEnhList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> commonFirstLevelPreauthList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> commonFirstLevelEnhancementList = new ArrayList<Map<String,Object>>();
		
		
		//############### GETTING PREMEDICAL TASKS #################################
//		com.shaic.ims.bpm.claim.servicev2.preauth.search.PreMedicalPreAuthTask preMedicalClaimTask = BPMClientContext.getProcessPreMedicalTask(userName,passWord);
//		com.shaic.ims.bpm.claim.corev2.PagedTaskList premedicalTasks =  preMedicalClaimTask.getTasks(userName, pageable,  payloadBOType);  
//		if(null != premedicalTasks) {
//			commonFirstLevelPreauthHumanTasksList.addAll(premedicalTasks.getHumanTasks());
//		}
		
		mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.FLP_CURRENT_QUEUE);
		
		//Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
		Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		//commonFirstLevelPreauthList = dbCalculationService.getTaskProcedure(setMapValues);
		commonFirstLevelPreauthList = dbCalculationService.GetTaskProcedureForClearCashless(setMapValues);
		
		
		
		if(intimationNo == null || (commonFirstLevelEnhancementList.isEmpty() && commonPreauthEnhList.isEmpty() && commonFirstLevelPreauthList.isEmpty())) {
			//############### GETTING PREAUTH TASKS #################################
//			com.shaic.ims.bpm.claim.servicev2.preauth.search.PreAuthTask preAuthTask = BPMClientContext.getProcessPreAuthTask(userName,passWord);
//			com.shaic.ims.bpm.claim.corev2.PagedTaskList preauthTasks = preAuthTask.getTasks(userName, pageable, payloadBOType);
//		    
//			if(null != preauthTasks) {
//				commonPreauthEnhHumanTasksList.addAll(preauthTasks.getHumanTasks());
//			}
		
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.PP_CURRENT_QUEUE);
			//Object[] setMapValues1 = SHAUtils.setObjArrayForGetTask(mapValues);
			Object[] setMapValues1 = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
			//commonPreauthEnhList = dbCalculationService.getTaskProcedure(setMapValues1);
			commonPreauthEnhList = dbCalculationService.GetTaskProcedureForClearCashless(setMapValues1);
			
		}
		
		if(intimationNo == null || (commonFirstLevelEnhancementList.isEmpty() && commonPreauthEnhList.isEmpty() && commonFirstLevelPreauthList.isEmpty())) {
			//############### GETTING PREMED ENH TASKS #################################
//			PreMedicalPreAuthEnhTask processPreMedicalEnhTask = BPMClientContext.getProcessPreMedicalEnhancementTask(userName,passWord);
//			PagedTaskList premedicalEnhTasks =  processPreMedicalEnhTask.getTasks(userName, pageable, payloadBOType);
//			if(null != premedicalEnhTasks) {
//				commonFirstLevelEnhancementHumanTasksList.addAll(premedicalEnhTasks.getHumanTasks());
			
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.FLE_CURRENT_QUEUE);
			//Object[] setMapValues1 = SHAUtils.setObjArrayForGetTask(mapValues);
			Object[] setMapValues1 = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
			//commonFirstLevelEnhancementList = dbCalculationService.getTaskProcedure(setMapValues1);	
			commonFirstLevelEnhancementList = dbCalculationService.GetTaskProcedureForClearCashless(setMapValues1);	
//			}
		}
		
		if(intimationNo == null || (commonFirstLevelEnhancementList.isEmpty() && commonPreauthEnhList.isEmpty() && commonFirstLevelPreauthList.isEmpty())) {
			//############### GETTING PREAUTH ENH TASKS #################################
//			com.shaic.ims.bpm.claim.servicev2.preauth.search.PreAuthEnhTask preAuthEnhTask = BPMClientContext.getProcessEnhancementTask(userName,passWord);
//			com.shaic.ims.bpm.claim.corev2.PagedTaskList preauthEnhTasks = preAuthEnhTask.getTasks(userName, pageable, payloadBOType); //userName="zma1"
//			
//			if (null != preauthEnhTasks) {
//				commonPreauthEnhHumanTasksList.addAll(preauthEnhTasks.getHumanTasks());
//			}
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.PE_CURRENT_QUEUE);
			//Object[] setMapValues1 = SHAUtils.setObjArrayForGetTask(mapValues);
			Object[] setMapValues1 = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
			
			//commonPreauthEnhList = dbCalculationService.getTaskProcedure(setMapValues1);
			commonPreauthEnhList = dbCalculationService.GetTaskProcedureForClearCashless(setMapValues1);
			
		}

		DBCalculationService dbCalcService = new DBCalculationService();
		for (Map<String, Object> outPutArray : commonPreauthEnhList) {
//			PayloadBOType payloadBOCashless = humanTask.getPayloadCashless();
//			PreAuthReqType preauthReq = payloadBOCashless.getPreAuthReq(); 
			Long keyValue = (Long) outPutArray.get(SHAConstants.CASHLESS_KEY);
			
			humanTaskMap.put(keyValue, outPutArray);
			
			
			if(keyValue != null) {
				List<Preauth> preauthByKey = getPreauthByKey(keyValue);
				if(preauthByKey != null && !preauthByKey.isEmpty()) {
					Preauth preauth = preauthByKey.get(0);
					if(preauth.getEnhancementType() != null) {
						List<Preauth> preauthByClaimnKey = getPreauthByClaimnKey(preauth.getClaim().getKey());
						if(preauthByClaimnKey != null && !preauthByClaimnKey.isEmpty()) {
							Preauth preauth2 = preauthByClaimnKey.get(0);
							SearchClearCashlessDTO filledDTO = getFilledDTO(preauth.getClaim().getIntimation(), preauth.getClaim(), preauth2, dbCalcService, preauth.getKey(), userName, passWord);
							filledDTO.setDbOutArray(outPutArray);
							
							filledDTO.setIsEnhancement(true);
							if(preauth.getStage().getKey().equals(ReferenceTable.PREAUTH_STAGE)){
								filledDTO.setIsEnhancement(false);
							}
							
							if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_QUERY_RECEIVED_STATUS))
									|| (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_QUERY_STATUS))) {
								filledDTO.setIsQueryReplyReceived(true);
							}
							
							if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS) )) {
								filledDTO.setIsReconsideration(true);
							}
							
							commonList.add(filledDTO);
						}
					} else {
						SearchClearCashlessDTO filledDTO = getFilledDTO(preauth.getClaim().getIntimation(), preauth.getClaim(), null, dbCalcService, preauth.getKey(), userName, passWord);
						filledDTO.setDbOutArray(outPutArray);
						
						if(preauth.getStage().getKey().equals(ReferenceTable.PREAUTH_STAGE)){
							filledDTO.setIsEnhancement(false);
						}
						
						if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_QUERY_RECEIVED_STATUS))
								|| (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_QUERY_STATUS))) {
							filledDTO.setIsQueryReplyReceived(true);
						}
						
						if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS) )) {
							filledDTO.setIsReconsideration(true);
						}
						if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_QUERY_RECEIVED_STATUS))) {
							filledDTO.setIsQueryReplyReceived(true);
						}
						commonList.add(filledDTO);
					}
				}
			}
		}
		
		
		for (Map<String, Object> outPutArray : commonFirstLevelPreauthList) {
			
//			PayloadBOType payloadBOCashless = humanTask.getPayloadCashless();
//			PreAuthReqType preauthReq = payloadBOCashless.getPreAuthReq(); 
			
//			Long cashlessKey = (Long) outPutArray.get(SHAConstants.CASHLESS_KEY);
			Long claimKey = (Long) outPutArray.get(SHAConstants.DB_CLAIM_KEY);
			String previousOutcome = (String)outPutArray.get(SHAConstants.OUTCOME);
			
			if(claimKey != null) {
				if(previousOutcome != null && (previousOutcome.equalsIgnoreCase(SHAConstants.OUTCOME_FOR_QUERY_REPLY_FLP))) {
					List<Preauth> preauthByClaimnKeyList = getPreauthByClaimnKey(claimKey);
					Preauth preauthByKey = getPreauth(preauthByClaimnKeyList);
					if(preauthByKey != null ) {
						Claim claimByKey = preauthByKey.getClaim();
						SearchClearCashlessDTO filledDTO = getFilledDTO(claimByKey.getIntimation(), claimByKey, null, dbCalcService, preauthByKey.getKey(), userName, passWord);
						filledDTO.setDbOutArray(outPutArray);
						filledDTO.setIsFirstLevelQueryReplyReceived(true);
						commonList.add(filledDTO);
					} else {
						Claim claimByKey = getClaimByKey(claimKey);
						SearchClearCashlessDTO filledDTO = getFilledDTO(claimByKey.getIntimation(), claimByKey, null, dbCalcService, null, userName, passWord);
						filledDTO.setDbOutArray(outPutArray);
						commonList.add(filledDTO);
					}
					
				} else {
					Claim claimByKey = getClaimByKey(claimKey);
					SearchClearCashlessDTO filledDTO = getFilledDTO(claimByKey.getIntimation(), claimByKey, null, dbCalcService, null, userName, passWord);
					filledDTO.setDbOutArray(outPutArray);
					commonList.add(filledDTO);
				}
				
//				if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_QUERY_RECEIVED_STATUS))) {
//					filledDTO.setIsQueryReplyReceived(true);
//				}
				
			}
		}
		
		for (Map<String, Object> outPutArray : commonFirstLevelEnhancementList) {
			
//			PayloadBOType payloadBOCashless = humanTask.getPayloadCashless();
//			PreAuthReqType preauthReq = payloadBOCashless.getPreAuthReq(); 
			Long claimKey = (Long) outPutArray.get(SHAConstants.DB_CLAIM_KEY);
			String previousOutcome = (String)outPutArray.get(SHAConstants.OUTCOME);
			
			if(claimKey != null) {
				if(previousOutcome != null && (previousOutcome.equalsIgnoreCase(SHAConstants.OUTCOME_FOR_QUERY_REPLY_FLE) || previousOutcome.equalsIgnoreCase("TRANSLATEENH"))) {
					List<Preauth> preauthByClaimnKeyList = getPreauthByClaimnKey(claimKey);
					Preauth preauthByKey = getPreauth(preauthByClaimnKeyList);
					if(preauthByKey != null) {
						Claim claimByKey = preauthByKey.getClaim();
						List<Preauth> preauthByClaimnKey = getPreauthByClaimnKey(claimByKey.getKey());
						if(preauthByClaimnKey != null && !preauthByClaimnKey.isEmpty()) {
							Preauth preauth2 = preauthByClaimnKey.get(0);
							SearchClearCashlessDTO filledDTO = getFilledDTO(claimByKey.getIntimation(), claimByKey, preauth2, dbCalcService, preauthByKey.getKey(), userName, passWord);
							filledDTO.setDbOutArray(outPutArray);
							filledDTO.setIsEnhancement(true);
							filledDTO.setIsFirstLevelQueryReplyReceived(true);
							commonList.add(filledDTO);
						}
					} else {
						Claim claimByKey = getClaimByKey(claimKey);
						List<Preauth> preauthByClaimnKey = getPreauthByClaimnKey(claimByKey.getKey());
						if(preauthByClaimnKey != null && !preauthByClaimnKey.isEmpty()) {
							Preauth preauth2 = preauthByClaimnKey.get(0);
							SearchClearCashlessDTO filledDTO = getFilledDTO(claimByKey.getIntimation(), claimByKey, preauth2, dbCalcService, null, userName, passWord);
							filledDTO.setDbOutArray(outPutArray);
							filledDTO.setIsEnhancement(true);
							commonList.add(filledDTO);
						}
					}
					
				} else {
					Claim claimByKey = getClaimByKey(claimKey);
					List<Preauth> preauthByClaimnKey = getPreauthByClaimnKey(claimByKey.getKey());
					if(preauthByClaimnKey != null && !preauthByClaimnKey.isEmpty()) {
						Preauth preauth2 = preauthByClaimnKey.get(0);
						SearchClearCashlessDTO filledDTO = getFilledDTO(claimByKey.getIntimation(), claimByKey, preauth2, dbCalcService, null, userName, passWord);
						filledDTO.setDbOutArray(outPutArray);
						filledDTO.setIsEnhancement(true);
						commonList.add(filledDTO);
					}
				}
				
			}
		}

		
		
		page.setPageItems(commonList);
		return page;
	}
	
	/*public  Page<SearchClearCashlessDTO> searchForCancelCashlessBPMN(
			SearchCancelCashlessFormDTO formDTO,
			String userName, String passWord, IntimationService intimationService) {
		this.intimationService = intimationService;
		String intimationNo = formDTO.getIntimationNo() != null ? formDTO.getIntimationNo()!= null ? formDTO.getIntimationNo() : null : null; ;
		String policyNo = formDTO.getPolicyNo() != null ? formDTO.getPolicyNo()!= null ? formDTO.getPolicyNo() : null : null; ;;
		
		Page<SearchClearCashlessDTO> page = new Page<SearchClearCashlessDTO>();
		
		List<HumanTask> humanTaskListDTO = new ArrayList<HumanTask>();
		PayloadBOType payloadBOType =  new PayloadBOType();
		
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();

		if(null != intimationNo && !intimationNo.isEmpty()) {
			IntimationType intimationType = new IntimationType();
			intimationType.setIntimationNumber(intimationNo);
			payloadBOType.setIntimation(intimationType);
		}
			
		if(null != policyNo && !policyNo.isEmpty()) {
			PolicyType policyType = new PolicyType();
			policyNo= formDTO.getPolicyNo();
			policyType.setPolicyId(policyNo);
			payloadBOType.setPolicy(policyType);
		}
			
		Pageable pageable = formDTO.getPageable();
		pageable.setPageNumber(1);
		
		pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
		
		Map<Long, HumanTask> humanTaskMap = new HashMap<Long, HumanTask>();
		Map<Long, Integer>	taskNumberMap = new HashMap<Long, Integer>();
		
		
		
		List<SearchClearCashlessDTO> commonList = new ArrayList<SearchClearCashlessDTO>();
		List<HumanTask> commonPreauthEnhHumanTasksList = new ArrayList<HumanTask>();
		List<HumanTask> commonFirstLevelPreauthHumanTasksList = new ArrayList<HumanTask>();
		List<HumanTask> commonFirstLevelEnhancementHumanTasksList = new ArrayList<HumanTask>();
		
		
		//############### GETTING PREMEDICAL TASKS #################################
		com.shaic.ims.bpm.claim.servicev2.preauth.search.PreMedicalPreAuthTask preMedicalClaimTask = BPMClientContext.getProcessPreMedicalTask(userName,passWord);
		com.shaic.ims.bpm.claim.corev2.PagedTaskList premedicalTasks =  preMedicalClaimTask.getTasks(userName, pageable,  payloadBOType);  
		if(null != premedicalTasks) {
			commonFirstLevelPreauthHumanTasksList.addAll(premedicalTasks.getHumanTasks());
		}
		
		if(intimationNo == null || (commonFirstLevelEnhancementHumanTasksList.isEmpty() && commonPreauthEnhHumanTasksList.isEmpty() && commonFirstLevelPreauthHumanTasksList.isEmpty())) {
			//############### GETTING PREAUTH TASKS #################################
			com.shaic.ims.bpm.claim.servicev2.preauth.search.PreAuthTask preAuthTask = BPMClientContext.getProcessPreAuthTask(userName,passWord);
			com.shaic.ims.bpm.claim.corev2.PagedTaskList preauthTasks = preAuthTask.getTasks(userName, pageable, payloadBOType);
		    
			if(null != preauthTasks) {
				commonPreauthEnhHumanTasksList.addAll(preauthTasks.getHumanTasks());
			}
		}
		
		if(intimationNo == null || (commonFirstLevelEnhancementHumanTasksList.isEmpty() && commonPreauthEnhHumanTasksList.isEmpty() && commonFirstLevelPreauthHumanTasksList.isEmpty())) {
			//############### GETTING PREMED ENH TASKS #################################
			PreMedicalPreAuthEnhTask processPreMedicalEnhTask = BPMClientContext.getProcessPreMedicalEnhancementTask(userName,passWord);
			PagedTaskList premedicalEnhTasks =  processPreMedicalEnhTask.getTasks(userName, pageable, payloadBOType);
			if(null != premedicalEnhTasks) {
				commonFirstLevelEnhancementHumanTasksList.addAll(premedicalEnhTasks.getHumanTasks());
			}
		}
		
		if(intimationNo == null || (commonFirstLevelEnhancementHumanTasksList.isEmpty() && commonPreauthEnhHumanTasksList.isEmpty() && commonFirstLevelPreauthHumanTasksList.isEmpty())) {
			//############### GETTING PREAUTH ENH TASKS #################################
			com.shaic.ims.bpm.claim.servicev2.preauth.search.PreAuthEnhTask preAuthEnhTask = BPMClientContext.getProcessEnhancementTask(userName,passWord);
			com.shaic.ims.bpm.claim.corev2.PagedTaskList preauthEnhTasks = preAuthEnhTask.getTasks(userName, pageable, payloadBOType); //userName="zma1"
			
			if (null != preauthEnhTasks) {
				commonPreauthEnhHumanTasksList.addAll(preauthEnhTasks.getHumanTasks());
			}
		}

		DBCalculationService dbCalcService = new DBCalculationService();
		for (HumanTask humanTask : commonPreauthEnhHumanTasksList) {
			PayloadBOType payloadBOCashless = humanTask.getPayloadCashless();
			PreAuthReqType preauthReq = payloadBOCashless.getPreAuthReq(); 
			humanTaskMap.put(preauthReq.getKey(), humanTask);
			taskNumberMap.put(preauthReq.getKey(),humanTask.getNumber());
			
			if(preauthReq != null) {
				List<Preauth> preauthByKey = getPreauthByKey(preauthReq.getKey());
				if(preauthByKey != null && !preauthByKey.isEmpty()) {
					Preauth preauth = preauthByKey.get(0);
					if(preauth.getEnhancementType() != null) {
						List<Preauth> preauthByClaimnKey = getPreauthByClaimnKey(preauth.getClaim().getKey());
						if(preauthByClaimnKey != null && !preauthByClaimnKey.isEmpty()) {
							Preauth preauth2 = preauthByClaimnKey.get(0);
							SearchClearCashlessDTO filledDTO = getFilledDTO(preauth.getClaim().getIntimation(), preauth.getClaim(), preauth2, dbCalcService, preauth.getKey(), userName, passWord, humanTask);
							filledDTO.setIsEnhancement(true);    
							if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_QUERY_RECEIVED_STATUS))) {
								filledDTO.setIsQueryReplyReceived(true);
							}
							
							if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS) )) {
								filledDTO.setIsReconsideration(true);
							}
							
							commonList.add(filledDTO);
						}
					} else {
						SearchClearCashlessDTO filledDTO = getFilledDTO(preauth.getClaim().getIntimation(), preauth.getClaim(), null, dbCalcService, preauth.getKey(), userName, passWord, humanTask);
						if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS) )) {
							filledDTO.setIsReconsideration(true);
						}
						if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_QUERY_RECEIVED_STATUS))) {
							filledDTO.setIsQueryReplyReceived(true);
						}
						commonList.add(filledDTO);
					}
				}
			}
		}
		
		
		for (HumanTask humanTask : commonFirstLevelPreauthHumanTasksList) {
			PayloadBOType payloadBOCashless = humanTask.getPayloadCashless();
			PreAuthReqType preauthReq = payloadBOCashless.getPreAuthReq(); 
			if(preauthReq != null) {
				if(preauthReq.getOutcome() != null && (preauthReq.getOutcome().equalsIgnoreCase("PREMEDICALQUERY") || preauthReq.getOutcome().equalsIgnoreCase("TRANSLATE"))) {
					List<Preauth> preauthByClaimnKeyList = getPreauthByClaimnKey(preauthReq.getKey());
					Preauth preauthByKey = getPreauth(preauthByClaimnKeyList);
					if(preauthByKey != null ) {
						Claim claimByKey = preauthByKey.getClaim();
						SearchClearCashlessDTO filledDTO = getFilledDTO(claimByKey.getIntimation(), claimByKey, null, dbCalcService, preauthByKey.getKey(), userName, passWord, humanTask);
						filledDTO.setIsFirstLevelQueryReplyReceived(true);
						commonList.add(filledDTO);
					} else {
						Claim claimByKey = getClaimByKey(preauthReq.getKey());
						SearchClearCashlessDTO filledDTO = getFilledDTO(claimByKey.getIntimation(), claimByKey, null, dbCalcService, null, userName, passWord, humanTask);
						commonList.add(filledDTO);
					}
					
				} else {
					Claim claimByKey = getClaimByKey(preauthReq.getKey());
					SearchClearCashlessDTO filledDTO = getFilledDTO(claimByKey.getIntimation(), claimByKey, null, dbCalcService, null, userName, passWord, humanTask);
					commonList.add(filledDTO);
				}
				
//				if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_QUERY_RECEIVED_STATUS))) {
//					filledDTO.setIsQueryReplyReceived(true);
//				}
				
			}
		}
		
		for (HumanTask humanTask : commonFirstLevelEnhancementHumanTasksList) {
			PayloadBOType payloadBOCashless = humanTask.getPayloadCashless();
			PreAuthReqType preauthReq = payloadBOCashless.getPreAuthReq(); 
			if(preauthReq != null) {
				if(preauthReq.getOutcome() != null && (preauthReq.getOutcome().equalsIgnoreCase("PREMEDICALQUERYENH") || preauthReq.getOutcome().equalsIgnoreCase("TRANSLATEENH"))) {
					List<Preauth> preauthByClaimnKeyList = getPreauthByClaimnKey(preauthReq.getKey());
					Preauth preauthByKey = getPreauth(preauthByClaimnKeyList);
					if(preauthByKey != null) {
						Claim claimByKey = preauthByKey.getClaim();
						List<Preauth> preauthByClaimnKey = getPreauthByClaimnKey(claimByKey.getKey());
						if(preauthByClaimnKey != null && !preauthByClaimnKey.isEmpty()) {
							Preauth preauth2 = preauthByClaimnKey.get(0);
							SearchClearCashlessDTO filledDTO = getFilledDTO(claimByKey.getIntimation(), claimByKey, preauth2, dbCalcService, preauthByKey.getKey(), userName, passWord, humanTask);
							filledDTO.setIsEnhancement(true);
							filledDTO.setIsFirstLevelQueryReplyReceived(true);
							commonList.add(filledDTO);
						}
					} else {
						Claim claimByKey = getClaimByKey(preauthReq.getKey());
						List<Preauth> preauthByClaimnKey = getPreauthByClaimnKey(claimByKey.getKey());
						if(preauthByClaimnKey != null && !preauthByClaimnKey.isEmpty()) {
							Preauth preauth2 = preauthByClaimnKey.get(0);
							SearchClearCashlessDTO filledDTO = getFilledDTO(claimByKey.getIntimation(), claimByKey, preauth2, dbCalcService, null, userName, passWord, humanTask);
							filledDTO.setIsEnhancement(true);
							commonList.add(filledDTO);
						}
					}
					
				} else {
					Claim claimByKey = getClaimByKey(preauthReq.getKey());
					List<Preauth> preauthByClaimnKey = getPreauthByClaimnKey(claimByKey.getKey());
					if(preauthByClaimnKey != null && !preauthByClaimnKey.isEmpty()) {
						Preauth preauth2 = preauthByClaimnKey.get(0);
						SearchClearCashlessDTO filledDTO = getFilledDTO(claimByKey.getIntimation(), claimByKey, preauth2, dbCalcService, null, userName, passWord, humanTask);
						filledDTO.setIsEnhancement(true);
						commonList.add(filledDTO);
					}
				}
				
			}
		}

		
		
		page.setPageItems(commonList);
		return page;
	}*/
	
	protected Preauth getPreauth(List<Preauth> preauthList) {
		Preauth correctPreauth = null;
		for (Preauth preauth : preauthList) {
			if (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_COORDINATOR_REPLY_RECEIVED_STATUS) ||
					preauth.getStatus().getKey().equals(ReferenceTable.PREMEDICAL_PREAUTH_COORDINATOR_REPLY) ||
					preauth.getStatus().getKey().equals(ReferenceTable.PREMEDICAL_ENHANCEMENT_COORDINATOR_REPLY) ||
					preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_COORDINATOR_REPLY_RECEIVED_STATUS) 
					|| preauth
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_RECEIVED_STATUS) || preauth
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS) || preauth
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS) || preauth
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_COORDINATOR_REPLY_RECEIVED_STATUS) ||
							preauth.getStatus().getKey().equals(ReferenceTable.PREMEDICAL_PREAUTH_COORDINATOR_REPLY) ||
							preauth.getStatus().getKey().equals(ReferenceTable.PREMEDICAL_ENHANCEMENT_COORDINATOR_REPLY) ||
							preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_COORDINATOR_REPLY_RECEIVED_STATUS) || preauth.getStatus().getKey()
									.equals(ReferenceTable.ENHANCEMENT_QUERY_RECEIVED_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_RECEIVED_STATUS)) {
				correctPreauth = preauth;
				break;
			}
		}
		return correctPreauth;
	}
	
	protected SearchClearCashlessDTO getFilledDTO(Intimation intimation, Claim claim, Preauth preauth, DBCalculationService dbCalculationService, Long preauthKey, String userName, String password) {
		SearchClearCashlessDTO dto = new SearchClearCashlessDTO();
		NewIntimationDto intimationDto = intimationService.getIntimationDto(intimation);
		dto.setNewIntimationDTO(intimationDto);
		// newIntimationDto.getPolicy().getProduct().getAutoRestoration()
//		ClaimDto claimDTO = new ClaimMapper().getClaimDto(claim);
		dto.setCpuCode(intimation.getCpuCode() != null ? String.valueOf(intimation.getCpuCode().getCpuCode()) : "" );
		if (preauthKey != null){
			dto.setPreauthKey(preauthKey);
		}
		if(preauth != null && preauth.getTotalApprovalAmount() != null) {
			dto.setPrauthAmount(preauth.getTotalApprovalAmount().longValue());
			
		}
		dto.setClaimKey(claim.getKey());
		dto.setPolicyNo(intimation.getPolicy().getPolicyNumber());
		dto.setIntimationNo(intimation.getIntimationId());
		dto.setInsuredPatientName(intimation.getInsuredPatientName());
		Hospitals hospitalDetail = getHospitalDetail(intimation.getHospital());
		dto.setHospitalName(hospitalDetail.getName());
		dto.setDateOfAdmission(intimation.getAdmissionDate());
		dto.setProductName(intimation.getPolicy().getProductName());
		Map<String, Double> balanceSI2 = dbCalculationService.getBalanceSI(intimationDto.getPolicy().getKey(), intimationDto.getInsuredPatient().getKey(), claim.getKey(), intimationDto.getOrginalSI(), intimationDto.getKey());
		dto.setBsiAmount(balanceSI2.containsKey(SHAConstants.TOTAL_BALANCE_SI) ? balanceSI2.get(SHAConstants.TOTAL_BALANCE_SI).longValue()  : 0l);
		dto.setUsername(userName);
		dto.setPassword(password);
	//	dto.setHumanTask(humanTask);
		return dto;
	}


	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<Preauth> getPreauthByKey(Long preauthKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByKey");
		query.setParameter("preauthKey", preauthKey);
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		return singleResult;
	}
	
	@SuppressWarnings("unchecked")
	public List<Preauth> getPreauthByIntimationKey(Long intimationKey) {
		Query query = entityManager
				.createNamedQuery("Preauth.findByIntimationKey");
		query.setParameter("intimationKey", intimationKey);
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		return singleResult;
	}
	
	@SuppressWarnings("unchecked")
	public List<Preauth> getPreauthByClaimnKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Preauth.findByClaimKey");
		query.setParameter("claimkey", claimKey);
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		return singleResult;
	}

	
	
	
	public Claim getClaimByKey(Long key) {
		Query query = entityManager.createNamedQuery("Claim.findByClaimKey");
		query.setParameter("claimKey", key);
		List<Claim> claim = (List<Claim>)query.getResultList();
		if(claim != null && ! claim.isEmpty()){
			for (Claim claim2 : claim) {
				entityManager.refresh(claim2);
			}
			return claim.get(0);
		}
		return null;
	}
	
	private Hospitals getHospitalDetail(Long hospitalId){

		Query findByHospitalKey = entityManager.createNamedQuery(
				"Hospitals.findByKey").setParameter("key", hospitalId);
		Hospitals hospitalDetail;
		try {
			hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			if(hospitalDetail != null){
				return hospitalDetail;
			}
			
		}
		catch(Exception e) {
			return null;
		}
		
		return null;
	}
	
	
}
/*=======
package com.shaic.claim.clearcashless.searchcashless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.ws.security.util.StringUtil;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.clearcashless.dto.SearchClearCashlessDTO;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.claim.premedical.search.ProcessPreMedicalTableDTO;
import com.shaic.claim.process.premedical.enhancement.search.SearchPreMedicalProcessingEnhancementTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.TmpHospital;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;

import com.shaic.newcode.wizard.domain.NewIntimationMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
@Stateless
public class SearchCancelCashlessService extends AbstractDAO<Intimation> {
	

	private IntimationService intimationService;
	
	public SearchCancelCashlessService(){
		
	}
	
	
	public  Page<SearchClearCashlessDTO> searchForCancelCashless(
			SearchCancelCashlessFormDTO formDTO,
			String userName, String passWord, IntimationService intimationService) {
		this.intimationService = intimationService;
		String intimationNo = formDTO.getIntimationNo() != null ? formDTO.getIntimationNo()!= null ? formDTO.getIntimationNo() : null : null; ;
		String policyNo = formDTO.getPolicyNo() != null ? formDTO.getPolicyNo()!= null ? formDTO.getPolicyNo() : null : null; ;;
		
		Page<SearchClearCashlessDTO> page = new Page<SearchClearCashlessDTO>();
		
		PayloadBOType payloadBOType =  new PayloadBOType();
		
		IntimationType intimationType = new IntimationType();
		
		ProductInfoType productInfo = new ProductInfoType();
		
		
		intimationType.setReason("HEALTH");
		
		productInfo.setLob("H");

		
		payloadBOType.setProductInfo(productInfo);
		
		payloadBOType.setIntimation(intimationType);
		
		
		if(null != intimationNo && !intimationNo.isEmpty()) {
			
			intimationType.setIntimationNumber(intimationNo);
			payloadBOType.setIntimation(intimationType);
		}
			
		if(null != policyNo && !policyNo.isEmpty()) {
			PolicyType policyType = new PolicyType();
			policyNo= formDTO.getPolicyNo();
			policyType.setPolicyId(policyNo);
			payloadBOType.setPolicy(policyType);
		}
			
		Pageable pageable = formDTO.getPageable();
		pageable.setPageNumber(1);
		
		pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
		
		Map<Long, HumanTask> humanTaskMap = new HashMap<Long, HumanTask>();
		Map<Long, Integer>	taskNumberMap = new HashMap<Long, Integer>();
		
		
		
		List<SearchClearCashlessDTO> commonList = new ArrayList<SearchClearCashlessDTO>();
		List<HumanTask> commonPreauthEnhHumanTasksList = new ArrayList<HumanTask>();
		List<HumanTask> commonFirstLevelPreauthHumanTasksList = new ArrayList<HumanTask>();
		List<HumanTask> commonFirstLevelEnhancementHumanTasksList = new ArrayList<HumanTask>();
		
		
		//############### GETTING PREMEDICAL TASKS #################################
		com.shaic.ims.bpm.claim.servicev2.preauth.search.PreMedicalPreAuthTask preMedicalClaimTask = BPMClientContext.getProcessPreMedicalTask(userName,passWord);
		com.shaic.ims.bpm.claim.corev2.PagedTaskList premedicalTasks =  preMedicalClaimTask.getTasks(userName, pageable,  payloadBOType);  
		if(null != premedicalTasks) {
			commonFirstLevelPreauthHumanTasksList.addAll(premedicalTasks.getHumanTasks());
		}
		
		if(intimationNo == null || (commonFirstLevelEnhancementHumanTasksList.isEmpty() && commonPreauthEnhHumanTasksList.isEmpty() && commonFirstLevelPreauthHumanTasksList.isEmpty())) {
			//############### GETTING PREAUTH TASKS #################################
			com.shaic.ims.bpm.claim.servicev2.preauth.search.PreAuthTask preAuthTask = BPMClientContext.getProcessPreAuthTask(userName,passWord);
			com.shaic.ims.bpm.claim.corev2.PagedTaskList preauthTasks = preAuthTask.getTasks(userName, pageable, payloadBOType);
		    
			if(null != preauthTasks) {
				commonPreauthEnhHumanTasksList.addAll(preauthTasks.getHumanTasks());
			}
		}
		
		if(intimationNo == null || (commonFirstLevelEnhancementHumanTasksList.isEmpty() && commonPreauthEnhHumanTasksList.isEmpty() && commonFirstLevelPreauthHumanTasksList.isEmpty())) {
			//############### GETTING PREMED ENH TASKS #################################
			PreMedicalPreAuthEnhTask processPreMedicalEnhTask = BPMClientContext.getProcessPreMedicalEnhancementTask(userName,passWord);
			PagedTaskList premedicalEnhTasks =  processPreMedicalEnhTask.getTasks(userName, pageable, payloadBOType);
			if(null != premedicalEnhTasks) {
				commonFirstLevelEnhancementHumanTasksList.addAll(premedicalEnhTasks.getHumanTasks());
			}
		}
		
		if(intimationNo == null || (commonFirstLevelEnhancementHumanTasksList.isEmpty() && commonPreauthEnhHumanTasksList.isEmpty() && commonFirstLevelPreauthHumanTasksList.isEmpty())) {
			//############### GETTING PREAUTH ENH TASKS #################################
			com.shaic.ims.bpm.claim.servicev2.preauth.search.PreAuthEnhTask preAuthEnhTask = BPMClientContext.getProcessEnhancementTask(userName,passWord);
			com.shaic.ims.bpm.claim.corev2.PagedTaskList preauthEnhTasks = preAuthEnhTask.getTasks(userName, pageable, payloadBOType); //userName="zma1"
			
			if (null != preauthEnhTasks) {
				commonPreauthEnhHumanTasksList.addAll(preauthEnhTasks.getHumanTasks());
			}
		}

		DBCalculationService dbCalcService = new DBCalculationService();
		for (HumanTask humanTask : commonPreauthEnhHumanTasksList) {
			PayloadBOType payloadBOCashless = humanTask.getPayloadCashless();
			PreAuthReqType preauthReq = payloadBOCashless.getPreAuthReq(); 
			humanTaskMap.put(preauthReq.getKey(), humanTask);
			taskNumberMap.put(preauthReq.getKey(),humanTask.getNumber());
			
			if(preauthReq != null) {
				List<Preauth> preauthByKey = getPreauthByKey(preauthReq.getKey());
				if(preauthByKey != null && !preauthByKey.isEmpty()) {
					Preauth preauth = preauthByKey.get(0);
					if(preauth.getEnhancementType() != null) {
						List<Preauth> preauthByClaimnKey = getPreauthByClaimnKey(preauth.getClaim().getKey());
						if(preauthByClaimnKey != null && !preauthByClaimnKey.isEmpty()) {
							Preauth preauth2 = preauthByClaimnKey.get(0);
							SearchClearCashlessDTO filledDTO = getFilledDTO(preauth.getClaim().getIntimation(), preauth.getClaim(), preauth2, dbCalcService, preauth.getKey(), userName, passWord, humanTask);
							filledDTO.setIsEnhancement(true);    
							if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_QUERY_RECEIVED_STATUS))) {
								filledDTO.setIsQueryReplyReceived(true);
							}
							
							if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS) )) {
								filledDTO.setIsReconsideration(true);
							}
							
							commonList.add(filledDTO);
						}
					} else {
						SearchClearCashlessDTO filledDTO = getFilledDTO(preauth.getClaim().getIntimation(), preauth.getClaim(), null, dbCalcService, preauth.getKey(), userName, passWord, humanTask);
						if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS) )) {
							filledDTO.setIsReconsideration(true);
						}
						if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_QUERY_RECEIVED_STATUS))) {
							filledDTO.setIsQueryReplyReceived(true);
						}
						commonList.add(filledDTO);
					}
				}
			}
		}
		
		
		for (HumanTask humanTask : commonFirstLevelPreauthHumanTasksList) {
			PayloadBOType payloadBOCashless = humanTask.getPayloadCashless();
			PreAuthReqType preauthReq = payloadBOCashless.getPreAuthReq(); 
			if(preauthReq != null) {
				if(preauthReq.getOutcome() != null && (preauthReq.getOutcome().equalsIgnoreCase("PREMEDICALQUERY") || preauthReq.getOutcome().equalsIgnoreCase("TRANSLATE"))) {
					List<Preauth> preauthByClaimnKeyList = getPreauthByClaimnKey(preauthReq.getKey());
					Preauth preauthByKey = getPreauth(preauthByClaimnKeyList);
					if(preauthByKey != null ) {
						Claim claimByKey = preauthByKey.getClaim();
						SearchClearCashlessDTO filledDTO = getFilledDTO(claimByKey.getIntimation(), claimByKey, null, dbCalcService, preauthByKey.getKey(), userName, passWord, humanTask);
						filledDTO.setIsFirstLevelQueryReplyReceived(true);
						commonList.add(filledDTO);
					} else {
						Claim claimByKey = getClaimByKey(preauthReq.getKey());
						SearchClearCashlessDTO filledDTO = getFilledDTO(claimByKey.getIntimation(), claimByKey, null, dbCalcService, null, userName, passWord, humanTask);
						commonList.add(filledDTO);
					}
					
				} else {
					Claim claimByKey = getClaimByKey(preauthReq.getKey());
					SearchClearCashlessDTO filledDTO = getFilledDTO(claimByKey.getIntimation(), claimByKey, null, dbCalcService, null, userName, passWord, humanTask);
					commonList.add(filledDTO);
				}
				
//				if(preauth.getStatus() != null && (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_QUERY_RECEIVED_STATUS))) {
//					filledDTO.setIsQueryReplyReceived(true);
//				}
				
			}
		}
		
		for (HumanTask humanTask : commonFirstLevelEnhancementHumanTasksList) {
			PayloadBOType payloadBOCashless = humanTask.getPayloadCashless();
			PreAuthReqType preauthReq = payloadBOCashless.getPreAuthReq(); 
			if(preauthReq != null) {
				if(preauthReq.getOutcome() != null && (preauthReq.getOutcome().equalsIgnoreCase("PREMEDICALQUERYENH") || preauthReq.getOutcome().equalsIgnoreCase("TRANSLATEENH"))) {
					List<Preauth> preauthByClaimnKeyList = getPreauthByClaimnKey(preauthReq.getKey());
					Preauth preauthByKey = getPreauth(preauthByClaimnKeyList);
					if(preauthByKey != null) {
						Claim claimByKey = preauthByKey.getClaim();
						List<Preauth> preauthByClaimnKey = getPreauthByClaimnKey(claimByKey.getKey());
						if(preauthByClaimnKey != null && !preauthByClaimnKey.isEmpty()) {
							Preauth preauth2 = preauthByClaimnKey.get(0);
							SearchClearCashlessDTO filledDTO = getFilledDTO(claimByKey.getIntimation(), claimByKey, preauth2, dbCalcService, preauthByKey.getKey(), userName, passWord, humanTask);
							filledDTO.setIsEnhancement(true);
							filledDTO.setIsFirstLevelQueryReplyReceived(true);
							commonList.add(filledDTO);
						}
					} else {
						Claim claimByKey = getClaimByKey(preauthReq.getKey());
						List<Preauth> preauthByClaimnKey = getPreauthByClaimnKey(claimByKey.getKey());
						if(preauthByClaimnKey != null && !preauthByClaimnKey.isEmpty()) {
							Preauth preauth2 = preauthByClaimnKey.get(0);
							SearchClearCashlessDTO filledDTO = getFilledDTO(claimByKey.getIntimation(), claimByKey, preauth2, dbCalcService, null, userName, passWord, humanTask);
							filledDTO.setIsEnhancement(true);
							commonList.add(filledDTO);
						}
					}
					
				} else {
					Claim claimByKey = getClaimByKey(preauthReq.getKey());
					List<Preauth> preauthByClaimnKey = getPreauthByClaimnKey(claimByKey.getKey());
					if(preauthByClaimnKey != null && !preauthByClaimnKey.isEmpty()) {
						Preauth preauth2 = preauthByClaimnKey.get(0);
						SearchClearCashlessDTO filledDTO = getFilledDTO(claimByKey.getIntimation(), claimByKey, preauth2, dbCalcService, null, userName, passWord, humanTask);
						filledDTO.setIsEnhancement(true);
						commonList.add(filledDTO);
					}
				}
				
			}
		}

		
		
		page.setPageItems(commonList);
		return page;
	}
	
	protected Preauth getPreauth(List<Preauth> preauthList) {
		Preauth correctPreauth = null;
		for (Preauth preauth : preauthList) {
			if (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_COORDINATOR_REPLY_RECEIVED_STATUS) ||
					preauth.getStatus().getKey().equals(ReferenceTable.PREMEDICAL_PREAUTH_COORDINATOR_REPLY) ||
					preauth.getStatus().getKey().equals(ReferenceTable.PREMEDICAL_ENHANCEMENT_COORDINATOR_REPLY) ||
					preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_COORDINATOR_REPLY_RECEIVED_STATUS) 
					|| preauth
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_RECEIVED_STATUS) || preauth
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS) || preauth
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS) || preauth
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_COORDINATOR_REPLY_RECEIVED_STATUS) ||
							preauth.getStatus().getKey().equals(ReferenceTable.PREMEDICAL_PREAUTH_COORDINATOR_REPLY) ||
							preauth.getStatus().getKey().equals(ReferenceTable.PREMEDICAL_ENHANCEMENT_COORDINATOR_REPLY) ||
							preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_COORDINATOR_REPLY_RECEIVED_STATUS) || preauth.getStatus().getKey()
									.equals(ReferenceTable.ENHANCEMENT_QUERY_RECEIVED_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS) || preauth.getStatus().getKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_RECEIVED_STATUS)) {
				correctPreauth = preauth;
				break;
			}
		}
		return correctPreauth;
	}
	
	protected SearchClearCashlessDTO getFilledDTO(Intimation intimation, Claim claim, Preauth preauth, DBCalculationService dbCalculationService, Long preauthKey, String userName, String password, HumanTask humanTask) {
		SearchClearCashlessDTO dto = new SearchClearCashlessDTO();
		NewIntimationDto intimationDto = intimationService.getIntimationDto(intimation);
		dto.setNewIntimationDTO(intimationDto);
		// newIntimationDto.getPolicy().getProduct().getAutoRestoration()
//		ClaimDto claimDTO = new ClaimMapper().getClaimDto(claim);
		dto.setCpuCode(intimation.getCpuCode() != null ? String.valueOf(intimation.getCpuCode().getCpuCode()) : "" );
		if (preauthKey != null){
			dto.setPreauthKey(preauthKey);
		}
		if(preauth != null && preauth.getTotalApprovalAmount() != null) {
			dto.setPrauthAmount(preauth.getTotalApprovalAmount().longValue());
			
		}
		dto.setClaimKey(claim.getKey());
		dto.setPolicyNo(intimation.getPolicy().getPolicyNumber());
		dto.setIntimationNo(intimation.getIntimationId());
		dto.setInsuredPatientName(intimation.getInsuredPatientName());
		Hospitals hospitalDetail = getHospitalDetail(intimation.getHospital());
		dto.setHospitalName(hospitalDetail.getName());
		dto.setDateOfAdmission(intimation.getAdmissionDate());
		dto.setProductName(intimation.getPolicy().getProductName());
		Map<String, Double> balanceSI2 = dbCalculationService.getBalanceSI(intimationDto.getPolicy().getKey(), intimationDto.getInsuredPatient().getKey(), claim.getKey(), intimationDto.getOrginalSI(), intimationDto.getKey());
		dto.setBsiAmount(balanceSI2.containsKey(SHAConstants.TOTAL_BALANCE_SI) ? balanceSI2.get(SHAConstants.TOTAL_BALANCE_SI).longValue()  : 0l);
		dto.setUsername(userName);
		dto.setPassword(password);
		dto.setHumanTask(humanTask);
		return dto;
	}


	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<Preauth> getPreauthByKey(Long preauthKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByKey");
		query.setParameter("preauthKey", preauthKey);
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		return singleResult;
	}
	
	@SuppressWarnings("unchecked")
	public List<Preauth> getPreauthByIntimationKey(Long intimationKey) {
		Query query = entityManager
				.createNamedQuery("Preauth.findByIntimationKey");
		query.setParameter("intimationKey", intimationKey);
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		return singleResult;
	}
	
	@SuppressWarnings("unchecked")
	public List<Preauth> getPreauthByClaimnKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Preauth.findByClaimKey");
		query.setParameter("claimkey", claimKey);
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		return singleResult;
	}

	
	
	
	public Claim getClaimByKey(Long key) {
		Query query = entityManager.createNamedQuery("Claim.findByClaimKey");
		query.setParameter("claimKey", key);
		List<Claim> claim = (List<Claim>)query.getResultList();
		if(claim != null && ! claim.isEmpty()){
			for (Claim claim2 : claim) {
				entityManager.refresh(claim2);
			}
			return claim.get(0);
		}
		return null;
	}
	
	private Hospitals getHospitalDetail(Long hospitalId){

		Query findByHospitalKey = entityManager.createNamedQuery(
				"Hospitals.findByKey").setParameter("key", hospitalId);
		Hospitals hospitalDetail;
		try {
			hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			if(hospitalDetail != null){
				return hospitalDetail;
			}
			
		}
		catch(Exception e) {
			return null;
		}
		
		return null;
	}
	
	
}*/
