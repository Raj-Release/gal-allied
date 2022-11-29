package com.shaic.claim.preauth.wizard.view;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.shaic.newcode.wizard.dto.SearchTableDTO;

public class InvestigationDetailsTableDTO extends SearchTableDTO implements
		Serializable {

	private static final long serialVersionUID = 5683621750023215079L;
	
	private Integer sNo;

	private String investigatorName;

	private String investigatorCode;

	private String investigatorContactNo;

	private String hospitalName;

	private String investigationTriggerPoints;

	private String investigationAssignedDate;
	
	private Date investigationCompletedDate;
	
	private String investigationCompletedDateStr;

	private String investigationReportReceivedDate;

	private String tat;

	private String status;

	private Long hospitalkey;

	private Long intimationkey;

	private Long policyKey;

	private Long claimKey;
	
	private Long investigAssignedKey;
	
	private String investigationApprovedDate;
	
	

	public String getInvestigationApprovedDate() {
		return investigationApprovedDate;
	}

	public void setInvestigationApprovedDate(String investigationApprovedDate) {
		this.investigationApprovedDate = investigationApprovedDate;
	}

	public Integer getsNo() {
		return sNo;
	}

	public void setsNo(Integer sNo) {
		this.sNo = sNo;
	}

	public String getInvestigatorName() {
		return investigatorName;
	}

	public void setInvestigatorName(String investigatorName) {
		this.investigatorName = investigatorName;
	}

	public String getInvestigatorCode() {
		return investigatorCode;
	}

	public void setInvestigatorCode(String investigatorCode) {
		this.investigatorCode = investigatorCode;
	}

	public String getInvestigatorContactNo() {
		return investigatorContactNo;
	}

	public void setInvestigatorContactNo(String investigatorContactNo) {
		this.investigatorContactNo = investigatorContactNo;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	

	public String getInvestigationTriggerPoints() {
		return investigationTriggerPoints;
	}

	public void setInvestigationTriggerPoints(String investigationTriggerPoints) {
		this.investigationTriggerPoints = investigationTriggerPoints;
	}

	public String getInvestigationAssignedDate() {
		return investigationAssignedDate;
	}

	public void setInvestigationAssignedDate(String investigationAssignedDate) {
		this.investigationAssignedDate = investigationAssignedDate;
	}
	
	


	public Date getInvestigationCompletedDate() {
		return investigationCompletedDate;
	}

	public void setInvestigationCompletedDate(Date investigationCompletedDate) {
		this.investigationCompletedDate = investigationCompletedDate;
	}

	public String getInvestigationReportReceivedDate() {
		return investigationReportReceivedDate;
	}

	public void setInvestigationReportReceivedDate(
			String investigationReportReceivedDate) {
		this.investigationReportReceivedDate = investigationReportReceivedDate;
	}

	public String getTat() {
		return tat;
	}

	public void setTat(String tat) {
		this.tat = tat;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public Long getIntimationkey() {
		return intimationkey;
	}

	public void setIntimationkey(Long intimationkey) {
		this.intimationkey = intimationkey;
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

	public Long getHospitalkey() {
		return hospitalkey;
	}

	public void setHospitalkey(Long hospitalkey) {
		this.hospitalkey = hospitalkey;
	}

	public String getInvestigationCompletedDateStr() {
		return investigationCompletedDateStr;
	}

	public void setInvestigationCompletedDateStr(
			String investigationCompletedDateStr) {
		this.investigationCompletedDateStr = investigationCompletedDateStr;
	}

	public Long getInvestigAssignedKey() {
		return investigAssignedKey;
	}

	public void setInvestigAssignedKey(Long investigAssignedKey) {
		this.investigAssignedKey = investigAssignedKey;
	}

	
}
