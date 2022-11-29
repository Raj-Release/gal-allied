package com.shaic.domain.omp;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPDocAcknowledgement;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;

/**
 * The persistent class for the IMS_CLS_STAGE_INFORMATION_T database table.
 * 
 */
@Entity
@Table(name = "IMS_CLS_OMP_STAGE_INFORMATION")
@NamedQueries({
	@NamedQuery(name = "OMPStageInformation.findAll", query = "SELECT i FROM OMPStageInformation i"),
	@NamedQuery(name = "OMPStageInformation.findByStageInfoKey", query = "SELECT i FROM OMPStageInformation i where i.key = :stageInfoKey order by i.key desc"),
	@NamedQuery(name = "OMPStageInformation.findByRodKey", query = "SELECT i FROM OMPStageInformation i where i.claim.key = :claimkey and i.reimbursement is not null and i.reimbursement.key = :rodKey order by i.key asc"),
	@NamedQuery(name = "OMPStageInformation.findByReimbStageNStatus", query = "SELECT i FROM OMPStageInformation i where (i.reimbursement is not null and i.reimbursement.key = :rodKey) and i.stage.key = :stageKey and i.status.key = :statusKey"),
	@NamedQuery(name = "OMPStageInformation.findByStageNStatus", query = "SELECT i FROM OMPStageInformation i where i.stage.key = :stageKey and i.status.key = :statusKey"), 
	@NamedQuery(name = "OMPStageInformation.findClaimByStatus", query = "SELECT i FROM OMPStageInformation i where i.claim.key = :claimkey and i.stage.key = :stageKey and i.status.key = :statusKey order by i.key desc"),
	@NamedQuery(name = "OMPStageInformation.findClaimByStatusKey", query = "SELECT i FROM OMPStageInformation i where i.claim.key = :claimkey and i.status.key = :statusKey order by i.key desc"),
	@NamedQuery(name = "OMPStageInformation.findAllByClaimKey", query = "SELECT i FROM OMPStageInformation i where i.claim.key = :claimkey order by i.key asc"),
	@NamedQuery(name = "OMPStageInformation.findAllByIntimationKey", query = "SELECT i FROM OMPStageInformation i where i.intimation.key = :intimationKey order by i.key asc"),
	@NamedQuery(name = "OMPStageInformation.findStageUserByClaimKey", query = "SELECT i FROM OMPStageInformation i where i.claim.key = :claimKey and i.status.stage.key = :stageKey and i.status.key = :statusKey "),
	@NamedQuery(name = "OMPStageInformation.findExecutiveSummary", query = "SELECT s.stage.key, count(s.stage.key) FROM OMPStageInformation s WHERE Lower(s.createdBy) like :createdBy and s.createdDate >= :fromDate and s.createdDate <= :toDate and s.status.key in ( :statusList ) group by s.stage.key order by s.stage.key asc"),
	@NamedQuery(name = "OMPStageInformation.findByStatus", query = "SELECT r.claim.key, r.key , r.status.key , r.stage.key , r.claim.intimation.cpuCode.cpuCode, r.claim.claimType.value FROM OMPStageInformation r  WHERE  r.stage.key >= :fromStage and  r.stage.key <= :toStage and  r.claim.intimation.cpuCode.cpuCode =:cpuCode group by r.claim.key, r.key , r.status.key , r.stage.key , r.claim.intimation.cpuCode.cpuCode , r.claim.claimType.value order by  r.claim.intimation.cpuCode.cpuCode asc"),
	@NamedQuery(name = "OMPStageInformation.findByStatusId", query = "SELECT r.claim.key, r.key , r.status.key , r.stage.key , r.claim.intimation.cpuCode.cpuCode, r.claim.claimType.value FROM OMPStageInformation r  WHERE  r.stage.key >= :fromStage and  r.stage.key <= :toStage and r.createdDate >= :fromDate and  r.createdDate <= :endDate and r.claim.intimation.cpuCode.cpuCode =:cpuCode group by r.claim.key, r.key , r.status.key , r.stage.key , r.claim.intimation.cpuCode.cpuCode , r.claim.claimType.value order by  r.claim.intimation.cpuCode.cpuCode asc"),
	@NamedQuery(name = "OMPStageInformation.findByStatusListAndReimbursementKey", query = "SELECT s FROM OMPStageInformation s WHERE (s.reimbursement is not null and s.reimbursement.key = :reimbursementKey) and s.status.key in ( :statusList ) order by s.key desc"),

})
public class OMPStageInformation extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="STAGE_INFORMATION_KEY")
	private Long key;

	@OneToOne
	@JoinColumn(name = "INTIMATION_KEY", nullable = false)
	private OMPIntimation intimation;
	
	/*
	 * Data inconsistency problem raised when joining the Claim table here in
	 * the View Claim history. So commenting the below code by srikanth
	 */

	@ManyToOne(fetch=FetchType.EAGER )
	@JoinColumn(name="CLAIM_TYPE_ID",  nullable=true)
	private MastersValue claimType;

	@Column(name = "ACTIVE_STATUS")
	private String activeStatus;

	@OneToOne
	@JoinColumn(name = "STAGE_ID", nullable = false)
	private Stage stage;

	@OneToOne
	@JoinColumn(name = "STATUS_ID", nullable = false)
	private Status status;

	@Column(name = "OFFICE_CODE")
	private String officeCode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "STATUS_REMARKS")
	private String statusRemarks;
	
