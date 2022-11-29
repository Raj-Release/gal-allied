package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name = "MAS_OP_LAB_DETAILS")
@NamedQueries({
		@NamedQuery(name = "OPLabPortals.findByKey", query = "SELECT m FROM OPLabPortals m where m.key = :key")
	}
		)
public class OPLabPortals extends AbstractEntity implements Serializable {
	
	@Id
	@Column(name = "PROVIDE_KEY")
	private Long key;
	
	@Column(name = "PROVIDER_CODE")
	private String providerCode;
	
	@Column(name = "PROVIDER_NAME")
	private String providerName;
	
	@Column(name = "PROVIDER_TYPE")
	private String providerType;
	
	@Column(name = "PROVIDER_ADDRESS_1")
	private String providerAddressOne;
	
	@Column(name = "PROVIDER_ADDRESS_2")
	private String providerAddressTwo;
	
	@Column(name = "PROVIDER_ADDRESS_3")
	private String providerAddressThree;
	
	@Column(name = "CITY")
	private String city;
	
	@Column(name = "STATE")
	private String state;
	
	@Column(name = "PROVIDER_CONTACT_NO")
	private String providerContactNo;
	
	@Column(name = "PROVIDER_STATUS")
	private String providerStatus;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "ACTIVE_FLAG")
	private Long activeFlag;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getProviderCode() {
		return providerCode;
	}

	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getProviderType() {
		return providerType;
	}

	public void setProviderType(String providerType) {
		this.providerType = providerType;
	}

	public String getProviderAddressOne() {
		return providerAddressOne;
	}

	public void setProviderAddressOne(String providerAddressOne) {
		this.providerAddressOne = providerAddressOne;
	}

	public String getProviderAddressTwo() {
		return providerAddressTwo;
	}

	public void setProviderAddressTwo(String providerAddressTwo) {
		this.providerAddressTwo = providerAddressTwo;
	}

	public String getProviderAddressThree() {
		return providerAddressThree;
	}

	public void setProviderAddressThree(String providerAddressThree) {
		this.providerAddressThree = providerAddressThree;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getProviderContactNo() {
		return providerContactNo;
	}

	public void setProviderContactNo(String providerContactNo) {
		this.providerContactNo = providerContactNo;
	}

	public String getProviderStatus() {
		return providerStatus;
	}

	public void setProviderStatus(String providerStatus) {
		this.providerStatus = providerStatus;
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

	public Long getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Long activeFlag) {
		this.activeFlag = activeFlag;
	}
	

}
