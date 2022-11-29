package com.shaic.domain.preauth;

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

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="IMS_CLS_TREATING_DOCTOR_DTLS")

@NamedQueries({
	@NamedQuery(name = "TreatingDoctorDetails.findAll", query = "SELECT i FROM TreatingDoctorDetails i"),
	@NamedQuery(name="TreatingDoctorDetails.findByClaimKey", query="SELECT i FROM TreatingDoctorDetails i where i.claimKey = :claimKey"),
	@NamedQuery(name="TreatingDoctorDetails.findByTransactionKey", query="SELECT i FROM TreatingDoctorDetails i where i.transactionKey = :transactionKey"),
	@NamedQuery(name="TreatingDoctorDetails.findByKey", query="SELECT i FROM TreatingDoctorDetails i where i.key = :primarykey")

})

public class TreatingDoctorDetails extends AbstractEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "IMS_CLS_SEQ_DR_DTLS_KEY_GENERATOR", sequenceName = "SEQ_DR_DTLS_KEY", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMS_CLS_SEQ_DR_DTLS_KEY_GENERATOR")
	@Column(name = "DR_DTLS_KEY")
	private Long key;
	
	@Column(name = "CLAIM_KEY")
	private Long claimKey;
	
	@Column(name = "TRANSACTION_KEY")
	private Long transactionKey;
	
	@Column(name = "TREATING_DOCTOR_NAME")
	private String doctorName;
	
	@Column(name = "TREATING_DR_QUALIFICATION")
	private String doctorQualification;

	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "OLD_TREATING_DR_NAME")
	private String oldDoctorName;
	
	@Column(name = "OLD_TREATING_DR_QUALIFICATION")
	private String oldQualification;
	
	@Column(name = "DC_DOCTOR_FLAG")
	private String dcDoctorFlag;
	
	@Column(name="TREATING_DOCTOR_SIGNATURE")
	private String treatingDoctorSignature;
	

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Long getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDoctorQualification() {
		return doctorQualification;
	}

	public void setDoctorQualification(String doctorQualification) {
		this.doctorQualification = doctorQualification;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getOldDoctorName() {
		return oldDoctorName;
	}

	public void setOldDoctorName(String oldDoctorName) {
		this.oldDoctorName = oldDoctorName;
	}

	public String getOldQualification() {
		return oldQualification;
	}

	public void setOldQualification(String oldQualification) {
		this.oldQualification = oldQualification;
	}

	public String getDcDoctorFlag() {
		return dcDoctorFlag;
	}

	public void setDcDoctorFlag(String dcDoctorFlag) {
		this.dcDoctorFlag = dcDoctorFlag;
	}

	public String getTreatingDoctorSignature() {
		return treatingDoctorSignature;
	}

	public void setTreatingDoctorSignature(String treatingDoctorSignature) {
		this.treatingDoctorSignature = treatingDoctorSignature;
	}

}
