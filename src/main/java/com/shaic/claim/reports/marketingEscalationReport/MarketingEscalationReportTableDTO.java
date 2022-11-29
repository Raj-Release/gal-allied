package com.shaic.claim.reports.marketingEscalationReport;

import java.io.Serializable;
import java.sql.Timestamp;

import com.shaic.arch.table.AbstractTableDTO;

public class MarketingEscalationReportTableDTO extends AbstractTableDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6576638928664161995L;
	private String intimationNo;
	private String cpuName;
	private String zone;
	private String hospitalName;
	private String escalatedDate;
	private String escalatedRole;
	private String escalatedBy;
	private String reasonForEscalation;
	private String actionTaken;
	private String doctorRemarks;
	private String recordedDate;
	private String recordedBy;
	private String productNameCode;
	private String lackOfMrkPersonnel;
	private String claimType;
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getCpuName() {
		return cpuName;
	}
	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getEscalatedDate() {
		return escalatedDate;
	}
	public void setEscalatedDate(String escalatedDate) {
		this.escalatedDate = escalatedDate;
	}
	public String getEscalatedRole() {
		return escalatedRole;
	}
	public void setEscalatedRole(String escalatedRole) {
		this.escalatedRole = escalatedRole;
	}
	public String getEscalatedBy() {
		return escalatedBy;
	}
	public void setEscalatedBy(String escalatedBy) {
		this.escalatedBy = escalatedBy;
	}
	public String getReasonForEscalation() {
		return reasonForEscalation;
	}
	public void setReasonForEscalation(String reasonForEscalation) {
		this.reasonForEscalation = reasonForEscalation;
	}
	public String getActionTaken() {
		return actionTaken;
	}
	public void setActionTaken(String actionTaken) {
		this.actionTaken = actionTaken;
	}
	public String getDoctorRemarks() {
		return doctorRemarks;
	}
	public void setDoctorRemarks(String doctorRemarks) {
		this.doctorRemarks = doctorRemarks;
	}
	public String getRecordedDate() {
		return recordedDate;
	}
	public void setRecordedDate(String recordedDate) {
		this.recordedDate = recordedDate;
	}
	public String getRecordedBy() {
		return recordedBy;
	}
	public void setRecordedBy(String recordedBy) {
		this.recordedBy = recordedBy;
	}
	public String getProductNameCode() {
		return productNameCode;
	}
	public void setProductNameCode(String productNameCode) {
		this.productNameCode = productNameCode;
	}
	public String getLackOfMrkPersonnel() {
		return lackOfMrkPersonnel;
	}
	public void setLackOfMrkPersonnel(String lackOfMrkPersonnel) {
		this.lackOfMrkPersonnel = lackOfMrkPersonnel;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	
	
	
	

}
