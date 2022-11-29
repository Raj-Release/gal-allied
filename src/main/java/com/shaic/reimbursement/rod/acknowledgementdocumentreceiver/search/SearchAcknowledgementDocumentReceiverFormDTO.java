/**
 * 
 */
package com.shaic.reimbursement.rod.acknowledgementdocumentreceiver.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

/**
 * @author ntv.narenj
 *
 */
public class SearchAcknowledgementDocumentReceiverFormDTO extends AbstractSearchDTO implements Serializable{
	
	
	private String intimationNo;
	private String cpuCode;
	private String policyNo;
	private SelectValue accidentOrdeath;	
	
	
	public SelectValue getAccidentOrdeath() {
		return accidentOrdeath;
	}
	public void setAccidentOrdeath(SelectValue accidentOrdeath) {
		this.accidentOrdeath = accidentOrdeath;
	}
	public String getIntimationNo() {
		return intimationNo;
		
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getHealthCardIdNo() {
		return cpuCode;
	}
	public void setHealthCardIdNo(String healthCardIdNo) {
		this.cpuCode = healthCardIdNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	

}
