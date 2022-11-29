package com.shaic.restservices.bancs.claimprovision.policydetail;

public class QualificationDetails {
	
	private Integer serialNumber;
	private String qualificationName;
	private String qualificationEffectiveFromDate;
	private String qualificationEffectiveToDate;
	
	public Integer getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getQualificationName() {
		return qualificationName;
	}
	public void setQualificationName(String qualificationName) {
		this.qualificationName = qualificationName;
	}
	public String getQualificationEffectiveFromDate() {
		return qualificationEffectiveFromDate;
	}
	public void setQualificationEffectiveFromDate(
			String qualificationEffectiveFromDate) {
		this.qualificationEffectiveFromDate = qualificationEffectiveFromDate;
	}
	public String getQualificationEffectiveToDate() {
		return qualificationEffectiveToDate;
	}
	public void setQualificationEffectiveToDate(String qualificationEffectiveToDate) {
		this.qualificationEffectiveToDate = qualificationEffectiveToDate;
	}
	
}
