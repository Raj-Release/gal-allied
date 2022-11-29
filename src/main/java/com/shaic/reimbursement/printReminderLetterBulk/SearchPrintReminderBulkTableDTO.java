/**
 * 
 */
package com.shaic.reimbursement.printReminderLetterBulk;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;

/**
 * 
 *
 */
@SuppressWarnings("serial")
public class SearchPrintReminderBulkTableDTO extends AbstractTableDTO  implements Serializable{
	
	private String sno;
	private String reminderType;
	private String category;
	private String claimIntimationNo;
	private String intimationDate;
	private String patientName;
	private String hospitalName;
	private String hospitalAddress;
	private ReimbursementQueryDto reimbQueryDto;
	private ClaimDto claimDto;
	private String claimType;
	private String cpuCode;
	private String letterDate;
	private Long claimKey;
	private Long intimationKey;
	private Long preauthKey;
	private Long queryKey;
	private Long rodKey;
	private Boolean selected = false;
		
	private String batchid;
	private String subBatchId;
	private Long docToken;
	
	private Integer printCount;
	private String printFlag;
		
	private String fileUrl; 
	
	
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public String getReminderType() {
		return reminderType;
	}
	public void setReminderType(String reminderType) {
		this.reminderType = reminderType;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getClaimIntimationNo() {
		return claimIntimationNo;
	}
	public void setClaimIntimationNo(String claimIntimationNo) {
		this.claimIntimationNo = claimIntimationNo;
	}
	public String getIntimationDate() {
		return intimationDate;
	}
	public void setIntimationDate(String intimationDate) {
		this.intimationDate = intimationDate;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
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
	public ReimbursementQueryDto getReimbQueryDto() {
		return reimbQueryDto;
	}
	public void setReimbQueryDto(ReimbursementQueryDto reimbQueryDto) {
		this.reimbQueryDto = reimbQueryDto;
	}
	public ClaimDto getClaimDto() {
		return claimDto;
	}
	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}
	
	public Long getQueryKey() {
		return queryKey;
	}
	public void setQueryKey(Long queryKey) {
		this.queryKey = queryKey;
	}
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public Long getIntimationKey() {
		return intimationKey;
	}
	public void setIntimationKey(Long intimationkey) {
		this.intimationKey = intimationkey;
	}
	public Long getPreauthKey() {
		return preauthKey;
	}
	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	public String getBatchid() {
		return batchid;
	}
	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}
	public String getSubBatchId() {
		return subBatchId;
	}
	public void setSubBatchId(String subBatchId) {
		this.subBatchId = subBatchId;
	}
	public Long getDocToken() {
		return docToken;
	}
	public void setDocToken(Long docToken) {
		this.docToken = docToken;
	}
	public Integer getPrintCount() {
		return printCount;
	}
	public void setPrintCount(Integer printCount) {
		this.printCount = printCount;
	}
	public String getPrintFlag() {
		return printFlag;
	}
	public void setPrintFlag(String printFlag) {
		this.printFlag = printFlag;
	}
	public String getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getLetterDate() {
		return letterDate;
	}
	public void setLetterDate(String letterDate) {
		this.letterDate = letterDate;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}	
	
}
