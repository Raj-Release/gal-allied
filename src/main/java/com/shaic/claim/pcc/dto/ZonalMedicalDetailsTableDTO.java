package com.shaic.claim.pcc.dto;

import java.util.Date;

public class ZonalMedicalDetailsTableDTO {
	
	private Long queryKey;
	
	private String medicalIdAndName;
	
	private String remarks;
	
	private Date assignDateAndTime;

	public Long getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(Long queryKey) {
		this.queryKey = queryKey;
	}

	public String getMedicalIdAndName() {
		return medicalIdAndName;
	}

	public void setMedicalIdAndName(String medicalIdAndName) {
		this.medicalIdAndName = medicalIdAndName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getAssignDateAndTime() {
		return assignDateAndTime;
	}

	public void setAssignDateAndTime(Date assignDateAndTime) {
		this.assignDateAndTime = assignDateAndTime;
	}
	

}
