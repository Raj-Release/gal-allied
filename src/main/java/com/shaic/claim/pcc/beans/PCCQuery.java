package com.shaic.claim.pcc.beans;

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
import com.shaic.domain.MastersValue;
import com.shaic.domain.Status;

@Entity
@Table(name = "IMS_CLS_PCC_QUERY")
@NamedQueries({
	@NamedQuery(name = "PCCQuery.findByKey", query = "SELECT i FROM PCCQuery i where i.key = :key"),
	@NamedQuery(name ="PCCQuery.findQuerysByPCCRole",query="SELECT r FROM PCCQuery r WHERE r.pccRequest.key = :pccrequestKey and r.status.key in (:statusList) and (r.roleAssignedBy = :roleAssignedBy OR r.roleAssigned = :roleAssigned) and (r.userAssignedBy = :userAssignedBy OR r.userAssigned = :userAssigned)"),
	@NamedQuery(name ="PCCQuery.findReplayByStatus",query="SELECT r FROM PCCQuery r WHERE r.pccRequest.key = :pccrequestKey and r.status.key in (:statusList)"),
	@NamedQuery(name ="PCCQuery.findPCCQueryByPCCkey",query="SELECT r FROM PCCQuery r WHERE r.pccRequest.key = :pccrequestKey"),
	@NamedQuery(name ="PCCQuery.findPCCQueryBySource",query="SELECT r FROM PCCQuery r WHERE r.pccRequest.key = :pccrequestKey and r.status.key = :statusKey and r.pccSource.key = :sourceKey"),
	@NamedQuery(name ="PCCQuery.findQueryByPCCkeyandrole",query="SELECT r FROM PCCQuery r WHERE r.pccRequest.key = :pccrequestKey and r.status.key in (:statusList) and r.roleAssignedBy = :roleAssignedBy"),
	@NamedQuery(name ="PCCQuery.findQueryByPCCkeyandassignedrole",query="SELECT r FROM PCCQuery r WHERE r.pccRequest.key = :pccrequestKey and r.status.key in (:statusList) and r.roleAssigned = :roleAssigned")
})

public class PCCQuery extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "IMS_CLS_SEQ_PCC_RESPONSE_KAY_GENERATOR", sequenceName = "SEQ_PCC_RESPONSE_KAY", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMS_CLS_SEQ_PCC_RESPONSE_KAY_GENERATOR")
	@Column(name = "PCC_RESPONSE_KEY")
	private Long key;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PCC_KEY", nullable = false)
	private PCCRequest pccRequest;
	
	@Column(name = "ROLE_ASSIGNED")
	private String roleAssigned;
	
	@Column(name = "USER_ASSIGNED")
	private String userAssigned;

	@Column(name = "ROLE_ASSIGNED_BY")
	private String roleAssignedBy;
	
	@Column(name = "USER_ASSIGNED_BY")
	private String userAssignedBy;
	
	@Column(name = "NEGOTIATED_AMOUNT")
	private Long negotiatedAmount;
	
	@Column(name = "SAVED_AMOUNT")
	private Long savedAmount;	
	
	@Column(name = "QUERY_REMARKS")
	private String queryRemarks;

	@Column(name = "QUERY_REPLY_REMARKS")
	private String queryReplyRemarks;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_KEY", nullable = false)
	private Status status;

	@Column(name = "REPLIED_BY")
	private String repliedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REPLIED_DATE")
	private Date repliedDate;

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
	
	@Column(name = "DELETED_FLAG")
	private String deletedFlag;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PCC_SOURCE", nullable = false)
	private MastersValue pccSource;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getRoleAssigned() {
		return roleAssigned;
	}

	public void setRoleAssigned(String roleAssigned) {
		this.roleAssigned = roleAssigned;
	}

	public String getUserAssigned() {
		return userAssigned;
	}

	public void setUserAssigned(String userAssigned) {
		this.userAssigned = userAssigned;
	}

	public String getRoleAssignedBy() {
		return roleAssignedBy;
	}

	public void setRoleAssignedBy(String roleAssignedBy) {
		this.roleAssignedBy = roleAssignedBy;
	}

	public String getUserAssignedBy() {
		return userAssignedBy;
	}

	public void setUserAssignedBy(String userAssignedBy) {
		this.userAssignedBy = userAssignedBy;
	}

	public Long getNegotiatedAmount() {
		return negotiatedAmount;
	}

	public void setNegotiatedAmount(Long negotiatedAmount) {
		this.negotiatedAmount = negotiatedAmount;
	}

	public Long getSavedAmount() {
		return savedAmount;
	}

	public void setSavedAmount(Long savedAmount) {
		this.savedAmount = savedAmount;
	}

	public String getQueryRemarks() {
		return queryRemarks;
	}

	public void setQueryRemarks(String queryRemarks) {
		this.queryRemarks = queryRemarks;
	}

	public String getQueryReplyRemarks() {
		return queryReplyRemarks;
	}

	public void setQueryReplyRemarks(String queryReplyRemarks) {
		this.queryReplyRemarks = queryReplyRemarks;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public PCCRequest getPccRequest() {
		return pccRequest;
	}

	public void setPccRequest(PCCRequest pccRequest) {
		this.pccRequest = pccRequest;
	}

	public MastersValue getPccSource() {
		return pccSource;
	}

	public void setPccSource(MastersValue pccSource) {
		this.pccSource = pccSource;
	}

}
