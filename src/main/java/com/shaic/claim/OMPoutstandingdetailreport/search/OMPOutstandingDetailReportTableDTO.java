package com.shaic.claim.OMPoutstandingdetailreport.search;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class OMPOutstandingDetailReportTableDTO extends AbstractTableDTO  implements Serializable  {
	
	
	private String policyno;
	private String branchoffice;
	private Long age;
	private String maximumpolicysuminsured;
	private String suminsured;
	private String intimationno;
	private String insuredname;
	private Date dateofintimation;
	private Date dateofloss;
	private String tpaintimationno;
	private String ailment;
	private String claimtype;
	private String eventcode;
	private String placeofvisite;
	private String amountinusd;
	private String conversionrate;
	private String amountininr;
	private String negotationfee;
	private String opinionfee;
	private String claimstatus;
	
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
	public String getInsuredname() {
		return insuredname;
	}
	public void setInsuredname(String insuredname) {
		this.insuredname = insuredname;
	}
	public Date getDateofintimation() {
		return dateofintimation;
	}
	public void setDateofintimation(Date dateofintimation) {
		this.dateofintimation = dateofintimation;
	}
	public Date getDateofloss() {
		return dateofloss;
	}
	public void setDateofloss(Date dateofloss) {
		this.dateofloss = dateofloss;
	}
	public String getTpaintimationno() {
		return tpaintimationno;
	}
	public void setTpaintimationno(String tpaintimationno) {
		this.tpaintimationno = tpaintimationno;
	}
	public String getAilment() {
		return ailment;
	}
	public void setAilment(String ailment) {
		this.ailment = ailment;
	}
	public String getClaimtype() {
		return claimtype;
	}
	public void setClaimtype(String claimtype) {
		this.claimtype = claimtype;
	}
	public String getEventcode() {
		return eventcode;
	}
	public void setEventcode(String eventcode) {
		this.eventcode = eventcode;
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
	public String getAmountininr() {
		return amountininr;
	}
	public void setAmountininr(String amountininr) {
		this.amountininr = amountininr;
	}
	public String getNegotationfee() {
		return negotationfee;
	}
	public void setNegotationfee(String negotationfee) {
		this.negotationfee = negotationfee;
	}
	public String getOpinionfee() {
		return opinionfee;
	}
	public void setOpinionfee(String opinionfee) {
		this.opinionfee = opinionfee;
	}
	public String getClaimstatus() {
		return claimstatus;
	}
	public void setClaimstatus(String claimstatus) {
		this.claimstatus = claimstatus;
	}
	
}