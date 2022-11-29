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
@Table(name="IMS_CLS_PA_DOC_LIST")
@NamedQueries({
	@NamedQuery(name="PACoveringLetterDocument.findAll", query="SELECT m FROM PACoveringLetterDocument m where m.activeStatus is not null and m.activeStatus = 1"),
	@NamedQuery(name="PACoveringLetterDocument.findByKey",query="SELECT o FROM PACoveringLetterDocument o where o.key = :primaryKey"),
	@NamedQuery(name= "PACoveringLetterDocument.findByClaimKey", query="SELECT m FROM PACoveringLetterDocument m where m.claim is not null and m.claim.key = :claimKey and  m.activeStatus is not null and m.activeStatus = 1")
})

public class PACoveringLetterDocument extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_DOCUMENTKEY_GENERATOR", sequenceName = "SEQ_DOCUMENTKEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_DOCUMENTKEY_GENERATOR" )
	@Column(name="DOCUMENT_TYPE_KEY")
	Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLAIM_KEY", nullable = false)
	private Claim claim;
	
	@Column(name="DESCRIPTION")
	private String docDescription;
	
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
	private Date modifieDate;  
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOC_ID", nullable = false)
	private DocumentCheckListMaster docMaster;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	public String getDocDescription() {
		return docDescription;
	}

	public void setDocDescription(String docDescription) {
		this.docDescription = docDescription;
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

	public Date getModifieDate() {
		return modifieDate;
	}

	public void setModifieDate(Date modifieDate) {
		this.modifieDate = modifieDate;
	}

	public DocumentCheckListMaster getDocMaster() {
		return docMaster;
	}

	public void setDocMaster(DocumentCheckListMaster docMaster) {
		this.docMaster = docMaster;
	}
	
}
