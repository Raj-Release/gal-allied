package com.shaic.claim.preauth.wizard.pages;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Preauth;

public class PreAuthPreviousQueryDetailsTableDTO extends AbstractTableDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String queryNo;
	
	public String getQueryNo() {
		return queryNo;
	}

	public void setQueryNo(String queryNo) {
		this.queryNo = queryNo;
	}

	private String hospitalName;
	private String hospitalCity;
	private String diagnosis;
	private String queryRemarks;
	private String designataion;
	private String queryRaisedDate;
	private String queryStatus;
	private String acknowledgementNumber;
	private String rodNumber;
	private String documentReceivedFrom;
	private String billClassification;
	private Integer sno;
	
	private String queryRaisedRole;
	private String viewStatus;
	
	private Preauth preAuth;
	
	private Reimbursement reimbursement;
	
	private String queryType;
	
	private String opnQryTyp;

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
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

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getQueryRemarks() {
		return queryRemarks;
	}

	public void setQueryRemarks(String queryRemarks) {
		this.queryRemarks = queryRemarks;
	}

	public String getDesignataion() {
		return designataion;
	}

	public void setDesignataion(String designataion) {
		this.designataion = designataion;
	}

	public String getQueryRaisedDate() {
		return queryRaisedDate;
	}

	public void setQueryRaisedDate(String queryRaisedDate) {
		this.queryRaisedDate = queryRaisedDate;
	}

	public String getQueryStatus() {
		return queryStatus;
	}

	public void setQueryStatus(String queryStatus) {
		this.queryStatus = queryStatus;
	}

	public String getQueryRaisedRole() {
		return queryRaisedRole;
	}

	public void setQueryRaisedRole(String queryRaisedRole) {
		this.queryRaisedRole = queryRaisedRole;
	}

	public String getViewStatus() {
		return viewStatus;
	}

	public void setViewStatus(String viewStatus) {
		this.viewStatus = viewStatus;
	}

	public Preauth getPreAuth() {
		return preAuth;
	}

	public void setPreAuth(Preauth preAuth) {
		this.preAuth = preAuth;
	}

	public Reimbursement getReimbursement() {
		return reimbursement;
	}

	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
	}

	public String getAcknowledgementNumber() {
		return acknowledgementNumber;
	}

	public void setAcknowledgementNumber(String acknowledgementNumber) {
		this.acknowledgementNumber = acknowledgementNumber;
	}

	public String getRodNumber() {
		return rodNumber;
	}

	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}

	public String getDocumentReceivedFrom() {
		return documentReceivedFrom;
	}

	public void setDocumentReceivedFrom(String documentReceivedFrom) {
		this.documentReceivedFrom = documentReceivedFrom;
	}

	public String getBillClassification() {
		return billClassification;
	}

	public void setBillClassification(String billClassification) {
		this.billClassification = billClassification;
	}

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

	public String getOpnQryTyp() {
		return opnQryTyp;
	}

	public void setOpnQryTyp(String opnQryTyp) {
		this.opnQryTyp = opnQryTyp;
	}
}
