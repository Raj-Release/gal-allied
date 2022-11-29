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
@Table(name= "MAS_CLAIM_SECTION_COVER")
@NamedQueries({
	@NamedQuery(name = "ClaimSectionCover.findAll", query = "SELECT csc FROM ClaimSectionCover csc"),
	@NamedQuery(name = "ClaimSectionCover.findByCoverKey", query = "SELECT csc FROM ClaimSectionCover csc where csc.coverKey=:coverKey"),
	@NamedQuery(name = "ClaimSectionCover.findBySectionKey", query = "SELECT csc FROM ClaimSectionCover csc where csc.sectionKey=:sectionKey"),
	@NamedQuery(name = "ClaimSectionCover.findBySectionKeyAndCoverCode", query = "SELECT csc FROM ClaimSectionCover csc where csc.sectionKey=:sectionKey and csc.coverCode=:coverCode")
	})

public class ClaimSectionCover implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "COVER_KEY")
	private Long coverKey;
	
	@Column(name = "SECTION_KEY")
	private Long sectionKey;
	
	@Column(name = "COVER_VALUE")
	private String coverValue;
	
	@Column(name = "ACTIVE_STATUS")
	private Boolean activeStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "COVER_CODE")
	private String coverCode;

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

	public String getCoverValue() {
		return coverValue;
	}

	public void setCoverValue(String coverValue) {
		this.coverValue = coverValue;
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

	public String getCoverCode() {
		return coverCode;
	}

	public void setCoverCode(String coverCode) {
		this.coverCode = coverCode;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getCoverKey() == null) {
            return obj == this;
        } else {
            return getCoverKey().equals(((ClaimSectionCover) obj).getCoverKey());
        }
    }

    @Override
    public int hashCode() {
        if (coverKey != null) {
            return coverKey.hashCode();
        } else {
            return super.hashCode();
        }
    }

}
