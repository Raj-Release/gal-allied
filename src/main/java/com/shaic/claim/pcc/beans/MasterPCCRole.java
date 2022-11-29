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
@Table(name="MAS_SEC_PCC_ROLE")
@NamedQueries({
	@NamedQuery(name = "MasterPCCRole.findAll", query = "SELECT i FROM MasterPCCRole i where i.activeStatus = 'Y'"),
	@NamedQuery(name ="MasterPCCRole.findByCodeList",query="SELECT r FROM MasterPCCRole r WHERE r.pccRoleCode in (:pccRoleCodes) and r.activeStatus = 'Y'"),
	@NamedQuery(name ="MasterPCCRole.findByCode",query="SELECT r FROM MasterPCCRole r WHERE r.pccRoleCode = :pccRoleCode and r.activeStatus = 'Y'")
})
public class MasterPCCRole  extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5923938010594591428L;

	@Id
	@Column(name = "PCC_ROLE_KEY")
	private Long key;
	
	@Column(name = "PCC_ROLE_CODE")
	private String pccRoleCode;
	
	@Column(name = "PCC_ROLE_DESC")
	private String pccRoleDesc;

	@Column(name = "ACTIVE_FLAG")
	private String activeStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getPccRoleCode() {
		return pccRoleCode;
	}

	public void setPccRoleCode(String pccRoleCode) {
		this.pccRoleCode = pccRoleCode;
	}

	public String getPccRoleDesc() {
		return pccRoleDesc;
	}

	public void setPccRoleDesc(String pccRoleDesc) {
		this.pccRoleDesc = pccRoleDesc;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}
