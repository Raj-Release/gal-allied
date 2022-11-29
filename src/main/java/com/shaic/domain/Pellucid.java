/**
 * 
 */
package com.shaic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author ntv.vijayar
 *
 */
@Entity
@Table(name="IMS_PELLUCID_DOC_HDR")
@NamedQueries({
@NamedQuery(name="Pellucid.findAll", query="SELECT p FROM Pellucid p where p.docGlxReadFlag = :docGlxReadFlag")
})

public class Pellucid {
	
	@Column(name = "INT_NUMBER")
	private String intNumber;

	@Id
	@Column(name = "DOC_ID")
	private Long docId;
	
	
	@Column(name = "INT_ID")
	private Long intId;
	
	@Column(name = "FILE_NAME")
	private String fileName;
	
	@Column(name = "DMS_URL")
	private String dmrUrl;
	
	@Column(name = "CREATED_ON")
	private Date createdOn;
	
	@Column(name = "DOC_GLX_READ_FLAG")
	private String docGlxReadFlag;

	public String getIntNumber() {
		return intNumber;
	}

	public void setIntNumber(String intNumber) {
		this.intNumber = intNumber;
	}

	public Long getDocId() {
		return docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

	public Long getIntId() {
		return intId;
	}

	public void setIntId(Long intId) {
		this.intId = intId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDmrUrl() {
		return dmrUrl;
	}

	public void setDmrUrl(String dmrUrl) {
		this.dmrUrl = dmrUrl;
	}


	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getDocGlxReadFlag() {
		return docGlxReadFlag;
	}

	public void setDocGlxReadFlag(String docGlxReadFlag) {
		this.docGlxReadFlag = docGlxReadFlag;
	}
	
}
