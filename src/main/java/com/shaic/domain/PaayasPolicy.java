package com.shaic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "IMS_CLS_PAAYAS_POLICY")
@NamedQueries({
		@NamedQuery(name = "PaayasPolicy.findAll", query = "SELECT i FROM PaayasPolicy i"),
		@NamedQuery(name = "PaayasPolicy.findByPolicyNumber", query = "SELECT i FROM PaayasPolicy i where i.policyNumber = :policyNumber")
})
public class PaayasPolicy implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name="ACTIVE_FLAG")
	private String activeFlag;
	
	@Column(name="CPU_CODE")
	private String cpuCode;

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}

	
}
