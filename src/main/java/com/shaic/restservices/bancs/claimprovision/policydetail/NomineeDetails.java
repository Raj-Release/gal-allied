package com.shaic.restservices.bancs.claimprovision.policydetail;

public class NomineeDetails {
	private String uniqueNumber;
	private Integer serialNo;
	private String nomineeName;
	private String relationshipWithProposer;
	private String dateOfBirth;
	private Integer age;
	private Integer nomineeSharePercentage;
	private String appointeeName;
	private Integer appointeeAge;
	private String appointeeRelationship;
	private Integer aadhaarNumberOfTheNominee; // chk in the original format
	private String accountHolderName;
	private String accountNumber;
	private String ifscCode;
	private String accountType;
	
	public String getUniqueNumber() {
		return uniqueNumber;
	}
	public void setUniqueNumber(String uniqueNumber) {
		this.uniqueNumber = uniqueNumber;
	}
	public Integer getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}
	public String getNomineeName() {
		return nomineeName;
	}
	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}
	public String getRelationshipWithProposer() {
		return relationshipWithProposer;
	}
	public void setRelationshipWithProposer(String relationshipWithProposer) {
		this.relationshipWithProposer = relationshipWithProposer;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Integer getNomineeSharePercentage() {
		return nomineeSharePercentage;
	}
	public void setNomineeSharePercentage(Integer nomineeSharePercentage) {
		this.nomineeSharePercentage = nomineeSharePercentage;
	}
	public String getAppointeeName() {
		return appointeeName;
	}
	public void setAppointeeName(String appointeeName) {
		this.appointeeName = appointeeName;
	}
	public Integer getAppointeeAge() {
		return appointeeAge;
	}
	public void setAppointeeAge(Integer appointeeAge) {
		this.appointeeAge = appointeeAge;
	}
	public String getAppointeeRelationship() {
		return appointeeRelationship;
	}
	public void setAppointeeRelationship(String appointeeRelationship) {
		this.appointeeRelationship = appointeeRelationship;
	}
	public Integer getAadhaarNumberOfTheNominee() {
		return aadhaarNumberOfTheNominee;
	}
	public void setAadhaarNumberOfTheNominee(Integer aadhaarNumberOfTheNominee) {
		this.aadhaarNumberOfTheNominee = aadhaarNumberOfTheNominee;
	}
	public String getAccountHolderName() {
		return accountHolderName;
	}
	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
}
