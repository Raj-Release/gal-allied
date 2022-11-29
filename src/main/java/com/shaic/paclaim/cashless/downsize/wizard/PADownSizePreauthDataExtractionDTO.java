package com.shaic.paclaim.cashless.downsize.wizard;

import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PADownSizePreauthDataExtractionDTO {
	
	private String downSizedAmt;
	private String medicalRemarks;
	private String reasonForDownSize;
	private String doctorNote;
	
	private String escalatedRole;
	private String escalationRemarks;
	private String escalatedUsername;
	private String escalatedDestination;
	
	public String specialistType;
	public String specialistRemarks;
	
	public Boolean isEscalateFromSpecialist = false;
	
	private Boolean CMA6 = false;
	private Boolean CMA5 = false;
	private Boolean CMA4 = false;
	private Boolean CMA3 = false;
	private Boolean CMA2 = false;
	private Boolean CMA1 = false;

	
	private BeanItemContainer<SelectValue> downsizeReason;
	
	private BeanItemContainer<SelectValue> escalateTo;
	
	
	
	public String getDownSizedAmt() {
		return downSizedAmt;
	}
	public void setDownSizedAmt(String downSizedAmt) {
		this.downSizedAmt = downSizedAmt;
	}
	public String getMedicalRemarks() {
		return medicalRemarks;
	}
	public void setMedicalRemarks(String medicalRemarks) {
		this.medicalRemarks = medicalRemarks;
	}
	public String getReasonForDownSize() {
		return reasonForDownSize;
	}
	public void setReasonForDownSize(String reasonForDownSize) {
		this.reasonForDownSize = reasonForDownSize;
	}
	public String getDoctorNote() {
		return doctorNote;
	}
	public void setDoctorNote(String doctorNote) {
		this.doctorNote = doctorNote;
	}
	public BeanItemContainer<SelectValue> getDownsizeReason() {
		return downsizeReason;
	}
	public void setDownsizeReason(BeanItemContainer<SelectValue> downsizeReason) {
		this.downsizeReason = downsizeReason;
	}
	public BeanItemContainer<SelectValue> getEscalateTo() {
		return escalateTo;
	}
	public void setEscalateTo(BeanItemContainer<SelectValue> escalateTo) {
		this.escalateTo = escalateTo;
	}
	public String getEscalatedRole() {
		return escalatedRole;
	}
	public void setEscalatedRole(String escalatedRole) {
		this.escalatedRole = escalatedRole;
	}
	public String getEscalationRemarks() {
		return escalationRemarks;
	}
	public void setEscalationRemarks(String escalationRemarks) {
		this.escalationRemarks = escalationRemarks;
	}
	public String getEscalatedUsername() {
		return escalatedUsername;
	}
	public void setEscalatedUsername(String escalatedUsername) {
		this.escalatedUsername = escalatedUsername;
	}
	public String getEscalatedDestination() {
		return escalatedDestination;
	}
	public void setEscalatedDestination(String escalatedDestination) {
		this.escalatedDestination = escalatedDestination;
	}
	public String getSpecialistType() {
		return specialistType;
	}
	public void setSpecialistType(String specialistType) {
		this.specialistType = specialistType;
	}
	public String getSpecialistRemarks() {
		return specialistRemarks;
	}
	public void setSpecialistRemarks(String specialistRemarks) {
		this.specialistRemarks = specialistRemarks;
	}
	public Boolean getIsEscalateFromSpecialist() {
		return isEscalateFromSpecialist;
	}
	public void setIsEscalateFromSpecialist(Boolean isEscalateFromSpecialist) {
		this.isEscalateFromSpecialist = isEscalateFromSpecialist;
	}
	
	
	public Boolean getCMA1() {
		return CMA1;
	}
	public void setCMA1(Boolean cMA1) {
		CMA1 = cMA1;
	}
	public Boolean getCMA2() {
		return CMA2;
	}
	public void setCMA2(Boolean cMA2) {
		CMA2 = cMA2;
	}
	public Boolean getCMA3() {
		return CMA3;
	}
	public void setCMA3(Boolean cMA3) {
		CMA3 = cMA3;
	}
	public Boolean getCMA4() {
		return CMA4;
	}
	public void setCMA4(Boolean cMA4) {
		CMA4 = cMA4;
	}
	public Boolean getCMA6() {
		return CMA6;
	}
	public void setCMA6(Boolean cMA6) {
		CMA6 = cMA6;
	}
	public Boolean getCMA5() {
		return CMA5;
	}
	public void setCMA5(Boolean cMA5) {
		CMA5 = cMA5;
	}
	
	
	
	
	

}
