package com.shaic.claim.cashlessprocess.downsize.search;

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
import com.shaic.claim.cashlessprocess.withdrawpreauth.SearchWithdrawCashLessProcessTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.withdrawWizard.WithdrawPreauthMapper;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.IncurredClaimRatio;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.DBCalculationService;

@Stateless
public class SearchDownsizeCashLessProcessService extends
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

	public SearchDownsizeCashLessProcessService() {
		super();
	}

	public Page<SearchWithdrawCashLessProcessTableDTO> search(
			SearchDownsizeCashLessProcessFormDTO formDTO, String userName, String passWord) {
		try 
		{
			String strIntimationNo = formDTO.getIntimationNo();
			String strPolicyNo = formDTO.getPolicyNo();		
			List<SearchWithdrawCashLessProcessTableDTO> results = new ArrayList<SearchWithdrawCashLessProcessTableDTO>(); 
			
			
			if(null != formDTO)
			{

				final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
				final CriteriaQuery<Preauth> criteriaQuery = builder
						.createQuery(Preauth.class);
	
				Root<Preauth> searchRoot = criteriaQuery.from(Preauth.class);
				
				List<Predicate> predicates = new ArrayList<Predicate>();
	
				//List<Claim> resultList = new ArrayList<Claim>();
				
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
				
				/*Predicate condition8 = builder.equal(searchRoot.<Preauth>get("claim").<Long>get("lobId"), ReferenceTable.HEALTH_LOB_KEY);
				predicates.add(condition8);	*/
				
				Predicate condition8 = builder.notLike(searchRoot.<Intimation>get("intimation").<String>get("processClaimType"), "%"+SHAConstants.PA_TYPE+"%");
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
				results=WithdrawPreauthMapper.getInstance().getWithdrawTableDTO(preauthList);
				
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
				for (SearchWithdrawCashLessProcessTableDTO resultDto:results) {

					if(resultDto.getIntimationNo() != null){
						Intimation intimationByNo = getIntimationByNo(resultDto.getIntimationNo());
						if(ReferenceTable.getGMCProductList().containsKey(intimationByNo.getPolicy().getProduct().getKey())){
							String colorCodeForGMC = getColorCodeForGMC(intimationByNo.getPolicy().getPolicyNumber(), intimationByNo.getInsured().getInsuredId().toString());
							resultDto.setColorCode(colorCodeForGMC);
						}
					}
					
					resultDto.setLob(lobValueList.get(i));
					resultDto.setUsername(userName);
					i++;
				}
				
				ImsUser imsUser = formDTO.getImsUser();
				
				String[] userRoleList = imsUser.getUserRoleList();
				
				
				WeakHashMap<String, Object> escalateValidation = SHAUtils.getEscalateValidation(userRoleList);
				
				if((Boolean) escalateValidation.get(SHAConstants.CMA4)){
					for (SearchWithdrawCashLessProcessTableDTO resultDto : results) {
						resultDto.setIsCheifMedicalOfficer(true);
					}
				}else if((Boolean) escalateValidation.get(SHAConstants.CMA3)){
					for (SearchWithdrawCashLessProcessTableDTO resultDto : results) {
						resultDto.setIsZonalMedicalHead(true);
					}
				}else if((Boolean) escalateValidation.get(SHAConstants.CMA2)){
					
					for (SearchWithdrawCashLessProcessTableDTO resultDto : results) {
						resultDto.setIsZonalSeniorMedicalApprover(true);
					}
				}else if((Boolean) escalateValidation.get(SHAConstants.CMA1)){
					
					for (SearchWithdrawCashLessProcessTableDTO resultDto : results) {
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
					for (SearchWithdrawCashLessProcessTableDTO tableDTO : results) {
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
				//List<SearchWithdrawCashLessProcessTableDTO> searchClaimTableDTOForHospitalInfo = new ArrayList<SearchWithdrawCashLessProcessTableDTO>();
				resultListForHospitalInfo = SHAUtils.getHospitalInformation(entityManager, hospTypeList);
				if(null!=resultListForHospitalInfo)
				{
				for(Hospitals hospitalInfo:resultListForHospitalInfo)
				{
					for (SearchWithdrawCashLessProcessTableDTO searchWithdrawCashLessProcessTableDTO : results) {
						searchWithdrawCashLessProcessTableDTO.setHospitalName(hospitalInfo.getName());
						searchWithdrawCashLessProcessTableDTO.setHospitalAddress(hospitalInfo.getAddress());
						searchWithdrawCashLessProcessTableDTO.setHospitalCity(hospitalInfo.getCity());
					}
				}
			}
			//Page<Preauth> pagedList = super.pagedList(formDTO.getPageable());
			Page<SearchWithdrawCashLessProcessTableDTO> page = new Page<SearchWithdrawCashLessProcessTableDTO>();
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
	
	
	/*public Page<SearchWithdrawCashLessProcessTableDTO> searchFromBPMNTask(SearchDownsizeCashLessProcessFormDTO formDTO, String userName, String passWord){
		
		String strIntimationNo = formDTO.getIntimationNo();
		String strPolicyNo = formDTO.getPolicyNo();		
		List<SearchWithdrawCashLessProcessTableDTO> results = new ArrayList<SearchWithdrawCashLessProcessTableDTO>(); 
		
//		DownsizePreAuthQF preAuthQF = null;
		try{
				Boolean isEscalateFromSpecialist = false;
//				preAuthQF = new DownsizePreAuthQF();
//				preAuthQF.setIntimationNumber((null != strIntimationNo && !("").equalsIgnoreCase(strIntimationNo))? strIntimationNo :null);
//				preAuthQF.setPolicyId((null != strPolicyNo && !("").equalsIgnoreCase(strPolicyNo)) ? strPolicyNo :null);
				
				String priority = formDTO.getPriority() != null ? formDTO.getPriority().getValue() != null ? formDTO.getPriority().getValue() : null : null;
				String source = formDTO.getSource() != null ? formDTO.getSource().getValue() != null ? formDTO.getSource().getValue(): null : null;
				String type = formDTO.getType() != null ? formDTO.getType().getValue() != null ? formDTO.getType().getValue(): null : null;
				
				
				PayloadBOType payloadBO = null;
				
				IntimationType intimationType = new IntimationType();
				PolicyType policyType = new PolicyType();
				
				
				if(null != strIntimationNo && ! strIntimationNo.equals(""))
				{
					if(payloadBO == null){
						payloadBO = new PayloadBOType();
					}
					
					intimationType.setIntimationNumber(strIntimationNo);
					payloadBO.setIntimation(intimationType);
				}
				if(null != strPolicyNo && !strPolicyNo.equals(""))
				{
					
					if(payloadBO == null){
						payloadBO = new PayloadBOType();
					}
					
					policyType.setPolicyId(strPolicyNo);
					payloadBO.setPolicy(policyType);
				}
				
				ClassificationType classification = null;
				
			    if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
							|| type != null && ! type.isEmpty()){
						classification = new ClassificationType();
						
						if(priority != null && ! priority.isEmpty())
							if(priority.equalsIgnoreCase(SHAConstants.ALL)){
								priority = null;
							}
						classification.setPriority(priority);
						if(source != null && ! source.isEmpty()){
							classification.setSource(source);
						}
						
						if(type != null && ! type.isEmpty()){
							if(type.equalsIgnoreCase(SHAConstants.ALL)){
								type = null;
							}
							classification.setType(type);
						}
						
						if(payloadBO == null){
							payloadBO = new PayloadBOType();
						}
						
						 payloadBO.setClassification(classification);
				}
			    
			    
			    payloadBO = SHAUtils.getCashlessPayloadForHealth(payloadBO);
			    
				
				com.shaic.ims.bpm.claim.servicev2.preauth.search.DownsizePreAuthTask processDownSizePreAuthTask = BPMClientContext.getProcessDownSizePreAuthTask(userName, passWord);
				
				Pageable pageable = formDTO.getPageable();
				pageable.setPageNumber(1);
				pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
				
				//com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks =  processDownSizePreAuthTask.getTasks(userName,passWord, pageable, null);
				com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks =  processDownSizePreAuthTask.getTasks(userName,pageable, payloadBO);
				
				Map<Long, com.shaic.ims.bpm.claim.modelv2.HumanTask> humanTaskMap = new HashMap<Long, com.shaic.ims.bpm.claim.modelv2.HumanTask>();
				Map<Long, Integer> taskNumberMap = new HashMap<Long, Integer>();
				
				
				if(null != tasks)
				{
					List<com.shaic.ims.bpm.claim.modelv2.HumanTask> humanTasksList = tasks.getHumanTasks();
					List<Long> keys = new ArrayList<Long>();  
					
					for (com.shaic.ims.bpm.claim.modelv2.HumanTask item: humanTasksList)
				    {
							PayloadBOType payloadBOCashless = item.getPayloadCashless();
							PreAuthReqType preauthReq = payloadBOCashless.getPreAuthReq(); 
							if(null != preauthReq)
							{
								Long keyValue = preauthReq.getKey();
								humanTaskMap.put(keyValue, item);
								taskNumberMap.put(keyValue, item.getNumber());
								keys.add(keyValue);
								if(preauthReq.getOutcome() != null && preauthReq.getOutcome().equalsIgnoreCase(SHAConstants.SPECIALIST_RECIEVED)){
									isEscalateFromSpecialist = true;
								}
							}
							
							//Map<String, String> valuesFromBPM = BPMClientContext.getMapFromPayload(item.getPayload(), "RegIntDetails");
							//Map<String, String> valuesFromBPM = BPMClientContext.getMapFromPayload(item.getPayload(), "PreAuthReq");
							//System.out.println("--the map----"+valuesFromBPM);
							if(null != valuesFromBPM.get("key") && !("").equals(valuesFromBPM.get("key")))
							{
								Long keyValue = Long.valueOf(valuesFromBPM.get("key"));
								humanTaskMap.put(keyValue, item);
								keys.add(keyValue);
							}
					   
						
						//Map<String, String> valuesFromBPM = BPMClientContext.getMapFromPayload(item.getPayload(), "RegIntDetails");
						Map<String, String> valuesFromBPM = BPMClientContext.getMapFromPayload(item.getPayload(), "PreAuthReq");
						if(null != valuesFromBPM.get("key") && !("").equals(valuesFromBPM.get("key")))
						{
							Long keyValue = Long.valueOf(valuesFromBPM.get("key"));
							//Key --> item mapping
							humanTaskMap.put(keyValue, item);
							keys.add(keyValue);
						}		
						
						
				    }
					
					List<Preauth> resultList = new ArrayList<Preauth>();
					resultList = SHAUtils.getIntimationInformation(SHAConstants.PREAUTH_SEARCH_SCREEN, entityManager, keys);
					
					results = WithdrawPreauthMapper.getInstance().getWithdrawTableDTO(resultList);
					
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
					for (SearchWithdrawCashLessProcessTableDTO resultDto:results) {
						resultDto.setLob(lobValueList.get(i));
						resultDto.setHumanTask(humanTaskMap.get(resultDto.getKey()));
						resultDto.setTaskNumber(taskNumberMap.get(resultDto.getKey()));
						resultDto.setUsername(userName);
						resultDto.setPassword(passWord);
						resultDto.setIsSpecialistReply(isEscalateFromSpecialist);
						i++;
					}
					
					ImsUser imsUser = formDTO.getImsUser();
					
					String[] userRoleList = imsUser.getUserRoleList();
					
					
					WeakHashMap<String, Object> escalateValidation = SHAUtils.getEscalateValidation(userRoleList);
					
					if((Boolean) escalateValidation.get(SHAConstants.CMA4)){
						for (SearchWithdrawCashLessProcessTableDTO resultDto : results) {
							resultDto.setIsCheifMedicalOfficer(true);
						}
					}else if((Boolean) escalateValidation.get(SHAConstants.CMA3)){
						for (SearchWithdrawCashLessProcessTableDTO resultDto : results) {
							resultDto.setIsZonalMedicalHead(true);
						}
					}else if((Boolean) escalateValidation.get(SHAConstants.CMA2)){
						
						for (SearchWithdrawCashLessProcessTableDTO resultDto : results) {
							resultDto.setIsZonalSeniorMedicalApprover(true);
						}
					}else if((Boolean) escalateValidation.get(SHAConstants.CMA1)){
						
						for (SearchWithdrawCashLessProcessTableDTO resultDto : results) {
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
						for (SearchWithdrawCashLessProcessTableDTO tableDTO : results) {
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
						/*for (SearchWithdrawCashLessProcessTableDTO searchWithdrawCashLessProcessTableDTO : results) {
							
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
				Page<SearchWithdrawCashLessProcessTableDTO> page = new Page<SearchWithdrawCashLessProcessTableDTO>();
				page.setPageItems(results);
				return page;
				}
					
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;

	}*/

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
	
	public Page<SearchWithdrawCashLessProcessTableDTO> searchFromDBTask(
			SearchDownsizeCashLessProcessFormDTO formDTO, String userName,
			String passWord) {

		String strIntimationNo = formDTO.getIntimationNo();
		String strPolicyNo = formDTO.getPolicyNo();
		List<SearchWithdrawCashLessProcessTableDTO> results = new ArrayList<SearchWithdrawCashLessProcessTableDTO>();

		List<Map<String, Object>> taskProcedure = null;

		try {
			Boolean isEscalateFromSpecialist = false;

			/*String priority = formDTO.getPriority() != null ? formDTO
					.getPriority().getValue() != null ? formDTO.getPriority()
					.getValue() : null : null;*/
			String source = formDTO.getSource() != null ? formDTO.getSource()
					.getValue() != null ? formDTO.getSource().getValue() : null
					: null;
			String type = formDTO.getType() != null ? formDTO.getType()
					.getValue() != null ? formDTO.getType().getValue() : null
					: null;
			Boolean priorityAll = formDTO.getPriorityAll() != null ? formDTO.getPriorityAll() : null;
			Boolean crm = formDTO.getCrm() != null ? formDTO.getCrm() : null;
			Boolean vip = formDTO.getVip() != null ? formDTO.getVip() : null;

			Map<String, Object> mapValues = new WeakHashMap<String, Object>();

			Integer totalRecords = 0;

			List<Long> keys = new ArrayList<Long>();

			mapValues.put(SHAConstants.CURRENT_Q,
					SHAConstants.PROCESS_DOWNSIZE_REQUEST_CURRENT_QUEUE);
			
			mapValues.put(SHAConstants.LOB, SHAConstants.HEALTH_LOB);
			mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.HEALTH_LOB_TYPE);

			mapValues.put(SHAConstants.USER_ID, userName);

			if (null != strIntimationNo && !strIntimationNo.equals("")) {
				strIntimationNo = formDTO.getIntimationNo();

				mapValues.put(SHAConstants.INTIMATION_NO, strIntimationNo);
			}
			if (null != strPolicyNo && !strPolicyNo.equals("")) {

				strPolicyNo = formDTO.getPolicyNo();
				mapValues.put(SHAConstants.POLICY_NUMBER, strPolicyNo);
			}

			if (/*priority != null && !priority.isEmpty() || */source != null
					&& !source.isEmpty() || type != null && !type.isEmpty()) {

				/*if (priority != null && !priority.isEmpty())
					if (!priority.equalsIgnoreCase(SHAConstants.ALL)) {
						mapValues.put(SHAConstants.PRIORITY, priority);
					}*/
				if (source != null && !source.isEmpty()) {

					mapValues.put(SHAConstants.STAGE_SOURCE, source);
				}

				if (type != null && !type.isEmpty()) {
					if (!type.equalsIgnoreCase(SHAConstants.ALL)) {
						mapValues.put(SHAConstants.RECORD_TYPE, type);
					}
				}

			}
			
			if (crm != null && crm.equals(Boolean.TRUE)) {
		    	mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "Y");
		    }
		    
		    if (vip != null && vip.equals(Boolean.TRUE)) {
		    	mapValues.put(SHAConstants.PRIORITY, "VIP");
		    }

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
				resultList = SHAUtils
						.getIntimationInformation(
								SHAConstants.PREAUTH_SEARCH_SCREEN,
								entityManager, keys);

				results = WithdrawPreauthMapper.getInstance()
						.getWithdrawTableDTO(resultList);

				for (SearchWithdrawCashLessProcessTableDTO tableDto : results) {
					if (tableDto.getKey() != null) {
						Object workflowKey = workFlowMap.get(tableDto.getKey());
						tableDto.setDbOutArray(workflowKey);
					}

				}

				List<Long> diagnosisList = new ArrayList<Long>();

				if (null != resultList) {
					for (Preauth preauth : resultList) {
						diagnosisList.add(preauth.getKey());
					}
				}
				List<String> lobValueList = new ArrayList<String>();

				if (null != resultList) {
					for (Preauth preauth : resultList) {

						Long lobId = preauth.getPolicy().getLobId();
						String lobValue = loadLobValue(lobId);
						lobValueList.add(lobValue);

					}
				}
				int i = 0;
				for (SearchWithdrawCashLessProcessTableDTO resultDto : results) {
					resultDto.setLob(lobValueList.get(i));
					// resultDto.setHumanTask(humanTaskMap.get(resultDto.getKey()));
					// resultDto.setTaskNumber(taskNumberMap.get(resultDto.getKey()));
					resultDto.setUsername(userName);
					resultDto.setPassword(passWord);
					resultDto.setIsSpecialistReply(isEscalateFromSpecialist);
					i++;
				}

				ImsUser imsUser = formDTO.getImsUser();

				String[] userRoleList = imsUser.getUserRoleList();

				WeakHashMap<String, Object> escalateValidation = SHAUtils
						.getEscalateValidation(userRoleList);

				if ((Boolean) escalateValidation.get(SHAConstants.CMA4)) {
					for (SearchWithdrawCashLessProcessTableDTO resultDto : results) {
						resultDto.setIsCheifMedicalOfficer(true);
					}
				} else if ((Boolean) escalateValidation.get(SHAConstants.CMA3)) {
					for (SearchWithdrawCashLessProcessTableDTO resultDto : results) {
						resultDto.setIsZonalMedicalHead(true);
					}
				} else if ((Boolean) escalateValidation.get(SHAConstants.CMA2)) {

					for (SearchWithdrawCashLessProcessTableDTO resultDto : results) {
						resultDto.setIsZonalSeniorMedicalApprover(true);
					}
				} else if ((Boolean) escalateValidation.get(SHAConstants.CMA1)) {

					for (SearchWithdrawCashLessProcessTableDTO resultDto : results) {
						resultDto.setIsZonalMedicalApprover(true);
					}
				}

				List<PedValidation> pedValidationList = SHAUtils
						.getByPreauthKey(entityManager, diagnosisList);

				List<Long> diagnosisInfo = new ArrayList<Long>();
				if (null != pedValidationList) {
					for (PedValidation pedValidation : pedValidationList) {

						diagnosisInfo.add(pedValidation.getDiagnosisId());
					}
				}

				List<Diagnosis> diagnosisValue = SHAUtils.getByDiagonsisKey(
						entityManager, diagnosisInfo);

				for (Diagnosis diagnosis : diagnosisValue) {
					for (SearchWithdrawCashLessProcessTableDTO tableDTO : results) {
						tableDTO.setDiagnosis(diagnosis.getValue());
					}
				}

				List<Hospitals> resultListForHospitalInfo = new ArrayList<Hospitals>();
				for (SearchWithdrawCashLessProcessTableDTO searchWithdrawCashLessProcessTableDTO : results) {
					
					if(searchWithdrawCashLessProcessTableDTO.getIntimationNo() != null){
						Intimation intimationByNo = getIntimationByNo(searchWithdrawCashLessProcessTableDTO.getIntimationNo());
						if(ReferenceTable.getGMCProductList().containsKey(intimationByNo.getPolicy().getProduct().getKey())){
							String colorCodeForGMC = getColorCodeForGMC(intimationByNo.getPolicy().getPolicyNumber(), intimationByNo.getInsured().getInsuredId().toString());
							searchWithdrawCashLessProcessTableDTO.setColorCode(colorCodeForGMC);
						}
					}

					List<Long> hospitalList = new ArrayList<Long>();
					hospitalList.add(searchWithdrawCashLessProcessTableDTO
							.getHospitalId());

					resultListForHospitalInfo = SHAUtils
							.getHospitalInformation(entityManager, hospitalList);

					for (Hospitals hospitalInfo : resultListForHospitalInfo) {
						searchWithdrawCashLessProcessTableDTO
								.setHospitalName(hospitalInfo.getName());
						searchWithdrawCashLessProcessTableDTO
								.setHospitalAddress(hospitalInfo.getAddress());
						searchWithdrawCashLessProcessTableDTO
								.setHospitalCity(hospitalInfo.getCity());
					}
					
					Claim claimObject = getClaimByIntimation(searchWithdrawCashLessProcessTableDTO.getIntimationNo());
					if (claimObject != null && claimObject.getCrcFlag() != null) {
						searchWithdrawCashLessProcessTableDTO.setCrmFlagged(claimObject.getCrcFlag());
					}
					if (claimObject != null && claimObject.getIsVipCustomer() != null) {
						searchWithdrawCashLessProcessTableDTO.setVipCustomer(claimObject.getIsVipCustomer());
					}
			 		if (claimObject != null && claimObject.getCrcFlaggedReason() != null) {
			 			searchWithdrawCashLessProcessTableDTO.setCrcFlaggedReason(claimObject.getCrcFlaggedReason());
			 		}
			 		if (claimObject != null && claimObject.getCrcFlaggedRemark() != null) {
			 			searchWithdrawCashLessProcessTableDTO.setCrcFlaggedRemark(claimObject.getCrcFlaggedRemark());
			 		}
			 		
			 		if (claimObject != null) {
						
						if(searchWithdrawCashLessProcessTableDTO.getCrmFlagged() != null){
							if(searchWithdrawCashLessProcessTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
								if (claimObject.getCrcFlag() != null && claimObject.getCrcFlag().equalsIgnoreCase("Y")) {
									searchWithdrawCashLessProcessTableDTO.setColorCodeCell("OLIVE");
								}
								searchWithdrawCashLessProcessTableDTO.setCrmFlagged(null);
							}
						}
						if (claimObject.getIsVipCustomer() != null && claimObject.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
							searchWithdrawCashLessProcessTableDTO.setColorCodeCell("VIP");
						}
						if (claimObject.getCrcFlag() != null && claimObject.getCrcFlag().equalsIgnoreCase("Y") 
								&& claimObject.getIsVipCustomer() != null && claimObject.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
							searchWithdrawCashLessProcessTableDTO.setColorCodeCell("CRMVIP");
						}
						
					}

				}
				Page<SearchWithdrawCashLessProcessTableDTO> page = new Page<SearchWithdrawCashLessProcessTableDTO>();
				page.setTotalRecords(totalRecords);
				page.setPageItems(results);
				return page;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

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
	
	public Claim getClaimByIntimation(String intimationno) {
		List<Claim> a_claimList = new ArrayList<Claim>();
		if (intimationno != null) {

			Query findByIntimationKey = entityManager.createNamedQuery("Claim.findByIntimationId");
			findByIntimationKey = findByIntimationKey.setParameter("intimationNumber", intimationno);
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
	
	
}
