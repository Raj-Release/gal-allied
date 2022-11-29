package com.shaic.reimbursement.investigationmaster;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class InvestigationMasterTableDTO extends AbstractTableDTO  implements Serializable{

	private int serialNumber;
	private String investigatorId;
	private String investigatorName;
	private String investigatorType;
	private String mobileNo;
	private String state;
	private String city;
	private Long status;
	private String statusToDisplay;
	
	public int getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getInvestigatorId() {
		return investigatorId;
	}
	public void setInvestigatorId(String investigatorId) {
		this.investigatorId = investigatorId;
	}
	public String getInvestigatorType() {
		return investigatorType;
	}
	public void setInvestigatorType(String investigatorType) {
		this.investigatorType = investigatorType;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public String getInvestigatorName() {
		return investigatorName;
	}
	public void setInvestigatorName(String investigatorName) {
		this.investigatorName = investigatorName;
	}
	public String getStatusToDisplay() {
		return statusToDisplay;
	}
	public void setStatusToDisplay(String statusToDisplay) {
		this.statusToDisplay = statusToDisplay;
	}
	
	
}
