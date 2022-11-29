package com.shaic.claim.preauth.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.domain.SublimitFunObject;

public class DiagnosisProcedureTableDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ProcedureDTO procedureDTO;
	
	private DiagnosisDetailsTableDTO diagnosisDetailsDTO;
	
	private String diagOrProcedure;
	
	private String description;
	
	private String pedOrExclusion;
	
	private Integer amountConsidered = 0;
	
	private String packageAmt;
	
	private String restrictionSI;
	
	private Integer utilizedAmt = 0;
	
	private Integer availableAmout = 0;
	
	private Integer minimumAmount = 0;
	
	private Integer minimumAmountOfAmtconsideredAndPackAmt = 0;
	
	private SelectValue coPayPercentage;
	
	private Double copayPercentageAmount;
	
	private List<String> coPayPercentageValues;
	
	private Integer coPayAmount = 0;
	
	private Integer netAmount = 0;
	
	private String subLimitAmount;
	
	private Integer subLimitUtilAmount = 0;
	
	private Integer subLimitAvaliableAmt = 0;
	
	private Integer netApprovedAmt = 0 ;
	
	private String remarks;

	private Boolean isEnabled = true;
	
	private Boolean isView = false;
	
	private Boolean isPaymentAvailable = true;
	
	private String sublimitApplicable;
	
	private String sublimitName;
	
	private String considerForPayment;
	
	private String pedImpactOnDiagnosis;
	
	private SelectValue reasonForNotPaying;

	private String notPayingReason;
	
	private Integer reverseAllocatedAmt =0;
	
	private Boolean isDeletedOne = false;
	
	private Integer postHospitalizationAmt = 0;
	
	private Boolean isAmbChargeApplicable = false;
	
	private String isAmbChargeFlag;
	
	private Integer ambulanceCharge = 0;
	
	private Integer amtWithAmbulanceCharge = 0;
	
	private Boolean isAmbulanceEnable = false;	
	
	private SelectValue considerForPaymnt;
	
	private String diagOrProcedureFlag;
	
	private Boolean isPedExclusionFlag = true;
	
	private List<String> copayValues = new ArrayList<String>();
	
	private SelectValue sublimitYesOrNo;
	
	private SublimitFunObject sublimitValues;
	
	private SelectValue coPayType;
	
	private boolean isIcdSublimitMapAvailable;
	
	private String icdSublimitMapName;
	
	private Boolean isDiagnosisSublimitChanged = false;
	
	private Boolean isProcedureSublimitChanged = false;
	
	private String agreedPackageAmt;
	
	private String reasonForPkgChange;
	
	public String getDiagOrProcedure() {
		return diagOrProcedure;
	}

	public void setDiagOrProcedure(String diagOrProcedure) {
		this.diagOrProcedure = diagOrProcedure;
	}

	public String getDescription() {
		if (procedureDTO != null) {
		this.description = procedureDTO.getProcedureNameValue();
	} else if(diagnosisDetailsDTO != null) {
		this.description = diagnosisDetailsDTO.getDiagnosis();
	} 
	return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPedOrExclusion() {
		String concatenatedString = "";
		if(procedureDTO != null) {
			concatenatedString = concatenatedString +  ((procedureDTO.getProcedureStatus() != null ? procedureDTO.getProcedureStatus().getValue() + " -" : "") + (procedureDTO.getExclusionDetails() != null ? procedureDTO.getExclusionDetails().getValue() + " ;" : ""));
			this.pedOrExclusion = procedureDTO.getExclusionDetails() != null ? procedureDTO.getExclusionDetails().getValue() : null; 
		} else if(diagnosisDetailsDTO != null) {
			
			List<PedDetailsTableDTO> pedList = diagnosisDetailsDTO.getPedList();
			if(!pedList.isEmpty()) {
				for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
					concatenatedString = concatenatedString +  ((pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis().getValue() + " -" : "") + (pedDetailsTableDTO.getExclusionDetails() != null ? pedDetailsTableDTO.getExclusionDetails().getValue() + " ;" : "")); 
				}
			}
		}
		pedOrExclusion = concatenatedString;
		return pedOrExclusion;
	}

	public void setPedOrExclusion(String pedOrExclusion) {
		this.pedOrExclusion = pedOrExclusion;
	}

	public Integer getAmountConsidered() {
		return amountConsidered;
	}

	public void setAmountConsidered(Integer amountConsidered) {
		this.amountConsidered = amountConsidered;
	}

	public String getPackageAmt() {
		return packageAmt;
	}

	public void setPackageAmt(String packageAmt) {
		this.packageAmt = packageAmt;
	}

	public String getRestrictionSI() {
		return restrictionSI;
	}

	public void setRestrictionSI(String restrictionSI) {
		this.restrictionSI = restrictionSI;
	}

	public Integer getUtilizedAmt() {
		return utilizedAmt;
	}

	public Integer getMinimumAmountOfAmtconsideredAndPackAmt() {
		return minimumAmountOfAmtconsideredAndPackAmt;
	}

	public void setMinimumAmountOfAmtconsideredAndPackAmt(
			Integer minimumAmountOfAmtconsideredAndPackAmt) {
		this.minimumAmountOfAmtconsideredAndPackAmt = minimumAmountOfAmtconsideredAndPackAmt;
	}

	public void setUtilizedAmt(Integer utilizedAmt) {
		this.utilizedAmt = utilizedAmt;
	}

	public Integer getAvailableAmout() {
		return availableAmout;
	}

	public void setAvailableAmout(Integer availableAmout) {
		this.availableAmout = availableAmout;
	}

	public Integer getMinimumAmount() {
		return minimumAmount;
	}

	public void setMinimumAmount(Integer minimumAmount) {
		this.minimumAmount = minimumAmount;
	}

	public Integer getCoPayAmount() {
		return coPayAmount;
	}

	public void setCoPayAmount(Integer coPayAmount) {
		this.coPayAmount = coPayAmount;
	}

	public Integer getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Integer netAmount) {
		this.netAmount = netAmount;
	}

	public String getSubLimitAmount() {
		return subLimitAmount;
	}

	public void setSubLimitAmount(String subLimitAmount) {
		this.subLimitAmount = subLimitAmount;
	}

	public Integer getSubLimitUtilAmount() {
		return subLimitUtilAmount;
	}

	public void setSubLimitUtilAmount(Integer subLimitUtilAmount) {
		this.subLimitUtilAmount = subLimitUtilAmount;
	}

	public Boolean getIsDeletedOne() {
		return isDeletedOne;
	}

	public SelectValue getSublimitYesOrNo() {
		return sublimitYesOrNo;
	}

	public void setSublimitYesOrNo(SelectValue sublimitYesOrNo) {
		this.sublimitYesOrNo = sublimitYesOrNo;
	}

	public SublimitFunObject getSublimitValues() {
		return sublimitValues;
	}

	public void setSublimitValues(SublimitFunObject sublimitValues) {
		this.sublimitValues = sublimitValues;
	}

	public void setIsDeletedOne(Boolean isDeletedOne) {
		this.isDeletedOne = isDeletedOne;
	}

	public Integer getSubLimitAvaliableAmt() {
		return subLimitAvaliableAmt;
	}

	public void setSubLimitAvaliableAmt(Integer subLimitAvaliableAmt) {
		this.subLimitAvaliableAmt = subLimitAvaliableAmt;
	}

	public String getSublimitApplicable() {
		return sublimitApplicable;
	}

	public void setSublimitApplicable(String sublimitApplicable) {
		this.sublimitApplicable = sublimitApplicable;
	}

	public String getSublimitName() {
		return sublimitName;
	}

	public void setSublimitName(String sublimitName) {
		this.sublimitName = sublimitName;
	}

	public String getConsiderForPayment() {
		return considerForPayment;
	}

	public void setConsiderForPayment(String considerForPayment) {
		this.considerForPayment = considerForPayment;
	}

	public Integer getNetApprovedAmt() {
		return netApprovedAmt;
	}

	public void setNetApprovedAmt(Integer netApprovedAmt) {
		this.netApprovedAmt = netApprovedAmt;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public ProcedureDTO getProcedureDTO() {
		return procedureDTO;
	}

	public void setProcedureDTO(ProcedureDTO procedureDTO) {
		this.procedureDTO = procedureDTO;
	}

	public DiagnosisDetailsTableDTO getDiagnosisDetailsDTO() {
		return diagnosisDetailsDTO;
	}

	public void setDiagnosisDetailsDTO(DiagnosisDetailsTableDTO diagnosisDetailsDTO) {
		this.diagnosisDetailsDTO = diagnosisDetailsDTO;
	}

	public SelectValue getCoPayPercentage() {
		return coPayPercentage;
	}

	public void setCoPayPercentage(SelectValue coPayPercentage) {
		this.coPayPercentage = coPayPercentage;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public List<String> getCopayValues() {
		return copayValues;
	}

	public void setCopayValues(List<String> copayValues) {
		this.copayValues = copayValues;
	}

	public Boolean getIsPaymentAvailable() {
		return isPaymentAvailable;
	}

	public void setIsPaymentAvailable(Boolean isPaymentAvailable) {
		this.isPaymentAvailable = isPaymentAvailable;
	}

	public List<String> getCoPayPercentageValues() {
		return coPayPercentageValues;
	}

	public void setCoPayPercentageValues(List<String> coPayPercentageValues) {
		this.coPayPercentageValues = coPayPercentageValues;
	}

	public Boolean getIsView() {
		return isView;
	}

	public void setIsView(Boolean isView) {
		this.isView = isView;
	}

	public Integer getPostHospitalizationAmt() {
		return postHospitalizationAmt;
	}

	public void setPostHospitalizationAmt(Integer postHospitalizationAmt) {
		this.postHospitalizationAmt = postHospitalizationAmt;
	}

	public Integer getReverseAllocatedAmt() {
		return reverseAllocatedAmt;
	}

	public void setReverseAllocatedAmt(Integer reverseAllocatedAmt) {
		this.reverseAllocatedAmt = reverseAllocatedAmt;
	}

	public Boolean getIsAmbChargeApplicable() {
		return isAmbChargeApplicable;
	}

	public void setIsAmbChargeApplicable(Boolean isAmbChargeApplicable) {
		this.isAmbChargeApplicable = isAmbChargeApplicable;
		this.isAmbChargeFlag = this.isAmbChargeApplicable != null && this.isAmbChargeApplicable ? "Y" : "N";
	}

	public Integer getAmbulanceCharge() {
		return ambulanceCharge;
	}

	public void setAmbulanceCharge(Integer ambulanceCharge) {
		this.ambulanceCharge = ambulanceCharge;
	}

	public Integer getAmtWithAmbulanceCharge() {
		return amtWithAmbulanceCharge;
	}

	public void setAmtWithAmbulanceCharge(Integer amtWithAmbulanceCharge) {
		this.amtWithAmbulanceCharge = amtWithAmbulanceCharge;
	}

	public Boolean getIsAmbulanceEnable() {
		return isAmbulanceEnable;
	}

	public void setIsAmbulanceEnable(Boolean isAmbulanceEnable) {
		this.isAmbulanceEnable = isAmbulanceEnable;
	}

	public String getIsAmbChargeFlag() {
		return isAmbChargeFlag;
	}

	public void setIsAmbChargeFlag(String isAmbChargeFlag) {
		this.isAmbChargeFlag = isAmbChargeFlag;
		
		if(this.isAmbChargeFlag != null && this.isAmbChargeFlag.equalsIgnoreCase("Y")) {
			this.isAmbChargeApplicable = true;
		}
	}

	public SelectValue getConsiderForPaymnt() {
		return considerForPaymnt;
	}

	public void setConsiderForPaymnt(SelectValue considerForPaymnt) {
		this.considerForPaymnt = considerForPaymnt;
	}

	public Double getCopayPercentageAmount() {
		return copayPercentageAmount;
	}

	public void setCopayPercentageAmount(Double copayPercentageAmount) {
		this.copayPercentageAmount = copayPercentageAmount;
	}

	public String getDiagOrProcedureFlag() {
		return diagOrProcedureFlag;
	}

	public void setDiagOrProcedureFlag(String diagOrProcedureFlag) {
		this.diagOrProcedureFlag = diagOrProcedureFlag;
	}

	public Boolean getIsPedExclusionFlag() {
		return isPedExclusionFlag;
	}

	public void setIsPedExclusionFlag(Boolean isPedExclusionFlag) {
		this.isPedExclusionFlag = isPedExclusionFlag;
	}

	public SelectValue getCoPayType() {
		return coPayType;
	}

	public void setCoPayType(SelectValue coPayType) {
		this.coPayType = coPayType;
	}

	public boolean isIcdSublimitMapAvailable() {
		return isIcdSublimitMapAvailable;
	}

	public void setIcdSublimitMapAvailable(boolean isIcdSublimitMapAvailable) {
		this.isIcdSublimitMapAvailable = isIcdSublimitMapAvailable;
	}

	public String getIcdSublimitMapName() {
		return icdSublimitMapName;
	}

	public void setIcdSublimitMapName(String icdSublimitMapName) {
		this.icdSublimitMapName = icdSublimitMapName;
	}

	public Boolean getIsDiagnosisSublimitChanged() {
		return isDiagnosisSublimitChanged;
	}

	public void setIsDiagnosisSublimitChanged(Boolean isDiagnosisSublimitChanged) {
		this.isDiagnosisSublimitChanged = isDiagnosisSublimitChanged;
	}

	public Boolean getIsProcedureSublimitChanged() {
		return isProcedureSublimitChanged;
	}

	public void setIsProcedureSublimitChanged(Boolean isProcedureSublimitChanged) {
		this.isProcedureSublimitChanged = isProcedureSublimitChanged;
	}

	public String getPedImpactOnDiagnosis() {
		return pedImpactOnDiagnosis;
	}

	public void setPedImpactOnDiagnosis(String pedImpactOnDiagnosis) {
		this.pedImpactOnDiagnosis = pedImpactOnDiagnosis;
	}

	public String getNotPayingReason() {
		return notPayingReason;
	}

	public void setNotPayingReason(String notPayingReason) {
		this.notPayingReason = notPayingReason;
	}

	public SelectValue getReasonForNotPaying() {
		return reasonForNotPaying;
	}

	public void setReasonForNotPaying(SelectValue reasonForNotPaying) {
		this.reasonForNotPaying = reasonForNotPaying;
	}

	public String getAgreedPackageAmt() {
		return agreedPackageAmt;
	}

	public void setAgreedPackageAmt(String agreedPackageAmt) {
		this.agreedPackageAmt = agreedPackageAmt;
	}

	public String getReasonForPkgChange() {
		return reasonForPkgChange;
	}

	public void setReasonForPkgChange(String reasonForPkgChange) {
		this.reasonForPkgChange = reasonForPkgChange;
	}
	
}