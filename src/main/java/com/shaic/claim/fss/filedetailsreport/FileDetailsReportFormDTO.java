package com.shaic.claim.fss.filedetailsreport;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class FileDetailsReportFormDTO extends AbstractSearchDTO implements Serializable {
	private Date fromDate;
	private Date toDate;
	private SelectValue statusType;
	
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
	
	public SelectValue getStatusType() {
		return statusType;
	}
	public void setStatusType(SelectValue statusType) {
		this.statusType = statusType;
	}
	
}
