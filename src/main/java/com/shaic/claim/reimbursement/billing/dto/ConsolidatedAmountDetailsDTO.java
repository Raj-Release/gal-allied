package com.shaic.claim.reimbursement.billing.dto;

import java.io.Serializable;

public class ConsolidatedAmountDetailsDTO implements Serializable {

	private static final long serialVersionUID = -6998183575903158319L;
	
	private Integer hospPayableAmt = 0 ;
	private Integer preHospPayableAmt = 0;
	private Integer postHospPayableAmt = 0;
	private Integer addonBenefitAmt = 0;
	private Integer amountPayableToInsAftPremium = 0;
	private Integer premiumAmt = 0;
	private Integer amountPayableAfterPremium = 0;
	private Integer lumpusmPayableAmt = 0;
	private Integer otherBenefitAmt = 0;
	public Integer getHospPayableAmt() {
		return hospPayableAmt;
	}
	public void setHospPayableAmt(Integer hospPayableAmt) {
		this.hospPayableAmt = hospPayableAmt;
	}
	public Integer getPreHospPayableAmt() {
		return preHospPayableAmt;
	}
	public void setPreHospPayableAmt(Integer preHospPayableAmt) {
		this.preHospPayableAmt = preHospPayableAmt;
	}
	public Integer getPostHospPayableAmt() {
		return postHospPayableAmt;
	}
	public void setPostHospPayableAmt(Integer postHospPayableAmt) {
		this.postHospPayableAmt = postHospPayableAmt;
	}
	public Integer getAddonBenefitAmt() {
		return addonBenefitAmt;
	}
	public void setAddonBenefitAmt(Integer addonBenefitAmt) {
		this.addonBenefitAmt = addonBenefitAmt;
	}
	public Integer getAmountPayableToInsAftPremium() {
		return amountPayableToInsAftPremium;
	}
	public void setAmountPayableToInsAftPremium(Integer amountPayableToInsAftPremium) {
		this.amountPayableToInsAftPremium = amountPayableToInsAftPremium;
	}
	public Integer getPremiumAmt() {
		return premiumAmt;
	}
	public void setPremiumAmt(Integer premiumAmt) {
		this.premiumAmt = premiumAmt;
	}
	public Integer getAmountPayableAfterPremium() {
		return amountPayableAfterPremium;
	}
	public void setAmountPayableAfterPremium(Integer amountPayableAfterPremium) {
		this.amountPayableAfterPremium = amountPayableAfterPremium;
	}
	public Integer getLumpusmPayableAmt() {
		return lumpusmPayableAmt;
	}
	public void setLumpusmPayableAmt(Integer lumpusmPayableAmt) {
		this.lumpusmPayableAmt = lumpusmPayableAmt;
	}
	public Integer getOtherBenefitAmt() {
		return otherBenefitAmt;
	}
	public void setOtherBenefitAmt(Integer otherBenefitAmt) {
		this.otherBenefitAmt = otherBenefitAmt;
	}
}
