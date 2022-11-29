package com.shaic.claim.cashlessprocess.processicac.search;

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
import com.shaic.domain.preauth.Stage;

@Entity
@Table(name = "IMS_CLS_ICAC_REQUEST")
@NamedQueries({
@NamedQuery(name = "IcacRequest.findAll", query = "SELECT i FROM IcacRequest i"),
@NamedQuery(name = "IcacRequest.findByReqByIcacKey", query="SELECT i FROM IcacRequest i where i.key = :key"),
@NamedQuery(name = "IcacRequest.findByintimationNumLike", query="SELECT i FROM IcacRequest i where i.intimationNum like :intimationNum ORDER BY i.key DESC"),
@NamedQuery(name = "IcacRequest.findByclmType", query="SELECT i FROM IcacRequest i where i.claimType.key = :key"),
@NamedQuery(name = "IcacRequest.findByintimationNumber", query="SELECT i FROM IcacRequest i where i.intimationNum = :intimationNum"),
@NamedQuery(name = "IcacRequest.findByResponseByOrderDes", query="SELECT i FROM IcacRequest i where i.intimationNum = :intimationNum ORDER BY i.key DESC")
})
public class IcacRequest extends AbstractEntity{
	@Id
	@SequenceGenerator(name="IMS_ICAC_REQUEST_GENERATOR", sequenceName = "SEQ_ICAC_REQ_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_ICAC_REQUEST_GENERATOR" ) 
	@Column(name="ICAC_KEY", updatable=false)
	private Long key;
	
	@Column(name = "INTIMATION_NUMBER")
	private String intimationNum;
	
	@Column(name="POLICY_NUMBER")
	private String policyNumber;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CLAIM_TYPE", nullable=true)
	private MastersValue claimType;
	
	@OneToOne
	@JoinColumn(name="CLAIM_STAGE",nullable=false)
	private Stage claimStage;
	
	@Column(name="REQUEST_REMARK")
	private String requestRemark;
	
	@Column(name="ICAC_FINAL_DECISION_FLAG")
	private String finalDecFlag;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status statusId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "DELETED_FLAG")
	private String deleteFlag;
	
	@Column(name = "RECORD_TYPE")
	private String recordType;
	
	@Column(name="RESPONSE_REMRKS")
	private String responseRemark;
	
	@Column(name="FINAL_RESPN_REMARKS")
	private String FinalrequestRemark;

	@Column(name="ICAC_REQUEST_SOURCE")
	private String icacRequestSource;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}
	

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public MastersValue getClaimType() {
		return claimType;
	}

	public void setClaimType(MastersValue claimType) {
		this.claimType = claimType;
	}

	public String getRequestRemark() {
		return requestRemark;
	}

	public void setRequestRemark(String requestRemark) {
		this.requestRemark = requestRemark;
	}

	public String getFinalDecFlag() {
		return finalDecFlag;
	}

	public void setFinalDecFlag(String finalDecFlag) {
		this.finalDecFlag = finalDecFlag;
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

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public Stage getClaimStage() {
		return claimStage;
	}

	public void setClaimStage(Stage claimStage) {
		this.claimStage = claimStage;
	}

	public Status getStatusId() {
		return statusId;
	}

	public void setStatusId(Status statusId) {
		this.statusId = statusId;
	}

	public String getIntimationNum() {
		return intimationNum;
	}

	public void setIntimationNum(String intimationNum) {
		this.intimationNum = intimationNum;
	}

	public String getResponseRemark() {
		return responseRemark;
	}

	public void setResponseRemark(String responseRemark) {
		this.responseRemark = responseRemark;
	}

	public String getFinalrequestRemark() {
		return FinalrequestRemark;
	}

	public void setFinalrequestRemark(String finalrequestRemark) {
		FinalrequestRemark = finalrequestRemark;
	}

	public String getIcacRequestSource() {
		return icacRequestSource;
	}

	public void setIcacRequestSource(String icacRequestSource) {
		this.icacRequestSource = icacRequestSource;
	}
	
}
