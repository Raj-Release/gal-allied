package com.shaic.claim.activitylog;

import java.util.Date;

public class ActivityLogFormDto {
	
	private String empId;
	private String empName;
	private String empIdName;
	private String intimationNo;
	private Date activityDate;
	
	public ActivityLogFormDto() {
		super();
	}

	public ActivityLogFormDto(String empId, String empName,
			String intimationNo, Date activityDate) {
		super();
		this.empId = empId;
		this.empName = empName;
		this.intimationNo = intimationNo;
		this.activityDate = activityDate;
	}
	
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getEmpIdName() {
		return empIdName;
	}
	public void setEmpIdName(String empIdName) {
		this.empIdName = empIdName;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public Date getActivityDate() {
		return activityDate;
	}
	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}
	
	@Override
	public String toString() {
		return "ActivityLogFormDto [empId=" + empId + ", empName=" + empName
				+ ", empIdName=" + empIdName + ", intimationNo=" + intimationNo
				+ ", activityDate=" + activityDate + "]";
	}
}
