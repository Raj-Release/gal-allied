package com.shaic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="MAS_SEC_ORG_CPU_MAPPING")
@NamedQueries({
@NamedQuery(name="OrgToCPUMapping.findCPUMapForOrgId", query="SELECT c FROM OrgToCPUMapping c where Lower(c.orgId) = Lower(:orgId) and c.activeStatus = 'Y'"),
@NamedQuery(name="OrgToCPUMapping.findEMPForCPU", query="SELECT c FROM OrgToCPUMapping c where c.cpuCode = :cpuCode and c.activeStatus = 'Y'")
})
//@NamedQuery(name="OrgToCPUMapping.findCPUByOrgUserId", query="SELECT c FROM OrgToCPUMapping c where Lower(c.userOrg.userId) = Lower(:userId) and c.activeStatus = 'Y'")})
public class OrgToCPUMapping {
	@Id
	@Column(name="ORGCPU_KEY")
	private Long orgCpukey;
	
	
	@Column(name = "ORG_ID")
	private String orgId ;
	
	@Column(name="CPU_CODE")
	private String cpuCode; 
	
	@Column(name="CPU_DESC")
	private String cpuName; 
			
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;  
	
	@Column(name="MODIFIED_BY")
	private String modified;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;

	public Long getOrgCpukey() {
		return orgCpukey;
	}

	public void setOrgCpukey(Long orgCpukey) {
		this.orgCpukey = orgCpukey;
	}

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getCpuName() {
		return cpuName;
	}

	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
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

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
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

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}	

}
