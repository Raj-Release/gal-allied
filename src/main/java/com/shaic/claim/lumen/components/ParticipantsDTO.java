package com.shaic.claim.lumen.components;

import com.shaic.arch.table.AbstractTableDTO;

@SuppressWarnings("serial")
public class ParticipantsDTO extends AbstractTableDTO {

	private String empName;

	public ParticipantsDTO(){

	}

	public ParticipantsDTO(String empName) {
		this.empName = empName;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

}
