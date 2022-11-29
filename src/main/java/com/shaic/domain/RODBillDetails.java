/**
 * 
 */
package com.shaic.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author ntv.vijayar
 *
 */


/**
 * The persistent class for the IMS_CLS_ROD_BILL_DETAILS database table.
 * 
 */
@Entity
@Table(name="IMS_CLS_ROD_BILL_DETAILS")
@NamedQueries({
@NamedQuery(name= "RODBillDetails.findAll", query="SELECT r FROM RODBillDetails r"),
@NamedQuery(name= "RODBillDetails.findByKey",query="SELECT r FROM RODBillDetails r WHERE r.key = :primaryKey"),
@NamedQuery(name= "RODBillDetails.findByRodDocumentSummaryKey",query="SELECT r FROM RODBillDetails r WHERE r.rodDocumentSummaryKey.key = :summaryKey and  (r.deletedFlag = 'N' or r.deletedFlag is null)"),
@NamedQuery(name= "RODBillDetails.findByReimbursementKey",query="SELECT r FROM RODBillDetails r WHERE r.reimbursementKey = :reimbursementKey and  (r.deletedFlag = 'N' or r.deletedFlag is null)"),
@NamedQuery(name= "RODBillDetails.findByRODDocSummaryAndCategoryId",query="SELECT r FROM RODBillDetails r WHERE r.rodDocumentSummaryKey.key = :summaryKey and r.billCategory.key = :billCategoryKey"),
@NamedQuery(name= "RODBillDetails.findSumOfAmount",query="SELECT sum(r.claimedAmountBills) FROM RODBillDetails r WHERE r.rodDocumentSummaryKey.key in (:doumentSummaryIds)"),
@NamedQuery(name= "RODBillDetails.findHospSumOfAmount",query="SELECT sum(r.claimedAmountBills) FROM RODBillDetails r WHERE r.rodDocumentSummaryKey.key in (:doumentSummaryIds) and r.billClassification.key = :billClassificationId"),
@NamedQuery(name= "RODBillDetails.findByDocSummaryAndBillClassification",query="SELECT r FROM RODBillDetails r WHERE r.rodDocumentSummaryKey.key = :summaryKey and  (r.deletedFlag = 'N' or r.deletedFlag is null) and r.billClassification.key = :billClassification")
})

public class RODBillDetails {
	
	@Id
	@SequenceGenerator(name="IMS_RODBILLDETAILS_KEY_GENERATOR", sequenceName = "SEQ_ROD_Bill_Details_Key", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_RODBILLDETAILS_KEY_GENERATOR" )
	@Column(name = "ROD_BILL_DETAILS_KEY")
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="DOCUMENT_SUMMARY_KEY", nullable=false)
	private RODDocumentSummary rodDocumentSummaryKey;
	
