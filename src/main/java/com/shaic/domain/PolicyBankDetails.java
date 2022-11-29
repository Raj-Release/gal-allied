package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "IMS_CLS_POLICY_BANK_DTLS")
@NamedQueries({
		@NamedQuery(name = "PolicyBankDetails.findAll", query = "SELECT i FROM PolicyBankDetails i"),
		@NamedQuery(name = "PolicyBankDetails.findByPolicyKey", query = "SELECT i FROM PolicyBankDetails i where i.policyKey = :policyKey order by i.key desc")
})
public class PolicyBankDetails implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_POLICY_BANK_DTLS_KEY_GENERATOR", sequenceName = "SEQ_POL_BANK_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_POLICY_BANK_DTLS_KEY_GENERATOR" ) 
	@Column(name = "POL_BANK_KEY")
	private Long key;
	
	@Column(name = "POLICY_KEY")
	private Long policyKey;
	
//	@Column(name = "CREATED_DATE")
//	private Date createdDate;
	
	@Column(name = "CUSTOMER_NAME")
	private String customerName;
	
	@Column(name = "ACCOUNT_NUMBER")
	private String accountNumber;
	
	@Column(name = "ACCOUT_TYPE")
	private String accountType;
	
	@Column(name = "BANK_NAME")
	private String bankName;
	
	@Column(name = "BRANCH_NAME")
	private String branchName;
	


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_FROM")
	private Date effectiveFrom;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_TO")
	private Date effectiveTo;
	
	@Column(name = "IFSC_CODE")
	private String ifscCode;
	
	@Column(name = "OTHERS_DTLS")
	private String othersDetails;
	
	@Transient
	private String strEffectiveFrom;
	
	@Transient
	private String strEffectiveTo;

//	@Column(nullable = true, columnDefinition = "NUMBER", name="ACTIVE_STATUS", length = 1)
//	private Boolean activeStatus;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}

//	public Date getCreatedDate() {
//		return createdDate;
//	}
//
//	public void setCreatedDate(Date createdDate) {
//		this.createdDate = createdDate;
//	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Date getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public Date getEffectiveTo() {
		return effectiveTo;
	}

	public void setEffectiveTo(Date effectiveTo) {
		this.effectiveTo = effectiveTo;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getOthersDetails() {
		return othersDetails;
	}

	public void setOthersDetails(String othersDetails) {
		this.othersDetails = othersDetails;
	}

//	public Boolean getActiveStatus() {
//		return activeStatus;
//	}
//
//	public void setActiveStatus(Boolean activeStatus) {
//		this.activeStatus = activeStatus;
//	}

	public String getStrEffectiveFrom() {
		return strEffectiveFrom;
	}

	public void setStrEffectiveFrom(String strEffectiveFrom) {
		this.strEffectiveFrom = strEffectiveFrom;
	}

	public String getStrEffectiveTo() {
		return strEffectiveTo;
	}

	public void setStrEffectiveTo(String strEffectiveTo) {
		this.strEffectiveTo = strEffectiveTo;
	}
	
	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}


}
