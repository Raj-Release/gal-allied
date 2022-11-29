package com.shaic.claim.viewEarlierRodDetails;

import com.shaic.arch.table.AbstractTableDTO;

public class ViewClosureDto extends AbstractTableDTO {

	private Integer sNo;
	
	private String intimationNo;
	
	private String rodNo;
	
	private String closedDate;
	
	private String closedBy;
	
	private String reasonForClosure;

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getRodNo() {
		return rodNo;
	}

	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}

	public String getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(String closedDate) {
		this.closedDate = closedDate;
	}

	public String getClosedBy() {
		return closedBy;
	}

	public void setClosedBy(String closedBy) {
		this.closedBy = closedBy;
	}

	public String getReasonForClosure() {
		return reasonForClosure;
	}

	public void setReasonForClosure(String reasonForClosure) {
		this.reasonForClosure = reasonForClosure;
	}

	public Integer getsNo() {
		return sNo;
	}

	public void setsNo(Integer sNo) {
		this.sNo = sNo;
	}
	
}
