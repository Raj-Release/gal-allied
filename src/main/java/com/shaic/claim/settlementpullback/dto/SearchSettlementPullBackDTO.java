package com.shaic.claim.settlementpullback.dto;

import java.io.Serializable;
import java.util.List;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.domain.ClaimPayment;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class SearchSettlementPullBackDTO extends  AbstractTableDTO implements Serializable {
	private static final long serialVersionUID = -3945436017059374220L;

	private String intimationNo;
	
	private String rodNo;
	
	private String policyNo;
	
	private String claimNo;
	
	private String insuredPatientName;
	
	private String cpuCode;
	
	private String policyDivCode;
	
	private String hospitalName;
	
	private String hospitalCode;
	
	private String claimType;
	
	private String claimStatus;
	
	private Long rodKey;
	
	private NewIntimationDto newIntimationDTO;
	
	private Long claimKey;
	
	private Boolean isReconsideration = false;
	
	private Boolean isAllowedToProceed = false;
	
	private Boolean isQueryReplyReceived = false;
	
	private Long approvedAmount;
	
	private Long netPaymentAmount;
	
	private String approverName;
	
	private String approvedDate;
	
	private Boolean isFirstLevelQueryReplyReceived = false;
	
	private String message = "";
	
	private ClaimPayment claimPayment;
	

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
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

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}



	public NewIntimationDto getNewIntimationDTO() {
		return newIntimationDTO;
	}

	public void setNewIntimationDTO(NewIntimationDto newIntimationDTO) {
		this.newIntimationDTO = newIntimationDTO;
	}


	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Boolean getIsReconsideration() {
		return isReconsideration;
	}

	public void setIsReconsideration(Boolean isReconsideration) {
		this.isReconsideration = isReconsideration;
	}

	public Boolean getIsQueryReplyReceived() {
		return isQueryReplyReceived;
	}

	public void setIsQueryReplyReceived(Boolean isQueryReplyReceived) {
		this.isQueryReplyReceived = isQueryReplyReceived;
	}

	public Boolean getIsFirstLevelQueryReplyReceived() {
		return isFirstLevelQueryReplyReceived;
	}

	public void setIsFirstLevelQueryReplyReceived(
			Boolean isFirstLevelQueryReplyReceived) {
		this.isFirstLevelQueryReplyReceived = isFirstLevelQueryReplyReceived;
	}

	public String getRodNo() {
		return rodNo;
	}

	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getPolicyDivCode() {
		return policyDivCode;
	}

	public void setPolicyDivCode(String policyDivCode) {
		this.policyDivCode = policyDivCode;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public Long getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(Long approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public Long getNetPaymentAmount() {
		return netPaymentAmount;
	}

	public void setNetPaymentAmount(Long netPaymentAmount) {
		this.netPaymentAmount = netPaymentAmount;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	public String getApprovedDate() {
		return approvedDate;
	}

	public Boolean getIsAllowedToProceed() {
		return isAllowedToProceed;
	}

	public void setIsAllowedToProceed(Boolean isAllowedToProceed) {
		this.isAllowedToProceed = isAllowedToProceed;
	}

	public void setApprovedDate(String approvedDate) {
		this.approvedDate = approvedDate;
	}

	
	public ClaimPayment getClaimPayment() {
		return claimPayment;
	}

	public void setClaimPayment(ClaimPayment claimPayment) {
		this.claimPayment = claimPayment;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
