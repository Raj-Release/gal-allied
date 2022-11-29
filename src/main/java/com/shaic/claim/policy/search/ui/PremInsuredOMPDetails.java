package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremInsuredOMPDetails {
	

	@JsonProperty("Age")
	private String insuredAge;
	
	@JsonProperty("AssigneeName")
	private String assigneename;
	
	@JsonProperty("AssigneeRelation")
	private String relation;

	@JsonProperty("DOB")
	private String dob;
	
	@JsonProperty("DateofExpiry")
	private String dateOfExpiry;
	
	@JsonProperty("IDCardNo")
	private String idCardNo;
	
	@JsonProperty("MedicalReportAttachedYN")
	private String medicalReportAttached;
	
	@JsonProperty("NameofInsured")
	private String insuredName;

	@JsonProperty("PassportNo")
	private String passportNo;

	@JsonProperty("PlaceofTravel1")
	private String placeofTravel1;
	
	@JsonProperty("PlaceofTravel2")
	private String placeofTravel2;
	
	@JsonProperty("PlaceofTravel3")
	private String placeofTravel3;
	
	@JsonProperty("PlaceofTravel4")
	private String placeofTravel4;
	
	@JsonProperty("PlaceofTravel5")
	private String placeofTravel5;
	
	@JsonProperty("Sex")
	private String gender;
	
	@JsonProperty("Plan")
	private String plan;
	
	@JsonProperty("PurposeofVisit")
	private String purposeofVisit;
	
	@JsonProperty("RiskSysID")
	private String riskSysID;
	
	@JsonProperty("SpecialExclusions")
	private String specialExclusions;
	
	@JsonProperty("TripIncludeUSAandCanada")
	private String tripIncludeUSAandCanada;
	
	@JsonProperty("TypeofVisa")
	private String typeofVisa;

	@JsonProperty("DateofCommencement")
	private String dateOfCommencement;
	
	@JsonProperty("CoverValidNotBeyond")
	private String coverValidNotBeyond;
	
	@JsonProperty("DaysofTravel")
	private String daysOfTravel;
	
	@JsonProperty("CountryofVisit")
	private String countryOfVisit;
	
	@JsonProperty("RelationwithSponsor")
	private String relationWithSponsor;
	
	@JsonProperty("SumInsured")
	private String sumInsured;
	
	@JsonProperty("AddressinCountryofStudy")
	private String addressinCountryOfStudy;

	@JsonProperty("AddressofSponsor")
	private String addressOfSponsor;
	
	@JsonProperty("NameofInstitution")
	private String nameOfInstitution;
	
	@JsonProperty("NameofSponsor")
	private String nameOfSponsor;

	@JsonProperty("PlaceofIssue")
	private String placeOfIssue;
	
	@JsonProperty("I_20No")
	private String i20No;
	
	@JsonProperty("Occupation")
	private String occuption;
	
	@JsonProperty("TripBand")
	private String tripBand;
	
	@JsonProperty("TripDaysBeyond180YN")
	private String tripDaysBeyond180yn;
	
	@JsonProperty("PurposeofTravel")
	private String purposeOfTravel;
	

	public String getInsuredAge() {
		return insuredAge;
	}

	public void setInsuredAge(String insuredAge) {
		this.insuredAge = insuredAge;
	}

	public String getAssigneename() {
		return assigneename;
	}

	public void setAssigneename(String assigneename) {
		this.assigneename = assigneename;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getDateOfExpiry() {
		return dateOfExpiry;
	}

	public void setDateOfExpiry(String dateOfExpiry) {
		this.dateOfExpiry = dateOfExpiry;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getMedicalReportAttached() {
		return medicalReportAttached;
	}

	public void setMedicalReportAttached(String medicalReportAttached) {
		this.medicalReportAttached = medicalReportAttached;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getPassportNo() {
		return passportNo;
	}

	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}

	public String getPlaceofTravel1() {
		return placeofTravel1;
	}

	public void setPlaceofTravel1(String placeofTravel1) {
		this.placeofTravel1 = placeofTravel1;
	}

	public String getPlaceofTravel2() {
		return placeofTravel2;
	}

	public void setPlaceofTravel2(String placeofTravel2) {
		this.placeofTravel2 = placeofTravel2;
	}

	public String getPlaceofTravel3() {
		return placeofTravel3;
	}

	public void setPlaceofTravel3(String placeofTravel3) {
		this.placeofTravel3 = placeofTravel3;
	}

	public String getPlaceofTravel4() {
		return placeofTravel4;
	}

	public void setPlaceofTravel4(String placeofTravel4) {
		this.placeofTravel4 = placeofTravel4;
	}

	public String getPlaceofTravel5() {
		return placeofTravel5;
	}

	public void setPlaceofTravel5(String placeofTravel5) {
		this.placeofTravel5 = placeofTravel5;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getPurposeofVisit() {
		return purposeofVisit;
	}

	public void setPurposeofVisit(String purposeofVisit) {
		this.purposeofVisit = purposeofVisit;
	}

	public String getRiskSysID() {
		return riskSysID;
	}

	public void setRiskSysID(String riskSysID) {
		this.riskSysID = riskSysID;
	}

	public String getSpecialExclusions() {
		return specialExclusions;
	}

	public void setSpecialExclusions(String specialExclusions) {
		this.specialExclusions = specialExclusions;
	}

	public String getTripIncludeUSAandCanada() {
		return tripIncludeUSAandCanada;
	}

	public void setTripIncludeUSAandCanada(String tripIncludeUSAandCanada) {
		this.tripIncludeUSAandCanada = tripIncludeUSAandCanada;
	}

	public String getTypeofVisa() {
		return typeofVisa;
	}

	public void setTypeofVisa(String typeofVisa) {
		this.typeofVisa = typeofVisa;
	}

	public String getDateOfCommencement() {
		return dateOfCommencement;
	}

	public void setDateOfCommencement(String dateOfCommencement) {
		this.dateOfCommencement = dateOfCommencement;
	}

	public String getCoverValidNotBeyond() {
		return coverValidNotBeyond;
	}

	public void setCoverValidNotBeyond(String coverValidNotBeyond) {
		this.coverValidNotBeyond = coverValidNotBeyond;
	}

	public String getDaysOfTravel() {
		return daysOfTravel;
	}

	public void setDaysOfTravel(String daysOfTravel) {
		this.daysOfTravel = daysOfTravel;
	}

	public String getCountryOfVisit() {
		return countryOfVisit;
	}

	public void setCountryOfVisit(String countryOfVisit) {
		this.countryOfVisit = countryOfVisit;
	}

	public String getRelationWithSponsor() {
		return relationWithSponsor;
	}

	public void setRelationWithSponsor(String relationWithSponsor) {
		this.relationWithSponsor = relationWithSponsor;
	}

	public String getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(String sumInsured) {
		this.sumInsured = sumInsured;
	}

	public String getAddressinCountryOfStudy() {
		return addressinCountryOfStudy;
	}

	public void setAddressinCountryOfStudy(String addressinCountryOfStudy) {
		this.addressinCountryOfStudy = addressinCountryOfStudy;
	}

	public String getAddressOfSponsor() {
		return addressOfSponsor;
	}

	public void setAddressOfSponsor(String addressOfSponsor) {
		this.addressOfSponsor = addressOfSponsor;
	}

	public String getNameOfInstitution() {
		return nameOfInstitution;
	}

	public void setNameOfInstitution(String nameOfInstitution) {
		this.nameOfInstitution = nameOfInstitution;
	}

	public String getNameOfSponsor() {
		return nameOfSponsor;
	}

	public void setNameOfSponsor(String nameOfSponsor) {
		this.nameOfSponsor = nameOfSponsor;
	}

	public String getPlaceOfIssue() {
		return placeOfIssue;
	}

	public void setPlaceOfIssue(String placeOfIssue) {
		this.placeOfIssue = placeOfIssue;
	}

	public String getI20No() {
		return i20No;
	}

	public void setI20No(String i20No) {
		this.i20No = i20No;
	}

	public String getOccuption() {
		return occuption;
	}

	public void setOccuption(String occuption) {
		this.occuption = occuption;
	}

	public String getTripBand() {
		return tripBand;
	}

	public void setTripBand(String tripBand) {
		this.tripBand = tripBand;
	}

	public String getTripDaysBeyond180yn() {
		return tripDaysBeyond180yn;
	}

	public void setTripDaysBeyond180yn(String tripDaysBeyond180yn) {
		this.tripDaysBeyond180yn = tripDaysBeyond180yn;
	}

	public String getPurposeOfTravel() {
		return purposeOfTravel;
	}

	public void setPurposeOfTravel(String purposeOfTravel) {
		this.purposeOfTravel = purposeOfTravel;
	}
		
		

	

}
