/**
 * 
 */
package com.shaic.reimbursement.rod.enterbilldetails.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

/**
 * @author ntv.narenj
 *
 */
public class SearchEnterBillDetailFormDTO extends AbstractSearchDTO implements Serializable{
	
	
	private String intimationNo;
	private SelectValue cpuCode;
	private String policyNo;
	private SelectValue accidentOrdeath;
	
	private SelectValue priorityNew;	
	
	
	private SelectValue billClassification;
	
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
	public SelectValue getBillClassification() {
		return billClassification;
	}
	public void setBillClassification(SelectValue billClassification) {
		this.billClassification = billClassification;
	}
	public SelectValue getAccidentOrdeath() {
		return accidentOrdeath;
	}
	public void setAccidentOrdeath(SelectValue accidentOrdeath) {
		this.accidentOrdeath = accidentOrdeath;
	}
	public SelectValue getPriorityNew() {
		return priorityNew;
	}
	public void setPriorityNew(SelectValue priorityNew) {
		this.priorityNew = priorityNew;
	}
	

}
