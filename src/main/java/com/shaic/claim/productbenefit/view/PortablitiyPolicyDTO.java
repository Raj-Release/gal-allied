package com.shaic.claim.productbenefit.view;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonObject;
import com.shaic.arch.table.AbstractTableDTO;

public class PortablitiyPolicyDTO extends AbstractTableDTO  implements Serializable {
	
	private String insuredName;
	
	private String productName;
	
	private String policyNo;
	
	private String policyType;
	
	private String tbaCode;
	
	private Date policyStartDate;
	
	private Long periodElapsed;
	
	private String policyTerm;
	
	private Date dateOfBirth;
	
	private String pedDeclared;
	
	private String pedIcdCode;
	
	private String pedDescription;

	private Long familySize;
	
	private String remarks;
	
	private String requestId;
	
	private Date memberEntryDate;
	
	private Long siFist;
	
	private Long siSecond;
	
	private Long siThird;
	
	private Long siFourth;
	
	private Long siFirstFloat;
	
	private Long siSecondFloat;
	
	private Long siThirdFloat;
	
	private Long siFourthFloat;
	
	private Long siFirstChange;
	
	private Long siSecondChange;
	
	private Long siThirdChange;
	
	private Long siFourthChange;
	
	private Integer serialNo;
	
	private String portabilityJson;

	/**
	 * Below Attributes were Added as part of CR R1080
	 */
	
	private String custId;
	private Date policyFrmDate;
	private Date policyToDate;
	private String policyStrFrmDate;
	private String policyStrToDate;
	private String underwritingYear;
	private Long sumInsured;
	private Long cumulativeBonus;
	private String detailPEDAny;
	private Integer waitingPeriod;
	private Integer exclusionYear1;
	private Integer exclusionYear2;

	/**
	 * As per Mr. Satish Sir's instruction, flag value 1 was represented as YES ,
	 * other than 1 was represented as NO
	 */
	private String exclusionYear1Str;
	
	/**
	 * As per Mr. Satish Sir's instruction, flag value 1 was represented as YES ,
	 * other than 1 was represented as NO
	 */
	private String exclusionYear2Str;
	
	/**
	 * As per Mr. Satish Sir's instruction, flag value 1 was represented as YES ,
	 * other than 1 was represented as NO
	 */
	private String waiver30Days;
	private Long year;
	
	/**
	 * As per Mr. Satish Sir's instruction, flag value 1 was represented as YES ,
	 * other than 1 was represented as NO
	 */
	private String PEDwaiver;
	private int noofClaims;
	private Long amount;
	private String natureOfIllnessClaimPaid;
	private String insurerName;
	private int activeStatus;
	private String currentPolicyNo;

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public String getTbaCode() {
		return tbaCode;
	}

	public void setTbaCode(String tbaCode) {
		this.tbaCode = tbaCode;
	}

