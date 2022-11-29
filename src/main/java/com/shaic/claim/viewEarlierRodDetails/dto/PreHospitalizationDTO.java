package com.shaic.claim.viewEarlierRodDetails.dto;

import com.shaic.claim.rod.wizard.dto.ViewBillSummaryRemarksDTO;

public class PreHospitalizationDTO  extends ViewBillSummaryRemarksDTO {
	
	private Integer sno;
	
	private String details;
	
//	private Long claimedAmt;
	private Double claimedAmt;
	
	//private Long billingNonPayable;
	
	private Double billingNonPayable;
	
//	private Long netAmount;
	
	private Long amount;
	
	private Long deductingNonPayable;
	
	private Long payableAmount;
	
	private String reason;
	//Changes done for new pre hospitalization veiw.
	private Double reasonableDeduction;
	
	private Double netAmount;
	
	private String classificationFlag;
	
	private String nonPayableRmrksForAssessmentSheet;


	public String getClassificationFlag() {
		return classificationFlag;
	}

	public void setClassificationFlag(String classificationFlag) {
		this.classificationFlag = classificationFlag;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	/*public Long getClaimedAmt() {
		return claimedAmt;
	}

	public void setClaimedAmt(Long claimedAmt) {
		this.claimedAmt = claimedAmt;
	}

	public Long getBillingNonPayable() {
		return billingNonPayable;
	}

	public void setBillingNonPayable(Long billingNonPayable) {
		this.billingNonPayable = billingNonPayable;
	}
*/
	/*public Long getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Long netAmount) {
		this.netAmount = netAmount;
	}*/

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getDeductingNonPayable() {
		return deductingNonPayable;
	}

	public void setDeductingNonPayable(Long deductingNonPayable) {
		this.deductingNonPayable = deductingNonPayable;
	}

	public Long getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(Long payableAmount) {
		this.payableAmount = payableAmount;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

	public Double getReasonableDeduction() {
		return reasonableDeduction;
	}

	public void setReasonableDeduction(Double reasonableDeduction) {
		this.reasonableDeduction = reasonableDeduction;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Double getClaimedAmt() {
		return claimedAmt;
	}

	public void setClaimedAmt(Double claimedAmt) {
		this.claimedAmt = claimedAmt;
	}

	public Double getBillingNonPayable() {
		return billingNonPayable;
	}

	public void setBillingNonPayable(Double billingNonPayable) {
		this.billingNonPayable = billingNonPayable;
	}

	public String getNonPayableRmrksForAssessmentSheet() {
		return nonPayableRmrksForAssessmentSheet;
	}

	public void setNonPayableRmrksForAssessmentSheet(
			String nonPayableRmrksForAssessmentSheet) {
		this.nonPayableRmrksForAssessmentSheet = nonPayableRmrksForAssessmentSheet;
	}	

}
