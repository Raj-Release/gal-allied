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
 * The persistent class for the MAS_COPAY_T database table.
 * 
 */
@Entity
@Table(name = "MAS_COPAY")
@NamedQueries({
		@NamedQuery(name = "MasCopay.findAll", query = "SELECT o FROM MasCopay o"),
		@NamedQuery(name = "MasCopay.findByProduct", query = "SELECT o FROM MasCopay o where o.productKey = :productKey") })
public class MasCopay implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "AVAILABLE_PED")
	private Long availablePed;

	@Column(name = "ENTRY_AGE_FROM")
	private Long entryAgeFrom;

	@Column(name = "PRODUCT_KEY")
	private Long productKey;

	@Id
	@Column(name = "\"COPAY_KEY\"")
	private Long key;

	@Column(name = "MAX_PERCENTAGE")
	private Double maxPercentage;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name = "SI_FROM")
	private Double siFrom;
	
	@Column(name = "SI_TO")
	private Double siTo; 

	public MasCopay() {
	}

	public Long getAvailablePed() {
		return this.availablePed;
	}

	public void setAvailablePed(Long availablePed) {
		this.availablePed = availablePed;
	}

	public Long getEntryAgeFrom() {
		return this.entryAgeFrom;
	}

	public void setEntryAgeFrom(Long entryAgeFrom) {
		this.entryAgeFrom = entryAgeFrom;
	}

	public Long getProductKey() {
		return productKey;
	}

	public void setProductKey(Long productKey) {
		this.productKey = productKey;
	}

	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Double getMaxPercentage() {
		return this.maxPercentage;
	}

	public void setMaxPercentage(Double maxPercentage) {
		this.maxPercentage = maxPercentage;
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

	public Double getSiFrom() {
		return siFrom;
	}

	public void setSiFrom(Double siFrom) {
		this.siFrom = siFrom;
	}

	public Double getSiTo() {
		return siTo;
	}

	public void setSiTo(Double siTo) {
		this.siTo = siTo;
	}

}