package com.shaic.claim.policy.search.ui;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremInsuredDetails {
	
	@JsonProperty("DOB")
	private String dob;
	
	@JsonProperty("Gender")
	private String gender;

	@JsonProperty("HealthCardNo")
	private String healthCardNo;

	@JsonProperty("InsuredName")
	private String insuredName;
	
	@JsonProperty("PEDDetails")
	private List<PremPEDDetails> pedDetails;

	@JsonProperty("Relation")
	private String relation;

	@JsonProperty("RiskSysID")
	private String riskSysId;
	
	@JsonProperty("SumInsured")
	private String sumInsured;
	
	@JsonProperty("AddBenefitsHospCash")
	private String addBenefitsHospCash;
	
	@JsonProperty("AddBenefitsPatientCare")
	private String addBenefitsPatientCare;

	@JsonProperty("CoPay")
	private String coPay;
	
	@JsonProperty("DeductiableAmt")
	private String deductiableAmt;
	
	@JsonProperty("NomineeDetails")
	private List<PremInsuredNomineeDetails>  nomineeDetails;
	
	@JsonProperty("Portability")
	private List<PremPortability> portablitityDetails;

	@JsonProperty("PortabilityPrevPolicyDetails")
	private List<PremPortabilityPrevPolicyDetails> portabilityPrevPolicyDetails;

	@JsonProperty("InsuredCummBonus")
	private String cumulativeBonus;
	
	@JsonProperty("SelfDeclaredPED")
	private String selfDeclaredPed;
	
	@JsonProperty("Age")
	private String insuredAge;

	@JsonProperty("Section2SI")
	private String section2SI;
	
	/**
	 * Added column for GPA product
	 */
	
	@JsonProperty("Category")
	private String category;
	
	@JsonProperty("CategoryDescription")
	private String categoryDescription;
	
	@JsonProperty("EarningParentSI")
	private String earningParentSI;
	
	@JsonProperty("EffFmDt")
	private String effectiveFromDate;
	
	@JsonProperty("EffToDt")
	private String effectiveToDate;
	
	@JsonProperty("HospExpn")
	private String hospitalExpension;
	
	@JsonProperty("Inpatient")
	private String inPatient;
	//GMC
	@JsonProperty("InnerLimit")
	private String innerLimt;
	
	@JsonProperty("PEDCoPay")
	private String pedCoPay;
	
	@JsonProperty("VoluntaryCoPay")
	private String voluntaryCoPay;
	
	@JsonProperty("CompCoPay")
	private String compCopay;
	
	@JsonProperty("DependendentDetails")
	private List<PremDependentInsuredDetails>  dependentDetailsList;
	
	@JsonProperty("DependentFloaterSI")
	private String dependentFloaterSI;
	
	@JsonProperty("EmpId")
	private String employeeId;
	
	@JsonProperty("GHCPolicyType")
	private String ghcPolicyType;
	
	@JsonProperty("GHCScheme")
	private String ghcScheme;
	
	//

	@JsonProperty("MajorDisabilities")
	private String majorDisabilities;
	
	@JsonProperty("MedicalExtn")
	private String medicalExtension;
	
	@JsonProperty("MedExpensesOtherPAClaim")
	private String medicalExtensionOtherPaClaim;
	
	@JsonProperty("MemID")
	private String memberID;
	
	@JsonProperty("MonthlyIncome")
	private String monthlyIncome;
	
	@JsonProperty("NoOfPersons")
	private String numberOfPerson;
	
	@JsonProperty("NomineeName")
	private String nomineeName;
	
	@JsonProperty("NomineeRelation")
	private String nomineeRelation;
	
	@JsonProperty("NomineeSharePercent")
	private String nomineeSharePercent;
	
	@JsonProperty("Occupation")
	private String occupation;
	
	@JsonProperty("Outpatient")
	private String outPatient;
	
	@JsonProperty("RecType")
	private String recType;
	
	@JsonProperty("RiskGroup")
	private String riskGroup;
	
	@JsonProperty("Table1_Table5")
	private String table1;
	
	@JsonProperty("Table2_Table6")
	private String table2;
	
	@JsonProperty("Table3_Table7")
	private String table3;
	
	@JsonProperty("Table4")
	private String table4;
	
	@JsonProperty("TransMortRems")
	private String transMortRems;
	
	@JsonProperty("TutionFees")
	private String tutionFees;
	
	@JsonProperty("WorkPlaceAccidentYN")
	private String workPlaceAccident;
	
	//Jet Privillage Column
	@JsonProperty("Address")
	private String address;
	
	@JsonProperty("JetUniqueID")
	private String jetUniqueId;
	
	@JsonProperty("MailId")
	private String mailId;
	
	@JsonProperty("InsuredEmail")
	private String insuredEmail;
	
	@JsonProperty("MainMemberYN")
	private String mainMember;
	
	@JsonProperty("MobileNumber")
	private String mobileNumber;
	
	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("ContactNo")
	private String contactNumber;
	
	@JsonProperty("LinkEmailId")
	private String linkEmailId;
	
	@JsonProperty("LinkEmpName")
	private String linkEmpName;
	
	@JsonProperty("LinkEmpNo")
	private String linkEmpNo;
	
	@JsonProperty("LinkMobileNo")
	private String linkMobileNo;
	
	@JsonProperty("SCG_Section_I")
	private String section1_SI;
	
	@JsonProperty("SCG_Section_II")
	private String section2_SI;
	
	@JsonProperty("SCG_Section_III")
	private String section3_SI;
	
	@JsonProperty("AgeAtEntry")
	private String entryAge;
	
	@JsonProperty("PolicyYr")
	private String policyYear;
	
	@JsonProperty("Address1")
	private String address1;
	
	@JsonProperty("Address2")
	private String address2;
	
	@JsonProperty("Address3")
	private String address3;
	
	@JsonProperty("City")
	private String city;
	
	@JsonProperty("PinCode")
	private String pinCode;
	
	@JsonProperty("State")
	private String state;
	
	/**
	 * 
	 * R0974
	 */
	@JsonProperty("AccountNo")
	private String accountNo;
	
	@JsonProperty("BankName")
	private String bankName;
	
	@JsonProperty("BranchName")
	private String branchName;
	
	@JsonProperty("IFSC_Code")
	private String ifscCode;
	
	@JsonProperty("NameofAccountHolder")
	private String nameOfAccountHolder;
	
	@JsonProperty("GMCContinuityBenefits")
	private List<PremGMCContinuityBenefits>  continuityBenefits;
	
	@JsonProperty("DependantSI_YN")
	private String  dependantSIFlag;
	
	@JsonProperty("CertificateNo")
	private String  certificateNo;
	
	@JsonProperty("GMCNomineeDetails")
	private List<PremInsuredNomineeDetails>  gmcNomineeDetails;
	
	@JsonProperty("TopUpPolicyNo")
	private String  topUpPolicyNo;
	
	@JsonProperty("TopUpPolicyRiskId")
	private String  topUpPolicyRiskId;

	@JsonProperty("VIPPolicy")
	private String  vipPolicy;
	
	private String baNCSSourceRiskID;

	@JsonProperty("MCI_Plan")
	private String policyPlan;
	
	@JsonProperty("BuyBackPED")
	private String buyBackPed;
	
	@JsonProperty("Days")
	private String hcpDays;
	
	@JsonProperty("DefineSI")
	private String definedLimit;
	
	@JsonProperty("Lumbsum")
	private String lumbSum;
	
	public String getSelfDeclaredPed() {
		return selfDeclaredPed;
	}

	public void setSelfDeclaredPed(String selfDeclaredPed) {
		this.selfDeclaredPed = selfDeclaredPed;
	}

	public String getCumulativeBonus() {
		return cumulativeBonus;
	}

	public void setCumulativeBonus(String cumulativeBonus) {
		this.cumulativeBonus = cumulativeBonus;
	}


	public String getDeductiableAmt() {
		return deductiableAmt;
	}

	public void setDeductiableAmt(String deductiableAmt) {
		this.deductiableAmt = deductiableAmt;
	}

	public String getCoPay() {
		return coPay;
	}

	public void setCoPay(String coPay) {
		this.coPay = coPay;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getHealthCardNo() {
		return healthCardNo;
	}

	public void setHealthCardNo(String healthCardNo) {
		this.healthCardNo = healthCardNo;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	
	public String getAddBenefitsPatientCare() {
		return addBenefitsPatientCare;
	}

	public void setAddBenefitsPatientCare(String addBenefitsPatientCare) {
		this.addBenefitsPatientCare = addBenefitsPatientCare;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getRiskSysId() {
		return riskSysId;
	}

	public void setRiskSysId(String riskSysId) {
		this.riskSysId = riskSysId;
	}

	public String getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(String sumInsured) {
		this.sumInsured = sumInsured;
	}

	public List<PremPEDDetails> getPedDetails() {
		return pedDetails;
	}

	public void setPedDetails(List<PremPEDDetails> pedDetails) {
		this.pedDetails = pedDetails;
	}
	
	public String getAddBenefitsHospCash() {
		return addBenefitsHospCash;
	}

	public void setAddBenefitsHospCash(String addBenefitsHospCash) {
		this.addBenefitsHospCash = addBenefitsHospCash;
	}

	public String getInsuredAge() {
		return insuredAge;
	}

	public void setInsuredAge(String insuredAge) {
		this.insuredAge = insuredAge;
	}

	public List<PremInsuredNomineeDetails> getNomineeDetails() {
		return nomineeDetails;
	}

	public void setNomineeDetails(List<PremInsuredNomineeDetails> nomineeDetails) {
		this.nomineeDetails = nomineeDetails;
	}

	public List<PremPortability> getPortablitityDetails() {
		return portablitityDetails;
	}

	public void setPortablitityDetails(List<PremPortability> portablitityDetails) {
		this.portablitityDetails = portablitityDetails;
	}

	public String getSection2SI() {
		return section2SI;
	}

	public void setSection2SI(String section2si) {
		section2SI = section2si;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public String getEarningParentSI() {
		return earningParentSI;
	}

	public void setEarningParentSI(String earningParentSI) {
		this.earningParentSI = earningParentSI;
	}

	public String getEffectiveFromDate() {
		return effectiveFromDate;
	}

	public void setEffectiveFromDate(String effectiveFromDate) {
		this.effectiveFromDate = effectiveFromDate;
	}

	public String getEffectiveToDate() {
		return effectiveToDate;
	}

	public void setEffectiveToDate(String effectiveToDate) {
		this.effectiveToDate = effectiveToDate;
	}

	public String getHospitalExpension() {
		return hospitalExpension;
	}

	public void setHospitalExpension(String hospitalExpension) {
		this.hospitalExpension = hospitalExpension;
	}

	public String getInPatient() {
		return inPatient;
	}

	public void setInPatient(String inPatient) {
		this.inPatient = inPatient;
	}

	public String getMajorDisabilities() {
		return majorDisabilities;
	}

	public void setMajorDisabilities(String majorDisabilities) {
		this.majorDisabilities = majorDisabilities;
	}

	public String getMedicalExtension() {
		return medicalExtension;
	}

	public void setMedicalExtension(String medicalExtension) {
		this.medicalExtension = medicalExtension;
	}

	public String getMemberID() {
		return memberID;
	}

	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}

	public String getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(String monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public String getNumberOfPerson() {
		return numberOfPerson;
	}

	public void setNumberOfPerson(String numberOfPerson) {
		this.numberOfPerson = numberOfPerson;
		if(numberOfPerson != null && numberOfPerson.equals("")){
			this.numberOfPerson = null;
		}
	}

	public String getNomineeName() {
		return nomineeName;
	}

	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}

	public String getNomineeRelation() {
		return nomineeRelation;
	}

	public void setNomineeRelation(String nomineeRelation) {
		this.nomineeRelation = nomineeRelation;
	}

	public String getNomineeSharePercent() {
		return nomineeSharePercent;
	}

	public void setNomineeSharePercent(String nomineeSharePercent) {
		this.nomineeSharePercent = nomineeSharePercent;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getOutPatient() {
		return outPatient;
	}

	public void setOutPatient(String outPatient) {
		this.outPatient = outPatient;
	}

	public String getRecType() {
		return recType;
	}

	public void setRecType(String recType) {
		this.recType = recType;
	}

	public String getRiskGroup() {
		return riskGroup;
	}

	public void setRiskGroup(String riskGroup) {
		this.riskGroup = riskGroup;
	}

	public String getTransMortRems() {
		return transMortRems;
	}

	public void setTransMortRems(String transMortRems) {
		this.transMortRems = transMortRems;
	}

	public String getTutionFees() {
		return tutionFees;
	}

	public void setTutionFees(String tutionFees) {
		this.tutionFees = tutionFees;
	}

	public String getWorkPlaceAccident() {
		return workPlaceAccident;
	}

	public void setWorkPlaceAccident(String workPlaceAccident) {
		this.workPlaceAccident = workPlaceAccident;
	}


	public String getTable1() {
		return table1;
	}

	public void setTable1(String table1) {
		this.table1 = table1;
	}

	public String getTable2() {
		return table2;
	}

	public void setTable2(String table2) {
		this.table2 = table2;
	}

	public String getTable3() {
		return table3;
	}

	public void setTable3(String table3) {
		this.table3 = table3;
	}

	public String getTable4() {
		return table4;
	}

	public void setTable4(String table4) {
		this.table4 = table4;
	}
	public String getInnerLimt() {
		return innerLimt;
	}

	public void setInnerLimt(String innerLimt) {
		this.innerLimt = innerLimt;
	}

	public String getPedCoPay() {
		return pedCoPay;
	}

	public void setPedCoPay(String pedCoPay) {
		this.pedCoPay = pedCoPay;
	}

	public String getVoluntaryCoPay() {
		return voluntaryCoPay;
	}

	public void setVoluntaryCoPay(String voluntaryCoPay) {
		this.voluntaryCoPay = voluntaryCoPay;
	}

	public String getCompCopay() {
		return compCopay;
	}

	public void setCompCopay(String compCopay) {
		this.compCopay = compCopay;
	}

	public List<PremDependentInsuredDetails> getDependentDetailsList() {
		return dependentDetailsList;
	}

	public void setDependentDetailsList(
			List<PremDependentInsuredDetails> dependentDetailsList) {
		this.dependentDetailsList = dependentDetailsList;
	}

	public String getDependentFloaterSI() {
		return dependentFloaterSI;
	}

	public void setDependentFloaterSI(String dependentFloaterSI) {
		this.dependentFloaterSI = dependentFloaterSI;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getJetUniqueId() {
		return jetUniqueId;
	}

	public void setJetUniqueId(String jetUniqueId) {
		this.jetUniqueId = jetUniqueId;
	}

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public String getMainMember() {
		return mainMember;
	}

	public void setMainMember(String mainMember) {
		this.mainMember = mainMember;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getLinkEmailId() {
		return linkEmailId;
	}

	public void setLinkEmailId(String linkEmailId) {
		this.linkEmailId = linkEmailId;
	}

	public String getLinkEmpName() {
		return linkEmpName;
	}

	public void setLinkEmpName(String linkEmpName) {
		this.linkEmpName = linkEmpName;
	}

	public String getLinkEmpNo() {
		return linkEmpNo;
	}

	public void setLinkEmpNo(String linkEmpNo) {
		this.linkEmpNo = linkEmpNo;
	}

	public String getLinkMobileNo() {
		return linkMobileNo;
	}

	public void setLinkMobileNo(String linkMobileNo) {
		this.linkMobileNo = linkMobileNo;
	}

	public String getSection1_SI() {
		return section1_SI;
	}

	public void setSection1_SI(String section1_SI) {
		this.section1_SI = section1_SI;
	}

	public String getSection2_SI() {
		return section2_SI;
	}

	public void setSection2_SI(String section2_SI) {
		this.section2_SI = section2_SI;
	}

	public String getSection3_SI() {
		return section3_SI;
	}

	public void setSection3_SI(String section3_SI) {
		this.section3_SI = section3_SI;
	}

	public List<PremPortabilityPrevPolicyDetails> getPortabilityPrevPolicyDetails() {
		return portabilityPrevPolicyDetails;
	}

	public void setPortabilityPrevPolicyDetails(
			List<PremPortabilityPrevPolicyDetails> portabilityPrevPolicyDetails) {
		this.portabilityPrevPolicyDetails = portabilityPrevPolicyDetails;
	}

	public String getEntryAge() {
		return entryAge;
	}

	public void setEntryAge(String entryAge) {
		this.entryAge = entryAge;
	}

	public String getPolicyYear() {
		return policyYear;
	}

	public void setPolicyYear(String policyYear) {
		this.policyYear = policyYear;
	}
	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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
	public String getInsuredEmail() {
		return insuredEmail;
	}

	public void setInsuredEmail(String insuredEmail) {
		this.insuredEmail = insuredEmail;
	}

	public String getMedicalExtensionOtherPaClaim() {
		return medicalExtensionOtherPaClaim;
	}

	public void setMedicalExtensionOtherPaClaim(String medicalExtensionOtherPaClaim) {
		this.medicalExtensionOtherPaClaim = medicalExtensionOtherPaClaim;
	}
	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getNameOfAccountHolder() {
		return nameOfAccountHolder;
	}

	public void setNameOfAccountHolder(String nameOfAccountHolder) {
		this.nameOfAccountHolder = nameOfAccountHolder;
	}

	public List<PremGMCContinuityBenefits> getContinuityBenefits() {
		return continuityBenefits;
	}

	public void setContinuityBenefits(
			List<PremGMCContinuityBenefits> continuityBenefits) {
		this.continuityBenefits = continuityBenefits;
	}

	public String getDependantSIFlag() {
		return dependantSIFlag;
	}

	public void setDependantSIFlag(String dependantSIFlag) {
		this.dependantSIFlag = dependantSIFlag;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public List<PremInsuredNomineeDetails> getGmcNomineeDetails() {
		return gmcNomineeDetails;
	}

	public void setGmcNomineeDetails(
			List<PremInsuredNomineeDetails> gmcNomineeDetails) {
		this.gmcNomineeDetails = gmcNomineeDetails;
	}

	public String getTopUpPolicyNo() {
		return topUpPolicyNo;
	}

	public void setTopUpPolicyNo(String topUpPolicyNo) {
		this.topUpPolicyNo = topUpPolicyNo;
	}

	public String getTopUpPolicyRiskId() {
		return topUpPolicyRiskId;
	}

	public void setTopUpPolicyRiskId(String topUpPolicyRiskId) {
		this.topUpPolicyRiskId = topUpPolicyRiskId;
	}
	
	public String getVipPolicy() {
		return vipPolicy;
	}

	public void setVipPolicy(String vipPolicy) {
		this.vipPolicy = vipPolicy;
	}

	public String getBaNCSSourceRiskID() {
		return baNCSSourceRiskID;
	}

	public void setBaNCSSourceRiskID(String baNCSSourceRiskID) {
		this.baNCSSourceRiskID = baNCSSourceRiskID;
	}
	
	public String getPolicyPlan() {
		return policyPlan;
	}

	public void setPolicyPlan(String policyPlan) {
		this.policyPlan = policyPlan;
	}

	public String getGhcPolicyType() {
		return ghcPolicyType;
	}

	public void setGhcPolicyType(String ghcPolicyType) {
		this.ghcPolicyType = ghcPolicyType;
	}

	public String getGhcScheme() {
		return ghcScheme;
	}

	public void setGhcScheme(String ghcScheme) {
		this.ghcScheme = ghcScheme;
	}

	public String getBuyBackPed() {
		return buyBackPed;
	}

	public void setBuyBackPed(String buyBackPed) {
		this.buyBackPed = buyBackPed;
	}
	public String getHcpDays() {
		return hcpDays;
	}

	public void setHcpDays(String hcpDays) {
		this.hcpDays = hcpDays;
	}
	public String getDefinedLimit() {
		return definedLimit;
	}

	public void setDefinedLimit(String definedLimit) {
		this.definedLimit = definedLimit;
	}

	public String getLumbSum() {
		return lumbSum;
	}

	public void setLumbSum(String lumbSum) {
		this.lumbSum = lumbSum;
	}
	
	
}
