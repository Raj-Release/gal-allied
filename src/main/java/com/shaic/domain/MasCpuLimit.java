package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MAS_COUNTRY_T database table.
 * 
 */
@Entity
@Table(name="MAS_CPU_LIMIT")
@NamedQueries({
@NamedQuery(name="MasCpuLimit.findByCode",query = "SELECT o FROM MasCpuLimit o where o.cpuCode = :cpuCode and o.polType = :polType and o.activeStatus = 1")
})
public class MasCpuLimit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7217543402237269703L;

	@Id
	@Column(name="\"CPU_LIMIT_KEY\"")
	private Long key;
	
	@Column(name="CPU_CODE")
	private Long cpuCode;
	
	@Column(name="CPU_LIMIT")
	private Long cpuLimit;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="PROCESSING_CPU")
	private Long processCpuCode;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	@Column(name="POLICY_TYPE")
	private String polType;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Date CreatedDate;
	
	@Column(name="SETTLEMENT_CPU_CODE")
	private Long settlementCpuCode;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}

	public Long getCpuLimit() {
		return cpuLimit;
	}

	public void setCpuLimit(Long cpuLimit) {
		this.cpuLimit = cpuLimit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getProcessCpuCode() {
		return processCpuCode;
	}

	public void setProcessCpuCode(Long processCpuCode) {
		this.processCpuCode = processCpuCode;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getPolType() {
		return polType;
	}

	public void setPolType(String polType) {
		this.polType = polType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return CreatedDate;
	}

	public void setCreatedDate(Date createdDate) {
		CreatedDate = createdDate;
	}

	public Long getSettlementCpuCode() {
		return settlementCpuCode;
	}

	public void setSettlementCpuCode(Long settlementCpuCode) {
		this.settlementCpuCode = settlementCpuCode;
	}
	

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Date activeStatusDate;

	
	
}