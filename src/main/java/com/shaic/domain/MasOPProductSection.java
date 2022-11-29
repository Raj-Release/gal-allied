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


@Entity
@Table(name="MAS_OP_PRODUCT_SECTION")
@NamedQueries({
	@NamedQuery(name ="MasOPProductSection.findAll", query = "SELECT s FROM MasOPProductSection s"),
	@NamedQuery(name = "MasOPProductSection.findByProdKey",query = "select s from MasOPProductSection s where s.productKey = :productKey and s.activeStatus = 1 order by opProdKey asc"),
})
public class MasOPProductSection implements Serializable{
	
	@Id
	@Column(name="OP_PROD_KEY")
	private Long opProdKey;
	
	@Column(name="OP_SEC_KEY")
	private Long opSecKey;
	
	@Column(name="PRODUCT_KEY")
	private Long productKey;
	
	@Column(name="PRODUCT_CODE")
	private String productCode;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;

	public Long getOpProdKey() {
		return opProdKey;
	}

	public void setOpProdKey(Long opProdKey) {
		this.opProdKey = opProdKey;
	}

	public Long getOpSecKey() {
		return opSecKey;
	}

	public void setOpSecKey(Long opSecKey) {
		this.opSecKey = opSecKey;
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

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	

}
