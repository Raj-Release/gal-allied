package com.shaic.claim.userproduct.document;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class SearchDoctorCriteriaDTO extends AbstractTableDTO implements Serializable {
	
	private String EmpCode;
	
	private String EmploginId;

	public String getEmpCode() {
		return EmpCode;
	}

	public void setEmpCode(String empCode) {
		EmpCode = empCode;
	}

	public String getEmploginId() {
		return EmploginId;
	}

	public void setEmploginId(String emploginId) {
		EmploginId = emploginId;
	}
	
	

}
