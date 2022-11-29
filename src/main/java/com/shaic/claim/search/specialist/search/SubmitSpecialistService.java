package com.shaic.claim.search.specialist.search;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.submitSpecialist.SubmitSpecialistDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.PreauthEscalate;
import com.shaic.domain.reimbursement.Specialist;
import com.shaic.domain.reimbursement.SpecialistMapping;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.specialistprocessing.submitspecialist.search.SearchSubmitSpecialistAdviseTableDTO;
//import com.shaic.ims.bpm.claim.HumanTask;
import com.vaadin.v7.data.util.BeanItemContainer;

@Stateless
public class SubmitSpecialistService  extends  AbstractDAO<PreauthEscalate>  {
	
	public SubmitSpecialistService(){
		
	}
	
	/*public Page<SubmitSpecialistTableDTO> bpmnSearch(SubmitSpecialistFormDTO formDTO, Boolean reimburementFlag)
	{
		try 
		{
			String strIntimationNo = "";
			String strPolicyNo = "";
			SelectValue refByCode = formDTO.getRefferedBy();
			String strRefBy = "";
			
			String priority = null != formDTO  && formDTO.getPriority() != null ? formDTO.getPriority().getValue() != null ? formDTO.getPriority().getValue() : null : null;
			String source = null != formDTO  && formDTO.getSource() != null ? formDTO.getSource().getValue() != null ? formDTO.getSource().getValue(): null : null;
			String type = null != formDTO  && formDTO.getType() != null ? formDTO.getType().getValue() != null ? formDTO.getType().getValue(): null : null;
			String specialistType = formDTO.getSpecialistType() != null ? formDTO.getSpecialistType().getValue() != null ? formDTO.getSpecialistType().getValue(): null : null;
			
			com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBO = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();;
			IntimationType intimationType = new IntimationType();
			PolicyType policyType = new PolicyType();
			PedReqType pedType = new PedReqType();
			
			
			
			ProductInfoType productInfo = new ProductInfoType();
			
			
			intimationType.setReason("HEALTH");
			
//			productInfo.setLob("H");

			
			payloadBO.setProductInfo(productInfo);
			
			payloadBO.setIntimation(intimationType);
			
			
			if(null != formDTO && null != formDTO.getIntimationNo())
			{
				strIntimationNo = formDTO.getIntimationNo();
				if(strIntimationNo != null && strIntimationNo.length() > 0){
					if(payloadBO == null){
						payloadBO = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
					}
					intimationType.setIntimationNumber(strIntimationNo);
					payloadBO.setIntimation(intimationType);
				}
				
			}
			
			if(null != formDTO && null != formDTO.getPolicyNo())
			{
				strPolicyNo = formDTO.getPolicyNo();
				if(strPolicyNo != null && strPolicyNo.length() > 0){
					
					if(payloadBO == null){
						payloadBO = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
					}
					policyType.setPolicyId(strPolicyNo);
					payloadBO.setPolicy(policyType);
				}
				
			}
			
			if(null != formDTO && null != refByCode)
			{
				strRefBy = String.valueOf(refByCode.getValue());
				if(strRefBy != null && strRefBy.length() > 0){
					if(payloadBO == null){
						payloadBO = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
					}
					pedType.setReferredBy(strRefBy);
					payloadBO.setPedReq(pedType);
				}
			}
			
			CustomerType customerType = new CustomerType();
			if(specialistType != null){
				
				if(payloadBO == null){
					payloadBO = new PayloadBOType();
				}
				customerType.setSpeciality(specialistType);
				payloadBO.setCustomer(customerType);
			}else{
				
				if(payloadBO == null){
					payloadBO = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
				}
				
				List<SpecialistMapping> specialityForUser = getSpecialityForUser(formDTO.getUsername());
				String speciality = "";
				if(specialityForUser != null && ! specialityForUser.isEmpty()){
					for (SpecialistMapping specialistMapping : specialityForUser) {
						if(specialistMapping.getSpecialistType() != null){
							speciality += specialistMapping.getSpecialistType()+",";
						}
					}
					if(! speciality.equalsIgnoreCase("")){
						speciality = speciality.substring(0, speciality.length()-1);
					}
					}
					
				
				customerType.setSpeciality(speciality);
				payloadBO.setCustomer(customerType);
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
			
			*//**
			 * BPM service is still not given. Hence using the 
			 * convert claim search BPM service as of now.
			 * Once the BPM service is up for this search menu,
			 * will replace the below given service with the
			 * valid one.
			 * 
			 * *//*
			SpecialistAdviseQF downSizeSpecialistQF = null;
			
			
			if(!((null == strIntimationNo || ("").equals(strIntimationNo)) && (null == strPolicyNo || ("").equals(strPolicyNo)) && (null == strRefBy || ("").equals(strRefBy))))
			{
				
				downSizeSpecialistQF = new SpecialistAdviseQF();
				downSizeSpecialistQF.setIntimationNumber(strIntimationNo);
				downSizeSpecialistQF.setPolicyId(strPolicyNo);
			}
			
			Pageable pageable = formDTO.getPageable();
			
			
			List<SubmitSpecialistTableDTO> searchSpecialistAdviseTableDTO  = new ArrayList<SubmitSpecialistTableDTO>();
			com.shaic.ims.bpm.claim.servicev2.preauth.search.SubmitSpecialistAdviseTask specialistAdviseTask = BPMClientContext.getSubmitSpecialistAdvise(formDTO.getUsername(),formDTO.getPassword());
			Map<Long, HumanTask> humanTaskMap = new HashMap<Long, HumanTask>();
			Map<Long, Integer> taskNumberMap = new HashMap<Long, Integer>();
			
			
			com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = specialistAdviseTask.getTasks(formDTO.getUsername(), formDTO.getPageable(), payloadBO);  //user Name="zma1"
			if (null != tasks)
			{
				List<HumanTask> humanTasks  = tasks.getHumanTasks(); 
				List<Long> keys = new ArrayList<Long>();  
				for (HumanTask item: humanTasks)
				{
					PayloadBOType payloadBOCashless = item.getPayloadCashless();
					PreAuthReqType preauthReqType = payloadBOCashless.getPreAuthReq();
					if(null != preauthReqType)
					{
						Long keyValue = preauthReqType.getKey();
						humanTaskMap.put(keyValue, item);
						taskNumberMap.put(keyValue,item.getNumber());
						keys.add(keyValue);				
					}
					
					//Map<String, String> valuesFromBPM = BPMClientContext.getMapFromPayload(item.getPayload(), "PreAuthReq");
					Long keyValue = Long.valueOf(valuesFromBPM.get("key"));
					humanTaskMap.put(keyValue, item);
					keys.add(keyValue);				
				}
				
				//keys.add(194l);
				
				DownsizeSpecialistAdviseTask downsizeSpecialistTask = BPMClientContext.getDownsizeSpecialistAdviseTask(formDTO.getUsername(), formDTO.getPassword());
				PagedTaskList tasks2 = downsizeSpecialistTask.getTasks(formDTO.getUsername(), formDTO.getPageable(), payloadBO);
				if (null != tasks2)
				{
					List<HumanTask> humanTasks2  = tasks2.getHumanTasks();  
					for (HumanTask item: humanTasks2)
					{
						PayloadBOType payloadBOCashless = item.getPayloadCashless();
						PreAuthReqType preauthReqType = payloadBOCashless.getPreAuthReq();
						if(null != preauthReqType)
						{
							Long keyValue = preauthReqType.getKey();
							humanTaskMap.put(keyValue, item);
							keys.add(keyValue);				
						}
						
						//Map<String, String> valuesFromBPM = BPMClientContext.getMapFromPayload(item.getPayload(), "PreAuthReq");
						Long keyValue = Long.valueOf(valuesFromBPM.get("key"));
						humanTaskMap.put(keyValue, item);
						keys.add(keyValue);				
					}
				}
				
				if(null != keys && 0!= keys.size())
				{
					List<Preauth> resultList = new ArrayList<Preauth>();
					final CriteriaBuilder IntimationBuilder = entityManager.getCriteriaBuilder();
					final CriteriaQuery<Preauth> criteriaQuery = IntimationBuilder
							.createQuery(Preauth.class);
					Root<Preauth> searchRootForIntimation = criteriaQuery.from(Preauth.class);
					criteriaQuery.where(searchRootForIntimation.<Long> get("key").in(keys));		
					final TypedQuery<Preauth> intimateInfoQuery = entityManager
							.createQuery(criteriaQuery);
					//resultList =  intimateInfoQuery.getResultList();
					resultList = SHAUtils.getIntimationInformation(SHAConstants.PREAUTH_SEARCH_SCREEN, entityManager, keys);
					List<Preauth> pageItemList = resultList;
					searchSpecialistAdviseTableDTO = SubmitSpecialistMapper.getInstance()
							.getSearchAdviseOnPEDTableDTO(resultList);
					for (SubmitSpecialistTableDTO tableDTO : searchSpecialistAdviseTableDTO) {
						
						if(tableDTO.getCpuId() != null){
							tableDTO.setCpuCode(tableDTO.getCpuId().toString());
						}
						
                         if(tableDTO.getHospitalkey() != null){
                        	 Hospitals hospitalDetails = getHospitalDetails(tableDTO.getHospitalkey());
                        	 if(hospitalDetails != null){
                        		 tableDTO.setHospitalName(hospitalDetails.getName());
                        		 tableDTO.setHospitalCity(hospitalDetails.getCity());
                        		 tableDTO.setHospitalAddress(hospitalDetails.getAddress());
                        	 }
                         }
                         
                         if(tableDTO.getKey() != null){
                        	 Specialist submitSpecialist = getSubmitSpecialist(tableDTO.getKey());
                        	 if(submitSpecialist != null){
                        		 if(submitSpecialist.getSpcialistType() != null && submitSpecialist.getSpcialistType().getValue() != null){
                        			 tableDTO.setSpecialityType(submitSpecialist.getSpcialistType().getValue());
                        		 }
                        		 
                        		 if(submitSpecialist.getCreatedDate() != null){
        				    		 String formatDate = SHAUtils.formatDate(submitSpecialist.getCreatedDate());
        				    		 tableDTO.setDateOfRefer(formatDate);
        				    	 }
                        		 
                        		tableDTO.setDoctorComments(submitSpecialist.getReasonForReferring());
                        		tableDTO.setReferredBy(submitSpecialist.getCreatedBy());
                        		TmpEmployee employeeDetails = getEmployeeDetails(submitSpecialist.getCreatedBy());
                        		if(employeeDetails != null){
                        			tableDTO.setDoctorName(employeeDetails.getEmpFirstName());
                        		}
                        		tableDTO.setDoctorId(submitSpecialist.getCreatedBy());
                        		
                        	 }
                         }
					}
				}
			}
			
			for (SubmitSpecialistTableDTO submitSpecialistTableDTO : searchSpecialistAdviseTableDTO) {
				submitSpecialistTableDTO.setHumanTask(humanTaskMap.get(submitSpecialistTableDTO.getKey()));
				submitSpecialistTableDTO.setTaskNumber(taskNumberMap.get(submitSpecialistTableDTO.getKey()));
				submitSpecialistTableDTO.setIsReimburementFlag(reimburementFlag);
			}
			
			for (SubmitSpecialistTableDTO submitSpecialistTableDTO : searchSpecialistAdviseTableDTO) {
			      String lob = submitSpecialistTableDTO.getLob();
			      if(lob != null){
			    	  MastersValue master = getMaster(Long.valueOf(lob));
			    	  if(master != null){
			    		  submitSpecialistTableDTO.setLob(master.getValue());
			    	  }
			      }
			}
			Page<PreauthEscalate> pagedList = super.pagedList(formDTO.getPageable());
			Page<SubmitSpecialistTableDTO> page = new Page<SubmitSpecialistTableDTO>();
			page.setPageNumber(pagedList.getPageNumber());
			page.setPageItems(searchSpecialistAdviseTableDTO);
			page.setPagesAvailable(pagedList.getPagesAvailable());
			page.setTotalRecords(tasks.getTotalRecords());
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}*/
	
	
	public TmpEmployee getEmployeeDetails(String loginId) {
		TmpEmployee tmpEmployee = null;
		if(loginId != null){
			loginId = loginId.toLowerCase();
			/*
			 * Query query = entityManager
			 * .createNamedQuery("TmpEmployee.findByEmpName"); query =
			 * query.setParameter("empName", empName);
			 */
			Query query = entityManager
					.createNamedQuery("TmpEmployee.getEmpByLoginId");// .setParameter("primaryKey",
																		// key);
			query.setParameter("loginId", "%" + loginId + "%");
			List<TmpEmployee> tmpEmployeeList = query.getResultList();
			for (TmpEmployee tmpEmployee2 : tmpEmployeeList) {
				tmpEmployee = tmpEmployee2;
			}
			// TmpEmployee tmpEmployee = (TmpEmployee)query.getSingleResult();
			return tmpEmployee;
		}else{
			return null;
		}
	}
	
