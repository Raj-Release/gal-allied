package com.shaic.claim.OMPReopenClaimClaimLevel.SearchBased.search;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;


public class OMPReopenClaimClaimLevelSearchBasedTableDTO extends AbstractTableDTO  implements Serializable{
	
	private String serialNo;
	private String intimationNo;
	private String policyno;
	private String claimtype;
	private String insuredName;
	private String hospitalname;
	private String eventCode;
	private String classification;
	private String subclassification;
	private String dateofloss;
	private String reasonforclose;
	
	
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
	public String getDateofloss() {
		return dateofloss;
	}
	public void setDateofloss(String dateofloss) {
		this.dateofloss = dateofloss;
	}
	public String getReasonforclose() {
		return reasonforclose;
	}
	public void setReasonforclose(String reasonforclose) {
		this.reasonforclose = reasonforclose;
	}
	

}
