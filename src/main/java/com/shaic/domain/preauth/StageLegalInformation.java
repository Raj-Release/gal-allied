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
import com.shaic.domain.Status;

/**
 * The persistent class for the IMS_CLS_STAGE_INFORMATION_T database table.
 * 
 */
@Entity
@Table(name = "IMS_CLS_LEGAL_STG_INFORMATION")
@NamedQueries({
	@NamedQuery(name = "StageLegalInformation.findAll", query = "SELECT i FROM StageLegalInformation i"),
	@NamedQuery(name = "StageLegalInformation.findByCashlessKey", query = "SELECT i FROM StageLegalInformation i where i.claim.key = :claimkey and i.cashlessKey = :preauthKey order by i.key asc"),
	@NamedQuery(name = "StageLegalInformation.findCashlessByStageInfoKey", query = "SELECT i FROM StageLegalInformation i where i.key = :stageInfoKey order by i.key desc"),
	@NamedQuery(name = "StageLegalInformation.findByRodKey", query = "SELECT i FROM StageLegalInformation i where i.claim.key = :claimkey and (i.reimbursementKey = :rodKey or i.stage.key =:stageKey) order by i.key asc"),
	@NamedQuery(name = "StageLegalInformation.findByReimbStageNStatus", query = "SELECT i FROM StageLegalInformation i where i.reimbursementKey = :rodKey and i.stage.key = :stageKey and i.status.key = :statusKey"),
	@NamedQuery(name = "StageLegalInformation.findByStageNStatus", query = "SELECT i FROM StageLegalInformation i where i.stage.key = :stageKey and i.status.key = :statusKey"), 
	@NamedQuery(name = "StageLegalInformation.findByClaimKey", query = "SELECT i FROM StageLegalInformation i where i.claim.key = :claimkey order by i.key asc"),
	@NamedQuery(name = "StageLegalInformation.findClaimByStatus", query = "SELECT i FROM StageLegalInformation i where i.claim.key = :claimkey and i.stage.key = :stageKey and i.status.key = :statusKey order by i.key desc"),
	@NamedQuery(name = "StageLegalInformation.findClaimByStatusKey", query = "SELECT i FROM StageLegalInformation i where i.claim.key = :claimkey and i.status.key = :statusKey order by i.key desc"),
	@NamedQuery(name = "StageLegalInformation.findAllByClaimKey", query = "SELECT i FROM StageLegalInformation i where i.claim.key = :claimkey order by i.key asc"),
	@NamedQuery(name = "StageLegalInformation.findStageUserByClaimKey", query = "SELECT i FROM StageLegalInformation i where i.claim.key = :claimKey and i.status.stage.key = :stageKey and i.status.key = :statusKey "),
	@NamedQuery(name = "StageLegalInformation.findExecutiveSummary", query = "SELECT s.stage.key, count(s.stage.key) FROM StageLegalInformation s WHERE Lower(s.createdBy) like :createdBy and s.createdDate >= :fromDate and s.createdDate <= :toDate and s.status.key in ( :statusList ) group by s.stage.key order by s.stage.key asc"),
	@NamedQuery(name = "StageLegalInformation.findByStatus", query = "SELECT r.claim.key, r.key , r.status.key , r.stage.key , r.claim.intimation.cpuCode.cpuCode, r.claim.claimType.value FROM StageLegalInformation r  WHERE  r.stage.key >= :fromStage and  r.stage.key <= :toStage and  r.claim.intimation.cpuCode.cpuCode =:cpuCode group by r.claim.key, r.key , r.status.key , r.stage.key , r.claim.intimation.cpuCode.cpuCode , r.claim.claimType.value order by  r.claim.intimation.cpuCode.cpuCode asc"),
	@NamedQuery(name = "StageLegalInformation.findByStatusId", query = "SELECT r.claim.key, r.key , r.status.key , r.stage.key , r.claim.intimation.cpuCode.cpuCode, r.claim.claimType.value FROM StageLegalInformation r  WHERE  r.stage.key >= :fromStage and  r.stage.key <= :toStage and r.createdDate >= :fromDate and  r.createdDate <= :endDate and r.claim.intimation.cpuCode.cpuCode =:cpuCode group by r.claim.key, r.key , r.status.key , r.stage.key , r.claim.intimation.cpuCode.cpuCode , r.claim.claimType.value order by  r.claim.intimation.cpuCode.cpuCode asc"),
	@NamedQuery(name = "StageLegalInformation.findByStatusListAndReimbursementKey", query = "SELECT s FROM StageLegalInformation s WHERE s.reimbursementKey = :reimbursementKey and s.status.key in ( :statusList ) order by s.key desc"),
	@NamedQuery(name = "StageLegalInformation.findByStatusListAndCashlessKey", query = "SELECT s FROM StageLegalInformation s WHERE s.cashlessKey = :cashlessKey and s.status.key in ( :statusList ) order by s.key desc")

})
public class StageLegalInformation extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="IMS_CLS_LEGAL_STG_INFORMATION_KEY_GENERATOR", sequenceName = "SEQ_STAGE_INF_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_LEGAL_STG_INFORMATION_KEY_GENERATOR" )
	@Column(name="STAGE_INFORMATION_KEY")
	private Long key;

	@OneToOne
	@JoinColumn(name = "INTIMATION_KEY", nullable = false)
	private Intimation intimation;
	
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

	@OneToOne(fetch = FetchType.LAZY)
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
	private Claim claim;
	
	@Column(name = "CASHLESS_KEY")
	private Long cashlessKey;
	
	@Column(name = "REIMBURSEMENT_KEY")
	private Long reimbursementKey;

	@Column(name = "DM_CODE")
	private Long dmCode;
	
	@Column(name = "DOC_ACKNOWLEDGEMENT_KEY")
	private Long docAcknowledgementKey;
	
	@Column(name = "HISTORY_DATE")
	private Date historyDate;
	
	@Column(name = "ALERT_FLAG")
	private Boolean alertFlag;
	
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

	public Intimation getIntimation() {
		return intimation;
	}

	public void setIntimation(Intimation intimation) {
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

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	public Long getCashlessKey() {
		return cashlessKey;
	}

	public void setCashlessKey(Long cashlessKey) {
		this.cashlessKey = cashlessKey;
	}

	public Long getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
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

	public Long getDmCode() {
		return dmCode;
	}

	public void setDmCode(Long dmCode) {
		this.dmCode = dmCode;
	}

	public Long getDocAcknowledgementKey() {
		return docAcknowledgementKey;
	}

	public void setDocAcknowledgementKey(Long docAcknowledgementKey) {
		this.docAcknowledgementKey = docAcknowledgementKey;
	}

	public Date getHistoryDate() {
		return historyDate;
	}

	public void setHistoryDate(Date historyDate) {
		this.historyDate = historyDate;
	}

	public Boolean getAlertFlag() {
		return alertFlag;
	}

	public void setAlertFlag(Boolean alertFlag) {
		this.alertFlag = alertFlag;
	}

	
	
}