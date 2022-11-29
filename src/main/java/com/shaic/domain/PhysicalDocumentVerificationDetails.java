package com.shaic.domain;

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


@Entity
@Table(name = "IMS_CLS_PHY_DOC_VERIFY_DTLS")
@NamedQueries({
	@NamedQuery(name = "PhysicalDocumentVerificationDetails.findAll", query = "SELECT i FROM PhysicalDocumentVerificationDetails i") ,
	@NamedQuery(name ="PhysicalDocumentVerificationDetails.findByRodKey",query="SELECT r FROM PhysicalDocumentVerificationDetails r WHERE r.reimbursement.key = :primaryKey"),
	@NamedQuery(name ="PhysicalDocumentVerificationDetails.findByDocSummaryKey",query="SELECT r FROM PhysicalDocumentVerificationDetails r WHERE r.documentId = :primaryKey")
	
})
public class PhysicalDocumentVerificationDetails  extends AbstractEntity {
	

	private static final long serialVersionUID = 4052781126397799714L;

	@Id
	@SequenceGenerator(name="IMS_PHY_DOC_VER_DTLS_KEY_GENERATOR", sequenceName = "SEQ_PHY_DOC_VER_DTLS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_PHY_DOC_VER_DTLS_KEY_GENERATOR" ) 
	
	@Column(name="PHY_DOC_VER_DTLS_ID")
	private Long key;
	
	@Column(name="PHY_DOC_VER_ID")
	private Long physicalDocKey;
	
	@Column(name="DOCUMENT_ID")
	private Long documentId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ROD_KEY", nullable=true)
	private Reimbursement reimbursement;
	
	@Column(name = "DOCUMENT_VERIFICATION_FLAG")
	private String documentVerificationFlag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="PHY_DOC_REC_DATE")
	private Date physicalDocRecDate;
	
	@Column(name="PHY_DOC_TYPE")
	private Long phyDocRecType;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getPhysicalDocKey() {
		return physicalDocKey;
	}

	public void setPhysicalDocKey(Long physicalDocKey) {
		this.physicalDocKey = physicalDocKey;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public Reimbursement getReimbursement() {
		return reimbursement;
	}

	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
	}

	public String getDocumentVerificationFlag() {
		return documentVerificationFlag;
	}

	public void setDocumentVerificationFlag(String documentVerificationFlag) {
		this.documentVerificationFlag = documentVerificationFlag;
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

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Date getPhysicalDocRecDate() {
		return physicalDocRecDate;
	}

	public void setPhysicalDocRecDate(Date physicalDocRecDate) {
		this.physicalDocRecDate = physicalDocRecDate;
	}

	public Long getPhyDocRecType() {
		return phyDocRecType;
	}

	public void setPhyDocRecType(Long phyDocRecType) {
		this.phyDocRecType = phyDocRecType;
	}
	
	

}
