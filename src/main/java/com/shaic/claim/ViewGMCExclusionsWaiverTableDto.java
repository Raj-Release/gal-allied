package com.shaic.claim;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class ViewGMCExclusionsWaiverTableDto extends AbstractTableDTO implements Serializable {
	
	private String coverCode;
	private String coverDesc;
	private Long sumInsured;
	private Long rate;
	private Long ratePer;
	private Long premimum;
	public String getCoverCode() {
		return coverCode;
	}
	public void setCoverCode(String coverCode) {
		this.coverCode = coverCode;
	}
	public String getCoverDesc() {
		return coverDesc;
	}
	public void setCoverDesc(String coverDesc) {
		this.coverDesc = coverDesc;
	}
	public Long getSumInsured() {
		return sumInsured;
	}
	public void setSumInsured(Long sumInsured) {
		this.sumInsured = sumInsured;
	}
	public Long getRate() {
		return rate;
	}
	public void setRate(Long rate) {
		this.rate = rate;
	}
	public Long getRatePer() {
		return ratePer;
	}
	public void setRatePer(Long ratePer) {
		this.ratePer = ratePer;
	}
	public Long getPremimum() {
		return premimum;
	}
	public void setPremimum(Long premimum) {
		this.premimum = premimum;
	}
	
	
	

}
