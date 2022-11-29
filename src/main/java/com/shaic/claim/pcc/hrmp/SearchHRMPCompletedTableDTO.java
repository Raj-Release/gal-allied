package com.shaic.claim.pcc.hrmp;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class SearchHRMPCompletedTableDTO extends AbstractTableDTO  implements Serializable{
	
	/**
	 * "serialNumber", "intimationNo","referenceNo", "dateOfAdmission","status","hrmUserId",
		"hrmDate", "hrmRemarks", "divissionHeadUserId", "divissionHeadDate", "divissionHeadRemarks"}; 
	 */
	private static final long serialVersionUID = 1L;
	
	private String intimationNo;
	private String referenceNo;
	private String dateOfAdmission;
	private String hrmUserId;
	private String hrmDate;
	private String hrmRemarks;
	private String divissionHeadUserId;
	private String divissionHeadDate;
	private String divissionHeadRemarks;
	private String tabStatus;
	private Long claimKey;
	private String userName;
	private Long cashlessKey;
	private Long key;
	private String status;
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	public String getDateOfAdmission() {
		return dateOfAdmission;
	}
	public void setDateOfAdmission(String dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}
	public String getTabStatus() {
		return tabStatus;
	}
	public void setTabStatus(String tabStatus) {
		this.tabStatus = tabStatus;
	}
	public String getHrmUserId() {
		return hrmUserId;
	}
	public void setHrmUserId(String hrmUserId) {
		this.hrmUserId = hrmUserId;
	}
	public String getHrmRemarks() {
		return hrmRemarks;
	}
	public void setHrmRemarks(String hrmRemarks) {
		this.hrmRemarks = hrmRemarks;
	}
	public String getDivissionHeadUserId() {
		return divissionHeadUserId;
	}
	public void setDivissionHeadUserId(String divissionHeadUserId) {
		this.divissionHeadUserId = divissionHeadUserId;
	}
	public String getDivissionHeadDate() {
		return divissionHeadDate;
	}
	public void setDivissionHeadDate(String divissionHeadDate) {
		this.divissionHeadDate = divissionHeadDate;
	}
	public String getDivissionHeadRemarks() {
		return divissionHeadRemarks;
	}
	public void setDivissionHeadRemarks(String divissionHeadRemarks) {
		this.divissionHeadRemarks = divissionHeadRemarks;
	}
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getCashlessKey() {
		return cashlessKey;
	}
	public void setCashlessKey(Long cashlessKey) {
		this.cashlessKey = cashlessKey;
	}
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getHrmDate() {
		return hrmDate;
	}
	public void setHrmDate(String hrmDate) {
		this.hrmDate = hrmDate;
	}
	
}