//package com.shaic.domain;
//
//import java.io.Serializable;
//
//import javax.persistence.*;
//
//import java.math.BigDecimal;
//import java.util.Date;
//
//
///**
// * The persistent class for the IMS_TMP_PREV_POLICY_T database table.
// * 
// */
//@Entity
//@Table(name="IMS_TMP_PREV_POLICY")
//
//@NamedQueries( {
//	@NamedQuery(name="TmpPrevPolicy.findAll", query="SELECT i FROM TmpPrevPolicy i"),
//	@NamedQuery(name = "TmpPrevPolicy.findByPolAssrCode", query = "select o from TmpPrevPolicy o where o.proposerCode like :parentKey"),
//	@NamedQuery(name = "TmpPrevPolicy.findByPolicyNumber", query = "select o from TmpPrevPolicy o where o.policyNo like :policyNo")
//})
//public class TmpPrevPolicy implements Serializable {
//	private static final long serialVersionUID = 1L;
//
//	@Id
//	@Column(name="POLICY_NUMBER")
//	private String policyNo;	
//	
//	@Column(name="PROPOSER_NAME")
//	private String proposerName;
//
//	@Column(name="PROPOSER_CODE")
//	private String proposerCode;
//
//	@Column(name="PROPOSER_ADDRESS")
//	private String proposerAddress;	
//	
//	@Column(name="PROPOSER_TELEPHONE_NUMBER")
//	private String proposerTelephoneNo;
//	
//	@Column(name="PROPOSER_EMAIL_ID")
//	private String proposerEmailId;
//	
//	@Column(name="PROPOSER_FAX_NUMBER")
//	private String proposerFaxNumber;
//	
//	@Temporal(TemporalType.DATE)
//	@Column(name="POLICY_START_DATE")
//	private Date polFmDt;
//
//	@Column(name="ISSUING_OFFICE_CODE")
//	private String policyIssuingOfficeCode;
//
//	@Column(name="PREMIUM")
//	private Double polPremium;
//
//	@Column(name="PRODUCT_CODE")
//	private String productCode;
//	
//	@Column(name="PRODUCT_NAME")
//	private String productName;
//
//	@Temporal(TemporalType.DATE)
//	@Column(name="POLICY_END_DATE")
//	private Date polToDt;
//
//	@Column(name="POLICY_UW_YEAR")
//	private Long polUwYear;
//
//	@Column(name="SUM_INSURED")
//	private Double sumInsured;	
//	
//	@Column(name="RISK_NAME")
//	private String riskName;
//	
//	@Column(name="PREVIOUS_INSURER_NAME")
//	private String previousInsurerName;
//	
//	@Column(name="PRE_EXISTING_DISEASE")
//	private String preExistingDiseases;
//
//	public TmpPrevPolicy() {
//	}
//
//	public String getPolicyNo() {
//		return policyNo;
//	}
//
//	public void setPolicyNo(String policyNo) {
//		this.policyNo = policyNo;
//	}
//
//	public String getProposerName() {
//		return proposerName;
//	}
//
//	public void setProposerName(String proposerName) {
//		this.proposerName = proposerName;
//	}
//
//	public String getProposerCode() {
//		return proposerCode;
//	}
//
//	public void setProposerCode(String proposerCode) {
//		this.proposerCode = proposerCode;
//	}
//
//	public String getProposerAddress() {
//		return proposerAddress;
//		
//	}
//
//	public void setProposerAddress(String proposerAddress) {
//		this.proposerAddress = proposerAddress;
//	}
//
//	public Date getPolFmDt() {
//		return polFmDt;
//	}
//
//	public void setPolFmDt(Date polFmDt) {
//		this.polFmDt = polFmDt;
//	}
//
//	public String getPolicyIssuingOfficeCode() {
//		return policyIssuingOfficeCode;
//	}
//
//	public void setPolicyIssuingOfficeCode(String policyIssuingOfficeCode) {
//		this.policyIssuingOfficeCode = policyIssuingOfficeCode;
//	}
//
//	public Double getPolPremium() {
//		return polPremium;
//	}
//
//	public void setPolPremium(Double polPremium) {
//		this.polPremium = polPremium;
//	}
//	
//	public String getProposerTelephoneNo() {
//		return proposerTelephoneNo;
//	}
//
//	public void setProposerTelephoneNo(String proposerTelephoneNo) {
//		this.proposerTelephoneNo = proposerTelephoneNo;
//	}
//
//	public String getProposerEmailId() {
//		return proposerEmailId;
//	}
//
//	public void setProposerEmailId(String proposerEmailId) {
//		this.proposerEmailId = proposerEmailId;
//	}
//
//	public String getProposerFaxNumber() {
//		return proposerFaxNumber;
//	}
//
//	public void setProposerFaxNumber(String proposerFaxNumber) {
//		this.proposerFaxNumber = proposerFaxNumber;
//	}
//
//	public String getProductCode() {
//		return productCode;
//	}
//
//	public void setProductCode(String productCode) {
//		this.productCode = productCode;
//	}
//
//	public String getProductName() {
//		return productName;
//	}
//
//	public void setProductName(String productName) {
//		this.productName = productName;
//	}
//
//	public Date getPolToDt() {
//		return polToDt;
//	}
//
//	public void setPolToDt(Date polToDt) {
//		this.polToDt = polToDt;
//	}
//
//	public Long getPolUwYear() {
//		return polUwYear;
//	}
//
//	public void setPolUwYear(Long polUwYear) {
//		this.polUwYear = polUwYear;
//	}
//
//	public String getRiskName() {
//		return riskName;
//	}
//
//	public void setRiskName(String riskName) {
//		this.riskName = riskName;
//	}
//
//	public String getPreviousInsurerName() {
//		return previousInsurerName;
//	}
//
//	public void setPreviousInsurerName(String previousInsurerName) {
//		this.previousInsurerName = previousInsurerName;
//	}
//
//	public Double getSumInsured() {
//		return sumInsured;
//	}
//
//	public void setSumInsured(Double sumInsured) {
//		this.sumInsured = sumInsured;
//	}
//
//	public String getPreExistingDiseases() {
//		return preExistingDiseases;
//	}
//
//	public void setPreExistingDiseases(String preExistingDiseases) {
//		this.preExistingDiseases = preExistingDiseases;
//	}	
//
//}