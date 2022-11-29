package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.vaadin.v7.ui.DateField;

/**
 * @author srikanta.jatta
 *
 */
public class StopPaymentRequestDto extends AbstractTableDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1258812511890240173L;
	private ClaimDto claimDto;
	private Reimbursement reimbursementObj;
	private String intimationNo;
	private String rodNumber;
	private String utrNumber;	
	private Long rodKey;
	private Long claimPaymentKey;
	
	private SelectValue resonForStopPayment;
	private String stopPaymentReqRemarks;
	private String otherRemarks;
	private String policyNumber;
	private String status;
	private String action;
	private String validationRemarks;
	
	private PreauthDTO preauthDto;
	
	private String requestBy;
	private Date requestedDate;
	private String validateNBy;
	private Date validateDate;
	private Long reasonForStopPaymentKey;
	private String reasonForStopPaymentValue;
	private  Long stopPaymentKey;
	
	private SelectValue actionTaken;
	private String stopPaymentResReamrks;
	private String forActionTaken;
	
	private Boolean paymentMode;
	private String reIssuingPaymentMode;
	
	private List<UploadDocumentDTO> uploadedDocsTableList;
	
	private Date paymentCreditDate;
	
	private Date paidDate;
	
	public ClaimDto getClaimDto() {
		return claimDto;
	}
	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}
	public Reimbursement getReimbursementObj() {
		return reimbursementObj;
	}
	public void setReimbursementObj(Reimbursement reimbursementObj) {
		this.reimbursementObj = reimbursementObj;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getRodNumber() {
		return rodNumber;
	}
	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}
	public String getUtrNumber() {
		return utrNumber;
	}
	public void setUtrNumber(String utrNumber) {
		this.utrNumber = utrNumber;
	}
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	
	public Long getClaimPaymentKey() {
		return claimPaymentKey;
	}
	public void setClaimPaymentKey(Long claimPaymentKey) {
		this.claimPaymentKey = claimPaymentKey;
	}
	public PreauthDTO getPreauthDto() {
		return preauthDto;
	}
	public void setPreauthDto(PreauthDTO preauthDto) {
		this.preauthDto = preauthDto;
	}
	
	
	public SelectValue getResonForStopPayment() {
		return resonForStopPayment;
	}
	public void setResonForStopPayment(SelectValue resonForStopPayment) {
		this.resonForStopPayment = resonForStopPayment;
	}
	
	public String getStopPaymentReqRemarks() {
		return stopPaymentReqRemarks;
	}
	public void setStopPaymentReqRemarks(String stopPaymentReqRemarks) {
		this.stopPaymentReqRemarks = stopPaymentReqRemarks;
	}
	public String getOtherRemarks() {
		return otherRemarks;
	}
	public void setOtherRemarks(String otherRemarks) {
		this.otherRemarks = otherRemarks;
	}
	
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getValidationRemarks() {
		return validationRemarks;
	}
	public void setValidationRemarks(String validationRemarks) {
		this.validationRemarks = validationRemarks;
	}
	public String getRequestBy() {
		return requestBy;
	}
	public void setRequestBy(String requestBy) {
		this.requestBy = requestBy;
	}
	public Date getRequestedDate() {
		return requestedDate;
	}
	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}
	
	public String getValidateNBy() {
		return validateNBy;
	}
	public void setValidateNBy(String validateNBy) {
		this.validateNBy = validateNBy;
	}
	public Date getValidateDate() {
		return validateDate;
	}
	public void setValidateDate(Date validateDate) {
		this.validateDate = validateDate;
	}
	public Long getReasonForStopPaymentKey() {
		return reasonForStopPaymentKey;
	}
	public void setReasonForStopPaymentKey(Long reasonForStopPaymentKey) {
		this.reasonForStopPaymentKey = reasonForStopPaymentKey;
	}
	public Long getStopPaymentKey() {
		return stopPaymentKey;
	}
	public void setStopPaymentKey(Long stopPaymentKey) {
		this.stopPaymentKey = stopPaymentKey;
	}
	public String getReasonForStopPaymentValue() {
		return reasonForStopPaymentValue;
	}
	public void setReasonForStopPaymentValue(String reasonForStopPaymentValue) {
		this.reasonForStopPaymentValue = reasonForStopPaymentValue;
	}
	public SelectValue getActionTaken() {
		return actionTaken;
	}
	public void setActionTaken(SelectValue actionTaken) {
		this.actionTaken = actionTaken;
	}
	public String getStopPaymentResReamrks() {
		return stopPaymentResReamrks;
	}
	public void setStopPaymentResReamrks(String stopPaymentResReamrks) {
		this.stopPaymentResReamrks = stopPaymentResReamrks;
	}
	public String getForActionTaken() {
		return forActionTaken;
	}
	public void setForActionTaken(String forActionTaken) {
		this.forActionTaken = forActionTaken;
	}
	
	
	public Boolean getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(Boolean paymentMode) {
		this.paymentMode = paymentMode;
//		this.reIssuingPaymentMode = null != this.paymentMode && paymentMode ? "Cheque/DD" : "Bank Transfer";
	}
	
	public String getReIssuingPaymentMode() {
		return reIssuingPaymentMode;
	}
	public void setReIssuingPaymentMode(String reIssuingPaymentMode) {
		this.reIssuingPaymentMode = reIssuingPaymentMode;
	}
	public List<UploadDocumentDTO> getUploadedDocsTableList() {
		return uploadedDocsTableList;
	}
	public void setUploadedDocsTableList(
			List<UploadDocumentDTO> uploadedDocsTableList) {
		this.uploadedDocsTableList = uploadedDocsTableList;
	}
	public Date getPaymentCreditDate() {
		return paymentCreditDate;
	}
	public void setPaymentCreditDate(Date paymentCreditDate) {
		this.paymentCreditDate = paymentCreditDate;
	}
	public Date getPaidDate() {
		return paidDate;
	}
	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
