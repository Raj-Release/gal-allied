
package com.shaic.claim.reimbursement.rrc.services;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;


public class SearchRRCStatusTableDTO extends AbstractTableDTO  implements Serializable{
		
	private String intimationNo;
	
	private String rrcRequestNo;
	
	private Date dateOfRequest;
	//private String dateOfRequest;
	
	private String dateOfRequestForTable;
	
	private String requestorId;
	
	private String requestorName;
	
	private String rrcRequestType;
	
	private Long rodKey;
	private Long claimKey;
	
	private SelectValue eligibility;
	
	private String eligibilityValue;

	private String cpuCode;
	
	private String cpuName;
	
	//Added for report generation.
	private String cpuDivString;
	private String productName;
	private String claimType;
	private String diag;
	private String management;
	private String patientName;
	private String hospitalName;
	private Long hospitalId;
	private String amountClaimed;
	private String settledAmount;
	private String amountSaved;
	private String categoryValue;
	private String status;
//	private String statusDate;
	private String statusDate;
	private String remarks;
	private String creditsEmployeeName;
	private String creditsEmployeeName2;
	private String creditsEmployeeName3;
	private String creditsEmployeeName4;
	private String creditsEmployeeName5;
	private String creditsEmployeeName6;
	private String creditsEmployeeName7;
	private String lapseEmployeeName;
	private String lapseEmployeeName2;
	private String lapseEmployeeName3;
	private String lapseEmployeeName4;
	private String lapseEmployeeName5;
	private String lapseEmployeeName6;
	private String lapseEmployeeNmae7;
	private Date rrcProcessedDate;
	private Long requestorSavedAmount;
	private Boolean isRrcStatusScreen = false;
	
	private List<SearchRRCStatusTableDTO> totalList;
	public Long getRequestorSavedAmount() {
		return requestorSavedAmount;
	}

	public void setRequestorSavedAmount(Long requestorSavedAmount) {
		this.requestorSavedAmount = requestorSavedAmount;
	}

	public Long getReviewerSavedAmount() {
		return reviewerSavedAmount;
	}

	public void setReviewerSavedAmount(Long reviewerSavedAmount) {
		this.reviewerSavedAmount = reviewerSavedAmount;
	}

	public Long getModifierSavedAmout() {
		return modifierSavedAmout;
	}

	public void setModifierSavedAmout(Long modifierSavedAmout) {
		this.modifierSavedAmout = modifierSavedAmout;
	}

	private Long reviewerSavedAmount;
	private Long modifierSavedAmout;
	
	private String rrcType;
	
	public String getRrcType() {
		return rrcType;
	}

	public void setRrcType(String rrcType) {
		this.rrcType = rrcType;
	}

	public Date getRrcProcessedDate() {
		return rrcProcessedDate;
	}

	public void setRrcProcessedDate(Date rrcProcessedDate) {
		this.rrcProcessedDate = rrcProcessedDate;
	}

	public Date getRrcReviewedDate() {
		return rrcReviewedDate;
	}

	public void setRrcReviewedDate(Date rrcReviewedDate) {
		this.rrcReviewedDate = rrcReviewedDate;
	}

	public Date getRrcModifedDate() {
		return rrcModifedDate;
	}

	public void setRrcModifedDate(Date rrcModifedDate) {
		this.rrcModifedDate = rrcModifedDate;
	}

	private Date rrcReviewedDate;
	
	private Date rrcModifedDate;
	
	
public String getCreditsEmployeeName7() {
		return creditsEmployeeName7;
	}

	public void setCreditsEmployeeName7(String creditsEmployeeName7) {
		this.creditsEmployeeName7 = creditsEmployeeName7;
	}

	public String getLapseEmployeeNmae7() {
		return lapseEmployeeNmae7;
	}

	public void setLapseEmployeeNmae7(String lapseEmployeeNmae7) {
		this.lapseEmployeeNmae7 = lapseEmployeeNmae7;
	}

	//	private String rrcStatus;
	private String rrcStatusId;
	
	private String checkBoxStatus;
	
	//private String excelCheck; 
	
	private Double initialSumInsured;
	private Long presentSumInsured;
	private String productCode;
	private String pedDisclosed;
	private String hospitalType;
	private String initialPEDRecommendation;
	private String pedSuggestions;
	private String pedName;
	private String healthCardNo;
	
	private Long pedCode;
	private Long insuredKey;
	private Long hospitalTypeId;
	private Long pedInitiateKey;
	private String initiateRRCRemarks;
	private String processRRCRemarks;
	private String[] creditEmpList;
	private String[] lapseEmpList;
	
	
	
	private Long statusKey;
	
	

