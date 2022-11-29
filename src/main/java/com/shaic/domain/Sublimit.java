package com.shaic.domain;

import java.io.Serializable;
import java.sql.Timestamp;
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
@Table(name="MAS_SUBLIMIT")
@NamedQueries( {
@NamedQuery(name="Sublimit.findAll", query="SELECT s FROM Sublimit s"),
@NamedQuery(name = "Sublimit.findBySublimitName",query = "select s from Sublimit s where Lower(s.sublimitDesc) like :sublimitName and s.activeStatus is not null and s.activeStatus = 1"),
@NamedQuery(name = "Sublimit.findByKey",query = "select s from Sublimit s where s.key = :parentKey")
})
public class Sublimit  implements Serializable {
	
		@Id
		@Column(name="SUBLIMIT_KEY")
		private Long key;

		@Column(name="ACTIVE_STATUS")
		private Integer activeStatus;

		@Column(name="SUBLIMIT_DESC")
		private String sublimitDesc;
		
		@Column(name="CREATED_BY")
		private String createdBy;

		@Temporal(TemporalType.TIMESTAMP)
		@Column(name="CREATED_DATE")
		private Date createdDate;
		
		@Column(name="MODIFIED_BY")
		private String modifiedBy;

		@Temporal(TemporalType.TIMESTAMP)
		@Column(name="MODIFIED_DATE")
		private Date modifiedDate;

		public Long getKey() {
			return key;
		}

		public void setKey(Long key) {
			this.key = key;
		}

		public Integer getActiveStatus() {
			return activeStatus;
		}

		public void setActiveStatus(Integer activeStatus) {
			this.activeStatus = activeStatus;
		}

		public String getSublimitDesc() {
			return sublimitDesc;
		}

		public void setSublimitDesc(String sublimitDesc) {
			this.sublimitDesc = sublimitDesc;
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
