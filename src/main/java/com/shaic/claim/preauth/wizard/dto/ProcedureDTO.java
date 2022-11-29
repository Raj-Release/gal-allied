package com.shaic.claim.preauth.wizard.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractRowEnablerDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.SublimitFunObject;

public class ProcedureDTO  extends AbstractRowEnablerDTO {
	
	protected Long key;
	
	protected Long preauthKey;
	
	private String referenceNo;
	
	private String treatementType;
	
//	@NotNull(message = "Please Select Procedure Name")
	private SelectValue procedureName;
	
	private SelectValue procedureCode;
	
	//private Boolean statusFlag;
	
	private SelectValue copay;
	
	private Long packageRate;
	
	@NotNull(message = "Please Select Consider For Day Care.")
	private SelectValue considerForDayCare;
	
	private SelectValue sublimitApplicable;
	
	private String sublimitApplicableFlag;
	
	private SublimitFunObject sublimitName;
	
	private String sublimitDesc;
	
	private String sublimitAmount;
	
	protected String recTypeFlag = "A";
	
	private SelectValue considerForPayment;
	
	protected String procedureNameValue;
	
	protected String procedureCodeValue;
	
	protected String packageAvailable;
	
	protected Double approvedAmount;
	
	private Double netApprovedAmount;
	
	protected Double diffAmount;
	
	protected String pakageRate;
	
	protected String subLimitApplicable;
	
	protected String considerForPaymentValue;
	
	protected String considerForPaymentFlag;
	
	protected Double downsizedAmount;
	
	protected String dayCareProcedure;
	
	protected String dayCareProcedureFlag;
	
	protected String considerForDayCareValue;
	
	protected String considerForDayFlag;
	
	protected String policyAging;
	
	protected Long newProcedureFlag;
	
	protected SelectValue sublimitNameId;
	
	protected SelectValue procedureStatus;

	protected SelectValue exclusionDetails;
	
	private Double subLimitAmount;
	
	private String packageAmount;
	
	private String approvedRemarks;
	
	private Boolean enableOrDisable = true ;
	
	private String processFlag;
	
	private String actions;
	
	protected Double oldApprovedAmount;
	
	protected Double oldDiffAmount;
	
	private Double amountConsideredAmount;
	
	private String sittingsInput;
	
	private Boolean isAmbChargeApplicable;
	
	private String isAmbChargeFlag;
	
	private Integer ambulanceCharge = 0;
	
	private Integer amtWithAmbulanceCharge = 0;
	
	private String createdBy;
	    
	private Date createDate;
	
	private Boolean isSublimitValueChange;
	
	private SelectValue coPayTypeId;
	
	private Boolean isGMC = false;
	
	private Boolean isTata = false;
	
	private Long agreedPackageRate;
	
	private String pkgReasonForChge;

	private Long oldprocedureID; 
	
	private String procedureFlag;
	
	private Long oldSpecialityKey; 
	
    private String newProcedureName;
	
	private SelectValue speciality;
	
	private String specialityCodeValue;
	
	private String oldNewProcedureName;
	
	public Boolean getIsTata() {
		return isTata;
	}

	public void setIsTata(Boolean isTata) {
		this.isTata = isTata;
	}

	public Boolean getIsPayaas() {
		return isPayaas;
	}

	public void setIsPayaas(Boolean isPayaas) {
		this.isPayaas = isPayaas;
	}

	private Boolean isPayaas = false;
	
	public String getRecTypeFlag() {
		return recTypeFlag;
	}

	public void setRecTypeFlag(String recTypeFlag) {
		this.recTypeFlag = recTypeFlag;
	}

	private Double minimumAmount;
	
	private Double copayPercentage;
	
	private Double copayAmount;
	
	private Double netAmount;
	
