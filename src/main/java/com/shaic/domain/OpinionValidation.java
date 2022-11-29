package com.shaic.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.preauth.Stage;

@Entity
@Table(name="IMS_CLS_OPINION")
@NamedQueries({
	@NamedQuery(name ="OpinionValidation.findByKey",query="SELECT o FROM OpinionValidation o WHERE o.key = :primaryKey"),	
	@NamedQuery(name="OpinionValidation.findByAssignedUser", query="SELECT o.createdBy FROM OpinionValidation o where o.assignedDocName = :assignedUserId and o.opinionStatus= :status"),
	@NamedQuery(name ="OpinionValidation.findByIntimationNo",query="SELECT o FROM OpinionValidation o WHERE o.intimationNumber = :intimationNo"),
})
public class OpinionValidation extends AbstractEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="IMS_CLS_OPINION_KEY_GENERATOR", sequenceName = "SEQ_OPINION_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_OPINION_KEY_GENERATOR" )
	@Column(name = "OPINION_KEY")
	private Long key;
	
	@Column(name = "INTIMATION_NUMBER")
	private String intimationNumber;
	
	@Column(name = "CPU_CODE")
	private Long cpuCode;
	
	@Column(name = "CLAIM_STAGE")
	private String claimStage;	
	
	@OneToOne
	@JoinColumn(name="STAGE_ID",nullable=true)
	private Stage stage;
	
	@OneToOne
	@JoinColumn(name="STATUS_ID",nullable=true)
	private Status status;	
	
	@Column(name= "ASSIGNED_DATE")
	private Timestamp assignedDate;
	
	@Column(name = "ASSIGNED_ROLE_CODE")
	private String assignedRoleBy;
	
	@Column(name = "ASSIGNED_USER_ID")
	private String assignedDocName;
	
	@Column(name = "OPINION_STATUS")
	private Long opinionStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OPINION_STATUS_DATE")
	private Date opinionStatusDate;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name= "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "UPDATED_BY_USER_ID")
	private String updatedBy;
	
	@Column(name = "UPDATED_BY_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDateTime;
	
	@Column(name = "UPDATED_REMARKS")
	private String updatedRemarks;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDateTime;
	
	@Column(name = "APPROVE_REJECT_REMARKS")
	private String approveRejectRemarks;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public Long getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getClaimStage() {
		return claimStage;
	}

	public void setClaimStage(String claimStage) {
		this.claimStage = claimStage;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Timestamp getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(Timestamp assignedDate) {
		this.assignedDate = assignedDate;
	}


	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public String getAssignedDocName() {
		return assignedDocName;
	}

	public void setAssignedDocName(String assignedDocName) {
		this.assignedDocName = assignedDocName;
	}

	public Long getOpinionStatus() {
		return opinionStatus;
	}

	public void setOpinionStatus(Long opinionStatus) {
		this.opinionStatus = opinionStatus;
	}

	public Date getOpinionStatusDate() {
		return opinionStatusDate;
	}

	public void setOpinionStatusDate(Date opinionStatusDate) {
		this.opinionStatusDate = opinionStatusDate;
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
	
	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(Date updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	public String getUpdatedRemarks() {
		return updatedRemarks;
	}

	public void setUpdatedRemarks(String updatedRemarks) {
		this.updatedRemarks = updatedRemarks;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDateTime() {
		return modifiedDateTime;
	}

	public void setModifiedDateTime(Timestamp modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}

	public String getApproveRejectRemarks() {
		return approveRejectRemarks;
	}

	public void setApproveRejectRemarks(String approveRejectRemarks) {
		this.approveRejectRemarks = approveRejectRemarks;
	}

	public String getAssignedRoleBy() {
		return assignedRoleBy;
	}

	public void setAssignedRoleBy(String assignedRoleBy) {
		this.assignedRoleBy = assignedRoleBy;
	}	
	
	
}
