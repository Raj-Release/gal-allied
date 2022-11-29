package com.shaic.claim.reports.finapprovednotsettledreport;

import java.util.Date;
import java.util.List;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.newcode.wizard.dto.SearchTableDTO;
/**
 * AS Part of CR  R1201
 * @author Lakshminarayana
 *
 */
public class FinApprovedPaymentPendingReportDto extends SearchTableDTO {
	
	private Date frmDate;
	private Date toDate;
	private SelectValue cpuSelect;
	
	private String sno;
	private String claimNumber;
	private String intimationNumber;
	private String policyNumber;
	private String prodName;
	private String cpuCode;
	private String diagnosis; 
	private String hospitalName;
	private String hospCode;  
	private String patientName;
	private String paidDate; 
	private Double approvedAmount;  
	private Double claimedAmount;
	private Double deductAmount;
	private String clmType;
	private String medicalApprovedBy;
	private String billingApprovedBy;
	private String finApprovedBy;
	private String clmCoverage;
	private String sumInsured;
	private int age;
	private String financialYear;
	private String admissionDate;
	private String intimationDate;
	
	
	private List<FinApprovedPaymentPendingReportDto> searchResultList;


	public Date getFrmDate() {
		return frmDate;
	}


	public void setFrmDate(Date frmDate) {
		this.frmDate = frmDate;
	}


	public Date getToDate() {
		return toDate;
	}


	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}


	public SelectValue getCpuSelect() {
		return cpuSelect;
	}


	public void setCpuSelect(SelectValue cpuSelect) {
		this.cpuSelect = cpuSelect;
	}


	public String getSno() {
		return sno;
	}


	public void setSno(String sno) {
		this.sno = sno;
	}


	public String getClaimNumber() {
		return claimNumber;
	}


	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}


	public String getIntimationNumber() {
		return intimationNumber;
	}


	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}


	public String getPolicyNumber() {
		return policyNumber;
	}


	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}


	public String getProdName() {
		return prodName;
	}


	public void setProdName(String prodName) {
		this.prodName = prodName;
	}


	public String getCpuCode() {
		return cpuCode;
	}


	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}


	public String getDiagnosis() {
		return diagnosis;
	}


	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}


	public String getHospitalName() {
		return hospitalName;
	}


	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}


	public String getHospCode() {
		return hospCode;
	}


	public void setHospCode(String hospCode) {
		this.hospCode = hospCode;
	}


	public String getPatientName() {
		return patientName;
	}


	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}


	public String getPaidDate() {
		return paidDate;
	}


	public void setPaidDate(String paidDate) {
		this.paidDate = paidDate;
	}


	public Double getApprovedAmount() {
		return approvedAmount;
	}


	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}


	public Double getClaimedAmount() {
		return claimedAmount;
	}


	public void setClaimedAmount(Double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}


	public Double getDeductAmount() {
		return deductAmount;
	}


	public void setDeductAmount(Double deductAmount) {
		this.deductAmount = deductAmount;
	}


	public String getClmType() {
		return clmType;
	}


	public void setClmType(String clmType) {
		this.clmType = clmType;
	}


	public String getMedicalApprovedBy() {
		return medicalApprovedBy;
	}


	public void setMedicalApprovedBy(String medicalApprovedBy) {
		this.medicalApprovedBy = medicalApprovedBy;
	}


	public String getBillingApprovedBy() {
		return billingApprovedBy;
	}


	public void setBillingApprovedBy(String billingApprovedBy) {
		this.billingApprovedBy = billingApprovedBy;
	}


	public String getFinApprovedBy() {
		return finApprovedBy;
	}


	public void setFinApprovedBy(String finApprovedBy) {
		this.finApprovedBy = finApprovedBy;
	}


	public String getClmCoverage() {
		return clmCoverage;
	}


	public void setClmCoverage(String clmCoverage) {
		this.clmCoverage = clmCoverage;
	}


	public String getSumInsured() {
		return sumInsured;
	}


	public void setSumInsured(String sumInsured) {
		this.sumInsured = sumInsured;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public String getFinancialYear() {
		return financialYear;
	}


	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}


	public String getAdmissionDate() {
		return admissionDate;
	}


	public void setAdmissionDate(String admissionDate) {
		this.admissionDate = admissionDate;
	}


	public String getIntimationDate() {
		return intimationDate;
	}


	public void setIntimationDate(String intimationDate) {
		this.intimationDate = intimationDate;
	}


	public List<FinApprovedPaymentPendingReportDto> getSearchResultList() {
		return searchResultList;
	}


	public void setSearchResultList(
			List<FinApprovedPaymentPendingReportDto> searchResultList) {
		this.searchResultList = searchResultList;
	}

	

}
