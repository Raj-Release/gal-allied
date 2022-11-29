
package com.shaic;

import java.io.Serializable;
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

@Entity
@Table(name="IMS_CLS_CLAIM_REMARKS_DOCS")
@NamedQueries({
@NamedQuery(name="ClaimRemarksDocs.findByclmAlertKey",query = "SELECT o FROM ClaimRemarksDocs o where o.clmAlertKey = :clmAlertKey and o.activeStatus = 1")
})

public class ClaimRemarksDocs implements Serializable{

	@Id
	@SequenceGenerator(name="CLAIM_REMARKS_DOCS_KEY_GENERATOR", sequenceName = "SEQ_CLM_ALERT_DOC_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CLAIM_REMARKS_DOCS_KEY_GENERATOR" )
	@Column(name="CLM_ALERT_DOC_KEY")
	private Long key;
	
	@Column(name="CLM_REMARK_ALERT_KEY")
	private Long clmAlertKey;
	
	@Column(name="DOC_FROM")
	private String docFrom;
	
	@Column(name="DOC_TOCKEN")
	private Long docTocken;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifyBy;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="FILE_NAME")
	private String fileName;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getClmAlertKey() {
		return clmAlertKey;
	}

	public void setClmAlertKey(Long clmAlertKey) {
		this.clmAlertKey = clmAlertKey;
	}

	public String getDocFrom() {
		return docFrom;
	}

	public void setDocFrom(String docFrom) {
		this.docFrom = docFrom;
	}

	public Long getDocTocken() {
		return docTocken;
	}

	public void setDocTocken(Long docTocken) {
		this.docTocken = docTocken;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
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

	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
}
