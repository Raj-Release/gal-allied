package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class StopPaymentTrackingTableDTO extends AbstractTableDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String intimationNo;
	private String rodNumber;
	private String utrNumber;
	private String requestBy;
	private Date requestedDate;
	private String resonForStopPayment;
	private String stopPaymentReqRemarks;
	private String validateBy;
	private Date validateDate;
	private String action;
	private String validationRemarks;
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
	public String getResonForStopPayment() {
		return resonForStopPayment;
	}
	public void setResonForStopPayment(String resonForStopPayment) {
		this.resonForStopPayment = resonForStopPayment;
	}
	public String getStopPaymentReqRemarks() {
		return stopPaymentReqRemarks;
	}
	public void setStopPaymentReqRemarks(String stopPaymentReqRemarks) {
		this.stopPaymentReqRemarks = stopPaymentReqRemarks;
	}
	public String getValidateBy() {
		return validateBy;
	}
	public void setValidateBy(String validateBy) {
		this.validateBy = validateBy;
	}
	public Date getValidateDate() {
		return validateDate;
	}
	public void setValidateDate(Date validateDate) {
		this.validateDate = validateDate;
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
	
	


}
