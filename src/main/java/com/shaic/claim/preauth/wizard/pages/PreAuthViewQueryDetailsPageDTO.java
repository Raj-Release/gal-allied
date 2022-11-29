package com.shaic.claim.preauth.wizard.pages;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class PreAuthViewQueryDetailsPageDTO extends AbstractTableDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String intimationNo;
	private String claimNo;
	private String policyNo;
	private String productName;
	private String claimType;
	private String insuredPatientName;
	private String hospitalName;
	private String hospitalCity;
	private String hospitalType;
	private String dateOfAdmission;
	private String diagnosis;
	private String queryRaisedByRole;
	private String queryRaisedByIdOrName;
	private String queryRaisedByDesignation;
	private String queryRaisedDate;
	private String queryRemarks;
	private String queryStatus;

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
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

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
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

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getDateOfAdmission() {
		return dateOfAdmission;
	}

	public void setDateOfAdmission(String dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getQueryRaisedByRole() {
		return queryRaisedByRole;
	}

	public void setQueryRaisedByRole(String queryRaisedByRole) {
		this.queryRaisedByRole = queryRaisedByRole;
	}

	public String getQueryRaisedByIdOrName() {
		return queryRaisedByIdOrName;
	}

	public void setQueryRaisedByIdOrName(String queryRaisedByIdOrName) {
		this.queryRaisedByIdOrName = queryRaisedByIdOrName;
	}

	public String getQueryRaisedByDesignation() {
		return queryRaisedByDesignation;
	}

	public void setQueryRaisedByDesignation(String queryRaisedByDesignation) {
		this.queryRaisedByDesignation = queryRaisedByDesignation;
	}

	public String getQueryRaisedDate() {
		return queryRaisedDate;
	}

	public void setQueryRaisedDate(String queryRaisedDate) {
		this.queryRaisedDate = queryRaisedDate;
	}

	public String getQueryRemarks() {
		return queryRemarks;
	}

	public void setQueryRemarks(String queryRemarks) {
		this.queryRemarks = queryRemarks;
	}

	public String getQueryStatus() {
		return queryStatus;
	}

	public void setQueryStatus(String queryStatus) {
		this.queryStatus = queryStatus;
	}

}
