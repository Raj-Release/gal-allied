package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="MAS_SEC_USER_AUTOALLOCATION")
@NamedQueries({
	@NamedQuery(name="MasUserAutoAllocation.findByDoctor", query="SELECT m FROM MasUserAutoAllocation m where Lower(m.doctorId) = :doctorId"),
})
public class MasUserAutoAllocation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MAS_SEC_USER_AUTOALLOCATION_KEY_GENERATOR", sequenceName = "SEQ_DOCTOR_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MAS_SEC_USER_AUTOALLOCATION_KEY_GENERATOR" ) 
	@Column(name="DOCTOR_KEY", updatable=false)
	private Long key;
	
	@Column(name="DOCTOR_ID")
	private String doctorId;
	
	@Column(name="DOCTOR_NAME")
	private String docutorName;
	
	@Column(name="QUEUE_COUNT")
	private Integer queueCount;
	
	@Column(name="WINDOW_START_TIME")
	private String windowStartTime;
	
	@Column(name="WINDOW_END_TIME")
	private String windowEndTime;
		
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ELIGIBLE_ID", nullable = true)
	private MastersValue eligibleId;
	
	@Column(name="ELIGIBLE_DESC")
	private String eligibleDesc;
	
	@Column(name="ELIGIBLE_FLAG")
	private String eligibleFlag;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="USER_TYPE", nullable=true)
	private MastersValue userType;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getDocutorName() {
		return docutorName;
	}

	public void setDocutorName(String docutorName) {
		this.docutorName = docutorName;
	}

	public Integer getQueueCount() {
		return queueCount;
	}

	public void setQueueCount(Integer queueCount) {
		this.queueCount = queueCount;
	}

	public String getWindowStartTime() {
		return windowStartTime;
	}

	public void setWindowStartTime(String windowStartTime) {
		this.windowStartTime = windowStartTime;
	}

	public String getWindowEndTime() {
		return windowEndTime;
	}

	public void setWindowEndTime(String windowEndTime) {
		this.windowEndTime = windowEndTime;
	}

	public String getEligibleDesc() {
		return eligibleDesc;
	}

	public void setEligibleDesc(String eligibleDesc) {
		this.eligibleDesc = eligibleDesc;
	}

	public MastersValue getEligibleId() {
		return eligibleId;
	}

	public void setEligibleId(MastersValue eligibleId) {
		this.eligibleId = eligibleId;
	}

	public String getEligibleFlag() {
		return eligibleFlag;
	}

	public void setEligibleFlag(String eligibleFlag) {
		this.eligibleFlag = eligibleFlag;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
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

	public MastersValue getUserType() {
		return userType;
	}

	public void setUserType(MastersValue userType) {
		this.userType = userType;
	}
	
}
