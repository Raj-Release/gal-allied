package com.shaic.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="MAS_PRODUCT_CPU_ROUTING")
@NamedQueries({
@NamedQuery(name="MasProductCpuRouting.findByKey",query = "SELECT o FROM MasProductCpuRouting o where o.key = :key and o.activeStatus = 1")
})

public class MasProductCpuRouting implements Serializable {
	
	private static final long serialVersionUID = 7217543402237269703L;
	
	
	@Column(name="\"PRODUCT_CODE\"")
	private String productCode;
	
	@Id
	@Column(name="\"PRODUCT_KEY\"")
	private Long key;
	
	@Column(name="CPU_CODE")
	private String cpuCode;
    
    @Column(name="ACTIVE_STATUS")
	private String activeStatus;
    
    @Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Date CreatedDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
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
		return CreatedDate;
	}

	public void setCreatedDate(Date createdDate) {
		CreatedDate = createdDate;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
