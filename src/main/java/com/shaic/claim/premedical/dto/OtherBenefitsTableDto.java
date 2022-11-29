package com.shaic.claim.premedical.dto;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.domain.preauth.BenefitAmountDetails;

public class OtherBenefitsTableDto extends AbstractTableDTO{
	
	private Long benefitKey;
	
	private Long benefitObjId;
	
	private String benefitName;
	
	private Long noOfDays;
	
	private int sno;
	
	private SelectValue applicable;
	
	private Double amtClaimed;
	
	private Double nonPayable;
	
	private Double netPayable;
	
	private Double eligibleAmt;
	
	private Double approvedAmt;
	
	private Double amtPaybableToInsured;
	
	private Double amtAlreadyPaidToHospital;
	
    private Double amtAlreadyPaid;
    
    private Double balancePayable;
	
	private String remarks;
	
	private String nonPayableRmrksForAssessmentSheet;
	
	private boolean isEnabled;
	
	private Double totalBenefitApprovedAmt;
	
	private Long claimKey;
	
	private Long insuredKey;
	
	private String deletedFlag;
	
	private Integer noOfdaysLimit = 0;
	
	private Boolean isPreferred = false;
	
	public OtherBenefitsTableDto(){
		
	}
	public OtherBenefitsTableDto(BenefitAmountDetails benefitObj){
		
		this.sno = benefitObj.getSeqNo() != null ? Integer.valueOf(benefitObj.getSeqNo()) : 0 ;
		this.benefitObjId = benefitObj.getBenefit().getKey();
		this.benefitName = benefitObj.getBenefit().getLimitDescription();
		this.amtClaimed = benefitObj.getClaimedAmt();
		this.nonPayable = benefitObj.getNonPayableAmt();
		this.netPayable = benefitObj.getNetAmt();
		this.eligibleAmt = benefitObj.getEligibleAmt();
		this.approvedAmt = benefitObj.getPayableAmt();
		this.amtAlreadyPaid = benefitObj.getAmtAlreadyPaid();
		this.amtAlreadyPaidToHospital = benefitObj.getAlreadyPaidToHosp();
		this.balancePayable = benefitObj.getBalancePayable();
		this.amtPaybableToInsured = benefitObj.getAmountPayableToInsured();
		this.remarks = benefitObj.getRemarks();
		this.applicable = new SelectValue(SHAConstants.YES_FLAG.equals(benefitObj.getApplicable()) ? 1l : 0l, SHAConstants.YES_FLAG.equals(benefitObj.getApplicable()) ? "Yes" : "No");
		this.totalBenefitApprovedAmt = 0d;
		this.isEnabled =  SHAConstants.YES_FLAG.equals(benefitObj.getApplicable()) ? true : false;
		this.benefitKey = benefitObj.getKey();
		this.remarks = benefitObj.getRemarks();
		this.deletedFlag = benefitObj.getDeletedFlag();
		if(benefitObj.getNumberOfDays() != null){
			this.noOfDays = benefitObj.getNumberOfDays().longValue();
		}
		
	
	}
	
	public OtherBenefitsTableDto(int seqNo, Long benefitId, String name, Double eligibleAmt){
		this.sno = seqNo;
		this.benefitObjId = benefitId;
		this.benefitName = name;
		this.eligibleAmt = eligibleAmt;
		this.applicable = new SelectValue(null,"No");
		this.totalBenefitApprovedAmt = 0d;
		this.isEnabled = true;
		
	}

	public String getBenefitName() {
		return benefitName;
	}

	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName;
	}

	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
		this.sno = sno;
	}

	public SelectValue getApplicable() {
		return applicable;
	}

	public void setApplicable(SelectValue applicable) {
		this.applicable = applicable;
		if(this.applicable != null && this.applicable.getValue() != null && !this.applicable.getValue().isEmpty() && ("yes").equalsIgnoreCase(this.applicable.getValue())){
			this.isEnabled = true;	
		}
	}

	public Double getAmtClaimed() {
		return amtClaimed;
	}

	public void setAmtClaimed(Double amtClaimed) {
		this.amtClaimed = amtClaimed;
	}

	public Double getNonPayable() {
		return nonPayable;
	}

	public void setNonPayable(Double nonPayable) {
		this.nonPayable = nonPayable;
	}

	public Double getNetPayable() {
		return netPayable;
	}

	public void setNetPayable(Double netPayable) {
		this.netPayable = netPayable;
	}

	public Double getEligibleAmt() {
		return eligibleAmt;
	}

	public void setEligibleAmt(Double eligibleAmt) {
		this.eligibleAmt = eligibleAmt;
	}

	public Double getApprovedAmt() {
		return approvedAmt;
	}

	public void setApprovedAmt(Double approvedAmt) {
		this.approvedAmt = approvedAmt;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Long getBenefitKey() {
		return benefitKey;
	}

	public void setBenefitKey(Long benefitKey) {
		this.benefitKey = benefitKey;
	}
	
	public Double getTotalBenefitApprovedAmt() {
		return totalBenefitApprovedAmt;
	}
	
	public void setTotalBenefitApprovedAmt(Double totalBenefitApprovedAmt) {
		this.totalBenefitApprovedAmt = totalBenefitApprovedAmt;
	}
	public Long getNoOfDays() {
		return noOfDays;
	}
	public void setNoOfDays(Long noOfDays) {
		this.noOfDays = noOfDays;
	}
	public Double getAmtPaybableToInsured() {
		return amtPaybableToInsured;
	}
	public void setAmtPaybableToInsured(Double amtPaybableToInsured) {
		this.amtPaybableToInsured = amtPaybableToInsured;
	}
	public Double getAmtAlreadyPaid() {
		return amtAlreadyPaid;
	}
	public void setAmtAlreadyPaid(Double amtAlreadyPaid) {
		this.amtAlreadyPaid = amtAlreadyPaid;
	}
	public Double getBalancePayable() {
		return balancePayable;
	}
	public void setBalancePayable(Double balancePayable) {
		this.balancePayable = balancePayable;
	}
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public Long getInsuredKey() {
		return insuredKey;
	}
	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
	}
	public Double getAmtAlreadyPaidToHospital() {
		return amtAlreadyPaidToHospital;
	}
	public void setAmtAlreadyPaidToHospital(Double amtAlreadyPaidToHospital) {
		this.amtAlreadyPaidToHospital = amtAlreadyPaidToHospital;
	}
	public Long getBenefitObjId() {
		return benefitObjId;
	}
	public void setBenefitObjId(Long benefitObjId) {
		this.benefitObjId = benefitObjId;
	}
	public String getDeletedFlag() {
		return deletedFlag;
	}
	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}
	public Integer getNoOfdaysLimit() {
		return noOfdaysLimit;
	}
	public void setNoOfdaysLimit(Integer noOfdaysLimit) {
		this.noOfdaysLimit = noOfdaysLimit;
	}
	public Boolean getIsPreferred() {
		return isPreferred;
	}
	public void setIsPreferred(Boolean isPreferred) {
		this.isPreferred = isPreferred;
	}
	public String getNonPayableRmrksForAssessmentSheet() {
		return nonPayableRmrksForAssessmentSheet;
	}
	public void setNonPayableRmrksForAssessmentSheet(
			String nonPayableRmrksForAssessmentSheet) {
		this.nonPayableRmrksForAssessmentSheet = nonPayableRmrksForAssessmentSheet;
	}		
	
}