	public Hospitals getHospitalDetails(Long hospitalkey){
		
		
		Query findByHospitalKey = entityManager.createNamedQuery(
				"Hospitals.findByKey").setParameter("key",hospitalkey);
		
		List<Hospitals> hospitals = (List<Hospitals>)findByHospitalKey.getResultList();
		
		if(hospitals != null && ! hospitals.isEmpty()){
			return hospitals.get(0);
		}
		
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public MastersValue getMaster(Long a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		MastersValue a_mastersValue = new MastersValue();
		if (a_key != null) {
			Query query = entityManager
					.createNamedQuery("MastersValue.findByKey");
			query = query.setParameter("parentKey", a_key);
			List<MastersValue> mastersValueList = query.getResultList();
			for (MastersValue mastersValue : mastersValueList)
				a_mastersValue = mastersValue;
		}

		return a_mastersValue;
	}
	
	@SuppressWarnings("unchecked")
	public PreauthEscalate getSubmitAdviseByKey(Long submitAdviseKey) {
		
		Query findByKey = entityManager.createNamedQuery("Claim.findAll");

		List<PreauthEscalate> claimKeyList = (List<PreauthEscalate>) findByKey
				.getResultList();

		if (!claimKeyList.isEmpty()) {
			return claimKeyList.get(0);

		}
		return null;
	}
	
	public Page<SubmitSpecialistDTO> findByEsclateId(SubmitSpecialistTableDTO bean){
		
		SubmitSpecialistDTO formDTO=new SubmitSpecialistDTO();
		
		formDTO.setPageable(new Pageable(1, 1, 10));
		formDTO.getPageable().setPageSize(10);
		
		Query findByKey = entityManager.createNamedQuery("PreauthEscalate.findByEsclateId").setParameter("escalateKey", 234l);    //because 234 is specialist Doctor

		@SuppressWarnings("unchecked")
		List<PreauthEscalate> preauthEscalateList = (List<PreauthEscalate>) findByKey
				.getResultList();

		List<SubmitSpecialistDTO> listOfSubmitSpecialist=SubmitSpecialistMapper.getInstance().getSubmitSpecialistDTO(preauthEscalateList);
		
		
		Page<PreauthEscalate> pagedList = super.pagedList(formDTO.getPageable());
		pagedList.setPageNumber(1);
		Page<SubmitSpecialistDTO> page = new Page<SubmitSpecialistDTO>();
		//page.setPageNumber(pagedList.getPageNumber());
		page.setPageItems(listOfSubmitSpecialist);
		page.setPagesAvailable(pagedList.getPagesAvailable());
		return page;
		
	}
	
	public Page<SubmitSpecialistDTO> findSpecialistByClaimKey(SubmitSpecialistTableDTO bean){
		
		SubmitSpecialistDTO formDTO = new SubmitSpecialistDTO();
		
		formDTO.setPageable(new Pageable(1, 1, 10));
	    formDTO.getPageable().setPageSize(10);
	    
	    Query findByClaimKey = entityManager.createNamedQuery("Specialist.findByClaimKey").setParameter("claimKey", bean.getClaimKey());
	    
	    List<Specialist> specialistResult = (List<Specialist>) findByClaimKey.getResultList();
	    
	    List<SubmitSpecialistDTO> submitSpecialist = new ArrayList<SubmitSpecialistDTO>();
	    
	    for (Specialist specialist : specialistResult) {
			SubmitSpecialistDTO dto = new SubmitSpecialistDTO();
			dto.setKey(specialist.getKey());
			if(specialist.getCreatedDate() != null){
			dto.setRequestedDate(specialist.getCreatedDate().toString());
			}
			dto.setRequestorRemarks(specialist.getReasonForReferring());
			dto.setSpecialistRemarks(specialist.getSpecialistRemarks());
			submitSpecialist.add(dto);
		}
	    Page<PreauthEscalate> pagedList = super.pagedList(formDTO.getPageable());
		pagedList.setPageNumber(1);
		Page<SubmitSpecialistDTO> page = new Page<SubmitSpecialistDTO>();
		//page.setPageNumber(pagedList.getPageNumber());
		page.setPageItems(submitSpecialist);
		page.setPagesAvailable(pagedList.getPagesAvailable());
		return page;
		
	}
	
    public Page<SubmitSpecialistDTO> findByEsclate(SearchSubmitSpecialistAdviseTableDTO bean){
		
//    	SearchSubmitSpecialistAdviseTableDTO formDTO=new SearchSubmitSpecialistAdviseTableDTO();
////		
////		formDTO.setPageable(new Pageable(1, 1, 10));
////		formDTO.getPageable().setPageSize(10);
//		
//		Query findByKey = entityManager.createNamedQuery("PreauthEscalate.findByEsclateId").setParameter("escalateKey", 234l);    //because 234 is specialist Doctor
//
//		@SuppressWarnings("unchecked")
//		List<PreauthEscalate> preauthEscalateList = (List<PreauthEscalate>) findByKey
//				.getResultList();
////		List<SearchSubmitSpecialistAdviseTableDTO> listOfSubmitSpecialist=SubmitSpecialistMapper.getSubmitSpecialistDTO(preauthEscalateList);
//		
//		
////		Page<PreauthEscalate> pagedList = super.pagedList(formDTO.getPageable());
//		pagedList.setPageNumber(1);
//		Page<SearchSubmitSpecialistAdviseTableDTO> page = new Page<SearchSubmitSpecialistAdviseTableDTO>();
//		//page.setPageNumber(pagedList.getPageNumber());
////		page.setPageItems(listOfSubmitSpecialist);
//		page.setPagesAvailable(pagedList.getPagesAvailable());
//		return page;
    	return null;
		
	}
	
	public Boolean submitSpecialist(SubmitSpecialistDTO bean,SubmitSpecialistTableDTO searchDto){
		
		Query findByKey = entityManager.createNamedQuery("PreauthEscalate.findByKey").setParameter("parentKey", bean.getKey());
		
		PreauthEscalate existingList=(PreauthEscalate)findByKey.getSingleResult();
		
		if(existingList!=null){
			existingList.setSpecialistRemarks(bean.getSpecialistRemarks());
			entityManager.merge(existingList);
			entityManager.flush();
			entityManager.clear();
			
		//	setBPMOutCome(bean, searchDto, "APPROVE",existingList.getStatus());
			
			return true;
		}
		
		return false;
	}
	
	public Boolean submitSpecialistforReimbursement(SubmitSpecialistDTO bean, SubmitSpecialistTableDTO searchDto){
		
		Boolean specialistValue = Boolean.FALSE;
		
		Query findByKey = entityManager.createNamedQuery("Specialist.findKey").setParameter("primaryKey", bean.getKey());
		
		Specialist existingList = (Specialist) findByKey.getSingleResult();
		
		
		
		Status status = new Status();
//		status.setKey(ReferenceTable.SPECIALIST_REPLY_RECEIVED);
		
		if(existingList.getStage() != null){
			
			if(existingList.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)){
				status.setKey(ReferenceTable.CLAIM_REQUEST_SPECIALIST_REPLY_RECEIVED_STATUS);
			}else if(existingList.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE)){
				status.setKey(ReferenceTable.FINANCIAL_SPECIALIST_REPLY_RECEIVED_STATUS);
			}
		}
		
