package com.shaic.reimbursement.rod.acknowledgementdocumentreceiver.search;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class SearchAcknowledgementDocumentPendingTableDTO extends AbstractTableDTO  implements Serializable{
	
	private String intimationNo;
	private String claimNo;
	private String policyNo;
	private String insuredPatientName;
	private Long  cpuCode;
	private String hospitalName;
	private String hospitalAddress;
	private String hospitalCity;
	private Date dateofAdmission;
	private String rodNo;
	private String rodStatus;
	
	public SearchAcknowledgementDocumentPendingTableDTO( )
	{
		
	}
	
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getInsuredPatientName() {
		return insuredPatientName;
	}
	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}
	public Long getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getHospitalAddress() {
		return hospitalAddress;
	}
	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}
	public String getHospitalCity() {
		return hospitalCity;
	}
	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}
	public Date getDateofAdmission() {
		return dateofAdmission;
	}
	public void setDateofAdmission(Date dateofAdmission) {
		this.dateofAdmission = dateofAdmission;
	}
	public String getRodNo() {
		return rodNo;
	}
	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}
	public String getRodStatus() {
		return rodStatus;
	}
	public void setRodStatus(String rodStatus) {
		this.rodStatus = rodStatus;
	}
	
	
	
	

}
