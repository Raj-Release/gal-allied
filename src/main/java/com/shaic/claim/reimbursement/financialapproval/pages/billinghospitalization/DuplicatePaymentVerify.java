package com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization;

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

@Entity
@Table(name="IMS_CLS_DUP_PAYMENT_VERIFY_STS")
public class DuplicatePaymentVerify implements Serializable{
	
	@Id
	@SequenceGenerator(name="DUPLICATE_VERIFICATION_KEY_GENERATOR", sequenceName = "IMS_MASTXN.SEQ_DUP_PAYMENT_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DUPLICATE_VERIFICATION_KEY_GENERATOR" )
	
	@Column(name="DUP_PAYMENT_KEY")
	private Long key;
	
	@Column(name="CLAIM_NUMBER")
	private String claimNo;
	
	@Column(name="POLICY_NUMBER")
	private String policyNo;
	
	@Column(name="INSURED_NAME")
	private String insuredName;
	
	@Column(name="ACCOUNT_NUMBER")
	private String accountNo;
	
	@Column(name="IFSC_CODE")
	private String ifscCode;
	
	@Column(name="ACC_VERIFY_STAGE")
	private String verifyStage;
	
	@Column(name="VERIFIED_USER")
	private String verifiedUser;
	
	@Column(name="ROD_KEY")
	private Long rodKey;
	
	@Column(name="VERIFIED_DATE")
	private Date verifiedDate;
	
	@Column(name="ROD_NUMBER")
	private String rodNumber;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getVerifyStage() {
		return verifyStage;
	}

	public void setVerifyStage(String verifyStage) {
		this.verifyStage = verifyStage;
	}

	public String getVerifiedUser() {
		return verifiedUser;
	}

	public void setVerifiedUser(String verifiedUser) {
		this.verifiedUser = verifiedUser;
	}

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public Date getVerifiedDate() {
		return verifiedDate;
	}

	public void setVerifiedDate(Date verifiedDate) {
		this.verifiedDate = verifiedDate;
	}

	public String getRodNumber() {
		return rodNumber;
	}

	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}

	

}
