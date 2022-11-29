package com.shaic.claim.intimation.uprSearch;

import java.util.List;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.reimbursement.paymentprocesscpuview.PaymentProcessCpuPageDTO;

public class ClmPaymentCancelDto extends AbstractTableDTO{

	private String rodNumber;
	private String remarks;
	private String cancelDate;
	private String paymentType;
	private Long paymentKey;
	private List<PaymentProcessCpuPageDTO> settlementList;
	
	public String getRodNumber() {
		return rodNumber;
	}
	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public Long getPaymentKey() {
		return paymentKey;
	}
	public void setPaymentKey(Long paymentKey) {
		this.paymentKey = paymentKey;
	}
	public List<PaymentProcessCpuPageDTO> getSettlementList() {
		return settlementList;
	}
	public void setSettlementList(List<PaymentProcessCpuPageDTO> settlementList) {
		this.settlementList = settlementList;
	}	
	
	
}
