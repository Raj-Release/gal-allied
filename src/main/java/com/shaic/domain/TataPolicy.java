package com.shaic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="IMS_CLS_TATA_POLICY")
@NamedQueries({
@NamedQuery(name="TataPolicy.findAll", query="SELECT t FROM TataPolicy t"),
@NamedQuery(name="TataPolicy.findByPolicyNumber", query ="SELECT t FROM TataPolicy t where t.policyNumber = :policyNumber")
})
public class TataPolicy implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name="CPU_CODE")
	private Long cpuCode;

	@Column(name="ACTIVE_FLAG")
	private Long activeFlag;
	
	@Column(name="POLICY_KEY")
	private Long policyKey;

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public Long getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}

	public Long getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}

	public Long getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Long activeFlag) {
		this.activeFlag = activeFlag;
	}
}
