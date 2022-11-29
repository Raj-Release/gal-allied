package com.shaic.claim.OMPCloseClaimClaimLevel.SearchBased.search;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class OMPCloseClaimClaimLevelSearchBasedTableDTO extends AbstractTableDTO  implements Serializable{
	
	private Long serialNo;
	private String intimationNo;
	private String policyno;
	private String claimtype;
	private String insuredName;
	private String hospitalname;
	private String eventCode;
	private String classification;
	private String subclassification;
	private String noofrods;
	private Date dateofloss;
	private String claimstatus;
	
	public Long getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(Long serialNo) {
		this.serialNo = serialNo;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getPolicyno() {
		return policyno;
	}
	public void setPolicyno(String policyno) {
		this.policyno = policyno;
	}
	public String getClaimtype() {
		return claimtype;
	}
	public void setClaimtype(String claimtype) {
		this.claimtype = claimtype;
	}
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public String getHospitalname() {
		return hospitalname;
	}
	public void setHospitalname(String hospitalname) {
		this.hospitalname = hospitalname;
	}
	public String getEventCode() {
		return eventCode;
	}
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public String getSubclassification() {
		return subclassification;
	}
	public void setSubclassification(String subclassification) {
		this.subclassification = subclassification;
	}
	public String getNoofrods() {
		return noofrods;
	}
	public void setNoofrods(String noofrods) {
		this.noofrods = noofrods;
	}
	public Date getDateofloss() {
		return dateofloss;
	}
	public void setDateofloss(Date dateofloss) {
		this.dateofloss = dateofloss;
	}
	public String getClaimstatus() {
		return claimstatus;
	}
	public void setClaimstatus(String claimstatus) {
		this.claimstatus = claimstatus;
	}
	
	

}
