/**
 * 
 */
package com.shaic.reimbursement.rod.createrod.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

/**
 * @author ntv.narenj
 *
 */
public class SearchCreateRODFormDTO extends AbstractSearchDTO implements Serializable{
	
	
	private String intimationNo;
	private SelectValue cpuCode;
	private String policyNo;
	private String acknowledgementNo;
	private SelectValue isDocumentUploaded;
	private SelectValue accidentOrDeath;	
	private SelectValue priorityNew;
	
	
	
	public SelectValue getAccidentOrDeath() {
		return accidentOrDeath;
	}
	public void setAccidentOrDeath(SelectValue accidentOrDeath) {
		this.accidentOrDeath = accidentOrDeath;
	}
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
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getAcknowledgementNo() {
		return acknowledgementNo;
	}
	public void setAcknowledgementNo(String acknowledgementNo) {
		this.acknowledgementNo = acknowledgementNo;
	}
	public SelectValue getIsDocumentUploaded() {
		return isDocumentUploaded;
	}
	public void setIsDocumentUploaded(SelectValue isDocumentUploaded) {
		this.isDocumentUploaded = isDocumentUploaded;
	}
	public SelectValue getPriorityNew() {
		return priorityNew;
	}
	public void setPriorityNew(SelectValue priorityNew) {
		this.priorityNew = priorityNew;
	}

	
}
