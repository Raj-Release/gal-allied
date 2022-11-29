package com.shaic.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MAS_COUNTRY_CODE_T database table.
 * 
 */
@Entity
@Table(name="MAS_PRODUCT")
@NamedQueries({
	@NamedQuery(name="Product.findAll", query="SELECT m FROM Product m where m.activeStatus is not null and m.activeStatus = 1 order by m.value asc"),
	@NamedQuery(name="Product.findHealth", query="SELECT m FROM Product m where m.lineOfBusiness = 'Health' and m.activeStatus is not null and m.activeStatus = 1 order by m.value asc"),
	@NamedQuery(name="Product.findUnique", query="SELECT m.code, m.value,m.key from Product m where m.activeStatus is not null and m.activeStatus = 1 group by m.code,m.value,m.key"),
	@NamedQuery(name="Product.findByCode", query="SELECT m FROM Product m where m.code = :productCode"),
	@NamedQuery(name = "Product.findByKey",query = "select m from Product m where m.key = :key"),
	@NamedQuery(name = "Product.findByProductType", query = "SELECT m FROM Product m where m.code = :productCode and Lower(m.productType) = :productType"),
	@NamedQuery(name = "Product.findByProductTypeAndDate", query = "SELECT m FROM Product m where m.code = :productCode and Lower(m.productType) = :productType and m.effStartDate <= :inceptionDate and  m.effEndDate >= :inceptionDate"),
	@NamedQuery(name="Product.findOMPUnique", query="SELECT m.code, m.value from Product m where m.lineOfBusiness is not null and m.lineOfBusiness = 'OMP' group by m.code,m.value"),
	@NamedQuery(name = "Product.findByLineOfBusiness", query = "SELECT m FROM Product m where m.lineOfBusiness = :lineOfBusiness"),
	@NamedQuery(name="Product.findUniquebyLineOfBusiness", query="SELECT m.code, m.value,m.key from Product m where  m.lineOfBusiness = :lineOfBusiness group by m.code,m.value,m.key"),
	@NamedQuery(name="Product.findByCodeAndDate", query="SELECT m FROM Product m where m.code = :productCode and m.effStartDate <= :inceptionDate and  m.effEndDate >= :inceptionDate"),
	@NamedQuery(name="Product.findBySourceCode", query="SELECT m FROM Product m where m.sourceCode = :productCode"),
	@NamedQuery(name="Product.findAllProduct", query="SELECT m FROM Product m where m.activeStatus = 1 order by 1"),
})
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@Id
	@Column(name="\"PRODUCT_KEY\"")
	private Long key;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="\"CODE\"")
	private String code;
	
	@Column(name="IRDA_CODE",nullable=true)
	private String irdaCode;

	@Column(name="\"VALUE\"")
	private String value;

