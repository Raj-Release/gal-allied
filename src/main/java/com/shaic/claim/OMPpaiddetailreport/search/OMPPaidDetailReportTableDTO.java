package com.shaic.claim.OMPpaiddetailreport.search;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class OMPPaidDetailReportTableDTO extends AbstractTableDTO  implements Serializable {
	
	
	private String policyno;
	private String branchoffice;
	private Long age;
	private String maximumpolicysuminsured;
	private String suminsured;
	private String intimationno;
	private String tpaintimationno;
	private String insuredname;
	private String claimtype;
	private String ailment;
	private String dateofloss;
	private String country;
	private String amountinusd;
	private String conversionrate;
	private String amountininr;
	private String bankcharges;
	private String accountapprovalsdt;
	
	
	public String getPolicyno() {
		return policyno;
	}
	public void setPolicyno(String policyno) {
		this.policyno = policyno;
	}
	public String getBranchoffice() {
		return branchoffice;
	}
	public void setBranchoffice(String branchoffice) {
		this.branchoffice = branchoffice;
	}
	public Long getAge() {
		return age;
	}
	public void setAge(Long age) {
		this.age = age;
	}
	public String getMaximumpolicysuminsured() {
		return maximumpolicysuminsured;
	}
	public void setMaximumpolicysuminsured(String maximumpolicysuminsured) {
		this.maximumpolicysuminsured = maximumpolicysuminsured;
	}
	public String getSuminsured() {
		return suminsured;
	}
	public void setSuminsured(String suminsured) {
		this.suminsured = suminsured;
	}
	public String getIntimationno() {
		return intimationno;
	}
	public void setIntimationno(String intimationno) {
		this.intimationno = intimationno;
	}
	public String getTpaintimationno() {
		return tpaintimationno;
	}
	public void setTpaintimationno(String tpaintimationno) {
		this.tpaintimationno = tpaintimationno;
	}
	public String getInsuredname() {
		return insuredname;
	}
	public void setInsuredname(String insuredname) {
		this.insuredname = insuredname;
	}
	public String getClaimtype() {
		return claimtype;
	}
	public void setClaimtype(String claimtype) {
		this.claimtype = claimtype;
	}
	public String getAilment() {
		return ailment;
	}
	public void setAilment(String ailment) {
		this.ailment = ailment;
	}
	public String getDateofloss() {
		return dateofloss;
	}
	public void setDateofloss(String dateofloss) {
		this.dateofloss = dateofloss;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
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
	public String getAmountininr() {
		return amountininr;
	}
	public void setAmountininr(String amountininr) {
		this.amountininr = amountininr;
	}
	public String getBankcharges() {
		return bankcharges;
	}
	public void setBankcharges(String bankcharges) {
		this.bankcharges = bankcharges;
	}
	public String getAccountapprovalsdt() {
		return accountapprovalsdt;
	}
	public void setAccountapprovalsdt(String accountapprovalsdt) {
		this.accountapprovalsdt = accountapprovalsdt;
	}
	
	
	

}
