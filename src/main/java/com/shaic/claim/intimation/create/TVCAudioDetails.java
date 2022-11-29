package com.shaic.claim.intimation.create;

import java.io.Serializable;

public class TVCAudioDetails implements Serializable{
	
	private static final long serialVersionUID = -744184118752494434L;
	
	private String calldate;
	private String systemid;
	private String phonenumber;
	private String polnumber;
	private String extnnumber;
	private String audio_url;
	private String polsysid;
	private String custcode;
	private String resid;
	private String userid;
	private String callerid;
	
	public String getCalldate() {
		return calldate;
	}
	public void setCalldate(String calldate) {
		this.calldate = calldate;
	}
	public String getSystemid() {
		return systemid;
	}
	public void setSystemid(String systemid) {
		this.systemid = systemid;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public String getPolnumber() {
		return polnumber;
	}
	public void setPolnumber(String polnumber) {
		this.polnumber = polnumber;
	}
	public String getExtnnumber() {
		return extnnumber;
	}
	public void setExtnnumber(String extnnumber) {
		this.extnnumber = extnnumber;
	}
	public String getAudio_url() {
		return audio_url;
	}
	public void setAudio_url(String audio_url) {
		this.audio_url = audio_url;
	}
	public String getPolsysid() {
		return polsysid;
	}
	public void setPolsysid(String polsysid) {
		this.polsysid = polsysid;
	}
	public String getCustcode() {
		return custcode;
	}
	public void setCustcode(String custcode) {
		this.custcode = custcode;
	}
	public String getResid() {
		return resid;
	}
	public void setResid(String resid) {
		this.resid = resid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getCallerid() {
		return callerid;
	}
	public void setCallerid(String callerid) {
		this.callerid = callerid;
	}
}