//	@Column(name = "CREATED_DATE")
//	private Timestamp dmDate; 
	
	@OneToOne
	@JoinColumn(name = "CLAIM_KEY", nullable = false)
	private OMPClaim claim;
	
//	@OneToOne
//	@JoinColumn(name = "CASHLESS_KEY")
//	private Preauth preauth;
	
	@OneToOne
	@JoinColumn(name = "REIMBURSEMENT_KEY")
	private OMPReimbursement  reimbursement;

//	@Column(name = "ACTIVE_STATUS_DATE")
//	private String activeStatusDate;

//	@Column(name = "SUB_STATUS_ID")
//	private String subStatusId;
	
	@Column(name = "CLASIFICATION")
	private String classificationId;
	
	

	@Column(name = "SUB_CLASIFICATION")
	private String subClassificationId;
	
	@Column(name = "DOC_RECD_FM")
	private String classiDocumentRecivedFmId;
	
	@Column(name = "ROD_TYPE")
	private String rodType;
	
	@OneToOne
	@JoinColumn(name = "DOC_ACKNOWLEDGEMENT_KEY")
	private OMPDocAcknowledgement  acknowledgement;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public OMPIntimation getIntimation() {
		return intimation;
	}

	public void setIntimation(OMPIntimation intimation) {
		this.intimation = intimation;
	}	

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getStatusRemarks() {
		return statusRemarks;
	}

	public void setStatusRemarks(String statusRemarks) {
		this.statusRemarks = statusRemarks;
	}

	public MastersValue getClaimType() {
		return claimType;
	}

	public void setClaimType(MastersValue claimType) {
		this.claimType = claimType;
	}

//	public Timestamp getDmDate() {
//		return dmDate;
//	}
//
//	public void setDmDate(Timestamp dmDate) {
//		this.dmDate = dmDate;
//	}

	public OMPClaim getClaim() {
		return claim;
	}

	public void setClaim(OMPClaim claim) {
		this.claim = claim;
	}
	
//	public Preauth getPreauth() {
//		return preauth;
//	}
//
//	public void setPreauth(Preauth preauth) {
//		this.preauth = preauth;
//	}

	public OMPReimbursement getReimbursement() {
		return reimbursement;
	}

	public void setReimbursement(OMPReimbursement reimbursement) {
		this.reimbursement = reimbursement;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public String getClassificationId() {
		return classificationId;
	}

	public void setClassificationId(String classificationId) {
		this.classificationId = classificationId;
	}

	public String getSubClassificationId() {
		return subClassificationId;
	}

	public void setSubClassificationId(String subClassificationId) {
		this.subClassificationId = subClassificationId;
	}

	public String getClassiDocumentRecivedFmId() {
		return classiDocumentRecivedFmId;
	}

	public void setClassiDocumentRecivedFmId(String classiDocumentRecivedFmId) {
		this.classiDocumentRecivedFmId = classiDocumentRecivedFmId;
	}

	public String getRodType() {
		return rodType;
	}

	public void setRodType(String rodType) {
		this.rodType = rodType;
	}

	public OMPDocAcknowledgement getAcknowledgement() {
		return acknowledgement;
	}

	public void setAcknowledgement(OMPDocAcknowledgement acknowledgement) {
		this.acknowledgement = acknowledgement;
	}
	
}