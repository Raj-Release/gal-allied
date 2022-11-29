package com.shaic.claim.preauth.wizard.dto;

import java.util.Date;

import javax.persistence.Column;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractRowEnablerDTO;

public class TreatingDoctorDTO extends AbstractRowEnablerDTO{
	
	private Long key;
	
	private Long claimKey;
	
	private Long transactionKey;
	
	private String treatingDoctorName;
	
	private String qualification; 
	
	private Long activeStatus;
	
	private String createdBy;
    
    private Date createDate;
    
    private String modifiedBy;
    
    private Date modifiedDate;
    
    private int serialNo;
    
	private String oldDoctorName;
	
	private String oldQualification;
	
	private String dcDoctorFlag;
	
	private SelectValue treatingDoctorSignature;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Long getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}

	public String getTreatingDoctorName() {
		return treatingDoctorName;
	}

	public void setTreatingDoctorName(String treatingDoctorName) {
		this.treatingDoctorName = treatingDoctorName;
	}


	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
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

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getOldDoctorName() {
		return oldDoctorName;
	}

	public void setOldDoctorName(String oldDoctorName) {
		this.oldDoctorName = oldDoctorName;
	}

	public String getOldQualification() {
		return oldQualification;
	}

	public void setOldQualification(String oldQualification) {
		this.oldQualification = oldQualification;
	}

	public String getDcDoctorFlag() {
		return dcDoctorFlag;
	}

	public void setDcDoctorFlag(String dcDoctorFlag) {
		this.dcDoctorFlag = dcDoctorFlag;
	}

	public SelectValue getTreatingDoctorSignature() {
		return treatingDoctorSignature;
	}

	public void setTreatingDoctorSignature(SelectValue treatingDoctorSignature) {
		this.treatingDoctorSignature = treatingDoctorSignature;
	}
	
}
