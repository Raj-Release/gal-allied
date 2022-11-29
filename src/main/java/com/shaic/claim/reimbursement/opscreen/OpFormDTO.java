package com.shaic.claim.reimbursement.opscreen;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class OpFormDTO extends AbstractSearchDTO implements Serializable{

	private Date fromDate;
	private Date toDate;
	private SelectValue zoneType;
	
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
	public SelectValue getZoneType() {
		return zoneType;
	}
	public void setZoneType(SelectValue zoneType) {
		this.zoneType = zoneType;
	}
	
	
	
		
}
