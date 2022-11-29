package com.shaic.claim.pcc.dto;

import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;

public class PCCApproveDetailsTableDTO extends AbstractTableDTO{

	private static final long serialVersionUID = 6516581852249166232L;

	private String remarks;

	private String raiseRole;

	private String raiseBy;

	private Date raiseDate;
	
	private String status;

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRaiseBy() {
		return raiseBy;
	}

	public void setRaiseBy(String raiseBy) {
		this.raiseBy = raiseBy;
	}

	public Date getRaiseDate() {
		return raiseDate;
	}

	public void setRaiseDate(Date raiseDate) {
		this.raiseDate = raiseDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRaiseRole() {
		return raiseRole;
	}

	public void setRaiseRole(String raiseRole) {
		this.raiseRole = raiseRole;
	}
	
}
