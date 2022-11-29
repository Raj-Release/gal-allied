package com.shaic.claim.userproduct.document;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;


@Entity
@Table(name="MAS_USER_PRODUCT_MAPPING")
@NamedQueries({
	@NamedQuery(name="UserProductMapping.findAll", query="SELECT u FROM UserProductMapping u"),
	@NamedQuery(name="UserProductMapping.findByEmpProdKey",query="SELECT u from UserProductMapping u where u.key = :key"),
	@NamedQuery(name="UserProductMapping.findByUserId",query="SELECT u from UserProductMapping u where u.userNameId = :userId"),
	@NamedQuery(name="UserProductMapping.findByUserIdAndProdId",query="SELECT u from UserProductMapping u where u.userNameId = :userId and u.productKey = :productId"),
	@NamedQuery(name="UserProductMapping.findProductKeyByUserId",query="SELECT u.productKey from UserProductMapping u where UPPER(u.userNameId) = :userId")
})
public class UserProductMapping extends AbstractEntity{
	
	@Id
	@SequenceGenerator(name="IMS_CLS_SEQ_MAS_USER_PRODUCT_KEY_GENERATOR", sequenceName = "SEQ_USER_PRODUCT_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_SEQ_MAS_USER_PRODUCT_KEY_GENERATOR" )
	@Column(name="EMP_PRD_KEY",updatable=false)
	private Long key;
	
	@Column(name="USER_ID")
	private String userNameId;
	
	@Column(name="PRODUCT_CODE")
	private String productCode;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name="ELIGIBILITY_FLAG", length=1)
	private String eligibilityFlag;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name="PREAUTH_ELG_FLAG", length=1)
	private String preauthEligibility;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name="ENHANCE_ELG_FLAG", length=1)
	private String enhancementEligibility;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name="CREATED_BY", length=1)
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(nullable = false, columnDefinition = "VARCHAR(1)", name="MODIFIED_BY", length=1)
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="PRODUCT_ID")
	private Long productKey;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getUserNameId() {
		return userNameId;
	}

	public void setUserNameId(String userNameId) {
		this.userNameId = userNameId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getEligibilityFlag() {
		return eligibilityFlag;
	}

	public void setEligibilityFlag(String eligibilityFlag) {
		this.eligibilityFlag = eligibilityFlag;
	}

	public String getPreauthEligibility() {
		return preauthEligibility;
	}

	public void setPreauthEligibility(String preauthEligibility) {
		this.preauthEligibility = preauthEligibility;
	}

	public String getEnhancementEligibility() {
		return enhancementEligibility;
	}

	public void setEnhancementEligibility(String enhancementEligibility) {
		this.enhancementEligibility = enhancementEligibility;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getProductKey() {
		return productKey;
	}

	public void setProductKey(Long productKey) {
		this.productKey = productKey;
	}
	
	

}
