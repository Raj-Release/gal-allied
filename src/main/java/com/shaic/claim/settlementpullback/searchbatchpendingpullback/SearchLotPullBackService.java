package com.shaic.claim.settlementpullback.searchbatchpendingpullback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.ws.security.util.StringUtil;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.TmpHospital;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.NewIntimationMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

@Stateless
public class SearchLotPullBackService extends AbstractDAO<Intimation>{

	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	
	public SearchLotPullBackService(){
		
	}
	
	
	public  Page<SearchLotPullBackTableDTO> searchForSettlementPullBack(
			SearchLotPullBackFormDTO formDTO,
			String userName, String passWord) {
		Page<SearchLotPullBackTableDTO> page = new Page<SearchLotPullBackTableDTO>();
		String intimationNo = formDTO.getIntimationNo() != null ? formDTO.getIntimationNo()!= null ? formDTO.getIntimationNo() : null : null; ;
		String policyNo = formDTO.getPolicyNo() != null ? formDTO.getPolicyNo()!= null ? formDTO.getPolicyNo() : null : null; ;;
		String wkIntimationNo = null;
	
		
		List<Map<String, Object>> taskProcedure = null ;
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		workFlowMap= new WeakHashMap<Long, Object>();
		Integer totalRecords = 0; 
		
		mapValues.put(SHAConstants.USER_ID, userName);
		mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.PULL_BACK_CURRENT_QUEUE);
		mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);
		
		Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
		
		DBCalculationService dbCalculationService = new DBCalculationService();
