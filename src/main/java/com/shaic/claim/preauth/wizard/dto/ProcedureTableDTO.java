package com.shaic.claim.preauth.wizard.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;

public class ProcedureTableDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2119438677976843447L;
	
	private Long key;
	
	private Long preauthKey;
	
	@NotNull(message = "Please Select Procedure Name or Procedure Code.")
	private SelectValue procedureName;
	
	private SelectValue procedureCode;
	
	private String packageRate;
	
	private String dayCareProcedure;
	
	private SelectValue considerForDayCare;
	
	private String considerForDayFlag;
	
	private SelectValue sublimitApplicable;
	
	private String sublimitApplicableFlag;
	
	private SelectValue sublimitName;
	
	private String sublimitDesc;
	
	private String sublimitAmount;
	
	private SelectValue considerForPayment;
	
	private String considerForPaymentFlag;
	
	private String newProcedureFlag;
	
	private String remarks;
	

	public SelectValue getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(SelectValue procedureName) {
		this.procedureName = procedureName;
	}

	public SelectValue getProcedureCode() {
		return procedureCode;
	}

	public void setProcedureCode(SelectValue procedureCode) {
		this.procedureCode = procedureCode;
	}

	public String getPackageRate() {
		return packageRate;
	}

	public void setPackageRate(String packageRate) {
		this.packageRate = packageRate;
	}

	public String getDayCareProcedure() {
		return dayCareProcedure;
	}

	public void setDayCareProcedure(String dayCareProcedure) {
		this.dayCareProcedure = dayCareProcedure;
	}

	public SelectValue getConsiderForDayCare() {
		
	return considerForDayCare;
	}

	public void setConsiderForDayCare(SelectValue considerForDayCare) {
		this.considerForDayCare = considerForDayCare;
		
		this.considerForDayFlag = this.considerForDayCare!= null && this.considerForDayCare.getValue().equals("Yes") ? "Y" : "N";
	}

	public SelectValue getSublimitApplicable() {
		return sublimitApplicable;
	}

	public void setSublimitApplicable(SelectValue sublimitApplicable) {
		this.sublimitApplicable = sublimitApplicable;
		
		this.sublimitApplicableFlag=this.sublimitApplicable!= null && this.sublimitApplicable.getValue().equals("Yes") ? "Y" : "N";
	}

	public SelectValue getSublimitName() {
		return sublimitName;
	}

	public void setSublimitName(SelectValue sublimitName) {
		this.sublimitName = sublimitName;
	}

	public String getSublimitDesc() {
		return sublimitDesc;
	}

	public void setSublimitDesc(String sublimitDesc) {
		this.sublimitDesc = sublimitDesc;
	}

	public String getSublimitAmount() {
		return sublimitAmount;
	}

	public void setSublimitAmount(String sublimitAmount) {
		this.sublimitAmount = sublimitAmount;
	}

	public Long getPreauthKey() {
		return preauthKey;
	}

	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}

	public SelectValue getConsiderForPayment() {
		return considerForPayment;
	}

	public void setConsiderForPayment(SelectValue considerForPayment) {
		this.considerForPayment = considerForPayment;
		
		this.considerForPaymentFlag = this.considerForPayment!= null && this.considerForPayment.getValue().equals("Yes") ? "Y" : "N";
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

	public String getConsiderForDayFlag() {
		return considerForDayFlag;
	}

	public void setConsiderForDayFlag(String considerForDayFlag) {
		this.considerForDayFlag = considerForDayFlag;
	}

	public String getSublimitApplicableFlag() {
		return sublimitApplicableFlag;
	}

	public void setSublimitApplicableFlag(String sublimitApplicableFlag) {
		this.sublimitApplicableFlag = sublimitApplicableFlag;
	}

	public String getConsiderForPaymentFlag() {
		return considerForPaymentFlag;
	}

	public void setConsiderForPaymentFlag(String considerForPaymentFlag) {
		this.considerForPaymentFlag = considerForPaymentFlag;
	}

	public String getNewProcedureFlag() {
		return newProcedureFlag;
	}

	public void setNewProcedureFlag(String newProcedureFlag) {
		this.newProcedureFlag = "1";
	}
}
