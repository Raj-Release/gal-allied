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
@Table(name="MAS_WRH_RACK")
@NamedQueries({
	@NamedQuery(name="MasRack.findByRackKey", query="SELECT m FROM MasRack m where m.key = :rackId and m.activateStatus is not null and m.activateStatus = 'Y'"),
	@NamedQuery(name="MasRack.findByRackDesc", query="SELECT m FROM MasRack m where m.rackDesc = :rackDesc and m.activateStatus is not null and m.activateStatus = 'Y'")
})

public class MasRack extends AbstractEntity {
	
	@Id
	@Column(name="RACK_KEY")
	private Long key;
	
	@Column(name="RACK_DESC")
	private String rackDesc;
	
	@Column(name="STORAGE_KEY")
	private Long storageKey;
	
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

	public String getRackDesc() {
		return rackDesc;
	}

	public void setRackDesc(String rackDesc) {
		this.rackDesc = rackDesc;
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
	
	public Long getStorageKey() {
		return storageKey;
	}

	public void setStorageKey(Long storageKey) {
		this.storageKey = storageKey;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

}
