package com.shaic.claim.productbenefit.view;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class ContinuityBenefitDTO extends AbstractTableDTO  implements Serializable{
	
	private String policyNo;
	private Integer policyYr;
	private Date policyStartDate;
	private String insuredName;
	private Integer serialNo;
	private String createdBy;
	private Date createdDate;
	private Date policyFrmDate;
	private Date policyToDate;
	private String policyStrFrmDate;
	private String policyStrToDate;
	private String underwritingYear;
	private String detailPEDAny;
	private Integer waitingPeriod;
	private Integer exclusionYear1;
	private Integer exclusionYear2;
	private String inceptionDate;
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
	private int activeStatus;


	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}


	public Date getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(Date policyStartDate) {
		this.policyStartDate = policyStartDate;
	}


	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
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

	public Integer getPolicyYr() {
		return policyYr;
	}

	public void setPolicyYr(Integer policyYr) {
		this.policyYr = policyYr;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getInceptionDate() {
		return inceptionDate;
	}

	public void setInceptionDate(String inceptionDate) {
		this.inceptionDate = inceptionDate;
	}		

}