	public Long getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}

	public String[] getCreditEmpList() {
		return creditEmpList;
	}

	public void setCreditEmpList(String[] creditEmpList) {
		this.creditEmpList = creditEmpList;
	}

	public String[] getLapseEmpList() {
		return lapseEmpList;
	}

	public void setLapseEmpList(String[] lapseEmpList) {
		this.lapseEmpList = lapseEmpList;
	}

	public String getCreditsEmployeeName2() {
		return creditsEmployeeName2;
	}

	public void setCreditsEmployeeName2(String creditsEmployeeName2) {
		this.creditsEmployeeName2 = creditsEmployeeName2;
	}

	public String getCreditsEmployeeName3() {
		return creditsEmployeeName3;
	}

	public void setCreditsEmployeeName3(String creditsEmployeeName3) {
		this.creditsEmployeeName3 = creditsEmployeeName3;
	}

	public String getCreditsEmployeeName4() {
		return creditsEmployeeName4;
	}

	public void setCreditsEmployeeName4(String creditsEmployeeName4) {
		this.creditsEmployeeName4 = creditsEmployeeName4;
	}

	public String getCreditsEmployeeName5() {
		return creditsEmployeeName5;
	}

	public void setCreditsEmployeeName5(String creditsEmployeeName5) {
		this.creditsEmployeeName5 = creditsEmployeeName5;
	}

	public String getCreditsEmployeeName6() {
		return creditsEmployeeName6;
	}

	public void setCreditsEmployeeName6(String creditsEmployeeName6) {
		this.creditsEmployeeName6 = creditsEmployeeName6;
	}

	public String getLapseEmployeeName2() {
		return lapseEmployeeName2;
	}

	public void setLapseEmployeeName2(String lapseEmployeeName2) {
		this.lapseEmployeeName2 = lapseEmployeeName2;
	}

	public String getLapseEmployeeName3() {
		return lapseEmployeeName3;
	}

	public void setLapseEmployeeName3(String lapseEmployeeName3) {
		this.lapseEmployeeName3 = lapseEmployeeName3;
	}

	public String getLapseEmployeeName4() {
		return lapseEmployeeName4;
	}

	public void setLapseEmployeeName4(String lapseEmployeeName4) {
		this.lapseEmployeeName4 = lapseEmployeeName4;
	}

	public String getLapseEmployeeName5() {
		return lapseEmployeeName5;
	}

	public void setLapseEmployeeName5(String lapseEmployeeName5) {
		this.lapseEmployeeName5 = lapseEmployeeName5;
	}

	public String getLapseEmployeeName6() {
		return lapseEmployeeName6;
	}

	public void setLapseEmployeeName6(String lapseEmployeeName6) {
		this.lapseEmployeeName6 = lapseEmployeeName6;
	}

	public String getInitiateRRCRemarks() {
		return initiateRRCRemarks;
	}

	public void setInitiateRRCRemarks(String initiateRRCRemarks) {
		this.initiateRRCRemarks = initiateRRCRemarks;
	}

	public String getProcessRRCRemarks() {
		return processRRCRemarks;
	}

	public void setProcessRRCRemarks(String processRRCRemarks) {
		this.processRRCRemarks = processRRCRemarks;
	}

	public Long getPedInitiateKey() {
		return pedInitiateKey;
	}

	public void setPedInitiateKey(Long pedInitiateKey) {
		this.pedInitiateKey = pedInitiateKey;
	}

	public Long getHospitalTypeId() {
		return hospitalTypeId;
	}

	public void setHospitalTypeId(Long hospitalTypeId) {
		this.hospitalTypeId = hospitalTypeId;
	}

	public Long getInsuredKey() {
		return insuredKey;
	}

	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
	}



	public Long getPedCode() {
		return pedCode;
	}

	public void setPedCode(Long pedCode) {
		this.pedCode = pedCode;
	}

	public String getHealthCardNo() {
		return healthCardNo;
	}

	public void setHealthCardNo(String healthCardNo) {
		this.healthCardNo = healthCardNo;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getRrcRequestNo() {
		return rrcRequestNo;
	}

	public void setRrcRequestNo(String rrcRequestNo) {
		this.rrcRequestNo = rrcRequestNo;
	}

	public Date getDateOfRequest() {
		return dateOfRequest;
	}

	public void setDateOfRequest(Date dateOfRequest) {
		//this.dateOfRequest = dateOfRequest;
		if(dateOfRequest !=  null){
			String dateformate =new SimpleDateFormat("dd/MM/yyyy").format(dateOfRequest);
			setDateOfRequestForTable(dateformate);
			this.dateOfRequest = dateOfRequest;
			}
	}

	public String getRequestorId() {
		return requestorId;
	}

	public void setRequestorId(String requestorId) {
		this.requestorId = requestorId;
	}

	public String getRequestorName() {
		return requestorName;
	}

	public void setRequestorName(String requestorName) {
		this.requestorName = requestorName;
	}

	public String getRrcRequestType() {
		return rrcRequestType;
	}

	public void setRrcRequestType(String rrcRequestType) {
		this.rrcRequestType = rrcRequestType;
	}

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public SelectValue getEligibility() {
		return eligibility;
	}

	public void setEligibility(SelectValue eligibility) {
		this.eligibility = eligibility;
	}

	public String getEligibilityValue() {
		return eligibilityValue;
	}

	public void setEligibilityValue(String eligibilityValue) {
		this.eligibilityValue = eligibilityValue;
	}

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getDateOfRequestForTable() {
		return dateOfRequestForTable;
	}

	public void setDateOfRequestForTable(String dateOfRequestForTable) {
		this.dateOfRequestForTable = dateOfRequestForTable;
	}

	public String getCpuDivString() {
		return cpuDivString;
	}

	public void setCpuDivString(String cpuDivString) {
		this.cpuDivString = cpuDivString;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getDiag() {
		return diag;
	}

	public void setDiag(String diag) {
		this.diag = diag;
	}

	public String getManagement() {
		return management;
	}

	public void setManagement(String management) {
		this.management = management;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getAmountClaimed() {
		return amountClaimed;
	}

	public void setAmountClaimed(String amountClaimed) {
		this.amountClaimed = amountClaimed;
	}

	public String getSettledAmount() {
		return settledAmount;
	}

	public void setSettledAmount(String settledAmount) {
		this.settledAmount = settledAmount;
	}

	public String getAmountSaved() {
		return amountSaved;
	}

	public void setAmountSaved(String amountSaved) {
		this.amountSaved = amountSaved;
	}

	public String getCategoryValue() {
		return categoryValue;
	}

	public void setCategoryValue(String categoryValue) {
		this.categoryValue = categoryValue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getCreditsEmployeeName() {
		return creditsEmployeeName;
	}

	public void setCreditsEmployeeName(String creditsEmployeeName) {
		this.creditsEmployeeName = creditsEmployeeName;
	}

	public String getLapseEmployeeName() {
		return lapseEmployeeName;
	}

	public void setLapseEmployeeName(String lapseEmployeeName) {
		this.lapseEmployeeName = lapseEmployeeName;
	}

	public String getRrcStatusId() {
		return rrcStatusId;
	}

	public void setRrcStatusId(String rrcStatusId) {
		this.rrcStatusId = rrcStatusId;
	}

	public String getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	}

	public String getCheckBoxStatus() {
		return checkBoxStatus;
	}

	public void setCheckBoxStatus(String checkBoxStatus) {
		this.checkBoxStatus = checkBoxStatus;
	}

	public Double getInitialSumInsured() {
		return initialSumInsured;
	}

	public void setInitialSumInsured(Double initialSumInsured) {
		this.initialSumInsured = initialSumInsured;
	}

	public Long getPresentSumInsured() {
		return presentSumInsured;
	}

	public void setPresentSumInsured(Long presentSumInsured) {
		this.presentSumInsured = presentSumInsured;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getPedDisclosed() {
		return pedDisclosed;
	}

	public void setPedDisclosed(String pedDisclosed) {
		this.pedDisclosed = pedDisclosed;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getInitialPEDRecommendation() {
		return initialPEDRecommendation;
	}

	public void setInitialPEDRecommendation(String initialPEDRecommendation) {
		this.initialPEDRecommendation = initialPEDRecommendation;
	}

	public String getPedSuggestions() {
		return pedSuggestions;
	}

	public void setPedSuggestions(String pedSuggestions) {
		this.pedSuggestions = pedSuggestions;
	}

	public String getPedName() {
		return pedName;
	}

	public void setPedName(String pedName) {
		this.pedName = pedName;
	}

	public String getCpuName() {
		return cpuName;
	}

	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}

	public List<SearchRRCStatusTableDTO> getTotalList() {
		return totalList;
	}

	public void setTotalList(List<SearchRRCStatusTableDTO> totalList) {
		this.totalList = totalList;
	}

	public Boolean getIsRrcStatusScreen() {
		return isRrcStatusScreen;
	}

	public void setIsRrcStatusScreen(Boolean isRrcStatusScreen) {
		this.isRrcStatusScreen = isRrcStatusScreen;
	}

/*	public String getRrcStatus() {
		return rrcStatus;
	}

	public void setRrcStatus(String rrcStatus) {
		this.rrcStatus = rrcStatus;
	}*/
	

}
