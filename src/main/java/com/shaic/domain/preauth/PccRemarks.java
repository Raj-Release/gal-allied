package com.shaic.domain.preauth;

import java.io.Serializable;
import java.sql.Timestamp;
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
import com.shaic.domain.MastersValue;
import com.shaic.domain.Status;

@Entity
@Table(name = "IMS_CLS_PCC_REMARKS")
@NamedQueries({
	@NamedQuery(name ="PccRemarks.findByKey",query="SELECT r FROM PccRemarks r WHERE r.intimation.key = :intimationKey order by r.key desc"),
	@NamedQuery(name = "PccRemarks.findAll", query = "SELECT i FROM PccRemarks i"),
	@NamedQuery(name="PccRemarks.findByClaimKey", query="SELECT c FROM PccRemarks c WHERE c.claim is not null and c.claim.key = :claimKey"),
})
public class PccRemarks extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "IMS_CLS_SEQ_PCC_REMARKS_KEY_GENERATOR", sequenceName = "SEQ_PCC_REMARKS_KEY", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMS_CLS_SEQ_PCC_REMARKS_KEY_GENERATOR")
	@Column(name = "PCC_REMARKS_KEY")
	private Long key;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INTIMATION_KEY", nullable = false)
	private Intimation intimation;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CLAIM_TYPE_ID", nullable=true)
	private MastersValue claimType;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID", nullable = false)
	private Status status;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STAGE_ID", nullable = false)
	private Stage stage;

	@Column(name = "PCC_REMARKS")
	private String pccRemarks;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLAIM_KEY", nullable = false)
	private Claim claim;

	@Column(name = "CASHLESS_KEY")
	private Long cashlessKey;

	@Column(name = "REIMBURSEMENT_KEY")
	private Long reimbursementKey;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BI_DATE")
	private Date biDate;

	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;

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

	public PccRemarks() {
	}

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

	public MastersValue getClaimType() {
		return claimType;
	}

	public void setClaimType(MastersValue claimType) {
		this.claimType = claimType;
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

	public String getPccRemarks() {
		return pccRemarks;
	}

	public void setPccRemarks(String pccRemarks) {
		this.pccRemarks = pccRemarks;
	}

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

	public Date getBiDate() {
		return biDate;
	}

	public void setBiDate(Date biDate) {
		this.biDate = biDate;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
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

	
	
}
