package com.shaic.claim.reports.paymentbatchreport;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class PaymentBatchReportFormDTO extends AbstractTableDTO  implements Serializable{

	private String lotNoFrom;
	private String lotNoTo;
	private Date fromDate;
	private Date toDate;
	public String getLotNoFrom() {
		return lotNoFrom;
	}
	public void setLotNoFrom(String lotNoFrom) {
		this.lotNoFrom = lotNoFrom;
	}
	public String getLotNoTo() {
		return lotNoTo;
	}
	public void setLotNoTo(String lotNoTo) {
		this.lotNoTo = lotNoTo;
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
	
	
	
	
}