	public Date getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(Date policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public Long getPeriodElapsed() {
		return periodElapsed;
	}

	public void setPeriodElapsed(Long periodElapsed) {
		this.periodElapsed = periodElapsed;
	}

	public String getPolicyTerm() {
		return policyTerm;
	}

	public void setPolicyTerm(String policyTerm) {
		this.policyTerm = policyTerm;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPedDeclared() {
		return pedDeclared;
	}

	public void setPedDeclared(String pedDeclared) {
		this.pedDeclared = pedDeclared;
	}

	public String getPedIcdCode() {
		return pedIcdCode;
	}

	public void setPedIcdCode(String pedIcdCode) {
		this.pedIcdCode = pedIcdCode;
	}

	public String getPedDescription() {
		return pedDescription;
	}

	public void setPedDescription(String pedDescription) {
		this.pedDescription = pedDescription;
	}

	public Long getFamilySize() {
		return familySize;
	}

	public void setFamilySize(Long familySize) {
		this.familySize = familySize;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Date getMemberEntryDate() {
		return memberEntryDate;
	}

	public void setMemberEntryDate(Date memberEntryDate) {
		this.memberEntryDate = memberEntryDate;
	}

	public Long getSiFist() {
		return siFist;
	}

	public void setSiFist(Long siFist) {
		this.siFist = siFist;
	}

	public Long getSiSecond() {
		return siSecond;
	}

	public void setSiSecond(Long siSecond) {
		this.siSecond = siSecond;
	}

	public Long getSiThird() {
		return siThird;
	}

	public void setSiThird(Long siThird) {
		this.siThird = siThird;
	}

	public Long getSiFourth() {
		return siFourth;
	}

	public void setSiFourth(Long siFourth) {
		this.siFourth = siFourth;
	}

	public Long getSiFirstFloat() {
		return siFirstFloat;
	}

	public void setSiFirstFloat(Long siFirstFloat) {
		this.siFirstFloat = siFirstFloat;
	}

	public Long getSiSecondFloat() {
		return siSecondFloat;
	}

	public void setSiSecondFloat(Long siSecondFloat) {
		this.siSecondFloat = siSecondFloat;
	}

	public Long getSiThirdFloat() {
		return siThirdFloat;
	}

	public void setSiThirdFloat(Long siThirdFloat) {
		this.siThirdFloat = siThirdFloat;
	}

	public Long getSiFourthFloat() {
		return siFourthFloat;
	}

	public void setSiFourthFloat(Long siFourthFloat) {
		this.siFourthFloat = siFourthFloat;
	}

	public Long getSiFirstChange() {
		return siFirstChange;
	}

	public void setSiFirstChange(Long siFirstChange) {
		this.siFirstChange = siFirstChange;
	}

	public Long getSiSecondChange() {
		return siSecondChange;
	}

	public void setSiSecondChange(Long siSecondChange) {
		this.siSecondChange = siSecondChange;
	}

	public Long getSiThirdChange() {
		return siThirdChange;
	}

	public void setSiThirdChange(Long siThirdChange) {
		this.siThirdChange = siThirdChange;
	}

	public Long getSiFourthChange() {
		return siFourthChange;
	}

	public void setSiFourthChange(Long siFourthChange) {
		this.siFourthChange = siFourthChange;
	}

	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public String getPortabilityJson() {
		return portabilityJson;
	}

	public void setPortabilityJson(String portabilityJson) {
		this.portabilityJson = portabilityJson;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public Date getPolicyFrmDate() {
		return policyFrmDate;
	}

	public void setPolicyFrmDate(Date policyFrmDate) {
		this.policyFrmDate = policyFrmDate;
	}

	public Date getPolicyToDate() {
		return policyToDate;
	}

	public void setPolicyToDate(Date policyToDate) {
		this.policyToDate = policyToDate;
	}

	public String getUnderwritingYear() {
		return underwritingYear;
	}

	public void setUnderwritingYear(String underwritingYear) {
		this.underwritingYear = underwritingYear;
	}

	public Long getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(Long sumInsured) {
		this.sumInsured = sumInsured;
	}

	public Long getCumulativeBonus() {
		return cumulativeBonus;
	}

	public void setCumulativeBonus(Long cumulativeBonus) {
		this.cumulativeBonus = cumulativeBonus;
	}

	public String getDetailPEDAny() {
		return detailPEDAny;
	}

	public void setDetailPEDAny(String detailPEDAny) {
		this.detailPEDAny = detailPEDAny;
	}

	public Integer getWaitingPeriod() {
		return waitingPeriod;
	}

	public void setWaitingPeriod(Integer waitingPeriod) {
		this.waitingPeriod = waitingPeriod;
	}

	public Integer getExclusionYear1() {
		return exclusionYear1;
	}

	public void setExclusionYear1(Integer exclusionYear1) {
		this.exclusionYear1 = exclusionYear1;
		exclusionYear1Str = exclusionYear1 != null 
				&& exclusionYear1.intValue() == 1 ? "YES" : "NO";
//				(exclusionYear1.intValue() == 1 ? "YES" : (exclusionYear1.intValue() == 0 ? "NO" : "")) : "";
				
	}

	public Integer getExclusionYear2() {
		return exclusionYear2;
	}

	public void setExclusionYear2(Integer exclusionYear2) {
		this.exclusionYear2 = exclusionYear2;
		exclusionYear2Str = exclusionYear2 != null  
				&& exclusionYear2.intValue() == 1 ? "YES" : "NO";
//				(exclusionYear2.intValue() == 1 ? "YES" : (exclusionYear2.intValue() == 0 ? "NO" : "")) : "";
	}

	public String getPEDwaiver() {
		return PEDwaiver != null && !PEDwaiver.isEmpty() ? 
				(("1").equalsIgnoreCase(PEDwaiver) ? "YES" : (("0").equalsIgnoreCase(PEDwaiver) ? "NO" : "NO"))
				: "NO";
	}

	public void setPEDwaiver(String pEDwaiver) {
		PEDwaiver = pEDwaiver;
	}

	public int getNoofClaims() {
		return noofClaims;
	}

	public void setNoofClaims(int noofClaims) {
		this.noofClaims = noofClaims;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getNatureOfIllnessClaimPaid() {
		return natureOfIllnessClaimPaid;
	}

	public void setNatureOfIllnessClaimPaid(String natureOfIllnessClaimPaid) {
		this.natureOfIllnessClaimPaid = natureOfIllnessClaimPaid;
	}

	public String getInsurerName() {
		return insurerName;
	}

	public void setInsurerName(String insurerName) {
		this.insurerName = insurerName;
	}

	public String getWaiver30Days() {
		return waiver30Days != null && !waiver30Days.isEmpty() ? 
				(("1").equalsIgnoreCase(waiver30Days) ? "YES" : (("0").equalsIgnoreCase(waiver30Days) ? "NO" : "NO"))  
				: "NO";
	}

	public void setWaiver30Days(String waiver30Days) {
		this.waiver30Days = waiver30Days;
	}

	public Long getYear() {
		return year;
	}

	public void setYear(Long year) {
		this.year = year;
	}

	public int getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getPolicyStrFrmDate() {
		return policyFrmDate != null ? new SimpleDateFormat("dd-MM-yyyy").format(policyFrmDate) : "";
	}

	public void setPolicyStrFrmDate(String policyStrFrmDate) {
		this.policyStrFrmDate = policyStrFrmDate;
	}

	public String getPolicyStrToDate() {
		return policyToDate != null ? new SimpleDateFormat("dd-MM-yyyy").format(policyToDate) : "";
	}

	public void setPolicyStrToDate(String policyStrToDate) {
		this.policyStrToDate = policyStrToDate;
	}

	public String getCurrentPolicyNo() {
		return currentPolicyNo;
	}

	public void setCurrentPolicyNo(String currentPolicyNo) {
		this.currentPolicyNo = currentPolicyNo;
	}

	public String getExclusionYear1Str() {
//		exclusionYear1Str = exclusionYear1 != null ? 
//				(exclusionYear1.intValue() == 1 ? "YES" : (exclusionYear1.intValue() == 0 ? "NO" : ""))
//				: "";
		return exclusionYear1Str;
	}

	public void setExclusionYear1Str(String exclusionYear1Str) {
		this.exclusionYear1Str = exclusionYear1Str;
	}

	public String getExclusionYear2Str() {
//		exclusionYear2Str = exclusionYear2 != null ? 
//				(exclusionYear2.intValue() == 1 ? "YES" : (exclusionYear2.intValue() == 0 ? "NO" : ""))
//				: "";
		return exclusionYear2Str;
	}

	public void setExclusionYear2Str(String exclusionYear2Str) {
		this.exclusionYear2Str = exclusionYear2Str;
	}		
}