//	@Column(name="\"VERSION\"")
//	private BigDecimal version;
	
	@Column(name="MIN_SI_AUTO_RESTORATION")
	private Long autoRestoration;
	
	@Column(name="NON_ALLOPATHIC_FLAG")
	private String nonAllopathicFlag;
	
	@Column(name="DAY_CARE_FLAG")
	private String dayCareFlag;
	
	@Column(name="PRE_HOSPITALIZATION_FLAG")
	private String preHospitalizationFlag;
	
	@Column(name="POST_HOSPITALIZATION_FLAG")
	private String postHospitalizationFlag;
	
	@Column(name="PRE_HOSPITALIZATION_DAYS")
	private Long preHospitalizationDays;
	
	@Column(name="POST_HOSPITALIZATION_DAYS")
	private Long postHospitalizationDays;
	
	@Column(name="AUTO_RESTORATION_PERCENTAGE")
	private Long autoRestorationPercentage;
	
	@Column(name="PED_WAITING_PERIOD")
	private Long pedWaitingPeriod;
	
	@Column(name="PRODUCT_TYPE")
	private String productType;
	
	@Column(name="LINE_OF_BUSINESS")
	private String lineOfBusiness;
	
	@Column(name="AUTO_RESTORATION_FLAG")
	private String autoRestorationFlag;
	
	@Column(name="CALCULATION_TEMPLATE")
	private String calculationTemplateFlag;
	
	
	@Column(name = "PACKAGE_AVAILABLE_FLAG")
	private String packageAvailableFlag;
	
	@Column(name = "PRORATA_FLAG")
	private String prorataFlag;

	@Column(name="RECHARGE_SI_FLAG")
	private String rechargeSiFlag;
	
	@Column(name="RTA_RECHARGE_FLAG")
	private String rtaRechargeSiFlag;
	
	@Column(name="IS_INTEGRATED_FLAG")
	private String isIntegrated;
	
	@Column(name="HOSPITALISATION_FLAG")
	private String hospitalisationFlag;
	
	@Column(name="PRODUCT_SERVICE")
	private String productService;
	
	@Column(name="SEC_B_COPAY")
	private String sectionBcoPay;
	
	@Column(name="EFF_START_DATE")
	private Date effStartDate;
	
	@Column(name="EFF_END_DATE")
	private Date effEndDate;
	
	@Column(name="INTIMATION_TYPE")
	private String intimationType;

	@Column(name="PRD_WAITING_PERIOD")
	private String waitingPeriod;
	
	@Column(name="\"SOURCE_PRODUCT_CODE\"")
	private String sourceCode;
	
	@Column(name="FLP_BYPASS_FLAG")
	private Long flpByPassFlag;
	
	@Column(name="INSTALMENT_FLAG")
	private String policyInstalmentFlag;
	
	@Column(name="BONUS_APPLICABLE")
	private String bonusApplicableFlag;

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	
	@Column(name="SOURCE_LOB_ID")
	private String sourceLobId;	
	
	@Column(name="LOB_ID")
	private String lobId;
	
	@Column(name="DOCUMENT_TOKEN")
	private Long documentToken;
	
	@Column(name="DOCUMENT_TOKEN2")
	private Long documentToken2;
	
	@Column(name="CASHLESS_ELIGIBLE_FLAG")
	private String cashlessElgFlag;
	
	@Column(name="OUT_PATIENT_FLAG")
	private String outPatientFlag;
	
	@Column(name="DOCUMENT_TOKEN3")
	private Long documentToken3;
	
	@Column(name="DOCUMENT_TOKEN4")
	private Long documentToken4;

	public String getWaitingPeriod() {
		return waitingPeriod;
	}

	public void setWaitingPeriod(String waitingPeriod) {
		this.waitingPeriod = waitingPeriod;
	}

	public String getCalculationTemplateFlag() {
		return calculationTemplateFlag;
	}

	public void setCalculationTemplateFlag(String calculationTemplateFlag) {
		this.calculationTemplateFlag = calculationTemplateFlag;
	}

	public Product() {
	}

	public String getIsIntegrated() {
		return isIntegrated;
	}

	public void setIsIntegrated(String isIntegrated) {
		this.isIntegrated = isIntegrated;
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

	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIrdaCode() {
		return irdaCode;
	}

	public void setIrdaCode(String irdaCode) {
		this.irdaCode = irdaCode;
	}

	public Long getAutoRestoration() {
		return autoRestoration;
	}

	public void setAutoRestoration(Long autoRestoration) {
		this.autoRestoration = autoRestoration;
	}

	public String getNonAllopathicFlag() {
		return nonAllopathicFlag;
	}

	public void setNonAllopathicFlag(String nonAllopathicFlag) {
		this.nonAllopathicFlag = nonAllopathicFlag;
	}

	public String getDayCareFlag() {
		return dayCareFlag;
	}

	public void setDayCareFlag(String dayCareFlag) {
		this.dayCareFlag = dayCareFlag;
	}

	public String getPreHospitalizationFlag() {
		return preHospitalizationFlag;
	}

	public void setPreHospitalizationFlag(String preHospitalizationFlag) {
		this.preHospitalizationFlag = preHospitalizationFlag;
	}

	public String getPostHospitalizationFlag() {
		return postHospitalizationFlag;
	}

	public void setPostHospitalizationFlag(String postHospitalizationFlag) {
		this.postHospitalizationFlag = postHospitalizationFlag;
	}

	public Long getPreHospitalizationDays() {
		return preHospitalizationDays;
	}

	public void setPreHospitalizationDays(Long preHospitalizationDays) {
		this.preHospitalizationDays = preHospitalizationDays;
	}

	public Long getPostHospitalizationDays() {
		return postHospitalizationDays;
	}

	public void setPostHospitalizationDays(Long postHospitalizationDays) {
		this.postHospitalizationDays = postHospitalizationDays;
	}

	public Long getAutoRestorationPercentage() {
		return autoRestorationPercentage;
	}

	public void setAutoRestorationPercentage(Long autoRestorationPercentage) {
		this.autoRestorationPercentage = autoRestorationPercentage;
	}

	public Long getPedWaitingPeriod() {
		return pedWaitingPeriod;
	}

	public void setPedWaitingPeriod(Long pedWaitingPeriod) {
		this.pedWaitingPeriod = pedWaitingPeriod;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	
	public String getAutoRestorationFlag() {
		return autoRestorationFlag;
	}

	public void setAutoRestorationFlag(String autoRestorationFlag) {
		this.autoRestorationFlag = autoRestorationFlag;
	}

	public String getPackageAvailableFlag() {
		return packageAvailableFlag;
	}

	public void setPackageAvailableFlag(String packageAvailableFlag) {
		this.packageAvailableFlag = packageAvailableFlag;
	}

	public String getProrataFlag() {
		return prorataFlag;
	}

	public void setProrataFlag(String prorataFlag) {
		this.prorataFlag = prorataFlag;
	}

	public String getRechargeSiFlag() {
		return rechargeSiFlag;
	}

	public void setRechargeSiFlag(String rechargeSiFlag) {
		this.rechargeSiFlag = rechargeSiFlag;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}


	public String getHospitalisationFlag() {
		return hospitalisationFlag;
	}

	public void setHospitalisationFlag(String hospitalisationFlag) {
		this.hospitalisationFlag = hospitalisationFlag;
	}

	public String getLineOfBusiness() {
		return lineOfBusiness;
	}

	public void setLineOfBusiness(String lineOfBusiness) {
		this.lineOfBusiness = lineOfBusiness;
	}

	public String getProductService() {
		return productService;
	}

	public void setProductService(String productService) {
		this.productService = productService;
	}

	public String getSectionBcoPay() {
		return sectionBcoPay;
	}

	public void setSectionBcoPay(String sectionBcoPay) {
		this.sectionBcoPay = sectionBcoPay;
	}

	public Date getEffStartDate() {
		return effStartDate;
	}

	public void setEffStartDate(Date effStartDate) {
		this.effStartDate = effStartDate;
	}

	public Date getEffEndDate() {
		return effEndDate;
	}

	public void setEffEndDate(Date effEndDate) {
		this.effEndDate = effEndDate;
	}

	public String getIntimationType() {
		return intimationType;
	}

	public void setIntimationType(String intimationType) {
		this.intimationType = intimationType;
	}
	
	public String getSourceLobId() {
		return sourceLobId;
	}
	public void setSourceLobId(String sourceLobId) {
		this.sourceLobId = sourceLobId;
	}
	public String getLobId() {
		return lobId;
	}
	public void setLobId(String lobId) {
		this.lobId = lobId;
	}

	public String getRtaRechargeSiFlag() {
		return rtaRechargeSiFlag;
	}

	public void setRtaRechargeSiFlag(String rtaRechargeSiFlag) {
		this.rtaRechargeSiFlag = rtaRechargeSiFlag;
	}

	public Long getFlpByPassFlag() {
		return flpByPassFlag;
	}

	public void setFlpByPassFlag(Long flpByPassFlag) {
		this.flpByPassFlag = flpByPassFlag;
	}

	public String getPolicyInstalmentFlag() {
		return policyInstalmentFlag;
	}

	public void setPolicyInstalmentFlag(String policyInstalmentFlag) {
		this.policyInstalmentFlag = policyInstalmentFlag;
	}
	
	public Long getDocumentToken() {
		return documentToken;
	}

	public void setDocumentToken(Long documentToken) {
		this.documentToken = documentToken;
	}

	public String getBonusApplicableFlag() {
		return bonusApplicableFlag;
	}

	public void setBonusApplicableFlag(String bonusApplicableFlag) {
		this.bonusApplicableFlag = bonusApplicableFlag;
	}

	public Long getDocumentToken2() {
		return documentToken2;
	}

	public void setDocumentToken2(Long documentToken2) {
		this.documentToken2 = documentToken2;
	}

	public String getCashlessElgFlag() {
		return cashlessElgFlag;
	}

	public void setCashlessElgFlag(String cashlessElgFlag) {
		this.cashlessElgFlag = cashlessElgFlag;
	}

	public String getOutPatientFlag() {
		return outPatientFlag;
	}

	public void setOutPatientFlag(String outPatientFlag) {
		this.outPatientFlag = outPatientFlag;
	}

	public Long getDocumentToken3() {
		return documentToken3;
	}

	public void setDocumentToken3(Long documentToken3) {
		this.documentToken3 = documentToken3;
	}

	public Long getDocumentToken4() {
		return documentToken4;
	}

	public void setDocumentToken4(Long documentToken4) {
		this.documentToken4 = documentToken4;
	}
	
	
}