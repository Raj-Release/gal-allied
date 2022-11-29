package com.shaic.claim.outpatient.registerclaim.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.domain.Insured;

public class DocumentDetailsDTO implements Serializable {
	
	private static final long serialVersionUID = -3836731473287197112L;

	@NotNull(message = "Please select Claim Type.")
	private SelectValue claimType;
	
	@NotNull(message = "Please select Insured Patient Name.")
	private Insured insuredPatientName;
	
	@NotNull(message = "Please Enter Amount Claimed.")
	@Size(min = 1, message = "Please Enter Amount Claimed.")
	private String amountClaimed;
	
	@NotNull(message = "Please Enter Provision Amount .")
	@Size(min = 1, message = "Please Enter Provision Amount .")
	private String provisionAmt;
	
	@NotNull(message = "Please Select Document Received From.")
	private SelectValue documentReceivedFrom;
	
	@NotNull(message = "Please Choose Document Received Date.")
	private Date documentReceivedDate;
	
	@NotNull(message = "Please Select Mode of Receipt.")
	private SelectValue modeOfReceipt;
	
	@NotNull(message = "Please Enter Acknowledgement Contact Number.")
	@Size(min = 1, message = "Please Enter Acknowledgement Contact Number.")
	private String acknowledgementContactNumber;
	
	private Long balanceSI;
	
	private String emailID;
	
	private String additionalRemarks;
	
	private Long amountEligible;
	
	private Long amountPayable;
	
    private Boolean paymentMode;
	
	private Long paymentModeFlag;
	
	
	
	@NotNull(message = "Please Choose Payee Name")
	private SelectValue payeeName;
	
	private String payeeEmailId;
	
	private String panNo;
	
	
	@NotNull(message = "Please Enter Approval Remarks.")
	@Size(min = 1, message = "Please Enter Approval Remarks.")
	private String approvalRemarks;
	
	@NotNull(message = "Please Enter Rejection Remarks.")
	@Size(min = 1, message = "Please Enter Rejection Remarks.")
	private String rejectionRemarks;
	
	@NotNull(message = "Please Enter PayableAt")
	@Size(min = 1 , message = "Please Enter PayableAt")
	private String payableAt;
	
	private Long docAcknowledgementKey;
	
	@NotNull(message = "Please Enter Account No")
	@Size(min = 1 , message = "Please Enter Account No")
	private String accountNo;
	
	@NotNull(message = "Please Enter Ifsc code")
	@Size(min = 1 , message = "Please Enter Ifsc code")
	private String ifscCode;
	
	private String branch;
	
	private String bankName;
	
	private String city;
	
	private Double totalBillAmount;
	
	private Double deductions;
	
	private Double approvedAmount;
	
	private Double netPayableAmount;
	
	private String billDateStr;
	
	private List<InsuredDetailsDTO> insuredDetailsList;
	
	private List<DocumentCheckListDTO> documentCheckListDTO;
	
	public DocumentDetailsDTO() {
		this.insuredDetailsList = new ArrayList<InsuredDetailsDTO>();
		this.documentCheckListDTO = new ArrayList<DocumentCheckListDTO>();
	}

	public SelectValue getClaimType() {
		return claimType;
	}

	public void setClaimType(SelectValue claimType) {
		this.claimType = claimType;
	}

	public Insured getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(Insured insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	public String getAmountClaimed() {
		return amountClaimed;
	}

	public void setAmountClaimed(String amountClaimed) {
		this.amountClaimed = amountClaimed;
	}

	public String getProvisionAmt() {
		return provisionAmt;
	}

	public void setProvisionAmt(String provisionAmt) {
		this.provisionAmt = provisionAmt;
	}

	public Long getBalanceSI() {
		return balanceSI;
	}

	public void setBalanceSI(Long balanceSI) {
		this.balanceSI = balanceSI;
	}

	public Long getAmountEligible() {
		return amountEligible;
	}

	public void setAmountEligible(Long amountEligible) {
		this.amountEligible = amountEligible;
	}

	public Long getAmountPayable() {
		return amountPayable;
	}

	public void setAmountPayable(Long amountPayable) {
		this.amountPayable = amountPayable;
	}

	public Boolean getPaymentMode() {
		return paymentMode;
	}

	public String getPayeeEmailId() {
		return payeeEmailId;
	}

	public void setPayeeEmailId(String payeeEmailId) {
		this.payeeEmailId = payeeEmailId;
	}

	public void setPaymentMode(Boolean paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getApprovalRemarks() {
		return approvalRemarks;
	}

	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}

	public String getRejectionRemarks() {
		return rejectionRemarks;
	}

	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
	}

	public Long getPaymentModeFlag() {
		return paymentModeFlag;
	}

	public void setPaymentModeFlag(Long paymentModeFlag) {
		this.paymentModeFlag = paymentModeFlag;
	}

	public SelectValue getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(SelectValue payeeName) {
		this.payeeName = payeeName;
	}


	public String getBillDateStr() {
		return billDateStr;
	}

	public void setBillDateStr(String billDateStr) {
		this.billDateStr = billDateStr;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	
	public String getPayableAt() {
		return payableAt;
	}

	public void setPayableAt(String payableAt) {
		this.payableAt = payableAt;
	}

	public Long getDocAcknowledgementKey() {
		return docAcknowledgementKey;
	}

	public void setDocAcknowledgementKey(Long docAcknowledgementKey) {
		this.docAcknowledgementKey = docAcknowledgementKey;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public SelectValue getDocumentReceivedFrom() {
		return documentReceivedFrom;
	}

	public void setDocumentReceivedFrom(SelectValue documentReceivedFrom) {
		this.documentReceivedFrom = documentReceivedFrom;
	}

	public Date getDocumentReceivedDate() {
		return documentReceivedDate;
	}

	public void setDocumentReceivedDate(Date documentReceivedDate) {
		this.documentReceivedDate = documentReceivedDate;
	}

	public SelectValue getModeOfReceipt() {
		return modeOfReceipt;
	}

	public void setModeOfReceipt(SelectValue modeOfReceipt) {
		this.modeOfReceipt = modeOfReceipt;
	}

	public String getAcknowledgementContactNumber() {
		return acknowledgementContactNumber;
	}

	public void setAcknowledgementContactNumber(String acknowledgementContactNumber) {
		this.acknowledgementContactNumber = acknowledgementContactNumber;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getAdditionalRemarks() {
		return additionalRemarks;
	}

	public void setAdditionalRemarks(String additionalRemarks) {
		this.additionalRemarks = additionalRemarks;
	}

	public List<InsuredDetailsDTO> getInsuredDetailsList() {
		return insuredDetailsList;
	}

	public List<DocumentCheckListDTO> getDocumentCheckListDTO() {
		return documentCheckListDTO;
	}

	public void setDocumentCheckListDTO(
			List<DocumentCheckListDTO> documentCheckListDTO) {
		this.documentCheckListDTO = documentCheckListDTO;
	}

	public void setInsuredDetailsList(List<InsuredDetailsDTO> insuredDetailsList) {
		this.insuredDetailsList = insuredDetailsList;
	}

	public Double getTotalBillAmount() {
		return totalBillAmount;
	}

	public void setTotalBillAmount(Double totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}

	public Double getDeductions() {
		return deductions;
	}

	public void setDeductions(Double deductions) {
		this.deductions = deductions;
	}

	public Double getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public Double getNetPayableAmount() {
		return netPayableAmount;
	}

	public void setNetPayableAmount(Double netPayableAmount) {
		this.netPayableAmount = netPayableAmount;
	}
}
