package com.shaic.claim.reports.automationdashboard;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class AutomationDashboardTableDTO extends AbstractTableDTO  implements Serializable{
	
	private int serialNumber;
	private String intimationNo;
	private String date;
	private String intimationDate;
	private String status;
	private String lotNumber;
	private Double approvedAmount;
	private String hospitalName;
	private String hospitalCode;
	private String lotCreatedDate;
	private String batchNumber;
	private String batchCreatedDate;
	private String pmsNeftVerifedStatus;
	private String batchAutomated;
	
	public int getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getIntimationDate() {
		return intimationDate;
	}
	public void setIntimationDate(String intimationDate) {
		this.intimationDate = intimationDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	public Double getApprovedAmount() {
		return approvedAmount;
	}
	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public String getLotCreatedDate() {
		return lotCreatedDate;
	}
	public void setLotCreatedDate(String lotCreatedDate) {
		this.lotCreatedDate = lotCreatedDate;
	}
	public String getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	public String getBatchCreatedDate() {
		return batchCreatedDate;
	}
	public void setBatchCreatedDate(String batchCreatedDate) {
		this.batchCreatedDate = batchCreatedDate;
	}
	public String getPmsNeftVerifedStatus() {
		return pmsNeftVerifedStatus;
	}
	public void setPmsNeftVerifedStatus(String pmsNeftVerifedStatus) {
		this.pmsNeftVerifedStatus = pmsNeftVerifedStatus;
	}
	public String getBatchAutomated() {
		return batchAutomated;
	}
	public void setBatchAutomated(String batchAutomated) {
		this.batchAutomated = batchAutomated;
	}
	
}
