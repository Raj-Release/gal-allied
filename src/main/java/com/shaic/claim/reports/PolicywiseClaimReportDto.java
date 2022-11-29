package com.shaic.claim.reports;

import java.text.SimpleDateFormat;

import com.shaic.claim.ClaimDto;
import com.shaic.domain.ReferenceTable;

public class PolicywiseClaimReportDto {
	
	private int sno;
	private String intimationDate;
	private String intimationNo;
	private String claimNo;
	private String mainMemberName;
	private String insuredPatientName;
	private String age;
	private String gender;
	private String iDCard;
	private String dataOfDischarge;
	private String relationshipWithInsured;
	private String provisionalDiagnosis;
	private String dateOfAdmission;
	private String isNetworkHospital;
	private String hospitalName;
	private String hospitalCity;
	private String cashlessOrReimbursement;
	private String managementType;
	private String outstandingAmount;
	private String paidAmount;
	private Double claimedAmount;
	private String paidDate;
	private String chequeNo;
	private String chequeDate;	
	private String modeOfPayment;	
	private String queryRaisedDate;	
	private String queryReplyReceivedDate;	
	private String rejectionReason;	
	private String queryReason;
	private String claimStatus;	
	private String iCDCode;	
	private String iCDDescription;	
	private String employeeID;	
	private String claimClassification;	
	private String rODDate;	
	private String claimRejectedDate;
	
	
	private String policyPeriodFrom; 
	private String policyPeriodTo;
	private Double premium; 
	private Double basepremium;
	private String endorsementPremium;
	private String totalNoOfClaims;
	private String policyNumber;
	private Double icrRatio;
	private String color;
	

public PolicywiseClaimReportDto(){
	
}
public PolicywiseClaimReportDto(ClaimDto clmDto){
		
		this.intimationDate = clmDto.getNewIntimationDto().getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(clmDto.getNewIntimationDto().getCreatedDate()) : "";
		this.intimationNo = clmDto.getNewIntimationDto().getIntimationId();
		this.claimNo = clmDto.getClaimId() != null ? clmDto.getClaimId() : "";
		this.mainMemberName= clmDto.getNewIntimationDto().getPolicy().getProposerFirstName() ;
		this.insuredPatientName = (ReferenceTable.PA_LOB_KEY).equals(clmDto.getLobId()) && clmDto.getNewIntimationDto().getPolicy().getProduct().getKey() == 111l ? 
															(clmDto.getNewIntimationDto().getPaPatientName() != null ? clmDto.getNewIntimationDto().getPaPatientName() : "") 
														: clmDto.getNewIntimationDto().getInsuredPatient().getInsuredName();
		this.age = clmDto.getNewIntimationDto().getInsuredPatient().getInsuredAge() != null ? String.valueOf(clmDto.getNewIntimationDto().getInsuredPatient().getInsuredAge().intValue()) : "" ;
		this.gender = clmDto.getNewIntimationDto().getInsuredPatient().getInsuredGender() != null && clmDto.getNewIntimationDto().getInsuredPatient().getInsuredGender().getValue() != null ? clmDto.getNewIntimationDto().getInsuredPatient().getInsuredGender().getValue() : "";
		this.iDCard= clmDto.getNewIntimationDto().getInsuredPatient().getHealthCardNumber() != null ? clmDto.getNewIntimationDto().getInsuredPatient().getHealthCardNumber() : "";
//		this.dataOfDischarge = reimbDto.getDateOfDischarge() != null ? new SimpleDateFormat("dd/MM/yyyy").format(reimbDto.getDateOfDischarge()) : "";
		this.relationshipWithInsured =  clmDto.getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null &&
				clmDto.getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getValue() != null ? clmDto.getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getValue() : "";
		this.provisionalDiagnosis="";
		this.dateOfAdmission = clmDto.getNewIntimationDto().getAdmissionDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(clmDto.getNewIntimationDto().getAdmissionDate()) : "";
		this.isNetworkHospital = (clmDto.getNewIntimationDto()
				.getHospitalType() != null ? (clmDto.getNewIntimationDto()
				.getHospitalType().getValue().equalsIgnoreCase("Network") ? "NETWORK"
				: "NON-NETWORK"):"");
		this.hospitalName = clmDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getName();
		this.hospitalCity = clmDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getCity();
		this.cashlessOrReimbursement = clmDto.getClaimTypeValue();
		this.managementType= (clmDto.getNewIntimationDto().getManagementType() != null ? clmDto.getNewIntimationDto().getManagementType().getValue() : "");
		this.policyPeriodFrom = clmDto.getNewIntimationDto().getPolicy().getPolicyFromDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(clmDto.getNewIntimationDto().getPolicy().getPolicyFromDate()) : "";
		this.policyPeriodTo = clmDto.getNewIntimationDto().getPolicy().getPolicyToDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(clmDto.getNewIntimationDto().getPolicy().getPolicyToDate()) : "";
		this.claimStatus = clmDto.getStatusName();
	}
	
	
	
	
//	public PolicywiseClaimReportDto(PreauthDTO preauthDto){
//		
//		this.intimationDate = preauthDto.getNewIntimationDTO().getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(preauthDto.getNewIntimationDTO().getCreatedDate()):"";
//		this.intimationNo = preauthDto.getNewIntimationDTO().getIntimationId();
//		this.mainMemberName= preauthDto.getNewIntimationDTO().getPolicy().getProposerFirstName() ;
//		this.insuredPatientName = preauthDto.getNewIntimationDTO().getInsuredPatientName();
//		this.age = preauthDto.getNewIntimationDTO().getInsuredPatient().getInsuredAge() != null ? String.valueOf(preauthDto.getNewIntimationDTO().getInsuredPatient().getInsuredAge().intValue()) : "" ;
//		this.gender =preauthDto.getNewIntimationDTO().getInsuredPatient().getInsuredGender() != null ? preauthDto.getNewIntimationDTO().getInsuredPatient().getInsuredGender().getValue() != null ? preauthDto.getNewIntimationDTO().getInsuredPatient().getInsuredGender().getValue() : "" : "";
//		this.iDCard= preauthDto.getNewIntimationDTO().getInsuredPatient().getHealthCardNumber();
//		this.dataOfDischarge = preauthDto.getPreauthDataExtractionDetails().getDischargeDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(preauthDto.getPreauthDataExtractionDetails().getDischargeDate()) : "";
//		this.relationshipWithInsured =  preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null ? preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getValue() != null ? preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getValue() : "" : "";
//		this.provisionalDiagnosis=preauthDto.getPreauthDataExtractionDetails().getDiagnosis();
//		this.dateOfAdmission = preauthDto.getPreauthDataExtractionDetails().getAdmissionDateStr();
//		this.isNetworkHospital = (preauthDto.getNewIntimationDTO()
//				.getHospitalType() != null ? (preauthDto.getNewIntimationDTO()
//				.getHospitalType().getValue().equalsIgnoreCase("Network") ? "NETWORK"
//				: "NON-NETWORK"):"");
//		this.hospitalName = preauthDto.getNewIntimationDTO().getHospitalDto().getName();		
//		this.cashlessOrReimbursement = preauthDto.getClaimDTO().getClaimTypeValue();
//		this.managementType= preauthDto.getPreauthDataExtractionDetails().getTreatmentType().getValue();
//		this.policyPeriodFrom = preauthDto.getNewIntimationDTO().getPolicy().getPolicyFromDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(preauthDto.getNewIntimationDTO().getPolicy().getPolicyFromDate()) : "";
//		this.policyPeriodTo = preauthDto.getNewIntimationDTO().getPolicy().getPolicyToDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(preauthDto.getNewIntimationDTO().getPolicy().getPolicyToDate()) : "";
//		this.claimStatus = preauthDto.getClaimDTO().getStatusName();
//		
//	
//	}
//	
//public PolicywiseClaimReportDto(ReimbursementDto reimbDto){
//		
//		this.intimationDate = new SimpleDateFormat("dd/MM/yyyy").format(reimbDto.getClaimDto().getNewIntimationDto().getCreatedDate());
//		this.intimationNo = reimbDto.getClaimDto().getNewIntimationDto().getIntimationId();
//		this.mainMemberName= reimbDto.getClaimDto().getNewIntimationDto().getPolicy().getProposerFirstName() ;
//		this.insuredPatientName = reimbDto.getClaimDto().getNewIntimationDto().getInsuredPatientName();
//		this.age = reimbDto.getClaimDto().getNewIntimationDto().getInsuredPatient().getInsuredAge() != null ? String.valueOf(reimbDto.getClaimDto().getNewIntimationDto().getInsuredPatient().getInsuredAge().intValue()) : "" ;
//		this.gender = reimbDto.getClaimDto().getNewIntimationDto().getInsuredPatient().getInsuredGender().getValue();
//		this.iDCard= reimbDto.getClaimDto().getNewIntimationDto().getInsuredPatient().getHealthCardNumber();
//		this.dataOfDischarge = reimbDto.getDateOfDischarge() != null ? new SimpleDateFormat("dd/MM/yyyy").format(reimbDto.getDateOfDischarge()) : "";
//		this.relationshipWithInsured =  reimbDto.getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getValue();
//		this.provisionalDiagnosis="";
//		this.dateOfAdmission = reimbDto.getDateOfAdmission() != null ? new SimpleDateFormat("dd/MM/yyyy").format(reimbDto.getDateOfAdmission()) : "";
//		this.isNetworkHospital = (reimbDto.getClaimDto().getNewIntimationDto()
//				.getHospitalType() != null ? (reimbDto.getClaimDto().getNewIntimationDto()
//				.getHospitalType().getValue().equalsIgnoreCase("Network") ? "NETWORK"
//				: "NON-NETWORK"):"");
//		this.hospitalName = reimbDto.getClaimDto().getNewIntimationDto().getHospitalDto().getName();		
//		this.cashlessOrReimbursement = reimbDto.getClaimDto().getClaimTypeValue();
//		this.managementType= (reimbDto.getTreatmentTypeId() != null ? reimbDto.getTreatmentTypeId().toString() : "");
//		this.policyPeriodFrom = reimbDto.getClaimDto().getNewIntimationDto().getPolicy().getPolicyFromDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(reimbDto.getClaimDto().getNewIntimationDto().getPolicy().getPolicyFromDate()) : "";
//		this.policyPeriodTo = reimbDto.getClaimDto().getNewIntimationDto().getPolicy().getPolicyToDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(reimbDto.getClaimDto().getNewIntimationDto().getPolicy().getPolicyToDate()) : "";
//		this.claimStatus = reimbDto.getClaimDto().getStatusName();
//	}
	
	
	
	
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	public String getIntimationDate() {
		return intimationDate;
	}
	public void setIntimationDate(String intimationDate) {
		this.intimationDate = intimationDate;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getMainMemberName() {
		return mainMemberName;
	}
	public void setMainMemberName(String mainMemberName) {
		this.mainMemberName = mainMemberName;
	}
	public String getInsuredPatientName() {
		return insuredPatientName;
	}
	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getiDCard() {
		return iDCard;
	}
	public void setiDCard(String iDCard) {
		this.iDCard = iDCard;
	}
	public String getDataOfDischarge() {
		return dataOfDischarge;
	}
	public void setDataOfDischarge(String dataOfDischarge) {
		this.dataOfDischarge = dataOfDischarge;
	}
	public String getRelationshipWithInsured() {
		return relationshipWithInsured;
	}
	public void setRelationshipWithInsured(String relationshipWithInsured) {
		this.relationshipWithInsured = relationshipWithInsured;
	}
	public String getProvisionalDiagnosis() {
		return provisionalDiagnosis;
	}
	public void setProvisionalDiagnosis(String provisionalDiagnosis) {
		this.provisionalDiagnosis = provisionalDiagnosis;
	}
	public String getDateOfAdmission() {
		return dateOfAdmission;
	}
	public void setDateOfAdmission(String dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}
	public String getIsNetworkHospital() {
		return isNetworkHospital;
	}
	public void setIsNetworkHospital(String isNetworkHospital) {
		this.isNetworkHospital = isNetworkHospital;
	}
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
	public String getCashlessOrReimbursement() {
		return cashlessOrReimbursement;
	}
	public void setCashlessOrReimbursement(String cashlessOrReimbursement) {
		this.cashlessOrReimbursement = cashlessOrReimbursement;
	}
	public String getManagementType() {
		return managementType;
	}
	public void setManagementType(String managementType) {
		this.managementType = managementType;
	}
	public String getOutstandingAmount() {
		return outstandingAmount;
	}
	public void setOutstandingAmount(String outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}
	public String getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount;
	}
	public Double getClaimedAmount() {
		return claimedAmount;
	}
	public void setClaimedAmount(Double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}
	public String getPaidDate() {
		return paidDate;
	}
	public void setPaidDate(String paidDate) {
		this.paidDate = paidDate;
	}
	public String getChequeNo() {
		return chequeNo;
	}
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	public String getChequeDate() {
		return chequeDate;
	}
	public void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
	}
	public String getModeOfPayment() {
		return modeOfPayment;
	}
	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}
	public String getQueryRaisedDate() {
		return queryRaisedDate;
	}
	public void setQueryRaisedDate(String queryRaisedDate) {
		this.queryRaisedDate = queryRaisedDate;
	}
	public String getQueryReplyReceivedDate() {
		return queryReplyReceivedDate;
	}
	public void setQueryReplyReceivedDate(String queryReplyReceivedDate) {
		this.queryReplyReceivedDate = queryReplyReceivedDate;
	}
	public String getRejectionReason() {
		return rejectionReason;
	}
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
	public String getQueryReason() {
		return queryReason;
	}
	public void setQueryReason(String queryReason) {
		this.queryReason = queryReason;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public String getiCDCode() {
		return iCDCode;
	}
	public void setiCDCode(String iCDCode) {
		this.iCDCode = iCDCode;
	}
	public String getiCDDescription() {
		return iCDDescription;
	}
	public void setiCDDescription(String iCDDescription) {
		this.iCDDescription = iCDDescription;
	}
	public String getEmployeeID() {
		return employeeID;
	}
	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}
	public String getClaimClassification() {
		return claimClassification;
	}
	public void setClaimClassification(String claimClassification) {
		this.claimClassification = claimClassification;
	}
	public String getrODDate() {
		return rODDate;
	}
	public void setrODDate(String rODDate) {
		this.rODDate = rODDate;
	}
	public String getClaimRejectedDate() {
		return claimRejectedDate;
	}
	public void setClaimRejectedDate(String claimRejectedDate) {
		this.claimRejectedDate = claimRejectedDate;
	}

	public String getPolicyPeriodFrom() {
		return policyPeriodFrom;
	}

	public void setPolicyPeriodFrom(String policyPeriodFrom) {
		this.policyPeriodFrom = policyPeriodFrom;
	}

	public String getPolicyPeriodTo() {
		return policyPeriodTo;
	}

	public void setPolicyPeriodTo(String policyPeriodTo) {
		this.policyPeriodTo = policyPeriodTo;
	}

	public Double getPremium() {
		return premium;
	}

	public void setPremium(Double premium) {
		this.premium = premium;
	}

	public Double getBasepremium() {
		return basepremium;
	}

	public void setBasepremium(Double basepremium) {
		this.basepremium = basepremium;
	}

	public String getEndorsementPremium() {
		return endorsementPremium;
	}

	public void setEndorsementPremium(String endorsementPremium) {
		this.endorsementPremium = endorsementPremium;
	}

	public String getTotalNoOfClaims() {
		return totalNoOfClaims;
	}

	public void setTotalNoOfClaims(String totalNoOfClaims) {
		this.totalNoOfClaims = totalNoOfClaims;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public Double getIcrRatio() {
		return icrRatio;
	}

	public void setIcrRatio(Double icrRatio) {
		this.icrRatio = icrRatio;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
		
}
