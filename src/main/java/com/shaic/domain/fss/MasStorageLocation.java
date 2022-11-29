package com.shaic.domain.fss;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;


@Entity
@Table(name="MAS_WRH_STORAGE_LOCATION")
@NamedQueries({
	@NamedQuery(name="MasStorageLocation.findByLocationKey", query="SELECT m FROM MasStorageLocation m where m.key = :locationId and m.activateStatus is not null and m.activateStatus = 'Y'"),
	@NamedQuery(name="MasStorageLocation.findByLocationByDesc", query="SELECT m FROM MasStorageLocation m where m.storageDesc = :storageDesc and m.activateStatus is not null and m.activateStatus = 'Y'")
	//@NamedQuery(name="MasStorageLocation.findByLocationByCPU", query="Select o FROM MasStorageLocation o where o.cpu in(:cpuList) and o.activateStatus is not null and o.activateStatus = 'Y'")
	})

public class MasStorageLocation extends AbstractEntity {
	
	@Id
	@Column(name="STORAGE_KEY")
	private Long key;
	
	@Column(name="STORAGE_DESC")
	private String storageDesc;
	
	@Column(name="CPU_CODE")
	private Long cpu;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE", insertable = false, updatable=false)
	private Date createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="ACTIVE_STATUS")
	private String activateStatus;
	

	public String getStorageDesc() {
		return storageDesc;
	}

	public void setStorageDesc(String storageDesc) {
		this.storageDesc = storageDesc;
	}

	public Long getCpu() {
		return cpu;
	}

	public void setCpu(Long cpu) {
		this.cpu = cpu;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public String getActivateStatus() {
		return activateStatus;
	}

	public void setActivateStatus(String activateStatus) {
		this.activateStatus = activateStatus;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	

}
