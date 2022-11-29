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
 * The persistent class for the MAS_CLAIM_SECTION database table.
 * 
 */
/**
 * @author NEWUSER
 *
 */

@Entity
@Table(name= "MAS_CLAIM_SECTION")
@NamedQueries({
	@NamedQuery(name = "ClaimSection.findAll", query = "SELECT cs FROM ClaimSection cs"),
	@NamedQuery(name = "ClaimSection.findByKey", query = "SELECT cs FROM ClaimSection cs where cs.sectionKey = :sectionKey"),
	@NamedQuery(name = "ClaimSection.findByProduct", query = "SELECT cs FROM ClaimSection cs where cs.productKey  = :productKey order by cs.sectionKey asc"),
	@NamedQuery(name = "ClaimSection.findByProductAndSection", query = "SELECT cs FROM ClaimSection cs where cs.productKey  = :productKey and cs.sectionCode = :sectionCode")
	})
public class ClaimSection implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9032231935164199055L;
	
	@Id
	@Column(name = "SECTION_KEY")
	private Long sectionKey;
	
	@Column(name = "PRODUCT_KEY")
	private Long productKey;
	
	@Column(name = "PRODUCT_CODE")
	private String productCode;
	
	@Column(name = "SECTION_VALUE")
	private String sectionValue;
	
	@Column(name = "ACTIVE_STATUS")
	private Boolean activeStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "SECTION_CODE")
	private String sectionCode;

	public Long getSectionKey() {
		return sectionKey;
	}

	public void setSectionKey(Long sectionKey) {
		this.sectionKey = sectionKey;
	}

	public Long getProductKey() {
		return productKey;
	}

	public void setProductKey(Long productKey) {
		this.productKey = productKey;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getSectionValue() {
		return sectionValue;
	}

	public void setSectionValue(String sectionValue) {
		this.sectionValue = sectionValue;
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

	public String getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getSectionKey() == null) {
            return obj == this;
        } else {
            return getSectionKey().equals(((ClaimSection) obj).getSectionKey());
        }
    }

    @Override
    public int hashCode() {
        if (sectionKey != null) {
            return sectionKey.hashCode();
        } else {
            return super.hashCode();
        }
    }
}
