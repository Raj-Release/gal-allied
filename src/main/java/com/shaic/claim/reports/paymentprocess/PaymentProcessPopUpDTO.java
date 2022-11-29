package com.shaic.claim.reports.paymentprocess;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class PaymentProcessPopUpDTO extends AbstractTableDTO  implements Serializable{
	private String intimationNo;
	private String claimNumber;
	private String mainMemberName;
	private String nameOfTheInsured;
	private String settledAmount;
	private Date admissionDate;
	private String admissionDateValue;
	private String ddNo;
	private String bankName;
	private Date billReceivedDate;
	private String billReceivedDateValue;
	private String policyNo;
	private String insuredPatientName;
	private String hospitalName;
	private String address;
	private Date dischargeDate;
	private String dischargeDateValue;
	private Date ddDate;
	private String ddDateValue;
	private String billNumber;
	private Date billDate;
	private String billDateValue;
	private String ccZonalOfc;
	private String ccAreaOfc;
	private String ccBranchOfc;
	private String dischargeVoucher;
	private String dvCoveringLetter;
	private String nriAndTeleSales;
	private String paymentAndDischarge;
	private String hospitalPayment;
	private String billSummary;
	private String mailId;
	private String sendMail;
	private String confirm;
	private String close;
	
	public String getSendMail() {
		return sendMail;
	}
	public void setSendMail(String sendMail) {
		this.sendMail = sendMail;
	}
	public String getConfirm() {
		return confirm;
	}
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
	public String getClose() {
		return close;
	}
	public void setClose(String close) {
		this.close = close;
	}
	public String getMailId() {
		return mailId;
	}
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	public String getDischargeVoucher() {
		return dischargeVoucher;
	}
	public void setDischargeVoucher(String dischargeVoucher) {
		this.dischargeVoucher = dischargeVoucher;
	}
	public String getDvCoveringLetter() {
		return dvCoveringLetter;
	}
	public void setDvCoveringLetter(String dvCoveringLetter) {
		this.dvCoveringLetter = dvCoveringLetter;
	}
	public String getNriAndTeleSales() {
		return nriAndTeleSales;
	}
	public void setNriAndTeleSales(String nriAndTeleSales) {
		this.nriAndTeleSales = nriAndTeleSales;
	}
	public String getPaymentAndDischarge() {
		return paymentAndDischarge;
	}
	public void setPaymentAndDischarge(String paymentAndDischarge) {
		this.paymentAndDischarge = paymentAndDischarge;
	}
	public String getHospitalPayment() {
		return hospitalPayment;
	}
	public void setHospitalPayment(String hospitalPayment) {
		this.hospitalPayment = hospitalPayment;
	}
	public String getBillSummary() {
		return billSummary;
	}
	public void setBillSummary(String billSummary) {
		this.billSummary = billSummary;
	}
	public String getCcZonalOfc() {
		return ccZonalOfc;
	}
	public void setCcZonalOfc(String ccZonalOfc) {
		this.ccZonalOfc = ccZonalOfc;
	}
	public String getCcAreaOfc() {
		return ccAreaOfc;
	}
	public void setCcAreaOfc(String ccAreaOfc) {
		this.ccAreaOfc = ccAreaOfc;
	}
	public String getCcBranchOfc() {
		return ccBranchOfc;
	}
	public void setCcBranchOfc(String ccBranchOfc) {
		this.ccBranchOfc = ccBranchOfc;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	public String getMainMemberName() {
		return mainMemberName;
	}
	public void setMainMemberName(String mainMemberName) {
		this.mainMemberName = mainMemberName;
	}
	public String getNameOfTheInsured() {
		return nameOfTheInsured;
	}
	public void setNameOfTheInsured(String nameOfTheInsured) {
		this.nameOfTheInsured = nameOfTheInsured;
	}
	public String getSettledAmount() {
		return settledAmount;
	}
	public void setSettledAmount(String settledAmount) {
		this.settledAmount = settledAmount;
	}
	public Date getAdmissionDate() {
		return admissionDate;
	}
	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}
	public String getAdmissionDateValue() {
		return admissionDateValue;
	}
	public void setAdmissionDateValue(String admissionDateValue) {
		this.admissionDateValue = admissionDateValue;
	}
	public String getDdNo() {
		return ddNo;
	}
	public void setDdNo(String ddNo) {
		this.ddNo = ddNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public Date getBillReceivedDate() {
		return billReceivedDate;
	}
	public void setBillReceivedDate(Date billReceivedDate) {
		this.billReceivedDate = billReceivedDate;
	}
	public String getBillReceivedDateValue() {
		return billReceivedDateValue;
	}
	public void setBillReceivedDateValue(String billReceivedDateValue) {
		this.billReceivedDateValue = billReceivedDateValue;
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
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getDischargeDate() {
		return dischargeDate;
	}
	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}
	public String getDischargeDateValue() {
		return dischargeDateValue;
	}
	public void setDischargeDateValue(String dischargeDateValue) {
		this.dischargeDateValue = dischargeDateValue;
	}
	public Date getDdDate() {
		return ddDate;
	}
	public void setDdDate(Date ddDate) {
		this.ddDate = ddDate;
	}
	public String getDdDateValue() {
		return ddDateValue;
	}
	public void setDdDateValue(String ddDateValue) {
		this.ddDateValue = ddDateValue;
	}
	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	public String getBillDateValue() {
		return billDateValue;
	}
	public void setBillDateValue(String billDateValue) {
		this.billDateValue = billDateValue;
	}

}
