package com.shaic.claim.fvrdetails.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.FvrNotRequired;
import com.shaic.domain.FvrTriggerPoint;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.DBCalculationService;
//import com.shaic.ims.bpm.claim.service.preauth.FieldVisitProcess;
import com.vaadin.v7.data.Property.ReadOnlyException;
import com.vaadin.ui.Notification;

@Stateless
public class ViewFVRService extends AbstractDAO<FieldVisitRequest> {

	@EJB
	private ClaimService claimService;

	@Inject
	private ViewFVRHospitalService viewFVRHospitalService;

	public ViewFVRService() {
		super();
	}

	public List<ViewFVRDTO> searchFVR(Long intimationKey) {

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<FieldVisitRequest> criteriaQuery = builder
				.createQuery(FieldVisitRequest.class);

		Root<FieldVisitRequest> searchRoot = criteriaQuery
				.from(FieldVisitRequest.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		List<FieldVisitRequest> resultList = new ArrayList<FieldVisitRequest>();

		if (intimationKey != null) {
			Predicate preAuthPredicate = builder.equal(searchRoot
					.<Intimation> get("intimation").<Long> get("key"),
					intimationKey);
			predicates.add(preAuthPredicate);
		}
		criteriaQuery.select(searchRoot).where(
				builder.and(predicates.toArray(new Predicate[] {})));
		criteriaQuery.orderBy(builder.desc(searchRoot.<Long>get("key")));
		final TypedQuery<FieldVisitRequest> oldInitiatePedQuery = entityManager.createQuery(criteriaQuery);

		resultList = oldInitiatePedQuery.getResultList();
	    
	    List<ViewFVRDTO> finalList = new ArrayList<ViewFVRDTO>();
	    
		List<ViewFVRDTO> pedValidationListDTO = ViewFVRMapper.getInstance()
				.getViewFvrDto(resultList);
		Integer index = 1;
		for (ViewFVRDTO viewFVRDTO : pedValidationListDTO) {
			SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
			viewFVRDTO.setSno(index.toString());
			index++;
			if (viewFVRDTO.getHospitalVisitedDate() != null) {
				SimpleDateFormat simpledateformat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss.S");
				try {
					Date fvrhospitalVisitDate = simpledateformat
							.parse(viewFVRDTO.getHospitalVisitedDate()
									.toString());
					viewFVRDTO.setHospitalVisitedDate(ft
							.format(fvrhospitalVisitDate));
				} catch (ReadOnlyException | ParseException e) {
					e.printStackTrace();
				}
			}

			if (viewFVRDTO.getfVRAssignedDate() != null) {
				/*SimpleDateFormat simpledateformat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss.S");*/
				try {
					/*Date fvrAssignedDate = simpledateformat.parse(viewFVRDTO
							.getfVRAssignedDate().toString());*/
					viewFVRDTO.setFvrassignedDate(ft.format(viewFVRDTO
							.getfVRAssignedDate()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (viewFVRDTO.getfVRStatus() != null && viewFVRDTO.getfVRStatus().equalsIgnoreCase(SHAConstants.FVR_GRADING_COMPLETED)) {
				viewFVRDTO.setFvrGrading("Completed");
			} else {
				
				viewFVRDTO.setFvrGrading("Pending");
				/*if(null != viewFVRDTO.getFvrStatusKey() && !ReferenceTable.getFVRNotRequiredStatus().containsKey(viewFVRDTO.getFvrStatusKey())){
					viewFVRDTO.setFvrGrading("Pending");
				}
				else
				{
					viewFVRDTO.setFvrGrading("");
				}*/
			}

			if (viewFVRDTO.getfVRReceivedDate() != null) {
				SimpleDateFormat simpledateformat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss.S");
				try {
					Date fvrReceivedDate = simpledateformat.parse(viewFVRDTO
							.getfVRReceivedDate().toString());
					viewFVRDTO.setfVRreceivedDate(ft.format(fvrReceivedDate));
				} catch (ReadOnlyException | ParseException e) {
					e.printStackTrace();
				}
			}
			if(((null != viewFVRDTO.getStatusKey() && ReferenceTable.SKIPFVR.equals(viewFVRDTO.getStatusKey())))){
				if(viewFVRDTO.getStageKey() != null && ! viewFVRDTO.getStageKey().equals(ReferenceTable.CLAIM_REGISTRATION_STAGE)){
					List<FvrTriggerPoint> fvrTriggerPoints = getFVRTriggerPoints(viewFVRDTO.getKey());
					StringBuffer remarks = new StringBuffer();
					for (FvrTriggerPoint fvrTriggerPoint : fvrTriggerPoints) {
						remarks = remarks.append(fvrTriggerPoint.getRemarks()).append(",");
					}
					if(null != remarks){
						viewFVRDTO.setRemarks(remarks.toString());
					}
				}
				
			}else{
				if(viewFVRDTO.getStageKey() != null && ! viewFVRDTO.getStageKey().equals(ReferenceTable.CLAIM_REGISTRATION_STAGE)){
					List<FvrTriggerPoint> fvrTriggerPoints = getFVRTriggerPoints(viewFVRDTO.getKey());
					StringBuffer remarks = new StringBuffer();
					for (FvrTriggerPoint fvrTriggerPoint : fvrTriggerPoints) {
						remarks = remarks.append(fvrTriggerPoint.getRemarks()).append(",");
					}
					if(null != remarks){
						viewFVRDTO.setRemarks(remarks.toString());
					}
				}
				
			}
			finalList.add(viewFVRDTO);
		}
		
		List<FvrNotRequired> fvrNotRequired = getFvrNotRequiredByIntimationKey(intimationKey);
		
		if(null != fvrNotRequired && !fvrNotRequired.isEmpty()){
			ViewFVRDTO viewFVRDTO = null;
			
			for (FvrNotRequired fvrNotRequired2 : fvrNotRequired) {
				
				viewFVRDTO = new ViewFVRDTO();
				viewFVRDTO.setRemarks(fvrNotRequired2.getFvrLovRemarks());
				viewFVRDTO.setfVRStatus(fvrNotRequired2.getStatus());
				viewFVRDTO.setHospitalName(null);
				viewFVRDTO.setFvrNotRequiredKey(fvrNotRequired2.getKey());
				finalList.add(viewFVRDTO);
			}
		}
		
		return viewFVRHospitalService.searchContactno(finalList);
	}
	
	public List<FieldVisitRequest> setOrderList(List<FieldVisitRequest> list){
		List<FieldVisitRequest> fvrList = new ArrayList<FieldVisitRequest>();
		List<Long> keyList = new ArrayList<Long>();
		Map<Long, FieldVisitRequest> fvrMap = new HashMap<Long, FieldVisitRequest>();
        return null;
	}

	public Boolean sumbitFVR(ViewFVRFormDTO bean, Intimation intimation,Long preauthKey,Long stageKey) {
		FieldVisitRequest fieldVisitRequestDO = ViewFVRMapper.getInstance()
				.getFieldVisitRequest(bean);
		
		String strUserName = bean.getUsername();
		strUserName = SHAUtils.getUserNameForDB(strUserName);
		
		Claim claim = (Claim) claimService.getClaimforIntimation(intimation
				.getKey());
		
		
		if (claim != null) {
			MastersValue allocationTo = getEntityManager().find(
					MastersValue.class, bean.getAllocateTo().getId());
			
			
			Long hospital = intimation.getHospital();
			Hospitals hospitals = getHospitalDetailsByKey(hospital);
			Long cpuId = hospitals.getCpuId();
			if(cpuId != null){
				TmpCPUCode tmpCPUCode = getTmpCPUCode(cpuId);
				fieldVisitRequestDO.setFvrCpuId(tmpCPUCode.getCpuCode());
			}
			
			Status status = new Status();
			status.setKey(ReferenceTable.INITITATE_FVR);
			Stage stage = new Stage();
			stage.setKey(18L);
			fieldVisitRequestDO.setIntimation(claim.getIntimation());
			fieldVisitRequestDO.setClaim(claim);
			fieldVisitRequestDO.setPolicy(claim.getIntimation().getPolicy());
			fieldVisitRequestDO.setActiveStatus(claim.getActiveStatus());
//			fieldVisitRequestDO.setAssignedDate(new Date());
			fieldVisitRequestDO.setStatus(status);
			fieldVisitRequestDO.setStage(stage);
			fieldVisitRequestDO.setRepresentativeName(fieldVisitRequestDO
					.getRepresentativeName());
			fieldVisitRequestDO.setAllocationTo(allocationTo);
			if(bean.getAssignTo() != null){
				MastersValue assignTo = new MastersValue();
				assignTo.setKey(bean.getAssignTo().getId());
				assignTo.setValue(bean.getAssignTo().getValue());
				fieldVisitRequestDO.setAssignTo(assignTo);
			}
			if(bean.getFvrPriority() != null){
				MastersValue priority = new MastersValue();
				priority.setKey(bean.getFvrPriority().getId());
				priority.setValue(bean.getFvrPriority().getValue());
				fieldVisitRequestDO.setPriority(priority);
			}
			fieldVisitRequestDO.setCreatedBy(strUserName);
			
			if(bean.getTrgrPtsList() != null && !bean.getTrgrPtsList().isEmpty()){
				List<ViewFVRDTO> triggerList = bean.getTrgrPtsList();
				StringBuffer trgpts = new StringBuffer("");
				for (ViewFVRDTO viewFVRDTO : triggerList) {
					trgpts.append(viewFVRDTO.getRemarks());
				}
				bean.setTriggerPoints(trgpts.toString());
				fieldVisitRequestDO.setFvrTriggerPoints(bean.getTriggerPoints());				
			}				
			fieldVisitRequestDO.setTransactionKey(preauthKey);
			fieldVisitRequestDO.setTransactionFlag("C");
			entityManager.persist(fieldVisitRequestDO);
			entityManager.flush();
			entityManager.refresh(fieldVisitRequestDO);
			if(bean.getTrgrPtsList() != null && !bean.getTrgrPtsList().isEmpty()){
				saveTriggerPoints(fieldVisitRequestDO,bean.getTrgrPtsList());
			}			
			
			Notification.show(
					"You have Sucessfully Intiated the Field Visit Request",
					Notification.Type.WARNING_MESSAGE);
		//	return initiateBPM(fieldVisitRequestDO, claim, bean,stageKey);
			return initiateDB(fieldVisitRequestDO, claim, bean,stageKey);
		} else {
			Notification.show("PreAuth not Intiated for this Intimation");
			return false;
		}
	}
	
	public void saveTriggerPoints(FieldVisitRequest fieldVisitRequestObj, List<ViewFVRDTO> trgrPtsList){
		entityManager = getEntityManager();
		try{
			if(trgrPtsList != null && !trgrPtsList.isEmpty()){
				FvrTriggerPoint newFVRTRGPtsObj = null;
				
				for (ViewFVRDTO viewFVRDTO : trgrPtsList) {
					newFVRTRGPtsObj = new FvrTriggerPoint();
					newFVRTRGPtsObj.setFvrKey(fieldVisitRequestObj.getKey());
//					newFVRTRGPtsObj.setSeqNo(trgrPtsList.indexOf(viewFVRDTO)+1);
					newFVRTRGPtsObj.setRemarks(viewFVRDTO.getRemarks());
					newFVRTRGPtsObj.setDeleteFlag("N");
					newFVRTRGPtsObj.setCreatedDate(new Date());
//					newFVRTRGPtsObj.setDataFrom(SHAConstants.GALAXY_APP);
					entityManager.persist(newFVRTRGPtsObj);
					entityManager.flush();
				}				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
//	public void saveTriggerPoints(FieldVisitRequest fieldVisitRequestObj, List<ViewFVRDTO> trgrPtsList){
//		entityManager = getEntityManager();
//		saveTriggerPoints(fieldVisitRequestObj, trgrPtsList);
//	}
	
	private TmpCPUCode getTmpCPUCode(Long cpuId){
		try{
		Query findCpuCode = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
		 List<TmpCPUCode> tmpCPUCode = (List<TmpCPUCode>) findCpuCode.getResultList();
		
		 if(tmpCPUCode != null && ! tmpCPUCode.isEmpty()){
			 return tmpCPUCode.get(0);
		 }
		 
		}catch(Exception e){
			return null;
		}
		return null;
		
	}
	
	public Hospitals getHospitalDetailsByKey(Long hospitalKey) {
		Query query = entityManager.createNamedQuery(
				"Hospitals.findByHospitalKey").setParameter("key", hospitalKey);
		Hospitals hospitals = (Hospitals) query.getSingleResult();
		if (hospitals != null) {
			return hospitals;
		}
		return null;
	}

/*	public Boolean initiateBPM(FieldVisitRequest fieldVisitRequestDO,
			Claim claim, ViewFVRFormDTO bean,Long stageKey) {
		if (fieldVisitRequestDO != null && claim != null
				&& fieldVisitRequestDO.getKey() != null) {

			Hospitals hospitals = getEntityManager().find(Hospitals.class,
					claim.getIntimation().getHospital());

			Status status = new Status();
			status.setKey(ReferenceTable.INITITATE_FVR);
			Stage stage = new Stage();
			stage.setKey(ReferenceTable.FVR_STAGE_KEY);

//			IntimationRule intimationRule = new IntimationRule();
//			IntimationMessage intimationMessage = new IntimationMessage();
//
//			intimationMessage.setAmountClaimed(claim.getClaimedAmount());
//			intimationMessage.setClaimType(claim.getClaimType().getValue());
//			intimationMessage.setCpuCode(claim.getIntimation().getCpuCode()
//					.getCpuCode().toString());
//			intimationMessage.setHospitalType(hospitals.getHospitalType()
//					.getValue());
//			intimationMessage.setIntimationNumber(claim.getIntimation()
//					.getIntimationId());
//			intimationMessage.setIsBalanceSIAvailable(intimationRule
//					.isSumInsuredPositive(claim.getIntimation()));
//			intimationMessage.setIsClaimPending(!intimationRule
//					.isClaimExist(claim.getIntimation()));
//			intimationMessage.setIsPolicyValid(intimationRule
//					.isPolicyValid(claim.getIntimation()));
//			intimationMessage.setProvisionAmount(claim.getProvisionAmount());
//			intimationMessage.setKey(claim.getIntimation().getKey());
//			intimationMessage.setStatus(status.getProcessValue());

//			PreAuthReq preAuthReq = new PreAuthReq();
//			preAuthReq.setKey(fieldVisitRequestDO.getKey());
//			System.out.println("==================================="
//					+ fieldVisitRequestDO.getKey());
//			PreAuthReqDetails preAuthReqDetails = new PreAuthReqDetails();
//			preAuthReqDetails.setPolicyId(claim.getIntimation().getPolicy()
//					.getPolicyNumber());
//			preAuthReq.setPreAuthReqDetails(preAuthReqDetails);
			
			IntimationType intMsg = new IntimationType();
			ClaimType claimType = new ClaimType();
			HospitalInfoType hospitalInfo = new HospitalInfoType();
			intMsg.setStatus(status.getProcessValue());
			claimType.setClaimType(claim.getClaimType().getValue());
			intMsg.setIntimationNumber(claim.getIntimation().getIntimationId());
			intMsg.setIsClaimPending(true);
			intMsg.setIsPolicyValid(false);
			intMsg.setIsBalanceSIAvailable(true);
			hospitalInfo.setHospitalType(hospitals.getHospitalType().getValue());
			intMsg.setKey(claim.getIntimation().getKey());

			PolicyType policyType = new PolicyType();
			policyType.setPolicyId(claim.getIntimation().getPolicy().getPolicyNumber());
			
			ProductInfoType productInfo = new ProductInfoType();
			productInfo.setLob(SHAConstants.HEALTH_LOB);
			productInfo.setLobType(SHAConstants.HEALTH_LOB_FLAG);
			
			if(claim.getIntimation().getPolicy().getProduct().getKey() != null){
				productInfo.setProductId(claim.getIntimation().getPolicy().getProduct().getKey().toString());
				productInfo.setProductName(claim.getIntimation().getPolicy().getProduct().getValue());
			}
			
			
			

//			PedReqType pedReqDetails = new PedReqType();
//			pedReqDetails.setKey(initiatePed.getKey());
//			pedReqDetails.setRequestorRole(initiatePed.getCreatedBy());
			
            Intimation intimation = claim.getIntimation();

            Insured insured = intimation.getInsured();
			
			ClassificationType classificationType = new ClassificationType();
			if(claim != null && claim.getIsVipCustomer() != null && claim.getIsVipCustomer().equals(1l)){
				
				classificationType.setPriority(SHAConstants.VIP_CUSTOMER);
			}
			else if(insured != null && insured.getInsuredAge() != null && insured.getInsuredAge()>60){
				classificationType.setPriority(SHAConstants.SENIOR_CITIZEN);
			}else{
				classificationType.setPriority(SHAConstants.NORMAL);
			}
		
			classificationType.setType(SHAConstants.TYPE_FRESH);
			
			classificationType.setSource(SHAConstants.NORMAL);
			
			Stage stageValue = entityManager.find(Stage.class, stageKey);

			if(stageValue != null){
				classificationType.setSource(stageValue.getStageName());
			}
			
			if(intimation.getAdmissionDate() != null){
				String intimDate = SHAUtils.formatIntimationDateValue(intimation.getAdmissionDate());
				intMsg.setStatus(intimDate);
			}

			
			PreAuthReqType preauthReqType = new PreAuthReqType();
			preauthReqType.setKey(fieldVisitRequestDO.getKey());
			preauthReqType.setOutcome("SUBMIT");
			
			ClaimRequestType claimRequestType = new ClaimRequestType();
			if(null != intimation.getCpuCode() && null != intimation.getCpuCode().getCpuCode())
				claimRequestType.setCpuCode(String.valueOf(intimation.getCpuCode().getCpuCode()));
			
			FieldVisitType fieldVisitType = new FieldVisitType();
			
			Long cpuId = hospitals.getCpuId();
			if(cpuId != null){
			TmpCPUCode tmpCPUCode = getTmpCPUCode(cpuId);
			fieldVisitType.setRequestedBy(tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
			claimRequestType.setCpuCode(tmpCPUCode.getCpuCode() != null ? tmpCPUCode.getCpuCode().toString() : null);
			}

			ProcessActorInfoType processActor = new ProcessActorInfoType();
			processActor.setEscalatedByRole("");
			processActor.setEscalatedByUser(bean.getUsername());
			
			PayloadBOType payloadBo = new PayloadBOType();
			
			payloadBo.setFieldVisit(fieldVisitType);
			payloadBo.setPreAuthReq(preauthReqType);
			payloadBo.setClaim(claimType);
			payloadBo.setIntimation(intMsg);
			payloadBo.setHospitalInfo(hospitalInfo);
			payloadBo.setPolicy(policyType);
			payloadBo.setClassification(classificationType);
			payloadBo.setClaimRequest(claimRequestType);
			payloadBo.setProductInfo(productInfo);
			payloadBo.setProcessActorInfo(processActor);
			
			

			// preAuthReq.setKey(preauth.getKey());
			return executeFieldVisitProcess(payloadBo, bean);
		} else {
			return false;
		}

	}*/

//	@SuppressWarnings("static-access")
//	public Boolean execute(IntimationMessage intMsg, PreAuthReq preAuthReq,
//			ViewFVRFormDTO bean) {
//		BPMClientContext instance = new BPMClientContext();
//		System.out.println("BPM Init lookup called");
//		Context context = instance.getInitialContext(bean.getUsername(),
//				bean.getPassword());
//		FieldVisitProcess fieldVisitProcess = null;
//		try {
//			fieldVisitProcess = (FieldVisitProcess) context
//					.lookup("FieldVisitProcess#com.shaic.ims.bpm.claim.service.preauth.FieldVisitProcess");
//			fieldVisitProcess.initiate(bean.getUsername(), bean.getPassword(),
//					intMsg, preAuthReq);
//			System.out.println("Look up called at server side");
//			return true;
//		} catch (NamingException e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
	
	
	public Boolean initiateDB(FieldVisitRequest fieldVisitRequestDO,
			Claim claim, ViewFVRFormDTO bean,Long stageKey) {
		if (fieldVisitRequestDO != null && claim != null
				&& fieldVisitRequestDO.getKey() != null) {

			Hospitals hospitals = getEntityManager().find(Hospitals.class,
					claim.getIntimation().getHospital());

			
			Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claim, hospitals);
			
			Object[] inputArray = (Object[])arrayListForDBCall[0];
			
			Object[] parameter = new Object[1];
			parameter[0] = inputArray;			
			
			inputArray[SHAConstants.INDEX_FVR_KEY] = fieldVisitRequestDO.getKey();
			inputArray[SHAConstants.INDEX_FVR_NUMBER] = fieldVisitRequestDO.getFvrId();
			inputArray[SHAConstants.INDEX_FVR_CPU_CODE] = String.valueOf(fieldVisitRequestDO.getFvrCpuId());
			
			if(null != stageKey && ReferenceTable.PREAUTH_STAGE.equals(stageKey))
			{
				//inputArray[SHAConstants.INDEX_STAGE_SOURCE] = SHAConstants.SOURCE_PREAUTH_PROCESS;
				inputArray[SHAConstants.INDEX_OUT_COME] =  SHAConstants.FVR_INITIATE_OUTCOME_PREAUTH;
			}
			if(null != stageKey && ReferenceTable.ENHANCEMENT_STAGE.equals(stageKey))
			{
				//inputArray[SHAConstants.INDEX_STAGE_SOURCE] = SHAConstants.SOURCE_ENHANCEMENT_PROCESS;
				inputArray[SHAConstants.INDEX_OUT_COME] =  SHAConstants.FVR_INITIATE_OUTCOME_ENHANCEMENT;
			}			
		
			//Intimation intimation = claim.getIntimation();

           // Insured insured = intimation.getInsured();
			
			/*if(claim != null && claim.getIsVipCustomer() != null && claim.getIsVipCustomer().equals(1l)){
				
				inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.VIP_CUSTOMER;
			}
			else if(insured != null && insured.getInsuredAge() != null && insured.getInsuredAge()>60){
				inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.SENIOR_CITIZEN;
			}
			else
			{
				inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.NORMAL;
			}*/
		
			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.TYPE_FRESH;
			
			//classificationType.setSource(SHAConstants.NORMAL);
			inputArray[SHAConstants.INDEX_STAGE_SOURCE] = SHAConstants.NORMAL;
//			inputArray[SHAConstants.INDEX_ESCALATE_ROLE_ID] = "";
//			inputArray[SHAConstants.INDEX_ESCALATE_USER_ID] = bean.getUsername();
			
		  
			 return executeFieldVisitProcessDB(parameter,bean);
		 
			
		} 
		
		return false;

	}

//	@SuppressWarnings("static-access")
//	public Boolean execute(IntimationMessage intMsg, PreAuthReq preAuthReq,
//			ViewFVRFormDTO bean) {
//		BPMClientContext instance = new BPMClientContext();
//		System.out.println("BPM Init lookup called");
//		Context context = instance.getInitialContext(bean.getUsername(),
//				bean.getPassword());
//		FieldVisitProcess fieldVisitProcess = null;
//		try {
//			fieldVisitProcess = (FieldVisitProcess) context
//					.lookup("FieldVisitProcess#com.shaic.ims.bpm.claim.service.preauth.FieldVisitProcess");
//			fieldVisitProcess.initiate(bean.getUsername(), bean.getPassword(),
//					intMsg, preAuthReq);
//			System.out.println("Look up called at server side");
//			return true;
//		} catch (NamingException e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
	
	/*@SuppressWarnings("static-access")
	public Boolean executeFieldVisitProcess(PayloadBOType payloadBoType,
			ViewFVRFormDTO bean) {
		BPMClientContext instance = new BPMClientContext();
		System.out.println("BPM Init lookup called");
		Context context = instance.getInitialContext(BPMClientContext.BPMN_TASK_USER,
				BPMClientContext.BPMN_PASSWORD);
		FieldVisitProcess fieldVisitProcess = null;
		try {
			
			fieldVisitProcess = (FieldVisitProcess) context
					.lookup("FieldVisitProcess#com.shaic.ims.bpm.claim.servicev2.preauth.FieldVisitProcess");
			try{
			fieldVisitProcess.initiate(BPMClientContext.BPMN_TASK_USER,payloadBoType);
			}catch(Exception e){
				e.printStackTrace();
			}
			System.out.println("Look up called at server side");
			return true;
		} catch (NamingException e) {
			e.printStackTrace();
			return false;
		}
	}*/

	@SuppressWarnings("static-access")
	public Boolean executeFieldVisitProcessDB(Object[] parameter,
			ViewFVRFormDTO bean) {
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		String successMsg = dbCalculationService.revisedInitiateTaskProcedure(parameter);
		if(SHAConstants.SUCCESS_FLAG.equalsIgnoreCase(successMsg))
		{
			return true;
		}
		
		return false;
	}
	@Override
	public Class<FieldVisitRequest> getDTOClass() {
		return FieldVisitRequest.class;
	}
		
	public List<FvrNotRequired> getFvrNotRequiredByIntimationKey(Long intimationKey) {
		Query query = entityManager.createNamedQuery(
				"FvrNotRequired.findByIntimationKey").setParameter("intimationKey", intimationKey);
		List<FvrNotRequired> fvrNotRequired = (List<FvrNotRequired>) query.getResultList();
		if (fvrNotRequired != null && !fvrNotRequired.isEmpty()) {
			return fvrNotRequired;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public FieldVisitRequest getFVRByClaimKey(Long claimKey){
		
		Query query = entityManager.createNamedQuery(
				"FieldVisitRequest.findFvrByClaimKey").setParameter("claimKey", claimKey);
		
		List<FieldVisitRequest> fvrList=(List<FieldVisitRequest>) query.getResultList();
		if(fvrList != null && ! fvrList.isEmpty()){
			
				entityManager.refresh(fvrList.get(0));
				return fvrList.get(0);
		}
		
		return null;
		
	}
	
	public void saveAdditionalTriggerPoints(FieldVisitRequest fieldVisitRequestObj, List<ViewFVRDTO> trgrPtsList,PreauthDTO bean){
		entityManager = getEntityManager();
		try{
			if(trgrPtsList != null && !trgrPtsList.isEmpty()){
				FvrTriggerPoint newFVRTRGPtsObj = null;
				
				for (ViewFVRDTO viewFVRDTO : trgrPtsList) {
						if(null != viewFVRDTO.getRemarks() && !("").equalsIgnoreCase(viewFVRDTO.getRemarks()))
						{
						newFVRTRGPtsObj = new FvrTriggerPoint();
						newFVRTRGPtsObj.setKey(viewFVRDTO.getKey());
						newFVRTRGPtsObj.setFvrKey(fieldVisitRequestObj.getKey());
						newFVRTRGPtsObj.setRemarks(viewFVRDTO.getRemarks());
						viewFVRDTO.setExistingTriggerPoint(viewFVRDTO.getRemarks());
						newFVRTRGPtsObj.setDeleteFlag(SHAConstants.N_FLAG);
						if(null != newFVRTRGPtsObj.getKey()){
							entityManager.merge(newFVRTRGPtsObj);
							entityManager.flush();
						}
						else
						{
							newFVRTRGPtsObj.setCreatedDate(new Date());
							newFVRTRGPtsObj.setCreatedBy(bean.getStrUserName());
							newFVRTRGPtsObj.setTriggerPointsChanged(SHAConstants.YES_FLAG);
							entityManager.persist(newFVRTRGPtsObj);
							entityManager.flush();
							viewFVRDTO.setKey(newFVRTRGPtsObj.getKey());
						}
						
						if(null != viewFVRDTO.getDeletedList() && !viewFVRDTO.getDeletedList().isEmpty()){
							List<ViewFVRDTO> deletedList = viewFVRDTO.getDeletedList();
							for (ViewFVRDTO viewFVRDTO2 : deletedList) {
								newFVRTRGPtsObj.setDeleteFlag(SHAConstants.YES_FLAG);
								newFVRTRGPtsObj.setModifiedBy(bean.getStrUserName());
								newFVRTRGPtsObj.setModifiedDate(new Date());
								entityManager.merge(newFVRTRGPtsObj);
								entityManager.flush();
							}
						}
					}
			}
		}
	}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<FvrTriggerPoint> getFVRTriggerPoints(Long fvrKey) {
		Query query = entityManager
				.createNamedQuery("FvrTriggerPoint.findByKey").setParameter("fvrKey", fvrKey);
		List<FvrTriggerPoint> singleResult = query.getResultList();
		if (singleResult != null) {
			for (FvrTriggerPoint fvrGradingMaster : singleResult) {
				entityManager.refresh(fvrGradingMaster);
			}

		}

		return singleResult;
	}
}