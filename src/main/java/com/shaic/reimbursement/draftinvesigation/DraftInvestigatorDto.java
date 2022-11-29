package com.shaic.reimbursement.draftinvesigation;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.domain.TmpInvestigation;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
//import com.shaic.ims.bpm.claim.modelv2.HumanTask;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorDto;

public class DraftInvestigatorDto {
	
	private Long key;
	
	private String investigationNo;
	
	private String requestingRole;
	
	private String requestroIdOrName;
	
	private Long allocationToId;
	
	private String allocationToValue;
	
	private Long reasonForInitiatingInvestId;
	
	private String reasonForInitiatingInvestValue;
	
	private String allocationToValueForState;
	
	private String investiationApprovedRemarks;
	
	private String reasonForRefering;
	
	private String triggerPointsForFocus;
	
	private List<SelectValue> stateList;
	
	private SelectValue stateSelectValue;
	
	private List<SelectValue> cityList;
	
	private SelectValue citySelectValue;
	
	private List<SelectValue> allocationToIdList;
	
	private List<SelectValue> allocationToSelectValueList;
	
	private SelectValue allocationToSelectValue;
	
	private Long reasonForIniInvestId;
	
	private String reasonForIniInvestValue;
	
	private List<SelectValue> reasonForInitiatingInvestIdList;
	
	private List<SelectValue> reasonForInitiatingInvestSelectValueList;
	
	private SelectValue reasonForInitiatingInvestSelectValue;
	
	private List<TmpInvestigation> investigatorNameList;
	
	//private BeanItemContainer<SelectValue> investigatorNameContainer;
	
	private SelectValue investigatorNameListSelectValue;
	
	private String investigatorCode;
	
	private String investigatorTelNo;
	
	private String investigatorMobileNo;
	
	private String claimBackgroundDetails;
	
	private String investigationTriggerPoints;
	
	private ClaimDto claimDto;
	
	
	private String userName;
	
	private String passWord;
	
	private RRCDTO rrcDTO;
	
	private Long rodKey;
	
	private Integer taskNumber;
	
	private Date dischargeDate;
	
	private String diagnosisName;
	
	private Boolean isCashless = false;
	
	private List<DraftTriggerPointsToFocusDetailsTableDto> triggerPointsList;
	
	private List<DraftTriggerPointsToFocusDetailsTableDto> deletedTriggerPointsList;
	
	private String reimbReqBy;
	
	private Object dbOutArray;
	
	private String docFilePath;
	
	private String docType;
	
	private String docSource;
	
	private String crmFlagged;
	
	private NewIntimationDto newIntimationDto;
	
	private PreauthDTO preauthDTO;
	
	private String crcFlaggedReason;
	
	private String crcFlaggedRemark;
	
	private Long claimCount = 1l;
	
	private Boolean isPEDInitiated = false;
	
	private Boolean isPopupMessageOpened = false;
	   
    private String isSuspicious;
	 
	private String clmPrcsInstruction;
	
	private Map<String, String> suspiciousPopupMap = new HashMap<String, String>();
	
	private Map<String, String> nonPreferredPopupMap = new HashMap<String, String>();
	
	private Boolean is64VBChequeStatusAlert = false; 
	
	private Map<String, String> popupMap = new HashMap<String, String>();
	
	private String uhidIpNo;

	
	
	public String getUhidIpNo() {
		return uhidIpNo;
	}

	public void setUhidIpNo(String uhidIpNo) {
		this.uhidIpNo = uhidIpNo;
	}

	public Map<String, String> getPopupMap() {
		return popupMap;
	}

	public void setPopupMap(Map<String, String> popupMap) {
		this.popupMap = popupMap;
	}

	public Boolean getIs64VBChequeStatusAlert() {
		return is64VBChequeStatusAlert;
	}

	public void setIs64VBChequeStatusAlert(Boolean is64vbChequeStatusAlert) {
		is64VBChequeStatusAlert = is64vbChequeStatusAlert;
	}

	public Map<String, String> getNonPreferredPopupMap() {
		return nonPreferredPopupMap;
	}

