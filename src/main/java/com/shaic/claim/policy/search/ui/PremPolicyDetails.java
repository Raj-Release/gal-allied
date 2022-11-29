package com.shaic.claim.policy.search.ui;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremPolicyDetails {
	
	@JsonProperty("{")
	private String startToken;
	
	@JsonProperty("AgentCode")
	private String agentCode;
	
	@JsonProperty("AgentName")
	private String agentName;
	
	@JsonProperty("CoPay")
	private String coPay;
	
	@JsonProperty("CumulativeBonus")
	private String cumulativeBonus;
	
	@JsonProperty("EndorsementDetails")
	private List<PremEndorsementDetails> endorsementDetails;
	//private Collection<PremEndorsementDetails> endorsementDetails;
	
	@JsonProperty("InsuredDetails")
	private List<PremInsuredDetails> insuredDetails;
	//private Collection<PremInsuredDetails> insuredDetails;
	
	@JsonProperty("LOB")
	private String lob;
	
	@JsonProperty("OfficeCode")
	private String officeCode;
	
	
	@JsonProperty("PolicyEndDate")
	private String policyEndDate;
	
	@JsonProperty("PolicyEndNo")
	private String policyEndNo;
	
	@JsonProperty("PolicyNo")
	private String policyNo;
	
	@JsonProperty("PolicyStartDate")
	private String policyStartDate;
	
	@JsonProperty("PolicyStatus")
	private String policyStatus;
	
	@JsonProperty("PolicySumInsured")
	private String policySumInsured;
	
	@JsonProperty("Plan")
	private String policyPlan;
//	
	@JsonProperty("Zone")
	private String policyZone;
	
	@JsonProperty("PolicyType")
	private String policyType;
	
	@JsonProperty("PreviousPolicyDetails")
	private List<PremPreviousPolicyDetails> previousPolicyDetails;
	
	
	@JsonProperty("ProductCode")
	private String productCode;
	
	@JsonProperty("ProductName")
	private String productName;
	
	@JsonProperty("ProposerGender")
	private String proposerGender;
	
	@JsonProperty("PortedYN")
	private String portedYN;
	
	@JsonProperty("ProposerCode")
	private String proposerCode;
	
	@JsonProperty("ProposerDOB")
	private String proposerDOB;
	
	@JsonProperty("ProposerEmail")
	private String proposerEmail;
	
	@JsonProperty("ProposerFaxNo")
	private String proposerFaxNo;

	@JsonProperty("ProposerMobileNo")
	private String proposerMobileNo;
	
	@JsonProperty("ProposerName")
	private String proposerName;
	
	@JsonProperty("ProposerOfficeAddress")
	private String proposerOfficeAddress;
	
	@JsonProperty("ProposerOfficeEmail")
	private String proposerOfficeEmail;
	
	@JsonProperty("ProposerOfficeFaxNo")
	private String proposerOfficeFaxNo;
	
	@JsonProperty("ProposerOfficeTelNo")
	private String proposerOfficeTelNo;
	
	@JsonProperty("ProposerTelNo")
	private String proposerTelNo;
	
	@JsonProperty("ReceiptDate")
	private String receiptDate;
	
	@JsonProperty("ReceiptNo")
	private String receiptNo;
	
	@JsonProperty("RenewalPolicyNo")
	private String renewalPolicyNo;
	
	@JsonProperty("SMCode")
	private String smCode;
	
	@JsonProperty("SMName")
	private String smName;
	
	@JsonProperty("GrossPremium")
	private String grossPremium;
	
	@JsonProperty("DeductiableAmt")
	private String deductiableAmt;
	
	
	@JsonProperty("PolicyYr")
	private String policyYr;
	
	@JsonProperty("PaymentMade")
	private String paymentMode;
	
	@JsonProperty("PolicyTerm")
	private String policyTerm;
	
	@JsonProperty("PolType")
	private String polType;
	
	@JsonProperty("SchemeType")
	private String schemeType;
	
	@JsonProperty("ProposerTitle")
	private String proposerTitle;
	
	
	@JsonProperty("ProposerAddress1")
	private String proposerAddress1;
	
	
	@JsonProperty("ProposerAddress2")
	private String proposerAddress2;
	
	
	@JsonProperty("ProposerAddress3")
	private String proposerAddress3;
	
	@JsonProperty("ProposerOfficeAddress1")
	private String proposerOfficeAddress1;
	
	@JsonProperty("ProposerOfficeAddress2")
	private String proposerOfficeAddress2;
	
	@JsonProperty("ProposerOfficeAddress3")
	private String proposerOfficeAddress3;
	

	@JsonProperty("City")
	private String city;
	
	@JsonProperty("District")
	private String district;
	
	@JsonProperty("SubDistrict")
	private String subDistrict;

	@JsonProperty("Pincode")
	private String pinCode;
	
	@JsonProperty("State")
	private String state;
	
	@JsonProperty("PolSysID")
	private String polSysId;
	
	@JsonProperty("BankDetails")
	private List<PremBankDetails> bankDetails;
	
	@JsonProperty("SectionCode")
	private String sectionCode;
	
	@JsonProperty("SectionDescription")
	private String sectionDescription;
	
	@JsonProperty("ProposerPanNo")
	private String proposerPanNumber;
	
	@JsonProperty("CoverDetails")
	private List<PremPolicyRiskCover> premInsuredRiskCoverDetails;
	
	@JsonProperty("PA_InsuredDetails")
	private List<PremiaInsuredPA> premiaInsuredPAdetails;
	
	@JsonProperty("OMPIndividualDtls")
	private PremInsuredOMPDetails premiaInsuredOmpdetails;
	
	@JsonProperty("OMPCorporateDtls")
	private PremInsuredOMPDetails premiaInsuredOmpCorpdetails;
	
	@JsonProperty("OMPStudentDtls")
	private PremInsuredOMPDetails premiaInsuredOmpStudentdetails;
	
	@JsonProperty("JPInsuredDetails")
	private List<PremInsuredDetails> jpInsuredDetails;
		
	@JsonProperty("CFTDtls")
	private String cfdtls;
	//GMC
	
	@JsonProperty("GHCDays")
	private String ghcDays;
	
	@JsonProperty("GMCInsuredDetails")
	private List<PremInsuredDetails> gmcInsuredDetails;
	
	@JsonProperty("GMC_AilmentLimit")
	private List<PremGmcAilmentLimit> gmcAilmentLimit;
	
	@JsonProperty("GMC_CoPayLimit")
	private List<PremGmcCopayLimit> gmcCopayLimit;
	
	@JsonProperty("GMC_DeliveryExpLimit")
	private List<PremDeliveryExpLimit> gmcDeliveryLimit;
	
	@JsonProperty("GMC_PrePostHospLimit")
	private List<PremGmcPrePostLimit> gmcPrePostHospLimit;
	
	@JsonProperty("GMC_RoomRentLimit")
	private List<PremGmcRoomRentLimit> gmcRoomRentLimit;
	
	@JsonProperty("PolicyConditions")
	private List<PremGmcBenefitDetails> gmcPolicyConditions;
	
	@JsonProperty("ProposerNomineeDetails")
	private List<PremInsuredNomineeDetails> properNomineeDetails;
	
	@JsonProperty("CorporateBuffer")
	private String corporateBuffer;
	
	@JsonProperty("StopLossPercentage")
	private String stopLossPercentage;
	
	@JsonProperty("InnerLimit")
	private String innerLimit;

	/**
	 * Added new column for GPA
	 * @return
	 */
	@JsonProperty("PML")
	private String pml;
	
	@JsonProperty("GPA_InsuredDetails")
	private List<PremInsuredDetails> gpaInsuredDetails;
	
	@JsonProperty("GPA_CoverDetails")
	private List<PremPolicyRiskCover> gpaCoverDetails;
	
	
	//GPA unnamed policy
	private String riskSysId;

	@JsonProperty("PolicyPremiumCurrency")
	private String policyPremiumCurrency;
	
	@JsonProperty("PolicySICurrency")
	private String policySiCurrency;
	
	@JsonProperty("PremiaClaimCount")
	private String premiaClaimCount;
	
	
	//Jet Privillage Product
	@JsonProperty("MasterPolicyNumber")
	private String masterPolicyNumber;
	
	@JsonProperty("LinkPolicyNo")
	private String linkPolicyNo;
	
	@JsonProperty("PolicyCoverDetails")
	private List<PremPolicyCoverDetails> premPolicyCoverDetails;
	/**
	 * 
	 * R0794
	 */
	
	
	@JsonProperty("CommunicationType")
	private String communicationType;

	@JsonProperty("PaymentParty")
	private String paymentParty;
	
	@JsonProperty("GMCPolicyType")
	private String gmcPolicyType;
	
	/*@JsonProperty("ProposerNomineeDetails")
	private String proposerNomineeDetails;*/
	
	@JsonProperty("RelatedPolicies")
	private List<PremRelatedPolicies> relatedPolicies;
	
	private String baNCSofficeTelPhone;
	
	private String policySource;
	
	private String baNCSPremiumTax;
	
	private String baNCSStampDuty;
	
	private String baNCSTotalPremium;
	
	private String baNCSOfficeEmailId;
	
	private String baNCSOfficeFax;

	@JsonProperty("Days")
	private String phcDays;
	
	@JsonProperty("InstallementMethod")
	private String installementMethod;
	
	@JsonProperty("InstallementYN")
	private String instalmentFlag;
	
	@JsonProperty("BasePolicy")
	private String basePolicy;

	@JsonProperty("CovidPlan")
	private String covidPlan;
	
	@JsonProperty("UINNo")
	private String premiaUINCode;
	
	@JsonProperty("GMC_CorpBufferLimit")
	private List<PremGmcCorpBufferLimit> gmcCorpBufferLimit;
	
	@JsonProperty("NACBBuffer")
	private String nacbBuffer;
	
	@JsonProperty("VintageBuffer")
	private String vintageBuffer;
	
	@JsonProperty("AddOnPolicyNo")
	private String topUpPolicyNo;

	public String getPolType() {
		return polType;
	}

	public void setPolType(String polType) {
		this.polType = polType;
	}

	public String getSchemeType() {
		return schemeType;
	}

	public void setSchemeType(String schemeType) {
		this.schemeType = schemeType;
	}

	public String getProposerTitle() {
		return proposerTitle;
	}

	public void setProposerTitle(String proposerTitle) {
		this.proposerTitle = proposerTitle;
	}

	public String getPolicyYr() {
		return policyYr;
	}

	public void setPolicyYr(String policyYr) {
		this.policyYr = policyYr;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getPolicyTerm() {
		return policyTerm;
	}

	public void setPolicyTerm(String policyTerm) {
		this.policyTerm = policyTerm;
	}

	@JsonProperty("}")
	private String endToken;
	

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

	public String getCoPay() {
		return coPay;
	}

	public void setCoPay(String coPay) {
		this.coPay = coPay;
	}

	public String getCumulativeBonus() {
		return cumulativeBonus;
	}

	public void setCumulativeBonus(String cumulativeBonus) {
		this.cumulativeBonus = cumulativeBonus;
	}

	

	public void setInsuredDetails(ArrayList<PremInsuredDetails> insuredDetails) {
		this.insuredDetails = insuredDetails;
	}

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
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

	public String getCorporateBuffer() {
		return corporateBuffer;
	}

	public List<PremInsuredDetails> getGmcInsuredDetails() {
		return gmcInsuredDetails;
	}

	public void setGmcInsuredDetails(List<PremInsuredDetails> gmcInsuredDetails) {
		this.gmcInsuredDetails = gmcInsuredDetails;
	}

	public List<PremGmcAilmentLimit> getGmcAilmentLimit() {
		return gmcAilmentLimit;
	}

	public void setGmcAilmentLimit(List<PremGmcAilmentLimit> gmcAilmentLimit) {
		this.gmcAilmentLimit = gmcAilmentLimit;
	}

	public List<PremGmcCopayLimit> getGmcCopayLimit() {
		return gmcCopayLimit;
	}

	public void setGmcCopayLimit(List<PremGmcCopayLimit> gmcCopayLimit) {
		this.gmcCopayLimit = gmcCopayLimit;
	}

	public List<PremDeliveryExpLimit> getGmcDeliveryLimit() {
		return gmcDeliveryLimit;
	}

	public void setGmcDeliveryLimit(List<PremDeliveryExpLimit> gmcDeliveryLimit) {
		this.gmcDeliveryLimit = gmcDeliveryLimit;
	}

	public List<PremGmcPrePostLimit> getGmcPrePostHospLimit() {
		return gmcPrePostHospLimit;
	}

	public void setGmcPrePostHospLimit(List<PremGmcPrePostLimit> gmcPrePostHospLimit) {
		this.gmcPrePostHospLimit = gmcPrePostHospLimit;
	}

	public List<PremGmcRoomRentLimit> getGmcRoomRentLimit() {
		return gmcRoomRentLimit;
	}

	public void setGmcRoomRentLimit(List<PremGmcRoomRentLimit> gmcRoomRentLimit) {
		this.gmcRoomRentLimit = gmcRoomRentLimit;
	}

	public void setCorporateBuffer(String corporateBuffer) {
		this.corporateBuffer = corporateBuffer;
	}

	public String getProposerAddress3() {
		return proposerAddress3;
	}

	public void setProposerAddress3(String proposerAddress3) {
		this.proposerAddress3 = proposerAddress3;
	}

	public String getProposerOfficeAddress1() {
		return proposerOfficeAddress1;
	}

	public void setProposerOfficeAddress1(String proposerOfficeAddress1) {
		this.proposerOfficeAddress1 = proposerOfficeAddress1;
	}

	public String getProposerOfficeAddress2() {
		return proposerOfficeAddress2;
	}

	public void setProposerOfficeAddress2(String proposerOfficeAddress2) {
		this.proposerOfficeAddress2 = proposerOfficeAddress2;
	}

	public String getProposerOfficeAddress3() {
		return proposerOfficeAddress3;
	}

	public void setProposerOfficeAddress3(String proposerOfficeAddress3) {
		this.proposerOfficeAddress3 = proposerOfficeAddress3;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getPolicyEndDate() {
		return policyEndDate;
	}

	public void setPolicyEndDate(String policyEndDate) {
		this.policyEndDate = policyEndDate;
	}

	public String getPolicyEndNo() {
		return policyEndNo;
	}

	public void setPolicyEndNo(String policyEndNo) {
		this.policyEndNo = policyEndNo;
	}

	public String getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(String policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public String getPolicyStatus() {
		return policyStatus;
	}

	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}

	public String getPolicySumInsured() {
		return policySumInsured;
	}

	public void setPolicySumInsured(String policySumInsured) {
		this.policySumInsured = policySumInsured;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}


	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProposerCode() {
		return proposerCode;
	}

	public void setProposerCode(String proposerCode) {
		this.proposerCode = proposerCode;
	}

	public String getProposerDOB() {
		return proposerDOB;
	}

	public void setProposerDOB(String proposerDOB) {
		this.proposerDOB = proposerDOB;
	}

	public String getProposerEmail() {
		return proposerEmail;
	}

	public void setProposerEmail(String proposerEmail) {
		this.proposerEmail = proposerEmail;
	}

	public String getProposerFaxNo() {
		return proposerFaxNo;
	}

	public void setProposerFaxNo(String proposerFaxNo) {
		this.proposerFaxNo = proposerFaxNo;
	}

	public String getProposerMobileNo() {
		return proposerMobileNo;
	}

	public void setProposerMobileNo(String proposerMobileNo) {
		this.proposerMobileNo = proposerMobileNo;
	}

	public String getProposerName() {
		return proposerName;
	}

	public void setProposerName(String proposerName) {
		this.proposerName = proposerName;
	}

	public String getProposerOfficeAddress() {
		return proposerOfficeAddress;
	}

	public void setProposerOfficeAddress(String proposerOfficeAddress) {
		this.proposerOfficeAddress = proposerOfficeAddress;
	}

	public String getProposerOfficeEmail() {
		return proposerOfficeEmail;
	}

	public void setProposerOfficeEmail(String proposerOfficeEmail) {
		this.proposerOfficeEmail = proposerOfficeEmail;
	}

	public String getProposerOfficeFaxNo() {
		return proposerOfficeFaxNo;
	}

	public void setProposerOfficeFaxNo(String proposerOfficeFaxNo) {
		this.proposerOfficeFaxNo = proposerOfficeFaxNo;
	}

	public String getProposerOfficeTelNo() {
		return proposerOfficeTelNo;
	}

	public void setProposerOfficeTelNo(String proposerOfficeTelNo) {
		this.proposerOfficeTelNo = proposerOfficeTelNo;
	}

	public String getProposerTelNo() {
		return proposerTelNo;
	}

	public void setProposerTelNo(String proposerTelNo) {
		this.proposerTelNo = proposerTelNo;
	}

	public String getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}

	
	public String getRenewalPolicyNo() {
		return renewalPolicyNo;
	}

	public void setRenewalPolicyNo(String renewalPolicyNo) {
		this.renewalPolicyNo = renewalPolicyNo;
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

	
	public String getStartToken() {
		return startToken;
	}

	public void setStartToken(String startToken) {
		this.startToken = startToken;
	}

	public String getEndToken() {
		return endToken;
	}

	public void setEndToken(String endToken) {
		this.endToken = endToken;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public List<PremEndorsementDetails> getEndorsementDetails() {
		return endorsementDetails;
	}

	public void setEndorsementDetails(
			List<PremEndorsementDetails> endorsementDetails) {
		this.endorsementDetails = endorsementDetails;
	}

	public List<PremInsuredDetails> getInsuredDetails() {
		return insuredDetails;
	}

	public void setInsuredDetails(List<PremInsuredDetails> insuredDetails) {
		this.insuredDetails = insuredDetails;
	}

	public List<PremPreviousPolicyDetails> getPreviousPolicyDetails() {
		return previousPolicyDetails;
	}

	public void setPreviousPolicyDetails(
			List<PremPreviousPolicyDetails> previousPolicyDetails) {
		this.previousPolicyDetails = previousPolicyDetails;
	}

	public String getGrossPremium() {
		return grossPremium;
	}

	public void setGrossPremium(String grossPremium) {
		this.grossPremium = grossPremium;
	}

	public String getDeductiableAmt() {
		return deductiableAmt;
	}

	public void setDeductiableAmt(String deductiableAmt) {
		this.deductiableAmt = deductiableAmt;
	}

	public String getPolSysId() {
		return polSysId;
	}

	public void setPolSysId(String polSysId) {
		this.polSysId = polSysId;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getSubDistrict() {
		return subDistrict;
	}

	public void setSubDistrict(String subDistrict) {
		this.subDistrict = subDistrict;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<PremBankDetails> getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(List<PremBankDetails> bankDetails) {
		this.bankDetails = bankDetails;
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

	public List<PremPolicyRiskCover> getPremInsuredRiskCoverDetails() {
		return premInsuredRiskCoverDetails;
	}

	public void setPremInsuredRiskCoverDetails(
			List<PremPolicyRiskCover> premInsuredRiskCoverDetails) {
		this.premInsuredRiskCoverDetails = premInsuredRiskCoverDetails;
	}

	public List<PremiaInsuredPA> getPremiaInsuredPAdetails() {
		return premiaInsuredPAdetails;
	}

	public void setPremiaInsuredPAdetails(
			List<PremiaInsuredPA> premiaInsuredPAdetails) {
		this.premiaInsuredPAdetails = premiaInsuredPAdetails;
	}

	public PremInsuredOMPDetails getPremiaInsuredOmpdetails() {
		return premiaInsuredOmpdetails;
	}

	public void setPremiaInsuredOmpdetails(PremInsuredOMPDetails premiaInsuredOmpdetails) {
		this.premiaInsuredOmpdetails = premiaInsuredOmpdetails;
	}

	public PremInsuredOMPDetails getPremiaInsuredOmpCorpdetails() {
		return premiaInsuredOmpCorpdetails;
	}

	public void setPremiaInsuredOmpCorpdetails(
			PremInsuredOMPDetails premiaInsuredOmpCorpdetails) {
		this.premiaInsuredOmpCorpdetails = premiaInsuredOmpCorpdetails;
	}

	public PremInsuredOMPDetails getPremiaInsuredOmpStudentdetails() {
		return premiaInsuredOmpStudentdetails;
	}

	public void setPremiaInsuredOmpStudentdetails(
			PremInsuredOMPDetails premiaInsuredOmpStudentdetails) {
		this.premiaInsuredOmpStudentdetails = premiaInsuredOmpStudentdetails;
	}

	public String getCfdtls() {
		return cfdtls;
	}

	public void setCfdtls(String cfdtls) {
		this.cfdtls = cfdtls;
	}

	public String getPolicyPremiumCurrency() {
		return policyPremiumCurrency;
	}

	public void setPolicyPremiumCurrency(String policyPremiumCurrency) {
		this.policyPremiumCurrency = policyPremiumCurrency;
	}

	public String getPolicySiCurrency() {
		return policySiCurrency;
	}

	public void setPolicySiCurrency(String policySiCurrency) {
		this.policySiCurrency = policySiCurrency;
	}

	public String getPremiaClaimCount() {
		return premiaClaimCount;
	}

	public void setPremiaClaimCount(String premiaClaimCount) {
		this.premiaClaimCount = premiaClaimCount;
	}

	public String getProposerPanNumber() {
		return proposerPanNumber;
	}

	public void setProposerPanNumber(String proposerPanNumber) {
		this.proposerPanNumber = proposerPanNumber;
	}

	public String getPml() {
		return pml;
	}

	public void setPml(String pml) {
		this.pml = pml;
	}

	public List<PremInsuredDetails> getGpaInsuredDetails() {
		return gpaInsuredDetails;
	}

	public void setGpaInsuredDetails(List<PremInsuredDetails> gpaInsuredDetails) {
		this.gpaInsuredDetails = gpaInsuredDetails;
	}

	public List<PremPolicyRiskCover> getGpaCoverDetails() {
		return gpaCoverDetails;
	}

	public void setGpaCoverDetails(List<PremPolicyRiskCover> gpaCoverDetails) {
		this.gpaCoverDetails = gpaCoverDetails;
	}

	public String getRiskSysId() {
		return riskSysId;
	}

	public void setRiskSysId(String riskSysId) {
		this.riskSysId = riskSysId;
	}

	public String getStopLossPercentage() {
		return stopLossPercentage;
	}

	public void setStopLossPercentage(String stopLossPercentage) {
		this.stopLossPercentage = stopLossPercentage;
	}

	public String getInnerLimit() {
		return innerLimit;
	}

	public void setInnerLimit(String innerLimit) {
		this.innerLimit = innerLimit;
	}

	public List<PremGmcBenefitDetails> getGmcPolicyConditions() {
		return gmcPolicyConditions;
	}

	public void setGmcPolicyConditions(
			List<PremGmcBenefitDetails> gmcPolicyConditions) {
		this.gmcPolicyConditions = gmcPolicyConditions;
	}

	public String getMasterPolicyNumber() {
		return masterPolicyNumber;
	}

	public void setMasterPolicyNumber(String masterPolicyNumber) {
		this.masterPolicyNumber = masterPolicyNumber;
	}

	public List<PremInsuredDetails> getJpInsuredDetails() {
		return jpInsuredDetails;
	}

	public void setJpInsuredDetails(List<PremInsuredDetails> jpInsuredDetails) {
		this.jpInsuredDetails = jpInsuredDetails;
	}

	public String getLinkPolicyNo() {
		return linkPolicyNo;
	}

	public void setLinkPolicyNo(String linkPolicyNo) {
		this.linkPolicyNo = linkPolicyNo;
	}

	public List<PremPolicyCoverDetails> getPremPolicyCoverDetails() {
		return premPolicyCoverDetails;
	}

	public void setPremPolicyCoverDetails(
			List<PremPolicyCoverDetails> premPolicyCoverDetails) {
		this.premPolicyCoverDetails = premPolicyCoverDetails;
	}
	public String getProposerGender() {
		return proposerGender;
	}

	public void setProposerGender(String proposerGender) {
		this.proposerGender = proposerGender;
	}

	public String getPortedYN() {
		return portedYN;
	}

	public void setPortedYN(String portedYN) {
		this.portedYN = portedYN;
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

	public List<PremInsuredNomineeDetails> getProperNomineeDetails() {
		return properNomineeDetails;
	}

	public void setProperNomineeDetails(
			List<PremInsuredNomineeDetails> properNomineeDetails) {
		this.properNomineeDetails = properNomineeDetails;
	}

	public List<PremRelatedPolicies> getRelatedPolicies() {
		return relatedPolicies;
	}

	public void setRelatedPolicies(List<PremRelatedPolicies> relatedPolicies) {
		this.relatedPolicies = relatedPolicies;
	}

	public String getBaNCSofficeTelPhone() {
		return baNCSofficeTelPhone;
	}

	public void setBaNCSofficeTelPhone(String baNCSofficeTelPhone) {
		this.baNCSofficeTelPhone = baNCSofficeTelPhone;
	}

	public String getPolicySource() {
		return policySource;
	}

	public void setPolicySource(String policySource) {
		this.policySource = policySource;
	}

	public String getBaNCSPremiumTax() {
		return baNCSPremiumTax;
	}

	public void setBaNCSPremiumTax(String baNCSPremiumTax) {
		this.baNCSPremiumTax = baNCSPremiumTax;
	}

	public String getBaNCSStampDuty() {
		return baNCSStampDuty;
	}

	public void setBaNCSStampDuty(String baNCSStampDuty) {
		this.baNCSStampDuty = baNCSStampDuty;
	}

	public String getBaNCSTotalPremium() {
		return baNCSTotalPremium;
	}

	public void setBaNCSTotalPremium(String baNCSTotalPremium) {
		this.baNCSTotalPremium = baNCSTotalPremium;
	}

	public String getBaNCSOfficeEmailId() {
		return baNCSOfficeEmailId;
	}

	public void setBaNCSOfficeEmailId(String baNCSOfficeEmailId) {
		this.baNCSOfficeEmailId = baNCSOfficeEmailId;
	}

	public String getBaNCSOfficeFax() {
		return baNCSOfficeFax;
	}

	public void setBaNCSOfficeFax(String baNCSOfficeFax) {
		this.baNCSOfficeFax = baNCSOfficeFax;
	}
	
	public String getPhcDays() {
		return phcDays;
	}

	public void setPhcDays(String phcDays) {
		this.phcDays = phcDays;
	}

	public String getGhcDays() {
		return ghcDays;
	}

	public void setGhcDays(String ghcDays) {
		this.ghcDays = ghcDays;
	}

	public String getInstallementMethod() {
		return installementMethod;
	}

	public void setInstallementMethod(String installementMethod) {
		this.installementMethod = installementMethod;
	}

	public String getInstalmentFlag() {
		return instalmentFlag;
	}

	public void setInstalmentFlag(String instalmentFlag) {
		this.instalmentFlag = instalmentFlag;
	}

	public String getBasePolicy() {
		return basePolicy;
	}

	public void setBasePolicy(String basePolicy) {
		this.basePolicy = basePolicy;
	}

	public String getCovidPlan() {
		return covidPlan;
	}

	public void setCovidPlan(String covidPlan) {
		this.covidPlan = covidPlan;
	}

	public String getPremiaUINCode() {
		return premiaUINCode;
	}

	public void setPremiaUINCode(String premiaUINCode) {
		this.premiaUINCode = premiaUINCode;
	}
	public List<PremGmcCorpBufferLimit> getGmcCorpBufferLimit() {
		return gmcCorpBufferLimit;
	}

	public void setGmcCorpBufferLimit(List<PremGmcCorpBufferLimit> gmcCorpBufferLimit) {
		this.gmcCorpBufferLimit = gmcCorpBufferLimit;
	}

	public String getNacbBuffer() {
		return nacbBuffer;
	}

	public void setNacbBuffer(String nacbBuffer) {
		this.nacbBuffer = nacbBuffer;
	}

	public String getVintageBuffer() {
		return vintageBuffer;
	}

	public void setVintageBuffer(String vintageBuffer) {
		this.vintageBuffer = vintageBuffer;
	}

	public String getTopUpPolicyNo() {
		return topUpPolicyNo;
	}

	public void setTopUpPolicyNo(String topUpPolicyNo) {
		this.topUpPolicyNo = topUpPolicyNo;
	}
	
}
