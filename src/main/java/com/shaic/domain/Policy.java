package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * The persistent class for the IMS_CLS_POLICY database table.
 * 
 */
/**
 * @author Lakshminarayana
 *
 */
@Entity
@Table(name = "IMS_CLS_POLICY")
@NamedQueries({
		@NamedQuery(name = "Policy.findAll", query = "SELECT o FROM Policy o"),
		@NamedQuery(name = "Policy.findByPolicyNumber", query = "SELECT o FROM Policy o where o.policyNumber = :policyNumber"),
		@NamedQuery(name = "Policy.findByLikePolicyNumber", query = "SELECT o FROM Policy o where o.policyNumber = :policyNumber"),
		@NamedQuery(name = "Policy.findByRenewalPolicyNumber", query = "SELECT o FROM Policy o where o.policyNumber = :renewalPolicyNumber"),
		@NamedQuery(name = "Policy.findByKey", query = "SELECT o FROM Policy o where o.key = :policyKey"),
		@NamedQuery(name = "Policy.masterPolicyNumber", query = "SELECT o FROM Policy o where o.masterPolicyNumber like :masterPolicyNumber"),
		@NamedQuery(name = "Policy.findKeyByPolicyNo", query = "SELECT o FROM Policy o where o.policyNumber = :policyNumber")})

public class Policy implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name = "IMS_CLS_POLICY_KEY_GENERATOR", sequenceName = "SEQ_CLS_POLICY_KEY", allocationSize = 1)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMS_CLS_POLICY_KEY_GENERATOR")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMS_CLS_POLICY_KEY_GENERATOR")
	@GenericGenerator(name = "IMS_CLS_POLICY_KEY_GENERATOR",
    strategy = "com.shaic.domain.keygenerator.PolicyCustomKeyGenerator")
	@Column(name = "POLICY_KEY")
	private Long key;

	@Column(nullable = true, columnDefinition = "NUMBER", name="ACTIVE_STATUS", length = 1)
	private Boolean activeStatus;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

//	@Column(name = "HEALTH_CARD_NUMBER")
//	private String healthCardNumber;

	@Column(name = "ISSUING_OFFICE_CODE")
	private String homeOfficeCode;
	
	@Column(name = "POLICY_SYS_ID")
	private Long policySystemId;

//	TODO  to check for table reference column and remove transient. 
	@Transient
	private List<Insured> insured;
	
	@Transient
	private List<Insured> insuredPA;
	
	@Transient
	private List<PolicyRiskCover> policyRiskCoverDetails;
	
	@Transient
	private List<PolicyCoverDetails> policyCoverDetails;
	
	@Transient
	private List<PolicyBankDetails> policyBankDetails;
	
	@Transient
	private List<GpaBenefitDetails> gpaBenefitDetails;
	
	@Transient
	private Long unNamedInsuredId;
	
	@Transient
	private List<MasAilmentLimit> ailmentDetails;
	
	@Transient
	private List<MasCopayLimit> copayLimit;
	
	@Transient
	private List<MasDeliveryExpLimit> deliveryExpLimit;
	
	@Transient
	private List<MasPrePostHospLimit> prePostLimit;
	
	@Transient
	private List<MasRoomRentLimit> roomRentLimit;
	
	@Column(name = "POLICY_PLAN")
	private String policyPlan;

	@Column(name = "POL_ISSUING_ZONE")
//	@Transient
	private String policyZone;

	/*@Temporal(TemporalType.DATE)
	@Column(name = "INSURED_DOB")
	private Date insuredDob;*/

/*	@Column(name = "INSURED_ID")
	private String insuredId;*/

	/*@Column(name = "INSURED_LAST_NAME")
	private String insuredLastName;

	@Column(name = "INSURED_MIDDLE_NAME")
	private String insuredMiddleName;*/

/*	@Column(name = "INSURED_SUM_INSURED")
	private Double insuredSumInsured;*/

	@Column(name = "LOB_ID")
	private Long lobId;
	
	/*@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "POLICY_FROM_DATE")
	private Date policyFromDate;*/
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "POLICY_START_DATE")
	private Date policyFromDate;

	@Column(name = "POLICY_NUMBER")
	private String policyNumber;

	@Column(name = "POLICY_STATUS")
	private String policyStatus;

	/*@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "POLICY_TO_DATE")
	private Date policyToDate;*/
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "POLICY_END_DATE")
	private Date policyToDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECEIPT_DATE")
	private Date receiptDate;

	@Column(name = "RECEIPT_NUMBER")
	private String receiptNumber;

	@Column(name = "RENEWAL_POLICY_NUMBER")
	private String renewalPolicyNumber;

	
	@Column(name = "POLICY_ENDORSEMENT_NUMBER")
	private String endorsementNumber;

	@OneToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "PRODUCT_ID", nullable = false)
	@JoinColumn(name = "PRODUCT_ID", nullable = true)
	private Product product;
	
	@Column(name = "PRODUCT_NAME")
	private String productName;
	
	@Column(name = "PRODUCT_SCHEME_ID")
	private Long schemeId;
	
	@Transient
	private Double innerLimit;
	
	@Transient
	private List<Insured> dependentInsuredId;


