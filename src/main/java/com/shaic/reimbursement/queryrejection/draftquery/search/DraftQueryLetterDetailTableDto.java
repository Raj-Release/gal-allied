package com.shaic.reimbursement.queryrejection.draftquery.search;

import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class DraftQueryLetterDetailTableDto  extends AbstractTableDTO{

	private int sno;
	private Long queryDetailKey;
	private Long reimbKey;
	private Long queryKey;
	private String draftOrRedraftRemarks;
	private String processType;
	private String deltedFlag;
	private Date createdDate;
	private Date modifiedDate;
	
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	public String getDraftOrRedraftRemarks() {
		return draftOrRedraftRemarks;
	}
	public void setDraftOrRedraftRemarks(String draftOrRedraftRemarks) {
		this.draftOrRedraftRemarks = draftOrRedraftRemarks;
	}
	public String getProcessType() {
		return processType;
	}
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	public String getDeltedFlag() {
		return deltedFlag;
	}
	public void setDeltedFlag(String deltedFlag) {
		this.deltedFlag = deltedFlag;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public Long getQueryDetailKey() {
		return queryDetailKey;
	}
	public void setQueryDetailKey(Long queryDetailKey) {
		this.queryDetailKey = queryDetailKey;
	}
	public Long getReimbKey() {
		return reimbKey;
	}
	public void setReimbKey(Long reimbKey) {
		this.reimbKey = reimbKey;
	}
	public Long getQueryKey() {
		return queryKey;
	}
	public void setQueryKey(Long queryKey) {
		this.queryKey = queryKey;
	}
	
	
}
