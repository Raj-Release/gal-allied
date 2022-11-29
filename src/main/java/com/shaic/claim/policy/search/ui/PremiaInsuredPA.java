package com.shaic.claim.policy.search.ui;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class PremiaInsuredPA {
	
	@JsonProperty("Age")
	private String age;
	
	@JsonProperty("DOB")
	private String dateOfBirth;
	
	@JsonProperty("Gender")
	private String gender;
	
	//@JsonProperty("IDCardNo")
	@JsonProperty("HealthCardNo")
	private String idCardNumber;
	
	@JsonProperty("InsuredCummBonus")
	private String insuredCummulativeBonus;
	
	@JsonProperty("InsuredName")
	private String insuredName;
	
	@JsonProperty("NomineeDetails")
	private List<PremInsuredNomineeDetails>  nomineeDetails;
	
	@JsonProperty("CoverDetails")
	private List<PremCoverDetailsForPA> premInsuredRiskCoverDetails;
	
	@JsonProperty("MajorDisabilities")
	private String majorDisabilities;
	
	@JsonProperty("Relation")
	private String relation;
	
	@JsonProperty("RiskGroup")
	private String riskGroup;
	
	@JsonProperty("RiskSysID")
	private String riskSysId;
	
	@JsonProperty("SumInsured")
	private String sumInsured;

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
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

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	public String getInsuredCummulativeBonus() {
		return insuredCummulativeBonus;
	}

	public void setInsuredCummulativeBonus(String insuredCummulativeBonus) {
		this.insuredCummulativeBonus = insuredCummulativeBonus;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public List<PremInsuredNomineeDetails> getNomineeDetails() {
		return nomineeDetails;
	}

	public void setNomineeDetails(List<PremInsuredNomineeDetails> nomineeDetails) {
		this.nomineeDetails = nomineeDetails;
	}



	public List<PremCoverDetailsForPA> getPremInsuredRiskCoverDetails() {
		return premInsuredRiskCoverDetails;
	}

	public void setPremInsuredRiskCoverDetails(
			List<PremCoverDetailsForPA> premInsuredRiskCoverDetails) {
		this.premInsuredRiskCoverDetails = premInsuredRiskCoverDetails;
	}

	public String getMajorDisabilities() {
		return majorDisabilities;
	}

	public void setMajorDisabilities(String majorDisabilities) {
		this.majorDisabilities = majorDisabilities;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getRiskGroup() {
		return riskGroup;
	}

	public void setRiskGroup(String riskGroup) {
		this.riskGroup = riskGroup;
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
	

}
