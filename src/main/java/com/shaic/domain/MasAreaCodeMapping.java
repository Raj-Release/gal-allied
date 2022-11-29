package com.shaic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.util.Date;

@Entity
@Table(name="MAS_SEC_ORG_AREA_MAPPING")
@NamedQueries({
@NamedQuery(name="MasAreaCodeMapping.findAll", query="SELECT m FROM MasAreaCodeMapping m where m.activeStatus is not null and m.activeStatus = 'Y'"),
@NamedQuery(name="MasAreaCodeMapping.findAllAsc", query="SELECT m FROM MasAreaCodeMapping m where m.activeStatus is not null and m.activeStatus = 'Y' order by m.cpuCode asc"),
@NamedQuery(name="MasAreaCodeMapping.findByAreaOrgName",query = "SELECT o FROM MasAreaCodeMapping o where o.areaCode = :orgCpuNames and o.activeStatus is not null and o.activeStatus = 'Y'"),
@NamedQuery(name="MasAreaCodeMapping.findByCpuCode",query = "SELECT o FROM MasAreaCodeMapping o where o.cpuCode = :cpuCodeValue and o.activeStatus is not null and o.activeStatus = 'Y'"),


})

public class MasAreaCodeMapping implements Serializable {

		private static final long serialVersionUID = 7217543402237269703L;

		@Id
		@Column(name="\"ORG_AREA_KEY\"")
		private Long key;
		
		@Column(name="ORG_NAME")
		private String orgCpuName;
		
		@Column(name="AREA_CODE")
		private String areaCode;
		
		@Column(name="AREA_NAME")
		private String areaName;
		
		@Column(name="ACTIVE_STATUS")
		private String activeStatus;

		@Column(name="CREATED_BY")
		private String createdBy;

		@Column(name="CREATED_DATE")
		private Date CreatedDate;
		
//		@OneToOne(fetch = FetchType.LAZY)
//		@JoinColumn(name="ORG_CODE", nullable=true)
//		private TmpCPUCode cpuCode;
		
	
		@Column(name="ORG_CODE")
		private String cpuCode;

		public Long getKey() {
			return key;
		}

		public void setKey(Long key) {
			this.key = key;
		}

		public String getOrgCpuName() {
			return orgCpuName;
		}

		public void setOrgCpuName(String orgCpuName) {
			this.orgCpuName = orgCpuName;
		}

		public String getAreaCode() {
			return areaCode;
		}

		public void setAreaCode(String areaCode) {
			this.areaCode = areaCode;
		}

		public String getAreaName() {
			return areaName;
		}

		public void setAreaName(String areaName) {
			this.areaName = areaName;
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
			return CreatedDate;
		}

		public void setCreatedDate(Date createdDate) {
			CreatedDate = createdDate;
		}

		public String getCpuCode() {
			return cpuCode;
		}

		public void setCpuCode(String cpuCode) {
			this.cpuCode = cpuCode;
		}
		
		

}
