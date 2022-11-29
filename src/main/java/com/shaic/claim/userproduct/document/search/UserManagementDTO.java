package com.shaic.claim.userproduct.document.search;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.userproduct.document.ApplicableCpuDTO;

public class UserManagementDTO {

	private List<UserManagementDTO> limitList;
	
	private String userName;
	
	private String userId;
	
	@NotNull(message = "Please select User Type.")
	private SelectValue type;
	
	private Boolean highValueClaim;
	
	private Boolean deActivateUser;
	
	private String shiftStartTime;
	
	private String shiftEndTime;
	
	private SelectValue roleCtegory;
	
	private SelectValue limit;
	
	private Long amount;
	
	private String addedLimit;
	
	private List<UserMgmtApplicableCpuDTO> applicableCpuList;
	private List<UserManagementDTO> availableRoleLimitList;
	
	private List<UserManagementDTO> deletedList;
	
	private List<UserMgmtDaignosisDTO> diagnosisList;
	
	private List<UserManagementLabelDTO> labelDTOList;
	
	private SelectValue autoAlloccationType;
	
	private SelectValue claimFlagInUserMaster;
	
	private List<UserMgmtProductMappingDTO> productMappingList;
	
	//CR2019213
	private Boolean manaulFlag;
	
	private Boolean manualCodingFlag;
	
	private Boolean userBillingSelected = false;

	private Boolean restrictToBand;
	
	private Boolean manualPickMAFlag;
	
	private Boolean manualPickCombinedProcessFlag;
	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public SelectValue getType() {
		return type;
	}

	public void setType(SelectValue type) {
		this.type = type;
	}

	public Boolean getDeActivateUser() {
		return deActivateUser;
	}

	public void setDeActivateUser(Boolean deActivateUser) {
		this.deActivateUser = deActivateUser;
	}

	public String getShiftStartTime() {
		return shiftStartTime;
	}

	public void setShiftStartTime(String shiftStartTime) {
		this.shiftStartTime = shiftStartTime;
	}

	public String getShiftEndTime() {
		return shiftEndTime;
	}

	public void setShiftEndTime(String shiftEndTime) {
		this.shiftEndTime = shiftEndTime;
	}

	public List<UserMgmtApplicableCpuDTO> getApplicableCpuList() {
		return applicableCpuList;
	}

	public void setApplicableCpuList(
			List<UserMgmtApplicableCpuDTO> applicableCpuList) {
		this.applicableCpuList = applicableCpuList;
	}

	public Boolean getHighValueClaim() {
		return highValueClaim;
	}

	public void setHighValueClaim(Boolean highValueClaim) {
		this.highValueClaim = highValueClaim;
	}

	public SelectValue getRoleCtegory() {
		return roleCtegory;
	}

	public void setRoleCtegory(SelectValue roleCtegory) {
		this.roleCtegory = roleCtegory;
	}

	public SelectValue getLimit() {
		return limit;
	}

	public void setLimit(SelectValue limit) {
		this.limit = limit;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getAddedLimit() {
		return addedLimit;
	}

	public void setAddedLimit(String addedLimit) {
		this.addedLimit = addedLimit;
	}

	public List<UserManagementDTO> getLimitList() {
		return limitList;
	}

	public void setLimitList(List<UserManagementDTO> limitList) {
		this.limitList = limitList;
	}

	public List<UserManagementDTO> getAvailableRoleLimitList() {
		return availableRoleLimitList;
	}

	public void setAvailableRoleLimitList(
			List<UserManagementDTO> availableRoleLimitList) {
		this.availableRoleLimitList = availableRoleLimitList;
	}

	public List<UserManagementDTO> getDeletedList() {
		return deletedList;
	}

	public void setDeletedList(List<UserManagementDTO> deletedList) {
		this.deletedList = deletedList;
	}

	public Boolean getManaulFlag() {
		return manaulFlag;
	}

	public void setManaulFlag(Boolean manaulFlag) {
		this.manaulFlag = manaulFlag;
	}

	public List<UserMgmtDaignosisDTO> getDiagnosisList() {
		return diagnosisList;
	}

	public void setDiagnosisList(List<UserMgmtDaignosisDTO> diagnosisList) {
		this.diagnosisList = diagnosisList;
	}

	public SelectValue getAutoAlloccationType() {
		return autoAlloccationType;
	}

	public void setAutoAlloccationType(SelectValue autoAlloccationType) {
		this.autoAlloccationType = autoAlloccationType;
	}

	public SelectValue getClaimFlagInUserMaster() {
		return claimFlagInUserMaster;
	}

	public void setClaimFlagInUserMaster(SelectValue claimFlagInUserMaster) {
		this.claimFlagInUserMaster = claimFlagInUserMaster;
	}

	public List<UserMgmtProductMappingDTO> getProductMappingList() {
		return productMappingList;
	}

	public void setProductMappingList(
			List<UserMgmtProductMappingDTO> productMappingList) {
		this.productMappingList = productMappingList;
	}

	public Boolean getManualCodingFlag() {
		return manualCodingFlag;
	}

	public void setManualCodingFlag(Boolean manualCodingFlag) {
		this.manualCodingFlag = manualCodingFlag;
	}

	public Boolean getUserBillingSelected() {
		return userBillingSelected;
	}

	public void setUserBillingSelected(Boolean userBillingSelected) {
		this.userBillingSelected = userBillingSelected;
	}
	
	public Boolean getRestrictToBand() {
		return restrictToBand;
	}

	public void setRestrictToBand(Boolean restrictToBand) {
		this.restrictToBand = restrictToBand;
	}

	public Boolean getManualPickMAFlag() {
		return manualPickMAFlag;
	}

	public void setManualPickMAFlag(Boolean manualPickMAFlag) {
		this.manualPickMAFlag = manualPickMAFlag;
	}

	public Boolean getManualPickCombinedProcessFlag() {
		return manualPickCombinedProcessFlag;
	}

	public void setManualPickCombinedProcessFlag(
			Boolean manualPickCombinedProcessFlag) {
		this.manualPickCombinedProcessFlag = manualPickCombinedProcessFlag;
	}

	public List<UserManagementLabelDTO> getLabelDTOList() {
		return labelDTOList;
	}

	public void setLabelDTOList(List<UserManagementLabelDTO> labelDTOList) {
		this.labelDTOList = labelDTOList;
	}
	
	
	
	
}
