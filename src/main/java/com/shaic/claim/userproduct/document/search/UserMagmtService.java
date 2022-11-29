package com.shaic.claim.userproduct.document.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Digits;

import org.apache.ws.security.message.token.Timestamp;
import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.userproduct.document.ApplicableCpuDTO;
import com.shaic.claim.userproduct.document.ProductAndDocumentTypeDTO;
import com.shaic.claim.userproduct.document.UserProductMapping;
import com.shaic.domain.MasAreaCodeMapping;
import com.shaic.domain.MasAreaOrgUserCpuMapping;
import com.shaic.domain.MasRoleLimit;
import com.shaic.domain.MasSecUserDiagnosisMapping;
import com.shaic.domain.MasSecVSPUserLabelMapping;
import com.shaic.domain.MasUser;
import com.shaic.domain.MasUserDiagnosis;
import com.shaic.domain.MasUserLimitMapping;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Product;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.UserToOrgMapping;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;

@Stateless
public class UserMagmtService {
	
	//String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);

	public UserMagmtService() {
		super();
	}

	@PersistenceContext
	protected EntityManager entityManager;

	public Page<SearchDoctorDetailsTableDTO> search(
			SearchDoctorNameDTO searchFormDTO) {

		List<TmpEmployee> listOfClaim = new ArrayList<TmpEmployee>();
		try {
			String doctorName = null != searchFormDTO.getDoctorName()
					&& !searchFormDTO.getDoctorName().isEmpty() ? searchFormDTO
					.getDoctorName() : null;

			final CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			final CriteriaQuery<TmpEmployee> criteriaQuery = criteriaBuilder
					.createQuery(TmpEmployee.class);

			Root<TmpEmployee> root = criteriaQuery.from(TmpEmployee.class);

			List<Predicate> conditionList = new ArrayList<Predicate>();
			if (doctorName != null) {

				Predicate empName = criteriaBuilder.like(criteriaBuilder
						.lower(root.<String> get("empFirstName")), "%"
						+ doctorName.toLowerCase() + "%");
				Predicate empId = criteriaBuilder.like(
						criteriaBuilder.lower(root.<String> get("empId")), "%"
								+ doctorName.toLowerCase() + "%");
				
				Predicate finanPred = criteriaBuilder.or(empName, empId);
				
			//	Predicate conditionActiveStatus = criteriaBuilder.equal(root.<Long> get("activeStatus"), 1);
				
				Predicate finalQuery = criteriaBuilder.and(finanPred);
				conditionList.add(finalQuery);
			}

			if (doctorName != null) {
				criteriaQuery.select(root).where(
						criteriaBuilder.and(conditionList
								.toArray(new Predicate[] {})));
			}
			final TypedQuery<TmpEmployee> typedQuery = entityManager
					.createQuery(criteriaQuery);
			listOfClaim = typedQuery.getResultList();
			List<SearchDoctorDetailsTableDTO> tableDTO = UserManagementMapper
					.getDoctorDetails(listOfClaim);
			List<SearchDoctorDetailsTableDTO> result = new ArrayList<SearchDoctorDetailsTableDTO>();
			result.addAll(tableDTO);
			Page<SearchDoctorDetailsTableDTO> page = new Page<SearchDoctorDetailsTableDTO>();
			if (result.size() <= 10) {
				page.setHasNext(false);
			} else {
				page.setHasNext(true);
			}
			if (result.isEmpty()) {

			}
			page.setPageItems(result);
			page.setIsDbSearch(true);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;

	}

	public void userCreation(String userId,String userName) {
		
		Boolean emp = getEmployeeByName(userId);
		Boolean userEmp = getMasUserEmpDetails(userId);
		if (emp && userEmp) {
			// Notification.show("User Already Exists");
			showErrorMessage("User Already Exists");
		} else {
			MasUser userAvalialble = getShiftDetailsByIdForUserMgmt(userId);
			TmpEmployee userAvali = getEmployessValuesByInactiveStatus(userId);
			if(userAvalialble != null && userAvalialble.getActiveStatus() != null) {
				userAvalialble.setActiveStatus(SHAConstants.YES_FLAG);
				userAvalialble.setModifiedDate(new java.sql.Timestamp(System
					.currentTimeMillis()));
				userAvalialble.setModifiedBy(userName);
				userAvalialble.setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
				userAvalialble.setModifiedBy(userName);
				entityManager.merge(userAvalialble);
				entityManager.flush();
				entityManager.clear();
			}else{
			MasUser masUser = new MasUser();
			masUser.setEmpId(userId);
			masUser.setUserId(userId);
			masUser.setActiveStatus(SHAConstants.YES_FLAG);
			masUser.setCreatedDate(new java.sql.Timestamp(System
					.currentTimeMillis()));
			masUser.setCreatedBy(userName);
			masUser.setManualPickHybridFlag(SHAConstants.YES_FLAG);
			entityManager.persist(masUser);
			entityManager.flush();
			entityManager.clear();
			
			}
			
			if(userAvali == null){
			TmpEmployee tmpEmp = new TmpEmployee();
			tmpEmp.setActiveStatus(1l);
			tmpEmp.setEmpId(userId);
			tmpEmp.setLoginId(userId);
			
			tmpEmp.setCreatedDate(new java.sql.Timestamp(System
					.currentTimeMillis()));
			entityManager.persist(tmpEmp);
			}
			showSuccessMessage("User Created Successfully");

		}

	}

	public Boolean getEmployeeByName(String empId) {

		String tmpEmployee = null;

		empId = empId.toLowerCase();

		Query query = entityManager.createNamedQuery("TmpEmployee.findByEmpId");
		query.setParameter("empId", empId);

		List<TmpEmployee> tmpEmployeeList = query.getResultList();

		if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
			return true;

		}

		return false;

	}
	
	public Boolean getMasUserEmpDetails(String empId) {

		String tmpEmployee = null;

		empId = empId.toLowerCase();

		Query query = entityManager.createNamedQuery("MasUser.getEmpDetailsById");
		query.setParameter("empId", empId);

		List<MasUser> tmpEmployeeList = query.getResultList();

		if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
			return true;

		}

