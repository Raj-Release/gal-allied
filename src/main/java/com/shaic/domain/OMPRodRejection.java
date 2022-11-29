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

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.preauth.Stage;

/**
 * @author karthikeyan.r
 * The persistent class for the IMS_CLS_LUMEN_DETAILS database table.
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name="IMS_CLS_OMP_ROD_REJECT")
@NamedQueries({
	@NamedQuery(name="OMPRodRejection.findAll", query="SELECT m FROM OMPRodRejection m"),
	@NamedQuery(name="OMPRodRejection.findByRODKey", query="SELECT m FROM OMPRodRejection m WHERE m.reimbursementKey.key = :rodKey AND m.activeStatus = 1"),
	@NamedQuery(name="OMPRodRejection.findByRODKeyWithDeleted", query="SELECT m FROM OMPRodRejection m WHERE m.reimbursementKey.key = :rodKey")
})
public class OMPRodRejection extends AbstractEntity implements Serializable  {

	/*OMP_ROD_REJ_KEY         NOT NULL NUMBER(15)        
	REIMBURSEMENT_KEY                NUMBER(15)        
	DOC_ACKNOWLEDGEMENT_KEY          NUMBER(15)        
	CLAIM_KEY                        NUMBER(15)        
	ROD_NUMBER                       VARCHAR2(50 CHAR) 
	REJECTION_CATEGORY_ID            NUMBER(4)         
	CREATED_DATE                     DATE              
	CREATED_BY                       VARCHAR2(10 CHAR) 
	MODIFIED_DATE                    DATE              
	MODIFIED_BY                      VARCHAR2(10 CHAR) 
	ACTIVE_STATUS                    NUMBER           */ 
	
	@Id
	@SequenceGenerator(name="IMS_CLS_OMP_ROD_REJECT_GENERATOR", sequenceName = "SEQ_OMP_ROD_REJ_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_OMP_ROD_REJECT_GENERATOR")
	@Column(name="OMP_ROD_REJ_KEY")
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="REIMBURSEMENT_KEY", nullable=false)
	private OMPReimbursement reimbursementKey ;
	
	@Column(name="DOC_ACKNOWLEDGEMENT_KEY")
	private Long docAckKey;
	
	@Column(name="CLAIM_KEY")
	private Long claimKey;
	
	@Column(name = "ROD_NUMBER")
	private String rodNumber;
	
	@Column(name="REJECTION_CATEGORY_ID")
	private Long rejectionCategoryId;
	
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

	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public OMPReimbursement getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(OMPReimbursement reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public Long getDocAckKey() {
		return docAckKey;
	}

	public void setDocAckKey(Long docAckKey) {
		this.docAckKey = docAckKey;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public String getRodNumber() {
		return rodNumber;
	}

	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
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

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Long getRejectionCategoryId() {
		return rejectionCategoryId;
	}

	public void setRejectionCategoryId(Long rejectionCategoryId) {
		this.rejectionCategoryId = rejectionCategoryId;
	}
	
	
	
}
