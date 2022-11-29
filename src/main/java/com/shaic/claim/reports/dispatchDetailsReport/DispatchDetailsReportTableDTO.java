package com.shaic.claim.reports.dispatchDetailsReport;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;

public class DispatchDetailsReportTableDTO extends AbstractTableDTO implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String intimationNo;

	private String policyNo;

	private String rodNo;

	private String claimType;

	private String docReceivedFrom;

	private String rodType;

	private String chequeDDNumber;

	private Long paymentKey;

	private Long intimationKey;

	private Long rodKey;

	private Double settledAmount;

	private String courierPartner;

	private String awsNumber;

	private String updatedawsNumber;

	private String chequeDDdate;

	private String chequeDDStatus;

	private String chequeDDdeliveryTo;

	private String returnRemark;

	private String claimNumber;

	private String chequeDDReturndate;

	private String batchNumber;

	private Long dispatchStatus;

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

	public String getRodNo() {
		return rodNo;
	}

	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getDocReceivedFrom() {
		return docReceivedFrom;
	}

	public void setDocReceivedFrom(String docReceivedFrom) {
		this.docReceivedFrom = docReceivedFrom;
	}

	public String getRodType() {
		return rodType;
	}

	public void setRodType(String rodType) {
		this.rodType = rodType;
	}

	public String getChequeDDNumber() {
		return chequeDDNumber;
	}

	public void setChequeDDNumber(String chequeDDNumber) {
		this.chequeDDNumber = chequeDDNumber;
	}

	public Long getPaymentKey() {
		return paymentKey;
	}

	public void setPaymentKey(Long paymentKey) {
		this.paymentKey = paymentKey;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public Double getSettledAmount() {
		return settledAmount;
	}

	public void setSettledAmount(Double settledAmount) {
		this.settledAmount = settledAmount;
	}

	public String getCourierPartner() {
		return courierPartner;
	}

	public void setCourierPartner(String courierPartner) {
		this.courierPartner = courierPartner;
	}

	public String getAwsNumber() {
		return awsNumber;
	}

	public void setAwsNumber(String awsNumber) {
		this.awsNumber = awsNumber;
	}

	public String getUpdatedawsNumber() {
		return updatedawsNumber;
	}

	public void setUpdatedawsNumber(String updatedawsNumber) {
		this.updatedawsNumber = updatedawsNumber;
	}

	public String getChequeDDdate() {
		return chequeDDdate;
	}

	public void setChequeDDdate(String chequeDDdate) {
		this.chequeDDdate = chequeDDdate;
	}

	public String getChequeDDStatus() {
		return chequeDDStatus;
	}

	public void setChequeDDStatus(String chequeDDStatus) {
		this.chequeDDStatus = chequeDDStatus;
	}

	public String getChequeDDdeliveryTo() {
		return chequeDDdeliveryTo;
	}

	public void setChequeDDdeliveryTo(String chequeDDdeliveryTo) {
		this.chequeDDdeliveryTo = chequeDDdeliveryTo;
	}

	public String getReturnRemark() {
		return returnRemark;
	}

	public void setReturnRemark(String returnRemark) {
		this.returnRemark = returnRemark;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public String getChequeDDReturndate() {
		return chequeDDReturndate;
	}

	public void setChequeDDReturndate(String chequeDDReturndate) {
		this.chequeDDReturndate = chequeDDReturndate;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Long getDispatchStatus() {
		return dispatchStatus;
	}

	public void setDispatchStatus(Long dispatchStatus) {
		this.dispatchStatus = dispatchStatus;
	}

}
