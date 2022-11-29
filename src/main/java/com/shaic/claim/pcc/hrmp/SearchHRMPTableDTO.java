package com.shaic.claim.pcc.hrmp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryTableDTO;
import com.shaic.claim.pcc.hrmprocessing.HRMProcessing;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;

/**
 * @author Panneer Selvam .M
 *
 */
public class SearchHRMPTableDTO extends AbstractTableDTO  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String intimationNo;
	private String referenceNo;
	private String dateOfAdmission;
	private String cpuCode;
	private String hospitalCode;
	private String hospitalType;
	private String status;
	private String ageing;
	private Boolean headUser = false;
	private String tabStatus;
	private Long claimKey;
	private String userName;
	private Long cashlessKey;
	private Long key;
	private HRMProcessing hrmObj;
	private String loginId;
	private String hrmUserId;
	private String divissionHeadUserId;
	
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
	public String getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public String getHospitalType() {
		return hospitalType;
	}
	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAgeing() {
		return ageing;
	}
	public void setAgeing(String ageing) {
		this.ageing = ageing;
	}
	public void setHeadUser(boolean b) {
		// TODO Auto-generated method stub
		
	}
	public Boolean getHeadUser() {
		return headUser;
	}
	public void setHeadUser(Boolean headUser) {
		this.headUser = headUser;
	}
	public String getTabStatus() {
		return tabStatus;
	}
	public void setTabStatus(String tabStatus) {
		this.tabStatus = tabStatus;
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
	public HRMProcessing getHrmObj() {
		return hrmObj;
	}
	public void setHrmObj(HRMProcessing hrmObj) {
		this.hrmObj = hrmObj;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getHrmUserId() {
		return hrmUserId;
	}
	public void setHrmUserId(String hrmUserId) {
		this.hrmUserId = hrmUserId;
	}
	public String getDivissionHeadUserId() {
		return divissionHeadUserId;
	}
	public void setDivissionHeadUserId(String divissionHeadUserId) {
		this.divissionHeadUserId = divissionHeadUserId;
	}
	
}