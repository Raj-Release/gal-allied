package com.shaic.domain.preauth;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.Intimation;
import com.shaic.domain.Policy;
import com.shaic.domain.Status;
/**
 * The persistent class for the IMS_CLS_HOSPITAL_ACKNOWLEDGE_T database table.
 * 
 */
@Entity
@Table(name="IMS_CLS_HOSPITAL_ACKNOWLEDGE")
@NamedQueries({
	@NamedQuery(name="HospitalAcknowledge.findAll", query="SELECT i FROM HospitalAcknowledge i"),
	@NamedQuery(name="HospitalAcknowledge.findByHospitalKey", query="SELECT o FROM HospitalAcknowledge o where o.key = :hospitalKey")
})
public class HospitalAcknowledge extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2136968709072672590L;
	
	@Id
	@SequenceGenerator(name="IMS_CLS_HOSPITAL_ACKNOWLEDGE_KEY_GENERATOR", sequenceName = "SEQ_HOSPITAL_ACKNOWLEDGE_KEY" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_HOSPITAL_ACKNOWLEDGE_KEY_GENERATOR" ) 
	@Column(name="HOSPITAL_ACKNOWLEDGE_KEY")
	private Long key;	
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

	@Column(name="CONTACT_PERSON_NAME")
	private String contactPersonName;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
    
	@Column(name="DESIGNATION")
	private String designation;

	//Column name changed form claimKey to cashlessKdy 
	@OneToOne
	@JoinColumn(name="CASHLESS_KEY", nullable=false,updatable=false)
	private Preauth preauth;

	@OneToOne(fetch=FetchType.EAGER, cascade = {CascadeType.ALL})
	@JoinColumn(name="INTIMATION_KEY", insertable=false,updatable=false,nullable=false,unique=true)
	private Intimation intimation;

	@OneToOne
	@JoinColumn(name="POLICY_KEY",updatable=false)
	private Policy policy;

//	@Column(name="MIGRATED_APPLICATION_ID")
//	private Long migratedApplicationId;

//	@Column(name="MIGRATED_CODE")
//	private String migratedCode;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	@Column(name="OFFICE_CODE",updatable=false)
	private String officeCode;

	@Column(name="REMARKS")
	private String remarks;

	@OneToOne
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;
	
	@OneToOne
	@JoinColumn(name="STAGE_ID",nullable=false)
	private Stage stage;
	
//	@Column(name="SUB_STATUS_ID")
//	private Long subStatusId;

//	@Column(name="STATUS_DATE")
//	private Timestamp statusDate;

//	@Column(name="VERSION")
//	private Long version;

	public HospitalAcknowledge() {
	}
	
	

	public void setKey(Long key) {
		this.key = key;
	}



	public Long getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getContactPersonName() {
		return this.contactPersonName;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getDesignation() {
		return this.designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getOfficeCode() {
		return this.officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
		
	public Long getKey()
	{
		return this.key;
	}

	public Preauth getPreauth() {
		return preauth;
	}

	public void setPreauth(Preauth preauth) {
		this.preauth = preauth;
	}

	public Intimation getIntimation() {
		return intimation;
	}

	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	
}