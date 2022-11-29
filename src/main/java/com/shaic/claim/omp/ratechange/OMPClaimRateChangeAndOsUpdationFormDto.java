package com.shaic.claim.omp.ratechange;

import java.io.Serializable;
import java.util.Date;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class OMPClaimRateChangeAndOsUpdationFormDto extends AbstractSearchDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String intimationNo;
	private String eventcode;
	private Date intimationDate;
	private Date lossnDate;
	private String fromdate1;
	private String todate1;
	private String userId;
	private Long rodKey;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	private String password;
	
	public String getEventcode() {
		return eventcode;
	}
	public void setEventcode(String eventcode) {
		this.eventcode = eventcode;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public Date getIntimationDate() {
		return intimationDate;
	}
	public void setIntimationDate(Date intimationDate) {
		this.intimationDate = intimationDate;
	}
	public Date getLossnDate() {
		return lossnDate;
	}
	public void setLossnDate(Date lossnDate) {
	
	}
	public String getFromdate1() {
		return fromdate1;
	}
	public void setFromdate1(String fromdate1) {
		this.fromdate1 = fromdate1;
	}
	public String getTodate1() {
		return todate1;
	}
	public void setTodate1(String todate1) {
		this.todate1 = todate1;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	
	
}
