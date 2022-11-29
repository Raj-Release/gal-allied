package com.shaic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="MAS_SEC_OU_USER_MAPPING")
@NamedQueries({
@NamedQuery(name="UserToOrgMapping.findOrgForUserId", query="SELECT c FROM UserToOrgMapping c where Lower(c.userId) = Lower(:userId) and c.activeStatus = 'Y'"),
@NamedQuery(name="UserToOrgMapping.findUserIdForOrg", query="SELECT c FROM UserToOrgMapping c where c.orgId = :orgId and c.activeStatus = 'Y'"),
@NamedQuery(name="UserToOrgMapping.find", query="SELECT c FROM UserToOrgMapping c where c.orgId = :orgId and c.activeStatus = 'Y'"),
@NamedQuery(name="UserToOrgMapping.findByOrgIdUserId", query="SELECT c FROM UserToOrgMapping c where c.orgId = :orgId and Lower(c.userId) = Lower(:userId)"),
@NamedQuery(name="UserToOrgMapping.findByOrgIdCpu", query="SELECT c FROM UserToOrgMapping c where  lower(c.orgId) LIKE :orgId and Lower(c.userId) = Lower(:userId)"),
@NamedQuery(name="UserToOrgMapping.findByOrgIdCpuForRtl", query="SELECT c FROM UserToOrgMapping c where  lower(c.orgId) LIKE :orgId and Lower(c.userId) = Lower(:userId) and c.lobFlag=:lobFlag"),
@NamedQuery(name="UserToOrgMapping.findByOnlyUserId", query="SELECT c FROM UserToOrgMapping c where Lower(c.userId) = Lower(:userId)"),

})


public class UserToOrgMapping {
	
	@Id
	@SequenceGenerator(name="MAS_SEC_OU_USER_MAPPING_KEY_GENERATOR", sequenceName = "SEQ_ORG_KEY" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MAS_SEC_OU_USER_MAPPING_KEY_GENERATOR" )
	@Column(name = "ORG_KEY")
	private Long key;
	
	@Column(name = "ORG_ID")
	private String orgId;
	
	@Column(name = "USER_ID")
	private String userId;
			
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "ACTIVE_STATUS")
	private String activeStatus;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name ="LOB_FLAG")
	private String lobFlag;
	
	public String getLobFlag() {
		return lobFlag;
	}

	public void setLobFlag(String lobFlag) {
		this.lobFlag = lobFlag;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	
	
}
