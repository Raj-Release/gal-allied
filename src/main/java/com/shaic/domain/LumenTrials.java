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

import com.shaic.arch.fields.dto.AbstractEntity;

/**
 * @author karthikeyan.r
 * The persistent class for the IMS_CLS_LUMEN_TRIALS database table.
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name="IMS_CLS_LUMEN_TRIALS")
@NamedQueries({
	@NamedQuery(name="LumenTrials.findAll", query="SELECT m FROM LumenTrials m"),
	@NamedQuery(name="LumenTrials.findByLumenKey", query="SELECT m FROM LumenTrials m where m.lumenRequest.key = :lumenKey order by m.createdDate asc"),
	@NamedQuery(name="LumenTrials.findByLumenKeyWithStatus", query="SELECT m FROM LumenTrials m where m.lumenRequest.key = :lumenKey  and m.trialstatus = :lumenStatus and m.stage = :trailStage")
})
public class LumenTrials extends AbstractEntity implements Serializable  {

	@Id
	@SequenceGenerator(name="IMS_CLS_LUMEN_TRIALS_GENERATOR", sequenceName = "SEQ_LUMEN_TRIALS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_LUMEN_TRIALS_GENERATOR")
	@Column(name="LUMEN_TRIALS_KEY")
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INTIMATION_KEY", nullable=false)
	private Intimation intimation;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="LUMEN_REQUEST_KEY", nullable=false)
	private LumenRequest lumenRequest;
	
	@Column(name = "STATUS")
	private String trialstatus;
	
	@Column(name = "STATUS_REMARKS")
	private String statusRemarks;
	
	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name = "STAGE")
	private String stage;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	public Long getKey() {
		return key;
	}

	public Intimation getIntimation() {
		return intimation;
	}

	public LumenRequest getLumenRequest() {
		return lumenRequest;
	}

	public String getTrialstatus() {
		return trialstatus;
	}

	public String getStatusRemarks() {
		return statusRemarks;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public String getStage() {
		return stage;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}

	public void setLumenRequest(LumenRequest lumenRequest) {
		this.lumenRequest = lumenRequest;
	}

	public void setTrialstatus(String trialstatus) {
		this.trialstatus = trialstatus;
	}

	public void setStatusRemarks(String statusRemarks) {
		this.statusRemarks = statusRemarks;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}
