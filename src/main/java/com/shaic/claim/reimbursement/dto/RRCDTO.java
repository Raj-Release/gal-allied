/**
 * 
 */
package com.shaic.claim.reimbursement.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

/**
 * @author ntv.vijayar
 *
 */
public class RRCDTO {

	//For capturing employee details
	private String employeeName;

	private String employeeId;

	private String employeeZone;

	private String employeeDept;

	//For capturing policy details

	private String policyNo;

	private String intimationNo;

	private String productName;

	private String duration;

	//private String sumInsured;
	private Double sumInsured;

	//For capturing hospital details.
	private String hospitalName;

	private String hospitalCity;

	private String hospitalZone;

	private Date dateOfAdmission;

	private Date dateOfDischarge;

	//For insured details;
	private String insuredName;

	private Long insuredAge;

	private String sex;

	private String claimType;

	private String processingStage;

	private Long processingStageKey;

	private SelectValue significantClinicalInformation;

	private String significantClinicalInformationValue;

	/*@NotNull(message = "Please Enter Significant clinical information remarks")
	@Size(min = 1 , message = "Please Enter Significant clinical information remarks")*/
	private String remarks;

	/**
	 * RRC Request category Listener table and extra effort by employee
	 * table both use same DTO. Hence to store the values from these tables,
	 * two different list with same dto is used.
	 * */

	private List<ExtraEmployeeEffortDTO> employeeEffortList;

	//Added for storing RRC Request Category Listener table values.
	private List<ExtraEmployeeEffortDTO> rrcCategoryList;

	private QuantumReductionDetailsDTO quantumReductionDetailsDTO;
	private List<QuantumReductionDetailsDTO> quantumReductionDetailsDTOList;

	private Long statusKey;

	private Long stageKey;

	private Long requestorStageKey;

	private String rrcRequestType;

	private SelectValue eligibility;

	private String savedAmount;

	private String eligibilityRemarks;

	private String requestorID;


	@NotNull(message = "Please enter request on hold remarks")
	@Size(min = 1 , message = "Please enter request on hold remarks")
	private String requestOnHoldRemarks;

	private Long rrcRequestKey;

	private String rrcStatus;

	private Map<String, Object> dataSourcesMap;

	private NewIntimationDto newIntimationDTO;

	private ClaimDto claimDTO;

	private String strUserName;

	private String strPassword;

	private PolicyDto policyDto;

	private Long hospitalKey;
	private String claimNumber;
	private String reasonForAdmission;
	private Long intimationKey;
	private Long policyKey;
	private Long claimKey;

	private String rrcRequestNo;

	/**
	 * Added for review RRC request.
	 * */
	private String requestRRCSavedAmount;

	private String requestEligbilityRRCRemarks;

	private String requestRRCElgilibilityValue;

	private String followUp;

	//	private HumanTask humanTask; 

	private Long rodKey;

	private SelectValue rrcModifiedEligbility;

	private String rrcModifiedSavedAmount;

	private String rrcModifiedRemarks;

	private String reviewEligiblityValue;
	private SelectValue reviewEligiblity;



	private String createdBy;
	private Date createdDate;

	private String processedBy;
	private Date processedDate;

	private String reviewedBy;

	private Date reviewedDate;

	private Date rrcintiatedDate;

	private String rrcRequestProcessedDate;

	private Long requestedStageId;

	private List<SelectValue> employeeNameList;

	private Boolean isHoldRepeated;

	//private NewIntimationDto newIntimationDto;

	private ClaimDto claimDto;

	private PreauthDTO preauthDTO;

	private String crmFlagged;

	private String crcFlaggedReason;

	private String crcFlaggedRemark;



	private List<QuantumReductionDetailsDTO> quantumReductionListForClaimWiseRRCHistory;

	private String rrcModifiedEligiblityValue;

	private Long rrcRequestRRCEligiblityId;


	private Object dbOutArray ;

	private Boolean isRrcStatusScreen = false;
	private String rrcType;
	private ExtraEmployeeEffortDTO employeeEffortDTO;



	public Object getDbOutArray() {
		return dbOutArray;
	}

	public void setDbOutArray(Object dbOutArray) {
		this.dbOutArray = dbOutArray;
	}

	public String getRrcModifiedEligiblityValue() {
		return rrcModifiedEligiblityValue;
	}

	public void setRrcModifiedEligiblityValue(String rrcModifiedEligiblityValue) {
		this.rrcModifiedEligiblityValue = rrcModifiedEligiblityValue;
	}

	public List<QuantumReductionDetailsDTO> getQuantumReductionListForClaimWiseRRCHistory() {
		return quantumReductionListForClaimWiseRRCHistory;
	}

