package com.shaic.domain.preauth;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
import com.shaic.domain.MastersValue;
import com.shaic.domain.OPClaim;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;

/**
 * The persistent class for the IMS_CLS_STAGE_INFORMATION_T database table.
 * 
 */
@Entity
@Table(name = "IMS_CLS_OP_STAGE_INFORMATION")
@NamedQueries({
	@NamedQuery(name = "OPStageInformation.findAll", query = "SELECT i FROM OPStageInformation i"),
	@NamedQuery(name = "OPStageInformation.findByCashlessKey", query = "SELECT i FROM OPStageInformation i where i.claim.key = :claimkey and (i.preauth is not null and i.preauth.key = :preauthKey) order by i.key asc"),
	@NamedQuery(name = "OPStageInformation.findCashlessByStageInfoKey", query = "SELECT i FROM OPStageInformation i where i.key = :stageInfoKey order by i.key desc"),
	@NamedQuery(name = "OPStageInformation.findByRodKey", query = "SELECT i FROM OPStageInformation i where i.claim.key = :claimkey and ((i.reimbursement is not null and i.reimbursement.key = :rodKey) or i.stage.key =:stageKey) order by i.key asc"),
	@NamedQuery(name = "OPStageInformation.findByReimbStageNStatus", query = "SELECT i FROM OPStageInformation i where (i.reimbursement is not null and i.reimbursement.key = :rodKey) and i.stage.key = :stageKey and i.status.key = :statusKey"),
	@NamedQuery(name = "OPStageInformation.findByStageNStatus", query = "SELECT i FROM OPStageInformation i where i.stage.key = :stageKey and i.status.key = :statusKey"), 
	@NamedQuery(name = "OPStageInformation.findByClaimKey", query = "SELECT i FROM OPStageInformation i where i.claim.key = :claimkey and ((i.preauth is not null) or (i.preauth is null and i.status.key = 172)) order by i.key asc"),
	@NamedQuery(name = "OPStageInformation.findClaimByStatus", query = "SELECT i FROM OPStageInformation i where i.claim.key = :claimkey and i.stage.key = :stageKey and i.status.key = :statusKey order by i.key desc"),
	@NamedQuery(name = "OPStageInformation.findClaimByStatusKey", query = "SELECT i FROM OPStageInformation i where i.claim.key = :claimkey and i.status.key = :statusKey order by i.key desc"),
	@NamedQuery(name = "OPStageInformation.findAllByClaimKey", query = "SELECT i FROM OPStageInformation i where i.claim.key = :claimkey order by i.key asc"),
	@NamedQuery(name = "OPStageInformation.findAllByIntimationKey", query = "SELECT i FROM OPStageInformation i where i.intimation.key = :intimationKey order by i.key asc"),
	@NamedQuery(name = "OPStageInformation.findStageUserByClaimKey", query = "SELECT i FROM OPStageInformation i where i.claim.key = :claimKey and i.status.stage.key = :stageKey and i.status.key = :statusKey "),
	@NamedQuery(name = "OPStageInformation.findExecutiveSummary", query = "SELECT s.status.key, count(s.status.key) FROM OPStageInformation s WHERE Lower(s.createdBy) like :createdBy and s.createdDate >= :fromDate and s.createdDate <= :toDate and s.status.key in ( :statusList ) group by s.status.key order by s.status.key asc"),
	@NamedQuery(name = "OPStageInformation.findByStatus", query = "SELECT r.claim.key, r.key , r.status.key , r.stage.key , r.claim.intimation.cpuCode.cpuCode, r.claim.claimType.value FROM OPStageInformation r  WHERE  r.stage.key >= :fromStage and  r.stage.key <= :toStage and  r.claim.intimation.cpuCode.cpuCode =:cpuCode group by r.claim.key, r.key , r.status.key , r.stage.key , r.claim.intimation.cpuCode.cpuCode , r.claim.claimType.value order by  r.claim.intimation.cpuCode.cpuCode asc"),
	@NamedQuery(name = "OPStageInformation.findByStatusId", query = "SELECT r.claim.key, r.key , r.status.key , r.stage.key , r.claim.intimation.cpuCode.cpuCode, r.claim.claimType.value FROM OPStageInformation r  WHERE  r.stage.key >= :fromStage and  r.stage.key <= :toStage and r.createdDate >= :fromDate and  r.createdDate <= :endDate and r.claim.intimation.cpuCode.cpuCode =:cpuCode group by r.claim.key, r.key , r.status.key , r.stage.key , r.claim.intimation.cpuCode.cpuCode , r.claim.claimType.value order by  r.claim.intimation.cpuCode.cpuCode asc"),
	@NamedQuery(name = "OPStageInformation.findByStatusListAndReimbursementKey", query = "SELECT s FROM OPStageInformation s WHERE (s.reimbursement is not null and s.reimbursement.key = :reimbursementKey) and s.status.key in ( :statusList ) order by s.key desc"),
	@NamedQuery(name = "OPStageInformation.findByStatusListAndCashlessKey", query = "SELECT s FROM OPStageInformation s WHERE (s.preauth is not null and s.preauth.key = :cashlessKey) and s.status.key in ( :statusList ) order by s.key desc")

})
public class OPStageInformation extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="IMS_CLS_OP_STAGE_INFORMATION_KEY_GENERATOR", sequenceName = "SEQ_OP_STAGE_INFO_ID", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_OP_STAGE_INFORMATION_KEY_GENERATOR" )
	@Column(name="STAGE_INFORMATION_KEY")
	private Long key;

	@OneToOne
	@JoinColumn(name = "INTIMATION_KEY", nullable = false)
	private OPIntimation intimation;
	
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
	private OPClaim claim;
	
	@OneToOne
	@JoinColumn(name = "CASHLESS_KEY")
	private Preauth preauth;
	
	@OneToOne
	@JoinColumn(name = "REIMBURSEMENT_KEY")
	private Reimbursement  reimbursement;

//	@Column(name = "ACTIVE_STATUS_DATE")
//	private String activeStatusDate;

//	@Column(name = "SUB_STATUS_ID")
//	private String subStatusId;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public OPIntimation getIntimation() {
		return intimation;
	}

	public void setIntimation(OPIntimation intimation) {
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

	public OPClaim getClaim() {
		return claim;
	}

	public void setClaim(OPClaim claim) {
		this.claim = claim;
	}
	
	public Preauth getPreauth() {
		return preauth;
	}

	public void setPreauth(Preauth preauth) {
		this.preauth = preauth;
	}

	public Reimbursement getReimbursement() {
		return reimbursement;
	}

	public void setReimbursement(Reimbursement reimbursement) {
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
}