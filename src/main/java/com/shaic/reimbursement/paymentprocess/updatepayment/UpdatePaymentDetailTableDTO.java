/**
 * 
 */
package com.shaic.reimbursement.paymentprocess.updatepayment;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.narenj
 *
 */
public class UpdatePaymentDetailTableDTO extends AbstractTableDTO  implements Serializable{

	private String intimationNo;
	private String maApprovedDate;
	private String cpucode;
	private String cpuName;
	private String paymentCpuCodeString;
	private SelectValue paymentCpuCode;
	private String paymentCpuName;
	private String proposerName;
	private String employeeName;
	private String payeeNameString;
	private SelectValue paymentType;
	private String reasonForChange;
	private String ifscCode;
	private String beneficiaryAcno;
	private String bankName;
	private String branchName;
	private String branchCity;
	private String payableCity;
	private String emailID;
	private String typeOfClaim;
	private String approvedAmt;
	private String productName;
	private String checkBoxStatus;
	private Boolean chkSelect;
	private Long rodKey;
	private int totalCount;
	private String paymentTypeValue;
	//private Long paymentKey;
	private String recStatusFlag;
	private BeanItemContainer<SelectValue> payeeNameList;
	private SelectValue payeeName;
	private Long intimation_key;
	private Long policy_key;
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getMaApprovedDate() {
		return maApprovedDate;
	}
	public void setMaApprovedDate(String maApprovedDate) {
		this.maApprovedDate = maApprovedDate;
	}
	public String getCpucode() {
		return cpucode;
	}
	public void setCpucode(String cpucode) {
		this.cpucode = cpucode;
	}
	public String getCpuName() {
		return cpuName;
	}
	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}
	public SelectValue getPaymentCpuCode() {
		return paymentCpuCode;
	}
	public void setPaymentCpuCode(SelectValue paymentCpuCode) {
		this.paymentCpuCode = paymentCpuCode;
	}
	public String getPaymentCpuName() {
		return paymentCpuName;
	}
	public void setPaymentCpuName(String paymentCpuName) {
		this.paymentCpuName = paymentCpuName;
	}
	public String getProposerName() {
		return proposerName;
	}
	public void setProposerName(String proposerName) {
		this.proposerName = proposerName;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public SelectValue getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(SelectValue paymentType) {
		this.paymentType = paymentType;
	}
	public String getReasonForChange() {
		return reasonForChange;
	}
	public void setReasonForChange(String reasonForChange) {
		this.reasonForChange = reasonForChange;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getBeneficiaryAcno() {
		return beneficiaryAcno;
	}
	public void setBeneficiaryAcno(String beneficiaryAcno) {
		this.beneficiaryAcno = beneficiaryAcno;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getBranchCity() {
		return branchCity;
	}
	public void setBranchCity(String branchCity) {
		this.branchCity = branchCity;
	}
	public String getPayableCity() {
		return payableCity;
	}
	public void setPayableCity(String payableCity) {
		this.payableCity = payableCity;
	}
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	public String getTypeOfClaim() {
		return typeOfClaim;
	}
	public void setTypeOfClaim(String typeOfClaim) {
		this.typeOfClaim = typeOfClaim;
	}
	public String getApprovedAmt() {
		return approvedAmt;
	}
	public void setApprovedAmt(String approvedAmt) {
		this.approvedAmt = approvedAmt;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getCheckBoxStatus() {
		return checkBoxStatus;
	}
	public void setCheckBoxStatus(String checkBoxStatus) {
		this.checkBoxStatus = checkBoxStatus;
	}
	public Boolean getChkSelect() {
		return chkSelect;
	}
	public void setChkSelect(Boolean chkSelect) {
		this.chkSelect = chkSelect;
	}
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public String getPaymentTypeValue() {
		return paymentTypeValue;
	}
	public void setPaymentTypeValue(String paymentTypeValue) {
		this.paymentTypeValue = paymentTypeValue;
	}
	/*public Long getPaymentKey() {
		return paymentKey;
	}
	public void setPaymentKey(Long paymentKey) {
		this.paymentKey = paymentKey;
	}*/
	public String getRecStatusFlag() {
		return recStatusFlag;
	}
	public void setRecStatusFlag(String recStatusFlag) {
		this.recStatusFlag = recStatusFlag;
	}
	public String getPaymentCpuCodeString() {
		return paymentCpuCodeString;
	}
	public void setPaymentCpuCodeString(String paymentCpuCodeString) {
		this.paymentCpuCodeString = paymentCpuCodeString;
	}
	
	public Long getIntimation_key() {
		return intimation_key;
	}
	public void setIntimation_key(Long intimation_key) {
		this.intimation_key = intimation_key;
	}
	public Long getPolicy_key() {
		return policy_key;
	}
	public void setPolicy_key(Long policy_key) {
		this.policy_key = policy_key;
	}
	public String getPayeeNameString() {
		return payeeNameString;
	}
	public void setPayeeNameString(String payeeNameString) {
		this.payeeNameString = payeeNameString;
	}
	public SelectValue getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(SelectValue payeeName) {
		this.payeeName = payeeName;
	}
	public BeanItemContainer<SelectValue> getPayeeNameList() {
		return payeeNameList;
	}
	public void setPayeeNameList(BeanItemContainer<SelectValue> payeeNameList) {
		this.payeeNameList = payeeNameList;
	}
	
}
