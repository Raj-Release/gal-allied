/**
 * 
 */
package com.shaic.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;

/**
 * @author ntv.vijayar
 *
 */


@Entity
@Table(name="IMS_DMS_DOC_METADATA")
@NamedQueries({
@NamedQuery(name="DMSDocMetaData.findAll", query="SELECT d FROM DMSDocMetaData d"),
@NamedQuery(name="DMSDocMetaData.findByApplicationId", query="SELECT d FROM DMSDocMetaData d where d.applicationId = :applicationId"),
@NamedQuery(name="DMSDocMetaData.findByDocId", query="SELECT d FROM DMSDocMetaData d where d.docIdFax = :docIdFax")
})

public class DMSDocMetaData extends AbstractEntity{
	
	@Column(name="INT_ID")
	private Long intId;
	
	@Column(name = "INT_NUMBER")
	private String intNumber;
	
	@Column(name = "APPLICATION_ID")
	private Long applicationId;
	
	
	@Column(name = "DOC_TYPE")
	private String docType;
	
	@Id
	@Column(name = "DOC_ID_EFAX")
	private Long docIdFax;
	
	@Column(name = "FILE_SIZE")
	private Long fileSize;
	
	@Column(name = "FILE_NAME")
	private String fileName;
	
	@Column(name = "DOC_URL")
	private String docUrl;
	
	@Column(name = "ACTUAL_FILE_NAME")
	private String actualFileName;
	
	@Column(name = "FILE_KEY")
	private Long fileKey;

	@Override
	public Long getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKey(Long key) {
		// TODO Auto-generated method stub
		
	}

	public Long getIntId() {
		return intId;
	}

	public void setIntId(Long intId) {
		this.intId = intId;
	}

	public String getIntNumber() {
		return intNumber;
	}

	public void setIntNumber(String intNumber) {
		this.intNumber = intNumber;
	}

	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public Long getDocIdFax() {
		return docIdFax;
	}

	public void setDocIdFax(Long docIdFax) {
		this.docIdFax = docIdFax;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDocUrl() {
		return docUrl;
	}

	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}

	public String getActualFileName() {
		return actualFileName;
	}

	public void setActualFileName(String actualFileName) {
		this.actualFileName = actualFileName;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getIntId() == null) {
            return obj == this;
        } else {
            return getIntId().equals(((DMSDocMetaData) obj).getIntId());
        }
    }

    @Override
    public int hashCode() {
        if (intId != null) {
            return intId.hashCode();
        } else {
            return super.hashCode();
        }
    }

	public Long getFileKey() {
		return fileKey;
	}

	public void setFileKey(Long fileKey) {
		this.fileKey = fileKey;
	}
	
	

}
