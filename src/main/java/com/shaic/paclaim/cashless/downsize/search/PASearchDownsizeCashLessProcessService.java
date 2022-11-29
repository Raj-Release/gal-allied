package com.shaic.paclaim.cashless.downsize.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
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

import org.jfree.util.Log;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.cashlessprocess.withdrawpreauth.SearchWithdrawCashLessProcessTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.cashless.withdraw.search.PASearchWithdrawCashLessProcessTableDTO;
import com.shaic.paclaim.cashless.withdraw.search.PAWithdrawPreauthMapper;

@Stateless
public class PASearchDownsizeCashLessProcessService extends
		AbstractDAO<Preauth> {
	
	/**
	 * Entity manager is created to load LOB value from master service.
	 * When created instance for master service and tried to reuse the same, 
	 * faced error in entity manager invocation. Also when user, @Inject or @EJB
	 * annotation, faced issues in invocation. Hence for time being created
	 * entity manager instance and using the same. Later will check with siva 
	 * for code.
	 * */
	@PersistenceContext
	protected EntityManager entityManager;

	public PASearchDownsizeCashLessProcessService() {
		super();
	}

	public Page<PASearchWithdrawCashLessProcessTableDTO> search(
			PASearchDownsizeCashLessProcessFormDTO formDTO, String userName, String passWord) {
		try 
		{
			String strIntimationNo = formDTO.getIntimationNo();
			String strPolicyNo = formDTO.getPolicyNo();		
			List<PASearchWithdrawCashLessProcessTableDTO> results = new ArrayList<PASearchWithdrawCashLessProcessTableDTO>(); 
			
			
			if(null != formDTO)
			{

				final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
				final CriteriaQuery<Preauth> criteriaQuery = builder
						.createQuery(Preauth.class);
	
				Root<Preauth> searchRoot = criteriaQuery.from(Preauth.class);
				
				List<Predicate> predicates = new ArrayList<Predicate>();
	
				List<Claim> resultList = new ArrayList<Claim>();
				
				if (strIntimationNo != null) {
					Predicate intimationPredicate = builder.like(
							searchRoot.<Preauth> get("intimation")
									.<String> get("intimationId"), "%"
									+ strIntimationNo + "%");
					predicates.add(intimationPredicate);
				}
				if (strPolicyNo != null) {
					Predicate policyPredicate = builder
							.like(searchRoot.<Preauth> get("policy").<String> get("policyNumber"), "%"
									+ strPolicyNo + "%");
					predicates.add(policyPredicate);
				}
				
				
				Predicate condition1= builder.notEqual(searchRoot.<Status>get("status").<Long>get("key"),ReferenceTable.PREAUTH_REJECT_STATUS);				
				predicates.add(condition1);
				
				Predicate condition2 = builder.notEqual(searchRoot.<Status>get("status").<Long>get("key"),ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS);
				predicates.add(condition2);
				
				Predicate condition3 = builder.notEqual(searchRoot.<Status>get("status").<Long>get("key"),ReferenceTable.ENHANCEMENT_REJECT_STATUS);
				predicates.add(condition3);
				
				Predicate condition4 = builder.notEqual(searchRoot.<Status>get("status").<Long>get("key"),ReferenceTable.ENHANCEMENT_DENIAL_OF_CASHLESS_STATUS);
				predicates.add(condition4);
				
				Predicate condition5 = builder.notEqual(searchRoot.<Status>get("status").<Long>get("key"),ReferenceTable.CLEAR_CASHLESS_STATUS_FOR_PREAUTH);
				predicates.add(condition5);
				
				
				Predicate condition6 = builder.notEqual(searchRoot.<Status>get("status").<Long>get("key"),ReferenceTable.CLEAR_CASHLESS_STATUS_FOR_ENHANCEMENT);
				predicates.add(condition6);
				
				Predicate condition8 = builder.equal(searchRoot.<Preauth>get("claim").<Long>get("lobId"), ReferenceTable.PA_LOB_KEY);
				predicates.add(condition8);	
				criteriaQuery.select(searchRoot).where(
						builder.and(predicates.toArray(new Predicate[] {})));

				final TypedQuery<Preauth> searchClaimQuery = entityManager
						.createQuery(criteriaQuery);
				
				List<Preauth> pageItemList  = searchClaimQuery.getResultList();

				//Boolean isPreauth=false;
				
				for (Preauth preauth : pageItemList) {
                       entityManager.refresh(preauth);
					
				}
			    List<Long> keysList=new ArrayList<Long>();
			    for(Preauth preauth: pageItemList){
			    	keysList.add(preauth.getKey());
			    }
			    Long key=Collections.max(keysList);
			    
			    Query findAll = entityManager
						.createNamedQuery("Preauth.findByKey");
				findAll.setParameter("preauthKey", key);
				
				List<Preauth> preauthListfromDb=(List<Preauth>)findAll.getResultList();
				List<Preauth> preauthList=new ArrayList<Preauth>();
				for (Preauth preauth : preauthListfromDb) {
                    entityManager.refresh(preauth);
                    if(preauth.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
                    if(! (preauth.getStage().getKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE)) && 
							! (preauth.getStage().getKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE))
							&& !(preauth.getStage().getKey().equals(ReferenceTable.WITHDRAW_STAGE))){
                    	
                    	Map<Long, Long> preauthForWithdrawAndDownsize = ReferenceTable.getPreauthForWithdrawAndDownsize();
						Long status = preauthForWithdrawAndDownsize.get(preauth.getStatus().getKey());
						if(status != null){
							 preauthList.add(preauth);
						  }
					   }
                    }
				}
				results=PAWithdrawPreauthMapper.getInstance().getWithdrawTableDTO(preauthList);
				
				List<Long>  diagnosisList = new ArrayList<Long>();
				
				if(null!=preauthList){
					for (Preauth preauth : preauthList) {
						diagnosisList.add(preauth.getKey());
					}
				}
				List<String> lobValueList=new ArrayList<String>();
				
				if(null !=preauthList){
					for (Preauth preauth : preauthList) {
						
						Long lobId=preauth.getPolicy().getLobId();
						String lobValue=loadLobValue(lobId);
						lobValueList.add(lobValue);
						
					}
				}
				int i=0;
				for (PASearchWithdrawCashLessProcessTableDTO resultDto:results) {
					resultDto.setLob(lobValueList.get(i));
					resultDto.setUsername(userName);
					
					if(resultDto.getProductKey() != null && ReferenceTable.getGPAProducts().containsKey(resultDto.getProductKey())){
						if(resultDto.getPaPatientName() != null){
							resultDto.setInsuredPatientName(resultDto.getPaPatientName());
						}
					}
					i++;
				}
				
				ImsUser imsUser = formDTO.getImsUser();
				
				String[] userRoleList = imsUser.getUserRoleList();
				
				
				WeakHashMap<String, Object> escalateValidation = SHAUtils.getEscalateValidation(userRoleList);
				
				if((Boolean) escalateValidation.get(SHAConstants.CMA4)){
					for (PASearchWithdrawCashLessProcessTableDTO resultDto : results) {
						resultDto.setIsCheifMedicalOfficer(true);
					}
				}else if((Boolean) escalateValidation.get(SHAConstants.CMA3)){
					for (PASearchWithdrawCashLessProcessTableDTO resultDto : results) {
						resultDto.setIsZonalMedicalHead(true);
					}
				}else if((Boolean) escalateValidation.get(SHAConstants.CMA2)){
					
					for (PASearchWithdrawCashLessProcessTableDTO resultDto : results) {
						resultDto.setIsZonalSeniorMedicalApprover(true);
					}
				}else if((Boolean) escalateValidation.get(SHAConstants.CMA1)){
					
					for (PASearchWithdrawCashLessProcessTableDTO resultDto : results) {
						resultDto.setIsZonalMedicalApprover(true);
					}
				}
				
				List<PedValidation> pedValidationList=SHAUtils.getByPreauthKey(entityManager, diagnosisList);
				
				List<Long> diagnosisInfo=new ArrayList<Long>();
				if(null !=pedValidationList){
					for (PedValidation pedValidation : pedValidationList) {
						
						diagnosisInfo.add(pedValidation.getDiagnosisId());
					}
				}
				
				List<Diagnosis> diagnosisValue=SHAUtils.getByDiagonsisKey(entityManager, diagnosisInfo);
				
				for (Diagnosis diagnosis : diagnosisValue) {
					for (PASearchWithdrawCashLessProcessTableDTO tableDTO : results) {
						tableDTO.setDiagnosis(diagnosis.getValue());
					}
				}
				
				List<Long>  hospTypeList = new ArrayList<Long>();
				
				ListIterator<Preauth> iterClaim = preauthList.listIterator();
				
				while (iterClaim.hasNext())
				{
					 Preauth objClaim = iterClaim.next();
					 Long hospitalTypeId = objClaim.getIntimation().getHospital();
					 /*
					  * To fetch hospital information from VW_HOSPITALS 
					  * we require hospital type id. Hence forming
					  * below list
					  * 
					  * **/
					 hospTypeList.add(hospitalTypeId);
				}
				
				List<Hospitals> resultListForHospitalInfo = new ArrayList<Hospitals>();
				List<SearchWithdrawCashLessProcessTableDTO> searchClaimTableDTOForHospitalInfo = new ArrayList<SearchWithdrawCashLessProcessTableDTO>();
				resultListForHospitalInfo = SHAUtils.getHospitalInformation(entityManager, hospTypeList);
				if(null!=resultListForHospitalInfo)
				{
				for(Hospitals hospitalInfo:resultListForHospitalInfo)
				{
					for (PASearchWithdrawCashLessProcessTableDTO searchWithdrawCashLessProcessTableDTO : results) {
						searchWithdrawCashLessProcessTableDTO.setHospitalName(hospitalInfo.getName());
						searchWithdrawCashLessProcessTableDTO.setHospitalAddress(hospitalInfo.getAddress());
						searchWithdrawCashLessProcessTableDTO.setHospitalCity(hospitalInfo.getCity());
					}
				}
			}
			Page<Preauth> pagedList = super.pagedList(formDTO.getPageable());
			Page<PASearchWithdrawCashLessProcessTableDTO> page = new Page<PASearchWithdrawCashLessProcessTableDTO>();
			page.setPageItems(results);
			page.setIsDbSearch(true);
			return page;
		}
		}
			catch(Exception e){
				Log.info("No preauth for this condition");
				e.printStackTrace();
			}
		return null;		
	}
	
	
	public Page<PASearchWithdrawCashLessProcessTableDTO> searchFromDBTask(PASearchDownsizeCashLessProcessFormDTO formDTO, String userName, String passWord){
		
		String strIntimationNo = formDTO.getIntimationNo();
		String strPolicyNo = formDTO.getPolicyNo();		
		List<PASearchWithdrawCashLessProcessTableDTO> results = new ArrayList<PASearchWithdrawCashLessProcessTableDTO>(); 
		
//		DownsizePreAuthQF preAuthQF = null;
		try{
				Boolean isEscalateFromSpecialist = false;
//				preAuthQF = new DownsizePreAuthQF();
//				preAuthQF.setIntimationNumber((null != strIntimationNo && !("").equalsIgnoreCase(strIntimationNo))? strIntimationNo :null);
//				preAuthQF.setPolicyId((null != strPolicyNo && !("").equalsIgnoreCase(strPolicyNo)) ? strPolicyNo :null);
				
				String priority = formDTO.getPriority() != null ? formDTO.getPriority().getValue() != null ? formDTO.getPriority().getValue() : null : null;
				String source = formDTO.getSource() != null ? formDTO.getSource().getValue() != null ? formDTO.getSource().getValue(): null : null;
				String type = formDTO.getType() != null ? formDTO.getType().getValue() != null ? formDTO.getType().getValue(): null : null;
				
				
				Map<String, Object> mapValues = new WeakHashMap<String, Object>();
				
				List<Map<String, Object>> taskProcedure = null;

				Integer totalRecords = 0;

				List<Long> keys = new ArrayList<Long>();

				mapValues.put(SHAConstants.CURRENT_Q,
						SHAConstants.PROCESS_DOWNSIZE_REQUEST_CURRENT_QUEUE);
				
				mapValues.put(SHAConstants.LOB, SHAConstants.PA_LOB);
				mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.PA_LOB_TYPE);

				mapValues.put(SHAConstants.USER_ID, userName);

				if (null != strIntimationNo && !strIntimationNo.equals("")) {
					strIntimationNo = formDTO.getIntimationNo();

					mapValues.put(SHAConstants.INTIMATION_NO, strIntimationNo);
				}
				if (null != strPolicyNo && !strPolicyNo.equals("")) {

					strPolicyNo = formDTO.getPolicyNo();
					mapValues.put(SHAConstants.POLICY_NUMBER, strPolicyNo);
				}

				if (priority != null && !priority.isEmpty() || source != null
						&& !source.isEmpty() || type != null && !type.isEmpty()) {

					if (priority != null && !priority.isEmpty())
						if (!priority.equalsIgnoreCase(SHAConstants.ALL)) {
							mapValues.put(SHAConstants.PRIORITY, priority);
						}
					if (source != null && !source.isEmpty()) {

						mapValues.put(SHAConstants.STAGE_SOURCE, source);
					}

					if (type != null && !type.isEmpty()) {
						if (!type.equalsIgnoreCase(SHAConstants.ALL)) {
							mapValues.put(SHAConstants.RECORD_TYPE, type);
						}
					}

				}

				Pageable pageable = formDTO.getPageable();
				pageable.setPageNumber(1);
				pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);				
			
				Map<Long, Integer> taskNumberMap = new HashMap<Long, Integer>();
				
				
				Map<Long, Object> workFlowMap = new WeakHashMap<Long, Object>();

				//Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
				Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);

				DBCalculationService dbCalculationService = new DBCalculationService();
				taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);

				if (null != taskProcedure) {

					for (Map<String, Object> outPutArray : taskProcedure) {
						Long keyValue = (Long) outPutArray
								.get(SHAConstants.CASHLESS_KEY);
						workFlowMap.put(Long.valueOf(keyValue), outPutArray);

						totalRecords = (Integer) outPutArray
								.get(SHAConstants.TOTAL_RECORDS);

						keys.add(keyValue);
					}
					
					List<Preauth> resultList = new ArrayList<Preauth>();
					resultList = SHAUtils.getIntimationInformation(SHAConstants.PREAUTH_SEARCH_SCREEN, entityManager, keys);
					
					results = PAWithdrawPreauthMapper.getInstance().getWithdrawTableDTO(resultList);
					
					for (PASearchWithdrawCashLessProcessTableDTO tableDto : results) {
						if (tableDto.getKey() != null) {
							Object workflowKey = workFlowMap.get(tableDto.getKey());
							tableDto.setDbOutArray(workflowKey);
						}

					}
					List<Long>  diagnosisList = new ArrayList<Long>();
					
					if(null!=resultList){
						for (Preauth preauth : resultList) {
							diagnosisList.add(preauth.getKey());
						}
					}
					List<String> lobValueList=new ArrayList<String>();
					
					if(null !=resultList){
						for (Preauth preauth : resultList) {
							
							Long lobId=preauth.getPolicy().getLobId();
							String lobValue=loadLobValue(lobId);
							lobValueList.add(lobValue);
							
						}
					}
					int i=0;
					for (PASearchWithdrawCashLessProcessTableDTO resultDto:results) {
						resultDto.setLob(lobValueList.get(i));
					//	resultDto.setHumanTask(humanTaskMap.get(resultDto.getKey()));
					//	resultDto.setTaskNumber(taskNumberMap.get(resultDto.getKey()));
						resultDto.setUsername(userName);
						resultDto.setPassword(passWord);
						resultDto.setIsSpecialistReply(isEscalateFromSpecialist);
						i++;
					}
					
					ImsUser imsUser = formDTO.getImsUser();
					
					String[] userRoleList = imsUser.getUserRoleList();
					
					
					WeakHashMap<String, Object> escalateValidation = SHAUtils.getEscalateValidation(userRoleList);
					
					if((Boolean) escalateValidation.get(SHAConstants.CMA4)){
						for (PASearchWithdrawCashLessProcessTableDTO resultDto : results) {
							resultDto.setIsCheifMedicalOfficer(true);
						}
					}else if((Boolean) escalateValidation.get(SHAConstants.CMA3)){
						for (PASearchWithdrawCashLessProcessTableDTO resultDto : results) {
							resultDto.setIsZonalMedicalHead(true);
						}
					}else if((Boolean) escalateValidation.get(SHAConstants.CMA2)){
						
						for (PASearchWithdrawCashLessProcessTableDTO resultDto : results) {
							resultDto.setIsZonalSeniorMedicalApprover(true);
						}
					}else if((Boolean) escalateValidation.get(SHAConstants.CMA1)){
						
						for (PASearchWithdrawCashLessProcessTableDTO resultDto : results) {
							resultDto.setIsZonalMedicalApprover(true);
						}
					}
					
					
					List<PedValidation> pedValidationList=SHAUtils.getByPreauthKey(entityManager, diagnosisList);
					
					List<Long> diagnosisInfo=new ArrayList<Long>();
					if(null !=pedValidationList){
						for (PedValidation pedValidation : pedValidationList) {
							
							diagnosisInfo.add(pedValidation.getDiagnosisId());
						}
					}
					
					List<Diagnosis> diagnosisValue=SHAUtils.getByDiagonsisKey(entityManager, diagnosisInfo);
					
					for (Diagnosis diagnosis : diagnosisValue) {
						for (PASearchWithdrawCashLessProcessTableDTO tableDTO : results) {
							tableDTO.setDiagnosis(diagnosis.getValue());
						}
					}
					
//					List<Long>  hospTypeList = new ArrayList<Long>();
//					
//					ListIterator<Preauth> iterClaim = resultList.listIterator();
//					
//					while (iterClaim.hasNext())
//					{
//						 Preauth objClaim = iterClaim.next();
//						 Long hospitalTypeId = objClaim.getIntimation().getHospital();
//						 /*
//						  * To fetch hospital information from VW_HOSPITALS 
//						  * we require hospital type id. Hence forming
//						  * below list
//						  * 
//						  * **/
//						 hospTypeList.add(hospitalTypeId);
//					}
					
					List<Hospitals> resultListForHospitalInfo = new ArrayList<Hospitals>();
//					List<SearchWithdrawCashLessProcessTableDTO> searchClaimTableDTOForHospitalInfo = new ArrayList<SearchWithdrawCashLessProcessTableDTO>();
//					resultListForHospitalInfo = SHAUtils.getHospitalInformation(entityManager, hospTypeList);
//					if(null!=resultListForHospitalInfo)
//					{
//					for(Hospitals hospitalInfo:resultListForHospitalInfo)
//					{
						for (PASearchWithdrawCashLessProcessTableDTO searchWithdrawCashLessProcessTableDTO : results) {
							
							List<Long> hospitalList = new ArrayList<Long>();
							hospitalList.add(searchWithdrawCashLessProcessTableDTO.getHospitalId());
							
								resultListForHospitalInfo = SHAUtils.getHospitalInformation(entityManager, hospitalList);
								
								for (Hospitals hospitalInfo : resultListForHospitalInfo) {
									searchWithdrawCashLessProcessTableDTO.setHospitalName(hospitalInfo.getName());
									searchWithdrawCashLessProcessTableDTO.setHospitalAddress(hospitalInfo.getAddress());
									searchWithdrawCashLessProcessTableDTO.setHospitalCity(hospitalInfo.getCity());
								}

							}
//						}
//					}
//				}
				Page<Preauth> pagedList = super.pagedList(formDTO.getPageable());
				Page<PASearchWithdrawCashLessProcessTableDTO> page = new Page<PASearchWithdrawCashLessProcessTableDTO>();
				page.setPageItems(results);
				return page;
				}
					
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;

	}

	@Override
	public Class<Preauth> getDTOClass() {
		return Preauth.class;
	}

	/**
	 * Method to load Lob value
	 * 
	 * */
	public String loadLobValue(Long lobID)
	{
		MastersValue a_mastersValue = new MastersValue();
		if (lobID != null) {
			Query query = entityManager
					.createNamedQuery("MastersValue.findByKey");
			query = query.setParameter("parentKey", lobID);
			List<MastersValue> mastersValueList = query.getResultList();
			for (MastersValue mastersValue : mastersValueList)
				a_mastersValue = mastersValue;
		}

		return a_mastersValue.getValue();
	}
	
	public void setBPMOutCome(Preauth preauth,PreauthDTO preauthDto){
		
		Map<String, String> regIntDetailsReq = new HashMap<String, String>();
		
		Map<String,String> preauthReq=new HashMap<String, String>();
		
		preauthReq.put("key",preauth.getKey().toString());
		
		regIntDetailsReq.put("IntimationNumber",preauth.getIntimation().getIntimationId());
		
		//Context context = BPMClientContext.getInitialContext(user,password);
		
//		try {
//
////			withdrawPreauth = (WithdrawPreAuth) context
////					.lookup("WithdrawPreAuth#com.shaic.ims.bpm.claim.service.preauth.WithdrawPreAuth");
//			//withdrawPreauth.initiate(arg0, arg1, arg2, arg3);
//
//			System.out
//					.println("Lookup called and executed at the server side ");
//			return;
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}
		
		
		
		
		
		
		
	}
	
	
}
