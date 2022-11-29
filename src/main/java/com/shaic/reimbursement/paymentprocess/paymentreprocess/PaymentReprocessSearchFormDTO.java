package com.shaic.reimbursement.paymentprocess.paymentreprocess;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class PaymentReprocessSearchFormDTO  implements Serializable{

	private static final long serialVersionUID = 3558311391432382330L;
	
	private SelectValue type;
	private String intimationNumber;
	private String uprId;
	private String utrNo;
	private String cpuCode;
	private String paymentCPU;
	private String claimType;
	private SelectValue claimant;
	private SelectValue paymentMode;
	private SelectValue reprocessType;
	
	public SelectValue getType() {
		return type;
	}
	public void setType(SelectValue type) {
		this.type = type;
	}
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public String getUprId() {
		return uprId;
	}
	public void setUprId(String uprId) {
		this.uprId = uprId;
	}
	public String getUtrNo() {
		return utrNo;
	}
	public void setUtrNo(String utrNo) {
		this.utrNo = utrNo;
	}
	public String getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getPaymentCPU() {
		return paymentCPU;
	}
	public void setPaymentCPU(String paymentCPU) {
		this.paymentCPU = paymentCPU;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public SelectValue getClaimant() {
		return claimant;
	}
	public void setClaimant(SelectValue claimant) {
		this.claimant = claimant;
	}
	public SelectValue getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(SelectValue paymentMode) {
		this.paymentMode = paymentMode;
	}
	public SelectValue getReprocessType() {
		return reprocessType;
	}
	public void setReprocessType(SelectValue reprocessType) {
		this.reprocessType = reprocessType;
	}
	
}
