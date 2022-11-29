/**
 * 
 */
package com.shaic.claim.search.specialist.search;

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
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.reimbursement.Specialist;
import com.shaic.domain.reimbursement.SpecialistMapping;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.specialistprocessing.submitspecialist.search.SearchSubmitSpecialistAdviseMapper;
/**
 * @author ntv.narenj
 *
 */
@Stateless
public class SubmitSpecialistAdviseService extends AbstractDAO<Claim>{

	@PersistenceContext
	protected EntityManager entityManager;
	
	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	
	
	public SubmitSpecialistAdviseService() {
		super();
		
	}
	
	public  Page<SubmitSpecialistTableDTO> search(SubmitSpecialistFormDTO searchFormDTO, Boolean reimburementFlag) {
		
		
		try{
			String intimationNo = searchFormDTO.getIntimationNo();
			String policyNo = searchFormDTO.getPolicyNo();
			String refferedBy = null != searchFormDTO.getRefferedBy() ? searchFormDTO.getRefferedBy().getValue() : null;
			List<Map<String, Object>> taskProcedure = null ;
			
			String priority = searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
			String source = searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
			String type = searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
			String specialistType = searchFormDTO.getSpecialistType() != null ? searchFormDTO.getSpecialistType().getValue() != null ? searchFormDTO.getSpecialistType().getValue(): null : null;
			String priorityNew = searchFormDTO.getPriorityNew() != null ? searchFormDTO.getPriorityNew().getValue() != null ? searchFormDTO.getPriorityNew().getValue() : null : null;
			String cpuCode = searchFormDTO.getCpuCode() != null ? searchFormDTO.getCpuCode().getId() != null ? searchFormDTO.getCpuCode().getId() != null ? searchFormDTO.getCpuCode().getId().toString() : null : null : null;
			
			Boolean priorityAll = searchFormDTO.getPriorityAll() != null ? searchFormDTO.getPriorityAll() : null;
			Boolean crm = searchFormDTO.getCrm() != null ? searchFormDTO.getCrm() : null;
			Boolean vip = searchFormDTO.getVip() != null ? searchFormDTO.getVip() : null;
			
			String userName = searchFormDTO.getUsername();
			String passWord = searchFormDTO.getPassword();
			
			Long rodKey = null;
			String intimationNumber;
			List<String> intimationNoList = new ArrayList<String>();
			
			List<Long> rodKeyList = new ArrayList<Long>();
			List<Integer> taskNumber = new ArrayList<Integer>();

			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			Integer totalRecords = 0; 
			
			List<Long> keys = new ArrayList<Long>(); 
			
			mapValues.put(SHAConstants.CURRENT_Q,SHAConstants.REIMB_SPECIALIST_ADVICE_CURRENT_QUEUE);
			
			mapValues.put(SHAConstants.USER_ID,searchFormDTO.getUsername());
			
		/*	ProductInfoType productInfo = new ProductInfoType();
			
			
			DocReceiptACKType docReceiptACK = new DocReceiptACKType();
			
			
			
			ClaimType claimType = new ClaimType();
			
			
			intimationType.setReason("HEALTH");
			
//			productInfo.setLob("H");
			
			docReceiptACK.setDocUpload("HEALTH");
			
			claimType.setCoverBenifitType("HEALTH");
			

			
			payloadBOType.setProductInfo(productInfo);
			
			payloadBOType.setIntimation(intimationType);
			
			payloadBOType.setClaim(claimType);
			
			payloadBOType.setDocReceiptACK(docReceiptACK);
			*/
			
			if(intimationNo != null && !intimationNo.isEmpty()){
				
				mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);

			}
			
			
			if(policyNo != null && !policyNo.isEmpty()){
				

				mapValues.put(SHAConstants.POLICY_NUMBER, policyNo);
				
			}
			
			
			if(specialistType != null){
				

				mapValues.put(SHAConstants.SPECIALITY_NAME, specialistType);
			
			}else{

					List<SpecialistMapping> specialityForUser = getSpecialityForUser(searchFormDTO.getUsername());
					StringBuffer speciality = new StringBuffer();
					if(specialityForUser != null && ! specialityForUser.isEmpty()){
						for (SpecialistMapping specialistMapping : specialityForUser) {
							if(specialistMapping.getSpecialistType() != null){
								speciality.append(specialistMapping.getSpecialistType()).append(",");
							}
						}
						
						if(! speciality.toString().equalsIgnoreCase("")){
							String speciaist = speciality.toString().substring(0, speciality.toString().length()-1);
							speciality.append(speciaist);
						}
						
					}
					mapValues.put(SHAConstants.SPECIALITY_NAME, specialistType);

			}
			

			  if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
						|| type != null && ! type.isEmpty()){
					
					
					if(priority != null && ! priority.isEmpty())
						if(priority.equalsIgnoreCase(SHAConstants.ALL)){
							priority = null;
						}
					mapValues.put(SHAConstants.PRIORITY, priority);
					//classification.setPriority(priority);
					if(source != null && ! source.isEmpty()){
						
						mapValues.put(SHAConstants.STAGE_SOURCE, source);
					}
					
					if(type != null && ! type.isEmpty()){
						if(type.equalsIgnoreCase(SHAConstants.ALL)){
							type = null;
						}
						mapValues.put(SHAConstants.RECORD_TYPE, type);
						//classification.setType(type);
					}					
					

			  if(refferedBy != null){				
							
							if(refferedBy != null){
								
								
								
								String[] split = refferedBy.split("-");
								String refered = split[0];
								
								if(refered != null) {
									refered = refered.replaceAll("\\s","");
									mapValues.put(SHAConstants.PAYLOAD_REFERENCE_USER_ID, refered);
								}

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
			  
			  	if (crm != null && crm.equals(Boolean.TRUE)) {
			    	mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "Y");
			    }
			    
			    if (vip != null && vip.equals(Boolean.TRUE)) {
			    	mapValues.put(SHAConstants.PRIORITY, "VIP");
			    }
				
				if(cpuCode != null){
					
					mapValues.put(SHAConstants.CPU_CODE, cpuCode);
				}
			
		   /* if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
						|| type != null && ! type.isEmpty()){
					
					
					if(priority != null && ! priority.isEmpty())
						if(priority.equalsIgnoreCase(SHAConstants.ALL)){
							priority = null;
						}
					mapValues.put(SHAConstants.PRIORITY, priority);
					//classification.setPriority(priority);
					if(source != null && ! source.isEmpty()){
						
						mapValues.put(SHAConstants.STAGE_SOURCE, source);
					}
					
					if(type != null && ! type.isEmpty()){
						if(type.equalsIgnoreCase(SHAConstants.ALL)){
							type = null;
						}
						mapValues.put(SHAConstants.RECORD_TYPE, type);
						//classification.setType(type);
					}
<<<<<<< HEAD
					
					
			}*/

		
						
			Pageable pageable = searchFormDTO.getPageable();
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
	
			
			
			//Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
			Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
			
			DBCalculationService dbCalculationService = new DBCalculationService();
//			taskProcedure = dbCalculationService.getTaskProcedure(setMapValues);
			taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);
			
			//taskList = specialistTask.getTasks(userName,pageable,payloadBOType);
		
			
			if (null != taskProcedure) {
				
				for (Map<String, Object> outPutArray : taskProcedure) {
					Long keyValue = (Long) outPutArray.get(SHAConstants.PAYLOAD_ROD_KEY);
					String intimationId = (String)outPutArray.get(SHAConstants.INTIMATION_NO);
					intimationNoList.add(intimationId);
					rodKeyList.add(keyValue);
					workFlowMap.put(keyValue,outPutArray);
					
					 totalRecords = (Integer) outPutArray
							.get(SHAConstants.TOTAL_RECORDS);
					 
						 keys.add(keyValue);
						}
				
			}
			
		/*	if(null != specialistTask){
				List<HumanTask> humanTaskList = taskList.getHumanTasks();
				if(null == humanTaskList | humanTaskList.isEmpty()){
					return null;
				}
				
				for(HumanTask humanTask : humanTaskList){
					PayloadBOType payloadBO = humanTask.getPayload();
					if(payloadBO.getRod() != null){
					intimationNoList.add(payloadBO.getIntimation().getIntimationNumber());
					humanTaskListDTO.add(humanTask);

					rodKeyList.add(payloadBO.getRod().getKey());
					taskNumber.add(humanTask.getNumber());
					
					}
				}
			}*/
			List<SubmitSpecialistTableDTO> tableDTO = new ArrayList<SubmitSpecialistTableDTO>();
			for(int index = 0 ; index < intimationNoList.size() ; index++){
				
				intimationNumber = intimationNoList.get(index); 
				
				// humanTaskDTO = humanTaskListDTO.get(index);
				 
				 rodKey = rodKeyList.get(index);
				 

				// Integer taskNo = taskNumber.get(index);

				 
				 tableDTO.addAll(getIntimationData(intimationNumber,rodKey, reimburementFlag,searchFormDTO));
				 
			}
		
			Page<SubmitSpecialistTableDTO> page = new Page<SubmitSpecialistTableDTO>();
			page.setPageItems(tableDTO);
		//	page.setPageNumber(taskList.getCurrentPage());
			page.setTotalRecords(totalRecords);
			return page;
			}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		}
			return null;	
		
			
	}
	private List<SubmitSpecialistTableDTO> getIntimationData(String intimationNo,  Long rodKey, Boolean reimburementFlag,SubmitSpecialistFormDTO searchFormDTO){
		
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
	
		List<Claim> claimList = new ArrayList<Claim>(); 
		
		Root<Claim> root = criteriaQuery.from(Claim.class);
	
		List<Predicate> conditionList = new ArrayList<Predicate>();
		try{
		if(!intimationNo.isEmpty() || intimationNo != null){
			Predicate condition1 = criteriaBuilder.equal(root.<Intimation>get("intimation").<String>get("intimationId"), intimationNo);
			conditionList.add(condition1);
			criteriaQuery.select(root).where(
			criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
			claimList = typedQuery.getResultList();
		}
			

			for(Claim inti:claimList){
			System.out.println(inti.getIntimation().getIntimationId()+"oooooooooooooooooooooooooo"+inti.getIntimation().getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
			}
			
			List<SubmitSpecialistTableDTO> tableDTO = SearchSubmitSpecialistAdviseMapper.getInstance().getClaimDTO(claimList);
			
			for (SubmitSpecialistTableDTO submitSpecialistTableDTO : tableDTO) {
				
				Claim claim = getClaimByIntimation(submitSpecialistTableDTO.getIntimationkey());
				/*if(claim != null && claim.getCrcFlag()!= null && claim.getCrcFlag().equals("Y")) {
					submitSpecialistTableDTO.setColorCodeCell("OLIVE");
				}*/
				
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
				
				if(submitSpecialistTableDTO.getCpuId() != null){
					submitSpecialistTableDTO.setCpuCode(submitSpecialistTableDTO.getCpuId().toString());
				}
				
				submitSpecialistTableDTO.setSearchFormDTO(searchFormDTO);
			}
			//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
			tableDTO = getHospitalDetails(tableDTO,rodKey,reimburementFlag);
	
			return tableDTO;
		}catch(Exception e){
			return null;
		}
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
	

	@Override
	public Class<Claim> getDTOClass() {
		// TODO Auto-generated method stub
		return Claim.class;
	}
	

	private List<SubmitSpecialistTableDTO> getHospitalDetails(
			List<SubmitSpecialistTableDTO> tableDTO, Long rodKey,Boolean reimburementFlag) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
			
			
			try{
				Query findByHospitalKey = entityManager.createNamedQuery(
						"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalkey());
			     hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
		
				// tableDTO.get(index).setHumantask(humanTask); 
				 tableDTO.get(index).setRodKey(rodKey);
				 
				 if(rodKey != null){
					 Reimbursement reimbursementByKey = getReimbursementByKey(rodKey);
					 tableDTO.get(index).setReferredBy(reimbursementByKey.getModifiedBy());
					 try{
						 
				     Specialist submitSpecialist = getSubmitSpecialist(reimbursementByKey.getKey());
				     
				     if(submitSpecialist != null){
				    	 tableDTO.get(index).setReferredBy(submitSpecialist.getCreatedBy());
				    	 if(submitSpecialist.getCreatedBy() != null){
				    		 TmpEmployee employeeDetails = getEmployeeDetails(submitSpecialist.getCreatedBy());
				    		 if(employeeDetails != null){
				    			 tableDTO.get(index).setDoctorName(employeeDetails.getEmpFirstName());
				    		 }
				    		 tableDTO.get(index).setDoctorId(submitSpecialist.getCreatedBy());
				    		 tableDTO.get(index).setDoctorComments(submitSpecialist.getReasonForReferring());
				    	 }
				    	 
				    	 if(submitSpecialist.getCreatedDate() != null){
				    		 String formatDate = SHAUtils.formatDate(submitSpecialist.getCreatedDate());
				    		 tableDTO.get(index).setDateOfRefer(formatDate);
				    	 }
				    	 
				    	 if(submitSpecialist.getSpcialistType() != null && submitSpecialist.getSpcialistType().getValue() != null){
				    		 tableDTO.get(index).setSpecialityType(submitSpecialist.getSpcialistType().getValue());
				    	 }
				    	 
				   
				    	 
				    	 
				     }

					 }catch(Exception e){
						 
					 }
					 
					 
				 }
				 
				 tableDTO.get(index).setLob(getLOBValue(tableDTO.get(index).getlOBId()).getValue());
				 tableDTO.get(index).setIsReimburementFlag(reimburementFlag);
				 //tableDTO.get(index).setTaskNumber(taskNumber);
				 if(hospitalDetail != null){
					 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
					 tableDTO.get(index).setHospitalAddress(hospitalDetail.getAddress());
					 tableDTO.get(index).setHospitalCity(hospitalDetail.getCity());
				 }
				 	Object workflowKey = workFlowMap.get(tableDTO.get(index).getRodKey());
//					tableDto.setDbOutArray(workflowKey);
					tableDTO.get(index).setDbOutArray(workflowKey);
//				 tableDTO.get(index).setHospitalAddress(hospitalDetail.getAddress());
//				 tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId()+" ,"+hospitalDetail.getCity());
//				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
//				 tableDTO.get(index).setCpuCode(getTmpCPUCode(tableDTO.get(index).getCpuId()).getCpuCode());*/
			//	 tableDTO.get(index).setSpecialityType(getSpecialityType(tableDTO.get(index).getClaimKey()));
				
			 
			}catch(Exception e){
				continue;
			}
		
		}
		
		
		return tableDTO;
	}
	
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
	
	private Specialist getSubmitSpecialist(Long transactionKey){
		
		 Query findByClaimKey = entityManager.createNamedQuery("Specialist.findByTransactionKey").setParameter("transactionKey", transactionKey);
		 List<Specialist> specialistResult = (List<Specialist>) findByClaimKey.getResultList();
		 
		 if(specialistResult != null && ! specialistResult.isEmpty()){
			 return specialistResult.get(0);
		 }
		 
		 return null;
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
	
	/*private String getSpecialityType(Long claimKey){
		try{
			String specilityValue ="";
			Query findCpuCode = entityManager.createNamedQuery("Speciality.findByClaimKey").setParameter("claimKey", claimKey);
			List<Speciality> SpecialityList = findCpuCode.getResultList();
			for(Speciality speciality : SpecialityList){ 
				Query findSpecilty = entityManager.createNamedQuery("SpecialityType.findByKey").setParameter("key", speciality.getSpecialityType().getKey());
				SpecialityType result = (SpecialityType) findSpecilty.getSingleResult(); 
				specilityValue += result.getValue()+", ";
			}
			
			return specilityValue;
			}catch(Exception e){
				return null;
			}
		
		
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

