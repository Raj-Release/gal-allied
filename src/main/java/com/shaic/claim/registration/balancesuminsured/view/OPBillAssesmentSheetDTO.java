package com.shaic.claim.registration.balancesuminsured.view;

import java.io.Serializable;
import java.util.Date;

public class OPBillAssesmentSheetDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7007349632973195184L;

	private String claimNo;

	private String idNo;

	private String policyNo;

	private String policyType;

	private Date coveragePeriodFrom;

	private Date coveragePeriodTo;

	private String coveragePeriodFromStr;

	private String coveragePeriodToStr;
	
	private String sumInsuredOP;

	private String insuredName;

	private String claimantName;

	private Double age;

	private String relationship;

	private String payeeName;

	private String amountClaimed;

	private Double balanceSIAvailable;

	private Date billReceivedDate;

	private String billReceivedDateStr;
	
	private String mainBillDate;
	

	private Long totalBillAmount;

	private String deductions;

	private String approvedAmount;

	private String netPayAmount;

	private Date dateofApproval;

	private String preparedBy;

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public Date getCoveragePeriodFrom() {
		return coveragePeriodFrom;
	}

	public void setCoveragePeriodFrom(Date coveragePeriodFrom) {
		this.coveragePeriodFrom = coveragePeriodFrom;
	}

	public Date getCoveragePeriodTo() {
		return coveragePeriodTo;
	}

	public void setCoveragePeriodTo(Date coveragePeriodTo) {
		this.coveragePeriodTo = coveragePeriodTo;
	}

	
	public String getCoveragePeriodFromStr() {
		return coveragePeriodFromStr;
	}

	public void setCoveragePeriodFromStr(String coveragePeriodFromStr) {
		this.coveragePeriodFromStr = coveragePeriodFromStr;
	}

	public String getCoveragePeriodToStr() {
		return coveragePeriodToStr;
	}

	public void setCoveragePeriodToStr(String coveragePeriodToStr) {
		this.coveragePeriodToStr = coveragePeriodToStr;
	}

	public String getSumInsuredOP() {
		return sumInsuredOP;
	}

	public void setSumInsuredOP(String sumInsuredOP) {
		this.sumInsuredOP = sumInsuredOP;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getClaimantName() {
		return claimantName;
	}

	public void setClaimantName(String claimantName) {
		this.claimantName = claimantName;
	}

	public Double getAge() {
		return age;
	}

	public void setAge(Double age) {
		this.age = age;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getAmountClaimed() {
		return amountClaimed;
	}

	public void setAmountClaimed(String amountClaimed) {
		this.amountClaimed = amountClaimed;
	}

	public Double getBalanceSIAvailable() {
		return balanceSIAvailable;
	}

	public void setBalanceSIAvailable(Double balanceSIAvailable) {
		this.balanceSIAvailable = balanceSIAvailable;
	}

	public Date getBillReceivedDate() {
		return billReceivedDate;
	}

	public void setBillReceivedDate(Date billReceivedDate) {
		this.billReceivedDate = billReceivedDate;
	}

	
	public String getBillReceivedDateStr() {
		return billReceivedDateStr;
	}

	public void setBillReceivedDateStr(String billReceivedDateStr) {
		this.billReceivedDateStr = billReceivedDateStr;
	}

	public String getMainBillDate() {
		return mainBillDate;
	}

	public void setMainBillDate(String mainBillDate) {
		this.mainBillDate = mainBillDate;
	}

	public Long getTotalBillAmount() {
		return totalBillAmount;
	}

	public void setTotalBillAmount(Long totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}

	public String getDeductions() {
		return deductions;
	}

	public void setDeductions(String deductions) {
		this.deductions = deductions;
	}

	public String getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(String approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public String getNetPayAmount() {
		return netPayAmount;
	}

	public void setNetPayAmount(String netPayAmount) {
		this.netPayAmount = netPayAmount;
	}

	public Date getDateofApproval() {
		return dateofApproval;
	}

	public void setDateofApproval(Date dateofApproval) {
		this.dateofApproval = dateofApproval;
	}

	public String getPreparedBy() {
		return preparedBy;
	}

	public void setPreparedBy(String preparedBy) {
		this.preparedBy = preparedBy;
	}

}
