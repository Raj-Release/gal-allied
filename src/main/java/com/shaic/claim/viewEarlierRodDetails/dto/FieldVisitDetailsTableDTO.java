package com.shaic.claim.viewEarlierRodDetails.dto;

import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class FieldVisitDetailsTableDTO extends AbstractTableDTO{
	
	private Integer sno;
	
	private String representiveName;
	
	private String remarks;
	
	private Date fvrAssignedDate;
	
	private Date fvrReceivedDate;
	
	private String status;

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

	public String getRepresentiveName() {
		return representiveName;
	}

	public void setRepresentiveName(String representiveName) {
		this.representiveName = representiveName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getFvrAssignedDate() {
		return fvrAssignedDate;
	}

	public void setFvrAssignedDate(Date fvrAssignedDate) {
		this.fvrAssignedDate = fvrAssignedDate;
	}

	public Date getFvrReceivedDate() {
		return fvrReceivedDate;
	}

	public void setFvrReceivedDate(Date fvrReceivedDate) {
		this.fvrReceivedDate = fvrReceivedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
