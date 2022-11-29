package com.shaic.claim.bpc;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class ViewBusinessProfilChartDTO extends AbstractTableDTO  implements Serializable {
	
	private String intimationNumber;
	private String hospitalCode;
	private String employeeID;
	private String employeeName;
	private String hospitaldetails;
	private String smdetails;
	private String agentdetails;

	private String values;
	private Long slNo;
	private String labelName;
	private String colValue;
	
	
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public String getEmployeeID() {
		return employeeID;
	}
	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getValues() {
		return values;
	}
	public void setValues(String values) {
		this.values = values;
	}
	public Long getSlNo() {
		return slNo;
	}
	public void setSlNo(Long slNo) {
		this.slNo = slNo;
	}
	public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	public String getColValue() {
		return colValue;
	}
	public void setColValue(String colValue) {
		this.colValue = colValue;
	}
	public String getHospitaldetails() {
		return hospitaldetails;
	}
	public void setHospitaldetails(String hospitaldetails) {
		this.hospitaldetails = hospitaldetails;
	}
	public String getSmdetails() {
		return smdetails;
	}
	public void setSmdetails(String smdetails) {
		this.smdetails = smdetails;
	}
	public String getAgentdetails() {
		return agentdetails;
	}
	public void setAgentdetails(String agentdetails) {
		this.agentdetails = agentdetails;
	}

	

}
