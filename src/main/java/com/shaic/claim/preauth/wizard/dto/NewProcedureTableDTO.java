package com.shaic.claim.preauth.wizard.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;

public class NewProcedureTableDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2119438677976843447L;
	
	private Long key;
	
	private Long preauthKey;
	
	@NotNull(message = "Please Select Procedure Name.")
	private String procedureName;
	
	@NotNull(message = "Please Select Procedure Code.")
	private String procedureCode;
	
	private SelectValue considerForDayCare;
	
	private String considerForDayFlag;
	
	private String remarks;
	
	private String newProcedureFlag;

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public String getProcedureCode() {
		return procedureCode;
	}

	public void setProcedureCode(String procedureCode) {
		this.procedureCode = procedureCode;
	}

	public SelectValue getConsiderForDayCare() {
		return considerForDayCare;
	}

	public void setConsiderForDayCare(SelectValue considerForDayCare) {
		this.considerForDayCare = considerForDayCare;
		
		this.considerForDayFlag = this.considerForDayCare!= null && this.considerForDayCare.getValue().equals("Yes") ? "Y" : "N";
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getPreauthKey() {
		return preauthKey;
	}

	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}

	public String getConsiderForDayFlag() {
		return considerForDayFlag;
	}

	public void setConsiderForDayFlag(String considerForDayFlag) {
		this.considerForDayFlag = considerForDayFlag;
	}

	public String getNewProcedureFlag() {
		return newProcedureFlag;
	}

	public void setNewProcedureFlag(String newProcedureFlag) {
		this.newProcedureFlag = "1";
	}

}
