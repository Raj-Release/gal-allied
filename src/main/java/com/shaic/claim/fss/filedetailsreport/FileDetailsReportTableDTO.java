package com.shaic.claim.fss.filedetailsreport;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class FileDetailsReportTableDTO extends AbstractTableDTO  implements Serializable{
	
	private String clientName;
	private String storagelocation;
	private String rackDesc;
	private String shelfDesc;
	private String claimNumber;
	private String patientName;
	private Integer year;
	private String almirahNo;
	//private Long addlShelfDesc;
	private String bundleNo;
	private String fileStatus;
	
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getStoragelocation() {
		return storagelocation;
	}
	public void setStoragelocation(String storagelocation) {
		this.storagelocation = storagelocation;
	}
	public String getRackDesc() {
		return rackDesc;
	}
	public void setRackDesc(String rackDesc) {
		this.rackDesc = rackDesc;
	}
	public String getShelfDesc() {
		return shelfDesc;
	}
	public void setShelfDesc(String shelfDesc) {
		this.shelfDesc = shelfDesc;
	}
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public String getAlmirahNo() {
		return almirahNo;
	}
	public void setAlmirahNo(String almirahNo) {
		this.almirahNo = almirahNo;
	}
	/*public Long getAddlShelfDesc() {
		return addlShelfDesc;
	}
	public void setAddlShelfDesc(Long addlShelfDesc) {
		this.addlShelfDesc = addlShelfDesc;
	}*/
	public String getFileStatus() {
		return fileStatus;
	}
	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}
	public String getBundleNo() {
		return bundleNo;
	}
	public void setBundleNo(String bundleNo) {
		this.bundleNo = bundleNo;
	}
	
	
}
