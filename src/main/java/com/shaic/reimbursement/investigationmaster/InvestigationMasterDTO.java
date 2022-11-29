package com.shaic.reimbursement.investigationmaster;

import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;

public class InvestigationMasterDTO extends AbstractTableDTO{
	
	private int sNo;
	
	private Long key;
	
	private Long privateInvestigatorKey;
	
	private String investigatorType;
	
	private String investigatorCode;
	
	private String investigatorName;
	
	private SelectValue gender;
	
	private String mobileNumber;
	
	private String additionalMobileNumber;
	
	private String phoneNumber;
	
	private Boolean status;
	
	private SelectValue state;
	
	private SelectValue city;
	
	private String branchCode;
	
	private String starEmployeeID;
	
	private String emailID;
	
	private int allocationCount;
	
	private String coordinatorCode;
	
	private String coordinatorName;
	
	private String consultancyName;
	
	private String contactPerson;
	
	private SelectValue investigationZoneName;
	
	//private String investigationZoneName;
	
	private String allocationToValue;
	
	private String toEmailID;
	
	private String ccEmailID;
	
	
	private List<SelectValue> invetigatorTypeSelectIdList;
	
	private List<SelectValue> investigatorTypeSelectValueList;
	
	private SelectValue investigatorTypeSelectValue;
	
	private List<SelectValue> stateList;
	
	private List<SelectValue> cityList;
	
	private List<SelectValue> investigatorNameContainerList;

	private List<SelectValue> genderList;
	
	public List<SelectValue> getInvestigatorNameContainerList() {
		return investigatorNameContainerList;
	}

	public void setInvestigatorNameContainerList(
			List<SelectValue> investigatorNameContainerList) {
		this.investigatorNameContainerList = investigatorNameContainerList;
	}

	public int getsNo() {
		return sNo;
	}

	public void setsNo(int sNo) {
		this.sNo = sNo;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getInvestigatorType() {
		return investigatorType;
	}

	public void setInvestigatorType(String investigatorType) {
		this.investigatorType = investigatorType;
	}

	public String getInvestigatorName() {
		return investigatorName;
	}

	public void setInvestigatorName(String investigatorName) {
		this.investigatorName = investigatorName;
	}

	public SelectValue getGender() {
		return gender;
	}

	public void setGender(SelectValue gender) {
		this.gender = gender;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAdditionalMobileNumber() {
		return additionalMobileNumber;
	}

	public void setAdditionalMobileNumber(String additionalMobileNumber) {
		this.additionalMobileNumber = additionalMobileNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public SelectValue getState() {
		return state;
	}

	public void setState(SelectValue state) {
		this.state = state;
	}

	public SelectValue getCity() {
		return city;
	}

	public void setCity(SelectValue city) {
		this.city = city;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getStarEmployeeID() {
		return starEmployeeID;
	}

	public void setStarEmployeeID(String starEmployeeID) {
		this.starEmployeeID = starEmployeeID;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public int getAllocationCount() {
		return allocationCount;
	}

	public void setAllocationCount(int allocationCount) {
		this.allocationCount = allocationCount;
	}

	public String getCoordinatorCode() {
		return coordinatorCode;
	}

	public void setCoordinatorCode(String coordinatorCode) {
		this.coordinatorCode = coordinatorCode;
	}

	public String getCoordinatorName() {
		return coordinatorName;
	}

	public void setCoordinatorName(String coordinatorName) {
		this.coordinatorName = coordinatorName;
	}

	public String getConsultancyName() {
		return consultancyName;
	}

	public void setConsultancyName(String consultancyName) {
		this.consultancyName = consultancyName;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public SelectValue getInvestigationZoneName() {
		return investigationZoneName;
	}

	public void setInvestigationZoneName(SelectValue investigationZoneName) {
		this.investigationZoneName = investigationZoneName;
	}

	public String getAllocationToValue() {
		return allocationToValue;
	}

	public void setAllocationToValue(String allocationToValue) {
		this.allocationToValue = allocationToValue;
	}

	public String getToEmailID() {
		return toEmailID;
	}

	public void setToEmailID(String toEmailID) {
		this.toEmailID = toEmailID;
	}

	public String getCcEmailID() {
		return ccEmailID;
	}

	public void setCcEmailID(String ccEmailID) {
		this.ccEmailID = ccEmailID;
	}

	public Long getPrivateInvestigatorKey() {
		return privateInvestigatorKey;
	}

	public void setPrivateInvestigatorKey(Long privateInvestigatorKey) {
		this.privateInvestigatorKey = privateInvestigatorKey;
	}

	public String getInvestigatorCode() {
		return investigatorCode;
	}

	public void setInvestigatorCode(String investigatorCode) {
		this.investigatorCode = investigatorCode;
	}

	public List<SelectValue> getInvetigatorTypeSelectIdList() {
		return invetigatorTypeSelectIdList;
	}

	public void setInvetigatorTypeSelectIdList(
			List<SelectValue> invetigatorTypeSelectIdList) {
		this.invetigatorTypeSelectIdList = invetigatorTypeSelectIdList;
	}

	public List<SelectValue> getInvestigatorTypeSelectValueList() {
		return investigatorTypeSelectValueList;
	}

	public void setInvestigatorTypeSelectValueList(
			List<SelectValue> investigatorTypeSelectValueList) {
		this.investigatorTypeSelectValueList = investigatorTypeSelectValueList;
	}

	public SelectValue getInvestigatorTypeSelectValue() {
		return investigatorTypeSelectValue;
	}

	public void setInvestigatorTypeSelectValue(
			SelectValue investigatorTypeSelectValue) {
		this.investigatorTypeSelectValue = investigatorTypeSelectValue;
	}

	public List<SelectValue> getStateList() {
		return stateList;
	}

	public void setStateList(List<SelectValue> stateList) {
		this.stateList = stateList;
	}

	public List<SelectValue> getCityList() {
		return cityList;
	}

	public void setCityList(List<SelectValue> cityList) {
		this.cityList = cityList;
	}

	public List<SelectValue> getGenderList() {
		return genderList;
	}

	public void setGenderList(List<SelectValue> genderList) {
		this.genderList = genderList;
	}
	
}
