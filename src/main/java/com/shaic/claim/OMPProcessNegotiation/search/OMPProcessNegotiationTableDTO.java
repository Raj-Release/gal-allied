package com.shaic.claim.OMPProcessNegotiation.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;

public class OMPProcessNegotiationTableDTO extends AbstractTableDTO  implements Serializable{
	
	
	private Long serialNo;
	
	private Long key;
	
	private String intimationNo;
	
	private String claimno;
	
	private String policyno;
	
	private String insuredName;
	
	private SelectValue eventcode;
	
	private String ailment;
	
	private Long claimKey;
	
	private Long rodKey;
	
	private String userName;
	
	private String rodnumber;
	
	public String getClaimno(){
		return claimno;
	}
	public void setClaimno(String claimno){
		this.claimno = claimno;
	}

	public Long getSerialNo(){
		return serialNo;
	}
	public void setSerialNo(Long serialNo){
		this.serialNo = serialNo;
	}
	public String getIntimationNo(){
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo){
		this.intimationNo = intimationNo;
	}
	public String getPolicyno(){
		return policyno;
	}
	public void setPolicyno(String policyno){
		this.policyno = policyno;
	}
	public String getInsuredName(){
		return insuredName;
	}
	public void setInsuredName(String insuredName){
		this.insuredName = insuredName;
	}
	public SelectValue getEventcode(){
		return eventcode;
	}
	public void setEventcode(SelectValue eventcode){
		this.eventcode = eventcode;
	}
	public String getAilment(){
		return ailment;
	}
	public void setAilment(String ailment){
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
	
}
