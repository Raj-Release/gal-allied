package com.shaic.claim.userproduct.document.search;

public class UserMgmtProductMappingDTO {
	
	
	private int sno;
	
	private Long key;
	
	private String productCodeWithName;
	
	private String productType;
	
	private Boolean productMappingEnable;
	
	private Boolean selected;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
		this.sno = sno;
	}

	public Boolean getProductMappingEnable() {
		return productMappingEnable;
	}

	public void setProductMappingEnable(Boolean productMappingEnable) {
		this.productMappingEnable = productMappingEnable;
	}

	public String getProductCodeWithName() {
		return productCodeWithName;
	}

	public void setProductCodeWithName(String productCodeWithName) {
		this.productCodeWithName = productCodeWithName;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

}
