package com.shaic.paclaim.billing.processclaimbilling.page.billreview;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;

public class AddOnCoversTableDTO  extends AbstractTableDTO implements Serializable{
	
	private SelectValue addonCovers;
	private Double eligibleForProduct;
	private Double amountClaimed;
	private Integer noOfchildAgeLess18;
	private Integer allowableChildren;
	private String billNo;
	private Date billDate;
	private Double billAmount;
	private Double deduction;
	private Double netamount;
	private Double siLimit;
	private Double eligibleAmount;
	private Double approvedAmount;
	private String reasonForDeduction;
	private String remarks;
	private Long claimKey;
	private Long coverId;
	private Long rodId;
	private Long productCode;
	private Double availableSI;
	private Double siAddOnCover;
	private String whenPayable;
	private Integer sNo;
	private String eligibleForPolicy;
	
	public Integer getsNo() {
		return sNo;
	}
	public void setsNo(Integer sNo) {
		this.sNo = sNo;
	}
	public Double getAvailableSI() {
		return availableSI;
	}
	public void setAvailableSI(Double availableSI) {
		this.availableSI = availableSI;
	}
	public Double getSiAddOnCover() {
		return siAddOnCover;
	}
	public void setSiAddOnCover(Double siAddOnCover) {
		this.siAddOnCover = siAddOnCover;
	}
	public String getWhenPayable() {
		return whenPayable;
	}
	public void setWhenPayable(String whenPayable) {
		this.whenPayable = whenPayable;
	}
	
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public SelectValue getAddonCovers() {
		return addonCovers;
	}
	public void setAddonCovers(SelectValue addonCovers) {
		this.addonCovers = addonCovers;
	}
	public Double getEligibleForProduct() {
		return eligibleForProduct;
	}
	public void setEligibleForProduct(Double eligibleForProduct) {
		this.eligibleForProduct = eligibleForProduct;
	}
	public Double getAmountClaimed() {
		return amountClaimed;
	}
	public void setAmountClaimed(Double amountClaimed) {
		this.amountClaimed = amountClaimed;
	}
	public Integer getNoOfchildAgeLess18() {
		return noOfchildAgeLess18;
	}
	public void setNoOfchildAgeLess18(Integer noOfchildAgeLess18) {
		this.noOfchildAgeLess18 = noOfchildAgeLess18;
	}
	public Integer getAllowableChildren() {
		return allowableChildren;
	}
	public void setAllowableChildren(Integer allowableChildren) {
		this.allowableChildren = allowableChildren;
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
	public Double getNetamount() {
		return netamount;
	}
	public void setNetamount(Double netamount) {
		this.netamount = netamount;
	}
	public Double getSiLimit() {
		return siLimit;
	}
	public void setSiLimit(Double siLimit) {
		this.siLimit = siLimit;
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
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Long getCoverId() {
		return coverId;
	}
	public void setCoverId(Long coverId) {
		this.coverId = coverId;
	}
	public Long getRodId() {
		return rodId;
	}
	public void setRodId(Long rodId) {
		this.rodId = rodId;
	}
	public Long getProductCode() {
		return productCode;
	}
	public void setProductCode(Long productCode) {
		this.productCode = productCode;
	}
	public String getEligibleForPolicy() {
		return eligibleForPolicy;
	}
	public void setEligibleForPolicy(String eligibleForPolicy) {
		this.eligibleForPolicy = eligibleForPolicy;
	}
	
	

}
