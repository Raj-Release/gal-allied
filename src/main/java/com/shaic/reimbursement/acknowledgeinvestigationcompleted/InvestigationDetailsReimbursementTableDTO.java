package com.shaic.reimbursement.acknowledgeinvestigationcompleted;

import com.shaic.arch.table.AbstractTableDTO;

public class InvestigationDetailsReimbursementTableDTO extends AbstractTableDTO {
	
	private Long key;
	
	private String select;
	
	private Integer sno;
	
	private String investigatorName;
	
	private String investigatorCode;
	
	private String investigatorContactNo;
	
	private String hospitalName;
	
	private String remarks;
	
	private String investigationAssignedDate;
	
	private Long investigationAssignedKey;
	
	private String investigationCompletedDate;
	
	private String investigatorStatus;	

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getInvestigationAssignedDate() {
		return investigationAssignedDate;
	}

	public void setInvestigationAssignedDate(String investigationAssignedDate) {
		this.investigationAssignedDate = investigationAssignedDate;
	}

	public String getInvestigatorStatus() {
		return investigatorStatus;
	}

	public void setInvestigatorStatus(String investigatorStatus) {
		this.investigatorStatus = investigatorStatus;
	}


	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

	public String getInvestigationCompletedDate() {
		return investigationCompletedDate;
	}

	public void setInvestigationCompletedDate(String investigationCompletedDate) {
		this.investigationCompletedDate = investigationCompletedDate;
	}

	public Long getInvestigationAssignedKey() {
		return investigationAssignedKey;
	}

	public void setInvestigationAssignedKey(Long investigationAssignedKey) {
		this.investigationAssignedKey = investigationAssignedKey;
	}

}