		if(existingList.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_SPECIALIST_STATUS)  || 
				existingList.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS)
				|| existingList.getStatus().getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_SPECIALIST)){
			existingList.setStatus(status);
		}
		
		
		if(existingList != null){
			String userNameForDB = SHAUtils.getUserNameForDB(searchDto.getUsername());
			existingList.setModifiedBy(userNameForDB);
			existingList.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			existingList.setSpecialistRemarks(bean.getSpecialistRemarks());
			if(bean.getTmpFileName() != null){
				existingList.setFileName(bean.getTmpFileName());
			}
			if(bean.getTmpFileToken() != null){
				existingList.setFileToken(bean.getTmpFileToken());
			}
			entityManager.merge(existingList);
			entityManager.flush();
			entityManager.clear();
			
			if(searchDto.getRodKey() != null){
				updateReimbursementStatus(searchDto.getRodKey(), status, searchDto.getUsername());
			}
			
			submitSpecialistAdviseReimbTaskToDB(bean, searchDto, "SUBMIT",existingList);
			
//			return true;
			
			specialistValue = Boolean.TRUE;
		}
		return specialistValue;	
	}
	
	public void updateReimbursementStatus(Long reimbursmentKey,Status status,String userName){
		
		Reimbursement reimbursement = getReimbursementByKey(reimbursmentKey);
		if(reimbursement != null){
			reimbursement.setStatus(status);
			String userNameForDB = SHAUtils.getUserNameForDB(userName);
			reimbursement.setModifiedBy(userNameForDB);
			reimbursement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(reimbursement);
			entityManager.flush();
			entityManager.clear();
			
//			Claim claim = reimbursement.getClaim();
//			claim.setStage(reimbursement.getStage());
//			claim.setStatus(status);
//			entityManager.merge(claim);
//			entityManager.flush();
		}
		
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
	
  public Boolean submitSpecialistforPreauth(SubmitSpecialistDTO bean, SubmitSpecialistTableDTO searchDto){
		
		Query findByKey = entityManager.createNamedQuery("Specialist.findKey").setParameter("primaryKey", bean.getKey());
		
		Status status = new Status();

		Specialist existingList = (Specialist) findByKey.getSingleResult();
		Preauth preauth = null;
		if(existingList != null){
			entityManager.refresh(existingList);
			
			status.setKey(ReferenceTable.SPECIALIST_REPLY_RECEIVED);
			
			if(existingList.getStatus().getKey().equals(ReferenceTable.PREAUTH_ESCALATION_STATUS) 
					|| existingList.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_ESCALATION_STATUS)){
			
			status.setKey(ReferenceTable.SPECIALIST_REPLY_RECEIVED);
			existingList.setStatus(status);
			}else if(existingList.getStatus().getKey().equals(ReferenceTable.DOWNSIZE_ESCALATION_STATUS)){
				
				status.setKey(ReferenceTable.DOWNSIZE_SUBMIT_SPECIALIST_REPLY);
				existingList.setStatus(status);
			}
			
			existingList.setSpecialistRemarks(bean.getSpecialistRemarks());
			if(bean.getTmpFileName() != null){
				existingList.setFileName(bean.getTmpFileName());
			}
			if(bean.getTmpFileToken() != null){
				existingList.setFileToken(bean.getTmpFileToken());
			}
			
			String strUserName = searchDto.getUsername();
			strUserName = SHAUtils.getUserNameForDB(strUserName);
			existingList.setModifiedBy(strUserName);
			existingList.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(existingList);
			entityManager.flush();
			entityManager.clear();
			
			if(existingList.getTransactionFlag() != null && existingList.getTransactionFlag().equalsIgnoreCase("C")
					&& existingList.getTransactionKey() != null){

				preauth = getPreauthById(existingList.getTransactionKey());
				
				if(preauth != null){
					preauth.setStatus(status);
					entityManager.merge(preauth);
					entityManager.flush();
					entityManager.clear();
				}
				
			}
		}
		
			String outCome = SHAConstants.OUTCOME_FOR_SUBMIT_SPECIALIST_PREAUTH;

			
			if(preauth.getStage().getKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE) || 
					preauth.getStage().getKey().equals(ReferenceTable.ENHANCEMENT_STAGE)){
				
				outCome = SHAConstants.OUTCOME_FOR_SUBMIT_SPECIALIST_ENHANCEMENT;
				
			} else if (preauth.getStage().getKey().equals(ReferenceTable.DOWNSIZE_STAGE)) {
				outCome = SHAConstants.OUTCOME_FOR_SUBMIT_SPECIALIST_DOWNSIZE;
			}
			
			
			
			
