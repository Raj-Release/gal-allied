package com.shaic.claim.lumen;

import java.io.Serializable;
import java.util.Date;

import com.shaic.domain.LumenRequest;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;

@SuppressWarnings("serial")
public class LumenDetailsDTO implements Serializable{
	
	private LumenRequest lumenRequest;
	private String participantType;
	private String employeeId;
	private String employeeName;
	private String employeeZone;
	private String employeeDept;
	private Status status;
	private Stage stage;
	// check this is really needed or not
	private String createdBy;
	private Date createdDate;
	
	public LumenRequest getLumenRequest() {
		return lumenRequest;
	}
	public String getParticipantType() {
		return participantType;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public String getEmployeeZone() {
		return employeeZone;
	}
	public String getEmployeeDept() {
		return employeeDept;
	}
	public Status getStatus() {
		return status;
	}
	public Stage getStage() {
		return stage;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setLumenRequest(LumenRequest lumenRequest) {
		this.lumenRequest = lumenRequest;
	}
	public void setParticipantType(String participantType) {
		this.participantType = participantType;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public void setEmployeeZone(String employeeZone) {
		this.employeeZone = employeeZone;
	}
	public void setEmployeeDept(String employeeDept) {
		this.employeeDept = employeeDept;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
}
