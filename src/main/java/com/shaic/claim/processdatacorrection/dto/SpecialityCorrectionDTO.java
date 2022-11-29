package com.shaic.claim.processdatacorrection.dto;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractRowEnablerDTO;

public class SpecialityCorrectionDTO extends AbstractRowEnablerDTO{
	
	private Long key;
	
	private SelectValue specialityType;
	
	@NotNull
	private SelectValue actualspecialityType;

	private SelectValue procedure;

	private String remarks;
	
	private SelectValue actualProcedure;

	private String actualRemarks;
	
	private Boolean hasChanges = false;
	

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public SelectValue getSpecialityType() {
		return specialityType;
	}

	public void setSpecialityType(SelectValue specialityType) {
		this.specialityType = specialityType;
	}

	public Boolean getHasChanges() {
		return hasChanges;
	}

	public void setHasChanges(Boolean hasChanges) {
		this.hasChanges = hasChanges;
	}

	public SelectValue getProcedure() {
		return procedure;
	}

	public void setProcedure(SelectValue procedure) {
		this.procedure = procedure;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public SelectValue getActualProcedure() {
		return actualProcedure;
	}

	public void setActualProcedure(SelectValue actualProcedure) {
		this.actualProcedure = actualProcedure;
	}

	public String getActualRemarks() {
		return actualRemarks;
	}

	public void setActualRemarks(String actualRemarks) {
		this.actualRemarks = actualRemarks;
	}

	public SelectValue getActualspecialityType() {
		return actualspecialityType;
	}

	public void setActualspecialityType(SelectValue actualspecialityType) {
		this.actualspecialityType = actualspecialityType;
	}
	
}
