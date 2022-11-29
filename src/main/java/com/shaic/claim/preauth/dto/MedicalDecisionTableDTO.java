package com.shaic.claim.preauth.dto;

import java.io.Serializable;
import java.util.List;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;

public class MedicalDecisionTableDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ProcedureDTO procedureDTO;
	
	private DiagnosisDetailsTableDTO diagnosisDetailsDTO;
	
	private Long key;
	
	private String referenceNo;

	private String treatmentType;

	private String procedureOrDiagnosis;

	private String description;

	private String pedOrExclusionDetails;

	private String currentSubLimitAmount;

	private String sumInsuredRestriction;

	private String subLimitUtilizedAmount;

	private String availableSublimit;

	private String packageAmount;

	private String approvedAmount;

	private String remarks;

	private String addedAmount;

	private String cumulativeAmt;

	private String totalApprovedAmt;
	
	private String reduceAmount;
	
	private String downSizeCumulativeAmt;
	
	private String downSizeTotalApproveAmt;
	
	private String oldApprovedAmount;
	
	private String oldDiffAmount;
	
	private String procedureOrDiag;

	private Boolean isEnabled = true;
	
	//Added for testing
	private String packageRate;

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
	}

	public String getDescription() {
		/*if (procedureDTO != null) {
			this.procedureOrDiagnosis = "";
		} else if(diagnosisDetailsDTO != null) {
			this.procedureOrDiagnosis = diagnosisDetailsDTO.getPedName();
		}*/
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

	public String getPedOrExclusionDetails() {
		String concatenatedString = "";
		if(procedureDTO != null) {
			concatenatedString = concatenatedString +  ((procedureDTO.getProcedureStatus() != null ? procedureDTO.getProcedureStatus().getValue() + " -" : "") + (procedureDTO.getExclusionDetails() != null ? procedureDTO.getExclusionDetails().getValue() + " ;" : ""));
			this.pedOrExclusionDetails = procedureDTO.getExclusionDetails() != null ? procedureDTO.getExclusionDetails().getValue() : null; 
		} else if(diagnosisDetailsDTO != null) {
			
			List<PedDetailsTableDTO> pedList = diagnosisDetailsDTO.getPedList();
			if(!pedList.isEmpty()) {
				for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
					concatenatedString = concatenatedString +  ((pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO.getPedExclusionImpactOnDiagnosis().getValue() + " -" : "") + (pedDetailsTableDTO.getExclusionDetails() != null ? pedDetailsTableDTO.getExclusionDetails().getValue() + " ;" : "")); 
				}
			}
		}
		pedOrExclusionDetails = concatenatedString;
		return pedOrExclusionDetails;
	}

	public void setPedOrExclusionDetails(String pedOrExclusionDetails) {
		this.pedOrExclusionDetails = pedOrExclusionDetails;
	}

	public String getCurrentSubLimitAmount() {
		return currentSubLimitAmount;
	}

	public void setCurrentSubLimitAmount(String currentSubLimitAmount) {
		this.currentSubLimitAmount = currentSubLimitAmount;
	}

	public String getSumInsuredRestriction() {
		return sumInsuredRestriction;
	}

	public void setSumInsuredRestriction(String sumInsuredRestriction) {
		this.sumInsuredRestriction = sumInsuredRestriction;
	}

	public String getSubLimitUtilizedAmount() {
		return subLimitUtilizedAmount;
	}

	public void setSubLimitUtilizedAmount(String subLimitUtilizedAmount) {
		this.subLimitUtilizedAmount = subLimitUtilizedAmount;
	}

	public String getAvailableSublimit() {
		return availableSublimit;
	}

	public void setAvailableSublimit(String availableSublimit) {
		this.availableSublimit = availableSublimit;
	}

	public String getPackageAmount() {
		if(procedureDTO != null) {
			this.packageAmount = procedureDTO.getPackageAmount(); 
		} else if(diagnosisDetailsDTO != null) {
			this.packageAmount = "-";
		}
		return packageAmount;
	}

	public void setPackageAmount(String packageAmount) {
		this.packageAmount = packageAmount;
	}

	public String getApprovedAmount() {
		if(procedureDTO != null) {
			if(this.totalApprovedAmt != null && this.totalApprovedAmt.length() > 0) {
				Double amt = new Double("0");
				if(this.addedAmount != null && SHAUtils.isValidDouble(this.addedAmount)) {
					amt = Double.valueOf(this.addedAmount);
				} else if(this.cumulativeAmt != null && SHAUtils.isValidDouble(this.cumulativeAmt)) {
					amt = Double.valueOf(this.cumulativeAmt);
				}
				
//				procedureDTO.setApprovedAmount(amt);
			} else {
				procedureDTO.setApprovedAmount(this.approvedAmount != null && SHAUtils.isValidDouble(this.approvedAmount) ? Double.valueOf(this.approvedAmount) : Double.valueOf("0"));
			}
			
		} else if(diagnosisDetailsDTO != null) {
			if(this.totalApprovedAmt != null && this.totalApprovedAmt.length() > 0) {
				Double amt = new Double("0");
				if(this.addedAmount != null && SHAUtils.isValidDouble(this.addedAmount)) {
					amt = Double.valueOf(this.addedAmount);
				} else if(this.cumulativeAmt != null && SHAUtils.isValidDouble(this.cumulativeAmt)) {
					amt = Double.valueOf(this.cumulativeAmt);
				}
//				diagnosisDetailsDTO.setApprovedAmount(amt);
			} else {
				diagnosisDetailsDTO.setApprovedAmount((null != this.approvedAmount && !("").equals(this.approvedAmount)) && SHAUtils.isValidDouble(this.approvedAmount)  ? Double.valueOf(this.approvedAmount) : Double.valueOf("0"));
			}
			
		}

		return approvedAmount;
	}

	public void setApprovedAmount(String approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public String getRemarks() {
		if (procedureDTO != null) {
			procedureDTO.setApprovedRemarks(this.remarks);
		} else if(diagnosisDetailsDTO != null) {
			diagnosisDetailsDTO.setApproveRemarks(this.remarks);
		} 
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getProcedureOrDiagnosis() {

		/*if (procedureDTO != null) {
			this.procedureOrDiagnosis = procedureDTO.getProcedureNameValue();
		} else if(diagnosisDetailsDTO != null) {
			this.procedureOrDiagnosis = diagnosisDetailsDTO.getDiagnosis();
		} */
		return procedureOrDiagnosis;
	}

	public void setProcedureOrDiagnosis(String procedureOrDiagnosis) {
		this.procedureOrDiagnosis = procedureOrDiagnosis;
	}

	public ProcedureDTO getProcedureDTO() {
		return procedureDTO;
	}

	public void setProcedureDTO(ProcedureDTO procedureDTO) {
		this.procedureDTO = procedureDTO;
	}

	public DiagnosisDetailsTableDTO getPedValidationTableDTO() {
		return diagnosisDetailsDTO;
	}

	public void setPedValidationTableDTO(DiagnosisDetailsTableDTO pedValidationTableDTO) {
		this.diagnosisDetailsDTO = pedValidationTableDTO;
	}

	public String getPackageRate() {
		this.packageRate = "-";
		if (procedureDTO != null) {
			this.packageRate = procedureDTO.getPackageRate() != null ? procedureDTO
					.getPackageRate().toString() : "-";
		}
		return packageRate;
	}

	public void setPackageRate(String packageRate) {
		this.packageRate = packageRate;
	}

	public String getAddedAmount() {
//		if(procedureDTO != null) {
//			Double amt = new Double("0");
//			if(this.addedAmount != null && SHAUtils.isValidDouble(this.addedAmount)) {
//				amt = Double.valueOf(this.addedAmount);
//			} 
//			procedureDTO.setApprovedAmount(amt);
//			
//		} else if(diagnosisDetailsDTO != null) {
//			Double amt = new Double("0");
//			if(this.addedAmount != null && SHAUtils.isValidDouble(this.addedAmount)) {
//				amt = Double.valueOf(this.addedAmount);
//			} 
//			diagnosisDetailsDTO.setApprovedAmount(amt);
//		}
		return addedAmount;
	}

	public void setAddedAmount(String addedAmount) {
		this.addedAmount = addedAmount;
	}

	public String getCumulativeAmt() {
//		if(procedureDTO != null) {
//			Double amt = new Double("0");
//			if(this.cumulativeAmt != null && SHAUtils.isValidDouble(this.cumulativeAmt)) {
//				amt = Double.valueOf(this.cumulativeAmt);
//			} 
//			procedureDTO.setApprovedAmount(amt);
//			
//		} else if(diagnosisDetailsDTO != null) {
//			Double amt = new Double("0");
//			if(this.cumulativeAmt != null && SHAUtils.isValidDouble(this.cumulativeAmt)) {
//				amt = Double.valueOf(this.cumulativeAmt);
//			} 
//			diagnosisDetailsDTO.setApprovedAmount(amt);
//		}
		return cumulativeAmt;
	}

	public void setCumulativeAmt(String cumulativeAmt) {
		this.cumulativeAmt = cumulativeAmt;
	}

	public String getTotalApprovedAmt() {
		return totalApprovedAmt;
	}

	public void setTotalApprovedAmt(String totalApprovedAmt) {
		this.totalApprovedAmt = totalApprovedAmt;

	}

	public DiagnosisDetailsTableDTO getDiagnosisDetailsDTO() {
		return diagnosisDetailsDTO;
	}

	public void setDiagnosisDetailsDTO(DiagnosisDetailsTableDTO diagnosisDetailsDTO) {
		this.diagnosisDetailsDTO = diagnosisDetailsDTO;
	}
	public String getReduceAmount() {
		return reduceAmount;
	}

	public void setReduceAmount(String reduceAmount) {
		this.reduceAmount = reduceAmount;
	}

	public String getDownSizeCumulativeAmt() {
		return downSizeCumulativeAmt;
	}

	public void setDownSizeCumulativeAmt(String downSizeCumulativeAmt) {
		this.downSizeCumulativeAmt = downSizeCumulativeAmt;
	}

	public String getDownSizeTotalApproveAmt() {
		return downSizeTotalApproveAmt;
	}

	public void setDownSizeTotalApproveAmt(String downSizeTotalApproveAmt) {
		this.downSizeTotalApproveAmt = downSizeTotalApproveAmt;
	}

	public String getOldApprovedAmount() {
		return oldApprovedAmount;
	}

	public void setOldApprovedAmount(String oldApprovedAmount) {
		this.oldApprovedAmount = oldApprovedAmount;
	}

	public String getOldDiffAmount() {
		return oldDiffAmount;
	}

	public void setOldDiffAmount(String oldDiffAmount) {
		this.oldDiffAmount = oldDiffAmount;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getProcedureOrDiag() {
		return procedureOrDiag;
	}

	public void setProcedureOrDiag(String procedureOrDiag) {
		this.procedureOrDiag = procedureOrDiag;
	}
	
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

}