/*	@Column(name = "PROPOSER_FIRST_NAME")
	private String proposerFirstName;

	@Column(name = "PROPOSER_LAST_NAME")
	private String proposerLastName;

	@Column(name = "PROPOSER_MIDDLE_NAME")
	private String proposerMiddleName;
*/
	
	@Column(name = "PROPOSER_NAME")
	private String proposerFirstName;
		
	@JoinColumn(name="GENDER_ID")
	@OneToOne(fetch = FetchType.LAZY)
	private MastersValue proposerGender;
	
	@Column(name = "PORTED_POLICY")
	private String portedPolicy;
	
	@Column(name = "Communication_Type")
	private String communicationType;

	@Column(name = "Payment_Party")
	private String paymentParty;
	
	@Column(name = "GMC_Policy_Type")
	private String gmcPolicyType;
	
	@Transient
	private List<GmcCoorporateBufferLimit> gmcCorpBufferLimit;

	public String getProposerFirstName() {
		return proposerFirstName;
	}

	public void setProposerFirstName(String proposerFirstName) {
		this.proposerFirstName = proposerFirstName;
	}

	@Column(name = "PROPOSER_CODE")
	private String proposerCode;
	
	@Column(name = "PROPOSER_DOB")
	private Date proposerDob;
	
	/*@Column(name = "PROPOSER_ADDRESS")
	private String prosperAddress;*/
	

	
	// @JoinColumn(name = "RELATIONSHIP_WITH_INSURED_ID", nullable = false)
	

	// @Column(name = "RELATIONSHIP_WITH_PROPOSER_ID")
	// private Long relationshipWithProposerId;

	
	@Column(name = "SM_CODE")
	private String smCode;

	@Column(name = "SM_NAME")
	private String smName;

	@Column(name = "AGENT_CODE")
	private String agentCode;

	@Column(name = "AGENT_NAME")
	private String agentName;

	
	@Column (name = "SUM_INSURED")
	private Double totalSumInsured;
	
	/*@Column(name = "TOTAL_SUM_INSURED")
	private Double totalSumInsured;*/

	@OneToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "POLICY_TYPE_ID", nullable = false)
	@JoinColumn(name = "POLICY_TYPE_ID", nullable = true)
	private MastersValue policyType;

	@OneToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "PRODUCT_TYPE_ID", nullable = false)
	@JoinColumn(name = "PRODUCT_TYPE_ID", nullable = true)
	private MastersValue productType;

	@Column(name = "RECHARGED_SI")
	private Double rechargeSI;

	@Column(name = "RESTORED_SI")
	private Double restoredSI;
	
	@Column(name = "SUM_INSURED_II")
	private Double sumInsuredII;

/*	@Column(name = "COPAY")
	private Integer copay;
*/
	
	@Column(name = "COPAY_PERCENTAGE")
	private Long copay;
	
	@Column(name = "CUMULATIVE_BONUS")
	private Double cummulativeBonus;

	
//	@Column(name = "PROPOSER_ADDRESS")
	@Transient
	private String proposerAddress;
	
	@Column(name = "PROPOSER_ADDRESS_LINE1")
	private String proposerAddress1;
	
	@Column(name = "PROPOSER_ADDRESS_LINE2")
	private String proposerAddress2;
	
	@Column(name = "PROPOSER_ADDRESS_LINE3")
	private String proposerAddress3;
	
	@Column(name = "PROPOSER_OFFICE_ADDRESS_LINE1")
	private String polOfficeAddr1;
	
	@Column(name = "PROPOSER_OFFICE_ADDRESS_LINE2")
	private String polOfficeAddr2;
	
	@Column(name = "PROPOSER_OFFICE_ADDRESS_LINE3")
	private String polOfficeAddr3;
	
	/* @Column(name = "ADDRESS_2")
	private String polAddr2;

	@Column(name = "ADDRESS_3")
	private String polAddr3;*/

	@Column(name = "PROPOSER_TELEPHONE_NUMBER")
	private String polTelephoneNumber;

	@Column(name = "PROPOSER_FAX_NUMBER")
	private String polFaxnumber;

	@Column(name = "PROPOSER_EMAIL_ID")
	private String polEmailId;

	@Column(name = "REGISTERED_MOBILE_NUMBER")
	private String registeredMobileNumber;

