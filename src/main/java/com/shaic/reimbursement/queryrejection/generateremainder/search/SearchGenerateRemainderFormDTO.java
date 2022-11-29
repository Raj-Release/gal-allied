/**
 * 
 */
package com.shaic.reimbursement.queryrejection.generateremainder.search;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.shaic.domain.ReferenceTable;

/**
 * @author ntv.narenj
 *
 */
public class SearchGenerateRemainderFormDTO extends AbstractSearchDTO implements Serializable{
	
	
	private String intimationNo;
	private SelectValue cpuCode;
	private String claimNo;
	private SelectValue claimType; 
	private SelectValue category;
	private SelectValue reminderType;
	private Date reminderDate;
	private long lobId = ReferenceTable.HEALTH_LOB_KEY;
	
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
	public long getLobId() {
		return lobId;
	}
	public void setLobId(long lobId) {
		this.lobId = lobId;
	}

}