	private String sublimitNameValue;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getPreauthKey() {
		return preauthKey;
	}

	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}

	public SelectValue getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(SelectValue procedureName) {
		this.procedureName = procedureName;
	}

	public SelectValue getProcedureCode() {
		return procedureCode;
	}

	public void setProcedureCode(SelectValue procedureCode) {
		this.procedureCode = procedureCode;
	}


	public SelectValue getConsiderForDayCare() {
		return considerForDayCare;
	}

	public void setConsiderForDayCare(SelectValue considerForDayCare) {
		this.considerForDayCare = considerForDayCare;
		this.considerForDayFlag = this.considerForDayCare != null && this.considerForDayCare.getValue().toString().toLowerCase().contains("yes") ? "Y" : "N";
	}

	public SelectValue getSublimitApplicable() {
		
		return sublimitApplicable;
		
	}

	public void setSublimitApplicable(SelectValue sublimitApplicable) {
		this.sublimitApplicable = sublimitApplicable;
		this.sublimitApplicableFlag = this.sublimitApplicable != null && this.sublimitApplicable.getValue().toString().toLowerCase().contains("yes") ? "Y" : "N";
	}

	public String getSublimitApplicableFlag() {
		return sublimitApplicableFlag;
	}

	public void setSublimitApplicableFlag(String sublimitApplicableFlag) {
		this.sublimitApplicableFlag = sublimitApplicableFlag;
		this.sublimitApplicable = new SelectValue();
		this.sublimitApplicable.setId((this.sublimitApplicableFlag != null && this.sublimitApplicableFlag.equalsIgnoreCase("Y")) ? ReferenceTable.COMMONMASTER_YES : ReferenceTable.COMMONMASTER_NO);
	}

	public SublimitFunObject getSublimitName() {
		return sublimitName;
	}

	public void setSublimitName(SublimitFunObject sublimitName) {
		this.sublimitName = sublimitName;
	}

	public String getSublimitDesc() {
		return sublimitDesc;
	}

	public void setSublimitDesc(String sublimitDesc) {
		this.sublimitDesc = sublimitDesc;
	}

	public String getSublimitAmount() {
		return sublimitAmount;
	}

	public void setSublimitAmount(String sublimitAmount) {
		this.sublimitAmount = sublimitAmount;
	}

	public SelectValue getConsiderForPayment() {
		return considerForPayment;
	}

	public void setConsiderForPayment(SelectValue considerForPayment) {
		this.considerForPayment = considerForPayment;
		this.considerForPaymentFlag = this.considerForPayment != null && this.considerForPayment.getValue().toString().toLowerCase().contains("yes") ? "Y" : "N";
	}

	public String getProcedureNameValue() {
		return procedureNameValue;
	}

	public void setProcedureNameValue(String procedureNameValue) {
		this.procedureNameValue = procedureNameValue;
	}

	public String getProcedureCodeValue() {
		return procedureCodeValue;
	}

	public void setProcedureCodeValue(String procedureCodeValue) {
		this.procedureCodeValue = procedureCodeValue;
	}

	public String getPackageAvailable() {
		return packageAvailable;
	}

	public void setPackageAvailable(String packageAvailable) {
		this.packageAvailable = packageAvailable;
	}

	public Double getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public String getPakageRate() {
		return pakageRate;
	}

	public void setPakageRate(String pakageRate) {
		this.pakageRate = pakageRate;
	}

	public String getSubLimitApplicable() {
		return subLimitApplicable;
	}

	public void setSubLimitApplicable(String subLimitApplicable) {
		this.subLimitApplicable = subLimitApplicable;
	}

	public String getConsiderForPaymentValue() {
		return considerForPaymentValue;
	}

	public void setConsiderForPaymentValue(String considerForPaymentValue) {
		this.considerForPaymentValue = considerForPaymentValue;
	}

	public String getConsiderForPaymentFlag() {
		return considerForPaymentFlag;
	}

	public Long getPackageRate() {
		return packageRate;
	}

	public void setPackageRate(Long packageRate) {
		this.packageRate = packageRate;
	}

	public void setConsiderForPaymentFlag(String considerForPaymentFlag) {
		this.considerForPaymentFlag = considerForPaymentFlag;
		this.considerForPayment = new SelectValue();
		this.considerForPayment.setId((this.considerForPaymentFlag != null && this.considerForPaymentFlag.equalsIgnoreCase("Y")) ? ReferenceTable.COMMONMASTER_YES : ReferenceTable.COMMONMASTER_NO);
	}

	public Double getDownsizedAmount() {
		return downsizedAmount;
	}

	public void setDownsizedAmount(Double downsizedAmount) {
		this.downsizedAmount = downsizedAmount;
	}

	public String getDayCareProcedure() {
		return dayCareProcedure;
	}

	public void setDayCareProcedure(String dayCareProcedure) {
		this.dayCareProcedure = dayCareProcedure;
		this.dayCareProcedureFlag = this.dayCareProcedure != null && this.dayCareProcedure.toLowerCase().equalsIgnoreCase("yes") ? "Y" : "N";
	}

	public String getConsiderForDayCareValue() {
		return considerForDayCareValue;
	}

	public void setConsiderForDayCareValue(String considerForDayCareValue) {
		this.considerForDayCareValue = considerForDayCareValue;
	}

	public String getConsiderForDayFlag() {
		return considerForDayFlag;
	}

	public void setConsiderForDayFlag(String considerForDayFlag) {
		this.considerForDayFlag = considerForDayFlag;
		this.considerForDayCare = new SelectValue();
		this.considerForDayCare.setId((this.considerForDayFlag != null && this.considerForDayFlag.equalsIgnoreCase("Y")) ? ReferenceTable.COMMONMASTER_YES : ReferenceTable.COMMONMASTER_NO);
	}

	public String getPolicyAging() {
		return policyAging;
	}

	public void setPolicyAging(String policyAging) {
		this.policyAging = policyAging;
	}

	public Long getNewProcedureFlag() {
		return newProcedureFlag;
	}

	public void setNewProcedureFlag(Long newProcedureFlag) {
		this.newProcedureFlag = newProcedureFlag;
	}

	public SelectValue getProcedureStatus() {
		return procedureStatus;
	}

	public void setProcedureStatus(SelectValue procedureStatus) {
		this.procedureStatus = procedureStatus;
	}

	public SelectValue getExclusionDetails() {
		return exclusionDetails;
	}

	public void setExclusionDetails(SelectValue exclusionDetails) {
		this.exclusionDetails = exclusionDetails;
	}

	public Double getSubLimitAmount() {
		return subLimitAmount;
	}

	public void setSubLimitAmount(Double subLimitAmount) {
		this.subLimitAmount = subLimitAmount;
	}

	public String getPackageAmount() {
		return packageAmount;
	}

	public void setPackageAmount(String packageAmount) {
		this.packageAmount = packageAmount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	protected String remarks;

	public String getDayCareProcedureFlag() {
		return dayCareProcedureFlag;
	}

	public void setDayCareProcedureFlag(String dayCareProcedureFlag) {
		this.dayCareProcedureFlag = dayCareProcedureFlag;
		this.dayCareProcedure = this.dayCareProcedureFlag != null && this.dayCareProcedureFlag.toLowerCase().equalsIgnoreCase("y") ? "Yes" : "No";
	}

	public SelectValue getSublimitNameId() {
		return sublimitNameId;
	}

	public void setSublimitNameId(SelectValue sublimitNameId) {
		this.sublimitNameId = sublimitNameId;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getTreatementType() {
		return treatementType;
	}

	public void setTreatementType(String treatementType) {
		this.treatementType = treatementType;
	}

	public String getApprovedRemarks() {
		return approvedRemarks;
	}

	public void setApprovedRemarks(String approvedRemarks) {
		this.approvedRemarks = approvedRemarks;
	}

	public Boolean getEnableOrDisable() {
		return enableOrDisable;
	}

	public void setEnableOrDisable(Boolean enableOrDisable) {
		this.enableOrDisable = enableOrDisable;
	}

	public String getProcessFlag() {
		return processFlag;
	}

	public void setProcessFlag(String processFlag) {
		this.processFlag = processFlag;
	}

	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

	/*public Boolean getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(Boolean statusFlag) {
		this.statusFlag = statusFlag;
	}
*/
	public Double getDiffAmount() {
		return diffAmount;
	}

	public void setDiffAmount(Double diffAmount) {
		this.diffAmount = diffAmount;
	}

	public Double getOldApprovedAmount() {
		return oldApprovedAmount;
	}

	public void setOldApprovedAmount(Double oldApprovedAmount) {
		this.oldApprovedAmount = oldApprovedAmount;
	}

	public Double getOldDiffAmount() {
		return oldDiffAmount;
	}

	public void setOldDiffAmount(Double oldDiffAmount) {
		this.oldDiffAmount = oldDiffAmount;
	}

	public Double getAmountConsideredAmount() {
		return amountConsideredAmount;
	}

	public void setAmountConsideredAmount(Double amountConsideredAmount) {
		this.amountConsideredAmount = amountConsideredAmount;
	}

	public Double getMinimumAmount() {
		return minimumAmount;
	}

	public void setMinimumAmount(Double minimumAmount) {
		this.minimumAmount = minimumAmount;
	}

	public Double getCopayPercentage() {
		return copayPercentage;
	}

	public void setCopayPercentage(Double copayPercentage) {
		this.copayPercentage = copayPercentage;
	}

	public Double getCopayAmount() {
		return copayAmount;
	}

	public void setCopayAmount(Double copayAmount) {
		this.copayAmount = copayAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public SelectValue getCopay() {
		return copay;
	}

	public void setCopay(SelectValue copay) {
		this.copay = copay;
	}

	public String getSublimitNameValue() {
		return sublimitNameValue;
	}

	public void setSublimitNameValue(String sublimitNameValue) {
		this.sublimitNameValue = sublimitNameValue;
	}

	public String getSittingsInput() {
		return sittingsInput;
	}

	public void setSittingsInput(String sittingsInput) {
		this.sittingsInput = sittingsInput;
	}

	public Double getNetApprovedAmount() {
		return netApprovedAmount;
	}

	public void setNetApprovedAmount(Double netApprovedAmount) {
		this.netApprovedAmount = netApprovedAmount;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Boolean getIsAmbChargeApplicable() {
		return isAmbChargeApplicable;
	}

	public void setIsAmbChargeApplicable(Boolean isAmbChargeApplicable) {
		this.isAmbChargeApplicable = isAmbChargeApplicable;
		this.isAmbChargeApplicable = isAmbChargeApplicable;
		this.isAmbChargeFlag = this.isAmbChargeApplicable != null && this.isAmbChargeApplicable ? "Y" : "N";
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

	public Boolean getIsSublimitValueChange() {
		return isSublimitValueChange;
	}

	public void setIsSublimitValueChange(Boolean isSublimitValueChange) {
		this.isSublimitValueChange = isSublimitValueChange;
	}

	public SelectValue getCoPayTypeId() {
		return coPayTypeId;
	}

	public void setCoPayTypeId(SelectValue coPayTypeId) {
		this.coPayTypeId = coPayTypeId;
	}

	public Boolean getIsGMC() {
		return isGMC;
	}

	public void setIsGMC(Boolean isGMC) {
		this.isGMC = isGMC;
	}

	public Long getAgreedPackageRate() {
		return agreedPackageRate;
	}

	public void setAgreedPackageRate(Long agreedPackageRate) {
		this.agreedPackageRate = agreedPackageRate;
	}

	public String getPkgReasonForChge() {
		return pkgReasonForChge;
	}

	public void setPkgReasonForChge(String pkgReasonForChge) {
		this.pkgReasonForChge = pkgReasonForChge;
	}
	
	public Long getOldprocedureID() {
		return oldprocedureID;
	}

	public void setOldprocedureID(Long oldprocedureID) {
		this.oldprocedureID = oldprocedureID;
	}

	public String getProcedureFlag() {
		return procedureFlag;
	}

	public void setProcedureFlag(String procedureFlag) {
		this.procedureFlag = procedureFlag;
	}
	
	public Long getOldSpecialityKey() {
		return oldSpecialityKey;
	}

	public void setOldSpecialityKey(Long oldSpecialityKey) {
		this.oldSpecialityKey = oldSpecialityKey;
	}
	
	public String getNewProcedureName() {
		return newProcedureName;
	}

	public void setNewProcedureName(String newProcedureName) {
		this.newProcedureName = newProcedureName;
	}

	public SelectValue getSpeciality() {
		return speciality;
	}

	public void setSpeciality(SelectValue speciality) {
		this.speciality = speciality;
	}

	public String getSpecialityCodeValue() {
		return specialityCodeValue;
	}

	public void setSpecialityCodeValue(String specialityCodeValue) {
		this.specialityCodeValue = specialityCodeValue;
	}
	
	public String getOldNewProcedureName() {
		return oldNewProcedureName;
	}

	public void setOldNewProcedureName(String oldNewProcedureName) {
		this.oldNewProcedureName = oldNewProcedureName;
	}
}
