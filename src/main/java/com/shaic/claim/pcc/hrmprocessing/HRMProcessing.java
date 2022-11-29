package com.shaic.claim.pcc.hrmprocessing;

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
import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Stage;

@Entity
@Table(name = "IMS_CLS_HRM_DIV_DISCHARGE_DOC")
@NamedQueries({
		//@NamedQuery(name = "HRMProcessing.findByLoginId", query = "SELECT o FROM HRMProcessing o where o.intimation.key = :intimationKey and o.preauth.key = :cashlessKey and o.hrmDoctorCode = :hrmDoctorCode and o.status.key is not null ")
	@NamedQuery(name = "HRMProcessing.findByLoginId", query = "SELECT o FROM HRMProcessing o where o.intimation.key = :intimationKey and o.preauth.key = :cashlessKey and Lower(o.hrmDoctorCode) LIKE :hrmDoctorCode and o.status.key =:statusKey and o.activeFlag = 'Y'"),
	@NamedQuery(name = "HRMProcessing.findById", query = "SELECT o FROM HRMProcessing o where o.key = :hrmKey and o.activeFlag = 'Y'"),
	@NamedQuery(name = "HRMProcessing.findByIntimationKey", query = "SELECT o FROM HRMProcessing o where o.intimation.key = :intimationKey and o.activeFlag = 'Y'")

})
public class HRMProcessing extends AbstractEntity {

	private static final long serialVersionUID = -3873349889369766466L;

	@Id
	@SequenceGenerator(name="IMS_HRM_GENERATOR", sequenceName = "SEQ_HRM_DIV_DISCHARGE_DOC", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_HRM_GENERATOR" ) 
	@Column(name="HRM_DIV_DISCHARGE_DOC_KEY", updatable=false)
	
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INTIMATION_KEY", nullable = false)
	private Intimation intimation;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLAIM_KEY", nullable = false)
	private Claim claim;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CASHLESS_KEY", nullable = false)
	private Preauth preauth;
	
	@Column(name="HRM_DOCTOR_CODE")
	private String hrmDoctorCode;
	
	@Column(name="ZONAL_HEAD_CODE")
	private String zonalHeadCode;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STAGE_ID", nullable = false)
	private Stage stage;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID", nullable = false)
	private Status status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HRM_COMPLETED_DT")
	private Date hrmCompletedDate;
	
	@Column(name="HRM_REMARKS")
	private String hrmRemarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ZONAL_COMPLETED_DT")
	private Date zonalCompletedDate;
	
	@Column(name="ZONAL_REMARKS")
	private String zonalRemarks;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="ACTIVE_FLAG")
	private String activeFlag;

	@Column(name="ESCLATION_LEVEL")
	private String esclationLevel;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Intimation getIntimation() {
		return intimation;
	}

	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	public String getHrmDoctorCode() {
		return hrmDoctorCode;
	}

	public void setHrmDoctorCode(String hrmDoctorCode) {
		this.hrmDoctorCode = hrmDoctorCode;
	}

	public String getZonalHeadCode() {
		return zonalHeadCode;
	}

	public void setZonalHeadCode(String zonalHeadCode) {
		this.zonalHeadCode = zonalHeadCode;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getHrmCompletedDate() {
		return hrmCompletedDate;
	}

	public void setHrmCompletedDate(Date hrmCompletedDate) {
		this.hrmCompletedDate = hrmCompletedDate;
	}

	public String getHrmRemarks() {
		return hrmRemarks;
	}

	public void setHrmRemarks(String hrmRemarks) {
		this.hrmRemarks = hrmRemarks;
	}

	public Date getZonalCompletedDate() {
		return zonalCompletedDate;
	}

	public void setZonalCompletedDate(Date zonalCompletedDate) {
		this.zonalCompletedDate = zonalCompletedDate;
	}

	public String getZonalRemarks() {
		return zonalRemarks;
	}

	public void setZonalRemarks(String zonalRemarks) {
		this.zonalRemarks = zonalRemarks;
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

	public String getModifedBy() {
		return modifedBy;
	}

	public void setModifedBy(String modifedBy) {
		this.modifedBy = modifedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Preauth getPreauth() {
		return preauth;
	}

	public void setPreauth(Preauth preauth) {
		this.preauth = preauth;
	}

	public String getEsclationLevel() {
		return esclationLevel;
	}

	public void setEsclationLevel(String esclationLevel) {
		this.esclationLevel = esclationLevel;
	}
}
