package com.shaic.claim.OMPProcessOmpClaimApprover.search;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class OMPProcessOmpClaimApproverTableDTO extends AbstractTableDTO  implements Serializable{
	

	private Long key;
	
	private String serialNumber;
	
	private String intimationNo;
	
	private String claimno;
	
	private String policyno;
	
	private String insuredName;
	
	private String classification;
	
	private String subClassification;
//	
	private Date lossdate;
	
	private String eventcode;
	
	private String ailment;
	
	private Long claimKey;
	
	private Long rodKey;
	
	private String userName;
	
	private String rodnumber;
	
	private String eventDesc;
	
	private String lossDetail;
	
	private String nonHospitalizationFlag;
	
	
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
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public String getSubClassification() {
		return subClassification;
	}
	public void setSubClassification(String subClassification) {
		this.subClassification = subClassification;
	}
	public String getEventcode() {
		return eventcode;
	}
	public void setEventcode(String eventcode) {
		this.eventcode = eventcode;
	}
	public String getAilment() {
		return ailment;
	}
	public void setAilment(String ailment) {
		this.ailment = ailment;
	}
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRodnumber() {
		return rodnumber;
	}
	public void setRodnumber(String rodnumber) {
		this.rodnumber = rodnumber;
	}
	public String getEventDesc() {
		return eventDesc;
	}
	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}
	public String getLossDetail() {
		return lossDetail;
	}
	public void setLossDetail(String lossDetail) {
		this.lossDetail = lossDetail;
	}
	public String getNonHospitalizationFlag() {
		return nonHospitalizationFlag;
	}
	public void setNonHospitalizationFlag(String nonHospitalizationFlag) {
		this.nonHospitalizationFlag = nonHospitalizationFlag;
	}
	

}
