package com.shaic.claim.outpatient.processOPpages;

import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.vaadin.v7.ui.TextField;

@SuppressWarnings("serial")
public class OPBillDetailsDTO extends AbstractTableDTO{
	private int sno;
//	private String queryContent;
	private SelectValue category;
	private SelectValue receivedStatus;
	private Date billDate;
	
	private Long billNumber;
	private Double billAmount;
	private Double deductibleAmount;
	private Double payableAmount;
	private String remarks;
	
	private TextField payAmtfield;
	private TextField deducAmtfield;
	private TextField billAmtfield;
	
	
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
//	public String getQueryContent() {
//		return queryContent;
//	}
//	public void setQueryContent(String queryContent) {
//		this.queryContent = queryContent;
//	}
	public SelectValue getCategory() {
		return category;
	}
	public void setCategory(SelectValue category) {
		this.category = category;
	}
	public SelectValue getReceivedStatus() {
		return receivedStatus;
	}
	public void setReceivedStatus(SelectValue receivedStatus) {
		this.receivedStatus = receivedStatus;
	}
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	public Long getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(Long billNumber) {
		this.billNumber = billNumber;
	}
	public Double getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}
	public Double getDeductibleAmount() {
		return deductibleAmount;
	}
	public void setDeductibleAmount(Double deductibleAmount) {
		this.deductibleAmount = deductibleAmount;
	}
	public Double getPayableAmount() {
		return payableAmount;
	}
	public void setPayableAmount(Double payableAmount) {
		this.payableAmount = payableAmount;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public TextField getPayAmtfield() {
		return payAmtfield;
	}
	public void setPayAmtfield(TextField payAmtfield) {
		this.payAmtfield = payAmtfield;
	}
	public TextField getDeducAmtfield() {
		return deducAmtfield;
	}
	public void setDeducAmtfield(TextField deducAmtfield) {
		this.deducAmtfield = deducAmtfield;
	}
	public TextField getBillAmtfield() {
		return billAmtfield;
	}
	public void setBillAmtfield(TextField billAmtfield) {
		this.billAmtfield = billAmtfield;
	}
}
