package com.shaic.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="MAS_CLAIMS_PORTAL")
@NamedQueries({
	@NamedQuery(name="MasClaimPortal.findByApplicationName", query="SELECT m FROM MasClaimPortal m where UPPER(m.portal) = :portal and m.activeStatus is not null and m.activeStatus = 1")
})
public  class MasClaimPortal  extends AbstractEntity {
	
	@Id
	@Column(name="PORTAL_KEY")
	private Long key;
	
	@Column(name="PORTAL")
	private String portal;
	
	@Column(name="SOURCE_NAME")
	private String sourceName;
	
	@Column(name="ACTIVE_STATUS")
	private Integer activeStatus;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getPortal() {
		return portal;
	}

	public void setPortal(String portal) {
		this.portal = portal;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}
	

}
