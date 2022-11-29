package com.shaic.claim.preauth;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;

public class HRMTableDTO extends AbstractTableDTO  implements Serializable{
	
	private String anhOrNanh;
	private String diagnosis;
	private String surgicalProcedure;
	private Double claimedAmt;
	private Long packageAmt;
	private SelectValue requestType;
	private String requestTypeValue;
	private String docRemarks;
	private Date assigneeDateAndTime;
	private String hrmReplyRemarks;
	private Date replyDateAndTime;
	private String docUserId;
	private String docName;
	private String docDeskNumber;
	private String intimationNO;
	private String hrmId;
	private Long cashlessKey;	
	private String hospitalCode;
	private String assigneeDateAndTimeStr;
	
	

	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public String getRequestTypeValue() { 
		return requestTypeValue;
	}
	public void setRequestTypeValue(String requestTypeValue) {
		this.requestTypeValue = requestTypeValue;
	}
	public String getHrmId() {
		return hrmId;
	}
	public Long getCashlessKey() {
		return cashlessKey;
	}
	public void setCashlessKey(Long cashlessKey) {
		this.cashlessKey = cashlessKey;
	}
	public void setHrmId(String hrmId) {
		this.hrmId = hrmId;
	}
	public String getIntimationNO() {
		return intimationNO;
	}
	public void setIntimationNO(String intimationNO) {
		this.intimationNO = intimationNO;
	}
	public String getAnhOrNanh() {
		return anhOrNanh;
	}
	public void setAnhOrNanh(String anhOrNanh) {
		this.anhOrNanh = anhOrNanh;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public String getSurgicalProcedure() {
		return surgicalProcedure;
	}
	public void setSurgicalProcedure(String surgicalProcedure) {
		this.surgicalProcedure = surgicalProcedure;
	}
	public Double getClaimedAmt() {
		return claimedAmt;
	}
	public void setClaimedAmt(Double claimedAmt) {
		this.claimedAmt = claimedAmt;
	}
	public Long getPackageAmt() {
		return packageAmt;
	}
	public void setPackageAmt(Long packageAmt) {
		this.packageAmt = packageAmt;
	}
	public SelectValue getRequestType() {
		return requestType;
	}
	public void setRequestType(SelectValue requestType) {
		this.requestType = requestType;
	}
	public String getDocRemarks() {
		return docRemarks;
	}
	public void setDocRemarks(String docRemarks) {
		this.docRemarks = docRemarks;
	}
	public Date getAssigneeDateAndTime() {
		return assigneeDateAndTime;
	}
	public void setAssigneeDateAndTime(Date assigneeDateAndTime) {
		this.assigneeDateAndTime = assigneeDateAndTime;
	}
	public String getHrmReplyRemarks() {
		return hrmReplyRemarks;
	}
	public void setHrmReplyRemarks(String hrmReplyRemarks) {
		this.hrmReplyRemarks = hrmReplyRemarks;
	}
	public Date getReplyDateAndTime() {
		return replyDateAndTime;
	}
	public void setReplyDateAndTime(Date replyDateAndTime) {
		this.replyDateAndTime = replyDateAndTime;
	}
	public String getDocUserId() {
		return docUserId;
	}
	public void setDocUserId(String docUserId) {
		this.docUserId = docUserId;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getDocDeskNumber() {
		return docDeskNumber;
	}
	public void setDocDeskNumber(String docDeskNumber) {
		this.docDeskNumber = docDeskNumber;
	}
	public String getAssigneeDateAndTimeStr() {
		return assigneeDateAndTimeStr;
	}
	public void setAssigneeDateAndTimeStr(String assigneeDateAndTimeStr) {
		this.assigneeDateAndTimeStr = assigneeDateAndTimeStr;
	}
	
	
	

}
