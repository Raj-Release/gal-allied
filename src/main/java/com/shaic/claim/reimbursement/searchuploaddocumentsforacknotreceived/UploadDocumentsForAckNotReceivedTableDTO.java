package com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class UploadDocumentsForAckNotReceivedTableDTO extends AbstractTableDTO  implements Serializable{

	
private String intimationNo;
	
	private Long ackDocKey;
	

	private Long claimKey;
	
	private String claimNo;
	
	private String policyNo;
	
	private String acknowledgementNo;
	
	private String docReceivedType;
	
	private String insuredPatientName;
	
	private String cpuCode;
	
	private String claimType;
	
	private String hospitalName;
	
	private String hospitalCity;
	
	private Date dateOfAdmission;
	
	private String dateOfAdimissionValue;
	

	private String reasonForAdmission;
	
	private Long cpuId;
	
	private Long hospitalNameID;
	
	
	private String hospitalizationFlag;
	
	private String healthCardIdNo;
	
	
	
	
	public String getHealthCardIdNo() {
		return healthCardIdNo;
	}

	public void setHealthCardIdNo(String healthCardIdNo) {
		this.healthCardIdNo = healthCardIdNo;
	}

	public String getHospitalizationFlag() {
		return hospitalizationFlag;
	}

	public void setHospitalizationFlag(String hospitalizationFlag) {
		this.hospitalizationFlag = hospitalizationFlag;
	}

	public String getPreHospitalizationFlag() {
		return preHospitalizationFlag;
	}

	public void setPreHospitalizationFlag(String preHospitalizationFlag) {
		this.preHospitalizationFlag = preHospitalizationFlag;
	}

	public String getPostHospitalizationFlag() {
		return postHospitalizationFlag;
	}

	public void setPostHospitalizationFlag(String postHospitalizationFlag) {
		this.postHospitalizationFlag = postHospitalizationFlag;
	}

	public String getPartialHospitalizationFlag() {
		return partialHospitalizationFlag;
	}

	public void setPartialHospitalizationFlag(String partialHospitalizationFlag) {
		this.partialHospitalizationFlag = partialHospitalizationFlag;
	}

	public String getHospitalizationRepeatFlag() {
		return hospitalizationRepeatFlag;
	}

	public void setHospitalizationRepeatFlag(String hospitalizationRepeatFlag) {
		this.hospitalizationRepeatFlag = hospitalizationRepeatFlag;
	}

	public String getLumpSumAmountFlag() {
		return lumpSumAmountFlag;
	}

	public void setLumpSumAmountFlag(String lumpSumAmountFlag) {
		this.lumpSumAmountFlag = lumpSumAmountFlag;
	}

	public String getAddOnBenefitsHospitalCashFlag() {
		return addOnBenefitsHospitalCashFlag;
	}

	public void setAddOnBenefitsHospitalCashFlag(
			String addOnBenefitsHospitalCashFlag) {
		this.addOnBenefitsHospitalCashFlag = addOnBenefitsHospitalCashFlag;
	}

	public String getAddOnBenefitsPatientCareFlag() {
		return addOnBenefitsPatientCareFlag;
	}

	public void setAddOnBenefitsPatientCareFlag(String addOnBenefitsPatientCareFlag) {
		this.addOnBenefitsPatientCareFlag = addOnBenefitsPatientCareFlag;
	}

	private String preHospitalizationFlag;
	
	//private Long preHospitalizationFlag;
	
	
	//private Long postHospitalizationFlag;
	private String postHospitalizationFlag;
	
	
	//private Long partialHospitalizationFlag;
	private String partialHospitalizationFlag;
	
	
	private String hospitalizationRepeatFlag;
	
	
	//private Long lumSumAmountFlag;
	private String lumpSumAmountFlag;
	
	
	//private Long addOnBenefitsHospitalCashFlag;
	
	private String addOnBenefitsHospitalCashFlag;
	
	
	//private Long addOnBenefitsPatientCareFlag;
	private String addOnBenefitsPatientCareFlag;
	
	public Long getAckDocKey() {
		return ackDocKey;
	}

	public void setAckDocKey(Long ackDocKey) {
		this.ackDocKey = ackDocKey;
	}
	
	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	/*public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode2) {
		this.cpuCode = ""+cpuCode2;
	}*/

	public String getClaimType() {
		return claimType;
	}

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
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

	public Date getDateOfAdmission() {
		return dateOfAdmission;
	}

	public void setDateOfAdmission(Date dateOfAdmission) {
		if(dateOfAdmission !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(dateOfAdmission);
			setDateOfAdimissionValue(dateformat);
		    this.dateOfAdmission = dateOfAdmission;
		}
	}

	public String getDateOfAdimissionValue() {
		return dateOfAdimissionValue;
	}

	public void setDateOfAdimissionValue(String dateOfAdimissionValue) {
		this.dateOfAdimissionValue = dateOfAdimissionValue;
	}
	
	public String getReasonForAdmission() {
		return reasonForAdmission;
	}

	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}

	public Long getCpuId() {
		return cpuId;
	}

	public void setCpuId(Long cpuId) {
		this.cpuId = cpuId;
	}

	public Long getHospitalNameID() {
		return hospitalNameID;
	}

	public void setHospitalNameID(Long hospitalNameID) {
		this.hospitalNameID = hospitalNameID;
	}

	public String getAcknowledgementNo() {
		return acknowledgementNo;
	}

	public void setAcknowledgementNo(String acknowledgementNo) {
		this.acknowledgementNo = acknowledgementNo;
	}

	public String getDocReceivedType() {
		return docReceivedType;
	}

	public void setDocReceivedType(String docReceivedType) {
		this.docReceivedType = docReceivedType;
	}
	
	
}
