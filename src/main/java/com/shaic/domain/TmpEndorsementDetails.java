//package com.shaic.domain;
//
//import java.io.Serializable;
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//
//@Entity
//@Table(name="IMS_TMP_ENDORSEMENT_DETAILS")
//@NamedQueries( {
//	@NamedQuery(name="TmpEndorsementDetails.findAll", query="SELECT i FROM TmpEndorsementDetails i"),
//	@NamedQuery(name = "TmpEndorsementDetails.findByPolicyNo",query = "select o from TmpEndorsementDetails o where o.policyNumber like :policyNumber")
//})
//
//public class TmpEndorsementDetails implements Serializable{
//
//	@Id
//	@Column(name= "POLICY_NUMBER")
//	private String policyNumber;
//
//	@Column(name= "ENDORSEMENT_NUMBER")
//	private String endorsementNumber;
//	
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name= "ENDORSEMENT_DATE")
//	private Date endoresementDate;
//	
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name= "EFFECTIVE_FROM_DATE")
//	private Date effectiveFromDate;
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name= "EFFECTIVE_TO_DATE")
//    private Date effectiveToDate;
//	
//	@Column(name= "ENDORSEMENT_TYPE")
//	private String endorsementType;
//	
//	@Column(name= "ENDORSEMENT_TEXT")
//	private String endorsementText;
//	
//	@Column(name= "SUM_INSURED")
//	private Double sumInsured;
//	  
//	@Column(name= "REVISED_SUM_INSURED")
//	private Double revisedSumInsured;
//	
//	@Column(name= "PREMIUM")
//	private Double premium;
//
//	public String getPolicyNumber() {
//		return policyNumber;
//	}
//
//	public void setPolicyNumber(String policyNumber) {
//		this.policyNumber = policyNumber;
//	}
//
//	public String getEndorsementNumber() {
//		return endorsementNumber;
//	}
//
//	public void setEndorsementNumber(String endorsementNumber) {
//		this.endorsementNumber = endorsementNumber;
//	}
//
//	public Date getEndoresementDate() {
//		return endoresementDate;
//	}
//
//	public void setEndoresementDate(Date endoresementDate) {
//		this.endoresementDate = endoresementDate;
//	}
//
//	public Date getEffectiveFromDate() {
//		return effectiveFromDate;
//	}
//
//	public void setEffectiveFromDate(Date effectiveFromDate) {
//		this.effectiveFromDate = effectiveFromDate;
//	}
//
//	public Date getEffectiveToDate() {
//		return effectiveToDate;
//	}
//
//	public void setEffectiveToDate(Date effectiveToDate) {
//		this.effectiveToDate = effectiveToDate;
//	}
//
//	public String getEndorsementType() {
//		return endorsementType;
//	}
//
//	public void setEndorsementType(String endorsementType) {
//		endorsementType = endorsementType;
//	}
//
//	public String getEndorsementText() {
//		return endorsementText;
//	}
//
//	public void setEndorsementText(String endorsementText) {
//		this.endorsementText = endorsementText;
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
//	public Double getRevisedSumInsured() {
//		return revisedSumInsured;
//	}
//
//	public void setRevisedSumInsured(Double revisedSumInsured) {
//		this.revisedSumInsured = revisedSumInsured;
//	}
//
//	public Double getPremium() {
//		return premium;
//	}
//
//	public void setPremium(Double premium) {
//		this.premium = premium;
//	}
//	
//}
