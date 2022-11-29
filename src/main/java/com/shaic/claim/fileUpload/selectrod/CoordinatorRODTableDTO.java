package com.shaic.claim.fileUpload.selectrod;

import java.io.Serializable;

import com.shaic.domain.Claim;

public class CoordinatorRODTableDTO implements Serializable{
	
	private static final long serialVersionUID = -2094919702035499277L;
	
	private int sno;
	private String rodNumber;
	private String billClasification;
	private Long rodClaimedAmount;
	private Long rodApprovedAmount;
	private String rodStatus;
	private Long rodKey;
	private Claim claim;
	
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	public String getRodNumber() {
		return rodNumber;
	}
	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}
	public String getBillClasification() {
		return billClasification;
	}
	public void setBillClasification(String billClasification) {
		this.billClasification = billClasification;
	}
	public Long getRodClaimedAmount() {
		return rodClaimedAmount;
	}
	public void setRodClaimedAmount(Long rodClaimedAmount) {
		this.rodClaimedAmount = rodClaimedAmount;
	}
	public Long getRodApprovedAmount() {
		return rodApprovedAmount;
	}
	public void setRodApprovedAmount(Long rodApprovedAmount) {
		this.rodApprovedAmount = rodApprovedAmount;
	}
	public String getRodStatus() {
		return rodStatus;
	}
	public void setRodStatus(String rodStatus) {
		this.rodStatus = rodStatus;
	}
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	public Claim getClaim() {
		return claim;
	}
	public void setClaim(Claim claim) {
		this.claim = claim;
	}	
	
}
