package com.shaic.arch.table;

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

import com.shaic.arch.fields.dto.AbstractEntity;

/**
 * @author GokulPrasath.A
 *
 */
@Entity
@Table(name = "MAS_CVC_CATEGORY")
@NamedQueries({
@NamedQuery(name = "AuditMasCategory.findAll", query = "SELECT i FROM AuditMasCategory i where i.activeStatus = :activeStatus"),
@NamedQuery(name = "AuditMasCategory.findByKey", query="SELECT i FROM AuditMasCategory i where i.key = :key"),
})
public class AuditMasCategory extends AbstractEntity{
	@Id
//	@SequenceGenerator(name="IMS_AUDIT_MAS_CATEGORY_KEY_GENERATOR", sequenceName = "SEQ_CVC_AUD_KEY", allocationSize = 1)
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_AUDIT_MAS_CATEGORY_KEY_GENERATOR" ) 
	@Column(name="CVC_CATEGORY_KEY", updatable=false)
	private Long key;
	
	@Column(name="CATEGORY")
	private String category;
	
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

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	
}
