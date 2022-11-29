package com.shaic.claim.preauth.wizard.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractRowEnablerDTO;
import com.shaic.domain.preauth.ProcedureSpecialityMaster;
import com.shaic.domain.preauth.SpecialityType;

@SuppressWarnings("serial")
public class SpecialityDTO extends AbstractRowEnablerDTO {
	
	private Long preAuthKey;
	
	private Boolean enableOrDisable;
	
    private String createdBy;
    
    private Date createDate;
    
    private String presenterString;
    
    private String procedureValue;
    
    private Long addprocedureId;
    
    private int serialNo;

	@NotNull
	private SelectValue specialityType;
	
//	@NotNull
	private SelectValue procedure;
	
	private SelectValue oldspecialityType;

	private String splFlag;
	
	private SelectValue oldProcedure;
	
	private String oldRemarks;
	
	public SpecialityDTO() {
		this.specialityType = new SelectValue();
		this.procedure = new SelectValue();
	}
	
    public SpecialityDTO(String presenterString) {
    	this.presenterString = presenterString;
    }

	//	@NotNull(message="Remarks can't be blank")
//	@Size(min = 1, message = "Please Enter Remarks")
	private String remarks;


	public SelectValue getSpecialityType() {
		return specialityType;
	}

	public void setSpecialityType(SelectValue specialityType) {
		this.specialityType = specialityType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	

	public Long getPreAuthKey() {
		return preAuthKey;
	}

	public void setPreAuthKey(Long preAuthKey) {
		this.preAuthKey = preAuthKey;
	}

	public Boolean getEnableOrDisable() {
		return enableOrDisable;
	}

	public void setEnableOrDisable(Boolean enableOrDisable) {
		this.enableOrDisable = enableOrDisable;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public SelectValue getProcedure() {
		return procedure;
	}

	public void setProcedure(SelectValue procedure) {
		this.procedure = procedure;
	}

	public String getProcedureValue() {
		return procedureValue;
	}

	public void setProcedureValue(String procedureValue) {
		this.procedureValue = procedureValue;
	}

	public Long getAddprocedureId() {
		return addprocedureId;
	}

	public void setAddprocedureId(Long addprocedureId) {
		this.addprocedureId = addprocedureId;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public SelectValue getOldspecialityType() {
		return oldspecialityType;
	}

	public void setOldspecialityType(SelectValue oldspecialityType) {
		this.oldspecialityType = oldspecialityType;
	}

	public String getPresenterString() {
		return presenterString;
	}

	public void setPresenterString(String presenterString) {
		this.presenterString = presenterString;
	}

	public String getSplFlag() {
		return splFlag;
	}

	public void setSplFlag(String splFlag) {
		this.splFlag = splFlag;
	}

	public SelectValue getOldProcedure() {
		return oldProcedure;
	}

	public void setOldProcedure(SelectValue oldProcedure) {
		this.oldProcedure = oldProcedure;
	}

	public String getOldRemarks() {
		return oldRemarks;
	}

	public void setOldRemarks(String oldRemarks) {
		this.oldRemarks = oldRemarks;
	}

}
