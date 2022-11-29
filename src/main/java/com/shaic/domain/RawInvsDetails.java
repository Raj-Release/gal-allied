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
@Entity
@Table(name="IMS_CLS_RAW_INV_DTLS")
@NamedQueries({
	@NamedQuery(name="RawInvsDetails.findAll", query="SELECT r FROM RawInvsDetails r where r.key = :key"),
	@NamedQuery(name="RawInvsDetails.findAllByRawInvKey", query="SELECT r FROM RawInvsDetails r where r.rawInvstigation.key = :rawInvkey and (r.deletedFlag is null or r.deletedFlag = 'N')"),
	@NamedQuery(name="RawInvsDetails.findByRawInvKey", query = "SELECT r FROM RawInvsDetails r where r.rawInvstigation.key = :rawInvkey and r.requestedStatus = :statusList and (r.deletedFlag is null or r.deletedFlag = 'N')"),
	@NamedQuery(name="RawInvsDetails.findAllByRawInvKeyWithoutFilter", query="SELECT r FROM RawInvsDetails r where r.rawInvstigation.key = :rawInvkey order by r.createdDate desc "),
	@NamedQuery(name="RawInvsDetails.findByRecordType", query = "SELECT r FROM RawInvsDetails r where r.rawInvstigation.key = :rawInvkey and (r.deletedFlag is null or r.deletedFlag = 'N') and r.recordType is null"),
	@NamedQuery(name="RawInvsDetails.findByHeaderKey", query = "SELECT r FROM RawInvsDetails r where r.rawInvstigation.key =:rawInvkey"),
	@NamedQuery(name="RawInvsDetails.findByIntimationNo", query = "SELECT r FROM RawInvsDetails r where r.rawInvstigation.intimationNo = :intimationNo"),


})
public class RawInvsDetails implements Serializable{
private static final long serialVersionUID = 1086997314154372927L;
	@Id
	@SequenceGenerator(name="SEQ_RAW_INV_DTLS_KEY_GENERATOR", sequenceName = "SEQ_RAW_INV_DTLS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_RAW_INV_DTLS_KEY_GENERATOR")
	@Column(name = "RAW_INV_DTLS_KEY")
	private Long key;
	
	/*@Column(name = "RAW_INV_KEY")
	private Long rawInvkey;*/
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RAW_INV_KEY")
	private RawInvsHeaderDetails rawInvstigation;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CATEGORY_ID",nullable=false)
	private RawCategory rawCategory;    
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SUB_CATEGORY_ID")
	private RawSubCategory rawSubCategory;    
	
	@Column(name = "REQUESTED_REMARKS")
	private String requestedRemarks;
	
	@Column(name = "REQUESTED_BY")
	private String requestedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REQUESTED_DATE")
	private Date requestedDate;
	
	@Column(name = "REQUEST_STAGE_ID")
	private Long requestedStage;
	
	@Column(name = "REQUEST_STATUS_ID")
	private Long requestedStatus;
	
	@Column(name = "REPLIED_BY")
	private String repliedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REPLIED_DATE")
	private Date repliedDate;
	
	@Column(name = "RESOLUTION_TYPE")
	private String redolutionType;
	
	@Column(name = "REPLIED_REMARKS")
	private String repliedRemarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifyby;
	
	@Column(name = "DELETED_FLAG")
	private String deletedFlag;
	
	@Column(name = "RECORD_TYPE")
	private String recordType;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getRequestedRemarks() {
		return requestedRemarks;
	}

	public void setRequestedRemarks(String requestedRemarks) {
		this.requestedRemarks = requestedRemarks;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public Date getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	public Long getRequestedStage() {
		return requestedStage;
	}

	public void setRequestedStage(Long requestedStage) {
		this.requestedStage = requestedStage;
	}

	public Long getRequestedStatus() {
		return requestedStatus;
	}

	public void setRequestedStatus(Long requestedStatus) {
		this.requestedStatus = requestedStatus;
	}

	public String getRepliedBy() {
		return repliedBy;
	}

	public void setRepliedBy(String repliedBy) {
		this.repliedBy = repliedBy;
	}

	public Date getRepliedDate() {
		return repliedDate;
	}

	public void setRepliedDate(Date repliedDate) {
		this.repliedDate = repliedDate;
	}

	public String getRedolutionType() {
		return redolutionType;
	}

	public void setRedolutionType(String redolutionType) {
		this.redolutionType = redolutionType;
	}

	public String getRepliedRemarks() {
		return repliedRemarks;
	}

	public void setRepliedRemarks(String repliedRemarks) {
		this.repliedRemarks = repliedRemarks;
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

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifyby() {
		return modifyby;
	}

	public void setModifyby(String modifyby) {
		this.modifyby = modifyby;
	}
	
	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public RawCategory getRawCategory() {
		return rawCategory;
	}

	public void setRawCategory(RawCategory rawCategory) {
		this.rawCategory = rawCategory;
	}

	public RawSubCategory getRawSubCategory() {
		return rawSubCategory;
	}

	public void setRawSubCategory(RawSubCategory rawSubCategory) {
		this.rawSubCategory = rawSubCategory;
	}

	public RawInvsHeaderDetails getRawInvstigation() {
		return rawInvstigation;
	}

	public void setRawInvstigation(RawInvsHeaderDetails rawInvstigation) {
		this.rawInvstigation = rawInvstigation;
	}

	@Override
	public String toString() {
		return "RawInvsDetails [key=" + key + ", rawInvstigation="
				+ rawInvstigation + ", rawCategory=" + rawCategory
				+ ", rawSubCategory=" + rawSubCategory + ", requestedRemarks="
				+ requestedRemarks + ", requestedBy=" + requestedBy
				+ ", requestedDate=" + requestedDate + ", requestedStage="
				+ requestedStage + ", requestedStatus=" + requestedStatus
				+ ", repliedBy=" + repliedBy + ", repliedDate=" + repliedDate
				+ ", redolutionType=" + redolutionType + ", repliedRemarks="
				+ repliedRemarks + ", createdDate=" + createdDate
				+ ", createdBy=" + createdBy + ", modifiedDate=" + modifiedDate
				+ ", modifyby=" + modifyby + ", deletedFlag=" + deletedFlag
				+ ", recordType=" + recordType + "]";
	}
	
	
	
	
}
