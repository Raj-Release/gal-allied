package com.shaic.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;



 
@Entity
@Table(name="MAS_COUNTRY")
@NamedQueries({
	@NamedQuery(name="Country.findAll", query="SELECT m FROM Country m where m.activeStatus is not null  ORDER BY m.value "),
	@NamedQuery(name="Country.findByName", query="SELECT m FROM Country m where m.value = :countryName "),
	@NamedQuery(name="Country.findByKey", query="SELECT m FROM Country m where m.key = :key "),
	})
public class Country implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="COUNTRY_KEY")
	private Long key;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;


	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;


	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	@Column(name="VALUE")
	private String value;


	public Country() {
	}


	public Long getKey() {
		return key;
	}


	public void setKey(Long key) {
		this.key = key;
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


	public Timestamp getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Timestamp createdDate) {
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


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((Country) obj).getKey());
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