package com.shaic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "IMS_CLS_KOTAK_POLICY")
@NamedQueries({
		@NamedQuery(name = "StarKotakPolicy.findAll", query = "SELECT i FROM StarKotakPolicy i"),
		@NamedQuery(name = "StarKotakPolicy.findByPolicyNumber", query = "SELECT i FROM StarKotakPolicy i where i.policyNumber = :policyNumber")
})
public class StarKotakPolicy implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name="CPU_CODE")
	private String cpuCode;
	
	@Column(name="ACTIVE_FLAG")
	private String activeFlag;
	
	@Column(name="POLICY_KEY")
	private Long policyKey;

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Long getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}
}
