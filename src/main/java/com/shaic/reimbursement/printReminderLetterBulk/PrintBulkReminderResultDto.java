package com.shaic.reimbursement.printReminderLetterBulk;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PrintBulkReminderResultDto {
	
	private Long reminderDetailsKey;
	private String intimationNo;
	private Long intimationKey;
	private Long preauthKey;
	private Long claimKey;
	private String claimNo;
	private Long queryKey;
	private List<SearchPrintReminderBulkTableDTO> resultListDto;
	private String batchid;
	private String subBatchid;
	private Integer totalNoofRecords;
	private Date letterGeneratedDate;
	private String letterDateValue;
	private String cpuCode;
	private String claimType;
	private String documentReceivedFrom;
	private String category;
	private String reminderType;
	private String print;
	private Integer printCount;
	private Integer reminderCount;
	private String status;
	private Long docToken;
	private Integer sno;
	private String userName;
	
	
//	private List<String> selectedFileUrls;
	
	 
	public List<SearchPrintReminderBulkTableDTO> getResultListDto() {
		return resultListDto;
	}
	public void setResultListDto(
			List<SearchPrintReminderBulkTableDTO> resultListDto) {
		this.resultListDto = resultListDto;
	}
	public String getBatchid() {
		return batchid;
	}
	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}
	public String getSubBatchid() {
		return subBatchid;
	}
	public void setSubBatchid(String subBatchid) {
		this.subBatchid = subBatchid;
	}
	public Integer getTotalNoofRecords() {
		return totalNoofRecords;
	}
	public void setTotalNoofRecords(Integer totalNoofRecords) {
		this.totalNoofRecords = totalNoofRecords;
	}
	public Date getLetterGeneratedDate() {
		return letterGeneratedDate;
	}
	public void setLetterGeneratedDate(Date letterGeneratedDate) {
		this.letterGeneratedDate = letterGeneratedDate;
		letterDateValue = letterGeneratedDate != null ? new SimpleDateFormat("dd/MM/yyyy").format(letterGeneratedDate) : "";		
	}
	public String getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getDocumentReceivedFrom() {
		return documentReceivedFrom;
	}
	public void setDocumentReceivedFrom(String documentReceivedFrom) {
		this.documentReceivedFrom = documentReceivedFrom;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getReminderType() {
		return reminderType;
	}
	public void setReminderType(String reminderType) {
		this.reminderType = reminderType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPrint() {
		return print;
	}
	public void setPrint(String print) {
		this.print = print;
	}
	public Integer getReminderCount() {
		return reminderCount;
	}
	public void setReminderCount(Integer reminderCount) {
		this.reminderCount = reminderCount;
	}
	public Long getDocToken() {
		return docToken;
	}
	public void setDocToken(Long docToken) {
		this.docToken = docToken;
	}
	public Integer getSno() {
		return sno;
	}
	public void setSno(Integer sno) {
		this.sno = sno;
	}
//	public List<String> getSelectedFileUrls() {
//		return selectedFileUrls;
//	}
//	public void setSelectedFileUrls(List<String> selectedFileUrls) {
//		this.selectedFileUrls = selectedFileUrls;
//	}
	public String getLetterDateValue() {
		return letterDateValue;
	}
	public void setLetterDateValue(String letterDateValue) {
		this.letterDateValue = letterDateValue;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public Long getIntimationKey() {
		return intimationKey;
	}
	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}
	public Long getPreauthKey() {
		return preauthKey;
	}
	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public Long getQueryKey() {
		return queryKey;
	}
	public void setQueryKey(Long queryKey) {
		this.queryKey = queryKey;
	}
	public Integer getPrintCount() {
		return printCount;
	}
	public void setPrintCount(Integer printCount) {
		this.printCount = printCount;
	}
	public Long getReminderDetailsKey() {
		return reminderDetailsKey;
	}
	public void setReminderDetailsKey(Long reminderDetailsKey) {
		this.reminderDetailsKey = reminderDetailsKey;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	 
}
