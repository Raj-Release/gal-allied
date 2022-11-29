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
@Table(name="MAS_WRH_SHELF")
@NamedQueries({
	@NamedQuery(name="MasShelf.findByShelfKey", query="SELECT m FROM MasShelf m where m.key = :shelfId and m.activateStatus is not null and m.activateStatus = 'Y'"),
	@NamedQuery(name="MasShelf.findByShelfDesc", query="SELECT m FROM MasShelf m where m.shelfDesc = :shelfName and m.activateStatus is not null and m.activateStatus = 'Y'")
})
public class MasShelf extends AbstractEntity {
	
	@Id
	@Column(name="SHELF_KEY")
	private Long key;
	
	@Column(name="SHELF_DESC")
	private String shelfDesc;
	
	@Column(name="RACK_KEY")
	private Long rackKey;
	
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
	
	public String getShelfDesc() {
		return shelfDesc;
	}

	public void setShelfDesc(String shelfDesc) {
		this.shelfDesc = shelfDesc;
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
	
	public Long getRackKey() {
		return rackKey;
	}

	public void setRackKey(Long rackKey) {
		this.rackKey = rackKey;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

}
