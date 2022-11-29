package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;
/**
 * @author karthikeyan.r
 * The persistent class for the MAS_HOSPITAL_SCORE database table.
 * 
 */

@SuppressWarnings("serial")
@Entity
@Table(name="MAS_OMP_DOC_RELATED")
@NamedQueries({
	@NamedQuery(name="OMPFileType.findAll", query="SELECT m FROM OMPFileType m"),
	@NamedQuery(name="OMPFileType.findDistinctFileType", query="SELECT m FROM OMPFileType m WHERE m.activeStatus = 'Y' ORDER BY m.fileTypeKey ASC"),
	@NamedQuery(name="OMPFileType.findDocByFile", query="SELECT m FROM OMPFileType m WHERE m.fileTypeKey = :fileTypeKey AND m.activeStatus = 'Y'")
})
public class OMPFileType extends AbstractEntity implements Serializable  {


	@Column(name = "OMP_REL_KEY")
	private Long key;
	
	@Column(name = "FILE_TYP_KEY")
	private Long fileTypeKey;
	
	@Column(name = "FILE_TYPE_DESC")
	private String fileTypeDesc;
	
	@Id
	@Column(name = "DOC_TYP_KEY")
	private Long docTypeKey;
	
	@Column(name = "DOC_TYP_DESC")
	private String docTypeDesc;
	
	@Column(name = "ACTIVE_STATUS")
	private String activeStatus;
	
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
	
	@Override
	public Long getKey() {
		return key;
	}

	@Override
	public void setKey(Long key) {
		this.key = key;
	}

	public Long getFileTypeKey() {
		return fileTypeKey;
	}

	public void setFileTypeKey(Long fileTypeKey) {
		this.fileTypeKey = fileTypeKey;
	}

	public String getFileTypeDesc() {
		return fileTypeDesc;
	}

	public void setFileTypeDesc(String fileTypeDesc) {
		this.fileTypeDesc = fileTypeDesc;
	}

	public Long getDocTypeKey() {
		return docTypeKey;
	}

	public void setDocTypeKey(Long docTypeKey) {
		this.docTypeKey = docTypeKey;
	}

	public String getDocTypeDesc() {
		return docTypeDesc;
	}

	public void setDocTypeDesc(String docTypeDesc) {
		this.docTypeDesc = docTypeDesc;
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

}
