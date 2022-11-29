package com.shaic.claim.policy.search.ui;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremDependentInsuredDetails {
	
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
	
	@JsonProperty("MailId")
	private String mailId;

	@JsonProperty("NomineeDetails")
	private List<PremInsuredNomineeDetails>  nomineeDetails;
	
	@JsonProperty("Portability")
	private List<PremPortability> portablitityDetails;


	@JsonProperty("InsuredCummBonus")
	private String cumulativeBonus;
	
	@JsonProperty("SelfDeclaredPED")
	private String selfDeclaredPed;
	
	@JsonProperty("Age")
	private String insuredAge;

	@JsonProperty("Section2SI")
	private String section2SI;
	
	//GMC
	@JsonProperty("InnerLimit")
	private String innerLimt;
	
	@JsonProperty("PEDCoPay")
	private String pedCoPay;
	
	@JsonProperty("VoluntaryCoPay")
	private String voluntaryCoPay;
	
	@JsonProperty("CompCoPay")
	private String compCopay;
	
	@JsonProperty("EffFmDt")
	private String effectiveFromDate;
	
	@JsonProperty("EffToDt")
	private String effectiveToDate;
	
	@JsonProperty("GMCContinuityBenefits")
	private List<PremGMCContinuityBenefits>  continuityBenefits;
	
	@JsonProperty("TopUpPolicyNo")
	private String  topUpPolicyNo;
	
	@JsonProperty("TopUpPolicyRiskId")
	private String  topUpPolicyRiskId;

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

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public List<PremGMCContinuityBenefits> getContinuityBenefits() {
		return continuityBenefits;
	}

	public void setContinuityBenefits(
			List<PremGMCContinuityBenefits> continuityBenefits) {
		this.continuityBenefits = continuityBenefits;
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

	

}