	public void setNonPreferredPopupMap(Map<String, String> nonPreferredPopupMap) {
		this.nonPreferredPopupMap = nonPreferredPopupMap;
	}
	private Boolean isGeoSame = Boolean.TRUE;

	public DraftInvestigatorDto(){
		
	}
	
	public DraftInvestigatorDto(AssignInvestigatorDto dto) {
		
		this.claimDto = dto.getClaimDto();
		this.triggerPointsList = dto.getInvestigatorTriggerPointsList();
		this.claimBackgroundDetails = dto.getClaimBackgroundDetails();
		this.investigationTriggerPoints = dto.getFactsOfCase();
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getInvestigationNo() {
		return investigationNo;
	}

	public void setInvestigationNo(String investigationNo) {
		this.investigationNo = investigationNo;
	}

	public String getRequestingRole() {
		return requestingRole;
	}

	public void setRequestingRole(String requestingRole) {
		this.requestingRole = requestingRole;
	}

	public String getRequestroIdOrName() {
		return requestroIdOrName;
	}

	public void setRequestroIdOrName(String requestroIdOrName) {
		this.requestroIdOrName = requestroIdOrName;
	}

	public Long getAllocationToId() {
		return allocationToId;
	}

	public void setAllocationToId(Long allocationToId) {
		this.allocationToId = allocationToId;
	}

	public String getAllocationToValue() {
		return allocationToValue;
	}

	public void setAllocationToValue(String allocationToValue) {
		this.allocationToValue = allocationToValue;
	}

	public String getAllocationToValueForState() {
		return allocationToValueForState;
	}

	public void setAllocationToValueForState(String allocationToValueForState) {
		this.allocationToValueForState = allocationToValueForState;
	}

	public String getInvestiationApprovedRemarks() {
		return investiationApprovedRemarks;
	}

	public void setInvestiationApprovedRemarks(String investiationApprovedRemarks) {
		this.investiationApprovedRemarks = investiationApprovedRemarks;
	}

	public String getReasonForRefering() {
		return reasonForRefering;
	}

	public void setReasonForRefering(String reasonForRefering) {
		this.reasonForRefering = reasonForRefering;
	}

	public String getTriggerPointsForFocus() {
		return triggerPointsForFocus;
	}

	public void setTriggerPointsForFocus(String triggerPointsForFocus) {
		this.triggerPointsForFocus = triggerPointsForFocus;
	}

	public List<SelectValue> getStateList() {
		return stateList;
	}

	public void setStateList(List<SelectValue> stateList) {
		this.stateList = stateList;
	}

	public SelectValue getStateSelectValue() {
		return stateSelectValue;
	}

	public void setStateSelectValue(SelectValue stateSelectValue) {
		this.stateSelectValue = stateSelectValue;
	}

	public List<SelectValue> getCityList() {
		return cityList;
	}

	public void setCityList(List<SelectValue> cityList) {
		this.cityList = cityList;
	}

	public SelectValue getCitySelectValue() {
		return citySelectValue;
	}

	public void setCitySelectValue(SelectValue citySelectValue) {
		this.citySelectValue = citySelectValue;
	}

	public List<SelectValue> getAllocationToIdList() {
		return allocationToIdList;
	}

	public void setAllocationToIdList(List<SelectValue> allocationToIdList) {
		this.allocationToIdList = allocationToIdList;
	}

	public SelectValue getAllocationToSelectValue() {
		return allocationToSelectValue;
	}

	public void setAllocationToSelectValue(SelectValue allocationToSelectValue) {
		this.allocationToSelectValue = allocationToSelectValue;
	}

	public List<TmpInvestigation> getInvestigatorNameList() {
		return investigatorNameList;
	}

	public void setInvestigatorNameList(List<TmpInvestigation> investigatorNameList) {
		this.investigatorNameList = investigatorNameList;
	}

	public SelectValue getInvestigatorNameListSelectValue() {
		return investigatorNameListSelectValue;
	}

	public void setInvestigatorNameListSelectValue(
			SelectValue investigatorNameListSelectValue) {
		this.investigatorNameListSelectValue = investigatorNameListSelectValue;
	}

	public String getInvestigatorCode() {
		return investigatorCode;
	}

	public void setInvestigatorCode(String investigatorCode) {
		this.investigatorCode = investigatorCode;
	}

	public String getInvestigatorTelNo() {
		return investigatorTelNo;
	}

	public void setInvestigatorTelNo(String investigatorTelNo) {
		this.investigatorTelNo = investigatorTelNo;
	}

	public String getInvestigatorMobileNo() {
		return investigatorMobileNo;
	}

	public void setInvestigatorMobileNo(String investigatorMobileNo) {
		this.investigatorMobileNo = investigatorMobileNo;
	}

	public String getClaimBackgroundDetails() {
		return claimBackgroundDetails;
	}

	public void setClaimBackgroundDetails(String claimBackgroundDetails) {
		this.claimBackgroundDetails = claimBackgroundDetails;
	}

	public String getInvestigationTriggerPoints() {
		return investigationTriggerPoints;
	}

	public void setInvestigationTriggerPoints(String investigationTriggerPoints) {
		this.investigationTriggerPoints = investigationTriggerPoints;
	}

	public ClaimDto getClaimDto() {
		return claimDto;
	}

	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}

	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public RRCDTO getRrcDTO() {
		return rrcDTO;
	}

