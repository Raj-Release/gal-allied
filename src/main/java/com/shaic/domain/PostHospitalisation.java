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
@Table(name="IMS_CLS_POST_HOSPITALIZATION")
@NamedQueries( {
@NamedQuery(name="PostHospitalisation.findAll", query="SELECT m FROM PostHospitalisation m"),
@NamedQuery(name = "PostHospitalisation.findByReimbursement",query = "select o from PostHospitalisation o where o.reimbursement.key = :reimbursementKey")
})

public class PostHospitalisation {
	
	@Id
	@Column(name = "POST_HOSPITALIZATION_KEY")
	private Long key;
	
	@OneToOne
	@JoinColumn(name="REIMBURSEMENT_KEY")
	private Reimbursement reimbursement;    
	
	@Column(name = "BILL_TYPE_NUMBER")
	private Long billTypeNumber;
	
	@Column(name = "CLAIMED_AMOUNT_BILLS")
	//private Long claimedAmountBills;
	private Double claimedAmountBills;
	
	@Column(name = "REASONABLE_DEDUCTION_AMOUNT")
	//private Long deductibleAmount;
	private Double deductibleAmount;
	/*@Column(name = "TOTAL_AMOUNT")
	private Long totalAmount;
	
	@Column(name = "PAYABLE_AMOUNT")
	private Long payableAmount;*/
	
	@Column(name = "NON_PAYABLE_AMOUNT")
	//private Long nonPayableAmount;
	private Double nonPayableAmount;
	
	@Column(name = "NET_PAYABLE_AMOUNT")
	//private Long netAmount;
	private Double netAmount;
	
	@Column(name = "NON_PAYABLE_REASON")
	private String reason;

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

/*	public Long getClaimedAmountBills() {
		return claimedAmountBills;
	}

	public void setClaimedAmountBills(Long claimedAmountBills) {
		this.claimedAmountBills = claimedAmountBills;
	}
*/
	/*public Long getDeductibleAmount() {
		return deductibleAmount;
	}

	public void setDeductibleAmount(Long deductibleAmount) {
		this.deductibleAmount = deductibleAmount;
	}*/

	/*public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Long getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(Long payableAmount) {
		this.payableAmount = payableAmount;
	}*/

	/*public Long getNonPayableAmount() {
		return nonPayableAmount;
	}

	public void setNonPayableAmount(Long nonPayableAmount) {
		this.nonPayableAmount = nonPayableAmount;
	}*/

	/*public Long getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Long netAmount) {
		this.netAmount = netAmount;
	}*/

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Double getClaimedAmountBills() {
		return claimedAmountBills;
	}

	public void setClaimedAmountBills(Double claimedAmountBills) {
		this.claimedAmountBills = claimedAmountBills;
	}

	public Double getDeductibleAmount() {
		return deductibleAmount;
	}

	public void setDeductibleAmount(Double deductibleAmount) {
		this.deductibleAmount = deductibleAmount;
	}

	public Double getNonPayableAmount() {
		return nonPayableAmount;
	}

	public void setNonPayableAmount(Double nonPayableAmount) {
		this.nonPayableAmount = nonPayableAmount;
	}
	
	

}