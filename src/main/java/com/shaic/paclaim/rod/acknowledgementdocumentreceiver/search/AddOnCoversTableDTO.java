package com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;

public class AddOnCoversTableDTO extends AbstractTableDTO  implements Serializable{

	private SelectValue covers;
	private SelectValue optionalCover;
	private Double claimedAmount;
	private Long coverKey;
	private Boolean isReconsideration = false;
	private Long unNamedKey;
	
	/**
	 * Added for PA Screens.
	 * */
	
	private int slNo;
	
	private SelectValue addOnCovers;
	
	private String eligibleForPolicy;
	
	//private Double amountClaimed;
	
	private String remarks;
	
	private Long coverId;
	
	private Long claimKey;
	
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public Long getCoverKey() {
		return coverKey;
	}
	public void setCoverKey(Long coverKey) {
		this.coverKey = coverKey;
	}
	public SelectValue getCovers() {
		return covers;
	}
	public void setCovers(SelectValue covers) {
		this.covers = covers;
	}
	public Double getClaimedAmount() {
		return claimedAmount;
	}
	public void setClaimedAmount(Double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public int getSlNo() {
		return slNo;
	}
	public void setSlNo(int slNo) {
		this.slNo = slNo;
	}
	public SelectValue getAddOnCovers() {
		return addOnCovers;
	}
	public void setAddOnCovers(SelectValue addOnCovers) {
		this.addOnCovers = addOnCovers;
	}
	public String getEligibleForPolicy() {
		return eligibleForPolicy;
	}
	public void setEligibleForPolicy(String eligibleForPolicy) {
		this.eligibleForPolicy = eligibleForPolicy;
	}
	/*public Double getAmountClaimed() {
		return amountClaimed;
	}
	public void setAmountClaimed(Double amountClaimed) {
		this.amountClaimed = amountClaimed;
	}*/
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public SelectValue getOptionalCover() {
		return optionalCover;
	}
	public void setOptionalCover(SelectValue optionalCover) {
		this.optionalCover = optionalCover;
	}
	public Long getCoverId() {
		return coverId;
	}
	public void setCoverId(Long coverId) {
		this.coverId = coverId;
	}
	public Boolean getIsReconsideration() {
		return isReconsideration;
	}
	public void setIsReconsideration(Boolean isReconsideration) {
		this.isReconsideration = isReconsideration;
	}
	public Long getUnNamedKey() {
		return unNamedKey;
	}
	public void setUnNamedKey(Long unNamedKey) {
		this.unNamedKey = unNamedKey;
	}
	
	
	
}
