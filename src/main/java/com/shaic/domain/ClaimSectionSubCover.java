package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author NEWUSER
 *
 */
@Entity
@Table(name= "MAS_CLAIM_SECTION_SUB_COVER")
@NamedQueries({
	@NamedQuery(name = "ClaimSectionSubCover.findAll", query = "SELECT cssc FROM ClaimSectionSubCover cssc"),
	@NamedQuery(name = "ClaimSectionSubCover.findBySecKey", query = "SELECT cssc FROM ClaimSectionSubCover cssc where cssc.sectionKey=:sectionKey"),
	@NamedQuery(name = "ClaimSectionSubCover.findByCoverKey", query = "SELECT cssc FROM ClaimSectionSubCover cssc where cssc.coverKey=:coverKey"),
	@NamedQuery(name = "ClaimSectionSubCover.findByCoverKeyAndSubCoverCode", query = "SELECT cssc FROM ClaimSectionSubCover cssc where cssc.coverKey=:coverKey and cssc.subCoverCode=:subCoverCode")
	})
public class ClaimSectionSubCover implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4051797884131311957L;
	
	@Id
	@Column(name = "SUB_COVER_KEY")
	private Long subCoverkey;
	
	@Column(name = "COVER_KEY")
	private Long coverKey;
	
	@Column(name = "SECTION_KEY")
	private Long sectionKey;
	
	@Column(name = "SUB_COVER_VALUE")
	private String subCoverValue;
	
	@Column(name = "ACTIVE_STATUS")
	private Boolean activeStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "SUB_COVER_CODE")
	private String subCoverCode;

	public Long getSubCoverkey() {
		return subCoverkey;
	}

	public void setSubCoverkey(Long subCoverkey) {
		this.subCoverkey = subCoverkey;
	}

	public Long getCoverKey() {
		return coverKey;
	}

	public void setCoverKey(Long coverKey) {
		this.coverKey = coverKey;
	}

	public Long getSectionKey() {
		return sectionKey;
	}

	public void setSectionKey(Long sectionKey) {
		this.sectionKey = sectionKey;
	}

	public String getSubCoverValue() {
		return subCoverValue;
	}

	public void setSubCoverValue(String subCoverValue) {
		this.subCoverValue = subCoverValue;
	}

	public Boolean getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Boolean activeStatus) {
		this.activeStatus = activeStatus;
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

	public String getSubCoverCode() {
		return subCoverCode;
	}

	public void setSubCoverCode(String subCoverCode) {
		this.subCoverCode = subCoverCode;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getSubCoverkey() == null) {
            return obj == this;
        } else {
            return getSubCoverkey().equals(((ClaimSectionSubCover) obj).getSubCoverkey());
        }
    }

    @Override
    public int hashCode() {
        if (subCoverkey != null) {
            return subCoverkey.hashCode();
        } else {
            return super.hashCode();
        }
    }
}
