// changes at DB level in progress

package com.shaic.domain.preauth;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.domain.Product;

/**
 * The Master for MAS_CLAIM_LIMIT.
 * 
 */
@Entity
@Table(name = "MAS_CLAIM_LIMIT")
@NamedQueries({
		@NamedQuery(name = "ClaimLimit.findAll", query = "SELECT i FROM ClaimLimit i"),
		@NamedQuery(name = "ClaimLimit.findByKey", query = "SELECT i FROM ClaimLimit i where i.key = :limitKey"),
		@NamedQuery(name = "ClaimLimit.findBySubLimitIdAndProductKey", query = "SELECT i FROM ClaimLimit i where (i.key = :limitKey and i.product.key = :productKey)"),
		@NamedQuery(name = "ClaimLimit.findBySubLimitIdWithName",query= "SELECT i FROM ClaimLimit i where (i.product.key = :productKey and i.sumInsured =:sumInsured and i.limitName = :limitName)")
		})

public class ClaimLimit implements Serializable {
	private static final long serialVersionUID = 3760145401215411454L;

	@Id
	@Column(name = "LIMIT_KEY")
	private Long key;

	@OneToOne
	@JoinColumn(name = "PRODUCT_KEY", nullable = false)
	private Product product;
	//
	// @OneToOne
	// @JoinColumn(name="FK_PRODUCT_SUM_INSURED_KEY",nullable=false)
	// private ProductSumInsured productSumInsured;
	//

	@Column(name = "SUM_INSURED")
	private Integer sumInsured;

	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;

	/*
	 * @Column(name = "VERSION") private Long version;
	 */

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	/*
	 * @Temporal(TemporalType.TIMESTAMP)
	 * 
	 * @Column(name = "ACTIVE_STATUS_DATE") private Date activeStatusDate;
	 */

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	@Column(name = "MAX_PER_CLAIM_AMOUNT")
	private Long maxPerClaimAmount;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MAX_PERCENTAGE")
	private String maxPercentage;

	@Column(name = "CITY_CLASS")
	private String cityClass;

	@Column(name = "LIMIT_TYPE")
	private String limitType;

	@Column(name = "LIMIT_NAME")
	private String limitName;

	@Column(name = "LIMIT_DESCRIPTION")
	private String limitDescription;

	@Column(name = "MAX_PER_POLICY_PERIOD")
	private Long maxPerPolicyPeriod;

	@Column(name = "AGE_FROM")
	private Long ageFrom;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(Integer sumInsured) {
		this.sumInsured = sumInsured;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	/*
	 * public Long getVersion() { return version; }
	 * 
	 * public void setVersion(Long version) { this.version = version; }
	 */

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

	/*
	 * public Date getActiveStatusDate() { return activeStatusDate; }
	 * 
	 * public void setActiveStatusDate(Date activeStatusDate) {
	 * this.activeStatusDate = activeStatusDate; }
	 */

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getMaxPerClaimAmount() {
		return maxPerClaimAmount;
	}

	public void setMaxPerClaimAmount(Long maxPerClaimAmount) {
		this.maxPerClaimAmount = maxPerClaimAmount;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getMaxPercentage() {
		return maxPercentage;
	}

	public void setMaxPercentage(String maxPercentage) {
		this.maxPercentage = maxPercentage;
	}

	public String getCityClass() {
		return cityClass;
	}

	public void setCityClass(String cityClass) {
		this.cityClass = cityClass;
	}

	public String getLimitType() {
		return limitType;
	}

	public void setLimitType(String limitType) {
		this.limitType = limitType;
	}

	public String getLimitName() {
		return limitName;
	}

	public void setLimitName(String limitName) {
		this.limitName = limitName;
	}

	public String getLimitDescription() {
		return limitDescription;
	}

	public void setLimitDescription(String limitDescription) {
		this.limitDescription = limitDescription;
	}

	public Long getMaxPerPolicyPeriod() {
		return maxPerPolicyPeriod;
	}

	public void setMaxPerPolicyPeriod(Long maxPerPolicyPeriod) {
		this.maxPerPolicyPeriod = maxPerPolicyPeriod;
	}

	public Long getAgeFrom() {
		return ageFrom;
	}

	public void setAgeFrom(Long ageFrom) {
		this.ageFrom = ageFrom;
	}

}
