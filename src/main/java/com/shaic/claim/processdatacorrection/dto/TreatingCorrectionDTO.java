package com.shaic.claim.processdatacorrection.dto;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractRowEnablerDTO;

public class TreatingCorrectionDTO extends AbstractRowEnablerDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2142201719774943832L;

	private Long key;
	
	private String treatingDoctorName;
	
	private String qualification;

	private String actualtreatingDoctorName;

	private String actualqualification;
	
	private Boolean hasChanges = false;
	
	private int serialNo;
	
	private SelectValue treatingDoctorSignature;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getTreatingDoctorName() {
		return treatingDoctorName;
	}

	public void setTreatingDoctorName(String treatingDoctorName) {
		this.treatingDoctorName = treatingDoctorName;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getActualtreatingDoctorName() {
		return actualtreatingDoctorName;
	}

	public void setActualtreatingDoctorName(String actualtreatingDoctorName) {
		this.actualtreatingDoctorName = actualtreatingDoctorName;
	}

	public String getActualqualification() {
		return actualqualification;
	}

	public void setActualqualification(String actualqualification) {
		this.actualqualification = actualqualification;
	}

	public Boolean getHasChanges() {
		return hasChanges;
	}

	public void setHasChanges(Boolean hasChanges) {
		this.hasChanges = hasChanges;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public SelectValue getTreatingDoctorSignature() {
		return treatingDoctorSignature;
	}

	public void setTreatingDoctorSignature(SelectValue treatingDoctorSignature) {
		this.treatingDoctorSignature = treatingDoctorSignature;
	}
	

}
