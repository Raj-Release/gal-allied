package com.shaic.reimbursement.draftinvesigation;

import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class DraftTriggerPointsToFocusDetailsTableDto extends AbstractTableDTO {
	
	private Integer sno;
	private Long investigationDetailKey;
	private Long reimbKey;
	private Long investigationKey;
	private String remarks;
	private String processType;
	private String deltedFlag;
	private Date createdDate;
	private Date modifiedDate;
	
	
	public Integer getSno() {
		return sno;
	}
	public void setSno(Integer sno) {
		this.sno = sno;
	}
	public Long getInvestigationDetailKey() {
		return investigationDetailKey;
	}
	public void setInvestigationDetailKey(Long investigationDetailKey) {
		this.investigationDetailKey = investigationDetailKey;
	}
	public Long getReimbKey() {
		return reimbKey;
	}
	public void setReimbKey(Long reimbKey) {
		this.reimbKey = reimbKey;
	}
	public Long getInvestigationKey() {
		return investigationKey;
	}
	public void setInvestigationKey(Long investigationKey) {
		this.investigationKey = investigationKey;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
	
	

}