//	@Column(name = "PROPOSER_OFFICE_ADDRESS")
	@Transient
	private String polOfficeAddr;
	
	

	/*@Column(name = "OFFICE_ADDRESS_1")
	private String polOfficeAddr1;

	@Column(name = "OFFICE_ADDRESS_2")
	private String polOfficeAddr2;

	@Column(name = "OFFICE_ADDRESS_3")
	private String polOfficeAddr3;
*/
	@Column(name = "PROPOSER_OFFICE_TELEPHONE_NO")
	private String officeTelephone;

	@Column(name = "PROPOSER_OFFICE_FAX_NUMBER")
	private String officeFax;

	@Column(name = "PROPOSER_OFFICE_EMAIL_ID")
	private String officeEmailId;

	@Column(name = "GROSS_PREMIUM")
	private Double grossPremium;

	@Column(name = "TOTAL_PREMIUM_AMOUNT")
	private Double totalPremium;

	@Column(name = "PREMIUM_TAX")
	private Double premiumTax;

	@Column(name = "STAMP_DUTY")
	private Double stampDuty;
	
	@Column(name="SI_DEDUCTIBLE")
	private Double deductibleAmount;

	
	@Column(name = "JUNIOR_MARKETING_CODE")
	private String juniorMarketingCode;
	
	@Column(name = "JUNIOR_MARKETING_NAME")
	private String juniorMarketingName;
	
	@Column(name = "UNDERWRITING_YEAR")
	private Long policyYear;
	
	@Column(name = "POLICY_TERM")
	private Long policyTerm;


	@Column(name = "DISTRICT")
	private String proposerDist;
	
	@Column(name = "SUB_DISTRICT")
	private String proposerSubDist;
	
	@Column(name = "CITY")
	private String proposerCity;	
	
	@Column(name = "POLICY_PINCODE")
	private Long proposerPinCode;
	
	@Column(name = "STATE")
	private String proposerState;

	@Column(name = "CFT_DTLS")
	private String cftDetails;
	
	@Column(name = "POL_PREMI_CURRENCY")
	private String policyPremiaCurrency;
	
	@Column(name = "POL_SI_CURRENCY")
	private String policySiCurrency;

	@Column(name = "PREMIA_CLM_COUNT")
	private Long premiaClaimCount;
	
	/**
	 * added column for GPA
	 */
	
	@Column(name = "PAYMENTMADE")
	private Integer paymentMade;
	
	@Column(name = "PML")
	private Integer pml;
	
	@Column(name = "PROPOSERTITLE")
	private String proposerTitle;
	
	@Column(name = "GP_Policy_Type")
	private String gpaPolicyType;

	@Column(name = "PROPOSER_PAN_NUMBER")
	private String proposedPanNumber;
	
	@Column(name = "PRO_PANCARD_REMARKS")
	private String proPanCardRemarks;
	
	@Column(name="STOPLOSSPERCENTAGE")
	private Double stopLossPercentage;
	
	@Column(name="CORPORAT_BUFFER")
	private Double corporateBuffer;
	
	@Column(name="SECTION_CODE")
	private String sectionCode;
	
	@Column(name="SECTION_DESCRIPTION")
	private String sectionDescription;
	
	//Jet privillage
	@Column(name="MASTER_POLICY_NUMBER")
	private String masterPolicyNumber;
	
	@Column(name="LINKPOLICYNO")
	private String linkPolicyNumber;
	
	//BonusDetails
	@Column(name="CHEQUE_STATUS_FLAG")
	private String chequeStatus;

	@Column(name="POLICY_SOURCE")
	private String policySource;
	
	@Transient
	private List<PolicyNominee> proposerNomineeDetails;
	
	@Transient
	private List<RelatedPolicies> relatedPolicies;
	
	@Column(name = "PHC_BENEFIT_DAYS")
	private Integer phcBenefitDays;
	

	@Column(name = "HOSPITAL_CASH_BENEFIT_FLAG")
	private String hospitalCashBenefits;

	@Column(name="INSTALMENT_FLAG")
	private String policyInstalmentFlag;
	
	@Column(name = "INSTALMENT_TYPE")
	private String policyInstalmentType;
	
	
	@Column(name = "BASE_POLICY_NO")
	private String basePolicyNo;
	
	@Column(name = "IRDA_CODE")
	private String premiaUINCode;
	
	@Column(name = "OP_ALLOW_INTIMATION")
	private String opAllowIntimation;
	
	@Column(name = "OP_ALLOW_INTIMATION_REMARKS")
	private String OpAllowIntimationRemarks ;
	
	@Column(name=" NACB_BUFFER_AMT")
	private Double nacbBufferAmt;
	
	@Column(name="WINTAGE_BUFFER_AMT")
	private Double wintageBufferAmt;
	
	@Column(name = "TOPUP_POLICY_NO")
	private String topupPolicyNo;
	

	public Policy() {
	}

	public Boolean getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(Boolean activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	

	public String getCreatedBy() {
		return this.createdBy;
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

	public String getHomeOfficeCode() {
		return this.homeOfficeCode;
	}

	public void setHomeOfficeCode(String homeOfficeCode) {
		this.homeOfficeCode = homeOfficeCode;
	}

	

	

	

	
/*
	public Double getInsuredSumInsured() {
		return this.insuredSumInsured;
	}

	public void setInsuredSumInsured(Double insuredSumInsured) {
		this.insuredSumInsured = insuredSumInsured;
	}*/

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

/*	public String getProposerFirstName() {
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
	}*/

	

	// public Long getRelationshipWithProposerId() {
	// return this.relationshipWithProposerId;
	// }
	//
	// public void setRelationshipWithProposerId(
	// Long relationshipWithProposerId) {
	// this.relationshipWithProposerId = relationshipWithProposerId;
	// }

	

	public Double getTotalSumInsured() {
		return this.totalSumInsured;
	}

	public void setTotalSumInsured(Double totalSumInsured) {
		this.totalSumInsured = totalSumInsured;
	}

	public MastersValue getPolicyType() {
		return policyType;
	}

	public void setPolicyType(MastersValue policyType) {
		this.policyType = policyType;
	}

	public Double getDeductibleAmount() {
		return deductibleAmount;
	}

	public void setDeductibleAmount(Double deductibleAmount) {
		this.deductibleAmount = deductibleAmount;
	}

	public MastersValue getProductType() {
		return productType;
	}

	public void setProductType(MastersValue productType) {
		this.productType = productType;
	}

	public Double getRechargeSI() {
		return rechargeSI;
	}

	public void setRechargeSI(Double rechargeSI) {
		this.rechargeSI = rechargeSI;
	}

	public Double getRestoredSI() {
		return restoredSI;
	}

	public void setRestoredSI(Double restoredSI) {
		this.restoredSI = restoredSI;
	}

	

	public Double getSumInsuredII() {
		return sumInsuredII;
	}

	public void setSumInsuredII(Double sumInsuredII) {
		this.sumInsuredII = sumInsuredII;
	}

	public String getSmCode() {
		return smCode;
	}

	public void setSmCode(String smCode) {
		this.smCode = smCode;
	}

	public String getSmName() {
		return smName;
	}

	public void setSmName(String smName) {
		this.smName = smName;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public Long getCopay() {
		return copay;
	}

	public void setCopay(Long copay) {
		this.copay = copay;
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

	public String getRenewalPolicyNumber() {
		return renewalPolicyNumber;
	}

	public void setRenewalPolicyNumber(String renewalPolicyNumber) {
		this.renewalPolicyNumber = renewalPolicyNumber;
	}

	

	public String getEndorsementNumber() {
		return endorsementNumber;
	}

	public void setEndorsementNumber(String endorsementNumber) {
		this.endorsementNumber = endorsementNumber;
	}

	public String getProposerCode() {
		return proposerCode;
	}

	public void setProposerCode(String proposerCode) {
		this.proposerCode = proposerCode;
	}

	public Double getCummulativeBonus() {
		return cummulativeBonus;
	}

	public void setCummulativeBonus(Double cummulativeBonus) {
		this.cummulativeBonus = cummulativeBonus;
	}

	/*public String getPolAddr1() {
		return polAddr1;
	}

	public void setPolAddr1(String polAddr1) {
		this.polAddr1 = polAddr1;
	}

	public String getPolAddr2() {
		return polAddr2;
	}

	public void setPolAddr2(String polAddr2) {
		this.polAddr2 = polAddr2;
	}

	public String getPolAddr3() {
		return polAddr3;
	}

	public void setPolAddr3(String polAddr3) {
		this.polAddr3 = polAddr3;
	}*/

	public String getPolTelephoneNumber() {
		return polTelephoneNumber;
	}

	public void setPolTelephoneNumber(String polTelephoneNumber) {
		this.polTelephoneNumber = polTelephoneNumber;
	}

	public String getPolFaxnumber() {
		return polFaxnumber;
	}

	public void setPolFaxnumber(String polFaxnumber) {
		this.polFaxnumber = polFaxnumber;
	}

	public String getPolEmailId() {
		return polEmailId;
	}

	public void setPolEmailId(String polEmailId) {
		this.polEmailId = polEmailId;
	}

	public String getRegisteredMobileNumber() {
		return registeredMobileNumber;
	}

	public void setRegisteredMobileNumber(String registeredMobileNumber) {
		this.registeredMobileNumber = registeredMobileNumber;
	}

	public String getPolOfficeAddr1() {
		return polOfficeAddr1;
	}

	public void setPolOfficeAddr1(String polOfficeAddr1) {
		this.polOfficeAddr1 = polOfficeAddr1;
	}

	public String getPolOfficeAddr2() {
		return polOfficeAddr2;
	}

	public void setPolOfficeAddr2(String polOfficeAddr2) {
		this.polOfficeAddr2 = polOfficeAddr2;
	}

	public String getPolOfficeAddr3() {
		return polOfficeAddr3;
	}	
	
	public String getOfficeTelephone() {
		return officeTelephone;
	}

	public void setOfficeTelephone(String officeTelephone) {
		this.officeTelephone = officeTelephone;
	}

	public String getOfficeFax() {
		return officeFax;
	}

	public void setOfficeFax(String officeFax) {
		this.officeFax = officeFax;
	}

	public String getOfficeEmailId() {
		return officeEmailId;
	}

	public void setOfficeEmailId(String officeEmailId) {
		this.officeEmailId = officeEmailId;
	}

	public Double getGrossPremium() {
		return grossPremium;
	}

	public void setGrossPremium(Double grossPremium) {
		this.grossPremium = grossPremium;
	}

	public Double getTotalPremium() {
		return totalPremium;
	}

	public void setTotalPremium(Double totalPremium) {
		this.totalPremium = totalPremium;
	}

	public Double getPremiumTax() {
		return premiumTax;
	}

	public void setPremiumTax(Double premiumTax) {
		this.premiumTax = premiumTax;
	}

	public Double getStampDuty() {
		return stampDuty;
	}

	public void setStampDuty(Double stampDuty) {
		this.stampDuty = stampDuty;
	}

	

	public List<Insured> getInsured() {
		return insured;
	}

	public void setInsured(List<Insured> insured) {
		this.insured = insured;
	}

	
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Date getProposerDob() {
		return proposerDob;
	}

	public void setProposerDob(Date proposerDob) {
		this.proposerDob = proposerDob;
	}
	
	public String getJuniorMarketingCode() {
		return juniorMarketingCode;
	}

	public void setJuniorMarketingCode(String juniorMarketingCode) {
		this.juniorMarketingCode = juniorMarketingCode;
	}

	public String getJuniorMarketingName() {
		return juniorMarketingName;
	}

	public void setJuniorMarketingName(String juniorMarketingName) {
		this.juniorMarketingName = juniorMarketingName;
	}

	public String getProposerAddress() {
		
		String address = (proposerAddress1 != null ? proposerAddress1 : "") + (proposerAddress2 != null ? proposerAddress2 : "") + (proposerAddress3 != null ? proposerAddress3 : "" );

		this.setProposerAddress(address);
		
		return proposerAddress;
	}

	public void setProposerAddress(String proposerAddress) {
		this.proposerAddress = proposerAddress;
	}

	public String getProposerAddress1() {
		return proposerAddress1;
	}

	public void setProposerAddress1(String proposerAddress1) {
		this.proposerAddress1 = proposerAddress1;
	}

	public String getProposerAddress2() {
		return proposerAddress2;
	}

	public void setProposerAddress2(String proposerAddress2) {
		this.proposerAddress2 = proposerAddress2;
	}

	public String getProposerAddress3() {
		return proposerAddress3;
	}

	public void setProposerAddress3(String proposerAddress3) {
		this.proposerAddress3 = proposerAddress3;
		
	}
	

	public void setPolOfficeAddr3(String officeAddress3) {
		this.polOfficeAddr3 = officeAddress3;	
		
		
	}

	public Long getPolicyYear() {
		return policyYear;
	}

	public void setPolicyYear(Long policyYear) {
		this.policyYear = policyYear;
	}

	public String getPolOfficeAddr() {
		
		String address = (polOfficeAddr1 != null ? polOfficeAddr1 +", ": "") + (polOfficeAddr2 != null ? polOfficeAddr2 + ", ": "") + (polOfficeAddr3 != null ? polOfficeAddr3 : "" );
		this.setPolOfficeAddr(address);
		
		return polOfficeAddr;
	}

	public void setPolOfficeAddr(String polOfficeAddr) {
		this.polOfficeAddr = polOfficeAddr;
	}

	public Long getSchemeId() {
		return schemeId;
	}

	public void setSchemeId(Long schemeId) {
		this.schemeId = schemeId;
	}

	public Long getPolicySystemId() {
		return policySystemId;
	}

	public void setPolicySystemId(Long policySystemId) {
		this.policySystemId = policySystemId;
	}

	public String getPolicyPlan() {
		return policyPlan;
	}

	public void setPolicyPlan(String policyPlan) {
		this.policyPlan = policyPlan;
		
	}

	public String getPolicyZone() {
		return policyZone;
	}

	public void setPolicyZone(String policyZone) {
		this.policyZone = policyZone;
	}

	public Long getPolicyTerm() {
		return policyTerm;
	}

	public void setPolicyTerm(Long policyTerm) {
		this.policyTerm = policyTerm;
	}
	


	public String getProposerState() {
		return proposerState;
	}

	public void setProposerState(String proposerState) {
		this.proposerState = proposerState;
	}

	public String getProposerDist() {
		return proposerDist;
	}

	public void setProposerDist(String proposerDist) {
		this.proposerDist = proposerDist;
	}

	public String getProposerSubDist() {
		return proposerSubDist;
	}

	public void setProposerSubDist(String proposerSubDist) {
		this.proposerSubDist = proposerSubDist;
	}

	public String getProposerCity() {
		return proposerCity;
	}

	public void setProposerCity(String proposerCity) {
		this.proposerCity = proposerCity;
	}

	public Long getProposerPinCode() {
		return proposerPinCode;
	}

	public void setProposerPinCode(Long proposerPinCode) {
		this.proposerPinCode = proposerPinCode;
	}

	public List<PolicyRiskCover> getPolicyRiskCoverDetails() {
		return policyRiskCoverDetails;
	}

	public void setPolicyRiskCoverDetails(
			List<PolicyRiskCover> policyRiskCoverDetails) {
		this.policyRiskCoverDetails = policyRiskCoverDetails;
	}

	public List<PolicyBankDetails> getPolicyBankDetails() {
		return policyBankDetails;
	}

	public void setPolicyBankDetails(List<PolicyBankDetails> policyBankDetails) {
		this.policyBankDetails = policyBankDetails;
	}

	public List<Insured> getInsuredPA() {
		return insuredPA;
	}

	public void setInsuredPA(List<Insured> insuredPA) {
		this.insuredPA = insuredPA;
	}

	public String getCftDetails() {
		return cftDetails;
	}

	public void setCftDetails(String cftDetails) {
		this.cftDetails = cftDetails;
	}

	public String getPolicyPremiaCurrency() {
		return policyPremiaCurrency;
	}

	public void setPolicyPremiaCurrency(String policyPremiaCurrency) {
		this.policyPremiaCurrency = policyPremiaCurrency;
	}

	public String getPolicySiCurrency() {
		return policySiCurrency;
	}

	public void setPolicySiCurrency(String policySiCurrency) {
		this.policySiCurrency = policySiCurrency;
	}

	public Long getPremiaClaimCount() {
		return premiaClaimCount;
	}

	public void setPremiaClaimCount(Long premiaClaimCount) {
		this.premiaClaimCount = premiaClaimCount;
	}
	public String getProposedPanNumber() {
		return proposedPanNumber;
	}

	public void setProposedPanNumber(String proposedPanNumber) {
		this.proposedPanNumber = proposedPanNumber;
	}

	public String getProPanCardRemarks() {
		return proPanCardRemarks;
	}

	public void setProPanCardRemarks(String proPanCardRemarks) {
		this.proPanCardRemarks = proPanCardRemarks;
	}

	public Double getStopLossPercentage() {
		return stopLossPercentage;
	}

	public void setStopLossPercentage(Double stopLossPercentage) {
		this.stopLossPercentage = stopLossPercentage;
	}

	public Double getCorporateBuffer() {
		return corporateBuffer;
	}

	public void setCorporateBuffer(Double corporateBuffer) {
		this.corporateBuffer = corporateBuffer;
	}

	public List<MasAilmentLimit> getAilmentDetails() {
		return ailmentDetails;
	}

	public Integer getPaymentMade() {
		return paymentMade;
	}

	public void setPaymentMade(Integer paymentMade) {
		this.paymentMade = paymentMade;
	}

	public Integer getPml() {
		return pml;
	}

	public void setPml(Integer pml) {
		this.pml = pml;
	}

	public String getProposerTitle() {
		return proposerTitle;
	}

	public void setProposerTitle(String proposerTitle) {
		this.proposerTitle = proposerTitle;
	}

	public String getGpaPolicyType() {
		return gpaPolicyType;
	}

	public void setGpaPolicyType(String gpaPolicyType) {
		this.gpaPolicyType = gpaPolicyType;
	}

	public List<GpaBenefitDetails> getGpaBenefitDetails() {
		return gpaBenefitDetails;
	}

	public void setGpaBenefitDetails(List<GpaBenefitDetails> gpaBenefitDetails) {
		this.gpaBenefitDetails = gpaBenefitDetails;
	}
	public void setAilmentDetails(List<MasAilmentLimit> ailmentDetails) {
		this.ailmentDetails = ailmentDetails;
	}

	public List<MasCopayLimit> getCopayLimit() {
		return copayLimit;
	}

	public void setCopayLimit(List<MasCopayLimit> copayLimit) {
		this.copayLimit = copayLimit;
	}

	public List<MasDeliveryExpLimit> getDeliveryExpLimit() {
		return deliveryExpLimit;
	}

	public void setDeliveryExpLimit(List<MasDeliveryExpLimit> deliveryExpLimit) {
		this.deliveryExpLimit = deliveryExpLimit;
	}

	public List<MasPrePostHospLimit> getPrePostLimit() {
		return prePostLimit;
	}

	public void setPrePostLimit(List<MasPrePostHospLimit> prePostLimit) {
		this.prePostLimit = prePostLimit;
	}

	public List<MasRoomRentLimit> getRoomRentLimit() {
		return roomRentLimit;
	}

	public void setRoomRentLimit(List<MasRoomRentLimit> roomRentLimit) {
		this.roomRentLimit = roomRentLimit;
	}

	public Double getInnerLimit() {
		return innerLimit;
	}

	public void setInnerLimit(Double innerLimit) {
		this.innerLimit = innerLimit;
	}

	public List<Insured> getDependentInsuredId() {
		return dependentInsuredId;
	}

	public void setDependentInsuredId(List<Insured> dependentInsuredId) {
		this.dependentInsuredId = dependentInsuredId;
	}

	public String getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}

	public String getSectionDescription() {
		return sectionDescription;
	}

	public void setSectionDescription(String sectionDescription) {
		this.sectionDescription = sectionDescription;
	}

	public Long getUnNamedInsuredId() {
		return unNamedInsuredId;
	}

	public void setUnNamedInsuredId(Long unNamedInsuredId) {
		this.unNamedInsuredId = unNamedInsuredId;
	}

	public String getMasterPolicyNumber() {
		return masterPolicyNumber;
	}

	public void setMasterPolicyNumber(String masterPolicyNumber) {
		this.masterPolicyNumber = masterPolicyNumber;
	}

	public String getLinkPolicyNumber() {
		return linkPolicyNumber;
	}

	public void setLinkPolicyNumber(String linkPolicyNumber) {
		this.linkPolicyNumber = linkPolicyNumber;
	}

	public List<PolicyCoverDetails> getPolicyCoverDetails() {
		return policyCoverDetails;
	}

	public void setPolicyCoverDetails(List<PolicyCoverDetails> policyCoverDetails) {
		this.policyCoverDetails = policyCoverDetails;
	}

	
//	public String getProposerState() {
//		return proposerState;
//	}
//
//	public void setProposerState(String proposerState) {
//		this.proposerState = proposerState;
//	}
//
//	public String getProposerDist() {
//		return proposerDist;
//	}
//
//	public void setProposerDist(String proposerDist) {
//		this.proposerDist = proposerDist;
//	}
//
//	public String getProposerSubDist() {
//		return proposerSubDist;
//	}
//
//	public void setProposerSubDist(String proposerSubDist) {
//		this.proposerSubDist = proposerSubDist;
//	}
//
//	public String getProposerCity() {
//		return proposerCity;
//	}
//
//	public void setProposerCity(String proposerCity) {
//		this.proposerCity = proposerCity;
//	}
//
//	public Long getProposerPinCode() {
//		return proposerPinCode;
//	}
//
//	public void setProposerPinCode(Long proposerPinCode) {
//		this.proposerPinCode = proposerPinCode;
//	}
	
	
	public MastersValue getProposerGender() {
		return proposerGender;
	}

	public void setProposerGender(MastersValue proposerGender) {
		this.proposerGender = proposerGender;
	}

	public String getPortedPolicy() {
		return portedPolicy;
	}

	public void setPortedPolicy(String portedPolicy) {
		this.portedPolicy = portedPolicy;
	}

	public String getCommunicationType() {
		return communicationType;
	}

	public void setCommunicationType(String communicationType) {
		this.communicationType = communicationType;
	}

	public String getPaymentParty() {
		return paymentParty;
	}

	public void setPaymentParty(String paymentParty) {
		this.paymentParty = paymentParty;
	}

	public String getGmcPolicyType() {
		return gmcPolicyType;
	}

	public void setGmcPolicyType(String gmcPolicyType) {
		this.gmcPolicyType = gmcPolicyType;
	}

	public String getChequeStatus() {
		return chequeStatus;
	}

	public void setChequeStatus(String chequeStatus) {
		this.chequeStatus = chequeStatus;
	}

	public List<PolicyNominee> getProposerNomineeDetails() {
		return proposerNomineeDetails;
	}

	public void setProposerNomineeDetails(
			List<PolicyNominee> proposerNomineeDetails) {
		this.proposerNomineeDetails = proposerNomineeDetails;
	}

	public List<RelatedPolicies> getRelatedPolicies() {
		return relatedPolicies;
	}

	public void setRelatedPolicies(List<RelatedPolicies> relatedPolicies) {
		this.relatedPolicies = relatedPolicies;
	}

	public String getPolicySource() {
		return policySource;
	}

	public void setPolicySource(String policySource) {
		this.policySource = policySource;
	}

	public Integer getPhcBenefitDays() {
		return phcBenefitDays;
	}

	public void setPhcBenefitDays(Integer phcBenefitDays) {
		this.phcBenefitDays = phcBenefitDays;
	}
	
	public String getHospitalCashBenefits() {
		return hospitalCashBenefits;
	}

	public void setHospitalCashBenefits(String hospitalCashBenefits) {
		if(hospitalCashBenefits != null && hospitalCashBenefits.equalsIgnoreCase("1")){
	    	this.hospitalCashBenefits = "Y";
	    }else{
	    	this.hospitalCashBenefits = "N";
	    }
	}

	public String getPolicyInstalmentFlag() {
		return policyInstalmentFlag;
	}

	public void setPolicyInstalmentFlag(String policyInstalmentFlag) {
		this.policyInstalmentFlag = policyInstalmentFlag;
	}

	public String getPolicyInstalmentType() {
		return policyInstalmentType;
	}

	public void setPolicyInstalmentType(String policyInstalmentType) {
		this.policyInstalmentType = policyInstalmentType;
	}
	
	public String getBasePolicyNo() {
		return basePolicyNo;
	}

	public void setBasePolicyNo(String basePolicyNo) {
		this.basePolicyNo = basePolicyNo;
	}

	public String getPremiaUINCode() {
		return premiaUINCode;
	}

	public void setPremiaUINCode(String premiaUINCode) {
		this.premiaUINCode = premiaUINCode;
	}
	public List<GmcCoorporateBufferLimit> getGmcCorpBufferLimit() {
		return gmcCorpBufferLimit;
	}

	public void setGmcCorpBufferLimit(
			List<GmcCoorporateBufferLimit> gmcCorpBufferLimit) {
		this.gmcCorpBufferLimit = gmcCorpBufferLimit;
	}

	public Double getNacbBufferAmt() {
		return nacbBufferAmt;
	}

	public void setNacbBufferAmt(Double nacbBufferAmt) {
		this.nacbBufferAmt = nacbBufferAmt;
	}

	public Double getWintageBufferAmt() {
		return wintageBufferAmt;
	}

	public void setWintageBufferAmt(Double wintageBufferAmt) {
		this.wintageBufferAmt = wintageBufferAmt;
	}

	public String getOpAllowIntimation() {
		return opAllowIntimation;
	}

	public void setOpAllowIntimation(String opAllowIntimation) {
		this.opAllowIntimation = opAllowIntimation;
	}

	public String getOpAllowIntimationRemarks() {
		return OpAllowIntimationRemarks;
	}

	public void setOpAllowIntimationRemarks(String opAllowIntimationRemarks) {
		OpAllowIntimationRemarks = opAllowIntimationRemarks;
	}

	public String getTopupPolicyNo() {
		return topupPolicyNo;
	}

	public void setTopupPolicyNo(String topupPolicyNo) {
		this.topupPolicyNo = topupPolicyNo;
	}
	
}