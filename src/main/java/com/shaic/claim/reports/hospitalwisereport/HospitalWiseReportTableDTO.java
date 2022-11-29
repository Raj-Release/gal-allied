package com.shaic.claim.reports.hospitalwisereport;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class HospitalWiseReportTableDTO extends AbstractTableDTO  implements Serializable{
	private Date intimationDate;
	private String intimationDateValue;
	
	private Long hospitalTypeId;
	private String hospitalsTypeId;
	private Long policyKey;
	private Long preauthKey;
	private Long claimKey;
	private String rodNO;	

	private String hospitalCode;
	private String hospitalName;
	private String intimationNo;
	
	private String fvrAllocatedDt;
	private String fieldVisitor;
	private String policyNo;
	private String officeName;
	private String agentCode;
	private String agentName;
	private String smCode;
	private String smName;
	private String productName;
	private String patientName;
	private Double age;
	private String insuredCity;
	private Date dateOfAdmission;	
	private String dateOfAdmissionValue;
	private Integer durationOfStay;
	private String diagnosis;
	private Double claimedAmt;
	private Double paidAmt;
	private Double outstandingAmount;
	private String sex;
	private String relationWithInsured;
	private Date dateOfDischarge;
	private String dateOfDischargeValue;
	private String claimType;
	private Double cashlessAuthorizedAmount;
	private Date rodDate;
	private String rodDateValue;
	private Date chequeDate;
	private String chequeDateValue;
	private String chequeNo;
	private Double chequeAmount;
	private String icdCodes;
	
	public Long getPolicyKey() {
		return policyKey;
	}
	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}
	
	public String getHospitalsTypeId() {
		return hospitalsTypeId;
	}
	public void setHospitalsTypeId(String hospitalsTypeId) {
		this.hospitalsTypeId = hospitalsTypeId;
	}
	
	public Date getIntimationDate() {
		return intimationDate;
	}
	public void setIntimationDate(Date intimationDate) {
		
		if(intimationDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(intimationDate);
			setIntimationDateValue(dateformat);
		    this.intimationDate = intimationDate;
		}
		
	}
	public String getIntimationDateValue() {
		return intimationDateValue;
	}
	public void setIntimationDateValue(String intimationDateValue) {
		this.intimationDateValue = intimationDateValue;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getFvrAllocatedDt() {
		return fvrAllocatedDt;
	}
	public void setFvrAllocatedDt(String fvrAllocatedDt) {
		this.fvrAllocatedDt = fvrAllocatedDt;
	}
	public String getFieldVisitor() {
		return fieldVisitor;
	}
	public void setFieldVisitor(String fieldVisitor) {
		this.fieldVisitor = fieldVisitor;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getSmCode() {
		return smCode;
	}
	public void setSmCode(String smCode) {
		this.smCode = smCode;
	}
	public String getSmName() {
		return smName;
	}
	public void setSmName(String smName) {
		this.smName = smName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	
	public Double getAge() {
		return age;
	}
	public void setAge(Double age) {
		this.age = age;
	}
	public String getInsuredCity() {
		return insuredCity;
	}
	public void setInsuredCity(String insuredCity) {
		this.insuredCity = insuredCity;
	}
	public Date getDateOfAdmission() {
		return dateOfAdmission;
	}
	public void setDateOfAdmission(Date dateOfAdmission) {
		
		if(dateOfAdmission !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(dateOfAdmission);
			setDateOfAdmissionValue(dateformat);
		    this.dateOfAdmission = dateOfAdmission;
		}
	}
	public String getDateOfAdmissionValue() {
		return dateOfAdmissionValue;
	}
	public void setDateOfAdmissionValue(String dateOfAdmissionValue) {
		this.dateOfAdmissionValue = dateOfAdmissionValue;
	}
	
	
	public Integer getDurationOfStay() {
		return durationOfStay;
	}
	public void setDurationOfStay(Integer durationOfStay) {
		this.durationOfStay = durationOfStay;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	
	
	
	public Double getPaidAmt() {
		return paidAmt;
	}
	public void setPaidAmt(Double paidAmt) {
		this.paidAmt = paidAmt;
	}
	public Double getOutstandingAmount() {
		return outstandingAmount;
	}
	public void setOutstandingAmount(Double outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getRelationWithInsured() {
		return relationWithInsured;
	}
	public void setRelationWithInsured(String relationWithInsured) {
		this.relationWithInsured = relationWithInsured;
	}
	public Date getDateOfDischarge() {
		return dateOfDischarge;
	}
	public void setDateOfDischarge(Date dateOfDischarge) {
		
		if(dateOfDischarge !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(dateOfDischarge);
			setDateOfDischargeValue(dateformat);
		    this.dateOfDischarge = dateOfDischarge;
		}
		
	}
	public String getDateOfDischargeValue() {
		return dateOfDischargeValue;
	}
	public void setDateOfDischargeValue(String dateOfDischargeValue) {
		this.dateOfDischargeValue = dateOfDischargeValue;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	
	public Double getClaimedAmt() {
		return claimedAmt;
	}
	public void setClaimedAmt(Double claimedAmt) {
		this.claimedAmt = claimedAmt;
	}
	public Double getCashlessAuthorizedAmount() {
		return cashlessAuthorizedAmount;
	}
	public void setCashlessAuthorizedAmount(Double cashlessAuthorizedAmount) {
		this.cashlessAuthorizedAmount = cashlessAuthorizedAmount;
	}
	public Date getRodDate() {
		return rodDate;
	}
	public void setRodDate(Date rodDate) {
		if(rodDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(rodDate);
			setRodDateValue(dateformat);
		    this.rodDate = rodDate;
		}
	}
	public String getRodDateValue() {
		return rodDateValue;
	}
	public void setRodDateValue(String rodDateValue) {
		this.rodDateValue = rodDateValue;
	}
	public Date getChequeDate() {
		return chequeDate;
	}
	public void setChequeDate(Date chequeDate) {
		if(chequeDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(chequeDate);
			setChequeDateValue(dateformat);
		    this.chequeDate = chequeDate;
		}
	}
	public String getChequeDateValue() {
		return chequeDateValue;
	}
	public void setChequeDateValue(String chequeDateValue) {
		this.chequeDateValue = chequeDateValue;
	}
	public String getChequeNo() {
		return chequeNo;
	}
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	public Double getChequeAmount() {
		return chequeAmount;
	}
	public void setChequeAmount(Double chequeAmount) {
		this.chequeAmount = chequeAmount;
	}
	public String getIcdCodes() {
		return icdCodes;
	}
	public void setIcdCodes(String icdCodes) {
		this.icdCodes = icdCodes;
	}
	public Long getHospitalTypeId() {
		return hospitalTypeId;
	}
	public void setHospitalTypeId(Long hospitalTypeId) {
		this.hospitalTypeId = hospitalTypeId;
	}
	public Long getPreauthKey() {
		return preauthKey;
	}
	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public String getRodNO() {
		return rodNO;
	}
	public void setRodNO(String rodNO) {
		this.rodNO = rodNO;
	}

}
