/**
 * 
 */
package com.shaic.reimbursement.paymentprocess.createbatch.search;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.shaic.arch.table.AbstractTableDTO;

/**
 * @author ntv.narenj
 *
 */
public class SearchCreateBatchTableDTO extends AbstractTableDTO  implements Serializable{

	private String intimationNo;
	private String claimNo;
	private String policyNo;
	private String rodNo;
	private String paymentStatus;
	private String cpuCode;
	private String product;
	private String accountBatchNo;
	private String paymentType;
	private String ifscCode;
	private String beneficiaryAcntNo;
	private String branchName;
	private String typeOfClaim;
	private String lotNo;
	private Double serviceTax;
	private Integer approvedAmt;
	private Integer aprStAmt;
	private Integer tdsAmt;
	private Integer netAmt;
	private Integer tdsPerc;
	private String payeeName;
	private String payableAt;
	private String providerCode;
	private String refNo;
	private String panNo;
	private String paymentReqUID;
	private String paymentReqDt;
	private String paymentReqDateValue;
	
	private String emailID;
	private String checkBoxValue;
	private Integer claimCount; 
	private String pioCode;
	private String zonalMailId;
	private String userId;
	
	public String getPaymentReqDateValue() {
		return paymentReqDateValue;
	}
	public void setPaymentReqDateValue(String paymentReqDateValue) {
		this.paymentReqDateValue = paymentReqDateValue;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getZonalMailId() {
		return zonalMailId;
	}
	public void setZonalMailId(String zonalMailId) {
		this.zonalMailId = zonalMailId;
	}
	public String getPioCode() {
		return pioCode;
	}
	public void setPioCode(String pioCode) {
		this.pioCode = pioCode;
	}
	public Integer getClaimCount() {
		return claimCount;
	}
	public void setClaimCount(Integer claimCount) {
		this.claimCount = claimCount;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
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
	public String getRodNo() {
		return rodNo;
	}
	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getAccountBatchNo() {
		return accountBatchNo;
	}
	public void setAccountBatchNo(String accountBatchNo) {
		this.accountBatchNo = accountBatchNo;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getBeneficiaryAcntNo() {
		return beneficiaryAcntNo;
	}
	public void setBeneficiaryAcntNo(String beneficiaryAcntNo) {
		this.beneficiaryAcntNo = beneficiaryAcntNo;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getTypeOfClaim() {
		return typeOfClaim;
	}
	public void setTypeOfClaim(String typeOfClaim) {
		this.typeOfClaim = typeOfClaim;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public Double getServiceTax() {
		return serviceTax;
	}
	public void setServiceTax(Double serviceTax) {
		this.serviceTax = serviceTax;
	}
	public Integer getApprovedAmt() {
		return approvedAmt;
	}
	public void setApprovedAmt(Integer approvedAmt) {
		this.approvedAmt = approvedAmt;
	}
	public Integer getAprStAmt() {
		return aprStAmt;
	}
	public void setAprStAmt(Integer aprStAmt) {
		this.aprStAmt = aprStAmt;
	}
	public Integer getTdsAmt() {
		return tdsAmt;
	}
	public void setTdsAmt(Integer tdsAmt) {
		this.tdsAmt = tdsAmt;
	}
	public Integer getNetAmt() {
		return netAmt;
	}
	public void setNetAmt(Integer netAmt) {
		this.netAmt = netAmt;
	}
	public Integer getTdsPerc() {
		return tdsPerc;
	}
	public void setTdsPerc(Integer tdsPerc) {
		this.tdsPerc = tdsPerc;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getPayableAt() {
		return payableAt;
	}
	public void setPayableAt(String payableAt) {
		this.payableAt = payableAt;
	}
	public String getProviderCode() {
		return providerCode;
	}
	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public String getPanNo() {
		return panNo;
	}
	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}
	public String getPaymentReqUID() {
		return paymentReqUID;
	}
	public void setPaymentReqUID(String paymentReqUID) {
		this.paymentReqUID = paymentReqUID;
	}
	public String getPaymentReqDt() {
		return paymentReqDt;
	}
	public void setPaymentReqDt(String paymentReqDt) {
		if(paymentReqDt !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(paymentReqDt);
			setPaymentReqDateValue(dateformat);
		    this.paymentReqDt = paymentReqDt;
		}
	}
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	public String getCheckBoxValue() {
		return checkBoxValue;
	}
	public void setCheckBoxValue(String checkBoxValue) {
		this.checkBoxValue = checkBoxValue;
	}
	
}
