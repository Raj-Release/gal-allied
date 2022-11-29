package com.shaic.reimbursement.investigationmaster;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class InvestigationMasterFormDTO extends AbstractSearchDTO implements Serializable{
	
	
	private SelectValue investigatorType;
	private SelectValue investigatorName;
	
	public SelectValue getInvestigatorType() {
		return investigatorType;
	}
	public void setInvestigatorType(SelectValue investiogatorType) {
		this.investigatorType = investiogatorType;
	}
	public SelectValue getInvestigatorName() {
		return investigatorName;
	}
	public void setInvestigatorName(SelectValue investigatorName) {
		this.investigatorName = investigatorName;
	}
	
	
}