	public void setQuantumReductionListForClaimWiseRRCHistory(
			List<QuantumReductionDetailsDTO> quantumReductionListForClaimWiseRRCHistory) {
		this.quantumReductionListForClaimWiseRRCHistory = quantumReductionListForClaimWiseRRCHistory;
	}

	private Long preauthKey;

	public RRCDTO()
	{
		newIntimationDTO = new NewIntimationDto();
		claimDto = new ClaimDto();
		quantumReductionDetailsDTO = new QuantumReductionDetailsDTO();
		employeeEffortDTO = new ExtraEmployeeEffortDTO();
		//this.quantumReductionDetailsDTO = new QuantumReductionDetailsDTO();
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeZone() {
		return employeeZone;
	}

	public void setEmployeeZone(String employeeZone) {
		this.employeeZone = employeeZone;
	}

	public String getEmployeeDept() {
		return employeeDept;
	}

	public void setEmployeeDept(String employeeDept) {
		this.employeeDept = employeeDept;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	/*public String getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(String sumInsured) {
		this.sumInsured = sumInsured;
	}*/

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalCity() {
		return hospitalCity;
	}

	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}

	public String getHospitalZone() {
		return hospitalZone;
	}

	public void setHospitalZone(String hospitalZone) {
		this.hospitalZone = hospitalZone;
	}

	public Date getDateOfAdmission() {
		return dateOfAdmission;
	}

	public void setDateOfAdmission(Date dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}

	public Date getDateOfDischarge() {
		return dateOfDischarge;
	}

	public void setDateOfDischarge(Date dateOfDischarge) {
		this.dateOfDischarge = dateOfDischarge;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public Long getInsuredAge() {
		return insuredAge;
	}

	public void setInsuredAge(Long insuredAge) {
		this.insuredAge = insuredAge;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getProcessingStage() {
		return processingStage;
	}

	public void setProcessingStage(String processingStage) {
		this.processingStage = processingStage;
	}

	public SelectValue getSignificantClinicalInformation() {
		return significantClinicalInformation;
	}

	public void setSignificantClinicalInformation(
			SelectValue significantClinicalInformation) {
		this.significantClinicalInformation = significantClinicalInformation;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/*public List<RRCDTO> getEmployeeEffortList() {
		return employeeEffortList;
	}

	public void setEmployeeEffortList(List<RRCDTO> employeeEffortList) {
		this.employeeEffortList = employeeEffortList;
	}
	 *//*
	public QuantumReductionDetailsDTO getQuantumReductionDetailsDTO() {
		return quantumReductionDetailsDTO;
	}

	public void setQuantumReductionDetailsDTO(
			QuantumReductionDetailsDTO quantumReductionDetailsDTO) {
		this.quantumReductionDetailsDTO = quantumReductionDetailsDTO;
	}*/

	public Double getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(Double sumInsured) {
		this.sumInsured = sumInsured;
	}

	public List<ExtraEmployeeEffortDTO> getEmployeeEffortList() {
		return employeeEffortList;
	}

	public void setEmployeeEffortList(
			List<ExtraEmployeeEffortDTO> employeeEffortList) {
		this.employeeEffortList = employeeEffortList;
	}

	public List<QuantumReductionDetailsDTO> getQuantumReductionDetailsDTOList() {
		return quantumReductionDetailsDTOList;
	}

	public void setQuantumReductionDetailsDTOList(
			List<QuantumReductionDetailsDTO> quantumReductionDetailsDTOList) {
		this.quantumReductionDetailsDTOList = quantumReductionDetailsDTOList;
	}

	public QuantumReductionDetailsDTO getQuantumReductionDetailsDTO() {
		return quantumReductionDetailsDTO;
	}

	public void setQuantumReductionDetailsDTO(
			QuantumReductionDetailsDTO quantumReductionDetailsDTO) {
		this.quantumReductionDetailsDTO = quantumReductionDetailsDTO;
	}

	public Long getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}

	public Long getStageKey() {
		return stageKey;
	}

	public void setStageKey(Long stageKey) {
		this.stageKey = stageKey;
	}

	public Long getRequestorStageKey() {
		return requestorStageKey;
	}

	public void setRequestorStageKey(Long requestorStageKey) {
		this.requestorStageKey = requestorStageKey;
	}

	public String getRrcRequestType() {
		return rrcRequestType;
	}

	public void setRrcRequestType(String rrcRequestType) {
		this.rrcRequestType = rrcRequestType;
	}

	/*	public SelectValue getEligiblity() {
		return eligibility;
	}*/



	public void setEligibility(SelectValue eligibility) {
		this.eligibility = eligibility;
	}

	public String getEligibilityRemarks() {
		return eligibilityRemarks;
	}

	public void setEligibilityRemarks(String eligibilityRemarks) {
		this.eligibilityRemarks = eligibilityRemarks;
	}

	public String getRequestOnHoldRemarks() {
		return requestOnHoldRemarks;
	}

	public void setRequestOnHoldRemarks(String requestOnHoldRemarks) {
		this.requestOnHoldRemarks = requestOnHoldRemarks;
	}

	public Long getRrcRequestKey() {
		return rrcRequestKey;
	}

	public void setRrcRequestKey(Long rrcRequestKey) {
		this.rrcRequestKey = rrcRequestKey;
	}

	public String getSavedAmount() {
		return savedAmount;
	}

	public void setSavedAmount(String savedAmount) {
		this.savedAmount = savedAmount;
	}

	public List<ExtraEmployeeEffortDTO> getRrcCategoryList() {
		return rrcCategoryList;
	}

	public void setRrcCategoryList(List<ExtraEmployeeEffortDTO> rrcCategoryList) {
		this.rrcCategoryList = rrcCategoryList;
	}

	public String getSignificantClinicalInformationValue() {
		return significantClinicalInformationValue;
	}

	public void setSignificantClinicalInformationValue(
			String significantClinicalInformationValue) {
		this.significantClinicalInformationValue = significantClinicalInformationValue;
	}

	public String getRrcStatus() {
		return rrcStatus;
	}

	public void setRrcStatus(String rrcStatus) {
		this.rrcStatus = rrcStatus;
	}

	public Map<String, Object> getDataSourcesMap() {
		return dataSourcesMap;
	}

	public void setDataSourcesMap(Map<String, Object> dataSourcesMap) {
		this.dataSourcesMap = dataSourcesMap;
	}

	/*public NewIntimationDto getNewIntimationDTO() {
		return newIntimationDTO;
	}

	public void setNewIntimationDTO(NewIntimationDto newIntimationDTO) {
		this.newIntimationDTO = newIntimationDTO;
	}*/

	public ClaimDto getClaimDTO() {
		return claimDTO;
	}

	public void setClaimDTO(ClaimDto claimDTO) {
		this.claimDTO = claimDTO;
	}

	public String getStrUserName() {
		return strUserName;
	}

	public void setStrUserName(String strUserName) {
		this.strUserName = strUserName;
	}

	public String getStrPassword() {
		return strPassword;
	}

	public void setStrPassword(String strPassword) {
		this.strPassword = strPassword;
	}

	public PolicyDto getPolicyDto() {
		return policyDto;
	}

	public void setPolicyDto(PolicyDto policyDto) {
		this.policyDto = policyDto;
	}

	public Long getHospitalKey() {
		return hospitalKey;
	}

	public void setHospitalKey(Long hospitalKey) {
		this.hospitalKey = hospitalKey;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public String getReasonForAdmission() {
		return reasonForAdmission;
	}

	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
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

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public String getRrcRequestNo() {
		return rrcRequestNo;
	}

	public void setRrcRequestNo(String rrcRequestNo) {
		this.rrcRequestNo = rrcRequestNo;
	}




	public String getFollowUp() {
		return followUp;
	}

	public void setFollowUp(String followUp) {
		this.followUp = followUp;
	}

	public String getRequestRRCSavedAmount() {
		return requestRRCSavedAmount;
	}

	public void setRequestRRCSavedAmount(String requestRRCSavedAmount) {
		this.requestRRCSavedAmount = requestRRCSavedAmount;
	}

	public String getRequestEligbilityRRCRemarks() {
		return requestEligbilityRRCRemarks;
	}

	public void setRequestEligbilityRRCRemarks(String requestEligbilityRRCRemarks) {
		this.requestEligbilityRRCRemarks = requestEligbilityRRCRemarks;
	}

	public String getRequestRRCElgilibilityValue() {
		return requestRRCElgilibilityValue;
	}

	public void setRequestRRCElgilibilityValue(String requestRRCElgilibilityValue) {
		this.requestRRCElgilibilityValue = requestRRCElgilibilityValue;
	}


	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public SelectValue getRrcModifiedEligbility() {
		return rrcModifiedEligbility;
	}

	public void setRrcModifiedEligbility(SelectValue rrcModifiedEligbility) {
		this.rrcModifiedEligbility = rrcModifiedEligbility;
	}

	public String getRrcModifiedSavedAmount() {
		return rrcModifiedSavedAmount;
	}

	public void setRrcModifiedSavedAmount(String rrcModifiedSavedAmount) {
		this.rrcModifiedSavedAmount = rrcModifiedSavedAmount;
	}

	public String getRrcModifiedRemarks() {
		return rrcModifiedRemarks;
	}

	public void setRrcModifiedRemarks(String rrcModifiedRemarks) {
		this.rrcModifiedRemarks = rrcModifiedRemarks;
	}

	public String getReviewEligiblityValue() {
		return reviewEligiblityValue;
	}

	public void setReviewEligiblityValue(String reviewEligiblityValue) {
		this.reviewEligiblityValue = reviewEligiblityValue;
	}

	public SelectValue getReviewEligiblity() {
		return reviewEligiblity;
	}

	public void setReviewEligiblity(SelectValue reviewEligiblity) {
		this.reviewEligiblity = reviewEligiblity;
	}

	public SelectValue getEligibility() {
		return eligibility;
	}

	/*public HumanTask getRrcHumanTask() {
		return rrcHumanTask;
	}

	public void setRrcHumanTask(HumanTask rrcHumanTask) {
		this.rrcHumanTask = rrcHumanTask;
	}*/

	public String getProcessedBy() {
		return processedBy;
	}

	public void setProcessedBy(String processedBy) {
		this.processedBy = processedBy;
	}

	public Date getProcessedDate() {
		return processedDate;
	}

	public void setProcessedDate(Date processedDate) {
		this.processedDate = processedDate;
	}

	public String getReviewedBy() {
		return reviewedBy;
	}

	public void setReviewedBy(String reviewedBy) {
		this.reviewedBy = reviewedBy;
	}

	public Date getReviewedDate() {
		return reviewedDate;
	}

	public void setReviewedDate(Date reviewedDate) {
		this.reviewedDate = reviewedDate;
	}


	public Date getRrcintiatedDate() {
		return rrcintiatedDate;
	}

	public void setRrcintiatedDate(Date rrcintiatedDate) {
		this.rrcintiatedDate = rrcintiatedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getRrcRequestProcessedDate() {
		return rrcRequestProcessedDate;
	}

	public void setRrcRequestProcessedDate(String rrcRequestProcessedDate) {
		this.rrcRequestProcessedDate = rrcRequestProcessedDate;
	}

	public Long getRequestedStageId() {
		return requestedStageId;
	}

	public void setRequestedStageId(Long requestedStageId) {
		this.requestedStageId = requestedStageId;
	}

	public List<SelectValue> getEmployeeNameList() {
		return employeeNameList;
	}

	public void setEmployeeNameList(List<SelectValue> employeeNameList) {
		this.employeeNameList = employeeNameList;
	}

	public Boolean getIsHoldRepeated() {
		return isHoldRepeated;
	}

	public void setIsHoldRepeated(Boolean isHoldRepeated) {
		this.isHoldRepeated = isHoldRepeated;
	}

	/*	public NewIntimationDto getNewIntimationDto() {
		return newIntimationDto;
	}

	public void setNewIntimationDto(NewIntimationDto newIntimationDto) {
		this.newIntimationDto = newIntimationDto;
	}*/

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
	public NewIntimationDto getNewIntimationDTO() {
		return newIntimationDTO;
	}

	public void setNewIntimationDTO(NewIntimationDto newIntimationDTO) {
		this.newIntimationDTO = newIntimationDTO;
	}

	public Long getPreauthKey() {
		return preauthKey;
	}

	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}

	public Long getRrcRequestRRCEligiblityId() {
		return rrcRequestRRCEligiblityId;
	}

	public void setRrcRequestRRCEligiblityId(Long rrcRequestRRCEligiblityId) {
		this.rrcRequestRRCEligiblityId = rrcRequestRRCEligiblityId;
	}

	public Long getProcessingStageKey() {
		return processingStageKey;
	}

	public void setProcessingStageKey(Long processingStageKey) {
		this.processingStageKey = processingStageKey;
	}

	public String getRequestorID() {
		return requestorID;
	}

	public void setRequestorID(String requestorID) {
		this.requestorID = requestorID;
	}

	public PreauthDTO getPreauthDTO() {
		return preauthDTO;
	}

	public void setPreauthDTO(PreauthDTO preauthDTO) {
		this.preauthDTO = preauthDTO;
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

	public Boolean getIsRrcStatusScreen() {
		return isRrcStatusScreen;
	}

	public void setIsRrcStatusScreen(Boolean isRrcStatusScreen) {
		this.isRrcStatusScreen = isRrcStatusScreen;
	}

	public String getRrcType() {
		return rrcType;
	}

	public void setRrcType(String rrcType) {
		this.rrcType = rrcType;
	}

	public ExtraEmployeeEffortDTO getEmployeeEffortDTO() {
		return employeeEffortDTO;
	}

	public void setEmployeeEffortDTO(ExtraEmployeeEffortDTO employeeEffortDTO) {
		this.employeeEffortDTO = employeeEffortDTO;
	}


}
