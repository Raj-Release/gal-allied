package com.shaic.claim.omp.ratechange;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;

public class OMPClaimRateChangeAndOsUpdationTableDTO extends AbstractTableDTO  implements Serializable{
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String serialNumber;

	private String intimationNo;
	
	private String rodNo;
	
	private SelectValue eventCode;
	
	private String classification;
	
	private String outstandingAmount;
	
	private Date intimationDate;
	
	private Double conversionRate;
	
	private String history;
	
	private OMPIntimation intimation;
	
	private OMPClaim claim;
	
	private Long reimbursementKey;
	
	private Double modifidRate;

//	public String getSerialno() {
//		return serialno;
//	}
//
//	public void setSerialno(String serialno) {
//		this.serialno = serialno;
//	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getRodNo() {
		return rodNo;
	}

	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}

	public SelectValue getEventCode() {
		return eventCode;
	}

	public void setEventCode(SelectValue eventCode) {
		this.eventCode = eventCode;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getOutstandingAmount() {
		return outstandingAmount;
	}

	public void setOutstandingAmount(String outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}

	public Date getIntimationDate() {
		return intimationDate;
	}

	public void setIntimationDate(Date intimationDate) {
		this.intimationDate = intimationDate;
	}

	public Double getConversionRate() {
		return conversionRate;
	}

	public void setConversionRate(Double conversionRate) {
		this.conversionRate = conversionRate;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public OMPIntimation getIntimation() {
		return intimation;
	}

	public void setIntimation(OMPIntimation intimation) {
		this.intimation = intimation;
	}

	public OMPClaim getClaim() {
		return claim;
	}

	public void setClaim(OMPClaim claim) {
		this.claim = claim;
	}

	public Long getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public Double getModifidRate() {
		return modifidRate;
	}

	public void setModifidRate(Double modifidRate) {
		this.modifidRate = modifidRate;
	}

	
}
