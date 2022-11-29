package com.shaic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(name="MAS_SEC_ORG_CPU_MAPPING")
@NamedQueries({
	@NamedQuery(name="MasOrgCpuMapping.findCPUForUserId", query="SELECT c FROM MasOrgCpuMapping c where c.orgId = :orgId and c.activeStatus <> 'N'"),
})
public class MasOrgCpuMapping implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="\"ORGCPU_KEY\"")
	private Long key;
	
	@Column(name="ORG_ID")
	private String orgId;
	
	@Column(name="CPU_CODE")
	private String cpuCode;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}
	
}
