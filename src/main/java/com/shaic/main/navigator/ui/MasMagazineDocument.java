package com.shaic.main.navigator.ui;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;


@Entity
@Table(name = "MAS_MAGAZINE_GALLERY")
@NamedQueries({
	@NamedQuery(name = "MasMagazineDocument.findAll", query = "SELECT m FROM MasMagazineDocument m where m.activeFlag = 'Y'"),
	@NamedQuery(name = "MasMagazineDocument.findAllMagazine", query = "SELECT m FROM MasMagazineDocument m where m.activeFlag <> 'D' order by m.createdDate desc"),
	@NamedQuery(name = "MasMagazineDocument.findByMagazineCode", query = "Select m from MasMagazineDocument m where m.magazineCode =:magazineCode ")
})
public class MasMagazineDocument extends AbstractEntity{

	@Id
	@Column(name="MAG_GAL_KEY")
	private Long key;
	
	@Column(name="DOCUMENT_KEY")
	private Long documentKey;
        
	@Column(name="MAGAZINE_CODE")
	private String magazineCode;
	
	@Column(name="MAGAZINE_CATEGORY")
	private String magazineCategory;
	
	@Column(name="MAGAZINE_SUB_CATEGORY")
	private String magazineSubCategory;
	
	@Column(name="MAGAZINE_DESC")
	private String magazineDescription;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="ACTIVE_FLAG")
	private String activeFlag;
	
	
	public Long getDocumentKey() {
		return documentKey;
	}

	public void setDocumentKey(Long documentKey) {
		this.documentKey = documentKey;
	}

	public String getMagazineCode() {
		return magazineCode;
	}

	public void setMagazineCode(String magazineCode) {
		this.magazineCode = magazineCode;
	}

	public String getMagazineCategory() {
		return magazineCategory;
	}

	public void setMagazineCategory(String magazineCategory) {
		this.magazineCategory = magazineCategory;
	}

	public String getMagazineSubCategory() {
		return magazineSubCategory;
	}

	public void setMagazineSubCategory(String magazineSubCategory) {
		this.magazineSubCategory = magazineSubCategory;
	}

	public String getMagazineDescription() {
		return magazineDescription;
	}

	public void setMagazineDescription(String magazineDescription) {
		this.magazineDescription = magazineDescription;
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

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	@Override
	public Long getKey() {
		return key;
	}

	@Override
	public void setKey(Long key) {
		this.key=key;
		
	}
 
	
	
}