//			setBPMOutCome(bean, searchDto, outCome,status);
			
			setDBOutComeForCashlessSpecialist(searchDto,outCome);
			
			return true;
		
	}
  
	public Preauth getPreauthById(Long preauthKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByKey");
		query.setParameter("preauthKey", preauthKey);
		@SuppressWarnings("unchecked")
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()) {
			entityManager.refresh(singleResult.get(0));
			return singleResult.get(0);
		}
		
		return null;
		
		
	}

       public SubmitSpecialistDTO findSpecialistValue(SubmitSpecialistTableDTO bean){
		
		//SubmitSpecialistDTO formDTO = new SubmitSpecialistDTO();
		
	    Query findByClaimKey = entityManager.createNamedQuery("Specialist.findByClaimKey").setParameter("claimKey", bean.getClaimKey());
	    
	    List<Specialist> specialistResult = (List<Specialist>) findByClaimKey.getResultList();
	    
	    List<SubmitSpecialistDTO> submitSpecialist = new ArrayList<SubmitSpecialistDTO>();
	    
	    for (Specialist specialist : specialistResult) {
			SubmitSpecialistDTO dto = new SubmitSpecialistDTO();
			dto.setKey(specialist.getKey());
			if(specialist.getCreatedDate() != null){
			dto.setRequestedDate(SHAUtils.formatDate(specialist.getCreatedDate()));
			}
			dto.setRequestorRemarks(specialist.getReasonForReferring());
			dto.setFileName(specialist.getFileName());
			dto.setFileToken(specialist.getFileToken());
//			dto.setSpecialistRemarks(specialist.getSpecialistRemarks());
			submitSpecialist.add(dto);
		}
	   
		if(! submitSpecialist.isEmpty()){
			return submitSpecialist.get(0);
		}
		
		return null;
		
	}
	
	public void submitSpecialistAdviseReimbTaskToDB(SubmitSpecialistDTO bean, SubmitSpecialistTableDTO searchFormDto,String outCome,Specialist specialist){
		
		
		Map<String, Object> wrkFlowMap = (Map<String, Object>) searchFormDto.getDbOutArray();
		String reimbReplyBy = (String) wrkFlowMap.get(SHAConstants.PAYLOAD_REIMB_REQ_BY);
		
		wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_SUBMIT_SPECIALIST_ADVISE);
		if(SHAConstants.MA_CURRENT_QUEUE.equalsIgnoreCase(reimbReplyBy)) 
		{
			wrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.MA_STAGE_SOURCE);
		}
		else if (SHAConstants.FA_CURRENT_QUEUE.equalsIgnoreCase(reimbReplyBy))
		{
			wrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.FA_STAGE_SOURCE);
		}
		
		wrkFlowMap.put(SHAConstants.STAGE_SOURCE, SHAConstants.SOURCE_SPECIALIST_REPLY);
		wrkFlowMap.put(SHAConstants.ALLOCATED_USER, null);
		wrkFlowMap.put(SHAConstants.PAYLOAD_RRC_REQUEST_NUMBER,SHAConstants.PARALLEL_MEDICAL_PENDING);
		
		Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
		
		DBCalculationService dbCalService = new DBCalculationService();
		//dbCalService.initiateTaskProcedure(objArrayForSubmit);
		try{
			dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Status getStatusByKey(Long key) {

		try {
			Query findType = entityManager.createNamedQuery("Status.findByKey")
					.setParameter("statusKey", key);
			List<Status> status = findType.getResultList();
			if(null != status && !status.isEmpty())
			{
				entityManager.refresh(status.get(0));
				return status.get(0);
			}
			return null ;
		} catch (Exception e) {
			return null;
		}

	}
	
	private Specialist getSubmitSpecialist(Long transactionKey){
		
		 Query findByClaimKey = entityManager.createNamedQuery("Specialist.findByTransactionKey").setParameter("transactionKey", transactionKey);
		 List<Specialist> specialistResult = (List<Specialist>) findByClaimKey.getResultList();
		 
		 if(specialistResult != null && ! specialistResult.isEmpty()){
			 return specialistResult.get(0);
		 }
		 
		 return null;
	}
	
/*	public void setBPMOutCome(SubmitSpecialistDTO bean,SubmitSpecialistTableDTO searchFormDto,String outCome,Status status){
		if (searchFormDto != null) {
			
			//HumanTask humanTask = searchFormDto.getHumanTask();
			//PayloadBOType payloadBO = humanTask.getPayloadCashless();
	//		XMLElement payload = searchFormDto.getHumanTask().getPayload();
			Map<String, String> regIntDetailsReq = new HashMap<String, String>();
			Map<String,String> preauthReq=new HashMap<String, String>();
			
			//PreAuthReqType preauthReqType = payloadBO.getPreAuthReq();
			//IntimationType intimationType = payloadBO.getIntimation();
		
			
			if(searchFormDto.getKey()!=null){
				
				preauthReq.put("key", searchFormDto.getKey().toString());
				
				if(null != preauthReqType)
				{
					preauthReqType.
				}
			}			
			if(searchFormDto.getClaimNo()!=null){
				regIntDetailsReq.put("IntimationNumber",searchFormDto.getIntimationNo());
				if(null != intimationType && null != searchFormDto.getIntimationNo())
				{
					intimationType.setIntimationNumber(searchFormDto.getIntimationNo());
				}
				else
				{
					intimationType = new IntimationType();
					intimationType.setIntimationNumber(searchFormDto.getIntimationNo());
				}
			}
			
			payloadBO.setIntimation(intimationType);
			
			*//**
			 * Reg int details need to check with joseph.
			 * *//*
		//	payload = BPMClientContext.setNodeValue(payload, "RegIntDetails", regIntDetailsReq);
		//searchFormDto.getHumanTask().setOutcome(outCome);
		if(status.getKey().equals(ReferenceTable.SPECIALIST_REPLY_RECEIVED)){
			humanTask.setOutcome(outCome);
		}else{
			humanTask.setOutcome("APPROVE");
		}
		
		
		payloadBO.getPreAuthReq().setOutcome(outCome);
		payloadBO.getPreAuthReq().setResult(outCome);
		
		ClassificationType classification = payloadBO.getClassification();
		
		status = entityManager.find(Status.class, status.getKey());
		
		classification.setSource(status.getProcessValue());
		payloadBO.setClassification(classification);
		
		humanTask.setPayloadCashless(payloadBO);
	    PreAuthReq pedReqDetails=new PreAuthReq();
	    pedReqDetails.setKey(Long.valueOf(preauthReq.get("key")));
		//searchFormDto.getHumanTask().setPayload(payload);
	    //searchFormDto.getHumanTask().setPayloadCashless(payloadBO);
				
				try {
					BPMClientContext.printPayloadElement(searchFormDto.getHumanTask().getPayload());
				} catch (TransformerException e) {
					e.printStackTrace();
				}
		PayloadBOType payloadCashless = humanTask.getPayloadCashless();
		
//		payloadCashless.getPreAuthReq().setOutcome(outCome);
//		payloadCashless.getPreAuthReq().setResult(outCome);
		
		
		if(payloadCashless.getPreAuthReq() != null && payloadCashless.getPreAuthReq().getApproveDownsize() != null
				&& payloadCashless.getPreAuthReq().getApproveDownsize().equalsIgnoreCase(SHAConstants.SPECIALIST_DOWNSIZE"SPECIALIST")){
			payloadCashless.getPreAuthReq().setOutcome(SHAConstants.SPECIALIST_RECIEVED);
			humanTask.setPayloadCashless(payloadCashless);
			SubmitDownsizeSpecialistAdviseTask submitSpecialist = BPMClientContext.getSubmitSpecialistDownsizeTask(searchFormDto.getUsername(),searchFormDto.getPassword());
			try{
			   submitSpecialist.execute(searchFormDto.getUsername(), humanTask);
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{			
				SubmitSSpecialistAdviseTask specialistTask = BPMClientContext.getRefractoredSubmitSpecialistTask(searchFormDto.getUsername(), searchFormDto.getPassword());
				
				try{
				specialistTask.execute(searchFormDto.getUsername(), humanTask);
				}catch(Exception e){
					e.printStackTrace();
				}
		}
				InvokeHumanTask invokeHT=new InvokeHumanTask();
				invokeHT.execute("weblogic", "welcome1", searchFormDto.getHumanTask(), null, pedReqDetails, null, null);
				
				System.out.println("BPM Executed Successfully");
		}	
	}
	*/
	public List<SubmitSpecialistTableDTO> getSpecialistCompletedList(Date fromDate, Date toDate,String userName){
		
		List<SubmitSpecialistTableDTO> resultList = new ArrayList<SubmitSpecialistTableDTO>();
		
		fromDate.setHours(0);
		
		List<Long> statusList = new ArrayList<Long>();
		statusList.add(ReferenceTable.SPECIALIST_REPLY_RECEIVED);
		statusList.add(ReferenceTable.DOWNSIZE_SUBMIT_SPECIALIST_REPLY);
		statusList.add(ReferenceTable.CLAIM_REQUEST_SPECIALIST_REPLY_RECEIVED_STATUS);
		statusList.add(ReferenceTable.FINANCIAL_SPECIALIST_REPLY_RECEIVED_STATUS);
		
//		Calendar c = Calendar.getInstance();
//		c.setTime(toDate);
//		c.add(Calendar.DATE, 1);
//		toDate = c.getTime();
		fromDate = SHAUtils.getFromDate(SHAUtils.formatDateForBatchNo(fromDate));
		toDate = SHAUtils.getToDate(SHAUtils.formatDateForBatchNo(toDate));
		
		 Query findByClaimKey = entityManager.createNamedQuery("Specialist.findCompletedTask");
		 findByClaimKey.setParameter("fromDate", fromDate);
		 findByClaimKey.setParameter("toDate", toDate);
		 findByClaimKey.setParameter("statusKey", statusList);
		 findByClaimKey.setParameter("modifiedBy", userName.toLowerCase());
		 
		 
		 List<Specialist> specialistResult = (List<Specialist>) findByClaimKey.getResultList();
		 
		 for (Specialist specialist : specialistResult) {
			 SubmitSpecialistTableDTO dto = new SubmitSpecialistTableDTO();
			 
			dto.setIntimationNo(specialist.getClaim().getIntimation().getIntimationId());
			if(specialist.getSpcialistType() != null){
				dto.setSpecialityType(specialist.getSpcialistType().getValue());
			}
			if(specialist.getModifiedBy() != null){
			TmpEmployee employeeDetails = getEmployeeDetails(specialist.getModifiedBy());
				if(employeeDetails != null){
					dto.setDoctorName(employeeDetails.getEmpFirstName());
				}
			}
			dto.setDoctorComments(specialist.getSpecialistRemarks());
			if(specialist.getModifiedDate() != null){
				String formatDate = SHAUtils.formatDate(specialist.getModifiedDate());
				dto.setDateOfRefer(formatDate);
			}
			
			resultList.add(dto);

		}
		
		return resultList;
	}

	@Override
	public Class<PreauthEscalate> getDTOClass() {
		return PreauthEscalate.class;
	}
	
	 /*private TmpEmployee getEmployeeName(String initiatorId)
		{
		  TmpEmployee fvrInitiatorDetail;
			Query findByTransactionKey = entityManager.createNamedQuery(
					"TmpEmployee.getEmpByLoginId").setParameter("loginId", initiatorId.toLowerCase());
			try{
				fvrInitiatorDetail =(TmpEmployee) findByTransactionKey.getSingleResult();
				return fvrInitiatorDetail;
			}
			catch(Exception e)
			{
				return null;
			}
								
		}*/
	 
	  private List<SpecialistMapping> getSpecialityForUser(String initiatorId)
		{
		  List<SpecialistMapping> specialistList;
			Query findByTransactionKey = entityManager.createNamedQuery(
					"SpecialistMapping.findByUserId").setParameter("loginId", initiatorId.toLowerCase());
			try{
				specialistList =(List<SpecialistMapping>) findByTransactionKey.getResultList();
				return specialistList;
			}
			catch(Exception e)
			{
				return null;
			}
								
		}
	  
	  public BeanItemContainer<SelectValue> getSpecialistValue(String initiatorId){
		  
		  List<SelectValue> listOfValues = new ArrayList<SelectValue>();
		  
		  List<SpecialistMapping> specialityForUser = getSpecialityForUser(initiatorId);
		  if(specialityForUser != null){
			  for (SpecialistMapping specialistMapping : specialityForUser) {
				SelectValue selected = new SelectValue();
				selected.setId(specialistMapping.getKey());
				selected.setValue(specialistMapping.getSpecialistType());
				listOfValues.add(selected);
			}
		  }
		  
		  BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
		  container.addAll(listOfValues);
		  
		  return container;
		  
	  }
	  
	  public Page<SubmitSpecialistTableDTO> search(SubmitSpecialistFormDTO formDTO, Boolean reimburementFlag)
		{
			try 
			{
				String strIntimationNo = "";
				String strPolicyNo = "";
				SelectValue refByCode = formDTO.getRefferedBy();
				String strRefBy = "";
				List<Map<String, Object>> taskProcedure = null ;
				
				String priority = null != formDTO  && formDTO.getPriority() != null ? formDTO.getPriority().getValue() != null ? formDTO.getPriority().getValue() : null : null;
				String source = null != formDTO  && formDTO.getSource() != null ? formDTO.getSource().getValue() != null ? formDTO.getSource().getValue(): null : null;
				String type = null != formDTO  && formDTO.getType() != null ? formDTO.getType().getValue() != null ? formDTO.getType().getValue(): null : null;
				String specialistType = formDTO.getSpecialistType() != null ? formDTO.getSpecialistType().getValue() != null ? formDTO.getSpecialistType().getValue(): null : null;
				String priorityNew = formDTO.getPriorityNew() != null ? formDTO.getPriorityNew().getValue() != null ? formDTO.getPriorityNew().getValue() : null : null;
				String cpuCode = formDTO.getCpuCode() != null ? formDTO.getCpuCode().getId() != null ? formDTO.getCpuCode().getId() != null ? formDTO.getCpuCode().getId().toString() : null : null : null;
				
				Boolean priorityAll = formDTO.getPriorityAll() != null ? formDTO.getPriorityAll() : null;
				Boolean crm = formDTO.getCrm() != null ? formDTO.getCrm() : null;
				Boolean vip = formDTO.getVip() != null ? formDTO.getVip() : null;
				
				Map<String, Object> mapValues = new WeakHashMap<String, Object>();
				
				Integer totalRecords = 0; 
				
				List<Long> keys = new ArrayList<Long>(); 
				
				mapValues.put(SHAConstants.CURRENT_Q,SHAConstants.SPECIALIST_ADVICE_CURRENT_QUEUE);
				
				mapValues.put(SHAConstants.USER_ID,formDTO.getUsername());
				
				
				if(null != formDTO && null != formDTO.getIntimationNo())
				{
					strIntimationNo = formDTO.getIntimationNo();
					
					mapValues.put(SHAConstants.INTIMATION_NO, strIntimationNo);
					
				}
				
				if(null != formDTO && null != formDTO.getPolicyNo())
				{
					strPolicyNo = formDTO.getPolicyNo();
					mapValues.put(SHAConstants.POLICY_NUMBER, strPolicyNo);
					
				}
				
				if(null != formDTO && null != refByCode)
				{
					strRefBy = String.valueOf(refByCode.getValue());
					if(strRefBy != null && strRefBy.length() > 0){
						mapValues.put(SHAConstants.REFERENCE_USER_ID, strRefBy);
					}
				}
				
				if(specialistType != null){
					
					mapValues.put(SHAConstants.SPECIALITY_NAME, specialistType);
				}else{
					
					
					List<SpecialistMapping> specialityForUser = getSpecialityForUser(formDTO.getUsername());
					StringBuffer speciality = new StringBuffer();
					if(specialityForUser != null && ! specialityForUser.isEmpty()){
						for (SpecialistMapping specialistMapping : specialityForUser) {
							if(specialistMapping.getSpecialistType() != null){
								speciality.append(specialistMapping.getSpecialistType()).append(",");
							}
						}
						if(! speciality.toString().equalsIgnoreCase("")){
							String specialist = speciality.toString().substring(0, speciality.toString().length()-1);
							speciality.append(specialist);
						}
						}
						
					
//					mapValues.put(SHAConstants.SPECIALITY_NAME, speciality);
				}
				
				
				 if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
							|| type != null && ! type.isEmpty()){
						
						if(priority != null && ! priority.isEmpty())
							if(! priority.equalsIgnoreCase(SHAConstants.ALL)){
								mapValues.put(SHAConstants.PRIORITY, priority);
							}
						if(source != null && ! source.isEmpty()){
							
							mapValues.put(SHAConstants.STAGE_SOURCE, source);
						}
						
						if(type != null && ! type.isEmpty()){
							if(! type.equalsIgnoreCase(SHAConstants.ALL)){
								mapValues.put(SHAConstants.RECORD_TYPE, type);
							}
						}
						
				}
				 
				/*if (priorityNew != null && ! priorityNew.isEmpty() && !priorityNew.equalsIgnoreCase(SHAConstants.ALL)) {
					if (priorityNew.equalsIgnoreCase(SHAConstants.NORMAL)) {
						mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "N");
					} else {
						mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "Y");
					}
				}*/
				
				if(cpuCode != null){
					
					mapValues.put(SHAConstants.CPU_CODE, cpuCode);
				}
				
				if (crm != null && crm.equals(Boolean.TRUE)) {
			    	mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "Y");
			    }
			    
			    if (vip != null && vip.equals(Boolean.TRUE)) {
			    	mapValues.put(SHAConstants.PRIORITY, "VIP");
			    }
				 
				List<SubmitSpecialistTableDTO> searchSpecialistAdviseTableDTO  = new ArrayList<SubmitSpecialistTableDTO>();
				
				Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
				
				//Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
				Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
				
				DBCalculationService dbCalculationService = new DBCalculationService();
			//	taskProcedure = dbCalculationService.getTaskProcedure(setMapValues);
				taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);
				
				if (null != taskProcedure) {
					
					for (Map<String, Object> outPutArray : taskProcedure) {
						Long keyValue = (Long) outPutArray.get(SHAConstants.CASHLESS_KEY);
						workFlowMap.put(keyValue,outPutArray);
						
						 totalRecords = (Integer) outPutArray
								.get(SHAConstants.TOTAL_RECORDS);
						 
							 keys.add(keyValue);
							}
					
					
					
					
					if(null != keys && 0!= keys.size())
					{
						List<Preauth> resultList = new ArrayList<Preauth>();
						final CriteriaBuilder IntimationBuilder = entityManager.getCriteriaBuilder();
						final CriteriaQuery<Preauth> criteriaQuery = IntimationBuilder
								.createQuery(Preauth.class);
						Root<Preauth> searchRootForIntimation = criteriaQuery.from(Preauth.class);
						criteriaQuery.where(searchRootForIntimation.<Long> get("key").in(keys));		
						/*final TypedQuery<Preauth> intimateInfoQuery = entityManager
								.createQuery(criteriaQuery);*/
						//resultList =  intimateInfoQuery.getResultList();
						resultList = SHAUtils.getIntimationInformation(SHAConstants.PREAUTH_SEARCH_SCREEN, entityManager, keys);
						//List<Preauth> pageItemList = resultList;
						searchSpecialistAdviseTableDTO = SubmitSpecialistMapper.getInstance()
								.getSearchAdviseOnPEDTableDTO(resultList);
						for (SubmitSpecialistTableDTO tableDTO : searchSpecialistAdviseTableDTO) {
							
							if(tableDTO.getKey() != null){
								Object workflowKey = workFlowMap.get(tableDTO.getKey());
								tableDTO.setDbOutArray(workflowKey);
							}
							
							if(tableDTO.getCpuId() != null){
								tableDTO.setCpuCode(tableDTO.getCpuId().toString());
							}
							
	                         if(tableDTO.getHospitalkey() != null){
	                        	 Hospitals hospitalDetails = getHospitalDetails(tableDTO.getHospitalkey());
	                        	 if(hospitalDetails != null){
	                        		 tableDTO.setHospitalName(hospitalDetails.getName());
	                        		 tableDTO.setHospitalCity(hospitalDetails.getCity());
	                        		 tableDTO.setHospitalAddress(hospitalDetails.getAddress());
	                        	 }
	                         }
	                         
	                         if(tableDTO.getKey() != null){
	                        	 Specialist submitSpecialist = getSubmitSpecialist(tableDTO.getKey());
	                        	 if(submitSpecialist != null){
	                        		 if(submitSpecialist.getSpcialistType() != null && submitSpecialist.getSpcialistType().getValue() != null){
	                        			 tableDTO.setSpecialityType(submitSpecialist.getSpcialistType().getValue());
	                        		 }
	                        		 
	                        		 if(submitSpecialist.getCreatedDate() != null){
	        				    		 String formatDate = SHAUtils.formatDate(submitSpecialist.getCreatedDate());
	        				    		 tableDTO.setDateOfRefer(formatDate);
	        				    	 }
	                        		 
	                        		tableDTO.setDoctorComments(submitSpecialist.getReasonForReferring());
	                        		tableDTO.setReferredBy(submitSpecialist.getCreatedBy());
	                        		TmpEmployee employeeDetails = getEmployeeDetails(submitSpecialist.getCreatedBy());
	                        		if(employeeDetails != null){
	                        			tableDTO.setDoctorName(employeeDetails.getEmpFirstName());
	                        		}
	                        		tableDTO.setDoctorId(submitSpecialist.getCreatedBy());
	                        		
	                        	 }
	                         }
						}
					}
				}
				
				for (SubmitSpecialistTableDTO submitSpecialistTableDTO : searchSpecialistAdviseTableDTO) {
					submitSpecialistTableDTO.setIsReimburementFlag(reimburementFlag);
					 Claim claim = getClaimByIntimation(submitSpecialistTableDTO.getIntimationkey());
					 /*if(claim != null && claim.getCrcFlag()!= null && claim.getCrcFlag().equals("Y")) {
						 submitSpecialistTableDTO.setColorCodeCell("OLIVE");
					 }*/
					 if (claim != null) {
						
						if(submitSpecialistTableDTO.getCrmFlagged() != null){
							if(submitSpecialistTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
								if (claim.getCrcFlag() != null && claim.getCrcFlag().equalsIgnoreCase("Y")) {
									submitSpecialistTableDTO.setColorCodeCell("OLIVE");
								}
								submitSpecialistTableDTO.setCrmFlagged(null);
							}
						}
						if (claim.getIsVipCustomer() != null && claim.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
							submitSpecialistTableDTO.setColorCodeCell("VIP");
						}
						if (claim.getCrcFlag() != null && claim.getCrcFlag().equalsIgnoreCase("Y") 
								&& claim.getIsVipCustomer() != null && claim.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
							submitSpecialistTableDTO.setColorCodeCell("CRMVIP");
						}
							
					}
				}
				
				for (SubmitSpecialistTableDTO submitSpecialistTableDTO : searchSpecialistAdviseTableDTO) {
				      String lob = submitSpecialistTableDTO.getLob();
				      if(lob != null){
				    	  MastersValue master = getMaster(Long.valueOf(lob));
				    	  if(master != null){
				    		  submitSpecialistTableDTO.setLob(master.getValue());
				    	  }
				      }
				      submitSpecialistTableDTO.setSearchFormDTO(formDTO);
				}
				Page<PreauthEscalate> pagedList = super.pagedList(formDTO.getPageable());
				Page<SubmitSpecialistTableDTO> page = new Page<SubmitSpecialistTableDTO>();
				page.setPageNumber(pagedList.getPageNumber());
				page.setPageItems(searchSpecialistAdviseTableDTO);
				page.setPagesAvailable(pagedList.getPagesAvailable());
				page.setTotalRecords(totalRecords);
				return page;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;		
		}
	  
	  public void setDBOutComeForCashlessSpecialist(SubmitSpecialistTableDTO searchDto,String outCome){
			
				
				Map<String, Object> wrkFlowMap = (Map<String, Object>) searchDto.getDbOutArray();
				wrkFlowMap.put(SHAConstants.OUTCOME,outCome);
				
				wrkFlowMap.put(SHAConstants.STAGE_SOURCE,SHAConstants.SOURCE_SPECIALIST_REPLY);
				
				//Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(wrkFlowMap);
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
				
				DBCalculationService dbCalService = new DBCalculationService();
				//dbCalService.initiateTaskProcedure(objArrayForSubmit);
				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
					
		}
	  
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


}
