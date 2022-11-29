package com.shaic.domain;

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
import javax.persistence.Transient;

	/**
	 * Entity implementation class for Entity: MasUser
	 *
	 */
	@Entity
	@Table(name="MAS_CLAIM_AUDIT_USER_MAPPING")
	@NamedQueries({
		@NamedQuery(name="MasClmAuditUserMapping.findAll", query="SELECT m FROM MasClmAuditUserMapping m where m.activeStatus is not null and m.activeStatus = '1' order by m.userName asc "),
		@NamedQuery(name="MasClmAuditUserMapping.findByRoleClmProcessTypeUserId", query="SELECT m FROM MasClmAuditUserMapping m WHERE Lower(m.userRole) = :roleName and Lower(m.clmProcessType) = :clmProcessType and Lower(m.clmType) = :clmType and m.userId = :userId and m.activeStatus is not null and m.activeStatus = '1'"),
		@NamedQuery(name="MasClmAuditUserMapping.findClsQryRplUserByRoleClmProcessType", query="SELECT m FROM MasClmAuditUserMapping m WHERE Lower(m.userRole) = :roleName and Lower(m.clmProcessType) = :clmProcessType and Lower(m.clmType) = :clmType and m.activeStatus is not null and m.activeStatus = '1'"),
		@NamedQuery(name="MasClmAuditUserMapping.findQryRplUserIdByRoleClmProcessType", query="SELECT m FROM MasClmAuditUserMapping m WHERE Lower(m.userRole) = :roleName and Lower(m.clmProcessType) = :clmProcessType and (Lower(m.clmType) = :clmType1 or Lower(m.clmType) = :clmType2) and m.activeStatus is not null and m.activeStatus = '1'"),
		@NamedQuery(name="MasClmAuditUserMapping.findQryRplUserCPUByRoleClmProcessTypeUserID", query="SELECT m FROM MasClmAuditUserMapping m WHERE Lower(m.userRole) = :roleName and Lower(m.clmProcessType) = :clmProcessType and (Lower(m.clmType) = :clmType1 or Lower(m.clmType) = :clmType2) and m.userId = :userId and m.activeStatus is not null and m.activeStatus = '1'"),
		@NamedQuery(name="MasClmAuditUserMapping.findFaRoleByClmProcessType", query="SELECT m FROM MasClmAuditUserMapping m WHERE Lower(m.clmProcessType) = :clmProcessType and (Lower(m.clmType) = :clmType1 or Lower(m.clmType) = :clmType2) and Lower(m.userId) = :userId and m.activeStatus is not null and m.activeStatus = '1'"), 
		@NamedQuery(name="MasClmAuditUserMapping.findByUserIdClmTypeClmProcessType", query="SELECT m FROM MasClmAuditUserMapping m WHERE Lower(m.clmType) = :clmType and Lower(m.clmProcessType) = :clmProcessType and Lower(m.userId) = :userId and m.activeStatus is not null and m.activeStatus = '1'"),
		@NamedQuery(name="MasClmAuditUserMapping.findByEmpId", query="SELECT m FROM MasClmAuditUserMapping m WHERE Lower(m.userId) = :userId and m.activeStatus is not null and m.activeStatus = '1'")
		
/*		@NamedQuery(name="MasClmAuditUserMapping.getEmpByName", query="SELECT m FROM MasUser m WHERE m.userName LIKE :userName and m.activeStatus is not null and m.activeStatus <> '0'"),
		@NamedQuery(name="MasClmAuditUserMapping.getEmpById", query="SELECT m FROM MasUser m WHERE m.userId = :userId and m.activeStatus is not null and m.activeStatus <> '0'"),
		@NamedQuery(name="MasClmAuditUserMapping.getById", query="SELECT m FROM MasUser m WHERE Lower(m.userId) = :userId and m.activeStatus is not null and m.activeStatus <> '0'"),
		@NamedQuery(name="MasClmAuditUserMapping.getByKey", query="SELECT m FROM MasUser m WHERE m.key = :userId and m.activeStatus is not null and m.activeStatus <> '0'"),
		@NamedQuery(name="MasClmAuditUserMapping.getByPedReviewer", query="SELECT m FROM MasUser m WHERE Lower(m.userId) = :userId and m.pedReviewer = 'Y' and m.activeStatus is not null and m.activeStatus <> '0'"),
		@NamedQuery(name="MasClmAuditUserMapping.getByType", query="SELECT m FROM MasUser m WHERE m.userId = :userId and m.activeStatus is not null and m.activeStatus <> '0'"),
		@NamedQuery(name="MasClmAuditUserMapping.getEmpDetailsById",query = "SELECT m FROM MasUser m WHERE Lower(m.empId) =:empId and m.activeStatus is not null and m.activeStatus <> '0'"),
		@NamedQuery(name="MasClmAuditUserMapping.getByIdWithUserName", query="SELECT m FROM MasUser m WHERE Lower(m.userId) = :userId and m.userName is null and m.activeStatus is not null and m.activeStatus <> '0'")*/
	})

	public class MasClmAuditUserMapping implements Serializable {

		
		private static final long serialVersionUID = 1L;
		
		@Id
		@Column(name="AUDIT_USER_KEY")
		private Long key;
		
		@Column(name="USER_ID")
		private String userId;
		
		@Column(name="USER_NAME")
		private String userName;
		
		@Column(name="USER_ROLE_DESC")
		private String userRole;
		
		@Column(name="CLAIM_TYPE")
		private String clmType;

		@Column(name="PROCESS_TYPE")
		private String clmProcessType;
		
		@Column(name="CPU_CODE")
		private Long cpuCode;
				
		@Column(name="ACTIVE_STATUS")
		private String activeStatus;

		@Temporal(TemporalType.TIMESTAMP)
		@Column(name="CREATED_DATE")
		private Date createdDate;
		
		@Column(name="CREATED_BY")
		private String createdBy;
		
		@Column(name="MODIFIED_BY")
		private String modifiedBy;

		@Temporal(TemporalType.TIMESTAMP)
		@Column(name="MODIFIED_DATE")
		private Date modifiedDate;

		public MasClmAuditUserMapping() {
			super();
		}
		
		public Long getKey() {
			return key;
		}

		public Date getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}

		public void setKey(Long key) {
			this.key = key;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}
		
		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}
		
		public String getUserRole() {
			return userRole;
		}

		public void setUserRole(String userRole) {
			this.userRole = userRole;
		}

		public String getClmType() {
			return clmType;
		}

		public void setClmType(String clmType) {
			this.clmType = clmType;
		}

		public Long getCpuCode() {
			return cpuCode;
		}

		public void setCpuCode(Long cpuCode) {
			this.cpuCode = cpuCode;
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
