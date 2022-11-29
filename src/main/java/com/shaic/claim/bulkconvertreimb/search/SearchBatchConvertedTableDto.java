package com.shaic.claim.bulkconvertreimb.search;

import java.util.Date;
import java.util.List;

public class SearchBatchConvertedTableDto {

	private int sno;
	private String crNo;
	private String subCrNo;
	private Date letterDate;
	private String letterDateValue;
	private String cpuCode;
	private String category;
	private int noofRecords;
	private String status;
	private String printTaken;
	private Long printCount;
			
	List<SearchBulkConvertReimbTableDto> exportList;
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	public String getCrNo() {
		return crNo;
	}
	public void setCrNo(String crNo) {
		this.crNo = crNo;
	}
	public String getSubCrNo() {
		return subCrNo;
	}
	public void setSubCrNo(String subCrNo) {
		this.subCrNo = subCrNo;
	}
	public Date getLetterDate() {
		return letterDate;
	}
	public void setLetterDate(Date letterDate) {
		this.letterDate = letterDate;
	}
	public String getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getNoofRecords() {
		return noofRecords;
	}
	public void setNoofRecords(int noofRecords) {
		this.noofRecords = noofRecords;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<SearchBulkConvertReimbTableDto> getExportList() {
		return exportList;
	}
	public void setExportList(List<SearchBulkConvertReimbTableDto> exportList) {
		this.exportList = exportList;
	}
	public String getLetterDateValue() {
		return letterDateValue;
	}
	public void setLetterDateValue(String letterDateValue) {
		this.letterDateValue = letterDateValue;
	}
	public String getPrintTaken() {
		return printTaken;
	}
	public void setPrintTaken(String printTaken) {
		this.printTaken = printTaken;
	}
	
	public Long getPrintCount() {
		return printCount;
	}
	public void setPrintCount(Long printCount) {
		this.printCount = printCount;
	}
}