	@Column(name = "ITEM_NAME")
	private String itemName;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="BILL_CLASSIFICATION_ID", nullable=false)
	private MasBillClassification billClassification;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="BILL_CATEGORY_ID", nullable=false)
	private MasBillCategory billCategory;
	
	@Column(name= "ITEM_NUMBER")
	private Long itemNumber;
	
	@Column(name = "NUMBER_OF_DAYS_BILLS")
	private Double noOfDaysBills;
	
	@Column(name = "PER_DAY_AMOUNT_BILLS")
	private Double perDayAmountBills;
	
	@Column(name = "CLAIMED_AMOUNT_BILLS")
	private Double claimedAmountBills;
	
	@Column(name = "REASONABLE_DEDUCTION_AMOUNT")
	private Double deductibleAmount;
	
	@Column(name = "TOTAL_AMOUNT")
	private Double totalAmount;
	
	@Column(name = "NUMBER_OF_DAYS_ALLOWED")
	private Double noOfDaysPolicy;
	
	@Column(name = "PRODUCT_LIMIT_AMOUNT")
	private Double perDayAmountPolicy;
	
	@Column(name = "NON_PAYABLE_AMOUNT_PRODUCT")
	private Double nonPayableAmountProduct;
	
	@Column(name = "TOTAL_DISALLOWANCES_AMOUNT")
	private Double payableAmount;
	
	@Column(name = "NON_PAYABLE_AMOUNT")
	private Double nonPayableAmount;
	
	@Column(name = "NET_PAYABLE_AMOUNT")
	private Double netAmount;
	
	@Column(name = "NON_PAYABLE_REASON")
	private String nonPayableReason;
	
	@Column(name = "MEDICAL_REMARKS")
	private String medicalRemarks;
	
	@Column(name = "IRDA_LEVEL1_ID")
	private Long irdaLevel1Id;
	
	@Column(name = "IRDA_LEVEL2_ID")
	private Long irdaLevel2Id;
	
	@Column(name = "IRDA_LEVEL3_ID")
	private Long irdaLevel3Id;
	
	@Column(name = "DELETED_FLAG")
	private String deletedFlag;
	
	@Column(name = "REIMBURSEMENT_KEY")
	private Long reimbursementKey;
	
	@Column(name = "ROOM_TYPE")
	private String roomType;
	

	/**
	 * @return the key
	 */
	public Long getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(Long key) {
		this.key = key;
	}

	/**
	 * @return the rodDocumentSummaryKey
	 */
	public RODDocumentSummary getRodDocumentSummaryKey() {
		return rodDocumentSummaryKey;
	}

	/**
	 * @param rodDocumentSummaryKey the rodDocumentSummaryKey to set
	 */
	public void setRodDocumentSummaryKey(RODDocumentSummary rodDocumentSummaryKey) {
		this.rodDocumentSummaryKey = rodDocumentSummaryKey;
	}

	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return the billClassification
	 */
	public MasBillClassification getBillClassification() {
		return billClassification;
	}

	/**
	 * @param billClassification the billClassification to set
	 */
	public void setBillClassification(MasBillClassification billClassification) {
		this.billClassification = billClassification;
	}

	/**
	 * @return the billCategory
	 */
	public MasBillCategory getBillCategory() {
		return billCategory;
	}

	/**
	 * @param billCategory the billCategory to set
	 */
	public void setBillCategory(MasBillCategory billCategory) {
		this.billCategory = billCategory;
	}

	/**
	 * @return the itemNumber
	 */
	public Long getItemNumber() {
		return itemNumber;
	}

	/**
	 * @param itemNumber the itemNumber to set
	 */
	public void setItemNumber(Long itemNumber) {
		this.itemNumber = itemNumber;
	}


	/**
	 * @return the perDayAmountBills
	 */
	public Double getPerDayAmountBills() {
		return perDayAmountBills;
	}

	/**
	 * @param perDayAmountBills the perDayAmountBills to set
	 */
	public void setPerDayAmountBills(Double perDayAmountBills) {
		this.perDayAmountBills = perDayAmountBills;
	}

	/**
	 * @return the claimedAmountBills
	 */
	public Double getClaimedAmountBills() {
		return claimedAmountBills;
	}

	/**
	 * @param claimedAmountBills the claimedAmountBills to set
	 */
	public void setClaimedAmountBills(Double claimedAmountBills) {
		this.claimedAmountBills = claimedAmountBills;
	}

	/**
	 * @return the deductibleAmount
	 */
	public Double getDeductibleAmount() {
		return deductibleAmount;
	}

	/**
	 * @param deductibleAmount the deductibleAmount to set
	 */
	public void setDeductibleAmount(Double deductibleAmount) {
		this.deductibleAmount = deductibleAmount;
	}

	/**
	 * @return the totalAmount
	 */
	public Double getTotalAmount() {
		return totalAmount;
	}

	/**
	 * @param totalAmount the totalAmount to set
	 */
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * @return the noOfDaysPolicy
	 */
	public Double getNoOfDaysPolicy() {
		return noOfDaysPolicy;
	}

	/**
	 * @param noOfDaysPolicy the noOfDaysPolicy to set
	 */
	public void setNoOfDaysPolicy(Double noOfDaysPolicy) {
		this.noOfDaysPolicy = noOfDaysPolicy;
	}

	/**
	 * @return the perDayAmountPolicy
	 */
	public Double getPerDayAmountPolicy() {
		return perDayAmountPolicy;
	}

	/**
	 * @param perDayAmountPolicy the perDayAmountPolicy to set
	 */
	public void setPerDayAmountPolicy(Double perDayAmountPolicy) {
		this.perDayAmountPolicy = perDayAmountPolicy;
	}

	/**
	 * @return the payableAmount
	 */
	public Double getPayableAmount() {
		return payableAmount;
	}

	/**
	 * @param payableAmount the payableAmount to set
	 */
	public void setPayableAmount(Double payableAmount) {
		this.payableAmount = payableAmount;
	}

	/**
	 * @return the nonPayableAmount
	 */
	public Double getNonPayableAmount() {
		return nonPayableAmount;
	}

	/**
	 * @param nonPayableAmount the nonPayableAmount to set
	 */
	public void setNonPayableAmount(Double nonPayableAmount) {
		this.nonPayableAmount = nonPayableAmount;
	}

	/**
	 * @return the netAmount
	 */
	public Double getNetAmount() {
		return netAmount;
	}

	/**
	 * @param netAmount the netAmount to set
	 */
	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	/**
	 * @return the nonPayableReason
	 */
	public String getNonPayableReason() {
		return nonPayableReason;
	}

	/**
	 * @param nonPayableReason the nonPayableReason to set
	 */
	public void setNonPayableReason(String nonPayableReason) {
		this.nonPayableReason = nonPayableReason;
	}

	/**
	 * @return the medicalRemarks
	 */
	public String getMedicalRemarks() {
		return medicalRemarks;
	}

	/**
	 * @param medicalRemarks the medicalRemarks to set
	 */
	public void setMedicalRemarks(String medicalRemarks) {
		this.medicalRemarks = medicalRemarks;
	}

	/**
	 * @return the irdaLevel1Id
	 */
	public Long getIrdaLevel1Id() {
		return irdaLevel1Id;
	}

	/**
	 * @param irdaLevel1Id the irdaLevel1Id to set
	 */
	public void setIrdaLevel1Id(Long irdaLevel1Id) {
		this.irdaLevel1Id = irdaLevel1Id;
	}

	/**
	 * @return the irdaLevel2Id
	 */
	public Long getIrdaLevel2Id() {
		return irdaLevel2Id;
	}

	/**
	 * @param irdaLevel2Id the irdaLevel2Id to set
	 */
	public void setIrdaLevel2Id(Long irdaLevel2Id) {
		this.irdaLevel2Id = irdaLevel2Id;
	}

	/**
	 * @return the irdaLevel3Id
	 */
	public Long getIrdaLevel3Id() {
		return irdaLevel3Id;
	}

	/**
	 * @param irdaLevel3Id the irdaLevel3Id to set
	 */
	public void setIrdaLevel3Id(Long irdaLevel3Id) {
		this.irdaLevel3Id = irdaLevel3Id;
	}

	public Double getNonPayableAmountProduct() {
		return nonPayableAmountProduct;
	}

	public void setNonPayableAmountProduct(Double nonPayableAmountProduct) {
		this.nonPayableAmountProduct = nonPayableAmountProduct;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public Double getNoOfDaysBills() {
		return noOfDaysBills;
	}

	public void setNoOfDaysBills(Double noOfDaysBills) {
		this.noOfDaysBills = noOfDaysBills;
	}

	public Long getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	
}

	
	


