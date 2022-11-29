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
//import javax.persistence.Transient;
//
//
///**
// * The persistent class for the IMS_TMP_INSURED_T database table.
// * 
// */
//@Entity
//@Table(name="IMS_TMP_INSURED")
//@NamedQueries( {
//@NamedQuery(name="TmpInsured.findAll", query="SELECT i FROM TmpInsured i"),
//@NamedQuery(name = "TmpInsured.findByPolicyNo",
//query = "select o from TmpInsured o where o.policyNo = :parentKey"),
//@NamedQuery(name = "TmpInsured.findByHealthcard", query="SELECT i from TmpInsured i where i.healthCardNumber Like  :healthCard "),
//@NamedQuery(name = "TmpInsured.findByInsuredName", query="SELECT i from TmpInsured i where i.insuredName Like  :insuredName "),
//@NamedQuery(name = "TmpInsured.findByInsuredNameAndDOB", query="SELECT i from TmpInsured i where  Lower(i.insuredName) Like :insuredName and i.insuredDateOfBirth = :dob "),
//@NamedQuery(name = "TmpInsured.findByDOB", query="SELECT i from TmpInsured i where  i.insuredDateOfBirth = :dob "),
//@NamedQuery(name = "TmpInsured.findByPolicyAndInsuredName", query = "select o from TmpInsured o where o.policyNo = :policeNo and o.insuredName =:insuredName and o.insuredDateOfBirth = :dob "),
//@NamedQuery(name = "TmpInsured.findByInsured", query = "select o from TmpInsured o where o.key = :key")})
//public class TmpInsured implements Serializable {
///**
//	 * 
//	 */
//	private static final long serialVersionUID = -6984819129697419052L;
//
//	//	@Id
////	@OneToOne
////	@JoinColumn(name="PRAI_SYS_ID", referencedColumnName="POL_SYS_ID")
//	@Transient
//	private TmpPolicy policy;
//	
//	@Id
//	@Column(name="INSURED_ID")	
//	private Long key;
//	
//	@Column(name="POLICY_NUMBER")
//	private String policyNo;
//
//	@Column(name="GENDER")
//	private String insuredGender;
//
//	@Column(name="RELATIONSHIP_WITH_PROPOSER")
//	private String relationshipwithInsuredId;
//
//	@Column(name="INSURED_NAME")
//	private String insuredName;
//
//	@Column(name="INSURED_AGE")
//	private String insuredAge;
//
//	@Temporal(TemporalType.DATE)
//	@Column(name="INSURED_DOB")
//	private Date insuredDateOfBirth;
//
//	@Column(name="HEALTH_CARD_NUMBER")
//	private String healthCardNumber;
//
//	@Column(name="SUM_INSURED")
//	private Double insuredSumInsured;
//	
//	@Column(name="REGISTERED_MOBILE_NUMBER")
//	private String registerdMobileNumber;
//	
//	@Column(name="CUMULATIVE_BONUS")
//	private Double cummulativeBonus;
//	
//	@Column(name = "RESTORED_SI")
//	private Double restoredSI;
//	
//	
//
//	@Column(name = "RECHARGED_SI")
//	private Double rechargedSI;
//	
//	@Column(name = "EMPLOYEE_ID")
//	private String employeeId;
//	
//	public TmpInsured() {
//	}
//
//	public Long getKey() {
//		return key;
//	}
//
//	public void setKey(Long key) {
//		this.key = key;
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
//	
//
//	public String getInsuredGender() {
//		return insuredGender;
//	}
//
//	public void setInsuredGender(String insuredGender) {
//		this.insuredGender = insuredGender;
//	}
//
//	public String getInsuredName() {
//		return insuredName;
//	}
//
//	public void setInsuredName(String insuredName) {
//		this.insuredName = insuredName;
//	}
//
//	public String getInsuredAge() {
//		return insuredAge;
//	}
//
//	public void setInsuredAge(String insuredAge) {
//		this.insuredAge = insuredAge;
//	}
//
//	public Date getInsuredDateOfBirth() {
//		return insuredDateOfBirth;
//	}
//
//	public void setInsuredDateOfBirth(Date insuredDateOfBirth) {
//		this.insuredDateOfBirth = insuredDateOfBirth;
//	}
//
//	public TmpPolicy getPolicy() {
//		return policy;
//	}
//
//	public void setPolicy(TmpPolicy policy) {
//		this.policy = policy;
//	}
//
//	public String getHealthCardNumber() {
//		return healthCardNumber;
//	}
//
//	public void setHealthCardNumber(String healthCardNumber) {
//		this.healthCardNumber = healthCardNumber;
//	}
//
//	public String getRegisterdMobileNumber() {
//		return registerdMobileNumber;
//	}
//
//	public void setRegisterdMobileNumber(String registerdMobileNumber) {
//		this.registerdMobileNumber = registerdMobileNumber;
//	}
//	
//	public Double getInsuredSumInsured() {
//		return insuredSumInsured;
//	}
//
//	public void setInsuredSumInsured(Double insuredSumInsured) {
//		this.insuredSumInsured = insuredSumInsured;
//	}
//		
//	public Double getCummulativeBonus() {
//		return cummulativeBonus;
//	}
//
//	public void setCummulativeBonus(Double cummulativeBonus) {
//		this.cummulativeBonus = cummulativeBonus;
//	}
//	
//	
//	public String getRelationshipwithInsuredId() {
//		return relationshipwithInsuredId;
//	}
//
//	public void setRelationshipwithInsuredId(String relationshipwithInsuredId) {
//		this.relationshipwithInsuredId = relationshipwithInsuredId;
//	}
//
//	public Double getRestoredSI() {
//		return restoredSI;
//	}
//
//	public void setRestoredSI(Double restoredSI) {
//		this.restoredSI = restoredSI;
//	}
//
//	public Double getRechargedSI() {
//		return rechargedSI;
//	}
//
//	public void setRechargedSI(Double rechargedSI) {
//		this.rechargedSI = rechargedSI;
//	}
//
//	public String getEmployeeId() {
//		return employeeId;
//	}
//
//	public void setEmployeeId(String employeeId) {
//		this.employeeId = employeeId;
//	}
//	
//	@Override
//	public boolean equals(Object obj) {
//		if (obj == null) {
//			return false;
//		}
//		if (getClass() != obj.getClass()) {
//			return false;
//		}
//		if(this.getKey() == null || ((TmpInsured) obj).getKey() == null ) {
//			return false;
//		}
//		return this.getKey().equals(((TmpInsured) obj).getKey());
//	}
//	
//	
//
//	@Override
//    public int hashCode() {
//        if (key != null) {
//            return key.hashCode();
//        } else {
//            return super.hashCode();
//        }
//    }
//
//	@Override
//		public String toString() {
//			return this.insuredName;
//		}
//}