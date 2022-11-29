package com.shaic.reimbursement.assigninvesigation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.domain.TmpInvestigation;
import com.shaic.domain.preauth.MasPrivateInvestigator;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.reimbursement.draftinvesigation.DraftTriggerPointsToFocusDetailsTableDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public class AssignInvestigatorDto  extends AbstractTableDTO{
	
	private int sNo;
	
	private Long key;
	
	private String investigationNo;
	
	private String requestingRole;
	
	private String requestroIdOrName;
	
	private Long allocationToId;
	
	private String allocationToValue;
	
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
	
//	private BeanItemContainer<SelectValue> investigatorNameContainer;
	
	private List<SelectValue> investigatorNameContainerList;
	
	private SelectValue investigatorNameListSelectValue;
	
	private String email;
	
	private String gender;
	
	private String investigatorCode;
	
	private String investigatorTelNo;
	
	private String investigatorMobileNo;
	
	private String claimBackgroundDetails;
	
	private String investigationTriggerPoints;
	
	private ClaimDto claimDto;
	
	//private HumanTask humanTask;
	
	private String userName;
	
	private String passWord;
	
	private RRCDTO rrcDTO;
	
	private Long rodKey;
	
	private Integer taskNumber;
	
	private Date dischargeDate;
	
	private String diagnosisName;
	
	private Boolean isCashless = false;
	
	private Boolean isPrivateInvAllow = false;
	
	private Date dateOfInvestigation;
	
	private String dateOfInvestigationValue;
    private	List<DraftTriggerPointsToFocusDetailsTableDto> investigatorTriggerPointsList;
	private Object dbOutArray ;
	
	private String investigationType;
	
	private String factsOfTheCase;
	
	private List<SelectValue> gradingCategoryList;
	

	private String investigatorName;
	
	private String gradingRemarks;

	private String investigationCompletionRemarks;

	private SelectValue gradingCategorySelectValue;
	
	private List<AssignInvestigatorDto> multipleInvestigatorList;
	
	private Long assignedKey;

	private String reassignComment;
	
	private Date reassignedDate;
	
	private String reassignedFrom;
	
	private boolean reassignAllowed;
	
	private NewIntimationDto newIntimationDto;
	
	private PreauthDTO preauthDTO;
	
	private String crmFlagged;
	
	private String crcFlaggedReason;
	
	private String crcFlaggedRemark;
	
	private boolean isAllowed;
	
	private Integer maxCount;
	
	private Long claimCount = 1l;
	
	private Boolean isPEDInitiated = false;
	
	private Map<String, String> suspiciousPopupMap = new HashMap<String, String>();
	 
	private Boolean isPopupMessageOpened = false;
  
    private String isSuspicious;
	 
	 private String clmPrcsInstruction;
	 
	 private Map<String, String> nonPreferredPopupMap = new HashMap<String, String>();
	 
	 private Boolean is64VBChequeStatusAlert = false; 
	 
	 private Map<String, String> popupMap = new HashMap<String, String>();
	 
	 private Boolean investigationReportReviewed;
	 private String investigationReviewRemarks;
	 private Integer rollNo;
	 
	 /*Below fields for R0861*/
	 private List<SelectValue> zonalList;
	 private SelectValue zoneSelectValue;
	 private List<SelectValue> coordinatorList;
	 private SelectValue coordinatorSelectValue;
	 private List<MasPrivateInvestigator> privateInvestigatorsList;
	 private String zoneName;
	 private String coordinatorName;
	 
	 private String uhidIpNo;

	 private String docFilePath;
	 
	 private String docType;
	 
	 private String docSource;
	 
	 private List<SelectValue> investigatorNameSelectValueList;
		
	 
		
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

	public String getDocSource() {
		return docSource;
	}

	public void setDocSource(String docSource) {
		this.docSource = docSource;
	}

		public String getUhidIpNo() {
			return uhidIpNo;
		}

		public void setUhidIpNo(String uhidIpNo) {
			this.uhidIpNo = uhidIpNo;
		}
	
	 //CR2019058 adding new date fields
	 private Date reqDraftedDate;
	 private Date initiatedDate;
	 private Date approvedDate;
	 
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
	
	public Object getDbOutArray() {
		return dbOutArray;
	}

	public void setDbOutArray(Object dbOutArray) {
		this.dbOutArray = dbOutArray;
	}

	private String factsOfCase;

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

	/*public HumanTask getHumanTask() {
		return humanTask;
	}

	public void setHumanTask(HumanTask humanTask) {
		this.humanTask = humanTask;
	}
*/
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
	
	public Date getDateOfInvestigation() {
		return dateOfInvestigation;
	}

	public void setDateOfInvestigation(Date dateOfInvestigation) {
		if(dateOfInvestigation !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(dateOfInvestigation);
			setDateOfInvestigationValue(dateformat);
			this.dateOfInvestigation = dateOfInvestigation;
		}		
	}

	public String getFactsOfCase() {
		return factsOfCase;
	}

	public void setFactsOfCase(String factsOfCase) {
		this.factsOfCase = factsOfCase;
	}
	
	public List<DraftTriggerPointsToFocusDetailsTableDto> getInvestigatorTriggerPointsList() {
		return investigatorTriggerPointsList;
	}

	public void setInvestigatorTriggerPointsList(
			List<DraftTriggerPointsToFocusDetailsTableDto> investigatorTriggerPointsList) {
		this.investigatorTriggerPointsList = investigatorTriggerPointsList;
	}

	public String getInvestigationType() {
		return investigationType;
	}

	public void setInvestigationType(String investigationType) {
		this.investigationType = investigationType;
	}

	public String getFactsOfTheCase() {
		return factsOfTheCase;
	}

	public void setFactsOfTheCase(String factsOfTheCase) {
		this.factsOfTheCase = factsOfTheCase;
	}

	public String getDateOfInvestigationValue() {
		return dateOfInvestigationValue;
	}

	public void setDateOfInvestigationValue(String dateOfInvestigationValue) {
		this.dateOfInvestigationValue = dateOfInvestigationValue;
	}

	public List<SelectValue> getGradingCategoryList() {
		return gradingCategoryList;
	}

	public void setGradingCategoryList(List<SelectValue> gradingCategoryList) {
		this.gradingCategoryList = gradingCategoryList;
	}


	public String getInvestigatorName() {
		return investigatorName;
	}

	public void setInvestigatorName(String investigatorName) {
		this.investigatorName = investigatorName;
	}

	public String getGradingRemarks() {
		return gradingRemarks;
	}

	public void setGradingRemarks(String gradingRemarks) {
		this.gradingRemarks = gradingRemarks;
	}

	public String getInvestigationCompletionRemarks() {
		return investigationCompletionRemarks;
	}

	public void setInvestigationCompletionRemarks(
			String investigationCompletionRemarks) {
		this.investigationCompletionRemarks = investigationCompletionRemarks;

	}

	public SelectValue getGradingCategorySelectValue() {
		return gradingCategorySelectValue;
	}

	public void setGradingCategorySelectValue(SelectValue gradingCategorySelectValue) {
		this.gradingCategorySelectValue = gradingCategorySelectValue;
	}

	public int getsNo() {
		return sNo;
	}

	public void setsNo(int sNo) {
		this.sNo = sNo;
	}

	public List<AssignInvestigatorDto> getMultipleInvestigatorList() {
		return multipleInvestigatorList;
	}

	public void setMultipleInvestigatorList(
			List<AssignInvestigatorDto> multipleInvestigatorList) {
		this.multipleInvestigatorList = multipleInvestigatorList;
	}

	public Long getAssignedKey() {
		return assignedKey;
	}

	public void setAssignedKey(Long assignedKey) {
		this.assignedKey = assignedKey;
	}

	public String getReassignComment() {
		return reassignComment;
	}

	public Date getReassignedDate() {
		return reassignedDate;
	}

	public String getReassignedFrom() {
		return reassignedFrom;
	}

	public void setReassignComment(String reassignComment) {
		this.reassignComment = reassignComment;
	}

	public void setReassignedDate(Date reassignedDate) {
		this.reassignedDate = reassignedDate;
	}

	public void setReassignedFrom(String reassignedFrom) {
		this.reassignedFrom = reassignedFrom;
	}

	public boolean isReassignAllowed() {
		return reassignAllowed;
	}

	public void setReassignAllowed(boolean reassignAllowed) {
		this.reassignAllowed = reassignAllowed;
	}

	public NewIntimationDto getNewIntimationDto() {
		return newIntimationDto;
	}

	public void setNewIntimationDto(NewIntimationDto newIntimationDto) {
		this.newIntimationDto = newIntimationDto;
	}

	public String getCrmFlagged() {
		return crmFlagged;
	}

	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
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

	public PreauthDTO getPreauthDTO() {
		return preauthDTO;
	}

	public void setPreauthDTO(PreauthDTO preauthDTO) {
		this.preauthDTO = preauthDTO;
	}

	public boolean isAllowed() {
		return isAllowed;
	}

	public void setIsAllowed(boolean isAllowed) {
		this.isAllowed = isAllowed;
	}

	public Integer getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
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

	public Map<String, String> getSuspiciousPopupMap() {
		return suspiciousPopupMap;
	}

	public void setSuspiciousPopupMap(Map<String, String> suspiciousPopupMap) {
		this.suspiciousPopupMap = suspiciousPopupMap;
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
	public Boolean getIsGeoSame() {
		return isGeoSame;
	}

	public void setIsGeoSame(Boolean isGeoSame) {
		this.isGeoSame = isGeoSame;
	}

	public Boolean getInvestigationReportReviewed() {
		return investigationReportReviewed;
	}

	public void setInvestigationReportReviewed(Boolean investigationReportReviewed) {
		this.investigationReportReviewed = investigationReportReviewed;
	}

	public String getInvestigationReviewRemarks() {
		return investigationReviewRemarks;
	}

	public void setInvestigationReviewRemarks(String investigationReviewRemarks) {
		this.investigationReviewRemarks = investigationReviewRemarks;
	}

	public Integer getRollNo() {
		return rollNo;
	}

	public void setRollNo(Integer rollNo) {
		this.rollNo = rollNo;
	}

	public List<SelectValue> getZonalList() {
		return zonalList;
	}

	public void setZonalList(List<SelectValue> zonalList) {
		this.zonalList = zonalList;
	}

	public SelectValue getZoneSelectValue() {
		return zoneSelectValue;
	}

	public void setZoneSelectValue(SelectValue zoneSelectValue) {
		this.zoneSelectValue = zoneSelectValue;
	}

	public List<SelectValue> getCoordinatorList() {
		return coordinatorList;
	}

	public void setCoordinatorList(List<SelectValue> coordinatorList) {
		this.coordinatorList = coordinatorList;
	}

	public SelectValue getCoordinatorSelectValue() {
		return coordinatorSelectValue;
	}

	public void setCoordinatorSelectValue(SelectValue coordinatorSelectValue) {
		this.coordinatorSelectValue = coordinatorSelectValue;
	}

	public List<MasPrivateInvestigator> getPrivateInvestigatorsList() {
		return privateInvestigatorsList;
	}

	public void setPrivateInvestigatorsList(List<MasPrivateInvestigator> privateInvestigatorsList) {
		this.privateInvestigatorsList = privateInvestigatorsList;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getCoordinatorName() {
		return coordinatorName;
	}

	public void setCoordinatorName(String coordinatorName) {
		this.coordinatorName = coordinatorName;
	}

	public List<SelectValue> getInvestigatorNameContainerList() {
		return investigatorNameContainerList;
	}

	public void setInvestigatorNameContainerList(
			List<SelectValue> investigatorNameContainerList) {
		this.investigatorNameContainerList = investigatorNameContainerList;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public List<SelectValue> getInvestigatorNameSelectValueList() {
		return investigatorNameSelectValueList;
	}

	public void setInvestigatorNameSelectValueList(
			List<SelectValue> investigatorNameSelectValueList) {
		this.investigatorNameSelectValueList = investigatorNameSelectValueList;
	}


	public Date getInitiatedDate() {
		return initiatedDate;
	}

	public void setInitiatedDate(Date initiatedDate) {
		this.initiatedDate = initiatedDate;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Date getReqDraftedDate() {
		return reqDraftedDate;
	}

	public void setReqDraftedDate(Date reqDraftedDate) {
		this.reqDraftedDate = reqDraftedDate;
	}

	public List<SelectValue> getReasonForInitiatingInvestIdList() {
		return reasonForInitiatingInvestIdList;
	}

	public void setReasonForInitiatingInvestIdList(
			List<SelectValue> reasonForInitiatingInvestIdList) {
		this.reasonForInitiatingInvestIdList = reasonForInitiatingInvestIdList;
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

	public String getReasonForIniInvestValue() {
		return reasonForIniInvestValue;
	}

	public void setReasonForIniInvestValue(String reasonForIniInvestValue) {
		this.reasonForIniInvestValue = reasonForIniInvestValue;
	}

	public Long getReasonForIniInvestId() {
		return reasonForIniInvestId;
	}

	public void setReasonForIniInvestId(Long reasonForIniInvestId) {
		this.reasonForIniInvestId = reasonForIniInvestId;
	}

	public Boolean getIsPrivateInvAllow() {
		return isPrivateInvAllow;
	}

	public void setIsPrivateInvAllow(Boolean isPrivateInvAllow) {
		this.isPrivateInvAllow = isPrivateInvAllow;
	}
	
	
	
	
}
