package com.shaic.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MAS_MASTERS_VALUE_T database table.
 * 
 */
@Entity
@Table(name="MAS_OMP_DOC_FILE_CAT")
@NamedQueries( {
@NamedQuery(name="OMPDocumentMaster.findAll", query="SELECT m FROM OMPDocumentMaster m"),
@NamedQuery(name = "OMPDocumentMaster.findByKey",query = "select o from OMPDocumentMaster o where o.key = :parentKey"),
@NamedQuery(name = "OMPDocumentMaster.findByDescription",query = "select o from OMPDocumentMaster o where Lower(o.ompDcfDescription) = :description"),
@NamedQuery(name = "OMPDocumentMaster.findByType",query = "select o from OMPDocumentMaster o where o.ompDcfType = :type"),

})
public class OMPDocumentMaster implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5709582455020907914L;


	@Id
	@Column(name="OMP_DCF_KEY")
	private Long key;


	@Column(name="ACTIVE_STATUS")
	private Integer activeStatus;

	@Column(name="OMP_DCF_TYPE")
	private String ompDcfType;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	@Column(name="OMP_DCF_DESCRIPTION")
	private String ompDcfDescription;


	public OMPDocumentMaster() {
	}

	public Integer getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}


	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	

	 @Override
	    public boolean equals(Object obj) {
	        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
	            return false;
	        } else if (getKey() == null) {
	            return obj == this;
	        } else {
	            return getKey().equals(((OMPDocumentMaster) obj).getKey());
	        }
	    }

	    @Override
	    public int hashCode() {
	        if (key != null) {
	            return key.hashCode();
	        } else {
	            return super.hashCode();
	        }
	    }

		public String getOmpDcfDescription() {
			return ompDcfDescription;
		}

		public void setOmpDcfDescription(String ompDcfDescription) {
			this.ompDcfDescription = ompDcfDescription;
		}

		public String getOmpDcfType() {
			return ompDcfType;
		}

		public void setOmpDcfType(String ompDcfType) {
			this.ompDcfType = ompDcfType;
		}

}
