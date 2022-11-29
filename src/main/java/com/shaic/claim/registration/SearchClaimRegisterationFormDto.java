package com.shaic.claim.registration;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class SearchClaimRegisterationFormDto extends AbstractSearchDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SelectValue hospitalType;
	
	private SelectValue cpucode;
	
	private String policyNumber;
	
	private String intimationNumber;
	
	private Date intimatedDate;
	
	private String userId;
	
	private String password;
	
	private SelectValue accDeath;
	
	private String lob;
	
	private String lobType;
	

	public SelectValue getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(SelectValue hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public Date getIntimatedDate() {
		return intimatedDate;
	}

	public void setIntimatedDate(Date intimatedDate) {
		this.intimatedDate = intimatedDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public SelectValue getCpucode() {
		return cpucode;
	}

	public void setCpucode(SelectValue cpucode) {
		this.cpucode = cpucode;
	}

	public SelectValue getAccDeath() {
		return accDeath;
	}

	public void setAccDeath(SelectValue accDeath) {
		this.accDeath = accDeath;
	}

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public String getLobType() {
		return lobType;
	}

	public void setLobType(String lobType) {
		this.lobType = lobType;
	}
	
}
