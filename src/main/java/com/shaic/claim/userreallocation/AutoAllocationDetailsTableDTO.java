package com.shaic.claim.userreallocation;

import com.shaic.arch.table.AbstractTableDTO;

public class AutoAllocationDetailsTableDTO extends AbstractTableDTO {

	private Integer sNumber;
	private String intimationNo;
	private String doctorId;
	private String doctorName;
	private Double claimedAmt;
	private Long cpu;
	private String assignedDate;
	private String completedDate;
	private Boolean chkSelect = false;
	private Long reAllocateTo;
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public Double getClaimedAmt() {
		return claimedAmt;
	}
	public void setClaimedAmt(Double claimedAmt) {
		this.claimedAmt = claimedAmt;
	}
	public String getCpu() {
		return cpu != null ? String.valueOf(cpu) : "";
	}
	public void setCpu(Long cpu) {
		this.cpu = cpu;
	}
	public String getAssignedDate() {
		return assignedDate;
	}
	public void setAssignedDate(String assignedDate) {
		this.assignedDate = assignedDate;
	}
	public String getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(String completedDate) {
		this.completedDate = completedDate;
	}
	public Boolean getChkSelect() {
		return chkSelect;
	}
	public void setChkSelect(Boolean chkSelect) {
		this.chkSelect = chkSelect;
	}
	public Integer getsNumber() {
		return sNumber;
	}
	public void setsNumber(Integer sNumber) {
		this.sNumber = sNumber;
	}
	public Long getReAllocateTo() {
		return reAllocateTo;
	}
	public void setReAllocateTo(Long reAllocateTo) {
		this.reAllocateTo = reAllocateTo;
	}
	
}
