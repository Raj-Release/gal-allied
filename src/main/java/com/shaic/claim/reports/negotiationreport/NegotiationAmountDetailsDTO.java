package com.shaic.claim.reports.negotiationreport;

import java.io.Serializable;

import javax.persistence.Column;

import com.shaic.arch.table.AbstractTableDTO;



public class NegotiationAmountDetailsDTO extends AbstractTableDTO implements Serializable{
	
	private Long slNo;
	
	private String intimationNo;
	
	private Double negotiatedAmt;
	
	private Double savedAmt;
	
	private Double claimAppAmt;
	
	private String stage;
	
	private String status;

	private String negotiationWith;
	
	private Double hstCLTrans;
	
	private Double totalNegotiationSaved;
	
	private Double claimedAmt;

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public Double getNegotiatedAmt() {
		return negotiatedAmt;
	}

	public void setNegotiatedAmt(Double negotiatedAmt) {
		this.negotiatedAmt = negotiatedAmt;
	}

	public Double getSavedAmt() {
		return savedAmt;
	}

	public void setSavedAmt(Double savedAmt) {
		this.savedAmt = savedAmt;
	}

	public Double getClaimAppAmt() {
		return claimAppAmt;
	}

	public void setClaimAppAmt(Double claimAppAmt) {
		this.claimAppAmt = claimAppAmt;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNegotiationWith() {
		return negotiationWith;
	}

	public void setNegotiationWith(String negotiationWith) {
		this.negotiationWith = negotiationWith;
	}

	public Long getSlNo() {
		return slNo;
	}

	public void setSlNo(Long slNo) {
		this.slNo = slNo;
	}

	public Double getHstCLTrans() {
		return hstCLTrans;
	}

	public void setHstCLTrans(Double hstCLTrans) {
		this.hstCLTrans = hstCLTrans;
	}

	public Double getTotalNegotiationSaved() {
		return totalNegotiationSaved;
	}

	public void setTotalNegotiationSaved(Double totalNegotiationSaved) {
		this.totalNegotiationSaved = totalNegotiationSaved;
	}

	public Double getClaimedAmt() {
		return claimedAmt;
	}

	public void setClaimedAmt(Double claimedAmt) {
		this.claimedAmt = claimedAmt;
	}
	
	
}
