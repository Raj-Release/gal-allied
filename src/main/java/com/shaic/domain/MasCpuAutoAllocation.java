package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "MAS_CPU_AUTO_ALLOCATION")
@NamedQueries({
		@NamedQuery(name = "MasCpuAutoAllocation.findByCode", query = "SELECT m FROM MasCpuAutoAllocation m where m.cpucode = :cpuCode and m.activeStatus is not null and m.activeStatus <> 'N' "),
		@NamedQuery(name = "MasCpuAutoAllocation.findByName", query = "SELECT m FROM MasCpuAutoAllocation m where Lower(m.cpuName) LIKE :cpuName and m.activeStatus is not null and m.activeStatus <> 'N' ") })
public class MasCpuAutoAllocation implements Serializable {

	@Id
	@Column(name = "\"AUTO_CPU_KEY\"")
	private Long cpuKey;

	@Column(name = "AUTO_CPU_CODE")
	private Long cpucode;

	@Column(name = "AUTO_CPU_NAME")
	private String cpuName;

	@Column(name = "MIN_AMOUNT")
	private Double minAmount;

	@Column(name = "MAX_AMOUNT")
	private Double maxAmount;

	@Column(name = "AUTO_ALLOC_WITH_LIMIT")
	private String withinLimit;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROCESS_ABV_LIMIT", nullable = true)
	private MastersValue processCases;

	@Column(name = "AUTO_ALLOC_ABV_LIMIT")
	private String aboveLimit;

	@Column(name = "PROCESSED_CORP_OFFICE")
	private String processCorp;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	@Column(name = "ACTIVE_STATUS")
	private String activeStatus;

	public Long getCpuKey() {
		return cpuKey;
	}

	public void setCpuKey(Long cpuKey) {
		this.cpuKey = cpuKey;
	}

	public Long getCpucode() {
		return cpucode;
	}

	public void setCpucode(Long cpucode) {
		this.cpucode = cpucode;
	}

	public String getCpuName() {
		return cpuName;
	}

	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}

	public Double getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(Double minAmount) {
		this.minAmount = minAmount;
	}

	public Double getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(Double maxAmount) {
		this.maxAmount = maxAmount;
	}

	public String getWithinLimit() {
		return withinLimit;
	}

	public void setWithinLimit(String withinLimit) {
		this.withinLimit = withinLimit;
	}

	public String getAboveLimit() {
		return aboveLimit;
	}

	public void setAboveLimit(String aboveLimit) {
		this.aboveLimit = aboveLimit;
	}

	public String getProcessCorp() {
		return processCorp;
	}

	public void setProcessCorp(String processCorp) {
		this.processCorp = processCorp;
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

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public MastersValue getProcessCases() {
		return processCases;
	}

	public void setProcessCases(MastersValue processCases) {
		this.processCases = processCases;
	}

}
