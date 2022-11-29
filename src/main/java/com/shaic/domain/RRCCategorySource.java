package com.shaic.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="MAS_RRC_CATEGORY_SOURCE")

@NamedQueries({
	@NamedQuery(name="RRCCategorySource.findAll", query="SELECT m FROM RRCCategorySource m"),
	@NamedQuery(name="RRCCategorySource.findByKey", query="SELECT m FROM RRCCategorySource m where m.key = :key"),
	@NamedQuery(name="RRCCategorySource.findSourceByCategoryId", query="SELECT m FROM RRCCategorySource m where m.categoryId = :categoryId"),
	@NamedQuery(name="RRCCategorySource.findSourceBySubCatId", query="SELECT m FROM RRCCategorySource m where m.subCategoryId = :subCategoryId"),
})

public class RRCCategorySource {

	@Id
	@Column(name = "RRC_CATEGORY_SOURCE_KEY")
	private Long key;
	
	@Column(name = "RRC_CATEGORY_ID")
	private Long categoryId;
	
	@Column(name = "RRC_SUB_CATEGORY_ID")
	private Long subCategoryId;
	
	@Column(name = "RRC_CATEGORY_SOURCE_NAME")
	private String sourceName;

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

	public Long getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(Long subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	
	
	
}
