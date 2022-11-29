package com.shaic.domain;

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
	@Table(name="Mas_SeC_vsp_User_label")
	@NamedQueries({
		@NamedQuery(name="MasSecVSPUserLabelMapping.findByUserId", query="SELECT m FROM MasSecVSPUserLabelMapping m where  Lower(m.UserId) = Lower(:userId) and m.activeStatus is not null and m.activeStatus = 'Y'"),
	@NamedQuery(name="MasSecVSPUserLabelMapping.findByUserIdAndKey", query="SELECT m FROM MasSecVSPUserLabelMapping m where m.activeStatus is not null and m.activeStatus = 'Y' and Lower(m.UserId) = Lower(:userId)")
	})	
	public class MasSecVSPUserLabelMapping {
		
		@Id
		@SequenceGenerator(name="SEQ_USER_LABEL_KEY_GENERATOR", sequenceName = "SEQ_USER_LABEL_KEY", allocationSize = 1)
		@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_USER_LABEL_KEY_GENERATOR" )
		@Column(name="USER_LABEL_KEY")
		private Long key;
		
		@Column(name="USER_ID")
		private String UserId;
		
		@Column(name="ENABLE")
		private String vspEnable;
		
		@Column(name="INCLUDE")
		private String vspInclude;
		
		@Column(name="EXCLUDE")
		private String vspExclude;
		
		@Column(name = "CREATED_BY")
		private String createdBy;

		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "CREATED_DATE")
		private Date createdDate;
		
		@Column(name = "MODIFIED_BY")
		private String modifiedBy;

		@Column(name = "MODIFIED_DATE")
		private Timestamp modifiedDate;
		
		@Column(name="ACTIVE_STATUS")
		private String activeStatus;

		public Long getKey() {
			return key;
		}

		public void setKey(Long key) {
			this.key = key;
		}

		public String getUserId() {
			return UserId;
		}

		public void setUserId(String userId) {
			UserId = userId;
		}

		public String getVspEnable() {
			return vspEnable;
		}

		public void setVspEnable(String vspEnable) {
			this.vspEnable = vspEnable;
		}

		public String getVspInclude() {
			return vspInclude;
		}

		public void setVspInclude(String vspInclude) {
			this.vspInclude = vspInclude;
		}

		public String getVspExclude() {
			return vspExclude;
		}

		public void setVspExclude(String vspExclude) {
			this.vspExclude = vspExclude;
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

		public Timestamp getModifiedDate() {
			return modifiedDate;
		}

		public void setModifiedDate(Timestamp modifiedDate) {
			this.modifiedDate = modifiedDate;
		}

		public String getActiveStatus() {
			return activeStatus;
		}

		public void setActiveStatus(String activeStatus) {
			this.activeStatus = activeStatus;
		}

		
		
		
	}