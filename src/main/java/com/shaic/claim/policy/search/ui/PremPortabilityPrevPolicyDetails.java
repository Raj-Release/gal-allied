package com.shaic.claim.policy.search.ui;
import org.codehaus.jackson.annotate.JsonProperty;
/**
 * 
 * @author Lakshminarayana
 *
 */
public class PremPortabilityPrevPolicyDetails {

	@JsonProperty("Amount") 
	private String amount;
	
	@JsonProperty("CumulativeBonus") 
	private String cummulativeBonus;
	
	@JsonProperty("CustomerId") 
	private String customerId;
							
	@JsonProperty("Exclusion_1stYear") 
	private String exclusion_1stYr;
									
	@JsonProperty("Exclusion_2ndYear") 
	private String exclusion_2ndYr;
											
	@JsonProperty("InsurerName") 
	private String insurerName;
													
	@JsonProperty("NatureofIllness") 
	private String natureofIllness;
															
	@JsonProperty("NoofClaims") 
	private String noofClaims;
																	
	@JsonProperty("PEDDetails") 
	private String pedDetails;
																			
	@JsonProperty("PEDWaiver") 
	private String pedWaiver;
																					
	@JsonProperty("PolicyFmDt") 
	private String policyFmDt;
																							
	@JsonProperty("PolicyNumber") 
	private String policyNumber;
																									
	@JsonProperty("PolicyToDt") 
	private String policyToDt;
																											
	@JsonProperty("PolicyType") 
	private String policyType;
																													
	@JsonProperty("ProductName") 
	private String productName;
																															
	@JsonProperty("SumInsured") 
	private String sumInsured; 
																																	
	@JsonProperty("UWYear") 
	private String uwYear; 
																																			
	@JsonProperty("Waiver_30Days") 
	private String waiver30Days;
																																						
	@JsonProperty("Year") 
	private String year;
	

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCummulativeBonus() {
		return cummulativeBonus;
	}

	public void setCummulativeBonus(String cummulativeBonus) {
		this.cummulativeBonus = cummulativeBonus;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getExclusion_1stYr() {
		return exclusion_1stYr;
	}

	public void setExclusion_1stYr(String exclusion_1stYr) {
		this.exclusion_1stYr = exclusion_1stYr;
	}

	public String getExclusion_2ndYr() {
		return exclusion_2ndYr;
	}

	public void setExclusion_2ndYr(String exclusion_2ndYr) {
		this.exclusion_2ndYr = exclusion_2ndYr;
	}

	public String getInsurerName() {
		return insurerName;
	}

	public void setInsurerName(String insurerName) {
		this.insurerName = insurerName;
	}

	public String getNatureofIllness() {
		return natureofIllness;
	}

	public void setNatureofIllness(String natureofIllness) {
		this.natureofIllness = natureofIllness;
	}

	public String getNoofClaims() {
		return noofClaims;
	}

	public void setNoofClaims(String noofClaims) {
		this.noofClaims = noofClaims;
	}

	public String getPedDetails() {
		return pedDetails;
	}

	public void setPedDetails(String pedDetails) {
		this.pedDetails = pedDetails;
	}

	public String getPedWaiver() {
		return this.pedWaiver;
	}

	public void setPedWaiver(String pedWaiver) {
		this.pedWaiver = pedWaiver;
	}

	public String getPolicyFmDt() {
		return policyFmDt;
	}

	public void setPolicyFmDt(String policyFmDt) {
		this.policyFmDt = policyFmDt;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getPolicyToDt() {
		return policyToDt;
	}

	public void setPolicyToDt(String policyToDt) {
		this.policyToDt = policyToDt;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
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

	public String getUwYear() {
		return uwYear;
	}

	public void setUwYear(String uwYear) {
		this.uwYear = uwYear;
	}

	public String getWaiver30Days() {
		return waiver30Days;
	}

	public void setWaiver30Days(String waiver30Days) {
		this.waiver30Days = waiver30Days;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}	
}
