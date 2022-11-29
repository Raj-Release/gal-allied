package com.shaic.paclaim.billing.processclaimbilling.page.billreview;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class PABillingConsolidatedDTO extends AbstractTableDTO implements Serializable {
	
	
	
	private String part;
	
	private String benefitsCover;
	
	private Double payableAmount = 0d;
	
	private Double amountAlreadypaid = 0d;
	
	private Double netPayableAmount = 0d;

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public String getBenefitsCover() {
		return benefitsCover;
	}

	public void setBenefitsCover(String benefitsCover) {
		this.benefitsCover = benefitsCover;
	}

	public Double getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(Double payableAmount) {
		this.payableAmount = payableAmount;
	}

	public Double getAmountAlreadypaid() {
		return amountAlreadypaid;
	}

	public void setAmountAlreadypaid(Double amountAlreadypaid) {
		this.amountAlreadypaid = amountAlreadypaid;
	}

	public Double getNetPayableAmount() {
		return netPayableAmount;
	}

	public void setNetPayableAmount(Double netPayableAmount) {
		this.netPayableAmount = netPayableAmount;
	}

}
