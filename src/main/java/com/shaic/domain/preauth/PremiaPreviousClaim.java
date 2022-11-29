
package com.shaic.domain.preauth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
* The Master for MAS_DOC_REJECT.
* 
*/
@Entity
@Table(name = "CLMV_POLICY_INTM")
@NamedQueries({
	@NamedQuery(name = "PremiaPreviousClaim.findAll", query = "SELECT i FROM PremiaPreviousClaim i"),
	@NamedQuery(name = "PremiaPreviousClaim.findByPolicyNo", query = "SELECT i FROM PremiaPreviousClaim i where i.policyNumber =:policyNo"),
	@NamedQuery(name = "PremiaPreviousClaim.findByPolAndRiskId", query = "SELECT i FROM PremiaPreviousClaim i where i.policyNumber =:policyNo AND i.riskId = :riskId"),
	@NamedQuery(name = "PremiaPreviousClaim.findForDuplicate", query = "SELECT i FROM PremiaPreviousClaim i where i.policyNumber =:policyNumber and i.riskId = :riskId and i.admittedDate =:admittedDate and lower(i.hospitalCode) =:hospitalCode"),
})

public class PremiaPreviousClaim implements Serializable {
	private static final long serialVersionUID = 3760145401215411454L;

	@Column(name = "POL_NO")
	private String policyNumber;

	
	@Column(name = "POL_SYS_ID")
	private Long policySysId;
	
	@Column(name = "END_NO_IDX")
	private Long endorsementNo;
	
	@Id
	@Column(name = "INTM_NO")
	private String intimationNumber;
	
	@Column(name = "RISK_ID")
	private Long riskId;
	
	@Column(name = "INSURED_PATIENT_NAME")
	private String insuredPatientName;
	
	@Column(name = "INTIMATOR_NAME")
	private String intimatorName;
	
	@Column(name = "ADMITTED_DT")
	private String admittedDate;
	
	@Column(name = "ADDRESS1")
	private String address1;
	
	@Column(name = "ADDRESS2")
	private String address2;
	
	@Column(name = "AREA")
	private String area;
	
	@Column(name = "CONTACT_NO")
	private String contactNo;
	
	@Column(name = "ATTENDERS_MOBILE_NO")
	private String attendersMobileNo;
	
	@Column(name = "SAVESUBMITTEDON")
	private String saveSubmittedOn;
	
	@Column(name = "MODIFIED_ON")
	private String modifiedOn;
	
	@Column(name = "CREATED_ON")
	private String createdOn;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "HOSPITAL_CODE")
	private String hospitalCode;

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public Long getPolicySysId() {
		return policySysId;
	}

	public void setPolicySysId(Long policySysId) {
		this.policySysId = policySysId;
	}

	public Long getEndorsementNo() {
		return endorsementNo;
	}

	public void setEndorsementNo(Long endorsementNo) {
		this.endorsementNo = endorsementNo;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public Long getRiskId() {
		return riskId;
	}

	public void setRiskId(Long riskId) {
		this.riskId = riskId;
	}

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	public String getIntimatorName() {
		return intimatorName;
	}

	public void setIntimatorName(String intimatorName) {
		this.intimatorName = intimatorName;
	}

	public String getAdmittedDate() {
		return admittedDate;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getAttendersMobileNo() {
		return attendersMobileNo;
	}

	public void setAttendersMobileNo(String attendersMobileNo) {
		this.attendersMobileNo = attendersMobileNo;
	}

	public String getSaveSubmittedOn() {
		return saveSubmittedOn;
	}

	public void setSaveSubmittedOn(String saveSubmittedOn) {
		this.saveSubmittedOn = saveSubmittedOn;
	}

	public String getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public void setAdmittedDate(String admittedDate) {
		this.admittedDate = admittedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	
	
	}
