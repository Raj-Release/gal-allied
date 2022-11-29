package com.shaic.restservices.bancs.triggerPartyEndorsement;

import java.util.ArrayList;

public class TriggerPartyEndorsementDetails {
	 private String firstName;
	 private String middleName;
	 private String lastName;
	 ArrayList<TriggerPartyEndorseAddressDetails> addressDetails;
	 private String eiaAvailable;
	 private String eiaRequired;
	 private String eiaCreditRequired;
	 private String eiaNumber;
	 private String eiaWhenLinked;
	 private String epolicyRequired;
	 private String insuranceRepositoryCode;
	 private String idProof;
	 private String addressProof;
	 private String dobProof;
	 private String fatherName;
	 private String spouseName;
	 ArrayList<TriggerPartyEndorseBankDetails> bankDetails;


	 // Getter Methods 

	 public String getFirstName() {
	  return firstName;
	 }

	 public String getMiddleName() {
	  return middleName;
	 }

	 public String getLastName() {
	  return lastName;
	 }

	 public String getEiaAvailable() {
	  return eiaAvailable;
	 }

	 public String getEiaRequired() {
	  return eiaRequired;
	 }

	 public String getEiaCreditRequired() {
	  return eiaCreditRequired;
	 }

	 public String getEiaNumber() {
	  return eiaNumber;
	 }

	 public String getEiaWhenLinked() {
	  return eiaWhenLinked;
	 }

	 public String getEpolicyRequired() {
	  return epolicyRequired;
	 }

	 public String getInsuranceRepositoryCode() {
	  return insuranceRepositoryCode;
	 }

	 public String getIdProof() {
	  return idProof;
	 }

	 public String getAddressProof() {
	  return addressProof;
	 }

	 public String getDobProof() {
	  return dobProof;
	 }

	 public String getFatherName() {
	  return fatherName;
	 }

	 public String getSpouseName() {
	  return spouseName;
	 }

	 // Setter Methods 

	 public void setFirstName(String firstName) {
	  this.firstName = firstName;
	 }

	 public void setMiddleName(String middleName) {
	  this.middleName = middleName;
	 }

	 public void setLastName(String lastName) {
	  this.lastName = lastName;
	 }

	 public void setEiaAvailable(String eiaAvailable) {
	  this.eiaAvailable = eiaAvailable;
	 }

	 public void setEiaRequired(String eiaRequired) {
	  this.eiaRequired = eiaRequired;
	 }

	 public void setEiaCreditRequired(String eiaCreditRequired) {
	  this.eiaCreditRequired = eiaCreditRequired;
	 }

	 public void setEiaNumber(String eiaNumber) {
	  this.eiaNumber = eiaNumber;
	 }

	 public void setEiaWhenLinked(String eiaWhenLinked) {
	  this.eiaWhenLinked = eiaWhenLinked;
	 }

	 public void setEpolicyRequired(String epolicyRequired) {
	  this.epolicyRequired = epolicyRequired;
	 }

	 public void setInsuranceRepositoryCode(String insuranceRepositoryCode) {
	  this.insuranceRepositoryCode = insuranceRepositoryCode;
	 }

	 public void setIdProof(String idProof) {
	  this.idProof = idProof;
	 }

	 public void setAddressProof(String addressProof) {
	  this.addressProof = addressProof;
	 }

	 public void setDobProof(String dobProof) {
	  this.dobProof = dobProof;
	 }

	 public void setFatherName(String fatherName) {
	  this.fatherName = fatherName;
	 }

	 public void setSpouseName(String spouseName) {
	  this.spouseName = spouseName;
	 }

	public ArrayList<TriggerPartyEndorseAddressDetails> getAddressDetails() {
		return addressDetails;
	}

	public void setAddressDetails(ArrayList<TriggerPartyEndorseAddressDetails> addressDetails) {
		this.addressDetails = addressDetails;
	}

	public ArrayList<TriggerPartyEndorseBankDetails> getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(ArrayList<TriggerPartyEndorseBankDetails> bankDetails) {
		this.bankDetails = bankDetails;
	}
	 
}	
