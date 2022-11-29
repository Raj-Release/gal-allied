package com.shaic.restservices.bancs.claimprovision.policydetail;

public class ProposerDetails {

	private String proposerCode;
	private String typeOfParty;
	private String title;
	private String firstName;
	private String middleName;
	private String lastName;
	private String nationality;
	private String businessName;
	private String whetherRegistered;
	private String registrationNo;
	private String registrationDate;
	private String parentPartyCode;
	private String dateOfBirth;
	private String gender;
	private String motherMaidenName;
	private String fatherName;
	private String husbandName;
	private String occupation;
	private String maritalStatus;
	private String pan;
	private String aadhaarVirtualId;
	private String aadhaarNumber;
	private String aadhaarEnrollmentNumber;
	private String passportNo;
	private String voterID;
	private String communicationType;
	private String physicallyChallenged;
	private String preferredLanguage;
	private String partyPaymentMethod;
	private String annualIncome;
	private String partyType;
	private String partySubCategory;
	private String policyHolderType;
	private String typeOfMember;
	private String subCategoryOfPolicyHolder;
	private String subCategoryOfMember;
	private String shortName;
	private String socialSectorCode;
	private String legacyPartyCode;
	private String legacyPartyName;
	private String eiaRequired;
	private String eiaCreditRequired;
	private String eiaAvailable;
	private String eiaNumber;
	private String eiaWhenLinked;
	private String epolicyRequired;
	private String insuranceRepositoryCode;
	private String issueAllPoliciesInElectronicFormat; // chk the original format
	private String eInsuranceDematAccountVerified; // chk the original format
	private String idProof;
	private String addressProof;
	private String dobProof;
//	private String fatherName; // duplicate parameter - need to chk how many father name parameter coming in original format
	private String spouseName;
	private String rejectedReason;
	private String cKYCCompleted;
	private String eKYCCompleted;
	private String incomeTaxPayerTINNo;
	private String remarks;
	private String otherCategoriesOfPerson;
	private String partyClassification;
	private String partyInternalCode;
	private String cinNumber;
	private String tan;
	private String typeOfOrganization;
	private String industry;
	private String partyReferenceEnrollmentNumber;
	private QualificationDetails qualificationDetails;
	private BankDetails bankDetails;
	private GSTDetails gstDetails;
	private TDSDetails tdsDetails;
	private ServiceTaxDetails serviceTaxDetails;
	private AddressDetails addressDetails;
	private CreditCardDetails creditCardDetails;
	private DocumentDetails documentDetails;
	
	
	public String getProposerCode() {
		return proposerCode;
	}
	public void setProposerCode(String proposerCode) {
		this.proposerCode = proposerCode;
	}
	public String getTypeOfParty() {
		return typeOfParty;
	}
	public void setTypeOfParty(String typeOfParty) {
		this.typeOfParty = typeOfParty;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getWhetherRegistered() {
		return whetherRegistered;
	}
	public void setWhetherRegistered(String whetherRegistered) {
		this.whetherRegistered = whetherRegistered;
	}
	public String getRegistrationNo() {
		return registrationNo;
	}
	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}
	public String getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}
	public String getParentPartyCode() {
		return parentPartyCode;
	}
	public void setParentPartyCode(String parentPartyCode) {
		this.parentPartyCode = parentPartyCode;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getMotherMaidenName() {
		return motherMaidenName;
	}
	public void setMotherMaidenName(String motherMaidenName) {
		this.motherMaidenName = motherMaidenName;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getHusbandName() {
		return husbandName;
	}
	public void setHusbandName(String husbandName) {
		this.husbandName = husbandName;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public String getAadhaarVirtualId() {
		return aadhaarVirtualId;
	}
	public void setAadhaarVirtualId(String aadhaarVirtualId) {
		this.aadhaarVirtualId = aadhaarVirtualId;
	}
	public String getAadhaarNumber() {
		return aadhaarNumber;
	}
	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}
	public String getAadhaarEnrollmentNumber() {
		return aadhaarEnrollmentNumber;
	}
	public void setAadhaarEnrollmentNumber(String aadhaarEnrollmentNumber) {
		this.aadhaarEnrollmentNumber = aadhaarEnrollmentNumber;
	}
	public String getPassportNo() {
		return passportNo;
	}
	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}
	public String getVoterID() {
		return voterID;
	}
	public void setVoterID(String voterID) {
		this.voterID = voterID;
	}
	public String getCommunicationType() {
		return communicationType;
	}
	public void setCommunicationType(String communicationType) {
		this.communicationType = communicationType;
	}
	public String getPhysicallyChallenged() {
		return physicallyChallenged;
	}
	public void setPhysicallyChallenged(String physicallyChallenged) {
		this.physicallyChallenged = physicallyChallenged;
	}
	public String getPreferredLanguage() {
		return preferredLanguage;
	}
	public void setPreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}
	public String getPartyPaymentMethod() {
		return partyPaymentMethod;
	}
	public void setPartyPaymentMethod(String partyPaymentMethod) {
		this.partyPaymentMethod = partyPaymentMethod;
	}
	public String getAnnualIncome() {
		return annualIncome;
	}
	public void setAnnualIncome(String annualIncome) {
		this.annualIncome = annualIncome;
	}
	public String getPartyType() {
		return partyType;
	}
	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}
	public String getPartySubCategory() {
		return partySubCategory;
	}
	public void setPartySubCategory(String partySubCategory) {
		this.partySubCategory = partySubCategory;
	}
	public String getPolicyHolderType() {
		return policyHolderType;
	}
	public void setPolicyHolderType(String policyHolderType) {
		this.policyHolderType = policyHolderType;
	}
	public String getTypeOfMember() {
		return typeOfMember;
	}
	public void setTypeOfMember(String typeOfMember) {
		this.typeOfMember = typeOfMember;
	}
	public String getSubCategoryOfPolicyHolder() {
		return subCategoryOfPolicyHolder;
	}
	public void setSubCategoryOfPolicyHolder(String subCategoryOfPolicyHolder) {
		this.subCategoryOfPolicyHolder = subCategoryOfPolicyHolder;
	}
	public String getSubCategoryOfMember() {
		return subCategoryOfMember;
	}
	public void setSubCategoryOfMember(String subCategoryOfMember) {
		this.subCategoryOfMember = subCategoryOfMember;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getSocialSectorCode() {
		return socialSectorCode;
	}
	public void setSocialSectorCode(String socialSectorCode) {
		this.socialSectorCode = socialSectorCode;
	}
	public String getLegacyPartyCode() {
		return legacyPartyCode;
	}
	public void setLegacyPartyCode(String legacyPartyCode) {
		this.legacyPartyCode = legacyPartyCode;
	}
	public String getLegacyPartyName() {
		return legacyPartyName;
	}
	public void setLegacyPartyName(String legacyPartyName) {
		this.legacyPartyName = legacyPartyName;
	}
	public String getEiaRequired() {
		return eiaRequired;
	}
	public void setEiaRequired(String eiaRequired) {
		this.eiaRequired = eiaRequired;
	}
	public String getEiaCreditRequired() {
		return eiaCreditRequired;
	}
	public void setEiaCreditRequired(String eiaCreditRequired) {
		this.eiaCreditRequired = eiaCreditRequired;
	}
	public String getEiaAvailable() {
		return eiaAvailable;
	}
	public void setEiaAvailable(String eiaAvailable) {
		this.eiaAvailable = eiaAvailable;
	}
	public String getEiaNumber() {
		return eiaNumber;
	}
	public void setEiaNumber(String eiaNumber) {
		this.eiaNumber = eiaNumber;
	}
	public String getEiaWhenLinked() {
		return eiaWhenLinked;
	}
	public void setEiaWhenLinked(String eiaWhenLinked) {
		this.eiaWhenLinked = eiaWhenLinked;
	}
	public String getEpolicyRequired() {
		return epolicyRequired;
	}
	public void setEpolicyRequired(String epolicyRequired) {
		this.epolicyRequired = epolicyRequired;
	}
	public String getInsuranceRepositoryCode() {
		return insuranceRepositoryCode;
	}
	public void setInsuranceRepositoryCode(String insuranceRepositoryCode) {
		this.insuranceRepositoryCode = insuranceRepositoryCode;
	}
	public String getIssueAllPoliciesInElectronicFormat() {
		return issueAllPoliciesInElectronicFormat;
	}
	public void setIssueAllPoliciesInElectronicFormat(
			String issueAllPoliciesInElectronicFormat) {
		this.issueAllPoliciesInElectronicFormat = issueAllPoliciesInElectronicFormat;
	}
	public String geteInsuranceDematAccountVerified() {
		return eInsuranceDematAccountVerified;
	}
	public void seteInsuranceDematAccountVerified(
			String eInsuranceDematAccountVerified) {
		this.eInsuranceDematAccountVerified = eInsuranceDematAccountVerified;
	}
	public String getIdProof() {
		return idProof;
	}
	public void setIdProof(String idProof) {
		this.idProof = idProof;
	}
	public String getAddressProof() {
		return addressProof;
	}
	public void setAddressProof(String addressProof) {
		this.addressProof = addressProof;
	}
	public String getDobProof() {
		return dobProof;
	}
	public void setDobProof(String dobProof) {
		this.dobProof = dobProof;
	}
	public String getSpouseName() {
		return spouseName;
	}
	public void setSpouseName(String spouseName) {
		this.spouseName = spouseName;
	}
	public String getRejectedReason() {
		return rejectedReason;
	}
	public void setRejectedReason(String rejectedReason) {
		this.rejectedReason = rejectedReason;
	}
	public String getcKYCCompleted() {
		return cKYCCompleted;
	}
	public void setcKYCCompleted(String cKYCCompleted) {
		this.cKYCCompleted = cKYCCompleted;
	}
	public String geteKYCCompleted() {
		return eKYCCompleted;
	}
	public void seteKYCCompleted(String eKYCCompleted) {
		this.eKYCCompleted = eKYCCompleted;
	}
	public String getIncomeTaxPayerTINNo() {
		return incomeTaxPayerTINNo;
	}
	public void setIncomeTaxPayerTINNo(String incomeTaxPayerTINNo) {
		this.incomeTaxPayerTINNo = incomeTaxPayerTINNo;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getOtherCategoriesOfPerson() {
		return otherCategoriesOfPerson;
	}
	public void setOtherCategoriesOfPerson(String otherCategoriesOfPerson) {
		this.otherCategoriesOfPerson = otherCategoriesOfPerson;
	}
	public String getPartyClassification() {
		return partyClassification;
	}
	public void setPartyClassification(String partyClassification) {
		this.partyClassification = partyClassification;
	}
	public String getPartyInternalCode() {
		return partyInternalCode;
	}
	public void setPartyInternalCode(String partyInternalCode) {
		this.partyInternalCode = partyInternalCode;
	}
	public String getCinNumber() {
		return cinNumber;
	}
	public void setCinNumber(String cinNumber) {
		this.cinNumber = cinNumber;
	}
	public String getTan() {
		return tan;
	}
	public void setTan(String tan) {
		this.tan = tan;
	}
	public String getTypeOfOrganization() {
		return typeOfOrganization;
	}
	public void setTypeOfOrganization(String typeOfOrganization) {
		this.typeOfOrganization = typeOfOrganization;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getPartyReferenceEnrollmentNumber() {
		return partyReferenceEnrollmentNumber;
	}
	public void setPartyReferenceEnrollmentNumber(
			String partyReferenceEnrollmentNumber) {
		this.partyReferenceEnrollmentNumber = partyReferenceEnrollmentNumber;
	}
	public QualificationDetails getQualificationDetails() {
		return qualificationDetails;
	}
	public void setQualificationDetails(QualificationDetails qualificationDetails) {
		this.qualificationDetails = qualificationDetails;
	}
	public BankDetails getBankDetails() {
		return bankDetails;
	}
	public void setBankDetails(BankDetails bankDetails) {
		this.bankDetails = bankDetails;
	}
	public GSTDetails getGstDetails() {
		return gstDetails;
	}
	public void setGstDetails(GSTDetails gstDetails) {
		this.gstDetails = gstDetails;
	}
	public TDSDetails getTdsDetails() {
		return tdsDetails;
	}
	public void setTdsDetails(TDSDetails tdsDetails) {
		this.tdsDetails = tdsDetails;
	}
	public ServiceTaxDetails getServiceTaxDetails() {
		return serviceTaxDetails;
	}
	public void setServiceTaxDetails(ServiceTaxDetails serviceTaxDetails) {
		this.serviceTaxDetails = serviceTaxDetails;
	}
	public AddressDetails getAddressDetails() {
		return addressDetails;
	}
	public void setAddressDetails(AddressDetails addressDetails) {
		this.addressDetails = addressDetails;
	}
	public CreditCardDetails getCreditCardDetails() {
		return creditCardDetails;
	}
	public void setCreditCardDetails(CreditCardDetails creditCardDetails) {
		this.creditCardDetails = creditCardDetails;
	}
	public DocumentDetails getDocumentDetails() {
		return documentDetails;
	}
	public void setDocumentDetails(DocumentDetails documentDetails) {
		this.documentDetails = documentDetails;
	}
	
}
