package com.shaic.arch.fields.dto;

import java.io.Serializable;

import com.shaic.cmn.login.ImsUser;

/**
 * @author GokulPrasath.A
 *
 */
public class MagazineDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long masMagKey;
	private Long masDocKey;
	private String masMagCode;
	private String masMagCategory;
	private String masMagSubCategory;
	private String masMagDesc;
	private String masCreatedDate;
	private String masCreatedBy;
	private String masModifiedDate;
	private String masModifiedBy;
	private String masActiveFlag;
	private ImsUser imsUser;
	private String action;
	
	
	
	public ImsUser getImsUser() {
		return imsUser;
	}
	public void setImsUser(ImsUser imsUser) {
		this.imsUser = imsUser;
	}
	public Long getMasMagKey() {
		return masMagKey;
	}
	public void setMasMagKey(Long masMagKey) {
		this.masMagKey = masMagKey;
	}
	public Long getMasDocKey() {
		return masDocKey;
	}
	public void setMasDocKey(Long masDocKey) {
		this.masDocKey = masDocKey;
	}
	public String getMasMagCode() {
		return masMagCode;
	}
	public void setMasMagCode(String masMagCode) {
		this.masMagCode = masMagCode;
	}
	public String getMasMagCategory() {
		return masMagCategory;
	}
	public void setMasMagCategory(String masMagCategory) {
		this.masMagCategory = masMagCategory;
	}
	public String getMasMagSubCategory() {
		return masMagSubCategory;
	}
	public void setMasMagSubCategory(String masMagSubCategory) {
		this.masMagSubCategory = masMagSubCategory;
	}
	public String getMasMagDesc() {
		return masMagDesc;
	}
	public void setMasMagDesc(String masMagDesc) {
		this.masMagDesc = masMagDesc;
	}
	public String getMasCreatedDate() {
		return masCreatedDate;
	}
	public void setMasCreatedDate(String masCreatedDate) {
		this.masCreatedDate = masCreatedDate;
	}
	public String getMasCreatedBy() {
		return masCreatedBy;
	}
	public void setMasCreatedBy(String masCreatedBy) {
		this.masCreatedBy = masCreatedBy;
	}
	public String getMasModifiedDate() {
		return masModifiedDate;
	}
	public void setMasModifiedDate(String masModifiedDate) {
		this.masModifiedDate = masModifiedDate;
	}
	public String getMasModifiedBy() {
		return masModifiedBy;
	}
	public void setMasModifiedBy(String masModifiedBy) {
		this.masModifiedBy = masModifiedBy;
	}
	public String getMasActiveFlag() {
		return masActiveFlag;
	}
	public void setMasActiveFlag(String masActiveFlag) {
		this.masActiveFlag = masActiveFlag;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}

}
