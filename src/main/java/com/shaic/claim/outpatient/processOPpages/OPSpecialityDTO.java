/**
 * 
 */
package com.shaic.claim.outpatient.processOPpages;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;

/**
 * @author ntv.narasimhaj
 *
 */
public class OPSpecialityDTO {
	
	private Long sNo;
		
	private Long preAuthKey;
	
	private Boolean enableOrDisable;
	
    private String createdBy;
    
    private Date createDate;
    
    private String presenterString;
    
    private String procedureValue;
    
    private Long addprocedureId;
    
    private int serialNo;
    
    private String remarks;
    
    private String pedfromPolicy;

	@NotNull
	private SelectValue specialityType;
	
	private SelectValue ped;

	public Long getsNo() {
		return sNo;
	}

	public void setsNo(Long sNo) {
		this.sNo = sNo;
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

	public String getPresenterString() {
		return presenterString;
	}

	public void setPresenterString(String presenterString) {
		this.presenterString = presenterString;
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

	public SelectValue getSpecialityType() {
		return specialityType;
	}

	public void setSpecialityType(SelectValue specialityType) {
		this.specialityType = specialityType;
	}

	public SelectValue getPed() {
		return ped;
	}

	public void setPed(SelectValue ped) {
		this.ped = ped;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPedfromPolicy() {
		return pedfromPolicy;
	}

	public void setPedfromPolicy(String pedfromPolicy) {
		this.pedfromPolicy = pedfromPolicy;
	}

}
