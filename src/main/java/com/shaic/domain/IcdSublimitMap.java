package com.shaic.domain;

import java.sql.Timestamp;
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
import javax.persistence.Transient;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.preauth.CriticalIllnessMaster;
import com.shaic.domain.preauth.IcdBlock;
import com.shaic.domain.preauth.IcdChapter;
import com.shaic.domain.preauth.IcdCode;
import com.shaic.domain.preauth.Stage;

/**
 * 
 * @author Lakshminarayana
 * 
 * The persistent class for the IMS_CLS_ICD_Sublimit_Map database table.
 * 
 */
@Entity
@Table(name="IMS_CLS_SUBLIMIT_ICD_MAP")
@NamedQueries({
@NamedQuery(name = "IcdSublimitMap.findAll", query="SELECT ismap FROM IcdSublimitMap ismap"),
@NamedQuery(name = "IcdSublimitMap.findByIcdCodeKey",query="SELECT ismap FROM IcdSublimitMap ismap WHERE ismap.icdCode.key = :icdCodeKey and ismap.activeStatus = 1"),
@NamedQuery(name = "IcdSublimitMap.findBySublimitMapKey",query="SELECT ismap FROM IcdSublimitMap ismap WHERE ismap.key = :sublimitMapKey"),
@NamedQuery(name = "IcdSublimitMap.findBySublimitKey",query="SELECT ismap FROM IcdSublimitMap ismap WHERE ismap.sublimit.key = :sublimitKey"),
@NamedQuery(name = "IcdSublimitMap.findByIcdChapKey",query="SELECT ismap FROM IcdSublimitMap ismap WHERE ismap.icdChapter.key = :icdChapterKey"),
@NamedQuery(name = "IcdSublimitMap.findByIcdBlockKey",query="SELECT ismap FROM IcdSublimitMap ismap WHERE ismap.icdBlock.key = :icdBlockKey")
})
public class IcdSublimitMap  extends AbstractEntity {
   
		@Id
		@SequenceGenerator(name="IMS_SUBLIMIT_ICD_KEY_GENERATOR", sequenceName = "SEQ_SUBLIMIT_ICD_KEY", allocationSize = 1)
		@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_SUBLIMIT_ICD_KEY_GENERATOR" )
		@Column(name="SUBLIMIT_ICD_KEY")
		private Long key;
		
		@OneToOne(fetch = FetchType.LAZY)
		@JoinColumn(name="SUBLIMIT_KEY", nullable=false)
		private Sublimit sublimit;
		
		@OneToOne(fetch = FetchType.LAZY)
		@JoinColumn(name="ICD_CHAPTER_KEY", nullable=false)
		private IcdChapter icdChapter;
		
		@OneToOne(fetch = FetchType.LAZY)
		@JoinColumn(name="ICD_BLOCK_KEY", nullable=false)
		private IcdBlock icdBlock;
		
		@OneToOne(fetch = FetchType.LAZY)
		@JoinColumn(name="ICD_CODE_KEY", nullable=false)    
		private IcdCode icdCode;
				
		@Column(name="ICD_CODE_DESC")   
		private String icdCodeDescription;
		
		@Column(name="ICD_CODE")
		private String icdCodeValue;
				
		@Column(name="CREATED_BY")
		private String createdBy;  
				
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name="CREATED_DATE")
		private Date createdDate;
		
		@Column(name = "MODIFIED_BY")
		private String modifiedBy;
	
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name="MODIFIED_DATE")
		private Date modifiedDate;
	
		@Column(name="ACTIVE_STATUS")
		private Integer activeStatus;

		public Long getKey() {
			return key;
		}

		public void setKey(Long key) {
			this.key = key;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (this.key == null || obj == null || !(this.getClass().equals(obj.getClass()))) {
				return false;
			}
			AbstractEntity that = (AbstractEntity) obj;

			return this.key.equals(that.getKey());
		}

		@Override
		public int hashCode() {
			return key == null ? 0 : key.hashCode();
		}

		public Sublimit getSublimit() {
			return sublimit;
		}

		public void setSublimit(Sublimit sublimit) {
			this.sublimit = sublimit;
		}

		public IcdChapter getIcdChapter() {
			return icdChapter;
		}

		public void setIcdChapter(IcdChapter icdChapter) {
			this.icdChapter = icdChapter;
		}

		public IcdBlock getIcdBlock() {
			return icdBlock;
		}

		public void setIcdBlock(IcdBlock icdBlock) {
			this.icdBlock = icdBlock;
		}

		public IcdCode getIcdCode() {
			return icdCode;
		}

		public void setIcdCode(IcdCode icdCode) {
			this.icdCode = icdCode;
		}

		public String getIcdCodeDescription() {
			return icdCodeDescription;
		}

		public void setIcdCodeDescription(String icdCodeDescription) {
			this.icdCodeDescription = icdCodeDescription;
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

		public Integer getActiveStatus() {
			return activeStatus;
		}

		public void setActiveStatus(Integer activeStatus) {
			this.activeStatus = activeStatus;
		}
		
}
