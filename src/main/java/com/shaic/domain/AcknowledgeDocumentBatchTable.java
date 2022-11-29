package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;


@Entity
@Table(name="IMS_CLS_REC_OF_DOC_BATCH_DTLS")
@NamedQueries({
	@NamedQuery(name="AcknowledgeDocumentBatchTable.findIntimationByAckBatchFlag", query="SELECT o FROM AcknowledgeDocumentBatchTable o where o.ackBatchRunFlag = 'N' or o.ackBatchRunFlag is null order by o.createdDate asc")
	
})
public class AcknowledgeDocumentBatchTable extends AbstractEntity implements Serializable {

	@Id
	@Column(name = "REC_OF_DOC_INTM_KEY")
	private Long recOfDocKey;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INTIMATION_KEY", nullable = false)
	private Intimation intimation;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLAIM_KEY", nullable = false)
	private Claim claim;
	
	@Id
	@Column(name = "CASHLESS_KEY")
	private Long cahlessKey;
	
	@Column(name = "APPROVED_AMOUNT")
	private Double approvalAmount;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	public Long getRecOfDocKey() {
		return recOfDocKey;
	}

	public void setRecOfDocKey(Long recOfDocKey) {
		this.recOfDocKey = recOfDocKey;
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

	public Long getCahlessKey() {
		return cahlessKey;
	}

	public void setCahlessKey(Long cahlessKey) {
		this.cahlessKey = cahlessKey;
	}

	public Double getApprovalAmount() {
		return approvalAmount;
	}

	public void setApprovalAmount(Double approvalAmount) {
		this.approvalAmount = approvalAmount;
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

	public String getAckBatchRunFlag() {
		return ackBatchRunFlag;
	}

	public void setAckBatchRunFlag(String ackBatchRunFlag) {
		this.ackBatchRunFlag = ackBatchRunFlag;
	}

	public Date getAckBatchRunDate() {
		return ackBatchRunDate;
	}

	public void setAckBatchRunDate(Date ackBatchRunDate) {
		this.ackBatchRunDate = ackBatchRunDate;
	}

	public String getAckBatchRemarks() {
		return ackBatchRemarks;
	}

	public void setAckBatchRemarks(String ackBatchRemarks) {
		this.ackBatchRemarks = ackBatchRemarks;
	}

	public String getRodBatchRunFlag() {
		return rodBatchRunFlag;
	}

	public void setRodBatchRunFlag(String rodBatchRunFlag) {
		this.rodBatchRunFlag = rodBatchRunFlag;
	}

	public Date getRodBatchRunDate() {
		return rodBatchRunDate;
	}

	public void setRodBatchRunDate(Date rodBatchRunDate) {
		this.rodBatchRunDate = rodBatchRunDate;
	}

	public String getRodBatchRemarks() {
		return rodBatchRemarks;
	}

	public void setRodBatchRemarks(String rodBatchRemarks) {
		this.rodBatchRemarks = rodBatchRemarks;
	}

	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "ACK_RUN_FLAG")
	private String ackBatchRunFlag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACK_RUN_DATE")
	private Date ackBatchRunDate;
	
	@Column(name = "ACK_RUN_REMARKS")
	private String ackBatchRemarks;
	
	@Column(name = "ROD_RUN_FLAG")
	private String rodBatchRunFlag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ROD_RUN_DATE")
	private Date rodBatchRunDate;
	
	@Column(name = "ROD_RUN_REMARKS")
	private String rodBatchRemarks;

	
	@Override
	public Long getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKey(Long key) {
		// TODO Auto-generated method stub
		
	}

}
