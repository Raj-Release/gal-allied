package com.shaic.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;


public class PolicyBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal key;
	
	private int activeStatus;

	private Timestamp activeStatusDate;

	
	private String activeStatusRemarks;

	
	private String createdBy;

	
	private Timestamp createdDate;

	private BigDecimal genderId;

	private String healthCardNumber;

	private String homeOfficeCode;

	private String homeOfficeName;

	private int insuredBalanceSi;

	private Date insuredDob;

	private String insuredFirstName;

	private String insuredId;

	private String insuredLastName;

	private String insuredMiddleName;

	private int insuredSumInsured;


	private BigDecimal migratedApplicationId;

	private String migratedCode;

	private String modifiedBy;

	private Timestamp modifiedDate;

	private String officeCode;

	private Timestamp policyFromDate;

	private String policyNumber;

	private String policyStatus;

	private int policySysId;

	private Timestamp policyToDate;

	private String productCode;

	private String proposerFirstName;

	private String proposerLastName;

	private String proposerMiddleName;

	private BigDecimal relationshipWithInsuredId;

	private BigDecimal relationshipWithProposerId;

	private String status;

	private Timestamp statusDate;

	private String statusRemarks;

	private int totalSumInsured;

	private BigDecimal version;
		
	public PolicyBean() {
	}



	public int getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(int i) {
		this.activeStatus = i;
	}

	public Timestamp getActiveStatusDate() {
		return this.activeStatusDate;
	}

	public void setActiveStatusDate(Timestamp activeStatusDate) {
		this.activeStatusDate = activeStatusDate;
	}

	public String getActiveStatusRemarks() {
		return this.activeStatusRemarks;
	}

	public void setActiveStatusRemarks(String activeStatusRemarks) {
		this.activeStatusRemarks = activeStatusRemarks;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public BigDecimal getGenderId() {
		return this.genderId;
	}

	public void setGenderId(BigDecimal genderId) {
		this.genderId = genderId;
	}

	public String getHealthCardNumber() {
		return this.healthCardNumber;
	}

	public void setHealthCardNumber(String healthCardNumber) {
		this.healthCardNumber = healthCardNumber;
	}

	public String getHomeOfficeCode() {
		return this.homeOfficeCode;
	}

	public void setHomeOfficeCode(String homeOfficeCode) {
		this.homeOfficeCode = homeOfficeCode;
	}

	public String getHomeOfficeName() {
		return this.homeOfficeName;
	}

	public void setHomeOfficeName(String homeOfficeName) {
		this.homeOfficeName = homeOfficeName;
	}

	public int getInsuredBalanceSi() {
		return this.insuredBalanceSi;
	}

	public void setInsuredBalanceSi(int i) {
		this.insuredBalanceSi = i;
	}

	public Date getInsuredDob() {
		return this.insuredDob;
	}

	public void setInsuredDob(Date insuredDob) {
		this.insuredDob = insuredDob;
	}

	public String getInsuredFirstName() {
		return this.insuredFirstName;
	}

	public void setInsuredFirstName(String insuredFirstName) {
		this.insuredFirstName = insuredFirstName;
	}

	public String getInsuredId() {
		return this.insuredId;
	}

	public void setInsuredId(String insuredId) {
		this.insuredId = insuredId;
	}

	public String getInsuredLastName() {
		return this.insuredLastName;
	}

	public void setInsuredLastName(String insuredLastName) {
		this.insuredLastName = insuredLastName;
	}

	public String getInsuredMiddleName() {
		return this.insuredMiddleName;
	}

	public void setInsuredMiddleName(String insuredMiddleName) {
		this.insuredMiddleName = insuredMiddleName;
	}

	public int getInsuredSumInsured() {
		return this.insuredSumInsured;
	}

	public void setInsuredSumInsured(int i) {
		this.insuredSumInsured = i;
	}

	public BigDecimal getKey() {
		return this.key;
	}

	public void setKey(BigDecimal key) {
		this.key = key;
	}

	public BigDecimal getMigratedApplicationId() {
		return this.migratedApplicationId;
	}

	public void setMigratedApplicationId(BigDecimal migratedApplicationId) {
		this.migratedApplicationId = migratedApplicationId;
	}

	public String getMigratedCode() {
		return this.migratedCode;
	}

	public void setMigratedCode(String migratedCode) {
		this.migratedCode = migratedCode;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getOfficeCode() {
		return this.officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public Timestamp getPolicyFromDate() {
		return this.policyFromDate;
	}

	public void setPolicyFromDate(Timestamp policyFromDate) {
		this.policyFromDate = policyFromDate;
	}

	public String getPolicyNumber() {
		return this.policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getPolicyStatus() {
		return this.policyStatus;
	}

	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}

	public int getPolicySysId() {
		return this.policySysId;
	}

	public void setPolicySysId(int i) {
		this.policySysId = i;
	}

	public Timestamp getPolicyToDate() {
		return this.policyToDate;
	}

	public void setPolicyToDate(Timestamp policyToDate) {
		this.policyToDate = policyToDate;
	}

	public String getProductCode() {
		return this.productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProposerFirstName() {
		return this.proposerFirstName;
	}

	public void setProposerFirstName(String proposerFirstName) {
		this.proposerFirstName = proposerFirstName;
	}

	public String getProposerLastName() {
		return this.proposerLastName;
	}

	public void setProposerLastName(String proposerLastName) {
		this.proposerLastName = proposerLastName;
	}

	public String getProposerMiddleName() {
		return this.proposerMiddleName;
	}

	public void setProposerMiddleName(String proposerMiddleName) {
		this.proposerMiddleName = proposerMiddleName;
	}

	public BigDecimal getRelationshipWithInsuredId() {
		return this.relationshipWithInsuredId;
	}

	public void setRelationshipWithInsuredId(BigDecimal relationshipWithInsuredId) {
		this.relationshipWithInsuredId = relationshipWithInsuredId;
	}

	public BigDecimal getRelationshipWithProposerId() {
		return this.relationshipWithProposerId;
	}

	public void setRelationshipWithProposerId(BigDecimal relationshipWithProposerId) {
		this.relationshipWithProposerId = relationshipWithProposerId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getStatusDate() {
		return this.statusDate;
	}

	public void setStatusDate(Timestamp statusDate) {
		this.statusDate = statusDate;
	}

	public String getStatusRemarks() {
		return this.statusRemarks;
	}

	public void setStatusRemarks(String statusRemarks) {
		this.statusRemarks = statusRemarks;
	}

	public int getTotalSumInsured() {
		return this.totalSumInsured;
	}

	public void setTotalSumInsured(int i) {
		this.totalSumInsured = i;
	}

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

//	public Intimation getImsClsIntimationn() {
//		return this.imsClsIntimationn;
//	}
//
//	public void setImsClsIntimationn(Intimation imsClsIntimationn) {
//		this.imsClsIntimationn = imsClsIntimationn;
//	}

}