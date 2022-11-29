package com.shaic.claim.reimbursement.financialapproval.pages.communicationPage;

public class NonPayableReasonDto {

	private String sno;
	private String billNo;
	private String itemName;
	private String deductibleOrNonPayableReason;
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public String getDeductibleOrNonPayableReason() {
		return deductibleOrNonPayableReason;
	}
	public void setDeductibleOrNonPayableReason(String deductibleOrNonPayableReason) {
		this.deductibleOrNonPayableReason = deductibleOrNonPayableReason;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}	
}
