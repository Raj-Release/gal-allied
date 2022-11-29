package com.shaic.claim.policy.search.ui.opsearch;

import java.io.Serializable;
import java.util.Date;

import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.OPClaim;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.Policy;

public class OPSearchIntimationDTO implements Serializable{

	private static final long serialVersionUID = 5138381465919429758L;
	
	private String opIntimationNo;
	private String policyNo;
	private String healthCardNo;
	private String productName;
	private String insuredPatientName;
	private String claimType;
	private Date opHealthCheckupDate;
	private String opVisitReason;
	private String opClaimNo;
	
	private OPClaim claim;
	private OPIntimation intimation;
	private Policy policy;
	
	public String getOpIntimationNo() {
		return opIntimationNo;
	}
	public void setOpIntimationNo(String opIntimationNo) {
		this.opIntimationNo = opIntimationNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getHealthCardNo() {
		return healthCardNo;
	}
	public void setHealthCardNo(String healthCardNo) {
		this.healthCardNo = healthCardNo;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public Date getOpHealthCheckupDate() {
		return opHealthCheckupDate;
	}
	public void setOpHealthCheckupDate(Date opHealthCheckupDate) {
		this.opHealthCheckupDate = opHealthCheckupDate;
	}
	public String getOpVisitReason() {
		return opVisitReason;
	}
	public void setOpVisitReason(String opVisitReason) {
		this.opVisitReason = opVisitReason;
	}
	public String getInsuredPatientName() {
		return insuredPatientName;
	}
	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}
	public OPClaim getClaim() {
		return claim;
	}
	public void setClaim(OPClaim claim) {
		this.claim = claim;
	}
	public OPIntimation getIntimation() {
		return intimation;
	}
	public void setIntimation(OPIntimation intimation) {
		this.intimation = intimation;
	}
	public Policy getPolicy() {
		return policy;
	}
	public void setPolicy(Policy policy) {
		this.policy = policy;
	}
	public String getOpClaimNo() {
		return opClaimNo;
	}
	public void setOpClaimNo(String opClaimNo) {
		this.opClaimNo = opClaimNo;
	}
	
}
