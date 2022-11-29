package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremInsuredNomineeDetails {
	
	@JsonProperty("NomineeAge")
	private String nomineeAge;
	
	@JsonProperty("NomineeName")
	private String nomineeName;
	
	@JsonProperty("NomineeRelation")
	private String nomineeRelation;
	
	@JsonProperty("NomineeDOB")
	private String nomineeDob;
	
	@JsonProperty("AppointeeAge")
	private String appointeeAge;
	
	@JsonProperty("AppointeeName")
	private String appointeeName;
	
	@JsonProperty("AppointeeRelationship")
	private String appointeeRelationship;
	
	@JsonProperty("NomineeRelationship")
	private String nomineeRelationship;
	
	@JsonProperty("NomineePercentage")
	private String nomineePercentage;
	
	@JsonProperty("IFSC Code")
	private String ifscCode;
	
	@JsonProperty("Bank Account Number")
	private String accountNumber;
	
	@JsonProperty("Beneficiary Name")
	private String beneficiaryName;
	

	public String getNomineeRelation() {
		return nomineeRelation;
	}

	public void setNomineeRelation(String nomineeRelation) {
		this.nomineeRelation = nomineeRelation;
	}

	public String getNomineeAge() {
		return nomineeAge;
	}

	public void setNomineeAge(String nomineeAge) {
		this.nomineeAge = nomineeAge;
	}

	public String getNomineeName() {
		return nomineeName;
	}

	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}

	public String getNomineeDob() {
		return nomineeDob;
	}

	public void setNomineeDob(String nomineeDob) {
		this.nomineeDob = nomineeDob;
	}

	public String getAppointeeAge() {
		return appointeeAge;
	}

	public void setAppointeeAge(String appointeeAge) {
		this.appointeeAge = appointeeAge;
	}

	public String getAppointeeName() {
		return appointeeName;
	}

	public void setAppointeeName(String appointeeName) {
		this.appointeeName = appointeeName;
	}

	public String getAppointeeRelationship() {
		return appointeeRelationship;
	}

	public void setAppointeeRelationship(String appointeeRelationship) {
		this.appointeeRelationship = appointeeRelationship;
	}

	public String getNomineeRelationship() {
		return nomineeRelationship;
	}

	public void setNomineeRelationship(String nomineeRelationship) {
		this.nomineeRelationship = nomineeRelationship;
	}

	public String getNomineePercentage() {
		return nomineePercentage;
	}

	public void setNomineePercentage(String nomineePercentage) {
		this.nomineePercentage = nomineePercentage;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}
	
	
	
	

}
