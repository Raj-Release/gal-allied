package com.shaic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="MAS_SEC_ROLE_LIMIT")
@NamedQueries({
	@NamedQuery(name="MasRoleLimit.getAmtByRole", query="SELECT m FROM MasRoleLimit m WHERE Lower(m.roleId) = :role and m.activeStatus is not null and m.activeStatus <> 'N'"),
	@NamedQuery(name="MasRoleLimit.getRoleTypeByCategory",query="SELECT m.roleId,m.maxAmt FROM MasRoleLimit m WHERE m.roleType=:roleType"),
	@NamedQuery(name="MasRoleLimit.findByroleId",query="SELECT m FROM MasRoleLimit m WHERE m.roleId =:roleId")
})
public class MasRoleLimit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="\"ROLE_KEY\"")
	private Long key;
	
	@Column(name="ROLE_ID")
	private String roleId;
	
	@Column(name="ROLE_DESC")
	private String roleDesc;
	
	@Column(name="MIN_AMOUNT")
	private Long minAmt;
	
	@Column(name="MAX_AMOUNT")
	private Long maxAmt;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	@Column(name="ROLE_TYPE")
	private String roleType;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public Long getMinAmt() {
		return minAmt;
	}

	public void setMinAmt(Long minAmt) {
		this.minAmt = minAmt;
	}

	public Long getMaxAmt() {
		return maxAmt;
	}

	public void setMaxAmt(Long maxAmt) {
		this.maxAmt = maxAmt;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	
	
}
