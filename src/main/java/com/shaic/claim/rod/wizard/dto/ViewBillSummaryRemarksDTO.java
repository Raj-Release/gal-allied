/**
 * 
 */
package com.shaic.claim.rod.wizard.dto;

import java.util.Date;

/**
 * @author ntv.vijayar
 *
 */
public class ViewBillSummaryRemarksDTO {
	
	
	private String deductibleNonPayableReasonFA;
	
	private String deductibleNonPayableReasonBilling;

	private Long billTypeNumber;
	
	private Long reimbursementKey;
	
	private Long billClassificationTypeId;
	
	private Date billingCompletedDate;
	
	private Date faCompletedDate;

	
	private Long billRemarksKey;

	public String getDeductibleNonPayableReasonFA() {
		return deductibleNonPayableReasonFA;
	}


	public void setDeductibleNonPayableReasonFA(String deductibleNonPayableReasonFA) {
		this.deductibleNonPayableReasonFA = deductibleNonPayableReasonFA;
	}


	public String getDeductibleNonPayableReasonBilling() {
		return deductibleNonPayableReasonBilling;
	}


	public void setDeductibleNonPayableReasonBilling(
			String deductibleNonPayableReasonBilling) {
		this.deductibleNonPayableReasonBilling = deductibleNonPayableReasonBilling;
	}


	public Long getBillTypeNumber() {
		return billTypeNumber;
	}


	public void setBillTypeNumber(Long billTypeNumber) {
		this.billTypeNumber = billTypeNumber;
	}


	public Long getReimbursementKey() {
		return reimbursementKey;
	}


	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}


	public Long getBillClassificationTypeId() {
		return billClassificationTypeId;
	}


	public void setBillClassificationTypeId(Long billClassificationTypeId) {
		this.billClassificationTypeId = billClassificationTypeId;
	}


	public Date getBillingCompletedDate() {
		return billingCompletedDate;
	}


	public void setBillingCompletedDate(Date billingCompletedDate) {
		this.billingCompletedDate = billingCompletedDate;
	}


	public Date getFaCompletedDate() {
		return faCompletedDate;
	}


	public void setFaCompletedDate(Date faCompletedDate) {
		this.faCompletedDate = faCompletedDate;
	}


	public Long getBillRemarksKey() {
		return billRemarksKey;
	}


	public void setBillRemarksKey(Long billRemarksKey) {
		this.billRemarksKey = billRemarksKey;
	}

}
