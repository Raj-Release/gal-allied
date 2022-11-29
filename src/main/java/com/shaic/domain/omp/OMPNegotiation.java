package com.shaic.domain.omp;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPReimbursement;


/**
 * 
 * The persistent class for the IMS_CLS_OMP_NEGOTIATION table.
 * 
 */

@Entity
@Table(name="IMS_CLS_OMP_NEGOTIATION")
@NamedQueries({
	@NamedQuery(name="OMPNegotiation.findByOMPNegotiation", query="SELECT o FROM OMPNegotiation o"),
	@NamedQuery(name="OMPNegotiation.findByOMPNegotiationkey", query="SELECT o FROM OMPNegotiation o  where o.key=:key"),
	@NamedQuery(name="OMPNegotiation.findByRodKey", query="SELECT o FROM OMPNegotiation o where o.rodKey.key=:rodKey order by o.key desc"),
	@NamedQuery(name="OMPNegotiation.findByClaimKey", query="SELECT o FROM OMPNegotiation o where o.claim.key=:claimKey and o.rodKey is not null and o.rodKey.approvedFlg is not null and o.rodKey.approvedFlg='Y' "
			+ "and o.rodKey.negotiationDone is not null and o.rodKey.negotiationDone='Y' order by o.key desc")
})
public class OMPNegotiation extends AbstractEntity implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_OMP_NEGOTIATION_KEY_GENERATOR", sequenceName = "SEQ_NEGOTIATION_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_OMP_NEGOTIATION_KEY_GENERATOR" ) 
	@Column(name="NEGOTIATION_KEY")
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CLAIM_KEY", nullable=false)
	private OMPClaim claim;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INTIMATION_KEY", nullable=false)
	private OMPIntimation intimation;
		
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ROD_KEY", nullable=false)
	private OMPReimbursement rodKey;
		
	@Column(name="AGREED_AMOUNT")
	private Double aggredAmount;
	
	@Column(name="NEGOTIATION_REMARKS")
	private String negotiationRemarks;
	
	@Temporal(TemporalType.DATE)
	@Column(name="NEGOTIATION_REQUESTED_DATE")
	private Date negotiationRequestedDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="NEGOTIATION_COMPLETED_DATE")
	private Date negotiationCompletedDate;
	
	@Column(name="NEGOTIATOR_NAME")
	private String negotiatorName;
	
	@Column(name="APPROVED_AMOUNT_DOLLAR")
	private Double approvedAmount;

	@Column(name="EXPENSE_AMOUNT_USD")
	private Double expenseAmountusd;
	
	@Column(name="DIFF_AMOUNT_USD")
	private Double diffAmountusd;
	
	@Column(name="HANDLING_CHARGES_USD")
	private Double handlingChargsUsd;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public OMPClaim getClaim() {
		return claim;
	}

	public void setClaim(OMPClaim claim) {
		this.claim = claim;
	}

	public OMPIntimation getIntimation() {
		return intimation;
	}

	public void setIntimation(OMPIntimation intimation) {
		this.intimation = intimation;
	}

	public OMPReimbursement getRodKey() {
		return rodKey;
	}

	public void setRodKey(OMPReimbursement rodKey) {
		this.rodKey = rodKey;
	}

	public Double getAggredAmount() {
		return aggredAmount;
	}

	public void setAggredAmount(Double aggredAmount) {
		this.aggredAmount = aggredAmount;
	}

	public String getNegotiationRemarks() {
		return negotiationRemarks;
	}

	public void setNegotiationRemarks(String negotiationRemarks) {
		this.negotiationRemarks = negotiationRemarks;
	}

	public Date getNegotiationRequestedDate() {
		return negotiationRequestedDate;
	}

	public void setNegotiationRequestedDate(Date negotiationRequestedDate) {
		this.negotiationRequestedDate = negotiationRequestedDate;
	}

	public Date getNegotiationCompletedDate() {
		return negotiationCompletedDate;
	}

	public void setNegotiationCompletedDate(Date negotiationCompletedDate) {
		this.negotiationCompletedDate = negotiationCompletedDate;
	}

	public String getNegotiatorName() {
		return negotiatorName;
	}

	public void setNegotiatorName(String negotiatorName) {
		this.negotiatorName = negotiatorName;
	}

	public Double getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public Double getExpenseAmountusd() {
		return expenseAmountusd;
	}

	public void setExpenseAmountusd(Double expenseAmountusd) {
		this.expenseAmountusd = expenseAmountusd;
	}

	public Double getDiffAmountusd() {
		return diffAmountusd;
	}

	public void setDiffAmountusd(Double diffAmountusd) {
		this.diffAmountusd = diffAmountusd;
	}

	public Double getHandlingChargsUsd() {
		return handlingChargsUsd;
	}

	public void setHandlingChargsUsd(Double handlingChargsUsd) {
		this.handlingChargsUsd = handlingChargsUsd;
	}
	
	

	
}
