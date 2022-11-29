package com.shaic.claim.reports.helpdeskstatusreport;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class HelpDeskStatusReportTableDTO extends AbstractTableDTO  implements Serializable{
	private String intimationNo;
	private Date intimationDate;
	private String intimationDateValue;
	private String policyNumber;
	private String issueOfficeCode;
	private String productType;
	private String patientName;	
	private String irdaStatus;
	private String hospitalName;
	private String hospitalType;	
	private String callerContactNo;
	private String hospitalCode;	
	private String claimType;
	private Date billReceivedDate;
	private String billReceivedDateValue;
	private Double billingAmount;
	
	private String medicalStatus;
	private String zonalMedicalStatus;
	private Date medicalApprovedDate;
	private String medicalApprovedDateValue;
	private Date zonalMedicalApprovedDate;
	private String zonalMedicalapprovedDateValue;
	
	private String billingStage;
	private Date billinCompletedDate;
	private String billinCompletedDateValue;
	private String financialStage;	
	private Date financialCompletedDate;
	private String financialCompletedDateValue;
	private String chequeStatus;	
	private String cpuCode;	
	private String cpuName;
	
	private Long remainder;	
	private Date remainderDate;	
	private String remainderDateValue;
	private String rodScanStatus;	
	private String rodBillStatus;
	private String zonalInvestigation;
	private String receivedFrom;
	private Long hospitalId;
	private Long organizationUnitId;
	private Date createdDate;
	private String createdDateValue;
	private Date rodCreatedDate;
	private String rodCreatedDateValue;
	
    private Long stageKey;
    private Long statusKey;
    
    private String hospitalizationFlag;
    private String preHospitalizationFlag;
    private String postHospitalizationFlag;
    private String partialHospitalizationFlag;
    private String hospitalizationRepeatFlag;
    private String addOnBenifits;
    private String patientCare;
    private Date lastQueryReplyDate;
    private String lastQueryReplyDateValue;
    
    private Long preauthKey;
    private Long claimKey;
    private Long reimbursementKey;
    
    
    
	
    
	public Long getReimbursementKey() {
		return reimbursementKey;
	}
	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}
	public Date getLastQueryReplyDate() {
		return lastQueryReplyDate;
	}
	public void setLastQueryReplyDate(Date lastQueryReplyDate) {
		if(lastQueryReplyDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(lastQueryReplyDate);
			setLastQueryReplyDateValue(dateformat);
		}
		this.lastQueryReplyDate = lastQueryReplyDate;
	}
	public String getLastQueryReplyDateValue() {
		return lastQueryReplyDateValue;
	}
	public void setLastQueryReplyDateValue(String lastQueryReplyDateValue) {
		this.lastQueryReplyDateValue = lastQueryReplyDateValue;
	}
	public String getCreatedDateValue() {
		return createdDateValue;
	}
	public void setCreatedDateValue(String createdDateValue) {
		this.createdDateValue = createdDateValue;
	}
	public String getRodCreatedDateValue() {
		return rodCreatedDateValue;
	}
	public void setRodCreatedDateValue(String rodCreatedDateValue) {
		this.rodCreatedDateValue = rodCreatedDateValue;
	}
	public Date getRodCreatedDate() {
		return rodCreatedDate;
	}
	public void setRodCreatedDate(Date rodCreatedDate) {
		if(rodCreatedDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(rodCreatedDate);
			setRodCreatedDateValue(dateformat);
		}
		this.rodCreatedDate = rodCreatedDate;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		if(createdDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(createdDate);
			setCreatedDateValue(dateformat);
		}
		this.createdDate = createdDate;
	}
	public Long getOrganizationUnitId() {
		return organizationUnitId;
	}
	public void setOrganizationUnitId(Long organizationUnitId) {
		this.organizationUnitId = organizationUnitId;
	}
	public Double getBillingAmount() {
		return billingAmount;
	}
	public void setBillingAmount(Double billingAmount) {
		this.billingAmount = billingAmount;
	}

	public Long getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public Date getIntimationDate() {
		return intimationDate;
	}
	public void setIntimationDate(Date intimationDate) {
		if(intimationDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(intimationDate);
			setIntimationDateValue(dateformat);
		}
		this.intimationDate = intimationDate;
	}
	public String getIntimationDateValue() {
		return intimationDateValue;
	}
	public void setIntimationDateValue(String intimationDateValue) {
		this.intimationDateValue = intimationDateValue;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getIssueOfficeCode() {
		return issueOfficeCode;
	}
	public void setIssueOfficeCode(String issueOfficeCode) {
		this.issueOfficeCode = issueOfficeCode;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getIrdaStatus() {
		return irdaStatus;
	}
	public void setIrdaStatus(String irdaStatus) {
		this.irdaStatus = irdaStatus;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getHospitalType() {
		return hospitalType;
	}
	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}
	public String getCallerContactNo() {
		return callerContactNo;
	}
	public void setCallerContactNo(String callerContactNo) {
		this.callerContactNo = callerContactNo;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	
	public Date getBillReceivedDate() {
		return billReceivedDate;
	}
	public void setBillReceivedDate(Date billReceivedDate) {
		
		if(billReceivedDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(billReceivedDate);
			setBillReceivedDateValue(dateformat);		
		    
		}
		this.billReceivedDate = billReceivedDate;
	}
	public String getBillReceivedDateValue() {
		return billReceivedDateValue;
	}
	public void setBillReceivedDateValue(String billReceivedDateValue) {
		
		this.billReceivedDateValue = billReceivedDateValue;
	}
	
	public String getMedicalStatus() {
		return medicalStatus;
	}
	public void setMedicalStatus(String medicalStatus) {
		this.medicalStatus = medicalStatus;
	}
	
	public Date getMedicalApprovedDate() {
		return medicalApprovedDate;
	}
	public void setMedicalApprovedDate(Date medicalApprovedDate) {
		
		if(medicalApprovedDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(medicalApprovedDate);
			setMedicalApprovedDateValue(dateformat);			
		    
		}
		this.medicalApprovedDate = medicalApprovedDate;
	}
	public String getMedicalApprovedDateValue() {
		return medicalApprovedDateValue;
	}
	public void setMedicalApprovedDateValue(String medicalApprovedDateValue) {
		this.medicalApprovedDateValue = medicalApprovedDateValue;
	}
	public String getBillingStage() {
		return billingStage;
	}
	public void setBillingStage(String billingStage) {
		this.billingStage = billingStage;
	}
	public Date getBillinCompletedDate() {
		
		
		return billinCompletedDate;
	}
	public void setBillinCompletedDate(Date billinCompletedDate) {
		if(billinCompletedDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(billinCompletedDate);
			setBillinCompletedDateValue(dateformat);			
		    
		}
		this.billinCompletedDate = billinCompletedDate;
	}
	public String getBillinCompletedDateValue() {
		return billinCompletedDateValue;
	}
	public void setBillinCompletedDateValue(String billinCompletedDateValue) {
		this.billinCompletedDateValue = billinCompletedDateValue;
	}
	public String getFinancialStage() {
		return financialStage;
	}
	public void setFinancialStage(String financialStage) {
		this.financialStage = financialStage;
	}
	public Date getFinancialCompletedDate() {
		return financialCompletedDate;
	}
	public void setFinancialCompletedDate(Date financialCompletedDate) {
		if(financialCompletedDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(financialCompletedDate);
			setFinancialCompletedDateValue(dateformat);			
		    
		}
		this.financialCompletedDate = financialCompletedDate;
	}
	public String getFinancialCompletedDateValue() {
		return financialCompletedDateValue;
	}
	public void setFinancialCompletedDateValue(String financialCompletedDateValue) {
		this.financialCompletedDateValue = financialCompletedDateValue;
	}
	public String getChequeStatus() {
		return chequeStatus;
	}
	public void setChequeStatus(String chequeStatus) {
		this.chequeStatus = chequeStatus;
	}
	
	
	public String getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	public Long getRemainder() {
		return remainder;
	}
	public void setRemainder(Long remainder) {
		this.remainder = remainder;
	}
	public Date getRemainderDate() {
		return remainderDate;
	}
	public void setRemainderDate(Date remainderDate) {
		if(remainderDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(remainderDate);
			setRemainderDateValue(dateformat);				    
		}
		this.remainderDate = remainderDate;
	}
	public String getRemainderDateValue() {
		return remainderDateValue;
	}
	public void setRemainderDateValue(String remainderDateValue) {
		
		this.remainderDateValue = remainderDateValue;
	}
	public Date getZonalMedicalApprovedDate() {
		return zonalMedicalApprovedDate;
	}
	public void setZonalMedicalApprovedDate(Date zonalMedicalApprovedDate) {
		if(zonalMedicalApprovedDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(zonalMedicalApprovedDate);
			setZonalMedicalapprovedDateValue(dateformat);				    
		}
		this.zonalMedicalApprovedDate = zonalMedicalApprovedDate;
	}
	public String getZonalMedicalapprovedDateValue() {
		return zonalMedicalapprovedDateValue;
	}
	public void setZonalMedicalapprovedDateValue(
			String zonalMedicalapprovedDateValue) {
		this.zonalMedicalapprovedDateValue = zonalMedicalapprovedDateValue;
	}
	public String getRodScanStatus() {
		return rodScanStatus;
	}
	public void setRodScanStatus(String rodScanStatus) {
		this.rodScanStatus = rodScanStatus;
	}
	public String getRodBillStatus() {
		return rodBillStatus;
	}
	public void setRodBillStatus(String rodBillStatus) {
		this.rodBillStatus = rodBillStatus;
	}
	public String getZonalInvestigation() {
		return zonalInvestigation;
	}
	public void setZonalInvestigation(String zonalInvestigation) {
		this.zonalInvestigation = zonalInvestigation;
	}
	public String getReceivedFrom() {
		return receivedFrom;
	}
	public void setReceivedFrom(String receivedFrom) {
		this.receivedFrom = receivedFrom;
	}
	public Long getStageKey() {
		return stageKey;
	}
	public void setStageKey(Long stageKey) {
		this.stageKey = stageKey;
	}
	public Long getStatusKey() {
		return statusKey;
	}
	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}
	public String getHospitalizationFlag() {
		return hospitalizationFlag;
	}
	public void setHospitalizationFlag(String hospitalizationFlag) {
		this.hospitalizationFlag = hospitalizationFlag;
	}
	public String getPreHospitalizationFlag() {
		return preHospitalizationFlag;
	}
	public void setPreHospitalizationFlag(String preHospitalizationFlag) {
		this.preHospitalizationFlag = preHospitalizationFlag;
	}
	public String getPostHospitalizationFlag() {
		return postHospitalizationFlag;
	}
	public void setPostHospitalizationFlag(String postHospitalizationFlag) {
		this.postHospitalizationFlag = postHospitalizationFlag;
	}
	public Long getPreauthKey() {
		return preauthKey;
	}
	
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}
	public String getPartialHospitalizationFlag() {
		return partialHospitalizationFlag;
	}
	public void setPartialHospitalizationFlag(String partialHospitalizationFlag) {
		this.partialHospitalizationFlag = partialHospitalizationFlag;
	}
	public String getHospitalizationRepeatFlag() {
		return hospitalizationRepeatFlag;
	}
	public void setHospitalizationRepeatFlag(String hospitalizationRepeatFlag) {
		this.hospitalizationRepeatFlag = hospitalizationRepeatFlag;
	}
	public String getAddOnBenifits() {
		return addOnBenifits;
	}
	public void setAddOnBenifits(String addOnBenifits) {
		this.addOnBenifits = addOnBenifits;
	}
	public String getPatientCare() {
		return patientCare;
	}
	public void setPatientCare(String patientCare) {
		this.patientCare = patientCare;
	}
	public String getCpuName() {
		return cpuName;
	}
	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}
	public String getZonalMedicalStatus() {
		return zonalMedicalStatus;
	}
	public void setZonalMedicalStatus(String zonalMedicalStatus) {
		this.zonalMedicalStatus = zonalMedicalStatus;
	}
	

	
}
