package com.shaic.claim;

import java.io.Serializable;
import java.util.Date;

public class SumInsuredBonusAlertDTO implements Serializable{

	private Integer sNo;

    private String productName; 

    private String previousPolicyNumber;

    private Long policyYear;

    private Date policyFromdate;
    
    private Date policyTodate;
    
    private Long sumInsured;
    
    private Long bonus;
    
    private Long utilizedAmount=0L;

	public Integer getsNo() {
		return sNo;
	}

	public void setsNo(Integer sNo) {
		this.sNo = sNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPreviousPolicyNumber() {
		return previousPolicyNumber;
	}

	public void setPreviousPolicyNumber(String previousPolicyNumber) {
		this.previousPolicyNumber = previousPolicyNumber;
	}

	public Long getPolicyYear() {
		return policyYear;
	}

	public void setPolicyYear(Long policyYear) {
		this.policyYear = policyYear;
	}

	public Date getPolicyFromdate() {
		return policyFromdate;
	}

	public void setPolicyFromdate(Date policyFromdate) {
		this.policyFromdate = policyFromdate;
	}

	public Date getPolicyTodate() {
		return policyTodate;
	}

	public void setPolicyTodate(Date policyTodate) {
		this.policyTodate = policyTodate;
	}

	public Long getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(Long sumInsured) {
		this.sumInsured = sumInsured;
	}

	public Long getBonus() {
		return bonus;
	}

	public void setBonus(Long bonus) {
		this.bonus = bonus;
	}

	public Long getUtilizedAmount() {
		return utilizedAmount;
	}

	public void setUtilizedAmount(Long utilizedAmount) {
		this.utilizedAmount = utilizedAmount;
	}
    
    
}
