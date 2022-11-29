package com.shaic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name= "MAS_PHC_BENEFIT")
@NamedQueries({
	@NamedQuery(name="MasHospitalCashBenefit.findAll",query="SELECT i from MasHospitalCashBenefit i"),
	@NamedQuery(name="MasHospitalCashBenefit.findByProductKey", query="SELECT o FROM MasHospitalCashBenefit o where o.productKey = :productKey"),
	@NamedQuery(name="MasHospitalCashBenefit.findHospCashBenefitByKey", query="SELECT o FROM MasHospitalCashBenefit o where o.key = :benefitKey"),
	@NamedQuery(name="MasHospitalCashBenefit.findByProductTypeId", query="SELECT o FROM MasHospitalCashBenefit o where o.productTypeId = :productTypeId and o.planType = :planType"),
	
	})

public class MasHospitalCashBenefit {
	
	@Id
	@Column(name="PHC_BENEFIT_KEY")
	private Long key;
	
	@Column(name="PRODUCT_KEY")
	private Long productKey;
	
	@Column(name="PRODUCT_CODE")
	private String productCode;
	
	@Column(name="PHC_BENEFIT_NAME")
	private String benefitName;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	  
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="COVER_CODE")
	private String coverCode;

	@Column(name ="PLAN_TYPE")
	private String planType;
	
	@Column(name="PRODUCT_TYPE_ID")
	private Long productTypeId;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
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

	public String getBenefitName() {
		return benefitName;
	}

	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName;
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

	public String getCoverCode() {
		return coverCode;
	}

	public void setCoverCode(String coverCode) {
		this.coverCode = coverCode;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public Long getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}  

}
