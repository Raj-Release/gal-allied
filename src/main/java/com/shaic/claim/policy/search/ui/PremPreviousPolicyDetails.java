package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremPreviousPolicyDetails {
	
		@JsonProperty("ProposerCode")
		private String proposerCode;
		
		@JsonProperty("PolicyStartDate")
		private String policyStartDate;
		
		@JsonProperty("PolicyEndDate")
		private String policyEndDate;
		
		@JsonProperty("ProductCode")
		private String productCode;

		
		@JsonProperty("ProductName")
		private String productName;
		
		@JsonProperty("SumInsured")
		private String sumInsured;
		
		@JsonProperty("Premium")
		private String premium;

		
		@JsonProperty("OfficeCode")
		private String officeCode;

		
		@JsonProperty("InsuredName")
		private String insuredName;

		
		@JsonProperty("PreExistingDisease")
		private String preExistingDisease;

		
		@JsonProperty("ProposerAddress")
		private String proposerAddress;
		
		
		@JsonProperty("ProposerTelNo")
		private String proposerTelNo;
		
		
		@JsonProperty("ProposerOfficeEmail")
		private String proposerOfficeEmail;
		
		@JsonProperty("ProposerOfficeFaxNo")
		private String proposerOfficeFaxNo;
		
		
		@JsonProperty("PreviousInsurerName")
		private String previousInsurerName;
		
		@JsonProperty("PolicyFromDate")
		private String policyFromDate;
		
		@JsonProperty("PolicyNo")
		private String policyNo;
		
		@JsonProperty("PolicyToDate")
		private String policyToDate;
		
		@JsonProperty("PolicyUWYear")
		private String policyUWYear;
		
		@JsonProperty("ProposerEmail")
		private String proposerEmail;
		
		@JsonProperty("ProposerFaxNo")
		private String proposerFaxNo;
		
		@JsonProperty("ProposerName")
		private String proposerName;


		public String getProposerCode() {
			return proposerCode;
		}


		public void setProposerCode(String proposerCode) {
			this.proposerCode = proposerCode;
		}


		public String getPolicyStartDate() {
			return policyStartDate;
		}


		public void setPolicyStartDate(String policyStartDate) {
			this.policyStartDate = policyStartDate;
		}


		public String getPolicyEndDate() {
			return policyEndDate;
		}


		public void setPolicyEndDate(String policyEndDate) {
			this.policyEndDate = policyEndDate;
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


		public String getSumInsured() {
			return sumInsured;
		}


		public void setSumInsured(String sumInsured) {
			this.sumInsured = sumInsured;
		}


		public String getPremium() {
			return premium;
		}


		public void setPremium(String premium) {
			this.premium = premium;
		}


		public String getOfficeCode() {
			return officeCode;
		}


		public void setOfficeCode(String officeCode) {
			this.officeCode = officeCode;
		}


		public String getInsuredName() {
			return insuredName;
		}


		public void setInsuredName(String insuredName) {
			this.insuredName = insuredName;
		}


		public String getPreExistingDisease() {
			return preExistingDisease;
		}


		public void setPreExistingDisease(String preExistingDisease) {
			this.preExistingDisease = preExistingDisease;
		}


		public String getProposerAddress() {
			return proposerAddress;
		}


		public void setProposerAddress(String proposerAddress) {
			this.proposerAddress = proposerAddress;
		}


		public String getProposerTelNo() {
			return proposerTelNo;
		}


		public void setProposerTelNo(String proposerTelNo) {
			this.proposerTelNo = proposerTelNo;
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


		public String getPreviousInsurerName() {
			return previousInsurerName;
		}


		public void setPreviousInsurerName(String previousInsurerName) {
			this.previousInsurerName = previousInsurerName;
		}


		public String getPolicyFromDate() {
			return policyFromDate;
		}


		public void setPolicyFromDate(String policyFromDate) {
			this.policyFromDate = policyFromDate;
		}


		public String getPolicyNo() {
			return policyNo;
		}


		public void setPolicyNo(String policyNo) {
			this.policyNo = policyNo;
		}


		public String getPolicyToDate() {
			return policyToDate;
		}


		public void setPolicyToDate(String policyToDate) {
			this.policyToDate = policyToDate;
		}


		public String getPolicyUWYear() {
			return policyUWYear;
		}


		public void setPolicyUWYear(String policyUWYear) {
			this.policyUWYear = policyUWYear;
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


		public String getProposerName() {
			return proposerName;
		}


		public void setProposerName(String proposerName) {
			this.proposerName = proposerName;
		}

	   
		

	}
