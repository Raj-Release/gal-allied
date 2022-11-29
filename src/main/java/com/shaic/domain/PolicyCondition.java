package com.shaic.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="MAS_POLICY_CONDITION")
@NamedQueries({
	@NamedQuery(name="PolicyCondition.findAll", query="SELECT m FROM PolicyCondition m where m.activeStatus is not null and m.activeStatus = 1"),
	@NamedQuery(name="PolicyCondition.findByProduct", query = "SELECT m FROM PolicyCondition m where m.product.key = :productKey and m.activeStatus is not null and m.activeStatus = 1 order by m.key"),
//	@NamedQuery(name="PolicyCondition.findByProductType", query = "SELECT m FROM PolicyCondition m where m.product.productType = :productType"),
	@NamedQuery(name="PolicyCondition.findByProductCode", query = "SELECT m FROM PolicyCondition m where m.productCode = :productCode and m.activeStatus is not null and m.activeStatus = 1"),
	@NamedQuery(name="PolicyCondition.findByProductCodeWithVersion", query="SELECT m FROM PolicyCondition m where m.product.key = :productkey and m.versionNumber <= :productversionNumber"),
	@NamedQuery(name="PolicyCondition.findByVersion", query="SELECT m FROM PolicyCondition m where m.product.key = :productKey and m.versionNumber = :productversionNumber"),
})
public class PolicyCondition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "POLICY_CONDITION_KEY")
	private BigDecimal key;
	
	@OneToOne
	// @JoinColumn(name = "PRODUCT_ID", nullable = false)
	@JoinColumn(name = "PRODUCT_ID", nullable = true)
	private Product product;
	
	@Column(name = "PRODUCT_CODE")
	private String productCode;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "PRODUCT_RULES")
	private String productRules;
	

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE", insertable = false, updatable = false)
	private Timestamp createdDate;
	
	
	
	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name = "CREATED_DATE")
	private Timestamp dmDate;
	
	@Column(name = "VERSION_NUMBER")
	private Long versionNumber;

	public BigDecimal getKey() {
		return key;
	}

	public void setKey(BigDecimal key) {
		this.key = key;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProductRules() {
		return productRules;
	}

	public void setProductRules(String productRules) {
		this.productRules = productRules;
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

	

	public Timestamp getDmDate() {
		return dmDate;
	}

	public void setDmDate(Timestamp dmDate) {
		this.dmDate = dmDate;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Long getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	} 


}
