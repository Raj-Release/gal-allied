/**
 * 
 */
package com.shaic.reimbursement.queryrejection.draftquery.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

/**
 * @author ntv.narenj
 *
 */
@SuppressWarnings("serial")
public class SearchDraftQueryLetterFormDTO extends AbstractSearchDTO implements Serializable{
	
	
	private String intimationNo;
	private SelectValue cpuCode;
	private String claimNo;
	
	private SelectValue priorityNew;
	
	private long lobKey;
	
	private SelectValue queryType;
	
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
	public long getLobKey() {
		return lobKey;
	}
	public void setLobKey(long lobKey) {
		this.lobKey = lobKey;
	}
	public SelectValue getQueryType() {
		return queryType;
	}
	public void setQueryType(SelectValue queryType) {
		this.queryType = queryType;
	}
	public SelectValue getPriorityNew() {
		return priorityNew;
	}
	public void setPriorityNew(SelectValue priorityNew) {
		this.priorityNew = priorityNew;
	}

}
