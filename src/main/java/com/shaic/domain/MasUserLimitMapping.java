package com.shaic.domain;

import java.io.Serializable;
import java.sql.Timestamp;
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
@Table(name="MAS_SEC_USER_LIMIT_MAPPING")
@NamedQueries({
	@NamedQuery(name="MasUserLimitMapping.getRoleByUser", query="SELECT m FROM MasUserLimitMapping m WHERE Lower(m.userId) = :userName and m.activeStatus is not null and m.activeStatus <> 'N' and m.claimType in (:typeList) "),
	@NamedQuery(name="MasUserLimitMapping.findByEmpId", query="SELECT m FROM MasUserLimitMapping m WHERE m.userId =:userId"),
	@NamedQuery(name="MasUserLimitMapping.findByEmpIdAndRoleId", query="SELECT m FROM MasUserLimitMapping m WHERE m.userId =:userId and m.roleId =:roleId"),
	@NamedQuery(name="MasUserLimitMapping.getActiveUserLimitByRoleId", query="SELECT m FROM MasUserLimitMapping m WHERE m.userId = :userName and m.roleId =:roleId and m.activeStatus is not null and m.activeStatus <> 'N'"),
	@NamedQuery(name="MasUserLimitMapping.findByEmpIdAndActiveStatus", query="SELECT m FROM MasUserLimitMapping m WHERE m.userId =:userId and m.activeStatus is not null and m.activeStatus <> 'N'"),
	@NamedQuery(name="MasUserLimitMapping.getActiveUserLimitByRoleIdAndActiceStatus", query="SELECT m FROM MasUserLimitMapping m WHERE m.userId = :userName and m.roleId =:roleId and m.activeStatus is not null"),
})
public class MasUserLimitMapping implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name="LIMIT_MAPPING_KEY_GENERATOR", sequenceName = "SEQ_USERROLE_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LIMIT_MAPPING_KEY_GENERATOR" ) 
	@Column(name="\"USERROLE_KEY\"")
	private Long key;
	
	@Column(name="ROLE_ID")
	private String roleId;
	
	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;

	@Column(name="CLAIM_TYPE")
	private String claimType;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
}