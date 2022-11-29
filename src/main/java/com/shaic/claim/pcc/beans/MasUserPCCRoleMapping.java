package com.shaic.claim.pcc.beans;

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
@Table(name = "MAS_SEC_USER_PCC_ROLE_MAPPING")
@NamedQueries({
	@NamedQuery(name = "MasUserPCCRoleMapping.findByKey", query = "SELECT i FROM MasUserPCCRoleMapping i where i.key = :key"),
	@NamedQuery(name ="MasUserPCCRoleMapping.findByRole",query="SELECT r FROM MasUserPCCRoleMapping r WHERE r.roleCode = :roleCode and r.activeStatus = 1"),
	@NamedQuery(name ="MasUserPCCRoleMapping.findUserIDsByRole",query="SELECT r.userID FROM MasUserPCCRoleMapping r WHERE r.roleCode = :roleCode and r.activeStatus = 1"),
	@NamedQuery(name ="MasUserPCCRoleMapping.findUserByRoleAndUserID",query="SELECT r.userID FROM MasUserPCCRoleMapping r WHERE r.roleCode = :roleCode and r.userID = :userID and r.activeStatus = 1"),

})
public class MasUserPCCRoleMapping extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5180004601742244410L;

	@Id
	@Column(name = "USER_PCC_ROLE_KEY")
	private Long key;
	
	@Column(name = "USER_ID")
	private String userID;
	
	@Column(name = "ROLE_CODE")
	private String roleCode;

	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
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
	
}
