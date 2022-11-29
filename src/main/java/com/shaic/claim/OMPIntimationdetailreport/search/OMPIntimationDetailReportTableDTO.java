package com.shaic.claim.OMPIntimationdetailreport.search;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class OMPIntimationDetailReportTableDTO extends AbstractTableDTO  implements Serializable{
	
	private String policyno;
	private String branchOffice;
	private Long age;
	private String maximumPolicySumInsured;
	private String sumInsured;
	private String intimationNo;
	private String induredName;
	private Date dateOfIntimation;
	private Date dateOfLoss;
	private String tpaIntimationNo;
	private String ailment;
	private String claimType;
	private String eventCode;
	private Date admissionDate;
	private String placeofvisite;
	private String amountinusd;
	private String conversionrate;
	private String amountinInr;
	
	public String getPolicyno() {
		return policyno;
	}
	public void setPolicyno(String policyno) {
		this.policyno = policyno;
	}
	public String getBranchOffice() {
		return branchOffice;
	}
	public void setBranchOffice(String branchOffice) {
		this.branchOffice = branchOffice;
	}
	public Long getAge() {
		return age;
	}
	public void setAge(Long age) {
		this.age = age;
	}
	public String getMaximumPolicySumInsured() {
		return maximumPolicySumInsured;
	}
	public void setMaximumPolicySumInsured(String maximumPolicySumInsured) {
		this.maximumPolicySumInsured = maximumPolicySumInsured;
	}
	public String getSumInsured() {
		return sumInsured;
	}
	public void setSumInsured(String sumInsured) {
		this.sumInsured = sumInsured;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getInduredName() {
		return induredName;
	}
	public void setInduredName(String induredName) {
		this.induredName = induredName;
	}
	public Date getDateOfIntimation() {
		return dateOfIntimation;
	}
	public void setDateOfIntimation(Date dateOfIntimation) {
		this.dateOfIntimation = dateOfIntimation;
	}
	public Date getDateOfLoss() {
		return dateOfLoss;
	}
	public void setDateOfLoss(Date dateOfLoss) {
		this.dateOfLoss = dateOfLoss;
	}
	public String getTpaIntimationNo() {
		return tpaIntimationNo;
	}
	public void setTpaIntimationNo(String tpaIntimationNo) {
		this.tpaIntimationNo = tpaIntimationNo;
	}
	public String getAilment() {
		return ailment;
	}
	public void setAilment(String ailment) {
		this.ailment = ailment;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getEventCode() {
		return eventCode;
	}
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	public Date getAdmissionDate() {
		return admissionDate;
	}
	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}
	public String getPlaceofvisite() {
		return placeofvisite;
	}
	public void setPlaceofvisite(String placeofvisite) {
		this.placeofvisite = placeofvisite;
	}
	public String getAmountinusd() {
		return amountinusd;
	}
	public void setAmountinusd(String amountinusd) {
		this.amountinusd = amountinusd;
	}
	public String getConversionrate() {
		return conversionrate;
	}
	public void setConversionrate(String conversionrate) {
		this.conversionrate = conversionrate;
	}
	public String getAmountinInr() {
		return amountinInr;
	}
	public void setAmountinInr(String amountinInr) {
		this.amountinInr = amountinInr;
	}
	

}
