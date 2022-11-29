package com.shaic.claim.reimbursement.rawanalysis;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class SearchProcessRawRequestFormDto extends AbstractSearchDTO implements Serializable{
	
	private String intimationNo;
	private String policyNo;
	private SelectValue cpuCode;
	private SelectValue intiatedFrom;
	private SelectValue claimType;
	private Date fromDate;
	private Date toDate;
	private String hospitalName;
	private Long hospitalKey;
	private String hospitalCode;
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public SelectValue getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(SelectValue cpuCode) {
		this.cpuCode = cpuCode;
	}
	public SelectValue getIntiatedFrom() {
		return intiatedFrom;
	}
	public void setIntiatedFrom(SelectValue intiatedFrom) {
		this.intiatedFrom = intiatedFrom;
	}
	public SelectValue getClaimType() {
		return claimType;
	}
	public void setClaimType(SelectValue claimType) {
		this.claimType = claimType;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public Long getHospitalKey() {
		return hospitalKey;
	}
	public void setHospitalKey(Long hospitalKey) {
		this.hospitalKey = hospitalKey;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
}
