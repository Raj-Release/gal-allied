/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.services;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

/**
 * @author ntv.vijayar
 *
 */
public class SearchProcessRRCRequestFormDTO  extends AbstractSearchDTO implements Serializable {
	
	private String intimationNo;
	
	private SelectValue cpu;
	
	private String rrcRequestNo;
	
	private SelectValue rrcRequestType;

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public SelectValue getCpu() {
		return cpu;
	}

	public void setCpu(SelectValue cpu) {
		this.cpu = cpu;
	}

	public String getRrcRequestNo() {
		return rrcRequestNo;
	}

	public void setRrcRequestNo(String rrcRequestNo) {
		this.rrcRequestNo = rrcRequestNo;
	}

	public SelectValue getRrcRequestType() {
		return rrcRequestType;
	}

	public void setRrcRequestType(SelectValue rrcRequestType) {
		this.rrcRequestType = rrcRequestType;
	}
}
