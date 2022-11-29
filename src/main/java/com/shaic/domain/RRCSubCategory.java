package com.shaic.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="MAS_RRC_SUB_CATEGORY")

@NamedQueries({
	@NamedQuery(name="RRCSubCategory.findAll", query="SELECT m FROM RRCSubCategory m"),
	@NamedQuery(name="RRCSubCategory.findByKey", query="SELECT m FROM RRCSubCategory m where m.key = :key"),
	@NamedQuery(name="RRCSubCategory.findSubCatByCategoryId", query="SELECT m FROM RRCSubCategory m where m.categoryId = :categoryId"),
})
public class RRCSubCategory {
	
	@Id
	@Column(name = "RRC_SUB_CATEGORY_KEY")
	private Long key;
	
	@Column(name = "RRC_CATEGORY_ID")
	private Long categoryId;
	
	@Column(name = "RRC_SUB_CATEGORY_NAME")
	private String subCategoryName;	

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

}
