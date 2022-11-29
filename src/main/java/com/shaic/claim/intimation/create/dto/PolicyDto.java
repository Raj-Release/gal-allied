package com.shaic.claim.intimation.create.dto;

import java.sql.Timestamp;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.Product;

public class PolicyDto {

	private Long key;

	private Boolean activeStatus;

	private Timestamp activeStatusDate;

	private String activeStatusRemarks;

	private String createdBy;

	private Timestamp createdDate;

	private SelectValue genderId;

	private String healthCardNumber;

	private String insuredAddress1;

	private String insuredAddress2;

	private String insuredAddress3;

	private String insuredContactNumber;

	private String insuredEmailId;

	private String homeOfficeCode;

	private String homeOfficeName;

	private Double insuredBalanceSi;

	private Date insuredDob;

	private String insuredFirstName;

	private String insuredId;

	private String insuredLastName;

	private String insuredMiddleName;

	private Double insuredSumInsured;

	private Long lobId;

	private String migratedCode;

	private String modifiedBy;

	private Timestamp modifiedDate;

	private String officeCode;

	private Date policyFromDate;

	private String policyNumber;

	private String policyStatus;

	private Long policySysId;

	private Date policyToDate;

	private Product product;

	private String proposerFirstName;

	private String proposerLastName;

	private String proposerMiddleName;

	private SelectValue relationshipWithInsuredId;

	private Double relationshipWithProposerId;

	private String status;

	private Timestamp statusDate;

	private String statusRemarks;


	private SelectValue policyType;

	private SelectValue productType;

	private Double totalSumInsured;

	private Integer version;

	private Double limitOfCoverage;

	private String sectionII;

	private Double rechargeSI;

	private Double sumInsuredII;

	private Double restoredSI;

	private Long copay;

	private String corporateBufferFlag;

	private Double totalBufferAmount;

	private Double noClaimBonus;
	
	private String policyPlan;
	
	private String proposerCode;
	
	private Double grossPremium;
	
	private Long policyYear;
	
	private Double totalPremium;
	
	private Double stampDuty;
	
	private Date receiptDate;
	
	private String receiptNumber;
	
	private Double premiumTax;

	
	
	// Please Don't delete it from here.
	private Date admissionDate;
	
	private Long claimKey;
	
	
	private Double cummulativeBonus;
	
	private String gmcPolicyType;
	
	private String linkPolicyNumber;
	
	private String paymentParty;
	
	private String polEmailId;
	
	private Long policyTerm;

	public PolicyDto() {
	}

