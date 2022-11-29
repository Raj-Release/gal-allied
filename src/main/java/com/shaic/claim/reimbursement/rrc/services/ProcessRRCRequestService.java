/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.dto.QuantumReductionDetailsDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.MastersValue;
import com.shaic.domain.RRCCategory;
import com.shaic.domain.RRCCategorySource;
import com.shaic.domain.RRCDetails;
import com.shaic.domain.RRCRequest;
import com.shaic.domain.RRCSubCategory;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.ui.Notification;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.vijayar
 *
 * This service file is common for search and submit services , related
 * with PROCESS RRC REQUEST menu.
 */
@Stateless
public class ProcessRRCRequestService extends AbstractDAO<RRCRequest>{


	@PersistenceContext
	protected EntityManager entityManager;

	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	public ProcessRRCRequestService(){
		super();
	}
	public  Page<SearchProcessRRCRequestTableDTO> search(
			SearchProcessRRCRequestFormDTO searchFormDTO,
			String userName, String passWord) {
		List<RRCRequest> rrcRequestList = new ArrayList<RRCRequest>();
		List<Long> rrcRequestKeyList = new ArrayList<Long>();

		try{

			String intimationNo = "";
			String priority = null != searchFormDTO && searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
			String source = null != searchFormDTO && searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
			String type = null != searchFormDTO && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;

			List<Map<String, Object>> taskProcedure = null ;

			Map<String, Object> mapValues = new WeakHashMap<String, Object>();

			workFlowMap= new WeakHashMap<Long, Object>();		


			Integer totalRecords = 0; 

			mapValues.put(SHAConstants.USER_ID, userName);
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.PROCESS_RRC_REQUEST_CURRENT_QUEUE);

			if(null != searchFormDTO.getIntimationNo() && !searchFormDTO.getIntimationNo().isEmpty() )
			{
				mapValues.put(SHAConstants.INTIMATION_NO, searchFormDTO.getIntimationNo());		

				/*DocReceiptACKType docReceiptACK = new DocReceiptACKType();


			ProductInfoType productInfo = new ProductInfoType();

			ClaimType claimType = new ClaimType();


			intimationType.setReason("HEALTH");

			productInfo.setLob("H");

			docReceiptACK.setDocUpload("HEALTH");

			claimType.setCoverBenifitType("HEALTH");



			payloadBOType.setProductInfo(productInfo);

			payloadBOType.setIntimation(intimationType);

			payloadBOType.setClaim(claimType);

			payloadBOType.setDocReceiptACK(docReceiptACK);*/

				if(null != searchFormDTO.getIntimationNo() && !searchFormDTO.getIntimationNo().isEmpty() )
				{
					intimationNo = null != searchFormDTO && null != searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() : null;
					/*	intimationType.setIntimationNumber(intimationNo);
				payloadBOType.setIntimation(intimationType);*/

				}
				String rrcRequestNo = searchFormDTO.getRrcRequestNo();
				//RRCType rrcType = new RRCType();
				if(null != rrcRequestNo && !rrcRequestNo.isEmpty())
				{	
					mapValues.put(SHAConstants.RRC_REQUEST_NUMBER, rrcRequestNo);
					//payloadBOType.getRrc().setRequestNo(rrcRequestNo);
				}
				String cpuId =  null;

				if(null !=  searchFormDTO.getCpu())
				{

					String str[] = searchFormDTO.getCpu().toString().split(" ");   		
					cpuId = str[0];
					mapValues.put(SHAConstants.CPU_CODE, String.valueOf(cpuId)); 

					cpuId = str[0];//searchFormDTO.getCpu().getValue();

				}

				String rrcRequestTypeId = null;
				if(null != searchFormDTO.getRrcRequestType())
				{				
					//rrcRequestTypeId = searchFormDTO.getRrcRequestType().getId();
					rrcRequestTypeId = searchFormDTO.getRrcRequestType().getValue();

					if(null != rrcRequestTypeId)
					{
						if((SHAConstants.ON_HOLD).equals(rrcRequestTypeId))
						{

							mapValues.put(SHAConstants.RRC_REQUEST_TYPE, SHAConstants.HOLD);
							mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.PROCESS_RRC_REQUEST_HOLD_QUEUE);
						}
						else
						{
							mapValues.put(SHAConstants.RRC_REQUEST_TYPE, rrcRequestTypeId.toUpperCase());

						}
					}

				}


				//ClassificationType classification = null;

				if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
						|| type != null && ! type.isEmpty()){
					if(priority != null && ! priority.isEmpty())
						if(priority.equalsIgnoreCase(SHAConstants.ALL)){
							mapValues.put(SHAConstants.PRIORITY, null);
						}
					mapValues.put(SHAConstants.PRIORITY, priority);
					//	classification.setPriority(priority);
					if(source != null && ! source.isEmpty()){
						//classification.setSource(source);
						mapValues.put(SHAConstants.STAGE_SOURCE, source);	
					}

					if(type != null && ! type.isEmpty()){
						if(!type.equalsIgnoreCase(SHAConstants.ALL)){						

							mapValues.put(SHAConstants.RECORD_TYPE, type);
							/*if(type.equalsIgnoreCase(SHAConstants.RECONSIDERATION))
							{
							mapValues.put(SHAConstants.RECONSIDER_FLAG,SHAConstants.YES_FLAG);
							}*/
						}
					}

				}



			}

			Pageable pageable = searchFormDTO.getPageable();

			pageable.setPageNumber(1);
			pageable.setPageSize(25);



			Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
			DBCalculationService dbCalculationService = new DBCalculationService();
			taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);	
			if (null != taskProcedure) {
				for (Map<String, Object> outPutArray : taskProcedure) {			
					String strKeyValue = (String)outPutArray.get(SHAConstants.RRC_REQUEST_KEY);
					if(null != strKeyValue)
					{
						Long keyValue = Long.parseLong(strKeyValue);
						rrcRequestKeyList.add(keyValue);
						workFlowMap.put(keyValue,outPutArray);
					}



					totalRecords = (Integer) outPutArray
							.get(SHAConstants.TOTAL_RECORDS);
				}
			}



			int pageNumber = searchFormDTO.getPageable().getPageNumber();
			int firtResult;
			if(pageNumber > 1){
				firtResult = (pageNumber-1) *25;
			}else{
				firtResult = 0;
			}

			if(!rrcRequestKeyList.isEmpty()){
				rrcRequestList = getRRCRequestList(rrcRequestKeyList,firtResult);
			}

			List<String> loginIdList = new ArrayList<String>();



			if(null != rrcRequestList && !rrcRequestList.isEmpty())
			{
				for(RRCRequest rrcRequest: rrcRequestList){
					if(rrcRequest.getRequestorID() != null)
						loginIdList.add(rrcRequest.getRequestorID().toLowerCase());
				}	
			}

			List<TmpEmployee> tmpEmployeeList = new ArrayList<TmpEmployee>();

			if(!loginIdList.isEmpty()){
				tmpEmployeeList = getTmpEmployeeList(loginIdList);
			}


			ProcessRRCRequestMapper processRRCRequestMapper =  ProcessRRCRequestMapper.getInstance();
			List<SearchProcessRRCRequestTableDTO> tableDTO = processRRCRequestMapper.getRRCRequestList(rrcRequestList);
			if(null != tableDTO && !tableDTO.isEmpty())
			{
				for (int index = 0; index < tableDTO.size() ; index++)
				{
					SearchProcessRRCRequestTableDTO searchProcessRRCRequestTableDTO  = tableDTO.get(index);
					if(null != tmpEmployeeList && !tmpEmployeeList.isEmpty())
					{
						for (TmpEmployee tmpEmpDetails : tmpEmployeeList) {
							if(searchProcessRRCRequestTableDTO.getRequestorId().equalsIgnoreCase(tmpEmpDetails.getLoginId()))
							{
								searchProcessRRCRequestTableDTO.setRequestorName(tmpEmpDetails.getEmpFirstName());
							}
						}
					}

					Object workflowKey = workFlowMap.get(searchProcessRRCRequestTableDTO.getKey());
					tableDTO.get(index).setDbOutArray(workflowKey);

				}

			}



			Page<SearchProcessRRCRequestTableDTO> page = new Page<SearchProcessRRCRequestTableDTO>();
			searchFormDTO.getPageable().setPageNumber(pageNumber+1);
			page.setHasNext(true);
			if(tableDTO.isEmpty()){
				searchFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(tableDTO);
			//	page.setTotalRecords(taskList.getTotalRecords());
			page.setTotalRecords(totalRecords);
			return page;
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		}
		return null;	


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

	public List<RRCRequest> getRRCRequestList(List<Long> rrcRequestKeyList,int firtResult )
	{

		final CriteriaBuilder rrcRequestBuilder = entityManager
				.getCriteriaBuilder();
		final CriteriaQuery<RRCRequest> criteriaQuery = rrcRequestBuilder
				.createQuery(RRCRequest.class);
		Root<RRCRequest> searchRootForRRCRequest = criteriaQuery
				.from(RRCRequest.class);
		criteriaQuery.where(searchRootForRRCRequest.<Long> get("rrcRequestKey")
				.in(rrcRequestKeyList));
		final TypedQuery<RRCRequest> rrcRequestQuery = entityManager
				.createQuery(criteriaQuery);

		criteriaQuery.orderBy(rrcRequestBuilder.asc(searchRootForRRCRequest.get("rrcRequestKey")));

		//	return rrcRequestQuery.setFirstResult(firtResult).setMaxResults(25).getResultList();


		return rrcRequestQuery.getResultList();
	}

	public List<TmpEmployee> getTmpEmployeeList(List<String> loginIdList)
	{
		final CriteriaBuilder rrcDetailsBuilder = entityManager
				.getCriteriaBuilder();
		final CriteriaQuery<TmpEmployee> rrcDetailsCriteriaQuery = rrcDetailsBuilder
				.createQuery(TmpEmployee.class);
		Root<TmpEmployee> searchRootInfoForRRCDetails = rrcDetailsCriteriaQuery
				.from(TmpEmployee.class);
		rrcDetailsCriteriaQuery.where(rrcDetailsBuilder.lower(searchRootInfoForRRCDetails.<String> get(
				"loginId")).in(loginIdList));
		final TypedQuery<TmpEmployee> rrcDetailsInfoQuery = entityManager
				.createQuery(rrcDetailsCriteriaQuery);
		return rrcDetailsInfoQuery.getResultList();
	}



	public List<ExtraEmployeeEffortDTO> getEmployeeDetailsFromRRCDetails(Long rrcRequestKey)
	{
		List<ExtraEmployeeEffortDTO> employeeEffortDTO = null;


		/*	final CriteriaBuilder rrcDetailsBuilder = entityManager
				.getCriteriaBuilder();
		final CriteriaQuery<RRCDetails> criteriaQuery = rrcDetailsBuilder
				.createQuery(RRCDetails.class);



		Root<RRCDetails> searchRootInfoForRRCDetails = criteriaQuery
				.from(RRCDetails.class);


		Criteria cr = session.createCriteria(User.class)
			    .setProjection(Projections.projectionList()
			      .add(Projections.property("id"), "id")
			      .add(Projections.property("Name"), "Name"))
			    .setResultTransformer(Transformers.aliasToBean(User.class));

		criteriaQuery.select("").where(conditionList.toArray(new Predicate[]{}));

		hospitalCriteriaQuery.where(searchRootForHospitalInfo.<Long> get("transactionKey")
				.in(preauthKeyList));
		final TypedQuery<PedValidation> hospitalInfoQuery = entityManager
				.createQuery(hospitalCriteriaQuery);
		diagnosisList = hospitalInfoQuery.getResultList();*/



		Query query = entityManager.createNamedQuery("RRCDetails.getEmployeeDataByRequestKey");
		query.setParameter("rrcRequestKey", rrcRequestKey);
		List<RRCDetails> rrcDetailsList = (List<RRCDetails>)query.getResultList();
		if(null != rrcDetailsList && !rrcDetailsList.isEmpty())
		{
			Long sNo = 1l;
			for (Iterator it = rrcDetailsList.iterator(); it.hasNext(); ) {
				employeeEffortDTO = new ArrayList<ExtraEmployeeEffortDTO>();
				Object[] myResult = (Object[]) it.next();
				ExtraEmployeeEffortDTO effortDTO = new ExtraEmployeeEffortDTO();
				effortDTO.setSlNo(sNo);
				effortDTO.setRrcDetailsKey((Long)myResult[0]);
				effortDTO.setRrcRequestKey((Long)myResult[1]);
				effortDTO.setEmployeeId((String)myResult[2]);
				SelectValue selEmployee = new SelectValue();
				selEmployee.setValue((String)myResult[3]);
				effortDTO.setSelEmployeeName(selEmployee);
				SelectValue selCreditType = new SelectValue();
				selCreditType.setId((Long)myResult[4]);
				effortDTO.setCreditType(selCreditType);
				effortDTO.setScore((Long)myResult[5]);
				effortDTO.setRemarks((String)myResult[6]);
				employeeEffortDTO.add(effortDTO);
				sNo++;
				/*String firstName = (String) myResult[0];
	               String lastName = (String) myResult[1];
	               System.out.println( "Found " + firstName + " " + lastName );*/
			}
			/*ProcessRRCRequestMapper mapper = new ProcessRRCRequestMapper();
			employeeEffortDTO = mapper.getEmployeeListenerTableData(rrcDetailsList);*/
			/*for (int i = 0; i < rrcDetailsList.size(); i++) {

				Object obj = rrcDetailsList.get(i);

				RRCDetails rrcDetails = (RRCDetails)obj;

				effortDTO.setRrcDetailsKey(rrcDetails.getKey());
				   effortDTO.setRrcRequestKey(null!= rrcDetails.getRrcRequest() ? rrcDetails.getRrcRequest().getKey() : null);
				   effortDTO.setEmployeeId(rrcDetails.getEmployeeId());
				   effortDTO.setEmployeeName(rrcDetails.getEmployeeName());
				   SelectValue selCreditType = new SelectValue();
				   selCreditType.setId(null != rrcDetails.getCreditTypeId() ? rrcDetails.getCreditTypeId().getKey():null);
				   effortDTO.setCreditType(selCreditType);
				   effortDTO.setScore(rrcDetails.getScore());
				   effortDTO.setRemarks(rrcDetails.getRemarks());
				   employeeEffortDTO.add(effortDTO);


			}*/
			/*for (RRCDetails rrcDetails : rrcDetailsList) {

			}*/
		}
		return employeeEffortDTO;

	}

	public List<QuantumReductionDetailsDTO> getQuantumReductionDataFromRRCRequest(Long rrcRequestKey)
	{
		List<QuantumReductionDetailsDTO> quantumReductionDetailsDTO = null;
		Query query = entityManager.createNamedQuery("RRCRequest.findByKey");
		query = query.setParameter("rrcRequestKey", rrcRequestKey);
		List<RRCRequest> rrcRequestList = query.getResultList();
		if(null != rrcRequestList && !rrcRequestList.isEmpty())
		{
			ProcessRRCRequestMapper mapper =  ProcessRRCRequestMapper.getInstance();
			quantumReductionDetailsDTO = mapper.getQuantumReductionDetailsList(rrcRequestList);
		}
		return quantumReductionDetailsDTO;

	}

	public QuantumReductionDetailsDTO getQuantumReductionDetailsDTOFromRRCRequest(Long rrcRequestKey)
	{
		List<QuantumReductionDetailsDTO> quantumReductionDetailsDTO = null;
		Query query = entityManager.createNamedQuery("RRCRequest.findByKey");
		query = query.setParameter("rrcRequestKey", rrcRequestKey);
		List<RRCRequest> rrcRequestList = query.getResultList();
		if(null != rrcRequestList && !rrcRequestList.isEmpty())
		{
			ProcessRRCRequestMapper mapper =  ProcessRRCRequestMapper.getInstance();
			quantumReductionDetailsDTO = mapper.getQuantumReductionDetailsList(rrcRequestList);
		}
		if(null != quantumReductionDetailsDTO && !quantumReductionDetailsDTO.isEmpty())
		{
			return quantumReductionDetailsDTO.get(0);
		}
		return null;

	}


	public List<QuantumReductionDetailsDTO> getQuantumReductionDetailsList(Long claimKey)
	{
		List<QuantumReductionDetailsDTO> quantumReductionDetailsDTO = null;
		Query query = entityManager.createNamedQuery("RRCRequest.findLatestRRCByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<RRCRequest> rrcRequestList = query.getResultList();
		if(null != rrcRequestList && !rrcRequestList.isEmpty())
		{
			ProcessRRCRequestMapper mapper =  ProcessRRCRequestMapper.getInstance();
			quantumReductionDetailsDTO = mapper.getQuantumReductionDetailsList(rrcRequestList);
		}

		List<String> intimationList = new ArrayList<String>();
		List<QuantumReductionDetailsDTO> finalResultantTableList = new ArrayList<QuantumReductionDetailsDTO>();

		if(null != quantumReductionDetailsDTO && !quantumReductionDetailsDTO.isEmpty())
		{
			for (QuantumReductionDetailsDTO quantumReductionDetailsDTO2 : quantumReductionDetailsDTO) {
				if(!intimationList.contains(quantumReductionDetailsDTO2.getRequestNo()))
				{
					SelectValue selectValue = new SelectValue();	
					if(quantumReductionDetailsDTO2.getAnhFlag() !=null ){
						if(quantumReductionDetailsDTO2.getAnhFlag().equals(ReferenceTable.YES_FLAG)){
							selectValue.setId(ReferenceTable.COMMONMASTER_YES);
							selectValue.setValue(SHAConstants.YES);
							quantumReductionDetailsDTO2.setAnh(selectValue);
						}else if(quantumReductionDetailsDTO2.getAnhFlag().equals(ReferenceTable.NO_FLAG)){
							selectValue.setId(ReferenceTable.COMMONMASTER_NO);
							selectValue.setValue(SHAConstants.No);
							quantumReductionDetailsDTO2.setAnh(selectValue);
						}	
					}
					intimationList.add(quantumReductionDetailsDTO2.getRequestNo());
					finalResultantTableList.add(quantumReductionDetailsDTO2);
				}

			}
		}

		//return quantumReductionDetailsDTO;
		return finalResultantTableList;
	}


	/**
	 * Added for Process RRC Request Submit 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	//public String submitProcessRRCRequest(RRCDTO rrcDTO)
	public RRCDTO submitProcessRRCRequest(RRCDTO rrcDTO)
	{
		//String rrcRequestNo = "";
		RRCDTO rrcRequestNo = null;
		try
		{
			rrcRequestNo = saveProcessRRCRequestValues(rrcDTO);
		}
		catch (Exception e) {
			e.printStackTrace();
			Notification.show("Already Submitted. Please Try Another Record.");
		}
		return rrcRequestNo;
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

	public RRCRequest getRRCRequestByKey(Long rrcRequestKey)
	{
		Query query = entityManager
				.createNamedQuery("RRCRequest.findByKey").setParameter(
						"rrcRequestKey", rrcRequestKey);
		List<RRCRequest> rrcRequestList = query.getResultList();

		if(rrcRequestList != null && !rrcRequestList.isEmpty()) {
			for (RRCRequest rrcRequest : rrcRequestList) {
				entityManager.refresh(rrcRequest);
			}
			return rrcRequestList.get(0);
		}
		return null;
	}

	private RRCDTO saveProcessRRCRequestValues(RRCDTO rrcDTO)
	{

		ProcessRRCRequestMapper processRequestMapper =  ProcessRRCRequestMapper.getInstance();
		/**
		 * Since RRC request stage and status is not finalized, 
		 * setting zonal stage and status for testing purpose. Once
		 * stage and status for RRC request is finalized, the same
		 * will be incorporated.
		 **/
		rrcDTO.setStageKey(ReferenceTable.RRC_STAGE);
		rrcDTO.setStatusKey(ReferenceTable.RRC_REQUEST_PROCESS_STATUS);

		//RRCDTO rrcDTO = preauthDTO.getRrcDTO();

		//Reimbursement reimbursement = getReimbursementByKey(preauthDTO.getKey());
		RRCRequest rrcRequest = getRRCRequestByKey(rrcDTO.getRrcRequestKey());

		String rrcReqNo = rrcRequest.getRrcRequestNumber();

		if(null != rrcDTO.getEligibility())
		{
			MastersValue masEligiblity = new MastersValue();
			masEligiblity.setKey(rrcDTO.getEligibility().getId());
			rrcRequest.setEligiblityTypeId(masEligiblity);
		}

		if(null != rrcRequest.getRequestedTypeId())
		{
			Long requestType = rrcRequest.getRequestedTypeId().getKey();
			if(ReferenceTable.RRC_REQUEST_STATUS_ON_HOLD.equals(requestType))
			{
				rrcDTO.setIsHoldRepeated(true);
			}
		}

		MastersValue masRequestedTypeId = new MastersValue();

		if(SHAConstants.RRC_STATUS_PROCESS.equalsIgnoreCase(rrcDTO.getRrcStatus()))
		{
			if(null != rrcDTO.getSavedAmount() && !("").equalsIgnoreCase(rrcDTO.getSavedAmount()))
				rrcRequest.setSavedAmount(Long.valueOf(rrcDTO.getSavedAmount()));
			if(null != rrcDTO.getEligibilityRemarks() && !("").equalsIgnoreCase(rrcDTO.getEligibilityRemarks()))
				rrcRequest.setEligibiltyRemarks(rrcDTO.getEligibilityRemarks());

			masRequestedTypeId.setKey(ReferenceTable.RRC_REQUEST_STATUS_FRESH);

			List<ExtraEmployeeEffortDTO> rrcCategoryList = rrcDTO.getRrcCategoryList();
			if(null != rrcCategoryList && !rrcCategoryList.isEmpty())
			{
				for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : rrcCategoryList) {
					if(null != extraEmployeeEffortDTO.getCategory())
					{
						RRCCategory rrcCategory = new RRCCategory();
						rrcCategory.setRrcCategoryKey(extraEmployeeEffortDTO.getCategoryKey());
						MastersValue masCategory = new MastersValue();
						masCategory.setKey(extraEmployeeEffortDTO.getCategory().getId());
						rrcCategory.setCategoryId(masCategory);
						if(extraEmployeeEffortDTO.getSubCategory() !=null){
							rrcCategory.setSubCategorykey(extraEmployeeEffortDTO.getSubCategory().getId());
							if(extraEmployeeEffortDTO.getSourceOfIdentification() !=null){
								rrcCategory.setSourcekey(extraEmployeeEffortDTO.getSourceOfIdentification().getId());
							}
						}	
						rrcCategory.setRrcRequest(rrcRequest.getRrcRequestKey());
						rrcCategory.setTalkSpokento(extraEmployeeEffortDTO.getTalkSpokento());
						rrcCategory.setTalkSpokenDate(extraEmployeeEffortDTO.getTalkSpokenDate());
						rrcCategory.setTalkMobto(extraEmployeeEffortDTO.getTalkMobto() !=null ? Long.parseLong(extraEmployeeEffortDTO.getTalkMobto()) : null);
						if(null != rrcCategory.getRrcCategoryKey())
						{	
							entityManager.merge(rrcCategory);
							entityManager.flush();
						}
						else
						{
							entityManager.persist(rrcCategory);
							entityManager.flush();
							entityManager.refresh(rrcCategory);
						}
					}
				}
			}
			List<ExtraEmployeeEffortDTO> extraEffortByEmpList = rrcDTO.getEmployeeEffortList();
			if(null != extraEffortByEmpList && !extraEffortByEmpList.isEmpty())
			{
				for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : extraEffortByEmpList) {
					RRCDetails rrcDetails = processRequestMapper.getRRCDetailsForEmployee(extraEmployeeEffortDTO);
					if(rrcDetails.getEmployeeId() !=null){
						String employee[] = rrcDetails.getEmployeeId().split("-");
						rrcDetails.setEmployeeId(employee[0].trim());
						rrcDetails.setEmployeeName(employee[1].trim());
					}
					if(null != rrcDetails.getRrcDetailsKey())
					{
						entityManager.merge(rrcDetails);
						entityManager.flush();
					}
					else
					{
						rrcDetails.setRrcRequest(rrcRequest.getRrcRequestKey());
						entityManager.persist(rrcDetails);
						entityManager.flush();
						entityManager.refresh(rrcDetails);
					}
				}
			}	
		}
		else  if(SHAConstants.RRC_STATUS_ON_HOLD.equalsIgnoreCase(rrcDTO.getRrcStatus()))
		{
			if(null != rrcDTO.getRequestOnHoldRemarks() && !("").equalsIgnoreCase(rrcDTO.getRequestOnHoldRemarks()))
				rrcRequest.setRequestHoldRemarks(rrcDTO.getRequestOnHoldRemarks());

			masRequestedTypeId.setKey(ReferenceTable.RRC_REQUEST_STATUS_ON_HOLD);
		}



		/**
		 * As of now, either fresh ROD or on hold rod alone
		 * is processed.
		 * */
		if(null!= rrcDTO.getSavedAmount())
		{
			rrcRequest.setRequestorSavedAmount( Long.valueOf(rrcDTO.getSavedAmount()));
		}

		if(null != rrcDTO.getProcessingStageKey())
		{
			Stage stage = new Stage();
			stage.setKey(rrcDTO.getProcessingStageKey());
			rrcRequest.setRequestedStageId(stage);
		}

		rrcRequest.setRequestedTypeId(masRequestedTypeId);

		rrcRequest.setProcessedBy(rrcDTO.getStrUserName());
		rrcRequest.setProcessedDate(new Timestamp(System.currentTimeMillis()));
		rrcRequest.setRrcInitiatedDate(rrcDTO.getRrcintiatedDate());
		rrcRequest.setRrcType("RRC REQUEST");
		Stage stage = getStageByKey(rrcDTO.getStageKey());
		Status status = getStatusByKey(rrcDTO.getStatusKey());

		if(null != stage)
		{
			rrcRequest.setStage(stage);
		}
		if(null != status)
		{
			rrcRequest.setStatus(status);
		}

		if(null != rrcRequest.getRrcRequestKey())
		{
			entityManager.merge(rrcRequest);
			entityManager.flush();
			entityManager.refresh(rrcRequest);
		}

		//if(submitRequestRRCTaskToBPM(rrcDTO))
		if(submitRequestRRCTaskToDB(rrcDTO))
		{
			return rrcDTO;
		}
		else
		{
			return null;
		}


		//return rrcReqNo;
	}

	//}


	public Boolean submitRequestRRCTaskToBPM(RRCDTO rrcDTO)
	{/*
		try
		{
			//SubmitProcessRRCRequestTask submitProcessRRCRequestTask = BPMClientContext.getProcessRRCRequestSubmitTask(rrcDTO.getStrUserName(), rrcDTO.getStrPassword());

			Map<String, Object> wrkFlowMap = (Map<String, Object>) rrcDTO.getDbOutArray();

			HumanTask humanTaskForRRC = rrcDTO.getRrcHumanTask();
			PayloadBOType payloadBO = humanTaskForRRC.getPayload();

			// DocReceiptACKType receiptAckType = new DocReceiptACKType();
			// receiptAckType.setAckNumber(rodDTO.getDocumentDetails().getRodNumber());
			// receiptAckType.setKey(reimbursement.getKey());
			//RRCType rrcType = payloadBO.getRrc();
			//rrcType.setOutcome(rrcDTO.getRrcStatus());
			//rrcType.setStatus(rrcDTO.getRrcStatus());
			//ClaimType claimTypeValue = new ClaimType();

			if(null != rrcDTO && rrcDTO.getClaimType().equalsIgnoreCase(SHAConstants.CASHLESS_CLAIM_TYPE))
			{
				claimTypeValue.setClaimType(SHAConstants.CASHLESS_CLAIM_TYPE);
			}
			else
			{
				claimTypeValue.setClaimType(SHAConstants.REIMBURSEMENT_CLAIM_TYPE);
			}

			if(null != rrcDTO.getEligibility())
			{
				if(("REFERPANEL").equalsIgnoreCase(rrcDTO.getEligibility().getValue()))
				{
					rrcDTO.getEligibility().setValue("REFERPANEL");
					rrcType.setEligibilityType(rrcDTO.getEligibility().getValue());
					humanTaskForRRC.setOutcome(rrcDTO.getEligibility().getValue());
					rrcType.setOutcome(rrcDTO.getEligibility().getValue());
				}

				else
				{
					if(null != rrcDTO.getEligibility().getValue() )
					{
					rrcType.setEligibilityType(rrcDTO.getEligibility().getValue());
					humanTaskForRRC.setOutcome(rrcDTO.getEligibility().getValue().toUpperCase());
					rrcType.setOutcome(rrcDTO.getEligibility().getValue());
						rrcType.setEligibilityType(rrcDTO.getEligibility().getValue());
						String output = rrcDTO.getEligibility().getValue().replaceAll("\\s+",""); 
						humanTaskForRRC.setOutcome(output.toUpperCase());
						rrcType.setOutcome(rrcDTO.getEligibility().getValue());

					}

				}

			}

			rrcType.setSource("FRESH");
			if(SHAConstants.RRC_STATUS_PROCESS.equalsIgnoreCase(rrcDTO.getRrcStatus()))
			{
				rrcType.setRequestType("FRESH");
			}
			else if(SHAConstants.RRC_STATUS_ON_HOLD.equalsIgnoreCase(rrcDTO.getRrcStatus()))
			{
				rrcType.setOutcome(SHAConstants.HOLD);
				rrcType.setRequestType(SHAConstants.HOLD);
				humanTaskForRRC.setOutcome(SHAConstants.HOLD);
			}
			//rrcType.setFromDate(String.valueOf(new Timestamp(System.currentTimeMillis())));
			rrcType.setFromDate(SHAUtils.formatDate(rrcDTO.getRrcintiatedDate()));
			//rrcType.setFromDate(String.valueOf(rrcDTO.getCreatedDate()));
			//rrcType.setToDate(String.valueOf(rrcDTO.getCreatedDate()));
			rrcType.setToDate(SHAUtils.formatDate(rrcDTO.getRrcintiatedDate()));
			//rrcType.setToDate(String.valueOf(new Timestamp(System.currentTimeMillis())));

			payloadBO.setRrc(rrcType);
			payloadBO.setClaim(claimTypeValue);
			humanTaskForRRC.setPayload(payloadBO);
			submitProcessRRCRequestTask.execute(rrcDTO.getStrUserName(), humanTaskForRRC);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	 */return true;}

	private  RRCRequest getStage(Long stageKey) {
		List <RRCRequest> stage;

		Query findByStageKey = entityManager.createNamedQuery("RRCRequest.findByStageKey");
		findByStageKey.setParameter("stageKey", stageKey);

		try{
			stage = findByStageKey.getResultList();
			if(null != stage && !stage.isEmpty())
				return stage.get(0);

		}catch(Exception e)
		{
			e.printStackTrace();

		}
		return null;

	}


	private Stage getStageByKey(Long stageKey)
	{
		List<Stage> stageObj  = new ArrayList<Stage>();
		Query stageQuery = entityManager.createNamedQuery("Stage.findByKey");
		stageQuery = stageQuery.setParameter("stageKey", stageKey);
		stageObj = stageQuery.getResultList();
		if(null != stageObj && !stageObj.isEmpty())
		{
			entityManager.refresh(stageObj.get(0));
			return stageObj.get(0);
		}
		return null;
	}


	private Status getStatusByKey(Long stageKey)
	{
		List<Status> statusObj  = new ArrayList<Status>();
		Query statusList = entityManager.createNamedQuery("Status.findByKey");
		statusList = statusList.setParameter("statusKey", stageKey);
		statusObj = statusList.getResultList();
		if(null != statusObj && !statusObj.isEmpty())
		{
			entityManager.refresh(statusObj.get(0));
			return statusObj.get(0);
		}
		return null;
	}

	private  RRCRequest getStatus(Long statusKey) {
		List <RRCRequest> stage;

		Query findByStageKey = entityManager.createNamedQuery("RRCRequest.findByStatusKey");
		findByStageKey.setParameter("statusKey", statusKey);

		try{
			stage = findByStageKey.getResultList();
			if(null != stage && !stage.isEmpty())
				return stage.get(0);

		}catch(Exception e)
		{
			e.printStackTrace();

		}
		return null;

	}
	@Override
	public Class<RRCRequest> getDTOClass() {
		// TODO Auto-generated method stub
		return RRCRequest.class;
	} 

	public Boolean submitRequestRRCTaskToDB(RRCDTO rrcDTO)
	{
		try
		{
			//SubmitProcessRRCRequestTask submitProcessRRCRequestTask = BPMClientContext.getProcessRRCRequestSubmitTask(rrcDTO.getStrUserName(), rrcDTO.getStrPassword());

			Map<String, Object> wrkFlowMap = (Map<String, Object>) rrcDTO.getDbOutArray();

			// DocReceiptACKType receiptAckType = new DocReceiptACKType();
			// receiptAckType.setAckNumber(rodDTO.getDocumentDetails().getRodNumber());
			// receiptAckType.setKey(reimbursement.getKey());
			/*	RRCType rrcType = payloadBO.getRrc();
			//rrcType.setOutcome(rrcDTO.getRrcStatus());
			rrcType.setStatus(rrcDTO.getRrcStatus());
			wrkFlowMap.put(SHAConstants.PAYLOAD, value)*/
			//ClaimType claimTypeValue = new ClaimType();

			if(null != rrcDTO && rrcDTO.getClaimType().equalsIgnoreCase(SHAConstants.CASHLESS_CLAIM_TYPE))
			{
				wrkFlowMap.put(SHAConstants.CLAIM_TYPE, SHAConstants.CASHLESS_CLAIM_TYPE);
				//claimTypeValue.setClaimType(SHAConstants.CASHLESS_CLAIM_TYPE);
			}
			else
			{
				wrkFlowMap.put(SHAConstants.CLAIM_TYPE, SHAConstants.REIMBURSEMENT_CLAIM_TYPE);
				//claimTypeValue.setClaimType(SHAConstants.REIMBURSEMENT_CLAIM_TYPE);
			}

			if(null != rrcDTO.getEligibility())
			{
				if(("REFERPANEL").equalsIgnoreCase(rrcDTO.getEligibility().getValue()))
				{
					rrcDTO.getEligibility().setValue("REFERPANEL");
					wrkFlowMap.put(SHAConstants.RRC_ELIGIBILITY_TYPE, rrcDTO.getEligibility().getId());
					wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.RRC_REFER_TO_PANEL_OUTCOME);
					//rrcType.setEligibilityType(rrcDTO.getEligibility().getValue());
					//humanTaskForRRC.setOutcome(rrcDTO.getEligibility().getValue());
					//rrcType.setOutcome(rrcDTO.getEligibility().getValue());
				}

				else
				{
					if(null != rrcDTO.getEligibility().getValue() )
					{
						/*rrcType.setEligibilityType(rrcDTO.getEligibility().getValue());
					humanTaskForRRC.setOutcome(rrcDTO.getEligibility().getValue().toUpperCase());
					rrcType.setOutcome(rrcDTO.getEligibility().getValue());*/
						/**
						 * OTHER THAN REFER PANEL, THE OUTCOME WOULD BE END PROCESS.
						 * */
						wrkFlowMap.put(SHAConstants.RRC_ELIGIBILITY_TYPE, rrcDTO.getEligibility().getId());
						wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.RRC_END_PROCESS_OUTCOME);
						/*String output = rrcDTO.getEligibility().getValue().replaceAll("\\s+",""); 
						humanTaskForRRC.setOutcome(output.toUpperCase());
						rrcType.setOutcome(rrcDTO.getEligibility().getValue());*/

					}

				}

			}

			//String source = (String)wrkFlowMap.get(SHAConstants.STAGE_SOURCE);
			//wrkFlowMap.
			//		rrcType.setSource("FRESH");
			if(SHAConstants.RRC_STATUS_PROCESS.equalsIgnoreCase(rrcDTO.getRrcStatus()))
			{
				wrkFlowMap.put(SHAConstants.RRC_REQUEST_TYPE, "FRESH");
				//rrcType.setRequestType("FRESH");
			}
			else if(SHAConstants.RRC_STATUS_ON_HOLD.equalsIgnoreCase(rrcDTO.getRrcStatus()))
			{
				wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.RRC_ON_HOLD_OUTCOME);
				wrkFlowMap.put(SHAConstants.RRC_REQUEST_TYPE, SHAConstants.HOLD);
				/*rrcType.setRequestType(SHAConstants.HOLD);
				humanTaskForRRC.setOutcome(SHAConstants.HOLD);*/
			}
			//rrcType.setFromDate(String.valueOf(new Timestamp(System.currentTimeMillis())));
			//rrcType.setFromDate(SHAUtils.formatDate(rrcDTO.getRrcintiatedDate()));
			//rrcType.setFromDate(String.valueOf(rrcDTO.getCreatedDate()));
			//rrcType.setToDate(String.valueOf(rrcDTO.getCreatedDate()));
			//rrcType.setToDate(SHAUtils.formatDate(rrcDTO.getRrcintiatedDate()));
			//rrcType.setToDate(String.valueOf(new Timestamp(System.currentTimeMillis())));
			Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);

			DBCalculationService dbCalService = new DBCalculationService();
			dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
			/*payloadBO.setRrc(rrcType);
			payloadBO.setClaim(claimTypeValue);
			humanTaskForRRC.setPayload(payloadBO);
			submitProcessRRCRequestTask.execute(rrcDTO.getStrUserName(), humanTaskForRRC);*/
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SelectValue>  getRRCSubCatValues(Long categoryId) {

		Query query = entityManager.createNamedQuery("RRCSubCategory.findSubCatByCategoryId");
		query = query.setParameter("categoryId", categoryId);

		List<RRCSubCategory> rrcSubCategories =  (List<RRCSubCategory>)query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> SubCatValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue select = null;
		for (RRCSubCategory subCategory : rrcSubCategories) {
			if(subCategory !=null){
				select = new SelectValue();
				select.setId(subCategory.getKey());
				select.setValue(subCategory.getSubCategoryName());
				selectValuesList.add(select);
			}
		}
		SubCatValueContainer.addAll(selectValuesList);
		return SubCatValueContainer;	
	}

	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SelectValue> getRRCSourceValues(Long subCategoryId) {

		Query query = entityManager.createNamedQuery("RRCCategorySource.findSourceBySubCatId");
		query = query.setParameter("subCategoryId", subCategoryId);

		List<RRCCategorySource> rrcCategoriesSource = (List<RRCCategorySource>) query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> sourceValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue select = null;
		for (RRCCategorySource catSource : rrcCategoriesSource) {
			select = new SelectValue();
			select.setId(catSource.getKey());
			select.setValue(catSource.getSourceName());
			selectValuesList.add(select);
		}
		sourceValueContainer.addAll(selectValuesList);
		return sourceValueContainer;	
	}
}