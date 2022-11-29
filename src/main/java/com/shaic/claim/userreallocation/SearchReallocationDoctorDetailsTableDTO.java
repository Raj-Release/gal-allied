package com.shaic.claim.userreallocation;

import java.io.Serializable;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SearchReallocationDoctorDetailsTableDTO extends AbstractTableDTO  implements Serializable {
	
	private String empId;
	
	private String doctorName;
	
	private String loginId;
	
	private String loginDate;
	
	private SelectValue status;
	
	private Integer assignedCount;
	
	private Integer completedCount;
	
	private Integer pendingCount;
	
	private Integer serialNo;
	
	private Boolean chkSelect;
	
	private String checkBoxStatus;
	
	private Integer statusValue;
	
	private List<AutoAllocationDetailsTableDTO> intimationDetailsList;
	
	private BeanItemContainer<SelectValue> statusList;
	
	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}

	public SelectValue getStatus() {
		return status;
	}

	public void setStatus(SelectValue status) {
		this.status = status;
	}

	public Integer getAssignedCount() {
		return assignedCount;
	}

	public void setAssignedCount(Integer assignedCount) {
		this.assignedCount = assignedCount;
	}

	public Integer getCompletedCount() {
		return completedCount;
	}

	public void setCompletedCount(Integer completedCount) {
		this.completedCount = completedCount;
	}

	public Integer getPendingCount() {
		return pendingCount;
	}

	public void setPendingCount(Integer pendingCount) {
		this.pendingCount = pendingCount;
	}

	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public Boolean getChkSelect() {
		return chkSelect;
	}

	public void setChkSelect(Boolean chkSelect) {
		this.chkSelect = chkSelect;
	}

	public String getCheckBoxStatus() {
		return checkBoxStatus;
	}

	public void setCheckBoxStatus(String checkBoxStatus) {
		this.checkBoxStatus = checkBoxStatus;
	}

	public Integer getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(Integer statusValue) {
		this.statusValue = statusValue;
	}

	public BeanItemContainer<SelectValue> getStatusList() {
		return statusList;
	}

	public void setStatusList(BeanItemContainer<SelectValue> statusList) {
		this.statusList = statusList;
	}

	public List<AutoAllocationDetailsTableDTO> getIntimationDetailsList() {
		return intimationDetailsList;
	}

	public void setIntimationDetailsList(
			List<AutoAllocationDetailsTableDTO> intimationDetailsList) {
		this.intimationDetailsList = intimationDetailsList;
	}
	
	

}
