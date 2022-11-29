package com.shaic.domain.preauth;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;

/**
 * The persistent class for the IMS_CLS_PED_INITIATE_T database table.
 * 
 */
@Entity

@Table(name="IMS_CLSB_PED_REVERSE_FEED")
@NamedQueries({
	@NamedQuery(name="PEDReverseFeed.findAll", query="SELECT i FROM PEDReverseFeed i")
})

public class PEDReverseFeed extends AbstractEntity{
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="IMS_CLSB_PED_REVERSE_FEED_KEY_GENERATOR", sequenceName = "SEQ_PED_UPD_SERVICE_TRAN_IND", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLSB_PED_REVERSE_FEED_KEY_GENERATOR" ) 
	@Column(name="SERVICE_TRANSACTION_ID", updatable=false)
	private Long key;
	
	@Column(name="POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name="ENDORSEMENT_STATUS")
	private String endorsementStatus;
	
	@Column(name="ENDORSEMENT_EFFECTIVE_DATE")
	private Date endorsementDate;
	
	@Column(name="GAE_END_APPR_YN")
	private String gaeEndApprYN;
	
	@Column(name="GAE_APPR_UID")
	private String gaeApprUID;
	
	@Column(name="GAE_APPR_NAME")
	private String gaeApprName;
	
	@Column(name="GAE_APPR_DT")
	private Date gaeApprDT;
	
	@Column(name="GAE_APPR_REMARKS")
	private String gaeApprRemarks;
	
	@Column(name="ACTIVE_STATUS")
	private Integer activeStatus;
	
	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="WORKITEM_ID")
	private Long workItemId;
	
	public PEDReverseFeed() {
		
	}
	
	public Long getKey() {
		return this.key;
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

	public String getEndorsementStatus() {
		return endorsementStatus;
	}

	public void setEndorsementStatus(String endorsementStatus) {
		this.endorsementStatus = endorsementStatus;
	}

	public Date getEndorsementDate() {
		return endorsementDate;
	}

	public void setEndorsementDate(Date endorsementDate) {
		this.endorsementDate = endorsementDate;
	}

	public String getGaeEndApprYN() {
		return gaeEndApprYN;
	}

	public void setGaeEndApprYN(String gaeEndApprYN) {
		this.gaeEndApprYN = gaeEndApprYN;
	}

	public String getGaeApprUID() {
		return gaeApprUID;
	}

	public void setGaeApprUID(String gaeApprUID) {
		this.gaeApprUID = gaeApprUID;
	}

	public String getGaeApprName() {
		return gaeApprName;
	}

	public void setGaeApprName(String gaeApprName) {
		this.gaeApprName = gaeApprName;
	}

	public Date getGaeApprDT() {
		return gaeApprDT;
	}

	public void setGaeApprDT(Date gaeApprDT) {
		this.gaeApprDT = gaeApprDT;
	}

	public String getGaeApprRemarks() {
		return gaeApprRemarks;
	}

	public void setGaeApprRemarks(String gaeApprRemarks) {
		this.gaeApprRemarks = gaeApprRemarks;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
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

	public Long getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(Long workItemId) {
		this.workItemId = workItemId;
	}
	
}