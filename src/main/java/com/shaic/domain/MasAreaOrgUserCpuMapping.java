package com.shaic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="MAS_SEC_ORG_AREA_USER_MAPPING")
@NamedQueries({
@NamedQuery(name="MasAreaOrgUserCpuMapping.findOrgForUserId", query="SELECT c FROM MasAreaOrgUserCpuMapping c where Lower(c.userId) = Lower(:userId) and c.activeStatus = 'Y'"),
@NamedQuery(name="MasAreaOrgUserCpuMapping.findUserIdForOrg", query="SELECT c FROM MasAreaOrgUserCpuMapping c where c.cpuCode = :orgId and c.activeStatus = 'Y'"),
@NamedQuery(name="MasAreaOrgUserCpuMapping.findByOrgIdCpu", query="SELECT c FROM MasAreaOrgUserCpuMapping c where  c.cpuCode = :orgId and c.userId = :userId and c.activeStatus = 'Y'"),
@NamedQuery(name="MasAreaOrgUserCpuMapping.findOrgCPUbyAreaCode", query="SELECT c FROM MasAreaOrgUserCpuMapping c where  c.areaCode = :areaCodeValue and c.userId = :orgUserId"),

})
public class MasAreaOrgUserCpuMapping {


	private static final long serialVersionUID = 7217543402237269703L;

	@Id
	@SequenceGenerator(name="MAS_SEC_ORG_AREA_USER_MAPPING_KEY_GENERATOR", sequenceName = "SEQ_ORG_AREA_USER_KEY" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MAS_SEC_ORG_AREA_USER_MAPPING_KEY_GENERATOR" )
	@Column(name="ORG_AREA_USER_KEY")
	private Long key;
	
//	@Column(name="ORG_AREA_KEY")
//	private Long areaKey;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ORG_AREA_KEY", nullable=true)
	private MasAreaCodeMapping masAreaMapping;
	
	@Column(name = "USER_ID")
	private String userId;
	
	@Column(name="AREA_CODE")
	private String areaCode;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="ORG_CODE")
	private String cpuCode;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public MasAreaCodeMapping getMasAreaMapping() {
		return masAreaMapping;
	}

	public void setMasAreaMapping(MasAreaCodeMapping masAreaMapping) {
		this.masAreaMapping = masAreaMapping;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
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

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	
}