	public Boolean getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(Boolean i) {
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

	public Double getInsuredBalanceSi() {
		return this.insuredBalanceSi;
	}

	public void setInsuredBalanceSi(Double i) {
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

	public Double getInsuredSumInsured() {
		return this.insuredSumInsured;
	}

	public void setInsuredSumInsured(Double i) {
		this.insuredSumInsured = i;
	}

	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getLobId() {
		return lobId;
	}

	public void setLobId(Long lobId) {
		this.lobId = lobId;
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

	public Date getPolicyFromDate() {
		return this.policyFromDate;
	}

	public void setPolicyFromDate(Date policyFromDate) {
		this.policyFromDate = policyFromDate;
	}

	public String getPolicyNumber() {
		return policyNumber;
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

	public Long getPolicySysId() {
		return this.policySysId;
	}

	public void setPolicySysId(Long i) {
		this.policySysId = i;
	}

	public Date getPolicyToDate() {
		return this.policyToDate;
	}

	public void setPolicyToDate(Date policyToDate) {
		this.policyToDate = policyToDate;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
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

	public SelectValue getRelationshipWithInsuredId() {
		return this.relationshipWithInsuredId;
	}

	public void setRelationshipWithInsuredId(SelectValue mastersValue) {
		this.relationshipWithInsuredId = mastersValue;
	}

	public Double getRelationshipWithProposerId() {
		return this.relationshipWithProposerId;
	}

	public void setRelationshipWithProposerId(Double relationshipWithProposerId) {
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

	public Double getTotalSumInsured() {
		return this.totalSumInsured;
	}

	public void setTotalSumInsured(Double totalSumInsured) {
		this.totalSumInsured = totalSumInsured;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public SelectValue getGenderId() {
		return genderId;
	}

	public void setGenderId(SelectValue genderId) {
		this.genderId = genderId;
	}

	public String getInsuredAddress1() {
		return insuredAddress1;
	}

	public void setInsuredAddress1(String insuredAddress1) {
		this.insuredAddress1 = insuredAddress1;
	}

	public String getInsuredAddress2() {
		return insuredAddress2;
	}

	public void setInsuredAddress2(String insuredAddress2) {
		this.insuredAddress2 = insuredAddress2;
	}

	public String getInsuredAddress3() {
		return insuredAddress3;
	}

	public void setInsuredAddress3(String insuredAddress3) {
		this.insuredAddress3 = insuredAddress3;
	}

	public String getInsuredContactNumber() {
		return insuredContactNumber;
	}

	public void setInsuredContactNumber(String insuredContactNumber) {
		this.insuredContactNumber = insuredContactNumber;
	}

	public String getInsuredEmailId() {
		return insuredEmailId;
	}

	public void setInsuredEmailId(String insuredEmailId) {
		this.insuredEmailId = insuredEmailId;
	}

	public SelectValue getPolicyType() {
		return policyType;
	}

	public void setPolicyType(SelectValue policyType) {
		this.policyType = policyType;
	}

	public SelectValue getProductType() {
		return productType;
	}

	public void setProductType(SelectValue productType) {
		this.productType = productType;
	}

	public Date getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}

	public Long getCopay() {
		return copay;
	}

	public void setCopay(Long copay) {
		this.copay = copay;
	}

	public Double getLimitOfCoverage() {
		return limitOfCoverage;
	}

	public void setLimitOfCoverage(Double limitOfCoverage) {
		this.limitOfCoverage = limitOfCoverage;
	}

	public String getSectionII() {
		return sectionII;
	}

	public void setSectionII(String sectionII) {
		this.sectionII = sectionII;
	}

	public Double getRechargeSI() {
		return rechargeSI;
	}

	public void setRechargeSI(Double rechargeSI) {
		this.rechargeSI = rechargeSI;
	}

	public Double getSumInsuredII() {
		return sumInsuredII;
	}

	public void setSumInsuredII(Double sumInsuredII) {
		this.sumInsuredII = sumInsuredII;
	}

	public Double getRestoredSI() {
		return restoredSI;
	}

	public void setRestoredSI(Double restoredSI) {
		this.restoredSI = restoredSI;
	}

	public String getCorporateBufferFlag() {
		return corporateBufferFlag;
	}

	public void setCorporateBufferFlag(String corporateBufferFlag) {
		this.corporateBufferFlag = corporateBufferFlag;
	}

	public Double getTotalBufferAmount() {
		return totalBufferAmount;
	}

	public void setTotalBufferAmount(Double totalBufferAmount) {
		this.totalBufferAmount = totalBufferAmount;
	}

	public Double getNoClaimBonus() {
		return noClaimBonus;
	}

	public void setNoClaimBonus(Double noClaimBonus) {
		this.noClaimBonus = noClaimBonus;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Double getCummulativeBonus() {
		return cummulativeBonus;
	}

	public void setCummulativeBonus(Double cummulativeBonus) {
		this.cummulativeBonus = cummulativeBonus;
	}

	public String getPolicyPlan() {
		return policyPlan;
	}

	public void setPolicyPlan(String policyPlan) {
		this.policyPlan = policyPlan;
	}

	public String getProposerCode() {
		return proposerCode;
	}

	public void setProposerCode(String proposerCode) {
		this.proposerCode = proposerCode;
	}

	public Double getGrossPremium() {
		return grossPremium;
	}

	public void setGrossPremium(Double grossPremium) {
		this.grossPremium = grossPremium;
	}

	public Long getPolicyYear() {
		return policyYear;
	}

	public void setPolicyYear(Long policyYear) {
		this.policyYear = policyYear;
	}

	public Double getTotalPremium() {
		return totalPremium;
	}

	public void setTotalPremium(Double totalPremium) {
		this.totalPremium = totalPremium;
	}

	public Double getStampDuty() {
		return stampDuty;
	}

	public void setStampDuty(Double stampDuty) {
		this.stampDuty = stampDuty;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public Double getPremiumTax() {
		return premiumTax;
	}

	public void setPremiumTax(Double premiumTax) {
		this.premiumTax = premiumTax;
	}

	public String getGmcPolicyType() {
		return gmcPolicyType;
	}

	public void setGmcPolicyType(String gmcPolicyType) {
		this.gmcPolicyType = gmcPolicyType;
	}

	public String getLinkPolicyNumber() {
		return linkPolicyNumber;
	}

	public void setLinkPolicyNumber(String linkPolicyNumber) {
		this.linkPolicyNumber = linkPolicyNumber;
	}

	public String getPaymentParty() {
		return paymentParty;
	}

	public void setPaymentParty(String paymentParty) {
		this.paymentParty = paymentParty;
	}

	public String getPolEmailId() {
		return polEmailId;
	}

	public void setPolEmailId(String polEmailId) {
		this.polEmailId = polEmailId;
	}

	public Long getPolicyTerm() {
		return policyTerm;
	}

	public void setPolicyTerm(Long policyTerm) {
		this.policyTerm = policyTerm;
	}

	


	
	
}