	public void setRrcDTO(RRCDTO rrcDTO) {
		this.rrcDTO = rrcDTO;
	}

	
	public List<SelectValue> getAllocationToSelectValueList() {
		return allocationToSelectValueList;
	}

	public void setAllocationToSelectValueList(
			List<SelectValue> allocationToSelectValueList) {
		this.allocationToSelectValueList = allocationToSelectValueList;
	}

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public Boolean getIsCashless() {
		return isCashless;
	}

	public void setIsCashless(Boolean isCashless) {
		this.isCashless = isCashless;
	}

	public Integer getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(Integer taskNumber) {
		this.taskNumber = taskNumber;
	}

	public Date getDischargeDate() {
		return dischargeDate;
	}

	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	public String getDiagnosisName() {
		return diagnosisName;
	}

	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}

	/*public BeanItemContainer<SelectValue> getInvestigatorNameContainer() {
		return investigatorNameContainer;
	}

	public void setInvestigatorNameContainer(
			BeanItemContainer<SelectValue> investigatorNameContainer) {
		this.investigatorNameContainer = investigatorNameContainer;
	}*/

	public List<DraftTriggerPointsToFocusDetailsTableDto> getTriggerPointsList() {
		return triggerPointsList;
	}

	public void setTriggerPointsList(
			List<DraftTriggerPointsToFocusDetailsTableDto> triggerPointsList) {
		this.triggerPointsList = triggerPointsList;
	}

	public List<DraftTriggerPointsToFocusDetailsTableDto> getDeletedTriggerPointsList() {
		return deletedTriggerPointsList;
	}

	public void setDeletedTriggerPointsList(
			List<DraftTriggerPointsToFocusDetailsTableDto> deletedTriggerPointsList) {
		this.deletedTriggerPointsList = deletedTriggerPointsList;
	}

	public String getReimbReqBy() {
		return reimbReqBy;
	}

	public void setReimbReqBy(String reimbReqBy) {
		this.reimbReqBy = reimbReqBy;
	}

	public Object getDbOutArray() {
		return dbOutArray;
	}

	public void setDbOutArray(Object dbOutArray) {
		this.dbOutArray = dbOutArray;
	}


	public String getDocFilePath() {
		return docFilePath;
	}

	public void setDocFilePath(String docFilePath) {
		this.docFilePath = docFilePath;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getCrmFlagged() {
		return crmFlagged;
	}

	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
	}

	public NewIntimationDto getNewIntimationDto() {
		return newIntimationDto;
	}

	public void setNewIntimationDto(NewIntimationDto newIntimationDto) {
		this.newIntimationDto = newIntimationDto;
	}

	public PreauthDTO getPreauthDTO() {
		return preauthDTO;
	}

	public void setPreauthDTO(PreauthDTO preauthDTO) {
		this.preauthDTO = preauthDTO;
	}

	public String getCrcFlaggedReason() {
		return crcFlaggedReason;
	}

	public void setCrcFlaggedReason(String crcFlaggedReason) {
		this.crcFlaggedReason = crcFlaggedReason;
	}

	public String getCrcFlaggedRemark() {
		return crcFlaggedRemark;
	}

	public void setCrcFlaggedRemark(String crcFlaggedRemark) {
		this.crcFlaggedRemark = crcFlaggedRemark;
	}

	public Long getClaimCount() {
		return claimCount;
	}

	public void setClaimCount(Long claimCount) {
		this.claimCount = claimCount;
	}

	public Boolean getIsPEDInitiated() {
		return isPEDInitiated;
	}

	public void setIsPEDInitiated(Boolean isPEDInitiated) {
		this.isPEDInitiated = isPEDInitiated;
	}

	public Boolean getIsPopupMessageOpened() {
		return isPopupMessageOpened;
	}

	public void setIsPopupMessageOpened(Boolean isPopupMessageOpened) {
		this.isPopupMessageOpened = isPopupMessageOpened;
	}

	public String getIsSuspicious() {
		return isSuspicious;
	}

	public void setIsSuspicious(String isSuspicious) {
		this.isSuspicious = isSuspicious;
	}

	public String getClmPrcsInstruction() {
		return clmPrcsInstruction;
	}

	public void setClmPrcsInstruction(String clmPrcsInstruction) {
		this.clmPrcsInstruction = clmPrcsInstruction;
	}

	public Map<String, String> getSuspiciousPopupMap() {
		return suspiciousPopupMap;
	}

	public void setSuspiciousPopupMap(Map<String, String> suspiciousPopupMap) {
		this.suspiciousPopupMap = suspiciousPopupMap;
	}
	public Boolean getIsGeoSame() {
		return isGeoSame;
	}

	public void setIsGeoSame(Boolean isGeoSame) {
		this.isGeoSame = isGeoSame;
	}
	
	public String getDocSource() {
		return docSource;
	}

	public void setDocSource(String docSource) {
		this.docSource = docSource;
	}

	public Long getReasonForInitiatingInvestId() {
		return reasonForInitiatingInvestId;
	}

	public void setReasonForInitiatingInvestId(Long reasonForInitiatingInvestId) {
		this.reasonForInitiatingInvestId = reasonForInitiatingInvestId;
	}

	public String getReasonForInitiatingInvestValue() {
		return reasonForInitiatingInvestValue;
	}

	public void setReasonForInitiatingInvestValue(
			String reasonForInitiatingInvestValue) {
		this.reasonForInitiatingInvestValue = reasonForInitiatingInvestValue;
	}

	public List<SelectValue> getReasonForInitiatingInvestSelectValueList() {
		return reasonForInitiatingInvestSelectValueList;
	}

	public void setReasonForInitiatingInvestSelectValueList(
			List<SelectValue> reasonForInitiatingInvestSelectValueList) {
		this.reasonForInitiatingInvestSelectValueList = reasonForInitiatingInvestSelectValueList;
	}

	public SelectValue getReasonForInitiatingInvestSelectValue() {
		return reasonForInitiatingInvestSelectValue;
	}

	public void setReasonForInitiatingInvestSelectValue(
			SelectValue reasonForInitiatingInvestSelectValue) {
		this.reasonForInitiatingInvestSelectValue = reasonForInitiatingInvestSelectValue;
	}

	public List<SelectValue> getReasonForInitiatingInvestIdList() {
		return reasonForInitiatingInvestIdList;
	}

	public void setReasonForInitiatingInvestIdList(
			List<SelectValue> reasonForInitiatingInvestIdList) {
		this.reasonForInitiatingInvestIdList = reasonForInitiatingInvestIdList;
	}

	public Long getReasonForIniInvestId() {
		return reasonForIniInvestId;
	}

	public void setReasonForIniInvestId(Long reasonForIniInvestId) {
		this.reasonForIniInvestId = reasonForIniInvestId;
	}

	public String getReasonForIniInvestValue() {
		return reasonForIniInvestValue;
	}

	public void setReasonForIniInvestValue(String reasonForIniInvestValue) {
		this.reasonForIniInvestValue = reasonForIniInvestValue;
	}
	
	
	
}
