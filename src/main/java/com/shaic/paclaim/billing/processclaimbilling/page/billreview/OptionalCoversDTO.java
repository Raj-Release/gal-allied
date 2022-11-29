package com.shaic.paclaim.billing.processclaimbilling.page.billreview;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;

public class OptionalCoversDTO   extends AbstractTableDTO implements Serializable{
	
	
	private Integer sNo =1;
	
	private String eligibleForPolicy= "";
	
	private String billNo= "";
	
	private Date billDate=new Date();
	
	private Integer noOfDaysClaimed= 0;
	
	private Double amountClaimedPerDay=0d;
	
	private Double totalClaimed=0d;
	
	private Double deduction=0d;
	
	private Double amtOfClaimPaid=0d;
	
	private Double applicableSI=0d;
	
	private Integer noOfDaysAllowed=0;
	
	private Integer maxNoOfDaysPerHospital=0;
	
	private Integer maxDaysAllowed=0;
	
	private Integer noOfDaysUtilised=0;
	
	private Integer noOfDaysAvailable=0;
	
	private Integer noOfDaysPayable=0;
	
	private Double allowedAmountPerDay=0d;
	
	private Double amtPerDayPayable=0d;
	
	private Double siLimit=0d;
	
	private Double limit=0d;
	
	private Double balanceSI=0d;
	
	private Double appAmt=0d;
	
	private String remarks="";
	
	private SelectValue covers;
	
	private SelectValue optionalCover;
	
	private Double claimedAmount;
	
	private Long coverKey;
	
	private SelectValue addOnCovers;
	
	private Long coverId;
	
	private Long key;
	
	private Long claimKey;
	
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public Integer getsNo() {
		return sNo;
	}
	public void setsNo(Integer sNo) {
		this.sNo = sNo;
	}
	public String getEligibleForPolicy() {
		return eligibleForPolicy;
	}
	public void setEligibleForPolicy(String eligibleForPolicy) {
		this.eligibleForPolicy = eligibleForPolicy;
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
	public Integer getNoOfDaysClaimed() {
		return noOfDaysClaimed;
	}
	public void setNoOfDaysClaimed(Integer noOfDaysClaimed) {
		this.noOfDaysClaimed = noOfDaysClaimed;
	}
	public Double getAmountClaimedPerDay() {
		return amountClaimedPerDay;
	}
	public void setAmountClaimedPerDay(Double amountClaimedPerDay) {
		this.amountClaimedPerDay = amountClaimedPerDay;
	}
	public Double getTotalClaimed() {
		return totalClaimed;
	}
	public void setTotalClaimed(Double totalClaimed) {
		this.totalClaimed = totalClaimed;
	}
	public Double getAmtOfClaimPaid() {
		return amtOfClaimPaid;
	}
	public void setAmtOfClaimPaid(Double amtOfClaimPaid) {
		this.amtOfClaimPaid = amtOfClaimPaid;
	}
	public Double getApplicableSI() {
		return applicableSI;
	}
	public void setApplicableSI(Double applicableSI) {
		this.applicableSI = applicableSI;
	}
	public Integer getNoOfDaysAllowed() {
		return noOfDaysAllowed;
	}
	public void setNoOfDaysAllowed(Integer noOfDaysAllowed) {
		this.noOfDaysAllowed = noOfDaysAllowed;
	}
	public Integer getMaxNoOfDaysPerHospital() {
		return maxNoOfDaysPerHospital;
	}
	public void setMaxNoOfDaysPerHospital(Integer maxNoOfDaysPerHospital) {
		this.maxNoOfDaysPerHospital = maxNoOfDaysPerHospital;
	}
	public Integer getMaxDaysAllowed() {
		return maxDaysAllowed;
	}
	public void setMaxDaysAllowed(Integer maxDaysAllowed) {
		this.maxDaysAllowed = maxDaysAllowed;
	}
	public Integer getNoOfDaysUtilised() {
		return noOfDaysUtilised;
	}
	public void setNoOfDaysUtilised(Integer noOfDaysUtilised) {
		this.noOfDaysUtilised = noOfDaysUtilised;
	}
	public Integer getNoOfDaysAvailable() {
		return noOfDaysAvailable;
	}
	public void setNoOfDaysAvailable(Integer noOfDaysAvailable) {
		this.noOfDaysAvailable = noOfDaysAvailable;
	}
	public Integer getNoOfDaysPayable() {
		return noOfDaysPayable;
	}
	public void setNoOfDaysPayable(Integer noOfDaysPayable) {
		this.noOfDaysPayable = noOfDaysPayable;
	}
	public Double getAllowedAmountPerDay() {
		return allowedAmountPerDay;
	}
	public void setAllowedAmountPerDay(Double allowedAmountPerDay) {
		this.allowedAmountPerDay = allowedAmountPerDay;
	}
	public Double getAmtPerDayPayable() {
		return amtPerDayPayable;
	}
	public void setAmtPerDayPayable(Double amtPerDayPayable) {
		this.amtPerDayPayable = amtPerDayPayable;
	}
	public Double getSiLimit() {
		return siLimit;
	}
	public void setSiLimit(Double siLimit) {
		this.siLimit = siLimit;
	}
	public Double getLimit() {
		return limit;
	}
	public void setLimit(Double limit) {
		this.limit = limit;
	}
	public Double getAppAmt() {
		return appAmt;
	}
	public void setAppAmt(Double appAmt) {
		this.appAmt = appAmt;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public SelectValue getCovers() {
		return covers;
	}
	public void setCovers(SelectValue covers) {
		this.covers = covers;
	}
	public Double getClaimedAmount() {
		return claimedAmount;
	}
	public void setClaimedAmount(Double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}
	public Long getCoverKey() {
		return coverKey;
	}
	public void setCoverKey(Long coverKey) {
		this.coverKey = coverKey;
	}
	public SelectValue getAddOnCovers() {
		return addOnCovers;
	}
	public void setAddOnCovers(SelectValue addOnCovers) {
		this.addOnCovers = addOnCovers;
	}
	public Long getCoverId() {
		return coverId;
	}
	public void setCoverId(Long coverId) {
		this.coverId = coverId;
	}
	public SelectValue getOptionalCover() {
		return optionalCover;
	}
	public void setOptionalCover(SelectValue optionalCover) {
		this.optionalCover = optionalCover;
	}
	public Double getBalanceSI() {
		return balanceSI;
	}
	public void setBalanceSI(Double balanceSI) {
		this.balanceSI = balanceSI;
	}
	public Double getDeduction() {
		return deduction;
	}
	public void setDeduction(Double deduction) {
		this.deduction = deduction;
	}

	
}
