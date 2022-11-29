package com.shaic.claim.OMPReopenClaimRODLevel.SearchBased.search;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class OMPReopenClaimRODLevelSearchBasedTableDTO extends AbstractTableDTO  implements Serializable {
	
	private String serialNo;
	private String intimationNo;
	private String claimno;
	private String policyno;
	private String insuredName;
	private String hospitalname;
	private String eventCode;
	private Long classification;
	private String subclassification;
	private Date dateofloss;
	
	
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getClaimno() {
		return claimno;
	}
	public void setClaimno(String claimno) {
		this.claimno = claimno;
	}
	public String getPolicyno() {
		return policyno;
	}
	public void setPolicyno(String policyno) {
		this.policyno = policyno;
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
	public Long getClassification() {
		return classification;
	}
	public void setClassification(Long classification) {
		this.classification = classification;
	}
	public String getSubclassification() {
		return subclassification;
	}
	public void setSubclassification(String subclassification) {
		this.subclassification = subclassification;
	}
	public Date getDateofloss() {
		return dateofloss;
	}
	public void setDateofloss(Date dateofloss) {
		this.dateofloss = dateofloss;
	}
	

}
