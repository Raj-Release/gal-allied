package com.shaic.restservices;

/**
 * @author Velmurugan Rajendran
 *
 */
public class CRMRequest {
	
	private String intimationNumber;
	private String crmFlaggedReason;
	private String crmFlaggedRemarks;
	private String crcPriorityCode;
	private String crcPriorityDesc;
	
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	
	public String getCrmFlaggedReason() {
		return crmFlaggedReason;
	}
	public void setCrmFlaggedReason(String crmFlaggedReason) {
		this.crmFlaggedReason = crmFlaggedReason;
	}
	
	public String getCrmFlaggedRemarks() {
		return crmFlaggedRemarks;
	}
	public void setCrmFlaggedRemarks(String crmFlaggedRemarks) {
		this.crmFlaggedRemarks = crmFlaggedRemarks;
	}
	
	public String getCrcPriorityCode() {
		return crcPriorityCode;
	}
	public void setCrcPriorityCode(String crcPriorityCode) {
		this.crcPriorityCode = crcPriorityCode;
	}
	
	public String getCrcPriorityDesc() {
		return crcPriorityDesc;
	}
	public void setCrcPriorityDesc(String crcPriorityDesc) {
		this.crcPriorityDesc = crcPriorityDesc;
	}
	
	@Override
	public String toString() {
		return "CRMRequest [intimationNumber=" + intimationNumber
				+ ", crmFlaggedReason=" + crmFlaggedReason
				+ ", crmFlaggedRemarks=" + crmFlaggedRemarks
				+ ", crcPriorityCode=" + crcPriorityCode + ", crcPriorityDesc="
				+ crcPriorityDesc + "]";
	}
}
