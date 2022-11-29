package com.shaic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;


@Entity
@Table(name="IMS_CLS_LEGAL_ADVOCATE_FEE")
@NamedQueries({
@NamedQuery(name="LegalAdvocateFee.findAll", query="SELECT l FROM LegalAdvocateFee l"),
@NamedQuery(name ="LegalAdvocateFee.findByKey",query="SELECT l FROM LegalAdvocateFee l WHERE l.key = :primaryKey"),
@NamedQuery(name ="LegalAdvocateFee.findByIntimationNumber",query="SELECT l FROM LegalAdvocateFee l WHERE l.intimationNumber = :intimationNumber"),
@NamedQuery(name ="LegalAdvocateFee.findByIntimationNumberAndType",query="SELECT l FROM LegalAdvocateFee l WHERE l.intimationNumber = :intimationNumber and l.legalType = :legalType")})
public class LegalAdvocateFee extends AbstractEntity{
	
	/**
	 * 
	 */
	

	private static final long serialVersionUID = 3799807063257179012L;

	@Id
	@SequenceGenerator(name="IMS_CLS_LEGAL_ADVOCATE_FEE_GENERATOR", sequenceName = "SEQ_ADV_FEE_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_LEGAL_ADVOCATE_FEE_GENERATOR" ) 
	@Column(name="ADV_FEE_KEY", updatable=false)
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLAIM_KEY", nullable = true)
	private Claim claimKey;
	
	@Column(name="INTIMATION_NUMBER")
	private String intimationNumber;
	
	@Column(name="POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name="PRODUCT_NAME")
	private String productName;
	
	@Column(name="INSURED_NAME")
	private String insuredName;
	
	@Column(name="FINANCIAL_YEAR")
	private String financialYear;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REPUDIATION_ID", nullable = true)
	private MastersValue repudiationId;
	
	@Column(name="PROVISION_AMOUNT")
	private	Double	provisionAmount;
	
	@Column(name="LEGAL_TYPE")
	private String legalType;
	
	@Column(name="DCDRF_REMARKS")
	private	String	dcdrfRemarks;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ZONE_ID", nullable = true)
	private	TmpCPUCode	zoneId;
	
	@Column(name="CC_NO")
	private	String	ccNo;
	
	@Column(name="ADVOCATE_NAME")
	private	String	advocateName;
	
	@Column(name="CASE_ADVOCATE_NAME")
	private	String	caseAdvocateName;
	
	@Column(name="ADVOCATE_FEE")
	private	Double	advocateFee;
	
	@Column(name="PART_PAYMENT_FLAG")
	private	Boolean	partPaymentFlag;
	
	@Column(name="FULL_PAYMENT_FLAG")
	private	Boolean	fullPaymentFlag;
	
	@Column(name="PAID_AMOUNT")
	private	Double	paidAmount;
	
	@Column(name="DD_NAME")
	private	String	ddName;
	
	@Column(name="CREATED_BY")
	private	String	createdBy;
	
	@Column(name="CREATED_DATE")
	private	Date	createdDate;
	
	@Column(name="MODIFIED_BY")
	private	String	modifiedBy;
	
	@Column(name="MODIFIED_DATE")
	private	Date	modifiedDate;
	
	@Column(name="ACTIVE_STATUS")
	private	Long	activeStatus;
	
	

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Claim getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Claim claimKey) {
		this.claimKey = claimKey;
	}
	
	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}

	public MastersValue getRepudiationId() {
		return repudiationId;
	}

	public void setRepudiationId(MastersValue repudiationId) {
		this.repudiationId = repudiationId;
	}

	public Double getProvisionAmount() {
		return provisionAmount;
	}

	public void setProvisionAmount(Double provisionAmount) {
		this.provisionAmount = provisionAmount;
	}

	public String getLegalType() {
		return legalType;
	}

	public void setLegalType(String legalType) {
		this.legalType = legalType;
	}

	public String getDcdrfRemarks() {
		return dcdrfRemarks;
	}

	public void setDcdrfRemarks(String dcdrfRemarks) {
		this.dcdrfRemarks = dcdrfRemarks;
	}

	public TmpCPUCode getZoneId() {
		return zoneId;
	}

	public void setZoneId(TmpCPUCode zoneId) {
		this.zoneId = zoneId;
	}

	public String getCcNo() {
		return ccNo;
	}

	public void setCcNo(String ccNo) {
		this.ccNo = ccNo;
	}

	public String getAdvocateName() {
		return advocateName;
	}

	public void setAdvocateName(String advocateName) {
		this.advocateName = advocateName;
	}

	public Double getAdvocateFee() {
		return advocateFee;
	}

	public void setAdvocateFee(Double advocateFee) {
		this.advocateFee = advocateFee;
	}

	public Boolean getPartPaymentFlag() {
		return partPaymentFlag;
	}

	public void setPartPaymentFlag(Boolean partPaymentFlag) {
		this.partPaymentFlag = partPaymentFlag;
	}

	public Boolean getFullPaymentFlag() {
		return fullPaymentFlag;
	}

	public void setFullPaymentFlag(Boolean fullPaymentFlag) {
		this.fullPaymentFlag = fullPaymentFlag;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getDdName() {
		return ddName;
	}

	public void setDdName(String ddName) {
		this.ddName = ddName;
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

	public String getCaseAdvocateName() {
		return caseAdvocateName;
	}

	public void setCaseAdvocateName(String caseAdvocateName) {
		this.caseAdvocateName = caseAdvocateName;
	}
	
}
