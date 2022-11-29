package com.shaic.claim.outpatient.registerclaim.wizard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.SublimitFunObject;
//import com.shaic.domain.preauth.TmpPED;
import com.vaadin.ui.Button;


public class DiagnosisDetailsOPTableDTO extends AbstractTableDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long key;
	
	private Integer dummyKey;
	
	private Long intimationKey;
	
	private Long pedId;
	
	private String referenceNo;
	
	private String treatementType;
	
	@NotNull(message = "Please Select Diagnosis")
	private SelectValue diagnosisName;
	
	@NotNull(message = "Please Select ICD Chapter")
	private SelectValue icdChapter;
	
	@NotNull(message = "Please Select ICD Block")
	private SelectValue icdBlock;
	
	@NotNull(message = "Please Select ICD Code")
	private SelectValue icdCode;
	
	//@NotNull(message = "Please Select Sublimit Applicable")
	private SelectValue sublimitApplicable;
	
	private SublimitFunObject sublimitName;
	
	private String sublimitNameValue;
	
	private String sublimitAmt;
	
	protected String recTypeFlag = "A";
	
	
	///@NotNull(message = "Please Select Consider For Payment")
	private SelectValue considerForPayment;
	
	private Integer sumInsuredRestrictionValue;
	
	private String considerForPaymentValue;
	
	private SelectValue sumInsuredRestriction;
	
	private Long policyKey;
	
	private Long preauthKey;
	
	private String diagnosis;
	
	private Long diagnosisId;
	
	private Long icdChapterKey;
	
	private Long icdBlockKey;
	
	private Long icdCodeKey;
	
	private String pedName;
	
	private String policyAgeing;
	
	private SelectValue pedExclusionImpactOnDiagnosis;
	
	private String exclusionDiagnosis;
	
	private String exclusionDetailsValue;
	
	private String coPayValue;
	
	private SelectValue exclusionDetails;
	
	private String diagnosisRemarks;
	
	private Double downsizedAmount;
	
	private Double approvedAmount;
	
	private Double netApprovedAmount;
	
	private Double diffAmount;
	
	private String approveRemarks;
	
	private String process;
	
	private String processFlag;
	
	private String considerForPaymentFlag;
	
	private Boolean enableOrDisable;
	
	private Button deleteButton;
	
    private String createdBy;
    
    private Date createDate;
    
    private String presenterString;
    
    private Boolean isSublimitValueChange = false;
    
    private SelectValue coPayTypeId;
    
    public DiagnosisDetailsOPTableDTO(){
    	
    }

    public DiagnosisDetailsOPTableDTO(String presenterString){

    	this.presenterString = presenterString;
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
	private String actions;
	
	public String getRecTypeFlag() {
		return recTypeFlag;
	}
	public void setRecTypeFlag(String recTypeFlag) {
		this.recTypeFlag = recTypeFlag;
	}
	private List<InsuredPedDetails> listOfPED;
	
	private Double oldApprovedAmount;
	
	private Double oldDiffAmount;
	
	private Double amountConsideredAmount;
	
	private Double minimumAmount;
	
	private Double copayPercentage;
	
	private Double copayAmount;
	
	private Double netAmount;
	
	private Double copay;
	
	private String icdChapterValue;
	
	private String icdCodeValue;
	
	private String icdBlockValue;
	
	private String sittingsInput;
	
	private Boolean isAmbChargeApplicable;
	
	private String isAmbChargeFlag;
	
	private Integer ambulanceCharge = 0;
	
	private Integer amtWithAmbulanceCharge = 0;
	
//	private List<PedDetailsTableDTO> pedList = new ArrayList<PedDetailsTableDTO>();
	
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
	private Double subLimitAmount;
	
	private Long stageId;
	
	private String stageName;
	
	private Long statusId;
	
	private String sublimitApplicableValue;
	
	private String sublimitApplicableFlag;
	
	private String sublimtNameId;
	
	private SelectValue sumInsuredRestrictionId;
	
	private String action;
	
	private String remarks;
	
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public String getPedName() {
		return pedName;
	}
	public void setPedName(String pedName) {
		this.pedName = pedName;
	}
	public String getPolicyAgeing() {
		return policyAgeing;
	}
	public void setPolicyAgeing(String policyAgeing) {
		this.policyAgeing = policyAgeing;
	}
	public SelectValue getPedExclusionImpactOnDiagnosis() {
		return pedExclusionImpactOnDiagnosis;
	}
	public void setPedExclusionImpactOnDiagnosis(
			SelectValue pedExclusionImpactOnDiagnosis) {
		this.pedExclusionImpactOnDiagnosis = pedExclusionImpactOnDiagnosis;
	}
	public SelectValue getExclusionDetails() {
		return exclusionDetails;
	}
	public void setExclusionDetails(SelectValue exclusionDetails) {
		this.exclusionDetails = exclusionDetails;
	}
	public String getDiagnosisRemarks() {
		return diagnosisRemarks;
	}
	public void setDiagnosisRemarks(String diagnosisRemarks) {
		this.diagnosisRemarks = diagnosisRemarks;
	}
	public Double getSubLimitAmount() {
		return subLimitAmount;
	}
	public void setSubLimitAmount(Double subLimitAmount) {
		this.subLimitAmount = subLimitAmount;
	}
	public Double getApprovedAmount() {
		return approvedAmount;
	}
	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}
	public Double getDownsizedAmount() {
		return downsizedAmount;
	}
	public void setDownsizedAmount(Double downsizedAmount) {
		this.downsizedAmount = downsizedAmount;
	}
	public Long getIntimationKey() {
		return intimationKey;
	}
	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}
	public Long getPolicyKey() {
		return policyKey;
	}
	public Long getIcdChapterKey() {
		return icdChapterKey;
	}
	public void setIcdChapterKey(Long icdChapterKey) {
		this.icdChapterKey = icdChapterKey;
	}
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public Long getIcdBlockKey() {
		return icdBlockKey;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public void setIcdBlockKey(Long icdBlockKey) {
		this.icdBlockKey = icdBlockKey;
	}
	public Long getIcdCodeKey() {
		return icdCodeKey;
	}
	public void setIcdCodeKey(Long icdCodeKey) {
		this.icdCodeKey = icdCodeKey;
	}
	public Long getStageId() {
		return stageId;
	}
	public void setStageId(Long stageId) {
		this.stageId = stageId;
	}
	public String getStageName() {
		return stageName;
	}
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}
	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}
	
	public String getSublimitApplicableFlag() {
		return sublimitApplicableFlag;
	}
	public void setSublimitApplicableFlag(String sublimitApplicableFlag) {
		this.sublimitApplicableFlag = sublimitApplicableFlag;
		this.sublimitApplicable = new SelectValue();
		this.sublimitApplicable.setId((this.sublimitApplicableFlag != null && this.sublimitApplicableFlag.equalsIgnoreCase("Y")) ? ReferenceTable.COMMONMASTER_YES : ReferenceTable.COMMONMASTER_NO);
	}
	public String getConsiderForPaymentFlag() {
		return considerForPaymentFlag;
	}
	public void setConsiderForPaymentFlag(String considerForPaymentFlag) {
		this.considerForPaymentFlag = considerForPaymentFlag;
		this.considerForPayment = new SelectValue();
		this.considerForPayment.setId((this.considerForPaymentFlag != null && this.considerForPaymentFlag.equalsIgnoreCase("Y")) ? ReferenceTable.COMMONMASTER_YES : ReferenceTable.COMMONMASTER_NO);
	}
	public String getApproveRemarks() {
		return approveRemarks;
	}
	public void setApproveRemarks(String approveRemarks) {
		this.approveRemarks = approveRemarks;
	}
	public String getProcess() {
		return process;
	}
	public void setProcess(String process) {
		this.process = process;
		this.processFlag = this.process.toLowerCase().contains("yes") ? "Y" : "N";
	}
	public String getProcessFlag() {
		return processFlag;
	}
	public void setProcessFlag(String processFlag) {
		this.processFlag = processFlag;
	}
	public Long getPreauthKey() {
		return preauthKey;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getSublimtNameId() {
		return sublimtNameId;
	}
	public void setSublimtNameId(String sublimtNameId) {
		this.sublimtNameId = sublimtNameId;
	}
	public Long getDiagnosisId() {
		return diagnosisId;
	}
	public SelectValue getDiagnosisName() {
		
		return diagnosisName;
	}
	public void setDiagnosisName(SelectValue diagnosisName) {
		this.diagnosisName = diagnosisName;
		if(this.diagnosisName != null) {
			this.diagnosis = this.diagnosisName.getValue();
		}
		
	}
	public SelectValue getIcdChapter() {
		if(this.presenterString != null && this.presenterString.equalsIgnoreCase("premedicalPreauth") && icdChapter == null) {
			icdChapter = new SelectValue();
		}
		return icdChapter;
	}
	public void setIcdChapter(SelectValue icdChapter) {
		this.icdChapter = icdChapter;
	}
	public SelectValue getIcdBlock() {
		if(this.presenterString != null && this.presenterString.equalsIgnoreCase("premedicalPreauth") && icdBlock == null) {
			icdBlock = new SelectValue();
		}
		return icdBlock;
	}
	public void setIcdBlock(SelectValue icdBlock) {
		this.icdBlock = icdBlock;
	}
	public SelectValue getIcdCode() {

		if(this.presenterString != null && this.presenterString.equalsIgnoreCase("premedicalPreauth") && icdCode == null) {
			icdCode = new SelectValue();
		}
		return icdCode;
	}
	public void setIcdCode(SelectValue icdCode) {
		if(icdCode != null){
			this.icdCode = icdCode;
		}
	}
	public SublimitFunObject getSublimitName() {
		return sublimitName;
	}
	public void setSublimitName(SublimitFunObject sublimitName) {
		this.sublimitName = sublimitName;
	}
	public String getSublimitAmt() {
		return sublimitAmt;
	}
	public void setSublimitAmt(String sublimitAmt) {
		this.sublimitAmt = sublimitAmt;
	}
	public SelectValue getSumInsuredRestriction() {
		return sumInsuredRestriction;
	}
	public void setSumInsuredRestriction(SelectValue sumInsuredRestriction) {
		this.sumInsuredRestriction = sumInsuredRestriction;
	}
	public void setDiagnosisId(Long diagnosisId) {
		this.diagnosisId = diagnosisId;
//		
	}
	public SelectValue getSublimitApplicable() {
		return sublimitApplicable;
	}
	public void setSublimitApplicable(SelectValue sublimitApplicable) {
		this.sublimitApplicable = sublimitApplicable;
		this.sublimitApplicableFlag = this.sublimitApplicable != null && null != this.sublimitApplicable.getValue() && this.sublimitApplicable.getValue().toString().toLowerCase().contains("yes") ? "Y" : "N";
	}
	public String getSublimitApplicableValue() {
		return sublimitApplicableValue;
	}
	public void setSublimitApplicableValue(String sublimitApplicableValue) {
		this.sublimitApplicableValue = sublimitApplicableValue;
		this.sublimitApplicableFlag = this.sublimitApplicableValue != null && this.sublimitApplicableValue.toLowerCase().contains("yes") ? "Y" : "N";
		
	}
	public SelectValue getConsiderForPayment() {
		return considerForPayment;
	}
	public void setConsiderForPayment(SelectValue considerForPayment) {
		this.considerForPayment = considerForPayment;
		this.considerForPaymentFlag = this.considerForPayment != null && null != this.considerForPayment.getValue() && this.considerForPayment.getValue().toString().toLowerCase().contains("yes") ? "Y" : "N";
	}
	public String getConsiderForPaymentValue() {
		return considerForPaymentValue;
	}
	public void setConsiderForPaymentValue(String considerForPaymentValue) {
		this.considerForPaymentValue = considerForPaymentValue;
		this.considerForPaymentFlag = this.considerForPaymentValue != null && this.considerForPaymentValue.toLowerCase().contains("yes") ? "Y" : "N";
	}
	public SelectValue getSumInsuredRestrictionId() {
		return sumInsuredRestrictionId;
	}
	public void setSumInsuredRestrictionId(SelectValue sumInsuredRestrictionId) {
		this.sumInsuredRestrictionId = sumInsuredRestrictionId;
	}
	public Long getPedId() {
		return pedId;
	}
	public void setPedId(Long pedId) {
		this.pedId = pedId;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getPedId() == null && getDummyKey() == null) {
            return obj == this;
        } else if(getPedId() != null && getDummyKey() != null){
            return ((getPedId().equals(((DiagnosisDetailsOPTableDTO) obj).getPedId())) && (getDummyKey().equals(((DiagnosisDetailsOPTableDTO) obj).getDummyKey())));  
        } else {
        	return getPedId().equals(((DiagnosisDetailsOPTableDTO) obj).getDummyKey());
        }
    }

    @Override
    public int hashCode() {
        if (dummyKey != null && pedId != null) {
            return pedId.hashCode() + dummyKey.hashCode();
        } else {
            return super.hashCode();
        }
    }
	public Boolean getEnableOrDisable() {
		return enableOrDisable;
	}
	public void setEnableOrDisable(Boolean enableOrDisable) {
		this.enableOrDisable = enableOrDisable;
	}
	public Button getDeleteButton() {
		return deleteButton;
	}
	public void setDeleteButton(Button deleteButton) {
		this.deleteButton = deleteButton;
	}
	public String getActions() {
		return actions;
	}
	public void setActions(String actions) {
		this.actions = actions;
	}
	public List<InsuredPedDetails> getListOfPED() {
		return listOfPED;
	}
	public void setListOfPED(List<InsuredPedDetails> listOfPED) {
		this.listOfPED = listOfPED;
	}
	public Integer getDummyKey() {
		return dummyKey;
	}
	public void setDummyKey(Integer dummyKey) {
		this.dummyKey = dummyKey;
	}
	/*public List<PedDetailsTableDTO> getPedList() {
		return pedList;
	}
	public void setPedList(List<PedDetailsTableDTO> pedList) {
		this.pedList = pedList;
	}*/
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
	public Double getCopay() {
		return copay;
	}
	public void setCopay(Double copay) {
		this.copay = copay;
	}
	public String getSublimitNameValue() {
		return sublimitNameValue;
	}
	public void setSublimitNameValue(String sublimitNameValue) {
		this.sublimitNameValue = sublimitNameValue;
	}
	public Integer getSumInsuredRestrictionValue() {
		return sumInsuredRestrictionValue;
	}
	public void setSumInsuredRestrictionValue(Integer sumInsuredRestrictionValue) {
		this.sumInsuredRestrictionValue = sumInsuredRestrictionValue;
	}
	public String getExclusionDiagnosis() {
		return exclusionDiagnosis;
	}
	public void setExclusionDiagnosis(String exclusionDiagnosis) {
		this.exclusionDiagnosis = exclusionDiagnosis;
	}
	public String getExclusionDetailsValue() {
		return exclusionDetailsValue;
	}
	public void setExclusionDetailsValue(String exclusionDetailsValue) {
		this.exclusionDetailsValue = exclusionDetailsValue;
	}
	public String getCoPayValue() {
		return coPayValue;
	}
	public void setCoPayValue(String coPayValue) {
		this.coPayValue = coPayValue;
	}
	public String getIcdChapterValue() {
		return icdChapterValue;
	}
	public void setIcdChapterValue(String icdChapterValue) {
		this.icdChapterValue = icdChapterValue;
	}
	public String getIcdCodeValue() {
		return icdCodeValue;
	}
	public void setIcdCodeValue(String icdCodeValue) {
		this.icdCodeValue = icdCodeValue;
	}
	public String getIcdBlockValue() {
		return icdBlockValue;
	}
	public void setIcdBlockValue(String icdBlockValue) {
		this.icdBlockValue = icdBlockValue;
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
	public String getPresenterString() {
		return presenterString;
	}

	public void setPresenterString(String presenterString) {
		this.presenterString = presenterString;
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
}
