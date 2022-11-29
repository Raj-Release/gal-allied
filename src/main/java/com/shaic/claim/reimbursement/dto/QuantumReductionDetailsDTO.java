/**
 * 
 */
package com.shaic.claim.reimbursement.dto;

import com.shaic.arch.fields.dto.SelectValue;

/**
 * @author ntv.vijayar
 *
 */
public class QuantumReductionDetailsDTO {
	
	private Long slNo;
	
	private String requestNo;
	
	private Long preAuthAmount =0L;
	
	private Long finalBillAmount;
	
	private Long settlementAmount = 0L;
	
	private Long anhAmount;
	
	private Long savedAmount;
	
	private String diagnosis;
	
	private String management;
	
	private String significantClinicalInformationValue;
	
	private String eligiblity;
	
	private String requestedSavedAmount;	
	
	private String requestRemarks;
	
	private SelectValue anh;
	
	private String anhFlag;
	

	public String getRequestRemarks() {
		return requestRemarks;
	}

	public void setRequestRemarks(String requestRemarks) {
		this.requestRemarks = requestRemarks;
	}

	public Long getSlNo() {
		return slNo;
	}

	public void setSlNo(Long slNo) {
		this.slNo = slNo;
	}

	
	public Long getPreAuthAmount() {
		return preAuthAmount;
	}

	public void setPreAuthAmount(Long preAuthAmount) {
		this.preAuthAmount = preAuthAmount;
	}

	public Long getFinalBillAmount() {
		return finalBillAmount;
	}

	public void setFinalBillAmount(Long finalBillAmount) {
		this.finalBillAmount = finalBillAmount;
	}

	public Long getSettlementAmount() {
		return settlementAmount;
	}

	public void setSettlementAmount(Long settlementAmount) {
		this.settlementAmount = settlementAmount;
	}

	public Long getAnhAmount() {
		return anhAmount;
	}

	public void setAnhAmount(Long anhAmount) {
		this.anhAmount = anhAmount;
	}

	public Long getSavedAmount() {
		return savedAmount;
	}

	public void setSavedAmount(Long savedAmount) {
		this.savedAmount = savedAmount;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getManagement() {
		return management;
	}

	public void setManagement(String management) {
		this.management = management;
	}

	public String getSignificantClinicalInformationValue() {
		return significantClinicalInformationValue;
	}

	public void setSignificantClinicalInformationValue(
			String significantClinicalInformationValue) {
		this.significantClinicalInformationValue = significantClinicalInformationValue;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getEligiblity() {
		return eligiblity;
	}

	public void setEligiblity(String eligiblity) {
		this.eligiblity = eligiblity;
	}

	public String getRequestedSavedAmount() {
		return requestedSavedAmount;
	}

	public void setRequestedSavedAmount(String requestedSavedAmount) {
		this.requestedSavedAmount = requestedSavedAmount;
	}

	public SelectValue getAnh() {
		return anh;
	}

	public void setAnh(SelectValue anh) {
		this.anh = anh;
	}

	public String getAnhFlag() {
		return anhFlag;
	}

	public void setAnhFlag(String anhFlag) {
		this.anhFlag = anhFlag;
	}
	

}
