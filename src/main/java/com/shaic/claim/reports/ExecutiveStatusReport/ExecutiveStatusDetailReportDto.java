package com.shaic.claim.reports.ExecutiveStatusReport;

import java.text.SimpleDateFormat;

import com.shaic.claim.ClaimDto;
import com.shaic.claim.ReimbursementDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Preauth;

/**
 * @author Lakshminarayana
 *
 */
public class ExecutiveStatusDetailReportDto {
	
	private int sno;
	private String registeredDate;
	private String claimProcessingUnit;
	private String intimationNo;
	private String intimationDate;
	private String policyNumber;
	private String policyIssueOfficeCode;
	private String relationshipWithInsured;
	private String sumInsured;
	private String balanceSumInsured;
	private String mainMemberName;
	private String insuredName;
	private String patientName;
	private String patientAge;
	private String admissionDate;
	private String admissionReason;
	private String hospitalType;
	private String hospitalName;
	private String state;
	private String city;
	private String fieldVisitorType;
	private String fieldVisitorName;
	private String isFVRReportUploaded;	
	private String isPreauthGiven;	
	private String provisionalAmount;	
	private String dischargeDate;	
	private String isCashlessOrReimbursement;	
	private String createdModifiedBy;
	private String screenStage;
	private Long claimKey;
	private String processDateNtime;
	private String transacOutcome;
	private String transacRemarks;
	
public ExecutiveStatusDetailReportDto(ClaimDto clmDto){
		
		this.intimationDate = clmDto.getNewIntimationDto().getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(clmDto.getNewIntimationDto().getCreatedDate()):"";
		this.intimationNo = clmDto.getNewIntimationDto().getIntimationId();
		this.policyNumber = clmDto.getNewIntimationDto().getPolicy().getPolicyNumber();
		this.registeredDate = clmDto.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(clmDto.getCreatedDate()) :"";
		this.claimKey = clmDto.getKey();
		this.mainMemberName= clmDto.getNewIntimationDto().getPolicy().getProposerFirstName() ;
		this.patientName = clmDto.getNewIntimationDto().getInsuredPatient().getInsuredName();
		this.patientAge = clmDto.getNewIntimationDto().getInsuredPatient().getInsuredAge() != null ? String.valueOf(clmDto.getNewIntimationDto().getInsuredPatient().getInsuredAge().intValue()): "";
//		this.dischargeDate = new SimpleDateFormat("dd/MM/yyyy").format(reimbDto.getDateOfDischarge());
		this.relationshipWithInsured =  clmDto.getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null ? (clmDto.getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getValue() != null ? clmDto.getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getValue() : "") : "";
		this.admissionDate = clmDto.getNewIntimationDto().getAdmissionDate() != null ?  new SimpleDateFormat("dd/MM/yyyy").format(clmDto.getNewIntimationDto().getAdmissionDate()):"";
		this.hospitalName = clmDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getName();		
		this.claimProcessingUnit = clmDto.getNewIntimationDto().getCpuCode();
		this.policyIssueOfficeCode = clmDto.getNewIntimationDto().getPolicy().getHomeOfficeCode() != null ? clmDto.getNewIntimationDto().getPolicy().getHomeOfficeCode() : "";
		this.insuredName = clmDto.getNewIntimationDto().getPolicy().getProposerFirstName();
		this.admissionReason = clmDto.getNewIntimationDto().getReasonForAdmission();
		this.hospitalType = clmDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getHospitalType() != null ? clmDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getHospitalType().getValue() : "";
		this.state = clmDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getState();
		this.city = clmDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getCity();
		this.isCashlessOrReimbursement = clmDto.getClaimType() != null ?  clmDto.getClaimType().getValue() : "";
//		this.screenStage = clmDto.getStageName();
		this.provisionalAmount = clmDto.getProvisionAmount() != null ? String.valueOf(clmDto.getCurrentProvisionAmount()):"";
	}

public ExecutiveStatusDetailReportDto(Claim clmObj){
	
	this.intimationDate = clmObj.getIntimation().getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(clmObj.getIntimation().getCreatedDate()):"";
	this.intimationNo = clmObj.getIntimation().getIntimationId();
	this.policyNumber = clmObj.getIntimation().getPolicy().getPolicyNumber();
	this.claimKey = clmObj.getKey();
	this.patientName = clmObj.getIntimation().getInsured().getInsuredName();
	this.patientAge = clmObj.getIntimation().getInsured().getInsuredAge() != null ? String.valueOf(clmObj.getIntimation().getInsured().getInsuredAge().intValue()): "";
	this.admissionDate = clmObj.getIntimation().getAdmissionDate() != null ?  new SimpleDateFormat("dd/MM/yyyy").format(clmObj.getIntimation().getAdmissionDate()):"";
	this.claimProcessingUnit = clmObj.getIntimation().getCpuCode() != null ? clmObj.getIntimation().getCpuCode().getDescription() + " - " + String.valueOf(clmObj.getIntimation().getCpuCode().getCpuCode()) : "";
	this.insuredName = clmObj.getIntimation().getPolicy().getProposerFirstName();
	this.admissionReason = clmObj.getIntimation().getAdmissionReason();
	this.isCashlessOrReimbursement = clmObj.getClaimType() != null ?  clmObj.getClaimType().getValue() : "";
	this.screenStage = clmObj.getStage().getStageName();
}

public ExecutiveStatusDetailReportDto(Preauth preauthObj){
	
	this.intimationDate = new SimpleDateFormat("dd/MM/yyyy").format(preauthObj.getIntimation().getCreatedDate());
	this.intimationNo = preauthObj.getIntimation().getIntimationId();
	this.policyNumber = preauthObj.getClaim().getIntimation().getPolicy().getPolicyNumber();
	this.mainMemberName= preauthObj.getIntimation().getPolicy().getProposerFirstName() ;
	this.patientName = preauthObj.getIntimation().getInsured().getInsuredName();
	this.patientAge = preauthObj.getIntimation().getInsured().getInsuredAge() != null ? String.valueOf(preauthObj.getIntimation().getInsured().getInsuredAge().intValue()) : "";
	this.admissionDate = preauthObj.getDataOfAdmission() != null ?new SimpleDateFormat("dd/MM/yyyy").format(preauthObj.getDataOfAdmission()) :"";
	this.claimProcessingUnit = preauthObj.getIntimation().getCpuCode() != null ? preauthObj.getIntimation().getCpuCode().getDescription() + " - " + String.valueOf(preauthObj.getIntimation().getCpuCode().getCpuCode()) : "";
	this.insuredName = preauthObj.getIntimation().getPolicy().getProposerFirstName();
	this.admissionReason = preauthObj.getIntimation().getAdmissionReason() != null ? preauthObj.getIntimation().getAdmissionReason() : "";
	this.isCashlessOrReimbursement = preauthObj.getClaim().getClaimType() != null ?  preauthObj.getClaim().getClaimType().getValue() : "";
	this.createdModifiedBy = preauthObj.getModifiedBy() != null ? preauthObj.getModifiedBy() : (preauthObj.getCreatedBy() != null ? preauthObj.getCreatedBy() : "");
	this.screenStage = preauthObj.getStage().getStageName();
}
	
public static ExecutiveStatusDetailReportDto updateStatusDto(ExecutiveStatusDetailReportDto statusDto, Claim clmObj){
	
	statusDto.intimationDate = clmObj.getIntimation().getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(clmObj.getIntimation().getCreatedDate()):"";
	statusDto.intimationNo = clmObj.getIntimation().getIntimationId();
	statusDto.policyNumber = clmObj.getIntimation().getPolicy().getPolicyNumber();
	statusDto.claimKey = clmObj.getKey();
	statusDto.patientName = clmObj.getIntimation().getInsured().getInsuredName();
	statusDto.patientAge = clmObj.getIntimation().getInsured().getInsuredAge() != null ? String.valueOf(clmObj.getIntimation().getInsured().getInsuredAge().intValue()): "";
	statusDto.admissionDate = clmObj.getIntimation().getAdmissionDate() != null ?  new SimpleDateFormat("dd/MM/yyyy").format(clmObj.getIntimation().getAdmissionDate()):"";
	statusDto.claimProcessingUnit = clmObj.getIntimation().getCpuCode() != null ? clmObj.getIntimation().getCpuCode().getDescription() + " - " + String.valueOf(clmObj.getIntimation().getCpuCode().getCpuCode()) : "";
	statusDto.insuredName = clmObj.getIntimation().getPolicy().getProposerFirstName();
	statusDto.admissionReason = clmObj.getIntimation().getAdmissionReason();
	statusDto.isCashlessOrReimbursement = clmObj.getClaimType() != null ?  clmObj.getClaimType().getValue() : "";
	statusDto.screenStage = clmObj.getStage().getStageName();	
	
	return statusDto; 
}
	public ExecutiveStatusDetailReportDto(PreauthDTO preauthDto){
		
		this.intimationDate = new SimpleDateFormat("dd/MM/yyyy").format(preauthDto.getNewIntimationDTO().getCreatedDate());
		this.intimationNo = preauthDto.getNewIntimationDTO().getIntimationId();
		this.mainMemberName= preauthDto.getNewIntimationDTO().getPolicy().getProposerFirstName() ;
		this.patientName = preauthDto.getNewIntimationDTO().getInsuredPatient().getInsuredName();
		this.patientAge = preauthDto.getNewIntimationDTO().getInsuredPatient().getInsuredAge() != null ? String.valueOf(preauthDto.getNewIntimationDTO().getInsuredPatient().getInsuredAge().intValue()) : "";
		this.dischargeDate = new SimpleDateFormat("dd/MM/yyyy").format(preauthDto.getPreauthDataExtractionDetails().getDischargeDate());
		this.relationshipWithInsured =  preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null ? ( preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getValue() != null ? preauthDto.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getValue() : "") : "";
		this.admissionDate = preauthDto.getPreauthDataExtractionDetails().getAdmissionDateStr();
		this.hospitalName = preauthDto.getNewIntimationDTO().getHospitalDto().getName();		
		this.registeredDate = preauthDto.getClaimDTO().getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(preauthDto.getClaimDTO().getCreatedDate()) : "";
		this.claimProcessingUnit = preauthDto.getNewIntimationDTO().getCpuCode();
		this.policyIssueOfficeCode = preauthDto.getNewIntimationDTO().getPolicy().getHomeOfficeCode() != null ? preauthDto.getNewIntimationDTO().getPolicy().getHomeOfficeCode() : "";
		this.insuredName = preauthDto.getNewIntimationDTO().getPolicy().getProposerFirstName();
		this.admissionReason = preauthDto.getNewIntimationDTO().getReasonForAdmission();
		this.hospitalType = preauthDto.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalType() != null ? preauthDto.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalType().getValue() : "";
		this.state = preauthDto.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getState();
		this.city = preauthDto.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getCity();
		this.isCashlessOrReimbursement = preauthDto.getClaimDTO().getClaimType() != null ?  preauthDto.getClaimDTO().getClaimType().getValue() : "";
		this.createdModifiedBy = preauthDto.getClaimDTO().getModifiedBy() != null ? preauthDto.getClaimDTO().getModifiedBy() : (preauthDto.getClaimDTO().getCreatedBy() != null ? preauthDto.getClaimDTO().getCreatedBy() : "");
	}
	
public ExecutiveStatusDetailReportDto(Reimbursement reimbObj){
		
		this.intimationDate = new SimpleDateFormat("dd/MM/yyyy").format(reimbObj.getClaim().getIntimation().getCreatedDate());
		this.intimationNo = reimbObj.getClaim().getIntimation().getIntimationId();
		this.policyNumber = reimbObj.getClaim().getIntimation().getPolicy().getPolicyNumber();
//		this.mainMemberName= reimbObj.getClaim().getIntimation().getPolicy().getProposerFirstName() ;
		this.patientName = reimbObj.getClaim().getIntimation().getInsured().getInsuredName();
		this.patientAge = reimbObj.getClaim().getIntimation().getInsured().getInsuredAge() != null ? String.valueOf(reimbObj.getClaim().getIntimation().getInsured().getInsuredAge().intValue()): "";
//		this.dischargeDate = new SimpleDateFormat("dd/MM/yyyy").format(reimbObj.getDateOfDischarge());
//		this.relationshipWithInsured =  reimbObj.getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null ? (reimbObj.getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getValue() != null ? reimbObj.getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getValue() : "") : "";
		this.admissionDate = new SimpleDateFormat("dd/MM/yyyy").format(reimbObj.getDateOfAdmission());
//		this.hospitalName = reimbObj.getClaimDto().getNewIntimationDto().getHospitalDto().getName();		
//		this.registeredDate = reimbObj.getClaimDto().getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(reimbObj.getClaimDto().getCreatedDate()) : "";
		this.claimProcessingUnit = reimbObj.getClaim().getIntimation().getCpuCode() != null ? reimbObj.getClaim().getIntimation().getCpuCode().getDescription() + " - " + String.valueOf(reimbObj.getClaim().getIntimation().getCpuCode().getCpuCode()) : "";
//		this.policyIssueOfficeCode = reimbObj.getClaimDto().getNewIntimationDto().getPolicy().getHomeOfficeCode() != null ? reimbObj.getClaimDto().getNewIntimationDto().getPolicy().getHomeOfficeCode() : "";
		this.insuredName = reimbObj.getClaim().getIntimation().getPolicy().getProposerFirstName();
		this.admissionReason = reimbObj.getClaim().getIntimation().getAdmissionReason();
//		this.hospitalType = reimbObj.getClaimDto().getNewIntimationDto().getHospitalDto().getRegistedHospitals().getHospitalType() != null ? reimbObj.getClaimDto().getNewIntimationDto().getHospitalDto().getRegistedHospitals().getHospitalType().getValue() : "";
//		this.state = reimbObj.getClaimDto().getNewIntimationDto().getHospitalDto().getRegistedHospitals().getState();
//		this.city = reimbObj.getClaimDto().getNewIntimationDto().getHospitalDto().getRegistedHospitals().getCity();
		this.isCashlessOrReimbursement = reimbObj.getClaim().getClaimType() != null ?  reimbObj.getClaim().getClaimType().getValue() : "";
		this.createdModifiedBy = reimbObj.getModifiedBy() != null ? reimbObj.getModifiedBy() : (reimbObj.getCreatedBy() != null ? reimbObj.getCreatedBy() : "");		
	}
	
public ExecutiveStatusDetailReportDto(ReimbursementDto reimbDto){
		
		this.intimationDate = new SimpleDateFormat("dd/MM/yyyy").format(reimbDto.getClaimDto().getNewIntimationDto().getCreatedDate());
		this.intimationNo = reimbDto.getClaimDto().getNewIntimationDto().getIntimationId();
		this.mainMemberName= reimbDto.getClaimDto().getNewIntimationDto().getPolicy().getProposerFirstName() ;
		this.patientName = reimbDto.getClaimDto().getNewIntimationDto().getInsuredPatient().getInsuredName();
		this.patientAge = reimbDto.getClaimDto().getNewIntimationDto().getInsuredPatient().getInsuredAge() != null ? String.valueOf(reimbDto.getClaimDto().getNewIntimationDto().getInsuredPatient().getInsuredAge().intValue()): "";
		this.dischargeDate = new SimpleDateFormat("dd/MM/yyyy").format(reimbDto.getDateOfDischarge());
		this.relationshipWithInsured =  reimbDto.getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null ? (reimbDto.getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getValue() != null ? reimbDto.getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getValue() : "") : "";
		this.admissionDate = new SimpleDateFormat("dd/MM/yyyy").format(reimbDto.getDateOfAdmission());
		this.hospitalName = reimbDto.getClaimDto().getNewIntimationDto().getHospitalDto().getName();		
		this.registeredDate = reimbDto.getClaimDto().getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(reimbDto.getClaimDto().getCreatedDate()) : "";
		this.claimProcessingUnit = reimbDto.getClaimDto().getNewIntimationDto().getCpuCode();
		this.policyIssueOfficeCode = reimbDto.getClaimDto().getNewIntimationDto().getPolicy().getHomeOfficeCode() != null ? reimbDto.getClaimDto().getNewIntimationDto().getPolicy().getHomeOfficeCode() : "";
		this.insuredName = reimbDto.getClaimDto().getNewIntimationDto().getPolicy().getProposerFirstName();
		this.admissionReason = reimbDto.getClaimDto().getNewIntimationDto().getReasonForAdmission();
		this.hospitalType = reimbDto.getClaimDto().getNewIntimationDto().getHospitalDto().getRegistedHospitals().getHospitalType() != null ? reimbDto.getClaimDto().getNewIntimationDto().getHospitalDto().getRegistedHospitals().getHospitalType().getValue() : "";
		this.state = reimbDto.getClaimDto().getNewIntimationDto().getHospitalDto().getRegistedHospitals().getState();
		this.city = reimbDto.getClaimDto().getNewIntimationDto().getHospitalDto().getRegistedHospitals().getCity();
		this.isCashlessOrReimbursement = reimbDto.getClaimDto().getClaimType() != null ?  reimbDto.getClaimDto().getClaimType().getValue() : "";
		this.createdModifiedBy = reimbDto.getClaimDto().getModifiedBy() != null ? reimbDto.getClaimDto().getModifiedBy() : (reimbDto.getClaimDto().getCreatedBy() != null ? reimbDto.getClaimDto().getCreatedBy() : "");
		
	}
		
	
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}

	public String getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(String registeredDate) {
		this.registeredDate = registeredDate;
	}

	public String getClaimProcessingUnit() {
		return claimProcessingUnit;
	}

	public void setClaimProcessingUnit(String claimProcessingUnit) {
		this.claimProcessingUnit = claimProcessingUnit;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getIntimationDate() {
		return intimationDate;
	}

	public void setIntimationDate(String intimationDate) {
		this.intimationDate = intimationDate;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getPolicyIssueOfficeCode() {
		return policyIssueOfficeCode;
	}

	public void setPolicyIssueOfficeCode(String policyIssueOfficeCode) {
		this.policyIssueOfficeCode = policyIssueOfficeCode;
	}

	public String getRelationshipWithInsured() {
		return relationshipWithInsured;
	}

	public void setRelationshipWithInsured(String relationshipWithInsured) {
		this.relationshipWithInsured = relationshipWithInsured;
	}

	public String getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(String sumInsured) {
		this.sumInsured = sumInsured;
	}

	public String getBalanceSumInsured() {
		return balanceSumInsured;
	}

	public void setBalanceSumInsured(String balanceSumInsured) {
		this.balanceSumInsured = balanceSumInsured;
	}

	public String getMainMemberName() {
		return mainMemberName;
	}

	public void setMainMemberName(String mainMemberName) {
		this.mainMemberName = mainMemberName;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientAge() {
		return patientAge;
	}

	public void setPatientAge(String patientAge) {
		this.patientAge = patientAge;
	}

	public String getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(String admissionDate) {
		this.admissionDate = admissionDate;
	}

	public String getAdmissionReason() {
		return admissionReason;
	}

	public void setAdmissionReason(String admissionReason) {
		this.admissionReason = admissionReason;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getFieldVisitorType() {
		return fieldVisitorType;
	}

	public void setFieldVisitorType(String fieldVisitorType) {
		this.fieldVisitorType = fieldVisitorType;
	}

	public String getFieldVisitorName() {
		return fieldVisitorName;
	}

	public void setFieldVisitorName(String fieldVisitorName) {
		this.fieldVisitorName = fieldVisitorName;
	}

	public String getIsFVRReportUploaded() {
		return isFVRReportUploaded;
	}

	public void setIsFVRReportUploaded(String isFVRReportUploaded) {
		this.isFVRReportUploaded = isFVRReportUploaded;
	}

	public String getIsPreauthGiven() {
		return isPreauthGiven;
	}

	public void setIsPreauthGiven(String isPreauthGiven) {
		this.isPreauthGiven = isPreauthGiven;
	}

	public String getProvisionalAmount() {
		return provisionalAmount;
	}

	public void setProvisionalAmount(String provisionalAmount) {
		this.provisionalAmount = provisionalAmount;
	}

	public String getDischargeDate() {
		return dischargeDate;
	}

	public void setDischargeDate(String dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	public String getIsCashlessOrReimbursement() {
		return isCashlessOrReimbursement;
	}

	public void setIsCashlessOrReimbursement(String isCashlessOrReimbursement) {
		this.isCashlessOrReimbursement = isCashlessOrReimbursement;
	}

	public String getCreatedModifiedBy() {
		return createdModifiedBy;
	}

	public void setCreatedModifiedBy(String createdModifiedBy) {
		this.createdModifiedBy = createdModifiedBy;
	}

	public String getScreenStage() {
		return screenStage;
	}

	public void setScreenStage(String screenStage) {
		this.screenStage = screenStage;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public String getProcessDateNtime() {
		return processDateNtime;
	}

	public void setProcessDateNtime(String processDateNtime) {
		this.processDateNtime = processDateNtime;
	}

	public String getTransacOutcome() {
		return transacOutcome;
	}

	public void setTransacOutcome(String transacOutcome) {
		this.transacOutcome = transacOutcome;
	}

	public String getTransacRemarks() {
		return transacRemarks;
	}

	public void setTransacRemarks(String transacRemarks) {
		this.transacRemarks = transacRemarks;
	}
	
}
