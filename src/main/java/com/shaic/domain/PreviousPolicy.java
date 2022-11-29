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

@Entity
@Table(name="IMS_CLS_PREVIOUS_POLICY_DTLS")

@NamedQueries( {
	@NamedQuery(name="PreviousPolicy.findAll", query="SELECT i FROM PreviousPolicy i"),
	@NamedQuery(name = "PreviousPolicy.findByPolAssrCode",
		query = "select o from PreviousPolicy o where o.proposerCode like :parentKey order by o.policyFrmDate desc"),
	@NamedQuery(name = "PreviousPolicy.findByCurrentPolicy", query = "select o from PreviousPolicy o where o.currentPolicyNumber = :policyNumber order by o.policyFrmDate desc"),
	@NamedQuery(name = "PreviousPolicy.findByPreviousPolicyAndCurrentPolicyNo", query = "select o from PreviousPolicy o where o.policyNumber like :policyNumber and o.currentPolicyNumber like :currentPolicyNumber")
})

public class PreviousPolicy implements Serializable {

	@Id
	@SequenceGenerator(name="IMS_CLS_PREV_POLICY_DTLS_KEY_GENERATOR", sequenceName = "SEQ_CLS_PREV_POLICY_DTLS_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_PREV_POLICY_DTLS_KEY_GENERATOR" )
	@Column(name = "PREVIOUS_POLICY_KEY")
	private Long key;
	
	@Column(name = "POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name = "PROPOSER_CODE")
	private String proposerCode;
	
	@Column(name = "PROPOSER_NAME")
	private String proposerName;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "POLICY_START_DATE")
	private Date policyFrmDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "POLICY_END_DATE")
	private Date policyToDate;
	
	@Column(name = "UNDERWRITING_YEAR")
	private Long underWritingYear;
	
	@Column(name = "PREVIOUS_INSURER_NAME")
	private String previousInsurerName;
	
	@Column(name = "PRODUCT_CODE")
	private String productCode;
	
	@Column(name = "PRODUCT_NAME")
	private String productName;

	@Column(name = "SUM_INSURED")
	private Double sumInsured;

	@Column(name = "PREMIUM")
	private Double premium;
	
	@Column(name = "ISSUING_OFFICE_CODE")
	private String issuingOfficeCode;   
	
	@Column(name = "PROPOSER_ADDRESS")
	private String proposerAddress;
	
	@Column(name = "PROPOSER_TELEPHONE_NUMBER")
	private String proposerTelephoneNo;
	
	@Column(name = "PROPOSER_EMAIL_ID")
	private String proposerEmailId;

	@Column(name = "PROPOSER_FAX_NUMBER")
	private String proposerFaxNo;
	
	@Column(name = "INSURED_NAME")
	private String insuredName;

	@Column(name = "PRE_EXISTING_DISEASE")
	private String preExistingDisease;
	
	@Column(name = "CUR_POLICY_NUMBER")
	private String currentPolicyNumber;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getProposerCode() {
		return proposerCode;
	}

	public void setProposerCode(String proposerCode) {
		this.proposerCode = proposerCode;
	}

	public String getProposerName() {
		return proposerName;
	}

	public void setProposerName(String proposerName) {
		this.proposerName = proposerName;
	}

	public Date getPolicyFrmDate() {
		return policyFrmDate;
	}

	public void setPolicyFrmDate(Date policyFrmDate) {
		this.policyFrmDate = policyFrmDate;
	}

	public Date getPolicyToDate() {
		return policyToDate;
	}

	public void setPolicyToDate(Date policyToDate) {
		this.policyToDate = policyToDate;
	}

	public Long getUnderWritingYear() {
		return underWritingYear;
	}

	public void setUnderWritingYear(Long underWritingYear) {
		this.underWritingYear = underWritingYear;
	}

	public String getPreviousInsurerName() {
		return previousInsurerName;
	}

	public void setPreviousInsurerName(String previousInsurerName) {
		this.previousInsurerName = previousInsurerName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Double getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(Double sumInsured) {
		this.sumInsured = sumInsured;
	}

	public Double getPremium() {
		return premium;
	}

	public void setPremium(Double premium) {
		this.premium = premium;
	}

	public String getIssuingOfficeCode() {
		return issuingOfficeCode;
	}

	public void setIssuingOfficeCode(String issuingOfficeCode) {
		this.issuingOfficeCode = issuingOfficeCode;
	}

	public String getProposerAddress() {
		return proposerAddress;
	}

	public void setProposerAddress(String proposerAddress) {
		this.proposerAddress = proposerAddress;
	}


	public String getProposerEmailId() {
		return proposerEmailId;
	}

	public void setProposerEmailId(String proposerEmailId) {
		this.proposerEmailId = proposerEmailId;
	}


	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getPreExistingDisease() {
		return preExistingDisease;
	}

	public void setPreExistingDisease(String preExistingDisease) {
		this.preExistingDisease = preExistingDisease;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProposerTelephoneNo() {
		return proposerTelephoneNo;
	}

	public void setProposerTelephoneNo(String proposerTelephoneNo) {
		this.proposerTelephoneNo = proposerTelephoneNo;
	}

	public String getProposerFaxNo() {
		return proposerFaxNo;
	}

	public void setProposerFaxNo(String proposerFaxNo) {
		this.proposerFaxNo = proposerFaxNo;
	}

	public String getCurrentPolicyNumber() {
		return currentPolicyNumber;
	}

	public void setCurrentPolicyNumber(String currentPolicyNumber) {
		this.currentPolicyNumber = currentPolicyNumber;
	}	
	
}
