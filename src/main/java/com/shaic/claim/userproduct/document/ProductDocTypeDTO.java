package com.shaic.claim.userproduct.document;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class ProductDocTypeDTO extends AbstractTableDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int sno;
	
	private Long productTypeKey;
	
	private String prodCode;
	
	private String productDocType;
	
	private Boolean preauthValue;
	
	private Boolean enhancementValue;
	
	private Boolean preauthcheckBoxValue = false;
	
	private Boolean enhancheckBoxValue = false;
	
	private Boolean isEnabled = true;
	
	private Long activeStatus;

	
	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
		this.sno = sno;
	}
	
	public Long getProductTypeKey() {
		return productTypeKey;
	}

	public void setProductTypeKey(Long productTypeKey) {
		this.productTypeKey = productTypeKey;
	}

	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	public String getProductDocType() {
		return productDocType;
	}

	public void setProductDocType(String productDocType) {
		this.productDocType = productDocType;
	}

	public Boolean getPreauthValue() {
		return preauthValue;
	}

	public void setPreauthValue(Boolean preauthValue) {
		this.preauthValue = preauthValue;
	}

	public Boolean getEnhancementValue() {
		return enhancementValue;
	}

	public void setEnhancementValue(Boolean enhancementValue) {
		this.enhancementValue = enhancementValue;
	}

	public Boolean getPreauthcheckBoxValue() {
		return preauthcheckBoxValue;
	}

	public void setPreauthcheckBoxValue(Boolean preauthcheckBoxValue) {
		this.preauthcheckBoxValue = preauthcheckBoxValue;
	}

	public Boolean getEnhancheckBoxValue() {
		return enhancheckBoxValue;
	}

	public void setEnhancheckBoxValue(Boolean enhancheckBoxValue) {
		this.enhancheckBoxValue = enhancheckBoxValue;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	

}
