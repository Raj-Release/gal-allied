package com.shaic.claim.OMPclaimprocesschecker.search;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class OMPClaimProcessCheckerTableDTO extends AbstractTableDTO  implements Serializable{
	
	private String serialNo;
	private String intimationNo;
	private String claimno;
	private String policyno;
	private String insuredPatientName;
//	private Date lossDate;
	private String eventCode;
	private String ailment;

	public String getSerialNo(){
		return serialNo;
	}
	public void setSerialNo(String serialNo){
		this.serialNo = serialNo;
	}
	public String getIntimationNo(){
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo){
		this.intimationNo = intimationNo;
	}
	public String getclaimno(){
		return claimno;
	}
	public void setclaimno(String claimno){
		this.claimno = claimno;
	}
	public String getPolicyno(){
		return policyno;
	}
	public void setPolicyno(String policyno){
		this.policyno = policyno;
	}
	public String getInsuredPatientName(){
		return insuredPatientName;
	}
	public void setInsuredPatientName(String insuredPatientName){
		this.insuredPatientName = insuredPatientName;
	}
//	public Date getLossDate(){
//		return lossDate;
//	}
//	public void setLossDate(Date lossDate){
//		this.lossDate = lossDate;
//	}
	public String getEventCode(){
		return eventCode;
	}
	public void setEventCode(String eventCode){
		this.eventCode = eventCode;
	}
	public String getAilment(){
		return ailment;
	}
	public void setAilment(String ailment){
		this.ailment = ailment;
	}
}
