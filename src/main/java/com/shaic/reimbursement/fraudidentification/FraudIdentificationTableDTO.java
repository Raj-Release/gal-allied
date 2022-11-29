package com.shaic.reimbursement.fraudidentification;

import java.io.Serializable;
import java.util.Date;

import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.domain.Policy;

public class FraudIdentificationTableDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6516614835760036025L;
	private String serialNumber; 
	private String parameterType;
	private String parameterValue;
	private Boolean newRecord;
	private Boolean disable;
	private Boolean edit;
	private Date effectiveStartDate;
	private Date effectiveEndDate;
	private String recipientTo;
	private String recipientCc;
	private String userRemarks;
	private String hospitalName;
	private String hospitalCity;
	private String hospitalAddress;
	private String hospitalIrdaCode;
	private String hospitalInternalCode;
	private String policyNumber;
	private String productName;
	private String policyStartDate;
	private String policyEndDate;
	private String intermediaryName;
	private String intermediaryCode;

	private HospitalDto hospitalDto;
	private Policy policy;
	
	public FraudIdentificationTableDTO() {

		// this.tmpPolicy = new TmpPolicy();
		this.policy = new Policy();		
		this.hospitalDto = new HospitalDto();
		edit = false;
		newRecord=false;
		disable=false;
	}
	
	public HospitalDto getHospitalDto() {
		return hospitalDto;
	}

	public void setHospitalDto(HospitalDto hospitalDto) {
		this.hospitalDto = hospitalDto;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}
	
	public Boolean getNewRecord() {
		return newRecord;
	}

	public void setNewRecord(Boolean newRecord) {
		this.newRecord = newRecord;
	}
	
	public Date getEffectiveEndDate() {
		return effectiveEndDate;
	}

	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getIntermediaryCode() {
		return intermediaryCode;
	}

	public void setIntermediaryCode(String intermediaryCode) {
		this.intermediaryCode = intermediaryCode;
	}

	public String getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(String policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public String getPolicyEndDate() {
		return policyEndDate;
	}

	public void setPolicyEndDate(String policyEndDate) {
		this.policyEndDate = policyEndDate;
	}

	public String getIntermediaryName() {
		return intermediaryName;
	}

	public void setIntermediaryName(String intermediaryName) {
		this.intermediaryName = intermediaryName;
	}
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Date getEffectiveStartDate() {
		return effectiveStartDate;
	}
	
	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}

	public String getRecipientTo() {
		return recipientTo;
	}

	public void setRecipientTo(String recipientTo) {
		this.recipientTo = recipientTo;
	}

	public String getRecipientCc() {
		return recipientCc;
	}

	public void setRecipientCc(String recipientCc) {
		this.recipientCc = recipientCc;
	}

	public String getUserRemarks() {
		return userRemarks;
	}

	public void setUserRemarks(String userRemarks) {
		this.userRemarks = userRemarks;
	}

	public Boolean getEdit() {
		return edit;
	}

	public void setEdit(Boolean edit) {
		this.edit = edit;
	}

	public Boolean getDisable() {
		return disable;
	}

	public void setDisable(Boolean disable) {
		this.disable = disable;
	}

/*	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
*/
	public String getParameterType() {
		return parameterType;
	}

	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalCity() {
		return hospitalCity;
	}

	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}

	public String getHospitalAddress() {
		return hospitalAddress;
	}

	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}

	public String getHospitalIrdaCode() {
		return hospitalIrdaCode;
	}

	public void setHospitalIrdaCode(String hospitalIrdaCode) {
		this.hospitalIrdaCode = hospitalIrdaCode;
	}

	public String getHospitalInternalCode() {
		return hospitalInternalCode;
	}

	public void setHospitalInternalCode(String hospitalInternalCode) {
		this.hospitalInternalCode = hospitalInternalCode;
	}

	@Override
	public String toString() {
		return "FraudIdentificationTableDTO [hospitalName="
				+ hospitalName + ", hospitalCity=" + hospitalCity
				+ ", hospitalIrdaCode="+ hospitalIrdaCode + ", hospitalInternalCode="
				+ hospitalInternalCode+ ", hospitalAddress="+hospitalAddress+
				"]";
	}

	
}

