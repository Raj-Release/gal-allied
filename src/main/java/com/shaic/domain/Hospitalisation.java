package com.shaic.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="IMS_CLS_HOSPITALIZATION")
@NamedQueries( {
@NamedQuery(name="Hospitalisation.findAll", query="SELECT m FROM Hospitalisation m"),
@NamedQuery(name = "Hospitalisation.findByReimbursement",query = "select o from Hospitalisation o where o.reimbursement.key = :reimbursementKey"),
@NamedQuery(name = "Hospitalisation.findByReimbursementOrderByItemNo", query = "select o from Hospitalisation o where o.reimbursement.key = :reimbursementKey order by o.itemNumber asc")
})
public class Hospitalisation {
	
	@Id
	@Column(name = "HOSPITALIZATION_KEY")
	private Long key;
	
	@OneToOne
	@JoinColumn(name="REIMBURSEMENT_KEY")
	private Reimbursement reimbursement;    
	
	@Column(name = "BILL_TYPE_NUMBER")
	private Long billTypeNumber;
	
	@Column(name = "NUMBER_OF_DAYS_BILLS")
	private Double noOfDays;
	
	@Column(name = "PER_DAY_AMOUNT_BILLS")
	private Double perDayAmount;
	
	@Column(name = "CLAIMED_AMOUNT_BILLS")
	private Double claimedAmount;
	
	@Column(name = "REASONABLE_DEDUCTION_AMOUNT")
	private Long deductibleAmount;
	
	@Column(name = "TOTAL_AMOUNT")
	private Long totalAmount;
	
	@Column(name = "NUMBER_OF_DAYS_ALLOWED")
	private Double policyNoOfDays;
	
	@Column(name = "PRODUCT_LIMIT_AMOUNT")
	private Double policyPerDayAmount;
	
	@Column(name = "NON_PAYABLE_AMOUNT_PRODUCT")
	private Double nonPayableAmountProduct;
	
	@Column(name = "PRORATE_DEDUCTION_AMOUNT")
	private Double prorateDeductionAmount;
	

	@Column(name = "TOTAL_DISALLOWANCES_AMOUNT")
	private Long payableAmount;
	
	@Column(name = "NON_PAYABLE_AMOUNT")
	private Long nonPayableAmt;
	
	@Column(name = "NET_PAYABLE_AMOUNT")
	private Long netAmount;
	
	@Column(name = "NON_PAYABLE_REASON")
	private String reason;

	@Column(name = "ITEM_NUMBER")
	private Long itemNumber;
	
	@Column(name = "ITEM_MAPPING_NUMBER")
	private Long itemMappingNumber;
	
	@Column(name = "ROOM_TYPE")
	private String roomType;
	
	@Column(name="GMC_PROPORTIONATE_FLAG")
	private String gmcProportionateFlag;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Reimbursement getReimbursement() {
		return reimbursement;
	}

	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
	}

	public Long getBillTypeNumber() {
		return billTypeNumber;
	}

	public void setBillTypeNumber(Long billTypeNumber) {
		this.billTypeNumber = billTypeNumber;
	}

	
	public void setItemMappingNumber(Long  itemMappingNumber) {
		this.itemMappingNumber = itemMappingNumber;
	}

	public Double getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(Double noOfDays) {
		this.noOfDays = noOfDays;
	}

	public Double getPerDayAmount() {
		return perDayAmount;
	}

	public void setPerDayAmount(Double perDayAmount) {
		this.perDayAmount = perDayAmount;
	}

	public Double getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(Double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public Long getDeductibleAmount() {
		return deductibleAmount;
	}

	public void setDeductibleAmount(Long deductibleAmount) {
		this.deductibleAmount = deductibleAmount;
	}

	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getPolicyNoOfDays() {
		return policyNoOfDays;
	}

	public void setPolicyNoOfDays(Double policyNoOfDays) {
		this.policyNoOfDays = policyNoOfDays;
	}

	public Double getPolicyPerDayAmount() {
		return policyPerDayAmount;
	}

	public void setPolicyPerDayAmount(Double policyPerDayAmount) {
		this.policyPerDayAmount = policyPerDayAmount;
	}

	public Double getNonPayableAmountProduct() {
		return nonPayableAmountProduct;
	}

	public void setNonPayableAmountProduct(Double nonPayableAmountProduct) {
		this.nonPayableAmountProduct = nonPayableAmountProduct;
	}

	public Double getProrateDeductionAmount() {
		return prorateDeductionAmount;
	}

	public void setProrateDeductionAmount(Double prorateDeductionAmount) {
		this.prorateDeductionAmount = prorateDeductionAmount;
	}

	public Long getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(Long payableAmount) {
		this.payableAmount = payableAmount;
	}

	public Long getNonPayableAmt() {
		return nonPayableAmt;
	}

	public void setNonPayableAmt(Long nonPayableAmt) {
		this.nonPayableAmt = nonPayableAmt;
	}

	public Long getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Long netAmount) {
		this.netAmount = netAmount;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Long getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(Long itemNumber) {
		this.itemNumber = itemNumber;
	}

	public Long getItemMappingNumber() {
		return itemMappingNumber;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public String getGmcProportionateFlag() {
		return gmcProportionateFlag;
	}

	public void setGmcProportionateFlag(String gmcProportionateFlag) {
		this.gmcProportionateFlag = gmcProportionateFlag;
	}

	/*public void setItemMappingNumber(Long itemMappingNumber) {
		this.itemMappingNumber = itemMappingNumber;
	}*/
	
	

}