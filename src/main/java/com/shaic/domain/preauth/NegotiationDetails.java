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
import com.shaic.domain.Status;

@Entity
@Table(name="IMS_CLS_NEGOTIATION_DTLS")
@NamedQueries({
	@NamedQuery(name ="NegotiationDetails.findByKey",query="SELECT r FROM NegotiationDetails r WHERE r.key = :primaryKey"),
	@NamedQuery(name ="NegotiationDetails.findByClaimKey",query="SELECT r FROM NegotiationDetails r WHERE r.claim.key = :claimKey order by r.key desc"),
})
public class NegotiationDetails extends AbstractEntity implements Serializable{

	@Id
	@SequenceGenerator(name="SEQ_CASH_NEGOTIATION_KEY_GENERATOR", sequenceName = "SEQ_CASH_NEGOTIATION_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_CASH_NEGOTIATION_KEY_GENERATOR" )
	@Column(name = "NEGOTIATION_KEY")
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INTIMATION_KEY")
	private Intimation intimation;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLAIM_KEY")
	private Claim claim;
	
	@Column(name = "CASHLESS_KEY")
	private Long cashlessKey;
	
	@Column(name = "NEGOTIATION_FLAG")
	private String negotiationFlag;
	
	@Column(name = "NEGOTIATION_REMARKS")
	private String negotiationRemarks;
	
	@Column(name = "NEGOTIATION_CAN_REMARKS")
	private String negotiationCancelRemarks;
	
	@Column(name = "NEGOTIATED_REMARKS")
	private String negotiateRemarksByHospital;
	
	@Column(name = "HOSPITAL_AGREED")
	private String hospitalType;
	
	@OneToOne
	@JoinColumn(name="STAGE_ID",nullable=false)
	private Stage stageId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status statusId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name= "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="NEGOTIATION_AMOUNT")
	private Double negotiationAmt;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getCashlessKey() {
		return cashlessKey;
	}

	public void setCashlessKey(Long cashlessKey) {
		this.cashlessKey = cashlessKey;
	}

	public String getNegotiationFlag() {
		return negotiationFlag;
	}

	public void setNegotiationFlag(String negotiationFlag) {
		this.negotiationFlag = negotiationFlag;
	}

	public String getNegotiationRemarks() {
		return negotiationRemarks;
	}

	public void setNegotiationRemarks(String negotiationRemarks) {
		this.negotiationRemarks = negotiationRemarks;
	}

	public String getNegotiationCancelRemarks() {
		return negotiationCancelRemarks;
	}

	public void setNegotiationCancelRemarks(String negotiationCancelRemarks) {
		this.negotiationCancelRemarks = negotiationCancelRemarks;
	}

	public String getNegotiateRemarksByHospital() {
		return negotiateRemarksByHospital;
	}

	public void setNegotiateRemarksByHospital(String negotiateRemarksByHospital) {
		this.negotiateRemarksByHospital = negotiateRemarksByHospital;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public Stage getStageId() {
		return stageId;
	}

	public void setStageId(Stage stageId) {
		this.stageId = stageId;
	}

	public Status getStatusId() {
		return statusId;
	}

	public void setStatusId(Status statusId) {
		this.statusId = statusId;
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

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
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

	public Double getNegotiationAmt() {
		return negotiationAmt;
	}

	public void setNegotiationAmt(Double negotiationAmt) {
		this.negotiationAmt = negotiationAmt;
	}
}