//		taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);	
		// Slowlness Issue in Undo FA -  Below condition added for IMSSUPPOR-29947
		taskProcedure = dbCalculationService.revisedGetTaskProcedureForUndoFAandLot(setMapValues);	
		
		
		if (null != taskProcedure) {
			for (Map<String, Object> outPutArray : taskProcedure) {							
					Long keyValue = (Long) outPutArray.get(SHAConstants.PAYLOAD_ROD_KEY);
					workFlowMap.put(keyValue,outPutArray);
					/*Below Code add New Qry For IMSSUPPOR-28837 - Qry Tn*/
					wkIntimationNo = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
				totalRecords = (Integer) outPutArray
						.get(SHAConstants.TOTAL_RECORDS);
			}
		}
		
		List<SearchLotPullBackTableDTO> dtoList = new ArrayList<SearchLotPullBackTableDTO>();
		if(intimationNo != null && taskProcedure != null && !taskProcedure.isEmpty()) { // Condtion added IMSSUPPOR-28837 Qry Tn
			/*Below Code Comment and add New Qry For IMSSUPPOR-28837 - Qry Tn*/
//			Intimation intimationByNo = getIntimationByNo(intimationNo);
			Intimation intimationByNo = getIntimationByNumber(wkIntimationNo);

			if(intimationByNo != null) {
				Claim claimforIntimation = getClaimforIntimation(intimationByNo.getKey());
				if(claimforIntimation != null) {
					List<Reimbursement> rodByClaimKey = getRODByClaimKey(claimforIntimation.getKey());
					if(rodByClaimKey != null && !rodByClaimKey.isEmpty()) {
						try {

							for (Reimbursement reimbursement : rodByClaimKey) {
								ClaimPayment paymentDetailsByRodKey = getPaymentDetailsByRodNumber(reimbursement.getRodNumber());
								Boolean faApprovedAndNotLotCreated = SHAUtils.isLotCreatedAndBatchNotCreated(paymentDetailsByRodKey);
								if(faApprovedAndNotLotCreated) {
									String message = "";
										for (Reimbursement otherROD : rodByClaimKey) {
											if(!otherROD.getKey().equals(reimbursement.getKey())) {
													ClaimPayment otherRODPayment = claimPaymentDetails(reimbursement.getKey());
													 if(SHAUtils.isBatchCreated(otherROD, otherRODPayment)) {
														message = "For this intimation batch created, you cannot pull back";
													}
												
											}
										}
									
									
										Object workflowKey = workFlowMap.get(reimbursement.getKey());
										if(null != workflowKey) {
											SearchLotPullBackTableDTO filledDTO = getFilledDTO(intimationByNo, claimforIntimation, reimbursement, userName, passWord, true,paymentDetailsByRodKey);
											//filledDTO.setHumanTask(humanTask);
											filledDTO.setDbOutArray(workflowKey);
											filledDTO.setMessage(message);
											dtoList.add(filledDTO);
											page.setTotalRecords(totalRecords);
										}
									
								} else {
//									dtoList.add(getFilledDTO(intimationByNo, claimforIntimation, reimbursement, userName, passWord, false));
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
					} else {
//						dtoList.add(getFilledDTO(intimationByNo, claimforIntimation, null, userName, passWord, false));
					}
				
				}
			}
		}
		page.setPageItems(dtoList);
		return page;
	}


	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	
	public Claim getClaimforIntimation(Long intimationKey) {
		Claim a_claim = null;
		if (intimationKey != null) {

			Query findByIntimationKey = entityManager
					.createNamedQuery("Claim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter(
					"intimationKey", intimationKey);
			try {

				if (findByIntimationKey.getResultList().size() > 0) {					
					a_claim = (Claim)findByIntimationKey.getResultList().get(0);
					entityManager.refresh(a_claim);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}

		} else {
			// intimationKey null
		}

		return a_claim;

	}
	
	public Status getStatusByKey(Long key) {

		try {
			Query findType = entityManager.createNamedQuery("Status.findByKey")
					.setParameter("statusKey", key);
			//Status status = (Status) findType.getSingleResult();
			List<Status> statusList  = findType.getResultList();
			if(null != statusList && !statusList.isEmpty())
			{
				entityManager.refresh(statusList.get(0));
				return statusList.get(0);
			}
			return null;
		} catch (Exception e) {
			return null;
		}

	}
	
	@SuppressWarnings("unchecked")
	public Intimation getIntimationByNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery(
				"Intimation.findByIntimationNo").setParameter(
				"intimationNo", "%" + intimationNo);

		List<Intimation> intimationList = (List<Intimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}
	
	public Intimation getIntimationByNumber(String intimationNo) {
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
	
	public List<Reimbursement> getRODByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findLatestRODByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<Reimbursement> resultList = query.getResultList();
		if (!resultList.isEmpty()) {
			for (Reimbursement reimbursement : resultList) {
				entityManager.refresh(reimbursement);
			}
		}
		return resultList;
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
	
	public ClaimPayment getPaymentDetailsByRodNumber(String rodNumber)
	{
		List<ClaimPayment> paymentDetailsList = new ArrayList<ClaimPayment>();
		if(rodNumber != null){
		Query findByPaymentKey = entityManager.createNamedQuery(
				"ClaimPayment.findLatestByRodNo").setParameter("rodNumber", rodNumber);
		try{
			paymentDetailsList = (List<ClaimPayment>) findByPaymentKey.getResultList();
			if(paymentDetailsList != null && !paymentDetailsList.isEmpty()){
				for(ClaimPayment claimPaymentDetail: paymentDetailsList){
					entityManager.refresh(claimPaymentDetail);
				}
				return paymentDetailsList.get(0);
			}
		}
		catch(Exception e){
			e.printStackTrace();			
		}
		}
		return null;
	}
	
	public ClaimPayment claimPaymentDetails(Long rodKey) {

		Query query = entityManager.createNamedQuery("ClaimPayment.findByRodKey");
		query.setParameter("rodKey", rodKey);

		List<ClaimPayment> result = (List<ClaimPayment>) query.getResultList();
		if (result != null && !result.isEmpty()) {
			return result.get(0);
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
	
	 private String getEmployeeName(String initiatorId) {
			Query findByTransactionKey = entityManager.createNamedQuery(
					"TmpEmployee.getEmpByLoginId").setParameter("loginId", initiatorId.toLowerCase());
			try{
				List<TmpEmployee> listOfEmployees =(List<TmpEmployee>) findByTransactionKey.getResultList();
				if(listOfEmployees != null && !listOfEmployees.isEmpty()) {
					return listOfEmployees.get(0).getEmpFirstName();
				}
			}
			catch(Exception e)
			{
				return "";
			}
			return "";				
		}
	
	protected SearchLotPullBackTableDTO getFilledDTO(Intimation intimation, Claim claim, Reimbursement reimbursement,  String userName, String password, Boolean isAllowedToPullBack,ClaimPayment claimPayment) {
		SearchLotPullBackTableDTO dto = new SearchLotPullBackTableDTO();
		NewIntimationDto intimationDto = getIntimationDto(intimation);
		dto.setNewIntimationDTO(intimationDto);
		dto.setPolicyDivCode(intimationDto.getOrganizationUnit() != null ? intimationDto.getOrganizationUnit().getOrganizationUnitName() : "");
		dto.setCpuCode(intimation.getCpuCode() != null ? String.valueOf(intimation.getCpuCode().getCpuCode()) : "" );
		dto.setClaimKey(claim.getKey());
		dto.setPolicyNo(intimation.getPolicy().getPolicyNumber());
		dto.setIntimationNo(intimation.getIntimationId());
		dto.setClaimNo(claim.getClaimId());
		dto.setInsuredPatientName(intimation.getInsuredPatientName());
		Hospitals hospitalDetail = getHospitalDetail(intimation.getHospital());
		dto.setHospitalCode(hospitalDetail.getHospitalCode());
		dto.setClaimType(claim.getClaimType().getValue());
		dto.setClaimStatus(getStatusByKey(claim.getStatus().getKey()).getProcessValue());
		dto.setHospitalName(hospitalDetail.getName());
		dto.setUsername(userName);
		dto.setPassword(password);
		
//		dto.setPolicyDivCode(intimation.getPolicy().get);
		if(reimbursement != null) {
			dto.setRodNo(reimbursement.getRodNumber());
			dto.setRodKey(reimbursement != null ? reimbursement.getKey() : 0l);
			dto.setApprovedAmount(reimbursement.getFinancialApprovedAmount() != null ? reimbursement.getFinancialApprovedAmount().longValue() : 0l);
			dto.setApprovedDate(SHAUtils.formatDate(reimbursement.getFinancialCompletedDate()));
			dto.setApproverName(getEmployeeName(reimbursement.getModifiedBy()));
		}
		
		dto.setClaimPayment(claimPayment);
		dto.setLotNumber(claimPayment.getLotNumber());
		dto.setLotCreatedBy(claimPayment.getCreatedBy());
		dto.setLotCreatedDate(SHAUtils.formatDate(claimPayment.getCreatedDate()));
		dto.setApprovedAmount(claimPayment.getTotalApprovedAmount().longValue());
		
		dto.setIsAllowedToProceed(isAllowedToPullBack);
		return dto;
	}
	
	private List<Insured> getInsuredListByPolicyNumber(String policyNumber){
		PolicyService policyService = new PolicyService();
		List<Insured> insuredList = null;
		if(null != policyNumber){
			insuredList = policyService.getInsuredList(policyNumber, entityManager);	
		}
		
		return insuredList;
	}
	
	private Policy getPolicyByPolicyNubember(String policyNumber){
		if(policyNumber != null){
			Query query = entityManager.createNamedQuery("Policy.findByPolicyNumber");
			query.setParameter("policyNumber", policyNumber);
	
			List<Policy> resultList =  (List<Policy>) query.getResultList();
			if(resultList != null && resultList.size()!=0){
				entityManager.refresh(resultList.get(0));
				return resultList.get(0);
			}
		}
		return null;
	}
	
	public OrganaizationUnit getInsuredOfficeNameByDivisionCode(
			String polDivnCode) {
		List<OrganaizationUnit> organizationUnit = null;
		if(polDivnCode != null){			
			Query findAll = entityManager.createNamedQuery("OrganaizationUnit.findByUnitId").setParameter("officeCode", polDivnCode);
			organizationUnit = (List<OrganaizationUnit>)findAll.getResultList();
		}
		
		if(organizationUnit != null && ! organizationUnit.isEmpty()){
			return organizationUnit.get(0);
		}
		
		
		return null;
	}
	
	private SelectValue getClaimType(Long hospitalTypeKey) {
		SelectValue claimType = new SelectValue();
		
		try{	
			MastersValue claimTypeMaser; 
		if( hospitalTypeKey != null && hospitalTypeKey == ReferenceTable.NETWORK_HOSPITAL_TYPE_ID){
			
			claimTypeMaser = entityManager.find(MastersValue.class, ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
			
		}
		else{
			claimTypeMaser = entityManager.find(MastersValue.class,ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
		}
		
		if(claimTypeMaser != null){
			claimType.setId(claimTypeMaser.getKey());
			claimType.setValue(claimTypeMaser.getValue());
	
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return claimType;
	}
	
	public TmpCPUCode getCpuObjectByPincode(String pincode) {
		TmpCPUCode cpuObject = null;
		if (pincode != null) {
			cpuObject = entityManager.find(TmpCPUCode.class,
					Long.valueOf(StringUtils.trim(pincode)));
		}
		return cpuObject;
	}
	
	@SuppressWarnings("unchecked")
	public NewIntimationDto getIntimationDto(Intimation intimation) {
		NewIntimationDto intimationToRegister = null;
		if (null != intimation) {
			NewIntimationMapper newIntimationMapper = NewIntimationMapper.getInstance();
			intimationToRegister = newIntimationMapper
					.getNewIntimationDto(intimation);
			intimationToRegister.setIntimationSource(intimation
					.getIntimationSource());
			if (null != intimation.getPolicy()) {
				if(null == intimation.getPolicy().getInsured() || intimation.getPolicy().getInsured().isEmpty()){
					List<Insured> insuredList = getInsuredListByPolicyNumber(intimation.getPolicy().getPolicyNumber());
					if(null != insuredList && !insuredList.isEmpty()){
						intimation.getPolicy().setInsured(insuredList);
					}
				}
				
				
				String address = (intimation.getPolicy().getProposerAddress1() != null ? intimation.getPolicy().getProposerAddress1() : "") + 
						(intimation.getPolicy().getProposerAddress2() != null ? intimation.getPolicy().getProposerAddress2() : "") + 
						(intimation.getPolicy().getProposerAddress3() != null ? intimation.getPolicy().getProposerAddress3() : "" );

				intimation.getPolicy().setProposerAddress(address);				
				
				intimationToRegister.setPolicy(intimation.getPolicy());	
				
				if(intimation.getPolicy() != null && intimation.getPolicy().getPolicyNumber() != null){
					System.out.println("---Start time of UNDO LOT getPolicyByPolicyNubember()---"+System.currentTimeMillis()+" "+intimation.getIntimationId());
				    Policy policy = getPolicyByPolicyNubember(intimation.getPolicy().getPolicyNumber());
				    System.out.println("---End time of UNDO LOT getPolicyByPolicyNubember()---"+System.currentTimeMillis()+" "+intimation.getIntimationId());
				    intimationToRegister.setPolicy(policy);
				}
				
				System.out.println("---Start time of UNDO LOT getInsuredOfficeNameByDivisionCode()---"+System.currentTimeMillis()+" "+intimation.getIntimationId());
				intimationToRegister
						.setOrganizationUnit(getInsuredOfficeNameByDivisionCode(intimation
								.getPolicy().getHomeOfficeCode()));
				System.out.println("---End time of UNDO LOT getInsuredOfficeNameByDivisionCode()---"+System.currentTimeMillis()+" "+intimation.getIntimationId());
				if (null != intimation.getPolicy().getLobId()) {
					MastersValue lineofBusiness = entityManager.find(
							MastersValue.class, intimation.getPolicy()
									.getLobId());
					intimationToRegister.setLineofBusiness(lineofBusiness
							.getValue() != null ? lineofBusiness.getValue()
							: "");
				}
			}

			if (null != intimation.getInsured()) {
				intimationToRegister.setInsuredPatient(intimation.getInsured());
				intimationToRegister.setInsuredAge(intimationToRegister.getInsuredCalculatedAge());
			}
		
			Hospitals registeredHospital = null;
			TmpHospital tempHospital = null;

			if (intimation.getHospital() != null
					&& intimation.getHospitalType() != null
					&& StringUtils.contains(intimation.getHospitalType()
							.getValue().toLowerCase(), "network")) {
				registeredHospital = entityManager.find(Hospitals.class,
						intimation.getHospital());
				if (null != registeredHospital) {
					HospitalDto hospitaldto = new HospitalDto(
							registeredHospital);
					hospitaldto.setRegistedHospitals(registeredHospital);

					intimationToRegister.setHospitalDto(hospitaldto);
					if(registeredHospital.getAddress() != null){
						String[] hospAddress = StringUtil.split(registeredHospital.getAddress(),',');
						int length;
						if(hospAddress != null && hospAddress.length != 0 ){
							length=hospAddress.length;			
							intimationToRegister.getHospitalDto().setHospAddr1(hospAddress[0]);
							if(length >2){
								intimationToRegister.getHospitalDto().setHospAddr2(hospAddress[1]);
							}
							if(length >3){
								intimationToRegister.getHospitalDto().setHospAddr3(hospAddress[2]);
							}
							if(length >4){
								intimationToRegister.getHospitalDto().setHospAddr4(hospAddress[3]);
							}

						}
					}
					
					
				}
			} else {
				if(intimation.getHospital() != null) {
					tempHospital = entityManager.find(TmpHospital.class,
							intimation.getHospital());
					HospitalDto hospitalDto = new HospitalDto(tempHospital,
							intimation.getHospitalType().getValue());
					hospitalDto.setNotRegisteredHospitals(tempHospital);
				}
				
			}
			

			if (intimation.getHospitalType() != null){
				SelectValue claimType = getClaimType(intimation.getHospitalType().getKey());
				intimationToRegister.setClaimType(claimType);
			}
			
			
			TmpCPUCode cpuObject = intimation.getCpuCode();

			if (null != cpuObject) {
				intimationToRegister.setCpuCode(cpuObject.getCpuCode());
				intimationToRegister.setCpuId(cpuObject.getKey());
				intimationToRegister.setCpuAddress(cpuObject.getAddress());
			}
			
			String hospitalType = intimation.getHospitalType() != null ? intimation
					.getHospitalType().getValue().toString().toLowerCase()
					: null;
			
					
			if (hospitalType != null
					&& StringUtils.containsIgnoreCase(hospitalType, "network")) {
				if (registeredHospital != null) {

					if (null == cpuObject) {
						cpuObject = getCpuObjectByPincode(registeredHospital
								.getPincode());
					}
				}

			} else if (null != intimation.getHospitalType() && StringUtils.contains(intimation.getHospitalType()
					.getValue(), "not-registered")) {
//				if (tempHospital.getPincode() != null) {
//					cpuObject = getCpuObjectByPincode(tempHospital.getPincode()
//							.toString());
//				}
			}
			if(null == intimation.getCpuCode() && null != cpuObject )
			{
				intimationToRegister.setCpuCode(cpuObject.getCpuCode());
				intimationToRegister.setCpuId(cpuObject.getKey());
				
			}
			intimationToRegister
					.setIntimaterName(intimation.getIntimaterName() != null ? intimation
							.getIntimaterName() : "");
			if (intimation.getAdmissionDate() != null) {
				intimationToRegister.setAdmissionDate(intimation
						.getAdmissionDate());
			}

			if (intimation.getCreatedDate() != null) {
				intimationToRegister.setDateOfIntimation(intimation
						.getCreatedDate().toString());
			}

			if (intimation.getRoomCategory() != null) {
				SelectValue roomCategory = new SelectValue();
				roomCategory.setId(intimation.getRoomCategory().getKey());
				roomCategory.setValue(intimation.getRoomCategory().getValue());
				intimationToRegister.setRoomCategory(roomCategory);
			}
			if (intimation.getPolicy() != null) {
				intimationToRegister.setAgentBrokerCode(intimation.getPolicy()
						.getAgentCode() != null ? intimation.getPolicy()
						.getAgentCode() : "");
				intimationToRegister.setAgentBrokerName(intimation.getPolicy()
						.getAgentName() != null ? intimation.getPolicy()
						.getAgentName() : "");
				if(intimation.getPolicy().getProduct()!=null){
					intimationToRegister.setProductName(intimation.getPolicy()
							.getProduct().getValue() != null ? intimation
							.getPolicy().getProduct().getValue() : "");
				}
				
			}
			intimationToRegister.setStatus(intimation.getStatus());
			intimationToRegister.setStage(intimation.getStage());
		}
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		if(intimation.getInsured() != null && intimation.getPolicy() != null){
		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimation.getInsured().getInsuredId().toString(), intimation.getPolicy().getKey(),intimation.getInsured().getLopFlag());
		intimationToRegister.setOrginalSI(insuredSumInsured);
		}
		
		
		return intimationToRegister;
	}

}
