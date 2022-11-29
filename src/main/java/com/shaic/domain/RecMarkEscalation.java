package com.shaic.domain;

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
@Table(name = "IMS_CLS_MKT_ESCALTION")
@NamedQueries({
	@NamedQuery(name = "RecMarkEscalation.findAll", query = "SELECT i FROM RecMarkEscalation i"),
	@NamedQuery(name = "RecMarkEscalation.findRecMarkEscCountByIntimationNo", query = "SELECT i FROM RecMarkEscalation i where i.intimationNumber = :intimationNumber")
})
public class RecMarkEscalation  extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="IMS_CLS_MKT_ESCALTION_KEY_GENERATOR", sequenceName = "SEQ_MKT_ESCALATION" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_MKT_ESCALTION_KEY_GENERATOR" )
	@Column(name="MKT_ESC_KEY")
	private Long key;
	
	@Column(name="REF_NUMBER")
	private String refNumber;
	
	@Column(name="INTIMATION_NUMBER")
	private String intimationNumber;
	
	@Column(name="ESCALATED_ROLE")
	private String escalatedRole;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ESCALATED_DATE")
	private Date escalatedDate;
	
	@Column(name="ESCALATED_BY")
	private String escalatedBy;
	
	@Column(name="ESCALATION_REASON")
	private Long escalationReason;
	
	@Column(name="ACTION_TAKEN")
	private Long actionTaken;
	
	@Column(name="DOCTOR_REMARKS")
	private String doctorRemarks;
	
	@Column(name="ESCALATION_REMARKS")
	private String escalationRemarks;
	
	@Column(name="STATUS_ID")
	private Long statusId;
	
	@Column(name="STAGE_ID")
	private Long stageId;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="LACK_UNDERSTAND_MKT_PERSONEL")
	private String mrkPersonnel;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public String getEscalatedRole() {
		return escalatedRole;
	}

	public void setEscalatedRole(String escalatedRole) {
		this.escalatedRole = escalatedRole;
	}

	public Date getEscalatedDate() {
		return escalatedDate;
	}

	public void setEscalatedDate(Date escalatedDate) {
		this.escalatedDate = escalatedDate;
	}

	public String getEscalatedBy() {
		return escalatedBy;
	}

	public void setEscalatedBy(String escalatedBy) {
		this.escalatedBy = escalatedBy;
	}

	public Long getEscalationReason() {
		return escalationReason;
	}

	public void setEscalationReason(Long escalationReason) {
		this.escalationReason = escalationReason;
	}

	public Long getActionTaken() {
		return actionTaken;
	}

	public void setActionTaken(Long actionTaken) {
		this.actionTaken = actionTaken;
	}

	public String getDoctorRemarks() {
		return doctorRemarks;
	}

	public void setDoctorRemarks(String doctorRemarks) {
		this.doctorRemarks = doctorRemarks;
	}

	public String getEscalationRemarks() {
		return escalationRemarks;
	}

	public void setEscalationRemarks(String escalationRemarks) {
		this.escalationRemarks = escalationRemarks;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public Long getStageId() {
		return stageId;
	}

	public void setStageId(Long stageId) {
		this.stageId = stageId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public String getMrkPersonnel() {
		return mrkPersonnel;
	}

	public void setMrkPersonnel(String mrkPersonnel) {
		this.mrkPersonnel = mrkPersonnel;
	}
	
	

}
