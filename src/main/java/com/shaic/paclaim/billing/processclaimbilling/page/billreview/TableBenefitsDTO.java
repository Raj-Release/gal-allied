package com.shaic.paclaim.billing.processclaimbilling.page.billreview;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;

public class TableBenefitsDTO implements Serializable{
	
	
	
	private SelectValue classification;
	
	private String duration;
	
	private String billNo;
	
	private Date billDate;
	
	private Double billAmount;
	
	private Double deduction;
	
	private Double netAmount =0d;
	
	private Double eligibleAmount;
	
	private Double approvedAmount;
	
	private String reasonForDeduction;

	private Long slNo;
	
	private Long key;
	
	private String rodNo;
	
	private String fileType;
	
	private String fileName;
	
	private Double amtConsidered;
	
	public Double getAmtConsidered() {
		return amtConsidered;
	}

	public void setAmtConsidered(Double amtConsidered) {
		this.amtConsidered = amtConsidered;
	}

	public String getRodNo() {
		return rodNo;
	}

	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getSlNo() {
		return slNo;
	}

	public void setSlNo(Long slNo) {
		this.slNo = slNo;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}



	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
			this.billDate = billDate;
	}

	public Double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}

	public Double getDeduction() {
		return deduction;
	}

	public void setDeduction(Double deduction) {
		this.deduction = deduction;
	}

	public Double getEligibleAmount() {
		return eligibleAmount;
	}

	public void setEligibleAmount(Double eligibleAmount) {
		this.eligibleAmount = eligibleAmount;
	}

	public Double getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public String getReasonForDeduction() {
		return reasonForDeduction;
	}

	public void setReasonForDeduction(String reasonForDeduction) {
		this.reasonForDeduction = reasonForDeduction;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public SelectValue getClassification() {
		return classification;
	}

	public void setClassification(SelectValue classification) {
		this.classification = classification;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	
	

}