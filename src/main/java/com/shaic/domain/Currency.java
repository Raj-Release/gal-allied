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
 * The persistent class for the MAS_CURRENCY_T database table.
 * 
 */
@Entity
@Table(name="MAS_CURRENCY")

@NamedQueries({
	@NamedQuery(name="Currency.findAll", query="SELECT m FROM Currency m"),
	@NamedQuery(name="Currency.findByKey", query="SELECT m FROM Currency m where m.key=:key")
})
public class Currency implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="CUR_KEY")
	private Long key;

	@Column(name="CURRENCY_CODE")
	private String currencyCode;

	@Column(name="CURRENCY_NAME")
	private String currencyName;

	@Column(name="COUNTRY")
	private String country;

	@Column(name="LOCATION")
	private String location;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="CREATED_BY")
	private String createdBy;

	public Currency() {
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((Currency) obj).getKey());
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
}