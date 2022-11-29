package com.shaic.claim.cashlessprocess.withdrawpreauthpostprocess;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;

public class WithDrawPostProcessBillDetailsDTO extends AbstractTableDTO implements Serializable{
	
	private String claimNo;
	private List<DMSDocumentDetailsDTO> dmsDocumentDTOList ;
	private String rodNo="";
	private Date docReceivedDate;
	private String modeOfReceipt;
	private String remstatus;
	private Long remstatusKey;
	private Double approvedAmount;
	private String intimationNo;
	private String dateOfAdmission;
	private String dateOfDischarge;
	private String insuredPatientName;
	private String billClassification;
	private Boolean status;
	private PreauthDTO preauthDto;
	private Long ackDocKey;
	private Long rodKey;
	private List<UploadDocumentDTO> uploadDocList;
	private List<BillEntryDetailsDTO> billEntryDetailList;
	private String fileType;
	private Boolean isReconsiderationRequest = false;
	private Integer rodVersionCount;
	
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public List<DMSDocumentDetailsDTO> getDmsDocumentDTOList() {
		return dmsDocumentDTOList;
	}
	public void setDmsDocumentDTOList(List<DMSDocumentDetailsDTO> dmsDocumentDTOList) {
		this.dmsDocumentDTOList = dmsDocumentDTOList;
	}
	public String getRodNo() {
		return rodNo;
	}
	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}
	public Date getDocReceivedDate() {
		return docReceivedDate;
	}
	public void setDocReceivedDate(Date docReceivedDate) {
		this.docReceivedDate = docReceivedDate;
	}
	public String getModeOfReceipt() {
		return modeOfReceipt;
	}
	public void setModeOfReceipt(String modeOfReceipt) {
		this.modeOfReceipt = modeOfReceipt;
	}
	public String getRemstatus() {
		return remstatus;
	}
	public void setRemstatus(String remstatus) {
		this.remstatus = remstatus;
	}
	public Long getRemstatusKey() {
		return remstatusKey;
	}
	public void setRemstatusKey(Long remstatusKey) {
		this.remstatusKey = remstatusKey;
	}
	public Double getApprovedAmount() {
		return approvedAmount;
	}
	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getDateOfAdmission() {
		return dateOfAdmission;
	}
	public void setDateOfAdmission(String dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}
	public String getDateOfDischarge() {
		return dateOfDischarge;
	}
	public void setDateOfDischarge(String dateOfDischarge) {
		this.dateOfDischarge = dateOfDischarge;
	}
	public String getInsuredPatientName() {
		return insuredPatientName;
	}
	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}
	public String getBillClassification() {
		return billClassification;
	}
	public void setBillClassification(String billClassification) {
		this.billClassification = billClassification;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public PreauthDTO getPreauthDto() {
		return preauthDto;
	}
	public void setPreauthDto(PreauthDTO preauthDto) {
		this.preauthDto = preauthDto;
	}
	public Long getAckDocKey() {
		return ackDocKey;
	}
	public void setAckDocKey(Long ackDocKey) {
		this.ackDocKey = ackDocKey;
	}
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	public List<UploadDocumentDTO> getUploadDocList() {
		return uploadDocList;
	}
	public void setUploadDocList(List<UploadDocumentDTO> uploadDocList) {
		this.uploadDocList = uploadDocList;
	}
	public List<BillEntryDetailsDTO> getBillEntryDetailList() {
		return billEntryDetailList;
	}
	public void setBillEntryDetailList(List<BillEntryDetailsDTO> billEntryDetailList) {
		this.billEntryDetailList = billEntryDetailList;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public Boolean getIsReconsiderationRequest() {
		return isReconsiderationRequest;
	}
	public void setIsReconsiderationRequest(Boolean isReconsiderationRequest) {
		this.isReconsiderationRequest = isReconsiderationRequest;
	}
	public Integer getRodVersionCount() {
		return rodVersionCount;
	}
	public void setRodVersionCount(Integer rodVersionCount) {
		this.rodVersionCount = rodVersionCount;
	}

}
