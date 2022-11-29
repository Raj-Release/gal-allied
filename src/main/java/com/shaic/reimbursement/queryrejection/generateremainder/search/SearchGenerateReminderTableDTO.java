/**
 * 
 */
package com.shaic.reimbursement.queryrejection.generateremainder.search;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;

/**
 * @author ntv.narenj
 *
 */
public class SearchGenerateReminderTableDTO extends AbstractTableDTO  implements Serializable{
	
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
	private Long claimKey;
	private Long intimationKey;
	private Long preauthKey;
	private Long queryKey;
	private Long rodKey;
	private String batchId;
	
	private Long docToken;
	private Date generatedDate;
	
	private String fileUrl; 
	
	//Added for reminder batch
	private String presenterString = "";
	
	
	public String getPresenterString() {
		return presenterString;
	}
	public void setPresenterString(String presenterString) {
		this.presenterString = presenterString;
	}
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
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public Long getDocToken() {
		return docToken;
	}
	public void setDocToken(Long docToken) {
		this.docToken = docToken;
	}
	public Date getGeneratedDate() {
		return generatedDate;
	}
	public void setGeneratedDate(Date generatedDate) {
		this.generatedDate = generatedDate;
	}
//	public String getUserName() {
//		return userName;
//	}
//	public void setUserName(String userName) {
//		this.userName = userName;
//	}	
	
	
//	private String insuredPatientName;
//	private String cpuCode;
//	private Long hospitalNameID;
//	private String hospitalType;
//	private Long cpuId;
//	private String intimationNo;
//	private String claimNo;
//	private String policyNo;
//	
//	private String hospitalCity;
//	private String reasonForAdmission;
//	private String claimStatus;

	
}
