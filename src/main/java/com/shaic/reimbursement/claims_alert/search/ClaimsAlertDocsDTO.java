package com.shaic.reimbursement.claims_alert.search;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;

public class ClaimsAlertDocsDTO implements Serializable{

	private Integer sNo;

	private Long key; 

	private Long claimsAlertKey;

	private String fileName;

	private String fileToken;
	
	private String docsFrom;

	public Integer getsNo() {
		return sNo;
	}

	public void setsNo(Integer sNo) {
		this.sNo = sNo;
	}

	public Long getClaimsAlertKey() {
		return claimsAlertKey;
	}

	public void setClaimsAlertKey(Long claimsAlertKey) {
		this.claimsAlertKey = claimsAlertKey;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileToken() {
		return fileToken;
	}

	public void setFileToken(String fileToken) {
		this.fileToken = fileToken;
	}

	public String getDocsFrom() {
		return docsFrom;
	}

	public void setDocsFrom(String docsFrom) {
		this.docsFrom = docsFrom;
	}
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

}
