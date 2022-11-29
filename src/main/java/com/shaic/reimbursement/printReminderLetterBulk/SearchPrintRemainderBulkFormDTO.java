/**
 * 
 */
package com.shaic.reimbursement.printReminderLetterBulk;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

/**
 * 
 *
 */
public class SearchPrintRemainderBulkFormDTO extends AbstractSearchDTO implements Serializable{
	
	private SelectValue searchOption;
	private String intimationNo;
	private SelectValue cpuCode;
	private String claimNo;
	private SelectValue claimType; 
	private SelectValue category;
	private SelectValue reminderType;
	private Date reminderDate;
	private Date fromDate;
	private Date toDate;
	private String batchId;
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	
	public SelectValue getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(SelectValue cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public SelectValue getClaimType() {
		return claimType;
	}
	public void setClaimType(SelectValue claimType) {
		this.claimType = claimType;
	}
	public SelectValue getCategory() {
		return category;
	}
	public void setCategory(SelectValue category) {
		this.category = category;
	}
	public SelectValue getReminderType() {
		return reminderType;
	}
	public void setReminderType(SelectValue reminderType) {
		this.reminderType = reminderType;
	}
	public Date getReminderDate() {
		return reminderDate;
	}
	public void setReminderDate(Date reminderDate) {
		this.reminderDate = reminderDate;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public SelectValue getSearchOption() {
		return searchOption;
	}
	public void setSearchOption(SelectValue searchOption) {
		this.searchOption = searchOption;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

}