		return false;

	}

	public Boolean getEmployeeByActiveStatus(String empId) {

		String tmpEmployee = null;

		empId = empId.toLowerCase();

		Query query = entityManager
				.createNamedQuery("TmpEmployee.findByEmpIdWithInactive");
		query.setParameter("empId", empId);

		List<TmpEmployee> tmpEmployeeList = query.getResultList();

		if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
			return true;

		}

		return false;

	}

	private void showErrorMessage(String eMsg) {
		
		MessageBox.createError()
    	.withCaptionCust("Error").withHtmlMessage(eMsg.toString())
        .withOkButton(ButtonOption.caption("OK")).open();
	}
	
	private void showSuccessMessage(String eMsg) {
		
		MessageBox.create()
    	.withCaptionCust("Success").withHtmlMessage(eMsg.toString())
        .withOkButton(ButtonOption.caption("OK")).open();
	}

	public void updateUserManagementCpu(UserManagementDTO userMgmtDTO,
			String userName) {

		List<UserMgmtApplicableCpuDTO> cpuDetailsDto = userMgmtDTO
				.getApplicableCpuList();
		List<UserMgmtDaignosisDTO> diagnosisDto = userMgmtDTO.getDiagnosisList();
		
		//ProductMappingList
		List<UserMgmtProductMappingDTO> productMappingDto = userMgmtDTO.getProductMappingList();
		
		//Label 
		List<UserManagementLabelDTO> labelDto = userMgmtDTO.getLabelDTOList();
		
		
		TmpEmployee empkey = getEmployessValues(userMgmtDTO.getUserId());
		MasUser userTypes = getEmpByIdForUserMgmt(userMgmtDTO.getUserId());
		MasUser getShiftDetails = getShiftDetailsByIdForUserMgmt(userMgmtDTO
				.getUserId());
		SelectValue userSelectValue = userMgmtDTO.getType();
		
		SelectValue documentSelectValue = userMgmtDTO.getAutoAlloccationType();

		if (userMgmtDTO.getLimitList() != null
				&& !userMgmtDTO.getLimitList().isEmpty()) {
			addUserLimitMapping(userMgmtDTO.getLimitList(),
					userMgmtDTO.getUserId());
		}
		if(userMgmtDTO.getDeletedList() != null && !userMgmtDTO.getDeletedList().isEmpty()) {
			deleteUserLimit(userMgmtDTO.getDeletedList(),userMgmtDTO.getUserId());
		}

		if (userMgmtDTO.getDeActivateUser() != null
				&& userMgmtDTO.getDeActivateUser().equals(true)) {
			deactivateUserMgmt(userMgmtDTO,userName);
		} else if (userMgmtDTO.getDeActivateUser().equals(false)) {
			activateUserMgmt(userMgmtDTO,userName);
		}

		if (userSelectValue != null && userTypes != null) {
			if (userSelectValue.getValue().equalsIgnoreCase(
					SHAConstants.CPU_ALLOCATION_CORP_USER)) {
				userTypes.setCorporateUserFlag(SHAConstants.YES_FLAG);

			} else if (userSelectValue.getValue().equalsIgnoreCase(
					SHAConstants.CPU_ALLOCATION_CPU_USER)) {
				userTypes.setCorporateUserFlag(SHAConstants.N_FLAG);
			}
			if (empkey != null) {
				userTypes.setUserId(empkey.getEmpId());
			}
			userTypes.setUserName(userMgmtDTO.getUserName());

			if (userMgmtDTO.getHighValueClaim() != null
					&& userMgmtDTO.getHighValueClaim().equals(true)) {
				userTypes.setHighValueFlag(SHAConstants.YES_FLAG);
				userTypes.setShiftStartTime(userMgmtDTO.getShiftStartTime());
				userTypes.setShiftEndTime(userMgmtDTO.getShiftEndTime());
			} else if (userMgmtDTO.getHighValueClaim() != null
					&& userMgmtDTO.getHighValueClaim().equals(false)
					&& userMgmtDTO.getHighValueClaim() != null) {
				userTypes.setHighValueFlag(SHAConstants.N_FLAG);
				userTypes.setShiftStartTime("");
				userTypes.setShiftEndTime("");
			}
			//CR2019213
			if(userMgmtDTO.getManaulFlag() != null && userMgmtDTO.getManaulFlag().equals(true)){
				userTypes.setManualFlag(SHAConstants.YES_FLAG);
			}else {
				userTypes.setManualFlag(SHAConstants.N_FLAG);
			}
			// added Allow Manual coding field in User Management screen
			if(userMgmtDTO.getManualCodingFlag() != null && userMgmtDTO.getManualCodingFlag().equals(true)){
				userTypes.setManualCodingFlag(SHAConstants.YES_FLAG);
			}else {
				userTypes.setManualCodingFlag(SHAConstants.N_FLAG);
			}
			
			if(documentSelectValue != null){
				MastersValue documentTypeClaim = getMaster(documentSelectValue.getId());
	
				if(documentSelectValue.getValue().equalsIgnoreCase(documentTypeClaim.getValue()) ){
					userTypes.setDocumenType(documentTypeClaim.getMappingCode());
				}
			}
			
			if (userMgmtDTO.getClaimFlagInUserMaster() != null) {
				userTypes.setClaimApplicable(userMgmtDTO.getClaimFlagInUserMaster().getValue().toUpperCase());
			}
			
			//GLX2020178 by noufel
			if(userMgmtDTO.getRestrictToBand() != null && userMgmtDTO.getRestrictToBand().equals(true)){
				userTypes.setAllowRestrictToBand(SHAConstants.YES_FLAG);
			}else {
				userTypes.setAllowRestrictToBand(SHAConstants.N_FLAG);
			}
			
			//GLX2021004
			if(userMgmtDTO.getManualPickMAFlag() != null && userMgmtDTO.getManualPickMAFlag().equals(true)){
				userTypes.setManualPickFlagMA(SHAConstants.YES_FLAG);
			}else {
				userTypes.setManualPickFlagMA(SHAConstants.N_FLAG);
			}
			
			//added for combined processing CPU
			if(userMgmtDTO.getManualPickCombinedProcessFlag() != null && userMgmtDTO.getManualPickCombinedProcessFlag().equals(true)){
				userTypes.setManualPickHybridFlag(SHAConstants.YES_FLAG);
			}else {
				userTypes.setManualPickHybridFlag(SHAConstants.N_FLAG);
			}
			userTypes.setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
			userTypes.setModifiedBy(userName);

			entityManager.merge(userTypes);
		}
		if (cpuDetailsDto != null) {
			for (UserMgmtApplicableCpuDTO applicableCpuDTO : cpuDetailsDto) {
				if (applicableCpuDTO.getAccessability() != null) {

					String cpuCodeVal1[] = applicableCpuDTO.getCpuCode().split(
							"_");
					if (null != cpuCodeVal1) {
						String strcpuCode1 = cpuCodeVal1[0];

						if (applicableCpuDTO.getAccessability() != null
								&& applicableCpuDTO.getAccessability()) {
							List<MasAreaOrgUserCpuMapping> userAreaMappingList = getUserAreaOrgMappingDetails(
									strcpuCode1, userMgmtDTO.getUserId());

							
							if(applicableCpuDTO.getAreaCodeSelectedList() != null && !applicableCpuDTO.getAreaCodeSelectedList().isEmpty()) {
								for (String selectedValues1 : applicableCpuDTO.getAreaCodeSelectedList()) {
									String selectedValues2[] = selectedValues1.split("-");
										String selectedValues = selectedValues2[0].trim();
									MasAreaOrgUserCpuMapping isAreaCodeActiveOrNot = getUserMappingDetailsByAreaCode(selectedValues,userMgmtDTO.getUserId());
									MasAreaOrgUserCpuMapping userAreaMapping = new MasAreaOrgUserCpuMapping();
									MasAreaCodeMapping areaKeyAndCode = getAreaMappingCodeAndKey(selectedValues);
									if(userAreaMappingList == null || userAreaMappingList.isEmpty()){
										if(isAreaCodeActiveOrNot == null){
										userAreaMapping.setAreaCode(areaKeyAndCode.getAreaCode());
										userAreaMapping.setMasAreaMapping(areaKeyAndCode);
										userAreaMapping.setUserId(empkey.getEmpId());
										userAreaMapping.setCreatedBy(userName);
										userAreaMapping
												.setCreatedDate(new java.sql.Timestamp(
														System.currentTimeMillis()));
										userAreaMapping
													.setActiveStatus(SHAConstants.YES_FLAG);
										userAreaMapping.setCpuCode(strcpuCode1);
										entityManager.persist(userAreaMapping);
										entityManager.flush();
										entityManager.clear();
										}
										else if((isAreaCodeActiveOrNot != null && isAreaCodeActiveOrNot.getAreaCode().equalsIgnoreCase(selectedValues))
												&& isAreaCodeActiveOrNot.getActiveStatus() != null && isAreaCodeActiveOrNot.getActiveStatus().equalsIgnoreCase(SHAConstants.N_FLAG)){

											isAreaCodeActiveOrNot.setAreaCode(areaKeyAndCode.getAreaCode());
											isAreaCodeActiveOrNot.setMasAreaMapping(areaKeyAndCode);
											isAreaCodeActiveOrNot.setUserId(empkey.getEmpId());
											userAreaMapping.setCpuCode(strcpuCode1);
											isAreaCodeActiveOrNot.setModifiedBy(userName);
											isAreaCodeActiveOrNot
											.setModifiedDate(new java.sql.Timestamp(
													System.currentTimeMillis()));
											isAreaCodeActiveOrNot
											.setActiveStatus(SHAConstants.YES_FLAG);
											entityManager.merge(isAreaCodeActiveOrNot);
											entityManager.flush();
											entityManager.clear();
										}
									}else if(userAreaMappingList != null && !userAreaMappingList.isEmpty()){
										//										for(MasAreaOrgUserCpuMapping isselectedAreaCode : userAreaMappingList) {
//										MasAreaOrgUserCpuMapping isAreaCodeActiveOrNot = getUserMappingDetailsByAreaCode(selectedValues,userMgmtDTO.getUserId());
										if(isAreaCodeActiveOrNot == null) {
												userAreaMapping.setAreaCode(areaKeyAndCode.getAreaCode());
												userAreaMapping.setMasAreaMapping(areaKeyAndCode);
												userAreaMapping.setUserId(empkey.getEmpId());
												userAreaMapping.setCreatedBy(userName);
												userAreaMapping
												.setCreatedDate(new java.sql.Timestamp(
														System.currentTimeMillis()));
												userAreaMapping
												.setActiveStatus(SHAConstants.YES_FLAG);
												userAreaMapping.setCpuCode(strcpuCode1);
												entityManager.persist(userAreaMapping);
												entityManager.flush();
												entityManager.clear();
											}else if((isAreaCodeActiveOrNot != null && isAreaCodeActiveOrNot.getAreaCode().equalsIgnoreCase(selectedValues))
													&& isAreaCodeActiveOrNot.getActiveStatus() != null && isAreaCodeActiveOrNot.getActiveStatus().equalsIgnoreCase(SHAConstants.N_FLAG)){

												isAreaCodeActiveOrNot.setAreaCode(areaKeyAndCode.getAreaCode());
												isAreaCodeActiveOrNot.setMasAreaMapping(areaKeyAndCode);
												isAreaCodeActiveOrNot.setUserId(empkey.getEmpId());
												userAreaMapping.setCpuCode(strcpuCode1);
												isAreaCodeActiveOrNot.setModifiedBy(userName);
												isAreaCodeActiveOrNot
												.setModifiedDate(new java.sql.Timestamp(
														System.currentTimeMillis()));
												isAreaCodeActiveOrNot
												.setActiveStatus(SHAConstants.YES_FLAG);
												entityManager.merge(isAreaCodeActiveOrNot);
												entityManager.flush();
												entityManager.clear();
											}
											//										}
										}
								}
							}
							
							 if(userAreaMappingList != null && ! userAreaMappingList.isEmpty() && applicableCpuDTO.getAreaCodeSelectedList() != null){
								 List<String> deletedAreaCpuCode  = new ArrayList<String>();
								 List<String> dumpAreaCpuCode  = new ArrayList<String>();
								 deletedAreaCpuCode = applicableCpuDTO.getAreaCodeSelectedList();
								 for(MasAreaOrgUserCpuMapping updateDeleteFlagForAreaCode : userAreaMappingList){
									 dumpAreaCpuCode.add(updateDeleteFlagForAreaCode.getAreaCode().toString() + "-" +updateDeleteFlagForAreaCode.getMasAreaMapping().getAreaName());
									 if(deletedAreaCpuCode == null || deletedAreaCpuCode.isEmpty()){
										 MasAreaOrgUserCpuMapping deleteFlagUpdate = getUserMappingDetailsByAreaCode(updateDeleteFlagForAreaCode.getAreaCode(),userMgmtDTO.getUserId());
//										 deleteFlagUpdate.setUserId(empkey.getEmpId());
										 deleteFlagUpdate.setModifiedBy(userName);
										 deleteFlagUpdate
										 .setModifiedDate(new java.sql.Timestamp(
												 System.currentTimeMillis()));
										 deleteFlagUpdate
										 .setActiveStatus(SHAConstants.N_FLAG);
										 entityManager.merge(deleteFlagUpdate);
										 entityManager.flush();
										 entityManager.clear();
									 }
									 
									
								 }
								 if(deletedAreaCpuCode !=  null){

									 dumpAreaCpuCode.removeAll(deletedAreaCpuCode);
									 
									 for (String deleteAreaCodeRemoved : dumpAreaCpuCode ) {
										 String deleteAreaCode[] = deleteAreaCodeRemoved.split("-");
											String deleteAreaCodeToRemoved = deleteAreaCode[0].trim();
										 MasAreaOrgUserCpuMapping deleteFlagUpdate = getUserMappingDetailsByAreaCode(deleteAreaCodeToRemoved,userMgmtDTO.getUserId());
//										 deleteFlagUpdate.setUserId(empkey.getEmpId());
										 deleteFlagUpdate.setModifiedBy(userName);
										 deleteFlagUpdate
										 .setModifiedDate(new java.sql.Timestamp(
												 System.currentTimeMillis()));
										 deleteFlagUpdate
										 .setActiveStatus(SHAConstants.N_FLAG);
										 entityManager.merge(deleteFlagUpdate);
										 entityManager.flush();
										 entityManager.clear();
									 }
									 System.out.println(dumpAreaCpuCode);

								 }
							 }
							List<UserToOrgMapping> userMappingGmcRtl = getUserOrgMappingDetails(
									strcpuCode1, userMgmtDTO.getUserId());

							if (userMappingGmcRtl != null
									&& !userMappingGmcRtl.isEmpty()) {
								if (userMappingGmcRtl.size() == 2) {
									activeOrDeactiveRtlGmc(applicableCpuDTO,
											userMappingGmcRtl, 2, userName);
								} else {
									activeOrDeactiveRtlGmc(applicableCpuDTO,
											userMappingGmcRtl, 1, userName);
								}

							} else {
								TmpCPUCode tmpCpuCode = getCpuByCodeForUserMgmt(Long
										.valueOf(applicableCpuDTO.getCpuCode()));

								UserToOrgMapping userMapping;
								UserToOrgMapping userMappingGMC;
								userMapping = new UserToOrgMapping();
								userMapping.setOrgId(tmpCpuCode.getOrgId());
								userMapping.setUserId(empkey.getEmpId());
								userMapping.setCreatedBy(userName);
								userMapping
										.setCreatedDate(new java.sql.Timestamp(
												System.currentTimeMillis()));
								if (applicableCpuDTO.getAccessability() != null
										&& applicableCpuDTO.getAccessability()) {

									userMapping
											.setActiveStatus(SHAConstants.YES_FLAG);

									if ((applicableCpuDTO.getAccessability() != null && applicableCpuDTO
											.getAccessability())
											&& ((applicableCpuDTO
													.getGmcCheckBox() == null || (applicableCpuDTO
													.getGmcCheckBox() != null && !applicableCpuDTO
													.getGmcCheckBox())))
											&& ((applicableCpuDTO
													.getRetailCheckBox() == null || (applicableCpuDTO
													.getRetailCheckBox() != null && !applicableCpuDTO
													.getRetailCheckBox())))) {
										entityManager.persist(userMapping);
										entityManager.flush();
										entityManager.clear();
										//break;
									}

									if ((applicableCpuDTO.getGmcCheckBox() != null && applicableCpuDTO
											.getGmcCheckBox())
											&& (applicableCpuDTO
													.getRetailCheckBox() != null && applicableCpuDTO
													.getRetailCheckBox())) {

										userMapping
												.setLobFlag(SHAConstants.RETAIL_USER_MANAGEMENT);

										userMappingGMC = new UserToOrgMapping();
										userMappingGMC.setOrgId(tmpCpuCode
												.getOrgId());
										userMappingGMC.setUserId(empkey
												.getEmpId());
										userMappingGMC.setCreatedBy(userName);
										userMappingGMC
												.setCreatedDate(new java.sql.Timestamp(
														System.currentTimeMillis()));
										userMappingGMC
										.setActiveStatus(SHAConstants.YES_FLAG);
										userMappingGMC
												.setLobFlag(SHAConstants.GMC_POL_SERIVICE);

										entityManager.persist(userMappingGMC);
										entityManager.persist(userMapping);
										entityManager.flush();
										entityManager.clear();

									}

									else if ((applicableCpuDTO.getGmcCheckBox() != null && applicableCpuDTO
											.getGmcCheckBox())
											|| (applicableCpuDTO
													.getRetailCheckBox() != null && applicableCpuDTO
													.getRetailCheckBox())) {
										if (applicableCpuDTO.getGmcCheckBox() != null
												&& applicableCpuDTO
														.getGmcCheckBox()) {
											userMapping
													.setLobFlag(SHAConstants.GMC_POL_SERIVICE);
										} else if (applicableCpuDTO
												.getRetailCheckBox() != null
												&& applicableCpuDTO
														.getRetailCheckBox()) {
											userMapping
													.setLobFlag(SHAConstants.RETAIL_USER_MANAGEMENT);
										}
										userMapping.setActiveStatus(SHAConstants.YES_FLAG);
										entityManager.persist(userMapping);
										entityManager.flush();
										entityManager.clear();
									}

								}

							}
						} else {
							if (applicableCpuDTO.getAccessability() != null
									&& !applicableCpuDTO.getAccessability()) {
								List<UserToOrgMapping> userMappingGmcRtl = getUserOrgMappingDetails(
										strcpuCode1, userMgmtDTO.getUserId());
								for (UserToOrgMapping userToOrgMapping : userMappingGmcRtl) {
									userToOrgMapping
											.setActiveStatus(SHAConstants.N_FLAG);
									userToOrgMapping.setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
									userToOrgMapping.setModifiedBy(userName);
									entityManager.merge(userToOrgMapping);
									entityManager.flush();
								}
							}

						}

					}
				}
			}
		}
			

		if (null != userMgmtDTO && null != userMgmtDTO.getDeletedList()
				&& !userMgmtDTO.getDeletedList().isEmpty()) {
			for (UserManagementDTO userMgmtDto : userMgmtDTO.getDeletedList()) {
				MasUserLimitMapping empDetails = getUserLimitEmployeeByName(
						userMgmtDTO.getUserId(), userMgmtDto.getLimit()
								.getValue());
				if (null != empDetails) {
					empDetails.setActiveStatus(SHAConstants.N_FLAG);
					empDetails.setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
					empDetails.setModifiedBy(userName);
					entityManager.merge(empDetails);
					entityManager.flush();
					entityManager.clear();
				}
			}
		}
		
		// diagnosis List
		if(diagnosisDto != null){
			String currentUserName = (String) UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID);
			for(UserMgmtDaignosisDTO diagnosisList: diagnosisDto ){
				MasSecUserDiagnosisMapping userDiagnosis = getUserDiagnosisMappingDetails(
						diagnosisList.getKey(), userMgmtDTO.getUserId());
				if(userDiagnosis == null && diagnosisList.getDiagnosisEnable() != null && diagnosisList.getDiagnosisEnable()){
					MasSecUserDiagnosisMapping diagnosisMappingValue;
					diagnosisMappingValue = new MasSecUserDiagnosisMapping();
					diagnosisMappingValue.setUserId(userMgmtDTO.getUserId());
					diagnosisMappingValue.setDiagnosisKey(diagnosisList.getKey());
					diagnosisMappingValue.setDiagnosisValue(diagnosisList.getValue());
					if(diagnosisList.getDiagnosisEnable() != null && diagnosisList.getDiagnosisEnable()){
						diagnosisMappingValue.setActiveStatus((long) 1);
					}else{
						diagnosisMappingValue.setActiveStatus((long) 0);
					}
					diagnosisMappingValue.setCreatedBy(currentUserName.toUpperCase());
					diagnosisMappingValue.setCreatedDate(new java.sql.Timestamp(System.currentTimeMillis()));
					entityManager.persist(diagnosisMappingValue);
					entityManager.flush();
					entityManager.clear();
				}else if(userDiagnosis != null &&  diagnosisList.getDiagnosisEnable() != null && diagnosisList.getDiagnosisEnable()){
					if(userDiagnosis.getUserId().equalsIgnoreCase(userMgmtDTO.getUserId()) && userDiagnosis.getDiagnosisKey().equals(diagnosisList.getKey())){
						userDiagnosis.setDiagnosisValue(diagnosisList.getValue());
						if(diagnosisList.getDiagnosisEnable() != null && diagnosisList.getDiagnosisEnable()){
							userDiagnosis.setActiveStatus((long) 1);
						}else{
							userDiagnosis.setActiveStatus((long) 0);
						}
						userDiagnosis.setModifiedBy(currentUserName.toUpperCase());
						userDiagnosis.setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
						entityManager.merge(userDiagnosis);
						entityManager.flush();
						entityManager.clear();
					}
				}else if(userDiagnosis != null &&  diagnosisList.getDiagnosisEnable() != null && !diagnosisList.getDiagnosisEnable()){
					if(userDiagnosis.getUserId().equalsIgnoreCase(userMgmtDTO.getUserId()) && userDiagnosis.getDiagnosisKey().equals(diagnosisList.getKey())){
						userDiagnosis.setDiagnosisValue(diagnosisList.getValue());
						if(diagnosisList.getDiagnosisEnable() != null && diagnosisList.getDiagnosisEnable()){
							userDiagnosis.setActiveStatus((long) 1);
						}else{
							userDiagnosis.setActiveStatus((long) 0);
						}
						userDiagnosis.setModifiedBy(currentUserName.toUpperCase());
						userDiagnosis.setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
						entityManager.merge(userDiagnosis);
						entityManager.flush();
						entityManager.clear();
					}
				}
			}
			
		}
		
		
		// User Label List
				if(labelDto != null){
					String currentUserName = (String) UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID);
					for(UserManagementLabelDTO labelList: labelDto ){
						//MasSecVSPUserLabelMapping userLabel = getUserLabelMappingDetails(labelList.getKey(), userMgmtDTO.getUserId());
						MasSecVSPUserLabelMapping userLabel = getUserLabelMappingDetails(userMgmtDTO.getUserId());
						//Label Enable 
						if(userLabel == null &&  labelList.getLabelEnable() != null && labelList.getLabelEnable()){
							MasSecVSPUserLabelMapping labelMappingValue;
							labelMappingValue = new MasSecVSPUserLabelMapping();
							labelMappingValue.setUserId(userMgmtDTO.getUserId());
//							labelMappingValue.setKey(labelList.getKey());
							//labelMappingValue.setValue(labelList.getValue());
							if(labelList.getLabelEnable()!=null && labelList.getLabelEnable().equals(Boolean.TRUE)){
								labelMappingValue.setVspEnable("Y");
							}
							if(labelList.getIncludeValue()!=null && labelList.getIncludeValue().equals(Boolean.TRUE)){
								labelMappingValue.setVspInclude("Y");
								labelMappingValue.setVspExclude("N");
							}
							if(labelList.getExcludeValue()!=null && labelList.getExcludeValue().equals(Boolean.TRUE)){
								labelMappingValue.setVspExclude("Y");
								labelMappingValue.setVspInclude("N");
							}
							
							
							if(labelList.getLabelEnable() != null && labelList.getLabelEnable()){
								labelMappingValue.setActiveStatus("Y");
							}else{
								labelMappingValue.setActiveStatus("N");
							}
							labelMappingValue.setCreatedBy(currentUserName.toUpperCase());
							labelMappingValue.setCreatedDate(new java.sql.Timestamp(System.currentTimeMillis()));
							entityManager.persist(labelMappingValue);
							entityManager.flush();
							entityManager.clear();
						}else if(userLabel != null &&  labelList.getLabelEnable() != null && labelList.getLabelEnable()){
							if(userLabel.getUserId().equalsIgnoreCase(userMgmtDTO.getUserId())){
								
								if(labelList.getLabelEnable()!=null && labelList.getLabelEnable().equals(Boolean.TRUE)){
									userLabel.setVspEnable("Y");
									
								}else{
									userLabel.setVspEnable("N");
								}
								if(labelList.getIncludeValue()!=null && labelList.getIncludeValue().equals(Boolean.TRUE)){
									userLabel.setVspInclude("Y");
									userLabel.setVspExclude("N");
								}
								if(labelList.getExcludeValue()!=null && labelList.getExcludeValue().equals(Boolean.TRUE)){
									userLabel.setVspExclude("Y");
									userLabel.setVspInclude("N");
									
								}
								//userLabel.setDiagnosisValue(labelList.getValue());
								if(labelList.getLabelEnable() != null && labelList.getLabelEnable()){
									userLabel.setActiveStatus("Y");
								}else{
									userLabel.setActiveStatus("N");
								}
								userLabel.setModifiedBy(currentUserName.toUpperCase());
								userLabel.setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
								entityManager.merge(userLabel);
								entityManager.flush();
								entityManager.clear();
							}
						}else if(userLabel != null &&  labelList.getLabelEnable() != null && !labelList.getLabelEnable()){
							if(userLabel.getUserId().equalsIgnoreCase(userMgmtDTO.getUserId())){
								//userLabel.setDiagnosisValue(labelList.getValue());
								
								if(labelList.getLabelEnable()!=null && labelList.getLabelEnable().equals(Boolean.TRUE)){
									userLabel.setVspEnable("Y");
								}else{
									userLabel.setVspEnable("N");
								}
								if(labelList.getIncludeValue()!=null && labelList.getIncludeValue().equals(Boolean.TRUE)){
									userLabel.setVspInclude("Y");
									userLabel.setVspExclude("N");
								}
								if(labelList.getExcludeValue()!=null && labelList.getExcludeValue().equals(Boolean.TRUE)){
									userLabel.setVspExclude("Y");
									userLabel.setVspInclude("N");
								}
								
								if(labelList.getLabelEnable() != null && labelList.getLabelEnable()){
									userLabel.setActiveStatus("Y");
								}else{
									userLabel.setActiveStatus("N");
								}
								userLabel.setModifiedBy(currentUserName.toUpperCase());
								userLabel.setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
								entityManager.merge(userLabel);
								entityManager.flush();
								entityManager.clear();
							}
						}
					}
					
				}
					
					
		// ProductMappingList
		if (productMappingDto != null) {
			
			List<String> list = getUserProductMappingKey(userMgmtDTO.getUserId());
			
			for (UserMgmtProductMappingDTO userProductMappingDTO : productMappingDto) {
				if (userProductMappingDTO.getProductMappingEnable() != null) {

					String productMappingVal1[] = userProductMappingDTO
							.getProductCodeWithName().split(" - ");
					if (null != productMappingVal1) {
						String strProductCode1 = productMappingVal1[0];

						if (userProductMappingDTO.getProductMappingEnable() != null
								&& userProductMappingDTO
										.getProductMappingEnable() && !list.contains(userProductMappingDTO.getKey())) {
							UserProductMapping userProductMapping;
							userProductMapping = new UserProductMapping();
							userProductMapping.setProductCode(strProductCode1);
							userProductMapping
									.setEligibilityFlag(SHAConstants.YES_FLAG);
							userProductMapping.setCreatedBy(userName);
							userProductMapping
									.setCreatedDate(new java.sql.Timestamp(
											System.currentTimeMillis()));
							userProductMapping
									.setProductKey(userProductMappingDTO
											.getKey());
							userProductMapping
									.setActiveStatus(SHAConstants.STATUS_ACTIVE);
							userProductMapping
									.setUserNameId(userMgmtDTO.getUserId());

							if (userProductMappingDTO.getProductMappingEnable() != null
									&& userProductMappingDTO
											.getProductMappingEnable()) {
								entityManager.persist(userProductMapping);
								entityManager.flush();
								entityManager.clear();
							}

						} else {
							if (userProductMappingDTO.getProductMappingEnable() != null) {
								List<UserProductMapping> userMapping = getUserProductMappingDetails(
										userProductMappingDTO.getKey(), userMgmtDTO.getUserId());
								for (UserProductMapping userProdMapping : userMapping) {
									
								if(userProductMappingDTO.getProductMappingEnable())
								{
									userProdMapping
									.setEligibilityFlag(SHAConstants.YES_FLAG);
									userProdMapping
									.setActiveStatus(SHAConstants.STATUS_ACTIVE);
								}
								else{
									userProdMapping
											.setEligibilityFlag(SHAConstants.N_FLAG);
									userProdMapping
											.setActiveStatus(SHAConstants.STATUS_DEACTIVE);
								}
								userProdMapping.setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
								userProdMapping.setModifiedBy(userName);
									entityManager.merge(userProdMapping);
									entityManager.flush();
								}
							}
						}
					}
				}
			}
		}
	}

	private void activeOrDeactiveRtlGmc(
			UserMgmtApplicableCpuDTO applicableCpuDTO,
			List<UserToOrgMapping> userMappingGmcRtl, Integer count, String userId) {
		if (count == 2) {
			// if GMC only selected
			if (applicableCpuDTO.getGmcCheckBox() != null
					&& applicableCpuDTO.getGmcCheckBox()
					&& applicableCpuDTO.getRetailCheckBox() != null
					&& !applicableCpuDTO.getRetailCheckBox()) {
				for (UserToOrgMapping userToOrgMapping : userMappingGmcRtl) {
					if (userToOrgMapping.getLobFlag() != null
							&& userToOrgMapping.getLobFlag().equals(
									SHAConstants.RETAIL_USER_MANAGEMENT)) {
						userToOrgMapping.setActiveStatus(SHAConstants.N_FLAG);
						userToOrgMapping.setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
						userToOrgMapping.setModifiedBy(userId);
						entityManager.merge(userToOrgMapping);
						entityManager.flush();
					}
					if (userToOrgMapping.getLobFlag() == null
							|| (userToOrgMapping.getLobFlag() != null && userToOrgMapping
									.getLobFlag().equals(
											SHAConstants.GMC_POL_SERIVICE))) {
						userToOrgMapping.setActiveStatus(SHAConstants.YES_FLAG);
						userToOrgMapping
								.setLobFlag(SHAConstants.GMC_POL_SERIVICE);
						userToOrgMapping.setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
						userToOrgMapping.setModifiedBy(userId);
						entityManager.merge(userToOrgMapping);
						entityManager.flush();
					}
				}
			}

			// if Retail Only selected
			if (applicableCpuDTO.getGmcCheckBox() != null
					&& !applicableCpuDTO.getGmcCheckBox()
					&& applicableCpuDTO.getRetailCheckBox() != null
					&& applicableCpuDTO.getRetailCheckBox()) {

				for (UserToOrgMapping userToOrgMapping : userMappingGmcRtl) {

					if (userToOrgMapping.getLobFlag() != null
							&& userToOrgMapping.getLobFlag().equals(
									SHAConstants.GMC_POL_SERIVICE)
							|| (userToOrgMapping.getLobFlag() == null)) {
						userToOrgMapping.setActiveStatus(SHAConstants.N_FLAG);
						userToOrgMapping
								.setLobFlag(SHAConstants.GMC_POL_SERIVICE);
						userToOrgMapping.setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
						userToOrgMapping.setModifiedBy(userId);
						entityManager.merge(userToOrgMapping);
						entityManager.flush();
					}

					if ((userToOrgMapping.getLobFlag() != null && userToOrgMapping
							.getLobFlag().equals(
									SHAConstants.RETAIL_USER_MANAGEMENT))) {
						userToOrgMapping.setActiveStatus(SHAConstants.YES_FLAG);
						userToOrgMapping
								.setLobFlag(SHAConstants.RETAIL_USER_MANAGEMENT);
						userToOrgMapping.setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
						userToOrgMapping.setModifiedBy(userId);
						entityManager.merge(userToOrgMapping);
						entityManager.flush();
					}

				}
			}
			// Botch GMC & Retail selected
			if (applicableCpuDTO.getGmcCheckBox() != null
					&& applicableCpuDTO.getGmcCheckBox()
					&& applicableCpuDTO.getRetailCheckBox() != null
					&& applicableCpuDTO.getRetailCheckBox()) {
				for (UserToOrgMapping userToOrgMapping : userMappingGmcRtl) {
					if (userToOrgMapping.getLobFlag() != null
							&& userToOrgMapping.getLobFlag().equals(
									SHAConstants.RETAIL_USER_MANAGEMENT)) {
						userToOrgMapping.setActiveStatus(SHAConstants.YES_FLAG);
						userToOrgMapping.setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
						userToOrgMapping.setModifiedBy(userId);
						entityManager.merge(userToOrgMapping);
						entityManager.flush();
					}
					if (userToOrgMapping.getLobFlag() != null
							&& userToOrgMapping.getLobFlag().equals(
									SHAConstants.GMC_POL_SERIVICE)) {
						userToOrgMapping.setActiveStatus(SHAConstants.YES_FLAG);
						userToOrgMapping.setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
						userToOrgMapping.setModifiedBy(userId);
						entityManager.merge(userToOrgMapping);
						entityManager.flush();
					}
				
				}

				
			}

			// Both GMC & Retail not Selected
			if (applicableCpuDTO.getGmcCheckBox() != null
					&& !applicableCpuDTO.getGmcCheckBox()
					&& applicableCpuDTO.getRetailCheckBox() != null
					&& !applicableCpuDTO.getRetailCheckBox()) {

				for (UserToOrgMapping userToOrgMapping : userMappingGmcRtl) {
					if (userToOrgMapping.getLobFlag() != null
							&& userToOrgMapping.getLobFlag().equals(
									SHAConstants.RETAIL_USER_MANAGEMENT)) {
						userToOrgMapping.setActiveStatus(SHAConstants.N_FLAG);
						userToOrgMapping.setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
						userToOrgMapping.setModifiedBy(userId);
						entityManager.merge(userToOrgMapping);
						entityManager.flush();
					}
					if (userToOrgMapping.getLobFlag() != null
							&& userToOrgMapping.getLobFlag().equals(
									SHAConstants.GMC_POL_SERIVICE)) {
						userToOrgMapping.setActiveStatus(SHAConstants.YES_FLAG);
						userToOrgMapping.setLobFlag(null);
						userToOrgMapping.setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
						userToOrgMapping.setModifiedBy(userId);
						entityManager.merge(userToOrgMapping);
						entityManager.flush();
					}
					
				}
			}
			
		} else {
			// Only Gmc selected
			if (applicableCpuDTO.getGmcCheckBox() != null
					&& applicableCpuDTO.getGmcCheckBox()
					&& applicableCpuDTO.getRetailCheckBox() != null
					&& !applicableCpuDTO.getRetailCheckBox()) {
				userMappingGmcRtl.get(0).setActiveStatus(SHAConstants.YES_FLAG);
				userMappingGmcRtl.get(0).setLobFlag(
						SHAConstants.GMC_POL_SERIVICE);
				userMappingGmcRtl.get(0).setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
				userMappingGmcRtl.get(0).setModifiedBy(userId);
				entityManager.merge(userMappingGmcRtl.get(0));
				entityManager.flush();
			}
			// Only Retail selected
			if (applicableCpuDTO.getGmcCheckBox() != null
					&& !applicableCpuDTO.getGmcCheckBox()
					&& applicableCpuDTO.getRetailCheckBox() != null
					&& applicableCpuDTO.getRetailCheckBox()) {
				userMappingGmcRtl.get(0).setActiveStatus(SHAConstants.YES_FLAG);
				userMappingGmcRtl.get(0).setLobFlag(
						SHAConstants.RETAIL_USER_MANAGEMENT);
				userMappingGmcRtl.get(0).setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
				userMappingGmcRtl.get(0).setModifiedBy(userId);
				entityManager.merge(userMappingGmcRtl.get(0));
				entityManager.flush();
			}
			
			if(applicableCpuDTO.getGmcCheckBox() != null
					&& !applicableCpuDTO.getGmcCheckBox()
					&& applicableCpuDTO.getRetailCheckBox() != null
					&& !applicableCpuDTO.getRetailCheckBox()) {
				userMappingGmcRtl.get(0).setActiveStatus(SHAConstants.YES_FLAG);
				userMappingGmcRtl.get(0).setLobFlag(null);
				userMappingGmcRtl.get(0).setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
				userMappingGmcRtl.get(0).setModifiedBy(userId);
				entityManager.merge(userMappingGmcRtl.get(0));
				entityManager.flush();
			}
			
			if (applicableCpuDTO.getGmcCheckBox() != null
					&& applicableCpuDTO.getGmcCheckBox()
					&& applicableCpuDTO.getRetailCheckBox() != null
					&& applicableCpuDTO.getRetailCheckBox()) {
				if ((userMappingGmcRtl.get(0).getLobFlag() == null)
						|| (userMappingGmcRtl.get(0).getLobFlag() != null && userMappingGmcRtl
								.get(0).getLobFlag()
								.equals(SHAConstants.GMC_POL_SERIVICE))) {
					userMappingGmcRtl.get(0).setActiveStatus(
							SHAConstants.YES_FLAG);
					userMappingGmcRtl.get(0).setLobFlag(
							SHAConstants.GMC_POL_SERIVICE);
					userMappingGmcRtl.get(0).setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
					userMappingGmcRtl.get(0).setModifiedBy(userId);
					entityManager.merge(userMappingGmcRtl.get(0));
					entityManager.flush();
					String lobFlag = SHAConstants.RETAIL_USER_MANAGEMENT;
					newRecordInserted(userMappingGmcRtl, lobFlag);
				}

				if (userMappingGmcRtl.get(0).getLobFlag() != null
						&& userMappingGmcRtl.get(0).getLobFlag()
								.equals(SHAConstants.RETAIL_USER_MANAGEMENT)) {
					userMappingGmcRtl.get(0).setActiveStatus(
							SHAConstants.YES_FLAG);
					userMappingGmcRtl.get(0).setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
					userMappingGmcRtl.get(0).setModifiedBy(userId);
					entityManager.merge(userMappingGmcRtl.get(0));
					entityManager.flush();
					String lobFlag = SHAConstants.GMC_POL_SERIVICE;
					newRecordInserted(userMappingGmcRtl, lobFlag);

				}
			}

		}
	}

	private void newRecordInserted(List<UserToOrgMapping> userMappingGmcRtl,
			String lobFlag) {
		// New record inserted for retails
		UserToOrgMapping newUserMapping = new UserToOrgMapping();
		newUserMapping.setOrgId(userMappingGmcRtl.get(0).getOrgId());
		newUserMapping.setUserId(userMappingGmcRtl.get(0).getUserId());
		newUserMapping.setCreatedBy(userMappingGmcRtl.get(0).getCreatedBy());
		newUserMapping.setCreatedDate(new Date());
		newUserMapping.setActiveStatus(SHAConstants.YES_FLAG);
		newUserMapping.setLobFlag(lobFlag);
		entityManager.persist(newUserMapping);
		entityManager.flush();
	}

	private MasUser getEmployeeKeyForUserMgmt(String initiatorId) {
		MasUser userDetail;
		Query findByTransactionKey = entityManager.createNamedQuery(
				"MasUser.getEmpByName").setParameter("userName", initiatorId);
		try {
			userDetail = (MasUser) findByTransactionKey.getSingleResult();
			return userDetail;
		} catch (Exception e) {
			return null;
		}

	}

	private MasUser getEmpByIdForUserMgmt(String initiatorId) {
		MasUser userDetail;
		Query findByTransactionKey = entityManager.createNamedQuery(
				"MasUser.getEmpById").setParameter("userId", initiatorId);
		try {
			userDetail = (MasUser) findByTransactionKey.getSingleResult();
			return userDetail;
		} catch (Exception e) {
			return null;
		}

	}

	public MasUser getShiftDetailsByIdForUserMgmt(String userId) {
		MasUser userDetail;
		Query query = entityManager.createNamedQuery("MasUser.getByType")
				.setParameter("userId", userId);
		;
		try {
			userDetail = (MasUser) query.getSingleResult();
			return userDetail;
		} catch (Exception e) {
			return null;
		}
	}

	public TmpCPUCode getCpuByCodeForUserMgmt(Long cpuCode) {
		Query list = entityManager.createNamedQuery("TmpCPUCode.findByCode")
				.setParameter("cpuCode", cpuCode);
		List<TmpCPUCode> cpuCodeList = list.getResultList();

		if (cpuCodeList != null && !cpuCodeList.isEmpty()) {
			return cpuCodeList.get(0);
		}
		return null;
	}

	public UserToOrgMapping getCpuListByOrgIdForUserMgmt(String orgId,
			String userId) {
		Query list = entityManager
				.createNamedQuery("UserToOrgMapping.findByOrgIdUserId")
				.setParameter("orgId", orgId).setParameter("userId", userId);
		List<UserToOrgMapping> cpuList = list.getResultList();
		if (cpuList != null && !cpuList.isEmpty()) {
			return cpuList.get(0);
		}
		return null;

	}

	public TmpEmployee getEmployeeDetails(String empId) {
		TmpEmployee userDetail;
		Query findByTransactionKey = entityManager.createNamedQuery(
				"TmpEmployee.findByEmpId").setParameter("empId", empId.toLowerCase());
		try {
			userDetail = (TmpEmployee) findByTransactionKey.getSingleResult();
			return userDetail;
		} catch (Exception e) {
			return null;
		}

	}

	public void deactivateUserMgmt(UserManagementDTO bean,String userName) {
		Boolean empName = getEmployeeByName(bean.getUserId());
		if (empName) {
			if (null != bean.getUserId()) {
				TmpEmployee userDetails = getEmployessValues(bean.getUserId());

				if (userDetails != null
						&& userDetails.getActiveStatus() != null) {
					userDetails.setActiveStatus(0l);
					userDetails.setModifiedDate(new java.sql.Timestamp(System
							.currentTimeMillis()));
					entityManager.merge(userDetails);

				}
				MasUser masUserDetails = getEmployessValuesFromMasUser(bean
						.getUserId());
				if (masUserDetails != null
						&& masUserDetails.getActiveStatus() != null) {
					masUserDetails.setActiveStatus(SHAConstants.N_FLAG);
					masUserDetails.setModifiedDate(new java.sql.Timestamp(System
					.currentTimeMillis()));
					masUserDetails.setModifiedBy(userName);
					entityManager.merge(masUserDetails);
				}
			}

		}

	}

	public void activateUserMgmt(UserManagementDTO bean,String userName) {
		Boolean empName = getEmployeeByActiveStatus(bean.getUserId());
		if (empName) {
			if (null != bean.getUserId()) {
				TmpEmployee userDetails = getEmployessValuesByInactiveStatus(bean
						.getUserId());

				if (userDetails != null
						&& userDetails.getActiveStatus() != null) {
					userDetails.setActiveStatus(1l);
					userDetails.setModifiedDate(new java.sql.Timestamp(System
							.currentTimeMillis()));
					entityManager.merge(userDetails);

				}
				MasUser masUserDetails = getShiftDetailsByIdForUserMgmt(bean
						.getUserId());
				if (masUserDetails != null
						&& masUserDetails.getActiveStatus() != null) {
					masUserDetails.setActiveStatus(SHAConstants.YES_FLAG);
					masUserDetails.setModifiedDate(new java.sql.Timestamp(System
							.currentTimeMillis()));
					masUserDetails.setModifiedBy(userName);
					entityManager.merge(masUserDetails);
				}
			}

		}

	}

	public TmpEmployee getEmployessValues(String empId) {
		Query findByTransactionKey = entityManager.createNamedQuery(
				"TmpEmployee.findByEmpId").setParameter("empId",
				empId.toLowerCase());
		try {
			List<TmpEmployee> userDetail = findByTransactionKey.getResultList();
			if (null != userDetail && !userDetail.isEmpty()) {
				return userDetail.get(0);
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	public TmpEmployee getEmployessValuesByInactiveStatus(String empId) {
		Query findByTransactionKey = entityManager.createNamedQuery(
				"TmpEmployee.findByEmpIdWithInactive").setParameter("empId",
				empId.toLowerCase());
		try {
			List<TmpEmployee> userDetail = findByTransactionKey.getResultList();
			if (null != userDetail && !userDetail.isEmpty()) {
				return userDetail.get(0);
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	private MasUser getEmployessValuesFromMasUser(String empId) {
		Query findByTransactionKey = entityManager.createNamedQuery(
				"MasUser.getEmpDetailsById").setParameter("empId",
				empId.toLowerCase());
		try {
			List<MasUser> userDetail = findByTransactionKey.getResultList();
			if (null != userDetail && !userDetail.isEmpty()) {
				return userDetail.get(0);
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	public void addUserLimitMapping(List<UserManagementDTO> userMgmtList,
			String userName) {

		for (UserManagementDTO userManagementDTO : userMgmtList) {
			MasUserLimitMapping empDetails = getActiveUserLimitEmpByNameAndActiveStatus(
					userName, userManagementDTO.getLimit().getValue());
			if (empDetails == null) {
				empDetails = new MasUserLimitMapping();
				empDetails.setActiveStatus(SHAConstants.YES_FLAG);

				empDetails.setRoleId(userManagementDTO.getLimit().getValue());
				empDetails.setUserId(userName);
				if (userManagementDTO.getLimit().getValue().toString()
						.contains(SHAConstants.AMA)) {
					empDetails.setClaimType(SHAConstants.BOTH);
				} else if (userManagementDTO.getLimit().getValue().toString()
						.contains(SHAConstants.CMA)) {
					empDetails.setClaimType(SHAConstants.CASHLESS);
				} else if (userManagementDTO.getLimit().getValue().toString()
						.contains(SHAConstants.RMA)) {
					empDetails
							.setClaimType(SHAConstants.REIMBURSEMENT_USER_MANAGEMENT);
				} else if(userManagementDTO.getLimit().getValue().toString()
						.contains(SHAConstants.FA)) {
					empDetails
					.setClaimType(SHAConstants.FA);
				} else if(userManagementDTO.getLimit().getValue().toString()
						.contains(SHAConstants.BILLING_USER_LIMTS)) {
					empDetails
					.setClaimType(SHAConstants.REIMBURSEMENT_USER_MANAGEMENT);
				}
				entityManager.persist(empDetails);

			} else {
				empDetails.setActiveStatus(SHAConstants.YES_FLAG);
				empDetails.setRoleId(userManagementDTO.getLimit().getValue());
				empDetails.setUserId(userName);
				if (userManagementDTO.getLimit().getValue().toString()
						.contains(SHAConstants.AMA)) {
					empDetails.setClaimType(SHAConstants.BOTH);
				} else if (userManagementDTO.getLimit().getValue().toString()
						.contains(SHAConstants.CMA)) {
					empDetails.setClaimType(SHAConstants.CASHLESS);
				} else if (userManagementDTO.getLimit().getValue().toString()
						.contains(SHAConstants.RMA)) {
					empDetails
							.setClaimType(SHAConstants.REIMBURSEMENT_USER_MANAGEMENT);
				} else if(userManagementDTO.getLimit().getValue().toString()
						.contains(SHAConstants.FA)) {
					empDetails
					.setClaimType(SHAConstants.FA);
				} else if(userManagementDTO.getLimit().getValue().toString()
						.contains(SHAConstants.BILLING_USER_LIMTS)) {
					empDetails
					.setClaimType(SHAConstants.REIMBURSEMENT_USER_MANAGEMENT);
				}
				empDetails.setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
				empDetails.setModifiedBy(userName);
				entityManager.merge(empDetails);
			}
		}

	}
	public void deleteUserLimit(List<UserManagementDTO> userMgmtList,
			String userName) {
		for (UserManagementDTO userManagementDTO : userMgmtList) {
		MasUserLimitMapping empDetails = getActiveUserLimitEmpByName(
				userName, userManagementDTO.getLimit().getValue());
		if(empDetails!=null) {
			empDetails.setActiveStatus(SHAConstants.N_FLAG);
			empDetails.setModifiedDate(new java.sql.Timestamp(System.currentTimeMillis()));
			empDetails.setModifiedBy(userName);
			entityManager.merge(empDetails);
		}
		}
	}

	public MasUserLimitMapping getUserLimitEmployeeByName(String userId,
			String roleId) {

		String tmpEmployee = null;
		Query query = entityManager
				.createNamedQuery("MasUserLimitMapping.findByEmpIdAndRoleId");
		query.setParameter("userId", userId);
		query.setParameter("roleId", roleId);

		List<MasUserLimitMapping> tmpEmployeeList = query.getResultList();

		if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
			return tmpEmployeeList.get(0);

		}

		return null;

	}

	public List<UserToOrgMapping> getUserOrgMappingDetails(String orgId,
			String userId) {
		Query query = entityManager
				.createNamedQuery("UserToOrgMapping.findByOrgIdCpu");
		query.setParameter("orgId", "%" + orgId + "%");
		query.setParameter("userId", userId);
		List<UserToOrgMapping> tmpEmployeeList = query.getResultList();
		if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
			return tmpEmployeeList;
		}
		return tmpEmployeeList;

	}

	public MasRoleLimit getUserOrgMappingDetailsByRolrId(String roleId) {
		Query query = entityManager
				.createNamedQuery("MasRoleLimit.findByroleId");
		query.setParameter("roleId", roleId);
		List<MasRoleLimit> tmpEmployeeList = query.getResultList();
		if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
			return tmpEmployeeList.get(0);
		}
		return null;

	}

	public List<MasUserLimitMapping> getUserLimitEmployeeListByName(
			String userId) {

		String tmpEmployee = null;
		Query query = entityManager
				.createNamedQuery("MasUserLimitMapping.findByEmpId");
		query.setParameter("userId", userId);

		List<MasUserLimitMapping> tmpEmployeeList = query.getResultList();

		if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
			return tmpEmployeeList;

		}

		return null;

	}

	public MasUserLimitMapping deleteUserLimitByRoleId(String userId,
			String roleId) {
		Query query = entityManager.createNamedQuery("MasUserLimitMapping.");
		query.setParameter("userId", userId);
		query.setParameter("roleId", roleId);
		List<MasUserLimitMapping> tmpEmployeeList = query.getResultList();
		if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
			return tmpEmployeeList.get(0);

		}

		return null;
	}

	public MasUserLimitMapping getActiveUserLimitEmpByName(String userId,
			String roleId) {

		String tmpEmployee = null;
		Query query = entityManager
				.createNamedQuery("MasUserLimitMapping.getActiveUserLimitByRoleId");
		query.setParameter("userName", userId);
		query.setParameter("roleId", roleId);

		List<MasUserLimitMapping> tmpEmployeeList = query.getResultList();

		if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
			return tmpEmployeeList.get(0);

		}

		return null;

	}
	public MasUserLimitMapping getActiveUserLimitEmpByNameAndActiveStatus(String userId,
			String roleId) {

		String tmpEmployee = null;
		Query query = entityManager
				.createNamedQuery("MasUserLimitMapping.getActiveUserLimitByRoleIdAndActiceStatus");
		query.setParameter("userName", userId);
		query.setParameter("roleId", roleId);

		List<MasUserLimitMapping> tmpEmployeeList = query.getResultList();

		if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
			return tmpEmployeeList.get(0);

		}

		return null;

	}

	public List<MasUserLimitMapping> getUserLimitEmpListByNameAndActiveStatus(
			String userId) {

		String tmpEmployee = null;
		Query query = entityManager
				.createNamedQuery("MasUserLimitMapping.findByEmpIdAndActiveStatus");
		query.setParameter("userId", userId);

		List<MasUserLimitMapping> tmpEmployeeList = query.getResultList();

		if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
			return tmpEmployeeList;

		}

		return null;

	}

	public UserToOrgMapping getUserOrgMappingDetailsRetail(String orgId,
			String userId, String lobFlag) {
		Query query = entityManager
				.createNamedQuery("UserToOrgMapping.findByOrgIdCpuForRtl");
		query.setParameter("orgId", "%" + orgId + "%");
		query.setParameter("userId", userId);
		query.setParameter("lobFlag", lobFlag);
		List<UserToOrgMapping> tmpEmployeeList = query.getResultList();
		if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
			return tmpEmployeeList.get(0);
		}
		return null;

	}
	
	// AutoAllocationCategory
	public List<MasUserDiagnosis> getDiagnosisToMappingValues(){
		Query query = entityManager
						.createNamedQuery("MasUserDiagnosis.findAll");
		
		List<MasUserDiagnosis> dagnosisTableList = query.getResultList();
		
		if (dagnosisTableList != null && !dagnosisTableList.isEmpty()) {
			return dagnosisTableList;

		}

		return null;
	}
	
	public MasSecUserDiagnosisMapping getUserDiagnosisMappingDetails(Long key, String userId){
		Query query = entityManager
				.createNamedQuery("MasSecUserDiagnosisMapping.findByUserIdAndKey");
		query.setParameter("key", key);
		query.setParameter("userId", userId);
	
		List<MasSecUserDiagnosisMapping> diagnosisList = query.getResultList();
		if (diagnosisList != null && !diagnosisList.isEmpty()) {
			return diagnosisList.get(0);
		}
		return null;
	}
	
	public List<MasSecUserDiagnosisMapping> getUpdateDiagnosisList(String userId){
			Query query = entityManager
							.createNamedQuery("MasSecUserDiagnosisMapping.findByUserId");
			query.setParameter("userId", userId);
			
			List<MasSecUserDiagnosisMapping> dagnosisUpdateTableList = query.getResultList();
			
			if (dagnosisUpdateTableList != null && !dagnosisUpdateTableList.isEmpty()) {
				return dagnosisUpdateTableList;

			}

			return null;
		}
	
	public  List<MastersValue> getVSPMasterByMasterTypeCode() {
		Query query= entityManager.createNamedQuery("MastersValue.findByMasterTypeCode");
		
		query.setParameter("masterTypeCode", SHAConstants.VSP_MASTERTYPE_VALUE);
		
		List<MastersValue> masterList =  query.getResultList();
		
		if(null != masterList && !masterList.isEmpty()){

			return masterList;
			
		}else{
			
			return null;
		}

	}
	
	public MasSecVSPUserLabelMapping getUserLabelMappingDetails(String userId){
		Query query = entityManager
				.createNamedQuery("MasSecVSPUserLabelMapping.findByUserIdAndKey");
		//query.setParameter("key", key);
		query.setParameter("userId", userId);
	
		List<MasSecVSPUserLabelMapping> labelList = query.getResultList();
		if (labelList != null && !labelList.isEmpty()) {
			return labelList.get(0);
		}
		return null;
	}
	
	public List<MasSecVSPUserLabelMapping> getUpdateLabelList(String userId){
		Query query = entityManager
						.createNamedQuery("MasSecVSPUserLabelMapping.findByUserId");
		query.setParameter("userId", userId);
		
		List<MasSecVSPUserLabelMapping>labelUpdateTableList = query.getResultList();
		
		if (labelUpdateTableList != null && !labelUpdateTableList.isEmpty()) {
			return labelUpdateTableList;

		}

		return null;
	}
	
	
	
	public MastersValue getMaster(Long key) {
		MastersValue a_mastersValue = new MastersValue();
		if (key != null) {
			Query query = entityManager
					.createNamedQuery("MastersValue.findByKey");
			query = query.setParameter("parentKey", key);
			List<MastersValue> mastersValueList = query.getResultList();
			for (MastersValue mastersValue : mastersValueList){
				a_mastersValue = mastersValue;
				break;
			}
		}

		return a_mastersValue;
	}
	 public  MastersValue getMasterKeyBasedOnMappingCode(String mappingCode) {
	    	MastersValue masterKey = null;
				Query findByMappingCode = entityManager.createNamedQuery(
						"MastersValue.findByMappingCode").setParameter("mappingCode", mappingCode);
				try{
					List<MastersValue> masterList = (List<MastersValue>) findByMappingCode.getResultList();
					if(null != masterList && !masterList.isEmpty())
					{
						masterKey = masterList.get(0);
					}
					return masterKey;
					
				}catch(Exception e)
				{
					e.printStackTrace();
					return null;
				}

	}
	 
	 public  MastersValue getClaimApplicableValueBasedOnValue(String claimApplicableValue) {
			MastersValue masterKey = null;
			Query findByMasterTypeCodeClaimApplicable = entityManager.createNamedQuery(
					"MastersValue.findByMasterTypeCodeClaimApplicable");
			findByMasterTypeCodeClaimApplicable.setParameter("code", SHAConstants.CLAIM_APPLICABLE_VALUE);
			findByMasterTypeCodeClaimApplicable.setParameter("value", claimApplicableValue.toLowerCase());
			try{
				List<MastersValue> masterList = (List<MastersValue>) findByMasterTypeCodeClaimApplicable.getResultList();
				if(null != masterList && !masterList.isEmpty())
				{
					masterKey = masterList.get(0);
				}
				return masterKey;
				
			}catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}

		}
	 
		public List<Product> getProductMappingList(){
			Query query = entityManager
					.createNamedQuery("Product.findAllProduct");
		
			List<Product> productList = query.getResultList();
			if (productList != null && !productList.isEmpty()) {
				return productList;
			}
			return null;
		}
		
		public List<UserProductMapping> getUserProductMappingDetails(Long productId,
				String userId) {
			Query query = entityManager
					.createNamedQuery("UserProductMapping.findByUserIdAndProdId");
			query.setParameter("productId",productId);
			query.setParameter("userId", userId);
			List<UserProductMapping> tmpUserProductList = query.getResultList();
			if (tmpUserProductList != null && !tmpUserProductList.isEmpty()) {
				return tmpUserProductList;
			}
			return tmpUserProductList;

		}
		
		public List<String> getUserProductMappingKey(String userId) {
			Query query = entityManager
					.createNamedQuery("UserProductMapping.findProductKeyByUserId");
			query.setParameter("userId", (userId).toUpperCase());
			List<String> UserProductKeyList = query.getResultList();
			if (UserProductKeyList != null && !UserProductKeyList.isEmpty()) {
				return UserProductKeyList;
			}
			return UserProductKeyList;

		}

		
		public List<MasAreaOrgUserCpuMapping> getUserAreaOrgMappingDetails(String orgId,
				String userId) {
			Query query = entityManager
					.createNamedQuery("MasAreaOrgUserCpuMapping.findByOrgIdCpu");
			query.setParameter("orgId",  orgId );
			query.setParameter("userId", userId);
			List<MasAreaOrgUserCpuMapping> tmpEmployeeList = query.getResultList();
			if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
				return tmpEmployeeList;
			}
			return tmpEmployeeList;

		}
		
		public MasAreaCodeMapping getAreaMappingCodeAndKey(String cpuCode) {
			Query list = entityManager.createNamedQuery("MasAreaCodeMapping.findByAreaOrgName")
					.setParameter("orgCpuNames", cpuCode);
			List<MasAreaCodeMapping> cpuCodeList = list.getResultList();

			if (cpuCodeList != null && !cpuCodeList.isEmpty()) {
				return cpuCodeList.get(0);
			}
			return null;
		}
		
		
		public MasAreaOrgUserCpuMapping getUserMappingDetailsByAreaCode(String areaCode,String userId) {
			Query list = entityManager.createNamedQuery("MasAreaOrgUserCpuMapping.findOrgCPUbyAreaCode");
			list.setParameter("areaCodeValue",  areaCode );
			list.setParameter("orgUserId", userId);
			List<MasAreaOrgUserCpuMapping> cpuCodeList = list.getResultList();

			if (cpuCodeList != null && !cpuCodeList.isEmpty()) {
				return cpuCodeList.get(0);
			}
			return null;
		}
		
}
