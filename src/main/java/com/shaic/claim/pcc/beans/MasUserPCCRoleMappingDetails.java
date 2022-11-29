package com.shaic.claim.pcc.beans;

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

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.DocAcknowledgement;

@Entity
@Table(name = "MAS_SEC_USER_PCC_ROLE_MAP_DTLS")
@NamedQueries({
	@NamedQuery(name = "MasUserPCCRoleMappingDetails.findByKey", query = "SELECT r FROM MasUserPCCRoleMappingDetails r where r.key = :key"),
	@NamedQuery(name ="MasUserPCCRoleMappingDetails.findByUserRoleKey",query="SELECT r FROM MasUserPCCRoleMappingDetails r WHERE r.userPCCRoleKey.userID = :userID and r.userPCCRoleKey.roleCode = :roleCode and r.activeStatus = 1"),
	@NamedQuery(name ="MasUserPCCRoleMappingDetails.findCPUCodeByUserRoleKey",query="SELECT r.cpuCode FROM MasUserPCCRoleMappingDetails r WHERE r.userPCCRoleKey.userID = :userID and r.userPCCRoleKey.roleCode = :roleCode and r.activeStatus = 1"),
	@NamedQuery(name ="MasUserPCCRoleMappingDetails.findUserIDsByRoleKey",query="SELECT r.userPCCRoleKey.userID FROM MasUserPCCRoleMappingDetails r WHERE  r.userPCCRoleKey.roleCode = :roleCode and r.cpuCode = :cpuCode  and r.activeStatus = 1")

})

public class MasUserPCCRoleMappingDetails extends AbstractEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5180004601742244410L;

	@Id
	@Column(name = "USER_PCC_ROLE_DTLS_KEY")
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_PCC_ROLE_KEY", nullable = false,updatable=false)
	private MasUserPCCRoleMapping userPCCRoleKey;
	
	@Column(name = "CPU_CODE")
	private Long cpuCode;

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

	public Long getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}

	public MasUserPCCRoleMapping getUserPCCRoleKey() {
		return userPCCRoleKey;
	}

	public void setUserPCCRoleKey(MasUserPCCRoleMapping userPCCRoleKey) {
		this.userPCCRoleKey = userPCCRoleKey;
	}
	
	
	
	


}
