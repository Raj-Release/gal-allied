/**
 * 
 */
package com.shaic.claim.policy.search.ui.opsearch;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

/**
 * @author SARAVANA
 *
 */
public class SearchProcessOPClaimRequestTableDTO extends AbstractTableDTO  implements Serializable{
	
	
	private static final long serialVersionUID = 535798706985422630L;

	private String intimationNo;
	
	private String policyNo;
	
	private String healthCardIDNo;
	
	
	private Date healthCheckupDate;
	
	private String reasonForHealthVisit;
	
	private String amountClaimed;
	
	private String claimType;
	
	private String insuredPatientName;
	
	private Long healthCheckupKey;
	
	private Long claimKey;
	
	private String claimNo;
	
	private String pioCode;

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

	public String getHealthCardIDNo() {
		return healthCardIDNo;
	}

	public void setHealthCardIDNo(String healthCardIDNo) {
		this.healthCardIDNo = healthCardIDNo;
	}

	public Date getHealthCheckupDate() {
		return healthCheckupDate;
	}

	public void setHealthCheckupDate(Date healthCheckupDate) {
		this.healthCheckupDate = healthCheckupDate;
	}

	public String getReasonForHealthVisit() {
		return reasonForHealthVisit;
	}

	public void setReasonForHealthVisit(String reasonForHealthVisit) {
		this.reasonForHealthVisit = reasonForHealthVisit;
	}

	public String getAmountClaimed() {
		return amountClaimed;
	}

	public void setAmountClaimed(String amountClaimed) {
		this.amountClaimed = amountClaimed;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	public Long getHealthCheckupKey() {
		return healthCheckupKey;
	}

	public void setHealthCheckupKey(Long healthCheckupKey) {
		this.healthCheckupKey = healthCheckupKey;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getPioCode() {
		return pioCode;
	}

	public void setPioCode(String pioCode) {
		this.pioCode = pioCode;
	}

	
}
